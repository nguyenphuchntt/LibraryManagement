package utils;

import entities.Book;
import entities.Comment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class BookUtils {

    public static List<Object[]> getBookListForRecommend() {
        List<Object[]> results = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String hql = "SELECT s.title, s.author, s.category, s.year, s.description, s.averageRate, s.id, s.thumbnailLink " +
                    "FROM Transaction b " +
                    "JOIN b.book s " +
                    "GROUP BY s.isbn " +
                    "ORDER BY COUNT(s.isbn) DESC";

            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setMaxResults(10);
            session.getTransaction().commit();
            results = query.getResultList();
            return results;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static void updateBookAmountAfterBorrowed(List<String> booksID, boolean type) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            for (String bookID : booksID) {
                Book persistentBook = session.get(Book.class, bookID);

                if (persistentBook != null) {
                    if (type) {
                        persistentBook.setAmount(persistentBook.getQuantity() + 1);
                        System.out.println("return");
                    } else {
                        persistentBook.setAmount(persistentBook.getQuantity() - 1);
                        System.out.println("borrow");
                    }
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public static List<Book> getAllBooks() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Book> books = null;

        try {
            session.beginTransaction();
            books = session.createQuery("from Book", Book.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return books;
    }

    public static List<Book> searchBook(String isbn, String title, String author, String category, String year, int offset, int limit) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            StringBuilder stringBuilder = new StringBuilder("FROM Book b WHERE 1=1");

            if (isbn != null) {
                stringBuilder.append(" AND b.isbn LIKE :isbn");
                System.out.println("isbn" + isbn);
            }
            if (title != null) {
                stringBuilder.append(" AND b.title LIKE :title");
                System.out.println(title);
            }
            if (author != null) {
                stringBuilder.append(" AND b.author LIKE :author");
            }
            if (category != null) {
                stringBuilder.append(" AND b.category LIKE :category");
            }
            if (year != null) {
                stringBuilder.append(" AND b.year = :year");
            }

            Query<Book> query = session.createQuery(stringBuilder.toString(), Book.class);

            if (isbn != null) {
                query.setParameter("isbn", "%" + isbn + "%");
            }
            if (title != null) {
                query.setParameter("title", "%" + title + "%");
            }
            if (author != null) {
                query.setParameter("author", "%" + author + "%");
            }
            if (category != null) {
                query.setParameter("category", "%" + category + "%");
            }
            if (year != null) {
                query.setParameter("year", Integer.parseInt(year));
            }
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            List<Book> books = query.list();

            session.getTransaction().commit();
            return books;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static Book getBookByISBN(String isbn) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            Book book = session.get(Book.class, isbn);

            session.getTransaction().commit();
            return book;

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static boolean hadBook(String isbn) {
        return getBookByISBN(isbn) != null;
    }

    public static void alterBook(Book book) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            Book bookToUpdate = session.get(Book.class, book.getIsbn());

            bookToUpdate.setTitle(book.getTitle());
            bookToUpdate.setAuthor(book.getAuthor());
            bookToUpdate.setCategory(book.getCategory());
            bookToUpdate.setYear(book.getYear());
            bookToUpdate.setAmount(book.getQuantity());
            bookToUpdate.setDescription(book.getDescription());
            bookToUpdate.setPublisher(book.getPublisher());

            session.update(bookToUpdate);
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static boolean hadComment(String username, String bookIsbn) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String hql = "FROM Comment t WHERE t.username =:username AND t.book.id = :bookIsbn";
            Query<Comment> query = session.createQuery(hql, Comment.class);
            query.setParameter("username", username);
            query.setParameter("bookIsbn", bookIsbn);

            List<Comment> transactions = query.list();
            session.getTransaction().commit();

            return !transactions.isEmpty();

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static int getTotalRowBook() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Book b", Long.class);
            return query.uniqueResult().intValue();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public static Comment getBestCommentOfBook(String isbn) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String hql = "FROM Comment t WHERE t.book.isbn = :isbn ORDER BY t.rate DESC";
            Query<Comment> query = session.createQuery(hql, Comment.class);
            query.setParameter("isbn", isbn);
            query.setMaxResults(1);

            session.getTransaction().commit();

            return query.uniqueResultOptional().orElse(null);

        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}

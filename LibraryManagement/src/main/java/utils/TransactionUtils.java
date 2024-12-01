package utils;

import entities.LibraryManagement;
import entities.Transaction;
import DTO.TransactionDTO;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionUtils {

    public static void addBorrowTransactions(List<Transaction> transactions) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            for (Transaction transaction : transactions) {
                session.save(transaction);
            }

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

    public static void addReturnTransactions(List<TransactionDTO> transactionDTOs) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (TransactionDTO transactionDTO: transactionDTOs) {
                String hql = "UPDATE Transaction t SET t.return_time = :returnTime " +
                        "WHERE t.user.id = :username AND t.book.id = :bookId AND t.borrow_time = :time";

                session.createQuery(hql)
                        .setParameter("returnTime", now)
                        .setParameter("username", LibraryManagement.getInstance().getCurrentAccount())
                        .setParameter("bookId", transactionDTO.getBookId())
                        .setParameter("time", new Timestamp(sdf.parse(transactionDTO.getBorrowDate()).getTime()))
                        .executeUpdate();
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Error while adding transactions to the database!");
        } finally {
            session.close();
        }
    }

    public static List<Transaction> getFilteredBorrowTransactions(String title, String author, String category, String isbn, String username, int offset, int limit) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            StringBuilder stringBuilder = new StringBuilder("FROM Transaction b WHERE 1=1");

            stringBuilder.append(" AND b.user.username =:username");

            if (isbn != null) {
                stringBuilder.append(" AND b.book.isbn LIKE :isbn");
                System.out.println("isbn" + isbn);
            }
            if (title != null) {
                stringBuilder.append(" AND b.book.title LIKE :title");
                System.out.println(title);
            }
            if (author != null) {
                stringBuilder.append(" AND b.book.author LIKE :author");
            }
            if (category != null) {
                stringBuilder.append(" AND b.book.category LIKE :category");
            }

            stringBuilder.append(" ORDER BY b.return_time ASC");

            Query<Transaction> query = session.createQuery(stringBuilder.toString(), Transaction.class);

            query.setParameter("username", username);

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
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            List<Transaction> transactions = query.list();

            session.getTransaction().commit();
            return transactions;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static List<Transaction> getOverdueTransactions(int offset, int limit) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            String hql = "FROM Transaction t WHERE "
                    + "t.return_time IS NULL AND "
                    + "datediff(now(), t.borrow_time) > 2";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            List<Transaction> transactions = query.list();
            session.getTransaction().commit();
            return transactions;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static int getTotalRowBorrowedBook() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Transaction b"
                     + " WHERE b.user.username =: username", Long.class);
            query.setParameter("username", LibraryManagement.getInstance().getCurrentAccount());
            session.getTransaction().commit();
            return query.uniqueResult().intValue();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static int getTotalOverDueTransactions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Transaction b WHERE b.return_time IS NULL AND " +
                    "DATEDIFF(NOW(), b.borrow_time) > 21", Long.class);
            return query.uniqueResult().intValue();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }

}

package Utils;

import Entity.Book;
import org.hibernate.Session;

public class BookUtils {

    public static void addBook(Book book) {
        if (book == null) {
            System.out.println("Book is null!");
            return;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            session.save(book);

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

}

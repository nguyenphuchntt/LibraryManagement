//package database;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//
//public class HibernateUtil {
//    private static final SessionFactory sessionFactory;
//
//    static {
//        try {
//            sessionFactory = new Configuration().configure().addAnnotatedClass(Book.class).buildSessionFactory();
//        } catch (Exception e) {
//            throw new ExceptionInInitializerError(e);
//        }
//    }
//
//    public static SessionFactory getSessionFactory() {
//        return sessionFactory;
//    }
//
//    public static void shutdown() {
//        getSessionFactory().close();
//    }
//}

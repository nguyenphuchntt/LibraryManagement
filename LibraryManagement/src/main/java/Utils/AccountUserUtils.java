package Utils;

import Entity.Account;
import Entity.LibraryManagement;
import Entity.Person;
import database.DatabaseController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.sql.*;

public class AccountUserUtils {

    public static boolean isExistedUsername(String username) {
        ResultSet user = null;
        boolean existed = false;
        try {
            Connection connection = DatabaseController.getConnection();
            String sqlQuery = "SELECT username FROM account WHERE username = ?";
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, username);
            user = preparedStatement.executeQuery();

            existed = user.next();
        } catch (SQLException e) {
            System.out.println("SQL Exception: Cannot get result set!");
        }
        return existed;
    }

    public static boolean isExistedAccount(String username, String password) {
        boolean existed = false;
        String sqlQuery = "SELECT * FROM account WHERE username = ? AND password = ?";
        try {
            Statement useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = DatabaseController.getConnection().prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            existed = resultSet.next();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("Login: " + username + " failed by exception: " + e);
        }
        return existed;
    }

    public static Person getUserInfo(String username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        Person user = session.get(Person.class, username);
        session.close();
        return user;
    }

    public static Account getAccountInfo(String username) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();
        Account account = session.get(Account.class, username);
        session.close();
        return account;
    }

    public static void changePassword(String username, String password) {
        Connection connection = DatabaseController.getConnection();

        String sqlQuery = "UPDATE account SET password = ? WHERE username = ?";
        try {
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException -> changePassword function of Account controller: " + e.getMessage());
        }
        LibraryManagement.getInstance().setCurrentPassword(password);
    }

    public static void addUser(String username) {
        Person user = null;
        SessionFactory userSessionFactory = HibernateUtil.getSessionFactory();

        Session userSession = userSessionFactory.getCurrentSession();

        try {
            userSession.beginTransaction();

            user = new Person.Builder<>()
                    .username(username)
                    .name("null")
                    .role(true)
                    .build();

            userSession.save(user);

            userSession.getTransaction().commit();

            System.out.println("User saved!");
        } catch (Exception e) {
            System.out.println("Error saving user");
            e.printStackTrace();
        } finally {
            userSession.close();
        }
    }

    public static void addAccount(String username, String password) {
        Account account = null;
        SessionFactory accountSessionFactory = HibernateUtil.getSessionFactory();

        Session accoutSession = accountSessionFactory.getCurrentSession();

        try {
            accoutSession.beginTransaction();

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            account = new Account.Builder()
                    .username(username)
                    .password(password)
                    .joined_date(currentTimestamp)
                    .typeAccount(true)
                    .build();

            accoutSession.save(account);

            accoutSession.getTransaction().commit();

            System.out.println("Account saved!");
        } catch (Exception e) {
            System.out.println("Error saving account");
            e.printStackTrace();
        } finally {
            accoutSession.close();
        }
    }

    public static Person getCurrentUser() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Person user = null;
        try {
            session.beginTransaction();
            String username = LibraryManagement.getInstance().getCurrentAccount();

            String hql = "FROM Person WHERE username = :username";
            Query<Person> query = session.createQuery(hql, Person.class);
            query.setParameter("username", username);

            user = query.uniqueResult();

            session.getTransaction().commit();

            return user;
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

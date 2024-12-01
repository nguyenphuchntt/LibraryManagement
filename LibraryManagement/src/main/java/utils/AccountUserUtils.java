package utils;

import entities.Account;
import entities.LibraryManagement;
import entities.Person;
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

    public static Account isExistedAccount(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Account user = null;
        try {
            session.beginTransaction();

            String hql = "FROM Account WHERE username = :username AND password = :password";
            Query<Account> query = session.createQuery(hql, Account.class);
            query.setParameter("username", username);
            query.setParameter("password", password);

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
                    .typeAccount(false)
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
        return getUserInfo(LibraryManagement.getInstance().getCurrentAccount());
    }

    public static void updateUserInfo(String name, int age, String department) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            String username = LibraryManagement.getInstance().getCurrentAccount();
            Person user = session.get(Person.class, username);
            user.setName(name);
            if (age != 0) {
                System.out.println(age);
                user.setYearOfBirth(age);
            }
            user.setDepartment(department);

            session.update(user);

            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error saving account");
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

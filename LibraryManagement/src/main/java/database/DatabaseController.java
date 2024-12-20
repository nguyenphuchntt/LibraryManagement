package database;

import entities.*;
import utils.DotenvLoader;
import utils.FormatUtils;
import utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseController {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = DotenvLoader.getDotenv().get("MYSQL_USERNAME");
    private static final String MYSQL_PASSWORD = DotenvLoader.getDotenv().get("MYSQL_PASSWORD");
    private static final String accountCSVPath = Paths.get("src", "main", "resources", "csv", "account.csv").toString();
    private static final String announcementCSVPath = Paths.get("src", "main", "resources", "csv", "announcement.csv").toString();
    private static final String bookCSVPath = Paths.get("src", "main", "resources", "csv", "book.csv").toString();
    private static final String book_commentCSVPath = Paths.get("src", "main", "resources", "csv", "book_comment.csv").toString();
    private static final String transactionCSVPath = Paths.get("src", "main", "resources", "csv", "transaction.csv").toString();
    private static final String userCSVPath = Paths.get("src", "main", "resources", "csv", "user.csv").toString();
    private static final String messageCSVPath = Paths.get("src", "main", "resources", "csv", "message.csv").toString();
    private static final String SQL_FILE = Paths.get("src", "main", "java", "database", "createTableQuery.sql").toString();

    private static Connection connection;

    private static CountDownLatch exportDBLatch = new CountDownLatch(7);

    private static ExecutorService executor = Executors.newFixedThreadPool(7);

    public synchronized static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, MYSQL_PASSWORD);
            } catch (SQLException e) {
                System.out.println("Database connection failed");
                throw new RuntimeException(e);
            }
            System.out.println("Connected to database");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                exportDataToSCV();
                exportDBLatch.await();
                connection.close();
                connection = null;
                System.out.println("Connection closed");
            } catch (SQLException e) {
                System.out.println("Failed to close connection!");
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void ExportResultSetToCSV(ResultSet resultSets, String pathToSCV) throws SQLException, IOException {
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pathToSCV));

            ResultSetMetaData resultSetMetaData = resultSets.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                writer.write(columnName);
                if (i < columnCount) {
                    writer.write(",");
                }
            }

            writer.newLine();

            while (resultSets.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String value;

                    if (resultSetMetaData.getColumnTypeName(i).equalsIgnoreCase("TIMESTAMP")) {
                        Timestamp timestamp = resultSets.getTimestamp(i);
                        value = (timestamp != null) ? FormatUtils.getDateTimeFormat().format(timestamp) : "null";
                    } else if (resultSetMetaData.getColumnTypeName(i).equalsIgnoreCase("MEDIUMBLOB")) {
                        byte[] blobData = resultSets.getBytes(i);
                        value = (blobData != null) ? Base64.getEncoder().encodeToString(blobData) : "null";
                    } else if (resultSetMetaData.getColumnTypeName(i).equalsIgnoreCase("YEAR")) {
                        value = String.valueOf(resultSets.getInt(i));
                    } else if (resultSetMetaData.getColumnTypeName(i).equalsIgnoreCase("DATE")) {
                        Date date = resultSets.getDate(i);
                        value = (date != null) ? FormatUtils.getDateFormat().format(date) : "null";
                    } else {
                        value = resultSets.getString(i);
                    }

                    writer.write(value != null ? value : "null");

                    if (value != null && (value.contains(",") || value.contains("\"") || value.contains("\n"))) {
                        value = "\"" + value.replace("\"", "\"\"") + "\"";
                    }

                    if (i < columnCount) {
                        writer.write("##@#@");
                    }
                }
                writer.newLine();
            }
            System.out.println("Export CSV file executed successfully.");
        } finally {
            resultSets.close();
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static void importUserCSVToDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;
        String insertSQL = "INSERT IGNORE INTO `user` (username, name, age, gender, department) VALUES (?, ?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));

            reader.readLine();

            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setString(1, values[0]);
                if (!values[1].equalsIgnoreCase("null")) {
                    statement.setString(2, values[1]);
                } else {
                    statement.setNull(2, Types.VARCHAR);
                }
                statement.setInt(3, values[2].equalsIgnoreCase("null") ? Types.INTEGER : Integer.parseInt(values[2])); // yearOfBirth

                if (values[3].equalsIgnoreCase("null")) { // department
                    statement.setString(5, values[3]);
                } else {
                    statement.setNull(5, Types.VARCHAR);
                }

                if (values[4].equalsIgnoreCase("null")) {
                    statement.setNull(4, Types.VARCHAR);
                } else {
                    statement.setString(4, values[4]);
                }
                statement.addBatch();

            }

            statement.executeBatch();
            System.out.println("Import CSV user to DB executed successfully.");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void importAccountCSVtoBD(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;
        String insertSQL = "INSERT IGNORE INTO `account` (username , password, account_role, joined_date, avatar) VALUES (?, ?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));

            reader.readLine();

            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setString(1, values[0]); // username
                statement.setString(2, values[1]); // password
                statement.setInt(3, Integer.parseInt(values[2])); // account_type
                Timestamp timestamp = null;
                if (values[3] != null && !values[3].equalsIgnoreCase("null")) {
                    try {
                        timestamp = new Timestamp(FormatUtils.getDateTimeFormat().parse(values[3]).getTime());
                    } catch (ParseException e) {
                        System.err.println("Error parsing timestamp: " + values[3]);
                    }
                }
                statement.setTimestamp(4, timestamp); // joined_date

                byte[] blobData = null;
                if (values[4] != null && !values[4].equalsIgnoreCase("null")) {
                    blobData = Base64.getDecoder().decode(values[4]);
                }
                statement.setBytes(5, blobData); // avatar

                statement.addBatch();
            }

            statement.executeBatch();
            System.out.println("Import CSV account to DB executed successfully.");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void importBookCSVtoDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;
        String insertSQL = "INSERT IGNORE INTO `book` (id, title, author, publisher, year, quantity, description, averageRating, category, thumbnailLink) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));

            reader.readLine();
            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setString(1, values[0]); // book_id
                statement.setString(2, values[1]); // book_title
                if (values[2] != null && !values[2].equalsIgnoreCase("null")) {
                    statement.setString(3, values[2]); // author
                } else {
                    statement.setNull(3, Types.VARCHAR);
                }
                if (values[3] != null && !values[3].equalsIgnoreCase("null")) {
                    statement.setString(4, values[3]); // publisher
                } else {
                    statement.setNull(4, Types.VARCHAR);
                }
                try {
                    statement.setInt(5, Integer.parseInt(values[4])); // year
                } catch (NumberFormatException e) {
                    statement.setNull(5, Types.INTEGER);
                }
                statement.setInt(6, Integer.parseInt(values[5])); // quantity
                if (values[6] != null && !values[6].equalsIgnoreCase("null")) {
                    statement.setString(7, values[6]); // description
                } else {
                    statement.setNull(7, Types.VARCHAR);
                }
                try {
                    statement.setDouble(8, Double.parseDouble(values[7])); // averageRating
                } catch (NumberFormatException e) {
                    statement.setDouble(8, 0.0); // Default to 0.0
                }
                statement.setString(9, values[8]); // category
                if (values[9] != null && !values[9].equalsIgnoreCase("null")) {
                    statement.setString(10, values[9]);
                } else {
                    statement.setNull(10, Types.VARCHAR);
                } // thumbnail link

                statement.addBatch();
            }

            statement.executeBatch();
            System.out.println("Import CSV book to DB executed successfully.");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void importMessageCSVtoDB(String pathToCSV)  throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;

        String insertSQL = "INSERT IGNORE INTO `messages` (id, sender, receiver , content, timestamp) VALUES (?, ?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));
            reader.readLine();

            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setInt(1, Integer.parseInt(values[0]));
                statement.setString(2, values[1]);
                statement.setString(3, values[2]);
                statement.setString(4, values[3]);
                try {
                    statement.setTimestamp(5, new Timestamp(FormatUtils.getDateTimeFormat().parse(values[4]).getTime()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                statement.addBatch();
            }

            statement.executeBatch();
            System.out.println("Import CSV message to DB executed successfully.");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void importTransactionCSVtoDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;

        String insertSQL = "INSERT IGNORE INTO `transaction` (transaction_id, book_id, username , borrow_time, return_time) VALUES (?, ?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));

            reader.readLine();

            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setInt(1, Integer.parseInt(values[0])); // transaction_id
                statement.setString(2, values[1]); // book_id
                statement.setString(3, values[2]); // username

                Timestamp borrow_Timestamp = null; // time
                Timestamp return_Timestamp = null;
                try {
                    borrow_Timestamp = new Timestamp(FormatUtils.getDateTimeFormat().parse(values[3]).getTime());
                    statement.setTimestamp(4, borrow_Timestamp);

                    if (values[4] != null && !values[4].equalsIgnoreCase("null")) {
                        return_Timestamp = new Timestamp(FormatUtils.getDateTimeFormat().parse(values[4]).getTime());
                        statement.setTimestamp(5, return_Timestamp);
                    } else {
                        statement.setNull(5, java.sql.Types.TIMESTAMP);
                    }
                } catch (ParseException e) {
                    System.out.println("SQL query to add transaction failed!");
                    throw new RuntimeException(e);
                }
                statement.addBatch();

            }

            statement.executeBatch();
            System.out.println("Import CSV transaction to DB executed successfully.");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    private static void importCommentCSVtoDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;

        String insertSQL = "INSERT IGNORE INTO `book_comment` (comment_id, book_id, username , book_comment, rate) VALUES (?, ?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));

            reader.readLine();

            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setInt(1, Integer.parseInt(values[0]));
                statement.setString(2, values[1]);
                statement.setString(3, values[2]);
                statement.setString(4, values[3]);
                statement.setDouble(5, Double.parseDouble(values[4]));

                statement.addBatch();

            }

            statement.executeBatch();
            System.out.println("Import CSV comment to DB executed successfully.");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }



    private static void importAnnouncementCSVtoDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;

        String insertSQL = "INSERT IGNORE INTO `announcement` (announcement_id, content, start_date , end_date) VALUES (?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));

            reader.readLine();

            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setInt(1, Integer.parseInt(values[0]));
                statement.setString(2, values[1]);
                try {
                    statement.setDate(3, new Date(FormatUtils.getDateFormat().parse(values[2]).getTime()));
                    statement.setDate(4, new Date(FormatUtils.getDateFormat().parse(values[3]).getTime()));
                } catch (ParseException e) {
                    System.out.println("Can not parse date!");
                    throw new RuntimeException(e);
                }

                statement.addBatch();
            }

            statement.executeBatch();
            System.out.println("Import CSV announcement to DB executed successfully.");
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }

    public static void importDataFromCSV() {
        new Thread(() -> {
            try {
                importAccountCSVtoBD(accountCSVPath);
            } catch (SQLException | IOException e) {
                System.err.println("Error importing accounts: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                importUserCSVToDB(userCSVPath);
            } catch (SQLException | IOException e) {
                System.err.println("Error importing users: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                importBookCSVtoDB(bookCSVPath);
            } catch (SQLException | IOException e) {
                System.err.println("Error importing books: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                importTransactionCSVtoDB(transactionCSVPath);
            } catch (SQLException | IOException e) {
                System.err.println("Error importing transactions: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                importCommentCSVtoDB(book_commentCSVPath);
            } catch (SQLException | IOException e) {
                System.err.println("Error importing comments: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                importAnnouncementCSVtoDB(announcementCSVPath);
            } catch (SQLException | IOException e) {
                System.err.println("Error importing announcements: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                importMessageCSVtoDB(messageCSVPath);
            } catch (SQLException | IOException e) {
                System.err.println("Error importing messages: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();
    }


    public static void exportDataToSCV() throws SQLException {
        try {
            Statement useDatabaseStatement = null;
            useDatabaseStatement = DatabaseController.getConnection().createStatement();
            useDatabaseStatement.execute("USE library");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Statement accountQuery = DatabaseController.getConnection().createStatement();
        Statement userQuery = DatabaseController.getConnection().createStatement();
        Statement bookQuery = DatabaseController.getConnection().createStatement();
        Statement transactionQuery = DatabaseController.getConnection().createStatement();
        Statement commentQuery = DatabaseController.getConnection().createStatement();
        Statement announcementQuery = DatabaseController.getConnection().createStatement();
        Statement messageQuery = DatabaseController.getConnection().createStatement();

        ResultSet resultSetAccount = accountQuery.executeQuery("SELECT * from account");
        ResultSet resultSetUser = userQuery.executeQuery("SELECT * from user");
        ResultSet resultSetBook = bookQuery.executeQuery("SELECT * from book");
        ResultSet resultSetTransaction = transactionQuery.executeQuery("SELECT * from transaction");
        ResultSet resultSetComment = commentQuery.executeQuery("SELECT * from book_comment");
        ResultSet resultSetAnnouncement = announcementQuery.executeQuery("SELECT * from announcement");
        ResultSet resultSetMessage = messageQuery.executeQuery("SELECT * from messages");

        executor.submit(() -> {
            try {
                ExportResultSetToCSV(resultSetAccount, accountCSVPath);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                exportDBLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                ExportResultSetToCSV(resultSetUser, userCSVPath);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                exportDBLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                ExportResultSetToCSV(resultSetBook, bookCSVPath);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                exportDBLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                ExportResultSetToCSV(resultSetTransaction, transactionCSVPath);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                exportDBLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                ExportResultSetToCSV(resultSetComment, book_commentCSVPath);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                exportDBLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                ExportResultSetToCSV(resultSetAnnouncement, announcementCSVPath);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                exportDBLatch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                ExportResultSetToCSV(resultSetMessage, messageCSVPath);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            } finally {
                exportDBLatch.countDown();
            }
        });

        executor.shutdown();
    }

    public static List<Announcement> getAllAnnouncements() {
        LocalDate currentDate = LocalDate.now();
        List<Announcement> announcements = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String hql = "FROM Announcement a WHERE a.start_date <= :currentDate AND a.end_date >= :currentDate";

            Query<Announcement> query = session.createQuery(hql, Announcement.class);
            query.setParameter("currentDate", currentDate);

            announcements = query.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }

        return announcements;
    }

    public static <T> void saveEntity(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            session.save(entity);

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

    public static List<User> getUsersWhoSentMessagesToCurrentUser() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            String hql = "SELECT DISTINCT m.sender FROM Message m WHERE m.receiver.username = :currentUserId";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("currentUserId", LibraryManagement.getInstance().getCurrentAccount().getUsername());
            List<User> users = query.getResultList();
            session.getTransaction().commit();
            return users;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public static List<Message> getMessagesBetweenUsers(String receiver) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        String hql = "FROM Message m WHERE (m.sender.username = :userId AND m.receiver.username = :currentUserId) " +
                "OR (m.sender.username = :currentUserId AND m.receiver.username = :userId) "
                + "ORDER BY m.timestamp ASC";
        Query<Message> query = session.createQuery(hql, Message.class);
        query.setParameter("userId", receiver);
        query.setParameter("currentUserId", LibraryManagement.getInstance().getCurrentAccount().getUsername());
        List<Message> messages = query.getResultList();

        session.getTransaction().commit();
        session.close();
        return messages;
    }
}
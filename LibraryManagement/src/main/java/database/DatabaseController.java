package database;

import Entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;

public class DatabaseController {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "18092005";
    private static final String accountCSVPath = Paths.get("src", "main", "resources", "csv", "account.csv").toString();
    private static final String announcementCSVPath = Paths.get("src", "main", "resources", "csv", "announcement.csv").toString();
    private static final String bookCSVPath = Paths.get("src", "main", "resources", "csv", "book.csv").toString();
    private static final String book_commentCSVPath = Paths.get("src", "main", "resources", "csv", "book_comment.csv").toString();
    private static final String transactionCSVPath = Paths.get("src", "main", "resources", "csv", "transaction.csv").toString();
    private static final String userCSVPath = Paths.get("src", "main", "resources", "csv", "user.csv").toString();
    private static final String SQL_FILE = Paths.get("src", "main", "java", "database", "createTableQuery.sql").toString();

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to database");
                createTables(SQL_FILE);
            } catch (SQLException e) {
                System.out.println("Failed to create connection!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                exportDataToSCV();
                connection.close();
                connection = null;
                System.out.println("Connection closed");
            } catch (SQLException e) {
                System.out.println("Failed to close connection!");
                e.printStackTrace();
            }
        }
    }

    public static void createTables(String sqlFilePath) {
        try {
            Statement statement = connection.createStatement();

            String sqlCommands = new String(Files.readAllBytes(Paths.get(sqlFilePath)));

            String[] sqlStatements = sqlCommands.split(";");

            for (String sql : sqlStatements) {
                if (!sql.trim().isEmpty()) {
                    statement.execute(sql.trim());
                }
            }

            System.out.println("Create table SQL file executed successfully.");
        } catch (SQLException | IOException e) {
            System.out.println("SQL file execution failed!");
            e.printStackTrace();
        }
    }

    public static void ExportResultSetToCSV(ResultSet resultSets, String pathToSCV) throws SQLException, IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pathToSCV));

            SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format for TIMESTAMP

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
                        value = (timestamp != null) ? timestampFormat.format(timestamp) : "null";
                    } else if (resultSetMetaData.getColumnTypeName(i).equalsIgnoreCase("MEDIUMBLOB")) {
                        byte[] blobData = resultSets.getBytes(i);
                        value = (blobData != null) ? Base64.getEncoder().encodeToString(blobData) : "null";
                    } else if (resultSetMetaData.getColumnTypeName(i).equalsIgnoreCase("YEAR")) {
                        value = String.valueOf(resultSets.getInt(i));
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

    public static void importUserCSVToDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();

        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;
        String insertSQL = "INSERT IGNORE INTO `user` (username, name, yearOfBirth, gender, department) VALUES (?, ?, ?, ?, ?)";

        try {
            reader = new BufferedReader(new FileReader(pathToCSV));

            reader.readLine();

            statement = connection.prepareStatement(insertSQL);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split("##@#@");

                statement.setString(1, values[0]);
//                statement.setString(2, !values[1].equalsIgnoreCase("null") ? values[1] : java.sql.Types.VARCHAR);
                if (!values[1].equalsIgnoreCase("null")) {
                    statement.setString(2, values[1]);
                } else {
                    statement.setNull(2, Types.VARCHAR);
                }
                statement.setInt(3, values[2].equalsIgnoreCase("0") ? Types.INTEGER : Integer.parseInt(values[2])); // yearOfBirth
                statement.setInt(4, values[3].equalsIgnoreCase("null") ? Types.BOOLEAN : Integer.parseInt(values[3])); // gender
//                statement.setInt(5, Integer.parseInt(values[4])); // role
                if (values[4].equalsIgnoreCase("null")) { // department
                    statement.setString(5, values[4]);
                } else {
                    statement.setNull(5, Types.VARCHAR);
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

    public static void importAccountCSVtoBD(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();

        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format for TIMESTAMP

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
                        timestamp = new Timestamp(timestampFormat.parse(values[3]).getTime());
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

    public static void importBookCSVtoDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();

        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;
        String insertSQL = "INSERT IGNORE INTO `book` (book_id, book_title, author, publisher, year, quantity, description, averageRating, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    public static void addBook(Book book) {
        if (book == null) {
            System.out.println("Book is null!");
            return;
        }

        Connection connection = DatabaseController.getConnection();

        String sqlQuery = "INSERT IGNORE INTO `book` (book_id, book_title, author , publisher, year, quantity, description, averageRating, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);

            statement.setString(1, book.getIsbn()); //book_id
            statement.setString(2, book.getTitle()); //book_title

            if (book.getAuthor() != null) {// author
                statement.setString(3, book.getAuthor());
            } else {
                statement.setNull(3, Types.VARCHAR);
            }

            if (book.getPublisher() != null) {// publisher
                statement.setString(4, book.getPublisher());
            } else {
                statement.setNull(4, Types.VARCHAR);
            }
            if (book.getYear() != 0) { // year
                statement.setInt(5, book.getYear());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setInt(6, book.getQuantity()); // quantity

            if (book.getDescription() != null) { //description
                statement.setString(7, book.getDescription());
            } else {
                statement.setNull(7, Types.VARCHAR);
            }
            statement.setDouble(8, book.getAverageRate()); // rating
            statement.setString(9, book.getCategory()); // category

            statement.executeUpdate();
            System.out.println("Book added to the database successfully.");

        } catch (SQLException e) {
            System.out.println("SQL query to add book failed!");
            e.printStackTrace();
        }
    }

    public static void importTransactionCSVtoDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
        BufferedReader reader = null;
        PreparedStatement statement = null;
        String line;

        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format for TIMESTAMP

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
                    borrow_Timestamp = new Timestamp(timestampFormat.parse(values[3]).getTime());
                    statement.setTimestamp(4, borrow_Timestamp);

                    if (values[4] != null && !values[4].equalsIgnoreCase("null")) {
                        return_Timestamp = new Timestamp(timestampFormat.parse(values[4]).getTime());
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

    public static List<Object[]> getBookForRecommend() {
        List<Object[]> results = null;

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String hql = "SELECT s.title, s.author, s.category, s.year, s.description, s.averageRate, s.id " +
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

    public static void updateBookAmountAfterBorrowed(List<String> booksID, boolean type) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            for (String bookID : booksID) {
                Book persistentBook = session.get(Book.class, bookID);

                if (persistentBook != null) {
                    if (type) {
                        persistentBook.setAmount(persistentBook.getQuantity() + 1);
                    } else {
                        persistentBook.setAmount(persistentBook.getQuantity() - 1);
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

    public static void addBookComment(Comment comment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            session.save(comment);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }


    }

    public static void importCommentCSVtoDB(String pathToCSV) throws SQLException, IOException {
        Connection connection = DatabaseController.getConnection();
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

    public static void addComment(String book_id, String user_id, String comment, double rating) {
        Connection connection = DatabaseController.getConnection();

        String sqlQuery = "INSERT IGNORE INTO `book_comment` (book_id, user_id, book_comment, rate) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sqlQuery);

            statement.setString(1, book_id);
            statement.setInt(2, Integer.parseInt(user_id));
            statement.setString(3, comment);
            statement.setDouble(4, rating);

            statement.executeUpdate();

            System.out.println("Comment added to the database successfully.");

        } catch (SQLException e) {
            System.out.println("SQL query to add comment failed!");
            e.printStackTrace();
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


    public static void importDataFromCSV() {
        try {
            importAccountCSVtoBD(accountCSVPath);
            importUserCSVToDB(userCSVPath);
            importBookCSVtoDB(bookCSVPath);
            importTransactionCSVtoDB(transactionCSVPath);
            importCommentCSVtoDB(book_commentCSVPath);
            // thieu announcement
        } catch (SQLException e) {
            System.out.println("Initial import -> SQL exception: Cannot import data from CSV file!");
        } catch (IOException e) {
            System.out.println("Initial import -> IO exception: Cannot import data from CSV file!");
        }
    }

    public static void exportDataToSCV() {
        ResultSet resultSetAccount = null;
        ResultSet resultSetUser = null;
        ResultSet resultSetBook = null;
        ResultSet resultSetTransaction = null;
        ResultSet resultSetComment = null;

        try {
            Statement accountQuery = DatabaseController.getConnection().createStatement();
            Statement userQuery = DatabaseController.getConnection().createStatement();
            Statement bookQuery = DatabaseController.getConnection().createStatement();
            Statement transactionQuery = DatabaseController.getConnection().createStatement();
            Statement commentQuery = DatabaseController.getConnection().createStatement();

            resultSetAccount = accountQuery.executeQuery("SELECT * from account");
            resultSetUser = userQuery.executeQuery("SELECT * from user");
            resultSetBook = bookQuery.executeQuery("SELECT * from book");
            resultSetTransaction = transactionQuery.executeQuery("SELECT * from transaction");
            resultSetComment = commentQuery.executeQuery("SELECT * from book_comment");
        } catch (SQLException e) {
            System.out.println("SQL Exception: Cannot get result set!");
        }

        try {
            ExportResultSetToCSV(resultSetAccount, accountCSVPath);
            ExportResultSetToCSV(resultSetUser, userCSVPath);
            ExportResultSetToCSV(resultSetBook, bookCSVPath);
            ExportResultSetToCSV(resultSetTransaction, transactionCSVPath);
            ExportResultSetToCSV(resultSetComment, book_commentCSVPath);
        } catch (IOException e) {
            System.out.println("IO Exception: Cannot export result set!");
        } catch (SQLException e) {
            System.out.println("SQL Exception: Cannot export result set!");
        }

    }

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
            Statement useDatabaseStatement = connection.createStatement();
            useDatabaseStatement.execute("USE library");
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
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

    public static List<Book> searchBook(String isbn, String title, String author, String category, String year) {
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

    public static List<Transaction> getFilteredBorrowTransactions(String title, String author, String category, String isbn, String username) {
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

    public static boolean hadComment(String username, String bookIsbn) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String hql = "FROM Transaction t WHERE t.user.username =:username AND t.book.id = :bookIsbn";
            Query<Transaction> query = session.createQuery(hql, Transaction.class);
            query.setParameter("username", username);
            query.setParameter("bookIsbn", bookIsbn);

            List<Transaction> transactions = query.list();
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


}
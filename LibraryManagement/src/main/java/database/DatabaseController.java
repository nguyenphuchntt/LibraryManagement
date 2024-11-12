package database;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseController {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "18092005";
    private static final String accountCSVPath = "src/main/resources/csv/account.csv";
    private static final String announcementCSVPath = "src/main/resources/csv/announcement.csv";
    private static final String bookCSVPath = "src/main/resources/csv/book.csv";
    private static final String book_commentCSVPath = "src/main/resources/csv/book_comment.csv";
    private static final String transactionCSVPath = "src/main/resources/csv/transaction.csv";
    private static final String userCSVPath = "src/main/resources/csv/user.csv";
    private static final String SQL_FILE = "src/main/java/database/createTableQuery.sql";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connected to database");
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

}

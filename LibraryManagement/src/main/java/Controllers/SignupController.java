package Controllers;

import Entity.Account;
import Entity.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Timestamp;
import java.util.Random;

public class SignupController {

    @FXML
    private TextField username_TextField;
    @FXML
    private TextField password_TextField; // change to password field
    @FXML
    private TextField confirmPassword_TextField; // change to password field
    @FXML
    private Label signupMessage_Label;

    @FXML
    public void initialize() {}

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        String username = username_TextField.getText();
        String password = password_TextField.getText();
        String confirmPassword = confirmPassword_TextField.getText();
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signupMessage_Label.setText("Please fill all the fields");
            return;
        }
        if (!password.equals(confirmPassword)) {
            signupMessage_Label.setText("Passwords do not match");
            return;
        }

        long timestamp = System.currentTimeMillis() % 1000000;
        int randomNum = new Random().nextInt(1000);
        String id = String.format("%012d%03d", timestamp, randomNum);

        SessionFactory userSessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Person.class)
                .buildSessionFactory();

        Session userSession = userSessionFactory.getCurrentSession();

        Person user = null;
        Account account = null;
        try {
            userSession.beginTransaction();

            user = new Person.Builder<>()
                    .person_ID(Integer.parseInt(id))
                    .name("NguyenVanA")
                    .role(true)
                    .build();

            userSession.save(user);

            userSession.getTransaction().commit();

            System.out.println("User saved!");
        } catch (Exception e) {
            System.out.println("Error saving user");
            e.printStackTrace();
        } finally {
            userSessionFactory.close();
        }

        SessionFactory accountSessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Account.class)
                .buildSessionFactory();

        Session accoutSession = accountSessionFactory.getCurrentSession();

        try {
            accoutSession.beginTransaction();

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            account = new Account.Builder()
                    .account_ID(Integer.parseInt(id))
                    .user_ID(user)
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
            accountSessionFactory.close();
        }

//        try {
//            ResultSet user = null;
//            Connection connection = DatabaseController.getConnection();
//            String sqlQuery = "SELECT * FROM account WHERE username = ?";
//            Statement useDatabaseStatement = connection.createStatement();
//            useDatabaseStatement.execute("USE library");
//            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
//
//            preparedStatement.setString(1, username);
//            user = preparedStatement.executeQuery();
//
//            if (user.next()) {
//                signupMessage_Label.setText("Username already exists");
//                return;
//            }
//        } catch (SQLException e) {
//            System.out.println("SQLException -> Create account: " + e.getMessage());
//        }
//
//        long timestamp = System.currentTimeMillis() % 1000000;
//        int randomNum = new Random().nextInt(1000);
//
//        String userID = String.format("%012d%03d", timestamp, randomNum);
//
//        Person user = new Person.Builder<>()
//                .person_ID(Integer.parseInt(userID))
//                .role(true)
//                .build();
//
//        timestamp = System.currentTimeMillis() % 1000112;
//        String accountID = String.format("%012d%03d", timestamp, randomNum);
//
//        Account newAccount = new Account.Builder()
//                .account_ID(Integer.parseInt(accountID))
//                .user_ID(user)
//                .username(username)
//                .password(password)
//                .typeAccount(true)
//                .build();

//        user.setAccount(newAccount);
//        newAccount.setOwner(user);

//        DatabaseController.addUser(user);
//        DatabaseController.addAccount(newAccount);

        switchToLoginScene();
        username_TextField.clear();
        password_TextField.clear();
        confirmPassword_TextField.clear();
    }

    @FXML
    private void handleHadAccount(ActionEvent event) {
        switchToLoginScene();
    }

    private void switchToLoginScene() {
        try {
            Controller.getInstance().goToScene("Login.fxml");
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
        }
    }


}

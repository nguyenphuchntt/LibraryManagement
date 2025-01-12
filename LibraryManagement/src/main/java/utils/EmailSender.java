package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    private final String smtpHost = "smtp.gmail.com";
    private final String smtpPort = "587";
    private final String senderEmail = DotenvLoader.getDotenv().get("senderEmail");
    private final String senderPassword = DotenvLoader.getDotenv().get("senderPassword");

    public void sendOTP(String recipientEmail) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + generateOTP());

            Transport.send(message);
            System.out.println("OTP sent successfully");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static String generateOTP() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    public static void main(String[] args) {
        EmailSender mailer = new EmailSender();
        String recipientEmail = "nguyenvanphucuet@gmail.com";
        mailer.sendOTP(recipientEmail);
    }
}

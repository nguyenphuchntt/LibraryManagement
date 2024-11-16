-- SHOW DATABASES;
-- DROP DATABASE IF EXISTS library;
CREATE DATABASE IF NOT EXISTS library;

USE library;

CREATE TABLE IF NOT EXISTS `user` (
    user_id INT NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    yearOfBirth YEAR DEFAULT NULL,
    gender VARCHAR(10) DEFAULT NULL,
    role BOOLEAN NOT NULL,
    department VARCHAR(100) DEFAULT NULL,
    index (role)
);

CREATE TABLE IF NOT EXISTS account (
    account_id INT NOT NULL PRIMARY KEY,
    user_id INT NOT NULL,
    username VARCHAR(30) UNIQUE NOT NULL,
    password CHAR(60) NOT NULL,
    account_type BOOLEAN NOT NULL,
    joined_date TIMESTAMP NOT NULL,
    avatar MEDIUMBLOB DEFAULT NULL,
    CONSTRAINT `user_account_fk` FOREIGN KEY (user_id) REFERENCES `user` (user_id) ON UPDATE CASCADE,
    CONSTRAINT `user_accountRole_fk` FOREIGN KEY (account_type) REFERENCES `user` (role) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS book (
    book_id VARCHAR(20) NOT NULL PRIMARY KEY,
    book_title VARCHAR(100) NOT NULL,
    author VARCHAR(50) DEFAULT NULL,
    publisher VARCHAR(50) DEFAULT NULL,
    year INT DEFAULT NULL,
    quantity INT NOT NULL,
    description MEDIUMTEXT DEFAULT NULL,
    averageRating DOUBLE NOT NULL,
    category VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS book_comment (
    comment_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_id VARCHAR(20) NOT NULL,
    user_id INT NOT NULL,
    book_comment TEXT DEFAULT NULL,
    rate TINYINT NOT NULL,
    CONSTRAINT `book_comment_fk` FOREIGN KEY (book_id) REFERENCES book (book_id) ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS transaction (
    transaction_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_id VARCHAR(20) NOT NULL,
    user_id INT NOT NULL,
    type BOOLEAN NOT NULL,
    time TIMESTAMP NOT NULL,
    amount INT NOT NULL,
    CONSTRAINT `transaction_book_fk` FOREIGN KEY (book_id) REFERENCES book(book_id) ON UPDATE CASCADE,
    CONSTRAINT `transaction_user_fk` FOREIGN KEY (user_id) REFERENCES `user`(user_id) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS announcement (
    announcement_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    author_id INT NOT NULL,
    receiver_id INT NOT NULL,
    CONSTRAINT `announcement_author_fk` FOREIGN KEY (author_id) REFERENCES `user`(user_id) ON UPDATE CASCADE,
    CONSTRAINT `announcement_receiver_fk` FOREIGN KEY (receiver_id) REFERENCES `user`(user_id) ON UPDATE CASCADE
);

-- SHOW DATABASES;
-- DROP DATABASE IF EXISTS library;
CREATE DATABASE IF NOT EXISTS library;

USE library;

CREATE TABLE IF NOT EXISTS account (
    username VARCHAR(30) UNIQUE PRIMARY KEY NOT NULL,
    password CHAR(60) NOT NULL,
    account_role BOOLEAN NOT NULL DEFAULT FALSE,
    joined_date TIMESTAMP NOT NULL,
    avatar MEDIUMBLOB DEFAULT NULL,
    INDEX(account_role)
    );

ALTER TABLE account
    MODIFY username VARCHAR(30) COLLATE utf8_bin;

ALTER TABLE account
    MODIFY password VARCHAR(60) COLLATE utf8_bin;

CREATE TABLE IF NOT EXISTS `user` (
    username VARCHAR(30) COLLATE utf8_bin UNIQUE PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    yearOfBirth YEAR DEFAULT NULL,
    gender VARCHAR(10) DEFAULT NULL,
    department VARCHAR(100) DEFAULT NULL,
    CONSTRAINT `user_account_fk` FOREIGN KEY (username) REFERENCES `account` (username) ON UPDATE CASCADE
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
    category VARCHAR(50) NOT NULL,
    thumbnail_link VARCHAR(255) DEFAULT NULL
    );

CREATE TABLE IF NOT EXISTS book_comment (
    comment_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_id VARCHAR(20) NOT NULL,
    username VARCHAR(30) COLLATE utf8_bin NOT NULL,
    book_comment TEXT DEFAULT NULL,
    rate TINYINT NOT NULL,
    CONSTRAINT `book_comment_fk` FOREIGN KEY (book_id) REFERENCES book (book_id) ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS transaction (
    transaction_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    book_id VARCHAR(20) NOT NULL,
    username VARCHAR(30) COLLATE utf8_bin NOT NULL,
    borrow_time TIMESTAMP NOT NULL,
    return_time TIMESTAMP DEFAULT NULL,
    CONSTRAINT `transaction_book_fk` FOREIGN KEY (book_id) REFERENCES book(book_id) ON UPDATE CASCADE,
    CONSTRAINT `transaction_user_fk` FOREIGN KEY (username) REFERENCES `user`(username) ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS announcement (
    announcement_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    author_username VARCHAR(30) COLLATE utf8_bin NOT NULL,
    receiver_username VARCHAR(30) COLLATE utf8_bin NOT NULL,
    CONSTRAINT `announcement_author_fk` FOREIGN KEY (author_username) REFERENCES `user`(username) ON UPDATE CASCADE,
    CONSTRAINT `announcement_receiver_fk` FOREIGN KEY (receiver_username) REFERENCES `user`(username) ON UPDATE CASCADE
    );



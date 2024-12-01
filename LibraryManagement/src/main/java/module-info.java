module uet.oop.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires java.naming;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires com.google.gson;
    requires mysql.connector.j;

    opens controllers to javafx.fxml, org.hibernate.orm.core;
    exports controllers;

    opens entities to javafx.fxml, org.hibernate.orm.core;
    exports entities;

    opens database to javafx.fxml, org.hibernate.orm.core;
    exports database;

    opens utils to javafx.fxml, org.hibernate.orm.core;
    exports utils;

    exports DTO;
    opens DTO to javafx.fxml, org.hibernate.orm.core;
}
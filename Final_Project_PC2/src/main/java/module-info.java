module com.example.workshowjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens javafx_jdbc.entities to javafx.base;
    opens javafx_jdbc to javafx.fxml, javafx.graphics;
    opens javafx_jdbc.gui.util to javafx.fxml, javafx.graphics;
    opens javafx_jdbc.gui to javafx.fxml, javafx.graphics, javafx.base;


    exports javafx_jdbc;
    exports javafx_jdbc.entities;
    exports javafx_jdbc.service;
    exports javafx_jdbc.gui.listeners;
    exports javafx_jdbc.gui.util;
    exports javafx_jdbc.gui;
}
module com.example.workshowjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens javafx_jdbc.entities to javafx.base;


    opens javafx_jdbc to javafx.fxml, javafx.graphics;
    exports javafx_jdbc;
    exports javafx_jdbc.gui.util;
    opens javafx_jdbc.gui.util to javafx.fxml, javafx.graphics;
    exports javafx_jdbc.gui;
    opens javafx_jdbc.gui to javafx.fxml, javafx.graphics;
}
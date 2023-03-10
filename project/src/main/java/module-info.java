module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires lombok;

    opens com.example to javafx.fxml;
    exports com.example.client;
    opens com.example.client to javafx.fxml;
    exports com.example.server;
    opens com.example.server to javafx.fxml;
    exports com.example;
}
package com.example.client;

import com.example.ClientApp;
import com.example.entity.Player;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import static com.example.protocol.Constant.*;

public class RegisterController implements Initializable {

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    private Controller controller;

    private Scene scene;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @SneakyThrows
    @FXML
    public void register(ActionEvent event){

        Window owner = submitButton.getScene().getWindow();

        if (fullNameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your name");
            return;
        }

        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        String fullName = fullNameField.getText();
        controller.transfer(messageOutputStream, messageInputStream, Player.builder().username(fullName).build());
        messageOutputStream.writeMessage(new Message(SERVER_DRAW_PLACES, "CHE".getBytes(StandardCharsets.UTF_8)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("BlackJack!");
        stage.setScene(scene);
        stage.show();
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Socket socket = null;
        try {
            socket = new Socket("localhost",5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Client client = new Client(socket);
        messageOutputStream = client.getMessageOutputStream();
        messageInputStream = client.getMessageInputStream();
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource("hello-view.fxml"));
        this.scene = new Scene(fxmlLoader.load(), 600, 400);
        this.controller = fxmlLoader.getController();
        Thread listener = new Thread(new MessageListener(client, controller));
        listener.start();
    }
}

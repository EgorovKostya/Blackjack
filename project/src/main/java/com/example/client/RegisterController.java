package com.example.client;

import com.example.ClientApp;
import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
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

    private Stage stage;

    @FXML
    private TextField fullNameField;

    @FXML
    private Button submitButton;

    private Client client;

    @SneakyThrows
    @FXML
    public void register(ActionEvent event){

        Window owner = submitButton.getScene().getWindow();

        if (fullNameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your username");
            return;
        }

        String fullName = fullNameField.getText();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage = stage;
        controller.transfer(client, messageOutputStream, messageInputStream, Player.builder().username(fullName).build(), stage);
        messageOutputStream.writeMessage(new Message(SERVER_DRAW_PLACES, "CHE".getBytes(StandardCharsets.UTF_8)));
        stage.setTitle("BlackJack!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                messageOutputStream.writeMessage(new Message(PLAYER_LEAVE_THE_GAME, Parser.serialize(client.getPlayer())));
            }
        });

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
        this.client = client;
    }
}

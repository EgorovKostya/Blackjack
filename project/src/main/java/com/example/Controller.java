package com.example;

import com.example.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Button button1;

    @FXML
    public Button button2;

    @FXML
    public Button button3;

    @FXML
    public Button button4;

    @FXML
    public Button button5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Socket socket = null;
        try {
            socket = new Socket("localhost",5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Client client = new Client(socket, this);
    }
}
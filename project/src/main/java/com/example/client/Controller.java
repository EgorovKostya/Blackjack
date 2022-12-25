package com.example.client;

import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.protocol.Constant.*;

public class Controller implements Initializable {

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

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
        messageOutputStream = client.getMessageOutputStream();
        messageInputStream = client.getMessageInputStream();
        Thread listener = new Thread(new MessageListener(client));
        listener.start();
    }

    public void onClickAction1(ActionEvent actionEvent) {
        button1.setText("Вы");
        button1.setDisable(true);
        byte[] des = Parser.serialize(Player.builder().username("Petya").placeId((byte) 1).build());
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction2(ActionEvent actionEvent) {
        button2.setText("Вы");
        button2.setDisable(true);
        byte[] des = Parser.serialize(Player.builder().username("WHO").placeId((byte) 2).build());
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction3(ActionEvent actionEvent) {
        button3.setText("Вы");
        button3.setDisable(true);
        byte[] des = Parser.serialize(Player.builder().username("MIsha").placeId((byte) 3).build());
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction4(ActionEvent actionEvent) {
        button4.setText("Вы");
        button4.setDisable(true);
        byte[] des = Parser.serialize(Player.builder().username("Kostya").placeId((byte) 4).build());
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction5(ActionEvent actionEvent) {
        button5.setText("Вы");
        button5.setDisable(true);
        byte[] des = Parser.serialize(Player.builder().username("Sirin").placeId((byte) 5).build());
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void drawPlayerPlaces(Player player) {
        switch (player.getPlaceId()) {
            case 1: {
                button1.setDisable(true);
                setText(button1, player.getUsername());
                break;
            }
            case 2: {
                button2.setDisable(true);
                setText(button2, player.getUsername());
                break;
            }
            case 3: {
                button3.setDisable(true);
                setText(button3, player.getUsername());
                break;
            }
            case 4: {
                button4.setDisable(true);
                setText(button4, player.getUsername());
                break;
            }
            case 5: {
                button5.setDisable(true);
                setText(button5, player.getUsername());
                break;
            }
        }
    }

    private void setText(Button button, String username) {
        Platform.runLater(() -> button.setText(username));
    }
}
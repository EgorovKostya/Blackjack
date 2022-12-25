package com.example.client;

import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import static com.example.protocol.Constant.*;

public class Controller {

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

    private Player player;


    public void onClickAction1(ActionEvent actionEvent) {
        button1.setText("Вы");
        button1.setDisable(true);
        player.setPlaceId((byte) 1);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction2(ActionEvent actionEvent) {
        button2.setText("Вы");
        button2.setDisable(true);
        player.setPlaceId((byte) 2);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction3(ActionEvent actionEvent) {
        button3.setText("Вы");
        button3.setDisable(true);
        player.setPlaceId((byte) 3);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction4(ActionEvent actionEvent) {
        button4.setText("Вы");
        button4.setDisable(true);
        player.setPlaceId((byte) 4);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction5(ActionEvent actionEvent) {
        button5.setText("Вы");
        button5.setDisable(true);
        player.setPlaceId((byte) 5);
        byte[] des = Parser.serialize(player);
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

    public void transfer(MessageOutputStream messageOutputStream, MessageInputStream messageInputStream, Player player) {
        this.messageOutputStream = messageOutputStream;
        this.messageInputStream = messageInputStream;
        this.player = player;
    }
}
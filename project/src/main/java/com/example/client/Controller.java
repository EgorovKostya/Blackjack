package com.example.client;

import com.example.entity.Hand;
import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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

    @FXML
    public Button leaveButton;

    private Player player;

    private Client client;


    public void onClickAction1(ActionEvent actionEvent) {
        button1.setDisable(true);
        player.setPlaceId((byte) 1);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction2(ActionEvent actionEvent) {
        button2.setDisable(true);
        player.setPlaceId((byte) 2);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction3(ActionEvent actionEvent) {
        button3.setDisable(true);
        player.setPlaceId((byte) 3);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction4(ActionEvent actionEvent) {
        button4.setDisable(true);
        player.setPlaceId((byte) 4);
        byte[] des = Parser.serialize(player);
        messageOutputStream.writeMessage(new Message(TAKE_PLACE, des));
    }

    public void onClickAction5(ActionEvent actionEvent) {
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

    public void onClickActionLeave(ActionEvent actionEvent) {
        messageOutputStream.writeMessage(new Message(PLAYER_LEAVE, Parser.serialize(player)));
    }

    private void setText(Button button, String username) {
        Platform.runLater(() -> button.setText(username));
    }

    public void transfer(Client client, MessageOutputStream messageOutputStream,
                         MessageInputStream messageInputStream, Player player, Stage stage) {
        this.client = client;
        client.setPlayer(player);
        client.setStage(stage);
        this.messageOutputStream = messageOutputStream;
        this.messageInputStream = messageInputStream;
        this.player = player;

    }

    public void deletePlayerPlace(byte i) {
        switch (i) {
            case 0: {
                button1.setDisable(false);
                setText(button1, "Занять");
                break;
            }
            case 1: {
                button2.setDisable(false);
                setText(button2, "Занять");
                break;
            }
            case 2: {
                button3.setDisable(false);
                setText(button3, "Занять");
                break;
            }
            case 3: {
                button4.setDisable(false);
                setText(button4, "Занять");
                break;
            }
            case 4: {
                button5.setDisable(false);
                setText(button5, "Занять");
                break;
            }
        }
    }

    public void drawFreePlaces(ArrayList<Player> players) {
        System.out.println(players);
        for (byte i = 0; i < 5; i++) {
            if (players.get(i) == null) {
                deletePlayerPlace(i);
            }
        }
        messageOutputStream.writeMessage(new Message(SERVER_DRAW_PLACES, "CHE".getBytes(StandardCharsets.UTF_8)));
    }

    public void disableLeaveButton() {
        leaveButton.setDisable(true);
    }

    public void drawPLayersCards(ArrayList<Hand> hand) {
        for (byte i = 0; i < 6; i++) {
            switch (i) {
                case 0: {
                    //рисовалка карт для дилера
                    break;
                }
                case 1: {
                    //рисовалка карт для игрока под местом 1
                    break;
                }
                case 2: {
                    //рисовалка карт для игрока под местом 2
                    break;
                }
                case 3: {
                    //рисовалка карт для игрока под местом 3
                    break;
                }
                case 4: {
                    //рисовалка карт для игрока под местом 4
                    break;
                }
                case 5: {
                    //рисовалка карт для игрока под местом 5
                    break;
                }
            }
        }
    }
}
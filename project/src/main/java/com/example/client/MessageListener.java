package com.example.client;

import com.example.entity.Hand;
import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.protocol.Constant.*;

public class MessageListener implements Runnable {

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    private Client client;

    private Controller controller;

    private Hand serverHand;

    private Hand playerHand;

    public MessageListener(Client client, Controller controller) {
        this.client = client;
        this.messageInputStream = client.getMessageInputStream();
        this.messageOutputStream = client.getMessageOutputStream();
        this.controller = controller;
    }


    @SneakyThrows
    @Override
    public void  run() {
        Message message;
        try {
            while ((message = messageInputStream.readMessage()) != null) {
                switch (message.getType()) {
                    case DRAW_PLACES: {
                        ArrayList<Player> deserialize = (ArrayList<Player>) Parser.deserialize(message.getData());
                        byte i = 0;
                        for (Player player: deserialize) {
                            if (player != null) {
                                controller.drawPlayerPlaces(player);
                            } else {
                                controller.deletePlayerPlace(i);
                            }
                            i++;
                        }
                        break;
                    }
                    case SOMEONE_ENTERED_ROOM: {
                        Player player = (Player) Parser.deserialize(message.getData());
                        controller.drawPlayerPlaces(player);
                        break;
                    }
                    case DRAW_FREE_PLACES: {
                        ArrayList<Player> players = (ArrayList<Player>) Parser.deserialize(message.getData());
                        controller.drawFreePlaces(players);
                        break;
                    }
                    case GAME_STARTED: {
                        controller.disableLeaveButton();
                        ArrayList<Hand> hand = (ArrayList<Hand>) Parser.deserialize(message.getData());
                        playerHand = hand.get(client.getPlayer().getPlaceId());
                        serverHand = hand.get(0);
                        controller.drawPLayersCards(hand);
                        break;
                    }
                    case DRAW_CARDS: {
                        ArrayList<Hand> hand = (ArrayList<Hand>) Parser.deserialize(message.getData());
                        controller.drawCardsWhoAlredyPlay(hand);
                        break;
                    }
                }
            }

        } catch ( Throwable e) {
            e.printStackTrace();
        }
    }
}

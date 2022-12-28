package com.example.client;

import com.example.entity.Hand;
import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import javafx.application.Platform;
import lombok.SneakyThrows;

import javax.net.ssl.SNIHostName;
import java.util.ArrayList;

import static com.example.protocol.Constant.*;

public class MessageListener implements Runnable {

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    private Client client;

    private Controller controller;

    private ArrayList<Player> places = new ArrayList<>();

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
                        ArrayList<Hand> hands = (ArrayList<Hand>) Parser.deserialize(message.getData());

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.drawPlayersScore(hands);
                            }
                        });
                        controller.drawPLayersCards(hands);
                        for (int i = 1; i < 6; i++) {
                            if (check21Win(hands.get(i)) && containsThisPlaceId(places, i)) {
                                Player player = getPlayerWithThisPlaceId(places, i);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.drawWonMessage(player);
                                    }
                                });
                            }
                        }
                        break;
                    }
                    case DRAW_CARDS: {
                        ArrayList<Hand> hand = (ArrayList<Hand>) Parser.deserialize(message.getData());
                        controller.drawCardsWhoAlreadyPlay(hand);
                        break;
                    }
                    case DRAW_PLUS_MINUS: {
                        ArrayList<Player> players = (ArrayList<Player>) Parser.deserialize(message.getData());
                        for (int i = 0; i < 5; i++) {
                            if (client.getPlayer().getUsername().equals(players.get(i).getUsername())) {
                                controller.drawPlusAndMinus(players.get(i).getPlaceId());
                            }
                        }
                        break;
                    }
                    case DRAW_SCORES: {
                        ArrayList<Hand> hands = (ArrayList<Hand>) Parser.deserialize(message.getData());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.drawPlayersScore(hands);
                            }
                        });
                        break;
                    }
                    case YOU_WON_GAME: {
                        Player player = (Player) Parser.deserialize(message.getData());
                        System.out.println(player);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.drawWonMessage(player);
                            }
                        });
                        break;
                    }
                    case DRAW_EXTRA_CART: {
                        ArrayList<Hand> hands = (ArrayList<Hand>) Parser.deserialize(message.getData());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.drawTwoLastCards(hands);
                            }
                        });
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.drawPlayersScoreMore(hands);

                            }
                        });
                        break;
                    }
                    case YOU_LOSE_GAME: {
                        Player player = (Player) Parser.deserialize(message.getData());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.drawLoserMessage(player);
                            }
                        });
                        break;
                    }
                    case PLAYER_RESET_PLACES: {
                        places = new ArrayList<>();
                        break;
                    }
                    case NEW_PLACE_ID: {
                        Player player = (Player) Parser.deserialize(message.getData());
                        places.add(player);
                        break;
                    }
                    case DRAW_DEALER_CARDS: {
                        Hand hand = (Hand) Parser.deserialize(message.getData());
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.drawDealerCardsAndScore(hand);
                            }
                        });
                        break;
                    }
                }
            }

        } catch ( Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean containsThisPlaceId(ArrayList<Player> places, int i) {
        for (Player player: places) {
            if (player.getPlaceId() == i) {
                return true;
            }
        }
        return false;
    }

    private Player getPlayerWithThisPlaceId(ArrayList<Player> places, int i) {
        for (Player player: places) {
            if (player.getPlaceId() == i) {
                return player;
            }
        }
        return null;
    }

    private boolean check21Win(Hand hand) {
        byte sum = 0;
        for (byte rank: hand.getCards()) {
            sum += rank;
        }
        return sum == 21;
    }


}

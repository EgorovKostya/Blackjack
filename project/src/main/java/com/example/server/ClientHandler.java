package com.example.server;

import com.example.entity.Hand;
import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import com.example.util.ChooseCard;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.protocol.Constant.*;

public class ClientHandler implements Runnable {

    public static List<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    private Hand serverHand;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            messageInputStream = new MessageInputStream(socket.getInputStream());
            messageOutputStream = new MessageOutputStream(socket.getOutputStream());
            clientHandlers.add(this);
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        Message message;
        while (!socket.isClosed()) {
            message = messageInputStream.readMessage();
            switch (message.getType()) {
                case TAKE_PLACE: {
                    Player player = (Player) Parser.deserialize(message.getData());
                    Server.places.set(player.getPlaceId() - 1, player);
                    System.out.println(Server.places);
                    sendMessageToAllClients(new Message(SOMEONE_ENTERED_ROOM, Parser.serialize(player)));
                    byte cntOfOccupiedPlaces = 0;
                    for (byte i = 0; i < 5; i++) {
                        if (Server.places.get(i) != null) {
                            cntOfOccupiedPlaces++;
                        }
                    }
                    messageOutputStream.writeMessage(new Message(NEW_PLACE_ID, Parser.serialize(player)));
                    if (cntOfOccupiedPlaces == 5) {
                        firstHand();
                    }
                    break;
                }
                case SERVER_DRAW_PLACES: {
                    byte[] des = Parser.serialize(Server.places);
                    messageOutputStream.writeMessage(new Message(DRAW_PLACES, des));

                    if (Server.hands.size() != 0) {
                        messageOutputStream.writeMessage(new Message(DRAW_CARDS, Parser.serialize(Server.hands)));
                        messageOutputStream.writeMessage(new Message(DRAW_SCORES, Parser.serialize(Server.hands)));
                    }
                    if (Server.answers.size() == 5) {
                        messageOutputStream.writeMessage(new Message(DRAW_DEALER_CARDS,
                                Parser.serialize(Server.hands.get(0))));
                    }
                    break;
                }
                case PLAYER_LEAVE: {
                    leavePlayer(message);
                    break;
                }
                case PLAYER_LEAVE_THE_GAME: {
                    leavePlayer(message);
                    remove();
                    close(socket, messageOutputStream, messageInputStream);
                    break;
                }
                case PLAYER_DONT_TAKEN_CARD_ANYMORE: {
                    String placeId = (String) Parser.deserialize(message.getData());
                    Server.answers.add(placeId);
                    Server.notPermanentPlayers.add(placeId);
                    dealerWork();
                    System.out.println(Server.answers);
                    break;
                }
                case PLAYER_TAKE_ONE_MORE_CARD: {
                    String placeId = (String) Parser.deserialize(message.getData());
                    Hand hand = Server.hands.get(Integer.parseInt(placeId));
                    giveOneMoreCard(hand);
                    sendMessageToAllClients(new Message(DRAW_EXTRA_CART, Parser.serialize(Server.hands)));
                    if (checkOverMaximum(hand)) {
                        messageOutputStream.writeMessage(new Message(OVER_MAXIMUM, Parser.serialize(Server.places.get(Integer.parseInt(placeId) - 1))));
                        Server.answers.add(placeId);
                        Server.notPermanentPlayers.add(placeId);
                        dealerWork();
                    }
                    System.out.println(Server.hands);
                    break;
                }
            }
        }
    }

    private void firstHand() {
        ArrayList<Hand> hands = new ArrayList<>();
        for (byte i = 0; i < 6; i++) {
            Hand hand = new Hand();
            hand.getCards()[0] = ChooseCard.getRandomCards();
            hand.getCards()[1] = ChooseCard.getRandomCards();
            if (hand.getCards()[0] == 11 && hand.getCards()[1] == 11) {
                hand.getCards()[1] = 1;
            }
            hands.add(hand);
        }
        Server.hands = hands;
        System.out.println(Parser.serialize(hands).length);
        sendMessageToAllClients(new Message(GAME_STARTED, Parser.serialize(hands)));
        sendMessageToAllClients(new Message(DRAW_PLUS_MINUS, Parser.serialize(Server.places)));
        for (byte i = 1; i < 6; i++) {
            if (check21Win(hands.get(i))) {
                Server.answers.add(String.valueOf(i));
                Server.winnersWith21.add(String.valueOf(i));
            }
        }
    }

    private void dealerWork() {
        if (Server.answers.size() == 5) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessageToAllClients(new Message(DRAW_DEALER_CARDS, Parser.serialize(Server.hands.get(0))));
            //логика с игрой дилера
            dealerMove();
        }
    }

    private void leavePlayer(Message message) {
        Player player = (Player) Parser.deserialize(message.getData());
        for (byte i = 0; i < 5; i++) {
            if (Server.places.get(i) != null) {
                if (player.getUsername().equals(Server.places.get(i).getUsername())) {
                    Server.places.set(i, null);
                }
            }
        }
        messageOutputStream.writeMessage(new Message(PLAYER_RESET_PLACES, "FDFS".getBytes(StandardCharsets.UTF_8)));
        byte[] des = Parser.serialize(Server.places);
        messageOutputStream.writeMessage(new Message(DRAW_FREE_PLACES, des));
        sendMessageAnotherPlayers(new Message(DRAW_PLACES, des));
    }

    private void dealerMove() {
        System.out.println(Server.hands.get(0));
        System.out.println(1);
        if (getCardsSum(Server.hands.get(0)) >= 17) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dealerStopTakenCard();
            return;
        }
        dealerStep();
    }

    private void dealerStopTakenCard() {
        System.out.println(Server.notPermanentPlayers);
        for (String placeId: Server.notPermanentPlayers) {
            int dealerSum = getCardsSum(Server.hands.get(0));
            int playerSum = getCardsSum(Server.hands.get(Integer.parseInt(placeId)));
            System.out.println( dealerSum + " " + playerSum);
            if (dealerSum == playerSum) {
                System.out.println(placeId + "player draw");
                messageOutputStream.writeMessage(new Message(GAME_RESULT_DRAW, Parser.serialize(Server.places.get(Integer.parseInt(placeId) - 1))));
            } else if ((dealerSum < playerSum && dealerSum >= 22) || (playerSum < dealerSum && dealerSum < 22) || (playerSum >= 22 && dealerSum < 22)) {
                System.out.println(placeId + "player lose");
                messageOutputStream.writeMessage(new Message(YOU_LOSE_GAME, Parser.serialize(Server.places.get(Integer.parseInt(placeId) - 1))));
            } else {
                System.out.println(placeId + "player won");
                messageOutputStream.writeMessage(new Message(YOU_WON_GAME, Parser.serialize(Server.places.get(Integer.parseInt(placeId) - 1))));
            }
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        restartGame();
    }

    private void restartGame() {
        sendMessageToAllClients(new Message(RESET_TABLE, "".getBytes(StandardCharsets.UTF_8)));
        Server.answers = new ArrayList<>();
        Server.winnersWith21 = new ArrayList<>();
        Server.notPermanentPlayers = new ArrayList<>();
        Server.places = new ArrayList<>();
        Server.places.add(null);
        Server.places.add(null);
        Server.places.add(null);
        Server.places.add(null);
        Server.places.add(null);
        Server.hands = new ArrayList<>();
    }

    private void dealerStep() {
        Hand hand = Server.hands.get(0);
        giveOneMoreCard(hand);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendMessageToAllClients(new Message(DRAW_DEALER_CARDS, Parser.serialize(Server.hands.get(0))));
        dealerMove();
    }

    private int getCardsSum(Hand hand) {
        byte sum = 0;
        for (byte rank : hand.getCards()) {
            sum += rank;
        }
        return sum;
    }
    private boolean checkOverMaximum(Hand hand) {
        byte sum = 0;
        for (byte rank: hand.getCards()) {
            sum += rank;
        }
        return sum > 21;
    }

    private void giveOneMoreCard(Hand hand) {
        for (byte i = 2; i < hand.getCards().length; i++) {
            if (hand.getCards()[i] == 0) {
                hand.getCards()[i] = ChooseCard.getRandomCards();
                break;
            }
        }
    }

    private boolean check21Win(Hand hand) {
        byte sum = 0;
        for (byte rank: hand.getCards()) {
            sum += rank;
        }
        return sum == 21;
    }

    private void sendMessageToAllClients(Message message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.messageOutputStream.writeMessage(message);
        }
    }


    public void sendMessageAnotherPlayers(Message message) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (!clientHandler.socket.equals(socket)) {
                clientHandler.messageOutputStream.writeMessage(message);
            }
        }
    }

    public void remove() {
        clientHandlers.remove(this);
    }

    public void close(Socket socket, MessageOutputStream messageOutputStream, MessageInputStream messageInputStream) {
        try {
            if (messageInputStream.getInputStream() != null) {
                messageInputStream.getInputStream().close();
            }
            if (messageOutputStream.getOutputStream() != null) {
                messageOutputStream.getOutputStream().close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

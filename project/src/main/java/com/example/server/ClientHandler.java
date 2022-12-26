package com.example.server;

import com.example.client.Client;
import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import static com.example.protocol.Constant.*;

public class ClientHandler implements Runnable {

    public static List<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

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
        while ((message = messageInputStream.readMessage()) != null) {
            switch (message.getType()) {
                case TAKE_PLACE: {
                    Player player = (Player) Parser.deserialize(message.getData());
                    Server.places.set(player.getPlaceId() - 1, player);
                    System.out.println(Server.places);
                    sendMessageAnotherPlayers(new Message(SOMEONE_ENTERED_ROOM, Parser.serialize(player)));
                }
                case SERVER_DRAW_PLACES: {
                    byte[] des = Parser.serialize(Server.places);
                    messageOutputStream.writeMessage(new Message(DRAW_PLACES, des));
                    break;
                }
                case PLAYER_LEAVE: {
                    Player player = (Player) Parser.deserialize(message.getData());
                    for (byte i = 0; i < 5; i++) {
                        if (Server.places.get(i) != null) {
                            if (player.getUsername().equals(Server.places.get(i).getUsername())) {
                                Server.places.set(i, null);
                            }
                        }
                    }
                    byte[] des = Parser.serialize(Server.places);
                    messageOutputStream.writeMessage(new Message(DRAW_FREE_PLACES, des));
                    sendMessageAnotherPlayers(new Message(DRAW_PLACES, des));
                    break;
                }
                case PLAYER_LEAVE_THE_GAME: {
                    remove();
                    break;
                }
            }
        }
        System.out.println(Server.places);
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
            if (messageOutputStream.getOutputStream() != null) {
                messageOutputStream.getOutputStream().close();
            }
            if (messageInputStream.getInputStream() != null) {
                messageInputStream.getInputStream().close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.example.server;

import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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
            close(socket, messageOutputStream, messageInputStream);
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
                }
            }
        }
    }
    public void sendMessageAnotherPlayers(Message message) {
        for (ClientHandler clientHandler: clientHandlers) {
            if (!clientHandler.socket.equals(socket)) {
                clientHandler.messageOutputStream.writeMessage(message);
            }
        }
    }
    public void remove() {
        clientHandlers.remove(this);
    }

    public void close(Socket socket, MessageOutputStream messageOutputStream, MessageInputStream messageInputStream) {
        remove();
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

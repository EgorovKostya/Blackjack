package com.example.client;

import com.example.entity.Player;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import lombok.Data;

import java.io.*;
import java.net.Socket;

@Data
public class Client {

    private Socket socket;

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    private Controller controller;

    private Player player;

    public Client(Socket socket, Controller controller) {
        try {

            this.socket = socket;
            messageInputStream = new MessageInputStream(socket.getInputStream());
            messageOutputStream = new MessageOutputStream(socket.getOutputStream());
            this.controller = controller;
        } catch (IOException e) {
            close(socket, messageOutputStream, messageInputStream);
        }
    }

    public Client() {
    }


    public void sendMessage(Message message) {
        messageOutputStream.writeMessage(message);
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

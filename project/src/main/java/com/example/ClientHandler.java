package com.example;

import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    @Override
    public void run() {
        String message;
        while (socket.isConnected()) {

        }
    }

    public void remove() {
        clientHandlers.remove(this);
    }
}

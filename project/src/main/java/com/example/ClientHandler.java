package com.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    public static List<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;

    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;

    private String username;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            username = bufferedReader.readLine();
            clientHandlers.add(this);
            sendMessage(username + "Entered the chat");
        } catch (IOException e) {
            close(socket, bufferedWriter, bufferedReader);
        }
    }

    private void close(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        remove();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
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
            try {
                message = bufferedReader.readLine();
                sendMessage(message);
            } catch (IOException e){
                close(socket, bufferedWriter, bufferedReader);
                break;
            }
        }
    }

    public void sendMessage(String message) {
        for (ClientHandler clientHandler: clientHandlers) {
            try {

                if (!clientHandler.username.equals(username)) {
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                close(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    public void remove() {
        clientHandlers.remove(this);
        sendMessage(username + "left the chat");
    }
}

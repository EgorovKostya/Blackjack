package com.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;

    private String username;

    public Client(Socket socket) {
        try {

            this.socket = socket;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            listenMessage();
            sendMessage();
        } catch (IOException e) {
            close(socket, bufferedWriter, bufferedReader);
        }
    }

    public Client() {

    }


    public void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Scanner scanner = new Scanner(System.in);

                    while (socket.isConnected()) {
                        bufferedWriter.write(username + ": " + scanner.nextLine());
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    close(socket, bufferedWriter, bufferedReader);
                }
            }
        }).start();
    }

    public void listenMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message;
                    while (socket.isConnected()) {
                        message = bufferedReader.readLine();
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void close(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
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
}

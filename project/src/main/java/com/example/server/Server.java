package com.example.server;

import com.example.entity.Hand;
import com.example.entity.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    public static List<Player> places = new ArrayList<>();

    private ServerSocket serverSocket;

    public static List<Hand> hands = new ArrayList<>();

    public static List<String> answers = new ArrayList<>();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

    }

    public void start(){
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = null;
                socket = serverSocket.accept();
                System.out.println("New client enter the game");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException E) {
            close();
        }
    }

    private void close() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        places.add(null);
        places.add(null);
        places.add(null);
        places.add(null);
        places.add(null);
        ServerSocket serverSocket = new ServerSocket(5555);
        Server server = new Server(serverSocket);
        server.start();
    }
}

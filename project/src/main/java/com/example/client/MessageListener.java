package com.example.client;

import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;

import static com.example.protocol.Constant.*;

public class MessageListener implements Runnable {

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    private Client client;

    private Controller controller;

    public MessageListener(Client client) {
        this.client = client;
        this.messageInputStream = client.getMessageInputStream();
        this.messageOutputStream = client.getMessageOutputStream();
        this.controller = client.getController();
    }


    @Override
    public void run() {
        Message message;
        try {
            while ((message = messageInputStream.readMessage()) != null) {
                switch (message.getType()) {
                    case DRAW_PLACES: {
                        byte[] temp = message.getData();
                        for (byte i = 0; i < temp.length; i++) {
                            if (temp[i] == 1) {

                            }
                        }
                    }
                    case SOMEONE_ENTERED_ROOM: {
                        Player player = (Player) Parser.deserialize(message.getData());
                        System.out.println(player);
                        controller.drawPlayerPlaces(player);
                        break;
                    }
                }
            }

        } catch ( Throwable e) {
            client.close(client.getSocket(), client.getMessageOutputStream(), client.getMessageInputStream());
        }
    }
}

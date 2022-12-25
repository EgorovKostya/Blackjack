package com.example.client;

import com.example.entity.Player;
import com.example.mapper.Parser;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;
import com.example.protocol.MessageOutputStream;
import lombok.SneakyThrows;

import java.util.ArrayList;

import static com.example.protocol.Constant.*;

public class MessageListener implements Runnable {

    private MessageInputStream messageInputStream;

    private MessageOutputStream messageOutputStream;

    private Client client;

    private Controller controller;

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
                        byte[] temp = message.getData();
                        for (Player player: (ArrayList<Player>) Parser.deserialize(message.getData())) {
                            if (player != null) {
                                controller.drawPlayerPlaces(player);
                            }
                        }
                        break;
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
            e.printStackTrace();
        }
    }
}

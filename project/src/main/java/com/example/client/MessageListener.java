package com.example.client;

import com.example.Controller;
import com.example.protocol.Message;
import com.example.protocol.MessageInputStream;

import static com.example.protocol.Constant.*;

public class MessageListener implements Runnable {

    private MessageInputStream messageInputStream;

    private Client client;

    private Controller controller;

    public MessageListener(Client client) {
        this.client = client;
        this.messageInputStream = client.getMessageInputStream();
        this.controller = client.getController();
    }


    @Override
    public void run() {
        Message message;

        try {

            while ((message = messageInputStream.readMessage()) != null) {

                switch (message.getType()) {
//                    case ENTER_ROOM: {
//                        client.sendMessage(new Message());
//                        break;
//                    }
//                    case TAKE_PLACE: {
//
//                    }
                }
            }

        } catch ( Throwable e) {
            client.close(client.getSocket(), client.getMessageOutputStream(), client.getMessageInputStream());
        }
    }
}

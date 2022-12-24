package com.example.protocol;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.ProtocolException;


import static com.example.protocol.Message.*;

@Data
public class MessageInputStream {

    private InputStream inputStream;

    public MessageInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @SneakyThrows
    public Message readMessage() {
        if (inputStream.read() != HEADER_1 && inputStream.read() != HEADER_2) {
            throw new ProtocolException("Another version of protocol");
        }
        for (byte i = 0; i < 5; i++) {
            places[i] = (byte) inputStream.read();
        }

        byte type = (byte) inputStream.read();

        int length = inputStream.read();
        byte[] data  = new byte[length];

        for (int i = 0; i < length; i++) {
            data[i] = (byte) inputStream.read();
        }
        return new Message(type, data);
    }
}

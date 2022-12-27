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
        byte start = (byte) inputStream.read();
        byte fin = (byte) inputStream.read();
        if (start != HEADER_1 && fin != HEADER_2) {
            throw new ProtocolException("Another version of protocol");
        }
        byte type = (byte) inputStream.read();

        int length = inputStream.read();

        byte[] data  = new byte[length];


//
//// 3)чтение типа сообщения (messageTypeLength байт)
//        byte[] messageTypeBytes = new byte[messageTypeLength];
//        in.read(messageTypeBytes,0,messageTypeLength);
        for (int i = 0; i < length; i++) {
            data[i] = (byte) inputStream.read();
        }
        return new Message(type, data);
    }
}

package com.example.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.Socket;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private InputStream inputStream;
    private OutputStream outputStream;
    public static final byte HEADER_1 = 0X01;
    public static final byte HEADER_2 = 0X02;
    public static final byte[] places = new byte[5];

    private byte type;

    private byte[] data;


    @SneakyThrows
    public Message(Socket socket) {
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public Message(byte type, byte[] data) {
        this.type = type;
        this.data = data;
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

    @SneakyThrows
    public void writeMessage(Message message) {
        outputStream.write(HEADER_1);
        outputStream.write(HEADER_2);
        outputStream.write(places);

        byte type = message.getType();

        byte[] data = message.getData();

        outputStream.write(type);

        int length = data.length;

        outputStream.write(length);

        for (int i = 0; i < length; i++){
            outputStream.write(data[i]);
        }
    }
}

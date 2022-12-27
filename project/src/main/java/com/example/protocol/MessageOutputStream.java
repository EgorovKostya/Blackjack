package com.example.protocol;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.util.Arrays;

import static com.example.protocol.Message.*;

@Data
public class MessageOutputStream {

    private OutputStream outputStream;


    public MessageOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    @SneakyThrows
    public void writeMessage(Message message) {
        outputStream.write(HEADER_1);
        outputStream.write(HEADER_2);

        byte type = message.getType();

        byte[] data = message.getData();

        outputStream.write(type);

        int length = data.length;

        outputStream.write(length);

        byte[] tamp = new byte[length];
        for (int i = 0; i < length; i++){
            tamp[i] = data[i];
        }

        outputStream.write(tamp);
    }
}

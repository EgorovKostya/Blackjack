package com.example.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.io.OutputStream;


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

    public Message(byte type, byte[] data) {
        this.type = type;
        this.data = data;
    }
}

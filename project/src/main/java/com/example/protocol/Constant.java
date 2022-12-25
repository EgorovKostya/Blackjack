package com.example.protocol;

public class Constant {

    public static final byte ENTER_ROOM = 0; // -client send message with him USERNAME ---- (String)
    public static final byte TAKE_PLACE = 1; // -client send message with him PLACE ---- (String)


    public static final byte SEND_ID = 64; // -com.example.server send message with him ID(equals him place) --- (String)
    public static final byte SOMEONE_ENTERED_ROOM = 65;
    public static final byte DRAW_PLACES = 66;
}

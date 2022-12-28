package com.example.protocol;

public class Constant {

    public static final byte TAKE_PLACE = 1; // -client send message with him PLACE ---- (String)
    public static final byte PLAYER_LEAVE = 2; // player leave place
    public static final byte PLAYER_LEAVE_THE_GAME = 3; // player leave game
    public static final byte PLAYER_DONT_TAKEN_CARD_ANYMORE = 4;
    public static final byte PLAYER_TAKE_ONE_MORE_CARD = 5;

    public static final byte SOMEONE_ENTERED_ROOM = 65;
    public static final byte DRAW_PLACES = 66;
    public static final byte SERVER_DRAW_PLACES = 67;
    public static final byte DRAW_FREE_PLACES = 68;
    public static final byte GAME_STARTED = 69;
    public static final byte DRAW_CARDS = 70;
    public static final byte DRAW_PLUS_MINUS = 71;
    public static final byte DRAW_SCORES = 72;
    public static final byte YOU_WON_GAME = 73;
    public static final byte DRAW_EXTRA_CART = 74;
    public static final byte YOU_LOSE_GAME = 75;
    public static final byte NEW_PLACE_ID = 76;
    public static final byte PLAYER_RESET_PLACES = 77;
    public static final byte DRAW_DEALER_CARDS = 78;
    public static final byte OVER_MAXIMUM = 79;
    public static final byte GAME_RESULT_DRAW = 80;
    public static final byte RESET_TABLE = 81;
}

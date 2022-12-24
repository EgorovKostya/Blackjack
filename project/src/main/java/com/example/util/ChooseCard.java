package com.example.util;

import java.util.Random;

public class ChooseCard {

    public static byte[] cards = new byte[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};


    public static byte getRandomCards() {
        Random random = new Random();
        return cards[random.nextInt(13)];
    }
}

package com.example.entity;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {

    private String username;
    private byte placeId;
    private byte[] cards = new byte[11];
}

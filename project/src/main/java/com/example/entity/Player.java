package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {

    private String username;
    private String id;
    private byte placeId;
    private byte[] cards = new byte[11];
}

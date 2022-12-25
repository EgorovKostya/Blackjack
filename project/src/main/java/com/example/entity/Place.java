package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.OutputStream;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place implements Serializable {

    private byte id;
    private String username;
}

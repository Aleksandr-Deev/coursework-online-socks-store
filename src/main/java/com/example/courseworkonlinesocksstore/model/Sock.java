package com.example.courseworkonlinesocksstore.model;

import lombok.Data;

@Data
public class Sock {

    private final Color color;
    private final Size size;
    private final int cottonPercentage;

    public Sock(Color color, Size size, int cottonPercentage) {
        this.color = color;
        this.size = size;
        this.cottonPercentage = cottonPercentage;
    }
}

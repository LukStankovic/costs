package com.stankovic.lukas.vydaje.Model;

public class Entry {

    public final int id;
    public final int userId;
    public final int accountId;
    public final String name;
    public final double amount;
    public final String longitude;
    public final String latitude;
    public final String datetime;
    public final String timestamp;

    public Entry(int id, int userId, int accountId, String name, double amount, String longitude, String latitude, String datetime, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.name = name;
        this.amount = amount;
        this.longitude = longitude;
        this.latitude = latitude;
        this.datetime = datetime;
        this.timestamp = timestamp;
    }
}

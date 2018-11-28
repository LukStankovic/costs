package com.stankovic.lukas.vydaje.Model;

public class User {

    public final int id;
    public final String surname;
    public final String name;
    public final String login;

    public User(int id, String surname, String name, String login) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.login = login;
    }
}

package com.lunarapps.hakuna.models;

import java.util.Date;

public class User extends Person {

    private static final long serialVersionUID = 161616L;
    private String username;
    private String password;
    private String email;

    public User(String firstname, String lastname, Sex s, String username, String password) {
        super(firstname, lastname, s);
        this.username = username;
        this.password = password;

    }

    public User(String firstname, String lastname, Sex s, String email, String username, String password) {
        super(firstname, lastname, s);
        this.email = email;
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }
}


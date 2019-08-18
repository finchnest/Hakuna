package com.lunarapps.hakuna.models;

import java.io.Serializable;

public abstract class Person implements Serializable {

    private static final long serialVersionUID=151515L;

    private String firstname;
    private String lastname;
    private Sex sex;


    public Person(String firstname, String lastname, Sex s){
        this.firstname=firstname;
        this.lastname=lastname;
        this.sex=s;
    }


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Sex getSex() {
        return sex;
    }
}

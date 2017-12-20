package com.codificador.contactapp;

import java.io.Serializable;

/**
 * Created by Seng on 11/17/2017.
 */

public class Contact implements Serializable{

    private String name;
    private String number;
    private long id;

    public Contact(long id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public Contact(String name, String number) {
        id = 0;
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

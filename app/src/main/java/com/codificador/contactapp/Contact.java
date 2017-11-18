package com.codificador.contactapp;

import java.io.Serializable;

/**
 * Created by Seng on 11/17/2017.
 */

public class Contact implements Serializable{

    private String name;
    private String number;

    public Contact(String name, String number) {
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
}

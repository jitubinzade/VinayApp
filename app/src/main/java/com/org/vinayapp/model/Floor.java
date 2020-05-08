package com.org.vinayapp.model;

/**
 * Created by JITU on 11/04/2020.
 */
public class Floor {

    private String name;
    private int id;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

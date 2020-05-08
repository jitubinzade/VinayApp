package com.org.vinayapp.model;

/**
 * Created by JITU on 03/05/2020.
 */
public class Section {
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

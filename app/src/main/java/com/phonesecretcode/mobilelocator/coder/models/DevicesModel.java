package com.phonesecretcode.mobilelocator.coder.models;

import java.io.Serializable;

public class DevicesModel implements Serializable {

//    private static int nextId = 1;
    private int ID;
    private String name;
    private int parentID;

    public DevicesModel(int ID, String name, int parentID) {
        this.ID = ID;
        this.name = name;
        this.parentID = parentID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }
}

package com.phonesecretcode.mobilelocator.coder.models;

import java.io.Serializable;

public class MobileModels implements Serializable {

    private int ID;
    private String title, desc;
    private int parentID, extra;

    public MobileModels(int ID, int parentID, String title, String desc, int extra) {
        this.ID = ID;
        this.parentID = parentID;
        this.title = title;
        this.desc = desc;
        this.extra = extra;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public int getExtra() {
        return extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }
}

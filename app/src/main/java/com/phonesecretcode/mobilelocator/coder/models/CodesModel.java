package com.phonesecretcode.mobilelocator.coder.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "codes")
public class CodesModel implements Serializable {

    @PrimaryKey()
    private int ID;
    private String code, codeDesc, brand;
    private int brandID;
    private boolean isFavourite;

    @Ignore
    public CodesModel(String code, String codeDesc, int brandID) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.brandID = brandID;
    }

    public CodesModel(int ID, String code, String codeDesc, String brand) {
        this.ID = ID;
        this.code = code;
        this.codeDesc = codeDesc;
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}

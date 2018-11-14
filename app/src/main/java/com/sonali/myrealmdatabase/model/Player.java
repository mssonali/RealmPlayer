package com.sonali.myrealmdatabase.model;

import io.realm.RealmObject;

public class Player extends RealmObject {

    String id;
    String pName;
    String pGender;
    String pYear;
    byte pImag[];


    public byte[] getpImag() {
        return pImag;
    }

    public void setpImag(byte[] pImag) {
        this.pImag = pImag;
    }


    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getppGender() {
        return pGender;
    }

    public void setppGender(String pScore) {
        this.pGender = pScore;
    }

    public String getpYear() {
        return pYear;
    }

    public void setpYear(String pYear) {
        this.pYear = pYear;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

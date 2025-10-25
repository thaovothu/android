package com.midterm.bt4_contact.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String mobile;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String email;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] avatar;

    public Contact (String mobile, String name, String email){
        this.mobile = mobile;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return avatar;
    }

    public void setImage(byte[] avatar) {
        this.avatar = avatar;
    }



}

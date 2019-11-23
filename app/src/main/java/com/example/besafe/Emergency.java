package com.example.besafe;

public class Emergency {
    private String Name,Phone,UserPhone;
    private int id;

    public Emergency() {
    }

    public Emergency(String name, String phone, String userPhone, int id) {
        Name = name;
        Phone = phone;
        UserPhone = userPhone;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}

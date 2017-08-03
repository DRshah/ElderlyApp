package com.example.android.elderlyapp.core;

/**
 * Created by Karan on 01-10-2016.
 */
public class Contact {
    String name,number;
    Contact(String n,String no){
        name=n;
        number=no;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}

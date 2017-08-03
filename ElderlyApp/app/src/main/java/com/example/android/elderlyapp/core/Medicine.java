package com.example.android.elderlyapp.core;

/**
 * Created by Karan on 30-09-2016.
 */
public class Medicine {
    private String medicine;
    private String time;
    private String rqs;
    public Medicine(String m,String t,String rqs){
        medicine=m;
        time=t;
        this.rqs = rqs;
    }
    public String getMedicine(){
        return medicine;
    }
    public String getTime(){
        return time;
    }
    public String getRqs(){
        return rqs;
    }
}

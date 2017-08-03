package com.example.android.elderlyapp.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Karan on 30-09-2016.
 */
public class DBAdapter {
    public DBAdapter(){}

    private SQLiteDatabase db;
    public DBAdapter(Context context){
        DBHelper helper=new DBHelper(context);
        db=helper.getWritableDatabase();
    }
    private static final String DB_NAME="ElderlyAppDB.sqlite";
    private static final int DB_VERSION=1;
    private static final String TABLE_MEDICINE="medicines";
    private static final String COL_MEDNAME="medname";
    private static final String COL_TIME="time";
    private static final String COL_MED_ID="id";
    private static final String COL_RQ="rqs";
    private static final String CREATE_MEDICINE=String.format("create table %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s text not null,%s text,%s text);",TABLE_MEDICINE,COL_MED_ID,COL_MEDNAME,COL_TIME,COL_RQ);

    private static final String TABLE_CONTACT="contacts";
    private static final String COL_NAME="name";
    private static final String COL_NUMBER="number";
    private static final String CREATE_CONTACTS=String.format("create table %s(%s text,%s text);",TABLE_CONTACT,COL_NAME,COL_NUMBER);
    //return an array list of all medicines
    public ArrayList<Medicine> getMedicines(){
        ArrayList<Medicine> medicines=new ArrayList<Medicine>();

        Cursor cursor=db.query(TABLE_MEDICINE,null,null,null,null,null,null);
        int id=cursor.getColumnIndex(COL_MED_ID);
        int timneId=cursor.getColumnIndex(COL_TIME);
        int medId=cursor.getColumnIndex(COL_MEDNAME);
        int medRq=cursor.getColumnIndex(COL_RQ);
        while(cursor.moveToNext()){
            Medicine m=new Medicine(cursor.getString(medId),cursor.getString(timneId),cursor.getString(medRq));
            medicines.add(m);
        }
        return medicines;
    }
//    public ArrayList<Medicine> getAlarmtimes(String name){
//        ArrayList<Medicine> medicines=new ArrayList<Medicine>();
//        String select[]={COL_TIME};
//        String args[]={name};
//        Cursor cursor=db.query(TABLE_MEDICINE, select, COL_MEDNAME + " like ?", args, null, null, null);
//        int timneId=cursor.getColumnIndex(COL_TIME);
//        while(cursor.moveToNext()){
//            Medicine m=new Medicine(null,cursor.getString(timneId));
//            medicines.add(m);
//        }
//        return medicines;
//    }
    public ArrayList<Contact> getContacts(){
        ArrayList<Contact> contacts=new ArrayList<Contact>();
        Cursor cursor=db.query(TABLE_CONTACT, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            Contact c=new Contact(cursor.getString(cursor.getColumnIndex(COL_NAME)),cursor.getString(cursor.getColumnIndex(COL_NUMBER)));
            contacts.add(c);
        }
        return contacts;
    }
    public boolean insertContact(String n,String no){
        ContentValues cv=new ContentValues();
        cv.put(COL_NAME,n);
        cv.put(COL_NUMBER,no);
        long id=db.insert(TABLE_CONTACT,null,cv);
        if(id==-1)
            return false;
        return true;
    }


    public  boolean updateList(String t,String tt){
        ContentValues cv=new ContentValues();
        //cv.put(COL_MEDNAME,name);
        cv.put(COL_TIME,tt);
        long id=db.update(TABLE_MEDICINE,cv,COL_TIME +" like \""+t+"\"",null);
        if(id<=0)
            return false;
        return true;
    }



    public boolean insertMedicine(String name,String t,String r){
        ContentValues cv=new ContentValues();
        cv.put(COL_MEDNAME,name);
        cv.put(COL_TIME,t);
        cv.put(COL_RQ,r);
        long id=db.insert(TABLE_MEDICINE,null,cv);
        if(id==-1)
            return false;
        return true;
    }
    //private static final String COL_TABLET_COUNT="tabletcount";

    public boolean deleteMedicine(String t){
        int f=db.delete(TABLE_MEDICINE,COL_TIME +" like \""+t+"\"",null);
        System.out.print("deleted from DB");
        if(f==1)
            return true;
        else
            return false;

    }

    class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {

            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_MEDICINE);
            db.execSQL(CREATE_CONTACTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

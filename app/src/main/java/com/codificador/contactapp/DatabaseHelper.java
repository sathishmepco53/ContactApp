package com.codificador.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Seng on 12/7/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "contacts.db";
    static final String TABLE_NAME = "contact";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_NUMBER = "number";
    static final String COLUMN_ID = "id";
    static final String CREATE_QUERY = "create table "+TABLE_NAME+"("
                                        + COLUMN_ID+" integer primary key,"
                                        + COLUMN_NAME+" text,"
                                        + COLUMN_NUMBER+" text)";
    static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertContact(Contact newContact){
        try{
            SQLiteDatabase database = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME,newContact.getName());
            contentValues.put(COLUMN_NUMBER,newContact.getNumber());
            long row = database.insert(TABLE_NAME,null,contentValues);
            return row;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> list = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
            long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            Contact contact = new Contact(id,name,number);
            list.add(contact);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public int delete(long id){
        SQLiteDatabase database = getWritableDatabase();
        String whereArgs[] = {id+""};
        int rows = database.delete(TABLE_NAME,COLUMN_ID+" = ?",whereArgs);
        return rows;
    }

    public int update(Contact contact){
        try{
            SQLiteDatabase database = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME,contact.getName());
            contentValues.put(COLUMN_NUMBER,contact.getNumber());
            int rows = database.update(TABLE_NAME,contentValues,COLUMN_ID+" =? ",new String[]{contact.getId()+""});
            return rows;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
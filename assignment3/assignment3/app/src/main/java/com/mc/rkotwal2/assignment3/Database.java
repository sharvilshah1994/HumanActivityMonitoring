package com.mc.rkotwal2.assignment3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.SensorEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.id;
import static android.provider.Contacts.SettingsColumns.KEY;
import static java.sql.Types.INTEGER;

/**
 * Created by rkotwal2 on 11/21/2016.
 */

public class Database extends SQLiteOpenHelper {
    public static final String DB_NAME = "/mnt/sdcard/Android/data/Rashik.db" ;
    public String TableName ;

    public static final String[] myStringArrayX = new String[51];
    public static final String[] myStringArrayY = new String[51];
    public static final String[] myStringArrayZ = new String[51];
    public static final String label = "label";

    public void initialise() {
        for(int i=0;i<50;i++) {
            myStringArrayX[i]="X"+Integer.toString(i);
            myStringArrayY[i]="Y"+Integer.toString(i);
            myStringArrayZ[i]="Z"+Integer.toString(i);
        }
    }

    public Database(Context context)
    {
        super (context,DB_NAME,null,1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db)
    {
        initialise();
       // db.execSQL("DROP TABLE IF EXISTS '"+"Group27"+"'");
        //SQLiteDatabase db=this.getWritableDatabase();
        TableName="mGroup27";

        String createTable = "create table " + TableName + "( Id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT 1," + myStringArrayX[0] + " REAL," + myStringArrayY[0] + " REAL," + myStringArrayZ[0] + " REAL,";
        for(int i=1;i<50;i++) {
            createTable+=myStringArrayX[i] + " REAL," + myStringArrayY[i] + " REAL," + myStringArrayZ[i] + " REAL,";
        }
        createTable+=label + " TEXT)";

        db.execSQL(createTable);
    }

    /*public SQLiteDatabase onCreat(SQLiteDatabase db) {

        initialise();
        //db=this.getWritableDatabase();
        db=this.getWritableDatabase();
        System.out.print(db);

        return db;
        //    db.close();
    }*/


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+getTableName());
        //onCreate(db);
    }

    public void insertData( String TableName, SensorEvent event, float x, float y , float z, int i)
    {
        SQLiteDatabase  db=this.getWritableDatabase();
        ContentValues cV = new ContentValues();
        long timeInMillis = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
        // if(val==true)
        //cV.put(timestamp,getDateTime());
        cV.put(myStringArrayX[i],x);
        cV.put(myStringArrayY[i],y);
        cV.put(myStringArrayZ[i],z);
        System.out.println("in insert " +cV);
//        if(isTableExists(TableName))
        db.insert(TableName,null,cV);
    }

    /*
    public Cursor getdata()
    {
        if (TableName!=null) {
            Cursor cursor = getReadableDatabase().query(TableName,
                    new String[]{X, Y, Z},
                    null, null, null, null, null);

            return cursor;
        }
        else return null;

    }*/

    public void setTableName(String str )
    {
        TableName=str;

    }
    public String getTableName()
    {
        return TableName;
    }

    /*
    public boolean isTableExists(String tName) {
        SQLiteDatabase mDatabase;

        mDatabase = getReadableDatabase();


        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }*/

    public int countTableEntry()
    {

        if (TableName!=null) {
            SQLiteDatabase db= this.getReadableDatabase();
            String countEntry = "select count(*) from "+getTableName();
            Cursor c = db.rawQuery(countEntry,null);
            c.moveToNext();
            return Integer.parseInt(c.getString(0));

        }
        return 0;

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



}
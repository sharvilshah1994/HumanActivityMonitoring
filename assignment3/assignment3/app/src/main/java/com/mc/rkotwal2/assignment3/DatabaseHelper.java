package com.mc.rkotwal2.assignment3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by malvika on 9/25/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String database_name = "/mnt/sdcard/Android/data/Kotwal.db";
    //public  static final String training = "/mnt/sdcard/Android/data/training.db";
    //public  static final String testing = "/mnt/sdcard/Android/data/testing.db";

    public static final String table_name = "Patient_data";
    public static final String table_name1 = "training_data";
    public static final String table_name2 = "testing_data";
    public static final String col_1 = "X";
    public static final String col_2 = "Y";
    public static final String col_3 = "Z";
    public static final String col_4 = "Label";
    public float x;
    public float y;
    public float z;

    public DatabaseHelper(Context context) {
        super(context, database_name, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + table_name + "( Id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT 1," + col_1 + " REAL, " + col_2 + " REAL, " + col_3 + " REAL, " + col_4 + " TEXT );");

        //fetch(db);
        //attach
        //db.execSQL("ATTACH '/mnt/sdcard/Android/data/Kotwal.db' As Malvika");
        //db.execSQL("INSERT INTO Kotwal1.Patient_data Select * from Malvika.Patient_data");

        /*copying into testing table
        db.execSQL("INSERT INTO " + table_name2 + "SELECT * From " +table_name +" where ID BETWEEN '"+ 1 + "' AND '" + 333 );
        db.execSQL("INSERT INTO " + table_name2 + "SELECT * From " +table_name +" where ID BETWEEN '"+ 968 + "' AND '" + 1301 );
        db.execSQL("INSERT INTO " + table_name2 + "SELECT * From " +table_name +" where ID BETWEEN '"+ 1936 + "' AND '" + 2269 );
        //copying into training table

        db.execSQL("INSERT INTO " + table_name1 + "SELECT * From " +table_name +" where ID BETWEEN '"+ 334 + "' AND '" + 967 );
        db.execSQL("INSERT INTO " + table_name1 + "SELECT * From " +table_name +" where ID BETWEEN '"+ 1302 + "' AND '" + 1935 );
        db.execSQL("INSERT INTO " + table_name1 + "SELECT * From " +table_name +" where ID BETWEEN '"+ 2270 + "' AND '" + 2910 );*/


    }

    public void createnewtable() {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("create table if not exists " + table_name1 + "( Id" + " INTEGER, " + col_1 + " REAL, " + col_2 + " REAL, " + col_3 + " REAL, " + col_4 + " TEXT );");
        db.execSQL("create table if not exists " + table_name2 + "( Id" + " INTEGER, " + col_1 + " REAL, " + col_2 + " REAL, " + col_3 + " REAL, " + col_4 + " TEXT );");
    }

    public void delete()
    {
        SQLiteDatabase db = this.getWritableDatabase();
       // db.execSQL("DROP TABLE IF EXISTS training_data");
        db.execSQL("DROP TABLE IF EXISTS testing_data");
    }

    public void fetch() {
        SQLiteDatabase dbfetch = this.getWritableDatabase();
        //copying into testing table
       // dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT col_1, col_2, col_3, col_4 From " + table_name + " where ID BETWEEN '1' AND '333'");// + ""'1'" +  AND  + ""'333'");
        //dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT col_1, col_2, col_3, col_4 From " + table_name + " where ID BETWEEN '968' AND '1301'"); //+ '968'' + " AND " + 1301 );
        //dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT col_1, col_2, col_3, col_4 From " + table_name + " where ID BETWEEN '1936' AND '2269'"); //+ 1936 + " AND " + 2269 );
        //copying into training table
        dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT * From " + table_name + " where ID BETWEEN " + 1 + " AND "  + 333 );
        dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT * From " + table_name + " where ID BETWEEN " + 968 + " AND " + 1301 );
        dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT * From " + table_name + " where ID BETWEEN " + 1936 + " AND " + 2269 );
        /*dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT * From " + table_name + " where ID BETWEEN " + 334 + " AND "  + 967 );
        dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT * From " + table_name + " where ID BETWEEN " + 1302 + " AND " + 1935 );
        dbfetch.execSQL("INSERT INTO " + table_name2 + " SELECT * From " + table_name + " where ID BETWEEN " + 2270 + " AND " + 2910 );*/

    }

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
    }



    public void put(float x, float y, float z,String ActivityName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, x);
        contentValues.put(col_2, y);
        contentValues.put(col_3, z);
        contentValues.put(col_4, ActivityName);
        System.out.println("in insert " +contentValues);
        db.insert(table_name, null, contentValues);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+table_name);
        onCreate(db);
    }
}
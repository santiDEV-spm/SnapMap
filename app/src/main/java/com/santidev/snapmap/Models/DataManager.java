package com.santidev.snapmap.Models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataManager {

    private SQLiteDatabase db;

    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_TITLE = "image_title";
    public static final String TABLE_ROW_URI = "image_uri";

    public static final String TABLE_ROW_TAG = "tag";
    private static final String TABLE_ROW_TAG1 = "tag1";
    private static final String TABLE_ROW_TAG2 = "tag2";
    private static final String TABLE_ROW_TAG3 = "tag3";

    private static final String DB_NAME = "snapmap_db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_PHOTOS = "snapmap_table_photos";
    private static final String TABLE_TAGS = "snapmap_table_tags";

    /**Nuevos en la version 2 de la BD**/
    public static final String TABLE_ROW_LOCATION_LAT = "gps_table_lat";
    public static final String TABLE_ROW_LOCATION_LONG = "gps_table_long";

    public DataManager(Context context){
        SnapMapSQLiteOpenHelper helper = new SnapMapSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void addPhoto(Photo photo){
        String query = "INSERT INTO "+
                        TABLE_PHOTOS + " ("+
                        TABLE_ROW_TITLE+ ", "+
                        TABLE_ROW_URI + ", "+
                        TABLE_ROW_LOCATION_LONG + ", "+
                        TABLE_ROW_LOCATION_LAT + ", "+
                        TABLE_ROW_TAG1+ ", "+
                        TABLE_ROW_TAG2+ ", "+
                        TABLE_ROW_TAG3+ ") "
                        + " VALUES (" +
                        "'"+photo.getTitle()+"', " +
                        "'"+photo.getStorageLocation()+"', " +
                        photo.getGpsLocation().getLatitude() + ", "+
                        photo.getGpsLocation().getLatitude() + ", " +
                        "'"+photo.getTag1()+"', " +
                        "'"+photo.getTag2()+"', " +
                        "'"+photo.getTag3()+"'" +
                        ");";

        Log.i("addPhoto() = ", query);
        db.execSQL(query);

        addTag(photo.getTag1());
        addTag(photo.getTag2());
        addTag(photo.getTag3());


    }

    private void addTag(String tag){
       String query = "INSERT INTO " +
                TABLE_TAGS + " ("
                + TABLE_ROW_TAG + ") " +
                "SELECT '"+ tag +"' " +
                " WHERE NOT EXISTS ("+
                " SELECT 1 FROM "+
                TABLE_TAGS+
                " WHERE " + TABLE_ROW_TAG + " = '" + tag +"'" + ");" ;

        Log.i("addTag1 () =", query);
        db.execSQL(query);
    }

    public Cursor getTitles(){
        Cursor cursor = db.rawQuery("SELECT " + TABLE_ROW_ID + "," +
                                    TABLE_ROW_TITLE +
                                    " FROM "+TABLE_PHOTOS + ";", null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getTitlesWithTag(String tag){
        Cursor cursor = db.rawQuery("SELECT "+TABLE_ROW_ID+","+
                                    TABLE_ROW_TITLE+" FROM "+TABLE_PHOTOS+" WHERE "+
                                    TABLE_ROW_TAG1+ " = '"+ tag + "' OR " +
                                    TABLE_ROW_TAG2 + " = '"+ tag+"' OR " +
                                    TABLE_ROW_TAG3 + " = '" + tag + "' ;", null);

        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getPhoto(int id){
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHOTOS+
                                        " WHERE "+ TABLE_ROW_ID + " = "+ id, null);

        cursor.moveToFirst();
        return  cursor;
    }

    public Cursor getTags(){
        Cursor cursor = db.rawQuery("SELECT " + TABLE_ROW_ID +
                                        ", "+ TABLE_ROW_TAG +
                                        " FROM " + TABLE_TAGS,null);

        cursor.moveToFirst();
        return cursor;
    }

    private class SnapMapSQLiteOpenHelper extends SQLiteOpenHelper{

        public SnapMapSQLiteOpenHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableQuery = "CREATE TABLE " +
                                    TABLE_PHOTOS + " (" +
                                    TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
                                    TABLE_ROW_TITLE + " TEXT NOT NULL, "+
                                    TABLE_ROW_URI + " TEXT NOT NULL, "+
                                    TABLE_ROW_LOCATION_LONG + " real, " + //CAMBIO EN LA CREACION POR LA VERSION 2
                                    TABLE_ROW_LOCATION_LAT + " real, " + //CAMBIO EN LA CREACION POR LA VERSION
                                    TABLE_ROW_TAG1 + " TEXT NOT NULL, "+
                                    TABLE_ROW_TAG2 + " TEXT NOT NULL, "+
                                    TABLE_ROW_TAG3 + " TEXT NOT NULL"+
                                    ");";
            db.execSQL(newTableQuery);

            newTableQuery = "CREATE TABLE "+
                            TABLE_TAGS+ " (" +
                            TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            TABLE_ROW_TAG + " TEXT NOT NULL"+
                            ");";
            db.execSQL(newTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Actualizacion de la v2
            if(oldVersion < 2) {
                String addColLong = "ALTER TABLE " + TABLE_PHOTOS + " ADD " + TABLE_ROW_LOCATION_LONG +
                        " real;";

                String addColLat = "ALTER TABLE " + TABLE_PHOTOS + " ADD " + TABLE_ROW_LOCATION_LAT +
                        " real;";

                db.execSQL(addColLong);
                db.execSQL(addColLat);
            }
            //cuando llegamos al final del onUpgrade, la version de la BD se actualiza a new Version
        }
    }

}

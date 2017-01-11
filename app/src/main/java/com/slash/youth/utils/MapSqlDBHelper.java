package com.slash.youth.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhouyifeng on 2017/1/11.
 */
public class MapSqlDBHelper extends SQLiteOpenHelper {

    public MapSqlDBHelper() {
        super(CommonUtils.getContext(), "map.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateMapSearchHisTable = "create table  map_search_his\n" +
                "(\n" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "  name VARCHAR,   \n" +
                "  address VARCHAR, \n" +
                "  lat double,\n" +
                "  lng double\n" +
                ")";
        db.execSQL(sqlCreateMapSearchHisTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getMapReadableDB() {
        return getReadableDatabase();
    }

    public SQLiteDatabase getMapWritableDB() {
        return getWritableDatabase();
    }
}

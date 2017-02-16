package com.slash.youth.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.slash.youth.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DBManager {
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "province_city_zone.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.slash.youth";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置
    public SQLiteDatabase database;
    private Context context;
    private File databaseFile;
    public static String databasePath;

    public DBManager(Context context) {
        this.context = context;
    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        File file = new File(dbfile);
        try {
            if (!(file.exists())) {
                InputStream isr = this.context.getResources().openRawResource(
                        R.raw.province_city_zone);

                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = isr.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                IOUtils.close(fos);
                IOUtils.close(isr);
                LogKit.v("not exists copy fileDB size:" + file.length());
            } else {
                LogKit.v("exists fileDB size:" + file.length());
            }

            database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);

            return database;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return database;
    }

    public void closeDatabase() {
        if (database != null) {
            this.database.close();
        }
    }
}
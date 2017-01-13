package com.slash.youth.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import com.slash.youth.R;

public class DBManager {
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "province_city_zone.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.slash.youth";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置
   // public static File databasePath = this.context.getDatabasePath(PACKAGE_NAME+"/"+DB_NAME);


     /* public static final String DB_PATH = "/data"
            + "data" + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置*/

    public SQLiteDatabase database;
    private Context context;
    private File databaseFile;
    public static String databasePath;
    //private final String path;

    public DBManager(Context context) {
        this.context = context;
       // path = this.context.getDatabasePath(DB_NAME).getAbsoluteFile().toString();

    }

    public void openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
        /*databaseFile = this.context.getDatabasePath(PACKAGE_NAME + "/" + DB_NAME);
        databasePath = databaseFile.getAbsolutePath().toString();
        this.database = this.openDatabase(databasePath);*/
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        File file = new File(dbfile);
        try {
           // if (!(file.exists())) {
                InputStream isr = this.context.getResources().openRawResource(
                        R.raw.province_city_zone);

              //  InputStream isr = this.context.getAssets().open(DB_NAME);

                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = isr.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
             //   }
                fos.close();
                isr.close();
            }

            database = SQLiteDatabase.openOrCreateDatabase(dbfile,null);

            return database;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return database;
    }
    public void closeDatabase() {
        if(database != null){
            this.database.close();
        }
    }
}
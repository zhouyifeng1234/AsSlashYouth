package com.slash.youth.utils;

import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zhouyifeng on 2016/9/1.
 */
public class JsonCacheUtils {
    public static void setCache(String urlString, String json) {
        String urlMd5 = MD5Utils.md5(urlString);
        File cacheDir = CommonUtils.getContext().getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        File cacheFile = new File(cacheDir, urlMd5);
        FileWriter fw = null;
        try {
            fw = new FileWriter(cacheFile);
            long deadLine = SystemClock.currentThreadTimeMillis() + 30 * 60 * 1000;
            fw.write(deadLine + "\r\n" + json);
            fw.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            IOUtils.close(fw);
        }
    }

    public String getCache(String urlString) {
        String urlMd5 = MD5Utils.md5(urlString);
        File cacheDir = CommonUtils.getContext().getCacheDir();
        File cacheFile = new File(cacheDir, urlMd5);
        if (!cacheFile.exists()) {
            return null;
        }
        FileReader fr = null;
        BufferedReader br = null;
        StringBuffer sbCache = new StringBuffer();
        try {
            fr = new FileReader(cacheFile);
            br = new BufferedReader(fr);
            long deadLine = Long.parseLong(br.readLine());
            if (SystemClock.currentThreadTimeMillis() > deadLine) {
                return null;
            }
            String line;
            while ((line = br.readLine()) != null) {
                sbCache.append(line);
            }
            return sbCache.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            IOUtils.close(br);
            IOUtils.close(fr);
        }
        return null;
    }
}

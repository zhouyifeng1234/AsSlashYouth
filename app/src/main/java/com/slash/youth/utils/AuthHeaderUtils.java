package com.slash.youth.utils;


import android.util.Base64;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhouyifeng on 2016/9/2.
 */
public class AuthHeaderUtils {
    private static final String AUTH_METHOD = "SYS";
    private static final String APPKEY = "slash-youth";
    private static final String APPSECRET = "8faf2d32-610a-11e6-b39c-00228f0e418a";

    /**
     * @param method GET | POST | DELETE | PUT
     * @param url    指请求URI，不包含 ? 以及 query_string
     * @return
     */
    public static Map getBasicAuthHeader(String method, String url) {
        try {
            Map header = new HashMap();
            String date = getGMTDate();
//            String date = "Wed, 20 Jul 1983 17:15:00 GMT";
            String encryptStr = String.format("%s %s\n%s", method, new URL(url).getPath(), date);
            String sign = hmacSHA1(APPSECRET, encryptStr);
            header.put("Date", date);
            String authorizationStr = String.format("%s %s:%s", AUTH_METHOD, APPKEY, sign);
            header.put("Authorization", authorizationStr);
//            System.out.println(date);
//            System.out.println(authorizationStr);
            return header;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String hmacSHA1(String key, String data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes("utf-8"));
//          必须使用 commons-codec 1.5及以上版本，否则base64加密后会出现换行问题
//          String sha1Str = Base64.encodeBase64String(rawHmac);
            String sha1Str = Base64.encodeToString(rawHmac, 0);
            return sha1Str;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * @return 获取GMT格式的当前日期时间
     */
    public static String getGMTDate() {
        long timeMillis = System.currentTimeMillis();
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        String dateStr = sdf.format(new Date(timeMillis));
//        String[] dateArr = dateStr.split(" ");
//        dateArr[4] = "GMT";
//        StringBuilder sbDate = new StringBuilder();
//        for (String str : dateArr) {
//            sbDate.append(str + " ");
//        }
//        dateStr = dateStr.replace("CST", "GMT+08:00");
//        dateStr = sbDate.toString().trim();
        return dateStr;
    }
}

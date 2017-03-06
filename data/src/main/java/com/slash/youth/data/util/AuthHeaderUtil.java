package com.slash.youth.data.util;


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

public class AuthHeaderUtil {
    private static final String AUTH_METHOD = "SYS";
    private static final String APPKEY = "slash-youth";
    private static final String APPSECRET = "8faf2d32-610a-11e6-b39c-00228f0e418a";

    /**
     * @param method GET | POST | DELETE | PUT
     * @param url    指请求URI，不包含 ? 以及 query_string
     * @return
     */
    public static String getBasicAuthHeader(String method, String url, String date) {
        try {
            String encryptStr = String.format("%s %s\n%s", method, new URL(url).getPath(), date);
            String sign = hmacSHA1(APPSECRET, encryptStr);
            String authorizationStr = String.format("%s %s:%s", AUTH_METHOD, APPKEY, sign);
            return authorizationStr;
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
            String sha1Str = Base64.encodeToString(rawHmac, 0);
            return sha1Str;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}

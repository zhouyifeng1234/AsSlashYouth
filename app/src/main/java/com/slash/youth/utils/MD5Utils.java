package com.slash.youth.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String md5(String str) {

        StringBuilder mess = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] bytes = str.getBytes();
            byte[] md5Bytes = md5.digest(bytes);
            for (byte b : md5Bytes) {
                int d = b & 0xff;
                // int d;
                // if (b >= 0) {
                // d = b;
                // } else {
                // d = 256 + b;
                // }
                String hexStr = Integer.toHexString(d);
                if (hexStr.length() == 1) {
                    hexStr = 0 + hexStr;
                }
                mess.append(hexStr);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return mess.toString();

    }
}

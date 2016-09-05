package com.slash.youth;

import com.slash.youth.utils.AuthHeaderUtils;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void testGetUrlString() {
//        BaseProtocol bp = new BaseProtocol();
//        String urlString = bp.getUrlString();
//
//        System.out.println(urlString);
    }

    @Test
    public void getGMTDateTest() {
//        for (char c = 'A'; c <= 'Z'; c++) {
//            try {
//                String gmtDate = AuthHeaderUtils.getGMTDate(c);
//                System.out.println(gmtDate);
//            } catch (Exception ex) {
//
//            }
//        }

//        String gmtDate = AuthHeaderUtils.getGMTDate();
//        System.out.println(gmtDate);

        AuthHeaderUtils.getBasicAuthHeader("POST", "http://121.42.145.178/uinfo/v1/api/vcard/skilllabel/get");

    }
}
package com.slash.youth;

import com.slash.youth.http.protocol.BaseProtocol;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void testGetUrlString() {
        BaseProtocol bp = new BaseProtocol();
        String urlString = bp.getUrlString();

        System.out.println(urlString);
    }
}
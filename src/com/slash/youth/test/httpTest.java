package com.slash.youth.test;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.HttpHelper;
import com.slash.youth.http.HttpHelper.HttpResult;

import android.test.AndroidTestCase;

public class httpTest extends AndroidTestCase {

	public void HttpGetTest() {
		HttpResult httpResult = HttpHelper.get("http://10.0.2.2:8080/test.json");
		int code = httpResult.getCode();
		System.out.println(code);
		String string = httpResult.getString();
		System.out.println(string);
	}
}

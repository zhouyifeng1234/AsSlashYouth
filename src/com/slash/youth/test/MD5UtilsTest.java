package com.slash.youth.test;

import com.slash.youth.utils.MD5Utils;
import com.slash.youth.utils.ToastUtils;

import android.test.AndroidTestCase;

public class MD5UtilsTest extends AndroidTestCase {

	public void md5Test() {
		// TODO Auto-generated method stub
		String md5 = MD5Utils.md5("123456");
		System.out.println("MD5:" + md5);
	}
}

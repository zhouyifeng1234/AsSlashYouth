package com.slash.youth.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @Description: 包信息工具类
 * @author zss
 */
public class PackageUtil {
	
	//获取版本名
	public static String getVersionName(Context context) {
		// 获取包管理器
		PackageManager pm = context.getPackageManager();
		// 获取包信息
		PackageInfo info;
		try {
			//0代表的是标志位
			info = pm.getPackageInfo(context.getPackageName(), 0);
//			info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "未知版本名";
	}
	
	//获取版本号
	public static int getVersionCode(Context context) {
		// 获取包管理器
		PackageManager pm = context.getPackageManager();
		// 获取包信息
		PackageInfo info;
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
}

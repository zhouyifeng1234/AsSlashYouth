package com.slash.youth.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.slash.youth.global.GlobalConstants;

public class SpUtils {

	public static void setBoolean(String key, boolean value) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		boolean value = sharedPreferences.getBoolean(key, defaultValue);
		return value;
	}

	public static void setString(String key, String value) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static void setLong(String key, long value) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public static void setInt(String key, int value) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		Editor edit = sharedPreferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static String getString(String key, String defaultValue) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		String value = sharedPreferences.getString(key, defaultValue);
		return value;
	}

	public static long getLong(String key, long defaultValue) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		long value = sharedPreferences.getLong(key, defaultValue);
		return value;
	}

	public static int getInt(String key, int  defaultValue) {
		SharedPreferences sharedPreferences = CommonUtils.getContext()
				.getSharedPreferences(GlobalConstants.SP_NAME,
						Context.MODE_PRIVATE);
		int value  = sharedPreferences.getInt(key, defaultValue);
		return value;
	}
}

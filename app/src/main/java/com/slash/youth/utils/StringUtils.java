package com.slash.youth.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}
	//判断是否是中英文
	public static boolean isSearchContent(String text) {
		String check = "^[\\u4E00-\\u9FA5A-Za-z]+$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(text);

		return matcher.matches();
	}


}

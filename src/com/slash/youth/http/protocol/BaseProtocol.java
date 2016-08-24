package com.slash.youth.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.os.SystemClock;

import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.HttpHelper;
import com.slash.youth.http.HttpHelper.HttpResult;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.MD5Utils;
import com.slash.youth.utils.StringUtils;

public abstract class BaseProtocol<T> {

	public T getData(int index) {
		String cacheJson = getCache(index);
		if (!StringUtils.isEmpty(cacheJson)) {
			return parseData(cacheJson);
		}
		String json = getDataFromServer(index);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		T data = parseData(json);
		setCache(index, json);
		return data;
	}

	public String getCache(int index) {
		String urlString = combineToUrl(index);
		String urlMd5 = MD5Utils.md5(urlString);
		File cacheDir = CommonUtils.getContext().getCacheDir();
		File cacheFile = new File(cacheDir, urlMd5);
		if (!cacheFile.exists()) {
			return null;
		}
		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer sbCache = new StringBuffer();
		try {
			fr = new FileReader(cacheFile);
			br = new BufferedReader(fr);
			long deadLine = Long.parseLong(br.readLine());
			if (SystemClock.currentThreadTimeMillis() > deadLine) {
				return null;
			}
			String line;
			while ((line = br.readLine()) != null) {
				sbCache.append(line);
			}
			return sbCache.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			IOUtils.close(br);
			IOUtils.close(fr);
		}
		return null;
	}

	public void setCache(int index, String json) {
		String urlString = combineToUrl(index);
		String urlMd5 = MD5Utils.md5(urlString);
		File cacheDir = CommonUtils.getContext().getCacheDir();
		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		File cacheFile = new File(cacheDir, urlMd5);
		FileWriter fw = null;
		try {
			fw = new FileWriter(cacheFile);
			long deadLine = SystemClock.currentThreadTimeMillis() + 30 * 60 * 1000;
			fw.write(deadLine + "\r\n" + json);
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.close(fw);
		}
	}

	public String getDataFromServer(int index) {
		String urlString = combineToUrl(index);
		HttpResult httpResult = HttpHelper.get(urlString);
		if (httpResult != null) {
			String result = httpResult.getString();
			return result;
		}
		return null;
	}

	public abstract T parseData(String json);

	public abstract String getPageKey();

	public abstract String getParameter();

	public String combineToUrl(int index) {
		String urlString = GlobalConstants.HttpUrl.SERVER_HOST + getPageKey() + "?index=" + index
				+ "&" + getParameter();
		return urlString;
	}
}

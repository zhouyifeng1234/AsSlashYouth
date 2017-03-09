package com.slash.youth.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class IOUtils {

	//本地存储数据
	public static void saveDate2Local(String path, ArrayList<Long> data) {
		ObjectOutputStream oos = null;
		try {
			if (data == null || data.size() == 0) {
				return;
			}
			//对象流读取一个集合
			oos = new ObjectOutputStream(new FileOutputStream(new File(CommonUtils.getApplication().getCacheDir(),path)));
			oos.writeObject(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//从本地读取
	public static ArrayList<Long> getDateFromLocal(String fileName) {
		ArrayList<Long> data = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(CommonUtils.getApplication().getCacheDir(),fileName)));
			data = (ArrayList<Long>) ois.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogKit.e(e);
			}
		}
		return true;
	}

}

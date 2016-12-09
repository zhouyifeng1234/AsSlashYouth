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

	public static final String UID_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ "Contacts_Uid";

	//本地读取数据
	public static ArrayList getDateFromLocal(String path) {
		ArrayList data = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File(path)));
			data = (ArrayList) ois.readObject();

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

	//本地存储数据
	public static void saveDate2Local(String path, ArrayList<Integer> data) {
		ObjectOutputStream oos = null;
		try {
			if (data == null || data.size() == 0) {
				return;
			}
			//对象流读取一个集合
			oos = new ObjectOutputStream(new FileOutputStream(new File(path)));
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

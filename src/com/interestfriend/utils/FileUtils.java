package com.interestfriend.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.net.Uri;
import android.os.Environment;

public class FileUtils {
	/**
	 * 判断SD是否可以
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 创建根目�?
	 * 
	 * @param path
	 *            目录路径
	 */
	public static void createDirFile(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return 创建的文�?
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * 删除文件�?
	 * 
	 * @param folderPath
	 *            文件夹的路径
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路�?
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * 获取文件的Uri
	 * 
	 * @param path
	 *            文件的路�?
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * 换算文件大小
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "未知大小";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 根据文件绝对路径获取文件名称
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isBlank(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 获取根目�?
	 */
	public static String getRootDir() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 使用当前时间戳拼接一个唯�?��文件�?
	 * 
	 * @param format
	 * @return
	 */
	public static String getFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}

	/**
	 * 获取文件扩展�?
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isBlank(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 判断sd卡是否存�?
	 * 
	 * @return
	 */
	public static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 创建文件�?
	 * 
	 * @param dir
	 */
	public static void createDir(String dir) {
		String sdpath = getRootDir();
		File destDir = new File(sdpath + dir);
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
	}

	/**
	 * 得到绝对路径
	 * 
	 * @param dir
	 * @return
	 */
	public static String getgetAbsoluteDir(String dir) {
		return getRootDir() + dir;

	}

	/**
	 * 获取拍照路径
	 */
	public static String getCameraPath() {
		String sdpath = getRootDir();
		File destDir = new File(sdpath + "/changlianxi/camera");
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
		createNoMediaFile();
		return destDir.getAbsolutePath();

	}

	public static String getCLXDir() {
		return getRootDir() + "/changlianxi/";

	}

	public static String getClxImgSavePath() {
		String path = getRootDir() + "/changlianxiImgSave/";
		File destDir = new File(path);
		if (!destDir.exists()) {// 创建文件�?
			destDir.mkdirs();
		}
		return path;

	}

	private static void createNoMediaFile() {
		File file = new File(getRootDir() + "/changlianxi/.nomedia");
		if (!file.exists()) {// 判断文件是否存在（不存在则创建这个文件）
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件转byte
	 * 
	 * @param f
	 * @return
	 */
	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1)
				out.write(b, 0, n);
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

}
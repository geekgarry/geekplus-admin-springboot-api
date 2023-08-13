package com.geekplus.common.util.getpic;

import java.util.Random;
import java.util.UUID;

public class UploadUtils {
	/**
	 * 获取随机名称
	 *
	 * @param realName
	 *            真实名称
	 * @return uuid
	 */
	public static String getUUIDName(String realName) {
		// realname 可能是 1.jpg 也可能是 1
		// 获取后缀名
		int index = realName.lastIndexOf(".");
		if (index == -1) {
			return UUID.randomUUID().toString().replace("-", "").toUpperCase();
		} else {
			return UUID.randomUUID().toString().replace("-", "").toUpperCase() + realName.substring(index);
		}
	}

	/**
	 * 获取文件真实名称
	 *
	 * @param name
	 * @return
	 */
	public static String getRealName(String name) {
		// c:/upload/1.jpg 1.jpg
		// 获取最后一个"/"
		int index = name.lastIndexOf("\\");
		return name.substring(index + 1);
	}

	/**
	 * 获取文件目录
	 *
	 * @param name
	 *            文件名称
	 * @return 目录
	 */
	public static String getDir(String name) {
		int i = name.hashCode();
		String hex = Integer.toHexString(i);
		int j = hex.length();
		for (int k = 0; k < 8 - j; k++) {
			hex = "0" + hex;
		}
		return "/" + hex.charAt(0) + "/" + hex.charAt(1);
	}

	/**
	 * 创建随机文件夹目录
	 *
	 * @return 目录
	 */
	public static int getFileName() {
		int max = 100000;
		int min = 10000;
		Random random = new Random();

		int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
	/**
	 * 随机创建数据库id
	 *
	 * @return 目录
	 */
	public static int getSqlId() {
		int max = 100000000;
		int min = 1000000;
		Random random = new Random();

		int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}

	public static void main(String[] args) {

		System.out.println(getSqlId());

	}
}

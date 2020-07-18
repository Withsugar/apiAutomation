package com.qa.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author anthony
 * @date Oct 17, 2019
 * @updateTime 5:04:50 PM
 */
public class RandomUtil {

	public static final Set<String> DIGITS = new ConcurrentSkipListSet<String>();

	/**
	 * 获取最短UUID
	 *
	 * @return
	 */
	public static long getLeastUID() {
		long uid = UUID.randomUUID().getLeastSignificantBits();
		return uid;
	}

	/**
	 * 随机生成几个数
	 *
	 * @return
	 */
	public synchronized static String getDigits(int i) {
		String s = RandomStringUtils.randomNumeric(i);
		while (DIGITS.contains(s)) {
			s = RandomStringUtils.randomNumeric(i);
		}
		DIGITS.add(s);
		return s;
	}

	/**
	 * 随机获取字符串
	 *
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
}

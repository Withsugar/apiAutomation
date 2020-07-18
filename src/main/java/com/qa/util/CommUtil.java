package com.qa.util;

import java.util.Calendar;
import java.util.UUID;

/**
 * @author anthony
 * @date Oct 31, 2019
 * @updateTime 3:31:51 PM
 */
public class CommUtil {

	public static String createNonce() {
		return UUID.randomUUID().toString();
	}

	public static long getTimestamp() {
		return Calendar.getInstance().getTimeInMillis();
	}


}

package com.qa.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密工具类
 */
public class MD5Util {

	public static void main(String[] args) {
		//String s = "123456";
		//amount+applicationCode+(1)+currencyCode+customerId+description+referenceId+returnUrl+version+Secret Key
		//String s="WvQqVHfRz57UdSU9mnZyaVdzPSSKr28Y808463Coins Package_15083d1d9ac-1853-450b-986f-643d830f7e331497600727633http://45.56.65.189:8087/third-paymentv1tanNsZCoe57r2pr9JKwdAs2empphOz8v";

		//applicationCode+(3)+customerId+description+referenceId+returnUrl+version+Secret Key
		//String s="WvQqVHfRz57UdSU9mnZyaVdzPSSKr28Y312321144221testmolTRX170890119http://192.168.2.248:8080/third-paymentv1tanNsZCoe57r2pr9JKwdAs2empphOz8v";
		//String s="20000WvQqVHfRz57UdSU9mnZyaVdzPSSKr28Y1IDR1061Coins Package_150247b6ddc-1027-49f4-a7b5-aa437ca03f501498815225491v1v1tanNsZCoe57r2pr9JKwdAs2empphOz8v";
		String s="2000WvQqVHfRz57UdSU9mnZyaVdzPSSKr28Y1SGD1061Coins Package_1470+3502e4cecaa-5c79-429b-b900-b82165571f9d1499929126216http://45.56.65.189:8087/third-paymentv1tanNsZCoe57r2pr9JKwdAs2empphOz8v";
		System.out.println(md5(s));
	}

	/**
	 * md5加密
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return DigestUtils.md5Hex(str);
	}

}

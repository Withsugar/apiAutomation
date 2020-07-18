package com.qa.util;


public class SinatureUtil {

	 //amount+applicationCode+(channelId 1)+currencyCode+customerId+description+referenceId+returnUrl+version+Secret Key
	 //applicationCode+(channelId 3)+customerId+description+referenceId+returnUrl+version+Secret Key
	 public static String getSinature(int amount,String applicationCode,short channelId, String currencyCode,
			 int customerId,String description,String referenceId,String returnUrl,String version,String SecretKey){
		String signature="";
		 if(channelId==1){
			 //signature=MD5.getMD5Code(amount+applicationCode+channelId+currencyCode+customerId+description+referenceId+returnUrl+version+SecretKey);
			String str = amount+applicationCode+channelId+currencyCode+customerId+description+referenceId+returnUrl+version+SecretKey;
			System.out.println("md5前签名串="+str);
			 signature=MD5Util.md5(str);
			 return signature;
		 }
		 String str =applicationCode+channelId+customerId+description+description+returnUrl+version+SecretKey;
		 System.out.println("md5前签名串="+str);
		 //signature=MD5.getMD5Code(applicationCode+channelId+customerId+description+description+returnUrl+version+SecretKey);
		 signature=MD5Util.md5(applicationCode+channelId+customerId+description+referenceId+returnUrl+version+SecretKey);
		 return signature;
	 }
}

package com.qa.util;

/**
 * Creaed by fj on 2018/10/24
 */

import org.bouncycastle.util.encoders.UrlBase64;

import javax.crypto.Cipher;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class RSASignature {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";
    public static final String ENCODING = "utf-8";

    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEQibDSDeAWnoRuGH6" +
            "s11eTLywiInneU/+DyDR+sZwbOujKY5W+YYbnccKVr5J71aGTjBXYfv/Blb7nr1oz0oqlcLrH4NVqPtPXIza3lD7hLtq" +
            "/FQOYRdzMnvg81R9iQtZQy9Xf/CmrvMUnVpkY8c5X2YhEgpV6ATzF9eufjcYPQIDAQAB";

    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMRCJsNIN4BaehG4" +
            "YfqzXV5MvLCIied5T/4PINH6xnBs66Mpjlb5hhudxwpWvknvVoZOMFdh+/8GVvuevWjPSiqVwusfg1Wo+09cjNreUPuEu2r8V" +
            "A5hF3Mye+DzVH2JC1lDL1d/8Kau8xSdWmRjxzlfZiESClXoBPMX165+Nxg9AgMBAAECgYEAuO4EjJquAbo/sBbn7E27OzJf0pC2MXF0WNd0e6gr+KAJ4fM0duwk3Dzt4uYd5JksFN30W0KvK32T1QGdRPeez57y" +
            "e8cNUwSlxDU8dUtK4Ya5+uq9gkjlCiQ/QhsTHYeRKgAfqRCqjO1cYAEBdgzsZadL6Rz1U93z4iDv8PCOi4ECQQDqwSexzwlWZabMSIC0IA+04bPmjwyMzq2wEJaBj0LRnUIYAU2TIHld6VWysDMHBAjvvmwLyRVJFixb10" +
            "iDkw2RAkEA1gUcJMwcByI0CcjsJzBlOfOxNkjx+8kDBeZPhGDqHgT+/sQ9vgEHb35gmevek1TqwpukX2GCVoU/pXdoRgp57QJAdhIEJvgcmCm0RNbKdM4TWESUuQeFpmRlE0KkkDE/yDGyBmTRYYhZwpQTGDZq08KBxCMgMKVIYWQXl3Gl9RdHIQJBAJO4lTNk2pel6Qsz7qQiCEyWwqN4d+XVWcvRquLxTccIpcTNSNyDs1EmhqDXKBrDSwKmES9wi1kSwdqA760ggAUCQFtYtm6IC4LBk85nT7ePQdwZNgxU7fYSHhjKV0W353qwvJWO3ftb1xXkmHGc/kG6fR0o9QBdpv6AP5O33DfbL+s=";

    /**
     * 生成签名，根据字段名ascii码，从小大到大
     * @param info
     * @return
     */
    public static String putPairsSequenceAndTogether(Map<String, String> info) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(info.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> arg0, Map.Entry<String, String> arg1) {
                return (arg0.getKey()).compareTo(arg1.getKey());
            }
        });
        String ret = "";
        for (Map.Entry<String, String> entry : infoIds) {
            ret += entry.getKey();
            ret += "=";
            ret += String.valueOf(entry.getValue());
            ret += "&";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }


    /**
     *  get private key
     *  @param key
     *  @return
     * @throws Exception
     * */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = java.util.Base64.decodeBase64(key.getBytes(ENCODING));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     *  get puhlic key
     *  @param key
     *  @return
     *  @throws Exception
     *  */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = java.util.Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     *  sign by private key
     *  @param content    signContent
     *  @return sign
     *  */
    public static String signByPrivateKey(String content) {
        try {
            PrivateKey priKey = getPrivateKey(PRIVATE_KEY);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(content.getBytes(ENCODING));
            byte[] signed = signature.sign();
            return new String(UrlBase64.encode(signed), ENCODING);
        } catch (Exception e) {
            throw new RuntimeException("sign error", e);
        }
    }

    public static boolean verifySignByPublicKey(String content, String sign) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(PUBLIC_KEY.getBytes(ENCODING));
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(ENCODING));
            return signature.verify(UrlBase64.decode(sign.getBytes(ENCODING)));
        } catch (Exception e) {
            throw new RuntimeException("verfify sign error", e);
        }
    }

    public static String encryptByPublicKey(String plainText, String publicKey) {
        try {
            PublicKey pubKey = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] enBytes = cipher.doFinal(plainText.getBytes(ENCODING));
            return new String(UrlBase64.encode(enBytes), ENCODING);
        } catch (Exception e) {
            throw new RuntimeException("encrypt error", e);
        }
    }


    public static String decryptByPrivateKey(String enStr, String privateKey) {
        try {
            PrivateKey priKey = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] deBytes = cipher.doFinal(UrlBase64.decode(enStr.getBytes(ENCODING)));
            return new String(deBytes);
        } catch (Exception e) {
            throw new RuntimeException("decrypt error", e);
        }
    }

    //bean 转化为map
    public static Map<String,Object> tranferBean2Map(Object obj) throws  Exception{
        //obj为空，结束方法
        if(obj==null)
            return null;
        Map<String, Object> map=new HashMap<String, Object>();
        /*Introspector 类为通过工具学习有关受目标 Java Bean 支持的属性、事件和方法的知识提供了一个标准方法。
         * java的自省机制
         * */
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] ps = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : ps) {
            String key = propertyDescriptor.getName();

            if(!"class".equals(key)){
                Method getter = propertyDescriptor.getReadMethod();
                Object value = getter.invoke(obj);
                map.put(key, value);
            }
        }
        return map;
    }


    public static void main (String[] args) throws Exception {
       // String sign = RSAignature.signByPrivateKey("createTime=1388487642&notifyUrl=http://oneboxhost/mock/ notify&orderDesc=QmonXLlbbD&outOrderId=48794928118907008081&partnerId=10000001" ,
       //         "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMMoZzPcaK7ie4yrapatAIVhD OFtUPy8rhhHMfn61YjcMm28Dz3a9oqnSHq6wjP0XxcUFRFuwGaDFPDv/L49cNQ+6bQ2PaNQvL 2LT86A6/R+aQM+u1ONbP8iGYJ63vpn6kk5+WY5TpyXsFzaDTjUyURrYjJRcryeEN7odGdVhOnZAg MBAAECgYAYrWA3Z5R5ILxcskQ9H00kkHwPeUI3Yyhke4QvRu9/adCanaATwz9Pkw2QL1NlPG5Vv b1YQffkPokEWmRMLfq5MznA00/CE+JXhp/SEi3w79s1K3ExUzI1OpGSvN08XeS68wpuiAJHnzslOl lQ8r2+/2oyo1UmzPEDlSY0rAqaTQJBAPBwWPfoeFdrrLRLp2Ey4nLLdYJw/s8mdQyiVjDqW/tkKzP 23tCgliTu5/9ClP3Qr41iRdHD6WJ4ctJ1d3JWUK8CQQDPydPvgoFD//4+fi9HgtoTZUyIqLZpNs1nZY Y7b+59yhWBIfWGP8DFhZrd/MyNpLfsoug4JOOqEe8nlG2EPj/3AkEApxMqf3oGxZiItfAsKxqUyHg g+7dRGNj8VP8pLWxs5k9AxicxxX8RVjC8/V9i8MxmcLRtF8ovDsHr59rAWa8o+QJBAJjPoDpjKrec mxjQaerYc5KSC+/wy32jHPoucsJhde4yYRA/rjYVyqo4sIUS9kgw3EZ+I/OuRXP8jnn4MXZw5U8CQQC0V3ugUJLBc/uLpXzDgLc1ISNRfPI61odmt3hqs421thIValXhVBXhHt/OkHhUVRIb4dDC85QPHX 3Q+RWma6cp");
      //  System.out.println(String.format("sign:%s", sign));
      //  System.out.println(RSAignature.verifySignByPublicKey("createTime=1388487642&notifyUrl=ht tp://oneboxhost/mock/notify&orderDesc=QmonXLlbbD&outOrderId=4879492811890700808 1&partnerId=10000001",
      //          sign, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDKGcz3Giu4nuMq2qWrQCFYQzhbVD8v K4YRzH5+tWI3DJtvA892vaKp0h6usIz9F8XFBURbsBmgxTw7/y+PXDUPum0Nj2jULy9i0/OgOv0f mkDPrtTjWz/IhmCet76Z+pJOflmOU6cl7Bc2g041MlEa2IyUXK8nhDe6HRnVYTp2QIDAQAB"));
        Map map = new HashMap();
       /* map.put("createTime","1388487642");
        map.put("notifyUrl","http://oneboxhost/mock/ notify");
        map.put("orderDesc","QmonXLlbbD");
        map.put("outOrderId","48794928118907008081");
        map.put("partnerId","10000001");*/

       /* OrderMipay orderMipay = new OrderMipay();
        orderMipay.setOutOrderId("aaa");
        orderMipay.setEmail("test");
        map = RSASignature.tranferBean2Map(orderMipay);
        System.out.println(map);


        String signUrl = RSASignature.putPairsSequenceAndTogether(map);*/
        String signUrl = "createTime=1388487642&email=ankit@xiaomi.com&firstname=Ankit&goodName=gas&orderAmount=9999&orderDesc=description&outOrderId=a3d002a8-6ce4-4c38-8563-3e806248&partnerId=21000002&phoneNo=9123456789&returnUrl=http://oneboxhost/mock/notify?orderId=a3d002a8-6ce4-4c38-8563-3e806248";
        String sign = RSASignature.signByPrivateKey(signUrl);
        System.out.println(sign);


        String content ="createTime=1388487642&email=ankit@xiaomi.com&firstname=Ankit&goodName=gas&orderAmount=9999&orderDesc=description&outOrderId=a3d002a8-6ce4-4c38-8563-3e806248&partnerId=21000002&phoneNo=9123456789&returnUrl=http://oneboxhost/mock/notify?orderId=a3d002a8-6ce4-4c38-8563-3e806248";
        String verifySign ="CGfG7aW0mPyhCV6o2vmMIkSSU_J9WiPEb0oErgLIWSniSA7W8gLWY4trBQhUMYUPiZfxJfG-HxCLER_LqWq3sy389KpLnTQfSCfyTNebBfX6q252a3lhLiU2uqYXOhM8h0BaKv2EwHhx7Mh50vvB80OgBo6cfbwiOQN6NWR4YD8.";
        System.out.println( RSASignature.verifySignByPublicKey(content,verifySign));
    }
}



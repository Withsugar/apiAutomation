package com.qa.util.crypt;

import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

//import org.testng.annotations.Test;

//import org.junit.Test;

/**
 * Created by yang peng on 2016/8/31.
 */
public class RsaUtil {


    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";


    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";


    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";


    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;


    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    private static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp7BCCDU4wkozPFvau143NvdGGAqWbOuY/eeJy" +
            "X6g0o/rUtK3T2XaZEpKGNQTU/RlyQ31FeRASPEOXRJrLYju0gIpEMAhNbvpwlSDwA+T/28LxuNo9" +
            "19+g1YEho9ER9fzVukOrUHRp9shb8BN+WIekrxh2Zs0YSZEYXI5OocN1JwIDAQAB";

    private static final String DEFAULT_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKnsEIINTjCSjM8W9q7Xjc290YYC" +
            "pZs65j954nJfqDSj+tS0rdPZdpkSkoY1BNT9GXJDfUV5EBI8Q5dEmstiO7SAikQwCE1u+nCVIPAD" +
            "5P/bwvG42j3X36DVgSGj0RH1/NW6Q6tQdGn2yFvwE35Yh6SvGHZmzRhJkRhcjk6hw3UnAgMBAAEC" +
            "gYBtHoGDYtQBcTsnihGkOhyAMROqQWzkALD3tmfrFu7Hzz429khisVXt+vVJZibSOC7tJ9X8lT/y" +
            "1ex56b6WOWEo+kqDECoq1DemXYIqTI8Wf2+iWn5w6Akg1mkwOHUnmXebAYz2MpIj+I34/N1cxJWT" +
            "Yg5E0Q8syysSktrUop2B0QJBAPyk8uusd4EHiojF2lLf3sPjN8uTvg51/I8e5E9KNhDw47nScjHA" +
            "vJav0r+MGTQ+s7/EFy5/SCTH3usejzzxYlMCQQCsLdce4Z1FqN4b+SD27VbTS7vDWReAKeBsnrX0" +
            "NCH9KeThVcqTA8jBfmOINDiqLVNVDEkhoJzgO9/Owx8RNK9dAkB3bXH3vjtvnyT5qq2d533ik5JY" +
            "haqeI2AUN8ItSSNttrGvsa+RnU6I3Ox+57cyX0GRMRzUOg7cdC8IbhW/9UtJAkAUU6EW4znG9IWs" +
            "NcAfhqt+bG1xRX5Rwh/dkusWNmVg1uZPd+pfas3zkfE8LjYWWR2CZ93mW2jJzCfC6CQtytN9AkAP" +
            "BOajQn5fBAiJEeXCAJrsCevr7DxQFtPVQwRtWmOWGf83AK7wlprPS7kSh5F5YTsc/7dENL+6yOoY" +
            "Uf6kpZlV";


    private static final String REQUEST_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIFtWC2Tk1ua4Uc3qzWeC4pn59A5" +
            "xFlY4Myk1FabfiSCBHtMkH4u3Pg7xu1Fphie68z90RvcNpVFu21Z8Ee8LWuD8tYzHWFwPf+ehyl7" +
            "3WjcWjFKiTsViddq/+WzpiCJRH/wqdfeNUUTFZ+x4CROg8TscVRkE2KQ1dQWjmgsTs1vAgMBAAEC" +
            "gYAZlBqRJ1FAEWPkR0Q23H07ZT2TEVeUsNP8nba5RxGu9WMdWwxTY7+X73ho/BLPWtkPMjpZXzQK" +
            "XDWmbtfl1XLEPOL6thdayFzrung8+mq/THxV7tV3MH+dsQf2JDSZulX9tgjOJsdOpGZavGwtAElH" +
            "j00wGGEkQ5BEq4eegAbOAQJBALiczKW1Igv8V1FML21DlTsBAb7yAGTw9DuznLo6aMqzXNtX4BoL" +
            "6ByrgsTCb91QO6Jyi5B7wYSSIL7VCkL0S2ECQQCzeZ9uX9mRAs1QePi9VNn4ZuUBzI43+Tg95EHG" +
            "xHcX6V5MsTub4/+Y3Y8xmsBfDR2YpXUR3jfNt3EDrSa73hrPAkBkt1aGiN08QLy6Y5/0Mky11gTH" +
            "0WsZpsL31zOH/dmxzEy6daSTUS+ehIthINq6mM0QAQHXH00iMD6hHcTtZi3BAkAiduOHxX45G4mj" +
            "v2c8rFOjINcvLkHNO91x/SgobEfWXqkOHyasO+80Qq+hve7dEd6yFmvRv5OFvIlGRHTgtLj/AkEA" +
            "owowCBMVifERUrpOrZtIPUKPmYZ/WsdEi1sCq+KGj6/9a5lan0J7mYdvhFmahdMYm1PIVuQEtF2g" +
            "6YWw+zvw5w==";


    private static final String REQUEST_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBbVgtk5NbmuFHN6s1nguKZ+fQOcRZWODMpNRW" +
            "m34kggR7TJB+Ltz4O8btRaYYnuvM/dEb3DaVRbttWfBHvC1rg/LWMx1hcD3/nocpe91o3FoxSok7" +
            "FYnXav/ls6YgiUR/8KnX3jVFExWfseAkToPE7HFUZBNikNXUFo5oLE7NbwIDAQAB";


    private static final String RESPONSE_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKWu0HB/CeX+Dy10VwWzH/QISjEZ" +
            "N/AfYp90y/+QHHCZr5h5K5VK5VBxzvgh7Wem6zHVU8QZ56K//V8Klk/6oVKl/A0OzyGpx3wl9D3g" +
            "nuKjAUe86FLgAOVASDa/OAqrQL0d5cWOmSIuHc5ZmVQpUPlbiGwdsJM/sBEm2RUhQl0ZAgMBAAEC" +
            "gYA8Dp4n6SThZbKCu4U/36pZfxfFrGqGdBn/ywqXXNmyR0NLdcDCoR92hYqMj1/LDsp6ieWPVASP" +
            "DiD97oyF+Ue4ZAZ+oIHzjcH1NceZD8d28YmhXnyg92Dtse0I4f5Hn7s/KM/unrc7grnOvzZXRpnx" +
            "FlmJfI1MhUaXDE+FNVbfcQJBAN64U1ym2/1lle7qlVpPY4MH7B2nVMy4ylGWYeCkZPKtetktMYBA" +
            "wHGmFUHS9CmHpgaLcc84w8jCTN154SuuA48CQQC+cKe7YCKs4iXBIxpJAntdLUSaUYHu4XX7d6ca" +
            "PjItngEByfSrLxs9FBd7StpHEZ2kRd6yZfQTc8bBZC/P3KDXAkAxWRQXbl1GCxEqi82l4ftBmCrH" +
            "80CFz9f8Nd7gAGzhnHCg2DOkoDRDujHxkMVKwmSWBKWl7YTr4alYVV3/6KGfAkA7+3u5NuR1E53U" +
            "oVvMFy4IARQUjwuf0/+3Ps6xI1nmqFek2plnuaSYrlVfDgqGS/QdI6yrABXKcrLtgsLUc6dtAkBr" +
            "OVTWS5R14/VMWf3GLCx15clKAfqMNmrje+4YwaetzW8au+pydzPhnejsInKaaDJkihznQzlKgCwC" +
            "cfCXw8xf";

    private static final String RESPONSE_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClrtBwfwnl/g8tdFcFsx/0CEoxGTfwH2KfdMv/" +
            "kBxwma+YeSuVSuVQcc74Ie1npusx1VPEGeeiv/1fCpZP+qFSpfwNDs8hqcd8JfQ94J7iowFHvOhS" +
            "4ADlQEg2vzgKq0C9HeXFjpkiLh3OWZlUKVD5W4hsHbCTP7ARJtkVIUJdGQIDAQAB";

    private static final String ADD_GOLD_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK4E3WcH1xMWBlZi9Vw1Ctw2ab7E" +
            "wsYALoUAOd9gvT0iwdbhxf1vH+LybISMDnTS+cAU8g4yeM6Jhys/g1qR9dLTDOI98tc8CKguZNOz" +
            "0hQTbp1sCnXDo9bthJiqQu/F+toJoAmRy5VXKZPk8Vw+jR4wgdx5Lcfvkzb9plhGAl8LAgMBAAEC" +
            "gYEArO+Gu4JOGdiUebskTPsljMzCb+QIEE9T67bazIPN7HijHBII+aA6lwYGE/8/ypdzvZDTMIq2" +
            "BcMkwjT/K73MBYuhGnBvtCl+Bj0YAYwaRIHDsidRsJidOmuY3skNT3BWrVpDC3eem+guSwy4Y63R" +
            "VwzsN2FZY+O/yWuW6pcKNaECQQD44ojPv++QGcmN7ytCK4lvtCHMDyLMbTZSTq1PyAycHITqXn+n" +
            "4P03iHmGZuXCqPi5sKIhoXza7AVgHqrMd6jbAkEAsv5shwU5cr/FA58Qoi/kZWUuFrI30sHX50yZ" +
            "BquMCLJ6thPsqkBM2y0Sq0kJELnAMYzWTvHYSwTzgWih8D6hkQJADegqmOEys6ydNI8era3dGh7I" +
            "nKxj12Rq2qKG0KRu//IgAzCQlYLe41F+6OGrCPuEGmwiQxrQ7TKvAjX0eURebQJAT949GOZY4v30" +
            "8A+7AVOzaxcBwLjXNeCu8RN5uMM1IfBZ1wL++FzmrmnnSay2MdNgkfuQ6Pr7E0ZrTVQd5jBcMQJA" +
            "Msh1CAk3qbJXmWWSCQvjiEfzGmaTUtP0q6iO06eciW+VmRFaepnHcDLDbiHO3wbhFQdBnniTjkXM" +
            "lIvcrMNXjA==";
    private static final String ADD_GOLD_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuBN1nB9cTFgZWYvVcNQrcNmm+xMLGAC6FADnf" +
            "YL09IsHW4cX9bx/i8myEjA500vnAFPIOMnjOiYcrP4NakfXS0wziPfLXPAioLmTTs9IUE26dbAp1" +
            "w6PW7YSYqkLvxfraCaAJkcuVVymT5PFcPo0eMIHceS3H75M2/aZYRgJfCwIDAQAB";


    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return Map
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }


    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return String
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }


    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return boolean
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * 用于数据过长时的加密解密
     *
     * @param cipher        Cipher
     * @param data          byte[]
     * @param crypt_max_len int
     * @return byte[]
     * @throws IOException Exception
     */
    private static byte[] dataSplit(Cipher cipher, byte[] data, int crypt_max_len) throws IOException, BadPaddingException, IllegalBlockSizeException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] cache;
        for (int j = 0; j < inputLen; j += crypt_max_len) {

            if (inputLen > crypt_max_len)
                cache = cipher.doFinal(data, j, crypt_max_len);
            else
                cache = cipher.doFinal(data, j, inputLen);
            out.write(cache, 0, cache.length);

        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;

    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return byte[]
     */
    private static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, privateK);
        return dataSplit(cipher, encryptedData, MAX_DECRYPT_BLOCK);
    }


    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return byte[]
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        return dataSplit(cipher, encryptedData, MAX_DECRYPT_BLOCK);
    }


    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     */
    private static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return dataSplit(cipher, data, MAX_DECRYPT_BLOCK);
    }


    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
//        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        return dataSplit(cipher, data, MAX_ENCRYPT_BLOCK);
    }


    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }


    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }


    /**
     * 解密
     */
    public static String decrypt(String str) throws Exception {
        byte[] decode = Base64Utils.decode(str);
        byte[] bytes = decryptByPrivateKey(decode, DEFAULT_PRIVATE_KEY);
        return new String(bytes);
    }


    /**
     * 加密
     */
    public static String encrypt(String str) throws Exception {
        byte[] bytes = str.getBytes();
        byte[] strBytes = encryptByPublicKey(bytes, DEFAULT_PUBLIC_KEY);
        return Base64Utils.encode(strBytes);
    }

    /**
     * 添加金币加密
     */
    private static String addGoldEncrypt(String str) throws Exception {
        byte[] bytes = str.getBytes();
        byte[] strBytes = encryptByPublicKey(bytes, ADD_GOLD_PUBLIC_KEY);
        return Base64Utils.encode(strBytes);
    }

    /**
     * 添加金币解密
     */
    private static String addGoldDecrypt(String str) throws Exception {
        byte[] decode = Base64Utils.decode(str);
        byte[] bytes = decryptByPrivateKey(decode, ADD_GOLD_PRIVATE_KEY);
        return new String(bytes);
    }


    static String encryptRequest(String str) throws Exception {
        byte[] bytes = str.getBytes();
        byte[] strBytes = encryptByPublicKey(bytes, REQUEST_PUBLIC_KEY);
        return Base64Utils.encode(strBytes);
    }


    static String encryptResponse(String str) throws Exception {
        byte[] bytes = str.getBytes();
        byte[] strBytes = encryptByPublicKey(bytes, RESPONSE_PUBLIC_KEY);
        return Base64Utils.encode(strBytes);
    }


    static String decryptRequest(String str) throws Exception {
        byte[] decode = Base64Utils.decode(str);
        byte[] bytes = decryptByPrivateKey(decode, REQUEST_PRIVATE_KEY);
        return new String(bytes);
    }

    static String decryptResponse(String str) throws Exception {
        byte[] decode = Base64Utils.decode(str);
        byte[] bytes = decryptByPrivateKey(decode, RESPONSE_PRIVATE_KEY);
        return new String(bytes);
    }


//    @Test
    public void createKeyPair() throws Exception {
        String str = addGoldEncrypt("药耀源;" + System.currentTimeMillis());
        System.out.println(str);
        System.out.println(addGoldDecrypt(str));
    }

    public static void main(String[] args) throws Exception {
//        Map<String, Object> stringObjectMap = genKeyPair();
//        String privateKey = getPrivateKey(stringObjectMap);
//        String publicKey = getPublicKey(stringObjectMap);
//        System.out.println(privateKey);
//        System.out.println(publicKey);

        // String str = "{\"data\":\"45vWpeZUQAr0ITPCNjkHU63xqwBSKJU+FGuacAsW/Jd9/yWr10wzqyi7SUPltctLdXnBPAZxc/gf\\nZlP/4AxH0OxBsGcgPBE322+Qq/FbNH+8s1GWngu6tov2U3T6qj9lrvGiyGroUh5GNCsp/pfZUxEX\\nzLOGhjGn3z9JB6JtcqnRkpo2wPNCtIkz7JZI+nUp7vpqdltMNFDmKiX96t5sYXz27iQaH1DEy9/4\\n6vlAcMdZ5GSuAaCGLUcBflhjFYcj1Jf8CrWOeESOFTi3UYx7FCcy3DysIqe090oje4Jprsr++G1T\\noqIPye4/Tps3vxJWMIhEq9Pjfli4Xmr6XrB4yRfbXPliGyW9X/Au8ots/jzX/HR+y53aKgFfonbl\\nGn6mWsL4M6eZ+E8YzasGXUU3MMFUNJc59cLBUfhXpzDWO6vln1nZjTFRu1lQaQkbg2dC/Iq2vFxA\\nXG6gGmbqw1qFPxclhb2cO4TJZwxbeXAFZvU5exBti1QMwPlv78r1vvUeZ25xBq3pjg0UsrtPMJTh\\nFQgHjc1XgzFoIp3qX8hJFVX1ew76tt7PQaFT1/z56eBuIp3qX8hJFVVDgLOA608HfVBl1Q4RP0IK\\nqD7//5O0+fyZ5SZyRYhMRnbUP+JLG984JYf2H3RVMBn6ZKavEHgz5Bclhb2cO4TJTM7OfhPAP0iY\\n3Y3wDzx+C6qGXYMpzbJlpoHZyfY2Fq3OfrZaRuOcPv4jl2c5kGUhH2ZT/+AMR9DyfnCji3fQzTAg\\nQ39avxy8cE6NHjQEg+HeV20Oiou1/4JBdAHMXVff3eR1BqAGH5zvyOfQpHfoQw==\",\"key\":\"nocJ9ox1w3i+q22U+G/YefD0kjPc9T8WK00ztTNk6tjxFq7tPvcG559qDZDXR5nmD3uojdGmAn2n\\nnh/0wzPAmarFw5Y9oJor7UxdxX4SiixgESSfwLrbWaKDAu5VJ2yXZZHI9rxdex2FH6iODWLhdFM7\\nG0BfxZY50GaH/E1VMK0=\"}";
        String str = "{\n" +
                "\t\"data\": \"LS\\/5+YoHuRtocpnDTWv02F1IYPjFgDD0AAaTd0ozGyTrtoQ8xozBR\\/6yJgjuGs\\/6WWRM+NDvxZQXb4cZflUvCNXJGxRZrAJDzRFnh8y82B6zBtLLm75K6t9OPpRssmtTgLz0MlLX8sSgMfj+e8mfOT5qtmUKNc8wZ3GfTQKisDSigZNffcEauwkO2awaBTxOVHbUHHqBkyPoHTmCsn66PJrL3bt32sCRCbpWLU3BGZI\\/GYMDl40oXyrKJxtcYlNT9YFNZIDBkr3Cvgpl+52BTPr9uAPuC6lYzfTBkUNt9O0xwwcoWCTym9z0tw8liZOaB0bD+wWbO9pV1I4rsmAqTijALmQt8Y1Ked9JpyIhynX9OANit\\/TJzgughBl1GVUYnXNMaSgThJ27szaRNimTkyIS+i2kQ6x07rCAJZuhGsLC9UHdycfyShYfRRN6Dz7p6mtoFurtebw=\",\n" +
                "\t\"key\": \"e\\/JG+lYlwX0DA86phSfISUoCNs1rARj4+mdhFeNkEmSodG9+jWYSvZeV4UxktDcViP5aL3hSJeNN\\n1QLjMhBSWyQihISRl+uyBIlRSdyBZM1aMwodbDFM4cg7Ej9tKzWkM0nGzPiOWjVQtY3KuCFwnWP8\\nIIbt680jlzhOkOq+DKE=\"\n" +
                "}";

        JSONObject jsonObject = new JSONObject(str);
//        JSONObject jsonObject = JSONObject.parseObject(str);
        String key = jsonObject.getString("key");
        byte[] decode = Base64Utils.decode(key);
        byte[] bytes = decryptByPrivateKey(decode, REQUEST_PRIVATE_KEY);
        String data = DesUtil.decrypt(jsonObject.getString("data"), new String(bytes));
        System.out.println(data);
//        String decrypt = decrypt("hvzFxk/jwrvElx3+ELAuPF/hIEOJ/1WlVfz8u2LyCS1rNqEB3jrKqP6HRIX4CphS/rAawf7wewIQ\n" +
//                "WMNog10UkDP4ciQC9okHOqe+cqIltyL6KP0Glb+qRfOBxbW4SXz/E/FIEYm4l5gJPqfDznIh3l3y\n" +
//                "2gLzFAjbS3z7X/jASOk=");
//        System.out.println(decrypt);
    }

}

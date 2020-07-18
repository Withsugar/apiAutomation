package com.qa.util.dlocal;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * dlocal 签名生成
 * Creaed by fj on 2019/6/18
 */
public class DlocalSignatureUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String CHARSET = "UTF-8";

    public static String calculateSignature(String x_Login, String x_Date, String secretKey, String body)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        // Create byte array with the required data for the signature.
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bout.write(x_Login.getBytes(CHARSET));
        bout.write(x_Date.getBytes(CHARSET));
        bout.write(body.getBytes(CHARSET));

        // Calculate the signature.
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(signingKey);
        byte[] signature = mac.doFinal(bout.toByteArray());

        // Create a String with the signature value.
        Formatter formatter = new Formatter();
        for (byte b : signature) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}

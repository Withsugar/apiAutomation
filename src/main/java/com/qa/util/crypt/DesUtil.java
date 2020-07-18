package com.qa.util.crypt;

import org.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Created by ZN_mager on 16/8/1.
 */
public class DesUtil {

    private static final String PASS = "rc2016!@";

    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    private static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     */
    private static byte[] decrypt(byte[] src, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
        return cipher.doFinal(src);
    }

    /**
     * <p>
     * Des加密str，然后base64转换
     *
     * @param str String
     * @return String
     */
    public static String encrypt(String str) {
        try {
            byte[] byteMing = str.getBytes(StandardCharsets.UTF_8);
            byte[] byteMi = encrypt(byteMing, PASS);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            assert byteMi != null;
            return base64Encoder.encode(byteMi);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String encrypt(String str, String password) {
        try {
            byte[] byteMing = str.getBytes(StandardCharsets.UTF_8);
            byte[] byteMi = encrypt(byteMing, password);
            BASE64Encoder base64Encoder = new BASE64Encoder();
            assert byteMi != null;
            return base64Encoder.encode(byteMi);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * Base64转换str,然后进行des解密
     *
     * @return String
     */
    public static String decrypt(String str) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] byteMing = decoder.decodeBuffer(str);
            byteMing = decrypt(byteMing, PASS);
            return new String(byteMing);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String decrypt(String str, String password) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] byteMing = decoder.decodeBuffer(str);
            byteMing = decrypt(byteMing, password);
            return new String(byteMing);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception {
        // String data ="S8aKjwzrNqUDVwBZl04F3s5E5VBvlELYbSneDB9Xv1jeQq9ZALbebYRrGwPQuuke3rGu8ZBPSGLs\\nBE83Sx9Ab/h51F65J2hHnXysMNw3Be2cCA7BuS3xFt6Kr3VTbYcpmTGfkae9RdLAU2AnaYO6Gd/X\\nS44/ofLD8zinGpb9fQdwthS6UT+Twxivp6tFpsNOIB5dxvUeBXawGROeEmT9J1Z6GuJ3FfVTyPN+\\n1HhtOtE+8VnFmS4otV5xh96Sm9ugR2XhCv8OHCEr/TyJAQXid84zBGfHg8S5AY/Jxw4EXi+R98tV\\njwh7GuHQ3NjfHlzXJDGfI0+nNDJ4oXJYYWnZiq8h8Xcp5QkfRvP2ksKQODsKQ/h2pju9ZMI9xw5J\\nRLKHPy2ulARDZ6SFUKwCjInncW/PSTxwjmm1FkgicGpC1JbCIf4NawwTWeWDoySedFT7KtE1wBWs\\nnIQIijHd16TgPHjAB445m5XUFMS93nWFEdgvFFNuFnjBLFfQ0ntepygG9PPaqoDFNWcd3nXk/ws9\\nFfxZ6RjsTl4d9dg3+Sg351P4edReuSdoR4JtXJWMrembl7IInRUyG/s5rCandIjZsULVOHRhSEmq\\nVCef6+RjTXSmA8MJ3fJTEChR+uCAfBP+0b/cUmMBKm0HuKSsDwtKz4hV2aT0SbR+PQ2rTz01lAE/\\nqPhmF8EfN48Hj1SXQTy8K90eyWlYCwmPgMwVxcSdD/BLTaxBsVRm4dDc2N8eXNc18ttjp0cch3ih\\nclhhadmKi0+JBF46JSvo5szbKvcFVwpD+HamO71kwj3HDklEsoc/La6UBENnpGtjtWUaLY3RPi4z\\nsMMcUYN1JL8Fqy90vMIh/g1rDBNZ5YOjJJ50VPsq0TXAFaychAiKMd3XpOA8eMAHjjmbldQUxL3e\\ndYUR2C8UU24WeMEs/RvK48UrwTr089qqgMU1Zx3edeT/Cz0VBoTQ24QHD4H12Df5KDfnU/h51F65\\nJ2hHgm1clYyt6ZuhdMFibbhwlzmsJqd0iNmxQtU4dGFISapUJ5/r5GNNdKYDwwnd8lMQd7XcxkpO\\nCtHRv9xSYwEqbQe4pKwPC0rPiFXZpPRJtH49DatPPTWUAXfjwDWwlYgoK/08iQEF4nfNJb4aWpNe\\nuAGPyccOBF4vngwnYJuBEfXUD3opKjykOMk4gMwWZHeH40Zqy7as9qpvvgQ6ngobuktaGalYLv8M\\no0N4jTGXIcwC1QItzOgS+gcDpSjn+76ZIkl35qFC5BWQIH7XZPuG9eTqOHDQWYiGMvvSEHpuB76J\\nyZnyFR+cGeQ6Rn83MEaZ4TpCpxdjvm1Es+Z1+5nLiVjSQCJzIl8VQWNW6vAmCjdCB3RjbUNh010Y\\nDztkTj4BnX8Z9aXIUVCalVvaVn0lpAPpzhGYMiYUd3rHqUA/v3/Wo1DBNPloqkdCvzbr9rxdI+bj\\nXIduzT7yk+5LlZreBrUt35gNxO0dgzZSciHi9vMFpNd7nvBZR9/XS44/ofLDDXo5zdXKdDIoAXzn\\ngFpgt0ReMlBn9LmO6RVYDojPli0sFOQnLcraio4s4I28R8qULkT7hF0v5+yVzZ/REchV8sEYtTeE\\nvYupTyTC46B/Lx5H7VApOLqtks47UWgFnEpyNQxcmQc5FlAiAC3wAN8aHhHZzkfy/Qdhq8wACcnz\\n+aJ4RMEyKGGghiHF3VKinHthi1UU3Piq6ffw3+EfyypwEII29CzMQbtCkKbtItNjPzyxeZ2L0yfK\\nXXjoJJtcdfFxpzQphKnTTiFeml0DOhohP3z34tRAHuatpfOOQymeRLcJkxbupPzAg7D0IkbTd2XK\\nzo1iSvrqfziud6Q8HaYKHgmTFu6k/MCDCODVobOI+oiCWGKLQxEgTp03ci360TndcStg2dHHpqyX\\nAfWZUq0u+YPQOMiR6v1e2L7uqK1sijjgbhourSmj/vQ9EpiIt9M9QvsmXaD/b/ROqN5bqsXx6Xac\\n/509tNpuY1RF9qzUTX0r3R7JaVgLCY+AzBXFxJ0PntyRQMh/iVDh0NzY3x5c15DY8dBDC0NKeKFy\\nWGFp2YoUU9cUv781B7nCG0zSiZr1o0N4jTGXIcwC1QItzOgS+gcDpSjn+76ZIkl35qFC5BWQ2ECL\\nbEcRplP94iq3faL82OtWQE7tCXa3sPsC/2m3sMKSqX/dLV9C9Rfuzek06OHzz5cMJbaVNQX758RB\\n4BDtsV+PtVto9Ssq91ktxU6ed6DfDBx+k5TA8Z/3fMduz6OE7bYBWSmTvZYO8xxagGySF7AZC6II\\ne5nxn/d8x27Po87OdNcDHxddv8K5H4fffhF4x2zJeFiJdL22fJU37D+n1tFNLHWPO5s3opN1Ts7p\\nTiAX6/3HbtNIeR97Yt/iLgt/38Bg54ijgOhON8jqxDlvrtHqNlqqtbDN55gZL98fWPzFw8S8gZ6f\\nK90eyWlYCwmPgMwVxcSdD9+GJq9PKFOb4dDc2N8eXNeBNb0SOn5lSXihclhhadmK/TNPFACpYIp5\\n1yRrBTibf6NDeI0xlyHMAtUCLczoEvoHA6Uo5/u+mSJJd+ahQuQVkB/OATGwbd5T/eIqt32i/Njr\\nVkBO7Ql2t7D7Av9pt7DCkql/3S1fQlJAIJ6eYNbE88+XDCW2lTUF++fEQeAQ7bFfj7VbaPUrinmq\\nmF5xcLSuJGT7T/FAXwmTFu6k/MCDJxLnCRXGl6oS/h5eLoPTaS3cz4aVwnsev8K5H4fffhFj5OM7\\njQc6Md/XS44/ofLD8zinGpb9fQdwthS6UT+Twxivp6tFpsNOIB5dxvUeBXbMSAewI4EaGlZ6GuJ3\\nFfVTyPN+1HhtOtE+8VnFmS4otV5xh96Sm9ug0kAwbxA1tSwsozEPI8QAzjLE4vZXZAms4vfuKe5e\\n7rweVPlSA7qo5sEYtTeEvYup2J5XUdSms1JH7VApOLqtks47UWgFnEpycTwRhZXOViqqwiixZMUe\\nfD+Op/iVPsKrw3PKBt/lVB1uXYl1e8UhPhTHRbvoejElG3Ol/jov5yPatPqYkgaK4Uj1gYRh0oyg\\nRsDhV0GsEXnWPEPM9s9LIugdGuRSHQlFmTGfkae9RdKwUDCWDwPSUBi+u9NnfBYwF+AmRfRongP0\\n89qqgMU1Zx3edeT/Cz0Vfu5faon6vAWvn0yxXsKZjV0s4e6WIt1g87ylWiCr81oS/K/Hyvtc7wmT\\nFu6k/MCD9npn8m4AwfujNt4fr0XxWTKEtgKx+GaHONVF7jpJLl+OBzcsxGXNer9oW1M4l04dlYL+\\nvPHkmM59FgxpVZcdMVN8BfA8ETpAG74bfeg09avJb226RrQ3e9FfY4yODOe1GdI4hg/cb7rch+E0\\nVorAouHQ3NjfHlzXqO+QcWLLinZ4oXJYYWnZih8nebT/mhv5Pq7XQHsZzFWjQ3iNMZchzALVAi3M\\n6BL6BwOlKOf7vpkiSXfmoULkFRUp296gTEZWi1UU3Piq6fcHv1k3ZjmV1A==";
        String data = "{\"data\":\"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\\n9PxdavUSbQ2y8sfW\",\"key\":\"VfSGfKHDrkgu884LBlzqMTwYsNnAo4mrJgSEuQ9iF1jgkCZk+xRRs+LC6LxNbc6+LNlgS4nWKH87\\nFSS+qngAB0jMPW7ATDij+NcQ5RJhEiWqhdtT0avcPBnOWbeIvWfTx8oCa8AO005ueDCLdgCnmQct\\npybJKIS5YGYJZmtvJ2o=\"}";
        JSONObject jb = new JSONObject(data);

        String key = RsaUtil.decryptResponse(jb.getString("key"));
        String res = decrypt(jb.getString("data"), key);
        System.out.println(res);
    }
}

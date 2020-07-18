package com.qa.util.crypt;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cloudsinn
 */
public class CryptUtil {

    private static Map<String, String> json64decode(String response) {
        Map<String, String> map = new HashMap<String, String>();
        JSONObject js = new JSONObject();
        String key = js.getString("key");
        String data = js.getString("data");
        map.put("data", data);
        map.put("key", key);
        return map;
    }

    public static String encryptRequest(String request) throws Exception {
        String des_key = RandomStringUtils.randomAlphanumeric(8);
        String data = DesUtil.encrypt(request, des_key);
        String key = RsaUtil.encryptRequest(des_key);
        JSONObject js = new JSONObject();
        js.put("data", data);
        js.put("key", key);
        return js.toString();
    }

    public static String decryptResponse(String enc_response) throws Exception {
        Map<String, String> map = json64decode(enc_response);
        String des_key = RsaUtil.decryptResponse(map.get("key"));
        return DesUtil.decrypt(map.get("data"), des_key);
    }

    public static String decryptRequest(String enc_request) throws Exception {
        Map<String, String> map = json64decode(enc_request);
        String des_key = RsaUtil.decryptRequest(map.get("key"));
        return DesUtil.decrypt(map.get("data"), des_key);
    }

    public static void main(String[] args) throws Exception {
        String request = "{\n" +
                "\t\"data\": \"UHhlZfB5HOrrE3t8iNMZ\\/Vsu4l9sswnrzcJIhNQA7Dk2UzG+1j0rSxzSh8XgCAs3XQahVxoA6wPd3inXbywl9eW2mWBQbhGohT6BdMWiYbppkY1qFNwuxXgkVZtfJsqVafENO0kitfH3niFHHJIKz4eewhCj8iO0N3dDO4RYh3\\/xzO\\/nGycH5j3gS3UjHQl6NEbazQe6GO\\/mp+a\\/IsG9IWMJTxnujNwqTjzw7No7z93Rzn3dmhuHkgq9nhw6YywjqgBG2iiSeDE=\",\n" +
                "\t\"key\": \"bx0mDBgz+VqJk8h47YgunDJKJ1OKeGbGtunmNO89PDwxuoeYnOur0osAQHi5zU9Lv4s8kSLZ3dl6\\ndYwB247u2OZrpdKQXDcLx5m\\/tkOX5\\/ssSR5fKoquQaLZwxBbEyTEvbb5+WiCm7O2muzPABX60Bj1\\nFhp7I\\/78n2KrjpSSClI=\"\n" +
                "}";
        String response = "{" +
                "\"data\": \"psvpWtnr7pzfeHTBFH1rYr/XH/kotBiU5XzIfWWRLr49c5aYB6UcvctyxuBFPw/c7kzvnzG3IFGy\\nsM6fua9YhAw87rv/tdxQabLReKyrtwepiUa/u6brSRAVoYPWZhKpBRg2Atzfudo=\"," +
                "\"key\": \"CBS1BdR+BFe1k37xTSOwj1tyTg5BxaVkttjIpvM9wjhNbnP9PXJ8yuXbnhaXTCuX8q0UAxyvALb6\\nWRV3FmGvQbz5MV79HryVq6X8Wi4JI7y0XsVbQmYVetVSizelWdiYuJNXkXcdNIlK85qbTgSYhgE4\\nQ0wMmYyFULpXJVO8c9E=\"" +
                "}";

        System.out.println(decryptRequest(request));
//        System.out.println(decryptResponse(response));
    }

}

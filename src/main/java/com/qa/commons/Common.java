package com.qa.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.restassured.response.Response;
import org.testng.Assert;
import util.crypt.CryptUtil;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.rc.qa.commons.AppConfig.appId;
import static com.rc.qa.commons.Config.deviceId;
import static com.rc.qa.commons.RcHttpUtil.simpleOkPost;
import static util.crypt.CryptUtil.encryptRequest;

public class Common {

    public static String getJson_value(String result, String key) {
        return JSON.parseObject(result).getString(key);
    }

    /**
     * 随机获取json key value
     *
     * @param result 转化为String后的response json体
     * @return 返回随机获取到的value
     */
    public static String getJson_value(String result) {
        JSONObject js = JSON.parseObject(result);
        Set<String> set = js.keySet();
        List<String> list = new ArrayList<>(set);
        int rand = new Random().nextInt(js.size());
        return js.getString(list.get(rand));
    }

    public static int getJson_size(String result) {
        return result.equals("") ? 0 : JSON.parseObject(result).size();
    }

    public static int getJsonArray_size(String result) {
        return result.equals("") ? 0 : JSON.parseArray(result).size();
    }

    /**
     * 随机获取jsonArray内某个index对应的value
     *
     * @param result String
     * @return String
     */
    public static String getJsonArray_value(String result) {
        JSONArray js = JSON.parseArray(result);
        int len = js.size();
        return js.getString(new Random().nextInt(len));
    }

    /**
     * 获取jsonArray内某个下标对应的值
     * @param result String
     * @param index 下标
     * @return
     */
    public static String getJsonArray_value(String result, int index) {
        return JSON.parseArray(result).getString(index);
    }

    /**
     * 随机查询json数组内jsonObject的某个key的value值
     *
     * @param result String
     * @param key    待查询的key
     * @return 返回key查询到的value
     */
    public static String getJsonArray_childValue(String result, String key) {
        JSONArray js = JSON.parseArray(result);
        int len = js.size();
        JSONObject jst;
        jst = (JSONObject) js.get(new Random().nextInt(len));
        return jst.getString(key);
    }

    private static MapUtil UserInfoMapUtil = new MapUtil();

    public static void userInfoMap(String userId, String token) {
        UserInfoMapUtil.put(userId, new MapUtil().put("userId", userId).put("accessToken", token).map);
    }

    static Map<String, Object> getUserInfoById(String userId) {
        JSONObject js = (JSONObject) JSON.toJSON(UserInfoMapUtil.map.get(userId));
        return new MapUtil().put("userId", js.get("userId")).put("accessToken", js.get("accessToken")).map;
    }

    static Map<String, Object> getTokenById(String userId) {
        JSONObject js = (JSONObject) JSON.toJSON(UserInfoMapUtil.map.get(userId));
        return new MapUtil().put("accessToken", js.get("accessToken")).map;
    }

    static MapUtil map5ParamsUtil(String userId) {
        return new MapUtil().put("appId", appId).put("deviceId", deviceId).put("nonce", "nonce")
                .put("timestamp", "123").put("userId", userId);
    }

    static MapUtil map4BodyUtil() {
        return new MapUtil().put("appId", appId).put("deviceId", deviceId).put("deviceType", 1).put("platformType", 2);
    }

    /**
     * @param bodyMap 把body装进map
     * @return 返回加密后的body体
     * @throws Exception 加密异常
     */
    public static String encryptBody(Map<String, Object> bodyMap) throws Exception {
        return encryptRequest(JSON.toJSONString(bodyMap));
    }

    public static String decResponse(Response response, int expectCode) throws Exception {
        String result = response.body().asString();
        int actualCode = response.getStatusCode();
        return response(actualCode, result, expectCode);
    }

    public static String decResponse2(okhttp3.Response response, int expectCode) throws Exception {
        String result = Objects.requireNonNull(response.body()).string();
        int actualCode = response.code();
        return response(actualCode, result, expectCode);
    }

    public static String response(int actualCode, String result, int expectCode) throws Exception {
        if (result.equals("")) {
            Assert.assertEquals(actualCode, expectCode);
            return "";
        } else {
            switch (actualCode) {
                case 404:
                case 502:
                    fangTang(RcHttpUtil.url, result);
                    System.out.println(result);
                    Assert.assertEquals(actualCode, expectCode);
                    return null;
                case 400:
                case 401:
                case 403:
                case 500:
                    String res = CryptUtil.decryptResponse(result);
                    fangTang(RcHttpUtil.url, res);
                    System.out.println(res);
                    Assert.assertEquals(actualCode, expectCode);
                    return null;
            }
            String res = CryptUtil.decryptResponse(result);
            if (actualCode != expectCode)
                fangTang(RcHttpUtil.url, res);
            System.out.println(res);
            Assert.assertEquals(actualCode, expectCode);
            return res;
        }
    }

    public static String keyAssertTrue(String actualJSON, String expectKey) {
        JSONObject js = JSON.parseObject(actualJSON);
        String ident = null;
        if (js.containsKey(expectKey))
            System.out.println("======= ident: " + (ident = (String) js.get(expectKey)));
        return ident;
    }

    public static void lostKeyAssert(String actualJson, List<String> expectList) {
        List<String> keys = new ArrayList<>();
        JSONObject js = JSON.parseObject(actualJson);
        for (String key : expectList) {
            if (!js.containsKey(key)) {
                keys.add(key);
            }
        }
        if (!keys.isEmpty())
            Assert.fail("Lost key: " + keys);
    }

    public static void getResponseKeySet(String response) {
        JSONObject js = JSON.parseObject(response);
        js.keySet().forEach(s -> System.out.print("\"" + s + "\","));
    }

    /**
     * 方糖报警推送
     */
    public static void fangTang(String title, String content) throws Exception {
        String key = "SCU43857T8f41f4a1187a3aec2949b70cf0fb48c55c5322402ba4e";
        String url = "https://sc.ftqq.com/" + key + ".send";
        String body = "text=" + title + "&desp=" + content;
        String response = simpleOkPost(url, Config.header2, body);
        System.out.println(response);
    }

    /**
     * 生成随机不重复字符串
     */
    public String UUIDString() {
        return UUID.randomUUID().toString();
    }

    /**
     * 13位时间戳 转日期
     * @return String
     */
    public String SimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis() - 8 * 3600 * 1000);
    }
}

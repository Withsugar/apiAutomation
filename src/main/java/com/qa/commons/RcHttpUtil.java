package com.qa.commons;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.Map;
import java.util.Objects;

import static com.rc.qa.commons.AppConfig.newLiveChatHost;
import static com.rc.qa.commons.AppConfig.version;
import static com.rc.qa.commons.Common.decResponse;
import static com.rc.qa.commons.Common.decResponse2;
import static io.restassured.RestAssured.given;

/**
 * @create: 2019-11-16 14:50
 * @author: cloudsinn
 * @description: Http各类型请求聚合
 **/
public class RcHttpUtil {
    protected static String url = null;
    static final String EXPECT_CODE = "expect_code";

    public interface multiClass {
        void multiPartMethod(RequestSpecification rs);
    }

    static {
        System.out.println("当前运行的version是：" + version);
        System.out.println("当前使用的host是：" + newLiveChatHost);
    }

    private static void putParams(Map<String, Object> replaceMap) {
        replaceMap.put("nonce", "4dbb7311-848f-45ed-b9c1-78383891e1fc");
        replaceMap.put("timestamp", System.currentTimeMillis());
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 创建一个不验证证书链的信任管理器
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // 安装信任所有的信任管理器
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // 与我们所有信任的管理器一起创建ssl套接字工厂
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static OkHttpClient client = getUnsafeOkHttpClient();

    private static void println(String uri) {
        System.out.println(uri);
    }

    static String httpGet(String host, String path, Map<String, Object> params, Map<String, Object> replaceMap) throws Exception {
        putParams(replaceMap);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        println(url);
        Response response = given()
                .relaxedHTTPSValidation()
                .get(url);
        return decResponse(response, (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }

    static String okGet(String url, Map<String, Object> replaceMap) throws Exception {
        putParams(replaceMap);
        println(url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return decResponse2(response, (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }

    static String simpleGet(String url) {
        println(url);
        Response response = given()
                .header("Content-type", Config.header1)
                .relaxedHTTPSValidation()
                .get(url);
        return response.body().asString();
    }

    public static String okPost(String host, String path, Map<String, Object> params, String body, Map<String, Object> replaceMap) throws Exception {
        final MediaType header = MediaType.get(Config.header1);
        putParams(replaceMap);
        println(url);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        RequestBody Body = RequestBody.create(body, header);
        Request request = new Request.Builder()
                .url(url)
                .post(Body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return decResponse2(response, (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }

    static String httpPost(String host, String path, Map<String, Object> params, String body, Map<String, Object> replaceMap) throws Exception {
        putParams(replaceMap);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        println(url);
        Response response = given()
                .header("Content-type", Config.header1)
                .relaxedHTTPSValidation()
                .body(body)
                .post(url);
        return decResponse(response, (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }

    static String simplePost(String url, String body) {
        println(url);
        Response response = given()
                .body(body)
                .post(url);
        System.out.println(response.getStatusCode());
        return response.body().asString();
    }

    static String simpleMultiPart(String url, multiClass mc) {
        println(url);
        RequestSpecification rs = given()
                .relaxedHTTPSValidation()
                .contentType("multipart/form-data");
        mc.multiPartMethod(rs);
        Response response = rs.post(url);
        System.out.println(response.getStatusCode());
        return response.body().asString();
    }

    static String simpleOkPost(String url, String headerStr, String body) throws Exception {
        final MediaType header = MediaType.get(headerStr);
        RequestBody Body = RequestBody.create(body, header);
        Request request = new Request.Builder()
                .url(url)
                .post(Body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    static String cookiePost(String url, String body) throws Exception {
        final MediaType header = MediaType.get(Config.header2);
        RequestBody Body = RequestBody.create(body, header);
        Request request = new Request.Builder()
                .url(url)
                .header("Cookie", Config.cookie)
                .post(Body)
                .build();
        okhttp3.Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }

    static String httpPut(String host, String path, Map<String, Object> params, String body, Map<String, Object> replaceMap) throws Exception {
        putParams(replaceMap);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        println(url);
        Response response = given()
                .relaxedHTTPSValidation()
                .body(body)
                .put(url);
        return decResponse(response, (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }

    static String httpPatch(String host, String path, Map<String, Object> params, String body, Map<String, Object> replaceMap) throws Exception {
        putParams(replaceMap);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        println(url);
        Response response = given()
                .relaxedHTTPSValidation()
                .body(body)
                .patch(url);
        return decResponse(response, (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }

    static String httpDelete(String host, String path, Map<String, Object> params, Map<String, Object> replaceMap) throws Exception {
        putParams(replaceMap);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        println(url);
        Response response = given()
                .relaxedHTTPSValidation()
                .delete(url);
        return decResponse(response, (Integer) replaceMap.getOrDefault(EXPECT_CODE, 204));
    }

    static String multiPost(String host, String path, Map<String, Object> params, Map<String, Object> replaceMap, multiClass mc) throws Exception {
        putParams(replaceMap);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        println(url);
        RequestSpecification rs = given()
                .relaxedHTTPSValidation()
                .contentType("multipart/form-data");
        mc.multiPartMethod(rs);
        return decResponse(rs.post(url), (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }

    static String multiPut(String host, String path, Map<String, Object> params, Map<String, Object> replaceMap, multiClass mc) throws Exception {
        putParams(replaceMap);
        url = Md5Url.md5Url(host, path, params, replaceMap);
        println(url);
        RequestSpecification rs = given()
                .relaxedHTTPSValidation()
                .contentType("multipart/form-data");
        mc.multiPartMethod(rs);
        return decResponse(rs.put(url), (Integer) replaceMap.getOrDefault(EXPECT_CODE, 200));
    }
}

package com.qa.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import paypal.HttpsUrlConnectionForTLS;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
           /* for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static String sendHttpsPost(String url, String param) {
        //String param="applicationCode=WvQqVHfRz57UdSU9mnZyaVdzPSSKr28Y&referenceId=TRX170890111&version=v1&returnUrl=http://192.168.2.248:8080/third-payment&description=testmolpay&customerId=12321144221&signature=60fbafb83971de420e9fc6228aa11c4e&channelId=3";
        //String str= HttpUtil.sendPost("https://sandbox-api.mol.com/payout/payments", param);
        String result_str = "";
        JSONObject jsonObject = null;
        try {
            HttpsUrlConnectionForTLS httpsUrlConnectionMessageSender = new HttpsUrlConnectionForTLS();
            HttpURLConnection connection = httpsUrlConnectionMessageSender.createConnection(
                    new URI(url));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            /*connection.setRequestProperty("Content-Type",  "application/json");*/
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(param.length()));
            connection.connect();

            DataOutputStream out2 = new DataOutputStream(connection.getOutputStream());
            BufferedReader reader = null;
            try {
                out2.writeBytes(param);
                out2.flush();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String lines;
                StringBuffer sb = new StringBuffer("");
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                result_str = sb.toString();
                //jsonObject = JSONObject.parseObject(result_str);
            } finally {
                if (out2 != null) out2.close();
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  System.out.println(jsonObject);
        return result_str;
    }


    /**
     * 发送 GET 请求（HTTP），K-V形式
     *
     * @param url
     * @param params
     * @return
     */
    public static JSONObject doGet(String url, Map<String, Object> params) throws Exception {
        String apiUrl = url;
        JSONObject jsonObject = new JSONObject();
        StringBuffer param = new StringBuffer();

        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout
                (20000)
                .setConnectTimeout(20000).setConnectionRequestTimeout(20000)
                .build();

        CloseableHttpClient client = HttpClientBuilder.create().setMaxConnTotal(3)
                .setMaxConnPerRoute(20000).build();
        try {
            HttpGet httpPost = new HttpGet(apiUrl);
            httpPost.setConfig(requestConfig);
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            jsonObject.put("code", statusCode);
            HttpEntity entity = response.getEntity();

            System.out.println("执行状态码 : " + statusCode);
            if (statusCode != 200) {
//                throw new Exception("返回状态异常:" + response.getStatusLine().getStatusCode() + ", msg: " + response.getStatusLine().getReasonPhrase());
            }
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
                jsonObject.put("body", JSONObject.parseObject(result));
                System.out.println(result);
            }
        } catch (IOException e) {
            System.out.println(result);
            e.printStackTrace();
            throw e;
        }
        return jsonObject;
    }


    public static void main(String[] args) throws Exception {
        BigDecimal bd = new BigDecimal(19.99);

        //发送 GET 请求
        // String s=HttpUtil.sendGet("http://101.201.199.64:8904/api/2.0/web/users", "userAccount=13011829695%40t.com");
        //System.out.println(s);
        String putstrresponse = "{\n" +
                "\t\"data\": \"pYBlDIexFP0HMUteAanE6TUZ991QDCC/qcdzLSrRWoI1JEFp2h/M6OR4Xz+NqHysbf74rEf0yQmE\r\nInbH0MQiM51bZpiMbPvhBhxfG3ey/cNBTtTPYAW6JA==\",\n" +
                "\t\"key\": \"aiYCwBA8aQJd2biq76GpQ5FnnrJvEFkhe7CS54Xn2NU7Pat0XiR12u6r1MugZ3l5YPv+RLuGxTCp\r\nD5dLn49tBUwHcG4L/g8A3c01qAgHWbUsSjzmnJ5Os5A8FCXwt3VN8eURW8g8ed4W7gjgdJ1fTHjK\r\nf1Wc5cX0mx4pcgqx1P8=\"\n" +
                "}\n";

       /* UserWebGoldDto userWebGoldDto = new UserWebGoldDto();
        userWebGoldDto.setGold(new BigDecimal(10));
        userWebGoldDto.setToken(MD5.getMD5Code(new StringBuilder().append("463").append("f7b9b80f-af35-4c91-852e-20bf16274f68").append("10").toString()));
        userWebGoldDto.setUserId(463);
        String josn = JSONObject.toJSONString(userWebGoldDto);
        String encryptJson = RsaUtil.encryptJson(josn); //加密请求体
        System.out.println(encryptJson);
       /* String str =  HttpUtil.doPut("http://101.201.199.64:8903/api/2.0/web/users",encryptJson);
        String res = RsaUtil.decryptJson(str); //解密response
        JSONObject jsonObj = JSONObject.parseObject(res);
        System.out.println(res+"====");*/

        //System.out.println(s);
        //发送 POST 请求
        // String sr=HttpUtil.sendPost("http://localhost:6144/Home/RequestPostString", "key=123&v=456");
        // System.out.println(sr);
        //  String sr=HttpUtil.sendPost("http://localhost:6144/Home/RequestPostString", "key=123&v=456");

      /*  String url ="https://sandbox-api.mol.com/payout/payments";
        String param ="applicationCode=WvQqVHfRz57UdSU9mnZyaVdzPSSKr28Y&referenceId=b6abab90-9f72-4488-b43c-c132d9dba39e1498817753574&version=v1&signature=9a6cc568f37f2820299d84527036c6f8";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("applicationCode","WvQqVHfRz57UdSU9mnZyaVdzPSSKr28Y");
        params.put("referenceId","b6abab90-9f72-4488-b43c-c132d9dba39e1498817753574");
        params.put("version","v1");
        params.put("signature","9a6cc568f37f2820299d84527036c6f8");*/
        //HttpUtil.doGet(url,params);
        // System.out.println(sr);
    }
}

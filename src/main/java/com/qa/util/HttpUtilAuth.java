package com.qa.util;

import com.alibaba.fastjson.JSONObject;
import constant.Constants;
import org.apache.commons.codec.binary.Base64;
import paypal.HttpsUrlConnectionForTLS;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Creaed by fj on 2019/1/24
 */



public class HttpUtilAuth {


    /**
     * get 请求,auth 验证
     * @param url
     * @param param
     * @param userName
     * @param password
     * @return
     */
    public static String sendGet(String url, String param,String userName,String password) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            //设置用户名和密码
            String auth = userName+":"+password;
            //对其进行加密
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);
            //设置认证属性
            connection.setRequestProperty("Authorization","Basic " + res);

            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
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
     * post 请求,auth 验证
     * @param url
     * @param param
     * @param userName
     * @param password
     * @return
     */
    public static String sendPost(String url, String param,String userName,String password) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            //设置用户名和密码
            String auth = userName+":"+password;
            //对其进行加密
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);
            //设置认证属性
            conn.setRequestProperty("Authorization","Basic " + res);

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
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * post 请求,auth 验证
     * @param url
     * @param param
     * @param userName
     * @param passWord
     * @return
     */
    public static String sendHttpsPost(String url,String param,String userName,String passWord){
        String result_str = "";
        JSONObject jsonObject = null;
        try{
            HttpsUrlConnectionForTLS httpsUrlConnectionMessageSender = new HttpsUrlConnectionForTLS();
            HttpURLConnection connection = httpsUrlConnectionMessageSender.createConnection(new URI(url));

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setConnectTimeout(50000);
            connection.setInstanceFollowRedirects(true);

            //设置用户名和密码
            String auth = userName+":"+passWord;
            //对其进行加密
            byte[] rel = Base64.encodeBase64(auth.getBytes());
            String res = new String(rel);
            //设置认证属性
            connection.setRequestProperty("Authorization","Basic " + res);

	        connection.setRequestProperty("Content-Type",  "application/json");
            /*connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");*/
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
        }catch(Exception e){
            e.printStackTrace();
        }

        //  System.out.println(jsonObject);
        return result_str;
    }

    public static void main(String[] args){
       String result = HttpUtilAuth.sendPost(Constants.PAYONEER_URL,"",
                Constants.PAYONEER_USERNAME,Constants.PAYONEER_PASSWORD);
        System.out.println(result);
    }
}

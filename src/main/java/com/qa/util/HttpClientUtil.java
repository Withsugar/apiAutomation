package com.qa.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    /**
     * 支付会话创建 以及解析
     */
    public static  JSONObject sendPost(String url, Map<String,Object> paramMap,String paramStr,Map<String,String> headerMap, String encoding) throws Exception {
        logger.error("创建请求对象");
        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().disableAutomaticRetries().build();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        try {
            //设置header信息
            if(headerMap != null && headerMap.size() > 0){
                headerMap.forEach(httpPost::setHeader);
            }
            //设置失效时间 看看要不要设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .build();

            httpPost.setConfig(requestConfig);
            //装填参数
            //将该字符串设置为HttpEntity，并设置编码方式
            String jsonString="";
            if(paramStr != null){
                jsonString=jsonString+paramStr;
            }
            if(paramMap != null && paramMap.size()> 0){
                jsonString =jsonString+JSONObject.toJSONString(paramMap);
            }
            logger.error("请求参数参数{}"+jsonString);
            HttpEntity entity = new StringEntity(jsonString, encoding);
            // do post
            httpPost.setEntity(entity);
            logger.error("请求对象组装完成");
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            logger.error("请求完成");
            if (null != httpEntity) {
                String resBody = EntityUtils.toString(response.getEntity());
                /** 将String数组转为JSON*/
                JSONObject responseBody = JSONObject.parseObject(resBody);
                logger.error(String.format("回调参数打印responseBody{}%s", responseBody));
                if (responseBody != null) {
                    return responseBody;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            httpPost.releaseConnection();
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
    * 利用HttpClient进行post请求的工具类
    */
        public static String doPost(String url,Map<String,String> map,String charset,String userName,String passWord){
            HttpClient httpClient = null;
            HttpPost httpPost = null;
            String result = null;
            try{
                httpClient = new SSLClient().SslHttpClientBuild();
                httpPost = new HttpPost(url);
                logger.error("url{}",url);
                //设置参数
                List<NameValuePair> list = new ArrayList<>();
                Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
                JSONObject jsonObject = new JSONObject();
                while(iterator.hasNext()){
                    Entry<String,String> elem = iterator.next();
                    jsonObject.put(elem.getKey(), elem.getValue());
                }
                //logger.error("请求参数{}",jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString()));//json 格式
                /*UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                //使用 UrlEncodedFormEntity 来设置 body,消息体内容类似于“KEY1=VALUE1&;KEY2=VALUE2&;...”这种形式,服务端接收以后也要依据这种协议形式做处理。
                httpPost.setEntity(entity);*/

                //设置用户名和密码
                String auth = userName+":"+passWord;
                //对其进行加密
                byte[] rel = Base64.encodeBase64(auth.getBytes());
                String res = new String(rel);
                //设置认证属性
                httpPost.setHeader("Authorization","Basic " + res);
                httpPost.setHeader("Content-type","application/json");
                HttpResponse response = httpClient.execute(httpPost);
                if(response != null){
                    HttpEntity resEntity = response.getEntity();
                    if(resEntity != null){
                        result = EntityUtils.toString(resEntity,charset);
                        logger.error("请求结果result={}",result);
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            return result;
        }
}

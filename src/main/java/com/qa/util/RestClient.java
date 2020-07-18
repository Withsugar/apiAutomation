package com.qa.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author anthony
 * @date Oct 21, 2019
 * @updateTime 5:51:58 PM
 */
public class RestClient {
	private static ObjectMapper OM = new ObjectMapper();

	private RequestSpecification request;

	/**
	 * 设定请求contentype 格式为json的可以用这个方法启动RestClient服务
	 *
	 * @param endpoint
	 * @param service
	 * @param store
	 */
	public RestClient(String endpoint, String service, String store) {
		this.request = RestAssured.given().contentType("application/json")
				.config(RestAssuredConfig.newConfig()
						.encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("utf-8")))
				.baseUri(endpoint + service);
	}

	/**
	 * https请求服务
	 *
	 * @param endpoint
	 * @param service
	 * @param oj
	 */
	public RestClient(String endpoint, String service, Object oj) {
		this.request = RestAssured.given().contentType("application/json")
				.config(RestAssuredConfig.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation())
						.encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("utf-8")))
				.baseUri(endpoint + service);
	}

	/**
	 * 启动一个RestClient服务
	 *
	 * @param endpoint
	 * @param service
	 */
	public RestClient(String endpoint, String service) {
		this.request = RestAssured.given()
				.config(RestAssuredConfig.newConfig()
						.encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("utf-8")))
				.baseUri(endpoint + service);
	}

	/**
	 * 当参数格式为json时可以调用该方法传参
	 *
	 * @param map
	 * @return
	 */

	public RestClient setBody(HashMap<String, Object> map) {
		this.request.body(JSONArray.toJSON(map));
		return this;
	}

	/**
	 * 当body为字符串时可以调用该方法传参
	 *
	 * @param str
	 * @return
	 */

	public RestClient setBody(String str) {
		this.request.body(str);
		return this;
	}

	/*
	 * 设置session
	 */
	public RestClient setSession(String sessionIdValue) {
		this.request.sessionId(sessionIdValue);
		return this;
	}

	/**
	 * 该方法用于往header里面传参
	 *
	 * @param key
	 * @param value
	 */
	public RestClient setHeader(String key, String value) {
		Header header = new Header(key, value);
		this.request.header(header);
		return this;

	}

	/**
	 * 获取特定cookie的值
	 *
	 * @param uri
	 * @param cookieName
	 * @return
	 */
	public String getCooike(String uri, String cookieName) {
		String cookies = this.request.get(uri).getCookie(cookieName);
		return cookies;
	}

	/**
	 * 单独处理以query String参数格式
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public RestClient params(String name, Object value) {
		this.request.param(name, value);
		return this;
	}

	/**
	 * 添加query字符串可以和setBody结合使用
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public void queryParam(String name, Object value) {
		this.request.queryParam(name, value);
	}

	/**
	 * 添加query字符串可以和setBody结合使用,传参的时候只需要传一个map
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public void queryParam(HashMap<String, Object> map) {

		Set<?> entrySet = map.entrySet();
		for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) itor.next();
			this.request.queryParam(entry.getKey().toString(), entry.getValue());
		}
	}

	/**
	 * 以表单的方式提交参数
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public RestClient formParams(String name, Object value) {
		this.request.formParams(name, value, new Object[0]);
		return this;
	}

	/**
	 * 发送post请求，http status code 不是200代表失败
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject post() throws Exception {
		Response resp = this.request.post();
		if (resp.getStatusCode() == 200) {
			JSONObject result = JSONObject.parseObject(resp.asString());
			return result;
		}
		throw new RuntimeException(resp.getStatusCode() + resp.asString());
	}

	/**
	 * 发送post请求，不校验http response code
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject postReq() throws Exception {
		Response resp = this.request.post();
		JSONObject result = JSONObject.parseObject(resp.asString());
		return result;
	}

	/**
	 * 发送patch请求，http status code 不是200代表失败
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject patch() throws Exception {
		Response resp = this.request.patch();
		if (resp.getStatusCode() == 200) {
			JSONObject result = JSONObject.parseObject(resp.asString());
			return result;
		}
		throw new RuntimeException(resp.getStatusCode() + resp.asString());
	}

	/**
	 * 发送put请求，http status code 不是200代表失败
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject put() throws Exception {
		Response resp = this.request.put();
		if (resp.getStatusCode() == 200) {
			JSONObject result = JSONObject.parseObject(resp.asString());
			return result;
		}
		throw new RuntimeException(resp.getStatusCode() + resp.asString());
	}

	/**
	 * 发送delete请求，http status code 不是200代表失败
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject delete() throws Exception {
		Response resp = this.request.delete();
		JSONObject result = JSONObject.parseObject(resp.asString());
		return result;
	}

	/**
	 * 发送get请求http status code 不是200代表失败
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject get() throws Exception {
		Response resp = this.request.get();
		if (resp.getStatusCode() == 200) {
			JSONObject result = JSONObject.parseObject(resp.asString());
			return result;
		}
		throw new RuntimeException(resp.getStatusCode() + resp.asString());
	}

	/**
	 * 发送get请求http status code 不是200代表失败
	 *
	 * @return
	 * @throws Exception
	 */
	public JSONObject getReq() throws Exception {
		Response resp = this.request.get();
		JSONObject result = JSONObject.parseObject(resp.asString());
		return result;
	}

	/**
	 * 发送get请求http status code 不是200代表失败
	 *
	 * @return
	 * @throws Exception
	 */
	public String getResponseString() throws Exception {
		Response resp = this.request.get();
		return resp.asString();
	}

	/**
	 * 根据resultType灵活调用上述返回值为JSONObject的post也可以用此方法
	 *
	 * @param resultType
	 * @return
	 * @throws Exception
	 */
	public <T> T post(Class<T> resultType) throws Exception {
		Response resp = this.request.post();
		if (resp.getStatusCode() == 200) {
			String result = resp.asString();
			return OM.readValue(result, resultType);
		}
		throw new RuntimeException(resp.getStatusCode() + resp.asString());
	}

	/**
	 * 根据resultType灵活调用上述返回值为JSONObject的get也可以用此方法
	 *
	 * @param resultType
	 * @return
	 * @throws Exception
	 */
	public <T> T get(Class<T> resultType) throws Exception {
		Response resp = this.request.get();
		if (resp.getStatusCode() == 200) {
			String result = resp.asString();
			return OM.readValue(result, resultType);
		}
		throw new RuntimeException(resp.getStatusCode() + resp.asString());
	}

	/**
	 * 请求包含multiPart只上传文件
	 *
	 * @param file
	 */
	public void uploadFile(File file) {
		this.request.multiPart(file);
	}

	/**
	 * 请求包含multiPart通过key-value形式上传文件
	 *
	 * @param key
	 * @param file
	 */
	public void uploadFile(String key, File file) {
		this.request.multiPart(key, file);
	}
}

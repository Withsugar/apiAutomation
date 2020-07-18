package com.qa.atomic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.testcases.UserRelatedTestCase;
import com.qa.util.RandomUtil;
import com.qa.util.RestClient;
import com.qa.util.crypt.CryptUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import static com.qa.util.crypt.CryptUtil.decryptResponse;


/**
 * @author liminyi
 * @date 2020/02/17
 * 30秒收益相关接口
 */
public class ThirtySecondApi {
	/**
	 * 获取30秒收益配置秒数接口
	 * @throws Exception
	 */
	public static JSONObject getGoddessReduceSeconds(HashMap<String, Object> map) throws Exception {
		String path = "/api/config/getGoddessReduceSeconds" ;
		map.put("path", path);
		RestClient rc = UserRelatedTestCase.signRelatedParam(map);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 获取女神状态列表
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getGoddessStatusList(HashMap<String, Object> map) throws Exception {
		String path = "/api/getGoddessStatusList";
		map.put("path", path);
		RestClient rc = UserRelatedTestCase.signRelatedParam(map);
		rc.queryParam(map);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 修改女神在线状态 0代表下线，1代表忙线，2是在线
	 *
	 * @param map
	 * @param status
	 * @throws Exception
	 */
	public static void editGoddessStatus(HashMap<String, Object> map, int status) throws Exception {
		String path = "/api/editGoddessStatus";
		map.put("path", path);
		RestClient rc = UserRelatedTestCase.signRelatedParam(map);
		rc.queryParam(map);
		map.clear();
//		map.put("appId", DataConfig.appId);
		map.put("status", status);
		rc.setBody(CryptUtil.encryptRequest(JSONArray.toJSON(map).toString()));
		rc.put();
	}

	/**
	 * 获取女神状态
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getGoddessOpenStatus(HashMap<String, Object> map) throws Exception {
		String path = "/api/getGoddessOpenStatus";
		map.put("path", path);
		RestClient rc = UserRelatedTestCase.signRelatedParam(map);
		rc.queryParam(map);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 上传女神视频
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject uploadGoddessVideo(HashMap<String, Object> map) throws Exception {
		String path = "/upload/uploadAlbumVideo";
		map.put("path", path);
		RestClient rc = UserRelatedTestCase.signRelatedParam(map);
		Random random = new Random();
		int num = random.nextInt(4);
		File videoFile = new File("src/test/resources/videos/" + num + ".mp4");
		File videoPic = new File("src/test/resources/videos/" + num + ".jpg");
		rc.uploadFile("video", videoFile);
		rc.uploadFile("videoPic", videoPic);
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

/**
 *
 * @param map
 * @param goddessId
 * @param callMode 1:女神墙 2:匹配 3:好友
 * @param isFriend 1:好友 2:非好友
 * @return
 * @throws Exception
 */
	public static JSONObject setGoddessVideoReduce(HashMap<String, Object> map,String goddessId,String callMode,String isFriend) throws Exception {
		String path = "/api/setGoddessVedioReduce";
		map.put("path", path);
		RestClient rc = UserRelatedTestCase.signRelatedParam(map);
		map.clear();
		map.put("areaCode", -1);
		map.put("callMode", callMode);
		map.put("videoMark", RandomUtil.getRandomString(8));
		map.put("appId", 20000);
		map.put("remoteUserId", goddessId);
		map.put("isFriend", isFriend);
		map.put("model", -1);
		rc.setBody(CryptUtil.encryptRequest(JSONArray.toJSON(map).toString()));
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	public static JSONObject getVideoCallIncome(HashMap<String, Object> map) throws Exception {
		String path = "/api/getVediomarkIncome";
		map.put("path", path);
		RestClient rc = UserRelatedTestCase.signRelatedParam(map);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

}

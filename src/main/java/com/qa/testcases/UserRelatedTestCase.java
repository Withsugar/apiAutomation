package com.qa.testcases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qa.util.CommUtil;
import com.qa.util.RandomUtil;
import com.qa.util.RestClient;
import com.qa.util.crypt.CryptUtil;
import com.qa.util.crypt.MD5;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;

import static com.qa.util.crypt.CryptUtil.decryptResponse;

/**
 *
 * @author anthony
 * @date Oct 17, 2019
 * @updateTime 5:01:31 PM
 */
public class UserRelatedTestCase {
	/**
	 * 第三方登录
	 *
	 * @param domainUrl
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject thirdPartyLogin(String domainUrl, HashMap<String, Object> map) throws Exception {
		String path = "/api/users/threeParties";
		RestClient rc = new RestClient(domainUrl, path);
		rc.setBody(CryptUtil.encryptRequest(JSONArray.toJSON(map).toString()));
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 发送手机验证码
	 *
	 * @param domainUrl
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject sendMsg(String domainUrl, HashMap<String, Object> map) throws Exception {
		String path = "/api/sms/sendCode";
		RestClient rc = new RestClient(domainUrl, path);
		rc.setBody(CryptUtil.encryptRequest(JSONArray.toJSON(map).toString()));
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 验证手机验证码
	 *
	 * @param domainUrl
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject verifyMsg(String domainUrl, HashMap<String, Object> map) throws Exception {
		String path = "/api/sms/checkCode";
		RestClient rc = new RestClient(domainUrl, path);
		rc.queryParam(map);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 通过配置提供的邮箱注册账号<br>
	 * gender 1男2女
	 *
	 * @throws Exception
	 */
	public static JSONObject emailRegist(HashMap<String, Object> map) throws Exception {
		String path = "/api/users/emails";
		RestClient rc = new RestClient("", path);
		rc.params("data", CryptUtil.encryptRequest(JSONArray.toJSON(map).toString()));
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 邮箱登录
	 *
	 * @param domainUrl
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static JSONObject emailLogin(String domainUrl, HashMap<String, Object> map) throws Exception {
		String path = "/api/users/emails";
		RestClient rc = new RestClient(domainUrl, path);
		rc.setBody(CryptUtil.encryptRequest(JSONArray.toJSON(map).toString()));
		JSONObject jsonObj = rc.patch();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 校验是否AEO
	 *
	 * @throws Exception
	 */
	public static JSONObject checkNewAeoUser(String userId) throws Exception {
		String path = "/api/users/checkNewAeoUser";
		RestClient rc = new RestClient("", path);
		rc.params("userId", userId);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 通过email检测邮箱是否注册
	 *
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static JSONObject emailRegisted(String email) throws Exception {
		String path = "/api/users/emails";
		RestClient rc = new RestClient("", path);
		rc.params("email", email);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 通过userId获取用户当前的举报状态
	 *
	 * @param userId
	 * @throws Exception
	 */
	public static JSONObject getUserReportStatus(String userId) throws Exception {
		String path = "/api/users/reports/status";
		RestClient rc = new RestClient("", path);
		rc.params("userId", userId);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 通过userId获取用户是否开启了wallex switch
	 *
	 * @param userId
	 * @param loginToken
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getUserWallexSwitch(String userId, String loginToken) throws Exception {
		String path = "/api/users/wallexSwitch";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 通过userId查询用户信息，并且取出结果返回的id和userId进行比较
	 *
	 * @param userId
	 * @throws Exception
	 */
	public static JSONObject getUserInfo(String userId, String loginToken) throws Exception {
		String path = "/api/users/" + userId;
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 更新用户profile的兴趣，生日，签名，语言等
	 *
	 * @param map
	 * @param userId
	 * @param loginToken
	 * @return
	 * @throws Exception
	 */
	public static JSONObject updateUserLanguage(HashMap<String, Object> map, String userId, String loginToken)
			throws Exception {
		String path = "/api/users/languageInterest";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		rc.setBody(CryptUtil.encryptRequest(JSONArray.toJSON(map).toString()));
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 根据userId查询用户视频呼叫限制
	 *
	 * @param userId
	 * @param videoHour
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getUserPowers(String userId, String loginToken, Integer videoHour) throws Exception {
		String path = "/api/users/powers";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		rc.params("appId", "");
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseArray(result);
	}

	/**
	 * 根据userId购买产品，返回值校验consumeId
	 *
	 * @param userId
	 * @param loginToken
	 * @param consumeId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject postUserCommodities(String userId, String loginToken, Integer consumeId) throws Exception {
		String path = "/api/users/commodities";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		rc.queryParam("appId", "");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("consumeId", consumeId);
		map.put("matchType", 0);
		map.put("matchUserId", 0);
		map.put("roomId", "");
		String reqStr = CryptUtil.encryptRequest(JSONArray.toJSON(map).toString());
		rc.setBody(reqStr);
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 根据userId获取用户消费列表
	 *
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static JSONArray getUserConsumes(String userId, String loginToken) throws Exception {
		String path = "/api/users/consumes";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		rc.queryParam("appId", "");
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseArray(result);
	}

	/**
	 * 根据userId给用户推送消息，并且验证返回
	 *
	 * @param pushUserId
	 * @param loginToken
	 * @throws Exception
	 */
	public static void postPush(String pushUserId, String loginToken) throws Exception {
		String path = "/api/pushes";
		RestClient rc = signRelatedParam(path, loginToken, pushUserId, CommUtil.createNonce(), CommUtil.getTimestamp());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("function", 0);
		map.put("text", "test");
		map.put("pushUserId", pushUserId);
		String reqStr = CryptUtil.encryptRequest(JSONArray.toJSON(map).toString());
		rc.setBody(reqStr);
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		Assert.assertTrue(result.startsWith("{"));
	}

	/**
	 * 获取最近活跃的用户列表
	 *
	 * @param userId
	 * @param loginToken
	 * @throws Exception
	 */
	public static void getRecentActiveUsers(String userId, String loginToken) throws Exception {
		String path = "/api/user/recommend/getRecentActiveUsers";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		rc.params("platformType", 2);
		rc.params("appId", "");
		rc.params("deviceName", "A0001");
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		System.out.println(JSONObject.parseObject(result));
		Assert.assertFalse(JSONObject.parseObject(result).getJSONObject("data").isEmpty());
	}

	/**
	 * 用户匹配
	 *
	 * @param userId
	 * @param loginToken
	 * @param gender
//	 * @param matchedId
	 * @throws Exception
	 */
	public static void userMatch(String domainUrl, String userId, String loginToken, Integer gender) throws Exception {
		String path = "/api/users/matches/randoms";
		RestClient rc = signRelatedParam(domainUrl, path, loginToken, userId, CommUtil.createNonce(),
				CommUtil.getTimestamp());
		rc.params("platformType", 2);
		rc.params("appId", "");
		rc.params("gender", gender);
		rc.params("matchMode", 1);
		rc.params("type", 2);
		rc.params("target", 0);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		JSONObject response = JSONObject.parseObject(result);
		System.out.println(response);
		if (!result.contains("没有找到匹配用户")) {
			Assert.assertTrue(response.getInteger("id").intValue() != 0);
		}
		// Thread.sleep(1000);
	}
	/**
	 * 用户取消匹配
	 *
	 * @param domainUrl
	 * @param userId
	 * @param loginToken
	 * @throws Exception
	 */
	public static void deleteMatch(String domainUrl, String userId, String loginToken) throws Exception {
		String path = "/api/users/matches";
		RestClient rc = signRelatedParam(domainUrl, path, loginToken, userId, CommUtil.createNonce(),
				CommUtil.getTimestamp());
		rc.params("appId", "");
		rc.delete();
	}

	/**
	 * 用户like
	 *
	 * @param bool
	 * @param loginToken
	 * @param likerId
	 * @param likedId
	 * @throws Exception
	 */
	public static void userAddLikeTest(Boolean bool, String loginToken, String likerId, String likedId)
			throws Exception {
		String path = "/api/profile/like/like";
		RestClient rc = signRelatedParam(path, loginToken, likerId, CommUtil.createNonce(), CommUtil.getTimestamp());
		rc.params("appId", "");
		rc.params("likeOrNot", bool);
		rc.params("likedId", likedId);
		rc.params("likerId", likerId);
		rc.params("platformType", 2);
		rc.params("site", 1);
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		JSONObject response = JSONObject.parseObject(result);
		System.out.println(response);
		Assert.assertTrue(response.getJSONObject("data").getBooleanValue("status"));
	}

	/**
	 * 获取用户被like的列表
	 *
	 * @param loginToken
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject likeListTest(String loginToken, String userId) throws Exception {
		String path = "/api/profile/like/show";
		RestClient rc = UserRelatedTestCase.signRelatedParam(path, loginToken, userId, CommUtil.createNonce(),
				CommUtil.getTimestamp());
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 通过userId查询用户被like次数
	 *
	 * @param loginToken
	 * @param likedId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getLikeInfoByIdTest(String loginToken, String likedId) throws Exception {
		String path = "/api/profile/like/enter";
		RestClient rc = UserRelatedTestCase.signRelatedParam(path, loginToken, likedId, CommUtil.createNonce(),
				CommUtil.getTimestamp());
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 通过用户id把对应用户添加到黑名单
	 *
	 * @param loginToken
	 * @param userId
	 * @param blackListId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject addUserToBlackList(String loginToken, String userId, String blackListId) throws Exception {
		String path = "/api/blacklists/" + blackListId;
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		JSONObject jsonObj = rc.postReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);

	}

	/**
	 * 查看黑名单列表
	 *
	 * @param loginToken
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getBlackList(String loginToken, String userId) throws Exception {
		String path = "/api/blacklists";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 删除黑名单
	 *
	 * @param loginToken
	 * @param userId
	 * @param blackListId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject removeUserFromBlackList(String loginToken, String userId, String blackListId)
			throws Exception {
		String path = "/api/blacklists/" + blackListId;
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		JSONObject jsonObj = rc.delete();
		return jsonObj;
	}

	/**
	 * 获取合作用户工作量
	 *
	 * @param loginToken
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getUserWorkLoad(String loginToken, String userId) throws Exception {
		String path = "/api/workload";
		RestClient rc = signRelatedParam(path, loginToken, userId, CommUtil.createNonce(), CommUtil.getTimestamp());
		JSONObject jsonObj = rc.getReq();
		String result = decryptResponse(jsonObj.toJSONString());
		return JSONObject.parseObject(result);
	}

	/**
	 * 用户注册用户的基本map，调用时可以修改参数<br>
	 * 主要字段解释：deviceID是登录设备号，password的明文是123456，gender性别1男2女
	 *
	 * @return
	 */
	public static HashMap<String, Object> thirdPartyMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("adid", "964B18F5-D018-455B-A765-4B8183D96B6F");
		map.put("appId", "");
		map.put("deviceId", "EC17D289-AAA8-4EF2-BAF5-7E32978C3D6F");
		map.put("deviceName", "iPhone8,2");
		map.put("deviceType", 1);
		map.put("systemVersion", "iOS 13.2");
		map.put("languageId", 2);
		map.put("languageName", "Chinese");
		map.put("platformType", 1);
		map.put("screenSize", "1242*2208");
		map.put("type", 2);
		map.put("threePartyId", "2352462188196345");
		map.put("threePartyEmail", "joykao@live.cn");
		map.put("userName", "Anthony Gao");
		map.put("birthday", 563040000000l);
		return map;
	}

	/**
	 * 用户注册用户的基本map，调用时可以修改参数<br>
	 * 主要字段解释：deviceID是登录设备号，password的明文是123456，gender性别1男2女
	 *
	 * @return
	 */
	public static HashMap<String, Object> userRegistMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("countryId", 42);
		map.put("countryName", "China");
//		map.put("password", DataConfig.password);
//		map.put("deviceId", DataConfig.deviceId);
		map.put("longitude", 116.8066406250);
		map.put("languageId", 2);
		map.put("deviceType", 1);
		map.put("gender", 2);
		map.put("languageName", "en");
		map.put("appId", "");
		map.put("birthday", 935404178935l);
		map.put("platformType", 2);
		map.put("systemVersion", 23);
//		map.put("userAccount", DataConfig.generateEmail());
		map.put("latitude", 39.5718222373);
		map.put("userName", "newyoyo" + RandomUtil.getDigits(5));
		map.put("screenSize", "1080*1920");
		map.put("deviceName", "A0001");
		return map;
	}

	/**
	 * 用户登录的基本map，调用时可以修改参数<br>
	 * 登录时最重要的两个字段是userAccount和password
	 *
	 * @return
	 */
	public static HashMap<String, Object> userLoginMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appId", "");
//		map.put("deviceId", DataConfig.deviceId);
		map.put("deviceName", "A0001");
		map.put("deviceType", 1);
		map.put("languageId", 2);
		map.put("latitude", 39.5718222373);
		map.put("longitude", 116.8066406250);
//		map.put("password", DataConfig.password);
		map.put("platformType", 2);
		map.put("pushToken", "");
		map.put("screenSize", "1080*1920");
		map.put("stone", 0);
		map.put("systemVersion", 23);
		map.put("timeZone", 0);
//		map.put("userAccount", DataConfig.emailAddress);
		return map;
	}

	/**
	 * 用户基本信息兴趣语言的map，默认添加了2个兴趣，调用时可以修改参数<br>
	 *
	 * @return
	 */
	public static HashMap<String, Object> userProfileMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appId", "");
		ArrayList<Integer> al = new ArrayList<Integer>();
		al.add(1);
		al.add(5);
		map.put("list", al);
		map.put("site", 1);
		return map;
	}

	/**
	 * 返回签名
	 *
	 * @param url        可以参照swagger中的域名+path
	 * @param loginToken 用户登录后产生的loginToken或者注册时产生的loginToken
	 * @param deviceId   用户使用的设备号
	 * @param nonce      UUID的随机字符串
	 * @param timestamp  时间戳
	 * @param userId     用户id
	 * @return
	 */
	public static String getSignValue(String url, String loginToken, String deviceId, String nonce, long timestamp,
			String userId) {
		String inputString = url + loginToken + deviceId + nonce + timestamp + userId;
		String sign = MD5.getMD5Code(inputString);
		return sign;
	}

	/**
	 * 返回已经签好名的RestClient
	 *
	 * @param path
	 * @param loginToken
	 * @param userId
	 * @param nonce
	 * @param timestamp
	 * @return
	 */
	public static RestClient signRelatedParam(String path, String loginToken, String userId, String nonce,
			long timestamp) {
		RestClient rc = new RestClient("", path);
		String deviceId = "";
		rc.queryParam("deviceId", deviceId);
		rc.queryParam("timestamp", timestamp);
		rc.queryParam("sign",
				getSignValue("" + path, loginToken, deviceId, nonce, timestamp, userId));
		rc.queryParam("userId", userId);
		rc.queryParam("nonce", nonce);
		return rc;
	}

	/**
	 * 返回已经签好名的RestClient
	 *
	 * @param domainUrl
	 * @param path
//	 * @param deviceId
	 * @param loginToken
	 * @param userId
	 * @param nonce
	 * @param timestamp
	 * @return
	 */
	public static RestClient signRelatedParam(String domainUrl, String path, String loginToken, String userId,
			String nonce, long timestamp) {
		RestClient rc = new RestClient(domainUrl, path);
//		String deviceId = DataConfig.deviceId;
//		rc.queryParam("deviceId", deviceId);
		rc.queryParam("timestamp", timestamp);
//		rc.queryParam("sign", getSignValue(domainUrl + path, loginToken, deviceId, nonce, timestamp, userId));
		rc.queryParam("userId", userId);
		rc.queryParam("nonce", nonce);
		return rc;
	}

	/**
	 * 返回已经签好名的RestClient
	 * @return
	 */
	public static RestClient signRelatedParam(HashMap<String, Object> map) {

		String deviceId = "";
		long timestamp = CommUtil.getTimestamp();
		String nonce = CommUtil.createNonce();
		map.put("deviceId", deviceId);
		map.put("timestamp", timestamp);
		map.put("sign", getSignValue(map.get("path").toString(),
				map.get("loginToken").toString(), deviceId, nonce, timestamp, map.get("userId").toString()));
		map.put("userId", map.get("userId").toString());
		map.put("nonce", nonce);
		RestClient rc = new RestClient( "",map.get("path").toString());
		rc.queryParam(map);
		return rc;
	}

}

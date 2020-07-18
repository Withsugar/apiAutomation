package com.qa.commons;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rc.qa.commons.AppConfig.*;
import static com.rc.qa.commons.Common.*;
import static com.rc.qa.commons.Md5Url.mapToString;
import static com.rc.qa.commons.RcHttpUtil.*;

/**
 * @create: 2019-12-04 23:19
 * @author: cloudsinn
 * @description: videoChat 管理系统
 **/
public class VideoChatMS {

    private static String uri;
    private static int adminId = 2;

    /* 登陆videoChat管理系统 */
    public static String VC_login() throws Exception {
        if (host.equals("tokyo"))
            adminId = 101;
        String url = opHost + "/admin/admin/login.json";
        String body = new jsonObjectUtil().put("email", "admin").put("password", "123456").json.toJSONString();
        String response = simpleOkPost(url, Config.header1, body);
        return getJson_value(response, "token");
    }

    static MapUtil map4ParamsUtil(String userId) {
        return new MapUtil().put("adminId", adminId).put("appId", appId).put("token", token).put("userId", userId);
    }

    /* userId 加金币 */
    public static void VIP_ReCharge_UID(String userId, String gold, String money) throws Exception {
        uri = opHost + "/admin/user/users/updateUserGold?";
        MapUtil paramUtil = new MapUtil().put("adminId", adminId).put("appId", appId).put("token", token);
        String params = mapToString(paramUtil.map);
        String body = new jsonObjectUtil()
                .put("userId", userId).put("gold", gold).put("money", money).json.toJSONString();
        System.out.println(simpleOkPost(uri + params, Config.header1, body));
    }

    /* otherId 加金币 */
    private static void VIP_ReCharge_AID(String otherId, String gold, String money) throws Exception {
        String userId = String.valueOf(99999999 - Integer.parseInt(otherId));
        VIP_ReCharge_UID(userId, gold, money);
    }

    /* 用户管理补金币 */
    public static void userAddCoin(String userId, String gold) throws Exception {
        uri = opHost + "/admin/user/users/gold.json?";
        MapUtil paramUtil = new MapUtil().put("adminId", adminId).put("appId", appId).put("token", token);
        String params = mapToString(paramUtil.map);
        String body = new jsonObjectUtil()
                .put("userId", userId).put("gold", gold).json.toJSONString();
        System.out.println(simpleOkPost(uri + params, Config.header1, body));
    }

    /* 查询用户信息 待优化显示 */
    private static void Inquire_userInfo(String otherUserId) throws Exception {
        uri = opHost + "/admin/user/users/otherUserId/" + otherUserId + "?";
        MapUtil paramUtil = new MapUtil().put("appId", appId).put("token", token);
        String params = mapToString(paramUtil.map);
        String response = simpleGet(uri + params);
        System.out.println(getJson_value(response, "id"));
        System.out.println(getJson_value(response, "userAccount"));
        System.out.println(getJson_value(response, "threePartyId"));
        System.out.println(getJson_value(response, "threePartyEmail"));
        System.out.println(getJson_value(response, "userName"));
        System.out.println(getJson_value(response, "appId"));
        System.out.println(getJson_value(response, "countryId"));
        System.out.println(getJson_value(response, "countryName"));
        System.out.println(getJson_value(response, "userType"));
        System.out.println(getJson_value(response, "gender"));
        System.out.println(response);
    }

    /**
     * -----------------------------------------------女神/合作女相关-----------------------------------------------------
     */

    //       添加女神，可以同时设置圈女神(色情女神)、上墙女神
    public static void Insert_Goddess(String userId) throws Exception {
        uri = opHost + "/admin/goddess/insertGoddess?";
        MapUtil paramUtil = map4ParamsUtil(userId)
                .put("managerGroupId", 339)         // 女神管理分组：339,520,550,506,549,380,417
                .put("groupId", 171)                // 女神墙管理 俄语组
                .put("eroticismBehavior", true);    // 设置色情女神
        String params = mapToString(paramUtil.map);
        System.out.println(simpleGet(uri + params));
    }

    /*  id: 16, name: "英语组"
        id: 18, name: "土耳其语"
        id: 20, name: "阿拉伯语"
        id: 23, name: "法语"
        id: 27, name: "泰语"
        id: 30, name: "印尼语"
        id: 33, name: "印地语"
        id: 114, name: "不许点"
        id: 119, name: "New"
        id: 120, name: "Featured"
        id: 166, name: "不许点2.0"
        id: 171, name: "俄语"*/
    /* 添加上墙女神 */
    public static void Insert_WallGoddess(String userId) {
        uri = opHost + "/admin/goddess/insertWallGoddess?";
        MapUtil paramUtil = map4ParamsUtil(userId).put("groupId", 171);
        String params = mapToString(paramUtil.map);
        System.out.println(simpleGet(uri + params));
    }

    // 取消墙女神
    public static void delete_WallGoddess(String userId) {
        uri = opHost + "/admin/goddess/deleteWallGoddess?";
        MapUtil paramUtil = map4ParamsUtil(userId).put("groupId", 171);
        String params = mapToString(paramUtil.map);
        System.out.println(simpleGet(uri + params));
    }

    // 取消女神, 调用取消女神接口，会同时取消女神和上墙女神
    public static void delete_Goddess(String userId) throws Exception {
        uri = opHost + "/admin/goddess/deleteGoddess?";
        MapUtil paramUtil = map4ParamsUtil(userId);
        String params = mapToString(paramUtil.map);
        System.out.println(simpleOkPost(uri + params, Config.header1, "{}"));
    }

    // 普通女添加成合作女 几个S开头的groupId ：285，294，311，316，318，328
    public static void add_cooperation_female(String userId, int groupId) {
        uri = opHost + "/admin/tempUser/addTempUser/" + userId + "/" + groupId + "?";
        MapUtil paramUtil = new MapUtil().put("appId", appId).put("token", token);
        String params = mapToString(paramUtil.map);
        System.out.println(simpleGet(uri + params));
    }

    // 合作女变普通女
    public static void delete_cooperation_female(String userId) {
        uri = opHost + "/admin/tempUser/removeTempUser/" + userId + "?";
        MapUtil paramUtil = new MapUtil().put("appId", appId).put("token", token);
        String params = mapToString(paramUtil.map);
        System.out.println(simpleGet(uri + params));
    }

    /**
     * ------------------------------------------------ 审核相关---------------------------------------------------------
     */

    /**
     * 通用方法：获取多媒体文件id
     *
     * @param response String
     * @param key      可取值：["picId","videoId","id"]
     * @return
     */
    public static String common_get_multiId(String response, String key) {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        String array = getJson_value(response, "data");
        int len = getJsonArray_size(array);
        for (int i = 0; i < len; i++) {
            list.add(getJson_value(getJsonArray_value(array, i), key));
        }
        System.out.println(String.join(",", list));
        return String.join(",", list);
    }

    /**
     * 通用方法：多媒体文件通过审核
     *
     * @param s1 初审通过多媒体id   可取值：["passImageIdList", "passVideoIdList", ""]
     * @param s2 初审不通过多媒体id  可取值：["removeImageIdList", "removeVideoIdList", ""]
     * @return 响应结果
     */
    public static String firstMultiTrailList(String uri, String collect, String s1, String s2) {
        MapUtil paramUtil = new MapUtil().put("appId", appId).put("token", token);
        String params = mapToString(paramUtil.map);
        return simpleMultiPart(uri + params, rs -> {
            rs.multiPart(s1, collect).multiPart(s2, "")
                    .multiPart("adminId", adminId).multiPart("pageSize", 30);
        });
    }

    // 通用方法：获取多媒体文件列表
    public static String getMultiTrailList(String uri) {
        MapUtil paramUtil = new MapUtil().put("adminId", adminId).put("appId", appId)
                .put("pageSize", 30).put("token", token);
        String params = mapToString(paramUtil.map);
        return simpleGet(uri + params);
    }

    // 通用方法：获取名字审核列表
    public static String get_nicknameTrailList() {
        uri = opHost + "/admin/userTempName/getUserTempNameList.json?";
        MapUtil paramUtil = new MapUtil().put("appId", appId).put("pageNo", 1)
                .put("pageSize", 50).put("token", token);
        String params = mapToString(paramUtil.map);
        return simpleGet(uri + params);
    }

    // 获取初审相册列表
    public static String get_firstAlbumTrialList() {
        uri = opHost + "/admin/userAlbumPicReview/getFirstTrialPicList.json?";
        return getMultiTrailList(uri);
    }

    // 获取首张头像初审列表
    public static String get_firstHeadImgTrailList() {
        uri = opHost + "/admin/userAlbumFirstPicReview/getFirstTrialPicList.json?";
        return getMultiTrailList(uri);
    }

    // 获取初审视频列表
    public static String get_firstVideoTrailList() {
        uri = opHost + "/admin/userAlbumVideoReview/getFirstTrialVideoList.json?";
        return getMultiTrailList(uri);
    }

    // 获取初审用户签名/自我介绍
    public static String get_firstIntroduceTrailList() {
        uri = opHost + "/admin/userTempIntroduceFirst/getUserTempIntroduceList.json?";
        return getMultiTrailList(uri);
    }

    // 初审图片通过审核
    public static void firstAlbumTrial_pass() {
        String response = get_firstAlbumTrialList();
        String collect = common_get_multiId(response, "picId");
        uri = opHost + "/admin/userAlbumPicReview/firstTrialPassAndRemove.json?";
        System.out.println(firstMultiTrailList(uri, collect, "passImageIdList", "removeImageIdList"));
    }

    // 初审首张头像通过审核
    public static void firstHeadImgTrail_pass() {
        String response = get_firstHeadImgTrailList();
        String collect = common_get_multiId(response, "picId");
        uri = opHost + "/admin/userAlbumFirstPicReview/firstTrialPassAndRemove.json?";
        System.out.println(firstMultiTrailList(uri, collect, "passImageIdList", "removeImageIdList"));
    }

    // 初审视频通过审核
    public static void firstVideoTrail_pass() {
        String response = get_firstVideoTrailList();
        String collect = common_get_multiId(response, "videoId");
        uri = opHost + "/admin/userAlbumVideoReview/firstTrialPassAndRemove.json?";
        System.out.println(firstMultiTrailList(uri, collect, "passVideoIdList", "removeVideoIdList"));
    }

    // 初审用户签名/自我介绍通过审核
    public static void firstIntroduceTrail_pass() {
        String response = get_firstIntroduceTrailList();
        System.out.println(response);
        String array = getJson_value(response, "data");
        int len = getJsonArray_size(array);
        List<String> introduceList = Collections.synchronizedList(new ArrayList<>());
        List<String> wordList = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < len; i++) {
            String id = getJson_value(getJsonArray_value(array, i), "id");
            String userId = getJson_value(getJsonArray_value(array, i), "userId");
            String introduce = getJson_value(getJsonArray_value(array, i), "introduce");
            String userType = getJson_value(getJsonArray_value(array, i), "userType");

            introduceList.add(new MapUtil().put("id", id).put("userId", userId).put("reviewId", id)
                    .put("introduce", introduce).put("userType", userType).put("sensitiveWord", "")
                    .put("entryLanguage", 1).put("reviewStatus", 2).put("status", true).map.toString());
            wordList.add(new MapUtil().put("reviewId", id).put("entryLanguage", 1).put("userId", userId)
                    .put("suspectSensitiveWords", "").put("source", "个性签名审核").put("attribution", introduce)
                    .put("userType", userType).map.toString());
        }
        System.out.println(introduceList);
        System.out.println(wordList);
        uri = opHost + "/admin/userTempIntroduceFirst/passAndRemove.json?";
        MapUtil paramUtil = new MapUtil().put("appId", appId).put("token", token);
        String params = mapToString(paramUtil.map);
        String result = simpleMultiPart(uri + params, rs -> {
            rs.multiPart("selectedIntroduce", introduceList).multiPart("unSelectedIntroduce", "[]")
                    .multiPart("enSelectedIntroduce", "[]").multiPart("adminId", adminId)
                    .multiPart("pageSize", 3).multiPart("wordList", wordList);
        });
        System.out.println(result);
    }

    // 通过所有名字审核
    public static void nicknameTrail_pass() throws UnsupportedEncodingException {
        String response = get_nicknameTrailList();
        String array = getJson_value(response, "list");
        int len = getJsonArray_size(array);
        List<String> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < len; i++) {
            list.add(new MapUtil().put("userId", getJson_value(getJsonArray_value(array, i), "userId"))
                    .put("username", getJson_value(getJsonArray_value(array, i), "username"))
                    .put("status", true).map.toString());
        }
        System.out.println(list);
        uri = opHost + "/admin/userTempName/passUserName.json?";
        MapUtil paramUtil = new MapUtil().put("adminId", adminId).put("appId", appId)
                .put("token", token).put("passNames", list).put("removeNames", "[]");
        String params = mapToString(paramUtil.map);
        System.out.println(simpleGet(uri + URLEncoder.encode(params, "UTF-8")));
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        set_host_appName("245", "livu");
        token = VC_login();
    }

    @Test(description = "初审图片通过审核")
    public static void firstAlbumTrial_pass_() {
        firstAlbumTrial_pass();
    }

    @Test(description = "初审首张头像通过审核")
    public static void firstHeadImgTrail_pass_() {
        firstHeadImgTrail_pass();
    }

    @Test(description = "初审视频通过审核")
    public static void firstVideoTrail_pass_() {
        firstVideoTrail_pass();
    }

    @Test(description = "初审用户签名/自我介绍通过审核, 未完待续。。。")
    public static void firstIntroduceTrail_pass_() {
        firstIntroduceTrail_pass();
    }

    @Test(description = "合作女名字通过审核")
    public static void nicknameTrail_pass_() throws UnsupportedEncodingException {
        nicknameTrail_pass();
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Test(description = "金币推广活动执行接口")
    public static void active_jobInfo() throws Exception {
        uri = "http://39.100.85.184:8989/xxl-job-admin-1.9.1-SNAPSHOT/jobinfo/trigger";
        String body = "id=104";
        System.out.println(cookiePost(uri, body));
    }

    @Test(description = "更改性别")
    public static void genderChange(String userId) {
        uri = opHost + "/admin/user/users/userGenderChange.json?";
        MapUtil paramUtil = map4ParamsUtil(userId);
        String params = mapToString(paramUtil.map);
        System.out.println(simpleGet(uri + params));
    }

    @Test(description = "userId 充值金币")
    public void vipCharge_byUserId() throws Exception {
        VIP_ReCharge_UID("57452722", "1000", "0");
    }

    @Test(description = "userId 补金币")
    public void addGold_byUserId() throws Exception {
        userAddCoin("57452722", "1000");
    }

    @Test(description = "otherID 充值金币")
    public void vipCharge_byOtherId() throws Exception {
        VIP_ReCharge_AID("42546714", "1000", "0");
    }

    @Test(description = "查询用户信息")
    public void Inquire_userInfo_byUserId() throws Exception {
        Inquire_userInfo("57377600");
    }

    @Test(description = "普通用户转上墙女神")
    public void set_user_goddess() throws Exception {
        Insert_Goddess("61174206");
    }

    @Test(description = "普通女神转上墙女神")
    public void set_user_wallGoddess() throws Exception {
        Insert_WallGoddess("61153574");
    }

    @Test(description = "解除女神设置")
    public void cancel_user_goddess() throws Exception {
        delete_Goddess("61151637");
    }
}

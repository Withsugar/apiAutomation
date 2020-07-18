package com.qa.commons;

import com.alibaba.fastjson.JSON;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.crypt.MD5;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rc.qa.commons.AppConfig.*;
import static com.rc.qa.commons.Common.*;
import static com.rc.qa.commons.Config.*;
import static com.rc.qa.commons.RcHttpUtil.*;
import static com.rc.qa.data.keySetData.user_friend_stick_keys;
import static util.crypt.CryptUtil.decryptResponse;
import static util.crypt.RsaUtil.encrypt;

/**
 * @create: 2019-11-16 14:50
 * @author: cloudsinn
 * @description: APi接口聚合类
 **/
public class LivUAndChatAPIFactory {

    private static String path = null;

    /**
     * ============================================================ Get =================================================
     */
//    获取所有的礼物, return [{k:v},{k:v}]
    public static String get_users_news_gifts_total(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/news/gifts/total?";
        MapUtil params = new MapUtil().put("appId", appId).put("deviceId", deviceId);
        return httpGet(baseURI, path, params.map, getUserInfoById(userId));
    }

    /**
     * 新服务器不再支持该接口，接口已下；
     * "转盘相关配置， 返回{key:[k:v], key:[k:v]}")
     */
    public static String get_getWheelResp(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getWheelResp?";
        MapUtil params = new MapUtil().put("appId", appId).put("deviceId", deviceId);
        return httpGet(baseURI, path, params.map, getUserInfoById(userId));
    }

    //    获取敏感词配置 返回{k:[{k:v},{k:v}]
    public static String get_sensitiveWord_config(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/sensitiveword/config?";
        MapUtil params = new MapUtil().put("appId", appId).put("deviceId", deviceId);
        return httpGet(baseURI, path, params.map, getUserInfoById(userId));
    }

    //    获取App配置信息1 return {k:v,k:v}
    public static String okGet_getAppConfig1(String baseURI) throws Exception {
        path = "/api/" + version + "/configs?";
        MapUtil params = new MapUtil().put("appId", appId).put("deviceId", deviceId).put("platformType", 2)
//                没有下面的put，返回数据将会增多
                .put("types", "matchLanguage,freeCommodity,timestamp,appConfig,snapshotNew,popupReqInterval,workload,exemptReqInterval,countryDirection,freeMatchTime");
        String url = Md5Url.md5Url(baseURI, path, params.map, new HashMap<>());
        return okGet(url, new HashMap<>());
    }

    //    获取App配置信息2 return {k:v,k:v}
    public static String okGet_getAppConfig2(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/configs?";
        MapUtil params = new MapUtil().put("types", "appVersion").put("appId", appId);
        return httpGet(baseURI, path, params.map, getUserInfoById(userId));
    }

    //    视频配置接口 return [{k:v,k:v}]
    public static String get_videoPlaySetting(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/videoPlaySetting?";
        MapUtil params = new MapUtil().put("deviceId", deviceId).put("appId", appId);
        return httpGet(baseURI, path, params.map, getUserInfoById(userId));
    }

    //    注册/登录前添加用户广告标识 return 空
    public static String get_before_advertInfo(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/before/advertInfo?";
        MapUtil params = new MapUtil().put("deviceId", deviceId).put("appId", appId)
                .put("advertId", "6e99586b-be00-408f-b960-3043efeb93c8");
        return httpGet(baseURI, path, params.map, getUserInfoById(userId));
    }

    /**
     * 获取消费品列表 return [{k:v,k:v}]
     * 1、定向匹配，随机匹配里选择定向男，或者定向女，单价9金币
     * 2、视频呼叫限制，加好友后，设置可以免费视频呼叫的次数，超限后需要支付价格才能好友视频，单价100金币，现在已取消该限制
     * 3、livu历史加好友，匹配历史记录里加好友需要支付的金币数 单价19金币
     * 4、安卓/IOS livu解封初始价格 账号被封后，解封需要支付的金币数 单价30金币
     * 5、livU 分钟计费 和分钟女好友视频通话价格 初始单价45金币
     * 6、livu男性匹配添加好友免费
     */
    public static String get_users_consumes(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/consumes?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取黑名单列表 return {k:v,k:[{k:v}],k:v...}
    public static String get_blacklists(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/blacklists?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取发送图片开关配置 return
    public static String get_imageSend_config(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/imagesend/config?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取新用户金币包或特殊金币包 return [{k:v,...},{k:v,...},{k:v,...}]
    public static String get_commodities_special(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/commodities/special?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取推广配置信息 return [空]
    public static String get_promotionInfo(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/promotions?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取用户当前的举报状态 return {空}
//    sealingReason 1:性别不符,2:色情裸露,3:其他，4:无原因，5:恶意诈骗，6:语言暴力，7:攻击歧视

    /**
     * 获取用户自己当前的被举报状态，查询未被举报的用户，返回为空
     * 已举报的并且成功的，返回sealingReason对应的值
     * sealingReason 1:性别不符,2:色情裸露,3:其他，4:无原因，5:恶意诈骗，6:语言暴力，7:攻击歧视
     * 该接口可用于查询封号状态
     */
    public static String get_users_reports_status(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/reports/status?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    视频配置接口 return [{k:v},{k:v}...243]
    public static String get_videoSetting(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/videoSetting?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    查询用户视频呼叫限制 return [{k:v,k:v...15}]"
    public static String get_users_powers(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/powers?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    返回通话，视频，好友上线提醒开关列表 return {k:{k:v,k:v},k:v}"
    public static String get_setting_getRemindSwitchList(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/setting/getRemindSwitch?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取墙来电和好友视频电话的转盘id return {k:[空]}" 该接口已下架
    public static String getWallCallAndFriendVideoWheelIds(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/raffle/config?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取区域配置，接口在App启动时调用
    public static String get_getAreaConfig(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/areas?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取大V用户组配置 return
    public static String get_getBigVSetting(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getBigvSetting?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取截图配置for老版本1 return
    public static String get_snapshots_newVersion_settings(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/snapshots/newversion/settings?";
        MapUtil params = map5ParamsUtil(userId).put("platformType", 2).put("deviceType", 1)
                .put("screenSize", "1080*2159").put("systemVersion", 28).put("deviceName", deviceName)
                .put("timeOutTimeMillis", 0);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /* 获取截图配置for老版本2 */
    public static String get_snapshots_settings(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/snapshots/settings?";
        MapUtil params = map5ParamsUtil(userId).put("platformType", 2).put("deviceType", 1)
                .put("screenSize", "1080*2159").put("systemVersion", 28).put("deviceName", deviceName)
                .put("timeOutTimeMillis", 0);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /* 获取截图配置for新版本3 */
    public static String get_getSnapshotConfig(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getSnapshotConfig?";
        MapUtil params = map5ParamsUtil(userId).put("platformType", 2).put("deviceType", 1)
                .put("screenSize", "1080*2159").put("systemVersion", 28).put("deviceName", deviceName)
                .put("timeOutTimeMillis", 0);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取角色相关活动 return {空}
    public static String get_getListRoleActivity(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getListRoleActivity?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取活动设置 新服务该接口被移除
    public static void get_activities_settings(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/activities/settings?";
        MapUtil params = map5ParamsUtil(userId);
        String response = httpGet(baseURI, path, params.map, getTokenById(userId));
        Assert.assertEquals(getJsonArray_size(response), 100000);
    }

    //    获取礼包列表 return
    public static String okGet_getGiftPackageList(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/gifts/bags?";
        MapUtil params = map5ParamsUtil(userId).put("giftBagTypes", "0");
        Map<String, Object> replaceMap = getTokenById(userId);
        String url = Md5Url.md5Url(baseURI, path, params.map, replaceMap);
        return okGet(url, replaceMap);
    }

    //    AEO签到状态 相关服务已下架，该接口基本放弃 return {k:{k:v,...},k:v}
    public static String get_getAEOSignStatus(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/getSignStatus?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    按分类平台获取贴纸 return [{k:v},{k:v}]
    public static String get_getDynamicStickers(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/dynamicStickers?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    查询用户翻译限制 return {k:v,k:v,k:v}
    public static String get_users_translate_limit(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/translate/limit?";
        MapUtil params = map5ParamsUtil(userId).put("version", "" + version + "").put("platformType", "2");
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取H5充值地址 return {k:v,k:v}
    public static String get_getH5PayWebsite(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/pay/h5/webSite?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取某个用户个人信息 return {k:v,k:v...29}
    public static String get_users_userId(String baseURI, String userId, String users) throws Exception {
        path = "/api/" + version + "/users/" + users + "?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取 女神墙|女神好友 价格并返回双方的ident(房间号) return {k:{k:v,k:v},K:v} √
    public static String get_getGoddessWallPrice(String baseURI, String userId, MapUtil params) throws Exception {
        path = "/api/" + version + "/getAllPrice?";
        params.map.putAll(map5ParamsUtil(userId).map);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    用户卡券列表 return {k:[],k:v}
    public static String get_user_ticket_list(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/user/ticket/list?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取赞的用户列表 return {k:v,k:v...4}"
    public static String get_users_praise_record(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/praise/record?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    随机匹配一个用户 匹配规则->新角色概率设置
    public static String get_users_matches_randoms(String baseURI, String userId, MapUtil params) throws Exception {
        path = "/api/" + version + "/users/matches/randoms?";
        params.map.putAll(map5ParamsUtil(userId).map);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    VIP体验卡 该功能已下架 return {}
    public static String get_getUserVipPrivilege(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getUserVipPrivilege?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    H5转盘入口，物品的背包 return [空]
    public static String get_getMyGiftBag(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getMyGiftBag?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取女神视频，先有女神在线，然后获取女神的视频
    public static String get_goddess_videos(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/goddess/videos?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取好友列表 return {k:v,k:[k:v,...],k:v,...}
    public static String get_users_friends(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/friends?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    查询关注好友上线提醒的好友列表 return {k:{k:{k:[k:v],k:v},k:v},k:v}
    public static String get_users_remind_friends(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/remind/friends?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取拉黑好友信息"
    public static String get_users_blackUserId(String baseURI, String userId, String blackUserId) throws Exception {
        path = "/api/" + version + "/users/" + blackUserId + "?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    通过secondId搜索好友 return {k:v,...6}
    public static String get_users_friend_searchById(String baseURI, String userId, int secondId) throws Exception {
        path = "/api/" + version + "/users/friend/searchById?";
        MapUtil params = map5ParamsUtil(userId).put("secondId", secondId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    查询与该用户的关系 被添加的用户，点击同意按钮调用该接口 return {k:v}
    public static String get_users_friends_relations(String baseURI, String userId, String friendUserId) throws Exception {
        path = "/api/" + version + "/users/friends/relations?";
        MapUtil params = map5ParamsUtil(userId).put("friendUserId", friendUserId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    查询用户信息及用户关系 return {k:v,k:v...30}
    //    secondId搜索用户，添加该用户为好友时，调用该接口
    public static String get_users_infos_relations(String baseURI, String userId, String friendUserId) throws Exception {
        path = "/api/" + version + "/users/infos/relations?";
        MapUtil params = map5ParamsUtil(userId).put("friendUserId", friendUserId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    好友列表，通过关键词搜索好友 return [{k:v,...}]
    public static String get_users_friend_search(String baseURI, String userId, String searchText) throws Exception {
        path = "/api/" + version + "/users/friend/search?";
        MapUtil params = map5ParamsUtil(userId).put("searchQ", searchText);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    点击好友触发，根据用户id，获取用户基本信息及用户关系 return [{k:v...29}]",
    public static String get_friend_info_relation(String baseURI, String userId, String friendUserId) throws Exception {
        path = "/api/" + version + "/users?";
        MapUtil params = map5ParamsUtil(userId).put("friendUserIdList", friendUserId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    "教育弹框标志，True/False")
    public static void get_popup_sign_education(String baseURI, String userId, String Boolean) throws Exception {
        path = "/api/" + version + "/popup/sign/education?";
        MapUtil params = map5ParamsUtil(userId).put("matchId", "294188312105582677");
        String response = httpGet(baseURI, path, params.map, getTokenById(userId));
        assert response.equals(Boolean);
    }

    //    返回用户色情标识，true/false
    public static String get_selectMatchEroticismSign(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/selectMatchEroticismSign?";
        MapUtil params = map5ParamsUtil(userId).put("matchUserId", "55647510").put("matchId", "294188312105582677");
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取一个用户的礼物计数详情
    public static String get_users_news_gifts_counts(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/news/gifts/counts?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取女神开关状态 return {online:true/false} 上墙女神多一个groupSwitch字段
    public static String get_getGoddessOpenStatus(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getGoddessOpenStatus?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    合作女神获取自己的等级和价格
    public static String get_getPartnerLevelAndPrice(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getPartnerLevelAndPrice?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取合作女性等级列表（视频通话价格列表），获取女
    public static String get_getLevelList(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getLevelList?";
        MapUtil params = map5ParamsUtil(userId).put("role", "0");
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取视频分钟总收益
    public static String get_getVideoMarkIncome(String baseURI, String userId, String roomId) throws Exception {
        path = "/api/" + version + "/getVediomarkIncome?";
        MapUtil params = map5ParamsUtil(userId).put("videoMark", roomId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    客户端请求分组->获取女神墙列表 return [{k:v,...},...10] √
    public static String get_selectAllInWallGroup(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/selectAllInWallGroup?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取女神状态列表{k:v,k:[k:v,...],...4} √
    public static String get_getGoddessStatusList(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getGoddessStatusList?";
        MapUtil params = map5ParamsUtil(userId).put("group", "33").put("countryCode", "182")
                .put("size", "20").put("page", "1");
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取商品（金币）列表(yaar有单独的第三方支付接口提供） return [{k:v},...7]
    public static String get_commodities(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/commodities?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取合作用户(合作女)的工作量 见： 运营活动-> KPI展示开关配置
    public static String get_workload(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/workload?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    "运营位入口，获取KPI开关"
    public static String get_getKpiSwitch(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getKpiSwitch?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    "色情之获取用户停止视频标识 {k:{k:v,...},k:v}")
    public static String get_video_getStopSign(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/video/getStopSign?";
        MapUtil params = map5ParamsUtil(userId).put("matchRoomId", "294368496997892112");
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    popup举报：男号对女号色情，系统弹出举报提示，然后女号举报男号 return 空
    public static String get_popupReport(String baseURI, String userId, MapUtil params) throws Exception {
        path = "/api/" + version + "/popupReport?";
        MapUtil tempParams = map5ParamsUtil(userId).put("version", version);
        params.map.putAll(tempParams.map);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    "添加app教育弹框数据 return 空")
    public static void get_addPopupEducation(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/popup/education?";
        MapUtil params = map5ParamsUtil(userId).put("matchUserId", "56700432").put("matchId", "294369295523119105");
        String response = httpGet(baseURI, path, params.map, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    //    @Test(enabled = false, description = "拉取小助手消息，通过PushId拉取 return ")
    public static void testGet_68(String baseURI, String userId, int count) throws Exception {
        path = "";
        String response = httpGet(baseURI, path, new MapUtil().map, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    @Test(enabled = false, description = "广告标识符，保存渠道号 return ")
    public static void testGet_69(String baseURI, String userId, int count) throws Exception {
        path = "";
        String response = httpGet(baseURI, path, new MapUtil().map, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "获取签到记录 return {k:v,k:v,...8}")
    public static String get_getSignRecord(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getSignRecord?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    "用户翻译限制累计 return {k:v,...}",
    public static String get_users_translate_add(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/translate/add?";
        MapUtil params = map5ParamsUtil(userId).put("version", "" + version + "").put("platformType", "2");
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    // 批量获取用户
    public static String get_batchUsers(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/batchUsers?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("gender", 0).put("num", 200)
                .put("language", null).put("type", 0);
        Map<String, Object> replaceMap = getTokenById(userId);
        return httpGet(baseURI, path, params.map, replaceMap);
    }

    //    "邮箱注册第一步,检查邮箱是否注册 false 未注册，true 已注册"
    public static String get_mail_check(String baseURI, String email) throws Exception {
        path = "/api/" + version + "/users/emails?";
        MapUtil params = new MapUtil().put("appId", appId).put("deviceId", deviceId).put("email", email);
        return httpGet(baseURI, path, params.map, new HashMap<>());
    }

    /**
     * ============================================================ Post ================================================
     */
//    "App 打开统计 return 204 无返回值")
    public static void post_apps_opens(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/apps/opens?";
        MapUtil params = new MapUtil().put("appId", appId).put("deviceId", deviceId);
        MapUtil mBody = map4BodyUtil();
        Map<String, Object> replaceMap = getUserInfoById(userId);
        replaceMap.put(EXPECT_CODE, 204);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, replaceMap);
        Assert.assertEquals(getJson_size(response), count);
    }

    //    插入请求用户埋点记录 return 空
    public static void post_addUserRequestBuryRecordAll(String baseURI, int count) throws Exception {
        path = "/api/" + version + "/addUserRequestBuryRecordAll";
        MapUtil mBody = new MapUtil().put("free_name2", 2).put("country_id", 42).put("device_id", deviceId)
                .put("model", "EML-AL00").put("platform_type", 2).put("version", 100).put("cl", "zh").put("brand", "HUAWEI")
                .put("create_time", "2019-09-24 21:33:18").put("time", System.currentTimeMillis()).put("capi", "1080*2159")
                .put("app_id", appId).put("event_id", "12-1-1-2")
                .put("advert_identifier", "6e99586b-be00-408f-b960-3043efeb93c8");
        String body = encryptBody(mBody.map);
        String response;
        if (host.equals("iosOnline")) {
            response = httpPost(baseURI, path, new MapUtil().map, body, new HashMap<>());
        } else {
            response = simplePost(baseURI + path, body);
        }
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "插入用户埋点记录 return 空",
    public static void postAddUserBuryRecordAll(String baseURI, int count) throws Exception {
        path = "/api/" + version + "/addUserBuryRecordAll";
        MapUtil mBody = new MapUtil().put("appId", appId).put("xaid", deviceId).put("productId", "EML-AL00")
                .put("platformType", 2).put("cl", "zh").put("cn", "GooglePlay").put("type", 6)
                .put("version", " + version + ").put("dT", 1569332002057L).put("inTime", 1569332002056L)
                .put("upTime", 1569332002056L).put("createTime", 1569332002057L).put("ttype", 21)
                .put("capi", "1080*2159").put("model", "EML-AL00").put("brand", deviceName);

        String body = encryptBody(mBody.map);
        String response;
        if (host.equals("iosOnline")) {
            response = httpPost(baseURI, path, new MapUtil().map, body, new HashMap<>());
        } else {
            response = simplePost(baseURI + path, body);
        }
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "用户下载 第一次打开app记录 return 200")
    public static void post_saveOpenRecord(String baseURI, int count) throws Exception {
        path = "/api/" + version + "/saveOpenRecord?";
        MapUtil params = new MapUtil().put("appId", appId);
        MapUtil mBody = map4BodyUtil().put("deviceName", deviceName)
                .put("pushToken", "eefm7FYFSUk:APA91bFfQERV45xtWvzQonS21Hzx1WyPjtqcFrSkJRNCVAVkfIn9lA_Rd2FoROeloMR4YP2ZnlyTirF5SsThHmEs18Am_omVfClhgirM21BPZnhUZ-0JJDa1N3D1lsFyKvo7LtJYKX7c");
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, new HashMap<>());
        Assert.assertEquals(getJson_size(response), count);
    }

    //    Google第三方登录 return{k:v...53}
    public static String post_users_threeParties_Google(String baseURI, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/threeParties?";
        MapUtil params = map4BodyUtil();
        MapUtil tempBody = map4BodyUtil().put("languageId", 2).put("timeZone", 8)
                .put("headImg", "https://lh6.googleusercontent.com/-JJ0gWFET1LE/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rdSQrVgBcW4HdfiqVI2VZ2A5Ss59A/s96-c/photo.jpg")
                .put("userName", "googleRegister").put("type", 7).put("countryId", 42)
                .put("languageName", "zh").put("adid", "6e99586b-be00-408f-b960-3043efeb93c8")
                .put("countryName", "CN").put("invitationCode", "are you going to scarborough fair")
                .put("threePartyEmail", "googleRegister@gmail.com")
                .put("systemVersion", 28).put("screenSize", "1080*2159").put("deviceName", deviceName);
        if (appName.equals("yaar")) {
            tempBody.put("stone", 6001).put("type", 8);
        }
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, new HashMap<>());
    }

    //    FaceBook 第三方登陆
    public static String post_users_threeParties_FaceBook(String baseURI, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/threeParties?";
        MapUtil params = map4BodyUtil();
        MapUtil tempBody = map4BodyUtil().put("languageId", 2).put("timeZone", 8)
                .put("headImg", "https://graph.facebook.com/v3.3/2504033606588270/picture?height=400&width=400&migration_overrides=%7Boctober_2012%3Atrue%7D")
                .put("userName", "facebookRegister").put("type", 2).put("countryId", 42)
                .put("languageName", "zh").put("adid", "6e99586b-be00-408f-b960-3043efeb93c8")
                .put("countryName", "CN").put("invitationCode", "").put("threePartyEmail", "facebookRegister@gmail.com")
                .put("systemVersion", 28).put("screenSize", "1080*2159").put("deviceName", deviceName)
                .put("facebookGroupUsers", new JsonArrayUtil()
                        .add(new jsonObjectUtil().put("appId", "438060306778489").put("id", "2504033606588270").json)
                        .add(new jsonObjectUtil().put("appId", "1635849179780756").put("id", "2486093891715575").json)
                        .jsonArray);
//      [{"appId":"438060306778489", "id":"2504033606588270"},{ "appId":"1635849179780756", "id":"2486093891715575" }]
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, new HashMap<>());
    }

    //    "将第三方的数据分析软件的ID存到本地数据库 返回值为空")
    public static void post_kochava_add(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/kochava/add?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("screenSize", "1080*2159").put("deviceName", deviceName)
                .put("appType", 0).put("kochavaAppId", "kolovu-android-ug8n67")
                .put("kochavaDeviceId", "KA3601569331998t68ed776d4f8a4f2a81b35b6deb89d13b")
                .put("userId", userId);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "添加用户广告标识 return 空",
    public static void post_users_advertInfo(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/users/advertInfo?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("advertId", "6e99586b-be00-408f-b960-3043efeb93c8").put("userId", userId);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "用户离开视频，提交视频记录 return {k:v}",
    public static String post_users_videos(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/users/videos?";
        MapUtil params = map5ParamsUtil(userId).put("matchId", "294131951447572496");
        MapUtil mBody = map4BodyUtil().put("feeType", 0).put("gender", 1).put("genderCondition", 0).put("goddessLocation", 2)
                .put("goddessVideo", 1).put("inappFlag", 1).put("inmatchFlag", 1).put("isFriend", 1).put("isPay", 0)
                .put("isRealUser", true).put("userId", userId).put("matchFlag", true).put("matchMode", 1)
                .put("matchedGender", 1).put("onlineStatus", -1).put("requestType", 0).put("type", 2)
                .put("version", version).put("videoTime", "6627").put("matchUserId", userId);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    "关注好友上线提醒 return ")
    public static String post_friend_onlineSwitch_update(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/friend/onlineSwitch/update?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("userId", userId);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    @Test(enabled = false, description = "推送消息 单向给好友发消息，能发出去，未验证对方能否收到",
    public static void post_pushes2(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/pushes?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("function", 0);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    /**
     * 客服push发送，需要pushToken支持，
     * pushToken从客户端获取，然后在修改用户昵称中上传pushToken到服务器，最后运行此接口就能接通
     * pushToken可以从 saveOpenRecord？请求Body中获取
     */
    public static void get_services_pushes(String baseURI, String userId) throws Exception {
        path = "/api/services/pushes?";
        MapUtil params = map5ParamsUtil(userId).put("serverId", "serverId");
        Map<String, Object> replaceMap = getTokenById(userId);
        replaceMap.put("userId", "14924981");
        httpGet(baseURI, path, params.map, replaceMap);
    }

    /*发布服务端push消息*/
    public static String get_services_pushMsg(String baseURI, String userId) {
        path = "/api/services/pushmsg?";
        MapUtil params = map5ParamsUtil(userId).put("serverId", "serverId")
                .put("msg", "push.elva.alert").put("title", "message tile").put("body", "message body!");
        Map<String, Object> replaceMap = getTokenById(userId);
        replaceMap.put("uid", "14924981");
        String uri = Md5Url.md5Url(baseURI, path, params.map, replaceMap);
        return simpleGet(uri);
    }

    //    "收藏和取消收藏好友 return ", groups = {user_group, addFriend})
    public static String post_users_friend_stick(String baseURI, String userId, MapUtil params) throws Exception {
        path = "/api/" + version + "/users/friend/stick?";
        params.map.putAll(map5ParamsUtil(userId).map);
        String body = encryptBody(map4BodyUtil().map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    "修改好友备注 return ",
    public static String post_users_friend_remark(String baseURI, String userId, MapUtil params, String remark) throws Exception {
        path = "/api/" + version + "/users/friend/remark?";
        params.map.putAll(map5ParamsUtil(userId).map);
        MapUtil mBody = map4BodyUtil().put("remark", remark);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    "举报好友，1:性别不符, 2:色情裸露， 5：欺诈，6：语言攻击，3：其他",
    public static String post_users_report_friend_add(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/report/friend/add?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("site", 1);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    添加好友 添加secondId搜索到的用户为好友 return {k:v,...12}
    public static void post_users_friends(String baseURI, String userId, String friendId) throws Exception {
        path = "/api/" + version + "/users/friends?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("userFriendId", friendId);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        lostKeyAssert(response, user_friend_stick_keys);
    }

    //    发一条消息告诉好友：0627 希望与您成为好友
    public static void post_pushes1(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/pushes?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("function", 0);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    //    给用户点赞 return {k:v}
    public static String post_users_praise(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/praise?";
        MapUtil params = map5ParamsUtil(userId);
        Map<String, Object> replaceMap = getTokenById(userId);
        String nonce = "1569394845733";
        MapUtil tempBody = map4BodyUtil().put("nonce", nonce).
                put("key", MD5.getMD5Code(nonce + replaceMap.get("accessToken")));
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, replaceMap);
    }

    //    将一个用户加入黑名单 {k:v}"
    public static String post_blacklists_userId(String baseURI, String userId, String blackId) throws Exception {
        path = "/api/" + version + "/blacklists/" + blackId + "?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("deviceName", deviceName);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    赠送礼物给他人 表 rc_new_gift
    public static void post_users_news_gifts(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/news/gifts?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("timeOutTimeMillis", 10000).put("timeZone", 8);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        if (host.equals("online")) {
            Assert.assertEquals(getJson_value(response, "error"), "金币不足");
        } else {
            List<String> keys = Arrays.asList("freeCount", "giftId", "gold", "receiveUserId", "userId");
            lostKeyAssert(response, keys);
        }
    }

    //    女神心跳检查 return 204 空 √
    public static void post_goddess_healthCheck(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/goddess/healthCheck?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = new MapUtil().put("appId", appId).put("deviceId", deviceId)
                .put("deviceType", 1).put("platformType", 2).put("userId", userId);
        Map<String, Object> replaceMap = getTokenById(userId);
        replaceMap.put(EXPECT_CODE, 204);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, replaceMap);
        Assert.assertEquals(getJson_size(response), count);
    }

    //    视频挂断记录 该接口不再使用
    public static void post_addVideoRejectRecord(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/addVideoRejectRecord?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("callMode", 1).put("feeType", 0).put("goddessVideo", 1).put("isFriend", 2)
                .put("matchFlag", 0).put("remoteUserId", "40069061");
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    查询女神墙列表用户的在线状态0=离线，1=忙线，2=在线 [1,2,2,2,2,2] √
    public static String post_getGoddessStatus(String baseURI, String userId, int groupId, int[] list) throws Exception {
        path = "/api/" + version + "/getGoddessStatus?";
        MapUtil params = map5ParamsUtil(userId).put("group", groupId);
        MapUtil mBody = map4BodyUtil().put("deviceName", deviceName).put("userIdList", list);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    视频扣费,返回 goldNum, \"error\":\"金币不足\"
    public static String post_setGoddessVideoReduce(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/setGoddessVedioReduce?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("areaCode", -1).put("callMode", 1).put("isFriend", 2).put("model", -1);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    好友通话记录,好友视频连通前调用该接口 return 空
    public static String post_friend_video(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/friend/video?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("busyStatus", 0).put("feeType", 0).put("goddessCall", 1).put("gold", 60)
                .put("inappFlag", 1).put("matchFlag", 0).put("onlineStatus", 1)
                .put("requestType", 1).put("userId", userId);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    女神墙呼叫记录 return 空 √
    public static String post_wall_call(String baseURI, String userId, String videoUserId) throws Exception {
        path = "/api/" + version + "/wall/call?";
        MapUtil params = map5ParamsUtil(userId);
        Map<String, Object> replaceMap = getTokenById(userId);
        replaceMap.put(EXPECT_CODE, 204);
        MapUtil mBody = map4BodyUtil().put("onlineStatus", 1).put("roomId", "294308640838386741").put("type", 1)
                .put("userId", userId).put("videoUserId", videoUserId);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, replaceMap);
    }

    // "用户举报(一个用户只能举报一次，ios用户独享) return 空"
    public static String post_user_reports(String baseURI, String userId, String reportedUserId) throws Exception {
        path = "/api/" + version + "/users/reports?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("gender", 1).put("reportPage", 3).put("reportReason", 1)
                .put("reportedUserId", reportedUserId);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    "小助手领取金币 return ")
    public static String post_messages_golds(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/messages/golds?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("messageId", "201909284").put("userId", userId);
        if (host.equals("245"))
            mBody.put("messageId", "20010");
        if (host.equals("yaar245"))
            mBody.put("messageId", "20155");
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    @Test(enabled = false, description = "推送记录接口， 提交push打开记录")
    public static void testPost_31(String baseURI, String userId, int count) throws Exception {
        path = "";
        MapUtil mBody = map4BodyUtil();
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, new MapUtil().map, body, getUserInfoById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    description = "删除用户 return "
    public static String post_user_delete(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/41147197-2a5e-4e48-9fce-382412b41640b9144a/ce-7922-44b8-8212-9fe97d6eb68af415560a" +
                "/-1881-496e-816d-e17c3b772fe150bb73ae-916f-4ad5-b10c-c6e25f735d1e9ae6e28d-db7a-4d40-8ab7-2d85" +
                "629de761d/2ffbc0d-4aa9-43df-a09e-221446a910a371f1b/8ad-1b43-4d5d-b236-3769548eb93b04abdbd2-7/c0" +
                "2-4d48-8051-97a78db06d547c5cc00e-eed7-4622-/a7c0-39b1844dbd9868e5e50f-12ea-45c5-bd7b-4a1db19e177d?";
        MapUtil params = map5ParamsUtil(userId);
        Map<String, Object> replaceMap = getTokenById(userId);
        replaceMap.put(EXPECT_CODE, 204);
        MapUtil tempBody = map4BodyUtil().put("userId", userId)
                .put("a77450ae83840486191177d49745f1fb4", MD5.getMD5Code(userId + replaceMap.get("accessToken")));
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, replaceMap);
    }

    //    "完成任务领金币 return 空")
    public static String post_addReceiveGoldRecord(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/addReceiveGoldRecord?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("id", 118);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    "用户签到 return 空 基本没用了")
    public static void post_addSignGoldRecord(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/addSignGoldRecord?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("screenSize", "1080*2159").put("systemVersion", 28).put("deviceName", deviceName);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "添加用户停止视频记录 return 空")
    public static void post_video_addHangupRecord(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/video/addHangupRecord?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("location", 3).put("matchMode", 3).put("matchRoomId", "294376120864735285")
                .put("matchedUserId", "56700448").put("videoTime", 8255).put("screenSize", "1080*2159")
                .put("systemVersion", 28).put("deviceName", deviceName).put("userId", userId);
        String body = encryptBody(mBody.map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "更新设置提醒开关状态 return {k:{k:v},k:v} ")
    public static String post_setting_updateRemindSwitch(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/setting/updateRemindSwitch?";
        MapUtil params = map5ParamsUtil(userId);
        mBody.map.putAll(map4BodyUtil().map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    删除好友 无返回值
    public static void post_friend_delete(String baseURI, String userId, String friendUserId) throws Exception {
        path = "/api/" + version + "/friend/delete?";
        MapUtil params = map5ParamsUtil(userId).put("friendUserId", friendUserId);
        String body = encryptBody(map4BodyUtil().map);
        String response = httpPost(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_value(getJson_value(response, "data"), "status"), "true");
    }

    /**
     * ============================================================ put =================================================
     */
    //   "修改用户昵称，上传pushToken 也是在这里，上传token用于services/pushes push服务，pushToken从 saveOpenRecord？请求Body中获取",
    public static void put_users_userName(String baseURI, String userId, String userName) throws Exception {
        path = "/api/" + version + "/users?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("userName", userName)
                .put("pushToken", "eefm7FYFSUk:APA91bFfQERV45xtWvzQonS21Hzx1WyPjtqcFrSkJRNCVAVkfIn9lA_Rd2FoROeloMR4YP2ZnlyTirF5SsThHmEs18Am_omVfClhgirM21BPZnhUZ-0JJDa1N3D1lsFyKvo7LtJYKX7c");
        String body = encryptBody(mBody.map);
        String response = multiPut(baseURI, path, params.map, getTokenById(userId),
                rs -> rs.multiPart("data", body));
        Assert.assertEquals(getJson_value(response, "userName"), userName);
    }

    //    "用户退出登录 return 空",
    public static void put_users_statuses_logout(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/users/statuses?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil();
        String body = encryptBody(mBody.map);
        String response = httpPut(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    //    "提交用户的地理位置与国家信息 return {}k:v,...26",
    public static String put_users_locations(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/locations?";
        MapUtil params = map5ParamsUtil(userId);
        mBody.map.putAll(map4BodyUtil().map);
        String body = encryptBody(mBody.map);
        return httpPut(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    女神用户修改在线状态 return 空 √（0：离线，1：忙线，2：在线）
    public static void put_editGoddessStatus(String baseURI, String userId, int status) throws Exception {
        path = "/api/" + version + "/editGoddessStatus?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("status", status);
        String body = encryptBody(mBody.map);
        String response = httpPut(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    //    "记录女神拒绝次数，用于用户被动下线 return 空 √", groups = {goddess_group})
    public static void put_updateGoddessRejectCount(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/updateGoddessRejectCount?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("callMode", 1);
        String body = encryptBody(mBody.map);
        String response = httpPut(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    //    "修改视频之后女神用户的在线状态 √", groups = {goddess_group})
    public static void put_editAfterVideoGoddessStatus(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/editAfterVedioGoddessStatus?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("status", 2);
        String body = encryptBody(mBody.map);
        String response = httpPut(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    //    "女神更新自己价格档位 return {data:{k:v},k:v} √", groups = {goddess_group})
    public static String put_updatePriceLevel(String baseURI, String userId, MapUtil params) throws Exception {
        path = "/api/" + version + "/updatePriceLevel?";
        MapUtil tempParams = map5ParamsUtil(userId).put("role", "0");
        params.map.putAll(tempParams.map);
        MapUtil mBody = map4BodyUtil();
        String body = encryptBody(mBody.map);
        return httpPut(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    "女神接听电话，到视频结束中间的各种事件 return 空 √", groups = {goddess_group})
//    body 不正确，需要重抓
    public static void put_addGoddessEventRecord(String baseURI, String userId, int count) throws Exception {
        path = "/api/" + version + "/addGoddessEventRecord?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil();
        String body = encryptBody(mBody.map);
        String response = httpPut(baseURI, path, params.map, body, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), count);
    }

    /**
     * ============================================================ multiPost ===========================================
     */
    //    "举报用户(带有举报截图,只有性别不符和色裸露才使用此接口,别的原因调用老接口)",
    public static String post_users_reports_images(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/reports/images?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("matchUserId", "294307221161903149");
        mBody.map.putAll(tempBody.map);
        Map<String, Object> replaceMap = getTokenById(userId);
        replaceMap.put(EXPECT_CODE, 204);
        String body = encryptBody(mBody.map);
        return multiPost(baseURI, path, params.map, replaceMap,
                rs -> rs.multiPart("data", body)
                        .multiPart("reportImage", new File("./src/test/result/pronImage3.png")));
    }

    // 邮箱注册
    public static String multiPost_mail_signUp(String baseURI, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/emails?";
        MapUtil params = new MapUtil().put("appId", appId).put("deviceId", deviceId).put("timeZone", "8");
        mBody.put("appId", appId).put("birthday", 938434242772L).put("languageName", "zh").put("languageId", 2)
                .put("platformType", 2).put("deviceId", deviceId);
        String body = encryptBody(mBody.map);
        if (appName.equals("yaar")) {
            return multiPost(baseURI, path, params.map, new HashMap<>(),
                    rs -> rs.multiPart("data", body)
                            .multiPart("headImg", new File("./src/test/result/ImageGirl.jpeg")));
        } else {
            return multiPost(baseURI, path, params.map, new HashMap<>(),
                    rs -> rs.multiPart("data", body));
        }
    }

    /**
     * ============================================================ multiUpload =========================================
     */

    /*matchUserId 是房间号， ID是截图配置ID，见监管->截图配置->245数据表：rc_auth_video_snapshot_config
    matchUserId 是随机匹配里的ident对应的key值
    如果指定配置配置的截图限制次数为无限制(-1)，截图接口返回-1，也就是无限制
    如果指定配置配置的截图限制次数有值，返回的数量是已经截图的次数。*/
    public static String multi_upload_videos_snapshots(String baseURI, String userId, MapUtil params) throws Exception {
        path = "/upload/10131001/videos/snapshots?";
        MapUtil tempParams = map5ParamsUtil(userId).put("gender", 1).put("location", 6).put("videoType", "1")
                .put("page", "0")
                .put("model", "0").put("screenSize", "1080*2159").put("deviceType", "1").put("platformType", "2");
        params.map.putAll(tempParams.map);
        return multiPost(baseURI, path, params.map, getTokenById(userId),
                rs -> rs.multiPart("snapshot", new File("./src/test/result/pronImage3.png")));
    }

    //    上传图片
    public static String multi_uploadAlbumImage(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/upload/10131001/uploadAlbumImage?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", "2");
        mBody.map.putAll(map4BodyUtil().map);
        String relativelyPath = System.getProperty("user.dir");
        String body = encryptBody(mBody.map);
        return multiPost(baseURI, path, params.map, getTokenById(userId),
                rs -> rs.multiPart("data", body)
                        .multiPart("image", new File("./src/test/result/ImageGirl.jpeg")));
    }

    //    上传视频
    public static String multi_uploadAlbumVideo(String baseURI, String userId, String videoPath, String videoPicPath) throws Exception {
        path = "/upload/" + version + "/uploadAlbumVideo?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", "2");
        return multiPost(baseURI, path, params.map, getTokenById(userId),
                rs -> rs.multiPart("video", new File(videoPath))
                        .multiPart("videoPic", new File(videoPicPath)));
    }

    /**
     * ============================================================ Patch ===============================================
     */
    //    "自动登录：Kill已登录App后启动App触发自动登录 return {k:v,...}")
    public static String patch_users_autoLogin(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("languageId", 2).put("timeZone", 8);
        String body = encryptBody(mBody.map);
        return httpPatch(baseURI, path, params.map, body, getTokenById(userId));
    }

    //    邮箱登陆
    public static String patch_MailLogin(String baseURI, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/emails";
        mBody.put("appId", appId).put("deviceId", deviceId);
        String body = encryptBody(mBody.map);
//        System.out.println(decryptRequest(body));
        return httpPatch(baseURI, path, new HashMap<>(), body, new HashMap<>());
    }

    /**
     * ============================================================ Delete ==============================================
     */
    //    "取消随机匹配 running pass return 204",
    public static void put_users_matches_cancel(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/matches?";
        MapUtil params = map5ParamsUtil(userId);
        String response = httpDelete(baseURI, path, params.map, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    //    "将用户从黑名单中移除 running pass return 204",
    public static void put_blacklists_userId_remove(String baseURI, String userId, String blacklists) throws Exception {
        path = "/api/" + version + "/blacklists/" + blacklists + "?";
        MapUtil params = map5ParamsUtil(userId);
        String response = httpDelete(baseURI, path, params.map, getTokenById(userId));
        Assert.assertEquals(getJson_size(response), 0);
    }

    /**
     * 新增接口:=========================================new added=======================================================
     */

    //    获取用户相册发布限制状态
    public static void get_getUserProfileLimitStatus(String baseURI, String userId, String limit) throws Exception {
        path = "/api/" + version + "/getUserProfileLimitStatus?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        Map<String, Object> replaceMap = getTokenById(userId);
        String response = httpGet(baseURI, path, params.map, replaceMap);
        Assert.assertEquals(getJson_value(getJson_value(response, "data"), "limitStatus"), limit);
    }

    // 获取自己profile 图片
    public static String get_getUserAlbumPics(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/getUserAlbumPics?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    // 获取其他用户的相册,图片+视频
    public static String get_getOtherUserAlbum(String baseURI, String userId, String otherId) throws Exception {
        path = "/api/" + version + "/getOtherUserAlbum?";
        MapUtil params = map5ParamsUtil(userId).put("targetUserId", otherId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取所有语言列表
    public static String get_profile_language_all(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/profile/language/all?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2).put("devideLanguageId", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //    获取所有兴趣列表
    public static String get_profile_interest_all(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/profile/interest/all?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2).put("devideLanguageId", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    //更改：语言，兴趣，个人简介，生日的接口
    public static String post_profile_users_languageInterest(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/languageInterest?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil tempBody = map4BodyUtil().put("userId", userId);
        mBody.map.putAll(tempBody.map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    // 获取合作女钱包的开关
    public static String get_users_wallexSwitch(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/users/wallexSwitch?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /**
     * 合作女邮箱绑定接口一览：
     * <p>
     * Step 1
     * 是否需要验证或弹框（开启后，每次打开Livu都会提示邮箱认证）
     */
    //    "是否需要验证或弹框 return ")
    public static String get_cooperationEmail_entranceCue(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/cooperationemail/entranceCue?";
        MapUtil params = map5ParamsUtil(userId).put("platformType", "2");
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /**
     * Step 2
     * "检查合作女的邮箱是否验证 return {k:[k:v],k:v}"，该接口在输入邮箱点击认证的时候请求
     * 检查邮箱是否验证,0未认证，1认证了 3非合作女无需要验证 4方法出错
     */
    public static String get_cooperationEmail_userIdentification(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/cooperationemail/useridentification?";
        MapUtil params = map5ParamsUtil(userId);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /**
     * Step 3
     * 发送邮件,跳转到密码的url：加密显示并用于跳转，前端接收到需要拿endTime判断该链接是否有效，并进行相应操作
     */
    public static String post_sendEmailBySparkPost(String baseURI, String userId, String emailAccount) throws Exception {
        path = "/api/" + version + "/cooperationemail/sendemailbysparkpost?";
        MapUtil params = map5ParamsUtil(userId);
        MapUtil mBody = map4BodyUtil().put("emailAcount", emailAccount).put("userId", userId);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getUserInfoById(userId));
    }

    /**
     * Step 4
     * 验证邮件URL是否过期
     */
    public static void get_cooperationEmail_isExpireDate(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/cooperationemail/isExpireDate?";
        String params = "userId=" + userId + "&endTime=" + System.currentTimeMillis();
        String response = decryptResponse(simpleGet(baseURI + path + params));
        System.out.println(response);
        Assert.assertEquals(getJson_value(response, "data"), "true");
    }

    /**
     * Step 5
     */
    public static String save_password(String baseURI, String userId, String emailAccount, String password) {
        path = ":8902/api/" + version + "/cooperationemail/savepassword?userId=" + userId;
        MapUtil mBody = new MapUtil().put("userId", userId).put("emailAcount", emailAccount)
                .put("password", MD5.getMD5Code(password, true)).put("appId", appId)
                .put("platformType", "2").put("timestamp", System.currentTimeMillis());
        return simplePost(baseURI + path, JSON.toJSONString(mBody.map));
    }

    /**
     * 这个地址是：点击邮件中的链接，认证通过后跳转到的地址，提交密码会把body信息返给Step 5的接口，最终Step 5完成密码保存工作
     */
    public static String livU_mail_save_password(String baseURI, String userId, String emailAccount, String password) throws Exception {
        path = "/api/1/cooperationemail/savepassword?" + "userId=" + userId + "&endTime=" + System.currentTimeMillis();
        MapUtil mBody = new MapUtil()
                .put("password", MD5.getMD5Code(password, true)).put("emailAcount", emailAccount);
        String body = encryptBody(mBody.map);
        return simplePost(baseURI + path, body);
    }

    /**
     * Yaar 合作女绑定邮箱账号登陆
     */
    public static String post_users_yaarLogin(String baseURI, String userAccount, String password) throws Exception {
        path = "/api/" + yaar_Api + "/users/yaarlogin?";
        MapUtil params = new MapUtil().put("appId", yaar_AppId).put("platformType", "2").put("deviceId", deviceId);
        MapUtil mBody = new MapUtil().put("appId", yaar_AppId).put("platformType", "2").put("deviceId", deviceId)
                .put("stone", 6001).put("timeZone", "8")
                .put("userAccount", userAccount).put("password", encrypt(password));
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, new HashMap<>());
    }

    /**
     * livu H5 Facebook第三方登陆
     */
    public static String post_H5_facebook_login(String baseURI, String threePartyId) throws Exception {
        path = "/api/h5/1/users/loginThreeParties";
        MapUtil mBody = new MapUtil().put("appId", appId).put("deviceId", deviceId).put("deviceType", 3)
                .put("platformType", 3).put("birthday", 938434242772L).put("gender", 2)
                .put("headImg", "").put("languageId", 2).put("threePartyId", threePartyId)
                .put("userName", "cloudsinn").put("systemVersion", "MacIntel").put("threePartyEmail", "")
                .put("deviceName", deviceName).put("languageName", "Chinese").put("screenSize", "440*900");
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, new MapUtil().map, body, new HashMap<>());
    }

    // 查询最近活跃的用户
    public static String get_getRecentActiveUsers(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/user/recommend/getRecentActiveUsers?";
        MapUtil params = new MapUtil().put("deviceId", deviceId).put("appId", appId).put("timestamp", "123")
                .put("userId", userId).put("nonce", "nonce").put("deviceType", 1).put("platformType", "2");
        Map<String, Object> replaceMap = getTokenById(userId);
        return httpGet(baseURI, path, params.map, replaceMap);
    }

    /**
     * 用户购买商品接口
     * <p>
     * 消耗值：consumeId 对应值见 数据库 rc_consume表
     * <p>
     * app_id:20000
     * id	price	name
     * 8	9	定向匹配
     * 9	100	livu视频呼叫限制
     * 10	200	livu历史加好友
     * 12	30	livu安卓解封初始价格
     * 14	30	IOSLivu解锁初始金币
     * 16	45	livU 分钟计费
     * <p>
     * app_id:19999
     * id	price	name
     * 3	200	历史加好友
     * 5	9	匹配
     * 7	100	视频呼叫
     * 11	30	liveChat安卓解封初始价格
     * 13	30	star解封初始金币
     * 17	45	liveChat 分钟计费
     * <p>
     * matchType 匹配类型 1 定向男 2 定向女 3 女神匹配 -1 默认，无意义
     * <p>
     * roomId 来自匹配到真实用户返回的ident值
     */
    public static String post_users_commodities(String baseURI, String userId, MapUtil mBody) throws Exception {
        path = "/api/" + version + "/users/commodities?";
        MapUtil params = map5ParamsUtil(userId);
        mBody.map.putAll(map4BodyUtil().map);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    // 点赞或取消赞,只有第一次赞会收到通知
    public static String get_profile_like_like(String baseURI, String userId, String likedId) throws Exception {
        path = "/api/" + version + "/profile/like/like?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2)
                .put("site", 1).put("likerId", userId).put("likedId", likedId).put("likeOrNot", true);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    // 入口信息展示，有多少个人喜欢
    public static String get_profile_like_enter(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/profile/like/enter?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    // 小助手新增接口
    public static String getMessageList(String baseURI, String userId) throws Exception {
        path = "/api/assistant/getMessageList?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    // 领取金币
    public static String getMessageGolds(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/messages/golds?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    // 关闭领取金币弹窗,return true
    public static String closeMessage(String baseURI, String userId) throws Exception {
        path = "/api/assistant/closeMessage?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /**
     * 闪聊接口汇总
     * part1 主播相关接口
     */
    /* 获取主播闪聊入口 */
    public static String get_anchor_entrance(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/anchor/entrance?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /* 获取主播闪退次数 */
    public static String get_anchor_quitCount(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/anchor/quitCount?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /* 记录主播闪退次数 */
    public static String post_anchor_quitCount(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/anchor/quitCount?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        MapUtil mBody = map4BodyUtil().put("userId", userId);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    /**
     * part2 视频聊天接口
     */
    /* 记录用户状态，status字段: busy，free; source字段: quickChat 或者 videoChat */
    public static String post_user_status(String baseURI, String userId, String source, String status) throws Exception {
        path = "/api/" + version + "/user/status?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        MapUtil mBody = map4BodyUtil().put("userId", userId).put("source", source).put("status", status);
        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    /* 获取用户状态列表 remove true 移出， false 不移出 */
    public static String get_user_status_list(String baseURI, String userId, String userId1plus) throws Exception {
        path = "/api/" + version + "/user/status/list?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2).put("userIds", userId1plus);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /* 记录视频状态,联通：connect， 拒绝：refuse_call， 超时：wait_timeOut */
    public static String post_video_videoRecord(String baseURI, String userId, String callUserId) throws Exception {
        path = "/api/" + version + "/video/videoRecord?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        MapUtil mBody = map4BodyUtil().put("videoTime", 0).put("userId", userId)
                .put("status", "busy").put("extend", "connect").put("callUserId", callUserId);

        String body = encryptBody(mBody.map);
        return httpPost(baseURI, path, params.map, body, getTokenById(userId));
    }

    /* 配置相关接口->获取闪聊配置 返回：冻结时长 和 闪退秒数 两个字断 */
    public static String get_quickChatConfig(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/quickChatConfig?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }

    /* 闪聊主播调用接口-> 分发用户 */
    public static String get_quickChatUser(String baseURI, String userId) throws Exception {
        path = "/api/" + version + "/quickChatUser?";
        MapUtil params = map5ParamsUtil(userId).put("deviceType", 1).put("platformType", 2);
        return httpGet(baseURI, path, params.map, getTokenById(userId));
    }
}

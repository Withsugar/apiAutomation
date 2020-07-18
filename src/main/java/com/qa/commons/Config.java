package com.qa.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @create: 2019-11-16 14:50
 * @author: cloudsinn
 * @description: 基本配置信息类
 **/
public class Config {

    /**
     * 通用user相关信息一栏:==========================Common Users=======================================================
     */
    public static String aeoDeviceId = "cb29a520c9a1ea76";
    public static String deviceId = "e269dda07f0f85ed";
    static final String deviceName = "tissot_sprout";
//    static final String deviceName = "HWEML";

    public static List<String> accountAndId = Collections.synchronizedList(new ArrayList<>());
    public static List<String> userIds = Collections.synchronizedList(new ArrayList<>());
    /**
     * 更多女神账号:
     * u001@qq.com   123456
     * whiu1@1.com  whu41@41.com    hwylu1@1.1  wzu888@888.com  密码都是：111,111
     * <p>
     * api Version 一栏:==========================api version===========================================================
     * liveChat, livU, yaar
     */

    static final String yaar_Api = "yaar/10008001";
    static final String tumile_Api = "30130001";
    static final String livU_Api = "10136001";
    static final String ios_Api = "131";

    /**
     * AppId 一栏:======================================appId===========================================================
     */

    static final String tumile_AppId = "19999";
    static final String livU_AppId = "20000";
    static final String yaar_AppId = "66666";

    /**
     * 正式环境服务器地址 一栏:==========================online host=======================================================
     */
    static final String new_live_chat = "https://newlivechat.rcplatformhk.com";
    static final String ios_live_chat = "https://ioslivechat.rcplatformhk.com";
    static final String rc_log = "http://rclog.rcplatformhk.com";

    public static final String android_pay = "https://androidpay.rcplatformhk.com";
    public static final String rc_payment = "https://rcpayment.rcplatformhk.com";
    public static final String ios_pay = "https://iospay.rcplatformhk.com";
    public static final String livu_chat = "https://www.livuchat.com";
    /**
     * 测试环境服务器地址 一栏:==========================test host=======================================================
     */
    static final String new_live_chat_aws_tokyo = "http://test-newlivechat.playrc.net";
    static final String new_live_chat_op_tokyo = "http://18.176.34.107";
    static final String new_live_chat_182 = "http://173.255.197.182";
    static final String new_live_chat_245 = "http://39.100.88.245";
    /**
     * testNg groups 一栏:==========================testNg groups=======================================================
     */
    public static final String lostParams = "lostParams";
    public static final String fail182 = "fail182";     //182报错的
    public static final String fail245 = "fail245";     //245报错的
    public static final String test = "return500";
    public static final String patch = "patch";     //加了补丁的
    public static final String pass = "pass";

    /**
     * Mysql groups 一栏:==========================mysql groups=========================================================
     */
    /* host user and password */
    static final String sql245_tokyoPwd = "mofnuWs2YZvLMed&12VKmVFr";
    static final String sqlTokyoHost = "3.115.153.122"; // sqlTokyoHost只能在 "PLRL_5G" 上运行
    static final String sql245Host = "47.92.116.139";
    static final String sql245_TokyoUser = "testdb";

    static final String sql182Host = "173.255.197.182";
    static final String sql182Pwd = "rc_test@123";
    static final String sql182User = "rc_test";

    static final String sqlOnlinePwd = "d3d123d+YfNQNVdVFr";
    static final String sqlOnlineLogHost = "10.0.5.72";
    static final String sqlOnlineHost = "10.0.5.170";
    static final String sqlOnlineUser = "rc_read";

    /**
     * request groups 一栏:==========================request header=======================================================
     */
    static final String cookie = "xxljob_adminlte_settings=on; XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437";
    static final String header2 = "application/x-www-form-urlencoded; charset=UTF-8";
    static final String header1 = "application/json; charset=UTF-8";

    /*
     * 查国家经纬：https://api.map.baidu.com/lbsapi/getpoint/index.html
     * 查国家编码：https://yumingsuoxie.51240.com/
     */
    public static Object[][] CountryInfo() {
        return new Object[][]{
                {17, "AT", 14.602677, 47.472396}, {20, "BH", 50.544764, 26.072152}, {24, "BE", 4.448377, 50.482797},
                {30, "BR", -51.895097, -14.272296}, {34, "MM", 96.020652, 21.876783}, // 0~4

                {49, "CZ", 15.459452, 49.781579}, {50, "DK", 9.498623, 56.23271}, {54, "EG", 30.899946, 26.789856},
                {59, "FI", 25.744989, 61.91855}, {60, "FR", 2.225535, 46.199896}, // 5~9

                {65, "DE", 10.482859, 51.168038}, {68, "GR", 21.874617, 39.063676}, {77, "HU", 19.437476, 47.126603},
                {79, "IN", 78.929247, 20.615609}, {80, "ID", 113.933688, -0.734001}, // 10~14

                {83, "IR", 53.690058, 32.437904}, {85, "IT", 12.497528, 41.896644}, {90, "KH", 105.009073, 12.606623},
                {94, "KW", 47.679824, 29.343653}, {96, "LA", 102.466463, 19.850014}, // 15~19

                {104, "LU", 6.132222, 49.617785}, {108, "MY", 102.044468, 4.189991}, {126, "NL", 5.28178, 52.128916},
                {132, "NO", 8.52615, 60.467587}, {133, "OM", 55.998122, 21.477031}, // 20~24

                {140, "PL", 19.140824, 51.915348}, {142, "PT", -8.214393, 39.461179}, {144, "QA", 51.219241, 25.422983},
                {154, "SA", 45.095547, 23.936949}, {164, "ES", -3.698915, 40.432262}, // 25~29

                {165, "LK", 80.649627, 7.893979}, {171, "SE", 18.52823, 60.116359}, {172, "CH", 8.178932, 46.837326},
                {177, "TH", 100.930163, 15.836362}, {182, "TR", 35.274655, 38.938829}, // 30~34

                {186, "AE", 53.771642, 23.384012}, {187, "UA", 31.173916, 48.392648}, {42, "CN", 116.405994, 39.916927},
                {78, "IS", -17.895969, 65.013855}}; // 35~38
    }
}

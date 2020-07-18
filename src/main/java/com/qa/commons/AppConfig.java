package com.qa.commons;


import java.sql.Connection;

import static com.rc.qa.commons.Config.*;

/**
 * @create: 2019-11-16 14:50
 * @author: cloudsinn
 * @description: App host version在此确定后，被其他方法调用
 **/
public class AppConfig {

    public static String sqlLogHost = sqlOnlineLogHost;
    public static String rcLogHost = rc_log;
    public static String newLiveChatHost;
    public static Connection connection;
    public static String version;
    public static String sqlUser;
    public static String sqlHost;
    public static String sqlPwd;
    public static String appId;
    public static String token;
    static String opHost;

    /**
     * host    可选元素：["online", "iosOnline", "245", "tokyo", "182"]
     * appName 可选元素：["livu", "yaar", "iosLivu", "tumile"]
     */
    public static String host;
    public static String appName;

    public static void set_host_appName(String host, String appName) {
        AppConfig.host = host;
        AppConfig.appName = appName;
        switch (host) {
            case "online":
                newLiveChatHost = new_live_chat;
                sqlHost = sqlOnlineHost;
                sqlUser = sqlOnlineUser;
                sqlPwd = sqlOnlinePwd;
                break;
            case "iosOnline":
                rcLogHost = newLiveChatHost = ios_live_chat;
                sqlHost = sqlOnlineHost;
                sqlUser = sqlOnlineUser;
                sqlPwd = sqlOnlinePwd;
                break;
            case "182":
                opHost = newLiveChatHost = new_live_chat_182;
                sqlHost = sql182Host;
                sqlUser = sql182User;
                sqlPwd = sql182Pwd;
                break;
            case "245":
                opHost = newLiveChatHost = new_live_chat_245;
                sqlHost = sql245Host;
                sqlUser = sql245_TokyoUser;
                sqlPwd = sql245_tokyoPwd;
                break;
            case "tokyo":
                opHost = new_live_chat_op_tokyo;
                newLiveChatHost = new_live_chat_aws_tokyo;
                sqlHost = sqlTokyoHost;
                sqlUser = sql245_TokyoUser;
                sqlPwd = sql245_tokyoPwd;
                break;
        }
        switch (appName) {
            case "tumile":
                version = tumile_Api;
                appId = tumile_AppId;
                break;
            case "livu":
                version = livU_Api;
                appId = livU_AppId;
                break;
            case "yaar":
                version = yaar_Api;
                appId = yaar_AppId;
                break;
            case "iosLivu":
                version = ios_Api;
                appId = livU_AppId;
                break;
        }

    }

}

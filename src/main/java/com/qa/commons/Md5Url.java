package com.qa.commons;

import util.crypt.MD5;

import java.util.Map;

/**
 * @create: 2019-11-16 14:50
 * @author: cloudsinn
 * @description: url Md5加密类
 **/
public class Md5Url {

    public static String md5Url(String host, String path, Map<String, Object> params, Map<String, Object> replaceMap) {

//      替换path中的blackUser
        /*String blackId = (String) replaceMap.get("blacklists");
        if (blackId != null) {
            path = path.replaceFirst("(?<=blacklists/)\\d+", blackId);
        }*/

        if (params.size() == 0) {
            return host + path;
        }

        for (Map.Entry<String, Object> entry : replaceMap.entrySet()) {
            if (params.containsKey(entry.getKey())) {
                params.put(entry.getKey(), entry.getValue());
            }
        }

//      token为空替换参数后，直接返回替换后的值
        if (replaceMap.get("accessToken") == null) {
            String new_params = mapToString(params);
            return host + path + new_params;
        }

        StringBuilder sb;
        sb = new StringBuilder().append(host).append(path, 0, path.length() - 1)
                .append(replaceMap.get("accessToken"))
                .append(params.get("deviceId"))
                .append(params.get("nonce"))
                .append(params.get("timestamp"))
                .append(params.get("userId"));
        String newSign = MD5.getMD5Code(sb.toString());
//        System.out.println(newSign);

        params.put("sign", newSign);

        String new_params = mapToString(params);
        return host + path + new_params;
    }

    static String mapToString(Map<String, Object> map) {
        StringBuilder new_params = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            new_params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return new_params.substring(0, new_params.length() - 1);
    }
}

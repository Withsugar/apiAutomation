package com.qa.commons;

import com.alibaba.fastjson.JSONObject;

class jsonObjectUtil {
    public JSONObject json;

    public jsonObjectUtil() {
        json = new JSONObject();
    }

    public jsonObjectUtil put(String key, String value) {
        json.put(key, value);
        return this;
    }
}

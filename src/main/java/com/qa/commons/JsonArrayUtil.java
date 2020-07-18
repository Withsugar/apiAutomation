package com.qa.commons;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

class JsonArrayUtil {
    public JSONArray jsonArray;

    public JsonArrayUtil() {
        jsonArray = new JSONArray();
    }

    public JsonArrayUtil add(JSONObject json) {
        jsonArray.add(json);
        return this;
    }
}

package com.qa.commons;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @create: 2019-11-16 14:50
 * @author: cloudsinn
 * @description: Map方法类
 **/
public class MapUtil {

    public Map<String, Object> map;

    public MapUtil() {
        map = Collections.synchronizedMap(new HashMap<String, Object>());
    }

    public MapUtil put(String key, Object value) {
        map.put(key, value);
        return this;
    }
}


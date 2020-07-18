package com.qa.commons;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;

/**
 * @program RcQA
 * @description: 相关国家配置信息
 * @author: xiaopeng
 * @create: 2019/12/13 22:04
 */
@Data
public class CountryInfo {
    /* <ab, CountryDetail>*/
    private static HashMap<String, CountryDetail> countryMap = new HashMap<>();

    static {
        /* 读取txt文件*/
//        countryMap.pu
        /* 给map赋值*/
    }

    @Data
    @ToString
    public class CountryDetail {
        private String countryId;
        private String countryCode;
        private String countryShortName; //ab
        private String latitude;
        private String longitude;
    }
}

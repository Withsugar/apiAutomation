package com.qa.commons;

import com.alibaba.fastjson.*;
import com.deepoove.swagger.diff.SwaggerDiff;
import com.deepoove.swagger.diff.output.HtmlRender;
import com.deepoove.swagger.diff.output.MarkdownRender;
import io.restassured.response.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @program api-test-framework
 * @description: Swagger 工具类
 * @author: xiaopeng
 * @create: 2019/10/14 18:20
 */
public class SwaggerUtils {
    static SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd-HHmm");
    static String timeStamp = sdf.format(new Date());


    public static void main(String[] args) {
//        yaar
//        String newSpec = "http://39.100.88.245:8912/v2/api-docs?group=athena-web-api";
//        String oldSpec = "http://173.255.197.182:8910/v2/api-docs?group=live-chat";
//        livU&Chat
        String newSpec = "http://39.100.88.245:8902/v2/api-docs?group=athena-web-api";
        String oldSpec = "http://173.255.197.182:8900/v2/api-docs?group=live-chat";
        SwaggerDiff diff = SwaggerDiff.compareV2(oldSpec, newSpec);

        genHtmlReport(diff);

        genMDReport(diff);
    }

    public static void genMDReport(SwaggerDiff diff) {
        String md = new MarkdownRender().render(diff);
        try {
            FileWriter fw = new FileWriter(
                    "apiDiff-" + timeStamp + ".md");
            fw.write(md);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void genHtmlReport(SwaggerDiff diff) {
        String html = new HtmlRender("Changelog",
                "http://deepoove.com/swagger-diff/stylesheets/demo.css")
                .render(diff);

        try {
            FileWriter fw = new FileWriter(
                    "apiDiff-" + timeStamp + ".html");
            fw.write(html);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void extractSwaggerInfo() {
    /* <tag, >*/
        Map<String, Object> extMap = new HashMap<String, Object>();
        /* <> */
        Map<String, Object> apiMap = new HashMap<String, Object>();

        String apiJson = exportJson("http://173.255.197.182:8900/v2/api-docs?group=live-chat");

        JSONObject jsonObj = JSON.parseObject(apiJson);

        /* tags */
        JSONArray jsonArray = (JSONArray) JSONPath.eval(jsonObj, "$.tags");

        System.out.println("tags.size=" + jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
//            System.out.println("" + jsonArray.get(i));
        }


        /* paths */
        JSONObject pathObjs = (JSONObject) JSONPath.eval(jsonObj, "$.paths");

        System.out.println("api.size=" + pathObjs.size());
        for (String path : pathObjs.keySet()) {
            JSONObject methodsInAPI = pathObjs.getJSONObject(path);
            for (String method : methodsInAPI.keySet()) {

                JSONObject methodDetail = methodsInAPI.getJSONObject(method);
                String tag = (String) methodDetail.getJSONArray("tags").get(0); //此处假设必有tags字段，且非空
//                extMap.put(tag, )
            }
//            for (JSONObject apiInstance : pathObjs.getJSONObject(path)) {
//            }
        }
//        System.out.println("tags.size=" + jsonArray.size());

        System.out.println("" + JSONPath.eval(jsonObj, "$.paths"));
    }

    public static final String exportJson(String swaggerApiUrl) {
        Response rsp = given().get(swaggerApiUrl);

        return rsp.getBody().asString();
    }
}

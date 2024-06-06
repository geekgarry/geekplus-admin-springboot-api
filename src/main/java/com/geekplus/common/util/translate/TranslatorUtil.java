/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 8/8/23 03:48
 * description: 做什么的？
 */
package com.geekplus.common.util.translate;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class TranslatorUtil {
    /**
     * 英文2中文
     * @param word 英文
     * @return
     */
    // client: 指定客户端类型，这里使用gtx。
    // sl: 源语言代码（例如en代表英语）。
    // tl: 目标语言代码（例如zh-CN代表简体中文）。
    // dt: 数据类型，这里使用t代表翻译文本。
    // q: 要翻译的文本。
    public static String translate(String word){
        try {
            String url = "https://translate.googleapis.com/translate_a/single?" +
                    "client=gtx&" +
                    "sl=en" +
                    "&tl=zh-CN" +
                    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return parseResult(response.toString());
        }catch (Exception e){
            return  word;
        }
    }

    public static String translate(String word,String sl,String tl){
        try {
            String url = "https://translate.googleapis.com/translate_a/single?" +
                    "client=gtx&" +
                    "sl=" + sl +
                    "&tl=" + tl +
                    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return parseResult(response.toString());
        }catch (Exception e){
            return  word;
        }
    }

    private static String parseResult(String inputJson){
        JSONArray jsonArray2 = (JSONArray) new JSONArray(inputJson).get(0);
        StringBuilder result = new StringBuilder();
        for (Object o : jsonArray2) {
            result.append(((JSONArray) o).get(0).toString());
        }
        return result.toString();
    }

    // 对接的api为百度翻译
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private static String appid = "xxx";
    private static String securityKey = "xxx";
    // 发送查询
    public static String getTranslateResult(String query, String from, String to) {
        Map<String, Object> params = new HashMap();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", appid);
        // 随机数

        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);
        // 签名
        String src = appid + query + salt + securityKey;
        // 加密前的原文
        params.put("sign", SecureUtil.md5(src));
        return HttpUtil.get(TRANS_API_HOST, params);
    }

    public static void main(String[] args) {
        String res = getTranslateResult("苹果", "auto", "en");
        System.out.println(UnicodeUtil.toString(res));
        System.out.println(translate("noticeId"));
    }
}

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 8/8/23 03:48
 * description: 做什么的？
 */
package com.geekplus.common.util.translate;

import cn.hutool.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslatorUtil {
    /**
     * 英文2中文
     * @param word 英文
     * @return
     */
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

    private static String parseResult(String inputJson){
        JSONArray jsonArray2 = (JSONArray) new JSONArray(inputJson).get(0);
        StringBuilder result = new StringBuilder();
        for (Object o : jsonArray2) {
            result.append(((JSONArray) o).get(0).toString());
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(translate("noticeId"));
    }
}

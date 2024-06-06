package com.geekplus.common.util.weather;

/**
 * <p>
 *
 * </p>
 * <p>
 * Author:geekcjj
 * 2019-10-25 10:16
 */
import com.geekplus.common.domain.WeatherInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * 通过get请求向网站http://wthrcdn.etouch.cn/weather_mini获取某个 城市的天气状况数据，数据格式是Json
 *
 */
public class WeatherUtil {
    /**
     * 通过城市名称获取该城市的天气信息
     *
     * @param :cityName
     * @return
     */

    public  static String GetWeatherData(String cityname) {
        StringBuilder sb=new StringBuilder();;
        try {
            //cityname = URLEncoder.encode(cityName, "UTF-8");
            String weather_url = "http://wthrcdn.etouch.cn/weather_mini?city="+cityname;


            URL url = new URL(weather_url);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            GZIPInputStream gzin = new GZIPInputStream(is);
            InputStreamReader isr = new InputStreamReader(gzin, "utf-8"); // 设置读取流的编码格式，自定义编码
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while((line=reader.readLine())!=null)
                sb.append(line+" ");
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(sb.toString());
        return sb.toString();

    }


    /**
     * 将JSON格式数据进行解析 ，返回一个weather对象
     * @param  : str
     * @return
     */
    public static WeatherInfo GetWeather(String weatherInfobyJson){
        JSONTokener jsonTokener = new JSONTokener(weatherInfobyJson);
        JSONObject dataOfJson=(JSONObject) jsonTokener.nextValue();
        System.out.println(weatherInfobyJson);
        System.out.println(dataOfJson.getInt("status"));
        if(dataOfJson.getInt("status")!=1000)
            return null;

        //创建WeatherInfo对象，提取所需的天气信息
        WeatherInfo weatherInfo = new WeatherInfo();

        //从json数据中提取数据
        JSONObject data = dataOfJson.getJSONObject("data");
        //使用net.sf.josn-lib，需指定使用jdk版本
        //dataOfJson = JSONObject.fromObject(data);
        //下面使用org.json
        //dataOfJson = new JSONObject(data);
        weatherInfo.setCityname(data.getString("city"));
        System.out.println("1111111111111111111111111"+data.getString("city"));
        //weatherInfo.setAirquality(data.optString("aqi"));
        //获取预测的天气预报信息
        JSONArray forecast = data.getJSONArray("forecast");
        //取得当天的
        JSONObject result=forecast.getJSONObject(0);
        System.out.println("2222222222222222222222222"+result.getString("type"));
        weatherInfo.setDate(result.getString("date"));

        String high = result.getString("high").substring(2);
        String low  = result.getString("low").substring(2);

        weatherInfo.setTemperature(low+"~"+high);

        weatherInfo.setWeather(result.getString("type"));



        return weatherInfo;
    }
    public static void main(String[]  args){
        String info = WeatherUtil.GetWeatherData("宿迁");
        WeatherInfo weatherinfo = WeatherUtil.GetWeather(info);
        System.out.println(weatherinfo.toString());
        System.out.println(info);
    }
}

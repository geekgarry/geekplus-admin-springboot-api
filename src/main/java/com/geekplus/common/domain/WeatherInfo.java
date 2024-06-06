package com.geekplus.common.domain;

/**
 * <p>
 *
 * </p>
 * <p>
 * Author:geekcjj
 * 2019-10-25 10:21
 */
public class WeatherInfo {
    private String date;//时间
    private String cityname;//城市名
    private String weather;//天气
    private String temperature;//气温
    private String airquality;//pm2.5

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getAirquality() {
        return airquality;
    }

    public void setAirquality(String airquality) {
        this.airquality = airquality;
    }

    @Override
    public String toString() {
        return "WeatherInfo [date=" + date + ", cityname=" + cityname
                + ", weather=" + weather + ", temperature=" + temperature
                + ", airquality=" + airquality + "]";
    }
}

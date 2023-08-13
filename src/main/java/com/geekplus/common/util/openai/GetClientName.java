/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/3/23 22:18
 * description: 做什么的？
 */
package com.geekplus.common.util.openai;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class GetClientName {
    private static final Logger log = LoggerFactory.getLogger(GetClientName.class);


    public static String getOSAndBrowser(HttpServletRequest httpServletRequest){
        //List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from someInfo");
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        String allInfo = "";
        while (headerNames.hasMoreElements()){

            String header = headerNames.nextElement();
            allInfo += header + ": ";
            allInfo += httpServletRequest.getHeader(header) + "\n";
        }
        String osAndBrower = httpServletRequest.getHeader("user-agent");
        String os = whatOS(osAndBrower);
        String brower = whatBrower(osAndBrower);
        return allInfo;
    }
    public static UserAgent getBrowser(HttpServletRequest httpServletRequest){
        String ua = httpServletRequest.getHeader("User-Agent");
        log.info("******************************");
        log.info("操作系统及浏览器信息："+ua);
        //转成UserAgent对象
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        //获取浏览器信息
        Browser browser = userAgent.getBrowser();
        log.info("浏览器信息："+browser);
        log.info("浏览器信息ID："+userAgent.getId());
        //获取系统信息
        OperatingSystem os = userAgent.getOperatingSystem();
        log.info("系统信息："+os);
        //系统名称
        String system = os.getName();
        log.info("系统名称："+system);
        //浏览器名称
        String browserName = browser.getName();
        log.info("浏览器名称："+browserName);
        log.info("******************************");
        return userAgent;
    }
    //判断是啥操作系统
    public  static  String whatOS(final String str){

        String osStr = "未知";

        //这里就简单判断下
        if(str.contains("Windows")){

            osStr = "Windows";
        }
        else if(str.contains("Linux")){

            osStr = "Linux";
        }
        else if(str.contains("iPhone")){
            osStr ="iOS";
        }

        return osStr;
    }

    //判断是啥浏览器-简单判断下
    public static String whatBrower(final String str){

        String browerStr = "未知";
        if(str.contains("Chrome")){
            browerStr = "Chrome";
        }
        if(str.contains("Chrome Mobile")){
            browerStr = "Chrome Mobile";
        }
        else if(str.contains("Firefox")){
            browerStr = "Firefox";
        }
        else if(str.contains("Safari")){
            browerStr = "Safari";
        }
        else if(str.contains("Mobile Safari")){
            browerStr = "Mobile Safari";
        }
        else if(str.contains("Edge")){
            browerStr = "Edge";
        }
        else if(str.contains("Internet Explorer")){
            browerStr = "IE";
        }
        return  browerStr;
    }
}

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/4 6:53 下午
 * description: 做什么的？
 */
package com.geekplus.common.util;

import com.alibaba.fastjson.JSONObject;
import com.geekplus.common.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressUtil {
    private static final Logger log = LoggerFactory.getLogger(AddressUtil.class);

    // IP地址查询
    public static final String IP_URL = "https://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        String address = UNKNOWN;
        // 内网不查询
        if (IPUtils.isLocalHost(ip))
        {
            return "内网IP";
        }
        //if (MaikeConfig.isAddressEnabled())
        //{
            try
            {
                String rspStr = HttpClientUtil.get(IP_URL+"?ip=" + ip + "&json=true", Constant.GBK);
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s", region, city);
            }
            catch (Exception e)
            {
                log.error("获取地理位置异常 {}", ip);
            }
        //}
        return address;
    }
}

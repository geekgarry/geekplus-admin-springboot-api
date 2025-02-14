package com.geekplus.common.util;

import com.geekplus.common.constant.Constant;
import com.geekplus.common.domain.LoginBody;
import com.geekplus.common.util.http.IPUtils;
import com.geekplus.common.util.http.IpAddressUtil;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.webapp.system.entity.SysLoginLog;
import com.geekplus.webapp.system.service.SysLoginLogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 处理并记录日志文件
 *
 * @author
 */
@Slf4j
public class LogUtil
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }

    public static void recordLoginInfo(LoginBody loginBody, String status, String msg){
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtil.getRequest().getHeader("User-Agent"));
        final String ip = IPUtils.getIpAddr(ServletUtil.getRequest());
        String address = IpAddressUtil.getRealAddressByIP(ip);
        StringBuilder s = new StringBuilder();
        s.append(getBlock(ip));
        s.append(address);
        s.append(getBlock(loginBody.getUsername()));
        s.append(getBlock(status));
        s.append(getBlock(msg));
        // 打印信息到日志
        log.info(s.toString(), msg);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setLogUsername(loginBody.getUsername());
        loginLog.setLogLoginIp(ip);
        loginLog.setLoginLocation(address);
        loginLog.setLogBrowser(browser);
        loginLog.setLogSystem(os);
        loginLog.setLogMsg(msg);
        loginLog.setLogTime(new Date());
        // 日志状态
        if (Constant.LOGIN_SUCCESS.equals(status) || Constant.LOGOUT.equals(status))
        {
            loginLog.setLogType(1);
        }
        else if (Constant.LOGIN_FAIL.equals(status))
        {
            loginLog.setLogType(0);
        }
        // 插入数据
        SpringUtil.getBean(SysLoginLogService.class).insertSysLoginLog(loginLog);
    }
}

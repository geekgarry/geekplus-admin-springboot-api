package com.geekplus.framework.manager;

import cn.hutool.extra.spring.SpringUtil;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.util.LogUtil;
import com.geekplus.common.util.http.IpAddressUtil;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.common.util.http.IPUtils;
import com.geekplus.webapp.system.entity.SysLoginLog;
import com.geekplus.webapp.system.entity.SysOperLog;
import com.geekplus.webapp.system.service.SysLoginLogService;
import com.geekplus.webapp.system.service.SysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/4 6:42 下午
 * description: 记录日志异步工厂
 */
@Slf4j
public class LogFactory {

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLoginInfo(final String username, final String status, final String message,
                                            final Object... args){
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtil.getRequest().getHeader("User-Agent"));
        final String ip = IPUtils.getIp(ServletUtil.getRequest());
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = IpAddressUtil.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtil.getBlock(ip));
                s.append(address);
                s.append(LogUtil.getBlock(username));
                s.append(LogUtil.getBlock(status));
                s.append(LogUtil.getBlock(message));
                // 打印信息到日志
                log.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLoginLog logininfor = new SysLoginLog();
                logininfor.setLogUsername(username);
                logininfor.setLogLoginIp(ip);
                //logininfor.set(address);
                logininfor.setLogBrowser(browser);
                logininfor.setLogSystem(os);
                logininfor.setLogMsg(message);
                // 日志状态
                if (Constant.LOGIN_SUCCESS.equals(status) || Constant.LOGOUT.equals(status))
                {
                    logininfor.setLogType(1);
                }
                else if (Constant.LOGIN_FAIL.equals(status))
                {
                    logininfor.setLogType(0);
                }
                // 插入数据
                SpringUtil.getBean(SysLoginLogService.class).insertSysLoginLog(logininfor);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(IpAddressUtil.getRealAddressByIP(operLog.getOperIp()));
                SpringUtil.getBean(SysOperLogService.class).insertSysOperLog(operLog);
            }
        };
    }
}

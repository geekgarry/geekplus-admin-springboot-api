package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 功能：系统登录日志 对象:sys_login_log
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysLoginLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 系统登录日志 系统登录日志
     */
    private Long logId;

    /**
     * 系统登录日志 系统登录日志
     */
    private Long logUserId;

    /**
     * 系统登录日志 系统登录日志
     */
    private String logUsername;

    /**
     * 系统登录日志 系统登录日志
     */
    private String logLoginIp;

	/**
	 * 系统登录日志 系统登录地点
	 */
	private String loginLocation;

    /**
     * 系统登录日志 系统登录日志
     */
    private String logBrowser;

    /**
     * 系统登录日志 系统登录日志
     */
    private String logSystem;

    /**
     * 系统登录日志 系统登录日志
     */
    private Date logTime;

    /**
     * 系统登录日志 系统登录日志
     */
    private Integer logType;

    /**
     * 系统登录日志 系统登录日志
     */
    private String logMsg;

	/**
	 *获取登录日志ID
	 */
	public Long getLogId(){
		return logId;
	}

	/**
	 *设置登录日志ID
	 */
	public void setLogId(Long logId){
		this.logId = logId;
	}
	/**
	 *获取登录用户ID
	 */
	public Long getLogUserId(){
		return logUserId;
	}

	/**
	 *设置登录用户ID
	 */
	public void setLogUserId(Long logUserId){
		this.logUserId = logUserId;
	}
	/**
	 *获取登录用户名
	 */
	public String getLogUsername(){
		return logUsername;
	}

	/**
	 *设置登录用户名
	 */
	public void setLogUsername(String logUsername){
		this.logUsername = logUsername;
	}
	/**
	 *获取登录IP地址
	 */
	public String getLogLoginIp(){
		return logLoginIp;
	}

	/**
	 *设置登录IP地址
	 */
	public void setLogLoginIp(String logLoginIp){
		this.logLoginIp = logLoginIp;
	}

	public String getLoginLocation() {
		return loginLocation;
	}

	public void setLoginLocation(String loginLocation) {
		this.loginLocation = loginLocation;
	}

	/**
	 *获取登录浏览器
	 */
	public String getLogBrowser(){
		return logBrowser;
	}

	/**
	 *设置登录浏览器
	 */
	public void setLogBrowser(String logBrowser){
		this.logBrowser = logBrowser;
	}
	/**
	 *获取登录系统
	 */
	public String getLogSystem(){
		return logSystem;
	}

	/**
	 *设置登录系统
	 */
	public void setLogSystem(String logSystem){
		this.logSystem = logSystem;
	}
	/**
	 *获取登录事件
	 */
	public Date getLogTime(){
		return logTime;
	}

	/**
	 *设置登录事件
	 */
	public void setLogTime(Date logTime){
		this.logTime = logTime;
	}
	/**
	 *获取登录状态，0为失败，1为成功
	 */
	public Integer getLogType(){
		return logType;
	}

	/**
	 *设置登录状态，0为失败，1为成功
	 */
	public void setLogType(Integer logType){
		this.logType = logType;
	}
	/**
	 *获取登录信息（备注）
	 */
	public String getLogMsg(){
		return logMsg;
	}

	/**
	 *设置登录信息（备注）
	 */
	public void setLogMsg(String logMsg){
		this.logMsg = logMsg;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("logUserId", getLogUserId())
            .append("logUserName", getLogUsername())
            .append("logLoginIp", getLogLoginIp())
            .append("logBrowser", getLogBrowser())
            .append("logSystem", getLogSystem())
            .append("logTime", getLogTime())
            .append("logType", getLogType())
            .append("logMsg", getLogMsg())
            .toString();
    }
}

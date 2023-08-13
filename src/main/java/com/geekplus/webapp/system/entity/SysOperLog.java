package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 功能：系统操作日志 对象:sys_oper_log
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     * 系统操作日志 系统操作日志
     */
    private Long operId;

    /**
     * 系统操作日志 系统操作日志
     */
    private String title;

    /**
     * 系统操作日志 系统操作日志
     */
    private Integer businessType;

    /**
     * 系统操作日志 系统操作日志
     */
    private String method;

    /**
     * 系统操作日志 系统操作日志
     */
    private String requestMethod;

    /**
     * 系统操作日志 系统操作日志
     */
    private Integer operatorType;

    /**
     * 系统操作日志 系统操作日志
     */
    private String operName;

    /**
     * 系统操作日志 系统操作日志
     */
    private String groupName;

    /**
     * 系统操作日志 系统操作日志
     */
    private String operUrl;

    /**
     * 系统操作日志 系统操作日志
     */
    private String operIp;

	/**
	 * 系统操作日志 系统操作日志
	 */
	private String operBrowser;

	/**
	 * 系统操作日志 系统操作日志
	 */
	private String operOs;

    /**
     * 系统操作日志 系统操作日志
     */
    private String operLocation;

    /**
     * 系统操作日志 系统操作日志
     */
    private String operParam;

    /**
     * 系统操作日志 系统操作日志
     */
    private String jsonResult;

    /**
     * 系统操作日志 系统操作日志
     */
    private Integer status;

    /**
     * 系统操作日志 系统操作日志
     */
    private String errorMsg;

    /**
     * 系统操作日志 系统操作日志
     */
    private Date operTime;

	/**
	 *获取日志主键
	 */
	public Long getOperId(){
		return operId;
	}

	/**
	 *设置日志主键
	 */
	public void setOperId(Long operId){
		this.operId = operId;
	}
	/**
	 *获取模块标题
	 */
	public String getTitle(){
		return title;
	}

	/**
	 *设置模块标题
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/**
	 *获取业务类型（0其它 1新增 2修改 3删除）
	 */
	public Integer getBusinessType(){
		return businessType;
	}

	/**
	 *设置业务类型（0其它 1新增 2修改 3删除）
	 */
	public void setBusinessType(Integer businessType){
		this.businessType = businessType;
	}
	/**
	 *获取方法名称
	 */
	public String getMethod(){
		return method;
	}

	/**
	 *设置方法名称
	 */
	public void setMethod(String method){
		this.method = method;
	}
	/**
	 *获取请求方式
	 */
	public String getRequestMethod(){
		return requestMethod;
	}

	/**
	 *设置请求方式
	 */
	public void setRequestMethod(String requestMethod){
		this.requestMethod = requestMethod;
	}
	/**
	 *获取操作类别（0其它 1后台用户 2手机端用户）
	 */
	public Integer getOperatorType(){
		return operatorType;
	}

	/**
	 *设置操作类别（0其它 1后台用户 2手机端用户）
	 */
	public void setOperatorType(Integer operatorType){
		this.operatorType = operatorType;
	}
	/**
	 *获取操作人员
	 */
	public String getOperName(){
		return operName;
	}

	/**
	 *设置操作人员
	 */
	public void setOperName(String operName){
		this.operName = operName;
	}
	/**
	 *获取群组名称
	 */
	public String getGroupName(){
		return groupName;
	}

	/**
	 *设置群组名称
	 */
	public void setGroupName(String groupName){
		this.groupName = groupName;
	}
	/**
	 *获取请求URL
	 */
	public String getOperUrl(){
		return operUrl;
	}

	/**
	 *设置请求URL
	 */
	public void setOperUrl(String operUrl){
		this.operUrl = operUrl;
	}
	/**
	 *获取主机地址
	 */
	public String getOperIp(){
		return operIp;
	}

	/**
	 *设置主机地址
	 */
	public void setOperIp(String operIp){
		this.operIp = operIp;
	}

	public String getOperBrowser() {
		return operBrowser;
	}

	public void setOperBrowser(String operBrowser) {
		this.operBrowser = operBrowser;
	}

	public String getOperOs() {
		return operOs;
	}

	public void setOperOs(String operOs) {
		this.operOs = operOs;
	}

	/**
	 *获取操作地点
	 */
	public String getOperLocation(){
		return operLocation;
	}

	/**
	 *设置操作地点
	 */
	public void setOperLocation(String operLocation){
		this.operLocation = operLocation;
	}
	/**
	 *获取请求参数
	 */
	public String getOperParam(){
		return operParam;
	}

	/**
	 *设置请求参数
	 */
	public void setOperParam(String operParam){
		this.operParam = operParam;
	}
	/**
	 *获取返回参数
	 */
	public String getJsonResult(){
		return jsonResult;
	}

	/**
	 *设置返回参数
	 */
	public void setJsonResult(String jsonResult){
		this.jsonResult = jsonResult;
	}
	/**
	 *获取操作状态（0正常 1异常）
	 */
	public Integer getStatus(){
		return status;
	}

	/**
	 *设置操作状态（0正常 1异常）
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *获取错误消息
	 */
	public String getErrorMsg(){
		return errorMsg;
	}

	/**
	 *设置错误消息
	 */
	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}
	/**
	 *获取操作时间
	 */
	public Date getOperTime(){
		return operTime;
	}

	/**
	 *设置操作时间
	 */
	public void setOperTime(Date operTime){
		this.operTime = operTime;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("operId", getOperId())
            .append("title", getTitle())
            .append("businessType", getBusinessType())
            .append("method", getMethod())
            .append("requestMethod", getRequestMethod())
            .append("operatorType", getOperatorType())
            .append("operName", getOperName())
            .append("groupName", getGroupName())
            .append("operUrl", getOperUrl())
            .append("operIp", getOperIp())
            .append("operLocation", getOperLocation())
            .append("operParam", getOperParam())
            .append("jsonResult", getJsonResult())
            .append("status", getStatus())
            .append("errorMsg", getErrorMsg())
            .append("operTime", getOperTime())
            .toString();
    }
}

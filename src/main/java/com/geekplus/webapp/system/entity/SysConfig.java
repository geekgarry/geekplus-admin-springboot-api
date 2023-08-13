package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;

/**
 * 功能： 对象:sys_config
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     *
     */
    private Integer configId;

    /**
     *
     */
    private String configName;

    /**
     *
     */
    private String configKey;

    /**
     *
     */
    private String configValue;

    /**
     *
     */
    private String configType;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private String updateBy;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private String remark;

	/**
	 *获取参数主键
	 */
	public Integer getConfigId(){
		return configId;
	}

	/**
	 *设置参数主键
	 */
	public void setConfigId(Integer configId){
		this.configId = configId;
	}
	/**
	 *获取参数名称
	 */
	public String getConfigName(){
		return configName;
	}

	/**
	 *设置参数名称
	 */
	public void setConfigName(String configName){
		this.configName = configName;
	}
	/**
	 *获取参数键名
	 */
	public String getConfigKey(){
		return configKey;
	}

	/**
	 *设置参数键名
	 */
	public void setConfigKey(String configKey){
		this.configKey = configKey;
	}
	/**
	 *获取参数键值
	 */
	public String getConfigValue(){
		return configValue;
	}

	/**
	 *设置参数键值
	 */
	public void setConfigValue(String configValue){
		this.configValue = configValue;
	}
	/**
	 *获取系统内置（Y是 N否）
	 */
	public String getConfigType(){
		return configType;
	}

	/**
	 *设置系统内置（Y是 N否）
	 */
	public void setConfigType(String configType){
		this.configType = configType;
	}
	/**
	 *获取创建者
	 */
	public String getCreateBy(){
		return createBy;
	}

	/**
	 *设置创建者
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *获取创建时间
	 */
	public Date getCreateTime(){
		return createTime;
	}

	/**
	 *设置创建时间
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *获取更新者
	 */
	public String getUpdateBy(){
		return updateBy;
	}

	/**
	 *设置更新者
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *获取更新时间
	 */
	public Date getUpdateTime(){
		return updateTime;
	}

	/**
	 *设置更新时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *获取备注
	 */
	public String getRemark(){
		return remark;
	}

	/**
	 *设置备注
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

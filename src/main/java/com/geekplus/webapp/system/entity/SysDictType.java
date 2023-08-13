package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;
import java.util.Date;

/**
 * 功能： 对象:sys_dict_type
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysDictType extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     *
     */
    private Long dictId;

    /**
     *
     */
    private String dictName;

    /**
     *
     */
    private String dictType;

    /**
     *
     */
    private String status;

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
    private String remarks;

	/**
	 *获取
	 */
	public Long getDictId(){
		return dictId;
	}

	/**
	 *设置
	 */
	public void setDictId(Long dictId){
		this.dictId = dictId;
	}
	/**
	 *获取字典名称
	 */
	public String getDictName(){
		return dictName;
	}

	/**
	 *设置字典名称
	 */
	public void setDictName(String dictName){
		this.dictName = dictName;
	}
	/**
	 *获取字典类型
	 */
	public String getDictType(){
		return dictType;
	}

	/**
	 *设置字典类型
	 */
	public void setDictType(String dictType){
		this.dictType = dictType;
	}
	/**
	 *获取状态，0为正常，1为停用
	 */
	public String getStatus(){
		return status;
	}

	/**
	 *设置状态，0为正常，1为停用
	 */
	public void setStatus(String status){
		this.status = status;
	}
	/**
	 *获取创建人
	 */
	public String getCreateBy(){
		return createBy;
	}

	/**
	 *设置创建人
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
	 *获取更新人
	 */
	public String getUpdateBy(){
		return updateBy;
	}

	/**
	 *设置更新人
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
	public String getRemarks(){
		return remarks;
	}

	/**
	 *设置备注
	 */
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remarks", getRemarks())
            .toString();
    }
}

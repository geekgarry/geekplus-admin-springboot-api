package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;
import java.util.Date;

/**
 * 功能： 对象:sys_dict_data
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     *
     */
    private Long dictCode;

    /**
     *
     */
    private Integer dictSort;

    /**
     *
     */
    private String dictLabel;

    /**
     *
     */
    private String dictValue;

    /**
     *
     */
    private String dictType;

    /**
     *
     */
    private String cssClass;

    /**
     *
     */
    private String listClass;

    /**
     *
     */
    private String isDefault;

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
    private String remark;

	/**
	 *获取字典编码
	 */
	public Long getDictCode(){
		return dictCode;
	}

	/**
	 *设置字典编码
	 */
	public void setDictCode(Long dictCode){
		this.dictCode = dictCode;
	}
	/**
	 *获取字典排序
	 */
	public Integer getDictSort(){
		return dictSort;
	}

	/**
	 *设置字典排序
	 */
	public void setDictSort(Integer dictSort){
		this.dictSort = dictSort;
	}
	/**
	 *获取字典标签
	 */
	public String getDictLabel(){
		return dictLabel;
	}

	/**
	 *设置字典标签
	 */
	public void setDictLabel(String dictLabel){
		this.dictLabel = dictLabel;
	}
	/**
	 *获取字典键值
	 */
	public String getDictValue(){
		return dictValue;
	}

	/**
	 *设置字典键值
	 */
	public void setDictValue(String dictValue){
		this.dictValue = dictValue;
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
	 *获取样式属性（其他样式扩展）
	 */
	public String getCssClass(){
		return cssClass;
	}

	/**
	 *设置样式属性（其他样式扩展）
	 */
	public void setCssClass(String cssClass){
		this.cssClass = cssClass;
	}
	/**
	 *获取表格回显样式
	 */
	public String getListClass(){
		return listClass;
	}

	/**
	 *设置表格回显样式
	 */
	public void setListClass(String listClass){
		this.listClass = listClass;
	}
	/**
	 *获取是否默认（Y是 N否）
	 */
	public String getIsDefault(){
		return isDefault;
	}

	/**
	 *设置是否默认（Y是 N否）
	 */
	public void setIsDefault(String isDefault){
		this.isDefault = isDefault;
	}
	/**
	 *获取状态（0正常 1停用）
	 */
	public String getStatus(){
		return status;
	}

	/**
	 *设置状态（0正常 1停用）
	 */
	public void setStatus(String status){
		this.status = status;
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
            .append("dictCode", getDictCode())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictType", getDictType())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

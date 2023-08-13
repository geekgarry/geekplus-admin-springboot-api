package com.geekplus.webapp.system.entity;

import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.util.Date;
import java.util.Date;

/**
 * 功能： 对象:sys_notice
 *
 * @author CodeGenerator
 * @date 2023/06/18
 */
public class SysNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    /**
     *
     */
    private Long noticeId;

    /**
     *
     */
    private String noticeTitle;

    /**
     *
     */
    private String noticeContent;

    /**
     *
     */
    private String notifier;

    /**
     *
     */
    private String noticeType;

    /**
     *
     */
    private String noticeUrl;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private String createBy;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private String updateBy;

	/**
	 *获取
	 */
	public Long getNoticeId(){
		return noticeId;
	}

	/**
	 *设置
	 */
	public void setNoticeId(Long noticeId){
		this.noticeId = noticeId;
	}
	/**
	 *获取标题
	 */
	public String getNoticeTitle(){
		return noticeTitle;
	}

	/**
	 *设置标题
	 */
	public void setNoticeTitle(String noticeTitle){
		this.noticeTitle = noticeTitle;
	}
	/**
	 *获取内容
	 */
	public String getNoticeContent(){
		return noticeContent;
	}

	/**
	 *设置内容
	 */
	public void setNoticeContent(String noticeContent){
		this.noticeContent = noticeContent;
	}
	/**
	 *获取发布公告的人，一般是管理员
	 */
	public String getNotifier(){
		return notifier;
	}

	/**
	 *设置发布公告的人，一般是管理员
	 */
	public void setNotifier(String notifier){
		this.notifier = notifier;
	}
	/**
	 *获取公告类型
	 */
	public String getNoticeType(){
		return noticeType;
	}

	/**
	 *设置公告类型
	 */
	public void setNoticeType(String noticeType){
		this.noticeType = noticeType;
	}
	/**
	 *获取公告携带的url
	 */
	public String getNoticeUrl(){
		return noticeUrl;
	}

	/**
	 *设置公告携带的url
	 */
	public void setNoticeUrl(String noticeUrl){
		this.noticeUrl = noticeUrl;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeContent", getNoticeContent())
            .append("notifier", getNotifier())
            .append("noticeType", getNoticeType())
            .append("noticeUrl", getNoticeUrl())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}

package com.geekplus.webapp.function.entity;

import com.geekplus.common.annotation.Excel;
import com.geekplus.common.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ChatAI聊天记录日志
对象 chatAI_log
 *
 * @author 佚名
 * @date 2023-02-25
 */
public class ChatAILog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 用户IP */
    @Excel(name = "用户IP")
    private String userIp;

    /** 用户网络Mac地址 */
    @Excel(name = "用户网络Mac地址")
    private String userMac;

    /** 日志内容 */
    @Excel(name = "日志内容")
    private String chatContent;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setUserIp(String userIp)
    {
        this.userIp = userIp;
    }

    public String getUserIp()
    {
        return userIp;
    }
    public void setUserMac(String userMac)
    {
        this.userMac = userMac;
    }

    public String getUserMac()
    {
        return userMac;
    }
    public void setChatContent(String chatContent)
    {
        this.chatContent = chatContent;
    }

    public String getChatContent()
    {
        return chatContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("userIp", getUserIp())
            .append("userMac", getUserMac())
            .append("createTime", getCreateTime())
            .append("chatContent", getChatContent())
            .toString();
    }
}

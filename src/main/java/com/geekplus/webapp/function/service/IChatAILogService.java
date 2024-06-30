package com.geekplus.webapp.function.service;

import com.geekplus.webapp.function.entity.ChatAILog;

import java.util.List;

/**
 * ChatAI聊天记录日志
Service接口
 *
 * @author 佚名
 * @date 2023-02-25
 */
public interface IChatAILogService
{
    /**
     * 查询ChatAI聊天记录日志

     *
     * @param id ChatAI聊天记录日志
ID
     * @return ChatAI聊天记录日志

     */
    public ChatAILog selectChatAILogById(Long id);

    /**
     * 查询ChatAI聊天记录日志
列表
     *
     * @param chatAILog ChatAI聊天记录日志

     * @return ChatAI聊天记录日志
集合
     */
    public List<ChatAILog> selectChatAILogList(ChatAILog chatAILog);

    /**
     * 新增ChatAI聊天记录日志

     *
     * @param chatAILog ChatAI聊天记录日志

     * @return 结果
     */
    public int insertChatAILog(ChatAILog chatAILog);

    /**
     * 修改ChatAI聊天记录日志

     *
     * @param chatAILog ChatAI聊天记录日志

     * @return 结果
     */
    public int updateChatAILog(ChatAILog chatAILog);

    /**
     * 批量删除ChatAI聊天记录日志

     *
     * @param ids 需要删除的ChatAI聊天记录日志
ID
     * @return 结果
     */
    public int deleteChatAILogByIds(Long[] ids);

    /**
     * 删除ChatAI聊天记录日志
信息
     *
     * @param id ChatAI聊天记录日志
ID
     * @return 结果
     */
    public int deleteChatAILogById(Long id);

    public void deleteAll();
}

package com.geekplus.webapp.function.mapper;

import com.geekplus.webapp.function.entity.ChatAILog;

import java.util.List;

/**
 * ChatAI聊天记录日志
Mapper接口
 *
 * @author 佚名
 * @date 2023-02-25
 */
public interface ChatAILogMapper
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
     * 删除ChatAI聊天记录日志

     *
     * @param id ChatAI聊天记录日志
ID
     * @return 结果
     */
    public int deleteChatAILogById(Long id);

    /**
     * 批量删除ChatAI聊天记录日志

     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteChatAILogByIds(Long[] ids);

    public void removeTop35();

    //清空表数据，重置自增主键
    public void removeAll();
}

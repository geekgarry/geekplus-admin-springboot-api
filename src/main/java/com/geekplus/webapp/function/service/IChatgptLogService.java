package com.geekplus.webapp.function.service;

import com.geekplus.webapp.function.entity.ChatgptLog;

import java.util.List;

/**
 * Chatgpt聊天记录日志
Service接口
 *
 * @author 佚名
 * @date 2023-02-25
 */
public interface IChatgptLogService
{
    /**
     * 查询Chatgpt聊天记录日志

     *
     * @param id Chatgpt聊天记录日志
ID
     * @return Chatgpt聊天记录日志

     */
    public ChatgptLog selectChatgptLogById(Long id);

    /**
     * 查询Chatgpt聊天记录日志
列表
     *
     * @param chatgptLog Chatgpt聊天记录日志

     * @return Chatgpt聊天记录日志
集合
     */
    public List<ChatgptLog> selectChatgptLogList(ChatgptLog chatgptLog);

    /**
     * 新增Chatgpt聊天记录日志

     *
     * @param chatgptLog Chatgpt聊天记录日志

     * @return 结果
     */
    public int insertChatgptLog(ChatgptLog chatgptLog);

    /**
     * 修改Chatgpt聊天记录日志

     *
     * @param chatgptLog Chatgpt聊天记录日志

     * @return 结果
     */
    public int updateChatgptLog(ChatgptLog chatgptLog);

    /**
     * 批量删除Chatgpt聊天记录日志

     *
     * @param ids 需要删除的Chatgpt聊天记录日志
ID
     * @return 结果
     */
    public int deleteChatgptLogByIds(Long[] ids);

    /**
     * 删除Chatgpt聊天记录日志
信息
     *
     * @param id Chatgpt聊天记录日志
ID
     * @return 结果
     */
    public int deleteChatgptLogById(Long id);

    public void deleteAll();
}

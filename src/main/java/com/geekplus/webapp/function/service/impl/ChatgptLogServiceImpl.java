package com.geekplus.webapp.function.service.impl;

import com.geekplus.webapp.function.entity.ChatgptLog;
import com.geekplus.webapp.function.mapper.ChatgptLogMapper;
import com.geekplus.common.util.DateUtils;
import com.geekplus.webapp.function.service.IChatgptLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Chatgpt聊天记录日志
Service业务层处理
 *
 * @author 佚名
 * @date 2023-02-25
 */
@Service
public class ChatgptLogServiceImpl implements IChatgptLogService
{
    @Autowired
    private ChatgptLogMapper chatgptLogMapper;

    /**
     * 查询Chatgpt聊天记录日志

     *
     * @param id Chatgpt聊天记录日志
ID
     * @return Chatgpt聊天记录日志

     */
    @Override
    public ChatgptLog selectChatgptLogById(Long id)
    {
        return chatgptLogMapper.selectChatgptLogById(id);
    }

    /**
     * 查询Chatgpt聊天记录日志
列表
     *
     * @param chatgptLog Chatgpt聊天记录日志

     * @return Chatgpt聊天记录日志

     */
    @Override
    public List<ChatgptLog> selectChatgptLogList(ChatgptLog chatgptLog)
    {
        return chatgptLogMapper.selectChatgptLogList(chatgptLog);
    }

    /**
     * 新增Chatgpt聊天记录日志

     *
     * @param chatgptLog Chatgpt聊天记录日志

     * @return 结果
     */
    @Override
    public int insertChatgptLog(ChatgptLog chatgptLog)
    {
        chatgptLog.setCreateTime(DateUtils.getNowDate());
        return chatgptLogMapper.insertChatgptLog(chatgptLog);
    }

    /**
     * 修改Chatgpt聊天记录日志

     *
     * @param chatgptLog Chatgpt聊天记录日志

     * @return 结果
     */
    @Override
    public int updateChatgptLog(ChatgptLog chatgptLog)
    {
        return chatgptLogMapper.updateChatgptLog(chatgptLog);
    }

    /**
     * 批量删除Chatgpt聊天记录日志

     *
     * @param ids 需要删除的Chatgpt聊天记录日志
ID
     * @return 结果
     */
    @Override
    public int deleteChatgptLogByIds(Long[] ids)
    {
        return chatgptLogMapper.deleteChatgptLogByIds(ids);
    }

    /**
     * 删除Chatgpt聊天记录日志
信息
     *
     * @param id Chatgpt聊天记录日志
ID
     * @return 结果
     */
    @Override
    public int deleteChatgptLogById(Long id)
    {
        return chatgptLogMapper.deleteChatgptLogById(id);
    }

    @Override
    public void deleteAll(){ chatgptLogMapper.removeAll();}
}

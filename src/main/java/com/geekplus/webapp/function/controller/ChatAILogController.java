package com.geekplus.webapp.function.controller;

import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.enums.OperatorType;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.function.entity.ChatAILog;
import com.geekplus.common.annotation.Log;
import com.geekplus.common.domain.Result;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.enums.BusinessType;
//import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.function.service.IChatAILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ChatAI聊天记录日志
Controller
 *
 * @author 佚名
 * @date 2023-02-25
 */
@RestController
@RequestMapping("/geekplus/chatAILog")
public class ChatAILogController extends BaseController
{
    @Autowired
    private IChatAILogService chatgptLogService;

    /**
     * 查询ChatAI聊天记录日志列表
     */
    @GetMapping("/list")
    public PageDataInfo list(ChatAILog chatAILog)
    {
        startPage();
        List<ChatAILog> list = chatgptLogService.selectChatAILogList(chatAILog);
        return getDataTable(list);
    }

    /**
     * 导出ChatAI聊天记录日志列表
     */
    @Log(title = "ChatAI聊天记录日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(ChatAILog chatAILog)
    {
        List<ChatAILog> list = chatgptLogService.selectChatAILogList(chatAILog);
        ExcelUtil<ChatAILog> util = new ExcelUtil<ChatAILog>(ChatAILog.class);
        return util.exportExcel(list, "log");
    }

    /**
     * 获取ChatAI聊天记录日志详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return Result.success(chatgptLogService.selectChatAILogById(id));
    }

    /**
     * 新增ChatAI聊天记录日志
     */
    @Log(title = "ChatAI聊天记录日志", businessType = BusinessType.INSERT, operatorType = OperatorType.USER)
    @PostMapping
    public Result add(@RequestBody ChatAILog chatAILog)
    {
        return toResult(chatgptLogService.insertChatAILog(chatAILog));
    }

    /**
     * 修改ChatAI聊天记录日志
     */
    @Log(title = "ChatAI聊天记录日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody ChatAILog chatAILog)
    {
        return toResult(chatgptLogService.updateChatAILog(chatAILog));
    }

    /**
     * 删除ChatAI聊天记录日志
     */
    @Log(title = "ChatAI聊天记录日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(chatgptLogService.deleteChatAILogByIds(ids));
    }
}

package com.geekplus.webapp.function.controller;

import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.enums.OperatorType;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.function.entity.ChatgptLog;
import com.geekplus.common.annotation.Log;
import com.geekplus.common.domain.Result;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.enums.BusinessType;
//import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.webapp.function.service.IChatgptLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Chatgpt聊天记录日志
Controller
 *
 * @author 佚名
 * @date 2023-02-25
 */
@RestController
@RequestMapping("/geekplus/chatgptlog")
public class ChatgptLogController extends BaseController
{
    @Autowired
    private IChatgptLogService chatgptLogService;

    /**
     * 查询Chatgpt聊天记录日志列表
     */
    @GetMapping("/list")
    public PageDataInfo list(ChatgptLog chatgptLog)
    {
        startPage();
        List<ChatgptLog> list = chatgptLogService.selectChatgptLogList(chatgptLog);
        return getDataTable(list);
    }

    /**
     * 导出Chatgpt聊天记录日志列表
     */
    @Log(title = "Chatgpt聊天记录日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(ChatgptLog chatgptLog)
    {
        List<ChatgptLog> list = chatgptLogService.selectChatgptLogList(chatgptLog);
        ExcelUtil<ChatgptLog> util = new ExcelUtil<ChatgptLog>(ChatgptLog.class);
        return util.exportExcel(list, "log");
    }

    /**
     * 获取Chatgpt聊天记录日志详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return Result.success(chatgptLogService.selectChatgptLogById(id));
    }

    /**
     * 新增Chatgpt聊天记录日志
     */
    @Log(title = "Chatgpt聊天记录日志", businessType = BusinessType.INSERT, operatorType = OperatorType.USER)
    @PostMapping
    public Result add(@RequestBody ChatgptLog chatgptLog)
    {
        return toResult(chatgptLogService.insertChatgptLog(chatgptLog));
    }

    /**
     * 修改Chatgpt聊天记录日志
     */
    @Log(title = "Chatgpt聊天记录日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody ChatgptLog chatgptLog)
    {
        return toResult(chatgptLogService.updateChatgptLog(chatgptLog));
    }

    /**
     * 删除Chatgpt聊天记录日志
     */
    @Log(title = "Chatgpt聊天记录日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(chatgptLogService.deleteChatgptLogByIds(ids));
    }
}

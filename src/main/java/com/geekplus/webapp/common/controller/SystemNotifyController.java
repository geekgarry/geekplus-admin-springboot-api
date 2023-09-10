/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/13 11:55 下午
 * description: 做什么的？
 */
package com.geekplus.webapp.common.controller;

import com.geekplus.common.domain.Result;
import com.geekplus.common.core.socket.WebSocketServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/geekplus/socket")
public class SystemNotifyController {
    //页面请求
    @GetMapping("/index/{userId}")
    public ModelAndView socket(@PathVariable String userId) {
        ModelAndView mav = new ModelAndView("/socket1");
        mav.addObject("userId", userId);
        return mav;
    }

    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public Result pushToWeb(@PathVariable String cid, String message) {
        Map<String,Object> result = new HashMap<>();
        WebSocketServer.sendInfo(message, cid);
        result.put("code", cid);
        result.put("msg", message);
        return Result.success(result);
    }

}

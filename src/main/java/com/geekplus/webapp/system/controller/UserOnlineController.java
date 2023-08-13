package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.core.socket.WebSocketServer;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/6/14 2:44 下午
 * description: 做什么的？
 */
@RestController
@Slf4j
@RequestMapping("/sysUser")
public class UserOnlineController extends BaseController {

    @Resource
    private SysUserService sysUserService;

    @Log(title = "在线用户",businessType = BusinessType.SELECT)
    @GetMapping("/Online")
    public Result sysUserOnline() throws IOException {
        /**
         * 通过JSR303 验证数据的完整性 若存在错误的的格式则返回前端错误代码提示信息
         * errors.getFieldErrors()
         */
//        if(errors.hasErrors()){
//            return Result.error(ApiExceptionEnum.FAIL);
//        }
//        Set set2=WebSocketServer.getWebSocketSet();
//        log.info("在线用户：{}",set2);
        Set onlineSet= WebSocketServer.getSessionPool().keySet();
        //Set onlineSet=shiroSessionListener.getAtomSession();
        log.info("在线用户数：{},在线用户：{}",onlineSet.size(),onlineSet);
        Set<Map<String,Object>> onlineUser=new HashSet<>();
        onlineSet.stream().forEach((onlineToken) ->{
            //String onlineTokenUserName= StringUtils.strip(onlineMap.keySet().toString(),"[]");
            log.info("在线用户的set转为map展示：{}", onlineToken);
            String userName=onlineToken.toString().split(":")[1];
            String tokenId=onlineToken.toString().split(":")[0];
//            long dateTime=Long.parseLong(set1.toString().split(":")[2]);
//            Date nowTime = new Date(dateTime);
//            SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
//            String retStrFormatNowDate = sdFormatter.format(nowTime);
            SysUser userInfo=new SysUser();
            userInfo.setUserName(userName);
            LoginUser sysUser=sysUserService.selectUserBy(userInfo);
            Map<String,Object> map=new HashMap<>();
            map.put("token",tokenId);
            map.put("userName",userName);
            map.put("nickName",sysUser.getNickName());
            map.put("email",sysUser.getEmail());
            map.put("gender",sysUser.getGender());
            map.put("phoneunmber",sysUser.getPhoneunmber());
            map.put("loginTime",sysUser.getLoginTime());
            onlineUser.add(map);
        });
        return Result.success(onlineUser);
    }
}

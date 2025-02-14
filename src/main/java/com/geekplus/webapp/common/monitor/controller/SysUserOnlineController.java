package com.geekplus.webapp.common.monitor.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.webapp.common.monitor.entity.SysUserOnline;
import com.geekplus.webapp.common.monitor.service.ISysUserOnlineService;
import com.geekplus.webapp.system.entity.SysUser;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author
 */
@RestController
@RequestMapping("/monitor/online")
public class SysUserOnlineController extends BaseController {
    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private RedisUtil redisUtil;

    @RequiresRoles(value = {"admin","webManage","development"}, logical = Logical.OR)
    @RequiresPermissions("monitor:online:list")
    @GetMapping("/list")
    public PageDataInfo list(String ipAddr, String username)
    {
        Collection<String> keys = redisUtil.keys(Constant.PRE_REDIS_USER_TOKEN + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (String key : keys)
        {
            LoginUser user = (LoginUser) redisUtil.get(key);
            if (StringUtils.isNotEmpty(ipAddr) && StringUtils.isNotEmpty(username))
            {
                if (StringUtils.equals(ipAddr, user.getLoginIp()) && StringUtils.equals(username, user.getSysUser().getUsername()))
                {
                    userOnlineList.add(userOnlineService.selectOnlineByInfo(ipAddr, username, user));
                }
            }
            else if (StringUtils.isNotEmpty(ipAddr))
            {
                if (StringUtils.equals(ipAddr, user.getLoginIp()))
                {
                    userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipAddr, user));
                }
            }
            else if (StringUtils.isNotEmpty(username) && StringUtils.isNotNull(user))
            {
                if (StringUtils.equals(username, user.getSysUser().getUsername()))
                {
                    userOnlineList.add(userOnlineService.selectOnlineByUserName(username, user));
                }
            }
            else
            {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @RequiresRoles(value = {"admin","webManage","development"}, logical = Logical.OR)
    @RequiresPermissions("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public Result forceLogout(@PathVariable String tokenId)
    {
        redisUtil.del(Constant.PRE_REDIS_USER_TOKEN + tokenId);
        return Result.success();
    }

    /**
     * 统计在线用户数
     */
    @GetMapping("/count")
    public Result onlineCount(){
        Collection<String> keys = redisUtil.keys(Constant.PRE_REDIS_USER_TOKEN + "*");
        return Result.success(keys.size());
    }
}

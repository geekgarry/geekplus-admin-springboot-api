package com.geekplus.webapp.common.monitor.controller;

import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.Result;
import com.geekplus.framework.domain.Server;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控
 *
 * @author
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController extends BaseController
{
    @RequiresRoles(value = {"admin","webManage","development"}, logical = Logical.OR)
    @RequiresPermissions("monitor:server:list")
    @GetMapping()
    public Result getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return Result.success(server);
    }
}

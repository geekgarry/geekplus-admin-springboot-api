package com.geekplus.webapp.system.controller;

import com.geekplus.common.annotation.Log;
import com.geekplus.common.config.WebAppConfig;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.BusinessType;
import com.geekplus.common.enums.OperatorType;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.page.PageDataInfo;
import com.geekplus.common.util.ServletUtils;
import com.geekplus.common.util.file.FileUploadUtils;
import com.geekplus.common.util.file.FileUtils;
import com.geekplus.common.util.poi.ExcelUtil;
import com.geekplus.framework.jwtshiro.JwtTokenUtil;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.UserTokenService;
import com.geekplus.webapp.system.service.SysMenuService;
import com.geekplus.webapp.system.service.SysRoleService;
import com.geekplus.webapp.system.service.SysUserService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统用户表 系统用户表
 * Created by CodeGenerator on 2023/06/18.
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private UserTokenService userTokenService;
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 增加 系统用户表
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "添加系统用户",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    @PostMapping("/add")
    public Result add(@RequestBody SysUser sysUser) {
        return toResult(sysUserService.insertSysUser(sysUser));
    }

    @RequiresPermissions("system:user:add")
    @Log(title = "系统添加用户信息",businessType = BusinessType.INSERT,operatorType = OperatorType.MANAGE)
    @PostMapping("/addEncodePwd")
    public Result addEncodePwd(@RequestBody SysUser sysUser) {
        return toResult(sysUserService.insertSysUserEnCodePwd(sysUser));
    }

    /**
    * 删除 系统用户表
    */
    @RequiresPermissions("system:user:delete")
    @Log(title = "删除用户",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @GetMapping("/delete")
    public Result remove(@RequestParam Long userId) {
        return toResult(sysUserService.deleteSysUserById(userId));
    }

    /**
    * 批量删除 系统用户表
    */
    @RequiresPermissions("system:user:delete")
    @Log(title = "批量删除用户",businessType = BusinessType.DELETE,operatorType = OperatorType.MANAGE)
    @DeleteMapping("/{userIds}")
    public Result remove(@PathVariable Long[] userIds) {
        return toResult(sysUserService.deleteSysUserByIds(userIds));
    }

    /**
    * 更新 系统用户表
    */
    @RequiresPermissions("system:user:update")
    @Log(title = "更新用户信息",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
    @PutMapping
    public Result edit(@RequestBody SysUser sysUser) {
        return toResult(sysUserService.updateSysUser(sysUser));
    }

    /**
     * 管理员重置系统用户密码
     */
    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "重置用户密码",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
    @GetMapping("/resetUserPwd")
    public Result resetUserPwd(SysUser sysUser) {
        return toResult(sysUserService.updateSysUserPwd(sysUser));
    }

    /**
     * 用户修改系统用户密码
     */
    @RequiresPermissions("system:user:update")
    @Log(title = "修改用户密码",businessType = BusinessType.UPDATE,operatorType = OperatorType.MANAGE)
    @GetMapping("/updateUserPwd")
    public Result updateUserPwd(String oldPassword, String newPassword) {
        //Session session= SecurityUtils.getSubject().getSession();
        //Long userId=Long.parseLong(session.getAttribute("userId").toString());
        SysUser sysUser=new SysUser();
        sysUser.setUserId(userTokenService.getSysUserId(ServletUtils.getRequest()));
        sysUser.setPassword(oldPassword);
        if(sysUserService.selectSysUserByPassword(sysUser)!=null){
            sysUser.setPassword(newPassword);
            return toResult(sysUserService.updateSysUserPwd(sysUser));
        }else{
            return Result.error("原密码不正确");
        }
    }

    /**
    * 单条数据详情 系统用户表
    */
    @RequiresPermissions("system:user:detail")
    @GetMapping("/detail")
    public Result detail(@RequestParam Long userId) {
        SysUser sysUser = sysUserService.selectSysUserById(userId);
        List<SysRole> sysRoles=sysRoleService.getRolesByUserId(userId.toString());
        sysUser.setSysRoleList(sysRoles);
        return Result.success(sysUser);
    }

    /**
     * 系统用户信息
     */
    @RequiresPermissions("system:user:profile")
    @GetMapping("/userProfile")
    public Result userProfile() {
        LoginUser sysUser=new LoginUser();
        String token = userTokenService.getToken(ServletUtils.getRequest());
        if(token!= null) {
            if (jwtTokenUtil.verify(token)) {
                sysUser = userTokenService.getUserInfo(ServletUtils.getRequest());
            }
        }
        return Result.success(sysUser);
    }

    /**
     * 更新当前用户头像 系统用户表
     */
    @RequiresPermissions("system:user:avatar")
    @GetMapping("/updateAvatar")
    public Result updateAvatar(String avatar) {
        String userName=userTokenService.getSysUserName();
        boolean isUpdate=sysUserService.updateUserAvatar(userName,avatar);
        return isUpdate?Result.success((Object)avatar):Result.error();
    }

    /**
     * 头像上传
     */
    @RequiresPermissions("system:user:upAvatar")
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public Result avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = userTokenService.getUserInfo(ServletUtils.getRequest());
            String avatar = FileUploadUtils.upload(WebAppConfig.getAvatarPath(), file);
            if (sysUserService.updateUserAvatar(loginUser.getUserName(), avatar))
            {
                Result ajax = Result.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.setAvatar(avatar);
                //loginUserTokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return Result.error("上传图片异常，请联系管理员");
    }

    /**
     * 在线浏览所有用户头像
     */
    @RequiresPermissions("system:user:avatarList")
    @GetMapping("/getAvatarList")
    public Result getAvatarList(String fileFolder)
    {
        File file=new File(WebAppConfig.getProfile()+ File.separator + fileFolder);
        List<String> list= new ArrayList<>();
        FileUtils.getDirectoryAllFile(file,list);
        return Result.success(list);
    }

    /**
    * 条件查询所有 系统用户表
    */
    @RequiresPermissions("system:user:listAll")
    @GetMapping("/listAll")
    public PageDataInfo listAll(SysUser sysUser) {
        //PageHelper.startPage(page, size);
        List<SysUser> list = sysUserService.selectSysUserList(sysUser);
        PageDataInfo rspData = new PageDataInfo();
        rspData.setCode(HttpStatusCode.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        //PageInfo pageInfo = new PageInfo(list);
        return rspData;
    }

    /**
    * 条件查询所有 系统用户表
    */
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public PageDataInfo list(SysUser sysUser) {
        startPage();
        List<SysUser> list = sysUserService.selectSysUserList(sysUser);
        //PageInfo pageInfo = new PageInfo(list);
//        PageDataInfo rspData = new PageDataInfo();
//        rspData.setCode(HttpStatus.SUCCESS);
//        rspData.setMsg("查询成功");
//        rspData.setRows(list);
//        rspData.setTotal(new PageInfo(list).getTotal());
        //直接调用公共方法
        return getDataTable(list);
    }

    /**
     * 导出系统用户表
     */
    @RequiresPermissions("system:user:export")
    @Log(title = "导出系统用户表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public Result export(SysUser sysUser)
    {
        List<SysUser> list = sysUserService.selectSysUserList(sysUser);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "sysUser");
    }
}

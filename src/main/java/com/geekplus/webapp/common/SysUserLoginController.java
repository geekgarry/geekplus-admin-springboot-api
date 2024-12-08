package com.geekplus.webapp.common;

import com.geekplus.common.annotation.RepeatLogin;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.LoginBody;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.util.LogUtil;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.common.util.http.IPUtils;
import com.geekplus.common.util.encrypt.EncryptUtil;
import com.geekplus.common.util.sysmenu.SysMenuUtil;
import com.geekplus.framework.jwtshiro.JwtUtil;
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.SysMenuService;
import com.geekplus.webapp.system.service.SysRoleService;
import com.geekplus.webapp.system.service.SysUserService;
import com.geekplus.webapp.common.service.SysUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 6/18/23 23:01
 * description: 做什么的？
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserLoginController extends BaseController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SysUserTokenService tokenService;

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    @RepeatLogin
    public Result login(@RequestBody LoginBody loginBody, HttpServletResponse response){
        //添加用户认证信息
        String validateCode=loginBody.getValidateCode();
        String validateKey=loginBody.getValidateKey();
        //log.info("数据："+validateKey+"验证码："+validateCode);
        //自己系统的密码加密方式 ,这里简单示例一下MD5
        //String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        //Object salt=ByteSource.Util.bytes("userId");
        //Object md5Password=new SimpleHash("md5", password, salt, 1024);
        loginBody.setPassword(EncryptUtil.md5EncryptPwd(loginBody.getPassword()));
        if(validateCode==null||validateCode.equals("")){
            LogUtil.recordLoginInfo(loginBody, Constant.LOGIN_FAIL, ApiExceptionEnum.CODE_IS_NULL.getMsg());
            throw new BusinessException(ApiExceptionEnum.CODE_IS_NULL);
        }else if(redisUtil.get(validateKey)==null||redisUtil.get(validateKey).equals("")){
            LogUtil.recordLoginInfo(loginBody, Constant.LOGIN_FAIL, ApiExceptionEnum.CODE_IS_EXPIRE.getMsg());
            throw new BusinessException(ApiExceptionEnum.CODE_IS_EXPIRE);
        }
        String token;
        //UsernamePasswordToken upToken=new UsernamePasswordToken(loginUser.getUserName(),loginUser.getPassword());
        if(validateCode==redisUtil.get(validateKey)||validateCode.equalsIgnoreCase(redisUtil.get(validateKey).toString())){
            sysUserService.updateSysUserByUserName(loginBody.getUserName(), IPUtils.getIpAddr(ServletUtil.getRequest()));
            SysUser sysUserInfo =sysUserService.getSysUserInfoBy(loginBody.getUserName());
            //sysUserInfo =sysUserService.sysUserLoginBy(loginUser);
            if(sysUserInfo==null) {
                //subject.logout();
                LogUtil.recordLoginInfo(loginBody, Constant.LOGIN_FAIL,ApiExceptionEnum.LOGIN_USERNAME_ERROR.getMsg());
                throw new BusinessException(ApiExceptionEnum.LOGIN_USERNAME_ERROR);
            }
            if("1".equals(sysUserInfo.getStatus())) {
                //subject.logout();
                LogUtil.recordLoginInfo(loginBody, Constant.LOGIN_FAIL,ApiExceptionEnum.LOGIN_DISABLED_ERROR.getMsg());
                throw new BusinessException(ApiExceptionEnum.LOGIN_DISABLED_ERROR);
            }
            if(!loginBody.getPassword().equals(sysUserInfo.getPassword())){
                LogUtil.recordLoginInfo(loginBody, Constant.LOGIN_FAIL,ApiExceptionEnum.LOGIN_PASSWORD_ERROR.getMsg());
                throw new BusinessException(ApiExceptionEnum.LOGIN_PASSWORD_ERROR);
            }
            LoginUser loginUser = new LoginUser(sysUserInfo, sysUserService.getSysUserMenuPerms(sysUserInfo.getUserId()));
            token = tokenService.createToken(loginUser);
            //JwtToken upToken = new JwtToken(token);
//            try {
//                //进行验证，AuthenticationException可以catch到,但是AuthorizationException因为我们使用注解方式,是catch不到的,所以后面使用全局异常捕抓去获取
//                subject.login(upToken);
//                sysUserService.updateSysUserByUserName(loginUser.getUserName());
//                LogUtil.recordLoginInfo(loginUser,ConstValue.LOGIN_SUCCESS,"登录成功");
//                redisUtil.del(validateKey);
//            } catch (AuthenticationException e) {
//                //subject.logout();
//                LogUtil.recordLoginInfo(loginUser,ConstValue.LOGIN_FAIL,ApiExceptionEnum.LOGIN_FAIL.getMsg());
//                e.printStackTrace();
//                throw new ApiException(ApiExceptionEnum.LOGIN_FAIL);
//            }
            LogUtil.recordLoginInfo(loginBody, Constant.LOGIN_SUCCESS,"登录成功");
            redisUtil.del(validateKey);
        }else {
            LogUtil.recordLoginInfo(loginBody, Constant.LOGIN_FAIL,ApiExceptionEnum.CODE_ERROR.getMsg());
            throw new BusinessException(ApiExceptionEnum.CODE_ERROR);
        }
//        Cookie cookie = new Cookie(Constant.USER_HEADER_TOKEN, token);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//        response.addCookie(cookie);
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        return Result.success(res);
    }

    @GetMapping("/getMenu")
    public Result getMenuList(){
        //LoginUser sysUser= (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //log.info("用户ID为{}",sysUser.getUserId());
        HttpServletRequest request=ServletUtil.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        SysUser sysUser = loginUser.getSysUser();
        String userName = jwtUtil.getUserNameFromToken(tokenService.getToken(request));
        //String userName=JwtTokenUtil.verifyResult(token).getClaim("userName").asString();
        Map<String,Object> map=new HashMap<>();
        //log.info("=========================>"+sysUser.getUserId());
        //List<SysMenu> menuList=sysMenuService.getMenuTreeByUserName(userName);
        List<SysMenu> allMenuList=sysMenuService.getMenuPermsByUserName(userName);
        Set permsSet=allMenuList.stream().filter(sysMenu -> !StringUtils.isEmpty(sysMenu.getPerms())).map(SysMenu::getPerms).collect(Collectors.toSet());
        List<SysMenu> menuList= SysMenuUtil.getParentMenuList(allMenuList.stream().filter(sysMenu -> !sysMenu.getMenuType().equals("B")).collect(Collectors.toList()));
        //List<SysMenu> menuList=sysMenuService.getMenuTreeByUserId(loginUser.getUserId());
        map.put("userName", userName);
        map.put("nickName", sysUser.getNickName());
        map.put("userId", sysUser.getUserId());
        map.put("avatar", sysUser.getAvatar());
        map.put("menuList", menuList);
        map.put("permsSet",permsSet);
        return Result.success(map);
    }

    /* 登出操作 */
    @PostMapping("/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        HttpServletRequest request=ServletUtil.getRequest();
        String token = tokenService.getToken(request);
        tokenService.delLoginUser(token);
        return Result.success();
    }

    /**
      * @Author geekplus
      * @Description //获取当前用户的权限菜单Menus
      */
    @PostMapping("/getRoutesMenu")
    public Result getRoutesMenu() {
        String userName = tokenService.getSysUserName();
        return Result.success(sysMenuService.getMenuTreeByUserName(userName));
    }

    /**
     * @Author geekplus
     * @Description //获取当前用户的权限菜单Menus
     */
    @GetMapping("/refreshUserAuth")
    public Result refreshUserAuth() {
        String userName = tokenService.getSysUserName();
        SysUser sysUserInfo =sysUserService.getSysUserInfoBy(userName);
        Set<String> sysMenus = sysUserService.getSysUserMenuPerms(sysUserInfo.getUserId());
        LoginUser loginUser = new LoginUser(sysUserInfo, sysMenus);
        tokenService.refreshUserTokenId(loginUser);
        return Result.success();
    }

    /**
     * 未登录
     */
    @RequestMapping(value = "/unLogin", method = RequestMethod.GET)
    public void notLogin() {
        throw new BusinessException(ApiExceptionEnum.LOGIN_AUTH);
    }

    /**
     * 无权限访问
     */
    @RequestMapping(value = "/unAuth", method = RequestMethod.GET)
    public void notRole() {
        throw new BusinessException(ApiExceptionEnum.PERMISSION);
    }
}

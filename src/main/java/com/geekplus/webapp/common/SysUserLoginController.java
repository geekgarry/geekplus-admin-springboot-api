/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 6/18/23 23:01
 * description: 做什么的？
 */
package com.geekplus.webapp.common;

import com.geekplus.common.constant.Constant;
import com.geekplus.common.core.controller.BaseController;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.util.LogUtil;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.ServletUtil;
import com.geekplus.common.util.ServletUtils;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.common.util.ip.IpUtils;
import com.geekplus.common.util.shiro.ShiroEncrypt;
import com.geekplus.common.util.sysmenu.SysMenuUtil;
import com.geekplus.framework.jwtshiro.JwtTokenUtil;
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.SysMenuService;
import com.geekplus.webapp.system.service.SysUserService;
import com.geekplus.webapp.system.service.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserLoginController extends BaseController {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserTokenService tokenService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public Result login(@RequestBody LoginUser loginUser){
        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        String validateCode=loginUser.getValidateCode();
        String validateKey=loginUser.getValidateKey();
        //log.info("数据："+validateKey+"验证码："+validateCode);
        //自己系统的密码加密方式 ,这里简单示例一下MD5
        //String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        //Object salt=ByteSource.Util.bytes("userId");
        //Object md5Password=new SimpleHash("md5", password, salt, 1024);
        if(validateCode==null||validateCode.equals("")){
            LogUtil.recordLoginInfo(loginUser, Constant.LOGIN_FAIL, ApiExceptionEnum.CODE_IS_NULL.getMsg());
            throw new BusinessException(ApiExceptionEnum.CODE_IS_NULL);
        }else if(redisUtil.get(validateKey)==null||redisUtil.get(validateKey).equals("")){
            LogUtil.recordLoginInfo(loginUser, Constant.LOGIN_FAIL,ApiExceptionEnum.CODE_IS_EXPIRE.getMsg());
            throw new BusinessException(ApiExceptionEnum.CODE_IS_EXPIRE);
        }
        String token=null;
        LoginUser sysUserInfo=new LoginUser();
        //UsernamePasswordToken upToken=new UsernamePasswordToken(loginUser.getUserName(),loginUser.getPassword());
        if(validateCode==redisUtil.get(validateKey)||validateCode.equalsIgnoreCase(redisUtil.get(validateKey).toString())){
            //SysUser userParams = new SysUser();
            //userParams.setUserName(loginUser.getUserName());
            sysUserService.updateSysUserByUserName(loginUser.getUserName(),IpUtils.getIpAddr(ServletUtils.getRequest()));
            sysUserInfo =sysUserService.selectUserAllInfo(loginUser.getUserName());
            //sysUserInfo =sysUserService.selectUserBy(userParams);
            String formPassword= ShiroEncrypt.md5EncryptPwd(loginUser.getPassword());
            if(sysUserInfo==null) {
//                subject.logout();
                LogUtil.recordLoginInfo(loginUser, Constant.LOGIN_FAIL,ApiExceptionEnum.LOGIN_USERNAME_ERROR.getMsg());
                throw new BusinessException(ApiExceptionEnum.LOGIN_USERNAME_ERROR);
            }
            if("1".equals(sysUserInfo.getStatus())) {
//                subject.logout();
                LogUtil.recordLoginInfo(loginUser, Constant.LOGIN_FAIL,ApiExceptionEnum.LOGIN_DISABLED_ERROR.getMsg());
                throw new BusinessException(ApiExceptionEnum.LOGIN_DISABLED_ERROR);
            }
            if(!formPassword.equals(sysUserInfo.getPassword())){
                LogUtil.recordLoginInfo(loginUser, Constant.LOGIN_FAIL,ApiExceptionEnum.LOGIN_PASSWORD_ERROR.getMsg());
                throw new BusinessException(ApiExceptionEnum.LOGIN_PASSWORD_ERROR);
            }
            token = tokenService.loginToken(sysUserInfo);
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
            LogUtil.recordLoginInfo(loginUser, Constant.LOGIN_SUCCESS,"登录成功");
            redisUtil.del(validateKey);
        }else {
            LogUtil.recordLoginInfo(loginUser, Constant.LOGIN_FAIL,ApiExceptionEnum.CODE_ERROR.getMsg());
            throw new BusinessException(ApiExceptionEnum.CODE_ERROR);
        }
        //SysUser sUser = (SysUser)subject.getPrincipal();
        log.info("tokenId:"+token);
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("userName", sysUserInfo.getUserName());
        return Result.success(res);
    }

    @GetMapping("/getMenu")
    public Result getMenuList(){
        //LoginUser sysUser= (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //log.info("用户ID为{}",sysUser.getUserId());
        HttpServletRequest request=ServletUtil.getRequest();
//        String token = tokenService.getToken(request);
//        if (token==null||token.equals("")){
//            return Result.error(ApiExceptionEnum.NO_USER_ID);
//        }
        String token =tokenService.getToken(request);
        String userName=jwtTokenUtil.getUserNameFromToken(token);
        SysUser sysUser = new SysUser();
        sysUser.setUserName(userName);
        LoginUser loginUser =sysUserService.selectUserBy(sysUser);
        //String userName=JwtTokenUtil.verifyResult(token).getClaim("userName").asString();
        Map<String,Object> map=new HashMap<>();
        //log.info("=========================>"+sysUser.getUserId());
        //List<SysMenu> menuList=sysMenuService.getMenuTreeByUserName(userName);
        List<SysMenu> allMenuList=sysMenuService.getMenuPermsByUserName(userName);
        Set permsSet=allMenuList.stream().filter(sysMenu -> !StringUtils.isEmpty(sysMenu.getPerms())).map(SysMenu::getPerms).collect(Collectors.toSet());
        List<SysMenu> menuList= SysMenuUtil.getParentMenuList(allMenuList.stream().filter(sysMenu -> !sysMenu.getMenuType().equals("B")).collect(Collectors.toList()));
        //List<SysMenu> menuList=sysMenuService.getMenuTreeByUserId(loginUser.getUserId());
        map.put("userName", userName);
        map.put("nickName", loginUser.getNickName());
        map.put("userId", loginUser.getUserId());
        map.put("avatar", loginUser.getAvatar());
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

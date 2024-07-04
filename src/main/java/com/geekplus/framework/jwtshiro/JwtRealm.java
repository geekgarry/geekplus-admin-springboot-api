package com.geekplus.framework.jwtshiro;

import com.geekplus.common.core.LoggerFactory;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.util.DateTimeUtils;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.webapp.system.entity.SysMenu;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtRealm extends AuthorizingRealm {
    private static Logger log =  LoggerFactory.getLogger(JwtRealm.class.getName());

    @Autowired
    private UserTokenService tokenService;

//    @Autowired
//    private SysUserService sysUserService;
//
//    @Autowired
//    private SysRoleService sysRoleService;
//
//    @Autowired
//    private SysMenuService sysMenuService;

    // 让shiro支持我们自定义的token，即如果传入的token时JWTToken则放行
    // 必须重写不然shiro会报错
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        LoginUser loginUser = (LoginUser) principalCollection.getPrimaryPrincipal();
        //String userId = JwtTokenUtil.verifyResult(token).getClaim("userId").asString();
        //log.info("用户的信息{}", loginUser.getUserId());
        List<SysRole> roles = loginUser.getSysRoleList();
        //log.info("用户角色{}", roles);
        List<SysMenu> menus = loginUser.getSysMenuList();
        // 去重操作
//        Set set = new HashSet();
//        set.addAll(menus);
//        menus.clear();
//        menus.addAll(set);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

//        Set roleSet=userRolePermList.stream().map(map->map.get("roleKey").toString()).collect(Collectors.toSet());
//        Set permSet=userRolePermList.stream().map(map->map.get("permName").toString()).collect(Collectors.toSet());
        Set roleSet2=roles.stream().filter(sysRole -> !StringUtils.isEmpty(sysRole.getRoleKey())).map(SysRole::getRoleKey).collect(Collectors.toSet());
        Set permSet2=menus.stream().filter(sysMenu -> !StringUtils.isEmpty(sysMenu.getPerms())).map(SysMenu::getPerms).collect(Collectors.toSet());
        //permSet2.remove("");
        simpleAuthorizationInfo.addRoles(roleSet2);
        simpleAuthorizationInfo.addStringPermissions(permSet2);
        log.info("授权当前Subject用户为：{} 所属角色：{} ||| {}", loginUser.getUserName(),roleSet2,permSet2);
//        userRolePermList.stream().forEach(rolePermMap -> {
//        });
//        numbersList.stream().distinct().collect(Collectors.toList());
//        for (Map<String, Object> rolePermMap : userRolePermList) {
//            //添加角色
//            simpleAuthorizationInfo.addRole(rolePermMap.get("roleKey").toString());
//            //添加权限
//            simpleAuthorizationInfo.addStringPermission(rolePermMap.get("permName").toString());
//        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("####################### 执行Shiro权限认证 #####################");
        String token = (String) authenticationToken.getCredentials();// 重写了该类，实际上返回的是token
        if (token == null) {
            log.info("————————身份认证失败——————————");
            throw new AuthenticationException("token缺失！非法无效!");
        }
        // 根据token获得登录用户的email
        //String username = JwtTokenUtil.verifyResult(token).getClaim("userName").asString();
        //log.info("从token中获取用户名为{}",username);
        // 第一种方式
        // 获取用户输入的账号和密码(一般只需要获取账号就可以)
        //String username = (String) authenticationToken.getPrincipal();
        //String password = new String((char[]) authenticationToken.getCredentials());
        //System.out.println("用户："+username);
        // 第二种方式（推荐第一种）c
        // UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        // String username = usernamePasswordToken.getUsername();
        // String password = new String(usernamePasswordToken.getPassword());
        System.out.println("*************###################***************");
        // 通过username从redis或数据库中查找 User对象，如果找到则进行验证
        LoginUser loginUser=tokenService.checkUserTokenGetLoginUser(token);
        // 进行验证
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(loginUser, token, getName());
        log.info("认证用户SysUser:"+authenticationInfo.getPrincipals().getPrimaryPrincipal()+" token:"+authenticationInfo.getCredentials());
        return authenticationInfo;
    }

    public static void main(String[] args) {
//        String hashAlgorithmName = "MD5";
//        String credentials = "123456";
//        int hashIterations = 1024;
//        ByteSource credentialsSalt = ByteSource.Util.bytes("plus");
//        Object obj = new SimpleHash(hashAlgorithmName, credentials, credentialsSalt, hashIterations);
//        System.out.println(obj);
//        String value=null;
//        System.out.println(StringUtils.isNoneBlank(value));
//        System.out.println(StringUtils.isAnyBlank("fdhkhfd","ss",""));
    }
}

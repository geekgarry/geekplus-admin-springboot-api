//package com.geekplus.framework.shiro;
//
//import com.geekplus.common.domain.LoginUser;
//import com.geekplus.common.util.http.ServletUtil;
//import com.geekplus.webapp.system.pojo.SysMenu;
//import com.geekplus.webapp.system.pojo.SysRole;
//import com.geekplus.webapp.system.pojo.SysUser;
//import com.geekplus.webapp.system.service.*;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.crypto.hash.SimpleHash;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.util.ByteSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.util.StringUtils;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
///**
// * author     : geekplus
// * email      : geekcjj@gmail.com
// * date       : 2022/4/24 3:53 下午
// * description: 做什么的？
// */
//@Slf4j
//public class ShiroRealm extends AuthorizingRealm {
//
//    @Value("${geekplus.name}")
//    private String appName;
//
//    @Value("${token.expireTime}")
//    private long expireTime;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private SysUserService sysUserService;
//
//    @Autowired
//    private SysRoleService sysRoleService;
//
//    @Autowired
//    private SysMenuService sysMenuService;
//
//    @Autowired
//    private SysPermissionService sysPermissionService;
//
//    @Autowired
//    private RedisSessionDao redisSessionDao;
//
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        //获取登录用户名
//        SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
//        //添加角色和权限
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        List<SysRole> roleList=(List<SysRole>) ServletUtil.getSession().getAttribute("roles");
//        List<SysMenu> menuList=(List<SysMenu>) ServletUtil.getSession().getAttribute("menus");
////        List<Map<String,Object>> userRolePermList = sysPermissionService.getRolePermissionByUserName(sysUser);
//
////        Set roleSet=userRolePermList.stream().map(map->map.get("roleKey").toString()).collect(Collectors.toSet());
////        Set permSet=userRolePermList.stream().map(map->map.get("permName").toString()).collect(Collectors.toSet());
//        Set roleSet2=roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
//        Set permSet2=menuList.stream().map(SysMenu::getPerms).collect(Collectors.toSet());
//        //permSet2.remove("");
//        simpleAuthorizationInfo.addRoles(roleSet2);
//        simpleAuthorizationInfo.addStringPermissions(permSet2);
//        log.info("验证当前Subject用户为：{} 所属角色：{} ||| {}", sysUser.getUserName(),roleSet2,permSet2);
////        userRolePermList.stream().forEach(rolePermMap -> {
////        });
////        numbersList.stream().distinct().collect(Collectors.toList());
////        for (Map<String, Object> rolePermMap : userRolePermList) {
////            //添加角色
////            simpleAuthorizationInfo.addRole(rolePermMap.get("roleKey").toString());
////            //添加权限
////            simpleAuthorizationInfo.addStringPermission(rolePermMap.get("permName").toString());
////        }
//        return simpleAuthorizationInfo;
//    }
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        log.info("####################### 执行Shiro权限认证 #####################");
//        // 第一种方式
//        // 获取用户输入的账号和密码(一般只需要获取账号就可以)
//        String oldsesid = (String) redisTemplate.opsForValue().get(authenticationToken.getPrincipal());
//        String username = (String) authenticationToken.getPrincipal();
//        String password = new String((char[]) authenticationToken.getCredentials());
//        System.out.println("oldSessionId:"+oldsesid);
//        System.out.println("用户："+username+"密码："+password);
//        // 第二种方式（推荐第一种）c
//        // UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
//        // String username = usernamePasswordToken.getUsername();
//        // String password = new String(usernamePasswordToken.getPassword());
//        SysUser sUser=new SysUser();
//        sUser.setUserName(username);
//        sUser.setPassword(password);
//        System.out.println("*************###################***************");
//        // 通过username从数据库中查找 User对象，如果找到则进行验证
//        LoginUser sysUser = sysUserService.selectUserBy(sUser);
//        // 判断账号是否存在
//        if(sysUser == null){
//            throw new UnknownAccountException();
//        }
//        if(sysUser.getStatus().equals("1")){// 用户状态为1，表示该用户被禁用了
//            throw new DisabledAccountException();
//        }
////        if(sysUser.getStatus().equals("1")){
////            LogUtil.recordLoginInfo(sysUser, ConstValue.LOGIN_FAIL,ApiExceptionEnum.LOGIN_DISABLED_ERROR.getMsg());
////            throw new ApiException(ApiExceptionEnum.LOGIN_DISABLED_ERROR);
////        }
//        // 用户互踢
//        Session session = SecurityUtils.getSubject().getSession();
//        String newsesId = session.getId().toString();
//        if (!StringUtils.isEmpty(oldsesid) && !newsesId.equals(oldsesid)) {
//            Session oldSession = (Session) redisTemplate.opsForValue().get(oldsesid);
//            try {
//                redisSessionDao.delete(oldSession);
//                oldSession.stop();
//            } catch (Exception e) {
//                log.info("{} session stop success", session);
//            }
//            redisTemplate.delete(oldsesid);
//            log.debug("del session,oldsessionid:{}, newsessionid:{}", oldsesid, newsesId);
//        }
//        session.setTimeout(expireTime*1000);// 半个小时
//        // 进行验证
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
//                // 用户名
//                sysUser,
//                // 密码
//                sysUser.getPassword(),
//                // 盐值
//                ByteSource.Util.bytes("plus"),//sysUser.getUserId().toString()
//                getName()
//        );
//        log.info("***************************##########****************************");
//        //List<Role> roles = roleService.getRolesByUserId(operator.getUserId(), operator.getChannelId());
//        List<SysRole> roles = sysRoleService.getRolesByUserId(sysUser.getUserId().toString());
//        log.info("用户角色{}", roles.size());
//        List<SysMenu> menus = sysMenuService.getSysMenuByRoles(roles);
//        sysUser.setSysRoleList(roles);
//        Set set = new HashSet();
//        set.addAll(menus);
//        menus.clear();
//        menus.addAll(set);
//        //session.setAttribute("roleName", roles.stream().map(SysRole::getRoleName).collect(Collectors.toSet()));
//        session.setAttribute("roles", roles);
//        //session.setAttribute("menuName", menus.stream().map(SysMenu::getMenuName).collect(Collectors.toSet()));
//        session.setAttribute("menus", menus);
//        session.setAttribute("userId", sysUser.getUserId());
//        session.setAttribute("userName", sysUser.getUserName());
//        session.setAttribute("nickName", sysUser.getNickName());
//        redisTemplate.opsForValue().set(appName + ":user:" +sysUser.getUserId(), newsesId, expireTime, TimeUnit.SECONDS);
//        log.info("SysUser:"+authenticationInfo.getPrincipals()+"Info:"+authenticationInfo.getPrincipals().getPrimaryPrincipal());
//        return authenticationInfo;
//    }
//    public static void main(String[] args) {
//        String hashAlgorithmName = "MD5";
//        String credentials = "123456";
//        int hashIterations = 1024;
//        ByteSource credentialsSalt = ByteSource.Util.bytes("plus");
//        Object obj = new SimpleHash(hashAlgorithmName, credentials, credentialsSalt, hashIterations);
//        System.out.println(obj);
//    }
//}

package com.geekplus.webapp.common.service;

import com.geekplus.common.constant.Constant;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.http.CookieUtil;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.common.util.uuid.UUIDUtil;
import com.geekplus.framework.jwtshiro.JwtUtil;
import com.geekplus.webapp.system.entity.SysRole;
import com.geekplus.webapp.system.entity.SysUser;
import com.geekplus.webapp.system.service.SysRoleService;
import com.geekplus.webapp.system.service.SysUserService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/26 10:21 下午
 * description: 做什么的？
 */
@Component
@Slf4j
public class SysUserTokenService {

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    @Value("${token.expireTime}")
    private Long expireTime;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private JwtUtil jwtUtil;

    // 确保每个用户一个会话
    protected final ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<String, String>();

    /**
     * 登录的时候需要去执行的token生成还有redis存储以token为key，loginUser为value的数据
     * @param loginUser
     * @return
     */
    public String createToken(LoginUser loginUser){
        SysUser sysUser = loginUser.getSysUser();
        String tokenUuid= UUIDUtil.getShaUUID(sysUser.getUserName());
        loginUser.setTokenId(tokenUuid);
        String token = refreshToken(loginUser);
        kickOutSameUser(sysUser.getUserName(), token);
        return token;
    }

    /**
     * 在用户需要延期token和刷新redis的时候
     * @param loginUser
     * @param
     * @return
     */
    public String refreshToken(LoginUser loginUser){
        String token = jwtUtil.sign(loginUser);
        refreshUserTokenId(loginUser);
        return token;
    }

    /**
     * 在用户需要延期token和刷新redis的时候
     * @param loginUser
     * @param
     * @return
     */
    public void refreshUserTokenId(LoginUser loginUser){
        setUserAgent(loginUser);
        setLoginUser(loginUser);
    }

    /**
      * @Author geekplus
      * @Description //这里是为了检查校验每次请求的token是否有效，
      * 有效就得到redis或数据库中的登录用户信息
      * @Param
      * @Throws
      * @Return {@link }
      */
    public LoginUser checkUserTokenGetLoginUser(String token) { //IsEffect
        if (!jwtUtil.verify(token)) {
            throw new AuthenticationException("用户认证失败！token已经过期失效，请重新登录！");
        }else {
            String username = jwtUtil.getUserNameFromToken(token);
            if (username == null) {
                throw new AuthenticationException("token非法无效!");
            }
            log.info("从token中获取用户名为{}", username);
            LoginUser user = (LoginUser) redisUtil.get(getTokenKey(jwtUtil.getTokenIdFromToken(token)));
            if(user==null || "".equals(user)) {
                user.setSysUser(sysUserService.getSysUserInfoBy(username));
            }
            //user.setTokenId(token);
            // 校验token是否超时失效 & 或者账号密码是否错误 核心部分
            if (!jwtTokenRefresh(token, user)) {
                throw new AuthenticationException("token已经过期失效，请重新登录！");
            }
            return user;
        }
    }

    /**
      * @Author geekplus
      * @Description //判断redis存储tokenId用户信息是否需要刷新过期时间，
      * 如果需要则重新签发token再放入返回响应头中，前端可以在当前请求的响应头中取到新的token，
      * 前端再设置覆盖原cookie的旧值
      * @Param
      * @Throws
      * @Return {@link }
      */
    public boolean jwtTokenRefresh(String token, LoginUser loginUser) {
        Long currentTimeMillis= System.currentTimeMillis();
        //LoginUser userInfo = (LoginUser) redisUtil.get(Constant.LOGIN_USER_TOKEN+token);
        String tokenId= jwtUtil.getTokenIdFromToken(token);
        //LoginUser logUser = tokenService.getUserInfo(ServletUtil.getRequest());
        //if(StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getSubject().getPrincipal())){
        //if(!"".equals(token) && null!=token && !token.equals("null")){
        if(redisUtil.hasKey(getTokenKey(tokenId))){
            //if(!JwtTokenUtil.verify(cacheToken,loginUser)){
            if(jwtUtil.checkRefresh(token,currentTimeMillis)){
                //redisUtil.del(getTokenKey(tokenId));
                String newAuthorization = refreshToken(loginUser);
                log.info("刷新的token: {}",newAuthorization);
                // 设置超时时间
                // 最后将刷新的AccessToken存放在Response的Header中的Cookie/Authorization字段返回,
                // 根据项目自由选择cookie还是header
                HttpServletResponse response = ServletUtil.getResponse();
                Cookie cookie = new Cookie(header, newAuthorization);
                cookie.setPath("/");
                response.addCookie(cookie);
                //response.setHeader(header, newAuthorization);
                //response.setHeader("Access-Control-Expose-Headers", header);
            }
//            else{
//                loginUser.setTokenId(tokenId);
//                redisUtil.set(Constant.LOGIN_USER_TOKEN+tokenId, loginUser, expireTime);
//                //throw new AuthenticationException("token认证失效,token过期,重新登录!");
//            }
            return true;
        }
        return false;
    }

    /**
     * 设置用户身份信息,在用户修改后更新到redis中
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getTokenId()))
        {
            refreshRedisTokenId(loginUser);
        }
    }

    /**
     * 获取用户身份信息，从token拿到tokenId，redis拿到用户信息
     *
     * @return 用户信息,个人中心用户信息查询
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            // 解析对应的权限以及用户信息
            String uuid = jwtUtil.getTokenIdFromToken(token);
            String userKey = getTokenKey(uuid);
            LoginUser user = (LoginUser)redisUtil.get(userKey);
            if(user == null) {
                setUserAgent(user);
                user.setSysUser(sysUserService.getSysUserInfoBy(jwtUtil.getUserNameFromToken(token)));
                redisUtil.set(userKey, user, expireTime);
            }
            return user;
        }
        return null;
    }

    /**
     * 刷新redis存储的登录用户信息
     * loginUser的信息是要从redis里取出，保证存在userKey
     * @param loginUser 登录信息
     */
    public void refreshRedisTokenId(LoginUser loginUser)
    {
        //loginUser.setLoginTime(new Date());
        // 把存储在redis中的充要信息去除，如密码等
        SysUser sysUser = loginUser.getSysUser();
        sysUser.setPassword(null);
        loginUser.setSysUser(sysUser);
        //loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getTokenId());
        redisUtil.set(userKey, loginUser, expireTime);
    }

    public Long getSysUserId(HttpServletRequest request){
        String token= getToken(request);
        if(StringUtils.isNotEmpty(token)){
            String userName= jwtUtil.getUserNameFromToken(token);
            SysUser sysUser = sysUserService.sysUserLoginBy(userName);
            //SysUser sysUser = sysUserService.selectSysUserById(userId);
            //List<SysRole> sysRoles=sysRoleService.getRolesByUserId(sysUser.getUserId().toString());
            //sysUser.setSysRoleList(sysRoles);
            return sysUser.getUserId();
        }
        return null;
    }

    public String getSysUserName(){
        String token= getToken(ServletUtil.getRequest());
        if(StringUtils.isNotEmpty(token)){
            String userName= jwtUtil.getUserNameFromToken(token);
            return userName;
        }
        return null;
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token){
        if (StringUtils.isNotEmpty(token))
        {
            if(jwtUtil.verify(token)){
                String userKey = getTokenKey(jwtUtil.getTokenIdFromToken(token));
                redisUtil.del(userKey);
            }
        }
    }

    //用户互踢
    private void kickOutSameUser(String newUserName,String token){
        //String userName=JwtTokenUtil.getUserNameFromToken(token);
        /** redis共享，账号互踢 */
        if(tokenMap.get(newUserName) != null) {
            String oldTokenId = tokenMap.get(newUserName);
            // 移除存储的之前用户名和sesion的键值对
            tokenMap.remove(newUserName);
            // 销毁之前session
            redisUtil.del(Constant.PRE_REDIS_USER_TOKEN+oldTokenId);
            // 确保每次登录之后将将用户名和session存进hashmap中
            // sessionUserMap.put(username, session);
        }
        // 确保每次登录之后将将用户名和session存进hashmap中
        tokenMap.put(newUserName, jwtUtil.getTokenIdFromToken(token));
    }

    /**
     * 获取请求token
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        //同时支持cookie，设置cookie的httpOnly为true
//        if(StringUtils.isEmpty(token)){
//            token = CookieUtil.getCookieValue(request, header);
//        }
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constant.AUTHENTICATION_PREFIX))
        {
            token = token.replace(Constant.AUTHENTICATION_PREFIX, "");
        }
        return token;
    }

    //获取redis的key为tokenId
    private String getTokenKey(String uuid)
    {
        return Constant.PRE_REDIS_USER_TOKEN + uuid;
    }

    /**
     *设置用户代理相关请求头信息，浏览器和操作系统
     */
    public void setUserAgent(LoginUser loginUser){
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtil.getRequest().getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        loginUser.setBrowser(browser);
        loginUser.setOs(os);
    }

}

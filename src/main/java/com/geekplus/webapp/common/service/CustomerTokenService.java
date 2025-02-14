package com.geekplus.webapp.common.service;

import com.geekplus.common.constant.Constant;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.http.IPUtils;
import com.geekplus.common.util.http.IpAddressUtil;
import com.geekplus.common.util.http.ServletUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.common.util.uuid.UUIDUtil;
import com.geekplus.framework.jwtshiro.UserJwtUtil;
import com.geekplus.webapp.function.entity.Users;
import com.geekplus.webapp.function.service.UsersService;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * author     : geekplus
 * email      :
 * date       : 9/22/24 7:18 PM
 * description: //TODO
 */
@Service
public class CustomerTokenService {

    //redis过期时间
    private static final long REDIS_EXPIRE_TIME = 6;
    //需要刷新的时间，等于redis的过期时间减去当前时间最后剩余的时间，如果小于这个刷新时间表示需要重新延期
    private static final long REFRESH_TIME = 30 * 60 * 60 * 1000;

    @Resource
    RedisUtil redisUtil;

    @Resource
    private UsersService usersService;
    /*****************************前端客户端tokenService******************************/
    /**
     * 创建令牌
     *
     * @param customer 用户信息
     * @return 令牌
     */
    public String createToken(Users customer)
    {
        String tokenId = UUIDUtil.getMd5UUID(customer.getUsername());
        customer.setTokenId(tokenId);
        //设置网络信息，包括登录ip和登录时间
        setUserInfoAgent(customer);
        //1.生成token
        String token = UserJwtUtil.sign(customer.getUsername(), tokenId);
        // 设置token缓存有效时间
        setRedisKeyValue(customer, tokenId);
        return token;
    }

    /**
     * 在用户需要延期token和刷新redis的时候
     * @param customer
     * @param tokenId
     * @return
     */
    public String refreshTokenId(Users customer, String tokenId){
        if(tokenId==null || "".equals(tokenId)){
            tokenId=UUIDUtil.getMd5UUID(customer.getUsername());
        }
        customer.setTokenId(tokenId);
        //设置网络信息，包括登录ip和登录时间
        setUserInfoAgent(customer);
        //生成token
        String token = UserJwtUtil.sign(customer.getUsername(), tokenId);
        //重新刷新redis缓存
        setRedisKeyValue(customer, tokenId);
        return token;
    }

    /**
     * 在用户突出登录时，移除redis中的tokenId
     * @param tokenId
     * @return
     */
    public void removeCustomerTokenId(String token, String tokenId){
        String tokenIdTemp = tokenId;
        if(token!=null && StringUtils.isNotEmpty(token)){
            tokenIdTemp = UserJwtUtil.getTokenId(token);
        }
        if(redisUtil.hasKey(UserJwtUtil.getCustomerTkIdKey(tokenIdTemp))){
            redisUtil.del(UserJwtUtil.getCustomerTkIdKey(tokenIdTemp));
        }
    }

    /**
     * 获取请求token
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(Constant.CUSTOMER_HEADER_TOKEN);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constant.AUTHENTICATION_PREFIX))
        {
            token = token.replace(Constant.AUTHENTICATION_PREFIX, "");
        }
        return token;
    }

    /**
     *设置用户代理相关请求头信息，浏览器和操作系统
     */
    public void setUserInfoAgent(Users customer){
        HttpServletRequest request = ServletUtil.getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        customer.setLoginIp(IPUtils.getIp(request));
        customer.setLoginAddress(IpAddressUtil.getRealAddressByIP(IPUtils.getIp(request)));
        customer.setLoginTime(new Date());
        //防止重要信息泄漏
        customer.setPassword(null);
        //customer.setBrowser(browser);
        //customer.setOs(os);
    }

    public boolean setRedisKeyValue(Users customer, String tokenId) {
        if (StringUtils.isNotNull(customer) && StringUtils.isNotEmpty(tokenId)) {
            //重新刷新redis缓存
            return redisUtil.set(Constant.PRE_REDIS_CUSTOMER_TOKEN + tokenId, customer, REDIS_EXPIRE_TIME, TimeUnit.HOURS);
        }
        return false;
    }

    public boolean refreshRedisKey(String tokenId) {
        if (StringUtils.isNotEmpty(tokenId)) {
            //重新刷新redis缓存
            return redisUtil.expire(Constant.PRE_REDIS_CUSTOMER_TOKEN + tokenId, REDIS_EXPIRE_TIME, TimeUnit.HOURS);
        }
        return false;
    }

    public boolean checkRedisKeyRefresh(String tokenId){
        if(redisUtil.getExpire(Constant.PRE_REDIS_CUSTOMER_TOKEN + tokenId) - System.currentTimeMillis() < REFRESH_TIME)
            return true;
        else
            return false;
    }

    /**
     * 校验token的有效性
     *
     * @param token
     */
    public Users checkUserTokenIsEffect(String token) throws AuthenticationException {
        // 校验token有效性
        if (!UserJwtUtil.verify(token)) {
            throw new AuthenticationException("用户认证失败！token已经过期失效，请重新登录！");
        }else {
            // 解密获得account，用于和数据库进行对比
            String account = UserJwtUtil.getAccount(token);
            if (account == null) {
                throw new AuthenticationException("用户名不存在！登录无效");
            }

            // 校验token是否失效 & 或者用户名是否存在或错误
            Users customerUser = jwtTokenRefreshMonth(token);
            return customerUser;
        }
    }

    /**
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能），这里是一个月的时间，判断是否在最后24小时后需要重新刷新
     *
     *
     * @param
     * @param
     * @return
     */
    public Users jwtTokenRefreshMonth(String token) {
        //String cacheToken = String.valueOf(redisUtil.get(Constant.LOGIN_CUSTOMER_TOKEN + token));
        if (redisUtil.hasKey(UserJwtUtil.getCustomerTokenKey(token))) {
            Users customer = (Users) redisUtil.get(UserJwtUtil.getCustomerTkIdKey(token));
            if (UserJwtUtil.checkTokenRefresh(token)) {
                String refreshToken = refreshTokenId(customer, UserJwtUtil.getTokenId(token));
                HttpServletResponse response = ServletUtil.getResponse();
                Cookie cookie = new Cookie(Constant.CUSTOMER_HEADER_TOKEN, refreshToken);
                cookie.setPath("/");
                response.addCookie(cookie);
                response.setHeader(Constant.CUSTOMER_HEADER_TOKEN, refreshToken);
                response.setHeader("Access-Control-Expose-Headers", Constant.CUSTOMER_HEADER_TOKEN);
            }else if(checkRedisKeyRefresh(UserJwtUtil.getTokenId(token))){
                refreshRedisKey(UserJwtUtil.getTokenId(token));
            }
            return customer;
        }else {
            Users customerDto = new Users();
            customerDto.setUsername(UserJwtUtil.getAccount(token));
            Users customer = usersService.selectUsersForLogin(customerDto);
            String refreshToken = refreshTokenId(customer, UserJwtUtil.getTokenId(token));
            HttpServletResponse response = ServletUtil.getResponse();
            Cookie cookie = new Cookie(Constant.CUSTOMER_HEADER_TOKEN, refreshToken);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.setHeader(Constant.CUSTOMER_HEADER_TOKEN, refreshToken);
            response.setHeader("Access-Control-Expose-Headers", Constant.CUSTOMER_HEADER_TOKEN);
            return customer;
        }
    }

    /**
     * JWTToken刷新生命周期 （实现： 用户在线操作不掉线功能），这里是30分钟，判断是否在最后12分钟后需要重新刷新
     *
     *
     * @param
     * @param
     * @return
     */
    public boolean jwtTokenRefresh(String token) {
        //String cacheToken = String.valueOf(redisUtil.get(Constant.LOGIN_CUSTOMER_TOKEN + token));
        if (redisUtil.hasKey(UserJwtUtil.getCustomerTokenKey(token))) {
            Users customer = (Users) redisUtil.get(UserJwtUtil.getCustomerTkIdKey(token));
            if (UserJwtUtil.checkRefresh(token)) {
                String refreshToken = refreshTokenId(customer, UserJwtUtil.getTokenId(token));
                HttpServletResponse response = ServletUtil.getResponse();
                Cookie cookie = new Cookie(Constant.CUSTOMER_HEADER_TOKEN, refreshToken);
                cookie.setPath("/");
                response.addCookie(cookie);
                response.setHeader(Constant.CUSTOMER_HEADER_TOKEN, refreshToken);
                response.setHeader("Access-Control-Expose-Headers", Constant.CUSTOMER_HEADER_TOKEN);
            }
            return true;
        }
        return false;
    }

    /******************************************************************************/
    /**
     * 这里保留了另一种刷新的实现方式，前端存储accessToken，后端存储refreshToken
     * accessToken请求刷新,refreshToken判断比对两个是否相同时间或者在一个有效时间内
     * @return
     */
    public String refreshToken(Users customer, String accessToken) {
        String redisTokenId = UserJwtUtil.getTokenId(accessToken);
        String newAccessToken = accessToken;
        // 定义前缀+token 为缓存中的key，得到对应的value（cacheToken）
        String cacheToken = String.valueOf(redisUtil.get(Constant.PRE_REDIS_USER_TOKEN + ':' + accessToken));
        // 判断缓存中的token是否存在
        if (cacheToken != null && !cacheToken.equals("") && !cacheToken.equals("null")) {
            // 校验token有效性
            // 缓存中存在，验证失败（JwtUtil.verify在上一篇文章中已经介绍）
            if (!UserJwtUtil.verify(accessToken) && !UserJwtUtil.isExpire(cacheToken)) {
                // 重新sign，得到新的token
                newAccessToken = UserJwtUtil.sign(customer.getUsername(), UUIDUtil.getBase64UUID(customer.getUsername()));
                // 写入到缓存中，key不变，将value换成新的token
                // 设置超时时间【这里除以1000是因为设置时间单位为秒了】【一般续签的时间都会乘以2】
                // 用户这次请求JWTToken值还在生命周期内，重新put新的生命周期时间（有效时间）
                redisUtil.set(customer.getUsername() + ':' + redisTokenId, cacheToken, Constant.EXPIRE_TIME);
                // 缓存中存在，验证成功
            } else if(UserJwtUtil.isExpire(cacheToken)) {
                //这里要重新生成accessToken和refreshToken
                String newTokenId = UUIDUtil.getBase64UUID(customer.getUsername());
                newAccessToken = UserJwtUtil.sign(customer.getUsername(), newTokenId);
                //refreshToken为accessToken的时间的两倍。redis的存储时间也和refreshToken一样
                //CustomerJwtUtil.sign(customer.getUserName(), UUIDUtil.getBase64UUID(customer.getUserName()));
                redisUtil.set(customer.getUsername() + ':' + newTokenId, cacheToken, Constant.EXPIRE_TIME);
            }
            return newAccessToken;
        }
        return null;
    }
}

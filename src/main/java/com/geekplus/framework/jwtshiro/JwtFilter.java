package com.geekplus.framework.jwtshiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.DateTimeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JwtFilter extends BasicHttpAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 登录标识 Authorization
    private static String LOGIN_SIGN = "Plus-Token";

    //private static final long exceedTime= 15 * 60 * 1000;

    /**
     * 该方法将在过滤器执行完成后执行
     * 当isAccessAllowed默认为true时必须实现该方法
     * 在执行完请求后执行退出登录逻辑，否则下次请求时没有携带将可以直接访问接口，无须重新登录
     * @param request
     * @param response
     * @throws Exception
     */
//    @Override
//    protected void postHandle(ServletRequest request, ServletResponse response){
//        //HttpServletRequest httpRequest = WebUtils.toHttp(request);
//        logger.info("执行postHandle：—————————");
//        //设置一个标记位
//        request.setAttribute("jwtFilter.FILTERED", true);
//    }

    /**
     * 该方法返回值表示是否跳过认证，这里如果返回 true，则不会再走一遍认证流程
     * 如果返回 false，则会执行 isAccessAllowed 方法，再执行isLoginAttempt方法，如果为false继续执行executeLogin
     * 方法，如果没有执行executeLogin或者执行结果也是false，则将执行sendChallenge方法，表示认证失败。
     * 返回 true 时，则必须在postHandle配置退出登录，这个方法将在执行完业务逻辑后执行，否则将导致下次没有携带token时直接使用上次的登录
     * 结果，从而非法访问接口。
     * 默认返回 false 时，isLoginAttempt这些方法将重复调用，所以不建议。如果要返回 false，建议复写 sendChallenge 方法，因为其响应内容为空。
     *
     * 也可以将 isAccessAllowed 作为纯判断是否需要认证，或者不复写该方法，本文不提供实现，如果有问题欢迎留言
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        logger.info("开始jwt 校验");
        //判断是不是第二次进入，是则直接返回
//        Boolean afterFiltered = (boolean)(request.getAttribute("jwtFilter.FILTERED"));
//        if( BooleanUtils.isTrue(afterFiltered)) {
//            return true;
//        }
        //如果不是登录请求
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
                System.out.println("jwt 校验通过");
                return true;
            } catch (Exception e) {
                /*
                 *注意这里捕获的异常其实是在Realm抛出的，但是由于executeLogin（）方法抛出的异常是从login（）来的，
                 * login抛出的异常类型是AuthenticationException，所以要去获取它的子类异常才能获取到我们在Realm抛出的异常类型。
                 * */
                //System.out.println("刷新token");
//                Throwable cause = e.getCause();
//                if (cause!=null&&cause instanceof TokenExpiredException){
//                    //AccessToken过期，尝试去刷新token
//                    if (refreshToken(request, response)){
//                        System.out.println("request.equals(\"success\")");
//                        return true;
//                    }
//                }
//                if (cause!=null&&(cause instanceof TokenExpiredException)){
//                    //尝试去刷新token
//                    throw new TokenExpiredException("token过期失效！");
//                }
                handleLoginException(response,new AuthenticationException("用户认证失败，token过期失效！"));//"token验证失败，无效操作，登录权限不足"
                return false;
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        //我这里是需要登录认证授权才能进入系统所以不能返回true，而是交给super.isAccessAllowed再做判断，所以按照上面判断逻辑是false
        return super.isAccessAllowed(request,response,mappedValue);
    }

    /**
     * 检测用户是否登录
     * 检测header里面是否包含Authorization字段即可
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);

        String authorization = httpRequest.getHeader(LOGIN_SIGN);
        return StringUtils.isNoneBlank(authorization);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String token = httpRequest.getHeader(LOGIN_SIGN);
        JwtToken jwtToken = new JwtToken(token);
        //提交给realm进行登录，如果错误会怕熬出异常并被捕获，如果没有抛出异常则返回true
        getSubject(request, response).login(jwtToken);
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setStatus(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
        System.out.println("token验证失败，没权限访问");
        PrintWriter out = httpResponse.getWriter();
        Map<String,Object> map=new HashMap<>();
        String urlPath = httpRequest.getRequestURI();
        map.put("code", HttpStatusCode.FORBIDDEN);
        map.put("msg","请求访问："+urlPath+"，认证失败，无法访问系统资源,请登录后访问");//登录状态已失效，请重新登录！
        //hSResponse.sendRedirect("/login");	//重定向到登陆界面
        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();
        return false;
    }

    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 在token校验无效之后返回json信息，这里使用jackson配合原生servlet写法，直接抛出的异常会被jwt过滤器捕获并自己处理，使用全局异常处理无效
     * @param response
     * @param e
     */
    private void handleLoginException(ServletResponse response, AuthenticationException e) {
        response.setContentType("application/json;charset=utf-8");
        try(PrintWriter writer = response.getWriter()){
            Result resultVo=Result.error(HttpStatusCode.FORBIDDEN,e.getMessage());
            ObjectMapper objectMapper=new ObjectMapper();
            writer.print(objectMapper.writeValueAsString(resultVo));
        }catch (IOException ioe){
            throw new BusinessException(ApiExceptionEnum.LOGIN_ACL);
        }
    }

    /**
     * 当 isAccessAllowed 可能返回false时需要复写该接口，否则默认将返回空白界面
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        super.sendChallenge(request, response);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().write(
                    JSONObject.toJSONString(Result.error(ApiExceptionEnum.LOGIN_STATE_EXPIRE)));
        } catch (IOException e) {
            throw new BusinessException("登录状态已失效！");
        }
        return false;
    }

    //刷新token
//    private Boolean refreshToken(ServletRequest request,ServletResponse response) {
//        System.out.println("refreshToken");
//
//        HttpServletRequest req= (HttpServletRequest) request;
//        //获取传递过来的accessToken
//        String accessToken=req.getHeader(LOGIN_SIGN);
//        //获取token里面的用户名
//        String username= JwtTokenUtil.verifyResult(accessToken).getClaim("userName").asString();
//        System.out.println("username"+username);
//        //判断refreshToken是否过期了，过期了那么所含的username的键不存在
//        System.out.println("redisUtil.hasKey(username)"+redisUtil.hasKey(ConstValue.LOGIN_USER_TOKEN+accessToken));
//        if (redisUtil.hasKey(ConstValue.LOGIN_USER_TOKEN+accessToken)){
//            //判断refresh的时间节点和传递过来的accessToken的时间节点是否一致，不一致校验失败
//            //long current= (long) redisUtil.get(username);
//            // 获取当前时间节点
//            long currentTimeMillis=System.currentTimeMillis();
//            long tokenMillis=JwtTokenUtil.verifyResult(accessToken).getExpiresAt().getTime();
//            //if (current==JWTUtil.getExpire(accessToken)){
//            if(tokenMillis-currentTimeMillis<exceedTime){
//                //long currentTimeMillis = System.currentTimeMillis();
//                //生成刷新的token
//                String token=JwtTokenUtil.token(username);
//                //刷新redis里面的refreshToken,过期时间是30min
//                redisUtil.set(username,currentTimeMillis,30*60);
//                //再次交给shiro进行认证
//                JwtToken jwtToken=new JwtToken(token);
//                try {
//                    getSubject(request, response).login(jwtToken);
//                    // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
//                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//                    httpServletResponse.setHeader(LOGIN_SIGN, token);
//                    httpServletResponse.setHeader("Access-Control-Expose-Headers", LOGIN_SIGN);
//                    return true;
//                }catch (Exception e){
//                    return false;
//                }
//            }
//        }
//        return false;
//    }

//    /**
//     * 检查是否需要,刷新Token
//     * @param account
//     * @param authorization
//     * @param response
//     * @return
//     */
//    private boolean refreshTokenIfNeed(String account,String authorization, ServletResponse response) {
//        Long currentTimeMillis= System.currentTimeMillis();
//        //检查刷新规则
//        if(this.refreshCheck(authorization,currentTimeMillis)){
//            String lockName = SecurityConsts.PREFIX_SHIRO_REFRESH_CHECK + account;
//            boolean b = syncCacheService.getLock(lockName, Constants.ExpireTime.ONE_HOUR);
//            if (b) {
//                LOGGER.info(String.format("为账户%s颁发新的令牌",account));
//                String newToken = JwtUtil.sign(account, String.valueOf(currentTimeMillis));
//                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//                httpServletResponse.setHeader(SecurityConsts.REQUEST_AUTH_HEADER, newToken);
//                httpServletResponse.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);
//            }
//            syncCacheService.releaseLock(lockName);
//        }
//        return true;
//    }
//
//    /**
//     * 检查是否需要更新Token
//     * @param authorization
//     * @param currentTimeMillis
//     * @return
//     */
//    private boolean refreshCheck(String authorization,Long currentTimeMillis){
//        String tokenMillis=JwtTokenUtil.verifyResult(authorization).getClaim(authorization, SecurityConsts.CURRENT_TIME_MILLIS);
//        if(Long.parseLong(tokenMillis)-(jwtProperties.refreshCheckTIme*60*1000) < currentTimeMillis) {
//            return true;
//        }
//        return false;
//    }

}

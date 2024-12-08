package com.geekplus.framework.jwtshiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekplus.common.constant.Constant;
import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.domain.Result;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 9/19/24 8:21 PM
 * description: 适用前端的客户JwtFilter，过滤器
 */
public class UserJwtFilter extends BasicHttpAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 登录标识 Authorization
    //private static String LOGIN_SIGN = "Customer-PT";

    /**
     * 该方法将在过滤器执行完成后执行
     * 当isAccessAllowed默认为true时必须实现该方法
     * 在执行完请求后执行退出登录逻辑，否则下次请求时没有携带将可以直接访问接口，无须重新登录
     * @param request
     * @param response
     * @throws Exception
     */
    //@Override
    //protected void postHandle(ServletRequest request, ServletResponse response){
    //}

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

        String authorization = httpRequest.getHeader(Constant.CUSTOMER_HEADER_TOKEN);
        return StringUtils.isNoneBlank(authorization);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String token = httpRequest.getHeader(Constant.CUSTOMER_HEADER_TOKEN);
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
        System.out.println("用户认证失败，没权限访问");
        PrintWriter out = httpResponse.getWriter();
        Map<String,Object> map=new HashMap<>();
        //String urlPath = httpRequest.getRequestURI();
        map.put("code", HttpStatusCode.FORBIDDEN);
        map.put("msg","认证失败，无法访问应用，请登录后访问");//登录状态已失效，请重新登录！
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
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, httpServletRequest.getHeader(HttpHeaders.ORIGIN));
        // 允许客户端请求方法
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,OPTIONS,PUT,DELETE");
        // 允许客户端提交的Header
        String requestHeaders = httpServletRequest.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (StringUtils.isNotEmpty(requestHeaders)) {
            httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);
        }
        // 允许客户端携带凭证信息(是否允许发送Cookie)
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
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
}

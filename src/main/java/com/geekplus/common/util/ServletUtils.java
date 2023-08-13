/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 6/19/23 02:27
 * description: 做什么的？
 */
package com.geekplus.common.util;


import com.geekplus.webapp.system.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class ServletUtils {
    private static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }
    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }
    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }
    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }
    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static SysUser getUser() {
        SysUser user = (SysUser) getRequest().getAttribute("user");
        if (user == null) {
            user = (SysUser) getSession().getAttribute("user");
            if (user == null) {
                log.error("ServletUtils 获取用户失败");
                throw new NullPointerException("ServletUtils 获取用户失败");
            }
        }
        return user;
    }
    /**
     * 获取业务员信息
     *
     * @return 业务员信息
     */
//    public static Salesman getSalesman() {
//        Salesman salesman = (Salesman) getRequest().getAttribute("salesman");
//        if (salesman == null) {
//            salesman = (Salesman) getSession().getAttribute("salesman");
//            if (salesman == null) {
//                log.error("请求中获取业务员失败");
//                throw new NullPointerException("请求中获取业务员失败");
//            }
//        }
//        return salesman;
//    }
    /**
     * 获取token
     *
     * @return token信息
     */
    public static String getToken() {
        String token = (String) getRequest().getAttribute("token");
        if (token == null) {
            log.error("request 中获取token失败");
            throw new NullPointerException("request 中获取token失败");
        }
        return token;
    }
    /**
     * 是否是Ajax异步请求
     *
     * @return 结果
     */
//    public static boolean isAjaxRequest() {
//        HttpServletRequest request = getRequest();
//        String accept = request.getHeader("accept");
//        if (accept != null && accept.indexOf("application/json") != -1) {
//            return true;
//        }
//        String xRequestedWith = request.getHeader("X-Requested-With");
//        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
//            return true;
//        }
//        String uri = request.getRequestURI();
//        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml")) {
//            return true;
//        }
//        String ajax = request.getParameter("__ajax");
//        if (StringUtils.inStringIgnoreCase(ajax, "json", "xml")) {
//            return true;
//        }
//        return false;
//    }

    public static boolean isAppletRequest() {
        String servletPath = getRequest().getServletPath();
        return servletPath.contains("/applet/");
    }
    /**
     * 将字符串渲染到客户端
     *
     * @param string 待渲染的字符串
     */
    public static void renderString(String string) {
        try {
            HttpServletResponse response = getResponse();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 判断是否有token
     *
     * @return 结果
     */
    public boolean hasToken() {
        String token = (String) getRequest().getAttribute("token");
        return token != null && !token.trim().equals("");
    }
}

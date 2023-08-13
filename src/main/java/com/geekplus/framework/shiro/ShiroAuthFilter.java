/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/24 10:36 下午
 * description: 做什么的？
 */
package com.geekplus.framework.shiro;

import com.alibaba.fastjson.JSON;
import com.geekplus.common.constant.HttpStatusCode;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

//@Configuration
//使用FormAuthenticationFilter时，去掉@Configuration，BasicHttpAuthenticationFilter可以正常使用，也可以去掉
public class ShiroAuthFilter extends BasicHttpAuthenticationFilter {

    private static final String[] filter = { "/**/*.css", "/profile/**" ,"/common/resource**/**","/*.html", "/**/*.js"};

    public ShiroAuthFilter() {
        super();
    }

    /**
     * 生成自定义token
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
//    @Override
//    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
//        //获取请求token
//        String token = TokenUtil.getRequestToken((HttpServletRequest) request);
//
//        return new AuthToken(token);
//    }

    /**
     * 对跨域提供支持
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
            httpServletResponse.setStatus(HttpStatusCode.SUCCESS);
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed=super.isAccessAllowed(request, response, mappedValue);
//        String token = ((HttpServletRequest) request).getHeader("Authorization");
//        if (StringUtils.isEmpty(token)) {
//            throw new ApiException("token不能为空");
//        }
//            executeLogin(request, response);
//        return true;
        //if (!allowed) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return allowed;
    }

    /**
     *  排除登录
     */
//    @Override
//    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        String token = httpServletRequest.getHeader("Authorization");
//        if (StringUtils.isEmpty(token)) {
//            throw new ApiException("token不能为空");
//        }
//        CustomToken jwtToken = new CustomToken();
//        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
//        getSubject(request, response).login(jwtToken);
//        // 如果没有抛出异常则代表登入成功，返回true
//        return true;
//    }

    /**
      * @Author geekplus
      * @Description //访问拒绝
      * @Param
      * @Throws
      * @Return {@link }
      */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("访问请求路径资源失败，没有通过认证，请登录");
        HttpServletResponse hSResponse = (HttpServletResponse) response;
        HttpServletRequest hSRequest=(HttpServletRequest)request;
        String urlPath=hSRequest.getRequestURI();
        hSResponse.setStatus(HttpStatusCode.SUCCESS);
        hSResponse.setContentType("application/json;charset=utf-8");

        PrintWriter out = hSResponse.getWriter();
        Map<String,Object> map=new HashMap<>();
        map.put("code", HttpStatusCode.FORBIDDEN);
        map.put("msg","请求访问："+urlPath+"，认证失败，无法访问系统资源,请登录后访问");//登录状态已失效，请重新登录！
        //hSResponse.sendRedirect("/login");	//重定向到登陆界面
        out.write(JSON.toJSONString(map));
        out.flush();
        out.close();
        return false;
//        String url = hSRequest.getRequestURI();
//
//        // 不需要过滤的请求地址以及文件
//        for (String str : filter) {
//            if (url.contains(str) || url.equalsIgnoreCase("/")){
//                return true;
//            }
//        }
        //这里是获取用户登录信息
//        Subject subject = getSubject(request, response);
//        // 如果没有获取到用户信息，将退出到登陆界面
//        if (null == subject.getPrincipal()) {
//            boolean isAjaxRequest = false;
//            if(!StringUtils.isBlank(hSRequest.getHeader("x-requested-with")) && hSRequest.getHeader("x-requested-with").equals("XMLHttpRequest")){
//                isAjaxRequest = true;
//            }
//            // 如果是Ajax返回指定数据
//            if (isAjaxRequest) {
//                //可以通过code403判断是否重定向，也可以自定义一个属性指定是session超时的重定向
//                hSResponse.setHeader("sessionstatus", "TIMEOUT");	//返回特定数据（头部信息）
//                hSResponse.setHeader("content_path", "/login");	//返回特定数据（首页登陆地址）
//                hSResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);	//403禁止
//                return false;
//            } else {	// 不是Ajax进行重定向处理
//                hSResponse.sendRedirect("/login");	//重定向到登陆界面
//                return false;
//            }
//        }
//        return true;
    }

    /**
     * token失效时候调用
     */
//    @Override
//    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        httpResponse.setContentType("application/json;charset=utf-8");
//        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtil.getOrigin());
//        httpResponse.setCharacterEncoding("UTF-8");
//        try {
//            //处理登录失败的异常
//            Throwable throwable = e.getCause() == null ? e : e.getCause();
//            Map<String, Object> result = new HashMap<>();
//            result.put("status", 400);
//            result.put("msg", "登录凭证已失效，请重新登录");
//            String json = MAPPER.writeValueAsString(result);
//            httpResponse.getWriter().print(json);
//        } catch (IOException e1) {
//        }
//        return false;
//    }
}

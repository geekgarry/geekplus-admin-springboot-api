package com.geekplus.framework.aspect;

import com.geekplus.common.annotation.RequestLimit;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.ip.IpUtils;
import com.geekplus.webapp.system.entity.SysRole;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/20/24 02:06
 * description: 做什么的？
 */
@Aspect
@Component
@Order(6)
public class RequestLimitAspect {
    private static final String REQ_LIMIT = "req_limit_";

    @Resource
    private RedisUtil redisUtil;

    /**
     * execution 指定controller
     * !execution 排查指定controller下的方法
     */
//    @Pointcut("(execution(* com.geekplus.webapp.*.controller.*.*(..)) && !execution(* com.geekplus.webapp.*.controller.CdpUploadController.upload(..)))")
//    public void requestLimit() {
//    }

    /**
     * 定义拦截规则：拦截com.springboot.bcode.api包下面的所有类中，有@RequestLimit Annotation注解的方法
     * 。
     */
    //@Around("execution(* com.*.xxxcontroller..*.*(..)) && @annotation(com.*.xxx.xxx.common.security.RequestLimit)")
    @Around("@annotation(com.geekplus.common.annotation.RequestLimit)")
    public Object method(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); // 获取被拦截的方法
        RequestLimit limt = method.getAnnotation(RequestLimit.class);
        // No request for limt,continue processing request
        if (limt == null) {
            return pjp.proceed();
        }
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        int time = limt.time();
        int count = 3;
        int waits = limt.waits();

        String ip = IpUtils.getIpAddr(request);
        String url = request.getRequestURI();
        String limitValue = "";
        LoginUser principal1 = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<SysRole> sysRoles=principal1.getSysRoleList();
        List<String> roleKey= sysRoles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
        List roleSet=Arrays.asList("admin", "webmanager", "commonuser");
        //判断用户是否为限制访问用户，在规定时间内最大请求数
        if(roleKey.containsAll(roleSet)) {
            if (principal1 == null) {
                limitValue = principal1.getUserName().toString();
            } else {
                limitValue = ip;
            }
        }

        // judge codition
        String key = requestLimitKey(url, limitValue);
        int nowCount = redisUtil.get(key) == null ? 0 : Integer.valueOf(redisUtil.get(key).toString());

        if (nowCount == 0) {
            nowCount++;
            redisUtil.set(key, String.valueOf(nowCount), time);
            return pjp.proceed();
        } else {
            nowCount++;
            redisUtil.set(key, String.valueOf(nowCount), time);
            if (nowCount >= count) {
                System.out.println("用户IP[" + ip + "]访问地址[" + url + "]访问次数["
                        + nowCount + "]超过了限定的次数[" + count + "]限定时间[" + waits
                        + "秒]");
                redisUtil.expire(key, waits);
                return returnLimit(request);
            }
        }

        return pjp.proceed();
    }

    /**
     * requestLimitKey: url_ip
     *
     * @param url
     * @param ip
     * @return
     */
    private static String requestLimitKey(String url, String ip) {

        StringBuilder sb = new StringBuilder();
        sb.append(REQ_LIMIT);
        sb.append(url);
        sb.append("_");
        sb.append(ip);
        return sb.toString();
    }

    /**
     * 返回拒绝信息
     *
     * @param request
     * @return
     * @throws IOException
     */
    private String returnLimit(HttpServletRequest request) throws IOException {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getResponse();
        PrintWriter out = response.getWriter();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        out.println("{\"code\": 50004,\"msg\":\"Service reject request!\"}");
        out.flush();
        out.close();
        return null;

    }
}

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 7/2/23 00:52
 * description: 做什么的？
 */
package com.geekplus.framework.aspect;

import com.alibaba.fastjson.JSON;
import com.geekplus.common.annotation.Log;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.util.AddressUtil;
import com.geekplus.common.util.ip.IpUtils;
import com.geekplus.webapp.system.entity.SysOperLog;
import com.geekplus.webapp.system.service.UserTokenService;
import com.geekplus.webapp.system.service.SysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
  * @Author geekplus
  * @Description // 统一处理请求，打印访问信息
  * @Param
  * @Throws
  * @Return {@link }
  */
@Aspect
@Component
@Slf4j
public class OperateLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(OperateLogAspect.class);
    //保证同步安全性（可以不加）
    private ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    @Resource
    private SysOperLogService sysOperLogService;
    @Resource
    private UserTokenService userTokenService;

    //配置织入点
    //@Pointcut("execution(* com.geekplus.webapp..*.controller..*.*(..))")
    @Pointcut("@annotation(com.geekplus.common.annotation.Log)")
    public void logPointcut() {

    }

    @Before("logPointcut()")
    public void before(JoinPoint joinPoint) {// 参数：连接点
        threadLocal.set(new Date().getTime());
        // 用户[1.2.3.4],在[xxx],访问了[com.nowcoder.community.service.xxx()].
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response=attributes.getResponse();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." +joinPoint.getSignature().getName();// 得到该连接点的类名和方法名
        log.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));
    }

    //在方法返回值之后切入内容（获得返回值）
    @AfterReturning(returning = "result", pointcut = "logPointcut()")
    public void doAfterReturning(JoinPoint joinPoint,Object result) throws Throwable {
        log.debug("方法返回值：" + result);
        handleLog(joinPoint,null,result);
        log.debug("耗时：" + (new Date().getTime() - threadLocal.get()) + "毫秒");
    }

    /**
      * @Author geekplus
      * @Description //拦截异常操作
      * @Param
      * @Throws
      * @Return {@link }
      */
    @AfterThrowing(pointcut = "logPointcut()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint,final Exception e,Object result) {
        // 获得注解
        Log controllerLog = null;
        try {
            controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null)
            {
                return;
            }
            SysOperLog sysOperLog=new SysOperLog();
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response=attributes.getResponse();
            String target = joinPoint.getSignature().getDeclaringTypeName() + "." +joinPoint.getSignature().getName();// 得到该连接点的类名和方法名
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
            String ip = IpUtils.getIpAddr(request);
            String address = AddressUtil.getRealAddressByIP(ip);

            LoginUser sysUser= userTokenService.getUserInfo(request);
            if(sysUser==null){
                sysOperLog.setOperName("游客");
            }else{
                sysOperLog.setOperName(sysUser.getUserName());
            }
            // 获取客户端操作系统
            String os = userAgent.getOperatingSystem().getName();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
            sysOperLog.setTitle(controllerLog.title());
            sysOperLog.setOperIp(ip);//request.getRemoteHost()
            sysOperLog.setOperLocation(address);
            sysOperLog.setMethod(target);
            sysOperLog.setOperUrl(request.getRequestURI());
            sysOperLog.setOperParam(argsArrayToString(joinPoint.getArgs()));
            sysOperLog.setGroupName("极客普拉斯");
            sysOperLog.setJsonResult(JSON.toJSONString(result));
            sysOperLog.setStatus(response.getStatus());
            sysOperLog.setBusinessType(controllerLog.businessType().ordinal());
            sysOperLog.setOperatorType(controllerLog.operatorType().ordinal());
            sysOperLog.setRequestMethod(request.getMethod());
            sysOperLog.setOperBrowser(browser);
            sysOperLog.setOperOs(os);
            sysOperLogService.insertSysOperLog(sysOperLog);
        } catch (Exception ex) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (int i = 0; i < paramsArray.length; i++)
            {
                if (!isFilterObject(paramsArray[i]))
                {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o)
    {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}

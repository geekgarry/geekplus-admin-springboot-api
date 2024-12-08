package com.geekplus.framework.aspect;

import com.geekplus.common.annotation.RepeatLogin;
import com.geekplus.common.domain.LoginBody;
import com.geekplus.common.domain.LoginUser;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/20/24 03:10
 * description: 做什么的？
 */
@Aspect
@Component
@Slf4j
public class RepeatLoginAspect {

    //@Autowired
    //private RedisTemplate redisTemplate;
    @Resource
    RedisUtil redisUtil;

    /**
     * 定义切点
     */
    //@Pointcut("execution(* com.geekplus.webapp..*.controller..*.*(..))")
    @Pointcut("@annotation(com.geekplus.common.annotation.RepeatLogin)")
    public void repeatLogin() {}

    @Around("repeatLogin()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取防重复提交注解
        RepeatLogin annotation = method.getAnnotation(RepeatLogin.class);

        //返回目标方法的签名
        Signature signature = joinPoint.getSignature();
        log.info("请求方法 : " + signature.getDeclaringTypeName() + "." + signature.getName());
        //log.info("代理的是哪一个方法："+signature.getName());
        Object[] obj = joinPoint.getArgs();
        log.info("获取目标方法的参数信息："+ Arrays.asList(obj));
        LoginBody loginBody=(LoginBody)Arrays.asList(obj).get(0);
        //log.info("获取目标方法的参数信息："+ loginUser.getUserName());
        // 获取token当做key
        String userName = loginBody.getUserName();//request.getHeader("token");
        if (StringUtils.isBlank(userName)) {
            throw new BusinessException(ApiExceptionEnum.LOGIN_AUTH);
        }

        String url = request.getRequestURI();
        /**
         *  通过前缀 + url + token 来生成redis上的 key
         *  可以在加上用户id，小编这里没办法获取，大家可以在项目中加上
         */
        String redisKey = "login_key:"
                .concat(url).concat(":")
                .concat(userName);
        log.info("==========redisKey ====== {}",redisKey);

        if (!redisUtil.hasKey(redisKey)) {
            redisUtil.set(redisKey, userName, annotation.seconds());
            try {
                //正常执行方法并返回
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                redisUtil.del(redisKey);
                throw new Throwable(throwable);
            }
        } else {
            // 抛出异常
            throw new BusinessException(ApiExceptionEnum.REPEAT_LOGIN);
        }
    }

    /**
     * 后置异常通知
     *  定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     *  throwing 只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "repeatLogin()",throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, NullPointerException exception){
        log.trace("- - - - - 后置异常通知 - - - - -");
    }
}

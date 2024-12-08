package com.geekplus.framework.aspect;

import com.geekplus.common.annotation.RepeatSubmit;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.string.StringUtils;
import com.geekplus.framework.jwtshiro.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/20/24 03:10
 * description: 做什么的？
 */
@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

    // 登录标识 Authorization
    private static String LOGIN_TOKEN = "Plus-Token";

    //@Autowired
    //private RedisTemplate redisTemplate;
    @Resource
    RedisUtil redisUtil;
    @Resource
    JwtUtil jwtUtil;
    /**
     * 定义切点
     */
    //@Pointcut("execution(* com.geekplus.webapp..*.controller..*.*(..))")
    @Pointcut("@annotation(com.geekplus.common.annotation.RepeatSubmit)")
    public void repeatSubmit() {}

    @Around("repeatSubmit()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        // 获取防重复提交注解
        RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);

        // 获取token当做key
        String token = request.getHeader(LOGIN_TOKEN);
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ApiExceptionEnum.LOGIN_ACL);
        }

        String url = request.getRequestURI();
        /**
         *  通过前缀 + url + token 来生成redis上的 key
         *  可以在加上用户id，小编这里没办法获取，大家可以在项目中加上
         */
        String redisKey = annotation.value()
                .concat(url).concat(":")
                .concat(jwtUtil.getTokenIdFromToken(token));
        log.info("==========redisKey ====== {}",redisKey);

        if (!redisUtil.hasKey(redisKey)) {
            redisUtil.set(redisKey, token, annotation.seconds());
            try {
                //正常执行方法并返回
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                redisUtil.del(redisKey);
                throw new Throwable(throwable);
            }
        } else {
            // 抛出异常
            throw new BusinessException(ApiExceptionEnum.REPEAT_SUBMIT);
        }
    }
}

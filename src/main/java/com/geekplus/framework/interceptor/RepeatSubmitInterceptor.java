package com.geekplus.framework.interceptor;

import com.geekplus.common.annotation.RepeatSubmit;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.redis.RedisUtil;
import com.geekplus.common.util.http.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 防止重复提交拦截器
 *
 * @author
 */
@Slf4j
@Component
public class RepeatSubmitInterceptor implements HandlerInterceptor
{

    /**
     * Redis的API
     */
    @Resource
    private RedisUtil redisUtil;

    /**
     * preHandler方法，在controller方法之前执行
     * <p>
     * 判断条件仅仅是用了uri，实际开发中根据实际情况组合一个唯一识别的条件。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            // 只拦截标注了@RepeatSubmit该注解
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 标注在方法上的@RepeatSubmit
            RepeatSubmit repeatSubmitByMethod = method.getAnnotation(RepeatSubmit.class);
            // 标注在controler类上的@RepeatSubmit
            RepeatSubmit repeatSubmitByCls = method.getDeclaringClass().getAnnotation(RepeatSubmit.class);
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            // 没有限制重复提交，直接跳过
            if (Objects.isNull(repeatSubmitByMethod) && Objects.isNull(repeatSubmitByCls)) {
                log.info("isNull");
                return true;
            }

            //组合判断条件，这里仅仅是演示，实际项目中根据架构组合条件
            //请求的URI
            String userName = ServletUtil.getParameter("userName");
            String uriKey = request.getRequestURI();
            String redisValue=Objects.nonNull(repeatSubmitByMethod) ? repeatSubmitByMethod.value() : repeatSubmitByCls.value();
            //存在即返回false，不存在即返回true
            Boolean ifAbsent = redisUtil.setIfAbsent(uriKey, redisValue,
                    Objects.nonNull(repeatSubmitByMethod) ? repeatSubmitByMethod.seconds() : repeatSubmitByCls.seconds());
            log.info("是否重复请求？");
            //如果存在，表示已经请求过了，直接抛出异常，由全局异常进行处理返回指定信息
            if (!ifAbsent) {//ifAbsent != null &&
                String msg = String.format("url:[%s]重复请求", uriKey);
                log.warn(msg);
                // throw new RepeatSubmitException(msg);
                throw new BusinessException(ApiExceptionEnum.LOGIN_DISABLED_ERROR);
            }
        }
        return true;
    }

}

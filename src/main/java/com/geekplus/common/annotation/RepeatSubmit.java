package com.geekplus.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解防止表单重复提交
 *
 * @author
 *
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit
{
    /**
     * 默认失效时间5秒
     *
     * @return
     */
    long seconds() default 5;
}

package com.geekplus.common.annotation;

import java.lang.annotation.*;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/20/24 08:02
 * description: 做什么的？
 */
@Inherited
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatLogin {

    /**
     * 默认失效时间3秒
     *
     * @return
     */
    long seconds() default 3;
}

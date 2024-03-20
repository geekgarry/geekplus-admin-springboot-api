package com.geekplus.common.annotation;

import java.lang.annotation.*;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 3/20/24 02:03
 * description: 做什么的？
 */
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimit {
    int time() default 60; //60s内

    int count() default 50000; //访问超过50次后拒绝访问

    int waits() default 300; //超过后等待时间
}

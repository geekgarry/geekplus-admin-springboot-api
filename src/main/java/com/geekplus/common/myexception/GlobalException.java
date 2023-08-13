/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/24 10:51 下午
 * description: 做什么的？
 */
package com.geekplus.common.myexception;

public class GlobalException extends RuntimeException {
    //捕获CommonException异常
    public GlobalException(String message) {
        super(message);
    }

}

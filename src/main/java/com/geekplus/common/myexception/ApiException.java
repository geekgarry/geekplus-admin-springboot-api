package com.geekplus.common.myexception;

import com.geekplus.common.enums.ApiExceptionEnum;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.security.NoSuchAlgorithmException;
//import com.netflix.discovery.util.StringUtil;

/**
 * 自定义通用异常
 */
@Data
public class ApiException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    protected Integer code;
    protected String msg;
    protected Object data;

    public ApiException(){
        ApiExceptionEnum exceptionEnum = ApiExceptionEnum.getByEClass(this.getClass());
        if (exceptionEnum != null) {
            code = exceptionEnum.getCode();
            msg = exceptionEnum.getMsg();
        }
    }

    public ApiException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiException(String format, Object... objects) {
        this();
        this.msg = StringUtils.join(objects);
    }

    public ApiException(ApiExceptionEnum apiCode, Object data) {
        this(apiCode);
        this.data = data;
    }

    public ApiException(ApiExceptionEnum exceptionEnum) {
        this.code=exceptionEnum.getCode();
        this.msg=exceptionEnum.getMsg();
    }

    public ApiException(String message) {
        //super();
        this();
        this.msg=message;
    }

    public ApiException(Exception e) {
        new ApiException(e.getMessage());
    }
}

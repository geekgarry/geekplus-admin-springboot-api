package com.geekplus.common.myexception;

import com.geekplus.common.enums.ApiExceptionEnum;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
//import com.netflix.discovery.util.StringUtil;

/**
 * 自定义通用异常
 */
@Data
public class BusinessException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    protected Integer code;
    protected String msg;
    protected Object data;

    public BusinessException(){
        ApiExceptionEnum exceptionEnum = ApiExceptionEnum.getByEClass(this.getClass());
        if (exceptionEnum != null) {
            code = exceptionEnum.getCode();
            msg = exceptionEnum.getMsg();
        }
    }

    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String format, Object... objects) {
        this();
        this.msg = StringUtils.join(objects);
    }

    public BusinessException(ApiExceptionEnum apiCode, Object data) {
        this(apiCode);
        this.data = data;
    }

    public BusinessException(ApiExceptionEnum exceptionEnum) {
        this.code=exceptionEnum.getCode();
        this.msg=exceptionEnum.getMsg();
    }

    public BusinessException(String message) {
        //super();
        this();
        this.msg=message;
    }

    public BusinessException(Exception e) {
        new BusinessException(e.getMessage());
    }
}

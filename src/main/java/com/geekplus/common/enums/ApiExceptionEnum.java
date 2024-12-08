package com.geekplus.common.enums;


import com.geekplus.common.myexception.BusinessException;

/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/24 11:21 下午
 * description: 做什么的？
 */

public enum ApiExceptionEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    PARAM_ERROR( 202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    DATA_UPDATE_ERROR(205, "数据版本异常"),
    NO_USER_ID(206,"没有登录，获取菜单权限失败"),
    REPEAT_SUBMIT(207,"重复提交请求"),
    REPEAT_LOGIN(2002,"重复登录请求"),

    LOGIN_AUTH(208, "未登陆"),
    LOGIN_FAIL(2088, "登录失败"),
    PERMISSION(209, "没有权限"),
    CODE_ERROR(210, "验证码错误"),
    CODE_IS_NULL(2101, "验证码为空"),
    CODE_IS_EXPIRE(2102, "验证码已过期"),
    LOGIN_USERNAME_ERROR(2111, "账号不存在"),
    LOGIN_PASSWORD_ERROR(2112, "密码不正确"),
    LOGIN_USER_ERROR(2111, "账号或密码错误"),
    LOGIN_DISABLED_ERROR(212, "该用户已被禁用"),
    REGISTER_MOBLE_ERROR(213, "手机号已被使用"),
    LOGIN_MUST(214, "用户需要登录"),
    LOGIN_ACL(215, "用户认证失败"),

    URL_ENCODE_ERROR( 216, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR( 217, "非法回调请求"),
    FETCH_ACCESSTOKEN_FAILD( 218, "获取accessToken失败"),
    FETCH_USERINFO_ERROR( 219, "获取用户信息失败"),
    CAPTCHA_ACCESS_FAIL(220, "获取验证码失败"),
    LOGIN_STATE_EXPIRE( 403, "登录状态失效"),

    PAY_RUN(220, "支付中"),
    CANCEL_ORDER_FAIL(225, "取消订单失败"),
    CANCEL_ORDER_NO(225, "不能取消预约"),

    HOSCODE_EXIST(230, "医院编号已经存在"),
    NUMBER_NO(240, "可预约号不足"),
    TIME_NO(250, "当前时间不可以预约"),

    SIGN_ERROR(300, "签名错误"),
    HOSPITAL_OPEN(310, "医院未开通，暂时不能访问"),
    HOSPITAL_LOCK(320, "医院被锁定，暂时不能访问"),
    ;

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private Class<? extends BusinessException> eClass;

    ApiExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static ApiExceptionEnum getByEClass(Class<? extends BusinessException> eClass) {
        if (eClass == null) {
            return null;
        }

        for (ApiExceptionEnum exceptionEnum : ApiExceptionEnum.values()) {
            if (eClass.equals(exceptionEnum.eClass)) {
                return exceptionEnum;
            }
        }

        return null;
    }

}

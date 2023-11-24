/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/25 9:03 上午
 * description: 做什么的？
 */
package com.geekplus.common.domain;

import com.geekplus.common.constant.HttpStatusCode;
import com.geekplus.common.enums.ApiExceptionEnum;
import com.geekplus.common.myexception.BusinessException;
import com.geekplus.common.util.string.StringUtils;
import lombok.Data;

import java.util.HashMap;

@Data
public class Result extends HashMap<String,Object> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 错误码，表示一种错误类型 如果是成功，则code为200
     */
    private int code;
    /**
     * 对错误的具体解释
     */
    private String msg;
    /**
     * 返回的结果包装在value中，value可以是单个对象
     */
    private Object data;
    /**
     * 数据的条数
     */
//    private String count;

    public Result() {

    }
//**************%%%%%%%%%%%%%################分割线@@@@@@@@@@@@@@@@@@@@^^^^^^^^&&&&&
//这里是继承HashMap实现的统一返回响应类，如果要使用把implements Serializable改为extends HashMap<String,Object>
    public Result(String msg) {
        super.put("msg",msg);
    }
    public Result(int code, String msg)
    {
        super.put("code", code);
        super.put("msg", msg);
    }
    public Result(int code, String msg, Object data)
    {
        super.put("code", code);
        super.put("msg", msg);
        if (StringUtils.isNotNull(data))
        {
            super.put("data", data);
        }
    }

//    public static Result success(String msg){
//        return new Result(200,msg);
//    }

    public static Result success(){
        return Result.success("操作成功");
    }
    public static Result success(Object object){
        return new Result(ApiExceptionEnum.SUCCESS.code(),ApiExceptionEnum.SUCCESS.msg(),object);
    }

    public static Result success(String msg,Object object){
        return new Result(HttpStatusCode.SUCCESS,msg,object);
    }

    public static Result success(String msg){
        return new Result(HttpStatusCode.SUCCESS,msg);
    }

    public static Result error(){
        return Result.error("操作失败");
    }

    public static Result error(String msg){
        return new Result(HttpStatusCode.ERROR,msg);
    }

    public static Result error(String msg, Object object){
        return new Result(HttpStatusCode.ERROR,msg,object);
    }

    public static Result error(int code,String msg){
        return new Result(code,msg);
    }

    public static Result error(ApiExceptionEnum exceptionEnum){
        return Result.error(exceptionEnum.code(),exceptionEnum.getMsg());
    }

    public static Result error(BusinessException e){
        return Result.error(e.getCode(),e.getMsg());//e.getData()
    }
//**************%%%%%%%%%%%%%################分割线@@@@@@@@@@@@@@@@@@@@^^^^^^^^&&&&&&
// 下面是统一响应结构使用implements Serializable
    /** 成功且带数据 **/
//    public static Result success(Object object) {
//        Result result = new Result();
//        result.setCode(ApiExceptionEnum.SUCCESS.code());
//        result.setMsg(ApiExceptionEnum.SUCCESS.msg());
//        result.setData(object);
//        return result;
//    }
//
//    /** 成功 **/
//    public static Result success(Integer code,String msg,Object data) {
//        Result result = new Result();
//        result.setCode(code);
//        result.setMsg(msg);
//        result.setData(data);
//        return result;
//    }
//
//    /** 成功且带数据和数量适用于layui表格数据 **/
//    public static Result success(Object object, String count) {
//        Result result = new Result();
//        result.setCode(0);
//        result.setMsg(ApiExceptionEnum.SUCCESS.msg());
//        result.setData(object);
////        result.setCount(count);
//        return result;
//    }
//
//    /** 成功但不带数据 **/
//    public static Result success() {
//        return success(true);
//    }
//
//    /** 失败 **/
//    public static Result error(Integer code, String msg) {
//        Result result = new Result();
//        result.setCode(code);
//        result.setMsg(msg);
//        return result;
//    }
//
//    /** 失败只带数据 **/
//    public static Result error(String msg) {
//        Result result = new Result();
//        result.setMsg(msg);
//        return result;
//    }
//
//    public static Result error(ApiException e) {
//        //return error(e.getCode(), e.getMsg());
//        ApiExceptionEnum ee = ApiExceptionEnum.getByEClass(e.getClass());
//
//        Result result = Result.error(e.getCode(),e.getMsg());
//        return result;
//    }
//
//    public static Result error(ApiExceptionEnum apiExceptionEnum) {
//        return error(apiExceptionEnum.code(),apiExceptionEnum.msg());
//    }
//
//    public static Result error() {
//        return error("请求服务错误");
//    }
}

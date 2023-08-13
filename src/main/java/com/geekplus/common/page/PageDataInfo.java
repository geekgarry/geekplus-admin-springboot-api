/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/25 10:01 上午
 * description: 做什么的？
 */
package com.geekplus.common.page;

import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

public class PageDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 总记录数 */
    private long total;

    /** 列表数据 */
    private T rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String msg;

    /** 页面号 */
//    private Integer pageNum;
//    /** 页面数 */
//    private Integer pageSize;
//    /** 总页面数 */
//    private Integer pages;

    /**
     * 分页
     *
     * @param list 列表数据
     * @param total 总记录数
     */
    public PageDataInfo(T list, int total)
    {
        this.rows = list;
        this.total = total;
    }

    public PageDataInfo() {

    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

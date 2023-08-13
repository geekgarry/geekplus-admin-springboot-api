/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 2022/4/25 10:02 上午
 * description: 做什么的？
 */
package com.geekplus.common.page;

public class PageData {
    //页面序号
    private int pageNum=1;
    //默认页面行数为10
    private int pageSize=10;
    //排序字段
    private String orderBy;

    //排序方向
    private String isAsc="asc";

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        this.isAsc = isAsc;
    }
}

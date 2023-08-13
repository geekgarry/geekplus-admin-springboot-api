/**
 * author     : geekplus
 * email      : geekcjj@gmail.com
 * date       : 6/19/23 16:29
 * description: 做什么的？
 */
package com.geekplus.common.page;

import com.geekplus.common.util.ServletUtil;
import com.geekplus.common.util.ServletUtils;

public class TableDataSupport {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderBy";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 封装分页对象
     */
    public static PageData getPageDomain()
    {
        PageData pageDomain = new PageData();
        pageDomain.setPageNum(ServletUtil.getParameterToInt(PAGE_NUM));
        pageDomain.setPageSize(ServletUtil.getParameterToInt(PAGE_SIZE));
        pageDomain.setOrderBy(ServletUtil.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtil.getParameter(IS_ASC));
        return pageDomain;
    }

    public static PageData buildPageRequest()
    {
        return getPageDomain();
    }
}

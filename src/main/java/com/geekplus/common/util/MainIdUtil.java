package com.geekplus.common.util;

import java.util.Date;

/**
 * @ClassName: MainIdUtil
 * @Description: 主键生成工具类 订单编号
 * @author: WeiZheng
 * @date: 2018年8月29日 上午9:58:09
 */
public class MainIdUtil {

	private static Date date = new Date();
    private static StringBuilder buf = new StringBuilder();
    private static int seq = 1;
    private static final int ROTATION = 99999;
    public static synchronized long next() {
        if (seq > ROTATION)
            seq = 1;
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        String str = String.format("%1$tY%1$tm%1$td%2$05d", date, seq++);
        return Long.parseLong(str);
    }
    public static synchronized String nextStr() {
        if (seq > ROTATION)
            seq = 1;
        buf.delete(0, buf.length());
        date.setTime(System.currentTimeMillis());
        return String.format("%1$tY%1$tm%1$td%2$05d", date, seq++);
    }
    private MainIdUtil(){
    }
    public static void main(String[] args) {
		System.out.println(MainIdUtil.next());
	}
}

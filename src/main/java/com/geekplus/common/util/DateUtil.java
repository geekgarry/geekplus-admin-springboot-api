package com.geekplus.common.util;

import com.geekplus.common.util.string.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DateUtil
 * @Description 日期工具类
 * @Author Zheng
 * @Date 2017年11月6日 下午3:16:16
 */
public class DateUtil {
	public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final Calendar calendar = Calendar.getInstance();
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(Date date,String pattern){
		if(null == pattern){
			pattern = DATE_FORMAT_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	/**
	 * 获取当前时间之后的特定天数日期
	* @Title: getExpiredTime
	* @Description:
	* @return Date
	* @author WeiZheng
	* @date 2018年8月8日下午2:21:29
	 */
	public static Date getExpiredTime(Date curDate,Integer days){
		if(null == curDate){
			calendar.setTime(new Date());
		}else{
			calendar.setTime(curDate);
		}
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return calendar.getTime();
	}
	/**
	 * 获取指定日期后的几个小时时间
	 * @param curDate 当前日期
	 * @param hours 小时
	 * @return
	 */
	public static Date getExpiredTimeOfHour(Date curDate,Integer hours){
		if(null == curDate){
			calendar.setTime(new Date());
		}else{
			calendar.setTime(curDate);
		}
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}
	/**
	 * 获取上个月日期
	* @Title: getLastMonthTime
	* @Description:
	* @param curDate
	* @return Date
	* @author WeiZheng
	* @date 2018年8月14日下午4:16:54
	 */
	public static Date getLastMonthTime(Date curDate){
		if(null == curDate){
			calendar.setTime(new Date());
		}else{
			calendar.setTime(curDate);
		}
		calendar.add(Calendar.DAY_OF_YEAR, -30);
		return calendar.getTime();
	}
	/**
	 * 获取区间范围时间
	* @Title: getCurMonthBetweenTime
	* @Description:
	* @param date
	* @return Map<String,String> startTime  endTime
	* @author WeiZheng
	* @date 2018年8月13日上午9:53:22
	 */
	public static Map<String,String> getCurMonthBetweenTime(Date date){
		Map<String,String> back = new HashMap<>();
		if(null == date){
		    date = new Date();
		}
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		back.put("startTime",dateToStr( calendar.getTime(), null));
		back.put("endTime", dateToStr( date, null));
		return back;
	}
	/**
	 * 获取当天区间范围时间
	* @Title: getCurMonthBetweenTime
	* @Description:
	* @param date
	* @return Map<String,String> startTime  endTime
	* @author WeiZheng
	* @date 2018年8月13日上午9:53:22
	 */
	public static Map<String,String> getCurDayBetweenTime(Date date){
		Map<String,String> back = new HashMap<>();
		if(null == date){
		    date = new Date();
		}
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		back.put("startTime",dateToStr( calendar.getTime(), null));
		back.put("endTime", dateToStr( date, null));
		return back;
	}
	/**
	 * 获取指定区间范围的时间 年份 月份
	 * 需要进行验证合法性 若不合法 则取当前月份的区间
	 * @param year 制定的年份
	 * @param month 制定的月份 0-11
	 * @return startTime 月份开始时间  endTime 月份结束时间
	 */
	public static Map<String,String> getRangeOfYearAndMonth(Integer year,Integer month){
		Map<String,String> back = new HashMap<>();
		/**
		 * 进行判断传入的参数是否合法
		 * 年份 > 2016 月份 >= 0 <= 11
		 */
		if(null == year ||  year < 2016 || null == month || month < 0 || month > 11 ) {
			calendar.setTime(new Date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		}else {
			calendar.set(year, month, 1, 0, 0, 0);
		}
		back.put("startTime",dateToStr( calendar.getTime(), null));
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.SECOND, -1);
		back.put("endTime", dateToStr( calendar.getTime(), null));
		return back;
	}

	/**
	 * 验证是否超过给定时间
	 * @param origin
	 * @param hour
	 * @return true: 未超时   , false:超时
	 */
    public static boolean isOutOfDay(Date origin,Integer hour){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(origin);
    	calendar.add(Calendar.HOUR_OF_DAY, hour);
    	return calendar.getTimeInMillis() > new Date().getTime();
    }
    /**
     * 获取订单编号 timestamp + 6位随机数字串
     * @return
     */
    public static String getOrderId(){
    	String str = "";
        str += new Date().getTime() + StringUtil.getRndNum(6);
    	return str;
    }
    public static void main(String[] args) {
    	/**
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.HOUR_OF_DAY, -23);
    	DateUtil.isOutOfDay(calendar.getTime(), 24);
    	Map<String,String> back = getCurMonthBetweenTime(null);
    	System.out.println("startTime: "+back.get("startTime")+";endTime: "+back.get("endTime"));
    	**/
    	Map<String,String> backMap = getRangeOfYearAndMonth(2014,0);
    	System.out.println("startTime: "+backMap.get("startTime")+"--> "+backMap.get("endTime"));
	}
	/**
	 * @param now
	 * @return
	 */
	public static Map<String, String> getCurDayOfLastWeek(Date now) {
		// TODO Auto-generated method stub
		Map<String,String> back = new HashMap<>();
		if(null == now){
			now = new Date();
		}
		calendar.setTime(now);
		calendar.set(Calendar.WEEK_OF_MONTH, 0);
		calendar.set(Calendar.DAY_OF_WEEK, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		back.put("startTime",dateToStr( calendar.getTime(), null));
		back.put("endTime", dateToStr( now, null));
		return back;
	}
	/**
	 * @param now
	 * @return
	 */
	public static Map<String, String> getCurDayOfLastMonth(Date now) {
		// TODO Auto-generated method stub
		Map<String,String> back = new HashMap<>();
		if(null == now){
			now = new Date();
		}
		calendar.setTime(now);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.AM_PM, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		back.put("startTime",dateToStr( calendar.getTime(), null));
		back.put("endTime", dateToStr( now, null));
		return back;
	}
}

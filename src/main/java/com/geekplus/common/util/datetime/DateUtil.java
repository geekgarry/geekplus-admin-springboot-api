package com.geekplus.common.util.datetime;

import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * author     : geekplus
 * date       : 2022/5/31 9:58 上午
 * description: 时间工具类
 */
public class DateUtil extends DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    private static final Calendar calendar = Calendar.getInstance();

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getDateTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    /**
     * 格式化日期
     * @param format
     * @param date
     * @return
     */
    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String dateFilePath()
    {
        //DateFormatUtils.format(new Date(), "yyyy/MM/dd");
        return dateTimeNow("yyyy/MM/dd");
    }

    public static final String datePath()
    {
        calendar.setTime(new Date());
        // 获取当前年份
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月份（注意月份从0开始计数，所以需要加1）
        int month = calendar.get(Calendar.MONTH) + 1;
        // 获取当前日期
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //DecimalFormat df = new DecimalFormat("00");
        //String formattedMonth = df.format(month);
        return year + File.separator + String.format("%02d",month) + File.separator + day;
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        //DateFormatUtils.format(new Date(), "yyyyMMdd");
        return dateTimeNow(YYYYMMDD);
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
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 获取当前时间之后的特定天数日期
     * @Title: getExpiredTime
     * @Description:
     * @return Date
     * @author GeekPlus
     * @date 2020年8月8日下午2:21:29
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
     * @author GeekPlus
     * @date 2020年8月14日下午4:16:54
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
     * @author GeekPlus
     * @date 2020年8月13日上午9:53:22
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
        back.put("startTime",dateTime(calendar.getTime()));
        back.put("endTime", dateTime(date));
        return back;
    }
    /**
     * 获取当天区间范围时间
     * @Title: getCurMonthBetweenTime
     * @Description:
     * @param date
     * @return Map<String,String> startTime  endTime
     * @author GeekPlus
     * @date 2020年8月13日上午9:53:22
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
        back.put("startTime",dateTime(calendar.getTime()));
        back.put("endTime", dateTime(date));
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
        back.put("startTime",dateTime(calendar.getTime()));
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
        back.put("endTime", dateTime(calendar.getTime()));
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
        back.put("startTime",dateTime(calendar.getTime()));
        back.put("endTime", dateTime(now));
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
        back.put("startTime",dateTime(calendar.getTime()));
        back.put("endTime", dateTime(now));
        return back;
    }
}

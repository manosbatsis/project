package com.topideal.inventory.tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间工具类
 * @author liujiacheng
 * @version 1.0
 */
public class TimeUtils {

    /**
     * 得到当前时间
     * 
     * @return
     */
    public static Timestamp getNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取某日期本周周一的时间
     * @param date
     * @return
     */
    public static Timestamp getThisWeekMonday(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        // 获得当前日期是一个星期的第几天  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
        if (1 == dayWeek) {  
            cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
        return new Timestamp(cal.getTime().getTime());
    }  
    /**
     * 获取某日期下周周一的时间
     * @param date
     * @return
     */
    public static Timestamp getNextWeekMonday(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getThisWeekMonday(date));  
        cal.add(Calendar.DATE, 7);  
        return new Timestamp(cal.getTime().getTime());
    }  
    /**
     * 获取某日期下个月一号的时间
     * @param date
     * @return
     */
    public static Timestamp nextMonthFirstDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);  
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        return new Timestamp(cal.getTime().getTime());
    }

    // ------------------------------时间格式 Begin------------------------------

    /**
     * 格式化时间
     * 
     * @param date
     * @param fmt
     * @return
     */
    public static String format(Date date, String fmt) {
        if (date == null)
            return "";
        DateFormat formatter = new SimpleDateFormat(fmt);
        return formatter.format(date);
    }

    /**
     * 格式化时间
     *
     * @param dateTime 时间毫秒值
     * @param fmt
     * @return
     * @author: Mancy
     */
    public static String format(Long dateTime, String fmt) {
        DateFormat formatter = new SimpleDateFormat(fmt);
        return formatter.format(dateTime);
    }

    /**
     * 格式化日期
     * 
     * @param date
     * @return
     */
    public static String formatDay(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    /**
     * 格式化当前日期
     * 
     * @return
     */
    public static String formatDay() {
        return format(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
    }


    /**
     * 格式化完整时间 到分
     * 
     * @param date
     * @return
     */
    public static String formatMinuteTime(Date date) {
        return format(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化完整时间
     *
     * @param date
     */
    public static String formatFullTime(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化当前时间
     * 
     * @return
     */
    public static String formatFullTime() {
        return format(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
    }


    // ------------------------------时间格式 End------------------------------
    // ------------------------------转换时间start----------------------------
    /**
     * 转换时间
     * 
     * @param dateStr
     * @param fmt
     */
    public static Timestamp parse(String dateStr, String fmt) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(fmt)) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat(fmt);
        try {
            return new Timestamp(formatter.parse(dateStr).getTime());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 转换日期
     * 
     * @param dateStr
     */
    public static Timestamp parseDay(String dateStr) {
        return parse(dateStr, "yyyy-MM-dd");
    }

    /**
     * 转换日期 到时分
     *
     * @param dateStr
     */
    public static Timestamp parseDayMinute(String dateStr) {
        return parse(dateStr, "yyyy-MM-dd HH:mm");
    }

    /**
     * 转换完整时间
     * 
     * @param dateStr
     */
    public static Timestamp parseFullTime(String dateStr) {
        return parse(dateStr, "yyyy-MM-dd HH:mm:ss");
    }
    // ------------------------------转换时间 End------------------------------
    // ------------------------------时间计算 start------------------------------

    /**
     * 加几天
     * @param date 时间
     * @param dayNum 要加的天数
     * @return
     */
    public static Timestamp addDay(Date date, int dayNum) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, dayNum);
        return new Timestamp(cal.getTime().getTime());
    }

   

    /**
     * 剩余时间
     * hourNum + date - 当前时间 = 剩余时间
     * @param date 时间
     * @param hourNum 几个小时
     */
    public static Timestamp surplusTime(Date date, int hourNum) {
        return new Timestamp(surplusTimeLong(date, hourNum));
    }

    /**
     * 剩余时间
     * hourNum + date - 当前时间 = 剩余时间
     * @param date 时间
     * @param hourNum 几个小时
     */
    public static long surplusTimeLong(Date date, int hourNum) {
        if (date == null) {
            return 0l;
        }
        Date nowDate = getNow();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hourNum);
        Date endDate = cal.getTime();
        int com = endDate.compareTo(nowDate);

        if (com <= 0) {
            return 0l;
        }
        else {
            long surplusTimeLong = endDate.getTime() - nowDate.getTime();
            return surplusTimeLong;
        }
    }
    
    
    
    /**
     * 获取上个月份  baols
     * @param date
     * @return
     */
    public static String getLastMonth(Date date){   	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date); // 设置为当前时间
    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
    	date = calendar.getTime();
    	return  dateFormat.format(date);
    }
    
    /**
     *  上个月的第一天  baols
     * @return
     */
    public static Timestamp  getfirstDate(){
    	Calendar c=Calendar.getInstance();
 		c.add(Calendar.MONTH, -1);
 		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01  00:00:00");
 		String gtime2 = sdf2.format(c.getTime()); //上月第一天
	
 		return  parse(gtime2,"yyyy-MM-dd HH:mm:ss");
    }
    
    
    /**
     *  上个月的最后一天  baols
     * @return
     */
    public static Timestamp  getEndDate(){
    	Calendar c=Calendar.getInstance();
 		c.add(Calendar.MONTH, -1);
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 00, 00, 00);
		//按格式输出
		String gtime = sdf.format(c.getTime()); //上月最后一天
	
 		return  parse(gtime,"yyyy-MM-dd HH:mm:ss");
    }
    
   /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2  baols
     * @return
     */
    public static int differentDays(Date date1,Date date2)
    {
        int days = (int) ((date1.getTime() - date2.getTime()) / (1000*3600*24));
        return days;
    }
    
    // ------------------------------时间计算 end------------------------------

}

package com.topideal.common.tools;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 时间工具类
 * @author liujiacheng
 * @version 1.0
 */
public class TimeUtils {

    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYY_MM = "yyyy-MM";

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
    
    /**
     * 获取当前日期下个月 一号凌晨零点零分的时间
     */
    public static Timestamp nextMonthFirstZeroPointDate() {
    	SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");  
        Calendar calendar = Calendar.getInstance();  
        calendar.add(Calendar.MONTH, 1);  
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));  
       String format = dft.format(calendar.getTime());
       String time=format+" 00:00:00";
       Timestamp timestamp = Timestamp.valueOf(time);     
        return timestamp;
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
    	if(date == null){
    		return null;
    	}
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
    	if(date == null){
    		return null;
    	}
        return format(date, "yyyy-MM-dd HH:mm");
    }

    /**
     * 格式化完整时间
     *
     * @param date
     */
    public static String formatFullTime(Date date) {
    	if(date == null){
    		return null;
    	}
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

    public static String formatFullTimeNum() {
        return format(new Date(System.currentTimeMillis()), "yyyyMMddHHmmss");
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
        } catch (ParseException e) {
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
     *  本月的第一天  baols
     * @return
     */
    public static Timestamp  getNowfirstDate(){
    	Calendar c=Calendar.getInstance();
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
     *  本月的最后一天  baols
     * @return
     */
    public static Timestamp  getNowEndDate(){
    	Calendar c=Calendar.getInstance(); 		
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 00, 00, 00);
		//按格式输出
		String gtime = sdf.format(c.getTime()); //上月最后一天
	
 		return  parse(gtime,"yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 获取指定月份下个月的第一天
     * */
    public static String getNextMonthFirstDay(String month){
    	String nextMonthFirstDay = null;
    	try {
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        	Date date = dateFormat.parse(month);
        	Calendar calendar = Calendar.getInstance();
        	calendar.setTime(date); 
        	calendar.add(Calendar.MONTH, 1);
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));  
        	nextMonthFirstDay = sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return nextMonthFirstDay;
    }

    /**获取月份的最后一天
     * */
    public static String getLastDayOfMonth(String yearMonth) {
        String lastDay = "";
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            Date date = dateFormat.parse(yearMonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            lastDay = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastDay;
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
    
    
    /**
     * 格式化日期到月
     * 
     * @param date
     * @return
     */
    public static String formatMonth(Date date) {
        return format(date, "yyyy-MM");
    }
   
    /**
     * 格式化timestamp到月
     * @return
     */
    public static String formatMonthForStr(String dateStr) {
    	try {
    		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
    		SimpleDateFormat formatMonth=new SimpleDateFormat("yyyy-MM"); //加上时间
        	Date date=sDateFormat.parse(dateStr);
        	return formatMonth.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

    /**
     * 通用时间转换， Str -> Str
     * @param dateStr
     * @param sourceFormat
     * @param targetFormat
     * @return
     */
    public static String formatStrToStr(String dateStr, String sourceFormat, String targetFormat) {
        try {
            SimpleDateFormat sDateFormat=new SimpleDateFormat(sourceFormat); //加上时间
            SimpleDateFormat sTargetFormat=new SimpleDateFormat(targetFormat); //加上时间
            Date date=sDateFormat.parse(dateStr);
            return sTargetFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期到日
     * @return
     */
    public static String formatToDay(String dateStr) {
    	try {
    		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
    		SimpleDateFormat formatMonth=new SimpleDateFormat("yyyy-MM-dd"); //加上时间
        	Date date=sDateFormat.parse(dateStr);
        	return formatMonth.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

	/**
	 *   字符串转日期类型
	 * @param strDate
	 * @return
	 * @throws Exception
	 */
	public  static java.sql.Date strToSqlDate(String strDate){
		String str = strDate;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		java.sql.Date sqlDate = null;
		try {
			date = format.parse(str);
		    sqlDate = new java.sql.Date(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlDate;
	}
    
    // ------------------------------时间计算 end------------------------------

    /**
     * 获取当前时间前 多少个小时的时间
     */
    public static String getNowBeforeHourDate(Integer hour){
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    }
    /**
     * 获取当前时间前N分钟的时间
     */
    public static String getNowBeforeMinTime(Integer minute){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,-minute);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
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
//    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
    	calendar.add(Calendar.MONTH, -1); // 设置为上一个月
    	date = calendar.getTime();
    	return  dateFormat.format(date);
    }
    
    /**
     * 获取上个月份  baols
     */
    public static String getUpMonth(String month) throws ParseException{   	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	Date date = dateFormat.parse(month);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date); // 设置为当前时间
//    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
    	calendar.add(Calendar.MONTH, -1); // 设置为上一个月
    	date = calendar.getTime();
    	return  dateFormat.format(date);
    }
	 /**
     * 获取下个月份  baols
     * @param date
     * @return
     */
    public static String getNextMonth(Date date){   	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date); // 设置为当前时间
//    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) +1); // 设置为上一个月
    	calendar.add(Calendar.MONTH, 1); // 设置为下一个月
    	date = calendar.getTime();
    	return  dateFormat.format(date);
    }
    
    /**
     * 获取下下个月份  baols
     * @param date
     * @return
     */
    public static String getNextDownMonth(Date date){   	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date); // 设置为当前时间
//    	calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) +2); // 设置为上一个月
    	calendar.add(Calendar.MONTH, 2); // 设置为下下个月
    	date = calendar.getTime();
    	return  dateFormat.format(date);
    }
    /**
     * 获取系统当前时间前一天日期的月份
     * @return
     * @throws ParseException
     */
    public static String getLastMonthsByNow() throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // 设置为当前时间
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1); // 设置为前一天
        Date date = calendar.getTime();
        String month = dateFormat.format(date);//当前月
        return month;
    }
    /**
     * 获取系统当前时间前一天日期近二个月的月份 
     * @return
     * @throws ParseException 
     */
    public static String getLastTwoMonthsByNow() throws ParseException{   
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date()); // 设置为当前时间
    	calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1); // 设置为前一天
    	Date date = calendar.getTime();
    	String monthOne = dateFormat.format(date);//当前月
    	String monthTwo = getUpMonth(monthOne);//上月
		String months = monthTwo+","+monthOne;
    	return months;
    }
    /**
     * 获取系统当前时间前N月的月份
     * @return num 前N月
     * @throws ParseException
     */
    public static String getAgoMonthByNow(int num) {
        if(num<1) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // 设置为当前时间
//        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - num); //减N月
        calendar.add(Calendar.MONTH, -num); // 设置为前n个月
        Date date = calendar.getTime();
        String month = dateFormat.format(date);//当前月
        return month;
    }

    /**   
     * 计算两个日期之间相差的天数   
     * @param date1   
     * @param date2   
     * @return   
     */    
    public static int daysBetween(Date date1,Date date2){
    	try {
    		
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		String date1Str = dateFormat.format(date1);
    		String date2Str = dateFormat.format(date2);
    		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    		date1 = dateFormat2.parse(date1Str);
    		date2 = dateFormat2.parse(date2Str);
		} catch (Exception e) {
		}
    
        Calendar cal = Calendar.getInstance();     
        cal.setTime(date1);     
        long time1 = cal.getTimeInMillis();                  
        cal.setTime(date2);     
        long time2 = cal.getTimeInMillis();          
        long between_days=(time2-time1)/(1000*3600*24);     
             
       return Integer.parseInt(String.valueOf(between_days));            
    }

    /**
     * 计算两个日期之间相差的月份
     * @param date1Str  (YYYY-MM)
     * @param date2Str  (YYYY-MM)
     * @return
     */
    public static int monthsBetween(String date1Str,String date2Str, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(sdf.parse(date1Str));
        c2.setTime(sdf.parse(date2Str));

        int yearResult = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        int monthResult = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return (Math.abs(yearResult) * 12 + Math.abs(monthResult));
    }

    /**
     * 时间比较大小
     * @param date1
     * @param date2
     * @return 1 date1>date2 
     */
    public static String timeComparisonSize (Timestamp date1,Timestamp date2){
    	
    	if (date1.getTime()>date2.getTime()) {
			return "1";
		}else {
			return "0";
		}
    	
    }
    
    /**
     * 传入开始时间 和结束时间 返回 开始时间和结束时间段内的每一天数据 (包含开始时间和结束时间)
     */
   public static  List<String>  getYearMonthDateList(String startTime,String endTime ){	   	   
	   List<String> result = new ArrayList<String>();
		Date d1;
		try {
			d1 = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
			Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);//定义结束日期
			Calendar dd = Calendar.getInstance();//定义日期实例
			dd.setTime(d1);//设置日期起始时间
			while(dd.getTime().before(d2)||dd.getTime().equals(d2)){//判断是否到结束日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String str = sdf.format(dd.getTime());
				result.add(str);
				dd.add(Calendar.DATE, 1);				
			}
			
		} catch (ParseException e) {
			return null;
		}//定义起始日期
			
		return result;
   } 
   
   /**
    * 传入一个时间 和 之后的天数(只算工作日) 得出最后的工作日日期 
    * @param startTime 开始时间
    * @param afterDay 向后加的工作日时间
    * @return
    */
   public static String getAfterDay(Timestamp startTime, int afterDay){
		Calendar dd = Calendar.getInstance();//定义日期实例
		dd.setTime(startTime);//设置日期起始时间
		for (int i = 0; i < afterDay; i++) {			
			if (dd.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY) {
				dd.add(Calendar.DATE, 3);
			}else if (dd.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
				dd.add(Calendar.DATE, 2);
			}else {
				dd.add(Calendar.DATE, 1);	
			} 			
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		return sdf.format(dd.getTime());	   
   }

   /**
    *  传入一个时间 和 之前的天数(只算工作日) 得出最之前的工作日日期 
    * @param startTime
    * @param beforeDay
    * @return
    */
   public static String getBeforeDay(Timestamp startTime, int beforeDay){
		Calendar dd = Calendar.getInstance();//定义日期实例
		dd.setTime(startTime);//设置日期起始时间
		for (int i = 0; i < beforeDay; i++) {			
			if (dd.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY) {
				dd.add(Calendar.DATE, -3);
			}else if (dd.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) {
				dd.add(Calendar.DATE, -2);
			}else {
				dd.add(Calendar.DATE, -1);	
			} 			
		}	   
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return  sdf.format(dd.getTime());
  }
   
   /**
    * 两个时间比较 如果第一个时间大于等于第二个时间 返回true 其他返回false
    * @param dataStr1
    * @param dataStr2
    * @param fmt
    * @return
    */
   public static boolean dateStrComparison(String dataStr1,String dataStr2,String fmt){	   
		try {
			DateFormat dateFormat = new SimpleDateFormat(fmt);
			boolean b=dateFormat.parse(dataStr1).getTime()>=dateFormat.parse(dataStr2).getTime();
			return b;					
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   return false;
   }
   
   /**
    * 失效日期和当前时间 比较判断是否过期  过期返回 0过期,未过期返回1
    * 
    * @return
    */
   public static String  isNotIsExpire(Timestamp overdueDate){	
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	   String overdueDateStr = format.format(overdueDate);// 失效日期年月日
	   Timestamp overdueTimestamp= Timestamp.valueOf(overdueDateStr+" 00:00:00");// 失效日期 转成 当前时间00:00:00
	   Date date = new Date();// 新建此时的的系统时间
	   String isExpire=null;// 是否过期（0是 1否）
	   if (overdueTimestamp.getTime()>date.getTime()) {
		   isExpire="1";// 未过期
	   }else {
		   isExpire="0";// 已过期
	   }	   	   
	   return isExpire;
		
   }

    /**
     * 失效日期和当前时间 比较判断是否过期  过期返回 0过期,未过期返回1
     *
     * @return
     */
    public static String isNotIsExpireByDate(Date overdueDate){
        Date date = new Date();// 新建此时的的系统时间
        String isExpire=null;// 是否过期（0是 1否）
        if (overdueDate.getTime()>date.getTime()) {
            isExpire="1";// 未过期
        }else {
            isExpire="0";// 已过期
        }
        return isExpire;

    }
    /**
     * 解析bootstrat 时间插件格式
     * @param s
     * @return
     */
    public static List<Long> parse(String s){
        try{
            List<Long> timestampList=new ArrayList<Long>();
            String[] dateArrays=s.split("-");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
            timestampList.add(sdf.parse(dateArrays[0]).getTime());
            timestampList.add(sdf.parse(dateArrays[1]).getTime()+1000*60*60*24);
            return timestampList;
        }catch(Exception e){
            return null;
        }
    }
    
    /**
     * 解析bootstrat 时间插件格式
     * @param
     * @return
     */
    public static List<String> parseToString(String s){
        try{
            List<String> list=new ArrayList<String>();
            String[] dateArrays=s.split("-");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
            list.add(sdf2.format(sdf.parse(dateArrays[0].trim()))+" 00:00:00");
            list.add(sdf2.format(sdf.parse(dateArrays[1].trim()))+" 23:59:59");
            return list;
        }catch(Exception e){
            return null;
        }
    }
    /**
     * 获取某个日期的开始时间
     * @param d
     * @return
     */
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar=Calendar.getInstance();
        if(null!=d){
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),   
        calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }
     
    /**
     * 获取某个日期的结束时间
     * @param d
     * @return
     */
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar=Calendar.getInstance();
        if(null!=d){
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),   
        calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }
    
    /**
     * 获取上周开始时间
     */
    @SuppressWarnings("unused")
    public static Date getBeginDayOfLastWeek() {
        Date date=new Date();
        if (date==null) {
            return null;
        }
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int dayofweek=cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek==1) {
            dayofweek+=7;
        }
        cal.add(Calendar.DATE, 2-dayofweek-7);
        return getDayStartTime(cal.getTime());
    }
     
     
    /**
     * 获取上周的结束时间
     * @return
     */
    public static Date getEndDayOfLastWeek(){
        Calendar cal=Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }
    
    /**
     * 获取今年是哪一年
     * @return
     */
    public static Integer getNowYear(){
        Date date = new Date();
        GregorianCalendar gc=(GregorianCalendar)Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }
    /**
     * 获取本月是哪一月
     * @return
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc=(GregorianCalendar)Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }
    /**
     * 获取上月的开始时间
     * @return
     */
    public static Date getBeginDayOfLastMonth() {
        Calendar calendar=Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth()-2, 1);
        return getDayStartTime(calendar.getTime());
    }
    /**
     * 获取上月的结束时间
     * @return
     */
    public static Date getEndDayOfLastMonth() {
        Calendar calendar=Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth()-2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth()-2, day);
        return getDayEndTime(calendar.getTime());
    }
    /**
     * 当月最后一天数据
     * @param date
     * @return
     */
    public static String getMonthLastDay(Timestamp date){
    	Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String lastDay= format.format(c.getTime());
		return lastDay;
    	
    }

    /**
	 * 获取当前日期是星期几
	 * 
	 * @param date
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	/**
	 * 获取当前时间前5小时
	 */
	public static String getFiveHoursAgoDate() {
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 5);
		return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}
	 /**
     * 将字符串时间格式转换成Date时间格式，参数String类型
     * 比如字符串时间："2017-12-15 21:49:03"
     * 转换后的date时间：Fri Dec 15 21:49:03 CST 2017
     * @param datetime 类型为String
     * @return
     */
    public static Date StringToDate(String datetime){
    	/*if (StringUtils.isEmpty(datetime)) {
    		return null;			
		}*/
        SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date==null) {
        	try {
        		date=format.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }        	
		}
        if (date==null) {
			throw new RuntimeException("日期为空日期转化失败");
		}
        return date;
    }

    /**
     * String 转Timestamp
     * @param datetime
     * @return
     */
    public static Timestamp StringToTimestamp(String datetime){
    	/*if (StringUtils.isEmpty(datetime)) {
    		return null;			
		}*/
        SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Timestamp time = null;
        try {
        	Date date = sdFormat.parse(datetime);
        	time=new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (time==null) {
        	try {
        		Date date=format.parse(datetime);
        		time=new Timestamp(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }        	
		}
        if (time==null) {
			throw new RuntimeException("日期为空日期转化失败");
		}
        return time;
    }
    /**
     * 获取当前月第一天
     */
	public static String getCurrentMonthFirstDay() {
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar c = Calendar.getInstance();   
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        
        return first ;
	}

    /**
     * 获取某月第一天
     */
    public static String getMonthFirstDay(Date date) {

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();

        c.setTime(date);

        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());

        return first ;
    }
	
    /**
     * 判断是否是在2到10号
     */
	public static boolean  isOrNotContainDay() {
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat monthFormat=new SimpleDateFormat("yyyy-MM");		
		Calendar c = Calendar.getInstance();
        String nowDate = format.format(c.getTime());
        String monthDate = monthFormat.format(c.getTime());
        List<String> dataList=new ArrayList<>();
        dataList.add(monthDate+"-02");
        dataList.add(monthDate+"-03");
        dataList.add(monthDate+"-04");
        dataList.add(monthDate+"-05");
        dataList.add(monthDate+"-06");
        dataList.add(monthDate+"-07");
        dataList.add(monthDate+"-08");
        dataList.add(monthDate+"-09");
        dataList.add(monthDate+"-10");
        
        if (dataList.contains(nowDate)) {
        	return true ;
		}else {
			return false ;
		}

	}
	
	/**
	 * 三个日期都是年月日
	 * 获取逾期天数
	 * @return
	 */
	public static int getOverdueDay(String dateStr1,String dateStr2,int day){
		Timestamp date1 = Timestamp.valueOf(dateStr1+" 00:00:00");
		Timestamp date2 = Timestamp.valueOf(dateStr2+" 00:00:00");		
		Long betweendays=(date1.getTime()-date2.getTime())/(1000*3600*24);		
		int dayNum=betweendays.intValue()-day;		
		return dayNum;
	};
    /**
     * 判断是否yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static boolean isDate(String date) {
        Pattern p = Pattern.compile("^([1-2]{1}\\d{3})\\-(([0]{1}[1-9]{1})|([1]{1}[0-2]{1}))\\-(([0]{1}[1-9]{1})|([1-2]{1}\\d{1})|([3]{1}[0-1]{1}))\\s(([0-1]{1}\\d{1})|([2]{1}[0-3]))\\:([0-5]{1}\\d{1})\\:([0-5]{1}\\d{1})$");
        return p.matcher(date).matches();
    }
    /**
     * 判断时间格式 格式必须为“YYYY-MM-dd”
     * @param sDate
     * @return
     */
    public static boolean isYmdDate(String sDate) {
        int legalLen = 10;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 获取传入日期的最后一天天数据
     * @param date
     * @return
     */
    public static Timestamp getMonthEndDate(Date date) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar ca = Calendar.getInstance();  
    	ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String enddate = format.format(ca.getTime());
    	return Timestamp.valueOf(enddate+" 00:00:00");
   }
   /**获取前一天日期
    * */
   public static String getYesterday() {
       try {
           SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
           Calendar cal = Calendar.getInstance();
           cal.add(Calendar.DATE, -1);
           Date time = cal.getTime();
           String yesterday = format.format(time);
           return yesterday;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }
   
   /**
    * 获取当月所有日期
    * @param date
    * @return
    */
   public static List<Date> getAllTheDateOftheMonth(Date date) {
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);

		int month = cal.get(Calendar.MONTH);
		while(cal.get(Calendar.MONTH) == month){
			list.add(cal.getTime());
			cal.add(Calendar.DATE, 1);
		}
		return list;
	}

   /**
    * 获取当月所有日期
    * @param date
    * @return
    */
   public static Date getLastYear(Date date) {
       Calendar c = Calendar.getInstance();
       c.setTime(date);
       c.add(Calendar.YEAR, -1);
       Date newDate = c.getTime();
       return newDate;
    }


    /**
     * 指定日期加上指定天数
     * @param date 指定日期
     * @param addDays 指定天数
     * @param isNatural 是否排除周六日
     * @return
     */
    public static Timestamp addDateByWorkDay(Timestamp date,int addDays, boolean isNatural) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < addDays; i++) {
            calendar.add(Calendar.DATE, 1);
            if (isNatural) {
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    i--;
                }
            }
        }
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取指定月份的第一天
     * @param month
     * @return
     */
    public static String getFirstDayStartTime(String month, String fmt) {
        if (StringUtils.isBlank(month)) {
            month = getLastMonth(new Date());
        }

        if (StringUtils.isBlank(fmt)) {
            fmt = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Timestamp timestamp = parse(month, "yyyy-MM");

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(timestamp);

        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return sdf.format(new Timestamp(calendar.getTimeInMillis()));
    }

    /**
     * 获取指定月份的最后一天
     * @param month
     * @return
     */
    public static String getLastDayEndTime(String month, String fmt) {
        if (StringUtils.isBlank(month)) {
            month = getLastMonth(new Date());
        }
        if (StringUtils.isBlank(fmt)) {
            fmt = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Timestamp timestamp = parse(month, "yyyy-MM");

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(timestamp);
        int lastMonthMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                lastMonthMaxDay, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return sdf.format(new Timestamp(calendar.getTimeInMillis()));
    }
    /**
     * 获取当前小时
     */
    public static int getHour() {
        Calendar calendar=Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时制
        return hour;
    }

    public static String format(Timestamp timestamp, String formatStr) {
        SimpleDateFormat df = new SimpleDateFormat(formatStr);//定义格式，不显示毫秒
        String str = df.format(timestamp);
        return str;
    }

    public static boolean isValidDate(String str, String formatStr) {
        boolean convertSuccess=true;
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
//            设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess=false;
        }
        return convertSuccess;
    }

    public static void main(String[] args) {
        System.out.println(format(addDay(getNow(), 7), YYYY_MM_DD));

    }
}

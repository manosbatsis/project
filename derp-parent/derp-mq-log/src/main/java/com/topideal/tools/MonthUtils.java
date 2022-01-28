package com.topideal.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 月份工具
 * @author zhanghx
 */
public class MonthUtils {
	
	/**
	 * 获取前N个月的当前月份
	 * @param i
	 * @return
	 */
	public static String getPreviousMonths(int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		return sdf.format(m);
	}
	
	/**
	 * 获取前N天的日期
	 * @param N
	 * @return
	 */
	public static String getPreviousDay(int n) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -n);
		Date m = c.getTime();
		return sdf.format(m);
	}
	public static void main(String[] args) {
		System.out.println(getPreviousDay(180));
	}

}

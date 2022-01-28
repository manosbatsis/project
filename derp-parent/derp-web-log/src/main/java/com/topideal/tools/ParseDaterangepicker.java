package com.topideal.tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by weixiaolei on 2018/8/10.
 */
public class ParseDaterangepicker {

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
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
        try{
        	String[] dateArrays=s.split("-");
        	Calendar c = Calendar.getInstance(); 
        	c.setTime(sdf2.parse(sdf2.format(sdf.parse(dateArrays[1].trim()))));
        	int day=c.get(Calendar.DATE); 
        	c.set(Calendar.DATE,day+1); 
        	String dayAfter=sdf2.format(c.getTime()); 
            
            List<String> list=new ArrayList<String>();
            list.add(sdf2.format(sdf.parse(dateArrays[0].trim()))+" 00:00:00");
            list.add(dayAfter + " 00:00:00");
            return list;
        }catch(Exception e){
            return null;
        }
    }




}

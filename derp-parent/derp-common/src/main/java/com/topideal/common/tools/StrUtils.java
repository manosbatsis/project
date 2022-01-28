package com.topideal.common.tools;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * 字符串工具类
 * @author Mancy
 * @version 1.0
 */
public class StrUtils {

     /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
      * @param  reg  [0-9]*  id判断    [0-9\\,]*
     * @return
     */
    public static boolean stringReg(String str,String reg){
        Pattern pattern = Pattern.compile(reg);
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 校验id
     * @param id
     * @return  true  正确  false 错误
     */
    public static boolean validateId(Long id){
        if(id==null){
            return false;
        }
        if(id==0){
            return false;
        }
        String str = String.valueOf(id);
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 校验ids ,分隔
     * @param str
     * @return
     */
    public static boolean validateIds(String str){
        Pattern pattern = Pattern.compile("[0-9\\,]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }


    /**
     * 将字符串的ids转换成 集合
     * @param ids
     * @return
     */
    public static List parseIds(String ids){
        List list = new ArrayList();
        if(StringUtils.isEmpty(ids)) return null;
        for (String s : ids.split(",")) {
           list.add(Long.parseLong(s));
        }
        return list;
    }
    /**
     * 将字符串的ids转换成 集合
     * @param ids
     * @return
     */
    public static List parseIdsToStr(String ids){
        List list = new ArrayList();
        if(StringUtils.isEmpty(ids)) return null;
        for (String s : ids.split(",")) {
           list.add(s);
        }
        return list;
    }
    
    /**
     * 数字转标准字符串
     * @param data
     * @return
     */
    public static String doubleFormatString(double data) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(data);
    }

    /**
     * 数字转标准字符串(保留4位小数，若小数位末位为0则不显示)
     * @param data
     * @return
     */
    public static String doubleFormatStringByFour(double data) {
        DecimalFormat df = new DecimalFormat("#,###.####");
        return df.format(data);
    }
    
    /**
     * 数字转标准字符串
     * @param data
     * @return
     */
    public static String intFormatString(int data) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(data);
    }

    /**
     * 数字转标准字符串
     * @param data
     * @return
     */
    public static String longFormatString(long data) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(data);
    }

    //字符串脱敏
    public static String desensitization(String data){
          if(StringUtils.isEmpty(data)) return "";
           /**1长度只有1 则直接返回
            2.长度=2 *2 后1
            3.长度=3 1*3 前1后1
            4.长度4-5 1**45  前1后2
            5.长度6-8 12****78 前2后2
            6.长度9-10 123****890 前3后3
            7.长度11-20  1234*************7890 前3后4
            8.长度>20  1234*********7890 前4后4*/
            if(data.length()==1) {
                return data;
            }else if(data.length()==2){
                return "*"+data.substring(1);
            }else if(data.length()==3){
                return data.substring(0,1)+"*"+data.substring(1,2);
            }else if(data.length()<=5){
                return data.substring(0,1)+"**"+data.substring(data.length()-2);
            }else if(data.length()<=8){
                return data.substring(0,2)+"***"+data.substring(data.length()-2);
            }else if(data.length()<=10){
                return data.substring(0,3)+"****"+data.substring(data.length()-3);
            }else if(data.length()<=20){
                return data.substring(0,3)+"*****"+data.substring(data.length()-4);
            }else if(data.length()>20){
                return data.substring(0,4)+"******"+data.substring(data.length()-4);
            }
        return data;
    }

    public static void main(String[] args){
       String str =  longFormatString(10000000000L);
        System.out.println(str);

    }
















}

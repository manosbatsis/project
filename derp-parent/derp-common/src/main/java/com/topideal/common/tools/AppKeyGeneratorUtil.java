package com.topideal.common.tools;

import java.util.Random;

public class AppKeyGeneratorUtil {
	/**生成32位的密钥**/
    public static String KeyValue19(){
        //定义一个字符串（0-9）
        String str="1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<19; ++i){
            //产生0-18的数字
            int number=random.nextInt(10);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }
    public static void main(String[] args) {
		System.out.println(AppKeyGeneratorUtil.KeyValue19()); 
	}
}

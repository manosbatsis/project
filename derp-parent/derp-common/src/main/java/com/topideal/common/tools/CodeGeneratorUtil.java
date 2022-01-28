package com.topideal.common.tools;

import java.util.Arrays;
import java.util.Random;

/**
 * 业务编码生成规则
 * Created by acer on 2016/9/18.
 */
public class CodeGeneratorUtil {


	static final IdGen IDG = IdGen.get();

	/**
	 * 生成业务编码
	 * @param prefix  前缀
	 * @return
     */
	public static  String getNo(String prefix) {
		return prefix+IDG.nextId()+getCode(3);
	}

	/*
	 * 定义一个获取随机验证码的方法
	 * lenth :位数
	 */
	public static String getCode(int lenth) {
		String string = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";//保存数字0-9 和 大小写字母
		char[] ch = new char[lenth]; //声明一个字符数组对象ch 保存 验证码
		for (int i = 0; i < lenth; i++) {
			Random random = new Random();//创建一个新的随机数生成器
			int index = random.nextInt(string.length());//返回[0,string.length)范围的int值    作用：保存下标
			ch[i] = string.charAt(index);//charAt() : 返回指定索引处的 char 值   ==》保存到字符数组对象ch里面
		}
		//将char数组类型转换为String类型保存到result
		String result = String.valueOf(ch);//方法二： String方法   valueOf(char c) ：返回 char 参数的字符串表示形式。
		return result;
	}

	public static void main(String[] args) {
		//DSDD1420307668202946560
		String no = getNo("DSDD");
		System.out.println("单号:"+no);
		System.out.println("长度:"+no.length());

		String rum = getCode(3);//随机码
		System.out.println("随机码:"+rum);

	}
}

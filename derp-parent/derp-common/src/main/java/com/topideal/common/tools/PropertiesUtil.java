package com.topideal.common.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 从properties文件中读数据工具类
 * 
 */
public class PropertiesUtil {

	private static Properties config = new Properties();

	static {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:/config/smurfs.properties");
		InputStream in = null;
		try {
			in = resource.getInputStream();
			config.load(in);
		} catch (IOException e) {

		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 通过key值 获取value 值
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		return (String) config.get(key);
	}
	
	/**
	 * 根据正则获取key数组
	 * @param reg
	 * @return
	 */
	public static String[] getKeysByReg(String reg) {
		
		Pattern pattern = Pattern.compile(reg);
		
		Set<String> keys = config.stringPropertyNames(); 
		
		Iterator<String> iterator = keys.iterator(); 
		
		while(iterator.hasNext()) {
			
			Matcher matcher = pattern.matcher(iterator.next());
			
			if(!matcher.find()) {
				iterator.remove();
			}
			
		}
		
		return keys.toArray(new String[] {});
	}

	public static void main(String[] args) {
		System.out.println(getValue("testu"));
	}

}

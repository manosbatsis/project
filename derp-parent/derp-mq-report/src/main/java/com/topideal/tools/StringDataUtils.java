package com.topideal.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理字符串工具
 * @author zhanghx
 */
public class StringDataUtils {

	/**
	 * 数组转换List
	 * @param id
	 * @return
	 */
	public static List transformationIds(String id){
		List depotIds = new ArrayList();
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			depotIds.add(ids[i]);
		}
		return depotIds;
	}
	
}

package com.topideal.order.service.base;

import java.util.List;

import com.topideal.common.system.bean.SelectBean;

/**
 * 包装方式
 * @author zhanghx
 */
public interface PackTypeService {
	
	/**
	 * 获取所有包装方式
	 * @return
	 */
	List<SelectBean> getSelectBean();
}

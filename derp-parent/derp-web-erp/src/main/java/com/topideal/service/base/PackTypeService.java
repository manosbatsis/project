package com.topideal.service.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.base.PackTypeModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 包装方式
 * @author zhanghx
 */
public interface PackTypeService {
	
	/**
	 * 获取所有包装方式
	 * @return
	 */
	List<SelectBean> getSelectBean() throws SQLException;
	/**
	 * 详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PackTypeModel serchByModel(PackTypeModel model) throws SQLException;
}

package com.topideal.dao.base;


import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.base.EnumItemModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 枚举列表 dao
 * 
 * @author lian_
 */
public interface EnumItemDao extends BaseDao<EnumItemModel> {

	/**
	 * 根据枚举类型id获取枚举值列表
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<EnumItemModel> getListById(Long id) throws SQLException;

}

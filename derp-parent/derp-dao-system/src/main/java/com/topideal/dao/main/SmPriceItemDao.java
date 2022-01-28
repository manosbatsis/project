package com.topideal.dao.main;/*package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.SmPriceItemModel;

import java.sql.SQLException;
import java.util.List;

*//**
 * 供应商商品价格 dao
 * @author lianchenxing
 *
 *//*
public interface SmPriceItemDao extends BaseDao<SmPriceItemModel> {
	*//**
	 * 根据表头ID删除表体
	 * @param ids
	 *//*
	void deleteBySMPriceId(List<Long> ids);
	*//**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds  表体ids
	 * @param id       表头id
	 *//*
	void delBesidesIds(List<Long> itemIds, Long id);
		

	*//**
	 * 获取list
	 * @param model
	 * @return
	 * @throws SQLException
	 *//*
	List<SmPriceItemModel> getList(SmPriceItemModel model) throws SQLException;








}

*/
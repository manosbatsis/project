package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.CustomerSalePriceItemModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 客户销售价格表体 dao
 * @author lian_
 */
public interface CustomerSalePriceItemDao extends BaseDao<CustomerSalePriceItemModel> {
	

	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param id 表头id
	 */
	void delBesidesIds(List<Long> itemIds, Long id);

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CustomerSalePriceItemModel getListByPage(CustomerSalePriceItemModel model) throws SQLException;


	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CustomerSalePriceItemModel getDetails(CustomerSalePriceItemModel model)throws SQLException;

	int deleteBySaleId(Long id) throws SQLException;
}


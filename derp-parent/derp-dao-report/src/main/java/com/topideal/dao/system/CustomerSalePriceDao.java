package com.topideal.dao.system;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.CustomerSalePriceModel;
/**
 * 客户销售价格表 dao
 * @author lian_
 */
public interface CustomerSalePriceDao extends BaseDao<CustomerSalePriceModel> {
		
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	CustomerSalePriceModel getListByPage(CustomerSalePriceModel model) throws SQLException;

	/**
	 * 弹框 客户列表
	 * @param model
	 * @return
	 */
	CustomerSalePriceModel getCustomerByPage(CustomerSalePriceModel model) throws SQLException;
	
	
	/**
	 * 根据实体查询
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	CustomerSalePriceModel selectByModel(CustomerSalePriceModel model) throws SQLException;

	/**
	 *  根据商家id,客户id,商品货号 获取价格
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	CustomerSalePriceModel getMerchandisePrice(CustomerSalePriceModel model) throws SQLException;










}


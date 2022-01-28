package com.topideal.mapper.system;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.system.CustomerSalePriceModel;
import com.topideal.mapper.BaseMapper;

/**
 * 客户销售价格表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface CustomerSalePriceMapper extends BaseMapper<CustomerSalePriceModel> {


	/**
	 * 分页
	 */
	PageDataModel<CustomerSalePriceModel> getListByPage(CustomerSalePriceModel model) throws SQLException;

	/**
	 * 弹框 客户列表
	 * @param model
	 * @return
	 */
	PageDataModel<CustomerSalePriceModel> getBrandByPage(CustomerSalePriceModel model);

	/**
	 *  根据商家id,客户id,商品货号 获取价格
	 * @return
	 */
	CustomerSalePriceModel getPrice(CustomerSalePriceModel model) throws SQLException;
}


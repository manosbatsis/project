package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.main.CustomerSalePriceItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

/**
 * 客户销售价格表体 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface CustomerSalePriceItemMapper extends BaseMapper<CustomerSalePriceItemModel> {

	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * @param itemIds
	 * @param id 表头id
	 */
	void delBesidesIds(@Param("itemIds") List<Long> itemIds, @Param("salePriceId") Long orderId);
	
	/**
	 * 分页
	 */
	PageDataModel<CustomerSalePriceItemModel> getListByPage(CustomerSalePriceItemModel model) throws SQLException;
	/**
	 * 获取详情
	 */
	CustomerSalePriceItemModel getDetails(CustomerSalePriceItemModel model) throws SQLException;
	
}


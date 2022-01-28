package com.topideal.mapper.purchase;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.PurchaseCorrelationModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PurchaseCorrelationMapper extends BaseMapper<PurchaseCorrelationModel> {
	
	/**
	 * 根据入库单集合id获取数据
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseCorrelationModel> getDetailsByIds(List list) throws SQLException;

}


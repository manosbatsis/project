package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.SaleReturnOrderModel;
import com.topideal.mapper.BaseMapper;

/**
 * 销售退货订单
 * @author lian_
 *
 */
@MyBatisRepository
public interface SaleReturnOrderMapper extends BaseMapper<SaleReturnOrderModel> {
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	int delSaleReturnOrder(List ids) throws SQLException;

}


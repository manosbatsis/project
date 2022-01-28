package com.topideal.dao.sale;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotBatchModel;
import com.topideal.entity.vo.sale.WayBillItemModel;


public interface OrderReturnIdepotBatchDao extends BaseDao<OrderReturnIdepotBatchModel> {


	/**
	 * 根据电商订单商品ID获取批次信息
	 * @param id
	 * @return
	 */
	List<OrderReturnIdepotBatchDTO> listOrderReturnBatchDTOById(List<Long> list);







}

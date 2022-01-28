package com.topideal.mapper.sale;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotBatchModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface OrderReturnIdepotBatchMapper extends BaseMapper<OrderReturnIdepotBatchModel> {

	/**
	 * 根据电商订单商品ID获取批次信息
	 * @param id
	 * @return
	 */
//	List<OrderReturnIdepotBatchModel> listOrderReturnBatchById(List<Long> list);

	/**
	 * 根据电商订单商品ID获取批次信息
	 * @param id
	 * @return
	 */
	List<OrderReturnIdepotBatchDTO> listOrderReturnBatchDTOById(List<Long> list);

}

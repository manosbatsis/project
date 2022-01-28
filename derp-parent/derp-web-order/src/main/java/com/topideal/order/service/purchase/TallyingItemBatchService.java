package com.topideal.order.service.purchase;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;


/**
 * 理货单批次
 * @author zhanghx
 */
public interface TallyingItemBatchService {

	/**
	 * 根据理货单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<TallyingItemBatchDTO> getGoodsAndBatch(Long id) throws SQLException;
	
}

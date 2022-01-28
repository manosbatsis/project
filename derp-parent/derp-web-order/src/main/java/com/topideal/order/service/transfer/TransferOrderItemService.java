package com.topideal.order.service.transfer;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;


/**
 * 调拨订单 service
 * @author yucaifu
 * */
public interface TransferOrderItemService {

	
	/**根据调拨订单id查询调拨商品
	 * @param dto
	 * @return
	 */
	public List<TransferOrderItemDTO> searchTransferOrderItem(TransferOrderItemDTO dto) throws SQLException;
	
	/**
	 * 按商品货号统计调拨单各商品数量
	 * */
	public List<TransferOrderItemModel> countByGoodsNo(TransferOrderItemModel model);
}

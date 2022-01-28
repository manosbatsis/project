package com.topideal.order.service.transfer;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import com.topideal.entity.vo.transfer.TransferInDepotItemModel;


/**
 * 调拨入库详情 service
 * @author yucaifu
 * */
public interface TransferInDepotItemService {

	/**
	 * 列表查询
	 * @param transferId
	 */
	public List<TransferInDepotItemDTO> searchItemList(Long transferId) throws SQLException;
	
}

package com.topideal.order.service.transfer;



import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import com.topideal.entity.vo.transfer.TransferOutDepotItemModel;



/**
 * 调拨出库详情 service
 * @author yucaifu
 * */
public interface TransferOutDepotItemService {

	
	/**根据调拨订单id查询调拨商品
	 * @param id
	 * @return
	 */
	public TransferOutDepotItemModel searchItem(TransferOutDepotItemModel model) throws SQLException;
	/**根据调拨出库id查询调拨出库详情
	 * @param transferId
	 * @return
	 */
	public List<TransferOutDepotItemDTO> searchItemList(Long transferId) throws SQLException;
}

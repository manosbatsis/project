package com.topideal.dao.transfer;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import com.topideal.entity.vo.transfer.TransferInDepotItemModel;

/**
 * 调拨入库表体 dao
 * @author lian_
 */
public interface TransferInDepotItemDao extends BaseDao<TransferInDepotItemModel>{
		
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);

	public List<Map<String, Object>> getInItemListByTransferId(Long transferId);

	//根据id查询调拨入库表体
	List<TransferInDepotItemDTO> getItemByTransferInId(Long transferId);

}


package com.topideal.dao.transfer;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import com.topideal.entity.vo.transfer.TransferOutDepotItemModel;

/**
 * 调拨出库表体 dao
 * @author lian_
 */
public interface TransferOutDepotItemDao extends BaseDao<TransferOutDepotItemModel>{
		
	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);

    public List<Map<String, Object>> getItemListByTransferId(Long transferId);


    public List<TransferOutDepotItemDTO> getDTOItemListByTransferId(Long transferId);

    /**
     * 根据出库单id和调出商品id， 根据失效时间降序查询调出商品信息
     */
    List<TransferOutDepotItemModel> getItemListByTransferIdAndGoodsId(Long transferId, Long outGoodsId);

	/**
	 * 根据调拨出库单id统计调出商品数量
	 * */
	List<Map<String, Object>> getItemSumByIds(List<Long> ids);
}


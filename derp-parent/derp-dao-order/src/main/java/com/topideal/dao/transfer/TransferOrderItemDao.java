package com.topideal.dao.transfer;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;

/**
 * 调拨订单表体 dao
 * @author lian_
 */
public interface TransferOrderItemDao extends BaseDao<TransferOrderItemModel>{
		
	/**
	 * 按商品货号统计调拨单各商品数量
	 * */
	public List<TransferOrderItemModel> countByGoodsNo(TransferOrderItemModel model);

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	Integer checkGoodsIsUse(Long id);

	/**根据调拨订单id查询调拨商品dto
	 * @param dto
	 * @return
	 */
	public List<TransferOrderItemDTO> searchTransferOrderItem(TransferOrderItemDTO dto) throws SQLException;

}


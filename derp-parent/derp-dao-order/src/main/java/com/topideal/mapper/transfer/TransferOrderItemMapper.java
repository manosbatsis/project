package com.topideal.mapper.transfer;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import com.topideal.mapper.BaseMapper;

/**
 *  调拨订单表体 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface TransferOrderItemMapper extends BaseMapper<TransferOrderItemModel> {
    
	/**
	 * 按商品货号统计调拨单各商品数量
	 * */
	public List<TransferOrderItemModel> countByGoodsNo(TransferOrderItemModel model);

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);

	/**根据调拨订单id查询调拨商品dto
	 * @param dto
	 * @return
	 */
	public List<TransferOrderItemDTO> searchTransferOrderItem(TransferOrderItemDTO dto) throws SQLException;
	
}


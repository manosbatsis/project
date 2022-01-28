package com.topideal.mapper.transfer;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.transfer.TransferOutDepotItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 调拨出库表体 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface TransferOutDepotItemMapper extends BaseMapper<TransferOutDepotItemModel> {

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);


	public List<Map<String, Object>> getItemListByTransferId(Long transferId);

	public List<TransferOutDepotItemDTO> getDTOItemListByTransferId(@Param("transferId") Long transferId);

	/**
	 * 根据出库单id和调出商品id， 根据失效时间降序查询调出商品信息
	 */
	List<TransferOutDepotItemModel> getItemListByTransferIdAndGoodsId(@Param("transferId")Long transferId, @Param("outGoodsId")Long outGoodsId);

	/**
	 * 根据调拨出库单id统计调出商品数量
	 * */
	List<Map<String, Object>> getItemSumByIds(@Param("ids") List<Long> ids);
}


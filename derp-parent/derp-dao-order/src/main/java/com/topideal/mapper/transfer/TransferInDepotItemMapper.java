package com.topideal.mapper.transfer;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.transfer.TransferInDepotItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 调拨入库
 * @author lian_
 */
@MyBatisRepository
public interface TransferInDepotItemMapper extends BaseMapper<TransferInDepotItemModel> {

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	Integer checkGoodsIsUse(@Param("id") Long id);

	public List<Map<String, Object>> getInItemListByTransferId(Long transferId);

	//根据id查询调拨入库表体
	List<TransferInDepotItemDTO> getItemByTransferInId(@Param("transferId") Long transferId);
}


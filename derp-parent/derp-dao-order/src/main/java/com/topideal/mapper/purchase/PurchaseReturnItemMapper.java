package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseReturnItemDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.Map;


@MyBatisRepository
public interface PurchaseReturnItemMapper extends BaseMapper<PurchaseReturnItemModel> {

	int delByMap(Map<String, Object> delMap);
	/**
	 * 采购退选择商品弹窗
	 * @param dto
	 * @return
	 */
	PageDataModel<PurchaseReturnItemModel> getPurchaseItemPopupByPage(PurchaseReturnItemDTO dto);
	/**
	 * 获取退货数量
	 * @param paramMap
	 * @return
	 */
	Integer getTotalRetrurnNum(Map<String,Object> paramMap);

}

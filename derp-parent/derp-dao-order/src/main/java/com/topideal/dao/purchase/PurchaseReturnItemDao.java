package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseReturnItemDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;

import java.util.Map;


public interface PurchaseReturnItemDao extends BaseDao<PurchaseReturnItemModel> {

	int delByMap(Map<String, Object> delMap);

	/**
	 * 采购退选择商品弹窗
	 * @param dto
	 * @return
	 */
	PurchaseReturnItemDTO getPurchaseItemPopupByPage(PurchaseReturnItemDTO dto);

	/**
	 * 获取退货数量
	 * @param paramMap
	 * @return
	 */
	Integer getTotalRetrurnNum(Map<String,Object> paramMap);


}

package com.topideal.mapper.purchase;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.purchase.PurchaseInvoiceItemDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface PurchaseInvoiceItemMapper extends BaseMapper<PurchaseInvoiceItemModel> {

	PurchaseInvoiceItemModel getInvoiceNum(Map<String, Object> queryMap);

	/**
	 * 查询表体信息
	 * @param purchaseInvoiceItemModel
	 * @return
	 */
	List<PurchaseInvoiceItemDTO> getPurchaseInvoiceItemModel(PurchaseInvoiceItemDTO purchaseInvoiceItemModel);

	/**
	 * 根据发票id集合删除发票表体
	 * @param invoiceIds
	 */
	void batchDelByInvoiceIds(@Param("invoiceIds") List<Long> invoiceIds);
}

package com.topideal.dao.purchase;

import java.util.List;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseInvoiceDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceModel;


public interface PurchaseInvoiceDao extends BaseDao<PurchaseInvoiceModel>{

	PurchaseInvoiceDTO getListByPage(PurchaseInvoiceDTO dto);


	/**
	 * 预计付款日期在当前日期前后7天内（比如：若当前日期为：8月13日，按当期日期加减7天内）
	 * 发票列表的采购订单号不存在应付单明细中，应付单排除已删除状态
	 * @return
	 */
	List<PurchaseInvoiceModel> getPurchaseInvoiceByPayDate();


	List<PurchaseInvoiceModel> getInvoiceByIds(List<Long> ids);





}

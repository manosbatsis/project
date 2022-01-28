package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseInvoiceDTO;
import com.topideal.entity.vo.purchase.PurchaseInvoiceModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@MyBatisRepository
public interface PurchaseInvoiceMapper extends BaseMapper<PurchaseInvoiceModel> {

	PageDataModel<PurchaseInvoiceDTO> getListByPage(PurchaseInvoiceDTO dto);


	/**
	 * 预计付款日期在当前日期前后7天内（比如：若当前日期为：8月13日，按当期日期加减7天内）
	 * 发票列表的采购订单号不存在应付单明细中，应付单排除已删除状态
	 * @return
	 */
	List<PurchaseInvoiceModel> getPurchaseInvoiceByPayDate();

	List<PurchaseInvoiceModel> getInvoiceByIds(@Param("ids") List<Long> ids);
}

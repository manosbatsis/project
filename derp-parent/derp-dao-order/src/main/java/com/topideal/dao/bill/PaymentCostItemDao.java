package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.PaymentCostItemDTO;
import com.topideal.entity.vo.bill.PaymentCostItemModel;

import java.util.List;
import java.util.Map;


public interface PaymentCostItemDao extends BaseDao<PaymentCostItemModel> {
		
	/**
	 * 项目额度预警查询应付费用表体
	 * @param queryOrderMap
	 * @return
	 */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap);


    int countByDTO(PaymentCostItemDTO costItemDTO);

	List<PaymentCostItemDTO> listForExport(PaymentCostItemDTO costItemDTO);
}

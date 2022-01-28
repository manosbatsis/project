package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.bill.PaymentCostItemDTO;
import com.topideal.entity.vo.bill.PaymentCostItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PaymentCostItemMapper extends BaseMapper<PaymentCostItemModel> {

	/**
	 * 项目额度预警查询应付订单费用表体
	 * @param queryOrderMap
	 * @return
	 */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap);

    int countByDTO(PaymentCostItemDTO costItemDTO);

    List<PaymentCostItemDTO> listForExport(PaymentCostItemDTO costItemDTO);
}

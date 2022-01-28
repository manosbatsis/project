package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/1 15:27
 * @Description: 费用差异调整单
 */
public interface ToCTempCostDiffOrderService {

    TocTemporaryReceiveBillCostItemDTO listCostDiffOrder(User user, TocTemporaryReceiveBillCostItemDTO dto) throws Exception;
}

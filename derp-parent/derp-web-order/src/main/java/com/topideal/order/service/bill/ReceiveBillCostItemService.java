package com.topideal.order.service.bill;

import com.topideal.entity.dto.bill.ReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.transfer.TransferOrderFrom;
import com.topideal.order.webapi.bill.form.ReceiveBillForm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应收账单service
 * @Author: Chen Yiluan
 * @Date: 2020-07-27 15:01
 **/
public interface ReceiveBillCostItemService {

    /**
     * 根据商品、po号、项目类型统计应收账单费用明细
     * @param billId 账单id
     * @param isAddItems 是否包含明细
     */
    List<ReceiveBillCostItemDTO> itemListGroupByBillId(Long billId, boolean isAddItems) throws SQLException;

    Map<String, Object> saveReceiveBillCost(ReceiveBillDTO dto) throws Exception;

    Map<String, String> saveApiReceiveBillCost(ReceiveBillForm form) throws Exception;
}

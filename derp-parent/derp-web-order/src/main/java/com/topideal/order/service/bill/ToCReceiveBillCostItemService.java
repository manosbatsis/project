package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.TocSettlementReceiveBillDTO;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillCostItemModel;
import com.topideal.order.webapi.bill.form.ToCReceiveBillCostItemForm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ToCReceiveBillCostItemService {


    /**
     * 手动添加费项列表
     */
    List<TocSettlementReceiveBillCostItemModel> listCostByHandAdd(Long billId) throws SQLException;

    Map<String, Object> saveReceiveBillCost(TocSettlementReceiveBillDTO dto) throws Exception;

    Map<String, Object> saveAPIReceiveBillCost(User user, Long id, List<ToCReceiveBillCostItemForm> costItem) throws Exception;
}

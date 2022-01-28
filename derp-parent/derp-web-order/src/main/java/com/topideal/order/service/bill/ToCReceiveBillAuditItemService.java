package com.topideal.order.service.bill;

import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;

import java.sql.SQLException;
import java.util.List;

/**
 * toc应收账单service
 **/
public interface ToCReceiveBillAuditItemService {

    List<TocSettlementReceiveBillAuditItemModel> listByBillId(Long billId) throws SQLException;

}

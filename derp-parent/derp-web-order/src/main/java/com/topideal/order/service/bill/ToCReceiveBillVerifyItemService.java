package com.topideal.order.service.bill;

import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;

import java.sql.SQLException;
import java.util.List;

/**
 * toc应收账单核销service
 **/
public interface ToCReceiveBillVerifyItemService {

    List<TocSettlementReceiveBillVerifyItemModel> listByBillId(Long billId) throws SQLException;

}

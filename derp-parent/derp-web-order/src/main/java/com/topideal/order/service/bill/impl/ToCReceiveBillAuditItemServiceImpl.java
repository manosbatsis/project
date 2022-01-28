package com.topideal.order.service.bill.impl;

import com.topideal.dao.bill.TocSettlementReceiveBillAuditItemDao;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.order.service.bill.ToCReceiveBillAuditItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description: toc应收账单审核service
 * @Author: Chen Yiluan
 * @Date: 2020/12/31 18:20
 **/
@Service
public class ToCReceiveBillAuditItemServiceImpl implements ToCReceiveBillAuditItemService {

    @Autowired
    private TocSettlementReceiveBillAuditItemDao tocSettlementReceiveBillAuditItemDao;

    @Override
    public List<TocSettlementReceiveBillAuditItemModel> listByBillId(Long billId) throws SQLException {
        TocSettlementReceiveBillAuditItemModel tocSettlementReceiveBillAuditItemModel = new TocSettlementReceiveBillAuditItemModel();
        tocSettlementReceiveBillAuditItemModel.setBillId(billId);
        return tocSettlementReceiveBillAuditItemDao.list(tocSettlementReceiveBillAuditItemModel);
    }
}

package com.topideal.order.service.bill.impl;

import com.topideal.dao.bill.TocSettlementReceiveBillVerifyItemDao;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import com.topideal.order.service.bill.ToCReceiveBillVerifyItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @Description: toc应收账单核销service
 * @Author: Chen Yiluan
 * @Date: 2020/12/31 18:24
 **/
@Service
public class ToCReceiveBillVerifyItemServiceImpl implements ToCReceiveBillVerifyItemService {

    @Autowired
    private TocSettlementReceiveBillVerifyItemDao tocSettlementReceiveBillVerifyItemDao;

    @Override
    public List<TocSettlementReceiveBillVerifyItemModel> listByBillId(Long billId) throws SQLException {
        TocSettlementReceiveBillVerifyItemModel verifyItemModel = new TocSettlementReceiveBillVerifyItemModel();
        verifyItemModel.setBillId(billId);
        return tocSettlementReceiveBillVerifyItemDao.list(verifyItemModel);
    }
}

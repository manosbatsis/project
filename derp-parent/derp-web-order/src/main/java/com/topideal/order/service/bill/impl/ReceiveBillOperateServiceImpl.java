package com.topideal.order.service.bill.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.dao.bill.ReceiveBillAuditItemDao;
import com.topideal.dao.bill.ReceiveBillOperateDao;
import com.topideal.entity.vo.bill.ReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.ReceiveBillOperateModel;
import com.topideal.order.service.bill.ReceiveBillOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: 应收账单service实现类
 * @Author: Chen Yiluan
 * @Date: 2020/07/27 15:01
 **/
@Service
public class ReceiveBillOperateServiceImpl implements ReceiveBillOperateService {

    @Autowired
    private ReceiveBillOperateDao receiveBillOperateDao;

    @Override
    public List<ReceiveBillOperateModel> listByBillId(Long billId) throws SQLException {
        ReceiveBillOperateModel itemModel = new ReceiveBillOperateModel();
        itemModel.setBillId(billId);
        return receiveBillOperateDao.list(itemModel);
    }

    @Override
    public Timestamp getMaxAuditDate(List<Long> billIds) {
        ReceiveBillOperateModel operateModel = receiveBillOperateDao.getLatestOperate(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1, billIds);
        if (operateModel != null) {
            return operateModel.getOperateDate();
        }
        return null;
    }
}

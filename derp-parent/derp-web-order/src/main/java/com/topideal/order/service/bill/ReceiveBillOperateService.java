package com.topideal.order.service.bill;

import com.topideal.entity.vo.bill.ReceiveBillOperateModel;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Description: 应收账单service
 * @Author: Chen Yiluan
 * @Date: 2020-07-27 15:01
 **/
public interface ReceiveBillOperateService {

    List<ReceiveBillOperateModel> listByBillId(Long billId) throws SQLException;

    /**
     * 获取应收账单开票的最大审核时间
     */
    Timestamp getMaxAuditDate(List<Long> billIds);

}

package com.topideal.dao.bill;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.PaymentVerificateItemModel;


public interface PaymentVerificateItemDao extends BaseDao<PaymentVerificateItemModel> {

    /**
     * 批量保存核销明细
     **/
    Integer batchSave(List<PaymentVerificateItemModel> list) throws SQLException;
    //批量更新
    void batchUpdatePaymentId(List<Long> list , Long paymentId) throws SQLException;

}

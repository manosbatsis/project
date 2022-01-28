package com.topideal.dao.bill;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.PaymentSummaryModel;


public interface PaymentSummaryDao extends BaseDao<PaymentSummaryModel> {
		
	//批量新增
	Integer batchSave(List<PaymentSummaryModel> list) throws SQLException;
	 //批量更新
    void batchUpdatePaymentId(List<Long> list , Long paymentId) throws SQLException;

}

package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.ReceiveInvoicenoModel;


public interface ReceiveInvoicenoDao extends BaseDao<ReceiveInvoicenoModel> {

    Long getMaxValue(String invoiceNoPrefix) throws Exception;









}

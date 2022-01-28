package com.topideal.dao.bill;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillItemDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillItemModel;


public interface AdvancePaymentBillItemDao extends BaseDao<AdvancePaymentBillItemModel> {

	List<AdvancePaymentBillItemDTO> getVeriAdvancePaymentList(AdvancePaymentBillDTO billDTO);
		










}

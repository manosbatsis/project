package com.topideal.mapper.bill;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillItemDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AdvancePaymentBillItemMapper extends BaseMapper<AdvancePaymentBillItemModel> {

	List<AdvancePaymentBillItemDTO> getVeriAdvancePaymentList(AdvancePaymentBillDTO billDTO);



}

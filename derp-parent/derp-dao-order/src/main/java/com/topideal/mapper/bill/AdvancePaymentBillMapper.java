package com.topideal.mapper.bill;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillExportDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AdvancePaymentBillMapper extends BaseMapper<AdvancePaymentBillModel> {


    PageDataModel<AdvancePaymentBillDTO> getListByPage(AdvancePaymentBillDTO dto);

	List<AdvancePaymentBillExportDTO> getExportExcel(AdvancePaymentBillExportDTO exportDTO);
}

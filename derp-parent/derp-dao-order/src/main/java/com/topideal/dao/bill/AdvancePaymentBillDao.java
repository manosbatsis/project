package com.topideal.dao.bill;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.AdvancePaymentBillDTO;
import com.topideal.entity.dto.bill.AdvancePaymentBillExportDTO;
import com.topideal.entity.vo.bill.AdvancePaymentBillModel;


public interface AdvancePaymentBillDao extends BaseDao<AdvancePaymentBillModel> {


    /**
     * 获取分页
     * @param dto
     * @return
     */
    AdvancePaymentBillDTO getListByPage(AdvancePaymentBillDTO dto);

    /**
     * 获取导出记录
     * @param exportDTO
     * @return
     */
	List<AdvancePaymentBillExportDTO> getExportExcel(AdvancePaymentBillExportDTO exportDTO);
}

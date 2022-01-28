package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.PaymentBillDTO;
import com.topideal.entity.vo.bill.PaymentBillModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface PaymentBillMapper extends BaseMapper<PaymentBillModel> {

    PageDataModel<PaymentBillDTO> getListByPage(PaymentBillDTO dto);

    List<PaymentBillModel> getNcBackfillList();

    List<PaymentBillModel> getNcVoucherFillBackList();

    List<PaymentBillModel> listByDto(PaymentBillDTO dto);

    void batchUpdate(@Param("paymentBillModels") List<PaymentBillModel> paymentBillModels);
    //批量新增
  	Integer batchSave(List<PaymentBillModel> list) throws SQLException;
  	
	/**
	 * 项目额度预警查询应付订单
	 * @param queryOrderMap
	 * @return
	 */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap);

    int countByDTO(PaymentBillDTO dto);

    List<PaymentBillDTO> listForExport(PaymentBillDTO dto);

    PageDataModel<PaymentBillDTO> getListByPageWithItem(PaymentBillDTO dto);

    PageDataModel<PaymentBillDTO> getListByPageWithCostItem(PaymentBillDTO dto);
}

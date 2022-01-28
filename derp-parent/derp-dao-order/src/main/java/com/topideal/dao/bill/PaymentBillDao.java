package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.PaymentBillDTO;
import com.topideal.entity.vo.bill.PaymentBillModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface PaymentBillDao extends BaseDao<PaymentBillModel> {

	PaymentBillDTO getListByPage(PaymentBillDTO dto);

	/**
     * 查询NC回填列表
     * @return
     */
    List<PaymentBillModel> getNcBackfillList();

    /**
     * NC凭证更新列表
     *
     * @return
     */
    List<PaymentBillModel> getNcVoucherFillBackList();

    /**
     * 查询应付单
     *
     * @return
     */
    List<PaymentBillModel> listByDto(PaymentBillDTO dto);


    /**
     * 批量更新应付单
     *
     * @param paymentBillModels
     * @return
     */
    void batchUpdate(List<PaymentBillModel> paymentBillModels);
	//批量新增
	Integer batchSave(List<PaymentBillModel> list) throws SQLException;
	/**
	 * 项目额度预警查询应付账单
	 * @param queryOrderMap
	 * @return
	 */
    List<Map<String, Object>> getProjectWarnList(Map<String, Object> queryOrderMap);

    /**
     * count by dto
     * @param dto
     * @return
     */
    int countByDTO(PaymentBillDTO dto);

    List<PaymentBillDTO> listForExport(PaymentBillDTO dto);

    PaymentBillDTO getListByPageWithItem(PaymentBillDTO dto);

    PaymentBillDTO getListByPageWithCostItem(PaymentBillDTO dto);
}

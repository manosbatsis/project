package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.PaymentItemDTO;
import com.topideal.entity.vo.bill.PaymentItemModel;

import java.sql.SQLException;
import java.util.List;


public interface PaymentItemDao extends BaseDao<PaymentItemModel> {


    /**
     * 获取应付明细
     * @param dto
     * @return
     */
    public List<PaymentItemDTO> getPaymentItemDto(PaymentItemDTO dto);
    //批量新增
  	Integer batchSave(List<PaymentItemModel> list) throws SQLException;
  	 //批量更新
    void batchUpdatePaymentId(List<Long> list , Long paymentId) throws SQLException;

    /**
     * list for export
     * @param itemDTO
     * @return
     */
    List<PaymentItemDTO> listForExport(PaymentItemDTO itemDTO);

    int countByDTO(PaymentItemDTO itemDTO);
}

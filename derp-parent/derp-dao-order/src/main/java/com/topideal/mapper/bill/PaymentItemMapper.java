package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.bill.PaymentItemDTO;
import com.topideal.entity.vo.bill.PaymentItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface PaymentItemMapper extends BaseMapper<PaymentItemModel> {


    /**
     * 获取应付明细
     * @param dto
     * @return
     */
    public List<PaymentItemDTO> getPaymentItemDto(PaymentItemDTO dto);
    //批量新增
  	Integer batchSave(List<PaymentItemModel> list) throws SQLException;
  	 //批量更新
  	void batchUpdatePaymentId(@Param("list")List<Long> list , @Param("paymentId")Long paymentId) throws SQLException;

    List<PaymentItemDTO> listForExport(PaymentItemDTO itemDTO);

    int countByDTO(PaymentItemDTO itemDTO);
}

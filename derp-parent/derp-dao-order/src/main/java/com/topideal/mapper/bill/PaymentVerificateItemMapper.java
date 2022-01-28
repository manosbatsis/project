package com.topideal.mapper.bill;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.PaymentVerificateItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PaymentVerificateItemMapper extends BaseMapper<PaymentVerificateItemModel> {

    /**
     * 批量保存核销明细
     **/
    Integer batchSave(@Param("list") List<PaymentVerificateItemModel> list) throws SQLException;
    //批量更新
  	void batchUpdatePaymentId(@Param("list")List<Long> list , @Param("paymentId")Long paymentId) throws SQLException;

}

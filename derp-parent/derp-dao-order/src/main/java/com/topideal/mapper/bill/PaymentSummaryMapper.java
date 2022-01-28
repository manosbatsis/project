package com.topideal.mapper.bill;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.PaymentSummaryModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PaymentSummaryMapper extends BaseMapper<PaymentSummaryModel> {
	//批量新增
	Integer batchSave(List<PaymentSummaryModel> list) throws SQLException;
	 //批量更新
  	void batchUpdatePaymentId(@Param("list")List<Long> list , @Param("paymentId")Long paymentId) throws SQLException;
}

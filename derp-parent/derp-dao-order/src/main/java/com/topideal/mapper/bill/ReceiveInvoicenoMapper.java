package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.ReceiveInvoicenoModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


@MyBatisRepository
public interface ReceiveInvoicenoMapper extends BaseMapper<ReceiveInvoicenoModel> {

    Long getMaxValue(@Param("invoiceNoPrefix") String invoiceNoPrefix) throws Exception;

}

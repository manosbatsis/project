package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.SaleCreditBillOrderItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleCreditBillOrderItemMapper extends BaseMapper<SaleCreditBillOrderItemModel> {

	Integer getRedempNum(Map<String,Object> map) throws SQLException;

}

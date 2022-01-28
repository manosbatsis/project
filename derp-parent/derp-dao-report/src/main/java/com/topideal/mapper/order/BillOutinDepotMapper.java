package com.topideal.mapper.order;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.BillOutinDepotModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface BillOutinDepotMapper extends BaseMapper<BillOutinDepotModel> {

    List<Map<String, Object>> countByCustomer(Map<String, Object> params) throws SQLException;

    List<Map<String, Object>> getBillOutDepotTop10ByBrand(Map<String, Object> params) throws SQLException;

}

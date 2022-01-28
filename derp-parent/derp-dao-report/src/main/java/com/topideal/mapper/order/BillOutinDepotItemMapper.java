package com.topideal.mapper.order;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.BillOutinDepotItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BillOutinDepotItemMapper extends BaseMapper<BillOutinDepotItemModel> {

	List<BillOutinDepotItemModel> getVipPoAccountByType(Map<String, Object> queryMap);

	List<Map<String, Object>> getVipDetails(Map<String, Object> queryMap);

	BillOutinDepotItemModel getAutoVeriOutinDepotAccount(Map<String, Object> queryMap);



}

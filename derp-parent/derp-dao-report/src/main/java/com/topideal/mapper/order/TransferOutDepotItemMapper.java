package com.topideal.mapper.order;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.TransferOutDepotItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 调拨出库表体 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface TransferOutDepotItemMapper extends BaseMapper<TransferOutDepotItemModel> {

	Integer getVIPOutDepotAccount(Map<String, Object> queryMap);

	List<Map<String, Object>> getVIPOutDepotDetails(Map<String, Object> queryMap);

}

package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.WarehouseOrderRelModel;
import com.topideal.mapper.BaseMapper;

/**
 * 采购入库关联采购订单表 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface WarehouseOrderRelMapper extends BaseMapper<WarehouseOrderRelModel> {
	/**
	 * 查询采购入库关联采购订单表    (根据采购入库单升序排列)
	 * @param modle
	 * @return
	 */
	List<WarehouseOrderRelModel> getAscPurchaseOrderIdList(WarehouseOrderRelModel modle)throws SQLException ;

}


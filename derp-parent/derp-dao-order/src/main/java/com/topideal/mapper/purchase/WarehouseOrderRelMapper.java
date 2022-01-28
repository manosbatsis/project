package com.topideal.mapper.purchase;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.entity.vo.purchase.WarehouseOrderRelModel;
import com.topideal.mapper.BaseMapper;

/**
 * 采购入库关联采购订单表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface WarehouseOrderRelMapper extends BaseMapper<WarehouseOrderRelModel> {

	/**
	 * 根据采购单id获取最后入仓时间
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseModel> getInboundDateById(@Param("id") Long id) throws SQLException;

	List<WarehouseOrderRelModel> listOrderByAudthTime(@Param("id") Long id);

	/**
	 * 根据采购订单id 统计红冲与否的数量
	 * @param purchaseId
	 * @return
	 */
	Integer countWriteOffNum(@Param("purchaseId") Long purchaseId, @Param("isWriteOff") String isWriteOff);
}



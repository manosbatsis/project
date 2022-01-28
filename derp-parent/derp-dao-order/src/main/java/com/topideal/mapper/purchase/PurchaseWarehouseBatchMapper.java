package com.topideal.mapper.purchase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;
import com.topideal.mapper.BaseMapper;

/**
 * 采购入库单商品批次详情 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface PurchaseWarehouseBatchMapper extends BaseMapper<PurchaseWarehouseBatchModel> {

	/**
	 * 根据采购入库单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseBatchModel> getGoodsAndBatch(@Param("id") Long id) throws SQLException;
	
	/**
	 * 根据采购入库单id获取商品信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseBatchModel>getGoodsAndBatch2(@Param("id") Long id) throws SQLException;
	
	/**
	 * 根据商品表体id获取批次信息
	 * @param queryMap
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseBatchModel>getGoodsAndBatch3(Map<String, Object> queryMap) throws SQLException;
	
	/**
	 * 根据采购入库单编码和货号获取商品信息和批次信息
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseBatchModel> getGoodBatchByWarehouseCodeAndGoodsNo(Map<String , Object> map) throws SQLException;

	/**
	 * 统计批次表商品量
	 * @param batchNumQueryMap
	 * @return
	 */
	Integer getBatchNum(Map<String, Object> batchNumQueryMap);
}


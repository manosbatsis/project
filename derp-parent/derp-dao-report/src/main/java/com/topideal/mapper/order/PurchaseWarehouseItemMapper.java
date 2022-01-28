package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.order.PurchaseAnalysisModel;
import com.topideal.entity.vo.order.PurchaseWarehouseItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 采购入库商品列表 mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface PurchaseWarehouseItemMapper extends BaseMapper<PurchaseWarehouseItemModel> {

	/**
	 * 分组查询采购入库数量（允许传付款主体查询）
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseWarehouseItemModel> getWarehouseNumGroupBy(Map<String, Object> paramMap) throws SQLException;
	

	/**
	 * 根据采购入库单表体对象查查采购入库单
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(@Param("id") Long id)throws SQLException;
	/**
	 * 新财务报表-查询采购入库数量
	 * @param paramMap
	 */
	List<Map<String,Object>> getPurWarehouseNum(Map<String, Object> paramMap);



	/**
	 * 事业部财务经销存采购入库加权
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getBuPurWarehouseAndAnalysisWeighte(Map<String, Object> paramMap) throws SQLException;
	

	/**
	 * (事业部财务经销存)获取采购残损明细表 来货残损数据 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getBuFinancePurchaseDamagedWornExpireList(Map<String, Object> paramMap)throws SQLException;
	
	
}


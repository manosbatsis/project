package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinanceSdWarehouseDetailsModel;


public interface BuFinanceSdWarehouseDetailsDao extends BaseDao<BuFinanceSdWarehouseDetailsModel>{
		

	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)采购SD入库明细数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceSdWarehouseDetails(Map<String, Object> map) throws SQLException;
	
	/**
	 * 获取(事业部财务经销存)采购入库SD明细 导出数据
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuFinanceSdWarehouseDetailsMap(Map<String, Object> paramMap)throws SQLException;

	/**
	 * 获取导出表头的后面各个每个商品类型金额
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuOrderGoodsAmont(Map<String, Object> paramMap)throws SQLException;
	/**
	 * 获取导出表头的后面各个类型
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuOrderGoodsAmontName(Map<String, Object> paramMap)throws SQLException;
	





}
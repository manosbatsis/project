package com.topideal.mapper.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.storage.TakesStockResultItemModel;

/**
 * 盘点结果详情表
 * @author zhanghx
 */
@MyBatisRepository
public interface TakesStockResultItemMapper extends BaseMapper<TakesStockResultItemModel> {

	/**
	 * 获取盘点结果盘盈数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getTakesStockNumBySurplus(Map<String, Object> map) throws SQLException;
	
	/**
	 * 获取盘点结果盘亏数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getTakesStockNumByDeficient(Map<String, Object> map) throws SQLException;
	
	/**
	 * 根据盘点结果ids 获取获取商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(@Param("list") List list)throws SQLException;

	/**
	 * 事业部财务报表-获取盘点结果盘盈数量
	 * @param map
	 */
	List<Map<String, Object>> getBuTakesStockSurplus(Map<String, Object> map);

	
	/**
	 * 事业部财务报表-获取盘点结果盘亏数量
	 * @param map
	 */
	List<Map<String, Object>> getBuTakesStockDeficient(Map<String, Object> map);

	/**
	 * 获取(事业部财务经销存)盘盈盘亏明细表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getBuFinanceTakesStockResults(Map<String, Object> map)throws SQLException;
	
}

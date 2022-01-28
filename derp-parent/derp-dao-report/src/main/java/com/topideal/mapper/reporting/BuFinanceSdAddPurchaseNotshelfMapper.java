package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceSdAddPurchaseNotshelfModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceSdAddPurchaseNotshelfMapper extends BaseMapper<BuFinanceSdAddPurchaseNotshelfModel> {

	
	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计SD采购在途明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceSdAddPurchaseNotshelf(Map<String, Object> map) throws SQLException;
	/**
	 * 获取(事业部财务经销存)采购在途SD明细 导出数据
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuFinanceSdAddPurchaseNotshelf(Map<String, Object> paramMap)throws SQLException;
	/**
	 *  获取导出表头的后面各个类型
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuOrderGoodsAmontName(Map<String, Object> paramMap)throws SQLException;
	/**
	 * 查询本期按商家 订单货号 简称分组的总金额
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuOrderGoodsAmont(Map<String, Object> paramMap)throws SQLException;

}

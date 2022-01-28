package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceAddPurchaseNotshelfDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceAddPurchaseNotshelfDetailsMapper extends BaseMapper<BuFinanceAddPurchaseNotshelfDetailsModel> {
	/**
	 * 清除商家 仓库 月份 (事业部财务经销存)累计采购在途明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceAddPurchaseNotshelfDetails(Map<String, Object> map) throws SQLException;
	
	/**
	 * 本期采购在途
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceAddPurchaseNotshelfDetailsModel>getFinanceSaleNotshelfList(Map<String, Object> map)throws SQLException;

	/**
	 * 总账导出 获取财务经销存本期在途
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getAllAccountFinNoshelf(Map<String, Object> map)throws SQLException;
	/**
	 * 累计采购采购在途导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceAddPurchaseNotshelfDetailsModel>getAddPurchaseNotshelfExport(Map<String, Object> map) throws SQLException;


}

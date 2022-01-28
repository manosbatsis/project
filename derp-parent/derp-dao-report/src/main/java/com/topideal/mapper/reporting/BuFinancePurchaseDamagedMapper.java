package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinancePurchaseDamagedModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinancePurchaseDamagedMapper extends BaseMapper<BuFinancePurchaseDamagedModel> {

	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)采购残损明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinancePurchaseDamaged(Map<String, Object> map) throws SQLException;

	/**
	 * 获取采购残损明细导出
	 */
	List<BuFinancePurchaseDamagedModel>getPurchaseDamagedExport(Map<String, Object> map)throws SQLException;
}

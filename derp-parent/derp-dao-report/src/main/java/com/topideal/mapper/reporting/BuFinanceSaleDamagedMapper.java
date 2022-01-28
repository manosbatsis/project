package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceSaleDamagedModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceSaleDamagedMapper extends BaseMapper<BuFinanceSaleDamagedModel> {


	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)销售残损明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceSaleDamaged(Map<String, Object> map) throws SQLException;
	/**
	 * 获取销售残损明细导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceSaleDamagedModel>getsaleDamagedExport(Map<String, Object> map)throws SQLException;

}

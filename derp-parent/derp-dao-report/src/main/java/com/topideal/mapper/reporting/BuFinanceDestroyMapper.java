package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceDestroyModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceDestroyMapper extends BaseMapper<BuFinanceDestroyModel> {

	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)销毁明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceDestroy(Map<String, Object> map) throws SQLException;

	/**
	 * 获取销毁明细导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceDestroyModel>getDestroyExport(Map<String, Object> map)throws SQLException;
}

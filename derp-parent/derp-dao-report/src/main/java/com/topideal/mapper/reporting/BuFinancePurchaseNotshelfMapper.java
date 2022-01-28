package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinancePurchaseNotshelfModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinancePurchaseNotshelfMapper extends BaseMapper<BuFinancePurchaseNotshelfModel> {

	/**
	 * 清除事业部商家 仓库 月份 (财务经销存)采购在途数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinancePurchaseNotshelf(Map<String, Object> map) throws SQLException;


}

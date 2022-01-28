package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuFinanceDecreasePurchaseNotshelfModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuFinanceDecreasePurchaseNotshelfMapper extends BaseMapper<BuFinanceDecreasePurchaseNotshelfModel> {

	/**
	 * 清除商家事业部 仓库 月份 (财务经销存)本期减少采购在途表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuFinanceDecreasePurchaseNotshelf(Map<String, Object> map) throws SQLException;
	/**
	 * 累计采购在途导出
	 * @param model
	 * @return
	 */
	List<BuFinanceDecreasePurchaseNotshelfModel>getDecreasePurchaseNotshelfExport(BuFinanceDecreasePurchaseNotshelfModel model);


}

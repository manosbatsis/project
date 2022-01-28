package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuFinanceDecreasePurchaseNotshelfModel;


public interface BuFinanceDecreasePurchaseNotshelfDao extends BaseDao<BuFinanceDecreasePurchaseNotshelfModel> {
		

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

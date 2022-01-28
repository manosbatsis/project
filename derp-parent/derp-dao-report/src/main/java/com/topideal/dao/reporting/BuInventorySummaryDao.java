package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.BuInventorySummaryDTO;
import com.topideal.entity.vo.reporting.BuInventorySummaryModel;


public interface BuInventorySummaryDao extends BaseDao<BuInventorySummaryModel> {
		

	/**
	 * 清空事业部商家、仓库、本月的报表数据
	 */
	public int delBuDepotMonthReport(Map<String, Object> map);

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	BuInventorySummaryDTO getListByPage(BuInventorySummaryDTO model) throws SQLException;


	/**
	 * 查询仓库月汇总报表
	 */
	public List<Map<String, Object>> listBuDepotMonth(Map<String, Object>map);


	/**
	 * 根据公司和报表月份获取业务进销存报表、事业部业务进销存报表、财务进销存报表、事业部财务进销存报表的所有标准条码
	 */
	List<String> commbarcodeListByMonth(Map<String, Object> params);

	/**
	 * 以公司+月份+仓库+标准条码维度统计报表
	 */
	List<Map<String, Object>> listCommbarcodeMonth(Map<String, Object> params);

	/**
	 * 月结自动校验根据条码统计
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getMonthlyAutoVeriListGroupByBarcode(Map<String, Object> params);

	/**
	 * 在库天数统计汇总商品在所有公司主体下保税仓和海外仓的期末正常品库存量
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getInWarehouseSumAccount(Map<String, Object> queryMap);
	
	/**
	 * 查询上月是否有 事业部业务经销存
	 * @param model
	 * @return
	 */
	public int getLastMonthSummaryCount(BuInventorySummaryModel model);
	/**
	 * 查询之前月是否有业务经销存
	 * @param model
	 * @return
	 */
	public int getBeforLastMonthSummaryCount(BuInventorySummaryModel model);

	/**
	 * 在库天数库存统计导出
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getInWarehouseExport(Map<String, Object> map);
}

package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.entity.vo.reporting.BuFinanceInventorySummaryModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface BuFinanceInventorySummaryDao extends BaseDao<BuFinanceInventorySummaryModel> {
	
	/**
	 * 获取事业部财务经销存 和商品标准条码
	 * @param model
	 * @return
	 */
	List<BuFinanceInventorySummaryModel> getSummaryList(BuFinanceInventorySummaryModel model);
	
	/**
	 * 清空事业部商家、仓库、本月的报表数据
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuDepotMonthReport(Map<String, Object> map) throws SQLException;
	/**
	 * 查询商家、仓库、月份报表的状态
	 * */
	String getSummaryStatus(Map<String, Object> map);

	/**
	 * 查询事业部 商家、仓库、上月货号的期末结存数量、调整标准成本单价、调整期末结存金额
	 * */
	Map<String, Object> getBuGoodsNoSummary(Map<String, Object> map);	

	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	BuFinanceInventorySummaryDTO getListByPage(BuFinanceInventorySummaryDTO model);
	/**
	 * 导出
	 * @param map
	 */
	List<Map<String, Object>> getBuListForMap(Map<String, Object> paramMap);
	/**
	 * 事业部 导出-查询本期所有仓库
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuDepotListForMap(Map<String, Object> paramMap);
	

	
	/**
	 * 事业部 导出-查询本期所有仓库商品库存
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuDepotEndNumForMap(Map<String, Object> paramMap);
	
	/**
	 * 事业部 导出-查询本期所有仓库组码库存
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuDepotEndNumForMapA(Map<String, Object> paramMap);
	
	

	/**
	 * 根据组码分组查询财务经销存总表  事业部财务进销存标准条码汇总
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> getBuListForGroupCommbarcodeMap(Map<String, Object> paramMap);

	/**
	 * 以公司+月份+仓库+标准条码维度统计报表
	 */
	List<Map<String, Object>> listCommbarcodeMonth(Map<String, Object> params);
	
	/**
	 * 关帐页面分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	 BuFinanceInventorySummaryDTO getListDescByPage(BuFinanceInventorySummaryDTO model) throws SQLException;
	 
	/**
	 * 改变状态为关帐
	 * @throws SQLException
	 */
	public int closeReport(BuFinanceInventorySummaryModel model) throws SQLException;

	/**
	 * 反关帐
	 * @throws SQLException
	 */
	public int updateNotClose(BuFinanceInventorySummaryModel model) throws SQLException;

	/**
	 * 根据商家、关账状态、关账归属月份统计未关账数量
	 * @Param
	 * @return
	 */
	Integer countBeforeMonthList(Map<String, Object> paramMap) throws SQLException;
	/**
	 * 根据商家、关账状态、关账归属月份统计关账数量
	 * @Param
	 * @return
	 */
	Integer countAftrerMonthList(Map<String, Object> paramMap) throws SQLException;

	/**
	 * 累计汇总表分页
	 * @param model
	 * @return
	 */
	BuFinanceInventorySummaryDTO getListAddByPage(BuFinanceInventorySummaryDTO model)throws SQLException;
	/**
	 * 累计汇总导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BuFinanceInventorySummaryDTO> getListAddExport(BuFinanceInventorySummaryDTO model) throws SQLException;
	/**
	 * 美赞FG负库存金额
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> getFgInventByGroupCommbar(Map<String, Object> map);

	/**
	 * 统计汇总事业部财务经销存总表
	 * @return
	 */
	List<Map<String, Object>> getMaxCloseAccountMerchantBu()throws SQLException;
	/**
	 * 查询年度采购结算金额、结算金额(销售洞察)
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getAmountYear(BuFinanceInventorySummaryDTO dto, String isParentBrand, List<Long> brandIds);
	/**
	 * 查询年度品牌采购结算金额(销售洞察)
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getBrandPurchaseAmountYear(BuFinanceInventorySummaryDTO dto, String isParentBrand, List<Long> brandIds);
	/**
	 * 查询年度品牌采购结算金额(销售洞察)
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getBrandList(@Param("dto") BuFinanceInventorySummaryDTO dto, @Param("isParentBrand") String isParentBrand);
	/**
	 * (销售洞察)年度进销存导出
	 * @param dto
	 * @return
	 */
	List<BuFinanceInventorySummaryDTO> getYearFinanceInventoryAnalysisExportList(BuFinanceInventorySummaryDTO dto, String isParentBrand, List<Long> brandIds);
	/**
	 * 查询商家、仓库、事业部、上月货号的期末结存数量、调整标准成本单价、调整期末结存金额
	 * */
	Map<String, Object> getGoodsNoSummary(Map<String, Object> map);
}

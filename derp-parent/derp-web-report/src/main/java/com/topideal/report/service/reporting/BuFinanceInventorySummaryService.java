package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.BuFinanceInventorySummaryDTO;
import com.topideal.mongo.entity.FileTaskMongo;

/**
 * 新财务报表
 */
public interface BuFinanceInventorySummaryService {
	/**
	 * 分页
	 */

	public BuFinanceInventorySummaryDTO getListByPage(User user,BuFinanceInventorySummaryDTO model);

    /**
     * 生成商家、月份的excel文件报表
     */
    public String createExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 生成商家、月份的excel sd财务经销存文件报表
     */
    public String createSdExcel(FileTaskMongo task, String basePath) throws Exception;
	
	/**
	 * 生成商家、月份的excel 事业部财务利息经销存
	 * */
	public String createInterestTask(FileTaskMongo task, String basePath) throws Exception;
	/**
	 * 生成商家、月份的excel 美赞成本差异导出
	 * */
	public String createCostDifferenceTask(FileTaskMongo task, String basePath) throws Exception;

	/**
     * 生成 商家、月份的excel 事业部财务总账
     */
    public String createAllAccountExcel(FileTaskMongo task, String basePath) throws Exception;
	
	/**
	 * 关帐分页
	 */
	public BuFinanceInventorySummaryDTO getListDesc(BuFinanceInventorySummaryDTO model, User user) throws SQLException;
	/**
	 * 关账
	 * @param month
	 * @param merchantId
	 * @param erchantName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> closeReport(String month, Long buId, User user) throws Exception;
	
	/**
	 * 修改为未关账
	 * @param month
	 * @param merchantId
	 * @param erchantName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateNotClose(String month, Long buId, User user) throws Exception;
	
	
	
	
	/**
	 * 累计汇总表分页
	 */
	public BuFinanceInventorySummaryDTO getListAddByPage(User user,BuFinanceInventorySummaryDTO model) throws SQLException;

    /**
     * 累计汇总导出
     *
     * @param model
     * @return
     * @throws SQLException
     */
    public List<BuFinanceInventorySummaryDTO> getListAddExport(User user,BuFinanceInventorySummaryDTO model) throws SQLException;

    /**
     * 暂估应收导出
     */
    public String createTempEstimatePdf(FileTaskMongo task, String basePath) throws Exception;
	
	/**
	 * 获取是否关账
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getSummaryStatus(Map<String, Object> map)throws Exception;
}

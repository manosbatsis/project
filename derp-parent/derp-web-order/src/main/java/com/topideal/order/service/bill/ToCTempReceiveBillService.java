package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.APIResponse;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.TocTemporaryCostBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillItemDTO;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.api.bean.dto.QueryTocTempReceiveBillDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * To C暂估应收表service
 **/
public interface ToCTempReceiveBillService {

    /**
     * 获取收入分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TocTemporaryReceiveBillDTO listTocTempReceiveBillByPage(User user, TocTemporaryReceiveBillDTO dto) throws SQLException;

    /**
     * 获取费用分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TocTemporaryCostBillDTO listTocTempCostReceiveBillByPage(User user,TocTemporaryCostBillDTO dto) throws SQLException;

    List<MerchantShopRelMongo> getMerchantShopRelList(Long merchantId) throws Exception;

    /**
     * 获取收入详情
     * @param id
     * @return
     */
    TocTemporaryReceiveBillDTO getDetails(Long id) throws SQLException;

    /**
     * 获取费用详情
     * @param id
     * @return
     */
    TocTemporaryCostBillDTO getCostDetails(Long id) throws SQLException;

    /**
     * 获取明细分页
     * @param dto
     * @throws SQLException
     */
    TocTemporaryReceiveBillItemDTO listToCTempReceiveItem(User user, TocTemporaryReceiveBillItemDTO dto) throws Exception;

    /**
     * 获取费用明细分页
     * @param dto
     * @throws SQLException
     */
    TocTemporaryReceiveBillCostItemDTO listToCTempReceiveCostItem(User user, TocTemporaryReceiveBillCostItemDTO dto) throws Exception;

    int countTempBillNum(User user,TocTemporaryReceiveBillItemDTO dto) throws SQLException;

    int countTempCostBillNum(User user,TocTemporaryReceiveBillCostItemDTO dto) throws SQLException;

    /**
     * 根据条件查询导出暂估收入
     */
    List<TocTemporaryReceiveBillDTO> listForExport(TocTemporaryReceiveBillDTO dto, User user) throws Exception;

    /**
     * 根据条件查询导出暂估费用
     */
    List<TocTemporaryCostBillDTO> listCostForExport(TocTemporaryCostBillDTO dto, User user) throws Exception;

    /**
     * 导出截至当前时间，表头状态为“部分结算”和“未结算”，表体电商订单为“未结算”的应收明细数据；
     */
    List<Map<String, Object>> listForExportTempItem(User user,TocTemporaryReceiveBillItemDTO dto)throws SQLException;

    /**
     * 导出截至当前时间，表头状态为“部分结算”和“未结算”，表体电商订单为“未结算”的费用明细数据；
     */
    List<Map<String, Object>> listForExportTempCostItem(User user,TocTemporaryReceiveBillCostItemDTO dto)throws SQLException;

    /**
     * 刷新
     */
    Map<String, Object> refreshBills(String ids, String type) throws Exception;

    /**
     * 暂估收入汇总导出
     * */
    List<ExportExcelSheet> exportSummaryBill(TocTemporaryReceiveBillDTO dto, User user) throws Exception;

    /**
     * 暂估费用汇总导出 (添加母品牌维度)
     * */
    List<ExportExcelSheet> exportCostSummaryBill(TocTemporaryCostBillDTO dto, User user) throws Exception;

    /**
     * 暂估收入完结
     */
    boolean endReceiveBill(Long id, String isEndPunch, String type) throws Exception;

    /**
     * 统计完结发货订单与退款订单正负红冲，待核销金额=0和≠0的数量
     */
    Map<String, Integer> endReceiveBillNum(Long id, String type) throws Exception;

    /**
     * 生成toc收入导出明细excel
     */
    public String createItemExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 生成toc费用导出明细excel
     */
    public String createCostExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 生成toc收入累计暂估导出excel
     */
    public String createTempItemExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 生成toc费用累计暂估导出excel
     */
    public String createTempCostExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 查询Toc暂估信息返回给外部接口
     * @param dto
     */
    APIResponse queryTocTempReceiveBill(QueryTocTempReceiveBillDTO dto) throws Exception;

    /**
     * 生成toc暂估收入汇总导出
     * @param task
     * @param basePath
     * @return
     */
    String createTempReceiveSummaryExcel(FileTaskMongo task, String basePath) throws Exception;

    /**
     * 生成toc暂估费用汇总导出
     * @param task
     * @param basePath
     * @return
     */
    String createTempCostSummaryExcel(FileTaskMongo task, String basePath) throws Exception;
}

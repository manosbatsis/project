package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillRebateItemDTO;
import com.topideal.entity.dto.bill.TocTemporaryReceiveBillDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * tob暂估核销service
 **/
public interface TobTemporaryReceiveBillService {

    /**
     * 分页查询tob收款跟踪表数据
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillDTO listToBTempBillTrackByPage(TobTemporaryReceiveBillDTO dto, User user) throws Exception;

    /**
     * 根据条件查询导出tob核销跟踪表
     */
    List<Map<String, Object>> listForExportToBTrack(TobTemporaryReceiveBillDTO dto, User user);

    /**
     * 分页查询tob暂估核销表数据
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillDTO listToBTempBillVerifyByPage(TobTemporaryReceiveBillDTO dto, User user) throws Exception;

    /**
     * 获取详情
     * @param id
     * @return
     */
    TobTemporaryReceiveBillDTO getDetails(Long id);

    /**
     * 分页查询tob应收核销明细
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillItemDTO listToBTempItemByPage(TobTemporaryReceiveBillItemDTO dto) throws Exception;

    /**
     * 分页查询tob应收核销返利明细
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillRebateItemDTO listToBTempRebateItemByPage(TobTemporaryReceiveBillRebateItemDTO dto) throws Exception;

    /**
     * 统计导出To B暂估明细数量
     * @param dto
     * @return
     * @throws SQLException
     */
    Integer getTempDetailsCount(TobTemporaryReceiveBillDTO dto) throws Exception;

    /**
     * 导出To B暂估收入明细
     * @param dto
     * @return
     * @throws SQLException
     */
    Map<String, List<Map<String, Object>>> exportTempDetails(TobTemporaryReceiveBillDTO dto, User user) throws Exception;

    /**
     * 导出To B暂估费用明细
     * @param dto
     * @return
     * @throws SQLException
     */
    Map<String, List<Map<String, Object>>> exportTempRebateDetails(TobTemporaryReceiveBillDTO dto, User user) throws Exception;


    /**
     * 完结核销
     * @param ids
     * @param type
     * @return
     * @throws SQLException
     */
    void endTobTemporaryReceiveBill(String ids, String type) throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws SQLException
     */
    void batchDelReceiveBill(String ids) throws Exception;
}

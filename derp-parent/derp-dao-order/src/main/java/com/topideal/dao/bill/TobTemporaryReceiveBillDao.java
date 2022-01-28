package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface TobTemporaryReceiveBillDao extends BaseDao<TobTemporaryReceiveBillModel> {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillDTO listToBTempBillByPage(TobTemporaryReceiveBillDTO dto) throws SQLException;

    /**
     * 根据条件查询导出
     * @param dto
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillDTO> listForExport(TobTemporaryReceiveBillDTO dto);

    /**
     * 批量更新tob暂估
     * @param models
     * @return
     * @throws SQLException
     */
    void batchUpdate(List<TobTemporaryReceiveBillModel> models);

    TobTemporaryReceiveBillDTO searchDTOById(Long id);

    /**
     * 根据ids查询
     */
    List<TobTemporaryReceiveBillDTO> listBillByRelIds(List<Long> ids);

    /**
     * 应收账龄报告（商家+事业部+客户+币种）
     * @param searchQueryMap
     * @return
     */
    List<TobTemporaryReceiveBillDTO> listBySearchQuery(Map<String, Object> searchQueryMap);


    /**
     * 根据（商家+事业部+客户+币种）的维度获取暂估的未核销金额
     * @param searchQueryMap
     * @return
     */
    List<Map<String,Object>> getItemBySearch(Map<String, Object> searchQueryMap);

    /**
     * 据（商家+事业部+客户+币种）的维度获取暂估费用的未核销金额
     * @param searchQueryMap
     * @return
     */
    Map<String, BigDecimal> getTocTemprayCostBillDTO(Map<String, Object> searchQueryMap);

    /**
     * 根据条件获取tob暂估表头
     * @param receiveBillDTO
     * @return
     */
    List<TobTemporaryReceiveBillDTO> listByDto(TobTemporaryReceiveBillDTO receiveBillDTO);

    /**
     * 根据上架单号查询tob暂估
     * @param shelfCodes
     * @param isWriteOff
     * @param merchantId
     * @return
     */
    List<TobTemporaryReceiveBillDTO> listByShelfCodes(List<String> shelfCodes, String isWriteOff, Long merchantId);

    /**
     * 批量更新tob暂估状态
     * @param ids
     * @param status
     * @return
     * @throws SQLException
     */
    void batchUpdateStatus(List<Long> ids, String status, String rebateStatus);
}

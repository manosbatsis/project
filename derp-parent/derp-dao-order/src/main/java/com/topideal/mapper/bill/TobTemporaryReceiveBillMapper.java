package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.ReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TobTemporaryReceiveBillMapper extends BaseMapper<TobTemporaryReceiveBillModel> {


    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<TobTemporaryReceiveBillDTO> listToBTempBillByPage(TobTemporaryReceiveBillDTO dto) throws SQLException;

    /**
     * 根据条件查询导出
     * @param dto
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillDTO> listForExport(TobTemporaryReceiveBillDTO dto) ;

    /**
     * 批量更新tob暂估
     * @param models
     * @return
     * @throws SQLException
     */
    void batchUpdate(@Param("models") List<TobTemporaryReceiveBillModel> models);

    TobTemporaryReceiveBillDTO searchDTOById(@Param("id") Long id);

    /**
     * 根据ids查询
     */
    List<TobTemporaryReceiveBillDTO> listBillByRelIds(@Param("ids") List<Long> ids);

    /**
     * 应收账龄报告（商家+事业部+客户+币种）
     * @param searchQueryMap
     * @return
     */
    List<TobTemporaryReceiveBillDTO> listBySearchQuery(Map<String, Object> searchQueryMap);

    /**
     * 根据（商家+事业部+客户+币种）的维度获取暂估收入的未核销金额表体信息
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
    List<TobTemporaryReceiveBillDTO> listByShelfCodes(@Param("shelfCodes")List<String> shelfCodes, @Param("isWriteOff") String isWriteOff, @Param("merchantId") Long merchantId);

    /**
     * 批量更新tob暂估状态
     * @param ids
     * @param status
     * @return
     * @throws SQLException
     */
    void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status, @Param("rebateStatus") String rebateStatus);
}

package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillCostItemMonthlyDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillRebateItemDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillRebateItemModel;
import com.topideal.mapper.BaseMapper;
import com.topideal.webService.B;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TobTemporaryReceiveBillRebateItemMapper extends BaseMapper<TobTemporaryReceiveBillRebateItemModel> {

    /**
     * 批量插入tob暂估核销返利明细
     * */
    Integer batchSave(List<TobTemporaryReceiveBillRebateItemModel> list) throws SQLException;

    /**
     * 根据tob暂估表头id删除返利明细
     * */
    int delItemsByBillIds(@Param("billIds") List<Long> billIds) throws SQLException;

    /**
     * 统计tob暂估核销明细的应收金额
     */
    BigDecimal listRebateItemPriceByIds(TobTemporaryReceiveBillCostItemMonthlyDTO costItemMonthlyDTO) throws SQLException;

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
    PageDataModel<TobTemporaryReceiveBillRebateItemDTO> listToBTempRebateItemByPage(TobTemporaryReceiveBillRebateItemDTO dto) throws SQLException;

    /**
     * 统计导出To B暂估返利明细数量
     * @param dto
     * @return
     * @throws SQLException
     */
    Integer getRebateTempDetailsCount(TobTemporaryReceiveBillDTO dto) throws Exception;

    /**
     * 获取未核销的tob暂估核销返利
     * @param dto
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillRebateItemDTO> getVerifyRebateItems(TobTemporaryReceiveBillDTO dto) throws SQLException;

    /**
     * 获取指定tob暂估的暂估费用
     * @param billIds
     * @return
     * @throws SQLException
     */
    List<TobTemporaryReceiveBillRebateItemModel> listByBillIds(@Param("billIds") List<Long> billIds) throws Exception;

    /**
     * 根据公司、事业部、刷新月份查询询返利结算状态为“已上架未结算”/“部分结算”且上架时间小于等于快照月份的应收暂估返利明细
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws Exception
     */
    List<TobTemporaryReceiveBillRebateItemModel> listNonVerifyByCloseAccount(@Param("merchantId") Long merchantId, @Param("buId") Long buId, @Param("month") String month) throws Exception;

    /**
     * 根据公司、事业部、刷新月份查询返利结算状态为“已结算”且上架时间小于等于快照月份、核销记录中核销月份有大于等于快照月份的应收暂估返利明细
     * @param merchantId
     * @param buId
     * @param month
     * @return
     * @throws Exception
     */
    List<TobTemporaryReceiveBillRebateItemModel> listAllVerifyByCloseAccount(@Param("merchantId") Long merchantId, @Param("buId") Long buId, @Param("month") String month) throws Exception;

    /**
     * 根据公司、是否红冲单、指定sd单号的应收暂估返利明细
     * @param merchantId
     * @param sdCodes
     * @param isWriteOff
     * @return
     * @throws Exception
     */
    List<TobTemporaryReceiveBillRebateItemModel> getWriteOffNonVerifyItems(@Param("merchantId") Long merchantId, @Param("sdCodes")List<String> sdCodes,
                                                                           @Param("isWriteOff") String isWriteOff) throws Exception;
    /**
     * 已核销过的sd单单号
     * @param merchantId
     * @return
     * @throws Exception
     */
    List<String> getVerifySdCodes(@Param("merchantId") Long merchantId) throws Exception;

}

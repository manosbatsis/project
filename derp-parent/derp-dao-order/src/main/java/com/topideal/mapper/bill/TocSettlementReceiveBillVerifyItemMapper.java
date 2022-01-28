package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface TocSettlementReceiveBillVerifyItemMapper extends BaseMapper<TocSettlementReceiveBillVerifyItemModel> {


    double getAllPriceByBillIds(@Param("idList") List<Long> idList);

    /**
     * 批量保存核销明细
     **/
    Integer batchSave(List<TocSettlementReceiveBillVerifyItemModel> list) throws SQLException;

    /**
     * 获取最新收款日期
     **/
    Timestamp getLatestReceiveDate(@Param("billId") Long billId) throws SQLException;

    /**
     * 查询toc应收账单收款核销记录中核销月份小于等于月结月份的已核销金额
     * @param billIds 应收账单id集合
     * @param month 月结月份
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getVerifyAmountByBillIds(@Param("billIds") List<Long> billIds, @Param("month") String month) throws SQLException;
}

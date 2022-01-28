package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface TocSettlementReceiveBillVerifyItemDao extends BaseDao<TocSettlementReceiveBillVerifyItemModel> {


    BigDecimal getAllPriceByBillIds(List<Long> idList);


    /**
     * 批量保存核销明细
     **/
    Integer batchSave(List<TocSettlementReceiveBillVerifyItemModel> list) throws SQLException;

    /**
     * 获取最新收款日期
     **/
    Timestamp getLatestReceiveDate(Long billId) throws SQLException;

    /**
     * 查询toc应收账单收款核销记录中核销月份小于等于月结月份的已核销金额
     * @param billIds 应收账单id集合
     * @param month 月结月份
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getVerifyAmountByBillIds(List<Long> billIds, String month) throws SQLException;
}

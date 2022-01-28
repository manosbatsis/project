package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.sql.Timestamp;


@MyBatisRepository
public interface TocSettlementReceiveBillAuditItemMapper extends BaseMapper<TocSettlementReceiveBillAuditItemModel> {


    int updateAuditItem(TocSettlementReceiveBillAuditItemModel model) throws SQLException;

    /**
     * 获取应收账单的最后一次审核时间
     */
    Timestamp getMaxAuditDate(Long billId) throws SQLException;

}

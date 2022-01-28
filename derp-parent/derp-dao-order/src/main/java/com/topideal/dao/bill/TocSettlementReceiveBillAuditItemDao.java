package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


public interface TocSettlementReceiveBillAuditItemDao extends BaseDao<TocSettlementReceiveBillAuditItemModel> {


    int updateAuditItem(TocSettlementReceiveBillAuditItemModel model) throws SQLException;

    /**
     * 获取应收账单的最后一次审核时间
     */
    Timestamp getMaxAuditDate(Long billId) throws SQLException;






}

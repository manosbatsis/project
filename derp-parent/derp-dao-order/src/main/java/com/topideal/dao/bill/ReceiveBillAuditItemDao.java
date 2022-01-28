package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.bill.ReceiveBillAuditItemModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


public interface ReceiveBillAuditItemDao extends BaseDao<ReceiveBillAuditItemModel>{

    int updateAuditItem(ReceiveBillAuditItemModel model) throws SQLException;


    /**
     * 获取应收账单开票的最大审核时间
     */
    Timestamp getMaxAuditDate(List<Long> billIds) throws SQLException;

    /**
     * 获取应收账单开票的最后一条提交记录
     */
    ReceiveBillAuditItemModel getLastAuditRecord(Long billId);



}

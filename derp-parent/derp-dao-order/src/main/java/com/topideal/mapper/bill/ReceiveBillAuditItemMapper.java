package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.bill.ReceiveBillAuditItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;


@MyBatisRepository
public interface ReceiveBillAuditItemMapper extends BaseMapper<ReceiveBillAuditItemModel> {

    int updateAuditItem(ReceiveBillAuditItemModel model) throws SQLException;

    /**
     * 获取应收账单开票的最大审核时间
     */
    Timestamp getMaxAuditDate(@Param("billIds") List<Long> billIds) throws SQLException;

    ReceiveBillAuditItemModel getLastAuditRecord(@Param("billId") Long billId);
}

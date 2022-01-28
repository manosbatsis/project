package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.BillMonthlySnapshotDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BillMonthlySnapshotService {

    /**
     * 分页查询
     * @param dto
     * @return
     * @throws SQLException
     */
    BillMonthlySnapshotDTO listBillMonthlySnapshotByPage(BillMonthlySnapshotDTO dto, User user) throws Exception;

    /**
     * 根据页面查询条件导出
     * @param dto
     * @return
     */
    List<Map<String, Object>> listForExport(BillMonthlySnapshotDTO dto, User user) throws Exception;
}

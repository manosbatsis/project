package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillItemMonthlyModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ToBTempRevenueMonthlySnapshotService {

    /**
     * 分页查询
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillItemMonthlyDTO listToBTempRevenueMonthlyByPage(TobTemporaryReceiveBillItemMonthlyDTO dto, User user) throws Exception;

    /**
     * 导出To B暂估收入明细
     * @param dto
     * @return
     * @throws SQLException
     */
    Map<String, List<Map<String, Object>>> exportToBTempRevenueMonthlySnapshot(TobTemporaryReceiveBillItemMonthlyDTO dto, User user) throws Exception;


}

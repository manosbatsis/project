package com.topideal.order.service.bill;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillCostItemMonthlyDTO;
import com.topideal.entity.dto.bill.TobTemporaryReceiveBillItemMonthlyDTO;
import com.topideal.entity.vo.bill.TobTemporaryReceiveBillCostItemMonthlyModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ToBTempCostMonthlySnapshotService {

    /**
     * 分页查询
     * @param dto
     * @return
     * @throws SQLException
     */
    TobTemporaryReceiveBillCostItemMonthlyDTO listToBTempCostMonthlyByPage(TobTemporaryReceiveBillCostItemMonthlyDTO dto, User user) throws Exception;

    /**
     * 导出To B暂估费用明细
     * @param dto
     * @return
     * @throws SQLException
     */
    Map<String, List<Map<String, Object>>> exportToBTempCostMonthlySnapshot(TobTemporaryReceiveBillCostItemMonthlyDTO dto, User user) throws Exception;

}

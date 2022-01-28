package com.topideal.service.main;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.main.CostPriceDifferenceDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * 成本单价差异service
 */
public interface CostPriceDifferenceService {

    /**
     * 列表（分页）
     * @param dto
     * @return
     * @throws SQLException
     */
    CostPriceDifferenceDTO listCostPriceDifferenceDTO(CostPriceDifferenceDTO dto, User user) throws SQLException;

    /**
     * 获取导出列表
     * @param dto
     * @return
     */
    List<CostPriceDifferenceDTO> getExportMainList(CostPriceDifferenceDTO dto, User user) throws SQLException;
}

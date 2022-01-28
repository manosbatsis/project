package com.topideal.report.service.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.WeightedPriceDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * 加权单价 service
 */
public interface WeightedPriceService {

    /**
     * 分页DTO
     * @param dto
     * @return
     */
    WeightedPriceDTO listPriceDTO(User user,WeightedPriceDTO dto) throws SQLException;

    /**
     * 根据页面查询条件导出
     */
    List<WeightedPriceDTO> listForExport(User user,WeightedPriceDTO dto) throws SQLException;


}

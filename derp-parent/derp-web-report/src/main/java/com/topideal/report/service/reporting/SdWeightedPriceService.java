package com.topideal.report.service.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SdWeightedPriceDTO;

import java.sql.SQLException;
import java.util.List;

public interface SdWeightedPriceService {
    /**
     * 分页DTO
     * @param dto
     * @return
     */
    SdWeightedPriceDTO listPriceDTO(User user,SdWeightedPriceDTO dto) throws SQLException;

    List<SdWeightedPriceDTO> listForExport(User user,SdWeightedPriceDTO dto) throws SQLException;
}

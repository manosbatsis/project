package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SdInterestPriceDTO;

public interface SdInterestPriceService {
    /**
     * 分页DTO
     * @param dto
     * @return
     */
    SdInterestPriceDTO listPriceDTO(User user,SdInterestPriceDTO dto) throws SQLException;

    List<SdInterestPriceDTO> listForExport(User user,SdInterestPriceDTO dto) throws SQLException;
}

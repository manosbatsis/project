package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.OccupationCapitalStatisticsDTO;

import java.util.List;

public interface OccupationCapitalStatisticsService {
    /**
     * 查询列表信息 分页
     * @param dto
     * @return
     */
    OccupationCapitalStatisticsDTO listDTOByPage(OccupationCapitalStatisticsDTO dto, User user) throws Exception;
    /**
     * 获取导出数量
     * @param dto
     * @throws Exception
     */
    Integer getOrderCount(OccupationCapitalStatisticsDTO dto, User user) throws Exception;

    /**
     * 导出
     * @param dto
     * @throws Exception
     */
    List<OccupationCapitalStatisticsDTO> listDTO(OccupationCapitalStatisticsDTO dto, User user) throws Exception;
}

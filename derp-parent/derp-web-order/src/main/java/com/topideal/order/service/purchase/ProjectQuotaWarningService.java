package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningExportDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningItemDTO;

import java.sql.SQLException;
import java.util.List;

public interface ProjectQuotaWarningService {

    /**
     * 分页
     * @param dto
     * @param user
     * @return
     */
    ProjectQuotaWarningDTO getListByPage(ProjectQuotaWarningDTO dto, User user);

    /**
     * 根据ID获取详情
     * @param id
     * @return
     */
    ProjectQuotaWarningDTO getProjectQuotaWarningById(Long id) throws SQLException;

    /**
     * 明细分页
     * @param dto
     * @param user
     * @return
     */
    ProjectQuotaWarningItemDTO getItemListByPage(ProjectQuotaWarningItemDTO dto, User user);

    /**
     * 获取导出
     * @param dto
     * @return
     */
    List<ProjectQuotaWarningDTO> exportProjectQuotaWarning(ProjectQuotaWarningDTO dto);

    List<ProjectQuotaWarningExportDTO> exportProjectQuotaWarningDetail(ProjectQuotaWarningItemDTO dto);
}

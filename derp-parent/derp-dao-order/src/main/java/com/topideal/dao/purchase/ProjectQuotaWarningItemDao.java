package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningItemDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningItemModel;

import java.util.List;


public interface ProjectQuotaWarningItemDao extends BaseDao<ProjectQuotaWarningItemModel> {


    ProjectQuotaWarningItemDTO getItemListByPage(ProjectQuotaWarningItemDTO dto);

    List<ProjectQuotaWarningItemDTO> exportProjectQuotaWarningDetail(ProjectQuotaWarningItemDTO dto);
}

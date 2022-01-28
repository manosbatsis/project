package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningModel;

import java.util.List;


public interface ProjectQuotaWarningDao extends BaseDao<ProjectQuotaWarningModel> {


    ProjectQuotaWarningDTO getListByPage(ProjectQuotaWarningDTO dto);

    List<ProjectQuotaWarningDTO> exportProjectQuotaWarning(ProjectQuotaWarningDTO dto);
}

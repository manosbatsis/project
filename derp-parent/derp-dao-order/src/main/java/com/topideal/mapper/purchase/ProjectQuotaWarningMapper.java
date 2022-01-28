package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.ProjectQuotaWarningDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaWarningModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface ProjectQuotaWarningMapper extends BaseMapper<ProjectQuotaWarningModel> {


    PageDataModel<ProjectQuotaWarningDTO> getListByPage(ProjectQuotaWarningDTO dto);

    List<ProjectQuotaWarningDTO> exportProjectQuotaWarning(ProjectQuotaWarningDTO dto);
}

package com.topideal.mapper.purchase;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaConfigModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface ProjectQuotaConfigMapper extends BaseMapper<ProjectQuotaConfigModel> {


    PageDataModel<ProjectQuotaConfigDTO> getListByPage(ProjectQuotaConfigDTO dto);

    List<ProjectQuotaConfigModel> getLatestConfigList(Map<String, Object> queryMap);

	List<ProjectQuotaConfigModel> getCoincidenceTimeList(ProjectQuotaConfigModel queryProjectQuatoConfigModel);

	List<ProjectQuotaConfigModel> getInScopeTimeList(ProjectQuotaConfigModel queryProjectQuatoConfigModel);
}

package com.topideal.dao.purchase;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;
import com.topideal.entity.vo.purchase.ProjectQuotaConfigModel;

import java.util.List;
import java.util.Map;


public interface ProjectQuotaConfigDao extends BaseDao<ProjectQuotaConfigModel> {


    /**
     * 分页
     * @param dto
     * @return
     */
    ProjectQuotaConfigDTO getListByPage(ProjectQuotaConfigDTO dto);

    /**
     * 获取最近生效项目额度配置
     * @param queryMap
     * @return
     */
    List<ProjectQuotaConfigModel> getLatestConfigList(Map<String, Object> queryMap);

    /**
     * 获取时间范围内配置
     * @param queryProjectQuatoConfigModel
     * @return
     */
	List<ProjectQuotaConfigModel> getCoincidenceTimeList(ProjectQuotaConfigModel queryProjectQuatoConfigModel);

	/**
	 * 符合时间范围配置
	 * @param queryProjectQuatoConfigModel
	 * @return
	 */
	List<ProjectQuotaConfigModel> getInScopeTimeList(ProjectQuotaConfigModel queryProjectQuatoConfigModel);
}

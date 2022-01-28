package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.DepartmentQuatoDTO;
import com.topideal.entity.dto.purchase.ProjectQuotaConfigDTO;

import java.sql.SQLException;

public interface ProjectQuotaConfigService {

    /**
     * 获取分页
     * @param dto
     * @param user
     * @return
     */
    ProjectQuotaConfigDTO getListByPage(ProjectQuotaConfigDTO dto, User user);

    /**
     * 新增编辑
     * @param dto
     * @param user
     * @return
     */
    Long saveOrUpdateProjectQuotaConfig(ProjectQuotaConfigDTO dto, User user) throws SQLException;

    /**
     * 审核
     * @param id
     * @param user
     */
    void auditProjectQuotaConfig(Long id, User user) throws SQLException;

    /**
     *删除
     * @param ids
     */
    void delProjectQuotaConfig(String ids) throws SQLException;

    /**
     * 获取详情
     * @param id
     */
    ProjectQuotaConfigDTO getProjectQuotaConfigById(Long id) throws SQLException;

    /**
     * 获取最新已审核期初账期信息
     * @param user
     * @param buId
     * @param superiorParentBrandId
     * @return
     */
    ProjectQuotaConfigDTO getLatestPeriodInfo(User user, Long buId, Long superiorParentBrandId);

    /**
     * 获取最新已审核部门额度配置信息
     * @param user
     * @param buId
     * @return
     */
	DepartmentQuatoDTO getLatestDepartmentQuato(User user, Long buId);

	/**
	 * 更新额度
	 * @param dto
	 * @param user
	 * @throws SQLException 
	 */
	void updateProjectQuota(ProjectQuotaConfigDTO dto, User user) throws SQLException;
}

package com.topideal.order.service.common;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.common.QuotaPeriodConfigDTO;

public interface QuotaPeriodConfigService {

	QuotaPeriodConfigDTO getListByPage(QuotaPeriodConfigDTO dto, User user);

	Long saveOrUpdateQuotaPeriodConfig(QuotaPeriodConfigDTO dto, User user) throws Exception;

	QuotaPeriodConfigDTO getQuotaPeriodConfigById(Long id) throws SQLException;

	void auditQuotaPeriodConfig(Long id, User user) throws SQLException;

	List<SelectBean> getQuotaSelectBeanByBuId(Long buId) throws SQLException;

}

package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.CustomerQuotaConfigDTO;
import com.topideal.entity.vo.sale.CustomerQuotaConfigModel;

public interface CustomerQuotaConfigService {

	/**
	 * 获取分页数据
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	CustomerQuotaConfigDTO listDTOByPage(CustomerQuotaConfigDTO dto ,User user) throws Exception;
	/**
	 * 调整客户额度
	 * @param dto
	 * @param user
	 * @throws Exception
	 */
	void updateCustomerQuota(CustomerQuotaConfigModel model, User user) throws Exception;
	/**
	 * 保存
	 * @param dto
	 * @throws Exception
	 */
	void saveCustomerQuotaConfigDTO(CustomerQuotaConfigDTO dto, User user) throws Exception;
	/**
	 * 审核
	 * @param dto
	 * @throws Exception
	 */
	void auditCustomerQuotaConfigDTO(CustomerQuotaConfigDTO dto, User user) throws Exception;
	/**
	 * 删除
	 * @param ids
	 * @throws Exception
	 */
	void delCustomerQuotaConfigDTO(String ids) throws Exception;
	
	/**
	 * 事业部+客户 查询历史配置记录，默认带出最后一个已审核记录
	 */
	//CustomerQuotaConfigDTO getLastestCustomerQuotaConfig(Long buId,Long customerId) throws Exception;
	
}

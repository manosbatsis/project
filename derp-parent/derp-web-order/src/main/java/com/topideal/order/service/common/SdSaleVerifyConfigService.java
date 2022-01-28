package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.SdSaleVerifyConfigDTO;

public interface SdSaleVerifyConfigService {
	/**
	 * 获取列表信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public SdSaleVerifyConfigDTO listDTOByPage(SdSaleVerifyConfigDTO dto, User user) throws Exception;
	/**
	 * 获取详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SdSaleVerifyConfigDTO searchDetail(Long id) throws Exception;
	/**
	 * 保存、审核
	 * @param dto
	 * @param user
	 * @param operate
	 * @throws Exception
	 */
	public void saveOrAudit(SdSaleVerifyConfigDTO dto,User user,String operate) throws Exception;
	/**
	 * 更新状态
	 * @param id
	 * @param user
	 * @param status
	 * @throws Exception
	 */
	public void modifyStatus(Long id, User user , String status) throws Exception;
	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void delVerifyConfig(String ids) throws Exception;
	

}

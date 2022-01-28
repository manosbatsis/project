package com.topideal.service.main;


import com.topideal.entity.dto.main.EmailConfigDTO;
import com.topideal.entity.vo.main.EmailConfigModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送配置信息service
 */
public interface EmailConfigService {
	
	/**
	 * 邮件发送配置信息（分页）
	 * @param model 
	 * @return
	 */
	EmailConfigDTO listEmail(EmailConfigDTO dto) throws SQLException;
	
	/**
	 * 新增邮件发送配置信息
	 * @param model
	 * @return
	 */
	Map<String, Object> saveEmail(EmailConfigModel model) throws Exception;
	
	/**
	 * 根据id删除邮件发送配置信息(支持批量)
	 * @param ids
	 * @return
	 */
	boolean delEmail(List<Long> ids) throws SQLException;
	
	/**
	 * 修改邮件发送配置信息
	 * @param model
	 * @return
	 */
	Map<String, Object> modifyEmail(EmailConfigModel model) throws Exception;
	
	/**
	 * 根据id获取邮件发送配置信息详情
	 * @param id
	 * @return
	 */
	EmailConfigDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据id禁用和启用
	 * 状态(1启用,0禁用)
	 * @return
	 * @throws SQLException
	 */
	boolean audit(Long id, String status) throws SQLException;
	
	
}

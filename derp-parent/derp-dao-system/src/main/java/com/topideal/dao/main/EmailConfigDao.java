package com.topideal.dao.main;


import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.EmailConfigDTO;
import com.topideal.entity.vo.main.EmailConfigModel;

/**
 * 发送邮件配置dao
 * @author 杨创
 *
 */
public interface EmailConfigDao extends BaseDao<EmailConfigModel> {

	EmailConfigDTO getListByPage(EmailConfigDTO dto);

	EmailConfigDTO searchDTOById(Long id);
		










}

package com.topideal.service.main.impl;

import com.topideal.dao.main.EmailConfigDao;
import com.topideal.entity.dto.main.EmailConfigDTO;
import com.topideal.entity.vo.main.EmailConfigModel;
import com.topideal.service.main.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理service实现类
 */
@Service
public class EmailConfigServiceImpl implements EmailConfigService {

	// 仓库信息dao
	@Autowired
	private EmailConfigDao emailConfigDao;
	
	/**
	 * 邮件发送配置信息（分页）
	 * @param model 
	 * @return
	 */
	@Override
	public EmailConfigDTO listEmail(EmailConfigDTO dto) throws SQLException {
		return emailConfigDao.getListByPage(dto);
	}
	/**
	 * 新增邮件发送配置信息
	 * @param model
	 * @return
	 */
	@Override

	public Map<String, Object> saveEmail(EmailConfigModel model) throws Exception {
		Map<String, Object> retMap=new HashMap<>();
		EmailConfigModel emailConfigModel=new EmailConfigModel();
		emailConfigModel.setMerchantId(model.getMerchantId());
		emailConfigModel.setCustomerId(model.getCustomerId());
		List<EmailConfigModel> list = emailConfigDao.list(emailConfigModel);
		if (list!=null&&list.size()>0) {
			retMap.put("code", "01");
			retMap.put("message", "该商家下的供应商的邮件提醒已经存在");
			return retMap;
		}
		Long save = emailConfigDao.save(model);
		retMap.put("code", "00");
		retMap.put("message", "保存成功");
		return retMap;
	}
	/**
	 * 根据id删除邮件发送配置信息(支持批量)
	 * @param ids
	 * @return
	 */
	@Override

	public boolean delEmail(List<Long> ids) throws SQLException {
		int num = emailConfigDao.delete(ids);
		if (num>0) {
			return true;
		}
		return false;
	}
	/**
	 * 修改邮件发送配置信息
	 * @param model
	 * @return
	 */
	@Override

	public Map<String, Object> modifyEmail(EmailConfigModel model) throws Exception {
		Map<String, Object> retMap=new HashMap<>();
		//根据id查询数据库 
		EmailConfigModel searchById = emailConfigDao.searchById(model.getId());
		// 前端 商家和客户不能编辑 所以后台 不用校验 该商家客户下是否存在邮件发送
		/*if (searchById.getMerchantId().intValue()!=model.getMerchantId().intValue()||searchById.getCustomerId().intValue()!=model.getCustomerId().intValue()) {
			EmailConfigModel emailConfigModel=new EmailConfigModel();
			emailConfigModel.setMerchantId(model.getMerchantId());
			emailConfigModel.setCustomerId(model.getCustomerId());
			List<EmailConfigModel> list = emailConfigDao.list(emailConfigModel);
			if (list!=null&&list.size()>0) {
				retMap.put("code", "01");
				retMap.put("message", "该商家该供应邮件提醒已经存在");
				return retMap;
			}
		}*/
		int modify = emailConfigDao.modify(model);		
		retMap.put("code", "00");
		retMap.put("message", "保存成功");
		return retMap;
	}
	/**
	 * 根据id获取邮件发送配置信息详情
	 * @param id
	 * @return
	 */
	@Override
	public EmailConfigDTO searchDetail(Long id) throws SQLException {
		EmailConfigDTO model = emailConfigDao.searchDTOById(id);
		return model;
	}
	
	/**
	 * 根据id禁用和启用
	 * 状态(1启用,0禁用)
	 * @return
	 */
	@Override

	public boolean audit(Long id ,String status) throws SQLException {
		EmailConfigModel model =new EmailConfigModel();
		model.setId(id);
		model.setStatus(status);
		int num = emailConfigDao.modify(model);
		if (num>0) {
			return true;
		}
		return false;
	}

	
	
}

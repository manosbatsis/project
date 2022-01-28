package com.topideal.service.base.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.dto.base.ApiSecretConfigDTO;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.base.ApiSecretConfigService;

/**
 * 接口配置
 * @author zhanghx
 */
@Service
public class ApiSecretConfigServiceImpl implements ApiSecretConfigService {

	@Autowired
	private ApiSecretConfigDao apiSecretConfigDao;
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	
	/**
	 * 列表（分页）
	 * @param model 
	 * @return
	 */
	@Override
	public ApiSecretConfigDTO getListByPage(ApiSecretConfigDTO dto) throws SQLException {
		return apiSecretConfigDao.getListByPage(dto);
	}

	/**
	 * 新增
	 * @param model 
	 * @return
	 */
	@Override
	
	public boolean saveApiSecret(ApiSecretConfigModel model) throws Exception {
		ApiSecretConfigModel config = new ApiSecretConfigModel();
		config.setMerchantId(model.getMerchantId());
		config = apiSecretConfigDao.searchByModel(config);
		if(config!=null){
			throw new RuntimeException("该商家的接口配置已存在");
		}
		MerchantInfoModel merchant = merchantInfoDao.searchById(model.getMerchantId());
		model.setTopidealCode(merchant.getTopidealCode());//卓志编码
		model.setPlatformName(merchant.getName());//商家名称
		model.setStatus(DERP_SYS.APISECRETCONFIG_STATUS_1);//启用
		Long id = apiSecretConfigDao.save(model);
		if(id != null){
			return true;
		}
		return false;
	}

	/**
	 * 编辑
	 * @param model 
	 * @return
	 */
	@Override
	public boolean modify(ApiSecretConfigModel model) throws SQLException {
		ApiSecretConfigModel query = apiSecretConfigDao.searchById(model.getId());
		
		if (query.getMerchantId().intValue()!=model.getMerchantId().intValue()) {
			ApiSecretConfigModel config = new ApiSecretConfigModel();
			config.setMerchantId(model.getMerchantId());
			config = apiSecretConfigDao.searchByModel(config);
			if(config!=null){
				throw new RuntimeException("该商家的接口配置已存在");
			}
		}
		MerchantInfoModel merchant = merchantInfoDao.searchById(model.getMerchantId());
		model.setTopidealCode(merchant.getTopidealCode());//卓志编码
		model.setPlatformName(merchant.getName());//商家名称	
		
		int num = apiSecretConfigDao.modify(model);
		if(num >= 1){
			return true;
		}
		return false;
	}

	/**
	 * 禁用启用
	 */
	@Override
	public boolean modifyAuditApi(ApiSecretConfigModel model) throws SQLException {
		int num = apiSecretConfigDao.modify(model);
		if(num >= 1){
			return true;
		}
		return false;
	}

	/**
	 * 获取详情
	 * @param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ApiSecretConfigDTO getDetails(Long id) throws SQLException {
		return apiSecretConfigDao.searchDTOById(id);
	}

	/**
	 * 批量删除
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean delApiSecretConfig(List<Long> list) throws SQLException {
		for (Long id : list) {
			ApiSecretConfigModel model = apiSecretConfigDao.searchById(id);
			if (DERP_SYS.APISECRETCONFIG_STATUS_1.equals(model.getStatus())) {
				throw new RuntimeException("只能删除禁用接口配置");
			}
		}
		int num = apiSecretConfigDao.delete(list);
	    if(num >= 1){
	        return true;
	    }
	    return false;
	}


}

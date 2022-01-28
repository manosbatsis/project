package com.topideal.service.base.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.*;
import com.topideal.entity.vo.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.base.ReptileConfigDao;
import com.topideal.entity.dto.base.ReptileConfigDTO;
import com.topideal.entity.vo.base.ReptileConfigModel;
import com.topideal.service.base.ReptileConfigService;

/**
 * 爬虫配置 serviceImpl
 * @author lian_
 */
@Service
public class ReptileConfigServiceImpl implements ReptileConfigService {

	@Autowired
	private ReptileConfigDao dao;
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	@Autowired
	private CustomerInfoDao customerInfoDao;
	@Autowired
	private DepotInfoDao depotInfoDao;
	@Autowired
	private MerchantShopRelDao merchantShopRelDao ;

	/**
	 * 分页查询
	 */
	@Override
	public ReptileConfigDTO getListByPage(ReptileConfigDTO dto) throws SQLException {
		return dao.getListByPage(dto);
	}

	/**
	 * 详情
	 */
	@Override
	public ReptileConfigDTO searchDetail(Long id) throws SQLException {
		return dao.searchDTOById(id);
	}
	
	/**
	 * 新增
	 */
	@Override
	public Map<String,Object> saveReptile(ReptileConfigModel model) throws SQLException {
		Map<String,Object> retMap = new HashMap<String, Object>();
		//检查用户+平台是否已存在
		ReptileConfigModel queryModel = new ReptileConfigModel();
		queryModel.setLoginName(model.getLoginName());
		queryModel.setPlatformType(model.getPlatformType());
		List<ReptileConfigModel> oldList = dao.list(queryModel);
		if(oldList!=null&&oldList.size()>0) {
			retMap.put("code", "99");
			retMap.put("message", "用户名在本平台已存在");
			return retMap;
		}
		
		model.setPlatformName(DERP.getLabelByKey(DERP.crawler_typeList, model.getPlatformType()));
	    //查询商家 客户 出库仓 入仓库 事业部
		MerchantInfoModel merchantModel = merchantInfoDao.searchById(model.getMerchantId());
		CustomerInfoModel customerInfoModel = customerInfoDao.searchById(model.getCustomerId());
		DepotInfoModel outDepot = depotInfoDao.searchById(model.getOutDepotId());
		DepotInfoModel inDepot = depotInfoDao.searchById(model.getInDepotId());

		model.setMerchantName(merchantModel.getName());
		model.setCustomerName(customerInfoModel.getName());
		model.setOutDepotName(outDepot.getName());
		model.setInDepotName(inDepot.getName());
		
		if(model.getShopId() != null) {
			MerchantShopRelModel shop = merchantShopRelDao.searchById(model.getShopId());
			model.setShopCode(shop.getShopCode());
			model.setShopName(shop.getShopName());
		}
		
		model.setCreateDate(TimeUtils.getNow());
		
		dao.save(model);
		
		retMap.put("code", "00");
		retMap.put("message", "成功");
		return retMap;
	}

	/**
	 * 批量删除
	 */
	@Override

	public boolean delReptile(List<Long> list) throws SQLException {
		int num = dao.delete(list);
	    if(num >= 1){
	        return true;
	    }
	    return false;
	}

	/**
	 * 编辑
	 */
	@Override
	public Map<String,Object> modifyReptile(ReptileConfigModel model) throws SQLException {
		Map<String,Object> retMap = new HashMap<String, Object>();
		
		//检查用户+平台是否已存在
		ReptileConfigModel queryModel = new ReptileConfigModel();
		queryModel.setLoginName(model.getLoginName());
		queryModel.setPlatformType(model.getPlatformType());
		List<ReptileConfigModel> oldList = dao.list(queryModel);
		if(oldList!=null&&oldList.size()>0) {
			for(ReptileConfigModel oldModel:oldList) {
				if(!oldModel.getId().equals(model.getId())) {
					retMap.put("code", "99");
					retMap.put("message", "用户名在本平台已存在");
					return retMap;
				}
			}
			
		}
		model.setPlatformName(DERP.getLabelByKey(DERP.crawler_typeList, model.getPlatformType()));
	    //查询商家 客户 出库仓 入仓库 事业部
		MerchantInfoModel merchantModel = merchantInfoDao.searchById(model.getMerchantId());
		CustomerInfoModel customerInfoModel = customerInfoDao.searchById(model.getCustomerId());
		DepotInfoModel outDepot = depotInfoDao.searchById(model.getOutDepotId());
		DepotInfoModel inDepot = depotInfoDao.searchById(model.getInDepotId());

		model.setMerchantName(merchantModel.getName());
		model.setCustomerName(customerInfoModel.getName());
		model.setOutDepotName(outDepot.getName());
		model.setInDepotName(inDepot.getName());
		
		if(model.getShopId() != null) {
			MerchantShopRelModel shop = merchantShopRelDao.searchById(model.getShopId());
			model.setShopCode(shop.getShopCode());
			model.setShopName(shop.getShopName());
		}
		
		dao.modify(model);
		
		retMap.put("code", "00");
		retMap.put("message", "成功");
		return retMap;
	}
	
	/**
	 * 查询客户下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBean(Long merchantId) throws SQLException {
		return dao.getSelectBean(merchantId);
	}

	/**
	 * 查询商家下拉列表
	 */
	@Override
	public List<MerchantInfoModel> getMerchant() throws SQLException {
		return dao.getSelectMerchant();
	}

	@Override
	public List<MerchantShopRelModel> getShopList(Long merchantId) throws SQLException {
		
		MerchantShopRelModel queryModel = new MerchantShopRelModel() ;
		queryModel.setStatus(DERP_SYS.MERCHANTSHOPREL_STATUS_1);
		//queryModel.setDataSourceCode(DERP.DATASOURCE_5);
		
		if(merchantId != null) {
			queryModel.setMerchantId(merchantId);
		}
		
		List<MerchantShopRelModel> shopList = merchantShopRelDao.list(queryModel) ;
		
		return shopList;
	}


	
	
}

package com.topideal.report.service.reporting.impl;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.topideal.dao.system.DepotInfoDao;
import com.topideal.entity.vo.system.DepotInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.report.service.reporting.MerchantInfoService;

import net.sf.json.JSONObject;

/**
 * 公司管理  impl
 */
@Service
public class MerchantInfoServiceImpl implements MerchantInfoService {
	
	/* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantInfoServiceImpl.class);
	@Autowired
	private MerchantInfoDao merchantInfoDao;//公司信息
	@Autowired
	private DepotInfoDao depotInfoDao;
	/**
	 * 公司列表
	 */
	@Override
	public List<MerchantInfoModel> listMerchantInfo(MerchantInfoModel model)
			throws SQLException {
		return merchantInfoDao.list(model);
	}
	@Override
	public List<SelectBean> getSelectBean(MerchantInfoModel model) throws SQLException {
		return merchantInfoDao.getSelectBean(model);
	}

	@Override
	public List<DepotInfoModel> depotListByMerchant(Long merchantId) throws SQLException {
		return depotInfoDao.depotListByMerchant(merchantId);
	}


}

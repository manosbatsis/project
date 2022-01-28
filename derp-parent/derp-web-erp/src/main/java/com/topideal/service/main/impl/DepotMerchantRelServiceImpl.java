package com.topideal.service.main.impl;



import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.main.DepotMerchantRelDao;
import com.topideal.entity.dto.main.DepotMerchantRelDTO;
import com.topideal.entity.vo.main.DepotMerchantRelModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.main.DepotMerchantRelService;

/**
 * 仓库商家关联表 serviceimpl
 * @author lchenxing
 */
@Service
public class DepotMerchantRelServiceImpl implements DepotMerchantRelService {

	@Autowired
	private DepotMerchantRelDao depotMerchantRelDao;
	
	/**
	 * 根据仓库id查询商家列表
	 * @param id
	 * @return
	 */
	@Override
	public List<DepotMerchantRelDTO> getMerchantByDepotId(Long id) throws SQLException {
		return depotMerchantRelDao.getMerchantByDepotId(id);
	}

	@Override
	public DepotMerchantRelDTO getByDepotIdAndMerchantId(Long depotId, Long merchantId) throws SQLException {
		return depotMerchantRelDao.getByDepotIdAndMerchantId(depotId, merchantId);
	}

	@Override
	public List<DepotMerchantRelDTO> getListByMerchantId(Long id) {
		return depotMerchantRelDao.getListByMerchantId(id);
	}



}

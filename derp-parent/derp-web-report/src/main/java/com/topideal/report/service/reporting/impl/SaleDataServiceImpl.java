package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.reporting.SaleDataDao;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.SaleDataService;

/**
 * 销售数据
 *
 */
@Service
public class SaleDataServiceImpl implements SaleDataService{


	@Autowired
	private SaleDataDao saleDataDao;//(财务经销存)采购入库明细
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	
	/**
	 * 累计销售汇总表分页
	 */
	@Override
	public SaleDataDTO getListAddSaleByPage(User user,SaleDataDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return saleDataDao.getListAddSale(dto);
	}

	@Override
	public List<SaleDataDTO> getListAddExport(User user,SaleDataDTO dto) throws SQLException {	
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}
		return saleDataDao.getListAddExport(dto);
	}




}

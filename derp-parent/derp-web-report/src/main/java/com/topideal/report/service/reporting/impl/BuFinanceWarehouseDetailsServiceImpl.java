package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.reporting.BuFinanceWarehouseDetailsDao;
import com.topideal.entity.dto.BuFinanceWarehouseDetailsDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.BuFinanceWarehouseDetailsService;

/**
 * 新财务进销存报表
 *
 */
@Service
public class BuFinanceWarehouseDetailsServiceImpl implements BuFinanceWarehouseDetailsService{


	@Autowired
	private BuFinanceWarehouseDetailsDao buFinanceWarehouseDetailsDao;//(财务经销存)采购入库明细
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	/**
	 * 累计汇总表分页
	 */
	@Override
	public BuFinanceWarehouseDetailsDTO getListAddWarehouseByPage(User user,BuFinanceWarehouseDetailsDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return buFinanceWarehouseDetailsDao.getListAddWarehouse(dto);
	}

	@Override
	public List<BuFinanceWarehouseDetailsDTO> getListAddExport(User user,BuFinanceWarehouseDetailsDTO dto) throws SQLException {	
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}
		return buFinanceWarehouseDetailsDao.getListAddExport(dto);
	}




}

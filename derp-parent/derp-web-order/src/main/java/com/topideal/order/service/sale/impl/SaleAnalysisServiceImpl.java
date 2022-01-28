package com.topideal.order.service.sale.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.sale.SaleAnalysisDao;
import com.topideal.entity.dto.sale.SaleAnalysisDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.order.service.sale.SaleAnalysisService;

/**
 * 销售出库分析service实现类
 */
@Service
public class SaleAnalysisServiceImpl implements SaleAnalysisService {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory
			.getLogger(SaleAnalysisServiceImpl.class);
	// 销售出库分析表
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	@Override
	public SaleAnalysisDTO listSaleAnalysisDTO(SaleAnalysisDTO dto,User user)
			throws SQLException {
		if(dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if(buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
		return saleAnalysisDao.queryDTOListByPage(dto);
	}

}

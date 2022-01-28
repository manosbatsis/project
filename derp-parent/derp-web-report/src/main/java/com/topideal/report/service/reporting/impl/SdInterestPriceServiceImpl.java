package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.reporting.SdInterestPriceDao;
import com.topideal.entity.dto.SdInterestPriceDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.SdInterestPriceService;

/**
 * @Author: Wilson Lau
 * @Date: 2021/8/19 15:52
 * @Description: 利息SD单价
 */

@Service
public class SdInterestPriceServiceImpl implements SdInterestPriceService {

    @Autowired
    private SdInterestPriceDao sdInterestPriceDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	
    @Override
    public SdInterestPriceDTO listPriceDTO(User user,SdInterestPriceDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
        return sdInterestPriceDao.getDtoListByPage(dto);
    }
    @Override
    public List<SdInterestPriceDTO> listForExport(User user,SdInterestPriceDTO dto) throws SQLException {

		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}
		
        return sdInterestPriceDao.listPrice(dto);
    }

}

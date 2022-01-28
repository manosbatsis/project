package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.reporting.SdWeightedPriceDao;
import com.topideal.entity.dto.SdWeightedPriceDTO;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.SdWeightedPriceService;

@Service
public class SdWeightedPriceServiceImpl implements SdWeightedPriceService {

    @Autowired
    private SdWeightedPriceDao sdweightedPriceDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;

    @Override
    public SdWeightedPriceDTO listPriceDTO(User user,SdWeightedPriceDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
        return sdweightedPriceDao.getDtoListByPage(dto);
    }
    @Override
    public List<SdWeightedPriceDTO> listForExport(User user,SdWeightedPriceDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}
        return sdweightedPriceDao.listPrice(dto);
    }

}

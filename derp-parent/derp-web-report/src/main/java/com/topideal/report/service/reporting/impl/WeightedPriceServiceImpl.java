package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.auth.User;
import com.topideal.dao.reporting.WeightedPriceDao;
import com.topideal.entity.dto.WeightedPriceDTO;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.WeightedPriceService;

/**
 * 加权单价service 实现类
 **/
@Service
public class WeightedPriceServiceImpl implements WeightedPriceService {

    @Autowired
    private WeightedPriceDao weightedPriceDao;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 用户事业部
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
    @Override
    public WeightedPriceDTO listPriceDTO(User user,WeightedPriceDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			if (buList.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			dto.setBuList(buList);
		}
        return weightedPriceDao.getDtoListByPage(dto);
    }

    @Override
    public List<WeightedPriceDTO> listForExport(User user,WeightedPriceDTO dto) throws SQLException {
		if (dto.getBuId() == null) {
			List<Long> buList = userBuRelMongoDao.getBuListByUser(user.getId());
			dto.setBuList(buList);
		}
        return weightedPriceDao.listPrice(dto);
    }
}

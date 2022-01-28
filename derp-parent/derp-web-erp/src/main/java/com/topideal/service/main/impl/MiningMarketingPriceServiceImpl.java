package com.topideal.service.main.impl;

import com.topideal.dao.main.MiningMarketingPriceDao;
import com.topideal.entity.dto.main.MiningMarketingPriceDTO;
import com.topideal.service.main.MiningMarketingPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * 采销报价
 * @author zhanghx
 */
@Service
public class MiningMarketingPriceServiceImpl implements MiningMarketingPriceService {

	@Autowired
	private MiningMarketingPriceDao miningMarketingPriceDao;
	
	/**
	 * 分页
	 * @param model 
	 * @return
	 */
	@Override
	public MiningMarketingPriceDTO listByPage(MiningMarketingPriceDTO dto) throws SQLException {
		return miningMarketingPriceDao.getListByPage(dto);
	}

}

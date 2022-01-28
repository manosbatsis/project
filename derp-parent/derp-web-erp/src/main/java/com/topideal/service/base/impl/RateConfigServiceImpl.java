package com.topideal.service.base.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.base.ExchangeRateConfigDao;
import com.topideal.entity.dto.base.ExchangeRateConfigDTO;
import com.topideal.entity.vo.base.ExchangeRateConfigModel;
import com.topideal.service.base.RateConfigService;

@Service
public class RateConfigServiceImpl implements RateConfigService {

	private static final Logger LOGGER= LoggerFactory.getLogger(RateConfigServiceImpl.class);

	@Autowired
	private ExchangeRateConfigDao exchangeRateConfigDao;


	@Override
	public ExchangeRateConfigDTO getListByPage(ExchangeRateConfigDTO dto) throws SQLException {
		return exchangeRateConfigDao.getListByPage(dto);
	}

	@Override
	public boolean saveRate(ExchangeRateConfigModel model) throws SQLException {
		Long save = exchangeRateConfigDao.save(model);
		if (save == null) {
			return false;
		}
		return true;
	}


	@Override
	public boolean delRateConfig(List<Long> list) throws SQLException {
		int num = exchangeRateConfigDao.delete(list);
		if (num <=0) {
			throw new RuntimeException("删除失败！");
		}
		return true;
	}

	//导出
	@Override
	public List<Map<String, Object>> listForExport(ExchangeRateConfigModel dto) throws Exception {
		return exchangeRateConfigDao.listForExport(dto);
	}

	//
	@Override
	public ExchangeRateConfigModel serchByModel(ExchangeRateConfigModel model)throws Exception {
		return exchangeRateConfigDao.searchByModel(model);
	}
	


}

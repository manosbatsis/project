package com.topideal.service.base.impl;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.ExchangeRateConfigDao;
import com.topideal.dao.base.ExchangeRateDao;
import com.topideal.entity.dto.base.ExchangeRateDTO;
import com.topideal.entity.vo.base.ExchangeRateModel;
import com.topideal.service.base.RateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RateServiceImpl implements RateService {

	private static final Logger LOGGER= LoggerFactory.getLogger(RateServiceImpl.class);

	@Autowired
	private ExchangeRateDao exchangeRateDao;
	@Autowired
	private ExchangeRateConfigDao exchangeRateConfigDao;

	@Override
	public ExchangeRateDTO searchDetail(Long id) throws SQLException {
		return exchangeRateDao.searchDTOById(id);
	}

	@Override
	public ExchangeRateDTO getListByPage(ExchangeRateDTO dto) throws SQLException {
		return exchangeRateDao.getListByPage(dto);
	}

	@Override
	public boolean saveRate(ExchangeRateModel model) throws SQLException {
		Long save = exchangeRateDao.save(model);
		if (save == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean modifyRate(ExchangeRateModel model) throws SQLException {
		int modify = exchangeRateDao.modify(model);
		if (modify<=0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean delRate(List<Long> list) throws SQLException {
		int num = 0;
		for (Long id : list) {
			ExchangeRateModel model = exchangeRateDao.searchById(id);
			if (!DERP_SYS.EXCHANGERATE_DATASOURCE_SGCJ.equals(model.getDataSource())) {
				throw new RuntimeException("兑换币种不是手工创建的，不能删除！");
			}

			ExchangeRateModel delModel = new ExchangeRateModel();
			delModel.setId(id);
			delModel.setStatus(DERP.DEL_CODE_006);
			delModel.setModifyDate(TimeUtils.getNow());
			int modify = exchangeRateDao.modify(delModel);
			if (modify>0) {
				num++;
			}
		}
		if (num != list.size()) {
			throw new RuntimeException("删除失败！");
		}
		return true;
	}
	
	@Override
	public boolean have(ExchangeRateModel model , Long id) throws SQLException {
		ExchangeRateModel rateModel = new ExchangeRateModel();
		rateModel.setOrigCurrencyCode(model.getOrigCurrencyCode());
		rateModel.setTgtCurrencyCode(model.getTgtCurrencyCode());
		rateModel.setEffectiveDate(model.getEffectiveDate());
		if (id != null) {
			rateModel.setId(id);
		}
		List<ExchangeRateModel> list = exchangeRateDao.list(rateModel);
		if (list.size()>0) {
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> listForExport(ExchangeRateDTO dto) throws Exception {
		ExchangeRateModel model = new ExchangeRateModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StringUtils.isNotBlank(dto.getEffectiveDateStr())) {
			Date date = sdf.parse(dto.getEffectiveDateStr());
			model.setEffectiveDate(date);
		}
		model.setOrigCurrencyCode(dto.getOrigCurrencyCode());
		model.setTgtCurrencyCode(dto.getTgtCurrencyCode());
		model.setDataSource(dto.getDataSource());
		List<ExchangeRateModel> list = exchangeRateDao.list(model);
		List<Map<String, Object>> result = new ArrayList<>();
		for (ExchangeRateModel exchangeRateModel : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("origCurrencyName", exchangeRateModel.getOrigCurrencyName());
			map.put("tgtCurrencyName", exchangeRateModel.getTgtCurrencyName());
			map.put("rate", exchangeRateModel.getRate());
			map.put("avgRate", exchangeRateModel.getAvgRate());
			map.put("rateDate", sdf.format(exchangeRateModel.getEffectiveDate()));
			map.put("createTime", exchangeRateModel.getCreateDate());
			map.put("dataSource", DERP_SYS.getLabelByKey(DERP_SYS.exchangeRate_dataSourceList, exchangeRateModel.getDataSource()));
			result.add(map);
		}
		return result;
	}

}

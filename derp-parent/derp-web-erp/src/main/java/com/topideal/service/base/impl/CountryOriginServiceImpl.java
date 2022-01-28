package com.topideal.service.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.service.base.CountryOriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 原产国表   serviceImpl
 * @author zhanghx
 */
@Service
public class CountryOriginServiceImpl implements CountryOriginService {

	@Autowired
	private CountryOriginDao countryOriginDao;
	
	/**
	 * 查询原产国下拉列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return countryOriginDao.getSelectBean();
	}

	@Override
	public CountryOriginModel getDetails(Long id) throws SQLException {
		return countryOriginDao.searchById(id);
	}

}

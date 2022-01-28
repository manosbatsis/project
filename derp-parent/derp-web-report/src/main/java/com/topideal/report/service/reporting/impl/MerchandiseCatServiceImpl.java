package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.system.MerchandiseCatDao;
import com.topideal.report.service.reporting.MerchandiseCatService;

/**
 * 商品分类表   serviceImpl
 * @author zhanghx
 */
@Service
public class MerchandiseCatServiceImpl implements MerchandiseCatService {

	//商品分类dao
	@Autowired
	private MerchandiseCatDao merchandiseCatDao;
	
	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return merchandiseCatDao.getSelectBean();
	}

}

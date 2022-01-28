package com.topideal.service.base.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.service.base.UnitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 标准单位 serviceImpl
 * @author zhanghx
 */
@Service
public class UnitInfoServiceImpl implements UnitInfoService {

	//标准单位dao
	@Autowired
	private UnitInfoDao unitInfoDao;
	
	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return unitInfoDao.getSelectBean();
	}

	@Override
	public UnitInfoModel getDetails(Long id) throws SQLException {
		return unitInfoDao.searchById(id);
	}

}

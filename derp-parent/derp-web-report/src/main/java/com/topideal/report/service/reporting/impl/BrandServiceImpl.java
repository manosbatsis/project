package com.topideal.report.service.reporting.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.system.BrandDao;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.report.service.reporting.BrandService;

@Service
public class BrandServiceImpl implements BrandService{

	@Autowired
	private BrandDao brandDao;
	
	/* 
	 * 查询品牌表下拉列表
	 */
	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return brandDao.getSelectBean();
	}

	@Override
	public List<BrandModel> getListBrandInfo() throws SQLException {
		List<BrandModel> listBrandModel = brandDao.getListBrandInfo();
/*		for(BrandModel brand: listBrandModel) {
			if(brand.getParentId() != null && brand.getParentName() != null) {
				brand.setName(brand.getParentName());
			}
		}*/
		return listBrandModel;
	}

	
	
}

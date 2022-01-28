package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.BrandModel;

/**
 * 品牌表 service
 * 
 * @author longcheng.mao
 */
public interface BrandService {

	/**
	 * 查询品牌表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;


	public List<BrandModel> getListBrandInfo() throws SQLException;
}

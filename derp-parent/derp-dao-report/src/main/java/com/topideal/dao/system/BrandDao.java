package com.topideal.dao.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.BrandModel;

/**
 * 品牌表
 * 
 */
public interface BrandDao extends BaseDao<BrandModel> {

	/**
	 * 查询品牌表下拉列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;
	
	
	/**
	 * 查询品牌下拉列表，包括父类
	 * @return
	 * @throws SQLException
	 */
	public List<BrandModel> getListBrandInfo() throws SQLException;

}

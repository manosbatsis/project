package com.topideal.service.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.base.CountryOriginModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 原产国表   service
 * @author zhanghx
 */
public interface CountryOriginService {

	/**
	 * 查询原产国下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;
	
	/**
	 * 原产国
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	CountryOriginModel getDetails(Long id) throws SQLException;
}

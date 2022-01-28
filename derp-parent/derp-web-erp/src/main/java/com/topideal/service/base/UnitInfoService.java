package com.topideal.service.base;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.base.UnitInfoModel;

/**
 * 标准单位service
 * @author zhanghx
 */
public interface UnitInfoService {

	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;
	
	/**
	 * 获取单位
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	UnitInfoModel getDetails(Long id) throws SQLException;
	
}

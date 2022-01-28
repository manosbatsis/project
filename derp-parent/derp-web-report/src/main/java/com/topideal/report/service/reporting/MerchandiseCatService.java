package com.topideal.report.service.reporting;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;

/**
 * 商品分类表   service
 * @author zhanghx
 */
public interface MerchandiseCatService {

	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;
}

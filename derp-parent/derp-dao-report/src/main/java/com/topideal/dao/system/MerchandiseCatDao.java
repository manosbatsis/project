package com.topideal.dao.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.MerchandiseCatModel;

/**
 * 商品分类表
 * @author lchenxing
 *
 */
public interface MerchandiseCatDao extends BaseDao<MerchandiseCatModel> {

	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;

}


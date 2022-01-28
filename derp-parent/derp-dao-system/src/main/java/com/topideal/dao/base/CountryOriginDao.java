package com.topideal.dao.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.base.CountryOriginModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 原产国表  Dao
 * @author lchenxing
 *
 */
public interface CountryOriginDao extends BaseDao<CountryOriginModel> {

	/**
	 * 查询原产国下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;

	/**
	 * 模糊查询
	 * @param countryOriginModel
	 * @return
	 */
    List<CountryOriginModel> listByLike(CountryOriginModel countryOriginModel);
}


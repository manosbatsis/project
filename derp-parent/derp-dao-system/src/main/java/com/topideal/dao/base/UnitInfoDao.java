package com.topideal.dao.base;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.base.UnitInfoModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 单位表   Dao
 * @author lchenxing
 */
public interface UnitInfoDao extends BaseDao<UnitInfoModel> {

	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean() throws SQLException;

	/**
	 * 模糊查询
	 * @param unitInfoModel
	 * @return
	 */
    List<UnitInfoModel> listByLike(UnitInfoModel unitInfoModel);
}


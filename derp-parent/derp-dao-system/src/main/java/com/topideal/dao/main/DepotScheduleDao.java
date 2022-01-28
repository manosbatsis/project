package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.DepotScheduleModel;

/**
 * 仓库附表  dao
 * @author lian_
 *
 */
public interface DepotScheduleDao extends BaseDao<DepotScheduleModel>{
		

	/**
	 * 根据仓库id获取仓库附表下拉框
	 * */
	List<SelectBean> loadSelectByDepotId(DepotScheduleModel model) throws SQLException;

}

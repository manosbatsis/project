package com.topideal.service.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.main.DepotScheduleModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 仓库附表管理service实现类
 */
public interface DepotScheduleService {
	
	
	/**
	 * 根据仓库id查询仓库附表下拉框
	 * */
	List<SelectBean> loadSelectByDepotId(DepotScheduleModel model) throws SQLException;

}

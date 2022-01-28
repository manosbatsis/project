package com.topideal.service.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.main.DepotScheduleDao;
import com.topideal.entity.vo.main.DepotScheduleModel;
import com.topideal.service.main.DepotScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * 仓库管理service实现类
 */
@Service
public class DepotScheduleServiceImpl implements DepotScheduleService {

	// 仓库信息dao
	@Autowired
	private DepotScheduleDao depotScheduleDao;


	/**
	 * 根据仓库id查询仓库附表下拉框
	 */
	@Override
	public List<SelectBean> loadSelectByDepotId(DepotScheduleModel model) throws SQLException {
		return depotScheduleDao.loadSelectByDepotId(model);
	}



}

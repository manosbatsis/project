package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.MonthlyAccountSnapshotDao;
import com.topideal.entity.dto.MonthlyAccountSnapshotDTO;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;
import com.topideal.inventory.service.MonthlyAccountSnapshotService;

/**
 * 库存-月结库存快照
 * @author 联想302
 *
 */
@Service
public class MonthlyAccountSnapshotServiceImpl implements MonthlyAccountSnapshotService {

	@Autowired
	private MonthlyAccountSnapshotDao  monthlyAccountSnapshotDao;
	
	@Override
	public MonthlyAccountSnapshotDTO listMonthlyAccountSnapshotModel(MonthlyAccountSnapshotDTO model)
			throws SQLException {
		// TODO Auto-generated method stub
		return monthlyAccountSnapshotDao.getlistDTOByPage(model);
	}

	@Override
	public List<Map<String, Object>> exportMonthlyAccountSnapshotModelMap(MonthlyAccountSnapshotModel model)
			throws Exception {
		// TODO Auto-generated method stub
		return monthlyAccountSnapshotDao.exportMonthlyAccountSnapshotModelMap(model);
	}

}

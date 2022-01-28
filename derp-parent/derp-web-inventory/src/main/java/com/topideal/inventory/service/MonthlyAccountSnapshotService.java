package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.MonthlyAccountSnapshotDTO;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;
/**
 * 库存管理 -月结库存快照-service实现类
 */
public interface MonthlyAccountSnapshotService {
	
	/**
	 * 月结库存快照列表（分页）
	 * @param model 
	 * @return
	 */
	MonthlyAccountSnapshotDTO listMonthlyAccountSnapshotModel(MonthlyAccountSnapshotDTO model) throws SQLException;
	
	
	   /**
     * 导出月结库存快照
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> exportMonthlyAccountSnapshotModelMap(MonthlyAccountSnapshotModel model) throws Exception;
	
}

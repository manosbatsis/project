package com.topideal.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.MonthlyAccountSnapshotDTO;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;

/**
 * 月结账单快照 dao
 * @author lian_
 */
public interface MonthlyAccountSnapshotDao extends BaseDao<MonthlyAccountSnapshotModel>{
		



	/**
	 *  分页查询库存商品快照表
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MonthlyAccountSnapshotModel getlistByPage(MonthlyAccountSnapshotModel model) throws SQLException;
	
	/**
	 *  分页查询库存商品快照表DTO
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	MonthlyAccountSnapshotDTO getlistDTOByPage(MonthlyAccountSnapshotDTO model) throws SQLException;





	   /**
  * 导出月结库存快照
  * @param id
  * @return
  * @throws Exception
  */
	List<Map<String,Object>> exportMonthlyAccountSnapshotModelMap(MonthlyAccountSnapshotModel model) throws Exception;


}


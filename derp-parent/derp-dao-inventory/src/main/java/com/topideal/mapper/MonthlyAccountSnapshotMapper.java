package com.topideal.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.MonthlyAccountSnapshotDTO;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;

/**
 * 月结账单快照 mapper
 * @author lian_
 */
@MyBatisRepository
public interface MonthlyAccountSnapshotMapper extends BaseMapper<MonthlyAccountSnapshotModel> {
	
	/**
     * 查询所有数据
     * @return
     */
    PageDataModel<MonthlyAccountSnapshotDTO> listDTOByPage(MonthlyAccountSnapshotDTO t)throws SQLException;

	   /**
  * 导出月结库存快照
  * @param id
  * @return
  * @throws Exception
  */
	List<Map<String,Object>> exportMonthlyAccountSnapshotModelMap(MonthlyAccountSnapshotModel model) throws Exception;

}


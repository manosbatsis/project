package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.BuLocationSummaryDTO;
import com.topideal.entity.vo.reporting.BuLocationSummaryModel;


public interface BuLocationSummaryDao extends BaseDao<BuLocationSummaryModel> {


    BuLocationSummaryDTO listDTOByPage(BuLocationSummaryDTO dto);
	/**
	 * 删除(事业部业务经销存)事业部库位经销存总报表
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuLocationSummary(Map<String, Object> map) throws SQLException;
	
	
	/**
	 *事业部库位经销存总报表期初
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getLocationSummaryInit(Map<String, Object> map);
	/**
	 * 业务经销存 库位进销存汇总表 导出
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLocationSummaryListForMap(Map<String, Object> map);

}

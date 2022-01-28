package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuBusinessInBadDetailsModel;


public interface BuBusinessInBadDetailsDao extends BaseDao<BuBusinessInBadDetailsModel> {
		


	/**
	 *删除(事业部业务经销存)来货残次
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuBusinessInBad(Map<String, Object> map) throws SQLException;

	/**
	 * 查询商家、仓库、月份(事业部业务经销存)来货残次(导出)
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> listInBadDetailsMap(Map<String, Object> map);





}

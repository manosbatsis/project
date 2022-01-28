package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuBusinessOutBadDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuBusinessOutBadDetailsMapper extends BaseMapper<BuBusinessOutBadDetailsModel> {

	/**
	 *删除 (事业部业务经销存)出库残次
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuBusinessOutBad(Map<String, Object> map) throws SQLException;
	/**
	 * 查询商家、仓库、月份(事业部业务经销存)出库残次(导出)
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> listOutBadDetailsMap(Map<String, Object> map);

}

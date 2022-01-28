package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuBusinessInBadDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuBusinessInBadDetailsMapper extends BaseMapper<BuBusinessInBadDetailsModel> {

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

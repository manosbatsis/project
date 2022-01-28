package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuBusinessAddTransferNoshelfDetailsModel;
import com.topideal.mapper.BaseMapper;

@MyBatisRepository
public interface BuBusinessAddTransferNoshelfDetailsMapper
		extends BaseMapper<BuBusinessAddTransferNoshelfDetailsModel> {

	/**
	 * 删除(事业部业务经销存)累计调拨在途明细表
	 * 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuBusinessAddTransferNoshelf(Map<String, Object> map) throws SQLException;

	/**
	 * 查询事业部商家、仓库、月份(事业部业务经销存)累计本调拨在途(导出)
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> listBuAddTransferNoshelfDetailsMap(Map<String, Object> map);

	/**
	 * 查询商家、仓库、月份(事业部业务经销存)本调拨在途(导出)
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> listBuTransferNoshelfDetailsMap(Map<String, Object> map);

	/**
	 * 在库天数查询累计调拨在途
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getInWarehouseSumTransferAccount(Map<String, Object> queryMap);

}

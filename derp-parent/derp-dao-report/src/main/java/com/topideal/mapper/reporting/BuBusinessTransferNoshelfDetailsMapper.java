package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuBusinessTransferNoshelfDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuBusinessTransferNoshelfDetailsMapper extends BaseMapper<BuBusinessTransferNoshelfDetailsModel> {
	/**
	 * 删除(事业部业务经销存)调拨在途明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuBusinessTransferNoshelf(Map<String, Object> map) throws SQLException;
	/**
	 * 查询商家、仓库、月份(事业部业务经销存)本调拨在途(导出)
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> listBuTransferNoshelfDetailsMap(Map<String, Object> map);



}

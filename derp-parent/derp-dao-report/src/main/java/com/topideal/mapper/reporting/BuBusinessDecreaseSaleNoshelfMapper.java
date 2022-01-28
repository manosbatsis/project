package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuBusinessDecreaseSaleNoshelfModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuBusinessDecreaseSaleNoshelfMapper extends BaseMapper<BuBusinessDecreaseSaleNoshelfModel> {

	/**
	 * 清除商家 仓库 月份 (事业部业务经分销)本期减少销售在途明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuBusinessDecreaseSaleNoshelf(Map<String, Object> map) throws SQLException;
	/**
	 * 查询商家、仓库、月份(事业部业务经销存)本期减少在途明细表 (导出)
	 * @param map
	 * @return
	 */
    List<Map<String, Object>> listBuDecreaseSaleNoshelfMap(Map<String, Object> map);

}

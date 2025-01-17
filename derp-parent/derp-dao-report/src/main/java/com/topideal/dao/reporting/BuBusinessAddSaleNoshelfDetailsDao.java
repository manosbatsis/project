package com.topideal.dao.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuBusinessAddSaleNoshelfDetailsModel;


public interface BuBusinessAddSaleNoshelfDetailsDao extends BaseDao<BuBusinessAddSaleNoshelfDetailsModel> {
		

	/**
	 * 清除商家 仓库 月份 删除 (事业部业务经销存)累计销售在途明细表
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	int delBuBusinessAddSaleNoshelfDetails(Map<String, Object> map) throws SQLException;

	/**
	 * 查询商家、仓库、月份(事业部业务经销存)累计销售在途明细表 (导出)
	 * @param map
	 * @return
	 */
    public List<Map<String, Object>> listBuAddSaleNoshelfDetailsMap(Map<String, Object> map);
    
	 //销售未确认导出
	 public List<Map<String, Object>> listBuSaleNoshelfDetailsMap(Map<String, Object> map);






}

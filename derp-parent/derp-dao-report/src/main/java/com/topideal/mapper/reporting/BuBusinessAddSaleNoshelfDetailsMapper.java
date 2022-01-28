package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuBusinessAddSaleNoshelfDetailsModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuBusinessAddSaleNoshelfDetailsMapper extends BaseMapper<BuBusinessAddSaleNoshelfDetailsModel> {


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

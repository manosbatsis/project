package com.topideal.mapper.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.reporting.BuPurchaseNotshelfInfoModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuPurchaseNotshelfInfoMapper extends BaseMapper<BuPurchaseNotshelfInfoModel> {

	/**
	 * 清空事业部商家、仓库、本月的报表数据
	 * @param map
	 * @return
	 */
	int delBuDepotMonthReport(Map<String, Object> map);
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
  	int insertBuBatch(List<BuPurchaseNotshelfInfoModel> list);  
  	
  	/**
 	 * 采购未上架导出
 	 * */
 	public List<Map<String, Object>> listBuForMap(Map<String, Object> map);
 	
 	/**
 	 * 累计采购未上架导出
 	 * */
 	public List<Map<String, Object>> getPurchaseAddForMap(Map<String, Object> map);
 	

}

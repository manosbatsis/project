package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuPurchaseNotshelfInfoModel;


public interface BuPurchaseNotshelfInfoDao extends BaseDao<BuPurchaseNotshelfInfoModel> {
		


	   /**
	  	 * 清空事业部商家、仓库、本月的报表数据
	  	 * 
	  	 * @return
	  	 */
	  	public int delBuDepotMonthReport(Map<String, Object> map);


	  	/**
	  	 * 批量插入
	  	 * @param list
	  	 * @return
	  	 */
	  	public int insertBuBatch(List<BuPurchaseNotshelfInfoModel> list);   
	  	/**
	 	 * 采购未上架导出
	 	 * */
	 	public List<Map<String, Object>> listBuForMap(Map<String, Object> map);

	 	public List<Map<String, Object>> getPurchaseAddForMap(Map<String, Object> map);
	 	

}

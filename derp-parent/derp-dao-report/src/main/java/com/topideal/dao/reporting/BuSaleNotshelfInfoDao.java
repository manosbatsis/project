package com.topideal.dao.reporting;

import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.reporting.BuSaleNotshelfInfoModel;


public interface BuSaleNotshelfInfoDao extends BaseDao<BuSaleNotshelfInfoModel> {
		
	 /**
	  * 清空事业部商家、仓库、本月的报表数据 
	  * @param map
	  * @return
	  */
	 public int delBuDepotMonthReport(Map<String, Object> map);

		/**
	    *批量插入
	    * */
	 public int insertBuBatch(List<BuSaleNotshelfInfoModel> list);


	 //销售未确认导出
	 public List<Map<String, Object>> listBuForMap(Map<String, Object> map);




}

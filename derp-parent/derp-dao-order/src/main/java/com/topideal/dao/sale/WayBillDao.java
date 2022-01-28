package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.WayBillModel;

/**
 * 运单表 dao
 * @author lian_
 *
 */
public interface WayBillDao extends BaseDao<WayBillModel>{
		




	/**
	 * 根据运单号查询运单表
	 * @param listWayBillNo
	 * @return
	 * @throws SQLException
	 */
	List<WayBillModel> selectByWayBillNo(List listWayBillNo,Long merchantId)throws SQLException;
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);




}


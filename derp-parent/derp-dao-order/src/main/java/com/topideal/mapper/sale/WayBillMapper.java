package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.sale.WayBillModel;
import com.topideal.mapper.BaseMapper;

/**
 * 运单表 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface WayBillMapper extends BaseMapper<WayBillModel> {
	
	/**
	 * 根据运单号查询运单表
	 * @param listWayBillNo
	 * @return
	 * @throws SQLException
	 */
	List<WayBillModel> selectByWayBillNo(@Param("list")List list,@Param("merchantId")Long merchantId)throws SQLException;
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);


}


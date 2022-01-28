package com.topideal.mapper.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.WayBillItemDTO;
import com.topideal.entity.vo.order.WayBillItemModel;

/**
 * 运单表体  mapper
 * @author lian_
 */
@MyBatisRepository
public interface WayBillItemMapper extends BaseMapper<WayBillItemModel> {

	/**
	 * 根据电商订单ids 获取获取运单信息和商品信息
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<Map<String,Object>> getList(@Param("list") List list)throws SQLException;
	/**
	 * 根据电商订单id获取运单表体信息
	 * @param id
	 * @return
	 */
	WayBillItemModel getWayBillItemByOrder(Map<String, Object> map);
	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String, Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String, Object> map);
}

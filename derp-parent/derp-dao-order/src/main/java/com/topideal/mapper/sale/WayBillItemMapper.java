package com.topideal.mapper.sale;

import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.WayBillItemDTO;
import com.topideal.entity.vo.sale.WayBillItemModel;
import com.topideal.mapper.BaseMapper;

/**
 * 运单表体 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface WayBillItemMapper extends BaseMapper<WayBillItemModel> {


	/**
	 * 根据电商订单id获取运单表体信息
	 * @param id
	 * @return
	 */
	List<WayBillItemDTO> listWayBillItemByOrderId(Long id);

	/**
	 * 迁移数据到历史表
	 * */
	int synsMoveToHistory(Map<String,Object> map);
	/**
	 * 删除已迁移到历史表的数据
	 * */
	int delMoveToHistory(Map<String,Object> map);
}


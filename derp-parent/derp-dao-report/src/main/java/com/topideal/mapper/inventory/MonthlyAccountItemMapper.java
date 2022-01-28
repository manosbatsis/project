package com.topideal.mapper.inventory;

import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.inventory.MonthlyAccountItemModel;

/**
 * 月结账单商品明细 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface MonthlyAccountItemMapper extends BaseMapper<MonthlyAccountItemModel> {

	/**
     * 根据月结id删除明细
     * @param monthlyAccountId
     */
	int delItemByMonthlyAccountId(@Param("monthlyAccountId") Long monthlyAccountId);

	/**
	 * 统计存货跌价信息
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getInventoryFallingPriceReserves(Map<String, Object> queryMap);

	
}


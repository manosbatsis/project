package com.topideal.order.service.bill;

import java.sql.SQLException;

import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.dto.bill.PlatformCostOrderItemDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;

/**
 * @Description: 应收费用
 * @Author: 杨创
 * @Date: 2020-08-31 15:01
 **/
public interface PlatformCostOrderService {

    /**
     * 获取分页
     * @param dto
     * @return
     * @throws SQLException
     */
	PlatformCostOrderDTO listLatformCostOrderByPage(PlatformCostOrderDTO dto) throws SQLException;
	/**
	 * 获取详情分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PlatformCostOrderItemModel listLatformCostOrderItemByPage(PlatformCostOrderItemModel model) throws SQLException;

	/**
	 * 获取详情分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PlatformCostOrderItemDTO listPlatformCostOrderItemByPage(PlatformCostOrderItemDTO dto) throws SQLException;

	/**
	 * 获取详情
	 * @return
	 */
	PlatformCostOrderDTO getDetails(PlatformCostOrderDTO dto)throws SQLException;
	

}

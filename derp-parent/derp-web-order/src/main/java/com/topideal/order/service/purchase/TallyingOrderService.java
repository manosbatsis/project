package com.topideal.order.service.purchase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.entity.dto.sale.PendingConfirmTallyingOrderVo;

/**
 * 理货单 service
 */
public interface TallyingOrderService {

	/**
	 * 采购模块的理货单列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	TallyingOrderDTO listPageByPurchase(TallyingOrderDTO dto, User user) throws SQLException;

	/**
	 * 列表（分页）
	 * 
	 * @param model
	 * @return
	 */
	TallyingOrderDTO listTallyingOrderPage(TallyingOrderDTO dto, User user) throws SQLException;

	/**
	 * 根据ID获取详情
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	TallyingOrderDTO searchDetail(Long id) throws SQLException;

	/**
	 * 修改理货单状态
	 * 
	 * @param ids
	 * @param state
	 * @param userInfoModel
	 */
	String modifyOrderState(String ids, String state, Long userId, String name, String topidealCode)
			throws SQLException;

	/**
	 * 调拨确认/驳回 理货单
	 * 
	 * @param ids
	 *            理货单id 多个时用,号分隔
	 * @param state
	 *            操作指令 010:确认 004:驳回
	 * @param userInfoModel
	 */
	public String updateTallyConfirmTransfer(String ids, String state, Long userId, String name,
			String topidealCode) throws SQLException;

	/**
	 * 获取待确认的理货单
	 * @Param
	 * @return
	 */
	List<PendingConfirmTallyingOrderVo> getPendingConfirmOrders(Map<String, Object> map) throws SQLException;


	/**
	 * 统计待确认的理货单数量
	 * @Param
	 * @return
	 */
	Integer countPendingConfirmOrders(Map<String, Object> map) throws SQLException;

	/**
	 * 获取导出列表
	 * @param model
	 * @param tallyingDateStr
	 * @return
	 * @throws SQLException 
	 */
	List<TallyingOrderDTO> getDetailsByExport(TallyingOrderDTO dto, User user) throws SQLException;

	TallyingOrderDTO searchDTODetail(Long id);
}

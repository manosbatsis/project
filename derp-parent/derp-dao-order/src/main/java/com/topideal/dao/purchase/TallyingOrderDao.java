package com.topideal.dao.purchase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.entity.dto.sale.PendingConfirmTallyingOrderVo;
import com.topideal.entity.vo.purchase.TallyingOrderModel;

/**
 * 理货单
 * @author lianchenxing
 *
 */
public interface TallyingOrderDao extends BaseDao<TallyingOrderModel>{
	/**
	 * 根据id修改理货单状态（批量）
	 * @param ids  多个id以 “,”隔开
	 * @param state  状态   2：确认  3：驳回
	 * @return
	 */
	int modifyOrderState(String ids, String state);
	
	/**
	 * 获取理货单对应的商品和批次信息
	 * @param dto
	 */
	public TallyingOrderDTO getDetails(TallyingOrderDTO dto);
	
	/**
	 * 采购模块-理货单分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	TallyingOrderDTO getListByPage(TallyingOrderDTO dto) throws SQLException;

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

	List<TallyingOrderDTO> getListDetails(TallyingOrderDTO dto);

	TallyingOrderDTO getDTODetails(TallyingOrderDTO dto);

	/**
	 * 调拨模块-理货单分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	TallyingOrderDTO getDTOListByPage(TallyingOrderDTO dto) throws SQLException;
}


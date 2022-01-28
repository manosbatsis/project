package com.topideal.mapper.purchase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.purchase.TallyingOrderDTO;
import com.topideal.entity.dto.sale.PendingConfirmTallyingOrderVo;
import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.purchase.TallyingOrderModel;
import com.topideal.mapper.BaseMapper;

/**
 * 理货单
 * @author lianchenxing
 *
 */
@MyBatisRepository
public interface TallyingOrderMapper extends BaseMapper<TallyingOrderModel> {
	/**
	 * 修改理货单状态
	 * @param ids
	 * @param state
	 * @return
	 */
	int modifyOrderState(@Param("ids") String ids, @Param("state") String state);

	/**
     * 查询出表体,理货单对应的商品和批次信息
     * @return model
     */
	public TallyingOrderDTO getDetails(TallyingOrderDTO dto);
	
	/**
	 * 采购模块-理货单分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<TallyingOrderDTO> getListByPage(TallyingOrderDTO dto) throws SQLException;

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
	 * 调拨理货单分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PageDataModel<TallyingOrderDTO> listDTOByPage(TallyingOrderDTO dto) throws SQLException;
}


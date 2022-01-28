package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotItemDTO;
import com.topideal.entity.vo.sale.OrderReturnIdepotItemModel;


public interface OrderReturnIdepotItemDao extends BaseDao<OrderReturnIdepotItemModel> {
		

	/**
	 * 根据外部单号和商品ID查询已入库的商品信息
	 * @return
	 */
	Integer getReturnNumByExternalCode(Map<String, Object> map);
    /**
     * 列表查询
     * @param model
     * @return
     * @throws SQLException
     */
    List<OrderReturnIdepotItemDTO> listOrderReturnIdepotItemDTO(OrderReturnIdepotItemDTO dto) throws SQLException;

    /**
     * 根据退款单id集合查询表体信息
     *
     * @return
     */
    List<OrderReturnIdepotItemDTO> listByOrderIds(List<Long> returnIdepotIds);

    /*根据退款单统计事业部*/
    List<Long> listBuByOrder(OrderReturnIdepotDTO dto);

    /**
     * 根据退款单id集合查询对应的结算金额最高的商品明细
     * @param orderIds 退款单ids
     * @param buIdFlag 是否需要事业部的维度
     * @return
     */
    List<OrderReturnIdepotItemModel> getMaxPriceByOrderId(List<Long> orderIds, Boolean buIdFlag);
    //批量新增
  	Integer batchSave(List<OrderReturnIdepotItemModel> list) throws SQLException;
  	//批量更新
  	void batchUpdate(List<OrderReturnIdepotItemModel> list) throws SQLException;
}

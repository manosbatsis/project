package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.PurchaseWarehouseDTO;
import com.topideal.entity.vo.order.PurchaseWarehouseModel;

/**
 * 采购入库单
 * @author zhanghx
 */
public interface PurchaseWarehouseDao extends BaseDao<PurchaseWarehouseModel> {
	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询采购入库单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getListPage(Long merchantId, String startTime, String endTime, Integer startIndex, Integer pageSize)throws SQLException;
	
	/**
	 * 报表api 根据 商家开始时间结束时间 查询采购入库单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Integer getListCount(Long merchantId, String startTime, String endTime);
	/**
	 * 根据采购订单id 获取poNo、币种、供应商名称、供应商编码
	 * @param id
	 * @return
	 */
	Map<String, Object> getOrderInfo(Long orderId);
	/**
	 * 根据采购入库单创建时间查询已入库数据
	 * @param PurchaseWarehouseDTO
	 * @return
	 */
	List<PurchaseWarehouseDTO> getByTimeOrder(PurchaseWarehouseDTO purchaseWarehouseDTO);

	/**
	 * 在库天数获取采购订单Id
	 * @param queryWarehouseMap
	 * @return
	 */
	String getPurchaseOrderIdByWarehouse(Map<String, Object> queryWarehouseMap);

	/**
	 * 获取未勾稽入库单
	 * @param queryMap
	 * @return
	 */
	List<PurchaseWarehouseModel> getUnCorrelateList();

	/**
	 * 获取在库天数明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getInWarehouseDaysDetail(Map<String, Object> queryMap);
}


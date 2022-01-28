package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderExportDTO;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderItemDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PlatformPurchaseOrderService {

	/**
	 * 分页查询
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PlatformPurchaseOrderDTO listPlatformPurchaseOrder(PlatformPurchaseOrderDTO dto) throws SQLException;

	/**
	 * 提交、驳回
	 * @param list
	 * @param status
	 * @throws SQLException
	 */
	//void modifyStatus(List<Long> list, String status) throws SQLException;

	/**
	 * 检查能否转销售单
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> checkOrderToSales(List<Long> list) throws SQLException;

	/**
	 * 根据ID查询详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	PlatformPurchaseOrderDTO searchDTOById(Long id) throws SQLException;

	/**
	 * 获取导出列表
	 * @param dto
	 * @return
	 */
	List<PlatformPurchaseOrderExportDTO> getExportList(PlatformPurchaseOrderExportDTO dto);

	/**
	 * 弹框获取明细
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	PlatformPurchaseOrderDTO getPlatformPurchaseOrderItems(List<Long> list) throws SQLException;

	/**
	 * 根据po和销售订单编号获取平台采购单对象
	 * @param dto
	 * @return
	 */
    List<PlatformPurchaseOrderDTO> listPlatformPurchaseOrderByCodeAndPoNo(PlatformPurchaseOrderDTO dto);

    /**
	 * 转销售单获取销售货号
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	Map<Long, List<MerchandiseInfoMogo>> getSaleItems(List<PlatformPurchaseOrderItemDTO> itemList, Long outDepotId ,  User user) throws Exception;

	/**
	 * 校验转销售单商品
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<String> checkBeforeConfirm(List<PlatformPurchaseOrderItemDTO> itemList, Long outDepotId , Long buId,User user) throws Exception;

	void savePlatFormPurchaseToSales(SaleOrderDTO dto, User user) throws Exception;
}

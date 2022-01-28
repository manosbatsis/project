package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderDTO;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderExportDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface PlatformPurchaseOrderDao extends BaseDao<PlatformPurchaseOrderModel> {

	PlatformPurchaseOrderDTO getListByPage(PlatformPurchaseOrderDTO dto);

	int modifyWherSubmit(PlatformPurchaseOrderModel updateOrder);

	/**
	 * 查询DTO
	 * @param id
	 * @return
	 */
	PlatformPurchaseOrderDTO searchDTOById(Long id);

	/**
	 * 获取导出list
	 * @param dto
	 * @return
	 */
	List<PlatformPurchaseOrderExportDTO> getExportList(PlatformPurchaseOrderExportDTO dto);


	/**
	 * 根据po和销售订单编号获取平台采购单对象
	 * @param dto
	 * @return
	 */
    List<PlatformPurchaseOrderDTO> listPlatformPurchaseOrderByCodeAndPoNo(PlatformPurchaseOrderDTO dto);


    /**
     * 分页查询近两个月的平台采购单(平台采购单未上架信息发送消息)
     * @param map
     * @return
     */
    List<PlatformPurchaseOrderModel> getPurchaseOrderMonth(Map<String, Object> map)throws SQLException;

	List<PlatformPurchaseOrderDTO> listDTO(PlatformPurchaseOrderDTO dto);
}

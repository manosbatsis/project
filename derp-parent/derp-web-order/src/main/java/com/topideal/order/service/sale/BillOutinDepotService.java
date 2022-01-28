package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.BillOutinDepotDTO;
import com.topideal.entity.dto.sale.BillOutinDepotItemDTO;
import com.topideal.entity.vo.sale.BillOutinDepotBatchModel;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;

public interface BillOutinDepotService {

	/**
	 * 获取分页
	 * @param dto
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	BillOutinDepotDTO listBillOutinDepotByPage(BillOutinDepotDTO dto,User user) throws SQLException;

	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 */
	BillOutinDepotDTO searchDetail(Long id);

	/**
	 * 根据查询条件获取条数
	 * @param dto
	 * @return
	 * @throws SQLException 
	 */
	Integer getOrderCount(BillOutinDepotDTO dto) throws SQLException;

	/**
	 * 获取导出列表
	 * @param dto
	 * @return
	 */
	List<BillOutinDepotDTO> getExportMainList(BillOutinDepotDTO dto,User user);

	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> importBillOutinDepot(List<Map<String, String>> data, User user) throws Exception;

	/**
	 * 获取明细分页
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	BillOutinDepotItemModel listBillOutinDepotItemByPage(BillOutinDepotItemModel model) throws SQLException;

	/**
	 * 获取批次分页
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	BillOutinDepotBatchModel listBillOutinDepotBatchByPage(BillOutinDepotBatchModel model) throws SQLException;

	/**
	 * 逻辑删除
	 * @param list
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	boolean delBillOutinDepot(List<Long> list) throws Exception;

	/**
	 * 获取订单明细数量
	 * @param list
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Integer> getOrderGoodsInfo(List<Long> list) throws SQLException;

	/**
	 * 审核
	 * @param id
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	Map<String, String> auditSaleOutDepot(Long id, User user) throws Exception;

	List<BillOutinDepotItemDTO> getExportItemList(BillOutinDepotDTO dto,User user);

}

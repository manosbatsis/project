package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.PurchaseSdOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseSdOrderPageDTO;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PurchaseSdOrderService {

	/**
	 * 统计分页信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PurchaseSdOrderPageDTO getPurchaseSdOrderPageList(PurchaseSdOrderPageDTO dto) throws SQLException;

	PurchaseSdOrderDTO searchDTOById(Long id);

	List<PurchaseSdOrderSditemModel> getSdItemList(Long id) throws SQLException;

	List<PurchaseSdOrderDTO> listDTO(PurchaseSdOrderPageDTO dto);

	Map<String, Object> getAmountAdjustmentDetail(Long id) throws SQLException;

	/**
	 * 金额调整
	 * @param id
	 * @param itemList
	 * @throws Exception
	 */
	void saveAmountAdjustment(String id, String itemList) throws Exception;

	/**
	 * 保存SD单
	 * @param id
	 * @param itemList
	 * @throws Exception
	 */
	void saveSdOrder(String id, User user) throws Exception;

	/**
	 * 导出
	 * @param dto
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseSdOrderPageDTO> getExportSdOrder(PurchaseSdOrderPageDTO dto) throws SQLException;

	/**
	 * 导入
	 * @param data
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> importOrder(List<List<Map<String, String>>> data, User user) throws SQLException, Exception;

	/**
	 * 逻辑删除SD单
	 * @param ids
	 * @throws Exception
	 */
	void delSdOrder(String ids) throws Exception;

}

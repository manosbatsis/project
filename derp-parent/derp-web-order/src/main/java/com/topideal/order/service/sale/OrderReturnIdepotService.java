package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotItemDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 电商订单退货 service
 * */
public interface OrderReturnIdepotService {

	/**
	 * 列表（分页）
	 * @param model
	 * @return
	 */
	OrderReturnIdepotDTO listOrderByPage(OrderReturnIdepotDTO dto)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	OrderReturnIdepotDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据表头ID获取详细信息(包括商品批次信息)
	 * @param id
	 * @return
	 */
	List<OrderReturnIdepotItemDTO> listItemByOrderId(Long id)throws SQLException;
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param model
	 * @return
	 */
	OrderReturnIdepotDTO listOrderAndItemByPage(OrderReturnIdepotDTO dto)throws SQLException;
	/**
	 * 根据条件获取电商订单退货
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	int listOrder(OrderReturnIdepotDTO dto) throws SQLException;
	/**
	 * 根据条件获取需要导出的数据
	 * @param list
	 * @return
	 */
	List<Map<String, Object>> getExportOrderMap(OrderReturnIdepotDTO dto) throws SQLException;
	/**
	 * 根据电商订单商品ID获取批次表体信息
	 * @param id
	 * @return
	 */
	List<OrderReturnIdepotBatchDTO> listOrderReturnBatchById(List<Long> itemIdList);
	/**
	 * 获取电商订单退货的统计数据
	 * @param merchantId
	 * @return
	 * @throws SQLException
	 */
	 Map<String,Object> getOrderDataList(Long merchantId)throws SQLException;


	/**
	 * 根据条件获取电商订单退货数量
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	Integer queryListCount(OrderReturnIdepotDTO dto)throws SQLException;
	/**
	 * 获取列表数据（表头）-分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	OrderReturnIdepotDTO newListByPage(OrderReturnIdepotDTO dto) throws SQLException;

	/**
	 * 根据ID删除手动导入订单
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int delImportOrder(List<Long> list) throws Exception;

	/**
	 * 检查手工导入订单是否满足条件
	 * @param list
	 * @return
	 */
	boolean checkConditionsOrder(List<Long> list) throws Exception;

	/**
	 * 校验要审核的订单
	 * @param list
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	void getOrderGoodsInfo(List<Long> list,Long merchantId) throws Exception;

	/**
	 * 审核电商订单退货
	 * @param list
	 */
	void examineOrder(List<Long> list , String topidealCode,User user) throws Exception;

	/**
	 * 导入
	 * @param user
	 * @param data
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> importOrder(User user, List<List<Map<String, String>>> data,String type) throws Exception;
}

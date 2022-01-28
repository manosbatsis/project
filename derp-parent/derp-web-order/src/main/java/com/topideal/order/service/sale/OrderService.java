package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderHistoryDTO;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.dto.sale.WayBillItemDTO;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.mongo.entity.MerchantShopRelMongo;

/**
 * 电商订单 service
 * */
public interface OrderService {

	/**
	 * 列表（分页）
	 * @param model 
	 * @return
	 */
	OrderModel listOrderByPage(OrderModel model)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	OrderModel searchDetail(Long id) throws SQLException;

	/**
	 * 根据表头ID获取详细信息(包括商品批次信息)
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<OrderItemDTO> listItemByOrderId(Long id)throws SQLException;
	/**
	 * 根据条件获取电商订单信息（包括表体信息）
	 * @param model
	 * @return
	 */
	OrderDTO listOrderAndItemByPage(OrderDTO dto)throws SQLException;
	/**
	 * 根据条件获取电商订单
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	int listOrder(OrderDTO dto) throws SQLException;
	/**
	 * 根据条件获取需要导出的数据
	 * @param list
	 * @return
	 */
	List<Map<String, Object>> getExportOrderMap(OrderDTO dto) throws SQLException;
	/**
	 * 根据电商订单id获取运单表体信息
	 * @param id
	 * @return
	 */
	List<WayBillItemDTO> listWayBillItemByOrderId(Long id);

	/**
	 * 根据条件获取电商订单数量
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	Integer queryListCount(OrderDTO dto)throws SQLException;
	/**
	 * 获取列表数据（表头）-分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	OrderDTO newListByPage(OrderDTO dto) throws SQLException;
	
	/**
	 * 手动导入电商订单
	 * @param user
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	Map<String, Object> importOrder(User user, Map<Integer, List<List<Object>>> data) throws Exception;
	
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
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * @param list
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	Map<String, Integer> getOrderGoodsInfo(List<Long> list,Long merchantId) throws Exception;
	
	/**
	 * 审核电商订单
	 * @param list
	 * @param topidealCode
	 * @param merchantId
	 * @throws Exception
	 */
	void auditOrder(List<Long> list , String topidealCode,Long merchantId) throws Exception;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	OrderDTO searchDtoDetail(Long id) throws SQLException;
	/**
	 * 根据ID查询历史表获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	OrderHistoryDTO searchHistoryDtoDetail(Long id) throws SQLException;
	
	List<MerchantShopRelMongo> getMerchantShopRelList(Long merchantId) throws Exception;
	
	/**
	 * 获取事业部补录列表数据-导出
	 * @param list
	 * @return
	 */
	List<OrderDTO> businessUnitRecordExport(OrderDTO dto) throws SQLException;
	/**
	 * 获取事业部补录列表数据-分页
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	OrderDTO listBusinessUnitRecordByPage(OrderDTO dto) throws SQLException;
	/**
	 * 事业部补录列表--批量更新
	 * @param list
	 * @param buId
	 * @param topidealCode
	 * @param merchantId
	 * @throws Exception
	 */
	String modifyBusinessUnitRecord(List<String> list ,Long buId,String buName, String topidealCode,Long merchantId,Long userId) throws Exception;

	/**
	 * 生成电商订单excel
	 * @param task
	 * @param basePath
	 * @return
	 * @throws Exception
	 */
	String createExportExcel(FileTaskMongo task, String basePath) throws Exception;
}

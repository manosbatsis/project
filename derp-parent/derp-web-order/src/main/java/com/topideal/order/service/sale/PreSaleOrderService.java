package com.topideal.order.service.sale;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;


/**
 * 预售单 service
 * */
public interface PreSaleOrderService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PreSaleOrderDTO listPreSaleOrderByPage(PreSaleOrderDTO dto,User user)throws SQLException;
	/**
	 * 删除预售单
	 * @param ids
	 * @return
	 * @throws Exception
	 */

	boolean delPreSaleOrder(List ids)throws Exception;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	PreSaleOrderDTO searchDetail(Long id) throws SQLException;
	/**
	 * 根据表头ID获取表体
	 * @param id
	 * @return
	 */
	List<PreSaleOrderItemDTO> listItemByOrderId(Long id)throws SQLException;

	/**
	 * 根据条件获取预售单信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<PreSaleOrderDTO> listPreSaleOrder(PreSaleOrderDTO dto,User user) throws SQLException;
	/**
	 * 新增/修改预售单
	 * @param json
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	boolean saveOrModifyTempOrder(String json, Long userId, String userName, String topidealCode)throws Exception;
	/**
	 * 新增预售单
	 * @param json
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	String addPreSaleOrder(String json, Long userId, String userName, String topidealCode)throws Exception;

	/**
	 * 修改预售单
	 * @param json
	 * @param userId
	 * @param userName
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	String modifyPreSaleOrder(String json, Long userId, String userName,String topidealCode) throws Exception;

	/**
	 * 校验预售单能否转成销售单
	 * @param ids
	 * @param codes
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	List<PreSaleOrderCorrelationDTO> checkPreSale(String ids,String codes) throws RuntimeException, SQLException;
	
	/**
	 * 校验预售单能否转成采购单
	 * @param ids
	 * @return
	 * @throws Exception 
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	PreSaleOrderDTO checkOrderState(Long id) throws Exception;

	/**
	 * 导入预售单
	 * @param data
	 * @param userId
	 * @param name
	 * @param merchantId
	 * @param merchantName
	 * @param topidealCode
	 * @param relMerchantIds
	 * @return
	 * @throws SQLException
	 */
	Map saveImportPreSaleOrder(Map<Integer, List<List<Object>>> data, Long userId, String name, Long merchantId,
									  String merchantName, String topidealCode, String relMerchantIds) throws SQLException;

	/**
	 * 校验该要生成采购单的供应商是否配置加价比例
	 * @param preSleIds
	 * @param supplierId
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	Map<String, Object> checkPurchaseCommission(String preSleIds,Long supplierId,User user) throws RuntimeException, SQLException;
	/**
	 * 根据PO号判断是否存在采购单
	 * @param poNo
	 * @param user
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	Map<String, String> checkExistPurchaseByPO(String poNo,User user) throws RuntimeException, SQLException;
	/**
	 * 生成采购订单
	 * @param json
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Long GeneratePurchaseOrder(String json, User user) throws Exception;
	/**
	 * 是否开启价格管理
	 * @param id
	 * @param supplierId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Boolean getPurchasePriceManage(Long id, Long supplierId, User user) throws Exception;
	/**
	 * 获取采购价格
	 * @param id
	 * @param supplierId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Map<String, List<BigDecimal>> getPurchasePrice(Long id, Long supplierId , User user) throws Exception;
}

package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.SaleReturnOrderDTO;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import net.sf.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售退货订单 service
 * */
public interface SaleReturnOrderService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	SaleReturnOrderDTO listSaleReturnOrderByPage(SaleReturnOrderDTO dto, User user)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SaleReturnOrderDTO searchDetail(Long id) throws SQLException;
	/**
	 * 新增
	 * @param json
	 * @param userId
	 * @param name
	 * @param topidealCode
	 * @return
	 * @throws Exception
	 */
	void saveSaleReturnOrder(SaleReturnOrderDTO dto, User user) throws Exception;

	/**
	 * 导入销售退货订单(代销退货)
	 * @param data
	 * @param userId
	 * @param userName
	 * @param merchantId
	 * @param merchantName
	 * @return
	 * @throws SQLException
	 */
	Map importSaleReturnOrder(Map<Integer, List<List<Object>>> data,  User user ,String relMerchantIds)throws SQLException;

	/**
	 * 导入销售退货订单(消费者退货)
	 * @param data
	 * @param userId
	 * @param userName
	 * @param merchantId
	 * @param merchantName
	 * @return
	 * @throws SQLException
	 */
	Map importSaleReturnOrder2(Map<Integer, List<List<Object>>> data, User user ,String relMerchantIds)throws SQLException;


	/**
	 * 根据表头ID获取表体
	 * @param id
	 * @return
	 */
	List<SaleReturnOrderItemDTO> listItemByOrderId(Long id)throws SQLException;
	/**
	 * 删除销售退货订单
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	boolean delSaleReturnOrder(List ids)throws Exception;
	/**
	 * 审核
	 * @param ids
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> auditSaleReturnOrder(List ids, User user)throws Exception;

	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	Map<String, Integer> getOrderGoodsInfo(List<Long> ids) throws Exception;

	SaleReturnOrderDTO listSaleOrderAndOutDepotOrder(SaleReturnOrderDTO dto,Long merchantId)throws SQLException;
	/**
	 * 需校验是否相同出仓仓库、相同客户
	 * @param ids
	 * @throws SQLException
	 * @throws ParameterException
	 */
	void isSameParameter(String ids) throws SQLException, RuntimeException;
	/**
	 * 判断是否可以出库打托
	 * @param id
	 * @return
	 */
	Map<String, String> isOutdepotReport(Long id) throws Exception;


	List<SaleReturnOrderItemDTO> getOrignGrossNetWeightItem(List<SaleReturnOrderItemDTO> itemList);


	/**
	 * 获取表体详情
	 * @param itemDto
	 * @return
	 * @throws Exception
	 */
	List<SaleReturnOrderItemDTO> getSaleReturnOrderItem(SaleReturnOrderItemDTO itemDto)throws Exception;
	/**
	 * 校验入库时间
	 */
	Map<String, String> validInDepotDate(Long id, String returnInDate) throws Exception;


	/**
	 * 根据销售退货订单查询对应出入库单是否存在
	 */
	Map<String, String> isExistSaleReturnIdepot(SaleReturnOrderDTO dto) throws SQLException;
	/**
	 * 保存 销售退货入库
	 * @param dto
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Map<String, String> saveSaleReturnIdepot(JSONObject jsonData, User user) throws Exception;
	/**
	 * 查询所有数据 不分页
	 * @param dto
	 * @return
	 */
	List<SaleReturnOrderDTO> getListDTO(SaleReturnOrderDTO dto, User user) throws SQLException;

	/**
	 * 是否能生成预申报单
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	Boolean checkCreateDeclare(String ids) throws Exception;
	/**
	 * 购销退货 选择商品
	 * @param form
	 * @return
	 * @throws Exception
	 */
	List<SaleReturnOrderItemDTO> getSalePopupListByPage(String saleOrderCode , String unNeedGoodsIdsAndPoNo, String goodsName,String goodsNo,String barcode, String poNo, Long inDepotId) throws Exception;
}

package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 销售订单 service
 * */
public interface SaleOrderService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 */
	SaleOrderDTO listSaleOrderByPage(SaleOrderDTO dto,User user)throws SQLException;
	/**
	 * 根据ID获取详情
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	SaleOrderDTO searchDetail(Long id) throws SQLException;
	/**
	 * 修改销售订单
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	String modifySaleOrder(SaleOrderDTO dto, User user, String rejectReason) throws Exception;
	/**
	 * 新增/修改并审核销售订单
	 * @param json
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	boolean saveOrModifyTempOrder(SaleOrderDTO dto, User user)throws Exception;

	/**
	 * 根据表头ID获取表体
	 * @param id
	 * @return
	 */
	List<SaleOrderItemDTO> listItemByOrderId(Long id)throws SQLException;
	/**
	 * 删除销售订单
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	boolean delSaleOrder(List ids  ,User user)throws Exception;

	/**
	 * 中转仓出库
	 * @param list
	 * @param userId
	 * @param userName
	 * @param topidealCode
	 * @return
	 */
	boolean confirmSaleOrderStockOut(List list, Long userId, String userName, String topidealCode,Long merchantId,String outDepotDateStr)throws Exception;
	/**
	 * 完结出库
	 * @param list
	 * @param user
	 * @return
	 * @throws Exception
	 */
	boolean confirmEndStockOut(List list, Long userId, String userName) throws Exception;
	/**
	 * 计算销售订单出库的百分比
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String,String> differenceRatio(Long id)throws Exception;

	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * @param ids
	 * @param type 用于判断是哪个业务调用； 1：审核  2：中转仓出库
	 * @return
	 * @throws Exception
	 */
	Map<String,Integer> getOrderGoodsInfo(List<Long> ids,String type)throws Exception;
	/**
	 * 导入购销销售订单
	 * @param data
	 * @param id
	 * @param name
	 * @param merchantId
	 * @param merchantName
	 * @param topidealCode
	 * @return
	 * @throws SQLException
	 */
	Map importSaleOrder(List<List<Map<String, String>>> data, User user) throws Exception;

	/**
	 * 根据条件获取销售订单信息
	 * @param dto
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	List<SaleOrderDTO> listSaleOrder(SaleOrderDTO dto, User user) throws SQLException;
	/**
	 * 校验订单能否生成采购订单（已审核且出仓仓库为中转仓的销售订单）
	 * @param ids
	 * @return
	 * @throws ParameterException
	 * @throws SQLException
	 */
	SaleOrderDTO checkOrderState(Long id) throws RuntimeException, SQLException;

	/**
	 * 获取上架记录
	 * @param saleOrderModel
	 * @return
	 * @throws SQLException
	 */
	Map<String,Object> listShelfListByOrderId(Long saleId,String saleOutCode) throws SQLException;
	/**
	 * 保存上架商品信息
	 * @param json
	 * @param id
	 * @param name
	 * @return
	 */
	Map<String,Object> saveSaleShelf(ShelfDTO dto, User user) throws Exception;
	/**
	 * 获取上架明细分页数据
	 * @param model
	 * @return
	 */
	SaleShelfModel listSaleShelfByPage(SaleShelfModel model) throws SQLException;
	/**
	 * 判断是否已经完全上架
	 * @param id
	 * @return
	 */
	boolean shelfIsEnd(Long id) throws Exception;

	List<SalePoRelModel> checkExistByPo(String poNo,Long orderId,Long merchantId,Long buId) throws Exception;

	/**
	 * 导入上架
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	Map importSaleShelf(List<List<Map<String,String>>> data,User user) throws Exception;

	/**
	 * 获取待上架的订单（销售订单和销售出库单）
	 * @Param
	 * @return
	 */
	List<PendingShelfSaleOrderVo> getPendingShelfSaleOrders(Map<String, Object> map) throws SQLException;

	/**
	 * 获取待审核的订单（采购、销售、调拨、销售退订单）
	 * @Param
	 * @return
	 */
	List<PendingCheckOrderVo> getPendingCheckOrders(Map<String, Object> map) throws SQLException;

	/**
	 * 统计待审核的订单数量（采购、销售、调拨、销售退订单）
	 * @Param
	 * @return
	 */
	Integer countPendingCheckOrders(Map<String, Object> map) throws SQLException;

	/**
	 * 统计待上架的订单数量(销售订单和销售出库单）
	 * @Param
	 * @return
	 */
	Integer countPendingShelfOrders(Map<String, Object> map) throws SQLException;



   String auditPurchase(SaleOrderModel saleOrder , Long userId, String name,String topidealCode) throws Exception;
	/***
	 * 销售新增代销获取分页数据
	 * @param model
	 * @param codes
	 * @return
	 * @throws SQLException
	 */
	SaleOrderDTO saleGetListSaleOrderByPage(SaleOrderDTO dto, String codes,User user) throws SQLException;

	/**
	 * 预售转销--》访问预售单编辑页面
	 * @param json
	 * @return
	 */
	SaleOrderDTO preSaleOrderToSaleEdit(String json);

	/**
	 * 新增内部销售单--带出采购单某些信息以及选择的销售出库仓
	 * @param purchaseId
	 * @param outDepotId
	 * @param merchantId
	 * @param merchantName
	 * @return
	 * @throws SQLException
	 */
	SaleOrderDTO getSaleOrderByPurchaseId(Long purchaseId,Long outDepotId,Long merchantId,String merchantName) throws SQLException;

	/**
	 * 校验该要生成采购单的供应商是否配置加价比例
	 * @param saleIds
	 * @param supplierId
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	Map<String, Object> checkPurchaseCommission(String saleIds,Long supplierId,User user) throws RuntimeException, SQLException;

	/**
	 * 平台采购单转销售
	 * @param platformPurchaseIds
	 * @param outDepotId
	 * @param buId
	 * @param platformSalesNum
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> modifyPlatFormPurchaseToSales(String platformPurchaseIds, String outDepotId, String buId, String platformSalesNum,User user) throws Exception;

	/**
	 * 商品导入
	 * @param data
	 * @param user
	 * @return
	 */
	Map importSaleGoods(List<List<Map<String, String>>> data, User user, SaleOrderDTO dto,String salePriceManage);

	/**
	 * 金额调整
	 * @param json
	 * @return
	 * @throws SQLException
	 */
    boolean amountAdjust(String json, User user) throws SQLException;
	/**
	 * 获取打托出库表体数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
    SaleOrderDTO listSaleOutBySaleId(Long id) throws Exception;
	/**
	 * 打托出库
	 * @param json
	 * @param user
	 * @return
	 * @throws Exception
	 */
    boolean saveSaleOrderOut(SaleOutDepotDTO saleOutDTO, User user) throws Exception;

	/**
	 * 商品导出
	 * @param json
	 * @return
	 */
	List<SaleOrderItemDTO> exportListItem(String json);

	/**
	 * 根据标准条码获取标准品牌
	 * @param commbarcode
	 * @return
	 */
	String getBrandParentName(String commbarcode);
	/**
	 * 根据销售编码和商品货号获取商品信息
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SaleOrderItemModel getItemByCodeAndNo(SaleOrderItemModel model) throws SQLException;
	/**
	  * 判断商品上架月份是否已关账
	 * @param user
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Map<String, String> checkExistFinanceClose(User user,Long id) throws SQLException;

	/**
	 * 判断是否存在采购单
	 * @param dto
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	Map<String, String> checkExistPurchase(SaleOrderDTO dto,User user) throws SQLException;

	/**
	 * 保存并审核 生成采购订单
	 * @param json
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	String createPurchaseOrder(Long saleId,Long buId,Long depotId, User user) throws Exception;
	/**
	 * 根据PO号判断是否存在采购单
	 * @param poNo
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	Map<String, String> checkExistPurchaseByPO(String poNo,User user) throws SQLException;

	/**
	 * 列表页面 生成采购订单
	 * @param json
	 * @return
	 * @throws Exception
	 */
	Long GeneratePurchaseOrder(String json,User user) throws Exception;

	/**
	 * 金额确认
	 * @param json
	 * @return
	 * @throws SQLException
	 */
    boolean amountConfirm(String json,User user) throws SQLException;

    /**
     * 提交
     * @param json
     * @param user
     * @param type
     * @return
     * @throws Exception
     */
    Long updateSaleOrder(SaleOrderDTO dto, User user) throws Exception;
   /**
    * 是否启用销售价格管理
    * @param buId
    * @param customerId
    * @param user
    * @return
    * @throws Exception
    */
    Boolean getSalePriceManage(Long buId,Long customerId,User user) throws Exception;
    /**
     * 获取销售价格管理的商品单价
     * @param customerId
     * @param commbarcode
     * @param currency
     * @param merchantId
     * @return
     */
    List<String> getSalePrice(String json,Long merchantId) throws Exception;

    /**
     * 导出清关资料
     * @param saleOrderDTO
     * @return
     */
    Map<String, FirstCustomsInfoDTO> exportDepotCustomsDeclares(Long id, List<Long> fileTempIds,String type) throws Exception;
	/**
	 * 融资申请弹窗详情
	 *
	 * @param id
	 * @param user
	 * @return
	 */
    SaleFinancingOrderDTO getFinanceDetail(Long orderId, User user)  throws Exception;
	/**
	 * 判断是否可生成预申报单
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	Boolean checkCreateDeclare(String ids) throws Exception;
	/**
	 * 修改po号
	 * @param orderId
	 * @param user
	 * @throws Exception
	 */
	void modifyPoNo(Long orderId, User user , String poNo, String remark) throws Exception;

	/**
	 * 反审
	 * @param token
	 * @param id
	 * @param remark
	 * @throws Exception
	 */
	void reverseAudit(Long orderId, String remark, User user)throws Exception;
	/**
	 * 中转仓获取商品明细
	 * @param list
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<SaleOrderItemModel> getStockOutItemList(List<Long> list , User user) throws Exception;
	/**
	 * 获取表体信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	List<SaleOrderItemDTO> listSaleOrderItemDTO(SaleOrderItemDTO dto) throws Exception;
	/**
	 * 生上架入库单
	 * @param shelfModel
	 * @param saleOrder
	 * @param user
	 * @param shelfList
	 * @return
	 * @throws Exception
	 */
	InvetAddOrSubtractRootJson saveShelfIdepot(ShelfModel shelfModel, SaleOrderModel saleOrder, User user,List<SaleShelfModel> shelfList) throws Exception;
	/**
    * 是否开启采购价格管理
    * @param buId
    * @param customerId
    * @param user
    * @return
    * @throws Exception
    */
    Boolean getPurchasePriceManage(Long id,Long supplierId,User user) throws Exception;
    /**
     *
     * @param id
     * @param supplierId
     * @param user
     * @return
     * @throws Exception
     */
    Map<String, List<BigDecimal>> getPurchasePrice(Long id, Long supplierId , User user) throws Exception;
    /**
     * 批量驳回
     * @throws Exception
     */
    void batchRejection(String ids,User user) throws Exception;

	/**
	 * 获取各状态数量
	 * @param dto
	 * @param user
	 * @return
	 */
	List<Map<String, Object>> getStatusNum(SaleOrderDTO dto, User user);

	/**
	 * 申请判断是否可以红冲
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean validateApplyWriteOff(Long id) throws Exception;

	/**
	 * 申请红冲
	 * @param id
	 * @param user
	 * @param reason
	 * @throws Exception
	 */
	void saveWriteOff(Long id, User user, String reason) throws Exception;

	/**
	 * 点击审核红冲带出申请原因
	 * @param id
	 * @return
	 * @throws Exception
	 */
	String showAuditWriteOff(Long id) throws Exception;

	/**
	 * 审核校验
	 * @param id
	 * @param writeOffDate
	 * @param auditResult
	 * @param user
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> validateAuditWriteOff(Long id, String writeOffDate, String auditResult, User user) throws Exception;

	/**
	 * 审核红冲
	 * @param id
	 * @param writeOffDate
	 * @param auditResult
	 * @param user
	 * @throws Exception
	 */
	void auditWriteOff(Long id, String writeOffDate, String auditResult, User user) throws Exception;
}

package com.topideal.order.service.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.entity.dto.purchase.*;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.entity.vo.purchase.PurchaseContractModel;
import com.topideal.entity.vo.purchase.PurchaseLinkInfoModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.order.webapi.purchase.form.PurchaseWarehouseForm;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 采购订单 service
 *
 * @author zhanghx
 */
public interface PurchaseOrderService {

	/**
	 * 列表（分页）
	 *
	 * @param model
	 * @return
	 */
	PurchaseOrderDTO listPurchaseOrderPage(PurchaseOrderDTO dto, User user) throws Exception;

	/**
	 * 新增
	 *
	 * @param model
	 * @return
	 */
	String savePurchaseOrder(PurchaseOrderModel model, User user) throws Exception;

	/**
	 * 根据id删除(支持批量)
	 *
	 * @param ids
	 * @return
	 */
	boolean delPurchaseOrder(List<Long> ids, User user) throws Exception;

	/**
	 * 修改
	 *
	 * @param model
	 * @return
	 */
	String modifyPurchaseOrder(PurchaseOrderModel model, User user) throws Exception;

	/**
	 * 根据id获取详情
	 *
	 * @param id
	 * @return
	 */
	PurchaseOrderModel searchDetail(Long id) throws SQLException;

	/**
	 * 生成预申报单
	 *
	 * @param list
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	DeclareOrderDTO generateDeclareOrder(List<Long> list, User user) throws Exception;

	/**
	 * 完结入库校验
	 *
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	String endPurchaseOrderCheck(List<Long> list) throws SQLException;

	/**
	 * 完结入库
	 *
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean endPurchaseOrder(List<Long> list, String endDateStr,User user) throws SQLException;

	/**
	 * 中转仓入库
	 *
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	List<InvetAddOrSubtractRootJson> saveInWarehouse(List<Long> list, User user, String inWarehouseDate) throws Exception;

	/**
	 * 导出查询表头表体
	 *
	 * @param
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getDetailsByExport(PurchaseOrderExportDTO dto, User user) throws SQLException;

	/**
	 * 根据ids获取销售订单、预售订单信息（多个订单时，表头只取其中一个，表体的商品去重叠加）转为采购订单
	 *
	 * @param ids
	 * @param type
	 * @throws SQLException
	 */
	Map<String,Object> listCopyOrSaleToOrder(String ids, String type,Long modalSupplierId,Long isUse,String purchaseCommission) throws SQLException;

	/**
	 * 2个页签导入采购订单
	 * @param data
	 * @param user
	 * @return map
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	Map importPurchaseOrder(List<List<Map<String, String>>> data, User user) throws Exception;

	/**
	 * 采购订单提交前校验合同号和PO号是否被使用
	 * @param model
	 * @return
	 */
	String checkContractNoAndPoNo(PurchaseOrderModel model) throws SQLException;
	/**
	 * 修改录入发票 和付款日期
	 * tag =1 修改录入日期
	 * tag=2 修改 付款日期
	 * invoiceDate 录入时间
	 * invoiceName 开票人
	 * payName 付款人
	 * payDate 付款时间
	 * id 采购订单id
	 * invoiceNo 发票号
	 * @param amounts
	 * @param prices
	 * @param goodsNos
	 * @param user
	 * @return
	 */
	void updatePurchaseOrderInvoice(PurchaseInvoiceDTO dto,String tag, User user)throws SQLException;

	/**
	 * 根据id获取仓库信息
	 */
	ViewResponseBean getDepotInfo(String id);

	/**
	 * 根据采购订单ID获取商品
	 * @param purchaseId
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderItemModel> getPurchaseItem(Long purchaseId) throws SQLException;


	void saveOrModifyTempOrder(PurchaseOrderModel model, User user) throws Exception;


	/**
	 * 根据商家获取待录入的采购订单
	 * @Param
	 * @return
	 */
	List<PurchaseOrderDTO> getPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException;

	/**
	 * 根据商家统计待录入的采购订单数量
	 * @Param
	 * @return
	 */
	Integer countPendingRecordOrders(PurchaseOrderDTO dto) throws SQLException;

	PurchaseOrderDTO searchDTODetail(Long id);

	PurchaseOrderDTO getListPurchaseOrderByPage(PurchaseOrderDTO dto, String codes,User user) throws SQLException;

	/**
	 * 获取编辑页面根据采购id 构建采购合同
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	PurchaseContractModel getContractModelByPurchaseId(Long id) throws SQLException;

	void modifyPurchaseContract(PurchaseContractModel model, User user) throws Exception;

	/**
	 * 生产PDF
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	ByteArrayOutputStream exportPurchaseContractPdf(String orderId) throws Exception;

	/**
	 * 获取采购合同明细
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderItemModel> getPurchaseContractItem(Long id) throws SQLException;

	/**
	 * 计算商品明细总量、总价
	 * @param itemList
	 * @return
	 */
	Map<String, Object> sumContractTotal(List<PurchaseOrderItemModel> itemList);

	void auditPurchaseContract(PurchaseContractModel model, User user, String status) throws SQLException, Exception;

	/**
	 * 获取归属时间
	 * @param valueOf
	 * @return
	 * @throws SQLException
	 */
	Timestamp getAttributionDate(Long valueOf) throws SQLException;

	/**
	 * 更新归属时间
	 * @param model
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	boolean updateAttributionDate(PurchaseOrderModel model) throws Exception;

	/**
	 * 选择内部采购单
	 * @param json
	 * @param merchantId
	 * @return
	 * @throws SQLException
	 */
	List<PurchaseOrderDTO> getOwnPurchaseOrder(String json,Long merchantId,String topidealCode) throws SQLException;

	/**
	 * 检查商品信息
	 * @param purchaseId
	 * @param outDepotId
	 * @param merchantId
	 * @return
	 * @throws SQLException
	 */
	String checkGoodsInfo(Long purchaseId,Long outDepotId,Long merchantId)throws Exception ;

	/**
	 * 根据采购订单ID获取拜耳合同json
	 * @throws SQLException
	 */
	Map<String, Object> getBayerContractByPurchaseId(Long id) throws Exception;

	/**
	 * 根据json保存采购合同
	 * @param map
	 * @throws SQLException
	 * @throws Exception
	 */
	void modifyJSONPurchaseContract(Map<String, Object> map, User user) throws Exception;

	/**
	 * 根据json保存采购合同并审核采购订单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Long auditJSONPurchaseContract(Map<String, Object> map, User user, String status) throws Exception;

	/**
	 * 获取保洁合同
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getPGContractByPurchaseId(Long id) throws Exception;

	/**
	 * 获取美赞合同
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getMeadContractByPurchaseId(Long id) throws Exception;

	ByteArrayOutputStream getTempDataFileStream(String orderCode, String fileTempCode, String direction) throws Exception;

	/**
	 * PDF文件名
	 * @param orderCode
	 * @return
	 * @throws SQLException
	 */
	String getPDFFileName(String orderCode) throws SQLException;

	/**
	 * 根据采购供应商获取模版
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	String getFileTempCode(Long tempId) throws SQLException;

	/**
	 * 查询文件路径导出合同
	 * @param orderCode
	 * @param fileTempCode
	 * @return
	 * @throws Exception
	 */
	String exportTempDataFile(String orderCode, String fileTempCode, User user) throws Exception;

	/**
	 * 获取交易链路配置
	 * @param purchaseOrderId
	 * @return
	 * @throws SQLException
	 */
	List<TradeLinkConfigModel> getTradeLinkList(Long purchaseOrderId) throws SQLException;


	/**
	 * 获取是否采购价格管理
	 * @param purchaseTradeLinkId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	PurchaseLinkInfoDTO getPurchaseLinkDtoByPurchaseId(Long id, Long purchaseTradeLinkId) throws SQLException;

	/**
	 * 保存采购链路信息
	 * @param model
	 * @throws SQLException
	 */
	Long saveOrUpdateLinkInfo(PurchaseLinkInfoModel model, User user) throws SQLException;

	/**
	 * 根据链路信息生成订单集合
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> generatePreOrder(Long id, User user) throws SQLException, Exception;

	/**
	 * 查询是否开启采购价格管理
	 * @param buId
	 * @param supplierId
	 * @return
	 */
	boolean getPurchasePriceManage(Long buId, Long supplierId, User user);

	/**
	 * 根据仓库是海外仓，则获取商品的箱托数量
	 * @param goodsId
	 * @param user
	 * @return
	 */
	com.alibaba.fastjson.JSONObject checkGoodsDepotData(Long goodsId, User user);

	/**
	 * 根据事业部+供应商+币种+商品查询采购单
	 * @param user
	 * @param buId
	 * @param supplierId
	 * @param currency
	 * @param goodIds
	 * @return
	 */
	List<Map<Long, net.sf.json.JSONArray>> getPurchaseByGoodsId(User user,Long depotId,String torrToUnit, Long buId, Long supplierId, String currency, String goodIds, Long stockLocationTypeId) throws Exception;

	/**
	 * 生成链路中订单
	 * @param map
	 * @param purchaseTradeLinkId
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> saveLinkOrder(Map<String, Object> map , Map<String, Object> batchMap,
			User user, Long purchaseTradeLinkId) throws Exception;

	/**
	 * 链路订单出库入库
	 * @param merchantIdOrderIdsMap
	 * @throws Exception
	 */
	void saveLinkInOutDepot(Map<String, Object> merchantIdOrderIdsMap, String batchNo,
			Date produceDate, Date overdueDate, User user) throws Exception;

	/**
	 * 获取订单编码
	 * @param merchantIdOrderIdsMap
	 * @return
	 * @throws SQLException
	 */
	List<String> getLinkOrderCode(Map<String, Object> merchantIdOrderIdsMap) throws SQLException;

	/**
	 * 采购金额调整模态框获取详情
	 * @throws SQLException
	 */
	Map<String, Object> getAmountAdjustmentDetail(Long id) throws SQLException;

	/**
	 * 保存采购金额调整
	 * @param model
	 * @param itemList
	 * @throws SQLException
	 */
	void saveAmountAdjustment(PurchaseOrderModel model, String itemList, User user) throws SQLException;

	/**
	 * 打托入库
	 * @param purchaseId
	 * @param inboundDate
	 * @param itemList
	 * @throws Exception
	 */
	void savePurchaseDelivery(PurchaseWarehouseForm form, User user) throws Exception;

	/**
	 * 推送库存
	 * @param jsonList
	 * @throws SQLException
	 */
	void pushInventory(List<InvetAddOrSubtractRootJson> jsonList) throws SQLException;

	/**
	 * 生成采购订单ID
	 * @param id
	 * @param tradeLinkId
	 * @return
	 * @throws SQLException
	 */
	Long saveJBPurchaseLinkByPurchaseId(Long id, User user, Long tradeLinkId) throws SQLException;

	/**
	 * 若起点公司的采购单状态待审核或已审核未入库的，系统自动审核自动入库
	 * @param purchaseTradeLinkId
	 * @throws SQLException
	 * @throws Exception
	 */
	void saveFirstOrderStatusAndIdepot(Long purchaseTradeLinkId, String batchNo, Date produceDate, Date overdueDate, User user) throws SQLException, Exception;

	/**
	 * 采购链路获取批次
	 * @param purchaseTradeLinkId
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getTradeLinkBatchNo(Long purchaseTradeLinkId) throws SQLException;

	/**
	 * SD单创建前校验
	 * @param id
	 * @throws SQLException
	 */
	void createSdOrderCheck(Long id) throws SQLException;

	/**
	 * 采购转销售前校验，PO号于销售单存在返回true，不存在返回false
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	boolean toSaleBeforeCheck(Long id) throws SQLException;


	/**
	 * 获取销售价格
	 * @param ids
	 * @param customerId
	 * @param user
	 * @return
	 */
	PurchaseOrderModel getCustomerBuIdPrice(String ids,Long customerId,User user) throws Exception;


	/**
	 * 采购单生成销售单
	 * @param id
	 * @param customerId
	 * @param items
	 * @throws SQLException
	 * @throws Exception
	 */
	Long saveSaleOrder(List<Long> id, Long customerId, String poNo, String items, User user) throws SQLException, Exception;

	/**
	 * 检查是否要生成内部供应商对应销售订单
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> checkInnerMerchantSaleOrder(Long id) throws SQLException;

	/**
	 * 保存内部公司销售订单
	 * @param id
	 * @param buId
	 * @param outDepotId
	 * @throws SQLException
	 * @throws Exception
	 */
	void saveInnerMerchantSaleOrders(Long id, Long outDepotId, Long buId, User user) throws Exception;

	/**
	 * 针对嘉宝-卓普信-宝信的交易链路（ID:1）生成的宝信采购单的单据状态默认为：已审核
	 * @param purchaseTradeLinkId
	 * @param merchantIdOrderIdsMap
	 * @throws SQLException
	 * @throws Exception
	 */
	void modifyJBLinkPurchaseOrder(Long purchaseTradeLinkId, Map<String, Object> merchantIdOrderIdsMap, User user) throws SQLException, Exception;

	/**
	 * 保存采购金额确认
	 * @param model
	 * @param
	 * @throws SQLException
	 */
	void saveConfirmAmountAdjustment(PurchaseOrderModel model, User user) throws SQLException;

	/**
	 * 提交采购合同
	 * @param model
	 * @param user
	 * @throws Exception
	 */
	void submitPurchaseContract(PurchaseContractModel model, User user) throws Exception;

	/**
	 * 提交（美赞、宝洁、拜耳）采购合同
	 */
	void submitJSONPurchaseContract(Map<String, Object> map, User user) throws Exception;

	/**
	 * 调整交易链路前校验
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	void toSaleStepBeforeCheck(Long id) throws Exception;

	/**
	 * 采购链路保存，修改商品信息
	 * @param purchaseTradeLinkId
	 * @param goodsInfoJson
	 * @throws SQLException
	 */
	void saveSaleStepGoodsInfo(Long purchaseTradeLinkId, String goodsInfoJson) throws SQLException;

	/**
	 * 校验对应采购订单是否存在交易链路配置
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> checkTradeLink(Long id) throws Exception;

	String getCreateFinancingOrderCheck(List<Long> idList) throws SQLException;

	/**
	 * 生成融资单
	 * @param idList
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	FinancingOrderDTO getFinancingOrder(List<Long> idList) throws Exception;

	/**
	 * 获取货品信息
	 * @param goodsId
	 * @return
	 */
	Map<String, Object> getGoodsProductInfo(Long goodsId);

	/**
	 * 提交卓普信
	 * @param dto
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	FinancingOrderDTO confirmSubmitSapience(FinancingOrderDTO dto, User user) throws Exception;

	/**
	 * 根据多个采购订单构造生成销售订单详情
	 * @param idList
	 * @return
	 */
    PurchaseOrderModel genSaleOrderByPurchaseIds(List<Long> idList,User user) throws Exception;

	/**
	 * 获取采购SD模态框金额信息
	 * @param id
	 * @return
	 */
	Map<String, Object> getSdAmountAdjustmentDetail(Long id) throws SQLException;

	/**
	 * 获取未入库数量
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	PurchaseOrderDTO getUnInwarehoustNum(PurchaseOrderDTO dto) throws SQLException;

	/***
	 * 删除融资信息
	 * @param purchaseOrders
	 * @throws SQLException
	 */
	void delPurchaseFinanceInfo(String purchaseOrders) throws SQLException;

	/**
	 * 生成预申报前校验
	 * @param list
	 * @param user
	 * @return
	 */
	void generateDeclareOrderCheck(List<Long> list, User user) throws SQLException;

	/**
	 * 生成卓普信发票
	 * @param dto
	 * @param user
	 * @throws SQLException
	 */
	void generateZPXInvoice(FinancingOrderDTO dto, User user) throws Exception;

	/**
	 * 反审操作
	 * @param id
	 * @param user
	 * @return
	 */
	Map<String,Object> getAuditCounterTrial(Long id,String remark,User user) throws SQLException;
	 /**
     * 批量驳回
     * @throws Exception
     */
    void batchRejection(String ids,User user) throws Exception;

	/**
	 * 检查相同SKU是否存在多条
	 * @param id
	 * @throws Exception
	 */
	boolean checkRepeatGoods(Long id) throws Exception;
	/**
	 * 商品导入
	 * @param data
	 * @param user
	 * @return
	 */
	Map importPurchaseGoods(List<List<Map<String, String>>> data, User user, PurchaseOrderDTO dto, Boolean purchasePriceManage);

	/**
	 * 申请红冲校验
	 * @param id
	 * @throws Exception
	 */
	boolean validateApply(Long id) throws Exception;

	/**
	 * 申请红冲保存
	 * @throws Exception
	 */
	void saveApplyWriteOff(Long id, String reason, User user) throws Exception;

	/**
	 * 审核红冲校验
	 * @param id
	 * @throws Exception
	 */
	PurchaseWriteOffDTO validateAuditWriteOff(Long id, User user) throws Exception;

	/**
	 * 审核红冲保存
	 * @throws Exception
	 */
	void saveAuditWriteOff(Long id, String auditResult, String attributiveDate, User user) throws Exception;

	/**
	 * 发起OA审批
	 * @param user
	 * @param ids
	 * @return
	 */
    ResponseBean getOAAuditPage(User user, String ids) throws Exception;

	/**
	 * 统计类型数量
	 * @param dto
	 * @return
	 */
	List<Map<String, Object>> getPurchaseOrderTypeNum(PurchaseOrderDTO dto, User user);

	/**
	 * 发起OA审批
	 * @param user
	 * @param dto
	 * @return
	 */
    ResponseBean callOAAudit(User user, PurchaseSaveOAAuditDTO dto) throws Exception;

	/**
	 * 查询本地服务器获取合同列表
	 * @param user
	 * @param purchaseOrderDTO
	 * @return
	 */
    List<AttachmentInfoMongo> listContract(User user, PurchaseOrderDTO purchaseOrderDTO) throws Exception;
}

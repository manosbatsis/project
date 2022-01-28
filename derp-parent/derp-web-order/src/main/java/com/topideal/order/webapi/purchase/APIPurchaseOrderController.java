package com.topideal.order.webapi.purchase;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.topideal.api.sapience.SapienceUtils;
import com.topideal.api.sapience.sapience008.FinancingQueryRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.*;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.entity.vo.purchase.PurchaseContractModel;
import com.topideal.entity.vo.purchase.PurchaseLinkInfoModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.*;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购订单 接口控制层
 *
 * @author gy
 */
@RestController
@RequestMapping("/webapi/order/purchase")
@Api(tags = "purchase-采购订单列表")
public class APIPurchaseOrderController {

	private static final Logger LOG = Logger.getLogger(APIPurchaseOrderController.class) ;

	private static final String[] MAIN_COLUMNS = {"采购订单编号", "订单状态", "入库仓库", "事业部", "供应商",
			"是否内部公司供应商", "PO号", "采购方式","框架合同号","采购试单申请编号","审批单号","采购币种", "归属日期", "发票日期", "理货单位", "是否已生成预申报单",
			"付款条款", "收货方式","融资申请号", "是否完结", "完结入库时间","创建人", "创建时间", "审核人", "审核时间","库位类型" };

	private static final String[] MAIN_KEYS = {"code", "statusLabel", "depotName", "buName", "supplierName",
			"isInnerMerchant","poNo", "purchaseMethodLabel","frameContractNo","tryApplyCode","approvalNo","currencyLabel", "attributionDate","drawInvoiceDate", "tallyingUnitLabel", "isGenerateLabel",
			"paymentProvisionLabel", "tradeTermsLabel","financingNo", "isEndLabel", "endDate","createName", "createDate", "auditName", "auditDate","stockLocationTypeName"} ;

	private static final String[] ITEM_COLUMNS = {"采购订单编号", "商品货号", "商品名称", "商品条码", "采购数量",
			"采购单价", "采购总金额(不含税)", "采购总金额(含税)", "税率", "税额"} ;

	private static final String[] ITEM_KEYS = {"orderCode", "goodsNo", "goodsName", "barcode", "num",
			"price", "amount", "taxAmount", "taxRate", "tax"} ;

	// 采购订单service
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;
	@Autowired
	private CommonBusinessService commonBusinessService ;

	/**
	 * 跳转新增页面，获取信息
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getAddPageInfo.asyn")
	@ApiOperation(value = "跳转新增页面，获取信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "销售订单、预售单、采购订单复制id,多个以','隔开", required = false),
			@ApiImplicitParam(name = "type", value = "转采购类型，销售订单-sale、预售单-preSale、采购订单复制-purchaseCopy", required = false),
			@ApiImplicitParam(name = "pageSource", value = "跳转页面入口：1-首页 2-订单列表页", required = false),
			@ApiImplicitParam(name = "modalSupplierId", value = "页面提供供应商ID", required = false),
			@ApiImplicitParam(name = "isUse", value = "是否使用加价比例 1-是 0-否", required = false),
			@ApiImplicitParam(name = "purchaseCommission", value = "加价比例字符串", required = false)
	})
	public ResponseBean<PurchaseOrderPageForm> getAddPageInfo(@RequestParam(value="token", required = true)String token,
															  String ids, String type, String pageSource,Long modalSupplierId,
															  Long isUse,String purchaseCommission) {

		try {
			PurchaseOrderPageForm pageForm = new PurchaseOrderPageForm() ;

			//销售订单、预售单、采购订单复制id不为空，则获取信息
			if(StringUtils.isNotBlank(ids)){
				Map<String,Object> map = purchaseOrderService.listCopyOrSaleToOrder(ids, type,modalSupplierId,isUse,purchaseCommission);

				PurchaseOrderModel purchaseOrder = (PurchaseOrderModel) map.get("purchaseOrder");
				PurchaseOrderDTO dto = new PurchaseOrderDTO() ;
				BeanUtils.copyProperties(purchaseOrder, dto);

				String unNeedIds = (String) map.get("unNeedIds");
				DepotInfoMongo depot = depotInfoService.getDetails(purchaseOrder.getDepotId());

				pageForm.setDepotType(depot.getType());
				pageForm.setDetails(dto);
				pageForm.setUnNeedIds(unNeedIds);
				pageForm.setType(type);

			}

			//跳转页面入口：1-首页 2-订单列表页
			if (StringUtils.isNotBlank(pageSource)) {
				pageForm.setPageSource(pageSource);
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, pageForm) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 根据采购订单ID获取信息
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getDetailsById.asyn")
	@ApiOperation(value = "根据采购订单ID获取信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true),
			@ApiImplicitParam(name = "pageSource", value = "跳转页面入口：1-首页 2-订单列表页", required = false)
	})
	public ResponseBean<PurchaseOrderPageForm> getDetailsById(@RequestParam(value="token", required = true)String token,
															  Long id, String pageSource) {

		try {

			PurchaseOrderPageForm pageForm = new PurchaseOrderPageForm() ;

			PurchaseOrderDTO dto = purchaseOrderService.searchDTODetail(id);
			DepotInfoMongo depot = depotInfoService.getDetails(dto.getDepotId());

			pageForm.setDetails(dto);
			pageForm.setDepotType(depot.getType());


			//跳转页面入口：1-首页 2-订单列表页
			if (StringUtils.isNotBlank(pageSource)) {
				pageForm.setPageSource(pageSource);
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, pageForm) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 根据采购订单ID获取合同信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getContractInfoByPurchaseId.asyn")
	@ApiOperation(value = "根据采购订单ID获取合同信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true),
			@ApiImplicitParam(name = "tempId", value = "文件模版ID", required = false)
	})
	public ResponseBean<PurchaseContractPageForm> toEditContractPage(String token, Long id, Long tempId) {

		try {
			PurchaseContractPageForm form = new PurchaseContractPageForm() ;

			String fileTempCode = purchaseOrderService.getFileTempCode(tempId) ;

			if("Bayer".equals(fileTempCode)) {

				Map<String, Object> map = purchaseOrderService.getBayerContractByPurchaseId(id) ;

				JSONObject detail = (JSONObject)map.get("detail");
				JSONArray goodsList = (JSONArray)map.get("goodsList") ;

				form.setPurchaseId(id);
				form.setDetail(detail);
				form.setGoodsList(goodsList);
				form.setContractType("1");

			}else if("PG".equals(fileTempCode)) {
				Map<String, Object> map = purchaseOrderService.getPGContractByPurchaseId(id) ;

				JSONObject detail = (JSONObject)map.get("detail");
				JSONArray goodsList = (JSONArray)map.get("goodsList") ;

				form.setPurchaseId(id);
				form.setDetail(detail);
				form.setGoodsList(goodsList);
				form.setContractType("2");

			}else if("mead".equals(fileTempCode)) {
				Map<String, Object> map = purchaseOrderService.getMeadContractByPurchaseId(id) ;

				JSONObject detail = (JSONObject)map.get("detail");
				JSONArray goodsList = (JSONArray)map.get("goodsList") ;

				form.setPurchaseId(id);
				form.setDetail(detail);
				form.setGoodsList(goodsList);
				form.setContractType("3");

			}else {

				PurchaseContractModel contractModel = purchaseOrderService.getContractModelByPurchaseId(id) ;
				List<PurchaseOrderItemModel> itemList = purchaseOrderService.getPurchaseContractItem(id);
				//计算商品明细总量、总价
				Map<String, Object> totalMap = purchaseOrderService.sumContractTotal(itemList) ;

				String currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, contractModel.getCurrency()) ;

				JSONObject detail = (JSONObject)JSONObject.toJSON(contractModel);
				JSONArray goodsList = (JSONArray)JSONArray.toJSON(itemList) ;

				Integer total = (Integer)totalMap.get("total") ;
				BigDecimal totalAccount = (BigDecimal)totalMap.get("totalAccount") ;

				form.setDetail(detail);
				form.setGoodsList(goodsList);
				form.setTotal(total);
				form.setTotalAccount(totalAccount);
				form.setCurrencyLabel(currencyLabel);
				form.setContractType("4");

			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, form) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 采购合同编辑提交
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/modifyPurchaseContract.asyn")
	@ApiOperation(value = "一般采购合同编辑提交")
	public ResponseBean<String> modifyPurchaseContract(PurchaseContractAddForm form) {
		try {

			String token = form.getToken();
			User user = ShiroUtils.getUserByToken(token);

			PurchaseContractModel model = new PurchaseContractModel() ;
			BeanUtils.copyProperties(form, model);

			Timestamp deliveryDate = TimeUtils.parse(form.getDeliveryDateStr(), "yyyy-MM-dd");
			model.setDeliveryDate(deliveryDate);

			// 响应结果集
			purchaseOrderService.modifyPurchaseContract(model, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 访问采购合同编辑提交
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/modifyJsonPurchaseContract.asyn")
	@ApiOperation(value = "美赞、宝洁、拜耳采购合同编辑提交")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<String> modifyJsonPurchaseContract(@RequestParam(value="token", required=true) String token,
														   @RequestBody Map<String, Object> map) {
		try {

			if(map == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(token);

			// 响应结果集
			purchaseOrderService.modifyJSONPurchaseContract(map, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}
	/**
	 * 访问采购合同编辑提交并提交采购订单
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/submitPurchaseContract.asyn")
	@ApiOperation(value = "一般采购合同编辑提交并提交采购订单")
	public ResponseBean<String> submitPurchaseContract(PurchaseContractAddForm form) {
		try {

			PurchaseContractModel model = new PurchaseContractModel() ;
			BeanUtils.copyProperties(form, model);

			Timestamp deliveryDate = TimeUtils.parse(form.getDeliveryDateStr(), "yyyy-MM-dd");
			model.setDeliveryDate(deliveryDate);

			/**判断是否嘉宝贸易*/
			User user = ShiroUtils.getUserByToken(form.getToken());

			// 响应结果集
			purchaseOrderService.submitPurchaseContract(model, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}


	/**
	 * 访问（美赞、宝洁、拜耳）采购合同编辑提交并提交采购订单
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/submitJSONPurchaseContract.asyn")
	@ApiOperation(value = "美赞、宝洁、拜耳采购合同编辑提交并提交采购订单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<String> submitJSONPurchaseContract(@RequestParam(value="token", required=true) String token,
														   @RequestBody Map<String, Object> map) {
		try {

			/**判断是否嘉宝贸易*/
			User user = ShiroUtils.getUserByToken(token);

			// 响应结果集
			purchaseOrderService.submitJSONPurchaseContract(map, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}


	/**
	 *  访问采购合同编辑提交并审核采购订单
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/auditPurchaseContract.asyn")
	@ApiOperation(value = "一般采购合同编辑提交并审核采购订单", notes="一般采购合同编辑提交并审核采购订单，若是嘉宝贸易主体，返回采购订单ID，作为跳转交易链路step2根据")
	@ApiImplicitParams({ @ApiImplicitParam(name = "status", value = "审核结果", required = true)})
	public ResponseBean<String> auditPurchaseContract(PurchaseContractAddForm form,
													  String status) {
		try {

			PurchaseContractModel model = new PurchaseContractModel() ;
			BeanUtils.copyProperties(form, model);

			Timestamp deliveryDate = TimeUtils.parse(form.getDeliveryDateStr(), "yyyy-MM-dd");
			model.setDeliveryDate(deliveryDate);

			/**判断是否嘉宝贸易*/
			User user = ShiroUtils.getUserByToken(form.getToken());

			// 响应结果集
			purchaseOrderService.auditPurchaseContract(model, user, status);

			if("1000000594".equals(user.getTopidealCode())) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, form.getOrderId().toString()) ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 访问采购合同编辑提交并审核采购订单
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/auditJSONPurchaseContract.asyn")
	@ApiOperation(value = "美赞、宝洁、拜耳采购合同编辑提交并审核采购订单", notes="采购合同编辑提交并审核采购订单，若是嘉宝贸易主体，返回采购订单ID，作为跳转交易链路step2根据")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<String> auditJSONPurchaseContract(@RequestParam(value="token", required=true) String token,
														  @RequestBody Map<String, Object> map) {
		try {

			if(map == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(token);

			String status = String.valueOf(map.get("status")) ;

			if(StringUtils.isBlank(status)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "审核结果为空") ;
			}

			// 响应结果集
			Long orderId = purchaseOrderService.auditJSONPurchaseContract(map, user, status);

			/**判断是否嘉宝贸易*/
			if("1000000594".equals(user.getTopidealCode())) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, orderId.toString()) ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}



	/**
	 * 反审
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/auditCounterTrial.asyn")
	@ApiOperation(value="反审")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true),
			@ApiImplicitParam(name = "remark", value = "反审原因", required = true)})
	public ResponseBean<String> auditCounterTrial(@RequestParam(value="token" , required = true)String token,
													  @RequestParam(value="id" , required = true)Long id,
												  @RequestParam(value="remark" , required = true)String remark) {
		try {

			User user=ShiroUtils.getUserByToken(token);

			Map<String,Object> map=purchaseOrderService.getAuditCounterTrial(id,remark,user);
			String code=(String)map.get("code");
			String message=(String)map.get("message");

			if("00".equals(code)){
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000.getCode(), message) ;
			}
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), message) ;
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}



	/**
	 * 获取分页数据
	 *
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/listPurchaseOrder.asyn")
	@ApiOperation(value="获取分页数据")
	private ResponseBean<PurchaseOrderDTO> listPurchaseOrder(PurchaseOrderForm form) {
		try {
			User user= ShiroUtils.getUserByToken(form.getToken()) ;

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;
			BeanUtils.copyProperties(form, dto);

			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseOrderService.listPurchaseOrderPage(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}
	/**
	 * 新增
	 *
	 * @param form
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/savePurchaseOrder.asyn")
	@ApiOperation(value="新增采购订单必填校验保存")
	public ResponseBean<String> savePurchaseOrder(PurchaseOrderAddForm form) {
		String result = "";
		try {

			PurchaseOrderModel model = new PurchaseOrderModel() ;

			BeanUtils.copyProperties(form, model);

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getSupplierId())
					.addObject(model.getTradeTerms())
					.addObject(model.getPaymentProvision())
					.addObject(model.getPoNo())
					.addObject(model.getBuId()).empty();

			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}




			List<PurchaseOrderItemModel> purchaseItems = JSONArray.parseArray(form.getItems(), PurchaseOrderItemModel.class);

			// 商品至少选择一个
			if (purchaseItems.isEmpty()) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "商品至少选择一个") ;
			}

			if (StringUtils.isNotBlank(form.getAttributionDateStr())) {
				model.setAttributionDate(TimeUtils.parse(form.getAttributionDateStr(), "yyyy-MM-dd"));
			}

			if(StringUtils.isNotBlank(form.getDrawInvoiceDateStr())) {
				model.setDrawInvoiceDate(TimeUtils.parse(form.getDrawInvoiceDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getInvoiceDateStr())) {
				model.setInvoiceDate(TimeUtils.parse(form.getInvoiceDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getPayDateStr())) {
				model.setPayDate(TimeUtils.parse(form.getPayDateStr(), "yyyy-MM-dd")) ;
			}
			if(StringUtils.isNotBlank(form.getPaymentDateStr())){
				model.setPaymentDate(TimeUtils.parse(form.getPaymentDateStr(), "yyyy-MM-dd")); ;
			}

			if(StringUtils.isNotBlank(form.getEstimateDeliverDateStr())){
				model.setEstimateDeliverDate(TimeUtils.parse(form.getEstimateDeliverDateStr(), "yyyy-MM-dd")); ;
			}
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			model.setMerchantId(user.getMerchantId());

			model.setItemList(purchaseItems);

			result = purchaseOrderService.savePurchaseOrder(model, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 编辑采购订单必填校验保存
	 * @param form
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/modifyPurchaseOrder.asyn")
	@ApiOperation(value="编辑采购订单必填校验保存")
	public ResponseBean<String> modifyPurchaseOrder(PurchaseOrderAddForm form) {
		String result = "";
		try {

			PurchaseOrderModel model = new PurchaseOrderModel() ;

			BeanUtils.copyProperties(form, model);

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getSupplierId())
					.addObject(model.getTradeTerms())
					.addObject(model.getPaymentProvision())
					.addObject(model.getBuId()).empty();
			/*
					.addObject(model.getApprovalNo())
					.addObject(model.getFrameContractNo())
			* */

			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			List<PurchaseOrderItemModel> purchaseItems =
					JSONArray.parseArray(form.getItems(), PurchaseOrderItemModel.class);

			// 商品至少选择一个
			if (purchaseItems.isEmpty()) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "商品至少选择一个") ;
			}

			if (StringUtils.isNotBlank(form.getAttributionDateStr())) {
				model.setAttributionDate(TimeUtils.parse(form.getAttributionDateStr(), "yyyy-MM-dd"));
			}

			if(StringUtils.isNotBlank(form.getDrawInvoiceDateStr())) {
				model.setDrawInvoiceDate(TimeUtils.parse(form.getDrawInvoiceDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getInvoiceDateStr())) {
				model.setInvoiceDate(TimeUtils.parse(form.getInvoiceDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getPayDateStr())) {
				model.setPayDate(TimeUtils.parse(form.getPayDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getPaymentDateStr())) {
				model.setPaymentDate(TimeUtils.parse(form.getPaymentDateStr(), "yyyy-MM-dd")); ;
			}
			if(StringUtils.isNotBlank(form.getEstimateDeliverDateStr())){
				model.setEstimateDeliverDate(TimeUtils.parse(form.getEstimateDeliverDateStr(), "yyyy-MM-dd")); ;
			}
			User user= ShiroUtils.getUserByToken(form.getToken());

			model.setItemList(purchaseItems);
			model.setMerchantId(user.getMerchantId());
			result = purchaseOrderService.modifyPurchaseOrder(model, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 完结入库校验
	 *
	 * @param ids
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/endPurchaseOrderCheck.asyn")
	@ApiOperation(value="完结入库校验")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以','隔开", required = true)})
	public ResponseBean<String> endPurchaseOrderCheck(@RequestParam(value="token" , required = true)String token,
													  @RequestParam(value="ids" , required = true)String ids) {
		String result = "";
		try {

			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011) ;
			}

			List<Long> list = StrUtils.parseIds(ids);
			result = purchaseOrderService.endPurchaseOrderCheck(list);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 完结入库
	 *
	 * @param ids
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/endPurchaseOrder.asyn")
	@ApiOperation(value="完结入库")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以','隔开", required = true),
			@ApiImplicitParam(name = "endDateStr", value = "完结日期", required = true)})
	public ResponseBean<String> endPurchaseOrder(@RequestParam(value="token" , required = true)String token,
												 @RequestParam(value="ids" , required = true)String ids,
												 @RequestParam(value="endDateStr", required=true)String endDateStr) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011) ;
			}
			if (StringUtils.isBlank(endDateStr)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(token);

			List<Long> list = StrUtils.parseIds(ids);
			boolean b = purchaseOrderService.endPurchaseOrder(list, endDateStr,user);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 删除
	 *
	 * @param ids
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/delPurchaseOrder.asyn")
	@ApiOperation(value="删除采购订单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以','隔开", required = true)})
	public ResponseBean delPurchaseOrder(@RequestParam(value="token" , required = true)String token,
										 @RequestParam(value="ids" , required = true)String ids) {
		try {

			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011) ;
			}

			User user = ShiroUtils.getUserByToken(token);

			List list = StrUtils.parseIds(ids);
			boolean b = purchaseOrderService.delPurchaseOrder(list, user);

			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999);
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 导入
	 *
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "导入采购订单")
	@PostMapping("/importPurchase.asyn")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<UploadResponse> importPurchase(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file,
													   @RequestParam(value = "token", required = true) String token) {

		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集

		try {

			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}

			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream()) ;

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user= ShiroUtils.getUserByToken(token);

			resultMap = purchaseOrderService.importPurchaseOrder(data, user);

			Integer success = (Integer) resultMap.get("success");
			Integer failure = (Integer) resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}


	/**
	 * 中转仓入库
	 *
	 * @param ids
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/inWarehouse.asyn")
	@ApiOperation(value="中转仓入库")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以','隔开", required = true),
			@ApiImplicitParam(name = "inWarehouseDateStr", value = "入库时间字符串", required = true)})
	public ResponseBean<String> inWarehouse(@RequestParam(value="token" , required = true)String token,
											@RequestParam(value="ids" , required = true)String ids,
											@RequestParam(value="inWarehouseDateStr" , required = true)String inWarehouseDateStr) {
		String result = "";
		try {

			User user= ShiroUtils.getUserByToken(token) ;
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011) ;
			}

			List<Long> list = StrUtils.parseIds(ids);
			List<InvetAddOrSubtractRootJson> jsonList = purchaseOrderService.saveInWarehouse(list, user, inWarehouseDateStr);

			boolean flag = true ;

//			for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonList) {
//				try {
//					commonBusinessService.saveAutoPurchaseAnalysis(invetAddOrSubtractRootJson.getOrderNo());
//				} catch (DerpException e) {
//
//					flag &= false ;
//
//					if(result.length() > 0) {
//						result += "<br>" ;
//					}
//
//					result += e.getMessage() ;
//
//					commonBusinessService.modifyCorrelationstatus((PurchaseWarehouseModel)e.getObj());
//				}
//			}

			if(flag) {
				purchaseOrderService.pushInventory(jsonList) ;
			}

			if(result.length() == 0) {
				result = "成功" ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}


	/**
	 * 生成预申报单前检查
	 *
	 * @param ids
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/generateDeclareOrderCheck.asyn")
	@ApiOperation(value="生成预申报单前检查")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以','隔开", required = true)})
	public ResponseBean<String> generateDeclareOrderCheck(@RequestParam(value="token" , required = true)String token,
														  @RequestParam(value="ids" , required = true)String ids) {

		try {
			User user= ShiroUtils.getUserByToken(token) ;
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011) ;
			}
			List<Long> list = StrUtils.parseIds(ids);

			purchaseOrderService.generateDeclareOrderCheck(list, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 生成预申报单
	 *
	 * @param ids
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/generateDeclareOrder.asyn")
	@ApiOperation(value="生成预申报单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以','隔开", required = true)})
	public ResponseBean<DeclareOrderDTO> generateDeclareOrder(@RequestParam(value="token" , required = true)String token,
															  @RequestParam(value="ids" , required = true)String ids) {

		try {
			User user= ShiroUtils.getUserByToken(token) ;
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011) ;
			}
			List<Long> list = StrUtils.parseIds(ids);

			DeclareOrderDTO dto = purchaseOrderService.generateDeclareOrder(list, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}



	/**
	 * 采购导出
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/exportPurchase.asyn")
	@ApiOperation(value = "导出采购订单")
	public void exportRelation(HttpServletResponse response, HttpServletRequest request,PurchaseOrderForm form) throws Exception {

		User user= ShiroUtils.getUserByToken(form.getToken());

		PurchaseOrderExportDTO dto = new PurchaseOrderExportDTO() ;
		BeanUtils.copyProperties(form, dto);

		String fileName = "采购订单导出";
		dto.setMerchantId(user.getMerchantId());
		// 获取导出的信息
		Map<String, Object> resultMap = purchaseOrderService.getDetailsByExport(dto, user);

		List<PurchaseOrderExportDTO> list  = (List<PurchaseOrderExportDTO>)resultMap.get("orderList") ;
		List<PurchaseOrderItemModel> itemList = (List<PurchaseOrderItemModel>)resultMap.get("itemList");

		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("采购订单", MAIN_COLUMNS, MAIN_KEYS, list);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品详情", ITEM_COLUMNS, ITEM_KEYS, itemList);

		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
		sheetList.add(mainSheet) ;
		sheetList.add(itemSheet) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);

		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
	}

	/**
	 * 采购合同导出PDF
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@GetMapping("/exportPurchaseContractPdf.asyn")
	@ApiOperation(value = "采购合同导出PDF")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "orderId", value = "采购订单ID", required = true)})
	public void exportPurchaseContractPdf(HttpServletResponse response, HttpServletRequest request,
										  @RequestParam(value="orderId", required=true)String orderId,
										  @RequestParam(value="token", required=true)String token) throws Exception {

		FileInputStream input = null ;

		PurchaseOrderDTO dto = purchaseOrderService.searchDTODetail(Long.valueOf(orderId));

		String basePath = ApolloUtilDB.orderBasepath+"/puchaseContract/"+dto.getMerchantId();
		String fileName = purchaseOrderService.getPDFFileName(dto.getCode()) ;
		String pdfFileName = basePath+"/"+ fileName ;

		File file = new File(basePath) ;
		if(!file.exists()) {
			file.mkdirs() ;
		}

		file = new File(pdfFileName) ;
		if(!file.exists()) {
			ByteArrayOutputStream bos = purchaseOrderService.exportPurchaseContractPdf(orderId);
			FileOutputStream out = new FileOutputStream(pdfFileName) ;

			bos.writeTo(out);
			out.flush();
		}

		input = new FileInputStream(pdfFileName) ;

		FileExportUtil.setHeader(response, request, fileName);
		response.setContentType("application/pdf");
		response.setContentLength(input.available());
		OutputStream out = response.getOutputStream();

		byte[] b = new byte[input.available()];
		while ((input.read(b)) != -1) {
			out.write(b);
		}

		input.close();
		out.flush();
		out.close();

	}


	@SuppressWarnings("unchecked")
	@PostMapping("/getPurchasePriceByGoodsId.asyn")
	@ApiOperation(value = "编辑新增获取采购单价")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部ID", required = true),
            @ApiImplicitParam(name = "depotId", value = "仓库id", required = true),
            @ApiImplicitParam(name = "torrToUnit", value = "理货单位 00-托盘 01-箱 02-件", required = true),
			@ApiImplicitParam(name = "supplierId", value = "供应商ID", required = true),
			@ApiImplicitParam(name = "currency", value = "币种", required = true),
			@ApiImplicitParam(name = "goodIds", value = "商品ids，多个以，号隔开", required = true),
			@ApiImplicitParam(name = "stockLocationTypeId", value = "库位类型id", required = false)})
	public ResponseBean<Boolean> getPurchasePriceByGoodsId(@RequestParam(value="token", required=true)String token, @RequestParam(value="depotId", required=true)Long depotId,
                                                           @RequestParam(value="torrToUnit", required=true)String torrToUnit,
														@RequestParam(value="buId", required=true)Long buId,
														@RequestParam(value="supplierId", required=true)Long supplierId,
														@RequestParam(value="currency", required=true)String currency,
														@RequestParam(value="goodIds", required=true)String goodIds,
														@RequestParam(value="stockLocationTypeId", required=false) Long stockLocationTypeId) {
		try {
			User user = ShiroUtils.getUserByToken(token);

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(buId)
					.addObject(supplierId)
					.addObject(currency)
					.addObject(goodIds)
					.empty();

			if(isNull) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			List<Map<Long, net.sf.json.JSONArray>> list=purchaseOrderService.getPurchaseByGoodsId(user,depotId,torrToUnit,buId,supplierId,currency,goodIds,stockLocationTypeId);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, list) ;
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}

	}



	/**
	 * 采购订单提交前校验合同号和PO号是否被使用
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/checkContractNoAndPoNo.asyn")
	@ApiOperation(value = "采购订单提交前校验合同号和PO号是否被使用", notes="原方法名getPurchaseOrder.asyn停用")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "poNo", value = "采购订单PO号", required = false),
			@ApiImplicitParam(name = "contractNo", value = "采购订单合同号", required = false)})
	private ResponseBean<String> getPurchaseOrder(@RequestParam(value="token", required = true)String token,
												  String poNo, String contractNo) {
		String result = "";
		try {

			User user= ShiroUtils.getUserByToken(token) ;

			PurchaseOrderModel model = new PurchaseOrderModel() ;
			// 设置商家id
			model.setMerchantId(user.getMerchantId());
			model.setPoNo(poNo);
			model.setContractNo(contractNo);
			// 响应结果集
			result = purchaseOrderService.checkContractNoAndPoNo(model);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 获取仓库详情
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getDepotInfo.asyn")
	@ApiOperation(value = "获取仓库详情")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库Id", required = true)})
	private ResponseBean<String> getDepotInfo(@RequestParam(value="token", required = true)String token,
											  @RequestParam(value="depotId", required = true)String depotId) {

		String result = "";
		try {
			// 响应结果集
			ViewResponseBean depotInfo = purchaseOrderService.getDepotInfo(depotId);
			result = (String) depotInfo.getData();

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 修改录入发票 和付款日期
	 * tag =1 修改录入日期
	 * tag=2 修改 付款日期
	 * invoiceDate 录入时间
	 * invoiceName 开票人
	 * payName 付款人
	 * payDate 付款时间
	 * drawInvoiceDate 开发票时间
	 * invoiceNo 发票号
	 * id 采购订单id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "修改录入发票 和付款日期")
	@PostMapping(value = "/updatePurchaseOrderInvoice.asyn", consumes= MediaType.APPLICATION_JSON_VALUE)
//	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
//			@ApiImplicitParam(name = "invoiceDate", value = "录入时间", required = false),
//			@ApiImplicitParam(name = "invoiceName", value = "开票人", required = false),
//			@ApiImplicitParam(name = "payName", value = "付款人", required = false),
//			@ApiImplicitParam(name = "payDate", value = "付款时间", required = false),
//			@ApiImplicitParam(name = "drawInvoiceDate", value = "开发票时间", required = false),
//			@ApiImplicitParam(name = "invoiceNo", value = "发票号", required = false),
//			@ApiImplicitParam(name = "id", value = "采购订单id", required = false),
//			@ApiImplicitParam(name = "tag", value = "1 修改录入日期,2 修改 付款日期", required = false),
//			@ApiImplicitParam(name = "goodsNos", value = "货号", required = false),
//			@ApiImplicitParam(name = "amounts", value = "金额", required = false)})
	public ResponseBean<String> updatePurchaseOrderInvoice(@RequestBody PurchaseInvoiceAddForm form){
		try {

			User user = ShiroUtils.getUserByToken(form.getToken());
			PurchaseInvoiceDTO dto = new PurchaseInvoiceDTO();
			BeanUtils.copyProperties(form,dto);
			if(StringUtils.isNotBlank(form.getPayDate())){
				dto.setPaymentDate(TimeUtils.parse(form.getPayDate(),"yyyy-MM-dd"));
			}
			if(StringUtils.isNotBlank(form.getInvoiceDate())){
				dto.setInvoiceDate(TimeUtils.parse(form.getInvoiceDate(),"yyyy-MM-dd"));
			}
			if(StringUtils.isNotBlank(form.getDrawInvoiceDate())){
				dto.setDrawInvoiceDate(TimeUtils.parse(form.getDrawInvoiceDate(),"yyyy-MM-dd"));
			}
			purchaseOrderService.updatePurchaseOrderInvoice(dto, form.getTag(), user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 根据采购订单Id获取采购商品
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getPurchaseItem.asyn")
	@ApiOperation(value = "根据采购订单Id获取采购商品")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单Id", required = false)})
	public ResponseBean<List<PurchaseOrderItemModel>> getPurchaseItem(@RequestParam(value="token", required=true)String token,
																	  @RequestParam(value="id", required=true)String id) {

		if(StringUtils.isBlank(id)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		Long purchaseId = Long.valueOf(id) ;

		try {
			List<PurchaseOrderItemModel> items = purchaseOrderService.getPurchaseItem(purchaseId) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, items) ;

		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}


	}

	/**
	 * 保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验
	 * @Param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/saveOrUpdateTempOrder.asyn")
	@ApiOperation(value = "保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验")
	public ResponseBean<String> saveOrUpdateTempOrder(PurchaseOrderAddForm form) {

		try {

			PurchaseOrderModel model = new PurchaseOrderModel() ;
			BeanUtils.copyProperties(form, model);

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils()
					.addObject(model.getBuId()).empty();

			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			List<PurchaseOrderItemModel> purchaseItems = null;

			// 商品至少选择一个
			if (StringUtils.isNotBlank(form.getItems())) {
				// 输入信息不完整
				purchaseItems = JSONArray.parseArray(form.getItems(), PurchaseOrderItemModel.class) ;
			}

			if (StringUtils.isNotBlank(form.getAttributionDateStr())) {
				model.setAttributionDate(TimeUtils.parse(form.getAttributionDateStr(), "yyyy-MM-dd"));
			}

			if(StringUtils.isNotBlank(form.getDrawInvoiceDateStr())) {
				model.setDrawInvoiceDate(TimeUtils.parse(form.getDrawInvoiceDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getInvoiceDateStr())) {
				model.setInvoiceDate(TimeUtils.parse(form.getInvoiceDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getPayDateStr())) {
				model.setPayDate(TimeUtils.parse(form.getPayDateStr(), "yyyy-MM-dd")) ;
			}

			if(StringUtils.isNotBlank(form.getPaymentDateStr())) {
				model.setPaymentDate(TimeUtils.parse(form.getPaymentDateStr(), "yyyy-MM-dd")); ;
			}
			if(StringUtils.isNotBlank(form.getEstimateDeliverDateStr())){
				model.setEstimateDeliverDate(TimeUtils.parse(form.getEstimateDeliverDateStr(), "yyyy-MM-dd")); ;
			}
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			model.setMerchantId(user.getMerchantId());
			model.setItemList(purchaseItems);

			purchaseOrderService.saveOrModifyTempOrder(model, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		}  catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping("getListPurchaseOrderByPage.asyn")
	@ApiOperation(value = "采购退货新增弹框查询")
	public ResponseBean<PurchaseOrderDTO> getListPurchaseOrderByPage(PurchaseOrderForm form) {
		try {

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;
			BeanUtils.copyProperties(form, dto);

			User user= ShiroUtils.getUserByToken(form.getToken()) ;
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseOrderService.getListPurchaseOrderByPage(dto, form.getCodes(),user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 获取归属时间，和插入必填项校验
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("getAttributionDate.asyn")
	@ApiOperation(value = "获取归属时间")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单Id", required = true)})
	public ResponseBean<String> getAttributionDate(@RequestParam(value="token", required=true)String token,
												   @RequestParam(value="id", required=true)String id) {
		try {

			if(StringUtils.isBlank(id)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			// 响应结果集
			Timestamp attributionDate = purchaseOrderService.getAttributionDate(Long.valueOf(id));

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, TimeUtils.format(attributionDate, "yyyy-MM-dd")) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("updateAttributionDate.asyn")
	@ApiOperation(value = "更新归属时间")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单Id", required = true),
			@ApiImplicitParam(name = "attributionDateStr", value = "归属时间", required = true)})
	public ResponseBean<String> updateAttributionDate(@RequestParam(value="token", required=true)String token,
													  @RequestParam(value="id", required=true)Long id,
													  @RequestParam(value="attributionDateStr", required=true)String attributionDateStr) {
		try {

			PurchaseOrderModel model = new PurchaseOrderModel() ;
			model.setId(id);

			if (id == null ||
					StringUtils.isBlank(attributionDateStr)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			model.setAttributionDate(TimeUtils.parse(attributionDateStr, "yyyy-MM-dd"));

			purchaseOrderService.updateAttributionDate(model);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/checkGoodsInfo.asyn")
	@ApiOperation(value = "采购转销售，检查商品信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "purchaseId", value = "采购订单Id", required = true),
			@ApiImplicitParam(name = "outDepotId", value = "出仓仓库Id", required = true)})
	private ResponseBean<String> checkGoodsInfo(@RequestParam(value="token", required=true)String token,
												@RequestParam(value="purchaseId", required=true)String purchaseId,
												@RequestParam(value="outDepotId", required=true)String outDepotId) {
		String msg = null;
		try{
			User user= ShiroUtils.getUserByToken(token);
			// 响应结果集
			msg = purchaseOrderService.checkGoodsInfo(Long.valueOf(purchaseId),Long.valueOf(outDepotId),user.getMerchantId());

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, msg) ;
		} catch(Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	@GetMapping("/exportTempDateFile.asyn")
	@ApiOperation(value = "导出美赞、宝洁、拜耳合同文件")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "orderCode", value = "采购订单号", required = true),
			@ApiImplicitParam(name = "fileTempCode", value = "文件模版编号", required = true)})
	public void exportTempDateFile(HttpServletResponse response, HttpServletRequest request,
								   @RequestParam(value="token", required=true)String token,
								   @RequestParam(value="orderCode", required=true)String orderCode,
								   @RequestParam(value="fileTempCode", required=true)String fileTempCode) {

		String path = null;
		InputStream input = null ;
		OutputStream out = null;
		try {

			User user = ShiroUtils.getUserByToken(token);

			path = purchaseOrderService.exportTempDataFile(orderCode, fileTempCode, user);
			String fileName = purchaseOrderService.getPDFFileName(orderCode) ;

			File file = new File(path);
			input = new FileInputStream(file);

			FileExportUtil.setHeader(response, request, fileName);
			response.setContentType("application/pdf");
			response.setContentLength(input.available());

			out = response.getOutputStream();

			byte[] b = new byte[input.available()];
			while ((input.read(b)) != -1) {
				out.write(b);
			}

			out.flush();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			response.setContentType("text/html;charset=utf-8");
			PrintWriter writer;
			try {
				writer = response.getWriter();
				writer.println("<html><head></head><script>alert('合同信息未保存，请保存后再导出');</script></html>");
			} catch (IOException e1) {
				LOG.error(e1.getMessage(), e1);
			}

		}finally {

			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}

			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/toSaleStepOnePage.asyn")
	@ApiOperation(value = "跳转采购链路1获取链路信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)})
	public ResponseBean<PurchaseLinkInfoDTO> toSaleStepOnePage(@RequestParam(value="token", required=true)String token,
															   @RequestParam(value="id", required=true)Long id) {

		try {
			PurchaseLinkInfoDTO dto = purchaseOrderService.getPurchaseLinkDtoByPurchaseId(id, null) ;

			DepotInfoMongo depot = depotInfoService.getDetails(dto.getStartPointDepotId());
			if(depot != null) {
				dto.setDepotType(depot.getType());
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping("/backToSaleStepOnePage.html")
	@ApiOperation(value = "返回采购链路1获取链路信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)})
	public ResponseBean<PurchaseLinkInfoDTO> backToSaleStepOnePage(
			@RequestParam(value="token", required=true)String token,
			@RequestParam(value="purchaseTradeLinkId", required=true)Long purchaseTradeLinkId) {

		try {
			PurchaseLinkInfoDTO dto = purchaseOrderService.getPurchaseLinkDtoByPurchaseId(null, purchaseTradeLinkId) ;

			DepotInfoMongo depot = depotInfoService.getDetails(dto.getStartPointDepotId());
			if(depot != null) {
				dto.setDepotType(depot.getType());
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping("/getTradeLinkList.asyn")
	@ApiOperation(value = "获取配置链路信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "purchaseOrderId", value = "采购订单ID", required = true)})
	public ResponseBean<List<TradeLinkConfigModel>> getTradeLinkList(
			@RequestParam(value="token", required=true)String token,
			@RequestParam(value="purchaseOrderId", required=true)Long purchaseOrderId) {

		List<TradeLinkConfigModel> tradeLinkConfigList = null ;

		try {
			tradeLinkConfigList = purchaseOrderService.getTradeLinkList(purchaseOrderId) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, tradeLinkConfigList) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 采购链路第一步保存链路信息
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/saveOrUpdateLinkStepOne.asyn")
	@ApiOperation(value = "采购链路第一步保存链路信息")
	public ResponseBean<String> saveOrUpdateLinkStepOne(PurchaseLinkInfoAddForm form) {

		PurchaseLinkInfoModel model = new PurchaseLinkInfoModel() ;
		BeanUtils.copyProperties(form, model);

		if(StringUtils.isNotBlank(form.getConDeliveryDateStr())) {
			model.setConDeliveryDate(TimeUtils.parse(form.getConDeliveryDateStr(), "yyyy-MM-dd"));
		}

		if(StringUtils.isNotBlank(form.getInfoAuditDateStr())) {
			model.setInfoAuditDate(TimeUtils.parse(form.getInfoAuditDateStr(), "yyyy-MM-dd"));
		}

		if(StringUtils.isNotBlank(form.getInfoShipDateStr())) {
			model.setInfoShipDate(TimeUtils.parse(form.getInfoShipDateStr(), "yyyy-MM-dd"));
		}

		try {

			User user = ShiroUtils.getUserByToken(form.getToken()) ;
			model.setMerchantId(user.getMerchantId());
			model.setMerchantName(user.getMerchantName());

			Long id = purchaseOrderService.saveOrUpdateLinkInfo(model, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, id.toString()) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}

	}

	/**
	 * 采购链路跳转步骤2
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/toSaleStepTwoPage.asyn")
	@ApiOperation(value = "采购链路跳转步骤2获取信息,生成预览订单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购链路ID", required = true)})
	public ResponseBean<PurchaseLinkInfoStepTwoForm> toSaleStepTwoPage(@RequestParam(value="token", required=true)String token,
																	   @RequestParam(value="id", required=true)Long id)  {

		try {
			User user = ShiroUtils.getUserByToken(token);
			/**
			 * 根据ID生成预览订单
			 */
			Map<String, Object> map = purchaseOrderService.generatePreOrder(id, user) ;

			PurchaseLinkInfoStepTwoForm form = new PurchaseLinkInfoStepTwoForm() ;
			form.setId(id);
			form.setMap(map);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, form) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}

	}

	/**
	 * 采购合同选择采购链路跳转步骤2
	 * @param id 采购订单id
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/toJBSaleStepTwoPage.asyn")
	@ApiOperation(value = "采购合同选择采购链路跳转步骤2,生成预览订单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true),
			@ApiImplicitParam(name = "tradeLinkId", value = "采购链路ID", required = true)
	})
	public ResponseBean<PurchaseLinkInfoStepTwoForm> toJBSaleStepTwoPage(@RequestParam(value="token", required=true)String token,
																		 @RequestParam(value="id", required=true)Long id,
																		 @RequestParam(value="tradeLinkId", required=true)Long tradeLinkId) {

		try {

			User user = ShiroUtils.getUserByToken(token);

			/**根据采购订单，生成采购链路信息*/
			Long purchaseLinkInfoId = purchaseOrderService.saveJBPurchaseLinkByPurchaseId(id, user, tradeLinkId);
			Map<String, Object> map = purchaseOrderService.generatePreOrder(purchaseLinkInfoId, user);

			PurchaseLinkInfoStepTwoForm form = new PurchaseLinkInfoStepTwoForm() ;
			form.setId(purchaseLinkInfoId);
			form.setMap(map);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, form) ;


		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}
	}

	/**
	 * 采购链路跳转步骤3
	 * @param
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/toSaleStepThreePage.asyn")
	@ApiOperation(value = "采购链路跳转步骤3")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购链路ID", required = true)})
	public ResponseBean<PurchaseLinkInfoStepThreeForm> toSaleStepThreePage(@RequestParam(value="token", required=true)String token,
																		   @RequestParam(value="id", required=true)Long id) {

		try {

			User user = ShiroUtils.getUserByToken(token);

			PurchaseLinkInfoStepThreeForm form = new PurchaseLinkInfoStepThreeForm() ;

			/**
			 * 根据ID生成预览订单
			 */
			Map<String, Object> map = purchaseOrderService.generatePreOrder(id, user) ;

			form.setId(id);
			form.setMap(map);

			StringBuffer goodsIdSB = new StringBuffer() ;
			StringBuffer goodsNoSB = new StringBuffer() ;
			StringBuffer goodsNumSB = new StringBuffer() ;
			Long outDepotId = null ;

			/**只获取第一个销售订单，作为校验库存依据*/
			for (String merchantName : map.keySet()) {
				Map<String, List> mapList = (Map<String, List>)map.get(merchantName);

				List<SaleOrderModel> saleList = (List<SaleOrderModel>)mapList.get("saleOrderList");

				SaleOrderModel saleOrderModel = saleList.get(0);
				List<SaleOrderItemModel> itemList = saleOrderModel.getItemList();

				outDepotId = saleOrderModel.getOutDepotId() ;

				for (SaleOrderItemModel saleOrderItemModel : itemList) {
					if(goodsIdSB.length() > 0) {
						goodsIdSB = goodsIdSB.append(",") ;
					}

					if(goodsNoSB.length() > 0) {
						goodsNoSB = goodsNoSB.append(",") ;
					}

					if(goodsNumSB.length() > 0) {
						goodsNumSB = goodsNumSB.append(",") ;
					}

					goodsIdSB.append(saleOrderItemModel.getGoodsId().toString()) ;
					goodsNoSB.append(saleOrderItemModel.getGoodsNo()) ;
					goodsNumSB.append(saleOrderItemModel.getNum().toString()) ;
				}

				break ;
			}

			form.setGoodsIds(goodsIdSB.toString());
			form.setGoodsNos(goodsNoSB.toString());
			form.setGoodsNums(goodsNumSB.toString());
			form.setOutDepotId(outDepotId) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, form) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping("saveFirstOrderStatusAndIdepot.asyn")
	@ApiOperation(value = "采购链路，源头订单审核，并入库")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "purchaseTradeLinkId", value = "采购链路ID", required = true)})
	public ResponseBean<String> saveFirstOrderStatusAndIdepot(@RequestParam(value="token", required=true)String token,
															  @RequestParam(value="purchaseTradeLinkId", required=true)Long purchaseTradeLinkId) {

		if(purchaseTradeLinkId == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			User user = ShiroUtils.getUserByToken(token);

			String batchNo = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_BNO);
			Timestamp produceDate = TimeUtils.parseDay(TimeUtils.getCurrentMonthFirstDay());
			Timestamp overdueDate = TimeUtils.addDay(produceDate, 730);

			/**若起点公司的采购单状态待审核或已审核未入库的，系统自动审核自动入库*/
			purchaseOrderService.saveFirstOrderStatusAndIdepot(purchaseTradeLinkId, batchNo, produceDate, overdueDate, user) ;

			Thread.sleep(2000L);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}
	}

	/**
	 * 生成链表订单并出入库
	 * @param purchaseTradeLinkId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("saveLinkOrderAndDepot.asyn")
	@ApiOperation(value = "采购链路，所有链路订单审核，并入库或出库",
			notes="采购链路，所有链路订单审核，并入库或出库，返回所有订单号以及对应状态")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "purchaseTradeLinkId", value = "采购链路ID", required = true)})
	public ResponseBean<List<String>> saveLinkOrderAndDepot(@RequestParam(value="token", required=true)String token,
															@RequestParam(value="purchaseTradeLinkId", required=true)Long purchaseTradeLinkId) {
		if(purchaseTradeLinkId == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			User user = ShiroUtils.getUserByToken(token);

			Map<String, Object> map = purchaseOrderService.generatePreOrder(purchaseTradeLinkId, user) ;

			Map<String, Object> batchMap = purchaseOrderService.getTradeLinkBatchNo(purchaseTradeLinkId) ;

			/**生成订单**/
			Map<String, Object> merchantIdOrderIdsMap = purchaseOrderService.saveLinkOrder(map, batchMap, user, purchaseTradeLinkId);

			/**针对嘉宝-卓普信-宝信的交易链路（ID:1）生成的宝信采购单的单据状态默认为：已审核**/
			purchaseOrderService.modifyJBLinkPurchaseOrder(purchaseTradeLinkId, merchantIdOrderIdsMap, user) ;

			/**休眠5秒后, 等待库存MQ处理上架逻辑**/
			Thread.sleep(4000L);

			/**获取订单号**/
			List<String> orderCode = purchaseOrderService.getLinkOrderCode(merchantIdOrderIdsMap) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, orderCode) ;
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/getPurchasePriceManage.asyn")
	@ApiOperation(value = "查询采购价格管理", notes="根据事业部查询，是否开启采购价格管理")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部ID", required = true)})
	public ResponseBean<Boolean> getPurchasePriceManage(@RequestParam(value="token", required=true)String token,
														@RequestParam(value="buId", required=true)Long buId,
														@RequestParam(value="supplierId", required=true)Long supplierId) {

		if(buId == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			User user = ShiroUtils.getUserByToken(token);

			boolean flag = purchaseOrderService.getPurchasePriceManage(buId, supplierId, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}

	}


	@SuppressWarnings("unchecked")
	@PostMapping("/getDepotGoodsNum.asyn")
	@ApiOperation(value = "若仓库是海外仓，获取商品的箱托数量", notes="仓库是海外仓，则获取商品的箱托数量")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "goodsId", value = "商品id", required = true)})
	public ResponseBean getDepotGoodsNum(@RequestParam(value="token", required=true)String token,
														@RequestParam(value="goodsId", required=true)Long goodsId) {

		try {
			User user = ShiroUtils.getUserByToken(token);
			com.alibaba.fastjson.JSONObject jsonObject = purchaseOrderService.checkGoodsDepotData(goodsId,user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, jsonObject) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
		}

	}



	/**
	 * 采购金额调整模态框获取详情
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("getAmountAdjustmentDetail.asyn")
	@ApiOperation(value = "采购金额调整模态框获取详情")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)})
	public ResponseBean<PurchaseAmountAdjustmentModalForm> getAmountAdjustmentDetail(@RequestParam(value="token", required=true)String token,
																					 @RequestParam(value="id", required=true)Long id) {

		if(id == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			PurchaseAmountAdjustmentModalForm form = new PurchaseAmountAdjustmentModalForm() ;

			Map<String, Object> map = purchaseOrderService.getAmountAdjustmentDetail(id) ;

			List<SdPurchaseConfigItemModel> headerList = (List<SdPurchaseConfigItemModel>)map.get("header") ;
			Map<String, Object> amountMap = (Map<String, Object>)map.get("amountMap") ;
			PurchaseOrderModel order = (PurchaseOrderModel)map.get("order") ;
			List<PurchaseOrderItemModel> itemList = (List<PurchaseOrderItemModel>)map.get("itemList") ;

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;

			BeanUtils.copyProperties(order, dto);
			dto.setItemList(itemList);

			form.setAmountMap(amountMap);
			form.setDetails(dto);
			form.setHeaderList(headerList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, form) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 采购SD金额调整模态框获取详情
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getSdAmountAdjustmentDetail.asyn")
	@ApiOperation(value = "创建采购SD单模态框获取详情")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)})
	public ResponseBean<PurchaseAmountAdjustmentModalForm> getSdAmountAdjustmentDetail(@RequestParam(value="token", required=true)String token,
																					   @RequestParam(value="id", required=true)Long id) {

		if(id == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			PurchaseAmountAdjustmentModalForm form = new PurchaseAmountAdjustmentModalForm() ;

			Map<String, Object> map = purchaseOrderService.getSdAmountAdjustmentDetail(id) ;

			List<SdPurchaseConfigItemModel> headerList = (List<SdPurchaseConfigItemModel>)map.get("header") ;
			Map<String, Object> amountMap = (Map<String, Object>)map.get("amountMap") ;
			PurchaseOrderModel order = (PurchaseOrderModel)map.get("order") ;
			List<PurchaseOrderItemModel> itemList = (List<PurchaseOrderItemModel>)map.get("itemList") ;

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;

			BeanUtils.copyProperties(order, dto);
			dto.setItemList(itemList);

			form.setAmountMap(amountMap);
			form.setDetails(dto);
			form.setHeaderList(headerList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, form) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 采购金额修改
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("saveAmountAdjustment.asyn")
	@ApiOperation(value = "采购金额修改")
	public ResponseBean<String> saveAmountAdjustment(PurchaseOrderAddForm form) {

		try {

			PurchaseOrderModel model = new PurchaseOrderModel() ;
			BeanUtils.copyProperties(form, model);

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getId())
					.addObject(model.getCurrency()).empty();

			if (isNull || StringUtils.isBlank(form.getItems())) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			purchaseOrderService.saveAmountAdjustment(model, form.getItems(), user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 采购金额确认
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("saveConfirmAmountAdjustment.asyn")
	@ApiOperation(value = "采购金额确认")
	public ResponseBean<String> saveConfirmAmountAdjustment(PurchaseOrderAddForm form) {


		try {

			PurchaseOrderModel model = new PurchaseOrderModel() ;
			BeanUtils.copyProperties(form, model);

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getId()).empty();

			if (isNull ) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			purchaseOrderService.saveConfirmAmountAdjustment(model, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 打托入库
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("purchaseDelivery.asyn")
	@ApiOperation(value = "打托入库", consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<String> purchaseDelivery(@RequestBody PurchaseWarehouseForm form) {
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(form.getPurchaseId()).addObject(form.getInboundDate()).empty();

			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
//			PurchaseWarehouseDTO dto = new PurchaseWarehouseDTO();
			User user = ShiroUtils.getUserByToken(form.getToken());
//			BeanUtils.copyProperties(form,dto);
//			if(StringUtils.isNotBlank(form.getInboundDate())){
//				dto.setInboundDate(TimeUtils.parse(form.getInboundDate(), "yyyy-MM-dd"));
//			}

			purchaseOrderService.savePurchaseDelivery(form, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * SD单创建前校验
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("createSdOrderCheck.asyn")
	@ApiOperation(value = "SD单创建前校验")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)
	})
	public ResponseBean<String> createSdOrderCheck(@RequestParam(value="token", required=true) String token ,
												   @RequestParam(value="id", required=true)Long id) {

		if (id == null) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {
			purchaseOrderService.createSdOrderCheck(id) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 获取采购订单详情明细
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("getPurchaseOrderDetails.asyn")
	@ApiOperation(value = "获取采购订单详情明细")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)
	})
	public ResponseBean<String> getPurchaseOrderDetails(@RequestParam(value="token", required=true) String token ,
														@RequestParam(value="id", required=true)Long id) {

		if(id == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {
			PurchaseOrderModel order = purchaseOrderService.searchDetail(id);

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;

			BeanUtils.copyProperties(order, dto);

			dto = purchaseOrderService.getUnInwarehoustNum(dto) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 根据多个采购订单构造生成销售订单详情
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("genSaleOrderByPurchaseIds.asyn")
	@ApiOperation(value = "根据多个采购订单构造生成销售订单详情")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以‘,’隔开", required = true)
	})
	public ResponseBean<String> genSaleOrderByPurchaseIds(@RequestParam(value="token", required=true) String token ,
														  @RequestParam(value="ids", required=true)String ids) {

		try {

			User user=ShiroUtils.getUserByToken(token);

			List<Long> idList = StrUtils.parseIds(ids);


			PurchaseOrderModel order = purchaseOrderService.genSaleOrderByPurchaseIds(idList,user);

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;

			BeanUtils.copyProperties(order, dto);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}


	/**
	 * 根据事业部和客户获取销售价格(采购转销售)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("getCustomerBuIdPrice.asyn")
	@ApiOperation(value = "根据事业部和客户获取销售价格")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "ids", required = true),
			@ApiImplicitParam(name = "customerId", value = "客户id", required = true)
	})
	public ResponseBean<PurchaseOrderDTO> getCustomerBuIdPrice(@RequestParam(value="token", required=true) String token ,
														  @RequestParam(value="id", required=true)String ids,
													 		@RequestParam(value="customerId", required=true)Long customerId) {

		try {

			User user=ShiroUtils.getUserByToken(token);

			PurchaseOrderModel order = purchaseOrderService.getCustomerBuIdPrice(ids,customerId,user);

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;

			BeanUtils.copyProperties(order, dto);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch(DerpException e){
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}


	@SuppressWarnings("unchecked")
	@PostMapping("saveSaleOrder.asyn")
	@ApiOperation(value = "采购转销售")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单ID，多个以‘,’隔开", required = true),
			@ApiImplicitParam(name = "customerId", value = "客户ID", required = true),
			@ApiImplicitParam(name = "items", value = "表体商品明细JSON字符串[{\"goodsNo\":\"商品货号\", \"amount\":\"金额\", \"price\":\"单价\", \"num\":\"数量\"}]", required = true,
					example = "[{\"goodsNo\":\"商品货号\", \"amount\":\"金额\", \"price\":\"单价\", \"num\":\"数量\"}]"),
	})
	public ResponseBean<String> saveSaleOrder(@RequestParam(value="token", required=true) String token ,
											  @RequestParam(value="ids", required=true)String ids,
											  @RequestParam(value="customerId", required=true)Long customerId,
											  @RequestParam(value="poNo", required=true)String poNo,
											  @RequestParam(value="items", required=true)String items) {

		if(StringUtils.isBlank(ids) && customerId == null && StringUtils.isBlank(items) && StringUtils.isBlank(poNo) ) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			User user = ShiroUtils.getUserByToken(token);

			List<Long> idList = StrUtils.parseIds(ids);

			Long saleId = purchaseOrderService.saveSaleOrder(idList, customerId,poNo, items, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, String.valueOf(saleId)) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 检查是否要生成内部供应商对应销售订单
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("checkInnerMerchantSaleOrder.asyn")
	@ApiOperation(value = "检查是否要生成内部供应商对应销售订单",
			notes="检查是否要生成内部供应商对应销售订单,返回flag是否生成销售订单，true-需要，false-无需生成。返回merchantName为客户内部公司名称")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)
	})
	public ResponseBean<Map<String, Object>> checkInnerMerchantSaleOrder(@RequestParam(value="token", required=true) String token ,
																		 @RequestParam(value="id", required=true)Long id) {

		if(id == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {
			Map<String, Object> map = purchaseOrderService.checkInnerMerchantSaleOrder(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 保存内部公司销售订单
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("saveInnerMerchantSaleOrders.asyn")
	@ApiOperation(value = "保存内部公司销售订单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true),
			@ApiImplicitParam(name = "outDepotId", value = "出库仓库ID", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部ID", required = true),
	})
	public ResponseBean<String> saveInnerMerchantSaleOrders(@RequestParam(value="token", required=true) String token ,
															@RequestParam(value="id", required=true)Long id,
															@RequestParam(value="outDepotId", required=true)Long outDepotId,
															@RequestParam(value="buId", required=true)Long buId) {

		if(id == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			User user = ShiroUtils.getUserByToken(token);

			purchaseOrderService.saveInnerMerchantSaleOrders(id, outDepotId, buId, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {

			//销售订单生成失败，采购订单审核成功
			if(e.getObj() != null) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, e.getObj()) ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage()) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 校验链路创建前校验
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("toSaleStepBeforeCheck.asyn")
	@ApiOperation(value = "校验链路创建前校验")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)
	})
	public ResponseBean toSaleStepBeforeCheck(@RequestParam(value="token", required=true)String token,
													  @RequestParam(value="id", required=true)Long id) {

		if(id == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {
			purchaseOrderService.toSaleStepBeforeCheck(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 采购链路保存，修改商品信息
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/saveSaleStepGoodsInfo.asyn")
	@ApiOperation(value = "采购链路保存，修改商品信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "purchaseTradeLinkId", value = "采购订单链路信息ID", required = true),
			@ApiImplicitParam(name = "goodsInfoJson", value = "修改商品JSON", required = true),
	})
	public ResponseBean<String> saveSaleStepGoodsInfo(@RequestParam(value="token" , required=true)String token,
													  @RequestParam(value="purchaseTradeLinkId" , required=true)Long purchaseTradeLinkId,
													  @RequestParam(value="goodsInfoJson" , required=true)String goodsInfoJson) {

		if(purchaseTradeLinkId == null
				|| StringUtils.isBlank(goodsInfoJson)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			purchaseOrderService.saveSaleStepGoodsInfo(purchaseTradeLinkId, goodsInfoJson) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);

			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	/**
	 * 检查采购订单是否存在交易链路配置
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("checkTradeLink.asyn")
	@ApiOperation(value = "检查采购订单是否存在交易链路配置")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单ID", required = true)
	})
	public ResponseBean<Map<String, Object>> checkTradeLink(@RequestParam(value="token" , required=true)String token,
															@RequestParam(value="id" , required=true)Long id) {

		if(id == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {
			Map<String, Object> map = purchaseOrderService.checkTradeLink(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map);
		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10015, e);
		}

	}

	/***
	 * 新增融资单获取货品信息
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getGoodsProductInfo.asyn")
	@ApiOperation(value = "新增融资单获取货品信息")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "goodsId", value = "商品ID", required = true)
	})
	public ResponseBean<Map<String, Object>> getGoodsProductInfo(@RequestParam(value="token" , required=true)String token,
																 @RequestParam(value="goodsId" , required=true)Long goodsId) {

		try {

			if(goodsId == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			Map<String, Object> productInfoMap = purchaseOrderService.getGoodsProductInfo(goodsId) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, productInfoMap) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		}

	}

	/***
	 * 提交卓普信
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/submitSapience.asyn")
	@ApiOperation(value = "提交卓普信")
	public ResponseBean<String> submitSapience(FinancingOrderForm form) {

		try {

			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(form.getBillCurrencCode())
					.addObject(form.getBorrowingDays()).addObject(form.getDepotCode())
					.addObject(form.getDepotName()).addObject(form.getSupplierCode())
					.addObject(form.getSupplierName()).addObject(form.getPoNo())
					.addObject(form.getWarehouseAddress()).addObject(form.getPaymentTermName())
					.addObject(form.getPurchaseOrders())
					.addObject(form.getItems())
					.empty();

			if(isNull) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			String items = form.getItems() ;

			com.alibaba.fastjson.JSONArray jsonArray = (com.alibaba.fastjson.JSONArray)com.alibaba.fastjson.JSONArray.parse(items);

			if(jsonArray.size() <= 0) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "表体信息不能为空");
			}

			User user = ShiroUtils.getUserByToken(form.getToken()) ;

			FinancingOrderDTO dto = new FinancingOrderDTO() ;
			BeanUtils.copyProperties(form, dto);

			if(StringUtils.isNotBlank(form.getExpDeliveryDateStr())){
				dto.setExpDeliveryDate(TimeUtils.parse(form.getExpDeliveryDateStr(), "yyyy-MM-dd"));
			}

			/**判断是否存在融资单，若存在先删除对应融资单信息*/
			purchaseOrderService.delPurchaseFinanceInfo(dto.getPurchaseOrders()) ;

			/**提交卓普信*/
			dto = purchaseOrderService.confirmSubmitSapience(dto, user) ;

			/**生成发票附件*/
			purchaseOrderService.generateZPXInvoice(dto, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		}

	}

	/**
	 * 根据所属类型获取卓普信接口，加载下拉框
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getSapienceApiInfo.asyn")
	@ApiOperation(value = "获取卓普信接口，加载下拉框")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean<String> getSapienceApiInfo(@RequestParam(value="token" , required=true)String token) {

		try {
			User user = ShiroUtils.getUserByToken(token) ;

			FinancingQueryRequest request = new FinancingQueryRequest();
			request.setMethod(EpassAPIMethodEnum.SAPIENCE_E008_METHOD.getMethod());
			request.setSourceCode("10");
			request.setDebtorCode(user.getTopidealCode());
			request.setDebtorName(user.getMerchantName());

			/**若为润佰公司，则将编码默认为2020120101，不取卓志主数据编码*/
			if("1000000626".equals(user.getTopidealCode())) {
				request.setDebtorCode("2020120101");
			}

			net.sf.json.JSONObject requestJson = net.sf.json.JSONObject.fromObject(request);

			String info = SapienceUtils.sendSapience(requestJson);
			JSONObject infoJSON =JSONObject.parseObject(info);
			Integer status = Integer.valueOf(infoJSON.getString("status"));
			if(status.intValue() == 1){
				if (infoJSON.get("notes") != null) {
					return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), infoJSON.getString("notes"));
				}
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), "获取卓普信接口返回异常");
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, infoJSON.getString("result"));

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e.getMessage());
		}

	}

	/**
	 * 提交代采
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/createFinancingOrderCheck.asyn")
	@ApiOperation(value = "检查能否生成代采")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单Ids,多个以‘,’ 隔开", required = true)
	})
	public ResponseBean<String> createFinancingOrderCheck(String token, String ids) {

		try {

			if(StringUtils.isBlank(ids)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			List<Long> idList = (List<Long>)StrUtils.parseIds(ids);

			String financingNos = purchaseOrderService.getCreateFinancingOrderCheck(idList);

			Map<String, Object> map = new HashMap<>() ;
			map.put("financingNos", financingNos) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@PostMapping("/toFinancingPage.asyn")
	@ApiOperation(value = "跳转融资代采页面")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购订单Ids,多个以‘,’ 隔开", required = true)
	})
	public ResponseBean<FinancingOrderDTO> toFinancingPage(String ids) {

		try {
			List<Long> idList = (List<Long>)StrUtils.parseIds(ids);

			FinancingOrderDTO dto = purchaseOrderService.getFinancingOrder(idList) ;

			List<FinancingOrderItemDTO> itemList = dto.getItemList();

			List<Long> goodsId = itemList.stream().map(FinancingOrderItemDTO::getGoodsId).collect(Collectors.toList());

			String unNeedIds = StringUtils.join(goodsId, ",");

			dto.setUnNeedIds(unNeedIds);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), e.getMessage());
		}

	}

	@ApiOperation(value = "批量驳回")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value = "/batchRejection.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean batchRejection(String token,String ids ) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			purchaseOrderService.batchRejection(ids, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "检查相同SKU是否存在多条")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据ids", required = true)
	})
	@PostMapping(value = "/checkRepeatGoods.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Boolean> checkRepeatGoods(String token,Long id ) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			boolean flag = purchaseOrderService.checkRepeatGoods(id);
			if(!flag){
				WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(), "单据存在多条相同SKU!");//未知异常
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "商品导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotId", value = "入库仓id"),
			@ApiImplicitParam(name = "supplierId", value = "供应商id", required = true),
			@ApiImplicitParam(name = "currency", value = "币种", required = true),
			@ApiImplicitParam(name = "buId", value = "事业部", required = true),
			@ApiImplicitParam(name = "purchasePriceManage", value = "是否开启价格管理 true or false", required = true),
			@ApiImplicitParam(name = "tallyingUnit", value = "理货单位"),
			@ApiImplicitParam(name = "stockLocationTypeId", value = "库位类型id")
	})
	@PostMapping(value="/importPurchaseGoods.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importPurchaseGoods(String token,
														@ApiParam(value = "上传的文件", required = true)
														@RequestParam(value = "file", required = true) MultipartFile file, PurchaseOrderAddForm form, Boolean purchasePriceManage, HttpSession session) {
		try{
			Map resultMap = new HashMap();//返回的结果集
			if(file==null){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}
			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if(data == null){//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			PurchaseOrderDTO dto = new PurchaseOrderDTO();
			dto.setDepotId(form.getDepotId());
			dto.setSupplierId(form.getSupplierId());
			dto.setCurrency(form.getCurrency());
			dto.setTallyingUnit(form.getTallyingUnit());
			dto.setBuId(Long.valueOf(form.getBuId()));
			dto.setStockLocationTypeId(form.getStockLocationTypeId());
			resultMap = purchaseOrderService.importPurchaseGoods(data,user,dto,purchasePriceManage);

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("stringList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			uploadResponse.setData(dataList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}


	@ApiOperation(value = "申请红冲校验")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单id", required = true)
	})
	@PostMapping(value = "/validateApply.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Boolean> validateApply(String token, Long id) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			boolean flag = purchaseOrderService.validateApply(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "申请红冲保存")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单id", required = true),
			@ApiImplicitParam(name = "reason", value = "红冲原因", required = true)
	})
	@PostMapping(value = "/saveApplyWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveApplyWriteOff(String token, Long id, String reason) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(reason).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(token);

			purchaseOrderService.saveApplyWriteOff(id, reason, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "审核红冲校验")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单id", required = true)
	})
	@PostMapping(value = "/validateAuditWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<PurchaseWriteOffDTO> validateAuditWriteOff(String token, Long id) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(token);
			PurchaseWriteOffDTO purchaseWriteOffDTO = purchaseOrderService.validateAuditWriteOff(id, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, purchaseWriteOffDTO);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}


	@ApiOperation(value = "审核红冲保存")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单id", required = true),
			@ApiImplicitParam(name = "auditResult", value = "审核结果 0-驳回 1-通过", required = true),
			@ApiImplicitParam(name = "attributiveDate", value = "归属日期, 格式yyyy-MM-dd", required = true)
	})
	@PostMapping(value = "/saveAuditWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveAuditWriteOff(String token, Long id, String auditResult, String attributiveDate) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id).addObject(auditResult).addObject(attributiveDate).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(token);

			purchaseOrderService.saveAuditWriteOff(id, auditResult, attributiveDate, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "进入OA审批页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的单据id,逗号分隔", required = true)
	})
	@PostMapping(value = "/getOAAuditPage.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getOAAuditPage(String token,String ids ) {
		try {
			User user= ShiroUtils.getUserByToken(token);
			boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			ResponseBean responseBean = purchaseOrderService.getOAAuditPage(user, ids);
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "发起OA审批")
	@PostMapping(value = "/callOAAudit.asyn")
	public ResponseBean callOAAudit(@RequestBody PurchaseSaveOAAuditDTO dto) {
		try {
			User user= ShiroUtils.getUserByToken(dto.getToken());
			ResponseBean responseBean = purchaseOrderService.callOAAudit(user, dto);
			return responseBean;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "获取采购订单列表各类型数量", notes="返回类型-数量集合")
	@PostMapping(value="/getPurchaseOrderTypeNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> getPurchaseOrderTypeNum(PurchaseOrderForm form) {
		try {
			User user= ShiroUtils.getUserByToken(form.getToken()) ;

			PurchaseOrderDTO dto = new PurchaseOrderDTO() ;
			BeanUtils.copyProperties(form, dto);

			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<Map<String, Object>> resultMap = purchaseOrderService.getPurchaseOrderTypeNum(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@ApiOperation(value = "获取合同列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "codes", value = "选中的单据编码,逗号分隔", required = true)
	})
	@PostMapping(value = "/listContract.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean listContract(String token, String codes) {
		try {
			User user= ShiroUtils.getUserByToken(token);
			boolean isNull = new EmptyCheckUtils().addObject(codes).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
			purchaseOrderDTO.setMerchantId(user.getMerchantId());
			purchaseOrderDTO.setCodes(codes);
			List<AttachmentInfoMongo> attachmentInfoMongos = purchaseOrderService.listContract(user, purchaseOrderDTO);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, attachmentInfoMongos);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
}

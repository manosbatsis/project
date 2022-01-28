/*
package com.topideal.order.web.purchase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.topideal.api.sapience.SapienceUtils;
import com.topideal.api.sapience.sapience008.FinancingQueryRequest;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.dto.purchase.FinancingOrderDTO;
import com.topideal.entity.dto.purchase.FinancingOrderItemDTO;
import com.topideal.entity.dto.purchase.PurchaseLinkInfoDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.vo.common.TradeLinkConfigModel;
import com.topideal.entity.vo.purchase.PurchaseContractModel;
import com.topideal.entity.vo.purchase.PurchaseLinkInfoModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.base.PackTypeService;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseOrderService;
import com.topideal.order.shiro.ShiroUtils;

import net.sf.json.JSONObject;

*/
/**
 * 采购订单 控制层
 * 
 * @author zhanghx
 *//*

@RequestMapping("/purchase")
@Controller
public class PurchaseOrderController {
	
	private static final Logger LOG = Logger.getLogger(PurchaseOrderController.class) ;

	private static final String[] MAIN_COLUMNS = {"采购订单编号", "订单状态", "入库仓库", "事业部", "供应商", 
			"是否内部公司供应商", "付款主体", "PO号", "合同号", "采购币种", "采购总金额", "汇率", "理货单位", "是否已生成预申报单", 
			"融资申请号", "是否完结", "完结入库时间", "LBX单号", "收货地点", "目的地名称", "业务模式", "预计入仓时间", "提单号", "预计付款时间", 
			"装船时间", "装货港", "卸货港", "提单毛重", "运输方式", "车次", "标准箱TEU",
			"托数", "承运商", "在途起始日期", "创建人", "创建时间", "审核人", "审核时间" };
	
	private static final String[] MAIN_KEYS = {"code", "statusLabel", "depotName", "buName", "supplierName", 
			"isInnerMerchant", "paySubjectLabel","poNo", "contractNo", "currencyLabel", "amount", "rate", "tallyingUnitLabel", "isGenerateLabel",
			"financingNo", "isEndLabel", "endDate", "lbxNo", "deliveryAddr", "destinationName", "businessModelLabel", "arriveDepotDate",
			"ladingBill", "paymentDate", "shipDate", "loadPort", "unloadPort", "grossWeight", "transportLabel",
			"trainNumber", "standardCaseTeu", "torrNum", "carrier", "cargoCuttingDate", "createName", "createDate", "auditName", "auditDate"} ;

	private static final String[] ITEM_COLUMNS = {"采购订单编号", "商品货号", "商品名称", "商品条码", "采购数量",
			"采购单价", "采购总金额(不含税)", "采购总金额(含税)", "税率", "税款", "品牌类型", "毛重", "净重", "箱号", "生产批次号", "成分占比"} ;
	
	private static final String[] ITEM_KEYS = {"orderCode", "goodsNo", "goodsName", "barcode", "num",
			"price", "amount", "taxAmount", "taxRate", "tax", "brandName", "grossWeight", "netWeight", "boxNo", "batchNo", "constituentRatio"} ;
	
	// 采购订单service
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;
	@Autowired
	private PackTypeService packTypeService;
	@Autowired
	private CommonBusinessService commonBusinessService ;

	*/
/**
	 * 访问列表页面
	 * 
	 * @param model
	 *//*

	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/purchase/purchase-list";
	}

	*/
/**
	 * 访问新增页面
	 * @param model
	 * @param ids  销售订单id 多个以“,”隔开
	 *//*

	@RequestMapping("/toAddPage.html")
	public String toAddPage(Model model, String ids, String type, String pageSource,Long modalSupplierId,
							Long isUse,String purchaseCommission) throws Exception {
		//销售订单、预售单、采购订单复制id不为空，则获取信息
		if(StringUtils.isNotBlank(ids)){
			Map<String,Object> map = purchaseOrderService.listCopyOrSaleToOrder(ids, type,modalSupplierId,isUse,purchaseCommission);
			PurchaseOrderModel purchaseOrder = (PurchaseOrderModel) map.get("purchaseOrder");
			String unNeedIds = (String) map.get("unNeedIds");
			DepotInfoMongo depot = depotInfoService.getDetails(purchaseOrder.getDepotId());
			model.addAttribute("depotType", depot.getType());
			model.addAttribute("detail", purchaseOrder);
			model.addAttribute("unNeedIds", unNeedIds);
			model.addAttribute("type", type);
		}
		//跳转页面入口：1-首页 2-订单列表页
		if (StringUtils.isNotBlank(pageSource)) {
			model.addAttribute("pageSource", pageSource);
		}
		User user= ShiroUtils.getUser(); 
		model.addAttribute("merchantName", user.getMerchantName());
		List<SelectBean> packTypeBean = packTypeService.getSelectBean();
		model.addAttribute("packTypeBean", packTypeBean);
		return "/derp/purchase/purchase-add";
	}

	*/
/**
	 * 访问编辑页面
	 * 
	 * @param model
	 *//*

	@RequestMapping("/toEditPage.html")
	public String toEditPage(Model model, Long id, String pageSource) throws Exception {
		PurchaseOrderDTO dto = purchaseOrderService.searchDTODetail(id);
		model.addAttribute("detail", dto);
		DepotInfoMongo depot = depotInfoService.getDetails(dto.getDepotId());

		if(depot != null){
			model.addAttribute("depotType", depot.getType());
		}

		if(depot!=null && depot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_A) && DERP_SYS.DEPOTINFO_ISTOPBOOKS_1.equals(depot.getIsTopBooks())){
			model.addAttribute("contractNo_required", "1");
		}else{
			model.addAttribute("contractNo_required", "0");
		}

		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		if (dto.getArriveDepotDate() != null) {
			String arriveDepotDate = sft.format(dto.getArriveDepotDate());
			model.addAttribute("arriveDepotDate", arriveDepotDate);
		}
		if (dto.getShipDate() != null) {
			String shipDate = sft.format(dto.getShipDate());
			model.addAttribute("shipDate", shipDate);
		}
		if (dto.getPaymentDate() != null) {
			String paymentDate = sft.format(dto.getPaymentDate());
			model.addAttribute("paymentDate", paymentDate);
		}
		if (dto.getArriveDate() != null) {
			String arriveDate = sft.format(dto.getArriveDate());
			model.addAttribute("arriveDate", arriveDate);
		}
		User user= ShiroUtils.getUser(); 
		model.addAttribute("merchantName", user.getMerchantName());
		List<SelectBean> packTypeBean = packTypeService.getSelectBean();
		model.addAttribute("packTypeBean", packTypeBean);
		//跳转页面入口：1-首页 2-订单列表页
		if (StringUtils.isNotBlank(pageSource)) {
			model.addAttribute("pageSource", pageSource);
		}
		return "/derp/purchase/purchase-edit";
	}

	*/
/**
	 * 访问批量导入页面
	 *//*

	@RequestMapping("/toImportPage.html")
	public String toImportPage() {
		return "/derp/purchase/purchase-import";
	}
	
	*/
/**
	 * 跳转代采页面
	 *//*

	@SuppressWarnings("unchecked")
	@RequestMapping("/toFinancingPage.html")
	public String toFinancingPage(String ids, Model model) {
		
		try {
			List<Long> idList = (List<Long>)StrUtils.parseIds(ids);
			
			FinancingOrderDTO dto = purchaseOrderService.getFinancingOrder(idList) ;
			
			List<FinancingOrderItemDTO> itemList = dto.getItemList();
			
			List<Long> goodsId = itemList.stream().map(FinancingOrderItemDTO::getGoodsId).collect(Collectors.toList());
			
			String unNeedIds = StringUtils.join(goodsId, ",");
			
			model.addAttribute("detail", dto) ;
			model.addAttribute("unNeedIds", unNeedIds) ;
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
		return "/derp/purchase/purchase-financingOrder-add";
	}
	
	*/
/**
	 * 根据所属类型获取卓普信接口，加载下拉框
	 * @param type
	 * @return
	 *//*

	@RequestMapping("/getSapienceApiInfo.asyn")
	@ResponseBody
	public ViewResponseBean getSapienceApiInfo() {
		
		try {
			User user = ShiroUtils.getUser();
			
			FinancingQueryRequest request = new FinancingQueryRequest();
			request.setMethod(EpassAPIMethodEnum.SAPIENCE_E008_METHOD.getMethod());
			request.setSourceCode("10");
			request.setDebtorCode(user.getTopidealCode());
			request.setDebtorName(user.getMerchantName());
			
			JSONObject requestJson = JSONObject.fromObject(request);
			
			String info = SapienceUtils.sendSapience(requestJson);
			
			return ResponseFactory.success(info) ;
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e) ;
		}
        
	}
	
	*/
/**
	 * 提交代采
	 * @param ids
	 * @return
	 * @throws Exception
	 *//*

	@SuppressWarnings("unchecked")
	@RequestMapping("/createFinancingOrderCheck.asyn")
	@ResponseBody
	public ViewResponseBean createFinancingOrderCheck(String ids) {
		
		try {
			
			if(StringUtils.isBlank(ids)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303) ;
			}
			
			List<Long> idList = (List<Long>)StrUtils.parseIds(ids);
			
			purchaseOrderService.getCreateFinancingOrderCheck(idList) ;
			
			return ResponseFactory.success() ;
			
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		
	}

	*/
/**
	 * 访问详情页面
	 * 
	 * @param model
	 * @param id
	 *            采购订单id
	 *//*

	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id, String pageSource) throws Exception {
		PurchaseOrderDTO dto = purchaseOrderService.searchDTODetail(id);
		model.addAttribute("detail", dto);
		DepotInfoMongo depot = depotInfoService.getDetails(dto.getDepotId());

		if(depot != null){
			model.addAttribute("depotType", depot.getType());
		}

		// 日期转换
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sft2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (dto.getArriveDepotDate() != null) {
			String arriveDepotDate = sft.format(dto.getArriveDepotDate());
			model.addAttribute("arriveDepotDate", arriveDepotDate);
		}
		if (dto.getShipDate() != null) {
			String shipDate = sft.format(dto.getShipDate());
			model.addAttribute("shipDate", shipDate);
		}
		if (dto.getPaymentDate() != null) {
			String paymentDate = sft.format(dto.getPaymentDate());
			model.addAttribute("paymentDate", paymentDate);
		}
		if (dto.getCreateDate() != null) {
			String createDate = sft2.format(dto.getCreateDate());
			model.addAttribute("createDate", createDate);
		}
		if (dto.getSubmitDate() != null) {
			String submitDate = sft2.format(dto.getSubmitDate());
			model.addAttribute("submitDate", submitDate);
		}
		if (dto.getAuditDate() != null) {
			String auditDate = sft2.format(dto.getAuditDate());
			model.addAttribute("auditDate", auditDate);
		}
		if (dto.getArriveDate() != null) {
			String arriveDate = sft.format(dto.getArriveDate());
			model.addAttribute("arriveDate", arriveDate);
		}
		User user= ShiroUtils.getUser(); 
		model.addAttribute("merchantName", user.getMerchantName());
		//跳转页面入口：1-首页 2-订单列表页
		if (StringUtils.isNotBlank(pageSource)) {
			model.addAttribute("pageSource", pageSource);
		}
		return "/derp/purchase/purchase-details";
	}
	
	*/
/**
	 * 访问采购合同编辑页面
	 * @throws SQLException 
	 *//*

	@RequestMapping("/toEditContractPage.html")
	public String toEditContractPage(Model model, Long id, Long tempId) throws SQLException {
		String fileTempCode = purchaseOrderService.getFileTempCode(tempId) ;
		
		if("Bayer".equals(fileTempCode)) {
			
			Map<String, Object> map = purchaseOrderService.getBayerContractByPurchaseId(id) ;
			
			model.addAllAttributes(map) ;
			model.addAttribute("id", id) ;
			
			return "/derp/purchase/purchase-bayer-contract-edit";
		}else if("PG".equals(fileTempCode)) {
			Map<String, Object> map = purchaseOrderService.getPGContractByPurchaseId(id) ;
			
			model.addAllAttributes(map) ;
			model.addAttribute("id", id) ;
			
			return "/derp/purchase/purchase-pg-contract-edit";
		}else if("mead".equals(fileTempCode)) {
			Map<String, Object> map = purchaseOrderService.getMeadContractByPurchaseId(id) ;
			
			model.addAllAttributes(map) ;
			model.addAttribute("id", id) ;
			
			return "/derp/purchase/purchase-mead-contract-edit";
		}else {
			PurchaseContractModel contractModel = purchaseOrderService.getContractModelByPurchaseId(id) ;
			List<PurchaseOrderItemModel> itemList = purchaseOrderService.getPurchaseContractItem(id);
			//计算商品明细总量、总价
			Map<String, Object> totalMap = purchaseOrderService.sumContractTotal(itemList) ;
			
			String currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, contractModel.getCurrency()) ;
			
			model.addAttribute("detail", contractModel);
			model.addAttribute("itemList", itemList);
			model.addAttribute("totalMap", totalMap);
			model.addAttribute("currencyLabel", currencyLabel);
			
			return "/derp/purchase/purchase-contract-edit";
		}
	}
	
	*/
/**
	 * 访问采购合同详情页面
	 * @throws SQLException 
	 *//*

	@RequestMapping("/toDetailContractPage.html")
	public String toDetailContractPage(Model model, Long id, Long tempId) throws SQLException {
		
		String fileTempCode = purchaseOrderService.getFileTempCode(tempId) ;
		
		if("Bayer".equals(fileTempCode)) {
			
			Map<String, Object> map = purchaseOrderService.getBayerContractByPurchaseId(id) ;
			
			model.addAllAttributes(map) ;
			model.addAttribute("id", id) ;
			
			return "/derp/purchase/purchase-bayer-contract-details";
		}else if("PG".equals(fileTempCode)) {
			Map<String, Object> map = purchaseOrderService.getPGContractByPurchaseId(id) ;
			
			model.addAllAttributes(map) ;
			model.addAttribute("id", id) ;
			
			return "/derp/purchase/purchase-pg-contract-details";
		}else if("mead".equals(fileTempCode)) {
			Map<String, Object> map = purchaseOrderService.getMeadContractByPurchaseId(id) ;
			
			model.addAllAttributes(map) ;
			model.addAttribute("id", id) ;
			
			return "/derp/purchase/purchase-mead-contract-details";
		}else {
			*/
/**待改造**//*

			PurchaseContractModel contractModel = purchaseOrderService.getContractModelByPurchaseId(id) ;
			List<PurchaseOrderItemModel> itemList = purchaseOrderService.getPurchaseContractItem(id);
			//计算商品明细总量、总价
			Map<String, Object> totalMap = purchaseOrderService.sumContractTotal(itemList) ;
			
			String currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, contractModel.getCurrency()) ;
			
			model.addAttribute("detail", contractModel);
			model.addAttribute("itemList", itemList);
			model.addAttribute("totalMap", totalMap);
			model.addAttribute("currencyLabel", currencyLabel);
			
			return "/derp/purchase/purchase-contract-details";
			*/
/**待改造**//*

		}
		
	}
	
	*/
/**
	 * 打托入库页面
	 * 
	 * @param model
	 *//*

	@RequestMapping("/toPurchaseInPage.html")
	public String toPurchaseInPage(Long id, Model model) throws Exception {
		
		PurchaseOrderDTO dto = purchaseOrderService.searchDTODetail(id);
		
		model.addAttribute("detail", dto);
		model.addAttribute("itemList", dto.getItemList());
		
		return "/derp/purchase/purchase-in";
	}
	
	*/
/**
	 * 访问采购合同编辑提交
	 * @throws SQLException 
	 *//*

	@RequestMapping("/modifyPurchaseContract.asyn")
	@ResponseBody
	public ViewResponseBean modifyPurchaseContract(PurchaseContractModel model, String deliveryDateStr) {
		try {
			
			Timestamp deliveryDate = TimeUtils.parse(deliveryDateStr, "yyyy-MM-dd");
			model.setDeliveryDate(deliveryDate);
			
			// 响应结果集
			purchaseOrderService.modifyPurchaseContract(model);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	*/
/**
	 * 访问采购合同编辑提交
	 * @throws SQLException 
	 *//*

	@RequestMapping("/modifyJsonPurchaseContract.asyn")
	@ResponseBody
	public ViewResponseBean modifyJsonPurchaseContract(@RequestBody Map<String, Object> map) {
		try {
			
			if(map == null) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			User user = ShiroUtils.getUser();
			
			// 响应结果集
			purchaseOrderService.modifyJSONPurchaseContract(map, user);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	*/
/**
	 * 访问采购合同编辑提交并审核采购订单
	 * @throws SQLException 
	 *//*

	@RequestMapping("/auditJSONPurchaseContract.asyn")
	@ResponseBody
	public ViewResponseBean auditJSONPurchaseContract(@RequestBody Map<String, Object> map) {
		try {
			
			if(map == null) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			String status = String.valueOf(map.get("status")) ;
			
			if(StringUtils.isBlank(status)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			User user = ShiroUtils.getUser() ;
			// 响应结果集
			Long orderId = purchaseOrderService.auditJSONPurchaseContract(map, user, status);
			
			*/
/**判断是否嘉宝贸易*//*

			
			if("1000000594".equals(user.getTopidealCode())) {
				return ResponseFactory.success(orderId);
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	*/
/**
	 * 访问（美赞、宝洁、拜耳）采购合同编辑提交并提交采购订单
	 * @throws SQLException 
	 *//*

	@RequestMapping("/submitJSONPurchaseContract.asyn")
	@ResponseBody
	public ViewResponseBean submitJSONPurchaseContract(@RequestBody Map<String, Object> map) {
		try {
			
			*/
/**判断是否嘉宝贸易*//*

			User user = ShiroUtils.getUser();
			
			// 响应结果集
			purchaseOrderService.submitJSONPurchaseContract(map, user);
			
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	*/
/**
	 * 访问采购合同编辑提交并提交采购订单
	 * @throws SQLException 
	 *//*

	@RequestMapping("/submitPurchaseContract.asyn")
	@ResponseBody
	public ViewResponseBean submitPurchaseContract(PurchaseContractModel model, String deliveryDateStr) {
		try {
			
			Timestamp deliveryDate = TimeUtils.parse(deliveryDateStr, "yyyy-MM-dd");
			model.setDeliveryDate(deliveryDate);
			
			*/
/**判断是否嘉宝贸易*//*

			User user = ShiroUtils.getUser();
			
			// 响应结果集
			purchaseOrderService.submitPurchaseContract(model, user);
			
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	*/
/**
	 * 访问采购合同编辑提交并审核采购订单
	 * @throws SQLException 
	 *//*

	@RequestMapping("/auditPurchaseContract.asyn")
	@ResponseBody
	public ViewResponseBean auditPurchaseContract(PurchaseContractModel model, String deliveryDateStr, String status) {
		try {
			
			Timestamp deliveryDate = TimeUtils.parse(deliveryDateStr, "yyyy-MM-dd");
			model.setDeliveryDate(deliveryDate);
			
			*/
/**判断是否嘉宝贸易*//*

			User user = ShiroUtils.getUser();
			
			// 响应结果集
			purchaseOrderService.auditPurchaseContract(model, user, status);
			
			if("1000000594".equals(user.getTopidealCode())) {
				return ResponseFactory.success(model.getOrderId());
			}
			
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	*/
/**
	 * 获取分页数据
	 * 
	 * @param model
	 *//*

	@RequestMapping("/listPurchaseOrder.asyn")
	@ResponseBody
	private ViewResponseBean listPurchaseOrder(PurchaseOrderDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseOrderService.listPurchaseOrderPage(dto, user);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	*/
/**
	 * 新增
	 * 
	 * @param model
	 *            订单
	 * @param contractNos
	 *            新增子合同号集合
	 * @param ids
	 *            新增商品id集合
	 * @param nums
	 *            新增商品数量集合
	 * @param prices
	 *            新增商品单价集合
	 * @param boxNos
	 *            新增箱号集合
	 * @param batchNos
	 *            新增批次号集合
	 * @param ratios
	 *            新增成分占比集合
	 * @param brandNames
	 *            新增品牌名称集合
	 *//*

	@RequestMapping("/savePurchaseOrder.asyn")
	@ResponseBody
	public ViewResponseBean savePurchaseOrder(PurchaseOrderModel model, String items,
			String arriveDepotDate2, String paymentDate2,
			String shipDate2, String arriveDate2) {
		String result = "";
		try {
			// 保存修改必填项空值校验，包括提交，审核
			boolean isNull = new EmptyCheckUtils().addObject(model.getSupplierId())
					.addObject(model.getPaySubject()).addObject(model.getCurrency())
					.addObject(model.getPoNo()).addObject(model.getAttributionDate())
					.addObject(model.getDepotId()).addObject(model.getBuId()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<PurchaseOrderItemModel> purchaseItems = JSONArray.parseArray(items, PurchaseOrderItemModel.class);
			
			// 商品至少选择一个
			if (purchaseItems.isEmpty()) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.isNotBlank(arriveDepotDate2)) {
				Date date = sdf.parse(arriveDepotDate2);
				model.setArriveDepotDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(paymentDate2)) {
				Date date = sdf.parse(paymentDate2);
				model.setPaymentDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(shipDate2)) {
				Date date = sdf.parse(shipDate2);
				model.setShipDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(arriveDate2)) {
				Date date = sdf.parse(arriveDate2);
				model.setArriveDate(new Timestamp(date.getTime()));
			}
			
			User user= ShiroUtils.getUser();
			// 设置商家id
			model.setMerchantId(user.getMerchantId());
			
			model.setItemList(purchaseItems);
			
			result = purchaseOrderService.savePurchaseOrder(model, user);
			
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305);
		}
		return ResponseFactory.success(result);
	}

	*/
/**
	 * 编辑
	 * 
	 * @param model
	 *            订单
	 * @param contractNos
	 *            编辑子合同号集合
	 * @param ids
	 *            表体id集合
	 * @param goodsIds
	 *            编辑商品id集合
	 * @param nums
	 *            编辑商品数量集合
	 * @param prices
	 *            编辑商品单价集合
	 * @param boxNos
	 *            编辑箱号集合
	 * @param batchNos
	 *            编辑批次号集合
	 * @param ratios
	 *            编辑成分占比集合
	 * @param brandNames编辑品牌名称集合
	 *//*

	@RequestMapping("/modifyPurchaseOrder.asyn")
	@ResponseBody
	public ViewResponseBean modifyPurchaseOrder(PurchaseOrderModel model, String items,
			String arriveDepotDate2, String paymentDate2,
			String shipDate2, String oldLbxNo, String oldContractNo, 
			String arriveDate2) {
		String result = "";
		try {
			// 保存修改必填项空值校验，包括提交，审核
			boolean isNull = new EmptyCheckUtils().addObject(model.getSupplierId())
					.addObject(model.getPaySubject()).addObject(model.getCurrency())
					.addObject(model.getPoNo()).addObject(model.getAttributionDate())
					.addObject(model.getDepotId()).addObject(model.getBuId()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<PurchaseOrderItemModel> purchaseItems = JSONArray.parseArray(items, PurchaseOrderItemModel.class);
			
			// 商品至少选择一个
			if (purchaseItems.isEmpty()) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.isNotBlank(arriveDepotDate2)) {
				Date date = sdf.parse(arriveDepotDate2);
				model.setArriveDepotDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(paymentDate2)) {
				Date date = sdf.parse(paymentDate2);
				model.setPaymentDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(shipDate2)) {
				Date date = sdf.parse(shipDate2);
				model.setShipDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(arriveDate2)) {
				Date date = sdf.parse(arriveDate2);
				model.setArriveDate(new Timestamp(date.getTime()));
			}
			User user= ShiroUtils.getUser(); 
			
			model.setItemList(purchaseItems);
			
			result = purchaseOrderService.modifyPurchaseOrder(model, user, oldLbxNo, oldContractNo);
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305);
		}
		return ResponseFactory.success(result);
	}

	*/
/**
	 * 完结入库校验
	 * 
	 * @param ids
	 *//*

	@SuppressWarnings("unchecked")
	@RequestMapping("/endPurchaseOrderCheck.asyn")
	@ResponseBody
	public ViewResponseBean endPurchaseOrderCheck(String ids) {
		String result = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			result = purchaseOrderService.endPurchaseOrderCheck(list);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	*/
/**
	 * 完结入库
	 * 
	 * @param ids
	 *//*

	@SuppressWarnings("unchecked")
	@RequestMapping("/endPurchaseOrder.asyn")
	@ResponseBody
	public ViewResponseBean endPurchaseOrder(String ids, HttpSession session) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			boolean b = purchaseOrderService.endPurchaseOrder(list);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	*/
/**
	 * 中转仓入库
	 * 
	 * @param ids
	 *//*

	@SuppressWarnings("unchecked")
	@RequestMapping("/inWarehouse.asyn")
	@ResponseBody
	public ViewResponseBean inWarehouse(String ids,String inWarehouseDateStr) {
		String result = "";
		try {
			User user= ShiroUtils.getUser(); 
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			List<InvetAddOrSubtractRootJson> jsonList = purchaseOrderService.saveInWarehouse(list, user.getId(), user.getName(), user.getTopidealCode(),inWarehouseDateStr);
			
			boolean flag = true ;
			
			for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonList) {
				try {
					commonBusinessService.saveAutoPurchaseAnalysis(invetAddOrSubtractRootJson.getOrderNo());
				} catch (DerpException e) {
					
					flag &= false ;
					
					if(result.length() > 0) {
						result += "<br>" ;
					}
					
					result += e.getMessage() ;
					
					commonBusinessService.modifyCorrelationstatus((PurchaseWarehouseModel)e.getObj());
				}
			}
			
			if(flag) {
				purchaseOrderService.pushInventory(jsonList) ;
			}
			
			if(result.length() == 0) {
				result = "成功" ;
			}
			
			return ResponseFactory.success(result);
		
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}

	*/
/**
	 * 删除
	 * 
	 * @param ids
	 *//*

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/delPurchaseOrder.asyn")
	@ResponseBody
	public ViewResponseBean delPurchaseOrder(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List list = StrUtils.parseIds(ids);
			boolean b = purchaseOrderService.delPurchaseOrder(list);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (RuntimeException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_301, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}

	*/
/**
	 * 生成预申报单
	 * 
	 * @param ids
	 *//*

	@SuppressWarnings("unchecked")
	@RequestMapping("/generateDeclareOrder.asyn")
	@ResponseBody
	public ViewResponseBean generateDeclareOrder(String ids, HttpSession session) {
		String result = "";
		try {
			User user= ShiroUtils.getUser();
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			result = purchaseOrderService.saveDeclareOrder(list, user.getId(), user.getName(), user.getMerchantName(), user.getMerchantId());
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}

	*/
/**
	 * 导入
	 * 
	 * @param
	 * @return int
	 * @throws IOException
	 *//*

	@SuppressWarnings("rawtypes")
	@RequestMapping("/importPurchase.asyn")
	@ResponseBody
	public ViewResponseBean importPurchase(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpSession session) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			
			resultMap = purchaseOrderService.importPurchaseOrder(data, user);
			
		} catch (NullPointerException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305);
		}
		
		return ResponseFactory.success(resultMap);
	}

	*/
/**
	 * 采购导出
	 * @param
	 * @return int
	 * @throws IOException
	 *//*

	@SuppressWarnings("unchecked")
	@RequestMapping("/exportPurchase.asyn")
	public void exportRelation(HttpServletResponse response, HttpServletRequest request,
			PurchaseOrderExportDTO dto) throws Exception {
		User user= ShiroUtils.getUser(); 
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
	
	*/
/**
	 * 采购合同导出PDF
	 * @param
	 * @return int
	 * @throws IOException
	 *//*

	@RequestMapping("/exportPurchaseContractPdf.asyn")
	public void exportPurchaseContractPdf(HttpServletResponse response, HttpServletRequest request,
			String orderId) throws Exception {
		
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

	*/
/**
	 * 获取详情
	 * @param model
	 *//*

	@RequestMapping("/getPurchaseOrder.asyn")
	@ResponseBody
	private ViewResponseBean getPurchaseOrder(PurchaseOrderModel model, String type, HttpSession session) {
		String result = "";
		try {
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			model.setMerchantId(user.getMerchantId());
			// 响应结果集
			result = purchaseOrderService.checkContractNoAndPoNo(model);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	
	*/
/**
	 * 获取仓库详情
	 * @param model
	 *//*

	@RequestMapping("/getDepotInfo.asyn")
	@ResponseBody
	private ViewResponseBean getDepotInfo(String depotId, HttpSession session) {
		String result = "";
		try {
			// 响应结果集
			ViewResponseBean depotInfo = purchaseOrderService.getDepotInfo(depotId);
			result = (String) depotInfo.getData();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(result);
	}
	
	// 允许前端传进来的timestamp类型的值为空
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
	*/
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
	 *//*

	@RequestMapping("/updatePurchaseOrderInvoice.asyn")
	@ResponseBody
	public ViewResponseBean updatePurchaseOrderInvoice(String invoiceDate,String invoiceName,String drawInvoiceDate,Long id,
			String payName,String payDate, String tag,String invoiceNo , 
			String goodsNos , String amounts){								
		try {
			String result = purchaseOrderService.updatePurchaseOrderInvoice(invoiceDate, invoiceName,drawInvoiceDate, id, payName, payDate, tag, invoiceNo, goodsNos, amounts);
			
			return ResponseFactory.success(result) ;
			
		} catch (SQLException e) {	
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);			
		}
		
	}
	
	*/
/**
	 * 根据采购订单Id获取采购商品
	 * @param id
	 * @return
	 *//*

	@RequestMapping("/getPurchaseItem.asyn") 
	@ResponseBody
	public ViewResponseBean getPurchaseItem(String id) {
		if(StringUtils.isBlank(id)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		Long purchaseId = Long.valueOf(id) ;
		
		try {
			List<PurchaseOrderItemModel> items = purchaseOrderService.getPurchaseItem(purchaseId) ;
			return ResponseFactory.success(items) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	
		
	}



	*/
/**
	 * 保存时只校验仓库、商品是否有值，这两个字段用户有输入值即可保存不做其他任何校验
	 * @Param
	 * @return
	 *//*

	@RequestMapping("/saveOrUpdateTempOrder.asyn")
	@ResponseBody
	public ViewResponseBean saveOrUpdateTempOrder(PurchaseOrderModel model, String items, 
			String arriveDepotDate2, String paymentDate2,
			String shipDate2, String oldLbxNo, String oldContractNo, 
			String arriveDate2) {
		
		try {
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getBuId()).empty();
			
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<PurchaseOrderItemModel> purchaseItems = JSONArray.parseArray(items, PurchaseOrderItemModel.class);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StringUtils.isNotBlank(arriveDepotDate2)) {
				Date date = sdf.parse(arriveDepotDate2);
				model.setArriveDepotDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(paymentDate2)) {
				Date date = sdf.parse(paymentDate2);
				model.setPaymentDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(shipDate2)) {
				Date date = sdf.parse(shipDate2);
				model.setShipDate(new Timestamp(date.getTime()));
			}
			if (StringUtils.isNotBlank(arriveDate2)) {
				Date date = sdf.parse(arriveDate2);
				model.setArriveDate(new Timestamp(date.getTime()));
			}
			User user= ShiroUtils.getUser();
			// 设置商家id
			model.setMerchantId(user.getMerchantId());

			model.setItemList(purchaseItems);

			purchaseOrderService.saveOrModifyTempOrder(model, user, oldLbxNo, oldContractNo);
			
		}  catch (DerpException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305);
		}
		
		return ResponseFactory.success();
	}
	
	@RequestMapping("getListPurchaseOrderByPage.asyn")
	@ResponseBody
	public ViewResponseBean getListPurchaseOrderByPage(PurchaseOrderDTO dto, String codes) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseOrderService.getListPurchaseOrderByPage(dto, codes);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	*/
/**
	 * 获取归属时间，和插入必填项校验
	 * @param id
	 * @return
	 *//*

	@RequestMapping("getAttributionDate.asyn")
	@ResponseBody
	public ViewResponseBean getAttributionDate(String id) {
		try {
			
			if(StringUtils.isBlank(id)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			// 响应结果集
			Timestamp attributionDate = purchaseOrderService.getAttributionDate(Long.valueOf(id));
			return ResponseFactory.success(attributionDate);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}
	
	@RequestMapping("updateAttributionDate.asyn")
	@ResponseBody
	public ViewResponseBean updateAttributionDate(PurchaseOrderModel model) {
		try {
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(model.getId()).addObject(model.getAttributionDate()).empty();
			if (isNull) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			purchaseOrderService.updateAttributionDate(model);
			
			return ResponseFactory.success();
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
	}
	@RequestMapping("/checkGoodsInfo.asyn")
	@ResponseBody
	private ViewResponseBean checkGoodsInfo(String purchaseId,String outDepotId,HttpSession session) {
		String msg = null;
		try{
			User user= ShiroUtils.getUser();
			// 响应结果集
			msg = purchaseOrderService.checkGoodsInfo(Long.valueOf(purchaseId),Long.valueOf(outDepotId),user.getMerchantId());
		}catch(SQLException e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(msg);
	}
	
	@RequestMapping("/exportTempDateFile.asyn")
	public void exportTempDateFile(HttpServletResponse response, HttpServletRequest request,
			String orderCode, String fileTempCode, String direction) {
		String path = null;
		InputStream input = null ;
		OutputStream out = null;
		try {
			
			User user = ShiroUtils.getUser();
			
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
	
	*/
/**
	 * 校验链路调整前校验
	 *//*

	@RequestMapping("toSaleStepBeforeCheck.asyn")
	@ResponseBody
	public ViewResponseBean toSaleStepBeforeCheck(Long id) {
		
		if(id == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			boolean flag = purchaseOrderService.toSaleStepBeforeCheck(id);
			
			return ResponseFactory.success(flag) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	@RequestMapping("/toSaleStepOnePage.html")
	public String toSaleStepOnePage(Long id, Model model) throws SQLException {
		
		PurchaseLinkInfoDTO dto = purchaseOrderService.getPurchaseLinkDtoByPurchaseId(id, null) ;
		
		DepotInfoMongo depot = depotInfoService.getDetails(dto.getStartPointDepotId());
		if(depot != null) {
			model.addAttribute("depotType", depot.getType());
		}
		
		model.addAttribute("detail", dto) ;
		return "/derp/purchase/purchase-trade-link-step-one" ;
	}
	
	@RequestMapping("/backToSaleStepOnePage.html")
	public String backToSaleStepOnePage(Long purchaseTradeLinkId, Model model) throws SQLException {
		
		PurchaseLinkInfoDTO dto = purchaseOrderService.getPurchaseLinkDtoByPurchaseId(null, purchaseTradeLinkId) ;
		
		DepotInfoMongo depot = depotInfoService.getDetails(dto.getStartPointDepotId());
		if(depot != null) {
			model.addAttribute("depotType", depot.getType());
		}
		
		model.addAttribute("detail", dto) ;
		return "/derp/purchase/purchase-trade-link-step-one" ;
	}
	
	@RequestMapping("/getTradeLinkList.asyn")
	@ResponseBody
	public ViewResponseBean getTradeLinkList(Long purchaseOrderId) {
		
		List<TradeLinkConfigModel> tradeLinkConfigList = null ;
		
		try {
			tradeLinkConfigList = purchaseOrderService.getTradeLinkList(purchaseOrderId) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		return ResponseFactory.success(tradeLinkConfigList) ;
	}
	
	*/
/**
	 * 采购链路第一步保存链路信息
	 * @param dto
	 * @param deliveryDateStr
	 * @return
	 *//*

	@RequestMapping("/saveOrUpdateLinkStepOne.asyn")
	@ResponseBody
	public ViewResponseBean saveOrUpdateLinkStepOne(PurchaseLinkInfoModel model, String deliveryDateStr) {
		
		Timestamp deliveryDate = TimeUtils.parse(deliveryDateStr, "yyyy-MM-dd");
		model.setConDeliveryDate(deliveryDate);
		
		Long id = null ;
		try {
			
			User user = ShiroUtils.getUser() ;
			model.setMerchantId(user.getMerchantId());
			model.setMerchantName(user.getMerchantName());
			
			id = purchaseOrderService.saveOrUpdateLinkInfo(model, user) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_301) ;
		}
		
		return ResponseFactory.success(id) ;
	}
	
	*/
/**
	 * 采购链路跳转步骤2
	 * @param model
	 * @param id
	 * @return
	 * @throws SQLException
	 *//*

	@RequestMapping("/toSaleStepTwoPage.html")
	public String toSaleStepTwoPage(Model model, Long id) throws Exception {
		
		User user = ShiroUtils.getUser();
		
		*/
/**
		 * 根据ID生成预览订单
		 *//*

		Map<String, Object> map = purchaseOrderService.generatePreOrder(id, user) ;
		
		model.addAttribute("id", id);
		model.addAttribute("map", map) ;
		return "/derp/purchase/purchase-trade-link-step-two" ;
	}
	
	*/
/**
	 * 采购链路嘉宝跳转步骤2
	 * @param model
	 * @param id 采购订单id
	 * @return
	 * @throws SQLException
	 *//*

	@RequestMapping("/toJBSaleStepTwoPage.html")
	public String toJBSaleStepTwoPage(Model model, Long id, Long tradeLinkId) throws Exception {
		
		User user = ShiroUtils.getUser();
		
		*/
/**根据采购订单，生成采购链路信息*//*

		Long purchaseLinkInfoId = purchaseOrderService.saveJBPurchaseLinkByPurchaseId(id, user, tradeLinkId);
		Map<String, Object> map = purchaseOrderService.generatePreOrder(purchaseLinkInfoId, user);
		
		model.addAttribute("id", purchaseLinkInfoId);
		model.addAttribute("map", map) ;
		return "/derp/purchase/purchase-trade-link-step-two" ;
	}
	
	*/
/**
	 * 采购链路跳转步骤3
	 * @param model
	 * @param id
	 * @return
	 * @throws SQLException
	 *//*

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/toSaleStepThreePage.html")
	public String toSaleStepThreePage(Model model, Long id) throws Exception {
		
		User user = ShiroUtils.getUser();
		
		*/
/**
		 * 根据ID生成预览订单
		 *//*

		Map<String, Object> map = purchaseOrderService.generatePreOrder(id, user) ;
		
		model.addAttribute("id", id);
		model.addAttribute("map", map) ;
		
		StringBuffer goodsIdSB = new StringBuffer() ;
		StringBuffer goodsNoSB = new StringBuffer() ;
		StringBuffer goodsNumSB = new StringBuffer() ;
		Long outDepotId = null ;
		
		*/
/**只获取第一个销售订单，作为校验库存依据*//*

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
		
		model.addAttribute("goodsIds", goodsIdSB.toString());
		model.addAttribute("goodsNos", goodsNoSB.toString());
		model.addAttribute("goodsNums", goodsNumSB.toString());
		model.addAttribute("outDepotId", outDepotId) ;
		
		return "/derp/purchase/purchase-trade-link-step-three" ;
	}
	
	@RequestMapping("saveFirstOrderStatusAndIdepot.asyn")
	@ResponseBody
	public ViewResponseBean saveFirstOrderStatusAndIdepot(Long purchaseTradeLinkId) {
		
		if(purchaseTradeLinkId == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		try {
			
			User user = ShiroUtils.getUser();
			
			String batchNo = SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_BNO);
			Timestamp produceDate = TimeUtils.parseDay(TimeUtils.getCurrentMonthFirstDay());
			Timestamp overdueDate = TimeUtils.addDay(produceDate, 730);
			
			*/
/**若起点公司的采购单状态待审核或已审核未入库的，系统自动审核自动入库*//*

			purchaseOrderService.saveFirstOrderStatusAndIdepot(purchaseTradeLinkId, batchNo, produceDate, overdueDate, user) ;
			
			Thread.sleep(2000L);
			
			return ResponseFactory.success() ;
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		}
	}
	
	*/
/**
	 * 采购链路保存，修改商品信息
	 * @param buId
	 * @return
	 *//*

	@RequestMapping("/saveSaleStepGoodsInfo.asyn")
	@ResponseBody
	public ViewResponseBean saveSaleStepGoodsInfo(Long purchaseTradeLinkId,
			String goodsInfoJson) {
		
		if(purchaseTradeLinkId == null
				|| StringUtils.isBlank(goodsInfoJson)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		try {
			
			purchaseOrderService.saveSaleStepGoodsInfo(purchaseTradeLinkId, goodsInfoJson) ;
		
			return ResponseFactory.success() ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/**
	 * 生成链表订单并出入库
	 * @param purchaseTradeLinkId
	 * @return
	 *//*

	@RequestMapping("saveLinkOrderAndDepot.asyn")
	@ResponseBody
	public ViewResponseBean saveLinkOrderAndDepot(Long purchaseTradeLinkId) {
		if(purchaseTradeLinkId == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		try {
			
			User user = ShiroUtils.getUser();
			
			Map<String, Object> map = purchaseOrderService.generatePreOrder(purchaseTradeLinkId, user) ;
			
			Map<String, Object> batchMap = purchaseOrderService.getTradeLinkBatchNo(purchaseTradeLinkId) ;
			
			*/
/**生成订单**//*

			Map<String, Object> merchantIdOrderIdsMap = purchaseOrderService.saveLinkOrder(map, batchMap, user, purchaseTradeLinkId);
			
			*/
/**针对嘉宝-卓普信-宝信的交易链路（ID:1）生成的宝信采购单的单据状态默认为：已审核**//*

			purchaseOrderService.modifyJBLinkPurchaseOrder(purchaseTradeLinkId, merchantIdOrderIdsMap, user) ;
			
			*/
/**休眠5秒后, 等待库存MQ处理上架逻辑**//*

			Thread.sleep(5000L);
			
			*/
/**获取订单号**//*

			List<String> orderCode = purchaseOrderService.getLinkOrderCode(merchantIdOrderIdsMap) ;
		
			return ResponseFactory.success(orderCode) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	@RequestMapping("/getPurchasePriceManage.asyn")
	@ResponseBody
	public ViewResponseBean getPurchasePriceManage(Long buId, Long supplierId) {
		
		if(buId == null
				|| supplierId == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		try {
			
			User user = ShiroUtils.getUser();
			
			boolean flag = purchaseOrderService.getPurchasePriceManage(buId, supplierId, user) ;
		
			return ResponseFactory.success(flag) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/**
	 * 采购金额调整模态框获取详情
	 *//*

	@RequestMapping("getAmountAdjustmentDetail.asyn")
	@ResponseBody
	public ViewResponseBean getAmountAdjustmentDetail(Long id) {
		
		if(id == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
		}
		
		try {
			Map<String, Object> map = purchaseOrderService.getAmountAdjustmentDetail(id) ;
		
			return ResponseFactory.success(map) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	*/
/**
	 * 采购金额修改
	 *//*

	@RequestMapping("saveAmountAdjustment.asyn")
	@ResponseBody
	public ViewResponseBean saveAmountAdjustment(PurchaseOrderModel model, 
			@RequestParam("items")String itemList) {
		
		// 必填项空值校验
		boolean isNull = new EmptyCheckUtils().addObject(model.getId())
				.addObject(model.getCurrency()).empty();
		
		if (isNull || StringUtils.isBlank(itemList)) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			
			User user = ShiroUtils.getUser();
			
			purchaseOrderService.saveAmountAdjustment(model, itemList, user) ;
		
			return ResponseFactory.success() ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	*/
/**
	 * 采购金额确认
	 *//*

	@RequestMapping("saveConfirmAmountAdjustment.asyn")
	@ResponseBody
	public ViewResponseBean saveConfirmAmountAdjustment(PurchaseOrderModel model) {
		
		// 必填项空值校验
		boolean isNull = new EmptyCheckUtils().addObject(model.getId()).empty();
		
		if (isNull ) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			User user = ShiroUtils.getUser();
			
			purchaseOrderService.saveConfirmAmountAdjustment(model, user) ;
		
			return ResponseFactory.success() ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	*/
/**
	 * 打托入库
	 *//*

	@RequestMapping("purchaseDelivery.asyn")
	@ResponseBody
	public ViewResponseBean purchaseDelivery(@RequestParam("purchaseId") String purchaseId ,
			@RequestParam("inboundDate")String inboundDate, @RequestParam("itemList")String itemList) {
		
		// 必填项空值校验
		boolean isNull = new EmptyCheckUtils().addObject(purchaseId)
				.addObject(inboundDate).addObject(itemList).empty();
		
		if (isNull) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			
			User user = ShiroUtils.getUser();
			
			purchaseOrderService.savePurchaseDelivery(purchaseId, inboundDate, itemList, user) ;
		
			return ResponseFactory.success() ;
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	*/
/**
	 * SD单创建前校验
	 *//*

	@RequestMapping("createSdOrderCheck.asyn")
	@ResponseBody
	public ViewResponseBean createSdOrderCheck(Long id) {
		
		if (id == null) {
			// 输入信息不完整
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			purchaseOrderService.createSdOrderCheck(id) ;
		
			return ResponseFactory.success() ;
		}catch (DerpException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	*/
/**
	 * 获取采购订单详情明细
	 * @param id
	 * @return
	 *//*

	@RequestMapping("getPurchaseOrderDetails.asyn")
	@ResponseBody
	public ViewResponseBean getPurchaseOrderDetails(Long id) {
		
		if(id == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			PurchaseOrderModel order = purchaseOrderService.searchDetail(id);
			
			return ResponseFactory.success(order) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/**
	 * 销售创建前校验
	 *//*

	@RequestMapping("toSaleBeforeCheck.asyn")
	@ResponseBody
	public ViewResponseBean toSaleBeforeCheck(Long id) {
		
		if(id == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			boolean flag = purchaseOrderService.toSaleBeforeCheck(id);
			
			return ResponseFactory.success(flag) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	@RequestMapping("saveSaleOrder.asyn")
	@ResponseBody
	public ViewResponseBean saveSaleOrder(Long id, Long customerId, String items) {
		
		if(id == null && customerId == null && StringUtils.isBlank(items)) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			
			User user = ShiroUtils.getUser();

			List<Long> ids = new ArrayList<>() ;
			ids.add(id) ;
			
			Long saleId = purchaseOrderService.saveSaleOrder(ids, customerId, items, user);
			
			return ResponseFactory.success(saleId) ;
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/**
	 * 检查是否要生成内部供应商对应销售订单
	 *//*

	@RequestMapping("checkInnerMerchantSaleOrder.asyn")
	@ResponseBody
	public ViewResponseBean checkInnerMerchantSaleOrder(Long id) {
		
		if(id == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			Map<String, Object> map = purchaseOrderService.checkInnerMerchantSaleOrder(id);
			
			return ResponseFactory.success(map) ;
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/**
	 * 保存内部公司销售订单
	 *//*

	@RequestMapping("saveInnerMerchantSaleOrders.asyn")
	@ResponseBody
	public ViewResponseBean saveInnerMerchantSaleOrders(Long id, Long outDepotId, Long buId) {
		
		if(id == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			
			User user = ShiroUtils.getUser();
			
			purchaseOrderService.saveInnerMerchantSaleOrders(id, outDepotId, buId, user);
			
			return ResponseFactory.success() ;
		} catch (DerpException e) {
			
			//销售订单生成失败，采购订单审核成功
			if(e.getObj() != null) {
				return ResponseFactory.success(e.getObj()) ;
			}
			
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/**
	 * 检查采购订单是否存在交易链路配置
	 *//*

	@RequestMapping("checkTradeLink.asyn")
	@ResponseBody
	public ViewResponseBean checkTradeLink(Long id) {
		
		if(id == null) {
			return ResponseFactory.error(StateCodeEnum.ERROR_303);
		}
		
		try {
			Map<String, Object> map = purchaseOrderService.checkTradeLink(id);
			
			return ResponseFactory.success(map) ;
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/***
	 * 获取货品信息
	 * @param poNos
	 * @return
	 *//*

	@RequestMapping("/getGoodsProductInfo.asyn")
	@ResponseBody
	public ViewResponseBean getGoodsProductInfo(Long goodsId) {
		
		try {
			
			if(goodsId == null) {
				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
			}
			
			Map<String, Object> productInfoMap = purchaseOrderService.getGoodsProductInfo(goodsId) ;
			
			return ResponseFactory.success(productInfoMap) ;
			
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}
	
	*/
/***
	 * 提交卓普信
	 * @param poNos
	 * @return
	 *//*

	@RequestMapping("/submitSapience.asyn")
	@ResponseBody
	public ViewResponseBean submitSapience(FinancingOrderDTO dto) {
		
		try {
			
			// 必填项空值校验
			boolean isNull = new EmptyCheckUtils().addObject(dto.getBillCurrencCode())
					.addObject(dto.getBorrowingDays()).addObject(dto.getDepotCode())
					.addObject(dto.getDepotName()).addObject(dto.getSupplierCode())
					.addObject(dto.getSupplierName()).addObject(dto.getPoNo())
					.addObject(dto.getWarehouseAddress()).addObject(dto.getPaymentTermName())
					.addObject(dto.getItems()) 
					.empty();
			
			if(isNull) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303) ;
			}
			
			String items = dto.getItems() ;
			
			com.alibaba.fastjson.JSONArray jsonArray = (com.alibaba.fastjson.JSONArray)com.alibaba.fastjson.JSONArray.parse(items);
			
			if(jsonArray.size() <= 0) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303) ;
			}
			
			User user = ShiroUtils.getUser();
			
			purchaseOrderService.confirmSubmitSapience(dto, user) ;
			
			return ResponseFactory.success() ;
			
		} catch (DerpException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
		
	}	
}
*/

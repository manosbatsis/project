//package com.topideal.order.web.sale;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.alibaba.fastjson.JSONArray;
//import com.topideal.common.constant.DERP;
//import com.topideal.common.constant.DERP_ORDER;
//import com.topideal.common.constant.DERP_SYS;
//import com.topideal.common.enums.MQInventoryEnum;
//import com.topideal.common.system.auth.User;
//import com.topideal.common.system.mq.RMQProducer;
//import com.topideal.common.system.web.ResponseFactory;
//import com.topideal.common.system.web.StateCodeEnum;
//import com.topideal.common.system.web.ViewResponseBean;
//import com.topideal.common.tools.EmptyCheckUtils;
//import com.topideal.common.tools.ExcelUtilXlsx;
//import com.topideal.common.tools.FileExportUtil;
//import com.topideal.common.tools.StrUtils;
//import com.topideal.common.tools.excel.ExportExcelSheet;
//import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
//import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
//import com.topideal.entity.dto.sale.SaleFinancingOrderDTO;
//import com.topideal.entity.dto.sale.SaleFinancingOrderItemDTO;
//import com.topideal.entity.dto.sale.SaleOrderDTO;
//import com.topideal.entity.dto.sale.SaleOrderItemDTO;
//import com.topideal.entity.dto.sale.SaleShelfDTO;
//import com.topideal.entity.vo.sale.SaleOrderItemModel;
//import com.topideal.entity.vo.sale.SaleOrderModel;
//import com.topideal.entity.vo.sale.SaleOutDepotModel;
//import com.topideal.entity.vo.sale.SalePoRelModel;
//import com.topideal.entity.vo.sale.SaleShelfModel;
//import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
//import com.topideal.mongo.dao.MerchantInfoMongoDao;
//import com.topideal.mongo.entity.DepotInfoMongo;
//import com.topideal.mongo.entity.MerchantInfoMongo;
//import com.topideal.order.service.base.DepotInfoService;
//import com.topideal.order.service.purchase.PurchaseOrderService;
//import com.topideal.order.service.sale.SaleOrderService;
//import com.topideal.order.service.sale.SaleOutDepotService;
//import com.topideal.order.service.sale.SalePoRelService;
//import com.topideal.order.service.sale.SaleShelfService;
//import com.topideal.order.shiro.ShiroUtils;
//import com.topideal.order.tools.DownloadExcelUtil;
//
//import net.sf.json.JSONObject;
//
///**
// * 销售订单 controller
// *
// */
//@RequestMapping("/sale")
//@Controller
//public class SaleOrderController {
//	/* 打印日志 */
//	protected Logger logger = LoggerFactory.getLogger(SaleOrderController.class);
//	
//	private static final String[] IMPORT_COLUMNS = { "销售单号","PO号","商品货号","条形码","商品名称"
//			,"上架时间","上架正常品数量","残损数量","少货量","备注"};
//	private static final String[] IMPORT_KEYS = {"saleCode" , "poNo","goodsNo","barcode","goodsName","shelfDate","shelfNum","damagedNum","lackNum","remark"} ;
//
//
//	private static final String[] SALE_COLUMNS = {"销售订单编号","订单状态","参照订单号","入仓仓库名称",
//			"出仓仓库名称","事业部","LBX单号","PO单号","客户","是否内部公司客户","销售类型","销售币种","销售总数量","销售总金额","交货日期","创建人",
//			"创建时间","审核人","审核时间","海外仓理货单位","原销出仓仓库","合同号","归属月份","运输方式",
//			"车次","标准箱TEU","托数","承运商","提单毛重","出库地点","收货地址"};
//	private static final String[] SALE_KEYS = {"code","stateLabel","referToOrder","inDepotName","outDepotName",
//						"buName","lbxNo","poNo","customerName","isInnerCustomer","businessModelLabel","currencyLabel","totalNum","totalAmount","deliveryDate",
//						"createName","createDate","auditName","auditDate","tallyingUnitLabel","originalOutDepotName",
//						"contractNo","ownMonth","transportLabel","trainno","teu","torusNumber","carrier","billWeight","outdepotAddr","receiverAddress"};
//	private static final String[] ITEM_COLUMNS ={"销售订单编号","PO号","标准品牌","商品编号","商品货号","商品名称",
//			"商品条形码","计量单位","销售数量","销售单价","销售总金额"};
//	private static final String[] ITEM_KEYS = {"orderCode","poNo","commBrandParentName","goodsCode","goodsNo","goodsName","barcode",
//			"unit","num","price","amount"};
//
//	private static final String[] SHELF_COLUMNS ={"销售订单编号","标准品牌","商品货号","商品名称",
//			"商品条形码","PO号","上架好品量","残损量","少货量","上架时间","销售单价","销售币种"};
//	private static final String[] SHELF_KEYS = {"code","commBrandParentName","goodsNo","goodsName","barcode",
//			"poNo","shelfNum","damagedNum","lackNum","shelfDate","price","currency"};
//
//	// 销售订单service
//	@Autowired
//	private SaleOrderService saleOrderService;
//	// 销售出库service
//	@Autowired
//	private SaleOutDepotService saleOutDepotService;
//	// 仓库
//	@Autowired
//	private DepotInfoService depotInfoService;
//	// 销售单_po关联表
//	@Autowired
//	private SalePoRelService salePoRelService;
//	// 采购订单service
//	@Autowired
//	private PurchaseOrderService purchaseOrderService;
//
//	@Autowired
//	private MerchantInfoMongoDao merchantInfoMongoDao;
//	@Autowired
//	private SaleShelfService saleShelfService;
//	@Autowired
//	private RMQProducer rocketMQProducer;//MQ
//
//	/**
//	 * 访问列表页面
//	 * */
//	@RequestMapping("/toPage.html")
//	public String toPage(Model model, HttpSession session) throws Exception {
//		return "/derp/sale/sale-list";
//	}
//
//	/**
//	 * 访问新增页面
//	 * @param model
//	 * @param ids   采购订单/调拨订单 ID
//	 * @param type  1  采购订单， 2 调拨订单
//	 * @return
//	 * @throws Exception
//	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping("/toAddPage.html")
//	public String toAddPage(Model model, String ids, String type, String pageSource,
//			String outDepotId,String platformPurchaseIds, String buId, String platformSalesNum)throws Exception{
//		//初始化数据
//		model.addAttribute("itemCount", 0);
//		User user= ShiroUtils.getUser();
//		if (StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource",pageSource);
//		}
//		boolean salePriceManage = false;
//		/*
//		 *	点击复制
//		 */
//		if(StringUtils.isNotBlank(ids)
//				|| StringUtils.isNotBlank(platformPurchaseIds)) {
//			
//			SaleOrderDTO saleOrderDTO = null;
//			List<SaleOrderItemDTO> saleOrderItemList = null;
//			
//			if(StringUtils.isNotBlank(ids)) {
//				saleOrderDTO = saleOrderService.searchDetail(Long.valueOf(ids));
//				saleOrderItemList = saleOrderService.listItemByOrderId(Long.valueOf(ids));
//				model.addAttribute("saleOrderItemList", saleOrderItemList);
//				model.addAttribute("saleOrderDTO", saleOrderDTO);
//			}
//			//平台采购单转销售
//			else if(StringUtils.isNotBlank(platformPurchaseIds)) {
//				Map<String, Object> salesMap = saleOrderService.modifyPlatFormPurchaseToSales(platformPurchaseIds, outDepotId, buId, platformSalesNum,user);
//				
//				saleOrderDTO = (SaleOrderDTO)salesMap.get("saleOrderDTO")  ;
//				saleOrderItemList = (List<SaleOrderItemDTO>)salesMap.get("saleOrderItemList")  ;
//				
//				if(saleOrderDTO == null) {
//					saleOrderDTO = new SaleOrderDTO() ;
//					saleOrderDTO.setOutDepotId(Long.valueOf(outDepotId));
//				}
//				
//				if(saleOrderItemList == null) {
//					saleOrderItemList = new ArrayList<>();
//				}
//				
//				model.addAttribute("platformPurchaseIds", platformPurchaseIds) ;
//				model.addAllAttributes(salesMap) ;
//			}
//			
//			int isTrainnoRequired = 0;	// 车次是否必填
//			int isTeuRequired = 0;	// 标准箱TEU是否必填
//			String isSameAreaDisabled = "false";	// 是否同关区是否禁用
//			int isSameAreaRequired = 0;	// 是否同关区是否必填
//			String inDepotDisabled = "false";// 入库仓库是否禁用
//			int inDepotRequired = 0;// 入库仓库是否必填
//			int isPoRequired = 0;// PO单号是否必填
//			int isContractNoRequired = 0;	// 合同号是否必填
//			DepotInfoMongo outDepot = depotInfoService.getDetails(saleOrderDTO.getOutDepotId());
//			if(outDepot != null) {			
//				model.addAttribute("outDepotType", outDepot.getType());
//				model.addAttribute("customsNo", outDepot.getCustomsNo());
//			}
//			
//			if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())){
//				isSameAreaRequired = 1;
//				if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
//					inDepotRequired = 1;
//				}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
//						DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
//					inDepotRequired = 1;
//				}
//			}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
//				isSameAreaDisabled = "true";	// 禁用
//				
//			}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())){
//				isSameAreaRequired = 0;
//				if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
//					inDepotRequired = 1;
//				}
//			}
//			/**
//			 * 1、当销售类型为购销-实销实结、采购执行时，PO单号非必填；
//			 * 2、当销售类型为购销-整批结算时，PO单号必填项
//			 */
//			if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel()) &&
//					DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderDTO.getBusinessModel())){
//				isPoRequired = 0;
//			}else if(DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
//				isPoRequired = 1;
//			}
//			// 同关区时合同号必填
//			if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
//				isContractNoRequired = 1;
//			}
//			//标准箱TEU:当运输方式为海运时必填
//			if(DERP.TRANSPORT_2.equals(saleOrderDTO.getTransport())){
//				isTeuRequired = 1;
//			}
//			//车次:当运输方式为陆运、港到仓拖车时必填
//			if(DERP.TRANSPORT_3.equals(saleOrderDTO.getTransport()) ||
//					DERP.TRANSPORT_4.equals(saleOrderDTO.getTransport())){
//				isTrainnoRequired = 1;
//			}
//			model.addAttribute("inDepotRequired", inDepotRequired);
//			model.addAttribute("inDepotDisabled", inDepotDisabled);
//			model.addAttribute("isSameAreaDisabled", isSameAreaDisabled);
//			model.addAttribute("isSameAreaRequired", isSameAreaRequired);
//			model.addAttribute("isPoRequired", isPoRequired);
//			model.addAttribute("isContractNoRequired", isContractNoRequired);
//			model.addAttribute("isTrainnoRequired", isTrainnoRequired);
//			model.addAttribute("isTeuRequired", isTeuRequired);
//			
//			salePriceManage = saleOrderService.getSalePriceManage(saleOrderDTO.getBuId(),saleOrderDTO.getCustomerId(),user);
//			
//		}
//		model.addAttribute("salePriceManage", salePriceManage);
//
//		Map<String, Object> merchantParams = new HashMap<>();
//		merchantParams.put("merchantId", user.getMerchantId());
//		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
////		model.addAttribute("priceDeclareRatio", merchantInfo.getPriceDeclareRatio());//公司的申报系数
//
//		model.addAttribute("merchantId", user.getMerchantId());
//		model.addAttribute("merchantName", user.getMerchantName());		
//
//		return "/derp/sale/sale-add";
//	}
//	/**
//	 * 销售新增购销退货获取分页数据
//	 *
//	 * @param model
//	 */
//	@RequestMapping("/saleGetListSaleOrderByPage.asyn")
//	@ResponseBody
//	private ViewResponseBean saleGetListSaleOrderByPage(SaleOrderDTO dto,String codes) {
//		try {
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			// 响应结果集
//			dto = saleOrderService.saleGetListSaleOrderByPage(dto, codes,user);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
//		} catch (Exception e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
//		}
//		return ResponseFactory.success(dto);
//	}
//
//
//	/**
//	 * 访问详情页面
//	 * */
//	@RequestMapping("/toDetailsPage.html")
//	public String toDetailsPage(Model model, Long id, String pageSource)throws Exception{
//
//
//		List<SaleShelfDTO> saleShelfDTOList = saleShelfService.searchDetailByOrderId(id);
//		SaleShelfDTO saleShelfDTO = new SaleShelfDTO();
//		saleShelfDTO.setSaleShelfDTOList(saleShelfDTOList);
//
//
//		SaleOrderDTO saleOrderDTO = saleOrderService.searchDetail(id);
//		List<SaleOrderItemDTO> itemList = saleOrderService.listItemByOrderId(id);
//		saleOrderDTO.setItemList(itemList);
//		try {
//			if(id != null) {
//				SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
//				saleOutDepotModel.setSaleOrderId(id);
//				//根据销售订单id查询销售出库信息
//				List<SaleOutDepotModel> saleOutModel = saleOutDepotService.listSaleOutDepotModel(saleOutDepotModel);
//				if(saleOutModel.size() >= 1) {
//					model.addAttribute("saleOutModel",saleOutModel.get(saleOutModel.size()-1));
//				}
//				SaleOrderDTO saleOrderDTO2 =new SaleOrderDTO();
//				saleOrderDTO2.setId(id);
//				//根据销售订单id查询销售上架信息
//				List<SaleShelfModel> listShelfList = saleOrderService.listShelfListByOrderId(saleOrderDTO2);
//				if(listShelfList.size() > 0) {
//					model.addAttribute("isNotShelf", "yes");		// 该销售订单有商品上架
//				}else{
//					model.addAttribute("isNotShelf", "no");		// 该销售订单还没有商品上架
//				}
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		if (StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource",pageSource);
//		}
//		model.addAttribute("detail", saleOrderDTO);
//		model.addAttribute("saleShelfDTO", saleShelfDTO);
//		SaleFinancingOrderDTO financeDTO = saleOrderService.findFinanceDetail(id);
//		model.addAttribute("finance",financeDTO);
//		// 如果销售类型为采购执行并且没有审核，就去审核采购执行
//		if(DERP_ORDER.SALEORDER_STATE_001.equals(saleOrderDTO.getState())
//				&& DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderDTO.getBusinessModel())){
//			return "/derp/sale/sale-purchaseExecute-audit";
//		}
//		return "/derp/sale/sale-details";
//	}
//
//	/**
//	 * 购销代销导入页面
//	 * */
//	@RequestMapping("/toImportPage.html")
//	public String toImportPage(){
//		return "/derp/sale/sale-import";
//	}
//	
//	/**
//	 * 上架导入页面
//	 * */
//	@RequestMapping("/toShelfImportPage.html")
//	public String toShelfImportPage(){
//		return "/derp/sale/sale-shelf-import";
//	}
//	/**
//	 * 访问编辑页面
//	 * @param model
//	 * @param id
//	 * @param pageSource
//	 * @param purchaseId 从新增内部销售单过来的
//	 * @param outDepotId 从新增内部销售单过来的
//	 * @param operate  02-提交 03-审核
//	 * @param session
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/toEditPage.html")
//	public String toEditPage(Model model, Long id, String pageSource,String purchaseId,String outDepotId,String operate)throws Exception{
//		User user= ShiroUtils.getUser();
//		SaleOrderDTO saleOrderDTO = new SaleOrderDTO();
//		List<SaleOrderItemDTO> itemList = new ArrayList<>();
//		// 从新增内部销售单过来
//		if(StringUtils.isNotBlank(purchaseId)){
//			saleOrderDTO = saleOrderService.getSaleOrderByPurchaseId(Long.valueOf(purchaseId), Long.valueOf(outDepotId), user.getMerchantId(),user.getMerchantName());
//			itemList = saleOrderDTO.getItemList();
//		}else{
//			saleOrderDTO = saleOrderService.searchDetail(id);
//			itemList = saleOrderService.listItemByOrderId(id);
//		}
//		saleOrderDTO.setItemList(itemList);
//		model.addAttribute("itemCount", itemList.size());
//		model.addAttribute("detail", saleOrderDTO);
//		DepotInfoMongo outDepot = depotInfoService.getDetails(saleOrderDTO.getOutDepotId());
//		if(outDepot != null) {			
//			model.addAttribute("outDepotType", outDepot.getType());
//			model.addAttribute("customsNo", outDepot.getCustomsNo());
//		}
//
//		String isSameAreaDisabled = "false";	// 是否同关区是否禁用
//		int isSameAreaRequired = 0;	// 是否同关区是否必填
//		String inDepotDisabled = "false";// 入库仓库是否禁用
//		int inDepotRequired = 0;// 入库仓库是否必填
//		int isPoRequired = 0;// PO单号是否必填
//		int isContractNoRequired = 0;	// 合同号是否必填
//		int isTrainnoRequired = 0;	// 车次是否必填
//		int isTeuRequired = 0;	// 标准箱TEU是否必填
//
//		/**
//		 * 1、当出仓仓库为保税仓时，是否同关区必填；
//		 * 2、当出仓仓库为海外仓时，是否同关区禁用；
//		 * 3、当出仓仓库为中转仓时，是否同关区非必填；
//		 */
//		/**
//		 * 1、当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填；
//		 * 3、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填；
//		 * 4、当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
//		 * 6、当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填
//		 */
//		if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())){
//			isSameAreaRequired = 1;
//			if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
//				inDepotRequired = 1;
//			}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
//					DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
//				inDepotRequired = 1;
//			}
//		}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
//			isSameAreaDisabled = "true";	// 禁用
//			
//		}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())){
//			isSameAreaRequired = 0;
//			if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
//				inDepotRequired = 1;
//			}
//		}
//		/**
//		 * 1、当销售类型为购销-实销实结、采购执行时，PO单号非必填；
//		 * 2、当销售类型为购销-整批结算时，PO单号必填项
//		 */
//		if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel()) &&
//				DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderDTO.getBusinessModel())){
//			isPoRequired = 0;
//		}else if(DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
//			isPoRequired = 1;
//		}
//		// 同关区时合同号必填
//		if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
//			isContractNoRequired = 1;
//		}
//		//标准箱TEU:当运输方式为海运时必填
//		if(DERP.TRANSPORT_2.equals(saleOrderDTO.getTransport())){
//			isTeuRequired = 1;
//		}
//		//车次:当运输方式为陆运、港到仓拖车时必填
//		if(DERP.TRANSPORT_3.equals(saleOrderDTO.getTransport()) ||
//		DERP.TRANSPORT_4.equals(saleOrderDTO.getTransport())){
//			isTrainnoRequired = 1;
//		}
//
//		Map<String, Object> merchantParams = new HashMap<>();
//		merchantParams.put("merchantId", user.getMerchantId());
//		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
////		model.addAttribute("priceDeclareRatio", merchantInfo.getPriceDeclareRatio());//公司的申报系数
//
//		model.addAttribute("buId", saleOrderDTO.getBuId());
//		model.addAttribute("inDepotId", saleOrderDTO.getInDepotId());
//		model.addAttribute("outDepotId", saleOrderDTO.getOutDepotId());
//		model.addAttribute("inDepotRequired", inDepotRequired);
//		model.addAttribute("inDepotDisabled", inDepotDisabled);
//		model.addAttribute("isSameAreaDisabled", isSameAreaDisabled);
//		model.addAttribute("isSameAreaRequired", isSameAreaRequired);
//		model.addAttribute("isPoRequired", isPoRequired);
//		model.addAttribute("isContractNoRequired", isContractNoRequired);
//		model.addAttribute("isTrainnoRequired", isTrainnoRequired);
//		model.addAttribute("isTeuRequired", isTeuRequired);
//		if (StringUtils.isNotBlank(pageSource)) {
//			model.addAttribute("pageSource",pageSource);
//		}
//		model.addAttribute("operate",operate);
//		boolean salePriceManage = saleOrderService.getSalePriceManage(saleOrderDTO.getBuId(),saleOrderDTO.getCustomerId(),user);
//		model.addAttribute("salePriceManage", salePriceManage);
//		
//		return "/derp/sale/sale-edit-interior";
//	}
//
//	/**
//	 * 获取分页数据
//	 * */
//	@RequestMapping("/listSaleOrder.asyn")
//	@ResponseBody
//	private ViewResponseBean listSaleOrder(SaleOrderDTO dto,String deliveryDateStr,String createDateStr, HttpSession session) {
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			//设置时间相关的条件
//			if(deliveryDateStr != null){
//				Timestamp deliveryDate = Timestamp.valueOf(deliveryDateStr+" 00:00:00");
//				dto.setDeliveryDate(deliveryDate);
//			}
//			if(createDateStr != null){
//				Timestamp createDate = Timestamp.valueOf(createDateStr+" 00:00:00");
//				dto.setCreateDate(createDate);
//			}
//			// 响应结果集
//			dto = saleOrderService.listSaleOrderByPage(dto,user);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(dto);
//	}
//	/**
//	 * 购销：校验PO号在中间表是否已存在，若已存在则给个提示
//	 * */
//	@RequestMapping("/checkExistByPo.asyn")
//	@ResponseBody
//	public ViewResponseBean checkExistByPo(String poNo,Long orderId,HttpSession session) {
//		List<SalePoRelModel> salePoRelList = new ArrayList<>();
//		try{
//			User user= ShiroUtils.getUser();
//			// 响应结果集
//			salePoRelList = saleOrderService.checkExistByPo(poNo,orderId,user.getMerchantId());
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(salePoRelList);
//	}
//	/**
//	 * 新增/修改
//	 * */
//	@RequestMapping("/saveOrModifyTempOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean saveOrModifyTempOrder(String json,HttpSession session) {
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            boolean b = saleOrderService.saveOrModifyTempOrder(json,user);
//            if(!b){
//                return ResponseFactory.error(StateCodeEnum.ERROR_301);
//            }
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success();
//	}
//
//	/**
//	 * 提交并审核
//	 * */
//	@RequestMapping("/addSaleOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean addSaleOrder(String json,HttpSession session) {
//		 String msg = null;
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            msg = saleOrderService.addSaleOrder(json,user.getId(),user.getName(), user.getTopidealCode());
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(msg);
//	}
//
//	/**
//	 * 修改并审核
//	 * */
//	@RequestMapping("/modifySaleOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean modifySaleOrder(String json, HttpSession session) {
//		String msg=null;
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            msg = saleOrderService.modifySaleOrder(json,user);
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(msg);
//	}
//
//	/**
//	 * 审核销售类型为采购执行的
//	 * */
//	@RequestMapping("/modifyPurchaseExecute.asyn")
//	@ResponseBody
//	public ViewResponseBean modifyPurchaseExecute(Long id, HttpSession session) {
//		String msg=null;
//		try{
//            User user= ShiroUtils.getUser();
//            SaleOrderModel model=new SaleOrderModel();
//            model.setId(id);
//            msg = saleOrderService.auditPurchase(model,user.getId(),user.getName(),user.getTopidealCode());
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(msg);
//	}
//
//	/**
//	 * 删除
//	 * */
//	@RequestMapping("/delSaleOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean delSaleOrder(String ids) {
//		try{
//            //校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//            boolean b = saleOrderService.delSaleOrder(list);
//            if(!b){
//                return ResponseFactory.error(StateCodeEnum.ERROR_301);
//            }
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success();
//	}
//
//
//	/**
//	 * 中转仓出库
//	 * */
//	@RequestMapping("/saleOrderStockOut.asyn")
//	@ResponseBody
//	private ViewResponseBean saleOrderStockOut(String ids,String outDepotDateStr, HttpSession session) {
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//			User user= ShiroUtils.getUser();
//			// 响应结果集
//			boolean bl = saleOrderService.confirmSaleOrderStockOut(list,user.getId(),user.getName(),user.getTopidealCode(),user.getMerchantId(),outDepotDateStr);
//			if(!bl){
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}
//	/**
//	 * 完结出库
//	 * */
//	@RequestMapping("/endStockOut.asyn")
//	@ResponseBody
//	private ViewResponseBean endStockOut(String ids, HttpSession session) {
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//            User user= ShiroUtils.getUser();
//			// 响应结果集
//			boolean bl = saleOrderService.confirmEndStockOut(list,user.getId(),user.getName());
//			if(!bl){
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}
//	/**
//	 * 计算销售订单出库的百分比
//	 * */
//	@RequestMapping("/differenceRatio.asyn")
//	@ResponseBody
//	private ViewResponseBean differenceRatio(String ids) {
//		Map<String,String> map =new HashMap<String,String>();
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateId(Long.parseLong(ids));
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//			// 响应结果集
//            map = saleOrderService.differenceRatio(Long.parseLong(ids));
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//
//		return ResponseFactory.success(map);
//	}
//	/**
//	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
//	 * */
//	@RequestMapping("/getOrderGoodsInfo.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderGoodsInfo(String ids,String type) {
//		Map<String,Integer> map =new HashMap<String,Integer>();
//		try{
//			//校验id是否正确
//            boolean isRight = StrUtils.validateIds(ids);
//            if(!isRight){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List list = StrUtils.parseIds(ids);
//			// 响应结果集
//            map = saleOrderService.getOrderGoodsInfo(list,type);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//
//		return ResponseFactory.success(map);
//	}
//
//	/**
//     * 下载导入模版
//     * @throws IOException
//     */
//    @RequestMapping("/downloadImportTemp.asyn")
//    public void downloadImportTemp(HttpServletResponse response, HttpServletRequest request,
//                               String ids) throws Exception {
//    	User user= ShiroUtils.getUser();
//        String sheetName = "上架导入模板";
//        // 获取导出的信息
//        Map<String, Object> map = new HashMap<>();
//		map.put("merchantId", user.getMerchantId());
//        List<SaleOutDepotModel> result = saleOrderService.listNoShelfRecord(map);
//        List<Map<String, Object>> objList = (List)result;
//        // 生成表格
//        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, IMPORT_COLUMNS, IMPORT_KEYS,objList);
//        // 导出文件
//        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//    }
//	/**
//	 * 导入上架
//	 * */
//	@RequestMapping("/importSaleShelf.asyn")
//	@ResponseBody
//	public ViewResponseBean importSaleShelf(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//            if(file==null){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List<List<Map<String,String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
//			if(data == null){//数据为空
//                return ResponseFactory.error(StateCodeEnum.ERROR_302);
//            }
//			User user= ShiroUtils.getUser();
//			resultMap = saleOrderService.importSaleShelf(data,user);
//			List<InvetAddOrSubtractRootJson> subtractRootJsonList = (List<InvetAddOrSubtractRootJson>)resultMap.get("subtractRootJsonList");
//			//发送库存入库
//			if(subtractRootJsonList!=null&&subtractRootJsonList.size()>0){
//				for(InvetAddOrSubtractRootJson rootJson : subtractRootJsonList){
//					rocketMQProducer.send(JSONObject.fromObject(rootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
//				}
//			}
//			resultMap.remove("subtractRootJsonList");
//        }catch(Exception e){
//			e.printStackTrace();
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(resultMap);
//	}
//
//	/**
//	 * 商品上架
//	 * */
//	@RequestMapping("/importSaleGoods.asyn")
//	@ResponseBody
//	public ViewResponseBean importSaleGoods(@RequestParam(value = "file", required = false) MultipartFile file,SaleOrderDTO dto,String salePriceManage,String unitId, HttpSession session) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//            if(file==null){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
//			if(data == null){//数据为空
//                return ResponseFactory.error(StateCodeEnum.ERROR_302);
//            }
//			User user= ShiroUtils.getUser();
//			resultMap = saleOrderService.importSaleGoods(data,user,dto,salePriceManage,unitId);
//        }catch(Exception e){
//			e.printStackTrace();
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(resultMap);
//	}
//
//	/**
//	 * 导入购销代销销售订单
//	 * */
//	@RequestMapping("/importSale.asyn")
//	@ResponseBody
//	public ViewResponseBean importSale(@RequestParam(value = "file", required = false) MultipartFile file, HttpSession session) {
//		Map resultMap = new HashMap();//返回的结果集
//		try{
//            if(file==null){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
//			if(data == null){//数据为空
//                return ResponseFactory.error(StateCodeEnum.ERROR_302);
//            }
//			logger.debug("=========================销售购销导入开始========================");
//			User user= ShiroUtils.getUser();
//			resultMap = saleOrderService.importSaleOrder(data,user);
//			logger.debug("=========================销售购销导入结束========================");
//		}catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(resultMap);
//	}
//
//
//	/**
//	 * 获取导出销售订单的数量
//	 */
//	@RequestMapping("/getOrderCount.asyn")
//	@ResponseBody
//	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOrderDTO dto,String deliveryDateStr,String createDateStr) throws Exception{
//		Map<String,Object> data=new HashMap<String,Object>();
//		try{
//			User user= ShiroUtils.getUser();
//			// 设置商家id
//			dto.setMerchantId(user.getMerchantId());
//			//设置时间相关的条件
//			if(deliveryDateStr != null){
//				Timestamp deliveryDate = Timestamp.valueOf(deliveryDateStr+" 00:00:00");
//				dto.setDeliveryDate(deliveryDate);
//			}
//			if(createDateStr != null){
//				Timestamp createDate = Timestamp.valueOf(createDateStr+" 00:00:00");
//				dto.setCreateDate(createDate);
//			}
//			// 响应结果集
//			List<SaleOrderDTO> result = saleOrderService.listSaleOrder(dto,user);
//			data.put("total",result.size());
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(data);
//	}
//	/**
//	 * 导出销售订单
//	 * */
//	@RequestMapping("/exportSaleOrder.asyn")
//	@ResponseBody
//	private void exportSaleOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOrderDTO dto,String deliveryDateStr,String createDateStr) throws Exception{
//		User user= ShiroUtils.getUser();
//		// 设置商家id
//		dto.setMerchantId(user.getMerchantId());
//		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
//		// 表头
//		List<SaleOrderDTO> result = saleOrderService.listSaleOrder(dto,user);
//		String mainSheetName = "销售订单";
//		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, SALE_COLUMNS, SALE_KEYS, result);
//		sheetList.add(mainSheet) ;
//		// 表体
//		List<SaleOrderItemDTO> itemList = new ArrayList<SaleOrderItemDTO>();
//		List<SaleShelfDTO> shelfList = new ArrayList<SaleShelfDTO>();
//		SaleOrderItemModel itemModel = new SaleOrderItemModel();
//		for(SaleOrderDTO sModel:result){
//			//商品信息
//			List<SaleOrderItemDTO> itemList1 = saleOrderService.listItemByOrderId(sModel.getId());
//			for(SaleOrderItemDTO item:itemList1){
//				item.setOrderCode(sModel.getCode());
//				item.setPoNo(sModel.getPoNo());
//				item.setCommBrandParentName(saleOrderService.getBrandParentName(item.getCommbarcode()));
//
//			}
//			if(itemList1 != null && itemList1.size()>0){
//				itemList.addAll(itemList1);
//			}
//
//			//上架信息
//			List<SaleShelfDTO> shelfList1 = saleShelfService.searchDetailByOrderId(sModel.getId());
//			if(shelfList1 != null && shelfList1.size() > 0) {				
//				for(SaleShelfDTO shelf : shelfList1){
//					itemModel.setOrderId(sModel.getId());
//					itemModel.setGoodsId(shelf.getGoodsId());
//					SaleOrderItemModel iModel = saleOrderService.getItemByCodeAndNo(itemModel);
//					
//					shelf.setCode(sModel.getCode());
//					shelf.setCommBrandParentName(saleOrderService.getBrandParentName(shelf.getCommbarcode()));
//					if(iModel != null){
//						shelf.setPrice(iModel.getPrice());
//					}
//					shelf.setCurrency(sModel.getCurrency());
//				}
//				shelfList.addAll(shelfList1);
//			}
//
//		}
//		String itemSheetName = "商品信息";
//		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, ITEM_COLUMNS, ITEM_KEYS, itemList);
//		sheetList.add(itemSheet) ;
//
//		String shelfSheetName = "上架信息";
//		ExportExcelSheet shelfSheet = ExcelUtilXlsx.createSheet(shelfSheetName, SHELF_COLUMNS, SHELF_KEYS, shelfList);
//		sheetList.add(shelfSheet) ;
//
//
//		//生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
//	}
//
//	/**
//	 * 校验订单能否生成采购订单（已审核、待审核的销售订单）
//	 * */
//	@RequestMapping("/checkOrderState.asyn")
//	@ResponseBody
//	private ViewResponseBean checkOrderState(Long id) {
//		SaleOrderDTO saleOrderDTO = null;
//		try{
//			// 响应结果集
//			saleOrderDTO = saleOrderService.checkOrderState(id);
//			
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(saleOrderDTO);
//	}
//	/**
//	 * 访问上架页面
//	 * */
//	@RequestMapping("/toSaleShelfPage.html")
//	public String toSaleShelfPage(Model model, Long id, HttpSession session)throws Exception{
//		User user= ShiroUtils.getUser();
//		SaleOrderDTO saleOrderParam = new SaleOrderDTO();
//		saleOrderParam.setId(id);
//		SaleOrderDTO saleOrderDTO = saleOrderService.queryInfoByOId(saleOrderParam);
//		List<SaleShelfModel> shelfList = saleOrderService.listShelfListByOrderId(saleOrderDTO);
//
//		// 根据销售订单ID在销售单_po关联表中查询它的PO单号
//		List<SalePoRelModel> saleOrderByPoList = salePoRelService.getAllByOrderId(saleOrderDTO.getId(),user.getMerchantId());
//		List<String>poList=new ArrayList<>();	// 存放该销售订单的所有PO单号
//		for (int i = 0; i < saleOrderByPoList.size(); i++) {
//			poList.add(saleOrderByPoList.get(i).getPoNo());
//		}
//
//		model.addAttribute("poList", poList);
//		model.addAttribute("detail", saleOrderDTO);
//		model.addAttribute("shelfList", shelfList);
//		return "/derp/sale/sale-shelf";
//	}
//	/**
//	 * 保存上架商品信息
//	 * @param json
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/saveSaleShelf.asyn")
//	@ResponseBody
//	public ViewResponseBean saveSaleShelf(String json, HttpSession session) {
//		Map<String,Object> result = new HashMap<String, Object>();
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            result = saleOrderService.saveSaleShelf(json,user);
//            
//        }catch(Exception e){
//			e.printStackTrace();
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(result);
//	}
//
//	/**
//	 * 获取上架明细分页数据
//	 * */
//	@RequestMapping("/listSaleShelf.asyn")
//	@ResponseBody
//	private ViewResponseBean listSaleShelf(SaleShelfModel model) {
//		try{
//			// 响应结果集
//			model = saleOrderService.listSaleShelfByPage(model);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(model);
//	}
//
//	/**
//	 * 判断是否已经完全上架
//	 * */
//	@RequestMapping("/shelfIsEnd.asyn")
//	@ResponseBody
//	private ViewResponseBean shelfIsEnd(Long id) {
//		boolean flag = false;
//		try{
//			// 响应结果集
//			flag = saleOrderService.shelfIsEnd(id);
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(flag);
//	}
//	/**
//	 * 签收
//	 * */
//	@RequestMapping("/receiveSaleOrder.asyn")
//	@ResponseBody
//	private ViewResponseBean receiveSaleOrder(Long id,String receiveDate,String manyPoNo ,HttpSession session) {
//		try{
//            if(id == null || StringUtils.isBlank(receiveDate)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//			// 响应结果集
//			boolean bl = saleOrderService.receiveSaleOrder(id,receiveDate,manyPoNo,user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName());
//			if(!bl){
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//        }catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}
//
//	/**
//	 * 访问编辑页面-----------预售转销
//	 * @param model
//	 * @param data
//	 * @param session
//	 * @param request
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/preSaleEditPage.html")
//	public String preSaleEditPage(Model model,String data,HttpSession session,HttpServletRequest request) throws Exception {
//		JSONObject jsonObj = JSONObject.fromObject(data);
//		String type = String.valueOf(jsonObj.get("type"));
//		String operate = "1";
//		if (jsonObj.containsKey("operate") && jsonObj.getString("operate") != null&& !"".equals(jsonObj.getString("operate"))) {
//			operate = String.valueOf(jsonObj.get("operate"));
//		}
//		SaleOrderDTO saleOrderDTO =new SaleOrderDTO();
//		if(type.equals("1")){// 生成预售转销
//			saleOrderDTO = saleOrderService.preSaleOrderToSaleEdit(data);
//		}else if(type.equals("2")){// 销售订单编辑预售转销的订单
//			Long saleOrderId = Long.valueOf(jsonObj.getString("saleOrderId"));
//			saleOrderDTO = saleOrderService.searchDetail(saleOrderId);
//			List<SaleOrderItemDTO> itemList = saleOrderService.listItemByOrderId(saleOrderId);
//			saleOrderDTO.setItemList(itemList);
//		}
//
//
//		model.addAttribute("itemCount", saleOrderDTO.getItemList().size());
//		model.addAttribute("detail", saleOrderDTO);
//		DepotInfoMongo outDepot = depotInfoService.getDetails(saleOrderDTO.getOutDepotId());
//
//		String isSameAreaDisabled = "false";	// 是否同关区是否禁用
//		int isSameAreaRequired = 0;	// 是否同关区是否必填
//		String inDepotDisabled = "false";// 入库仓库是否禁用
//		int inDepotRequired = 0;// 入库仓库是否必填
//		int isContractNoRequired = 0;	// 合同号是否必填
//		int isHKOutDepot = 0;	// 出仓仓库为香港仓，收件信息必填
//
//		/**
//		 * 1、当出仓仓库为保税仓时，是否同关区必填；
//		 * 2、当出仓仓库为海外仓时，是否同关区禁用；
//		 * 3、当出仓仓库为中转仓时，是否同关区非必填；
//		 */
//		/**
//		 * 1、当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填；
//		 * 2、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库禁用；
//		 * 3、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填；
//		 * 4、当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
//		 * 5、当出仓仓库为海外仓且销售类型为购销-整批结算时，入库仓库禁用，不予选择；
//		 * 6、当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填
//				*/
//		if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())){
//			isSameAreaRequired = 1;
//			if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
//				inDepotRequired = 1;
//			}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
//					DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
//				inDepotDisabled = "true";
//			}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
//					DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
//				inDepotRequired = 1;
//			}
//		}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
//			isSameAreaDisabled = "true";	// 禁用
//			isHKOutDepot=1;//1:必填
//			// 当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
//			if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
//				inDepotDisabled = "false";
//			}else if(DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
//				// 当出仓仓库为海外仓且销售类型为购销-整批结算时，入库仓库禁用，不予选择；
//				inDepotDisabled = "true";
//			}
//		}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())){
//			isSameAreaRequired = 0;
//			if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
//				inDepotRequired = 1;
//			}
//		}
//		// 同关区时合同号必填
//		if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
//			isContractNoRequired = 1;
//		}
//
//		model.addAttribute("buId", saleOrderDTO.getBuId());
//		model.addAttribute("inDepotId", saleOrderDTO.getInDepotId());
//		model.addAttribute("outDepotId", saleOrderDTO.getOutDepotId());
//		model.addAttribute("customsNo", outDepot.getCustomsNo());
//		model.addAttribute("inDepotRequired", inDepotRequired);
//		model.addAttribute("inDepotDisabled", inDepotDisabled);
//		model.addAttribute("isSameAreaDisabled", isSameAreaDisabled);
//		model.addAttribute("isSameAreaRequired", isSameAreaRequired);
//		model.addAttribute("isContractNoRequired", isContractNoRequired);
//		model.addAttribute("outDepotType", outDepot.getType());
//		model.addAttribute("isHKOutDepot", isHKOutDepot);
//		model.addAttribute("operate",operate);
//		return "/derp/sale/presale-to-sale-edit";
//	}
//
//	@RequestMapping("/showOwnPurchaseOrder.asyn")
//	@ResponseBody
//	private ViewResponseBean showOwnPurchaseOrder(String json,HttpSession session) {
//		List<PurchaseOrderDTO> purchaseOrderList = null;
//		try{
//			User user= ShiroUtils.getUser();
//			// 响应结果集
//			purchaseOrderList = purchaseOrderService.getOwnPurchaseOrder(json,user.getMerchantId(),user.getTopidealCode());
//		}catch(SQLException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(purchaseOrderList);
//	}
//
//	/**
//	 * 商品导出
//	 * @param response
//	 * @param request
//	 * @param id
//	 * @throws Exception
//	 */
//	@RequestMapping("/exportItems.asyn")
//	public void export(HttpServletResponse response, HttpServletRequest request, String  json) throws Exception{
//		/** 标题  */
//		String[] COLUMNS= {"序号","商品货号","条形码","托盘号","箱数","销售数量","销售总金额","申报单价","净重","毛重"};
//		String[] KEYS= {"seq","goodsNo","barcode","torrNo","boxNum","num","amount","declarePrice","netWeightSum","grossWeightSum"};
//
//		String sheetName = "商品导出";
//		List<SaleOrderItemDTO> itemList=null;
//		if(json != null || StringUtils.isNotBlank(json)){			//输入信息不完整
//			itemList = saleOrderService.exportListItem(json);
//		}
//		//生成表格
//		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , itemList);
//		//导出文件
//		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
//	}
//
//	/**
//	 * 判断是否可以出库打托
//	 * */
//	@RequestMapping("/checkOutDepotOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean checkOutDepotOrder(Long id) {
//		Map<String, String> result = new HashMap<>();
//		try{
//			// 响应结果集
//			result = saleOrderService.checkOutDepotOrder(id);
//		} catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(result);
//	}
//
//	/**
//	 * 访问出库打托页面
//	 * */
//	@RequestMapping("/toSaleOutPage.html")
//	public String toSaleOutPage(Model model, Long id, HttpSession session)throws Exception{
//		SaleOrderDTO saleOrderDTO = saleOrderService.searchDetail(id);
//		List<SaleOrderItemDTO> itemList = saleOrderService.listItemByOrderId(id);
//		saleOrderDTO.setItemList(itemList);
//		model.addAttribute("detail", saleOrderDTO);
//		return "/derp/sale/sale-out";
//	}
//
//	/**
//	 * 保存出库打托信息
//	 * @param json
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/saveSaleOut.asyn")
//	@ResponseBody
//	public ViewResponseBean saveSaleOut(String json, HttpSession session) {
//		try{
//			if(json == null || org.apache.commons.lang.StringUtils.isBlank(json)){
//				//输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			User user= ShiroUtils.getUser();
//			boolean b = saleOrderService.saveSaleOrderOut(json,user);
//			if(!b){
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		}catch(NullPointerException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}
//
//	/**
//	 * 显示金额调整信息
//	 * @param model
//	 * @param id
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/showOrderAmount.asyn")
//	@ResponseBody
//	public ViewResponseBean showOrderAmount(Model model, Long id, HttpSession session) {
//		SaleOrderDTO saleOrderDTO = null;
//		try {
//			saleOrderDTO = saleOrderService.searchDetail(id);
//			List<SaleOrderItemDTO> itemList = saleOrderService.listItemByOrderId(id);
//			saleOrderDTO.setItemList(itemList);
//		} catch (SQLException e) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}
//		return ResponseFactory.success(saleOrderDTO);
//	}
//
//
//	/**
//	 * 金额调整
//	 * @param model
//	 * @param id
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/amountAdjust.asyn")
//	@ResponseBody
//	public ViewResponseBean amountAdjust(String json,HttpSession session) {
//		try{
//			if(json == null || StringUtils.isBlank(json)){
//				//输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			User user= ShiroUtils.getUser();
//			boolean b = saleOrderService.amountAdjust(json,user);
//			if(!b){
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		}catch(NullPointerException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}
//	/**
//	   * 判断订单商品上架月份是否已关账
//	 * */
//	@RequestMapping("/checkExistFinanceClose.asyn")
//	@ResponseBody
//	public ViewResponseBean checkExistFinanceClose(Long id) {
//		Map<String, String> result = new HashMap<>();
//		try{
//			User user = ShiroUtils.getUser();
//			// 响应结果集
//			result = saleOrderService.checkExistFinanceClose(user,id);
//		} catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(result);
//	}
//	
//	/**
//	 * 提交并审核 判断是否存在采购单
//	 * */
//	@RequestMapping("/checkExistPurchase.asyn")
//	@ResponseBody
//	public ViewResponseBean checkExistPurchase(SaleOrderDTO dto) {
//		Map<String, String> result = new HashMap<>();
//		try{
//			User user = ShiroUtils.getUser();
//			// 响应结果集
//			result = saleOrderService.checkExistPurchase(dto,user);
//		} catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(result);
//	}
//	
//	/**
//	 * 新增/编辑页面 提交并审核生成采购订单
//	 * @param json
//	 * @param buId
//	 * @param depotId
//	 * @return
//	 */
//	@RequestMapping("/createPurchaseOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean createPurchaseOrder(Long saleId,Long buId,Long depotId) {
//		 String msg = null;
//		try{
//            
//            User user= ShiroUtils.getUser();
//            msg = saleOrderService.createPurchaseOrder(saleId,buId,depotId,user);
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(msg);
//	}
//	
//	/**
//	 * 根据PO号判断是否存在采购单
//	 * */
//	@RequestMapping("/checkExistPurchaseByPO.asyn")
//	@ResponseBody
//	public ViewResponseBean checkExistPurchaseByPO(String poNo) {
//		Map<String, String> result = new HashMap<>();
//		try{
//			User user = ShiroUtils.getUser();
//			// 响应结果集
//			result = saleOrderService.checkExistPurchaseByPO(poNo,user);
//		} catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success(result);
//	}
//	
//	/**
//	 * 列表页面 生成采购订单
//	 * @param json
//	 * @param buId
//	 * @param depotId
//	 * @return
//	 */
//	@RequestMapping("/GeneratePurchaseOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean GeneratePurchaseOrder(String json) {
//		Long id = null;
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            id = saleOrderService.GeneratePurchaseOrder(json,user);
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success(id);
//	}
//	
//	/**
//	 * 金额调整
//	 * @param model
//	 * @param id
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/amountConfirm.asyn")
//	@ResponseBody
//	public ViewResponseBean amountConfirm(String json) {
//		try{
//			if(json == null || StringUtils.isBlank(json)){
//				//输入信息不完整
//				return ResponseFactory.error(StateCodeEnum.ERROR_303);
//			}
//			User user= ShiroUtils.getUser();
//			boolean b = saleOrderService.amountConfirm(json,user);
//			if(!b){
//				return ResponseFactory.error(StateCodeEnum.ERROR_301);
//			}
//		}catch(NullPointerException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//		}catch(RuntimeException e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//		}catch(Exception e){
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//		}
//		return ResponseFactory.success();
//	}
//	
//	/**
//	 * 导出清关资料
//	 * @param session
//	 * @param response
//	 * @param request
//	 * @param id
//	 * @throws Exception
//	 */
//	@RequestMapping("/exportCustomsInfo.asyn")
//	public void exportCustomsDeclare(HttpSession session, HttpServletResponse response, HttpServletRequest request,String json) throws Exception {
//		try {
//			JSONObject jsonData = JSONObject.fromObject(json);
//			Long id = Long.valueOf(jsonData.getString("id"));
//			String inFileTempIds = (String) jsonData.get("inFileTempIds");
//			String outFileTempIds = (String) jsonData.get("outFileTempIds");
//
//			if (id == null) {
//				return;
//			}
//			boolean flag = false;
//			Map<String, Workbook> resultMap = new HashMap<>();
//			Map<String, FirstCustomsInfoDTO> wordMap = new HashMap<String, FirstCustomsInfoDTO>();
//			if (!StringUtils.isEmpty(inFileTempIds)) {
//				List<Long> inFileTempIdList = new ArrayList<>();
//				List<String> list = Arrays.asList(inFileTempIds.split(","));
//				for (String idStr : list) {
//					inFileTempIdList.add(Long.valueOf(idStr));
//				}
//				Map<String, FirstCustomsInfoDTO> resultInMap = saleOrderService.exportDepotCustomsDeclares(id, inFileTempIdList,"2");
//				Map<String,Workbook> multipartInCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultInMap, "2");
//				resultMap.putAll(multipartInCustomsDeclares);
//				if (resultInMap.containsKey(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU)) {					
//					wordMap.put("入仓仓库",resultInMap.get(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU));
//					flag = true;
//				}
//			}
//
//			if (!StringUtils.isEmpty(outFileTempIds)) {
//				List<Long> outFileTempIdList = new ArrayList<>();
//				List<String> list = Arrays.asList(outFileTempIds.split(","));
//				for (String idStr : list) {
//					outFileTempIdList.add(Long.valueOf(idStr));
//				}
//				Map<String, FirstCustomsInfoDTO> resultOutMap = saleOrderService.exportDepotCustomsDeclares(id, outFileTempIdList,"1");
//				Map<String, Workbook> multipartOutCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultOutMap, "1");
//				resultMap.putAll(multipartOutCustomsDeclares);
//				if (resultOutMap.containsKey(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU)) {					
//					wordMap.put("出仓仓库",resultOutMap.get(DERP_ORDER.TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU));
//					flag = true;
//				}
//			}
//			//导出压缩文件
//			String downloadFilename = URLEncoder.encode("销售订单一线清关资料.zip", "UTF-8");
//			// 指明response的返回对象是文件流
//			response.setContentType("application/octet-stream");
//			// 设置在下载框默认显示的文件名
//			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
//			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
//
//			if (flag) {
//				for(String key : wordMap.keySet()) {
//					FirstCustomsInfoDTO model = wordMap.get(key);
//					XWPFDocument document = DownloadExcelUtil.createCustomsDeclareWord(model);
//					zos.putNextEntry(new ZipEntry(key+"郑州清关资料模板（箱单加盖公章）.doc"));
//					ByteArrayOutputStream bos = new ByteArrayOutputStream();
//					document.write(bos);
//					bos.writeTo(zos);
//					zos.closeEntry();					
//				}
//			}
//
//			for (String key : resultMap.keySet()) {
//				Workbook wb = resultMap.get(key);
//				zos.putNextEntry(new ZipEntry(key));
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				wb.write(bos);
//				bos.writeTo(zos);
//				zos.closeEntry();
//			}
//			
//			zos.flush();
//			zos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 提交
//	 * @param json
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping("/submitSaleOrder.asyn")
//	@ResponseBody
//	public ViewResponseBean submitSaleOrder(String json,HttpSession session) {
//		try{
//            if(json == null || StringUtils.isBlank(json)){
//                //输入信息不完整
//                return ResponseFactory.error(StateCodeEnum.ERROR_303);
//            }
//            User user= ShiroUtils.getUser();
//            Long id = saleOrderService.submitSaleOrder(json,user);
//          
//        }catch(SQLException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(NullPointerException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
//        }catch(RuntimeException e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_302,e);
//        }catch(Exception e){
//            return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
//        }
//        return ResponseFactory.success();
//	}
//	
//	/**
//	 * 是否启用销售价格管理
//	 * @param buId
//	 * @return
//	 */
//	@RequestMapping("/getSalePriceManage.asyn")
//	@ResponseBody
//	public ViewResponseBean getSalePriceManage(Long buId,Long customerId) {
//		
//		if(buId == null || customerId == null) {
//			return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//		}		
//		try {
//			User user = ShiroUtils.getUser();
//			boolean flag = saleOrderService.getSalePriceManage(buId,customerId,user) ;
//		
//			return ResponseFactory.success(flag) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
//		}
//		
//	}
//	/**
//	 * 获取销售价格管理的价格
//	 * @param buId
//	 * @return
//	 */
//	@RequestMapping("/getSalePrice.asyn")
//	@ResponseBody
//	public ViewResponseBean getSalePrice(String json) {		
//		try {
//			if(StringUtils.isBlank(json)) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//			User user = ShiroUtils.getUser();
//			String salePrice = saleOrderService.getSalePrice(json,user.getMerchantId()) ;
//		
//			return ResponseFactory.success(salePrice) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e) ;
//		}
//		
//	}
//	/**
//	 * 查询融资申请详情
//	 * 查询宝丰公司（ERP19095700016）是否存在对应PO号的采购单
//	 * @param id
//	 * @return
//	 */
//	@RequestMapping("/getFinanceDetail.asyn")
//	@ResponseBody
//	public ViewResponseBean getFinanceDetail(Long orderId) {
//		try {
//			if(orderId == null) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//			User user = ShiroUtils.getUser();
//			SaleFinancingOrderDTO result = saleOrderService.getFinanceDetail(orderId,user) ;
//
//			return ResponseFactory.success(result) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e) ;
//		}
//
//	}
//
//	/**
//	 * 融资申请
//	 * @param dto
//	 * @return
//	 */
//	@RequestMapping("/applyFinance.asyn")
//	@ResponseBody
//	public ViewResponseBean applyFinance(SaleFinancingOrderDTO dto,String items) {
//		try {
//			// 必填项空值校验
//			boolean isNull = new EmptyCheckUtils().addObject(dto.getSaleAmount()).addObject(dto.getApplyDate())
//					.addObject(dto.getActualMarginAmount()).empty();
//
//			if(isNull) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//			if(StringUtils.isBlank(items)) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//			List<SaleFinancingOrderItemDTO> itemList = (List<SaleFinancingOrderItemDTO>) JSONArray.parseArray(items, SaleFinancingOrderItemDTO.class);
//			dto.setItemList(itemList);
//			User user = ShiroUtils.getUser();
//			String result = saleOrderService.applyFinance(dto,user) ;
//
//			return ResponseFactory.success(result) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e) ;
//		}
//	}
//
//	/**
//	 * 还款试算
//	 * @param orderId
//	 * @return
//	 */
//	@RequestMapping("/calRepayAmount.asyn")
//	@ResponseBody
//	public ViewResponseBean calRepayAmount(Long orderId,String dealineDate) {
//		try {
//			if(orderId == null) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//
//			SaleFinancingOrderDTO result = saleOrderService.calRepayAmount(orderId,dealineDate) ;
//
//			return ResponseFactory.success(result) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e) ;
//		}
//	}
//
//	/**
//	 * 申请赎回
//	 * @param dealineDate
//	 * @param orderId
//	 * @return
//	 */
//	@RequestMapping("/applyRansom.asyn")
//	@ResponseBody
//	public ViewResponseBean applyRansom(String dealineDate, Long orderId) {
//		try {
//			// 必填项空值校验
//			boolean isNull = new EmptyCheckUtils().addObject(orderId).addObject(dealineDate).empty();
//
//			if(isNull) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//
//			SaleFinancingOrderDTO result = saleOrderService.applyRansom(dealineDate,orderId) ;
//
//			return ResponseFactory.success(result) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e) ;
//		}
//	}
//
//	/**
//	 * 确认赎回
//	 * @param dealineDate
//	 * @param orderId
//	 * @return
//	 */
//	@RequestMapping("/confirmRansom.asyn")
//	@ResponseBody
//	public ViewResponseBean confirmRansom(String dealineDate,Long orderId) {
//		try {
//			// 必填项空值校验
//			boolean isNull = new EmptyCheckUtils().addObject(orderId).addObject(dealineDate).empty();
//
//			if(isNull) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//			User user = ShiroUtils.getUser();
//			String result = saleOrderService.confirmRansom(dealineDate,orderId ,user) ;
//
//			return ResponseFactory.success(result) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e) ;
//		}
//	}
//	/**
//	 * 申请赎回 作废
//	 * @param dealineDate
//	 * @param orderId
//	 * @return
//	 */
//	@RequestMapping("/cancelRansom.asyn")
//	@ResponseBody
//	public ViewResponseBean cancelRansom(String dealineDate,Long orderId) {
//		try {
//			// 必填项空值校验
//			boolean isNull = new EmptyCheckUtils().addObject(orderId).addObject(dealineDate).empty();
//
//			if(isNull) {
//				return ResponseFactory.error(StateCodeEnum.ERROR_302) ;
//			}
//
//			SaleFinancingOrderDTO result = saleOrderService.cancelRansom(dealineDate,orderId) ;
//
//			return ResponseFactory.success(result) ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseFactory.error(StateCodeEnum.ERROR_305,e) ;
//		}
//	}
//}

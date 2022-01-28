package com.topideal.order.webapi.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.PurchaseOrderDTO;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.purchase.PurchaseOrderService;
import com.topideal.order.service.sale.SaleOrderService;
import com.topideal.order.service.sale.SaleOutDepotService;
import com.topideal.order.service.sale.SalePoRelService;
import com.topideal.order.service.sale.SaleShelfService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.OrderGoodsInfoNumDTO;
import com.topideal.order.webapi.sale.dto.ResultDTO;
import com.topideal.order.webapi.sale.dto.SaleOrderResponseDTO;
import com.topideal.order.webapi.sale.form.SaleOrderAddForm;
import com.topideal.order.webapi.sale.form.SaleOrderForm;
import com.topideal.order.webapi.sale.form.SaveSaleOutDepotForm;
import com.topideal.order.webapi.sale.form.ShelfForm;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * webapi 销售订单
 *
 */
@RequestMapping("/webapi/order/sale")
@RestController
@Api(tags = "销售订单")
public class APISaleOrderController {
	/* 打印日志 */
	protected Logger logger = LoggerFactory.getLogger(APISaleOrderController.class);

	private static final String[] IMPORT_COLUMNS = { "销售单号","PO号","商品货号","条形码","商品名称"
			,"上架时间","上架正常品数量","残损数量","少货量","备注"};
	private static final String[] IMPORT_KEYS = {"saleCode" , "poNo","goodsNo","barcode","goodsName","shelfDate","shelfNum","damagedNum","lackNum","remark"} ;


	private static final String[] SALE_COLUMNS = {"销售订单编号","订单状态","入仓仓库名称","出仓仓库名称","事业部","LBX单号","PO单号","客户",
			"是否内部公司客户","销售类型","销售币种","销售总数量","销售总金额","创建人","创建时间","审核人","审核时间","海外仓理货单位","运输方式","库位类型"};
	private static final String[] SALE_KEYS = {"code","stateLabel","inDepotName","outDepotName","buName","lbxNo","poNo",
			"customerName","isInnerCustomer","businessModelLabel","currencyLabel","totalNum","totalAmount","createName","createDate",
			"auditName","auditDate","tallyingUnitLabel","transportLabel","stockLocationTypeName"};
	private static final String[] ITEM_COLUMNS ={"销售订单编号","PO号","标准品牌","商品编号","商品货号","商品名称","商品条形码","计量单位","销售数量",
			"销售单价","销售总金额","税率","税额"};
	private static final String[] ITEM_KEYS = {"orderCode","poNo","commBrandParentName","goodsCode","goodsNo","goodsName","barcode",
			"unit","num","price","amount","taxRate","tax"};

	private static final String[] SHELF_COLUMNS ={"销售订单编号","标准品牌","商品货号","商品名称","商品条形码","PO号","上架好品量","残损量","少货量",
			"上架时间","销售单价","销售币种"};
	private static final String[] SHELF_KEYS = {"code","commBrandParentName","goodsNo","goodsName","barcode","poNo","shelfNum",
			"damagedNum","lackNum","shelfDate","price","currency"};

	// 销售订单service
	@Autowired
	private SaleOrderService saleOrderService;
	// 销售出库service
	@Autowired
	private SaleOutDepotService saleOutDepotService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;
	// 销售单_po关联表
	@Autowired
	private SalePoRelService salePoRelService;
	// 采购订单service
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private SaleShelfService saleShelfService;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private BrandParentMongoDao brandParentMongoDao;


	/**
	 * 访问新增页面
	 * @param model
	 * @param ids   采购订单/调拨订单 ID
	 * @param type  1  采购订单， 2 调拨订单
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "访问新增页面")
	@PostMapping(value="/toAddPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOrderResponseDTO> toAddPage(SaleOrderForm form)throws Exception{
		SaleOrderResponseDTO responseDTO = new SaleOrderResponseDTO();
		try {
			/*
			 *	点击复制
			 */
			User user= ShiroUtils.getUserByToken(form.getToken());
			responseDTO.setPageSource(form.getPageSource());
			boolean salePriceManage = false;
			if(StringUtils.isNotBlank(form.getId()) || StringUtils.isNotBlank(form.getPlatformPurchaseIds())) {
				SaleOrderDTO saleOrderDTO = null;
				List<SaleOrderItemDTO> saleOrderItemList = null;

				if(StringUtils.isNotBlank(form.getId())) {
					saleOrderDTO = saleOrderService.searchDetail(Long.valueOf(form.getId()));
					saleOrderItemList = saleOrderService.listItemByOrderId(Long.valueOf(form.getId()));
					responseDTO.setSaleOrderDTO(saleOrderDTO);
					responseDTO.setSaleOrderItemList(saleOrderItemList);
				}
				//平台采购单转销售
				else if(StringUtils.isNotBlank(form.getPlatformPurchaseIds())) {
					Map<String, Object> salesMap = saleOrderService.modifyPlatFormPurchaseToSales(form.getPlatformPurchaseIds(), form.getOutDepotId(), form.getBuId(), form.getPlatformSalesNum(),user);

					saleOrderDTO = (SaleOrderDTO)salesMap.get("saleOrderDTO")  ;
					saleOrderItemList = (List<SaleOrderItemDTO>)salesMap.get("saleOrderItemList")  ;

					if(saleOrderDTO == null) {
						saleOrderDTO = new SaleOrderDTO() ;
						saleOrderDTO.setOutDepotId(Long.valueOf(form.getOutDepotId()));
					}

					if(saleOrderItemList == null) {
						saleOrderItemList = new ArrayList<>();
					}
					responseDTO.setPlatformPurchaseIds(form.getPlatformPurchaseIds());
					responseDTO.setSaleOrderDTO((SaleOrderDTO)salesMap.get("saleOrderDTO"));
					responseDTO.setSaleOrderItemList((List<SaleOrderItemDTO>)salesMap.get("saleOrderItemList"));
					if(salesMap.containsKey("errorMsg")) {
						responseDTO.setErrorMsg((String)salesMap.get("errorMsg"));
					}
				}

				int isTrainnoRequired = 0;	// 车次是否必填
				int isTeuRequired = 0;	// 标准箱TEU是否必填
				String isSameAreaDisabled = "false";	// 是否同关区是否禁用
				int isSameAreaRequired = 0;	// 是否同关区是否必填
				String inDepotDisabled = "false";// 入库仓库是否禁用
				int inDepotRequired = 0;// 入库仓库是否必填
				int isPoRequired = 0;// PO单号是否必填
				int isContractNoRequired = 0;	// 合同号是否必填
				DepotInfoMongo outDepot = depotInfoService.getDetails(saleOrderDTO.getOutDepotId());


				if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())){
					isSameAreaRequired = 1;
					if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
						inDepotRequired = 1;
					}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
							DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
						inDepotRequired = 1;
					}
				}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
					isSameAreaDisabled = "true";	// 禁用
				}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())){
					isSameAreaRequired = 0;
					if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
						inDepotRequired = 1;
					}
				}
				/**
				 * 1、当销售类型为购销-实销实结、采购执行时，PO单号非必填；
				 * 2、当销售类型为购销-整批结算时，PO单号必填项
				 */
				if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel()) &&
						DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderDTO.getBusinessModel())){
					isPoRequired = 0;
				}else if(DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
					isPoRequired = 1;
				}
				// 同关区时合同号必填
				if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
					isContractNoRequired = 1;
				}
				//标准箱TEU:当运输方式为海运时必填
				if(DERP.TRANSPORT_2.equals(saleOrderDTO.getTransport())){
					isTeuRequired = 1;
				}
				//车次:当运输方式为陆运、港到仓拖车时必填
				if(DERP.TRANSPORT_3.equals(saleOrderDTO.getTransport()) ||
						DERP.TRANSPORT_4.equals(saleOrderDTO.getTransport())){
					isTrainnoRequired = 1;
				}

				responseDTO.setInDepotRequired(String.valueOf(inDepotRequired));
				responseDTO.setInDepotDisabled(inDepotDisabled);
				responseDTO.setIsSameAreaDisabled(isSameAreaDisabled);
				responseDTO.setIsSameAreaRequired(String.valueOf(isSameAreaRequired));
				responseDTO.setIsPoRequired(String.valueOf(isPoRequired));
				responseDTO.setIsContractNoRequired(String.valueOf(isContractNoRequired));
				responseDTO.setIsTrainnoRequired(String.valueOf(isTrainnoRequired));
				responseDTO.setIsTeuRequired(String.valueOf(isTeuRequired));
				if(outDepot != null) {
					responseDTO.setOutDepotType(outDepot.getType());
				}

				salePriceManage = saleOrderService.getSalePriceManage(saleOrderDTO.getBuId(),saleOrderDTO.getCustomerId(),user);
			}
			responseDTO.setSalePriceManage(salePriceManage);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}
	/**
	 * 销售新增购销退货获取分页数据
	 *
	 * @param model
	 */
	@ApiOperation(value = "查询销售退新增购销退货列表信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codes", value = "销售订单code集合，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/saleGetListSaleOrderByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleOrderDTO> saleGetListSaleOrderByPage(SaleOrderForm form,String codes) {
		SaleOrderDTO dto = new SaleOrderDTO();
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setInDepotId(StringUtils.isNotBlank(form.getInDepotId()) ? Long.valueOf(form.getInDepotId()) : null);
			dto.setPoNo(form.getPoNo());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());

			// 响应结果集
			dto = saleOrderService.saleGetListSaleOrderByPage(dto, codes,user);

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}


	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOrderResponseDTO> toDetailsPage(String token,Long id)throws Exception{
		SaleOrderResponseDTO responseDTO = new SaleOrderResponseDTO();
		try {
			List<SaleShelfDTO> saleShelfDTOList = saleShelfService.searchDetailByOrderId(id);
			SaleShelfDTO saleShelfDTO = new SaleShelfDTO();
			saleShelfDTO.setSaleShelfDTOList(saleShelfDTOList);

			SaleOrderDTO saleOrderDTO = saleOrderService.searchDetail(id);
			List<SaleOrderItemDTO> itemList = saleOrderService.listItemByOrderId(id);
			saleOrderDTO.setItemList(itemList);

			if(id != null) {
				SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
				saleOutDepotModel.setSaleOrderId(id);
				//根据销售订单id查询销售出库信息
				List<SaleOutDepotModel> saleOutModel = saleOutDepotService.listSaleOutDepotModel(saleOutDepotModel);
				if(saleOutModel.size() >= 1) {
					responseDTO.setSaleOutModel(saleOutModel.get(saleOutModel.size()-1));
				}
				SaleOrderDTO saleOrderDTO2 =new SaleOrderDTO();
				saleOrderDTO2.setId(id);
				//根据销售订单id查询销售上架信息
//				List<SaleShelfModel> listShelfList = saleOrderService.listShelfListByOrderId(saleOrderDTO2);
//				if(listShelfList.size() > 0) {
//					// 该销售订单有商品上架
//					responseDTO.setIsNotShelf("yes");
//				}else{
//					// 该销售订单还没有商品上架
//					responseDTO.setIsNotShelf("no");
//				}
			}
			responseDTO.setSaleOrderDTO(saleOrderDTO);
			responseDTO.setSaleShelfDTO(saleShelfDTO);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}

	/**
	 * 访问编辑页面
	 * @param model
	 * @param id
	 * @param purchaseId 从新增内部销售单过来的
	 * @param outDepotId 从新增内部销售单过来的
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "访问编辑页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true),
		@ApiImplicitParam(name = "purchaseId", value = "采购订单id", required = false),
		@ApiImplicitParam(name = "outDepotId", value = "出库仓id", required = false)
	})
	@PostMapping(value="/toEditPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOrderResponseDTO> toEditPage(String token,Long id,String purchaseId,String outDepotId,String operate)throws Exception{
		SaleOrderResponseDTO responseDTO = new SaleOrderResponseDTO();
		try {
			User user= ShiroUtils.getUserByToken(token);
			SaleOrderDTO saleOrderDTO = new SaleOrderDTO();
			List<SaleOrderItemDTO> itemList = new ArrayList<>();
			// 从新增内部销售单过来
			if(StringUtils.isNotBlank(purchaseId)){
				saleOrderDTO = saleOrderService.getSaleOrderByPurchaseId(Long.valueOf(purchaseId), Long.valueOf(outDepotId), user.getMerchantId(),user.getMerchantName());
				itemList = saleOrderDTO.getItemList();
			}else{
				saleOrderDTO = saleOrderService.searchDetail(id);
				itemList = saleOrderService.listItemByOrderId(id);
			}
			saleOrderDTO.setItemList(itemList);
			DepotInfoMongo outDepot = depotInfoService.getDetails(saleOrderDTO.getOutDepotId());

			responseDTO.setItemCount(String.valueOf(itemList.size()));
			responseDTO.setSaleOrderDTO(saleOrderDTO);
			if(outDepot != null) {
				responseDTO.setOutDepotType(outDepot.getType());
				responseDTO.setCustomsNo(outDepot.getCustomsNo());
			}


			String isSameAreaDisabled = "false";	// 是否同关区是否禁用
			int isSameAreaRequired = 0;	// 是否同关区是否必填
			String inDepotDisabled = "false";// 入库仓库是否禁用
			int inDepotRequired = 0;// 入库仓库是否必填
			int isPoRequired = 0;// PO单号是否必填
			int isContractNoRequired = 0;	// 合同号是否必填
			int isTrainnoRequired = 0;	// 车次是否必填
			int isTeuRequired = 0;	// 标准箱TEU是否必填

			/**
			 * 1、当出仓仓库为保税仓时，是否同关区必填；
			 * 2、当出仓仓库为海外仓时，是否同关区禁用；
			 * 3、当出仓仓库为中转仓时，是否同关区非必填；
			 */
			/**
			 * 1、当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填；
			 * 3、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填；
			 * 4、当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
			 * 6、当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填
			 */
			if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())){
				isSameAreaRequired = 1;
				if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
					inDepotRequired = 1;
				}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
						DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
					inDepotRequired = 1;
				}
			}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
				isSameAreaDisabled = "true";	// 禁用

			}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())){
				isSameAreaRequired = 0;
				if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
					inDepotRequired = 1;
				}
			}
			/**
			 * 1、当销售类型为购销-实销实结、采购执行时，PO单号非必填；
			 * 2、当销售类型为购销-整批结算时，PO单号必填项
			 */
			if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel()) &&
					DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderDTO.getBusinessModel())){
				isPoRequired = 0;
			}else if(DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
				isPoRequired = 1;
			}
			// 同关区时合同号必填
			if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
				isContractNoRequired = 1;
			}
			//标准箱TEU:当运输方式为海运时必填
			if(DERP.TRANSPORT_2.equals(saleOrderDTO.getTransport())){
				isTeuRequired = 1;
			}
			//车次:当运输方式为陆运、港到仓拖车时必填
			if(DERP.TRANSPORT_3.equals(saleOrderDTO.getTransport()) ||
			DERP.TRANSPORT_4.equals(saleOrderDTO.getTransport())){
				isTrainnoRequired = 1;
			}
			responseDTO.setInDepotRequired(String.valueOf(inDepotRequired));
			responseDTO.setInDepotDisabled(inDepotDisabled);
			responseDTO.setIsSameAreaDisabled(isSameAreaDisabled);
			responseDTO.setIsSameAreaRequired(String.valueOf(isSameAreaRequired));
			responseDTO.setIsPoRequired(String.valueOf(isPoRequired));
			responseDTO.setIsContractNoRequired(String.valueOf(isContractNoRequired));
			responseDTO.setIsTrainnoRequired(String.valueOf(isTrainnoRequired));
			responseDTO.setIsTeuRequired(String.valueOf(isTeuRequired));
			responseDTO.setOperate(operate);
			Boolean salePriceManage = saleOrderService.getSalePriceManage(saleOrderDTO.getBuId(),saleOrderDTO.getCustomerId(),user);
			responseDTO.setSalePriceManage(salePriceManage);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);

	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询销售订单列表信息")
   	@PostMapping(value="/listSaleOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleOrderDTO> listSaleOrder(SaleOrderForm form, HttpSession session) {
		SaleOrderDTO dto = new SaleOrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setPoNo(form.getPoNo());
			dto.setStateList(form.getStateList());
			dto.setBusinessModel(form.getBusinessModel());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setOrderType(form.getOrderType());
			dto.setCreateDateStartDate(form.getCreateDateStartDate());
			dto.setCreateDateEndDate(form.getCreateDateEndDate());
			dto.setAmountStatus(form.getAmountStatus());
			dto.setAmountConfirmStatus(form.getAmountConfirmStatus());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setShelfDateStartDate(form.getShelfDateStartDate());
			dto.setShelfDateEndDate(form.getShelfDateEndDate());
			dto.setCreateName(form.getCreateName());
			dto.setWriteOffStatus(form.getWriteOffStatus());

			// 响应结果集
			dto = saleOrderService.listSaleOrderByPage(dto,user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 购销：校验PO号在中间表是否已存在，若已存在则给个提示
	 * */
	@ApiOperation(value = "校验PO号在销售-po关联表是否已存在")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "poNo", value = "po号", required = true),
		@ApiImplicitParam(name = "orderId", value = "单据id", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部id", required = true)
	})
	@PostMapping(value="/checkExistByPo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<String>> checkExistByPo(String token,String poNo,Long orderId,Long buId) {
		List poList = new ArrayList();
		try{
			User user= ShiroUtils.getUserByToken(token);
			// 响应结果集
			poList = saleOrderService.checkExistByPo(poNo,orderId,user.getMerchantId(),buId);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,poList);
	}
	/**
	 * 新增/修改
	 * */
	@ApiOperation(value = "新增/修改")
	@PostMapping(value="/saveOrModifyTempOrder.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean saveOrModifyTempOrder(@RequestBody SaleOrderAddForm form) {
		try{
            User user= ShiroUtils.getUserByToken(form.getToken());
			SaleOrderDTO dto = new SaleOrderDTO();
			BeanUtils.copyProperties(form,dto);
            boolean b = saleOrderService.saveOrModifyTempOrder(dto,user);
            if(!b){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"保存失败");
            }
        } catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 修改并审核
	 * */
	@ApiOperation(value = "修改并审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "销售订单信息json", required = true),
	})
	@ApiResponses({
		@ApiResponse(code=10000,message = "data => 记录修改并审核成功/失败消息")
	})
	@PostMapping(value="/modifySaleOrder.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<String> modifySaleOrder(@RequestBody  SaleOrderAddForm form) {
		String msg=null;
		try{
			SaleOrderDTO dto = new SaleOrderDTO();
			BeanUtils.copyProperties(form,dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            msg = saleOrderService.modifySaleOrder(dto, user, form.getRejectReason());
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 审核销售类型为采购执行的
	 * */
	@ApiOperation(value = "审核销售类型为采购执行的")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售订单id", required = true),
	})
	@ApiResponses({
		@ApiResponse(code=10000,message = "data => 记录审核成功/失败消息")
	})
	@PostMapping(value="/modifyPurchaseExecute.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> modifyPurchaseExecute(String token,Long id, HttpSession session) {
		String msg=null;
		try{
            User user= ShiroUtils.getUserByToken(token);
            SaleOrderModel model=new SaleOrderModel();
            model.setId(id);
            msg = saleOrderService.auditPurchase(model,user.getId(),user.getName(),user.getTopidealCode());
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true),
	})
	@PostMapping(value="/delSaleOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSaleOrder(String token,String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            User user = ShiroUtils.getUserByToken(token);
            boolean b = saleOrderService.delSaleOrder(list ,user);
            if(!b){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");
            }
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}


	/**
	 * 中转仓出库
	 * */
	@ApiOperation(value = "中转仓出库")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true),
		@ApiImplicitParam(name = "outDepotDateStr", value = "出库日期", required = true)
	})
	@PostMapping(value="/saleOrderStockOut.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean saleOrderStockOut(String token,String ids,String outDepotDateStr, HttpSession session) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
			User user= ShiroUtils.getUserByToken(token);
			// 响应结果集
			boolean bl = saleOrderService.confirmSaleOrderStockOut(list,user.getId(),user.getName(),user.getTopidealCode(),user.getMerchantId(),outDepotDateStr);
			if(!bl){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"中转仓出库失败");
			}
		}catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 完结出库
	 * */
	@ApiOperation(value = "完结出库")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/endStockOut.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean endStockOut(String token,String ids, HttpSession session) {
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            User user= ShiroUtils.getUserByToken(token);
			// 响应结果集
			boolean bl = saleOrderService.confirmEndStockOut(list,user.getId(),user.getName());
			if(!bl){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"完结出库失败");
			}
		}catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 计算销售订单出库的百分比
	 * */
	@ApiOperation(value = "计算销售订单出库的百分比")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/differenceRatio.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleOrderResponseDTO> differenceRatio(String token,String id) {
		SaleOrderResponseDTO responseDTO = new SaleOrderResponseDTO();
		try{
			Map<String,String> map =new HashMap<String,String>();
			//校验id是否正确
            boolean isRight = StrUtils.validateId(Long.parseLong(id));
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(id)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			// 响应结果集
            map = saleOrderService.differenceRatio(Long.parseLong(id));
            responseDTO.setOrderCode(map.get("orderCode"));
            responseDTO.setPercent(map.get("percent"));
		}catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}
	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * */
	@ApiOperation(value = "获取选中订单的所有商品和对应数量")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true),
		@ApiImplicitParam(name = "type", value = "类型 1：审核，2：中转仓出库", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message = "data => code:由 商品id-出库仓id-出库仓编码-商品货号-理货单位 拼接而成 ")
	})
	@PostMapping(value="/getOrderGoodsInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<OrderGoodsInfoNumDTO>> getOrderGoodsInfo(String token,String ids,String type) {
		List<OrderGoodsInfoNumDTO> goodsInfoNumList = new ArrayList<OrderGoodsInfoNumDTO>();
		try{
			Map<String,Integer> map =new HashMap<String,Integer>();
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
			// 响应结果集
            map = saleOrderService.getOrderGoodsInfo(list,type);

			for(Entry<String, Integer> entry : map.entrySet()) {
            	OrderGoodsInfoNumDTO numDTO = new OrderGoodsInfoNumDTO();
            	numDTO.setCode(entry.getKey());
            	numDTO.setNum(entry.getValue());
            	goodsInfoNumList.add(numDTO);
            }

		}catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,goodsInfoNumList);
	}

	/**
	 * 导入上架
	 * */
	@ApiOperation(value = "导入上架")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importSaleShelf.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSaleShelf(String token,
											@ApiParam(value = "上传的文件", required = true)
											@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
		try{
			Map resultMap = new HashMap();//返回的结果集
            if(file==null){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            List<List<Map<String,String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if(data == null){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			User user= ShiroUtils.getUserByToken(token);
			resultMap = saleOrderService.importSaleShelf(data,user);
			List<InvetAddOrSubtractRootJson> subtractRootJsonList = (List<InvetAddOrSubtractRootJson>)resultMap.get("subtractRootJsonList");
			//发送库存入库
			if(subtractRootJsonList!=null&&subtractRootJsonList.size()>0){
				for(InvetAddOrSubtractRootJson rootJson : subtractRootJsonList){
					rocketMQProducer.send(JSONObject.fromObject(rootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				}
			}
			resultMap.remove("subtractRootJsonList");

			//生成to B暂估
			if(resultMap.containsKey("shelfCodeList") && resultMap.get("shelfCodeList") != null) {
				List<String> shelfCodeList = (List<String>)resultMap.get("shelfCodeList");
				Map<String,Object> map = new HashMap<String, Object>();
	    		map.put("orderCodes", StringUtils.join(shelfCodeList, ","));
	    		map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);
				rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
				resultMap.remove("shelfCodeList");
			}

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}

	/**
	 * 商品上架
	 * */
	@ApiOperation(value = "商品导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "outDepotId", value = "出库仓id", required = true),
		@ApiImplicitParam(name = "customerId", value = "客户id", required = true),
		@ApiImplicitParam(name = "currency", value = "币种", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部", required = true),
		@ApiImplicitParam(name = "salePriceManage", value = "是否开启价格管理 true or false", required = true),
		@ApiImplicitParam(name = "unitId", value = "理货单位")
	})
	@PostMapping(value="/importSaleGoods.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSaleGoods(String token,
							@ApiParam(value = "上传的文件", required = true)
							@RequestParam(value = "file", required = true) MultipartFile file,SaleOrderForm form,String salePriceManage,String unitId, HttpSession session) {
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
			SaleOrderDTO dto = new SaleOrderDTO();
			dto.setOutDepotId(Long.valueOf(form.getOutDepotId()));
			dto.setCustomerId(Long.valueOf(form.getCustomerId()));
			dto.setCurrency(form.getCurrency());
			dto.setTallyingUnit(unitId);
			dto.setBuId(Long.valueOf(form.getBuId()));
			resultMap = saleOrderService.importSaleGoods(data,user,dto,salePriceManage);

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

	/**
	 * 导入购销代销销售订单
	 * */
	@ApiOperation(value = "导入购销代销销售订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importSale.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse>  importSale(String token,
									@ApiParam(value = "上传的文件", required = true)
									@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
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
			logger.debug("=========================销售购销导入开始========================");
			User user= ShiroUtils.getUserByToken(token);
			resultMap = saleOrderService.importSaleOrder(data,user);
			logger.debug("=========================销售购销导入结束========================");

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}


	/**
	 * 获取导出销售订单的数量
	 */
	@ApiOperation(value = "获取导出销售订单的数量")
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOrderForm form) throws Exception{
		Integer total = 0;
		try{
			SaleOrderDTO dto = new SaleOrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setPoNo(form.getPoNo());
			dto.setStateList(form.getStateList());
			dto.setBusinessModel(form.getBusinessModel());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setOrderType(form.getOrderType());
			dto.setCreateDateStartDate(form.getCreateDateStartDate());
			dto.setCreateDateEndDate(form.getCreateDateEndDate());
			dto.setAmountStatus(form.getAmountStatus());
			dto.setAmountConfirmStatus(form.getAmountConfirmStatus());
			dto.setIds(form.getIds());
			dto.setShelfDateStartDate(form.getShelfDateStartDate());
			dto.setShelfDateEndDate(form.getShelfDateEndDate());
			dto.setCreateName(form.getCreateName());
			dto.setWriteOffStatus(form.getWriteOffStatus());

			// 响应结果集
			List<SaleOrderDTO> result = saleOrderService.listSaleOrder(dto,user);
			total = result.size();
		}catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	/**
	 * 导出销售订单
	 * */
	@ApiOperation(value = "导出销售订单",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSaleOrder.asyn")
	private void exportSaleOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOrderForm form) throws Exception{
		try {
			SaleOrderDTO dto = new SaleOrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setPoNo(form.getPoNo());
			dto.setStateList(form.getStateList());
			dto.setBusinessModel(form.getBusinessModel());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setOrderType(form.getOrderType());
			dto.setCreateDateStartDate(form.getCreateDateStartDate());
			dto.setCreateDateEndDate(form.getCreateDateEndDate());
			dto.setAmountStatus(form.getAmountStatus());
			dto.setAmountConfirmStatus(form.getAmountConfirmStatus());
			dto.setIds(form.getIds());
			dto.setShelfDateStartDate(form.getShelfDateStartDate());
			dto.setShelfDateEndDate(form.getShelfDateEndDate());
			dto.setCreateName(form.getCreateName());
			dto.setWriteOffStatus(form.getWriteOffStatus());

			List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
			Map<String, BigDecimal> priceMap = new HashMap<String, BigDecimal>();
			// 表头
			List<SaleOrderDTO> result = saleOrderService.listSaleOrder(dto,user);
			String mainSheetName = "销售订单";
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, SALE_COLUMNS, SALE_KEYS, result);
			sheetList.add(mainSheet) ;

			List<Long> orderIds = result.stream().map(SaleOrderDTO :: getId).collect(Collectors.toList());
			Map<Long, SaleOrderDTO> saleOrderMap = result.stream().collect(Collectors.toMap(SaleOrderDTO::getId, s -> s,(k1,k2)->k1));;
			//商品信息
			Map<Long, BrandParentMongo> brandParentMap = new HashMap<Long, BrandParentMongo>();
			SaleOrderItemDTO itemDTO = new SaleOrderItemDTO();
			itemDTO.setOrderIds(orderIds);
			List<SaleOrderItemDTO> itemList = saleOrderService.listSaleOrderItemDTO(itemDTO);
			for(SaleOrderItemDTO item : itemList) {
				BrandParentMongo brandParent = brandParentMap.get(item.getGoodsId());
				if(brandParent == null) {
					brandParent = brandParentMongoDao.getBrandParentMongo(item.getGoodsId());
				}
				if(brandParent != null) {
					item.setCommBrandParentName(brandParent.getName());
					brandParentMap.put(item.getGoodsId(), brandParent);
				}
				SaleOrderDTO saleOrder = saleOrderMap.get(item.getOrderId());
				item.setOrderCode(saleOrder.getCode());
				item.setPoNo(saleOrder.getPoNo());

				String key = item.getOrderId()+"_"+item.getGoodsId();
				priceMap.put(key, item.getPrice());
			}
			//上架单信息
			SaleShelfDTO saleShelfDTO = new SaleShelfDTO();
			saleShelfDTO.setOrderIds(orderIds);
			List<SaleShelfDTO> shelfList = saleShelfService.listDTO(saleShelfDTO);
			for(SaleShelfDTO saleShelf : shelfList) {
				BrandParentMongo brandParent = brandParentMap.get(saleShelf.getGoodsId());
				if(brandParent != null) {
					saleShelf.setCommBrandParentName(brandParent.getName());
				}
				SaleOrderDTO saleOrder = saleOrderMap.get(saleShelf.getOrderId());
				saleShelf.setCode(saleOrder.getCode());
				saleShelf.setCurrency(DERP.getLabelByKey(DERP.currencyCodeList, saleOrder.getCurrency()));
				saleShelf.setPrice(priceMap.get(saleShelf.getOrderId()+"_"+saleShelf.getGoodsId()));
			}

			String itemSheetName = "商品信息";
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, ITEM_COLUMNS, ITEM_KEYS, itemList);
			sheetList.add(itemSheet) ;

			String shelfSheetName = "上架信息";
			ExportExcelSheet shelfSheet = ExcelUtilXlsx.createSheet(shelfSheetName, SHELF_COLUMNS, SHELF_KEYS, shelfList);
			sheetList.add(shelfSheet) ;

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel8(sheetList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 校验订单能否生成采购订单（已审核、待审核的销售订单）
	 * */
	@ApiOperation(value = "校验订单能否生成采购订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/checkOrderState.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleOrderDTO> checkOrderState(String token,Long id) {
		SaleOrderDTO saleOrderDTO = null;
		try{
			// 响应结果集
			saleOrderDTO = saleOrderService.checkOrderState(id);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleOrderDTO);
	}
	/**
	 * 访问上架页面
	 * */
	@ApiOperation(value = "上架页面数据回显")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售订单id", required = true),
		@ApiImplicitParam(name = "saleOutCode", value = "销售出库编号", required = true)
	})
	@PostMapping(value="/toSaleShelfPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOrderResponseDTO> toSaleShelfPage(String token,Long id, String saleOutCode)throws Exception{
		SaleOrderResponseDTO  responseDTO = new SaleOrderResponseDTO();
		try {
			Boolean isEmpty = new EmptyCheckUtils().addObject(id).addObject(saleOutCode).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);

			SaleOrderDTO saleOrderDTO = saleOrderService.searchDetail(id);
			Map<String,Object> resultMap = saleOrderService.listShelfListByOrderId(id,saleOutCode);
			List<SaleShelfModel> shelfList = (List<SaleShelfModel>) resultMap.get("shelfList");
			String saleDeclareOrderCode = (String) resultMap.get("saleDeclareOrderCode");

			// 根据销售订单ID在销售单_po关联表中查询它的PO单号
			List<SalePoRelModel> saleOrderByPoList = salePoRelService.getAllByOrderId(saleOrderDTO.getId(),user.getMerchantId());
			List<String>poList=new ArrayList<>();	// 存放该销售订单的所有PO单号
			for (int i = 0; i < saleOrderByPoList.size(); i++) {
				poList.add(saleOrderByPoList.get(i).getPoNo());
			}
			saleOrderDTO.setSaleOutDepotCode(saleOutCode);
			saleOrderDTO.setSaleDeclareCodes(saleDeclareOrderCode);

			responseDTO.setSaleOrderDTO(saleOrderDTO);
			responseDTO.setPoList(poList);
			responseDTO.setShelfList(shelfList);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);

	}
	/**
	 * 保存上架商品信息
	 * @param json
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "保存上架商品信息")
	@ApiResponses({
		@ApiResponse(code=10000,message = "id:销售id，state：单据状态")
	})
	@PostMapping(value="/saveSaleShelf.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<Map<String,Object>> saveSaleShelf(@RequestBody ShelfForm form) {
		Map<String,Object> result = new HashMap<String, Object>();
		try{

            User user= ShiroUtils.getUserByToken(form.getToken());
            ShelfDTO dto = new ShelfDTO();
            BeanUtils.copyProperties(form, dto);
            if(form.getShelfDate() != null){
				dto.setShelfDate(TimeUtils.parseDay(form.getShelfDate()));
			}else{
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017,"上架时间不能为空");
			}
            result = saleOrderService.saveSaleShelf(dto,user);
            //生成to B暂估
            String shelfCode = (String)result.get("shelfCode");
    		if(StringUtils.isNotBlank(shelfCode)) {
    			Map<String,Object> map = new HashMap<String, Object>();
    			map.put("orderCodes", shelfCode);
    			map.put("dataSource", DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1);
    			rocketMQProducer.send(JSONObject.fromObject(map).toString(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTopic(), MQOrderEnum.TOB_RECEIVE_BILL_GENERATE.getTags());
    			result.remove("shelfCode");
    		}

        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}

	/**
	 * 获取上架明细分页数据
	 * */
	@ApiOperation(value = "查询详情页面上架明细列表信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "orderId", value = "销售订单id", required = true),
		@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
		@ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true)
	})
	@PostMapping(value="/listSaleShelf.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleShelfModel> listSaleShelf(String orderId,Integer begin,Integer pageSize) {
		SaleShelfModel model = new SaleShelfModel();
		try{
			if(StringUtils.isBlank(orderId)){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			model.setOrderId(StringUtils.isNotBlank(orderId) ? Long.valueOf(orderId) : null);
			model.setBegin(begin);
			model.setPageSize(pageSize);
			// 响应结果集
			model = saleOrderService.listSaleShelfByPage(model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}

	/**
	 * 判断是否已经完全上架
	 * */
	@ApiOperation(value = "判断是否已经完全上架")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/shelfIsEnd.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Boolean> shelfIsEnd(String token,Long id) {
		boolean flag = false;
		try{
			// 响应结果集
			flag = saleOrderService.shelfIsEnd(id);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);
	}

	/**
	 * 访问编辑页面-----------预售转销
	 * @param model
	 * @param data
	 * @param session
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "查询预售转销详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "data", value = "json，封装了预售转销售的信息", required = true)
	})
	@PostMapping(value="/preSaleEditPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOrderResponseDTO> preSaleEditPage(String token,String data,HttpSession session,HttpServletRequest request) throws Exception {
		SaleOrderResponseDTO responseDTO = new SaleOrderResponseDTO();
		try {
			JSONObject jsonObj = JSONObject.fromObject(data);
			String type = String.valueOf(jsonObj.get("type"));
			String operate = String.valueOf(jsonObj.get("operate"));
			SaleOrderDTO saleOrderDTO =new SaleOrderDTO();
			if(type.equals("1")){// 生成预售转销
				saleOrderDTO = saleOrderService.preSaleOrderToSaleEdit(data);
			}else if(type.equals("2")){// 销售订单编辑预售转销的订单
				Long saleOrderId = Long.valueOf(jsonObj.getString("saleOrderId"));
				saleOrderDTO = saleOrderService.searchDetail(saleOrderId);
				List<SaleOrderItemDTO> itemList = saleOrderService.listItemByOrderId(saleOrderId);
				saleOrderDTO.setItemList(itemList);
			}

			DepotInfoMongo outDepot = depotInfoService.getDetails(saleOrderDTO.getOutDepotId());

			String isSameAreaDisabled = "false";	// 是否同关区是否禁用
			int isSameAreaRequired = 0;	// 是否同关区是否必填
			String inDepotDisabled = "false";// 入库仓库是否禁用
			int inDepotRequired = 0;// 入库仓库是否必填
			int isContractNoRequired = 0;	// 合同号是否必填
			int isHKOutDepot = 0;	// 出仓仓库为香港仓，收件信息必填

			/**
			 * 1、当出仓仓库为保税仓时，是否同关区必填；
			 * 2、当出仓仓库为海外仓时，是否同关区禁用；
			 * 3、当出仓仓库为中转仓时，是否同关区非必填；
			 */
			/**
			 * 1、当出仓仓库为保税仓，且是否同关区为“是”时，入库仓库必填；
			 * 2、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-整批结算时，入库仓库禁用；
			 * 3、当出仓仓库为保税仓，且是否同关区为“否” 且销售类型为购销-实销实结时，入库仓库必填；
			 * 4、当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
			 * 5、当出仓仓库为海外仓且销售类型为购销-整批结算时，入库仓库禁用，不予选择；
			 * 6、当出仓仓库为中转仓，且销售类型为购销-实销实结时，入库仓库必填
					*/
			if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType())){
				isSameAreaRequired = 1;
				if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
					inDepotRequired = 1;
				}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
						DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
					inDepotDisabled = "true";
				}else if(DERP.ISSAMEAREA_0.equals(saleOrderDTO.getIsSameArea()) &&
						DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
					inDepotRequired = 1;
				}
			}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
				isSameAreaDisabled = "true";	// 禁用
				isHKOutDepot=1;//1:必填
				// 当出仓仓库为海外仓且销售类型为购销-实销实结时，入库仓库必填，可选仓库仅为备查库；
				if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
					inDepotDisabled = "false";
				}else if(DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderDTO.getBusinessModel())){
					// 当出仓仓库为海外仓且销售类型为购销-整批结算时，入库仓库禁用，不予选择；
					inDepotDisabled = "true";
				}
			}else if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_D.equals(outDepot.getType())){
				isSameAreaRequired = 0;
				if(DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderDTO.getBusinessModel())){
					inDepotRequired = 1;
				}
			}
			// 同关区时合同号必填
			if(DERP.ISSAMEAREA_1.equals(saleOrderDTO.getIsSameArea())){
				isContractNoRequired = 1;
			}

			responseDTO.setItemCount(String.valueOf(saleOrderDTO.getItemList().size()));
			responseDTO.setSaleOrderDTO(saleOrderDTO);
			responseDTO.setCustomsNo(outDepot.getCustomsNo());
			responseDTO.setInDepotRequired(String.valueOf(inDepotRequired));
			responseDTO.setInDepotDisabled(inDepotDisabled);
			responseDTO.setIsSameAreaDisabled(isSameAreaDisabled);
			responseDTO.setIsSameAreaRequired(String.valueOf(isSameAreaRequired));
			responseDTO.setIsContractNoRequired(String.valueOf(isContractNoRequired));
			responseDTO.setOutDepotType(outDepot.getType());
			responseDTO.setIsHKOutDepot(String.valueOf(isHKOutDepot));
			responseDTO.setOperate(operate);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}

	@ApiOperation(value = "查询可生成销售订单的采购订单列表信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装了预售转销售的信息", required = true)
	})
	@PostMapping(value="/showOwnPurchaseOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<PurchaseOrderDTO>> showOwnPurchaseOrder(String token,String json,HttpSession session) {
		List<PurchaseOrderDTO> purchaseOrderList = null;
		try{
			User user= ShiroUtils.getUserByToken(token);
			// 响应结果集
			purchaseOrderList = purchaseOrderService.getOwnPurchaseOrder(json,user.getMerchantId(),user.getTopidealCode());
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,purchaseOrderList);
	}

	/**
	 * 商品导出
	 * @param response
	 * @param request
	 * @param id
	 * @throws Exception
	 */
	@ApiOperation(value = "商品导出",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/exportItems.asyn")
	public void export(HttpServletResponse response, HttpServletRequest request,String json) throws Exception{
		try {
			/** 标题  */
			String[] COLUMNS= {"序号","商品货号","条形码","销售数量","销售总金额"};
			String[] KEYS= {"seq","goodsNo","barcode","num","amount"};

			String sheetName = "商品导出";
			List<SaleOrderItemDTO> itemList= new ArrayList<SaleOrderItemDTO>();
			if(json != null || StringUtils.isNotBlank(json)){			//输入信息不完整
				itemList = saleOrderService.exportListItem(json);
			}
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS , itemList);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 访问出库打托页面
	 * */
	@ApiOperation(value = "查询出库打托详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售单据id", required = true)
	})
	@PostMapping(value="/toSaleOutPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOrderDTO> toSaleOutPage(String token,Long id, HttpSession session)throws Exception{
		SaleOrderDTO  saleOrderDTO = new SaleOrderDTO();
		try {
			saleOrderDTO = saleOrderService.listSaleOutBySaleId(id);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleOrderDTO);

	}

	/**
	 * 保存出库打托信息
	 * @param json
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "保存出库打托信息")
	@PostMapping(value="/saveSaleOut.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean saveSaleOut(@RequestBody SaveSaleOutDepotForm form) {
		try{
			SaleOutDepotDTO dto = new SaleOutDepotDTO();
			BeanUtils.copyProperties(form,dto);
			dto.setSaleOrderId(form.getOrderId());
			if(StringUtils.isNotBlank(form.getReturnOutDate())) {
				dto.setDeliverDate(TimeUtils.parseDay(form.getReturnOutDate()));
			}
			User user= ShiroUtils.getUserByToken(form.getToken());
			boolean b = saleOrderService.saveSaleOrderOut(dto,user);
			if(!b){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"打托出库失败");//未知异常
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 显示金额调整信息
	 * @param model
	 * @param id
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "显示商品信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售单据id", required = true)
	})
	@PostMapping(value="/showOrderAmount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleOrderDTO> showOrderAmount(String token,Long id, HttpSession session) {
		SaleOrderDTO saleOrderDTO = null;
		try {
			saleOrderDTO = saleOrderService.searchDetail(id);
			List<SaleOrderItemDTO> itemList = saleOrderService.listItemByOrderId(id);
			saleOrderDTO.setItemList(itemList);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleOrderDTO);
	}


	/**
	 * 金额调整
	 * @param model
	 * @param id
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "金额调整")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装了销售订单商品信息", required = true)
	})
	@PostMapping(value="/amountAdjust.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean amountAdjust(String token,String json,HttpSession session) {
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			boolean b = saleOrderService.amountAdjust(json,user);
			if(!b){
				return  WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"金额调整失败");//未知异常
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	   * 判断订单商品上架月份是否已关账
	 * */
	@ApiOperation(value = "判断订单商品上架月份是否已关账")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售单据id", required = true)
	})
	@PostMapping(value="/checkExistFinanceClose.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> checkExistFinanceClose(String token,Long id) {
		ResultDTO resultdto = new ResultDTO();
		try{
			Map<String, String> result = new HashMap<>();
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			result = saleOrderService.checkExistFinanceClose(user,id);
			resultdto.setCode(result.get("code"));
			resultdto.setMessage(result.get("message"));
			if("01".equals(result.get("code"))) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),result.get("message"));
			}
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultdto);
	}

	/**
	 * 提交并审核 判断是否存在采购单
	 * */
	@ApiOperation(value = "判断是否存在采购单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "poNo", value = "po号", required = true),
		@ApiImplicitParam(name = "merchantId", value = "公司id", required = true),
		@ApiImplicitParam(name = "customerId", value = "客户id", required = true)
	})
	@ApiResponses({
		@ApiResponse(code = 10000,message = "code => 返回是否存在采购单标识，00：不存在，01：存在")
	})
	@PostMapping(value="/checkExistPurchase.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<ResultDTO> checkExistPurchase(SaleOrderForm form) {
		ResultDTO resultdto = new ResultDTO();
		try{
			if(StringUtils.isBlank(form.getPoNo()) || StringUtils.isBlank(form.getMerchantId()) || StringUtils.isBlank(form.getCustomerId())){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			Map<String, String> result = new HashMap<>();
			User user = ShiroUtils.getUserByToken(form.getToken());
			SaleOrderDTO dto = new SaleOrderDTO();
			dto.setPoNo(form.getPoNo());
			dto.setMerchantId(Long.valueOf(form.getMerchantId()));
			dto.setCustomerId(Long.valueOf(form.getCustomerId()));

			// 响应结果集
			result = saleOrderService.checkExistPurchase(dto,user);
			resultdto.setCode(result.get("code"));
			resultdto.setData(result.get("innerMerchantName"));
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultdto);
	}

	/**
	 * 新增/编辑页面 提交并审核生成采购订单
	 * @param json
	 * @param buId
	 * @param depotId
	 * @return
	 */
	@ApiOperation(value = "提交并审核生成采购订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "saleId", value = "销售订单id", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部id", required = true),
		@ApiImplicitParam(name = "depotId", value = "出库仓id", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message="data => 返回生成采购单 成功/失败 信息")
	})
	@PostMapping(value="/createPurchaseOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> createPurchaseOrder(String token,Long saleId,Long buId,Long depotId) {
		 String msg = null;
		try{
            User user= ShiroUtils.getUserByToken(token);
            msg = saleOrderService.createPurchaseOrder(saleId,buId,depotId,user);
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 根据PO号判断是否存在采购单
	 * */
	@ApiOperation(value = "根据PO号判断是否存在采购单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "poNo", value = "po号", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message="code => 返回是否存在采购单标识，00：不存在，01：存在")
	})
	@PostMapping(value="/checkExistPurchaseByPO.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> checkExistPurchaseByPO(String token,String poNo) {
		String code = "";
		try{
			Map<String, String> result = new HashMap<>();
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			result = saleOrderService.checkExistPurchaseByPO(poNo,user);
			code = result.get("code");
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,code);
	}

	/**
	 * 列表页面 生成采购订单
	 * @param json
	 * @param buId
	 * @param depotId
	 * @return
	 */
	@ApiOperation(value = "列表页面 生成采购订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装了销售订单信息", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message="data => 生成的采购单id，以便跳转到编辑页面")
	})
	@PostMapping(value="/GeneratePurchaseOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Long> GeneratePurchaseOrder(String token,String json) {
		Long id = null;
		try{
            if(json == null || StringUtils.isBlank(json)){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            id = saleOrderService.GeneratePurchaseOrder(json,user);
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,id);
	}

	/**
	 * 金额确认
	 * @param model
	 * @param id
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "金额确认")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装了销售订单信息", required = true)
	})
	@PostMapping(value="/amountConfirm.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean amountConfirm(String token,String json) {
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			boolean b = saleOrderService.amountConfirm(json,user);
			if(!b){
				return  WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"金额确认失败");//未知异常
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 导出清关资料
	 * @param session
	 * @param response
	 * @param request
	 * @param id
	 * @throws Exception
	 */
//	@ApiOperation(value = "导出清关资料",produces= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "token", value = "令牌", required = true),
//		@ApiImplicitParam(name = "json", value = "json串，保存选中的模板ids", required = true)
//	})
//	@PostMapping(value="/exportCustomsInfo.asyn")
//	public void exportCustomsDeclare(HttpSession session, HttpServletResponse response, HttpServletRequest request,String json) throws Exception {
//		try {
//			JSONObject jsonData = JSONObject.fromObject(json);
//			Long id = Long.valueOf(jsonData.getString("id"));
//			String inFileTempIds = (String) jsonData.get("inFileTempIds");
//			String outFileTempIds = (String) jsonData.get("outFileTempIds");
//
//			if (id == null) {
//				throw new RuntimeException("id不能为空！");
//			}
//			Map<String, Workbook> resultMap = new HashMap<>();
//			Map<String, XWPFDocument> wordMap = new HashMap<>();
//			if (!StringUtils.isEmpty(inFileTempIds)) {
//				List<Long> inFileTempIdList = new ArrayList<>();
//				String[] list = inFileTempIds.split(",");
//				for (String idStr : list) {
//					inFileTempIdList.add(Long.valueOf(idStr));
//				}
//				Map<String, FirstCustomsInfoDTO> resultInMap = saleOrderService.exportDepotCustomsDeclares(id, inFileTempIdList, "2");
//
//				Map<String, Workbook> multipartInCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultInMap, "2");
//				resultMap.putAll(multipartInCustomsDeclares);
//				Map<String, XWPFDocument> multipartOutCustomsDeclaresWord = DownloadExcelUtil.createMultipartCustomsDeclaresWord(resultInMap, "2");
//				wordMap.putAll(multipartOutCustomsDeclaresWord);
//
//			}
//
//			if (!StringUtils.isEmpty(outFileTempIds)) {
//				List<Long> outFileTempIdList = new ArrayList<>();
//				String[] list = outFileTempIds.split(",");
//				for (String idStr : list) {
//					outFileTempIdList.add(Long.valueOf(idStr));
//				}
//				Map<String, Object> resultOutMap = saleOrderService.exportDepotCustomsDeclares(id, outFileTempIdList, "1");
//
//				Map<String, Workbook> multipartOutCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultOutMap, "1");
//				resultMap.putAll(multipartOutCustomsDeclares);
//				Map<String, XWPFDocument> multipartOutCustomsDeclaresWord = DownloadExcelUtil.createMultipartCustomsDeclaresWord(resultOutMap, "1");
//				wordMap.putAll(multipartOutCustomsDeclaresWord);
//			}
//
//			//导出压缩文件
//			String downloadFilename = URLEncoder.encode("销售订单一线清关资料.zip", "UTF-8");
//			// 指明response的返回对象是文件流
//			response.setContentType("application/octet-stream");
//			// 设置在下载框默认显示的文件名
//			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
//			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
//
//			for(String key : wordMap.keySet()) {
//				XWPFDocument document = wordMap.get(key);
//				zos.putNextEntry(new ZipEntry(key));
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				document.write(bos);
//				bos.writeTo(zos);
//				zos.closeEntry();
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
//			zos.flush();
//			zos.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	/**
	 * 提交
	 * @param json
	 * @param session
	 * @return
	 */
	@ApiOperation(value = "提交")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "封装了销售订单信息", required = true)
	})
	@PostMapping(value="/submitSaleOrder.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean submitSaleOrder(@RequestBody SaleOrderAddForm form) {
		try{
			SaleOrderDTO dto = new SaleOrderDTO();
			BeanUtils.copyProperties(form, dto);
            User user= ShiroUtils.getUserByToken(form.getToken());
            Long id = saleOrderService.updateSaleOrder(dto, user);

        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 是否启用销售价格管理
	 * @param buId
	 * @return
	 */
	@ApiOperation(value = "是否启用销售价格管理")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部id", required = true),
		@ApiImplicitParam(name = "customerId", value = "客户id", required = true)
	})
	@PostMapping(value="/getSalePriceManage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean getSalePriceManage(String token,Long buId,Long customerId) {

		if(buId == null) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {
			User user = ShiroUtils.getUserByToken(token);
			boolean flag = saleOrderService.getSalePriceManage(buId,customerId,user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e) ;
		}

	}
	/**
	 * 获取销售价格管理的价格
	 * @param buId
	 * @return
	 */
	@ApiOperation(value = "获取销售价格管理的单价")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "json串", required = true)
	})
	@PostMapping(value="/getSalePrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<String>> getSalePrice(String token,String json) {
		try {
			if(StringUtils.isBlank(json)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			List<String> result = saleOrderService.getSalePrice(json,user.getMerchantId()) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e) ;
		}

	}
	/**
	 * 查询融资申请详情
	 * 查询宝丰公司（ERP19095700016）是否存在对应PO号的采购单
	 * @param id
	 * @return
	 */

	@ApiOperation(value = "赊销申请弹窗回显")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "orderId", value = "销售订单id", required = true)
	})
	@PostMapping(value="/getFinanceDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleFinancingOrderDTO> getFinanceDetail(String token, Long orderId) {
		try {
			if(orderId == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			SaleFinancingOrderDTO result = saleOrderService.getFinanceDetail(orderId,user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e) ;
		}

	}

	@ApiOperation(value = "判断是否可生成预申报单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据id集合，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/checkCreateDeclare.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Boolean> checkCreateDeclare(String token,String ids) {
		boolean flag = false;
		try{
			// 响应结果集
			flag = saleOrderService.checkCreateDeclare(ids);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,flag);
	}
	@ApiOperation(value = "是否开启采购价格管理")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true),
			@ApiImplicitParam(name = "supplierId", value = "供应商id", required = true)
	})
	@PostMapping(value="/getPurchasePriceManage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Boolean> getPurchasePriceManage(String token,Long id, Long supplierId ) {
		try{
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			Boolean flag = saleOrderService.getPurchasePriceManage(id,supplierId,user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

	}
	@ApiOperation(value = "销售转采购获取采购价格")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true),
			@ApiImplicitParam(name = "supplierId", value = "供应商id", required = true)
	})
	@PostMapping(value="/getPurchasePrice.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Map<String, List<BigDecimal>>> getPurchasePrice(String token,Long id, Long supplierId ) {
		Map<String, List<BigDecimal>> result = new HashMap<String, List<BigDecimal>>();
		try{
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			result = saleOrderService.getPurchasePrice(id,supplierId,user);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result);
	}

	@ApiOperation(value = "修改PO号")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "orderId", value = "单据id", required = true),
			@ApiImplicitParam(name = "poNo", value = "po号", required = true),
			@ApiImplicitParam(name = "remark", value = "原因", required = true)
	})
	@PostMapping(value="/modifyPoNo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyPoNo(String token,Long orderId,String poNo, String remark) {
		try{
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			saleOrderService.modifyPoNo(orderId,user,poNo ,remark);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	@ApiOperation(value = "反审")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "orderId", value = "销售订单id", required = true),
			@ApiImplicitParam(name = "remark", value = "反审原因", required = true)
	})
	@PostMapping(value = "/reverseAudit.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean reverseAudit(String token,Long orderId, String remark) {
		try {
			boolean isNull = new EmptyCheckUtils().addObject(orderId).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			saleOrderService.reverseAudit(orderId,remark, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}

	@ApiOperation(value = "中转仓获取商品明细")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value = "/getStockOutItemList.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SaleOrderItemModel>> getStockOutItemList(String token,String ids ) {
		List<SaleOrderItemModel> resultList = new ArrayList<SaleOrderItemModel>();
		try {
			boolean isNull = new EmptyCheckUtils().addObject(ids).empty();
			if (isNull) {
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);

			List<Long> idList = StrUtils.parseIds(ids);
			// 响应结果集
			resultList = saleOrderService.getStockOutItemList(idList, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000 ,resultList);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
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
			saleOrderService.batchRejection(ids, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);//成功
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);//未知异常
		}
	}
	@ApiOperation(value = "获取列表各状态数量", notes="返回类型-数量集合")
	@PostMapping(value="/getStatusNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<Map<String, Object>>> getStatusNum(SaleOrderForm form){
		try {
			SaleOrderDTO dto = new SaleOrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setPoNo(form.getPoNo());
			dto.setStateList(form.getStateList());
			dto.setBusinessModel(form.getBusinessModel());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setOrderType(form.getOrderType());
			dto.setCreateDateStartDate(form.getCreateDateStartDate());
			dto.setCreateDateEndDate(form.getCreateDateEndDate());
			dto.setAmountStatus(form.getAmountStatus());
			dto.setAmountConfirmStatus(form.getAmountConfirmStatus());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			dto.setShelfDateStartDate(form.getShelfDateStartDate());
			dto.setShelfDateEndDate(form.getShelfDateEndDate());
			dto.setCreateName(form.getCreateName());
			dto.setWriteOffStatus(form.getWriteOffStatus());

			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<Map<String, Object>> resultMap = saleOrderService.getStatusNum(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
	@ApiOperation(value = "申请判断是否可以红冲")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/validateApplyWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Boolean> validateApplyWriteOff(Long id ,String token){
		try {
			if(id == null){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, "id不能为空");
			}
			// 响应结果集
			boolean flag = saleOrderService.validateApplyWriteOff(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
	@ApiOperation(value = "申请红冲")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true),
			@ApiImplicitParam(name = "reason", value = "申请原因", required = true)
	})
	@PostMapping(value="/saveWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveWriteOff(Long id ,String token, String reason){
		try {
			if(id == null){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, "id不能为空");
			}
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			saleOrderService.saveWriteOff(id, user, reason);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
	@ApiOperation(value = "点击审核红冲带出申请原因")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/showAuditWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> showAuditWriteOff(Long id ,String token){
		try {
			if(id == null){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, "id不能为空");
			}
			// 响应结果集
			String reason = saleOrderService.showAuditWriteOff(id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, reason);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
	@ApiOperation(value = "审核校验" , notes="回传参数 isRelSaleReturn->是否有销售退订单，shelfIdepotItemList -> 校验可用量商品集合 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true),
			@ApiImplicitParam(name = "writeOffDate", value = "归属日期", required = true),
			@ApiImplicitParam(name = "auditResult", value = "审核结果, 0-驳回，1-通过", required = true)
	})
	@PostMapping(value="/validateAuditWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String,Object>> validateAuditWriteOff(Long id ,String token, String writeOffDate, String auditResult){
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id)
					.addObject(writeOffDate).addObject(auditResult).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			Map<String,Object> resultMap = saleOrderService.validateAuditWriteOff(id,  writeOffDate,auditResult, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
	@ApiOperation(value = "审核红冲")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "选中的单据id", required = true),
			@ApiImplicitParam(name = "writeOffDate", value = "归属日期", required = true),
			@ApiImplicitParam(name = "auditResult", value = "审核结果, 0-驳回，1-通过", required = true)
	})
	@PostMapping(value="/auditWriteOff.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditWriteOff(Long id ,String token, String writeOffDate, String auditResult){
		try {
			boolean isNull = new EmptyCheckUtils().addObject(id)
					.addObject(writeOffDate).addObject(auditResult).empty();
			if (isNull) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			// 响应结果集
			saleOrderService.auditWriteOff(id,  writeOffDate,auditResult, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
}

package com.topideal.order.webapi.purchase;

import com.alibaba.fastjson.JSONObject;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.*;
import com.topideal.common.tools.excel.ExcelExportXlsx;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.common.CustomsPackingDetailsDTO;
import com.topideal.entity.dto.purchase.DeclareOrderDTO;
import com.topideal.entity.dto.purchase.DeclareOrderItemDTO;
import com.topideal.entity.dto.purchase.FirstCustomsInfoDTO;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.purchase.DeclareOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.webapi.purchase.form.DeclareDetailsAddForm;
import com.topideal.order.webapi.purchase.form.DeclareOrderAddForm;
import com.topideal.order.webapi.purchase.form.DeclareOrderDeliveryForm;
import com.topideal.order.webapi.purchase.form.DeclareOrderForm;
import com.topideal.order.webapi.sale.form.LogisticsAttorneyForm;
import com.topideal.order.webapi.sale.form.MerchandiseForm;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 预申报单 控制层
 */
@RestController

@RequestMapping("/webapi/order/declare")
@Api(tags = "declare-预申报单列表")
public class APIDeclareOrderController {

	private static final Logger LOG = Logger.getLogger(APIDeclareOrderController.class);

	private static final String[] COLUMNS = { "公司","预申报单号", "采购订单号", "供应商", "事业部","订单创建时间","PO号",  "入仓仓库", "订单状态", "贸易条款", "lbx单号",
			"海外仓理货单位", "创建人", "创建时间", "装货港", "目的地名称", "目的港名称", "卸货港", "收货地点",
			"进出口口岸", "入库关区", "装船时间", "国际运输方式", "陆运方式", "境外发货人", "承运商", "发票号", "报关合同号",
			"头程提单号", "二程提单号", "托数", "托盘材质", "箱数", "包装方式", "提单毛重", "车型及数量", "标准箱TEU",
			"唛头", "车次", "已接单", "订舱", "确认订舱信息", "预计离港时间", "预计到港时间", "工厂提货（装船）",
			"提交一线清关资料","审核一线清关资料","推送接口","完成申报","陆运订车",
			"确认订车信息","入仓时间","已上架"};

	private static final String[] KEYS = { "merchantName","code", "purchaseCode","supplierName", "buName","purchaseCreateDate","poNo","depotName", "statusLabel", "tradeTermsLabel", "lbxNo",
			"tallyingUnitLabel", "createName", "createDate", "portLoading","destinationName", "destinationPortName", "portDestNoLabel", "deliveryAddr",
			"imExpPort", "inPlatformCustoms", "plannedShipDate","transportLabel", "landTransportLabel", "shipper", "carrier", "invoiceNo", "contractNo",
			"ladingBill", "lbxNo", "torrNum", "palletMaterialLabel","boxNum","packType","billWeight","motorcycleType","standardCaseTeu",
			"mark", "trainNumber","createDate" ,"logisticsCommissionDate", "confirmBookingDate", "estimatedDeliveryDate", "arriveDate", "shipDate",
			"customsSubmitDate","customsConfirmDate", "pushWarehouseDate", "confirmDeclarationDate", "landCommissionDate",
			"confirmCatDate", "confirmDepotDate", "shelfDate" };

	private static final String[] ITEMS_COLUMNS = { "预申报单号", "采购订单号", "子合同号","商品编码","商品货号","商品名称","采购单价","申报数量","申报单价","申报总金额","采购单位","毛重","净重",
			"品牌名称","品牌类型","箱号","批次号","成分占比"};

	private static final String[] ITEMS_KEYS = {"declareCode", "purchase","contractNo","goodsCode","goodsNo","goodsName","purchasePrice","num","price","amount","purchaseUnit","netWeight","grossWeight",
			"brand","brandName","boxNo","batchNo","constituentRatio"};


	private static final String[] EXPORT_ITEM_COLUMNS = { "采购订单号",  "PO号","商品货号", "条形码", "托盘号", "箱数", "申报数量", "采购单价", "申报单价", "采购总金额", "品牌类型",
			"毛重（KG）", "净重（KG）" };

	private static final String[] EXPORT_ITEM_KEYS = { "purchase","poNo", "goodsNo", "barcode", "palletNo", "cartons", "num", "purchasePrice", "price", "amount",
			"brandName", "grossWeightSum", "netWeightSum" };

	// 预申报单service
	@Autowired
	private DeclareOrderService declareOrderService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;

	/**
	 * 根据ID查找详情
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "根据ID查找详情")
	@PostMapping(value ="/getDeclareDetails.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true) })
	public ResponseBean<DeclareOrderDTO> getDeclareDetails(@RequestParam(value = "token", required = true) String token,
														   @RequestParam(value = "id", required = true) Long id) {

		if (StringUtils.isBlank(token)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {
			DeclareOrderDTO dto = declareOrderService.searchDTODetail(id);

			List<DeclareOrderItemModel> itemList = declareOrderService.getItem(id,"1");
			dto.setItemList(itemList);

			List<PackingDetailsModel> packingList = declareOrderService.getPackingDetail(id);
			dto.setPackingList(packingList);

			DepotInfoMongo depotInfo = depotInfoService.getDetails(dto.getDepotId());
			dto.setDepotType(depotInfo.getType());

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}


	/**
	 * 复制前校验
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "复制前校验")
	@PostMapping(value ="/getDeclareOrderCopyCheck.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true) })
	public ResponseBean getDeclareOrderCopyCheck(@RequestParam(value = "token", required = true) String token,
														 @RequestParam(value = "id", required = true) Long id) {

		if (StringUtils.isBlank(token)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {
			User user=ShiroUtils.getUserByToken(token);
			//复制前校验
			declareOrderService.getDeclareDetailCheckList(user,id);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		}catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}


	/**
	 * 复制
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "复制")
	@PostMapping(value ="/getDeclareOrderCopyById.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true) })
	public ResponseBean<Boolean> getDeclareOrderCopyById(@RequestParam(value = "token", required = true) String token,
														 @RequestParam(value = "id", required = true) Long id) {

		if (StringUtils.isBlank(token)) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {
			User user=ShiroUtils.getUserByToken(token);

			DeclareOrderDTO dto = declareOrderService.declareOrderByCopyId(id);

			List<DeclareOrderItemModel> itemList = declareOrderService.getItem(id,"2");
			dto.setItemList(itemList);

			List<PackingDetailsModel> packingList = declareOrderService.getPackingDetail(id);
			dto.setPackingList(packingList);

			DepotInfoMongo depotInfo = depotInfoService.getDetails(dto.getDepotId());
			dto.setDepotType(depotInfo.getType());

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		}catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}



	/**
	 * 获取分页数据
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取分页数据")
	@PostMapping(value="/listDeclare.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<DeclareOrderDTO> listDeclare(DeclareOrderForm form) {

		if (StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {

			DeclareOrderDTO dto = new DeclareOrderDTO();

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = declareOrderService.listDeclare(dto,user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 获取分页数据
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "获取预申报单各类型数量", notes="返回类型-数量集合")
	@PostMapping(value="/getDeclareTypeNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> getDeclareTypeNum(DeclareOrderForm form) {

		if (StringUtils.isBlank(form.getToken())) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {

			DeclareOrderDTO dto = new DeclareOrderDTO();

			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<Map<String, Object>> resultMap = declareOrderService.getDeclareTypeNum(dto,user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 修改
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "预申报单修改")
	@PostMapping(value="/modifyDeclare.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean<String> modifyDeclare(@RequestBody DeclareOrderAddForm form) {

		try {

			if (StringUtils.isBlank(form.getToken())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			Boolean isEmpty = new EmptyCheckUtils().addObject(form.getPortLoading())
					.addObject(form.getDeliveryAddr()).addObject(form.getShipper())
					.addObject(form.getContractNo()).addObject(form.getInvoiceNo())
					.addObject(form.getTorrNum()).addObject(form.getPalletMaterial())
					.addObject(form.getPackType())
					.addObject(form.getBoxNum()).addObject(form.getBillWeight())
					.addObject(form.getLadingBill()).addObject(form.getPortDestNo())
					.addObject(form.getTradeTerms()).addObject(form.getPaymentProvision())
					.addObject(form.getPurchaseCode())
					.empty();

			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			if (form.getItems().isEmpty()) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, "表体信息为空");
			}

			DeclareOrderModel model = new DeclareOrderModel();
			BeanUtils.copyProperties(form, model);

			//物流委托书
			LogisticsAttorneyModel logisticsAttorneyModel = new LogisticsAttorneyModel() ;
			logisticsAttorneyModel.setCarrierInformationTempId(form.getCarrierInformationTempId());
			logisticsAttorneyModel.setCarrierInformationTempName(form.getCarrierInformationTempName());
			logisticsAttorneyModel.setCarrierInformationText(form.getCarrierInformationTempDetails());
			logisticsAttorneyModel.setShipperTempId(form.getShipperTempId());
			logisticsAttorneyModel.setShipperTempName(form.getShipperTempName());
			logisticsAttorneyModel.setShipperText(form.getShipperTempDetails());
			logisticsAttorneyModel.setConsigneeTempId(form.getConsigneeTempId());
			logisticsAttorneyModel.setConsigneeTempName(form.getConsigneeTempName());
			logisticsAttorneyModel.setConsigneeText(form.getConsigneeTempDetails());
			logisticsAttorneyModel.setNotifierTempId(form.getNotifierTempId());
			logisticsAttorneyModel.setNotifierTempName(form.getNotifierTempName());
			logisticsAttorneyModel.setNotifierText(form.getNotifierTempDetails());
			logisticsAttorneyModel.setDockingTempId(form.getDockingTempId());
			logisticsAttorneyModel.setDockingTempName(form.getDockingTempName());
			logisticsAttorneyModel.setDockingText(form.getDockingTempDetails());
			logisticsAttorneyModel.setType("1");

			if (StringUtils.isNotBlank(form.getArriveDate2())) {
				model.setArriveDate(TimeUtils.parse(form.getArriveDate2(), "yyyy-MM-dd"));
			}

			if (StringUtils.isNotBlank(form.getShipDateStr())) {
				model.setShipDate(TimeUtils.parse(form.getShipDateStr(), "yyyy-MM-dd"));
			}

			if(StringUtils.isNotBlank(form.getPlannedShipDateStr())) {
				model.setPlannedShipDate(TimeUtils.parse(form.getPlannedShipDateStr(), "yyyy-MM-dd"));
			}

			if(StringUtils.isNotBlank(form.getArriveDepotDateStr())) {
				model.setArriveDepotDate(TimeUtils.parse(form.getArriveDepotDateStr(), "yyyy-MM-dd"));
			}
			if(StringUtils.isNotBlank(form.getEstimatedDeliveryDate())){
				model.setEstimatedDeliveryDate(TimeUtils.parse(form.getEstimatedDeliveryDate(), "yyyy-MM-dd"));
			}

			User user = ShiroUtils.getUserByToken(form.getToken());

			DepotInfoMongo depotInfo = depotInfoService.getDetails(model.getDepotId());
			model.setDepotName(depotInfo.getName());

			List<DeclareOrderItemModel> list = new ArrayList<DeclareOrderItemModel>() ;
			for (int i = 0 ; i < form.getItems().size(); i ++) {

				DeclareOrderItemDTO dto = form.getItems().get(i) ;

				DeclareOrderItemModel itemModel = new DeclareOrderItemModel() ;

				BeanUtils.copyProperties(dto, itemModel);

				if (itemModel.getSeq() == null) {
					itemModel.setSeq(i + 1);// 序号
				}

				itemModel.setCreater(user.getId());// 创建人

				list.add(itemModel) ;
			}

			if (list.isEmpty()) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			model.setMerchantId(user.getMerchantId());
			model.setMerchantName(user.getMerchantName());
			model.setItemList(list);

			declareOrderService.modifyDeclare(model, logisticsAttorneyModel, user, form.getPurchaseCode(), form.getPackingList());

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	/**
	 * 批量取消
	 *
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "批量删除")
	@PostMapping(value="/cancelDeclare.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "预申报单id,多个以','隔开", required = true) })
	public ResponseBean cancelDeclare(@RequestParam(value = "token", required = true) String token,
									  @RequestParam(value = "ids", required = true) String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight || StringUtils.isBlank(token)) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user = ShiroUtils.getUserByToken(token);
			List<Long> list = StrUtils.parseIds(ids);

			declareOrderService.delDeclare(list, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}


	}

	/**
	 * 批量提交
	 *
	 * @param
	 */
	@SuppressWarnings({"rawtypes" })
	@PostMapping(value="/submitDeclareOrder.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "批量提交")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true) })
	public ResponseBean submitDeclareOrder(@RequestParam(value = "token", required = true) String token,
										   @RequestParam(value = "id", required = true) Long id) {

		try {

			User user = ShiroUtils.getUserByToken(token);
			declareOrderService.submitDeclareOrder(id, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 预申报导出
	 *
	 * @param
	 * @return void
	 * @throws IOException
	 */
	@GetMapping("/exportDeclare.asyn")
	@ApiOperation(value = "预申报导出")
	public void exportRelation(DeclareOrderForm form, HttpServletResponse response, HttpServletRequest request)
			throws Exception {

		if (StringUtils.isBlank(form.getToken())) {
			// 输入信息不完整
			return;
		}

		String sheetName = "预申报单";

		DeclareOrderDTO dto = new DeclareOrderDTO();

		BeanUtils.copyProperties(form, dto);

		User user = ShiroUtils.getUserByToken(form.getToken());
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());

		// 获取导出的信息 (表头)
		List<DeclareOrderDTO> list  = declareOrderService.getDeclareOrderByExport(dto,user);

		// 获取导出的信息 (表体)
		List<DeclareOrderItemDTO> itemList =new ArrayList<>();
		for(DeclareOrderDTO dtoModel:list){
			List<DeclareOrderItemDTO> items = declareOrderService.getDetailsByExport(dtoModel.getId());
			itemList.addAll(items);
		}

		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("预申报单", COLUMNS, KEYS, list);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品信息", ITEMS_COLUMNS, ITEMS_KEYS, itemList);

		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
		sheetList.add(mainSheet) ;
		sheetList.add(itemSheet) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 一线清关资料导出
	 *
	 * @param
	 * @return void
	 * @throws IOException
	 */
	@ApiOperation(value = "一线清关资料导出")
	@GetMapping("/exportCustomsDeclare.asyn")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true),
			@ApiImplicitParam(name = "fileTempIds", value = "模版id集合,多个用逗号隔开", required = true)})
	public void exportCustomsDeclare(HttpServletResponse response, Long id,String fileTempIds,String token){

		try {
			if (id == null) {
				throw new RuntimeException("id不能为空！");
			}
			User user = ShiroUtils.getUserByToken(token);
			Map<String, Workbook> resultMap = new HashMap<>();
			if (StringUtils.isNotEmpty(fileTempIds)) {
				List<Long> fileTempIdList = new ArrayList<>();
				String[] list = fileTempIds.split(",");
				for (String idStr : list) {
					fileTempIdList.add(Long.valueOf(idStr));
				}
				Map<String, Object> resultInMap = declareOrderService.exportCustomsDeclares(id, fileTempIdList ,user);

				Map<String, Workbook> multipartInCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultInMap, "2");
				resultMap.putAll(multipartInCustomsDeclares);

			}

			//导出压缩文件
			String downloadFilename = URLEncoder.encode("预申报单一线清关资料.zip", "UTF-8");
			// 指明response的返回对象是文件流
			response.setContentType("application/octet-stream");
			// 设置在下载框默认显示的文件名
			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

			for (String key : resultMap.keySet()) {
				Workbook wb = resultMap.get(key);
				zos.putNextEntry(new ZipEntry(key));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				wb.write(bos);
				bos.writeTo(zos);
				zos.closeEntry();
			}
			zos.flush();
			zos.close();


		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取一线清关资料
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value="/getFirstCustomsDeclareInfo.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "获取一线清关资料")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true) })
	public ResponseBean<FirstCustomsInfoDTO> getFirstCustomsDeclareInfo(
			@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "token", required = true) String token) {

		if (StringUtils.isBlank(token)) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {
			FirstCustomsInfoDTO firstModel = declareOrderService.getCustomsDeclare(id);

			if (firstModel == null) {
				firstModel = declareOrderService.saveCustomsDeclare(id);
			}

			List<CustomsPackingDetailsDTO> detailsDTOList = DownloadExcelUtil.calculateWeights(firstModel.getDetailsDTOList());

			firstModel.setDetailsDTOList(detailsDTOList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, firstModel);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	/**
	 * 编辑清关资料
	 */
//	@PostMapping("/modifyCustomsDeclare.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	@ApiOperation(value = "编辑清关资料")
//	public ResponseBean<String> modifyCustomsDeclare(FirstCustomsInfoAddForm form) {
//
//		try {
//
//			if (StringUtils.isBlank(form.getToken())) {
//				// 输入信息不完整
//				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
//			}
//
//			FirstCustomsInfoModel model = new FirstCustomsInfoModel();
//			BeanUtils.copyProperties(form, model);
//
//			// 校验id是否正确
//			boolean isRight = StrUtils.validateId(model.getId());
//			if (!isRight) {
//				// 输入信息不完整
//				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
//			}
//
//			if (StringUtils.isNotBlank(form.getSigningDateStr())) {
//				model.setSigningDate(TimeUtils.parse(form.getSigningDateStr(), "yyyy-MM-dd"));
//			}
//
//			if (StringUtils.isNotBlank(form.getInvoiceDateStr())) {
//				model.setInvoiceDate(TimeUtils.parse(form.getInvoiceDateStr(), "yyyy-MM-dd"));
//			}
//
//			if (StringUtils.isNotBlank(form.getShipDateStr())) {
//				model.setShipDate(TimeUtils.parse(form.getShipDateStr(), "yyyy-MM-dd"));
//			}
//
//			String[] goodsIds = {};
//			String[] palletNo = {};
//			String[] cartons = {};
//
//			if (StringUtils.isNotBlank(form.getGoodIds())) {
//				goodsIds = form.getGoodIds().split(",", -1);
//			}
//
//			if (StringUtils.isNotBlank(form.getPalletNos())) {
//				palletNo = form.getPalletNos().split(",", -1);
//			}
//
//			if (StringUtils.isNotBlank(form.getCarton())) {
//				cartons = form.getCarton().split(",", -1);
//			}
//
//			User user = ShiroUtils.getUserByToken(form.getToken());
//
//			declareOrderService.modifyCustomsDeclare(model, goodsIds, palletNo, cartons, user.getName());
//
//			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
//
//		} catch (Exception e) {
//			LOG.error(e.getMessage(), e);
//			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009, e);
//		}
//
//	}

	@PostMapping("/exportGoods.asyn")
	@ApiOperation(value = "编辑页面商品导入模版", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "json", value = "预申报单商品json", required = true) })
	public void exportGoods(@RequestParam(value = "json", required = true) String itemList,
							@RequestParam(value = "token", required = true) String token, HttpServletResponse response,
							HttpServletRequest request) throws Exception {

		List<Map<String, Object>> goodsList = declareOrderService.getGoodsList(itemList);

		SXSSFWorkbook wb = ExcelExportXlsx.createSXSSExcel8("申报商品", EXPORT_ITEM_COLUMNS, EXPORT_ITEM_KEYS,
				goodsList);

		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, "预申报-商品导入模板");

	}

	@SuppressWarnings("unchecked")
	@PostMapping(value="/importGoods.asyn",headers = "content-type=multipart/form-data")
	@ApiOperation(value = "导入商品")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "orderId", value = "预申报单ID", required = true),
			@ApiImplicitParam(name = "type", value = "类型 1-预申报列表 2-采购单跳转预申报", required = true)})
	public ResponseBean<UploadResponse> importGoods(@RequestParam(value = "orderId", required =false) String orderId,
													@RequestParam(value = "type", required =false) String type,
													@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file,
													@RequestParam(value = "token", required = true) String token) throws IOException {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

			User user=ShiroUtils.getUserByToken(token);

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			resultMap = declareOrderService.importGoods(data, orderId,type,user);

			Integer success = (Integer) resultMap.get("success");
			Integer failure = (Integer) resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			JSONObject responseJson = (JSONObject)JSONObject.toJSON(uploadResponse) ;
			responseJson.put("itemList", resultMap.get("itemList")) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, responseJson);

		}  catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "导入箱装明细")
	@PostMapping("/importPackingDetails.asyn")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean<UploadResponse> importPackingDetails(
			@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(value = "token", required = true) String token,String itemList) {

		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集

		try {

			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream()) ;

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user= ShiroUtils.getUserByToken(token);
			JSONArray jsonData = JSONArray.fromObject(itemList);
			List<DeclareOrderItemDTO> items = (List<DeclareOrderItemDTO>) JSONArray.toCollection(jsonData, DeclareOrderItemDTO.class);
			resultMap = declareOrderService.importPackingDetails(data, user, items);

			Integer success = (Integer) resultMap.get("success");
			Integer failure = (Integer) resultMap.get("failure");
			List<PackingDetailsModel>  packingList = (List<PackingDetailsModel>) resultMap.get("packingList");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			uploadResponse.setData(packingList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "更新轨迹时间")
	@PostMapping(value="/updateTrajectory.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> updateTrajectory(DeclareDetailsAddForm form) {

		boolean isEmpty = new EmptyCheckUtils().addObject(form.getToken())
				.addObject(form.getId()).addObject(form.getType()).empty();

		if (isEmpty) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
		}

		try {

			User user = ShiroUtils.getUserByToken(form.getToken());

			declareOrderService.updateTrajectory(form, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	/**
	 * 导出物流委托
	 * @throws Exception
	 */
	@GetMapping(value="/exportLogisticsDelegation.asyn")
	@ApiOperation(value = "导出物流委托")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true),
			@ApiImplicitParam(name = "type", value = "1、国际物流委托 2、陆运物流委托", required = true)})
	public void exportLogisticsDelegation(
			@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "type", required = true) String type,
			HttpServletResponse response, HttpServletRequest request) throws Exception{

		DeclareOrderDTO dto = declareOrderService.searchDTODetail(id);
		List<DeclareOrderItemModel> item = declareOrderService.getItem(id,"1");

		dto.setFirstGoodName(item.get(0).getGoodsName());

		double netWeight = item.stream().mapToDouble(DeclareOrderItemModel::getNetWeightSum).sum();

		dto.setNetWeight(netWeight);
		dto.setCreateDateStr(TimeUtils.format(new Date(), "yyyy-MM-dd"));

		if(dto.getArriveDate() != null) {
			dto.setArriveDateStr(TimeUtils.format(dto.getArriveDate(), "yyyy-MM-dd"));
		}

		Workbook workbook = null ;
		String fileName = "" ;

		//区分是国际物流运输方式还是陆运运输方式
		if("1".equals(type)){
			workbook = DownloadExcelUtil.exportTemplateExcel("DECLAREORDERKHY", dto);
			fileName = "空海运托运书" ;
		}else{
			workbook = DownloadExcelUtil.exportTemplateExcel("DECLAREORDERLY", dto);
			fileName = "陆运托运书" ;

			DeclareOrderModel declareOrderModel=new DeclareOrderModel();
			declareOrderModel.setId(id);
			declareOrderModel.setLandCommissionDate(TimeUtils.getNow());
			//如果预申报单当前状态是待清关或带物流委托状态；更新状态为带入仓
			if(DERP_ORDER.DECLAREORDER_STATUS_001.equals(dto.getStatus())||DERP_ORDER.DECLAREORDER_STATUS_002.equals(dto.getStatus())) {
				declareOrderModel.setStatus(DERP_ORDER.DECLAREORDER_STATUS_004);
			}
			declareOrderService.modifyDeclareOrder(declareOrderModel);
		}

		/*if(DERP.TRANSPORT_1.equals(dto.getTransport())
				|| DERP.TRANSPORT_2.equals(dto.getTransport())
				|| DERP.TRANSPORT_5.equals(dto.getTransport())) {

			workbook = DownloadExcelUtil.exportTemplateExcel("DECLAREORDERKHY", dto);
			fileName = "空海运托运书" ;

		}else if(DERP.TRANSPORT_3.equals(dto.getTransport())
				|| DERP.TRANSPORT_4.equals(dto.getTransport())
				|| DERP.TRANSPORT_6.equals(dto.getTransport())) {

			workbook = DownloadExcelUtil.exportTemplateExcel("DECLAREORDERLY", dto);
			fileName = "陆运托运书" ;
		}*/

		OutputStream out = null;
		try {
			FileExportUtil.setHeader(response, request, fileName +".xlsx");
			out = response.getOutputStream();
			workbook.write(out);
		} catch (IOException e) {
			System.err.println("文件导出异常:" + e);
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value="/declareDelivery.asyn", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "打托入库")
	public ResponseBean<String> declareDelivery(@RequestBody DeclareOrderDeliveryForm form) {

		// 必填项空值校验
		boolean isNull = new EmptyCheckUtils().addObject(form.getDeclareOrderId())
				.addObject(form.getInboundDate()).addObject(form.getItemList()).empty();

		if (isNull) {
			// 输入信息不完整
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
		}

		try {

			User user = ShiroUtils.getUserByToken(form.getToken());

			declareOrderService.saveDeclareDelivery(form, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}


	@ApiOperation(value = "编辑物流委托书")
	@PostMapping(value="/modifyLogisticsAttorney.asyn",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyLogisticsAttorney(LogisticsAttorneyForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());

			//物流委托书
			LogisticsAttorneyModel logisticsAttorneyModel = new LogisticsAttorneyModel() ;
			BeanUtils.copyProperties(form, logisticsAttorneyModel);
			logisticsAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_1);

			declareOrderService.modifyLogisticsAttorney(logisticsAttorneyModel , user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	@ApiOperation(value = "检查同一采购订单相同SKU是否存在多条")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true)
	})
	@PostMapping(value="/checkRepeatGoods.asyn",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean checkRepeatGoods(String token, Long id) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			declareOrderService.checkRepeatGoods(id);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	@ApiOperation(value = "选择商品弹窗")
	@PostMapping(value="/getItemPopupListByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<DeclareOrderItemDTO> getItemPopupListByPage(MerchandiseForm form) {
		try {
			DeclareOrderItemDTO list = declareOrderService.getItemPopupListByPage(form);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}

package com.topideal.order.webapi.sale;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.EmptyCheckUtils;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.SaleDeclareOrderDTO;
import com.topideal.entity.dto.sale.SaleDeclareOrderItemDTO;
import com.topideal.entity.vo.common.LogisticsAttorneyModel;
import com.topideal.order.service.sale.SaleDeclareOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.tools.DownloadExcelUtil;
import com.topideal.order.webapi.sale.form.LogisticsAttorneyForm;
import com.topideal.order.webapi.sale.form.MerchandiseForm;
import com.topideal.order.webapi.sale.form.SaleDeclareOrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequestMapping("/webapi/order/saleDeclare")
@RestController
@Api(tags = "销售预申报单")
public class APISaleDeclareOrderController {
	/* 打印日志 */
	protected Logger logger = LoggerFactory.getLogger(APISaleDeclareOrderController.class);

	@Autowired
	private SaleDeclareOrderService saleDeclareOrderService;

	@ApiOperation(value = "获取列表信息")
	@PostMapping(value="/listDTOByPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleDeclareOrderDTO> listDTOByPage(SaleDeclareOrderForm form) {
		SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
		try {
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto = saleDeclareOrderService.listDTOByPage(dto,user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	@ApiOperation(value = "获取列表销售预申报单各类型数量", notes="返回类型-数量集合")
	@PostMapping(value="/getDeclareTypeNum.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Map<String, Object>> getDeclareTypeNum(SaleDeclareOrderForm form) {
		try {
			SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
			BeanUtils.copyProperties(form, dto);

			User user = ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<Map<String, Object>> resultMap = saleDeclareOrderService.getDeclareTypeNum(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@ApiOperation(value = "访问销售单生成预申报单/编辑/详情/打托出库页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售预申报id,编辑/详情必填", required = false),
		@ApiImplicitParam(name = "saleIds", value = "销售订单id集合,多个用逗号隔开,销售单生成预申报单必填", required = false)
	})
	@PostMapping(value="/searchDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleDeclareOrderDTO> searchDetail(String token, Long id, String saleIds) {
		SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
		try {
			if(id == null && StringUtils.isBlank(saleIds)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			//销售单生成预申报单
			if(StringUtils.isNotBlank(saleIds)) {
				dto = saleDeclareOrderService.saleOrderToSaleDeclare(saleIds);
			}else if(id != null) {
				dto = saleDeclareOrderService.searchDetail(id);
				List<SaleDeclareOrderItemDTO> itemList = saleDeclareOrderService.searchItemsByOrderId(id);
				dto.setItemList(itemList);

			}

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	@ApiOperation(value = "保存")
	@PostMapping(value="/saveSaleDeclareOrder.asyn",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean saveSaleDeclareOrder(@RequestBody SaleDeclareOrderForm form) {
		try {
			SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());
			if(StringUtils.isNotBlank(form.getShipDate())) {
				dto.setShipDate(TimeUtils.parseDay(form.getShipDate()));
			}
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
			logisticsAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);

			saleDeclareOrderService.saveSaleDeclareOrder(dto, user ,logisticsAttorneyModel);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "推送出库指令")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售预申报id", required = true)
	})
	@PostMapping(value="/modifyPushOutOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean modifyPushOutOrder(String token, Long id) {
		try {
			User user = ShiroUtils.getUserByToken(token);
			saleDeclareOrderService.modifyPushOutOrder(id, user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "打托/提交清关资料/确认申报/确认清关资料/预约物流时间")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "销售预申报id", required = true),
		@ApiImplicitParam(name = "orderTime", value = "选择的时间", required = true),
		@ApiImplicitParam(name = "operate", value = "操作，2-打托完成 3-提交清关资料 4-确认清关资料 5-确认申报时间 6-预约物流 ", required = true)
	})
	@PostMapping(value="/updateTimeTrace.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean updateTimeTrace(String token, Long id,String orderTime, String operate) {
		try {
			boolean isEmpty = new EmptyCheckUtils().addObject(id).addObject(orderTime).addObject(operate).empty();
			if(isEmpty) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user = ShiroUtils.getUserByToken(token);
			saleDeclareOrderService.updateTimeTrace(id,orderTime, user ,operate);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 一线清关资料导出
	 *
	 * @param
	 * @return void
	 * @throws IOException
	 */
	@ApiOperation(value = "一线清关资料导出")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "预申报单id", required = true),
			@ApiImplicitParam(name = "inFileTempIds", value = "入库仓模版id集合，多个用逗号隔开", required = true),
			@ApiImplicitParam(name = "outFileTempIds", value = "出库仓模版id集合，多个用逗号隔开", required = true)
	})
	@GetMapping("/exportCustomsDeclare.asyn")
	public void exportCustomsDeclare(HttpServletResponse response, HttpServletRequest request,
								Long id,String inFileTempIds,String token,String outFileTempIds){
		try {
			if (id == null) {
				throw new RuntimeException("id不能为空！");
			}
			User user = ShiroUtils.getUserByToken(token);
			Map<String, Workbook> resultMap = new HashMap<>();
			Map<String, XWPFDocument> wordMap = new HashMap<>();
			if (!StringUtils.isEmpty(inFileTempIds)) {
				List<Long> inFileTempIdList = new ArrayList<>();
				String[] list = inFileTempIds.split(",");
				for (String idStr : list) {
					inFileTempIdList.add(Long.valueOf(idStr));
				}
				Map<String, Object> resultInMap = saleDeclareOrderService.exportCustomsDeclares(id, inFileTempIdList, "2",user);

				Map<String, Workbook> multipartInCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultInMap, "2");
				resultMap.putAll(multipartInCustomsDeclares);
				Map<String, XWPFDocument> multipartOutCustomsDeclaresWord = DownloadExcelUtil.createMultipartCustomsDeclaresWord(resultInMap, "2");
				wordMap.putAll(multipartOutCustomsDeclaresWord);

			}

			if (!StringUtils.isEmpty(outFileTempIds)) {
				List<Long> outFileTempIdList = new ArrayList<Long>();
				String[] list = outFileTempIds.split(",");
				for (String idStr : list) {
					outFileTempIdList.add(Long.valueOf(idStr));
				}
				Map<String, Object> resultOutMap = saleDeclareOrderService.exportCustomsDeclares(id, outFileTempIdList, "1",user);

				Map<String, Workbook> multipartOutCustomsDeclares = DownloadExcelUtil.createMultipartCustomsDeclares(resultOutMap, "1");
				resultMap.putAll(multipartOutCustomsDeclares);
				Map<String, XWPFDocument> multipartOutCustomsDeclaresWord = DownloadExcelUtil.createMultipartCustomsDeclaresWord(resultOutMap, "1");
				wordMap.putAll(multipartOutCustomsDeclaresWord);
			}

			//导出压缩文件
			String downloadFilename = URLEncoder.encode("销售预申报单一线清关资料.zip", "UTF-8");
			// 指明response的返回对象是文件流
			response.setContentType("application/octet-stream");
			// 设置在下载框默认显示的文件名
			response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
			ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

			for(String key : wordMap.keySet()) {
				XWPFDocument document = wordMap.get(key);
				zos.putNextEntry(new ZipEntry(key));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				document.write(bos);
				bos.writeTo(zos);
				zos.closeEntry();
			}

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
	@GetMapping(value="/exportLogisticsDelegation.asyn")
	@ApiOperation(value = "预约物流导出")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "预申报单id", required = true)
	})
	public void exportLogisticsDelegation(Long id,String token ,HttpServletResponse response, HttpServletRequest request){
		OutputStream out = null;
		try {
			Map<String, Object> result = saleDeclareOrderService.exportLogisticsDelegation(id);
			String transport = (String) result.get("transport");
			Workbook workbook = null ;
			String fileName = "" ;
			/**
			 * （1）若运输方式为：海运、空运、中欧铁路时则按空运模版导出物流委托书；
			 * （2）若运输方式为：陆运、港到仓拖车、其他时则按陆运模版导出物流委托
			 */
			if(DERP.TRANSPORT_1.equals(transport)
					|| DERP.TRANSPORT_2.equals(transport)
					|| DERP.TRANSPORT_5.equals(transport)) {

				workbook = DownloadExcelUtil.exportTemplateExcel("DECLAREORDERKHY", result);
				fileName = "空海运托运书" ;

			}else if(DERP.TRANSPORT_3.equals(transport)
					|| DERP.TRANSPORT_4.equals(transport)
					|| DERP.TRANSPORT_6.equals(transport)) {

				workbook = DownloadExcelUtil.exportTemplateExcel("DECLAREORDERLY", result);
				fileName = "陆运托运书" ;
			}

			FileExportUtil.setHeader(response, request, fileName +".xlsx");
			out = response.getOutputStream();
			workbook.write(out);

		}catch(Exception e) {
			System.err.println("文件导出异常:" + e);
			e.printStackTrace();
		}finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	@ApiOperation(value = "打托出库")
	@PostMapping(value="/saveSaleDeclareOut.asyn",consumes= MediaType.APPLICATION_JSON_VALUE)
	public ResponseBean saveSaleDeclareOut(@RequestBody SaleDeclareOrderForm form) {
		try {
			if(StringUtils.isBlank(form.getDeliverDate())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"出库时间不能为空");
			}

			SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());

			saleDeclareOrderService.saveSaleDeclareOut(dto, user ,form.getDeliverDate());

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "销售预申报id集合，多个用逗号隔开", required = true)
	})
	@PostMapping(value="/delSaleDeclareOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSaleDeclareOrder(String token, String ids) {
		try {

			saleDeclareOrderService.delSaleDeclareOrder(ids);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	@ApiOperation(value = "获取导出数量")
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Integer> delSaleDeclareOrder(SaleDeclareOrderForm form) {
		Integer result = 0;
		try {
			SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			List<SaleDeclareOrderDTO> list = saleDeclareOrderService.listSaleDeclareOrder(dto, user);

			result = list.size();

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}

	@ApiOperation(value = "导出" ,produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportOrder.asyn")
	public void exportOrder(SaleDeclareOrderForm form, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		try {
			SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			dto.setMerchantId(user.getMerchantId());
			dto.setMerchantName(user.getMerchantName());
			List<SaleDeclareOrderDTO> saleDeclareList = saleDeclareOrderService.listSaleDeclareOrder(dto ,user);
			List<SaleDeclareOrderItemDTO> itemList = new ArrayList<SaleDeclareOrderItemDTO>();
			for(SaleDeclareOrderDTO declare : saleDeclareList){
				List<SaleDeclareOrderItemDTO> declareItemList = saleDeclareOrderService.searchItemsByOrderId(declare.getId());
				for(SaleDeclareOrderItemDTO itemDTO : declareItemList){
					itemDTO.setSaleDeclareCode(declare.getCode());
				}
				itemList.addAll(declareItemList);
			}

			String[] SALE_COLUMNS = {"公司","预申报单号","销售订单号","PO号","客户","事业部","出库仓库","入库仓库","订单状态","贸易条款","创建人","海外仓理货单位",
					"LBX单号","装货港","目的地名称","目的港名称","卸货港","出库地点","进出口口岸","出库关区","入库关区","装船时间","启运港","运输方式","境外发货人",
					"承运商","发票号","报关合同号","提单号","二程提单号","车次","托数","箱数","托盘材质","包装方式","提单毛重","车型及数量","唛头","标准箱TEU","提货方式",
					"收货人姓名","详情地址","国家","已接单","导出打托资料","完成打托","提交一线清关资料","审核一线清关资料","已推接口","完成申报","预约物流","已出库",
					"部分上架","已上架"};

			String[] SALE_KEYS = {"merchantName","code","saleOrderCodes","poNo","customerName","buName","outDepotName","inDepotName","statusLabel",
					"tradeTermsLabel","createName","tallyingUnitLabel","lbxNo","loadPort","destination","destinationPort","portDesLabel",
					"deliveryAddr","imExpPort","outCustomsName","inCustomsName","shipDate","departurePort","transportLabel","shipper","carrier","invoiceNo",
					"contractNo","ladingBill","blNo","trainNo","torrNum","boxNum","torrPackTypeLabel","pack","billWeight","motorcycleType","mark","teu",
					"mailModeLabel","receiverName","receiverAddress","country","createDate","exportPallteizeDate","pallteizeDate","customsSubmitDate",
					"customsConfirmDate","pushOutDate","confirmDeclarationDate","logisticsCommissionDate","deliverDate","firstShelfDate","endShelfDate"};

			String[] ITEM_COLUMNS = {"预申报单号","销售订单号","商品货号","商品名称","商品编码","申报数量","销售单价","申报单价","申报总金额","品牌类型","毛重","净重",
					"箱数","箱号","托盘号","成分占比"};
//
			String[] ITEM_KEYS = {"saleDeclareCode","saleOrderCode","goodsNo","goodsName","goodsCode","num","salePrice","price","amount","brandName",
					"grossWeightSum","netWeightSum","boxNum","boxNo","torrNo","constituentRatio"};

			String mainSheetName = "销售预申报单";
			//生成表格
			List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
			String shelfSheetName = "销售预申报单";
			ExportExcelSheet saleDeclareSheet = ExcelUtilXlsx.createSheet(shelfSheetName, SALE_COLUMNS, SALE_KEYS, saleDeclareList);
			sheetList.add(saleDeclareSheet) ;
			String itemSheetName = "商品信息";
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, ITEM_COLUMNS, ITEM_KEYS, itemList);
			sheetList.add(itemSheet) ;

			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel8(sheetList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@ApiOperation(value = "选择商品弹窗")
	@PostMapping(value="/getSalePopupList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleDeclareOrderItemDTO> getSalePopupList(MerchandiseForm form) {
		try {
			SaleDeclareOrderItemDTO list = saleDeclareOrderService.getSalePopupListByPage(form);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	@ApiOperation(value = "导出打托资料" ,produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "销售预申报id", required = true)
	})
	@GetMapping(value="/exportPallteizeData.asyn")
	public void exportPallteizeData(Long id,String token, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		try {
			User user = ShiroUtils.getUserByToken(token);

			String[] SALE_COLUMNS = {"主体","出库仓库","条形码","货号","产品名称","订单数量","效期需求","备注"};

			String mainSheetName = "销售预申报打托资料";
			//生成表格
			SXSSFWorkbook wb = saleDeclareOrderService.exportPallteizeData(mainSheetName, SALE_COLUMNS, id,user) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
		}catch(Exception e) {
			e.printStackTrace();
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
			logisticsAttorneyModel.setType(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2);

			saleDeclareOrderService.modifyLogisticsAttorney(logisticsAttorneyModel , user);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	@ApiOperation(value = "复制销售预申报单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "销售预申报id", required = true)
	})
	@PostMapping(value="/copySaleDeclare.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleDeclareOrderDTO> copySaleDeclare(String token, Long id) {
		SaleDeclareOrderDTO dto = new SaleDeclareOrderDTO();
		try {
			if(id == null) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			dto = saleDeclareOrderService.copySaleDeclare(id);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
}

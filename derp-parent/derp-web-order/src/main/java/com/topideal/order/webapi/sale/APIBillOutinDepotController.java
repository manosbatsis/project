package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.BillOutinDepotDTO;
import com.topideal.entity.dto.sale.BillOutinDepotItemDTO;
import com.topideal.entity.vo.sale.BillOutinDepotBatchModel;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;
import com.topideal.order.service.sale.BillOutinDepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.OrderGoodsInfoNumDTO;
import com.topideal.order.webapi.sale.form.BillOutinDepotForm;
import com.topideal.order.webapi.sale.form.BillOutinDepotItemForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 账单出库列表
 * @author gy
 *
 */
@RestController
@RequestMapping("/webapi/order/billOutinDepot")
@Api(tags = "账单出库单")
public class APIBillOutinDepotController {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(APIBillOutinDepotController.class);

	private static final String[] MAIN_COLUMNS = {"账单出库单号", "出库仓库","事业部", "结算账单号", "处理类型", "账单出库时间",
					"账单总量", "账单金额", "库存调整类型", "单据状态", "账单来源", "币种","库位类型"} ;

	private static final String[] MAIN_KEYS = {"code", "depotName","buName","billCode", "processingTypeLabel", "deliverDate",
					"totalNum", "totalAmount", "operateTypeLabel", "stateLabel", "billSourceLabel", "currencyLabel","stockLocationTypeName"} ;

	private static final String[] ITEM_COLUMNS = {"账单出库单号", "商品货号", "平台SKU条码", "标准条码","条码", "商品名称",
					"PO号", "数量", "结算金额(不含税)", "税率", "税额", "结算金额(含税)"} ;

	private static final String[] ITEM_KEYS = {"outinDepotCode", "goodsNo", "platformSku", "commbarcode","barcode",  "goodsName",
					"poNo", "num", "amount", "taxRate", "tax", "taxAmount"} ;

	@Autowired
	BillOutinDepotService billOutinDepotService ;

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "访问详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "账单出库单id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BillOutinDepotDTO> toDetailsPage(String token, Long id)throws Exception{
		BillOutinDepotDTO dto = new BillOutinDepotDTO();
		try {
			dto = billOutinDepotService.searchDetail(id);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "访问账单出库单列表")
	@PostMapping(value="/listBillOutinDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BillOutinDepotDTO> listBillOutinDepot(BillOutinDepotForm form) {
		BillOutinDepotDTO dto = new BillOutinDepotDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			dto.setBillCode(form.getBillCode());
			dto.setState(form.getState());
			dto.setCode(form.getCode());
			dto.setProcessingType(form.getProcessingType());
			dto.setOperateType(form.getOperateType());
			if(StringUtils.isNotBlank(form.getBuId())) {
				dto.setBuId(Long.valueOf(form.getBuId()));
			}
			dto.setBillSource(form.getBillSource());
			dto.setCustomerName(form.getCustomerName());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setCustomerId(form.getCustomerId());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = billOutinDepotService.listBillOutinDepotByPage(dto,user);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}


	/**
	 * 手工导入
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="importBillOutinDepot.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importBillOutinDepot(String token,
										@ApiParam(value = "上传的文件", required = true)
										@RequestParam(value = "file", required = true) MultipartFile file) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user= ShiroUtils.getUserByToken(token);
			resultMap = billOutinDepotService.importBillOutinDepot(data, user);
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 详情页分页查询明细
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "详情页分页查询明细")
	@PostMapping(value="/listBillOutinDepotItem.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BillOutinDepotItemModel> listBillOutinDepotItem(BillOutinDepotItemForm form) {
		BillOutinDepotItemModel model = new BillOutinDepotItemModel();
		try{
			if(StringUtils.isNotBlank(form.getOutinDepotId())) {
				model.setOutinDepotId(Long.valueOf(form.getOutinDepotId()));
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			model.setBegin(form.getBegin());
			model.setPageSize(form.getPageSize());

			// 响应结果集
			model = billOutinDepotService.listBillOutinDepotItemByPage(model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}

	/**
	 * 详情页分页查询批次
	 * @param model
	 * @return
	 */
	@ApiOperation(value = "详情页分页查询批次")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "outinDepotId", value = "账单出库单id", required = true),
		@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
		@ApiImplicitParam(name = "pageSize", value = "每一页记录数", required = true),
	})
	@PostMapping(value="/listBillOutinDepotBatch.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BillOutinDepotBatchModel> listBillOutinDepotBatch(String token, String outinDepotId,int begin,int pageSize) {
		BillOutinDepotBatchModel model = new BillOutinDepotBatchModel();
		try{
			if(StringUtils.isNotBlank(outinDepotId)) {
				model.setOutinDepotId(Long.valueOf(outinDepotId));
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			model.setBegin(begin);
			model.setPageSize(pageSize);
			// 响应结果集
			model = billOutinDepotService.listBillOutinDepotBatchByPage(model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
	}

	/**
	 *
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中要删除的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="/delBillOutinDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delBillOutinDepot(String token,String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}
			if(StringUtils.isBlank(ids)) {
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			List<Long> list = StrUtils.parseIds(ids);
			boolean b = billOutinDepotService.delBillOutinDepot(list);
			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");//未知异常
			}
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * */
	@ApiOperation(value = "获取选中订单的所有商品和对应数量")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@ApiResponses({
		@ApiResponse(code = 10000,message = "data => code：由 商品id - 出库仓id - 出库仓编码 - 商品货号 拼接而成")
	})
	@PostMapping(value="/getOrderGoodsInfo.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<OrderGoodsInfoNumDTO>> getOrderGoodsInfo(String token,String ids) {
		List<OrderGoodsInfoNumDTO> resultList = new ArrayList<OrderGoodsInfoNumDTO>();
		try{
			Map<String,Integer> map =new HashMap<String,Integer>();
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)) {
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List<Long> list = StrUtils.parseIds(ids);
			// 响应结果集
            map = billOutinDepotService.getOrderGoodsInfo(list);
            for(Entry<String, Integer> entry : map.entrySet()) {
            	OrderGoodsInfoNumDTO billOutinDepotGoodsInfoNumDTO = new OrderGoodsInfoNumDTO();
            	billOutinDepotGoodsInfoNumDTO.setCode(entry.getKey());
            	billOutinDepotGoodsInfoNumDTO.setNum(entry.getValue());
            	resultList.add(billOutinDepotGoodsInfoNumDTO);
            }


		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}

	/**
	 * 审核
	 * */
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditBillOutinDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<UploadResponse> auditBillOutinDepot(String token ,String ids) {
		int count = 0; //失败条数
		int total = 0; //总条数
		StringBuffer failureMsg = new StringBuffer();
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)) {
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List<Long> list = StrUtils.parseIds(ids);
            total = list.size();
            User user= ShiroUtils.getUserByToken(token);
			for (Long id : list) {
				try {
					Map<String,String> resultDetail = billOutinDepotService.auditSaleOutDepot(id,user);
					if (resultDetail.containsKey("code") && resultDetail.get("code").equals("01")) {
						count ++;
						failureMsg.append(resultDetail.get("msg"));
					}
				} catch (Exception e) {
					count ++;
					failureMsg.append(e.getMessage()+"\n");
				}
			}

			if(count > 0) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),failureMsg.toString());
			}

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 获取导出出库清单的数量
	 */
	@ApiOperation(value = "获取导出出库清单的数量")
	@ApiResponses({
		@ApiResponse(code = 10000,message = "data => 导出的出库清单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(BillOutinDepotForm form) throws Exception{
		Integer total = 0;
		try{
			BillOutinDepotDTO dto = new BillOutinDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			dto.setBillCode(form.getBillCode());
			dto.setState(form.getState());
			dto.setCode(form.getCode());
			dto.setProcessingType(form.getProcessingType());
			dto.setOperateType(form.getOperateType());
			if(StringUtils.isNotBlank(form.getBuId())) {
				dto.setBuId(Long.valueOf(form.getBuId()));
			}
			dto.setBillSource(form.getBillSource());
			dto.setCustomerId(form.getCustomerId());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setIds(form.getIds());
			// 响应结果集
			Integer result = billOutinDepotService.getOrderCount(dto);
			total = result;

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}

	/**
	 * 导出销售出库单
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportAgreementCurrencyConfig.asyn")
	public ResponseBean exportBillOutinDepot(HttpServletResponse response, HttpServletRequest request,BillOutinDepotForm form) throws Exception{
		try {
			BillOutinDepotDTO dto = new BillOutinDepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			dto.setBillCode(form.getBillCode());
			dto.setState(form.getState());
			dto.setCode(form.getCode());
			dto.setProcessingType(form.getProcessingType());
			dto.setOperateType(form.getOperateType());
			if(StringUtils.isNotBlank(form.getBuId())) {
				dto.setBuId(Long.valueOf(form.getBuId()));
			}
			dto.setBillSource(form.getBillSource());
			dto.setCustomerName(form.getCustomerName());
			dto.setCustomerId(form.getCustomerId());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setIds(form.getIds());

			List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;

			// 响应结果集
			List<BillOutinDepotDTO> mainList = billOutinDepotService.getExportMainList(dto,user);
			String mainSheetName = "账单出库单";

			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
			sheetList.add(mainSheet) ;

			List<BillOutinDepotItemDTO> itemList = billOutinDepotService.getExportItemList(dto,user);
			String itemSheetName = "出库商品明细";
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, ITEM_COLUMNS, ITEM_KEYS, itemList);
			sheetList.add(itemSheet) ;

	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

	}
}

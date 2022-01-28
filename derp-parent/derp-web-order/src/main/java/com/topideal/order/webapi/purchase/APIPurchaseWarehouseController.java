package com.topideal.order.webapi.purchase;

import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.purchase.PurchaseWarehouseDTO;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportBean;
import com.topideal.entity.dto.purchase.PurchaseWarehouseExportDTO;
import com.topideal.entity.dto.purchase.PurchaseWarehouseItemExportDTO;
import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseWarehouseBatchService;
import com.topideal.order.service.purchase.PurchaseWarehouseService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.PurchaseWarehouseForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购入库单 controller
 *
 */
@RestController
@RequestMapping("/webapi/order/warehouse")
@Api(tags = "warehouse-采购入库单列表")
public class APIPurchaseWarehouseController {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(APIPurchaseWarehouseController.class);

	/** 标题 */
	private static final String[] COLUMNS1 = { "公司名称", "事业部","入库单号", "仓库名称", "预申报单号","采购单号", "理货单号", "理货时间", "合同号", "外部单号",
			"单据状态", "托板数量", "LBX单号", "入库时间" };
	private static final String[] KEYS1 = {"merchantName", "buName", "warehouseCode", "depotName", "declareCode", "purchaseCode", "tallyingCode", "tallyingDate", "contractNo", "extraCode",
			"stateLabel", "palletNum", "lbxNo", "inboundDate"} ;

	private static final String[] COLUMNS2 = { "预申报单号","采购单号", "商品货号", "商品名称", "商品条形码", "标准条码", "采购单价", "入库数量", "损货数量","过期数量","正常数量", "批次号", "生产日期",
			"失效日期","海外仓理货单位" };
	private static final String[] KEYS2 = {"declareCode","purchaseCode", "goodsNo", "goodsName", "barcode", "commbarcode", "purchasePrice", "tallyingNum", "wornNum", "expireNum",
			"normalNum2","batchNo", "productionDate", "overdueDate", "tallyingUnitLabel"} ;

	private static final String[] COLUMNS3 = { "入库单号", "商品货号","批次号","生产日期","失效日期", "是否勾稽", "勾稽数量", "未勾稽数量", "采购订单号" };
	private static final String[] KEYS3 = {"warehouseCode", "goodsNo", "batchNo", "productionDate", "overdueDate", "isArticulation", "num", "num2", "purchaseCode"} ;

	// 采购入库service
	@Autowired
	private PurchaseWarehouseService purchaseWarehouseService;
	// 采购入库商品批次
	@Autowired
	private PurchaseWarehouseBatchService purchaseWarehouseBatchService;
	@Autowired
	private CommonBusinessService commonBusinessService ;

	/**
	 * 根据ID查找详情
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getDetailsById.asyn")
	@ApiOperation(value = "根据ID查找详情")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购入库单id", required = true) })
	public ResponseBean<PurchaseWarehouseDTO> getDetailsById(@RequestParam(value="token", required=true)String token,
			@RequestParam(value="id", required=true) Long id) {

		try {
			PurchaseWarehouseDTO dto = purchaseWarehouseService.searchDTODetail(id);
			List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchService.getGoodsAndBatch(id);

			dto.setBatchList(batchList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 获取分页数据
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/listPurchaseWarehouse.asyn")
	@ApiOperation(value = "获取分页数据")
	public ResponseBean<PurchaseWarehouseDTO> listPurchaseWarehouse(PurchaseWarehouseForm form) {
		try {
			User user= ShiroUtils.getUserByToken(form.getToken()) ;

			PurchaseWarehouseDTO dto = new PurchaseWarehouseDTO() ;
			BeanUtils.copyProperties(form, dto);

			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseWarehouseService.listPurchaseWarehousePage(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 确认入仓
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/confirmDepot.asyn")
	@ApiOperation(value = "确认入仓")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "采购入库单id,多个以','隔开", required = true) })
	public ResponseBean<String> confirmDepot(@RequestParam(value="token", required=true)String token,
			@RequestParam(value="ids", required=true) String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
			}

			User user= ShiroUtils.getUserByToken(token) ;
			List<Long> list = StrUtils.parseIds(ids);

			boolean flag = true ;
			StringBuffer msgSb = new StringBuffer() ;

			List<InvetAddOrSubtractRootJson> jsonList = purchaseWarehouseService.confirmDepot(list, user.getId(), user.getName(),
					user.getTopidealCode());

//			for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonList) {
//				try {
//					commonBusinessService.saveAutoPurchaseAnalysis(invetAddOrSubtractRootJson.getOrderNo());
//				} catch (DerpException e) {
//
//					flag &= false ;
//
//					if(msgSb.length() > 0) {
//						msgSb.append("<br/>") ;
//					}
//
//					msgSb.append(e.getMessage()) ;
//
//					commonBusinessService.modifyCorrelationstatus((PurchaseWarehouseModel)e.getObj());
//				}
//			}

			if(jsonList != null && jsonList.size() > 0) {
				purchaseWarehouseService.pushInventory(jsonList, user);
			}

			if(msgSb.length() == 0) {
				msgSb.append("成功") ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, msgSb.toString());

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 关联采购单导入
	 *
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/importRelation.asyn")
	@ApiOperation(value = "关联采购单导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean<UploadResponse> importRelation(@ApiParam(value = "上传的文件", required = true)
	@RequestParam(value = "file", required = true)  MultipartFile file,
	@RequestParam(value = "token", required = true) String token) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集

			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user= ShiroUtils.getUserByToken(token) ;
			resultMap = purchaseWarehouseService.importRelation(data, user);

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse) ;

		} catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, e.getMessage()) ;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 入库单导入
	 *
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/importWarehouse.asyn")
	@ApiOperation(value = "入库单导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean<UploadResponse> importWarehouse(@ApiParam(value = "上传的文件", required = true)
	@RequestParam(value = "file", required = true)  MultipartFile file,
	@RequestParam(value = "token", required = true) String token) {
		try {

			Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集

			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user= ShiroUtils.getUserByToken(token) ;
			resultMap = purchaseWarehouseService.saveImportWarehouse(data, user);

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse) ;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 入库单明细导出
	 *
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "入库单明细导出", notes="若存在勾选订单导出，按勾选IDS导出，否则按搜索栏查询条件导出")
	@GetMapping("/exportRelation.asyn")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "采购入库订单ID，多个以‘,’隔开", required = false)
	})
	public void exportRelation(HttpServletResponse response, HttpServletRequest request,
			String ids, PurchaseWarehouseForm form) throws Exception {
		String sheetName = "入库明细";
		// 根据勾选的获取信息
		List<Long> list = StrUtils.parseIds(ids);

		User user= ShiroUtils.getUserByToken(form.getToken()) ;

		PurchaseWarehouseDTO dto = new PurchaseWarehouseDTO() ;
		BeanUtils.copyProperties(form, dto);

		dto.setMerchantId(user.getMerchantId());

		List<PurchaseWarehouseExportDTO> result = purchaseWarehouseService.getExportDetails(list, dto, user);

		List<PurchaseWarehouseItemExportDTO> itemList = new ArrayList<PurchaseWarehouseItemExportDTO>() ;

		for (PurchaseWarehouseExportDTO purchaseWarehouseExportDTO : result) {
			itemList.addAll(purchaseWarehouseExportDTO.getItemList()) ;
		}

		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("基本信息", COLUMNS1, KEYS1, result);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品信息", COLUMNS2, KEYS2, itemList);

		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);

		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 入库单勾稽明细导出
	 *
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "入库单勾稽明细导出")
	@GetMapping("/purchaseExportRelation.asyn")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "采购入库订单ID，多个以‘,’隔开", required = true),
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public void purchaseExportRelation(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value="ids", required=true)String ids,
			@RequestParam(value="token", required=true)String token) throws Exception {

		User user= ShiroUtils.getUserByToken(token) ;

		String sheetName = "入库勾稽关联导出";
		// 根据勾选的获取信息
		List<Long> list = StrUtils.parseIds(ids);
		List<PurchaseWarehouseExportBean> result = purchaseWarehouseService.getPurchaseExportDetails(list,
				user.getId());
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS3, KEYS3, result);
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 入库单勾稽明细校验仓库是否为在途仓
	 *
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/checkWarehouseDepotType.asyn")
	@ApiOperation(value = "入库单勾稽校验仓库是否为在途仓")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "采购入库订单ID，多个以‘,’隔开", required = true),
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean<String> checkExportRelation(@RequestParam(value="ids", required=true)String ids,
			@RequestParam(value="token", required=true)String token) {
		String result = "";
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}
			List<Long> list = StrUtils.parseIds(ids);
			result = purchaseWarehouseService.checkWarehouseDepotType(list);

			if(StringUtils.isBlank(result)) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, result) ;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}
	/**
	 * 删除
	 * @param ids
	 * */
	@SuppressWarnings("unchecked")
	@PostMapping("/delBatchByIds.asyn")
	@ApiOperation(value = "删除入库单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "ids", value = "采购入库订单ID，多个以‘,’隔开", required = true),
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean<String> delBatchByIds(@RequestParam(value="ids", required=true)String ids,
			@RequestParam(value="token", required=true)String token) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
            }
           List<Long> list = StrUtils.parseIds(ids);
           purchaseWarehouseService.delBatchByIds(list);

           return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

        }catch(Exception e){
        	LOGGER.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
        }
	}
}

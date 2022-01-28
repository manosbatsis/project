package com.topideal.order.webapi.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotItemModel;
import com.topideal.order.service.purchase.PurchaseReturnOdepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.PurchaseReturnOdepotForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购退货订单 控制层
 *
 * @author Gy
 */
@RestController
@RequestMapping("/webapi/order/purchaseReturnOdepot")
@Api(tags = "purchaseReturnOdepot-采购退货出库订单列表")
public class APIPurchaseReturnOdepotController {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(APIPurchaseReturnOdepotController.class);

	private static final String[] COLUMNS = { "出库清单编号","供应商","公司", "事业部","采购退订单号","出库时间",
				"PO单号","提单号","出库仓库","币种","单据状态","商品货号",
				"商品名称","商品条形码","数量","批次号","生产日期","失效日期"};

	private static final String[] KEYS = { "code", "supplierName", "merchantName", "buName", "purchaseReturnCode", "returnOutDate",
			"poNo", "blNo", "outDepotName", "currencyLabel", "statusLabel", "goodsNo",
			"goodsName", "barcode", "num", "batchNo", "productionDate", "overdueDate"};

	// 采购退货出库订单service
	@Autowired
	PurchaseReturnOdepotService purchaseReturnOdepotService;

	/**
	 * 获取分页数据
	 *
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/listPurchaseReturnOdepot.asyn")
	@ApiOperation(value = "获取分页数据")
	private ResponseBean<PurchaseReturnOdepotDTO> listPurchaseReturnOdepot(PurchaseReturnOdepotForm form) {
		try {

			if(StringUtils.isBlank(form.getToken())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user= ShiroUtils.getUserByToken(form.getToken());

			PurchaseReturnOdepotDTO dto = new PurchaseReturnOdepotDTO() ;
			BeanUtils.copyProperties(form, dto);

			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseReturnOdepotService.listPurchaseReturnOdepotPage(dto, user);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	/**
	 * 根据ID查询详情
	 * @param token
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getDetailsPById.asyn")
	@ApiOperation(value = "根据ID查询详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "采购退货出库单ID", required = true)
	})
	public ResponseBean<PurchaseReturnOdepotDTO> getDetailsPById(@RequestParam(value="token", required=true)String token,
			@RequestParam(value="id", required=true)Long id) {

		try {

			if(StringUtils.isBlank(token)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			PurchaseReturnOdepotDTO dto= purchaseReturnOdepotService.getDTOById(id) ;
			List<PurchaseReturnOdepotItemModel> itemList = purchaseReturnOdepotService.getItemListByOrderId(id) ;

			dto.setItemList(itemList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

	}

	/**
	 * 导入
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/importOrder.asyn")
	@ApiOperation(value = "导入采购退货出库订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	public ResponseBean<UploadResponse> importOrder(@ApiParam(value = "上传的文件", required = true)
	@RequestParam(value = "file", required = true)  MultipartFile file,
	@RequestParam(value = "token", required = true) String token) {

		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集

		try{
			if (file == null
					|| StringUtils.isBlank(token)) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(token) ;

			resultMap = purchaseReturnOdepotService.importPurchaseReturnOdepot(data, user) ;

			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, uploadResponse) ;

		} catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}


	@SuppressWarnings("unchecked")
	@PostMapping("auditPurchaseReturnOrder.asyn")
	@ApiOperation(value = "审核采购退货出库订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "采购退货出库订单ID，多个以','隔开", required = true)
	})
	public ResponseBean<String> auditPurchaseReturnOrder(@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "ids", required = true) String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(token) ;

			List<Long> list = StrUtils.parseIds(ids);
			boolean b = purchaseReturnOdepotService.auditPurchaseReturnOrder(list, user);

			if (!b) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999) ;
			}

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@GetMapping("/exportPurchaseReturnOdepot.asyn")
	@ApiOperation(value = "导出采购退货出库订单")
	public void exportPurchaseReturnOdepot(HttpServletResponse response, HttpServletRequest request, PurchaseReturnOdepotForm form) throws Exception {

		if (StringUtils.isBlank(form.getToken())) {
			// 输入信息不完整
			return  ;
		}

		PurchaseReturnOdepotDTO dto = new PurchaseReturnOdepotDTO() ;
		BeanUtils.copyProperties(form, dto);

		User user= ShiroUtils.getUserByToken(form.getToken()) ;
		String sheetName = "采购退货出库订单";
		dto.setMerchantId(user.getMerchantId());
		// 获取导出的信息
		List<PurchaseReturnOdepotExportDTO> result = purchaseReturnOdepotService.getDetailsByExport(dto,user);

		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}

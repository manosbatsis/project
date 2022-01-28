package com.topideal.order.webapi.purchase;

import com.alibaba.fastjson.JSONObject;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.purchase.PurchaseSdOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseSdOrderPageDTO;
import com.topideal.entity.vo.purchase.PurchaseSdOrderSditemModel;
import com.topideal.order.service.purchase.PurchaseSdOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.purchase.form.PurchaseSdOrderPageForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webapi/order/purchaseSdOrder")
@Api(tags = "purchaseSdOrder-采购SD列表")
public class APIPurchaseSdOrderController {

	private static final Logger LOG = Logger.getLogger(APIPurchaseSdOrderController.class) ;

	private static final String[] MAIN_KEY = {"code", "typeLabel", "purchaseCode", "poNo", "merchantName", "supplierName", "buName", "depotName", "totalNum",
			"totalAmount", "currencyLabel", "goodsNo", "goodsName", "num", "price", "amount", "sdTypeName", "sdPrice", "sdAmount"} ;
	private static final String[] MAIN_COLS = {"SD单号", "单据类型", "关联单号", "PO号", "公司", "供应商", "事业部", "仓库", "数量",
			"金额", "币种", "商品货号", "商品名称", "商品数量", "商品单价", "商品金额", "SD类型", "SD单价", "SD金额"} ;

	@Autowired
	private PurchaseSdOrderService purchaseSdOrderService ;


	/**
	 * 获取分页数据
	 * @param model
	 * @return
	 */
	@ApiOperation("获取分页数据")
	@PostMapping("/purchaseSdOrderList.asyn")
	private ResponseBean<PurchaseSdOrderPageDTO> purchaseSdOrderList(PurchaseSdOrderPageForm form) {
		try{

			if(StringUtils.isBlank(form.getToken())){
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(form.getToken()) ;

			PurchaseSdOrderPageDTO dto = new PurchaseSdOrderPageDTO() ;

			BeanUtils.copyProperties(form, dto);

			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = purchaseSdOrderService.getPurchaseSdOrderPageList(dto);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	/**
	 * 详情
	 */
	@ApiOperation("根据ID获取详情")
	@PostMapping("/getDetailById.asyn")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "SD单id", required = true),
	})
	public ResponseBean<PurchaseSdOrderDTO> getDetailById(Long id) {

		try {
			PurchaseSdOrderDTO dto = purchaseSdOrderService.searchDTOById(id) ;
			List<PurchaseSdOrderSditemModel> sdItemList = purchaseSdOrderService.getSdItemList(id) ;

			dto.setItemList(sdItemList);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto) ;

		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}

	}

	@ApiOperation("导出")
	@GetMapping("/exportOrder.asyn")
	public void exportOrder(HttpServletResponse response, HttpServletRequest request,
							PurchaseSdOrderPageForm form) throws Exception {

		if(StringUtils.isBlank(form.getToken())){
			return  ;
		}

		User user = ShiroUtils.getUserByToken(form.getToken()) ;

		PurchaseSdOrderPageDTO dto = new PurchaseSdOrderPageDTO() ;

		BeanUtils.copyProperties(form, dto);

		dto.setMerchantId(user.getMerchantId());

		List<PurchaseSdOrderPageDTO> exportList = purchaseSdOrderService.getExportSdOrder(dto) ;

		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList("采购SD单导出", MAIN_COLS, MAIN_KEY, exportList);

		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, "采购SD单导出");
	}

	@PostMapping("/getAmountAdjustmentDetail.asyn")
	@ApiOperation(value = "根据ID获取金额调整明细", notes = "返回值map key：order-订单 itemList-商品列表 totalSdAmount-SD总金额 totalAmount-总金额")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "SD单id", required = true),
	})
	public ResponseBean<Map> getAmountAdjustmentDetail(Long id) {
		try {

			Map<String, Object> map = purchaseSdOrderService.getAmountAdjustmentDetail(id) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, map) ;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	@PostMapping("/saveAmountAdjustment.asyn")
	@ApiOperation("保存金额调整")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "SD单id", required = true),
			@ApiImplicitParam(name = "items", value = "修改明细json", required = true),
	})
	public ResponseBean saveAmountAdjustment(@RequestParam("items")String itemList,
			@RequestParam("id")String id, @RequestParam("token") String token) {
		try {

			if(StringUtils.isBlank(id) ||
				StringUtils.isBlank(itemList) ||
					StringUtils.isBlank(token) ) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			purchaseSdOrderService.saveAmountAdjustment(id, itemList) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
		}catch (DerpException e) {
			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, e) ;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	@PostMapping("/saveSdOrder.asyn")
	@ApiOperation("保存SD单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "采购订单id", required = true)
	})
	public ResponseBean saveSdOrder(String id,  String token) {
		try {

			if(StringUtils.isBlank(id) || StringUtils.isBlank(token) ) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(token);

			purchaseSdOrderService.saveSdOrder(id, user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000) ;
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e) ;
		}
	}

	@PostMapping("/importOrder.asyn")
	@ApiOperation("导入SD单")
	@ApiImplicitParams({ @ApiImplicitParam(name = "token", value = "令牌", required = true)})
	public ResponseBean<UploadResponse> importOrder(@ApiParam(value = "上传的文件", required = true)
														@RequestParam(value = "file", required = false) MultipartFile file,
													@RequestParam(value = "token", required = true) String token) {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集

		try{
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			User user = ShiroUtils.getUserByToken(token);

			resultMap = purchaseSdOrderService.importOrder(data, user) ;

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

		}catch(Exception e){
        	LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@PostMapping("/delSdOrder.asyn")
	@ApiOperation("删除SD单")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "SD单id,多个以‘，’隔开", required = true),
	})
	public ResponseBean delSdOrder(@RequestParam("ids")String ids) {
		try {

			if(StringUtils.isBlank(ids)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009) ;
			}

			purchaseSdOrderService.delSdOrder(ids) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
}

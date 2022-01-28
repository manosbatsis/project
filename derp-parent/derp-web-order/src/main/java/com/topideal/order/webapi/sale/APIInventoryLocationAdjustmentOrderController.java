package com.topideal.order.webapi.sale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderItemDTO;
import com.topideal.order.service.sale.InventoryLocationAdjustmentOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.InventoryLocationAdjustmentOrderForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi 库位调整单
 * 
 */
@RestController
@RequestMapping("/webapi/order/inventoryLocationAdjustment")
@Api(tags = "库位调整单")
public class APIInventoryLocationAdjustmentOrderController {

	private static final String[] MAIN_COLUMNS = {"单据类型","仓库", "事业部","客户名称","归属日期", "调整原因", "平台订单号", "调增商品货号",
			"调减商品货号", "调整数量", "库存类型","平台编码","店铺编码"} ;

	private static final String[] MAIN_KEYS = {"typeLabel","depotName", "buName","customerName","ownDate","remark", "externalCode", "increaseGoodsNo",
			 "reduceGoodsNo", "adjustNum", "inventoryTypeLabel","platformCode","shopCode"} ;
	// 库位调整单service
	@Autowired
	private InventoryLocationAdjustmentOrderService inventoryLocationAdjustmentOrderService;


	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "库位调整单列表信息")   	
   	@PostMapping(value="/listInventoryLocationAdjust.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<InventoryLocationAdjustmentOrderDTO> listInventoryLocationAdjust(InventoryLocationAdjustmentOrderForm form, HttpSession session) {
		InventoryLocationAdjustmentOrderDTO dto = new InventoryLocationAdjustmentOrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			if(StringUtils.isNotBlank(form.getBuId())) {
				dto.setBuId(Long.valueOf(form.getBuId()));
			}
			if(StringUtils.isNotBlank(form.getType())) {
				dto.setType(form.getType());				
			}
			if(form.getCustomerId()!=null) {
				dto.setCustomerId(form.getCustomerId());				
			}

			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			// 响应结果集
			dto = inventoryLocationAdjustmentOrderService.listInventoryLocationAdjust(dto,user);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 导入
	 * */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importInventoryLocationAdjust.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importInventoryLocationAdjust(String token,
											@ApiParam(value = "上传的文件", required = true)
											@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
			if(file==null){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
			if(data == null){//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			resultMap = inventoryLocationAdjustmentOrderService.saveInventoryLocationAdjust(data,user);
			
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse); 
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 获取导出的数量
	 */
	@ApiOperation(value = "获取导出的数量")
	@ApiResponses({
		@ApiResponse(code=10000,message = "data = > 导出的库位调整单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(InventoryLocationAdjustmentOrderForm form) throws Exception{
		Integer result = 0;
		try{
			InventoryLocationAdjustmentOrderDTO dto = new InventoryLocationAdjustmentOrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setIds(form.getIds());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			if(StringUtils.isNotBlank(form.getBuId())) {
				dto.setBuId(Long.valueOf(form.getBuId()));
			}
			if(StringUtils.isNotBlank(form.getType())) {
				dto.setType(form.getType());				
			}
			if(form.getCustomerId()!=null) {
				dto.setCustomerId(form.getCustomerId());				
			}
			// 响应结果集
			result = inventoryLocationAdjustmentOrderService.getOrderCount(dto);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result); 
	}

	/**
	 * 导出
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportInventoryLocationAdjust.asyn")
	private ResponseBean exportInventoryLocationAdjust(HttpServletResponse response, HttpServletRequest request,InventoryLocationAdjustmentOrderForm form) throws Exception{
		InventoryLocationAdjustmentOrderDTO dto = new InventoryLocationAdjustmentOrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setIds(form.getIds());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));
			}
			if(StringUtils.isNotBlank(form.getBuId())) {
				dto.setBuId(Long.valueOf(form.getBuId()));
			}
			if(form.getCustomerId()!=null) {
				dto.setCustomerId(form.getCustomerId());				
			}
			dto.setType(form.getType());

			List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
	
			// 响应结果集
			List<InventoryLocationAdjustExportDTO> mainList = inventoryLocationAdjustmentOrderService.getExportMainList(dto,user);
			String mainSheetName = "库位调整单";
	
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
			sheetList.add(mainSheet) ;
	
			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="/delInventoryLocationAdjust.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delInventoryLocationAdjust(String token,String ids) {
		try{
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if(!isRight){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//未知异常
			}
			if(StringUtils.isBlank(ids)) {
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			List list = StrUtils.parseIds(ids);
			boolean b = inventoryLocationAdjustmentOrderService.delInventoryLocationAdjust(list);
			if(!b){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");//未知异常
			}
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
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
	public ResponseBean<InventoryLocationAdjustmentOrderDTO> toDetailsPage(String token,Long id)throws Exception{
		InventoryLocationAdjustmentOrderDTO inventoryLocationAdjustmentOrderDTO = new InventoryLocationAdjustmentOrderDTO();
		try {
			inventoryLocationAdjustmentOrderDTO = inventoryLocationAdjustmentOrderService.searchDetail(id);
			List<InventoryLocationAdjustmentOrderItemDTO> itemList = inventoryLocationAdjustmentOrderService.listItemByOrderId(id);
			inventoryLocationAdjustmentOrderDTO.setItemList(itemList);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,inventoryLocationAdjustmentOrderDTO);
	}

	/**
	 * 审核
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditInventoryLocationAdjust.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditInventoryLocationAdjust(String token,String ids) {
		try {
			//校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if(!isRight){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);//数据为空
			}
			if(StringUtils.isBlank(ids)) {
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			List<Long> list = StrUtils.parseIds(ids);
			User user= ShiroUtils.getUserByToken(token);
			inventoryLocationAdjustmentOrderService.auditInventoryLocationAdjust(list,user) ;
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
}

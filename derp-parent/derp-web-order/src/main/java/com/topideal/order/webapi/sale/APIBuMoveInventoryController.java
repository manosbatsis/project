package com.topideal.order.webapi.sale;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.topideal.entity.dto.sale.BuMoveInventoryDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryExportDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryItemDTO;
import com.topideal.order.service.sale.BuMoveInventoryService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.BuMoveInventoryForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi事业部移库单
 */
@RestController
@RequestMapping("/webapi/order/buMoveInventory")
@Api(tags = "事业部移库单")
public class APIBuMoveInventoryController {
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(APIBuMoveInventoryController.class);

	private static final String[] COLUMNS = { "移库单号", "来源业务单号","来源单据类别", "移出事业部", "移入事业部", "仓库",
			 "移库日期","海外理货单位", "商品货号", "商品条码", "商品名称","移库币种", "协议单价","移库数量","库存类型", "移库状态",
			 "创建人","创建日期","审核人","审核时间"};

	private static final String[] KEYS = { "code", "businessNo","orderTypeLabel", "outBuName", "inBuName", "depotName",
			"moveDate","tallyingUnitLabel", "goodsNo", "barcode", "goodsName", "currencyLabel","agreementPrice","num","typeLabel","statusLabel",
			"createName","createDate","auditName","auditDate"};

	// 事业部移库单service
	@Autowired
	private BuMoveInventoryService buMoveInventoryService;

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询事业部移库列表")	
	@PostMapping(value="/listBuMoveInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<BuMoveInventoryDTO> listBuMoveInventory(BuMoveInventoryForm form, HttpSession session) {
		BuMoveInventoryDTO dto =  new BuMoveInventoryDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setBusinessNo(form.getBusinessNo());
			dto.setStatus(form.getStatus());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));				
			}
			if(StringUtils.isNotBlank(form.getInBuId())) {
				dto.setInBuId(Long.valueOf(form.getInBuId()));				
			}
			if(StringUtils.isNotBlank(form.getOutBuId())) {
				dto.setOutBuId(Long.valueOf(form.getOutBuId()));				
			}
			dto.setCreateStartDate(form.getCreateStartDate());
			dto.setCreateEndDate(form.getCreateEndDate());
			dto.setMoveStartDate(form.getMoveStartDate());
			dto.setMoveEndDate(form.getMoveEndDate());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = buMoveInventoryService.listBuMoveInventoryByPage(dto,user);
			
		}catch(Exception e){
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
		@ApiImplicitParam(name = "id", value = "事业部移库单id", required = true)
	})
	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<BuMoveInventoryDTO> toDetailsPage(String token,Long id)throws Exception{
		BuMoveInventoryDTO buMoveInventoryDTO  = new BuMoveInventoryDTO();
		try {
			buMoveInventoryDTO = buMoveInventoryService.searchDetail(id);
			List<BuMoveInventoryItemDTO> itemList = buMoveInventoryService.listItemByOrderId(id);
			buMoveInventoryDTO.setItemList(itemList);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,buMoveInventoryDTO);
	}
	
	
	@ApiOperation(value = "获取导出的事业部移库单的数量")	
	@ApiResponses({
		@ApiResponse(code = 10000,message = "data => 导出的事业部移库单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,BuMoveInventoryForm form) throws Exception{
		Integer total = 0;
		try{
			BuMoveInventoryDTO dto =  new BuMoveInventoryDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setBusinessNo(form.getBusinessNo());
			dto.setStatus(form.getStatus());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));				
			}
			if(StringUtils.isNotBlank(form.getInBuId())) {
				dto.setInBuId(Long.valueOf(form.getInBuId()));				
			}
			if(StringUtils.isNotBlank(form.getOutBuId())) {
				dto.setOutBuId(Long.valueOf(form.getOutBuId()));				
			}
			dto.setCreateStartDate(form.getCreateStartDate());
			dto.setCreateEndDate(form.getCreateEndDate());
			dto.setMoveStartDate(form.getMoveStartDate());
			dto.setMoveEndDate(form.getMoveEndDate());
			dto.setIds(form.getIds());
			// 响应结果集
			List<BuMoveInventoryDTO> result = buMoveInventoryService.listBuMoveInventoryDTO(dto,user);
			total = result.size();
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportBuMoveInventory.asyn")
	private ResponseBean exportBuMoveInventory(HttpSession session, HttpServletResponse response, HttpServletRequest request,BuMoveInventoryForm form) throws Exception{
		try {
			BuMoveInventoryDTO dto =  new BuMoveInventoryDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setBusinessNo(form.getBusinessNo());
			dto.setStatus(form.getStatus());
			if(StringUtils.isNotBlank(form.getDepotId())) {
				dto.setDepotId(Long.valueOf(form.getDepotId()));				
			}
			if(StringUtils.isNotBlank(form.getInBuId())) {
				dto.setInBuId(Long.valueOf(form.getInBuId()));				
			}
			if(StringUtils.isNotBlank(form.getOutBuId())) {
				dto.setOutBuId(Long.valueOf(form.getOutBuId()));				
			}
			dto.setCreateStartDate(form.getCreateStartDate());
			dto.setCreateEndDate(form.getCreateEndDate());
			dto.setMoveStartDate(form.getMoveStartDate());
			dto.setMoveEndDate(form.getMoveEndDate());
			dto.setIds(form.getIds());
			
			String sheetName = "移库单导出模板";
			// 获取导出的信息
			List<BuMoveInventoryExportDTO> result = buMoveInventoryService.getDetailsByExport(dto,user);
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
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
	@PostMapping(value="/delBuMoveInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delBuMoveInventory(String token,String ids) {
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
			List list = StrUtils.parseIds(ids);
			boolean b = buMoveInventoryService.delBuMoveInventory(list);
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
	 * 导入
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importBuMoveInventory.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean importBuMoveInventory(String token,
											@ApiParam(value = "上传的文件", required = true)
											@RequestParam(value = "file", required = false) MultipartFile file,
											HttpSession session) throws IOException {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),file.getOriginalFilename(), 2);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			resultMap = buMoveInventoryService.saveImportBuMoveInventory(data, user.getId(), user.getName(),
					user.getMerchantId(), user.getMerchantName(),user.getTopidealCode());
			
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
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

	@ApiOperation(value = "审核")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditBuMoveInventory.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean auditBuMoveInventory(String token,String ids) {
		try {
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
			User user= ShiroUtils.getUserByToken(token);
			buMoveInventoryService.auditBuMoveInventory(list,user) ;
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

}

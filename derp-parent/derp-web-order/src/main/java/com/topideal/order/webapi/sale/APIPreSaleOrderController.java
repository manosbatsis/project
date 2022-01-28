package com.topideal.order.webapi.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.constant.DERP_SYS;
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
import com.topideal.entity.dto.sale.PreSaleOrderCorrelationDTO;
import com.topideal.entity.dto.sale.PreSaleOrderDTO;
import com.topideal.entity.dto.sale.PreSaleOrderItemDTO;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.sale.PreSaleOrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.PreSaleOrderResponseDTO;
import com.topideal.order.webapi.sale.form.PreSaleOrderForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi 预售单 
 *
 */
@RequestMapping("/webapi/order/preSale")
@RestController
@Api(tags = "预售单")
public class APIPreSaleOrderController {
	// 预售单service
	@Autowired
	private PreSaleOrderService preSaleOrderService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;
	
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询预售单列表信息")   	
   	@PostMapping(value="/listPreSaleOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<PreSaleOrderDTO> listPreSaleOrder(PreSaleOrderForm form, HttpSession session) {
		PreSaleOrderDTO dto = new PreSaleOrderDTO(); 
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setState(form.getState());
			dto.setPoNo(form.getPoNo());
			dto.setCreateDateStartDate(form.getCreateDateStartDate());
			dto.setCreateDateEndDate(form.getCreateDateEndDate());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			// 响应结果集
			dto = preSaleOrderService.listPreSaleOrderByPage(dto,user);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 删除
	 * */
	@ApiOperation(value = "删除") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
   	@PostMapping(value="/delPreSaleOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delPreSaleOrder(String token,String ids) {
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
			boolean b = preSaleOrderService.delPreSaleOrder(list);
			if(!b){
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");
			}
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "查询详情") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<PreSaleOrderDTO> toDetailsPage(String token, Long id)throws Exception{
		PreSaleOrderDTO preSaleOrderDTO = new PreSaleOrderDTO();
		try {
			preSaleOrderDTO = preSaleOrderService.searchDetail(id);
			List<PreSaleOrderItemDTO> itemList = preSaleOrderService.listItemByOrderId(id);
			preSaleOrderDTO.setItemList(itemList);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, preSaleOrderDTO);
	}
	/**
	 * 获取导出预售单的数量
	 */
	@ApiOperation(value = "获取导出预售单的数量") 
	@ApiResponses({
		@ApiResponse(code=10000,message=" data => 需要导出的预售单的数量")
	})
   	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,PreSaleOrderForm form) throws Exception{
		Integer total = 0;
		try{
			PreSaleOrderDTO dto = new PreSaleOrderDTO(); 
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setState(form.getState());
			dto.setPoNo(form.getPoNo());
			dto.setCreateDateStartDate(form.getCreateDateStartDate());
			dto.setCreateDateEndDate(form.getCreateDateEndDate());
			dto.setIds(form.getIds());
			
			// 响应结果集
			List<PreSaleOrderDTO> result = preSaleOrderService.listPreSaleOrder(dto,user);
			total = result.size();
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	/**
	 * 导出预售单
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportPreSaleOrder.asyn")
	private ResponseBean exportPreSaleOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request, PreSaleOrderForm form) throws Exception{
		try {
			PreSaleOrderDTO dto = new PreSaleOrderDTO(); 
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setCustomerId(StringUtils.isNotBlank(form.getCustomerId()) ? Long.valueOf(form.getCustomerId()) : null);
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setState(form.getState());
			dto.setPoNo(form.getPoNo());
			dto.setCreateDateStartDate(form.getCreateDateStartDate());
			dto.setCreateDateEndDate(form.getCreateDateEndDate());
			dto.setIds(form.getIds());
			
			// 响应结果集
			List<PreSaleOrderDTO> result = preSaleOrderService.listPreSaleOrder(dto,user);
			List<PreSaleOrderItemDTO> itemList = new ArrayList<PreSaleOrderItemDTO>();
			for(PreSaleOrderDTO preSaleOrderDTO:result){
				List<PreSaleOrderItemDTO> itemList1 = preSaleOrderService.listItemByOrderId(preSaleOrderDTO.getId());
				for(PreSaleOrderItemDTO item:itemList1){
					item.setOrderCode(preSaleOrderDTO.getCode());
				}
				if(itemList1 != null && itemList1.size()>0){
					itemList.addAll(itemList1);
				}
			}
			String fileName = "预售单导出模板";
			String[] columns={"公司","事业部","预售单号","客户","销售类型","出仓仓库","PO号","销售币种","理货单位","创建人","审核人","审核时间"};
			String[] keys = {"merchantName", "buName", "code", "customerName", "businessModelLabel", "outDepotName", "poNo", "currencyLabel", "tallyingUnitLabel", "createName", "auditName", "auditDate"} ;

			String[] columns1={"预售单号","商品货号","商品名称","条码","预售数量","预售单价(不含税)","预售总金额(不含税)","税率","税额","预售单价(含税)","预售总金额(含税)","品牌"};
			String[] keys1={"orderCode","goodsNo","goodsName","barcode","num","price","amount","taxRate","tax","taxPrice","taxAmount","brandName"};
			//生成表格

			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("预售单信息", columns, keys, result);
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("预售单商品信息", columns1, keys1, itemList);

			List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;

			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
	}
	
	/**
	 * 新增/修改(仅保存)
	 * */
	@ApiOperation(value = "新增/修改(仅保存)") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "预售单据信息json串", required = true)
	})
   	@PostMapping(value="/saveOrModifyTempOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean saveOrModifyTempOrder(String json,String token) {
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			boolean b = preSaleOrderService.saveOrModifyTempOrder(json,user.getId(),user.getName(), user.getTopidealCode());
			if(!b){
				return  WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"保存失败");
			}
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 提交并审核
	 * */
	@ApiOperation(value = "提交并审核") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "预售单据信息json串", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message=" data => 审核成功/失败信息")
	})
   	@PostMapping(value="/addPreSaleOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> addPreSaleOrder(String json,String token) {
		String msg = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			msg = preSaleOrderService.addPreSaleOrder(json,user.getId(),user.getName(), user.getTopidealCode());
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}
	/**
	 * 访问编辑页面
	 * */
	@ApiOperation(value = "访问编辑页面") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/toEditPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<PreSaleOrderResponseDTO> toEditPage(String token,Long id, HttpSession session)throws Exception{
		PreSaleOrderResponseDTO responseDTO = new PreSaleOrderResponseDTO();
		try {
			PreSaleOrderDTO preSaleOrderDTO = preSaleOrderService.searchDetail(id);
			List<PreSaleOrderItemDTO> itemList = preSaleOrderService.listItemByOrderId(id);
			preSaleOrderDTO.setItemList(itemList);
			DepotInfoMongo outDepot = depotInfoService.getDetails(preSaleOrderDTO.getOutDepotId());

			int isTallyingRequired = 0;	// 理货单位是否必填
			// 若是海外仓，理货单位必填
			if(outDepot!=null && DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType())){
				isTallyingRequired = 1;
			} 
			responseDTO.setPreSaleOrderDTO(preSaleOrderDTO);
			responseDTO.setItemCount(itemList.size());
			responseDTO.setIsTallyingRequired(isTallyingRequired);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}
	/**
	 * 修改并审核
	 * */
	@ApiOperation(value = "修改并审核") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "预售单据信息json串", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message=" data => 审核成功/失败信息")
	})
   	@PostMapping(value="/modifyPreSaleOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> modifyPreSaleOrder(String json, String token) {
		String msg=null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			msg = preSaleOrderService.modifyPreSaleOrder(json,user.getId(),user.getName(),user.getTopidealCode());
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}

	/**
	 * 校验预售单能否转成销售单
	 * */
	@ApiOperation(value = "校验预售单能否转成销售单") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(id和code必须有一个不为空)", required = false),
		@ApiImplicitParam(name = "codes", value = "预售单单号(id和code必须有一个不为空)", required = false)
	})
   	@PostMapping(value="/checkPreSale.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<List<PreSaleOrderCorrelationDTO>> checkPreSale(String token,String ids,String codes) {
		List<PreSaleOrderCorrelationDTO> list = null;
		try{
			if(StringUtils.isBlank(ids) && StringUtils.isBlank(codes)){
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"id和code必须有一个不为空");
        	}
			// 响应结果集
			list = preSaleOrderService.checkPreSale(ids,codes);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,list);
	}

	/**
	 * 校验能否转成采购订单
	 */
	@ApiOperation(value = "校验能否转成采购订单") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/checkOrderState.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<PreSaleOrderDTO> checkOrderState(String token,Long id) {
		PreSaleOrderDTO dto= null;
		try{
			// 响应结果集
			dto = preSaleOrderService.checkOrderState(id);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 导入预售单
	 * */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importPreSale.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importPreSale(String token,
									@ApiParam(value = "上传的文件", required = true)
									@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
		try{
			Map resultMap = new HashMap();//返回的结果集
			if(file==null){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}
			Map<Integer,List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(), file.getOriginalFilename(), 2);
			if(data == null){//数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			resultMap = preSaleOrderService.saveImportPreSaleOrder(data,user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName(), user.getTopidealCode(),user.getRelMerchantIds());
			
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse); 
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	/**
	 * 根据PO号判断是否存在采购单
	 * */
	@ApiOperation(value = "根据PO号判断是否存在采购单") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "poNo", value = "PO号", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message=" data => 是否存在采购单，00：不存在，01：存在")
	})
   	@PostMapping(value="/checkExistPurchaseByPO.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<String> checkExistPurchaseByPO(String token,String poNo) {
		String result = "";
		try{
			Map<String,String> data=new HashMap<String,String>();
			// 响应结果集
			User user= ShiroUtils.getUserByToken(token);
			data = preSaleOrderService.checkExistPurchaseByPO(poNo, user);			
			result = data.get("code");
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}
	/**
	 * 列表页面 生成采购订单
	 * @param json
	 * @param buId
	 * @param depotId
	 * @return
	 */
	@ApiOperation(value = "生成采购订单") 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "预售单信息json串", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message=" data => 生成的采购单id")
	})
   	@PostMapping(value="/GeneratePurchaseOrder.asyn")
	public ResponseBean<Long> GeneratePurchaseOrder(String token,String json) {
		Long id = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			id = preSaleOrderService.GeneratePurchaseOrder(json,user);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,id);
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
			Boolean flag = preSaleOrderService.getPurchasePriceManage(id,supplierId,user);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, flag);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
	}
	@ApiOperation(value = "转采购获取采购价格")
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
			result = preSaleOrderService.getPurchasePrice(id,supplierId,user);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, result);
	}
}

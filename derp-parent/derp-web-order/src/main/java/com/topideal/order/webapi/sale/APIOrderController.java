package com.topideal.order.webapi.sale;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.*;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.timer.FileTaskService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
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
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderHistoryDTO;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.dto.sale.WayBillItemDTO;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.order.service.sale.OrderService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.OrderGoodsInfoNumDTO;
import com.topideal.order.webapi.sale.dto.OrderResponseDTO;
import com.topideal.order.webapi.sale.form.OrderForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi 电商订单 
 * 
 */
@RestController
@RequestMapping("/webapi/order/order")
@Api(tags = "电商订单")
public class APIOrderController {

	// 电商订单service
	@Autowired
	private OrderService orderService;
	@Autowired
	private FileTaskService fileTaskService;
	
	
	@ApiOperation(value = "事业待补录列表导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/businessUnitRecordExport.asyn")
	private void businessUnitRecordExport(HttpSession session, HttpServletResponse response, HttpServletRequest request, OrderForm form) throws Exception{
		
		OrderDTO dto = new OrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken()); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setExternalCode(form.getExternalCode());
			dto.setDepotId(StringUtils.isNotBlank(form.getDepotId()) ? Long.valueOf(form.getDepotId()) : null);
			dto.setStorePlatformName(form.getStorePlatformName());
			dto.setShopCode(form.getShopCode());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setBarcode(form.getBarcode());
			String itemIds = form.getItemIds();
			if (StringUtils.isNotBlank(itemIds)) {
				List  itemIdsList=new ArrayList<>();
				String[] Ids = itemIds.split(",");
				for(String id:Ids){
					if(!StringUtils.isEmpty(id)) itemIdsList.add(Long.valueOf(id));
				}
				dto.setItemIdsList(itemIdsList);
			}
			//获取数据
			List<OrderDTO> result = orderService.businessUnitRecordExport(dto);

			String sheetName = "事业部补录";
	        String[] columns={"外部订单号","平台","店铺名称","出仓仓库","商品货号","商品条码","商品名称","发货时间"};
	        String[] keys={"externalCode","storePlatform_name","shopName","depotName","goodsNo", "barcode" ,"goodsName","deliverDate"};
	        //生成表格
	        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			

		} catch(Exception e){
			e.printStackTrace();
			//return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		//return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
		
	}
	
	/**
	 * 获取事业部补录列表分页数据
	 * */
	@ApiOperation(value = "获取事业部补录列表信息")   	
   	@PostMapping(value="/listBusinessUnitRecord.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<OrderDTO> listBusinessUnitRecord(OrderForm form, HttpSession session) {
		OrderDTO dto = new OrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken()); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setExternalCode(form.getExternalCode());
			dto.setDepotId(StringUtils.isNotBlank(form.getDepotId()) ? Long.valueOf(form.getDepotId()) : null);
			dto.setStorePlatformName(form.getStorePlatformName());
			dto.setShopCode(form.getShopCode());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setBarcode(form.getBarcode());;
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = orderService.listBusinessUnitRecordByPage(dto);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 查看某个商品的详情
	 * */
	@ApiOperation(value = "查看某个商品的详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
	@PostMapping(value="/listItemDetailsById.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<OrderDTO> listItemDetailsById(String token,Long id)throws Exception{
		OrderDTO dto =new OrderDTO();
		try{
			List<OrderItemDTO> itemList = new  ArrayList<OrderItemDTO>();
			dto = orderService.searchDtoDetail(id);
			itemList = orderService.listItemByOrderId(id);
			dto.setItemList(itemList);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
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
	public ResponseBean<OrderResponseDTO> toDetailsPage(String token, Long id)throws Exception{
		OrderResponseDTO responseDTO = new OrderResponseDTO();
		try {
			OrderDTO dto = orderService.searchDtoDetail(id);
			if(dto==null){// 查询历史表
				OrderHistoryDTO orderHistoryDTO = orderService.searchHistoryDtoDetail(id);
				dto = new OrderDTO();
				BeanUtils.copyProperties(orderHistoryDTO,dto);
			}
			List<OrderItemDTO> itemList = orderService.listItemByOrderId(id);
			if(itemList == null){
				itemList = new ArrayList<OrderItemDTO>();
			}
			List<WayBillItemDTO> wayBillList = orderService.listWayBillItemByOrderId(id);
			dto.setItemList(itemList);
			// 脱敏信息 加****
			
			dto.setMakerName(StrUtils.desensitization(dto.getMakerName()));// 订购人
			dto.setMakerRegisterNo(StrUtils.desensitization(dto.getMakerRegisterNo()));//注册号
			dto.setMakerTel(StrUtils.desensitization(dto.getMakerTel()));//电话号码
			dto.setReceiverName(StrUtils.desensitization(dto.getReceiverName()));//收件人：
			dto.setReceiverTel(StrUtils.desensitization(dto.getReceiverTel()));//收件人电话
			dto.setReceiverAddress(StrUtils.desensitization(dto.getReceiverAddress()));//收件人地址
			
			responseDTO.setOrderDTO(dto);
			responseDTO.setWayBillList(wayBillList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询电商订单列表信息")   	
   	@PostMapping(value="/listOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<OrderDTO> listOrder(OrderForm form, HttpSession session) {
		OrderDTO dto = new OrderDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken()); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setExternalCode(form.getExternalCode());
			dto.setDepotId(StringUtils.isNotBlank(form.getDepotId()) ? Long.valueOf(form.getDepotId()) : null);
			dto.setStorePlatformName(form.getStorePlatformName());
			dto.setOrderSource(StringUtils.isNotBlank(form.getOrderSource()) ? Integer.valueOf(form.getOrderSource()) : null);
			dto.setStatus(form.getStatus());
			dto.setShopCode(form.getShopCode());
			dto.setTradingStartDate(form.getTradingStartDate());
			dto.setTradingEndDate(form.getTradingEndDate());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setOrderTimeRange(form.getOrderTimeRange());
			dto.setExOrderId(form.getExOrderId());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());			
			// 响应结果集
			dto = orderService.listOrderAndItemByPage(dto);
			
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 获取导出电商订单的数量
	 */
	@ApiOperation(value = "获取导出电商订单的数量")
	@ApiResponses({
			@ApiResponse(code = 10000,message="data = > 导出的电商订单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(OrderForm form,HttpSession session) {
		try{
			OrderDTO dto = new OrderDTO();
			User user= ShiroUtils.getUserByToken(form.getToken()); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setExternalCode(form.getExternalCode());
			dto.setDepotId(StringUtils.isNotBlank(form.getDepotId()) ? Long.valueOf(form.getDepotId()) : null);
			dto.setStorePlatformName(form.getStorePlatformName());
			dto.setOrderSource(StringUtils.isNotBlank(form.getOrderSource()) ? Integer.valueOf(form.getOrderSource()) : null);
			dto.setStatus(form.getStatus());
			dto.setShopCode(form.getShopCode());
			dto.setTradingStartDate(form.getTradingStartDate());
			dto.setTradingEndDate(form.getTradingEndDate());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
			dto.setOrderTimeRange(form.getOrderTimeRange());
			dto.setExOrderId(form.getExOrderId());
			dto.setIds(form.getIds());
			// 响应结果集
			int count = orderService.listOrder(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,count);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	/**
	 * 导出电商订单
	 * */
	@ApiOperation(value = "导出")
	@PostMapping(value="/exportOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean exportOrder(OrderForm form) throws Exception{
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());

			FileTaskMongo taskModel = new FileTaskMongo();
			taskModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_WJRW));
			taskModel.setTaskType(DERP_REPORT.FILETASK_TASKTYPE_DSDD);
			taskModel.setTaskName("电商订单导出");
			taskModel.setMerchantId(user.getMerchantId());
			taskModel.setOwnMonth(TimeUtils.formatMonth(TimeUtils.getNow()));
			taskModel.setState("1");
			taskModel.setRemark("电商订单导出");
			taskModel.setUsername(user.getName());
			JSONObject paramJson = new JSONObject();
			paramJson.put("merchantId", user.getMerchantId());
			paramJson.put("code", form.getCode());
			paramJson.put("externalCode", form.getExternalCode());
			paramJson.put("depotId", StringUtils.isNotBlank(form.getDepotId()) ? Long.valueOf(form.getDepotId()) : null);
			paramJson.put("storePlatformName", form.getStorePlatformName());
			paramJson.put("orderSource", StringUtils.isNotBlank(form.getOrderSource()) ? Integer.valueOf(form.getOrderSource()) : null);
			paramJson.put("status", form.getStatus());
			paramJson.put("shopCode", form.getShopCode());
			paramJson.put("tradingStartDate", form.getTradingStartDate());
			paramJson.put("tradingEndDate", form.getTradingEndDate());
			paramJson.put("deliverStartDate", form.getDeliverStartDate());
			paramJson.put("deliverEndDate", form.getDeliverEndDate());
			paramJson.put("orderTimeRange", form.getOrderTimeRange());
			paramJson.put("exOrderId", form.getExOrderId());
			paramJson.put("ids", form.getIds());
			taskModel.setParam(paramJson.toString());
			taskModel.setCreateDate(TimeUtils.formatFullTime());
			taskModel.setModule(DERP_REPORT.FILETASK_MODULE_2);
			taskModel.setUserId(user.getId());
			fileTaskService.save(taskModel);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
	}

	/**
	 * 电商订单导入
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="importOrder.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importOrder(String token,
							@ApiParam(value = "上传的文件", required = true)
							@RequestParam(value = "file", required = true) MultipartFile file) {
		try {
			Map<String , Object> resultMap = new HashMap<String , Object>();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}
			Map<Integer, List<List<Object>>> data = ExcelUtil.parseExcelIncludeNull(file.getInputStream(),
					file.getOriginalFilename(), 2);
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			User user= ShiroUtils.getUserByToken(token);
			resultMap = orderService.importOrder(user , data) ;
			
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
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
	 * 删除手工导入订单
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除手工导入订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="delOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delOrder(String token,String ids) {        
        try {
        	//校验id是否正确
        	boolean isRight = StrUtils.validateIds(ids);
        	if(!isRight){
        		//输入信息不完整
    			return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);//
        	}
        	
        	List<Long> list = StrUtils.parseIds(ids);
			int rows = orderService.delImportOrder(list);
			
			if(rows > 0) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");//未知异常
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 检查手工导入订单是否满足条件
	 * 
	 */
	@ApiOperation(value = "检查手工导入订单是否满足条件")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="checkConditionsOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<OrderGoodsInfoNumDTO>> checkConditionsOrder(String token,String ids) {		
        try {
        	User user= ShiroUtils.getUserByToken(token);
        	//校验id是否正确
        	boolean isRight = StrUtils.validateIds(ids);
        	if(!isRight){
        		//输入信息不完整
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
        	}
        	if(StringUtils.isBlank(ids)){
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
        	List<Long> list = StrUtils.parseIds(ids);
        	
			boolean flag = orderService.checkConditionsOrder(list) ;
			
			if(flag) {
				Map<String,Integer> map = orderService.getOrderGoodsInfo(list,user.getMerchantId()) ;
				List<OrderGoodsInfoNumDTO> goodsInfoNumList = new ArrayList<OrderGoodsInfoNumDTO>(); 
				for(Entry<String, Integer> entry : map.entrySet()) {            	
	            	OrderGoodsInfoNumDTO numDTO = new OrderGoodsInfoNumDTO();
	            	numDTO.setCode(entry.getKey());
	            	numDTO.setNum(entry.getValue());
	            	goodsInfoNumList.add(numDTO);
	            }				
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,goodsInfoNumList);
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"操作失败，所选项存在非手工导入订单") ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	
	/**
	 * 订单审核
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "订单审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="examineOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean examineOrder(String token,String ids) {        
        try {
        	//校验id是否正确
        	boolean isRight = StrUtils.validateIds(ids);
        	if(!isRight){
        		//输入信息不完整
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
        	if(StringUtils.isBlank(ids)){
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
        	List<Long> list = StrUtils.parseIds(ids);
        	
        	User user= ShiroUtils.getUserByToken(token);
			orderService.auditOrder(list , user.getTopidealCode(),user.getMerchantId()) ;
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}

	/**
	 * 事业部补录列表--批量更新
	 * @param info
	 * @param buId
	 * @param buName
	 * @return
	 */
	@ApiOperation(value = "事业部补录列表--批量更新")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "info", value = "唯一标识，由‘外部交易单号：商品ID’拼接，多个用逗号隔开", required = true),
		@ApiImplicitParam(name = "buId", value = "事业部id", required = true),
		@ApiImplicitParam(name = "buName", value = "事业部名称", required = true)
	})
	@ApiResponses({
		@ApiResponse(code=10000,message = "data => 记录批量更新成功/失败消息")
	})
	@PostMapping(value="updateBusinessUnitRecord.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> updateBusinessUnitRecord(String token,String info,Long buId,String buName) {
		String msg = "";//返回的结果集
        try {
        	List<String> list = StrUtils.parseIdsToStr(info);// 外部交易单号：商品ID
        	User user= ShiroUtils.getUserByToken(token);
        	msg = orderService.modifyBusinessUnitRecord(list ,buId, buName,user.getTopidealCode(),user.getMerchantId(),user.getId()) ;
        	if(StringUtils.isNotBlank(msg)) {
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),msg);
        	}
        } catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 获取店铺下拉信息
	 * */
	@ApiOperation(value = "获取店铺下拉信息",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE) 
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
   	@PostMapping(value = "/getShopInfo.asyn")
	public ResponseBean<List<MerchantShopRelMongo>> toPage(String token,Model model) throws Exception {
		List<MerchantShopRelMongo> shopList = null;
		try {
			User user= ShiroUtils.getUserByToken(token); 
			shopList = orderService.getMerchantShopRelList(user.getMerchantId());
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,shopList);
		
	}	
}

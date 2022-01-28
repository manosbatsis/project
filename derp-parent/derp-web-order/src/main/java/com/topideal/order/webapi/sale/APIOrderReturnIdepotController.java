package com.topideal.order.webapi.sale;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.*;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.sale.OrderReturnIdepotBatchDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotDTO;
import com.topideal.entity.dto.sale.OrderReturnIdepotItemDTO;
import com.topideal.order.service.sale.OrderReturnIdepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.dto.OrderReturnIdepotResponseDTO;
import com.topideal.order.webapi.sale.form.OrderReturnIdepotForm;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * webapi 电商订单退货
 *
 */
@RequestMapping("/webapi/order/orderReturnIdepot")
@RestController
@Api(tags = "电商订单退货")
public class APIOrderReturnIdepotController {

	// 电商订单service
	@Autowired
	private OrderReturnIdepotService  orderReturnIdepotService;

	/**
	 * 查看某个商品的详情
	 * */
	@ApiOperation(value = "查看某个商品的详情")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/listItemDetailsById.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<OrderReturnIdepotDTO> listItemDetailsById(String token,Long id)throws Exception{
		OrderReturnIdepotDTO orderReturnIdepotDTO =new OrderReturnIdepotDTO();
		try{
			List<OrderReturnIdepotItemDTO> itemList = new  ArrayList<OrderReturnIdepotItemDTO>();
			orderReturnIdepotDTO = orderReturnIdepotService.searchDetail(id);
			itemList = orderReturnIdepotService.listItemByOrderId(id);
			orderReturnIdepotDTO.setItemList(itemList);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,orderReturnIdepotDTO);
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
	public ResponseBean<OrderReturnIdepotResponseDTO> toDetailsPage(String token, Long id)throws Exception{
		OrderReturnIdepotResponseDTO  responseDTO = new OrderReturnIdepotResponseDTO();
		try {
			OrderReturnIdepotDTO orderReturnIdepotDTO = orderReturnIdepotService.searchDetail(id);
			List<OrderReturnIdepotItemDTO> itemList = orderReturnIdepotService.listItemByOrderId(id);
			orderReturnIdepotDTO.setItemList(itemList);

			if(itemList == null){
				itemList = new ArrayList<OrderReturnIdepotItemDTO>();
			}
			List<Long> itemIdList=new ArrayList<>();
			for (int i = 0; i < itemList.size(); i++) {
				itemIdList.add(itemList.get(i).getId());
			}
			List<OrderReturnIdepotBatchDTO> batchAllList  = orderReturnIdepotService.listOrderReturnBatchById(itemIdList);

			// 脱敏信息 加****
			orderReturnIdepotDTO.setBuyerName(StrUtils.desensitization(orderReturnIdepotDTO.getBuyerName()));
			orderReturnIdepotDTO.setBuyerPhone(StrUtils.desensitization(orderReturnIdepotDTO.getBuyerPhone()));
			orderReturnIdepotDTO.setReturnAddr(StrUtils.desensitization(orderReturnIdepotDTO.getReturnAddr()));

			responseDTO.setOrderReturnIdepotDTO(orderReturnIdepotDTO);
			responseDTO.setBatchAllList(batchAllList);

		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,responseDTO);
	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询电商退订单列表信息")
   	@PostMapping(value="/listOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<OrderReturnIdepotDTO> listOrder(OrderReturnIdepotForm form, HttpSession session) {
		OrderReturnIdepotDTO dto = new OrderReturnIdepotDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setExternalCode(form.getExternalCode());
			dto.setInDepotCode(form.getInDepotCode());
			dto.setReturnInDepotId(StringUtils.isNotBlank(form.getReturnInDepotId()) ? Long.valueOf(form.getReturnInDepotId()) : null);
			dto.setStorePlatformCode(form.getStorePlatformCode());
			dto.setCode(form.getCode());
			dto.setStatus(form.getStatus());
			dto.setReturnCode(form.getReturnCode());
			dto.setOrderReturnSource(StringUtils.isNotBlank(form.getOrderReturnSource()) ? Integer.valueOf(form.getOrderReturnSource()) : null);
			dto.setReturnInCreateStartDate(form.getReturnInCreateStartDate());
			dto.setReturnInCreateEndDate(form.getReturnInCreateEndDate());
			dto.setReturnInStartDate(form.getReturnInStartDate());
			dto.setReturnInEndDate(form.getReturnInEndDate());
			dto.setAfterSaleType(form.getAfterSaleType());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = orderReturnIdepotService.listOrderAndItemByPage(dto);

		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return  WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	/**
	 * 获取导出电商订单退货的数量
	 */
	@ApiOperation(value = "获取导出电商订单退货的数量")
	@ApiResponses({
		@ApiResponse(code = 10000,message="data => 需要导出的电商订单退货的数量")
	})
   	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(OrderReturnIdepotForm form,HttpSession session) {
		try{
			OrderReturnIdepotDTO dto = new OrderReturnIdepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setExternalCode(form.getExternalCode());
			dto.setInDepotCode(form.getInDepotCode());
			dto.setReturnInDepotId(StringUtils.isNotBlank(form.getReturnInDepotId()) ? Long.valueOf(form.getReturnInDepotId()) : null);
			dto.setStorePlatformCode(form.getStorePlatformCode());
			dto.setCode(form.getCode());
			dto.setStatus(form.getStatus());
			dto.setReturnCode(form.getReturnCode());
			dto.setOrderReturnSource(StringUtils.isNotBlank(form.getOrderReturnSource()) ? Integer.valueOf(form.getOrderReturnSource()) : null);
			dto.setReturnInCreateStartDate(form.getReturnInCreateStartDate());
			dto.setReturnInCreateEndDate(form.getReturnInCreateEndDate());
			dto.setReturnInStartDate(form.getReturnInStartDate());
			dto.setReturnInEndDate(form.getReturnInEndDate());
			dto.setAfterSaleType(form.getAfterSaleType());
			dto.setIds(form.getIds());
			// 响应结果集
			int count = orderReturnIdepotService.listOrder(dto);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,count);
		} catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
	/**
	 * 导出电商订单
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportOrder.asyn")
	public ResponseBean exportOrder(HttpSession session, HttpServletResponse response, HttpServletRequest request, OrderReturnIdepotForm form) throws Exception{
		try {
			OrderReturnIdepotDTO dto = new OrderReturnIdepotDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setExternalCode(form.getExternalCode());
			dto.setInDepotCode(form.getInDepotCode());
			dto.setReturnInDepotId(StringUtils.isNotBlank(form.getReturnInDepotId()) ? Long.valueOf(form.getReturnInDepotId()) : null);
			dto.setStorePlatformCode(form.getStorePlatformCode());
			dto.setCode(form.getCode());
			dto.setStatus(form.getStatus());
			dto.setReturnCode(form.getReturnCode());
			dto.setOrderReturnSource(StringUtils.isNotBlank(form.getOrderReturnSource()) ? Integer.valueOf(form.getOrderReturnSource()) : null);
			dto.setReturnInCreateStartDate(form.getReturnInCreateStartDate());
			dto.setReturnInCreateEndDate(form.getReturnInCreateEndDate());
			dto.setReturnInStartDate(form.getReturnInStartDate());
			dto.setReturnInEndDate(form.getReturnInEndDate());
			dto.setAfterSaleType(form.getAfterSaleType());
			dto.setIds(form.getIds());

			//获取数据
			List<Map<String,Object>> result = orderReturnIdepotService.getExportOrderMap(dto);

	        String sheetName = "电商退货订单导出模板";
	        String[] columns={"ERP系统订单号","原发货系统单号","来源交易订单","平台售后单号","平台名称","店铺名称","店铺类型","售后类型","单据类型","退货入库仓库",
	        		"退货创建时间","退款完结时间","退货入库时间","事业部","客户","商品名称","商品货号","商品条形码","标准条码","退货正品数量","退货残品数量",
	        		"退货总数量","实际退款金额","币种","批次号","生产日期","失效日期","买家姓名","买家手机","退货人地址","邮编","物流公司","物流运单号",
	        		"内贸商品退款税额","内贸商品退款金额（不含税）","库位类型"};
	        String[] keys={"code","order_code","external_code","return_code","storePlatformName", "shop_name","shopTypeName" ,"after_sale_type","return_order_type",
	        		"return_in_depot_name","return_in_create_date","refund_end_date","return_in_date","bu_name","customer_name","in_goods_name",
	        		"in_goods_no","barcode","commbarcode","batchReturnNum","batchBadGoodsNum","batchTotalNum", "refund_amount" ,"currency","batch_no",
	        		"production_date","overdue_date","buyer_name","buyer_phone","return_addr","postcode","logistics_name","logistics_way_bill_no",
	        		"trade_refund_tax","trade_refund_amount","stock_location_type_name"};
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "type", value = "导入类型，1-退货导入 2-退款导入", required = true)
	})
	@PostMapping(value="importOrder.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importOrder(String token,String type,
										@ApiParam(value = "上传的文件", required = true)
										@RequestParam(value = "file", required = true) MultipartFile file) {
		try {
			Map<String , Object> resultMap = new HashMap<String , Object>();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
			}

			List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			User user= ShiroUtils.getUserByToken(token);
			resultMap = orderReturnIdepotService.importOrder(user , data, type) ;

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
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
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
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
        	}
        	if(StringUtils.isBlank(ids)){
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
        	List<Long> list = StrUtils.parseIds(ids);
			int rows = orderReturnIdepotService.delImportOrder(list);
			if(rows > 0) {
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"删除失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	/**
	 * 检查手工导入订单是否满足条件（导入并且待入库）
	 *
	 */
	@ApiOperation(value = "检查手工导入订单是否满足条件")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="checkConditionsOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean checkConditionsOrder(String token,String ids) {
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
			boolean flag = orderReturnIdepotService.checkConditionsOrder(list) ;

			if(flag) {
				orderReturnIdepotService.getOrderGoodsInfo(list,user.getMerchantId()) ;
				return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
			}else {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"操作失败，所选项存在非手工导入订单") ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}

	@ApiOperation(value = "审核")
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
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
        	}
        	if(StringUtils.isBlank(ids)){
        		return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
        	}
        	List<Long> list = StrUtils.parseIds(ids);

        	User user= ShiroUtils.getUserByToken(token);
			orderReturnIdepotService.examineOrder(list , user.getTopidealCode(),user) ;

			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}

package com.topideal.order.webapi.sale;

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

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.SaleReturnOdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnOdepotItemDTO;
import com.topideal.order.service.sale.SaleReturnOdepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.SaleReturnOdepotForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi销售退货出库单
 * 
 */
@RequestMapping("/webapi/order/saleReturnOdepot")
@RestController
@Api(tags = "销售退货出库单")
public class APISaleReturnOdepotController {

	// 销售退货出库单service
	@Autowired
	private SaleReturnOdepotService saleReturnOdepotService;
	

	/**
	 * 访问详情页面
	 * */
	@ApiOperation(value = "查询详情信息")  
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/toDetailsPage.html",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleReturnOdepotDTO> toDetailsPage(String token,Long id)throws Exception{
		SaleReturnOdepotDTO saleReturnOdepot = new SaleReturnOdepotDTO();
		try {
			saleReturnOdepot = saleReturnOdepotService.searchDetail(id);
			List<SaleReturnOdepotItemDTO> itemList = saleReturnOdepotService.listItemByOrderId(id);
			saleReturnOdepot.setItemList(itemList);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,saleReturnOdepot);
		
	}
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询销售退货出库单列表信息")   	
   	@PostMapping(value="/listSaleReturnOdepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<SaleReturnOdepotDTO> listSaleReturnOdepot(SaleReturnOdepotForm form, HttpSession session) {
		SaleReturnOdepotDTO dto = new SaleReturnOdepotDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerName(form.getCustomerName());
			dto.setStatus(form.getStatus());
			dto.setOrderCode(form.getOrderCode());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setReturnOutStartDate(form.getReturnOutStartDate());
			dto.setReturnOutEndDate(form.getReturnOutEndDate());
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			
			// 响应结果集
			dto = saleReturnOdepotService.listSaleReturnOdepotByPage(dto,user);
			
		}catch(Exception e){
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	/**
	 * 获取导出销售退货出库清单的数量
	 */
	@ApiOperation(value = "获取导出销售退货出库清单的数量")
	@ApiResponses({
			@ApiResponse(code = 10000,message="data = > 导出销售退货出库清单的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleReturnOdepotForm form) throws Exception{
		Integer total = 0;
		try {
			SaleReturnOdepotDTO dto = new SaleReturnOdepotDTO();			
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerName(form.getCustomerName());
			dto.setStatus(form.getStatus());
			dto.setOrderCode(form.getOrderCode());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setReturnOutStartDate(form.getReturnOutStartDate());
			dto.setReturnOutEndDate(form.getReturnOutEndDate());
			dto.setIds(form.getIds());
			
			// 响应结果集
			List<SaleReturnOdepotDTO> result = saleReturnOdepotService.listSaleReturnOdepot(dto,user);
			total = result.size();
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}
	/**
	 * 导出销售退货出库清单
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportSaleReturnOdepot.asyn")
	private ResponseBean exportSaleReturnOdepot(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleReturnOdepotForm form) throws Exception{
		try {
			SaleReturnOdepotDTO dto = new SaleReturnOdepotDTO();			
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setCode(form.getCode());
			dto.setOutDepotId(StringUtils.isNotBlank(form.getOutDepotId()) ? Long.valueOf(form.getOutDepotId()) : null);
			dto.setCustomerName(form.getCustomerName());
			dto.setStatus(form.getStatus());
			dto.setOrderCode(form.getOrderCode());
			dto.setBuId(StringUtils.isNotBlank(form.getBuId()) ? Long.valueOf(form.getBuId()) : null);
			dto.setReturnOutStartDate(form.getReturnOutStartDate());
			dto.setReturnOutEndDate(form.getReturnOutEndDate());
			dto.setIds(form.getIds());
			
			// 响应结果集
			List<SaleReturnOdepotDTO> result = saleReturnOdepotService.listSaleReturnOdepot(dto,user);
			List<SaleReturnOdepotItemDTO> itemList = new ArrayList<SaleReturnOdepotItemDTO>();
			for(SaleReturnOdepotDTO sModel:result){
				List<SaleReturnOdepotItemDTO> itemList1 = saleReturnOdepotService.listItemByOrderId(sModel.getId());
				for(SaleReturnOdepotItemDTO item:itemList1){
					item.setSreturnOdepotCode(sModel.getCode());
				}
				if(itemList1 != null && itemList1.size()>0){
					itemList.addAll(itemList1);
				}
			}
			String sheetName = "销售退货出库清单";
	        String[] columns={"销退出库单号","单据状态","销退订单号","退出仓库","事业部","退入仓库","退货客户名称","合同号","销退出库时间"};
	        String[] keys = {"code", "statusLabel", "orderCode", "outDepotName", "buName", "inDepotName", "customerName", "contractNo", "returnOutDate"} ;
	        
	        String[] columns1={"销退出库单号","PO号","退出商品编号","退出商品货号","退出商品名称","退货出库数量","退出批次","生产日期","失效日期"};
	        String[] keys1 = {"sreturnOdepotCode", "poNo", "outGoodsCode", "outGoodsNo", "outGoodsName", "returnNum", "returnBatchNo", "productionDate", "overdueDate"} ;
	        //生成表格
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("基本信息", columns, keys, result) ;
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品信息", columns1, keys1, itemList) ;
			
			List<ExportExcelSheet> sheets = new ArrayList<>() ;
			sheets.add(mainSheet) ;
			sheets.add(itemSheet) ;
			
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
			
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="importSaleReturnOdepot.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSaleReturnOdepot(String token,
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
			resultMap = saleReturnOdepotService.importSaleReturnOdepot(user, data);

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
	 * 审核
	 * */
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/auditSaleReturnOdepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<UploadResponse> auditSaleReturnOdepot(String token,String ids, HttpSession session) {
		try{
			Map<String,Object> bl = null;
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
            User user= ShiroUtils.getUserByToken(token); 
			// 响应结果集
			bl = saleReturnOdepotService.auditSaleReturnOdepot(list,user);
			Integer success = (Integer)bl.get("success");
			Integer failure = (Integer)bl.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) bl.get("failureMsg");
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
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的单据ids(多选用逗号隔开)", required = true)
	})
	@PostMapping(value="/delSaleReturnOdepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSaleReturnOdepot(String token,String ids) {
		try{
			if(StringUtils.isBlank(ids)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017.getCode(),"至少选择一条单据记录"); 
			}
			saleReturnOdepotService.delSaleReturnOdepot(ids);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000); 
		
		}catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
}

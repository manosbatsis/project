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
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.MaterialOutDepotDTO;
import com.topideal.entity.dto.sale.MaterialOutDepotItemDTO;
import com.topideal.order.service.sale.MaterialOutDepotService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.MaterialOutDepotForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 领料出库单
 * @date 2021-04-02
 */
@RestController
@RequestMapping("/webapi/order/materialOutDepot")
@Api(tags = "领料出库单")
public class APIMaterialOutDepotController {
	/* 打印日志 */
	protected Logger logger = LoggerFactory.getLogger(APIMaterialOutDepotController.class);
	@Autowired
	private MaterialOutDepotService materialOutDepotService;
	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "查询领料出库单列表分页信息")   	
   	@PostMapping(value="/listMaterialOut.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	private ResponseBean<MaterialOutDepotDTO> listMaterialOrder(MaterialOutDepotForm form, HttpSession session) {
		MaterialOutDepotDTO dto = new MaterialOutDepotDTO();
		try{
			dto.setCode(form.getCode());
			dto.setMaterialOrderCode(form.getMaterialOrderCode());
			dto.setOutDepotId(StringUtils.isBlank(form.getOutDepotId()) ? null : Long.valueOf(form.getOutDepotId()));
			dto.setCustomerId(StringUtils.isBlank(form.getCustomerId()) ? null : Long.valueOf(form.getCustomerId()));
			dto.setBuId(StringUtils.isBlank(form.getBuId()) ? null : Long.valueOf(form.getBuId()));
			dto.setStatus(form.getStatus());
			dto.setPoNo(form.getPoNo());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setBegin(form.getBegin());
            dto.setPageSize(form.getPageSize());
            
            dto = materialOutDepotService.listDTOByPage(dto, user);
            
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);	
	}
	
	/**
	 * 详情
	 * */
	@ApiOperation(value = "详情获取单据信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "选中的单据id", required = true)
	})
   	@PostMapping(value="/getDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<MaterialOutDepotDTO> getDetail(String token,Long id) {
		MaterialOutDepotDTO dto = new MaterialOutDepotDTO();
		try{
			if(id == null){
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            dto = materialOutDepotService.searchDetail(id);
            List<MaterialOutDepotItemDTO> itemList = materialOutDepotService.getItemDetail(dto.getId());
            dto.setItemList(itemList);
          
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);	
	}	
	
	/**
	 * 获取导出数量
	 * */
	@ApiOperation(value = "获取导出数量")   	
   	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(MaterialOutDepotForm form, HttpSession session) {
		Integer total = null;
		try{
			MaterialOutDepotDTO dto = new MaterialOutDepotDTO();
			dto.setCode(form.getCode());
			dto.setMaterialOrderCode(form.getMaterialOrderCode());
			dto.setOutDepotId(StringUtils.isBlank(form.getOutDepotId()) ? null : Long.valueOf(form.getOutDepotId()));
			dto.setCustomerId(StringUtils.isBlank(form.getCustomerId()) ? null : Long.valueOf(form.getCustomerId()));
			dto.setBuId(StringUtils.isBlank(form.getBuId()) ? null : Long.valueOf(form.getBuId()));
			dto.setStatus(form.getStatus());
			dto.setPoNo(form.getPoNo());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setIds(form.getIds());
            List<MaterialOutDepotDTO> result = materialOutDepotService.listOrderDTO(dto, user);
            if(result != null) {
            	total = result.size();
            }
          
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);	
	}
	
	/**
	 * 导出
	 * */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)   	
   	@GetMapping(value="/exportMaterialOutDepot.asyn")
	private ResponseBean exportMaterialOutDepot(MaterialOutDepotForm form, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		try {
			MaterialOutDepotDTO dto = new MaterialOutDepotDTO();
			dto.setCode(form.getCode());
			dto.setMaterialOrderCode(form.getMaterialOrderCode());
			dto.setOutDepotId(StringUtils.isBlank(form.getOutDepotId()) ? null : Long.valueOf(form.getOutDepotId()));
			dto.setCustomerId(StringUtils.isBlank(form.getCustomerId()) ? null : Long.valueOf(form.getCustomerId()));
			dto.setBuId(StringUtils.isBlank(form.getBuId()) ? null : Long.valueOf(form.getBuId()));
			dto.setStatus(form.getStatus());
			dto.setPoNo(form.getPoNo());
			dto.setDeliverStartDate(form.getDeliverStartDate());
			dto.setDeliverEndDate(form.getDeliverEndDate());
            User user= ShiroUtils.getUserByToken(form.getToken());
            dto.setMerchantId(user.getMerchantId());
            dto.setIds(form.getIds());
            //表头
            List<MaterialOutDepotDTO> result = materialOutDepotService.listOrderDTO(dto, user);
            // 表体
            List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
            for(MaterialOutDepotDTO outDepotDto : result) {
            	List<MaterialOutDepotItemDTO> items = materialOutDepotService.getItemDetail(outDepotDto.getId());
            	if(items != null && items.size() > 0) {
            		for(MaterialOutDepotItemDTO itemDTO : items) {
            			Map<String,Object> itemMap = new HashMap<String, Object>();
            			itemMap.put("orderCode", outDepotDto.getCode());
            			itemMap.put("goodsNo", itemDTO.getGoodsNo());
            			itemMap.put("goodsName", itemDTO.getGoodsName());
            			itemMap.put("barcode", itemDTO.getBarcode());
            			itemMap.put("transferNum", itemDTO.getTransferNum());
            			itemMap.put("wornNum", itemDTO.getWornNum());
            			itemMap.put("transferBatchNo", itemDTO.getTransferBatchNo());
            			itemMap.put("productionDate", itemDTO.getProductionDate());
            			itemMap.put("overdueDate", itemDTO.getOverdueDate());
            			itemMap.put("tallyingUnit", itemDTO.getTallyingUnitLabel());
            			itemList.add(itemMap);
            		}
            	}
            }
            
            String[] MATERIAL_COLUMNS = {"领料出库单号","PO号","单据状态","商家名称","客户名称","出库仓库","事业部","领料单号","发货时间"};
            String[] MATERIAL_KEYS = {"code","poNo","statusLabel","merchantName","customerName","outDepotName","buName","materialOrderCode","deliverDate"};
            List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;			
			String mainSheetName = "领料出库单信息";
			ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MATERIAL_COLUMNS, MATERIAL_KEYS, result);
			sheetList.add(mainSheet) ;
			
			
			String[] ITEM_COLUMNS ={"领料出库单号","商品货号","商品名称","商品条形码","好品数量","坏品数量","批次号","生产日期","失效日期","理货单位"};
			String[] ITEM_KEYS = {"orderCode","goodsNo","goodsName","barcode","transferNum","wornNum","transferBatchNo","productionDate","overdueDate","tallyingUnit"};
			String itemSheetName = "商品信息";
			ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, ITEM_COLUMNS, ITEM_KEYS, itemList);
			sheetList.add(itemSheet) ;

			//生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, "领料出库单信息导出");
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		}catch(Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
	}
	@ApiOperation(value = "导入领料出库单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importMaterialOutDepot.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse>  importMaterialOutDepot(String token,
									@ApiParam(value = "上传的文件", required = true)
									@RequestParam(value = "file", required = true) MultipartFile file, HttpSession session) {
		try{
			Map resultMap = new HashMap();//返回的结果集
            if(file==null){
                //输入信息不完整
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10008);
            }
            List<List<Map<String, String>>> data = ExcelUtilXlsx.parseAllSheet(file.getInputStream());
			if(data == null){//数据为空
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
			User user= ShiroUtils.getUserByToken(token);
			resultMap = materialOutDepotService.importMaterialOutDepot(data,user);
			
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);			
        }catch(Exception e){
        	e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
	}
	
	@ApiOperation(value = "审核")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "单据id集合，多个用逗号隔开", required = true)
	})
   	@PostMapping(value="/auditMaterialOrder.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean auditMaterialOrder(String ids, String token) {
		try{
            if(ids == null || StringUtils.isBlank(ids)){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            materialOutDepotService.auditMaterialOutDepot(ids,user);
          
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);	
	}
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "单据id集合，多个用逗号隔开", required = true)
	})
   	@PostMapping(value="/delMaterialOutDepot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean delMaterialOutDepot(String ids, String token) {
		try{
            if(ids == null || StringUtils.isBlank(ids)){
                //输入信息不完整
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            User user= ShiroUtils.getUserByToken(token);
            materialOutDepotService.delMaterialOutDepot(ids,user);
          
        }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);	
	}
}

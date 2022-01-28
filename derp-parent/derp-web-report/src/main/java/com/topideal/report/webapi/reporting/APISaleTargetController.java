package com.topideal.report.webapi.reporting;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
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
import com.topideal.entity.dto.SaleTargetDTO;
import com.topideal.report.service.reporting.SaleTargetService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.SaleTargetForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/webapi/report/saleTarget")
@Api(tags = "商品销售目标")
public class APISaleTargetController {
	
	private static final Logger LOGGER = Logger.getLogger(APISaleTargetController.class) ;
	
	private static final String [] COLUMNS = new String[] {"事业部", "商品条码", "商品名称", "标准条码", "销售计划月份", "类型", 
										"To B销量", "To C销量", "平台名称", "平台计划销量", "店铺编码", "店铺名称", "店铺计划销量"} ;
	private static final String [] KEYS = new String[] {"buName", "barcode", "goodsName", "commbarcode", "month", "typeLabel", 
										"toBNum", "toCNum", "storePlatformNameLabel", "storePlatformNum", "shopCode", "shopName", "shopNum"} ;
	
	@Autowired
	private SaleTargetService saleTargetService ;
	
	@ApiOperation(value = "获取详情信息")   	
   	@PostMapping(value="/getDetail.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean<SaleTargetDTO> toDetailPage(SaleTargetForm form) throws SQLException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			SaleTargetDTO dto = new SaleTargetDTO();
			BeanUtils.copyProperties(form, dto);
			resultMap = saleTargetService.getSaleTargetDetails(dto) ;
		
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultMap);
		
	}
	
	@ApiOperation(value = "获取列表信息")   	
   	@PostMapping(value="listSaleTarget.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleTargetDTO> listSaleTarget(SaleTargetForm form) {
		SaleTargetDTO dto = new SaleTargetDTO();
		try {
			BeanUtils.copyProperties(form, dto);
			User user = ShiroUtils.getUserByToken(form.getToken());
			dto = saleTargetService.listSaleTarget(dto,user) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
	}
	
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="importSaleTarget.asyn", headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importSaleTarget(String token, @RequestParam(value = "file", required = false) MultipartFile file) throws SQLException {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			List<Map<String, String>> saleTypeData = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "按销售类型计划");
			List<Map<String, String>> platformData = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "按平台计划");
			List<Map<String, String>> shopData = ExcelUtilXlsx.parseSheetBySheetName(file.getInputStream(), "按店铺计划");
			
			if (saleTypeData == null && platformData == null && shopData == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			resultMap = saleTargetService.importSaleTarget(saleTypeData, platformData, shopData, user);
			
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
	
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "params", value = "导出条件，buId_month_type拼接合成", required = false)
	})
	@GetMapping(value="exportSaleTarget.asyn")
	public void exportSaleTarget(String token,String params,HttpServletRequest request ,HttpServletResponse response) throws Exception {
		
		String mainSheetName = "销售目标导出" ;
		//拆分前端导入的参数
		String[] arr = null;
		
		if(StringUtils.isNotBlank(params)) {
			arr = params.split(",") ;
		}else {
			arr = new String [] {} ;
		}
		User user = ShiroUtils.getUserByToken(token);
		List<SaleTargetDTO> exportList = saleTargetService.exportSaleTarget(arr,user) ;
		// 生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(mainSheetName, COLUMNS, KEYS, exportList) ;
		// 导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
	}
	
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "params", value = "删除条件，，buId_month_type拼接合成", required = true)
	})
	@PostMapping(value="delSaleTarget.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delSaleTarget(String token, String params) throws Exception {		
		try {
			if(StringUtils.isBlank(params)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}			
			String[] arr = params.split(",");
			saleTargetService.delSaleTarget(arr) ;
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
}

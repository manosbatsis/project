package com.topideal.order.webapi.platformdata;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.ImportMessage;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.UploadResponse;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.sale.PlatformMerchandiseInfoDTO;
import com.topideal.order.service.platformdata.PlatformMerchandiseService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.platformdata.form.PlatformMerchandiseInfoForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 京东商品管理
 **/
@RestController
@RequestMapping("/webapi/order/platformMerchandise")
@Api(tags = "platformMerchandise-平台商品管理")
public class APIPlatformMerchandiseInfoController {

	private static final Logger LOG = Logger.getLogger(APIPlatformMerchandiseInfoController.class) ;
	
    @Autowired
    private PlatformMerchandiseService platformMerchandiseService;

    /**
     * 获取分页数据
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "获取分页数据")
	@PostMapping("/listByPage.asyn")
    private ResponseBean<PlatformMerchandiseInfoDTO> ListByPage(PlatformMerchandiseInfoForm form) {
        try {
        	
        	if(StringUtils.isBlank(form.getToken())) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
        	
        	PlatformMerchandiseInfoDTO dto = new PlatformMerchandiseInfoDTO() ;
        	
        	dto.setBrand(form.getBrand());
        	dto.setPlatform(form.getPlatform());
        	dto.setCrawlerType(form.getCrawlerType()) ;
        	dto.setUpc(form.getUpc()) ;
        	dto.setWareId(form.getWareId()) ;
        	dto.setBegin(form.getBegin());
        	dto.setPageSize(form.getPageSize());
        	
            User user= ShiroUtils.getUserByToken(form.getToken()) ;
            dto.setMerchantId(user.getMerchantId());
            
            dto = platformMerchandiseService.getListByPage(dto);
            
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
            
        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
        }
    }

	/**
	 * 导入
	 */
    @SuppressWarnings("unchecked")
	@ApiOperation(value = "商品导入")
	@PostMapping(value="/platformMerchandiseImport.asyn", headers = "content-type=multipart/form-data")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
	public ResponseBean<UploadResponse> platformMerchandiseImport(@RequestParam(value = "token", required = true)String token,
			@ApiParam(value = "上传的文件", required = true)
			@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
    	
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			User user= ShiroUtils.getUserByToken(token);
			resultMap = platformMerchandiseService.savePlatformMerchandiseImport(data,user.getMerchantId());
			
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("errorList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
	}
    
	/**
	 * 导入
	 * @param
	 * @return int
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "箱规导入")
	@PostMapping(value="/cartonSizeImport.asyn", headers = "content-type=multipart/form-data")
    @ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
	public ResponseBean<UploadResponse> cartonSizeImport(@RequestParam(value = "token", required = true)String token,
			@ApiParam(value = "上传的文件", required = true)
			@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		
		try {
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			User user= ShiroUtils.getUserByToken(token);
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			
			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			
			resultMap = platformMerchandiseService.updateCartonSizeImport(data,user.getMerchantId());
			
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("message");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
        	return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
		
	}
    /**
     * 导出
     * @throws IOException
     */
    @GetMapping("/export.asyn")
    @ApiOperation(value = "导出")
    public void export(HttpServletResponse response, HttpServletRequest request,
    		PlatformMerchandiseInfoForm form) throws Exception {
    	
        String sheetName = "平台商品";
        String[] COLUMNS = { "平台", "归属账号","商品编码", "商品名称","条形码","品牌","单位","箱规"};
		String[] KEYS = {"crawlerTypeLabel", "userCode" , "wareId" , "name","upc","brand","unit","cartonSize"};
		
		PlatformMerchandiseInfoDTO dto = new PlatformMerchandiseInfoDTO() ;
    	
    	dto.setBrand(form.getBrand());
    	dto.setPlatform(form.getPlatform());
    	dto.setCrawlerType(form.getCrawlerType()) ;
    	dto.setUpc(form.getUpc()) ;
    	dto.setWareId(form.getWareId()) ;
    	
        User user= ShiroUtils.getUserByToken(form.getToken()) ;
        dto.setMerchantId(user.getMerchantId());
		
        // 获取导出的信息
        List<PlatformMerchandiseInfoDTO> list = platformMerchandiseService.getList(dto);
        // 生成表格
        SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list) ;
        // 导出文件
        FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
    }
}

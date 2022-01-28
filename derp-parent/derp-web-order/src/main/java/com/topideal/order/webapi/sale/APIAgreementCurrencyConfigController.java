package com.topideal.order.webapi.sale;

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
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigDTO;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigExportDTO;
import com.topideal.order.service.base.DepotInfoService;
import com.topideal.order.service.sale.AgreementCurrencyConfigService;
import com.topideal.order.shiro.ShiroUtils;
import com.topideal.order.webapi.sale.form.AgreementCurrencyConfigForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * webapi 协议单价
 * @date 2021-02-05
 */
@RestController
@RequestMapping("/webapi/order/agreementCurrencyConfig")
@Api(tags = "协议单价")
public class APIAgreementCurrencyConfigController {
	private static final String[] COLUMNS = { "公司","移入事业部","移出事业部",  "商品货号",
			"商品名称", "协议单价", "协议币种", "创建人", "创建时间"};

	private static final String[] KEYS = { "merchantName", "inBuName", "outBuName", "goodsNo", "goodsName",
			"price","currencyLabel", "createName", "createDate"};
	// 协议单价service
	@Autowired
	private AgreementCurrencyConfigService agreementCurrencyConfigService;
	// 仓库
	@Autowired
	private DepotInfoService depotInfoService;

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "协议单价列表查询")
	@PostMapping(value="/listAgreementCurrencyConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<AgreementCurrencyConfigDTO> listAgreementCurrencyConfig(AgreementCurrencyConfigForm form, HttpSession session) {
		AgreementCurrencyConfigDTO dto = new AgreementCurrencyConfigDTO();
		try{
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setGoodsName(form.getGoodsName());
			if(StringUtils.isNotBlank(form.getInBuId())) {
				dto.setInBuId(Long.valueOf(form.getInBuId()));				
			}
			if(StringUtils.isNotBlank(form.getOutBuId())) {
				dto.setOutBuId(Long.valueOf(form.getOutBuId()));
			}
			dto.setBegin(form.getBegin());
			dto.setPageSize(form.getPageSize());
			// 响应结果集
			dto = agreementCurrencyConfigService.listAgreementCurrencyByPage(dto,user);
			
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "ids", value = "选中的协议单价ids(多个用逗号隔开)", required = true)
	})
	@PostMapping(value="/delAgreementCurrencyConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delAgreementCurrencyConfig(String token,String ids) {
		try{
            //校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10011);
            }
            if(StringUtils.isBlank(ids)) {
            	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
            }
            List list = StrUtils.parseIds(ids);
            boolean b = agreementCurrencyConfigService.delAgreementCurrencyConfig(list);
            if(!b){
                return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,"删除失败");
            }
        }catch(Exception e){
        	e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
	/**
	 * 获取导出协议单价的数量
	 */
	@ApiOperation(value = "获取导出协议单价的数量")
	@ApiResponses({
		@ApiResponse(code=10000,message="data => 导出协议单价的数量")
	})
	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<Integer> getOrderCount(AgreementCurrencyConfigForm form) throws Exception{
		Integer total = 0;
		try{
			AgreementCurrencyConfigDTO dto =new AgreementCurrencyConfigDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setGoodsName(form.getGoodsName());
			dto.setIds(form.getIds());
			if(StringUtils.isNotBlank(form.getInBuId())) {
				dto.setInBuId(Long.valueOf(form.getInBuId()));				
			}
			if(StringUtils.isNotBlank(form.getOutBuId())) {
				dto.setOutBuId(Long.valueOf(form.getOutBuId()));
			}
			// 响应结果集
			List<AgreementCurrencyConfigDTO> result = agreementCurrencyConfigService.listAgreementCurrencyConfig(dto,user);
			total = result.size();
			
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
	@GetMapping(value="/exportAgreementCurrencyConfig.asyn")
	public ResponseBean exportAgreementCurrencyConfig(HttpServletResponse response, HttpServletRequest request, AgreementCurrencyConfigForm form) throws Exception{
		try {
			AgreementCurrencyConfigDTO dto = new AgreementCurrencyConfigDTO();
			User user= ShiroUtils.getUserByToken(form.getToken());
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto.setGoodsNo(form.getGoodsNo());
			dto.setGoodsName(form.getGoodsName());
			dto.setIds(form.getIds());
			if(StringUtils.isNotBlank(form.getInBuId())) {
				dto.setInBuId(Long.valueOf(form.getInBuId()));				
			}
			if(StringUtils.isNotBlank(form.getOutBuId())) {
				dto.setOutBuId(Long.valueOf(form.getOutBuId()));
			}
			String sheetName = "协议单价导出";
			// 获取导出的信息
			List<AgreementCurrencyConfigExportDTO> result = agreementCurrencyConfigService.getDetailsByExport(dto,user);
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
	 * 提交
	 * */
	@ApiOperation(value = "提交")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "json", value = "提交的协议单价信息json串", required = true)
	})
	@PostMapping(value="/addAgreementCurrencyConfig.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<String> addAgreementCurrencyConfig(String token,String json,HttpSession session) {
		String msg = null;
		try{
			if(json == null || StringUtils.isBlank(json)){
				//输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			msg = agreementCurrencyConfigService.saveAgreementCurrencyConfig(json,user.getId(),user.getName(), user.getTopidealCode());
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,msg);
	}
	
	/**
	 * 导入协议单价
	 * */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importAgreementCurrencyConfig.asyn",headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importAgreementCurrencyConfig(String token,
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
			resultMap = agreementCurrencyConfigService.saveImportAgreementCurrencyConfig(data,user.getId(),user.getName(),user.getMerchantId(),user.getMerchantName(), user.getTopidealCode(),user.getRelMerchantIds());
			Integer success = (Integer)resultMap.get("success");
			Integer failure = (Integer)resultMap.get("failure");
			List<ImportMessage> errorList = (List<ImportMessage>) resultMap.get("msgList");
			UploadResponse uploadResponse = new UploadResponse();// 返回的结果集
			uploadResponse.setSuccess(success);
			uploadResponse.setFailure(failure);
			uploadResponse.setErrorList(errorList);
			
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,uploadResponse);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
	}
}
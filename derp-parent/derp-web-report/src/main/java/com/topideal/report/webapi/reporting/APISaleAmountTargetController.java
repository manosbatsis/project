package com.topideal.report.webapi.reporting;

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
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.SaleAmountTargetDTO;
import com.topideal.report.service.reporting.SaleAmountTargetService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.SaleAmountTargetForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 月度销售额目标控制器
 *
 */
@RestController
@RequestMapping("/webapi/report/saleAmountTarget")
@Api(tags = "月度销售额目标")
public class APISaleAmountTargetController {

	private static final Logger LOGGER = Logger.getLogger(APISaleAmountTargetController.class);

	@Autowired
	private SaleAmountTargetService saleAmountTargetService;

	@ApiOperation(value = "获取列表分页信息")   	
   	@PostMapping(value="listSaleAmountTarget.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<SaleAmountTargetDTO> listSaleAmountTarget(SaleAmountTargetForm form) {		
		SaleAmountTargetDTO dto = new SaleAmountTargetDTO();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			BeanUtils.copyProperties(form, dto);
			if(StringUtils.isNotBlank(form.getBuIds())) {
				List<Long> buList = StrUtils.parseIds(form.getBuIds());
				dto.setBuIds(buList);
			}
			if(StringUtils.isNotBlank(form.getDepartmentIds())) {
				List<Long> departmentList = StrUtils.parseIds(form.getDepartmentIds());
				dto.setDepartmentIds(departmentList);
			}
			if(StringUtils.isNotBlank(form.getBrandSuperiorIds())) {
				List<Long> brandSuperiorList = StrUtils.parseIds(form.getBrandSuperiorIds());
				dto.setBrandSuperiorIds(brandSuperiorList);
			}
			dto = saleAmountTargetService.listSaleAmountTarget(dto,user);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}

	/**
	 * 
	 */
	@ApiOperation(value = "获取导出数量")   	
   	@PostMapping(value="/getOrderCount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<Integer> getOrderCount(SaleAmountTargetForm form) throws Exception {
		Integer total = 0;
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			SaleAmountTargetDTO dto = new SaleAmountTargetDTO();			
			BeanUtils.copyProperties(form, dto);
			if(StringUtils.isNotBlank(form.getBuIds())) {
				List<Long> buList = StrUtils.parseIds(form.getBuIds());
				dto.setBuIds(buList);
			}
			if(StringUtils.isNotBlank(form.getDepartmentIds())) {
				List<Long> departmentList = StrUtils.parseIds(form.getDepartmentIds());
				dto.setDepartmentIds(departmentList);
			}
			if(StringUtils.isNotBlank(form.getBrandSuperiorIds())) {
				List<Long> brandSuperiorList = StrUtils.parseIds(form.getBrandSuperiorIds());
				dto.setBrandSuperiorIds(brandSuperiorList);
			}
			total = saleAmountTargetService.getOrderCount(dto,user);
			
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,total);
	}

	/**
	 * 根据搜索条件导出
	 *
	 * @param response
	 * @param request
	 * @param dto
	 * @throws Exception
	 */
	@ApiOperation(value = "导出",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping("/exportAmountList.asyn")
	public void exportAmountList(HttpServletResponse response, HttpServletRequest request, SaleAmountTargetForm form) throws Exception {
		/** 标题  */
		String[] COLUMNS = {"部门","事业部","母品牌", "计划月份", "币种","计划金额" };
		String[] KEYS = {"departmentName", "buName", "parentBrandName", "month", "currency", "totalPlanAmount"};

		String sheetName = "月度销售额目标导出";
		List<SaleAmountTargetDTO> list = null;
		User user = ShiroUtils.getUserByToken(form.getToken());
		SaleAmountTargetDTO dto = new SaleAmountTargetDTO();			
		BeanUtils.copyProperties(form, dto);
		if(StringUtils.isNotBlank(form.getBuIds())) {
			List<Long> buList = StrUtils.parseIds(form.getBuIds());
			dto.setBuIds(buList);
		}
		if(StringUtils.isNotBlank(form.getDepartmentIds())) {
			List<Long> departmentList = StrUtils.parseIds(form.getDepartmentIds());
			dto.setDepartmentIds(departmentList);
		}
		if(StringUtils.isNotBlank(form.getBrandSuperiorIds())) {
			List<Long> brandSuperiorList = StrUtils.parseIds(form.getBrandSuperiorIds());
			dto.setBrandSuperiorIds(brandSuperiorList);
		}
		if (dto != null) {
			list = saleAmountTargetService.exportAmountList(dto,user);
		}
		//生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, list);
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}

	/**
	 * 导入
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "导入")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/importAmountTarget.asyn", headers = "content-type=multipart/form-data")
	public ResponseBean<UploadResponse> importAmountTarget(String token,@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		try {
			Map resultMap = new HashMap();// 返回的结果集
			if (file == null) {
				// 输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

			if (data == null) {// 数据为空
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			User user= ShiroUtils.getUserByToken(token);
			resultMap = saleAmountTargetService.importAmountTarget(data,user);
			
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

	/**
	 * 删除
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "删除")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "params", value = "选中的单据id集合，多个用逗号隔开", required = true)
	})
	@PostMapping(value="delAmountTarget.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean delAmountTarget(String token,String params) throws Exception {
		try {
			if(StringUtils.isBlank(params)) {
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}
			String[] arr = params.split(",");
			saleAmountTargetService.delAmountTarget(arr) ;

		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
	}
}

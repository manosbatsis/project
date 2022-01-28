package com.topideal.report.web.reporting;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.SaleAmountTargetDTO;
import com.topideal.report.service.reporting.SaleAmountTargetService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.SaleAmountTargetForm;

/**
 * 月度销售额目标控制器
 * @author Gy
 *
 */
@RequestMapping("saleAmountTarget")
@Controller
public class SaleAmountTargetController {

	private static final Logger LOGGER = Logger.getLogger(SaleAmountTargetController.class);

	@Autowired
	private SaleAmountTargetService saleAmountTargetService;

	/**
	 * 列表页
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("toPage.html")
	public String toPage(Model model) throws SQLException {
		return "derp/reporting/sale-amount-target-list";
	}

	@RequestMapping("listSaleAmountTarget.asyn")
	@ResponseBody
	public ViewResponseBean listSaleAmountTarget(SaleAmountTargetForm form) {
		SaleAmountTargetDTO dto = new SaleAmountTargetDTO();
		try {
			User user = ShiroUtils.getUser();
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
			}//
			dto = saleAmountTargetService.listSaleAmountTarget(dto,user);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}

	/**
	 * 获取导出月度销售额的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(SaleAmountTargetForm form) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			User user = ShiroUtils.getUser();
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
			Integer result = saleAmountTargetService.getOrderCount(dto,user);
			data.put("total", result);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(data);
	}

	/**
	 * 根据搜索条件导出
	 *
	 * @param response
	 * @param request
	 * @param dto
	 * @throws Exception
	 */
	@RequestMapping("/exportAmountList.asyn")
	public void exportAmountList(HttpServletResponse response, HttpServletRequest request, SaleAmountTargetForm form) throws Exception {
		/** 标题  */
		String[] COLUMNS = {"部门","事业部","母品牌", "计划月份", "币种","计划金额" };
		String[] KEYS = {"departmentName", "buName", "parentBrandName", "month", "currency", "totalPlanAmount"};

		String sheetName = "月度销售额目标导出";
		List<SaleAmountTargetDTO> list = null;
		User user = ShiroUtils.getUser();
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
	 * 访问导入页
	 *
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("toImportPage.html")
	public String toImportPage(Model model) throws SQLException {
		return "derp/reporting/sale-amount-target-import";
	}

	/**
	 * 导入
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/importAmountTarget.asyn")
	@ResponseBody
	public ViewResponseBean importAmountTarget(@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		Map resultMap = new HashMap();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}

			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());

			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			User user= ShiroUtils.getUser();
			resultMap = saleAmountTargetService.importAmountTarget(data,user);
		} catch (NullPointerException e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}

	/**
	 * 删除
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("delAmountTarget.asyn")
	@ResponseBody
	public ViewResponseBean delAmountTarget(String params) throws Exception {
		try {
			if(StringUtils.isBlank(params)) {
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			String[] arr = params.split(",");
			saleAmountTargetService.delAmountTarget(arr) ;

		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
}

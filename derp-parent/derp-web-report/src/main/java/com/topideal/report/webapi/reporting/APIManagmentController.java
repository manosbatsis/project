package com.topideal.report.webapi.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.*;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.report.service.reporting.ManagmentService;
import com.topideal.report.shiro.ShiroUtils;
import com.topideal.report.webapi.form.ManagmentReportForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 经营报表管理
 */
@RestController
@RequestMapping("/webapi/report/management")
@Api(tags = "经营管理报表")
public class APIManagmentController {

	private static final Logger LOGGER = Logger.getLogger(APIManagmentController.class);

	@Autowired
	private ManagmentService managmentService;

	@ApiOperation(value = "获取客户下拉信息")
	@PostMapping(value="/getCustomerSelectList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getCustomerSelectList(ManagmentReportForm form) throws SQLException {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			result = managmentService.getCustomerSelectList(user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}

	@ApiOperation(value = "获取仓库下拉信息")
	@PostMapping(value="/getDepotSelectList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getDepotSelectList(ManagmentReportForm form) throws SQLException {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			result = managmentService.getDepotSelectList(user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}

	@ApiOperation(value = "获取事业部信息")
	@PostMapping(value="/getBuList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<BusinessUnitModel>> getBuList(ManagmentReportForm form) throws SQLException {
		List<BusinessUnitModel> buList = new ArrayList<BusinessUnitModel>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			buList = managmentService.getBuList(user, form.getDepartmentIds());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,buList);
	}

	@ApiOperation(value = "获取部门下拉列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getDepartmentSelectList.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseBean<List<SelectBean>> getDepartmentSelectList(ManagmentReportForm form) throws SQLException {
		List<SelectBean> result = new ArrayList<SelectBean>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			result = managmentService.getDepartmentSelectList(form, user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,result);
	}

	@ApiOperation(value = "获取公司列表信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true)
	})
	@PostMapping(value="/getMerchantList.asyn")
	public ResponseBean<List<MerchantInfoModel>> getMerchantList(String token) {
		List<MerchantInfoModel> merchantInfoModelList = new ArrayList<>();
		try {
			User user = ShiroUtils.getUserByToken(token);
			merchantInfoModelList = managmentService.getMerchantList(user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			//未知异常
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,merchantInfoModelList);
	}

	@ApiOperation(value = "部门销售金额统计")
   	@PostMapping(value="/getDepartmentSalesAmountStatistic.asyn")
	public ResponseBean<List> getDepartmentSalesAmountStatistic(ManagmentReportForm form) {
		// 返回的结果集
		List<ManageDepartmentSaleDataDTO> departmentSalesAmountStatistic = new ArrayList();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			departmentSalesAmountStatistic = managmentService.getDepartmentSalesAmountStatistic(form, user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			//未知异常
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, departmentSalesAmountStatistic);
	}

	@ApiOperation(value = "导出部门销售金额统计",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportDepartmentSalesAmountStatistic.asyn")
	public void exportDepartmentSalesAmountStatistic(ManagmentReportForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			List<ManageDepartmentSaleDataDTO> departmentSalesAmountStatistic = new ArrayList();
			String fileName = "实时销售金额统计报表";
			// 获取导出的信息
			departmentSalesAmountStatistic = managmentService.getDepartmentSalesAmountStatistic(form, user);
			departmentSalesAmountStatistic = departmentSalesAmountStatistic.stream().filter(entity -> {
				if(entity.getDepartmentSumFlag() != null && entity.getDepartmentSumFlag()) {
					entity.setDepartmentName(entity.getDepartmentSumTitle());
					return false;
				}
				return true;
			}).collect(Collectors.toList());
			String[] COLUMNS = genColumns(form).get("columns");
			String[] KEYS = genColumns(form).get("keys");
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(form.getMonth(), COLUMNS, KEYS,departmentSalesAmountStatistic);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Map<String, String[]> genColumns(ManagmentReportForm form) {
		List<String> title = new ArrayList<>();
		List<String> keyList = new ArrayList<>();
		Map<String, String[]> map = new HashMap<>();

		title.add("序号");
		keyList.add("No");

		title.add("部门");
		keyList.add("departmentName");
		if(form.getGroupByParentBrandFlag() != null && form.getGroupByParentBrandFlag()) {
			title.add("母品牌");
			keyList.add("parentBrandName");
		}
		if(form.getGroupByBrandParentFlag() != null && form.getGroupByBrandParentFlag()) {
			title.add("子品牌");
			keyList.add("brandParent");
		}
		if(form.getGroupByBuIdFlag() != null && form.getGroupByBuIdFlag()) {
			title.add("项目组");
			keyList.add("buName");
		}
		if(form.getGroupByMerchantFlag() != null && form.getGroupByMerchantFlag()) {
			title.add("公司");
			keyList.add("merchantName");
		}
		if(form.getGroupByChannelTypeFlag() != null && form.getGroupByChannelTypeFlag()) {
			title.add("渠道类型");
			keyList.add("channelType");
		}
		if(form.getGroupByCustomerFlag() != null && form.getGroupByCustomerFlag()) {
			title.add("客户名称");
			keyList.add("customerName");
		}
		if(form.getGroupByDepotFlag() != null && form.getGroupByDepotFlag()) {
			title.add("仓库");
			keyList.add("outDepotName");
		}
		if(form.getGroupByPlatformFlag() != null && form.getGroupByPlatformFlag()) {
			title.add("To C平台");
			keyList.add("storePlatformName");
		}

		title.add(form.getTargetMonth().split("-")[1] + "月销售金额(万)");
		keyList.add("monthAmount1");

		title.add(form.getMiddleMonth().split("-")[1] + "月销售金额(万)");
		keyList.add("monthAmount2");

		String beforeMonth = form.getBeforeMonth();
		title.add(beforeMonth.split("-")[1] + "月销售金额(万)");
		keyList.add("monthAmount3");

		map.put("columns", title.toArray(new String[0]));
		map.put("keys", keyList.toArray(new String[0]));
		return map;
	}

	@ApiOperation(value = "部门销售达成统计")
	@PostMapping(value="/getDepartmentSalesAchieveStatistic.asyn")
	public ResponseBean getDepartmentSalesAchieveStatistic(ManagmentReportForm form) {
		// 返回的结果集
		List<ManageDepartmentSaleAchieveDTO> departmentSalesAchieveStatistic = new ArrayList();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			departmentSalesAchieveStatistic = managmentService.getDepartmentSalesAchieveStatistic(form, user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			//未知异常
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,departmentSalesAchieveStatistic);
	}

	@ApiOperation(value = "导出部门销售达成统计",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportDepartmentSalesAchieveStatistic.asyn")
	public void exportDepartmentSalesAchieveStatistic(ManagmentReportForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			List<ManageDepartmentSaleAchieveDTO> departmentSalesAchieveStatistic = new ArrayList();
			// 获取导出的信息
			departmentSalesAchieveStatistic = managmentService.getDepartmentSalesAchieveStatistic(form, user);
			String[] COLUMNS;
			String[] FIRST_COLUMNS;
			String[] KEYS;
			if(StringUtils.isNotBlank(form.getMonth())) {
				String[] split = form.getMonth().split("-");
				String year = split[0];
				String month = split[1];
				form.setYear(year);
//				COLUMNS = new String[]{"序号", "部门", "母品牌", split[1] + "月销售金额(万)", form.getYear() + "年度销售金额(万)"};
//				KEYS = new String[]{"no", "departmentName", "parentBrandName", "monthAchieve", "yearAchieve"};
				COLUMNS = new String[]{"序号", "部门", "母品牌", "目标(万)", "达成(万)", "完成度", "目标(万)", "达成(万)", "完成度"};
				KEYS = new String[]{"no", "departmentName", "parentBrandName", "monthTargetAmount", "monthAchieveAmount", "monthPercentage", "yearTargetAmount", "yearAchieveAmount", "yearPercentage"};
				FIRST_COLUMNS = new String[]{"序号", "部门", "母品牌", month + "月销售金额", form.getYear() + "年度销售金额"};
			}else {
				COLUMNS = new String[]{"序号", "部门", "母品牌", "1月目标(万)", "1月达成(万)", "1月完成度", "2月目标(万)", "2月达成(万)", "2月完成度", "3月目标(万)", "3月达成(万)", "3月完成度",
						"4月目标(万)", "4月达成(万)", "4月完成度","5月目标(万)", "5月达成(万)", "5月完成度", "6月目标(万)", "6月达成(万)", "6月完成度", "7月目标(万)", "7月达成(万)", "7月完成度",
						"8月目标(万)", "8月达成(万)", "8月完成度","9月目标(万)", "9月达成(万)", "9月完成度", "10月目标(万)", "10月达成(万)", "10月完成度", "11月目标(万)", "11月达成(万)", "11月完成度","12月目标(万)", "12月达成(万)", "12月完成度"};
				KEYS = new String[]{"no", "departmentName", "parentBrandName", "1monthTargetAmount", "1monthAchieveAmount", "1monthPercentage", "2monthTargetAmount", "2monthAchieveAmount", "2monthPercentage", "3monthTargetAmount", "3monthAchieveAmount", "3monthPercentage",
						"4monthTargetAmount", "4monthAchieveAmount", "4monthPercentage", "5monthTargetAmount", "5monthAchieveAmount", "5monthPercentage", "6monthTargetAmount", "6monthAchieveAmount", "6monthPercentage", "7monthTargetAmount", "7monthAchieveAmount", "7monthPercentage",
						"8monthTargetAmount", "8monthAchieveAmount", "8monthPercentage", "9monthTargetAmount", "9monthAchieveAmount", "9monthPercentage", "10monthTargetAmount", "10monthAchieveAmount", "10monthPercentage",
						"11monthTargetAmount", "11monthAchieveAmount", "11monthPercentage","12monthTargetAmount", "12monthAchieveAmount", "12monthPercentage"};
				FIRST_COLUMNS = new String[]{"序号", "部门", "母品牌", "1月度销售金额","2月度销售金额","3月度销售金额","4月度销售金额","5月度销售金额","6月度销售金额","7月度销售金额","8月度销售金额","9月度销售金额","10月度销售金额","11月度销售金额","12月度销售金额"};
			}

			departmentSalesAchieveStatistic.stream().forEach(entity -> {
				if(entity.getDepartmentSumFlag() != null && entity.getDepartmentSumFlag()) {
					entity.setDepartmentName(entity.getDepartmentSumTitle());
				}
			});
			List<Map<String, Object>> data = genSalesAchieveList(departmentSalesAchieveStatistic, form);
			// 生成表格
			String sheetName = StringUtils.isNotBlank(form.getMonth())? form.getMonth() : form.getYear();
//			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, COLUMNS, KEYS,data);

			SXSSFWorkbook workbook = new SXSSFWorkbook();

			// 设置字体
			Font font = workbook.createFont();
			font.setFontName("宋体");
			font.setFontHeightInPoints((short)10);//设置字体大小

        	Sheet excel = workbook.createSheet(sheetName);
			CellStyle cellStyle = workbook.createCellStyle();// 设置表头
			cellStyle.setAlignment(HorizontalAlignment.CENTER);// 水平居中
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置背景色的时候一定要加上,否则设置颜色不起作用
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
			cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
			cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
			cellStyle.setBorderTop(BorderStyle.THIN);//上边框
			cellStyle.setBorderRight(BorderStyle.THIN);//右边框
			cellStyle.setFont(font);

			Row firstRow = excel.createRow(0);
        	Row secondRow = excel.createRow(1);

        	List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
        	// 设置第一行表头
        	int i = 0;
			for (String title : FIRST_COLUMNS) {
				Cell cell = firstRow.createCell(i);
				excel.setColumnWidth(i, 20 * 256);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title);

				if(i >= 3) {
					cellRangeAddressList.add(new CellRangeAddress(0,0,i, i+2));
					i = i+3;
				}else {
					i++;
				}
			}

			i = 0;
			for (String title : COLUMNS) { // 设置表头
				Cell cell = secondRow.createCell(i);
				excel.setColumnWidth(i, 20 * 256);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(title);
				i++;
			}

			// 生成前三格的合并对象
			cellRangeAddressList.add(new CellRangeAddress(0, 1, 0, 0));
			cellRangeAddressList.add(new CellRangeAddress(0, 1, 1, 1));
			cellRangeAddressList.add(new CellRangeAddress(0, 1, 2, 2));


			ExcelUtilXlsx.writeExcelWithCollap(workbook, sheetName, COLUMNS, KEYS, data, cellRangeAddressList);
			// 导出文件
			String fileName = sheetName + "销售达成统计报表";
			FileExportUtil.export2007ExcelFile(workbook, response, request, fileName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private List<Map<String, Object>> genSalesAchieveList(List<ManageDepartmentSaleAchieveDTO> departmentSalesAchieveStatistic, ManagmentReportForm form) {
		List<Map<String, Object>> data = new ArrayList<>();
		DecimalFormat df = new DecimalFormat("0.00%");
		DecimalFormat df2 = new DecimalFormat("0.00");
		departmentSalesAchieveStatistic.stream().forEach(entity -> {
			if(entity.getDepartmentSumFlag() != null && entity.getDepartmentSumFlag()) {
				return;
			}
			Map<String, Object> map = new HashMap<>();
			map.put("no", entity.getNo());
			map.put("departmentName", entity.getDepartmentName());
			map.put("parentBrandName", entity.getParentBrandName());

			int num = 1;
			for (SalesAchieveDTO salesAchieveDTO : entity.getMonthAchieveList()) {
				boolean monthflag = false;
				if(salesAchieveDTO.getMonthCompletionPercentage() == null || salesAchieveDTO.getMonthCompletionPercentage().compareTo(BigDecimal.ZERO) < 0) {
					monthflag = true;
				}
				String value = "目标:" + df2.format(salesAchieveDTO.getMonthTargetAmount()) + "\n"
						+ "达成:" + df2.format(salesAchieveDTO.getMonthAchieveAmount()) + "\n"
						+ "完成度:" + (monthflag ? "-" : df2.format(salesAchieveDTO.getMonthCompletionPercentage()) + "%");
				if(StringUtils.isNotBlank(form.getMonth())) {
					map.put("monthAchieve", value);
					map.put("monthTargetAmount", df2.format(salesAchieveDTO.getMonthTargetAmount()));
					map.put("monthAchieveAmount", df2.format(salesAchieveDTO.getMonthAchieveAmount()));
					map.put("monthPercentage", monthflag ? "-" : df2.format(salesAchieveDTO.getMonthCompletionPercentage()) + "%");
				}else {
					map.put(num + "monthAchieve", value);
					map.put(num + "monthTargetAmount", df2.format(salesAchieveDTO.getMonthTargetAmount()));
					map.put(num + "monthAchieveAmount", df2.format(salesAchieveDTO.getMonthAchieveAmount()));
					map.put(num + "monthPercentage", monthflag ? "-" : df2.format(salesAchieveDTO.getMonthCompletionPercentage()) + "%");
				}

				num++;
			}

			boolean yearflag = false;
			if(entity.getYearCompletionPercentage() == null || entity.getYearCompletionPercentage().compareTo(BigDecimal.ZERO) < 0) {
				yearflag = true;
			}
			String yearValue = "目标:" + df2.format(entity.getYearTargetAmount()) + "\n"
					+ "达成:" + df2.format(entity.getYearAchieveAmount()) + "\n"
					+ "完成度:" + (yearflag ? "-" : df2.format(entity.getYearCompletionPercentage()) + "%");

			map.put("yearAchieve", yearValue);
			map.put("yearTargetAmount", df2.format(entity.getYearTargetAmount()));
			map.put("yearAchieveAmount", df2.format(entity.getYearAchieveAmount()));
			map.put("yearPercentage", yearflag ? "-" : df2.format(entity.getYearCompletionPercentage())  + "%");
			data.add(map);
		});
		return data;
	}

	@ApiOperation(value = "部门库存统计")
	@PostMapping(value="/getDepartmentInventoryStatistic.asyn")
	public ResponseBean<List> getDepartmentInventoryStatistic(ManagmentReportForm form) {
		// 返回的结果集
		List<ManageDepartmentInventoryDTO> inventoryStatistic = new ArrayList();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			inventoryStatistic = managmentService.getDepartmentInventoryStatistic(form, user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			//未知异常
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, inventoryStatistic);
	}

	@ApiOperation(value = "导出部门库存统计",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportDepartmentInventoryStatistic.asyn")
	public void exportDepartmentInventoryStatistic(ManagmentReportForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
		String fileName = "库存统计报表";
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			List<ManageDepartmentInventoryDTO> departmentSalesAchieveStatistic = managmentService.getDepartmentInventoryStatistic(form, user);

			departmentSalesAchieveStatistic = departmentSalesAchieveStatistic.stream().filter(entity -> {
				if(entity.getDepartmentSumFlag() != null && entity.getDepartmentSumFlag()) {
					entity.setDepartmentName(entity.getDepartmentSumTitle());
					return false;
				}
				return true;
			}).collect(Collectors.toList());

			Map<String, Object> data = managmentService.exportDepartmentInventoryStatistic(form, user);

			String[] COLUMNS = (String[]) data.get("columns");
			String[] KEYS = (String[]) data.get("keys");
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(form.getMonth(), COLUMNS, KEYS, departmentSalesAchieveStatistic);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			//未知异常
		}
	}

	@ApiOperation(value = "部门库存清空天数统计")
	@PostMapping(value="/getDepartmentInventoryCleanStatistic.asyn")
	public ResponseBean getDepartmentInventoryCleanStatistic(ManagmentReportForm form) {
		// 返回的结果集
		List<ManageDepartmentInventoryCleanDTO> resultList = new ArrayList<>();
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			resultList = managmentService.getDepartmentInventoryCleanStatistic(form, user);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			//未知异常
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}

		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,resultList);
	}

	@ApiOperation(value = "导出部门库存清空天数统计",produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@GetMapping(value="/exportDepartmentInventoryCleanStatistic.asyn")
	public void exportDepartmentInventoryCleanStatistic(ManagmentReportForm form, HttpServletResponse response, HttpServletRequest request) throws Exception {
		String fileName = "库存清空天数统计报表";
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			List<ManageDepartmentInventoryCleanDTO> departmentSalesAchieveStatistic = managmentService.getDepartmentInventoryCleanStatistic(form, user);
			// 过滤掉合计
			departmentSalesAchieveStatistic = departmentSalesAchieveStatistic.stream().filter(entity -> {
				if(entity.getDepartmentSumFlag() != null && entity.getDepartmentSumFlag()) {
					entity.setDepartmentName(entity.getDepartmentSumTitle());
					return false;
				}
				return true;
			}).collect(Collectors.toList());
			Map<String, Object> data = managmentService.exportDepartmentInventoryCleanStatistic(form, user);

			String[] COLUMNS = (String[]) data.get("columns");
			String[] KEYS = (String[]) data.get("keys");
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(form.getMonth(), COLUMNS, KEYS, departmentSalesAchieveStatistic);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, fileName);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
			//未知异常
		}
	}
}

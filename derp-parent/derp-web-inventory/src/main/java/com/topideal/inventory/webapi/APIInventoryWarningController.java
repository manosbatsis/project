package com.topideal.inventory.webapi;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryBatchForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-库存预警-控制层
 */
@RestController
@RequestMapping("/webapi/inventory/inventoryWarning")
@Api(tags = "效期预警")
public class APIInventoryWarningController {


	// 批次库存service
	@Autowired
	private InventoryBatchService inventoryBatchService;


	@ApiOperation(value = "获取分页数据")
	@PostMapping(value = "/listInventoryWarning.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<InventoryBatchDTO> listInventoryWarning(HttpSession session, InventoryBatchForm form, String validityTypes) {

		try {
			// 响应结果集
			User user = ShiroUtils.getUserByToken(form.getToken());
			InventoryBatchDTO dto = new InventoryBatchDTO();
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			dto = inventoryBatchService.selectInventoryWarningByPage(dto, validityTypes);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}


	@ApiOperation(value = "导出效期预警")
	@GetMapping(value = "/exportInventoryWarning.asyn")
	private void exportInventoryWarning(HttpSession session, HttpServletResponse response, HttpServletRequest request, InventoryBatchForm form) {

		try {

			User user = ShiroUtils.getUserByToken(form.getToken());
			InventoryBatchDTO dto = new InventoryBatchDTO();
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			String sheetName = "效期预警";
			// 根据勾选的获取信息
			List<Map<String, Object>> result = inventoryBatchService.exportInventoryWarningMap(dto, form.getValidityTypes());
			String[] columns = {"商家名称", "仓库名称", "商品货号", "商品名称", "生产日期", "失效日期", "批次号", "总数量", "单位", "剩余效期（天）", "总效期（天）",
					"剩余效期", "效期区间"};
			String[] keys = {"merchant_name", "depot_name", "goods_no", "goods_name", "production_date", "overdue_date",
					"batch_no", "surplus_num", "unit", "surplus_days", "total_days", "surplus_date", "surplus_date_section"};
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}

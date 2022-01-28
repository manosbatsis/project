package com.topideal.inventory.webapi;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryBatchForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-商品库存明细-控制层
 */
@RestController
@RequestMapping("/webapi/inventory/productInventoryDetails")
@Api(tags = "商品库存明细")
public class APIProductInventoryDetailsController {

	// 批次库存明细service
	@Autowired
	private InventoryBatchService inventoryBatchService;


	@ApiOperation(value = "获取分页数据")
	@PostMapping(value = "/listProductInventoryDetails.asyn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<InventoryBatchDTO> listProductInventoryDetails(HttpSession session, InventoryBatchForm form) {
		try {
			User user = ShiroUtils.getUserByToken(form.getToken());
			InventoryBatchDTO dto = new InventoryBatchDTO();
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			List<Long> depotIdsList = new ArrayList<Long>();
			if (StringUtils.isNotBlank(form.getDepotIds()) && !"null".equals(form.getDepotIds())) {
				List<String> asList = Arrays.asList(form.getDepotIds().split(","));
				for (String depotIdStr : asList) {
					if (StringUtils.isNotBlank(depotIdStr)) depotIdsList.add(Long.valueOf(depotIdStr));
				}
			}
			dto.setDepotIdsList(depotIdsList);
			dto = inventoryBatchService.listProductInventoryDetailsByPage(dto);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, dto);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	@ApiOperation(value = "导出商品库存明细") 
	@GetMapping(value = "/exportProductInventoryDetails.asyn")
	@ApiImplicitParams({@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotIds", value = "调拨入库单id", required = true),
			@ApiImplicitParam(name = "goodsNo", value = "仓库id, 多个以英文逗号隔开", required = true)})
	private void exportProductInventoryDetails(HttpServletResponse response, HttpServletRequest request, String token, String depotIds, String goodsNo) {

		try {
			String sheetName0 = "商品库存明细";

			User user = ShiroUtils.getUserByToken(token);

			Map<String, Object> resultMap = inventoryBatchService.exportProductInventoryDetailsMap(user.getMerchantId(), depotIds, goodsNo);
			String[] columns0 = {"商家名称", "仓库名称", "条形码", "商品货号", "商品名称", "库存数量", "冻结数量", "坏品数量", "过期数量", "可用数量", "单位"};
			String[] keys0 = {"merchantName", "depotName", "barcode", "goodsNo", "goodsName", "surplusNum", "freezeNum",
					"badNum", "okayNum", "availableNum", "unitLabel"};
			//生成表格
			//SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			List<InventoryBatchDTO> result0 = (List<InventoryBatchDTO>) resultMap.get("result0");
			List<InventoryBatchDTO> result1 = (List<InventoryBatchDTO>) resultMap.get("result1");

			String sheetName1 = "批次库存明细";
			String[] columns1 = {"商家名称", "仓库名称", "条形码", "商品货号", "商品名称", "批次号", "生产日期", 
					"失效日期", "库存类型", "是否过期", "库存数量", "理货单位", "托盘号", "品牌", "标准条码"};
			String[] keys1 = {"merchantName", "depotName", "barcode", "goodsNo", "goodsName", "batchNo", "productionDate",
					"overdueDate", "typeLabel", "isExpireLabel", "surplusNum", "unitLabel", "lpn","brandName", "commbarcode"};
			List<ExportExcelSheet> sheetList = new ArrayList<>();
			ExportExcelSheet Sheet0 = new ExportExcelSheet();
			Sheet0.setColums(columns0);
			Sheet0.setKeys(keys0);
			Sheet0.setSheetNames(sheetName0);
			Sheet0.setResultList(result0);
			sheetList.add(Sheet0);
			ExportExcelSheet Sheet1 = new ExportExcelSheet();
			Sheet1.setColums(columns1);
			Sheet1.setKeys(keys1);
			Sheet1.setSheetNames(sheetName1);
			Sheet1.setResultList(result1);
			sheetList.add(Sheet1);
			SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList);

			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}


}

package com.topideal.inventory.webapi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.InventorySummaryDTO;
import com.topideal.inventory.service.InventorySummaryService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventorySummaryForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 库存管理-商品收发汇总-控制层 
 */
@RestController
@RequestMapping("/webapi/inventory/inventorySummary")
@Api(tags = "商品收发汇总")
public class APIInventorySummaryController {

	// 商品收发汇总service
	@Autowired
	private InventorySummaryService inventorySummaryService;


	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取商品收发汇总分页数据")
	@PostMapping(value = "/listInventorySummary.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<InventorySummaryDTO> listInventorySummary(InventorySummaryForm form) {
		InventorySummaryDTO dto = new InventorySummaryDTO();
		try{
		 	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
			// 响应结果集
		 	User user= ShiroUtils.getUserByToken(form.getToken());
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			if(StringUtils.isNotBlank(form.getCurrentMonth())){
				dto.setLastMonth(TimeUtils.getLastMonth(dateFormat.parse(form.getCurrentMonth())));
			}
			dto = inventorySummaryService.listInventorySummaryByPage(dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	

	/**
	 * 商品收发汇总
	 * */
	@ApiOperation(value = "导出商品收发汇总")
	@GetMapping(value ="/exportInventorySummary.asyn")
	private void exportInventorySummary(HttpSession session, HttpServletResponse response, HttpServletRequest request,InventorySummaryForm form){
		
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			//校验id是否正确
	        String sheetName = "商品收发汇总";
	        //根据勾选的获取信息
	        List<Map<String,Object>> result = inventorySummaryService.exportInventorySummaryMap(user.getMerchantId(), form.getDepotId(), form.getGoodsNo(), form.getCurrentMonth());
	        String[] columns={"商家名称","仓库名称","商品货号","商品名称","本月期初库存","本月累计入库","本月累计出库","库存余额","销售在途量","电商在途量","调拨出库在途","可用库存","单位","库存周转天数(按120天订单算)","在库库存断销时间"};
	        String[] keys={"merchantName","depotName","goodsNo","goodsName","openingInventoryNum","inDepotTotal","outDepotTotal","surplusNum","saleOnwayNum","eOnwayNum","transferOutNum","availableNum","unit","turnoverDays","noSaleDate"};
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

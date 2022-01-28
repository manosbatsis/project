package com.topideal.inventory.webapi;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.InventoryRealTimeDTO;
import com.topideal.inventory.service.InventoryRealTimeService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryRealTimeForm;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-实时库存-控制层 
 */
@RestController
@RequestMapping("/webapi/inventory/inventoryRealTime")
@Api(tags = "库存管理-实时库存")
public class APIInventoryRealTimeController {

	// 库存--实时库存service
	@Autowired
	private InventoryRealTimeService inventoryRealTimeService;
	 //仓库信息dao
    @Autowired
    private  DepotInfoMongoDao depotInfoMongoDao;

	/**
	 * 获取op和ofc的实时库存数据  
	 * */
	@ApiOperation(value = "获取实时库存分页数据")
	@PostMapping(value = "/searchInventoryRealTime.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<InventoryRealTimeDTO> searchStock(InventoryRealTimeForm form) {
		InventoryRealTimeDTO dto = new InventoryRealTimeDTO();
		try{
			 //校验id是否正确
            boolean isRight = StrUtils.validateId(form.getDepotId());
            if(!isRight){
                //输入信息不完整
				return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, "请先选择仓库");
            }
            User user= ShiroUtils.getUserByToken(form.getToken());
			BeanUtils.copyProperties(form,dto);

            //根据仓库id获取仓库编码和名称
    		Map<String,Object> depotMap=new HashMap<String,Object>();
    		depotMap.put("depotId", dto.getDepotId());
    		DepotInfoMongo depotMongo =depotInfoMongoDao.findOne(depotMap);
    		if(depotMongo!=null&&depotMongo.getCode().matches("WMS_360_01|WMS_360_02|WTX001|CNHZ001")){
    			//菜鸟仓查实时库存快照
				dto = inventoryRealTimeService.realTimeByPage(user.getMerchantId(), dto);
    		}else{
			    //响应结果集
				dto = inventoryRealTimeService.searchInventoryRealTime(user.getMerchantId(),depotMongo,dto.getGoodsNo(),dto.getPageNo(),dto.getPageSize(),
                		user.getMerchantName(),user.getTopidealCode(),dto.getBarcode());
    		}
    	}catch(Exception e){
    		e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}


	/**
	 * 导出实时库存
	 * */
	@ApiOperation(value = "导出实时库存")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "goodsNo", value = "商品货号"),
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "batchNo", value = "批次号")
	})
	@GetMapping(value = "/exportInventoryRealTime.asyn")
	private void exportInventoryRealTime(HttpServletResponse response, HttpServletRequest request,String token, String goodsNo,Long depotId,String batchNo){
		try {
			User user= ShiroUtils.getUserByToken(token);
			InventoryRealTimeDTO dto = new InventoryRealTimeDTO();
			//根据仓库id获取仓库编码和名称
    		Map<String,Object> depotMap=new HashMap<String,Object>();
    		depotMap.put("depotId", depotId);
    		DepotInfoMongo depotMongo =depotInfoMongoDao.findOne(depotMap);
    		//菜鸟仓查最后一天的实时库存快照
    		if(depotMongo!=null&&depotMongo.getCode().matches("WMS_360_01|WMS_360_02|WTX001|CNHZ001")){
    			//菜鸟仓查实时库存快照
				dto.setDepotId(depotId);
				dto.setBatchNo(batchNo);
				dto.setGoodsNo(goodsNo);
				dto = inventoryRealTimeService.searchRookieInventoryRealForExport(user.getMerchantId(),user.getMerchantName(),user.getTopidealCode(),depotMongo,dto);
    		}else{
				dto = inventoryRealTimeService.searchInventoryRealTimeForExport(user.getMerchantId(), depotMongo, goodsNo, user.getMerchantName(), user.getTopidealCode());
    		}
			// 校验id是否正确
			String sheetName = "实时库存";
			// 根据勾选的获取信息
			List<Map<String, Object>> result = inventoryRealTimeService.exportInventoryRealTimeMap(dto, batchNo);
			String[] columns = { "商家名称", "仓库名称", "商品货号", "商品名称","OPG号","生产日期", "失效日期", "批次号", "库存数量", "冻结数量", "锁定数量", "可用数量",
					"临保天数", "库存类型","理货单位", "托盘号", "查询时间" };
			String[] keys = { "merchantName", "depotName", "goodsNo", "goodsName","opgCode","productionDate", "overdueDate",
					"batchNo", "qty", "realFrozenStockNum", "lockStockNum", "realStockNum", "overDays", "stockType",
					 "uom", "lpn", "createDate" };
			// 生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

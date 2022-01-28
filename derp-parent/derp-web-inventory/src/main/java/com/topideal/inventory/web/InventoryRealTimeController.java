package com.topideal.inventory.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.entity.dto.InventoryRealTimeDTO;
import com.topideal.inventory.service.InventoryRealTimeService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;

/**
 * 库存管理-实时库存-控制层 
 */
@RequestMapping("/inventoryRealTime")
@Controller
public class InventoryRealTimeController {


	// 库存--实时库存service
	@Autowired
	private InventoryRealTimeService inventoryRealTimeService;
	 //仓库信息dao
    @Autowired
    private  DepotInfoMongoDao depotInfoMongoDao;

	@Autowired
	private MerchandiseInfoMogoDao  merchandiseInfoMogoDao;
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(HttpSession session,Model model)throws Exception  {
	
		User user= ShiroUtils.getUser();
		
		return "/inventory/inventoryRealTime-list";
	}


	
	/**
	 * 获取op和ofc的实时库存数据  
	 * */
	@RequestMapping("/searchInventoryRealTime.asyn")
	@ResponseBody
	private ViewResponseBean searchStock(HttpSession session,InventoryRealTimeDTO model) {
		try{
			 //校验id是否正确
            boolean isRight = StrUtils.validateId(model.getDepotId());
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            User user= ShiroUtils.getUser();
            //根据仓库id获取仓库编码和名称
    		Map<String,Object> depotMap=new HashMap<String,Object>();
    		depotMap.put("depotId", model.getDepotId());
    		DepotInfoMongo depotMongo =depotInfoMongoDao.findOne(depotMap);
    		if(depotMongo!=null&&depotMongo.getCode().matches("WMS_360_01|WMS_360_02|WTX001|CNHZ001")){
    			//菜鸟仓查实时库存快照
    			model = inventoryRealTimeService.realTimeByPage(user.getMerchantId(), model);
    		}else{
			    //响应结果集
                model = inventoryRealTimeService.searchInventoryRealTime(user.getMerchantId(),depotMongo,model.getGoodsNo(),model.getPageNo(),model.getPageSize(),
                		user.getMerchantName(),user.getTopidealCode(),model.getBarcode());
    		}
    	}catch(Exception e){
    		e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}


	/**
	 * 导出实时库存
	 * */
	@RequestMapping("/exportInventoryRealTime.asyn")
	@ResponseBody
	private void exportInventoryRealTime(HttpSession session, HttpServletResponse response, 
			         HttpServletRequest request, String goodsNo,Long depotId,String batchNo){

		try {
			User user= ShiroUtils.getUser();
			InventoryRealTimeDTO model = new InventoryRealTimeDTO();
			//根据仓库id获取仓库编码和名称
    		Map<String,Object> depotMap=new HashMap<String,Object>();
    		depotMap.put("depotId", depotId);
    		DepotInfoMongo depotMongo =depotInfoMongoDao.findOne(depotMap);
    		//菜鸟仓查最后一天的实时库存快照
    		if(depotMongo!=null&&depotMongo.getCode().matches("WMS_360_01|WMS_360_02|WTX001|CNHZ001")){
    			//菜鸟仓查实时库存快照
    			model.setDepotId(depotId);
    			model.setBatchNo(batchNo);
    			model.setGoodsNo(goodsNo);
    			model = inventoryRealTimeService.searchRookieInventoryRealForExport(user.getMerchantId(),user.getMerchantName(),user.getTopidealCode(),depotMongo,model);
    		}else{
			     model = inventoryRealTimeService.searchInventoryRealTimeForExport(
					     user.getMerchantId(), depotMongo, goodsNo, user.getMerchantName(), user.getTopidealCode());
    		}
			// 校验id是否正确
			String sheetName = "实时库存";
			// 根据勾选的获取信息
			List<Map<String, Object>> result = inventoryRealTimeService.exportInventoryRealTimeMap(model, batchNo);
			String[] columns = { "商家名称", "仓库名称", "商品货号","商品条码", "商品名称","OPG号","生产日期", "失效日期", "批次号", "库存数量", "冻结数量", "锁定数量", "可用数量",
					"临保天数", "库存类型","理货单位", "托盘号", "查询时间" };
			String[] keys = { "merchantName", "depotName", "goodsNo","barcode", "goodsName","opgCode","productionDate", "overdueDate",
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

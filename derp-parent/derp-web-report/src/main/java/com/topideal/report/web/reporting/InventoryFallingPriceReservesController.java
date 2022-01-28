package com.topideal.report.web.reporting;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.InventoryFallingPriceReservesDTO;
import com.topideal.entity.vo.reporting.InventoryFallingPriceReservesModel;
import com.topideal.report.service.reporting.InventoryFallingPriceReservesService;
import com.topideal.report.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存货跌价准备控制器
 * @author gy
 *
 */
@Controller
@RequestMapping("inventoryFallingPriceReserves")
public class InventoryFallingPriceReservesController {
	
	/**导出字段名*/
	private final static String[] exportColumns = { "商家名称", "仓库名称", "报表月份", "工厂源码", "商品货号", "商品条码", "标准条码", "商品名称", 
			"标准品牌", "母品牌", "二级分类", "生产日期", "失效日期", "首次上架日期", "生产批次号", "总效期(天)", "总库存", "库存类型", "锁定数量" , "冻结数量", "可售数量",
			"剩余失效(天)", "失效月份", "剩余效期占比(%)", "效期区间", "剩余效期占比(财务逻辑）",
			"2个月后剩余效期占比(%)", "2个月后剩余效期区间", "单价", "总金额", "跌价准备比例(%)", "计提总额"};
	
	/**导出字段反射key,对应model字段名*/
	private final static String[] exportKeys = {"merchantName", "depotName", "reportMonth", "factoryNo", "goodsNo", "barcode", "commbarcode", "goodsName", 
			"brandParent", "superiorParentBrandName", "typeName", "productionDate", "overdueDate", "firstShelfDate", "batchNo", "totalDays", "surplusNum", "inverntoryTypeLabel", "lockStockNum" , "realFrozenStockNum", "realStockNum",
            "surplusDays", "overdueMonth", "surplusProportion", "effectiveIntervalLabel", "financialSurplusProportionLabel",
            "twoMonthsSurplusProportion", "twoMonthsEffectiveIntervalLabel", "settlementPrice", "totalAmount", "depreciationReserveProportion", "totalProvision"} ;

	@Autowired
	private InventoryFallingPriceReservesService inventoryFallingPriceReservesService ;
	
	// mq
	@Autowired
    private RMQProducer rocketMQProducer;
	
	@RequestMapping("toPage.html")
	public String toPage(Model model) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
		String month = sdf.format(new Date());
		
		//判断是否每月第一天
		String currentMonthFirstDay = TimeUtils.getCurrentMonthFirstDay();
		String currentDate = TimeUtils.format(new Date(), "yyyy-MM-dd");
		
		if(currentMonthFirstDay.equals(currentDate)) {
			month = TimeUtils.getLastMonth(new Date()) ;
		}
		
		model.addAttribute("month", month) ;
		
		return "derp/reporting/inventoryFallingPriceReserves-list" ;
	}
	
	/**
	 * 获取分页信息
	 * @param model
	 * @return
	 */
	@RequestMapping("listInventoryFallingPriceReserves.asyn")
	@ResponseBody
	public ViewResponseBean listInventoryFallingPriceReserves(InventoryFallingPriceReservesDTO model) {
		try {
			User user=ShiroUtils.getUser();
			
			model.setMerchantId(user.getMerchantId());
			
			//如果月份为空，默认当前日期
			if(model.getReportMonth() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
				String month = sdf.format(new Date());
				
				model.setReportMonth(month);
			}
			
			model = inventoryFallingPriceReservesService.listInventoryFallingPriceReservesPage(model) ;
			
			return ResponseFactory.success(model) ;
			
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305) ;
		}
	}
	
	/**
	 * 刷新仓库、月汇总报表
	 * */
	@RequestMapping("/flashInventoryFallingPriceReserves.asyn")
	@ResponseBody
	private ViewResponseBean flashInventoryFallingPriceReserves(InventoryFallingPriceReservesDTO model) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{
			
			User user=ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			
			//发送mq消息
			Map<String, Object> body = new HashMap<String, Object>();
			body.put("merchantId", user.getMerchantId());
			body.put("depotId", model.getDepotId()==null ? "" :String.valueOf(model.getDepotId()));
			body.put("month", model.getReportMonth() ==null ? "" : model.getReportMonth());
			
			JSONObject json = JSONObject.fromObject(body);
			System.out.println(json.toString());
			SendResult result =rocketMQProducer.send(json.toString(), MQReportEnum.INVENTORY_FALLING_PRICE_RESERVES.getTopic(), MQReportEnum.INVENTORY_FALLING_PRICE_RESERVES.getTags());
			System.out.println(result);
			if(result== null||!result.getSendStatus().name().equals("SEND_OK")){
				retMap.put("code", "01");
				retMap.put("message", "刷新消息发送失败");
				return ResponseFactory.success(retMap);
			}
			retMap.put("code", "00");
			retMap.put("message", "成功");
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(retMap);
	}
	
	@RequestMapping("getCount.asyn")
	@ResponseBody
	public ViewResponseBean getCount(InventoryFallingPriceReservesDTO dto) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			
			//如果月份为空，默认当前日期
			if(dto.getReportMonth() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
				String month = sdf.format(new Date());
				dto.setReportMonth(month);
			}
			User user=ShiroUtils.getUser();
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			int total = inventoryFallingPriceReservesService.getCount(dto);
			data.put("total", total);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(data);
	}
	
	@RequestMapping("exportInventoryFallingPriceReserves.asyn")
	public void exportInventoryFallingPriceReserves(InventoryFallingPriceReservesDTO dto,HttpServletRequest request ,HttpServletResponse response) {
		try {
			//如果月份为空，默认当前日期
			if(dto.getReportMonth() == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM") ;
				String month = sdf.format(new Date());
				dto.setReportMonth(month);
			}
			User user=ShiroUtils.getUser();
			dto.setMerchantId(user.getMerchantId());
			
			List<InventoryFallingPriceReservesDTO> exportList = inventoryFallingPriceReservesService.getExportList(dto);
			
			String exportSheetName = "存货跌价准备报表" ;
			
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(exportSheetName, exportColumns, exportKeys, exportList) ;
			// 导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, exportSheetName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}

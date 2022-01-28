package com.topideal.inventory.web;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.topideal.entity.dto.InventoryDetailsDTO;
import com.topideal.inventory.service.InventoryDetailsService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.tools.DownloadExcelUtil;

/**
 * 库存管理-商品收发明细-控制层 
 */
@RequestMapping("/inventoryDetails")
@Controller
public class InventoryDetailsController {

	// 商品收发明细service
	@Autowired
	private InventoryDetailsService inventoryDetailsService;

	/*@Autowired
	private InventoryDetailsHistoryService inventoryDetailsHistoryService;*/
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session)throws Exception  {
		User user= ShiroUtils.getUser();
		return "/inventory/inventoryDetails-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryDetails.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryDetails(InventoryDetailsDTO model,HttpSession session) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model = inventoryDetailsService.listInventoryDetails(model);
			
		}catch(SQLException e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	
	/**
	 * 导出商品收发明细
	 * */
	@RequestMapping("/exportInventoryDetails.asyn")
	@ResponseBody
	private void exportInventoryDetails(HttpSession session, HttpServletResponse response, HttpServletRequest request,Long depotId,String goodsNo,String batchNo, String startTime,String endTime,String thingStatus,String orderNo,String businessNo,Long buId,String barcode,
			String commbarcode,String orderTimeRange,String operateType){

		try {
			User user= ShiroUtils.getUser();
			InventoryDetailsDTO model =new  InventoryDetailsDTO();
		    model.setMerchantId(user.getMerchantId());
		    model.setDepotId(depotId);
		    model.setGoodsNo(goodsNo);
		    model.setBatchNo(batchNo);
		    model.setStartTime(startTime);
		    model.setEndTime(endTime);
		    model.setThingStatus(thingStatus);
		    model.setOrderNo(orderNo);
		    model.setBusinessNo(businessNo);
		    model.setBuId(buId);
		    model.setBarcode(barcode);
		    model.setCommbarcode(commbarcode);
		    model.setOperateType(operateType);
        	String sheetName = "商品收发明细";        	
        	model.setOrderTimeRange(orderTimeRange);
        	List<Map<String,Object>> result = inventoryDetailsService.exportInventoryDetailsMap(model);

        	String[] columns={"商家名称","仓库名称","事业部","事物类型","来源单据号","业务单据号","变更日期","商品货号","条码","标准条码","商品名称","批次号","数量","单位","库存类型","是否过期","库位货号","操作类型"};
        	String[] keys={"merchant_name","depot_name","bu_name","thing_name","order_no","business_no","divergence_date","goods_no","barcode","commbarcode","goods_name","batch_no","num","unit","type","is_expire","location_goods_no","operate_type"};
        	//生成表格
        	SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
        	//导出文件
        	FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}

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

import com.topideal.common.constant.ConfigConstants;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.entity.vo.InventoryFreezeDetailsModel;
import com.topideal.inventory.service.InventoryFreezeDetailsService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.tools.DownloadExcelUtil;

/**
 * 库存管理-库存冻结和解冻收发明细-控制层 
 */
@RequestMapping("/inventoryFreezeDetails")
@Controller
public class InventoryFreezeDetailsController {

	// 商品收发明细service
	@Autowired
	private InventoryFreezeDetailsService inventoryFreezeDetailsService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session)throws Exception  {
		User user= ShiroUtils.getUser();
		return "/inventory/inventoryFreezeDetails-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryFreezeDetails.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryFreezeDetails(InventoryFreezeDetailsModel model,HttpSession session) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model = inventoryFreezeDetailsService.listInventoryFreezeDetails(model);
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
	@RequestMapping("/exportInventoryFreezeDetails.asyn")
	@ResponseBody
	private void exportInventoryFreezeDetails(HttpSession session, HttpServletResponse response, HttpServletRequest request,Long depotId,String goodsNo,String status,String orderNo,String businessNo){

		try {
			User user= ShiroUtils.getUser();
		    InventoryFreezeDetailsModel model =new  InventoryFreezeDetailsModel();
		    model.setMerchantId(user.getMerchantId());
		    model.setDepotId(depotId);
		    model.setGoodsNo(goodsNo);
		    model.setStatus(status);
		    model.setOrderNo(orderNo);
		    model.setBusinessNo(businessNo);
		    
        	String sheetName = "商品冻结和解冻收发明细";
        	//根据勾选的获取信息
        	List<Map<String,Object>> result = inventoryFreezeDetailsService.exportInventoryFreezeDetailsMap(model);
        	String[] columns={"商家名称","仓库名称","商品货号","商品名称","单据类型","来源单据号","业务单据号","变更日期","数量","操作类型"};
        	String[] keys={"merchant_name","depot_name","goods_no","goods_name","status","order_no","business_no","divergence_date","num","operate_type"};
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

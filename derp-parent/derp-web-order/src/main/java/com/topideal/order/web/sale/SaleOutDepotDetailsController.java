package com.topideal.order.web.sale;

import java.sql.SQLException;
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
import com.topideal.entity.dto.sale.SaleOutDepotDTO;
import com.topideal.order.service.sale.SaleOutDepotService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 销售出库明细 controller
 * 
 */
@RequestMapping("/saleOutDetails")
@Controller
public class SaleOutDepotDetailsController {

	// 销售出库service
	@Autowired
	private SaleOutDepotService saleOutDepotService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		return "/derp/sale/outdetails-list";
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listSaleOutDepotDetails.asyn")
	@ResponseBody
	private ViewResponseBean listSaleOutDepotDetails(SaleOutDepotDTO dto, HttpSession session) {
		try{
			User user= ShiroUtils.getUser(); 	
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = saleOutDepotService.listSaleOutDepotDetailsByPage(dto,user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 获取导出销售出库明细的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotDTO dto) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser(); 	
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepotDetails(dto,user);
			data.put("total",result.size());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 导出销售出库明细
	 * */
	@RequestMapping("/exportSaleOutDepotDetails.asyn")
	@ResponseBody
	private void exportSaleOutDepotDetails(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleOutDepotDTO dto) throws Exception{
		User user= ShiroUtils.getUser(); 	
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		// 响应结果集
		List<SaleOutDepotDTO> result = saleOutDepotService.listSaleOutDepotDetails(dto,user);
		String sheetName = "销售出库明细";
        String[] columns={"销售出库单号","销售订单号","出库仓名称","事业部","唯品账单号","LBX单号","PO号","客户名称","销售类型","商品货号","商品名称","出库数量","销售数量","海外仓理货单位"};
        String[] keys = {"code", "saleOrderCode", "outDepotName", "buName", "vipsBillNo", "lbxNo", "poNo", "customerName", "saleTypeLabel", "goodsNo", "goodsName", "transferNum", "saleNum", "tallyingUnitLabel"} ;
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}

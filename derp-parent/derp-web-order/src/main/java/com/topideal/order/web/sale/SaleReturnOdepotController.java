package com.topideal.order.web.sale;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.SaleReturnOdepotDTO;
import com.topideal.entity.dto.sale.SaleReturnOdepotItemDTO;
import com.topideal.order.service.sale.SaleReturnOdepotService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 销售退货出库单 controller
 * 
 */
@RequestMapping("/saleReturnOdepot")
@Controller
public class SaleReturnOdepotController {

	// 销售退货出库单service
	@Autowired
	private SaleReturnOdepotService saleReturnOdepotService;
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		return "/derp/sale/salereturnodepot-list";
	}

	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		SaleReturnOdepotDTO saleReturnOdepot = saleReturnOdepotService.searchDetail(id);
		List<SaleReturnOdepotItemDTO> itemList = saleReturnOdepotService.listItemByOrderId(id);
		saleReturnOdepot.setItemList(itemList);
		model.addAttribute("detail", saleReturnOdepot);
		return "/derp/sale/salereturnodepot-details";
	}
	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listSaleReturnOdepot.asyn")
	@ResponseBody
	private ViewResponseBean listSaleReturnOdepot(SaleReturnOdepotDTO dto, HttpSession session) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = saleReturnOdepotService.listSaleReturnOdepotByPage(dto,user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 获取导出销售退货出库清单的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleReturnOdepotDTO dto) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser();	
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			List<SaleReturnOdepotDTO> result = saleReturnOdepotService.listSaleReturnOdepot(dto,user);
			data.put("total",result.size());
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	/**
	 * 导出销售退货出库清单
	 * */
	@RequestMapping("/exportSaleReturnOdepot.asyn")
	@ResponseBody
	private void exportSaleReturnOdepot(HttpSession session, HttpServletResponse response, HttpServletRequest request,SaleReturnOdepotDTO dto) throws Exception{
		User user= ShiroUtils.getUser();	
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		// 响应结果集
		List<SaleReturnOdepotDTO> result = saleReturnOdepotService.listSaleReturnOdepot(dto,user);
		List<SaleReturnOdepotItemDTO> itemList = new ArrayList<SaleReturnOdepotItemDTO>();
		for(SaleReturnOdepotDTO sModel:result){
			List<SaleReturnOdepotItemDTO> itemList1 = saleReturnOdepotService.listItemByOrderId(sModel.getId());
			for(SaleReturnOdepotItemDTO item:itemList1){
				item.setSreturnOdepotCode(sModel.getCode());
			}
			if(itemList1 != null && itemList1.size()>0){
				itemList.addAll(itemList1);
			}
		}
		String sheetName = "销售退货出库清单";
        String[] columns={"销退出库单号","单据状态","销退订单号","退出仓库","事业部","退入仓库","退货客户名称","合同号","销退出库时间"};
        String[] keys = {"code", "statusLabel", "orderCode", "outDepotName", "buName", "inDepotName", "customerName", "contractNo", "returnOutDate"} ;
        
        String[] columns1={"销退出库单号","退出商品编号","退出商品货号","退出商品名称","退货出库数量","退出批次","生产日期","失效日期"};
        String[] keys1 = {"sreturnOdepotCode", "outGoodsCode", "outGoodsNo", "outGoodsName", "returnNum", "returnBatchNo", "productionDate", "overdueDate"} ;
        //生成表格
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet("基本信息", columns, keys, result) ;
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet("商品信息", columns1, keys1, itemList) ;
		
		List<ExportExcelSheet> sheets = new ArrayList<>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets);
		
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
}

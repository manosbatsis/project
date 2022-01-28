/*package com.topideal.order.web.purchase;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.purchase.PurchaseAnalysisDTO;
import com.topideal.entity.dto.purchase.PurchaseAnalysisExportDTO;
import com.topideal.order.service.purchase.PurchaseAnalysisService;
import com.topideal.order.shiro.ShiroUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


*//**
 * 采购入库勾稽 控制层
 * @author zhanghx
 *//*
@RequestMapping("/difference")
@Controller
public class PurchaseAnalysisController {
	
	*//** 标题  *//*
	private static final String[] COLUMNS= {"采购订单号", "事业部","采购商品货号","采购商品名称","采购数量","是否完结","完结时间","勾稽入库单号","入库商品货号","入库商品名称", "海外仓理货单位", "勾稽入库数量","差异数","入库是否组合","批次号","生产日期","失效日期","勾稽关联时间"};

	private static final String[] KEYS = {"orderCode", "buName", "goodsNo", "goodsName", "purchaseNum", "isEndLabel", "endDate", "warehouseCode", "goodsNo", "goodsName", "tallyingUnitLabel", "warehouseNum", "differenceNum", "isGroupLabel", "batchNo", "productionDate", "overdueDate", "relDate"} ;
	
	@Autowired
	private PurchaseAnalysisService purchaseAnalysisService;

	*//**
	 * 采购入库勾稽明细 访问列表页面
	 * @param model
	 *//*
	@RequestMapping("/toPage.html")
	public String toPage() throws Exception {
		return "/derp/purchase/difference-list";
	}
	
	*//**
	 * 采购入库差异分析 访问列表页面
	 * @param model
	 *//*
	@RequestMapping("/toPageByCheckTheDetails.html")
	public String toPageByCheckTheDetails() throws Exception {
		return "/derp/purchase/checkthedetails-list";
	}
	
	*//**
	 * 采购订单跟踪报表 访问列表页面
	 * @param model
	 *//*
	@RequestMapping("/toPageByFollowing.html")
	public String toPageByFollowing() throws Exception {
		return "/derp/purchase/following-list";
	}

	*//**
	 * 采购入库勾稽明细 获取分页数据
	 * @param model
	 *//*
	@RequestMapping("/listDifference.asyn")
	@ResponseBody
	private ViewResponseBean listDifference(PurchaseAnalysisDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto = purchaseAnalysisService.getlistByPage(dto, user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	*//**
	 * 采购入库差异分析 获取分页数据
	 * @param model
	 *//*
	@RequestMapping("/listCheckTheDetails.asyn")
	@ResponseBody
	private ViewResponseBean listCheckTheDetails(PurchaseAnalysisDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto = purchaseAnalysisService.getCheckTheDetailsListByPage(dto, user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	*//**
	 * 采购订单跟踪报表 获取分页数据
	 * @param model
	 *//*
	@RequestMapping("/listFollowing.asyn")
	@ResponseBody
	private ViewResponseBean listFollowing(PurchaseAnalysisDTO dto) {
		try {
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			dto = purchaseAnalysisService.getFollowingListByPage(dto, user);
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(dto);
	}
	
	*//**
	 * 勾稽明细导出
	 * @param 
	 * @return int
	 * @throws IOException 
	 *//*
	@RequestMapping("/exportAnalysis.asyn")
	public void exportRelation(HttpServletResponse response, HttpServletRequest request, PurchaseAnalysisDTO dto) throws Exception{
		User user= ShiroUtils.getUser();
		String sheetName = "勾稽明细导出";
		dto.setMerchantId(user.getMerchantId());
        //获取导出的信息
        List<PurchaseAnalysisExportDTO> result = purchaseAnalysisService.getListByExport(dto, user);
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, COLUMNS, KEYS, result) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
	}
	
	*//**
	 * 导出采购入库差异分析表
	 * @param session
	 * @param model
	 * @param response
	 *//*
	@RequestMapping("/exportCheckTheDetails.asyn")
	public void exportCheckTheDetails(PurchaseAnalysisDTO dto,HttpServletResponse response,HttpServletRequest request) {
		try {
			User user= ShiroUtils.getUser(); 
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			List<PurchaseAnalysisDTO> result = purchaseAnalysisService.getListCheckTheDetails(dto, user);
			
			String sheetName = "采购入库差异分析表";
	        String[] columns={"采购订单编号", "事业部","商品货号","商品名称","海外仓理货单位","采购数量","入库单号","入库数量","入库商品货号","入库商品名称","差异","采购时间","是否完结入库","完结入库时间"};
	        String[] keys = {"orderCode", "buName", "goodsNo", "goodsName", "tallyingUnitLabel", "purchaseNum", "warehouseCode", "warehouseNum", "goodsNo", "goodsName", "differenceNum", "purchaseCreateDate", "isEndLabel", "endDate"} ;
	        //生成表格
			SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcelByObjList(sheetName, columns, keys, result) ;
			//导出文件
			FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
*/
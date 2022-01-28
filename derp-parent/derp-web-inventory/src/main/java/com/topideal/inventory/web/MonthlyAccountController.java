package com.topideal.inventory.web;

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
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.MonthlyAccountItemService;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.inventory.shiro.ShiroUtils;

/**
 * 库存管理-月库存转结-控制层 
 */
@RequestMapping("/monthlyAccount")
@Controller
public class MonthlyAccountController {

	// 月库存转结表头service
	@Autowired
	private MonthlyAccountService monthlyAccountService;
	
	// 月库存转结表体service
	@Autowired
	private MonthlyAccountItemService monthlyAccountItemService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(HttpSession session,Model model, Long depotId, String settlementMonth)throws Exception  {
		User user= ShiroUtils.getUser();
		model.addAttribute("depotId", depotId);
		model.addAttribute("settlementMonth", settlementMonth);
		return "/inventory/monthlyAccount-list";
	}

	/**
	 * 新增
	 * */
	@RequestMapping("/saveMonthlyAccount.asyn")
	@ResponseBody
	public ViewResponseBean saveMonthlyAccount(Long depotIdAdd,String settlementMonthAdd,HttpSession session) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		try{
			User user= ShiroUtils.getUser(); 			
			retMap = monthlyAccountService.saveMonthlyAccount(user,depotIdAdd,settlementMonthAdd);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}

	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listMonthlyAccount.asyn")
	@ResponseBody
	private ViewResponseBean listMonthlyAccount(HttpSession session,MonthlyAccountDTO model) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model = monthlyAccountService.listDTOMonthlyAccount(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	
	/**
	 * 导出批次库存明细
	 * */
	@RequestMapping("/exportMonthlyAccountMap.asyn")
	@ResponseBody
	private void exportMonthlyAccountMap(HttpSession session, HttpServletResponse response, HttpServletRequest request, Long id){

		try {
			
        	//根据勾选的获取信息
			User user= ShiroUtils.getUser();
   
    		MonthlyAccountModel model=new  MonthlyAccountModel();
    		model.setMerchantId(user.getMerchantId());
    		model.setId(id);
        	List<Map<String,Object>> result1 = monthlyAccountService.exportMonthlyAccountMap(model);
        	MonthlyAccountItemModel monthlyAccountItemModel=new MonthlyAccountItemModel();
        	monthlyAccountItemModel.setMonthlyAccountId(id);
        	List<Map<String,Object>> result2 =	monthlyAccountItemService.exportMonthlyAccountItemMap(monthlyAccountItemModel);
        	String[] columns1={"商家名称","仓库名称","结算年月","期初时间","期末时间","结转时间","结转状态","结转方式"};
        	String[] keys1={"merchant_name","depot_name","settlement_month","first_date","end_date","settlement_date","state","depot_type"};
        	String sheetName1 = "月库存结转" ;
        	
        	ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, result1);
        	
         	String[] columns2={"仓库名称","商品货号","商品名称","商品条码","批次","生产日期","失效日期","库存余量","仓库现存量","单位","库存类型","期末结账库存","是否过期","标准条码"};
        	String[] keys2={"depot_name","goods_no","goods_name","barcode","batch_no","production_date","overdue_date","surplus_num","available_num","unit","type","settlement_num","is_expire","commbarcode"};
        	String sheetName2 = "月库存结转详情" ;
        	
        	ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, result2);
        	
        	List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
    		sheets.add(mainSheet) ;
    		sheets.add(itemSheet) ;
        	
    		//生成表格
    		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
        	//导出文件
        	FileExportUtil.export2007ExcelFile(wb, response, request, "月结库存");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	
	/**
	 * 刷新月结账单数据
	 * @param session
	 * @param response
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/refreshMonthlyBill.asyn")
	@ResponseBody
	private	ViewResponseBean refreshMonthlyBill(HttpSession session, HttpServletResponse response, HttpServletRequest request, String  ids){
	     Map<String,Object> retrunMap=new HashMap<String,Object>();
		try {
		  //校验id是否正确
	        boolean isRight = StrUtils.validateIds(ids);
	        if(isRight){
	        	List<Long> list = StrUtils.parseIds(ids);
	        	for(Long id:list){
	        		MonthlyAccountModel monthlyAccountModel =monthlyAccountService.searchById(id);
	        		if(monthlyAccountModel!=null){	        			
	        			monthlyAccountItemService.refreshMonthlyBill(monthlyAccountModel,monthlyAccountModel.getEndDate(),monthlyAccountModel.getId());
	        		}else{
	        			 return ResponseFactory.error(StateCodeEnum.ERROR_302);
	        		}
	        	}

	            return ResponseFactory.success();
	        }else{
	        	return ResponseFactory.error(StateCodeEnum.ERROR_302);
	        }
   
     }catch(Exception e){
    	 e.printStackTrace();
         return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
     }

	}
	
	@RequestMapping("/updateNotSettlement.asyn")
	@ResponseBody
	private	ViewResponseBean updateNotSettlement(Long  id){
		Map<String, Object> updateNotSettlement= new HashMap<>();
		try {
			updateNotSettlement = monthlyAccountService.updateNotSettlement(id);		  
	        return ResponseFactory.success(updateNotSettlement);  
     }catch(Exception e){
    	 e.printStackTrace();
         return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
     }

	}
}

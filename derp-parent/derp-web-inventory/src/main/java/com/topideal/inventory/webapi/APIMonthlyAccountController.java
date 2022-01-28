package com.topideal.inventory.webapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.MonthlyAccountDTO;
import com.topideal.entity.vo.MonthlyAccountItemModel;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.inventory.service.MonthlyAccountItemService;
import com.topideal.inventory.service.MonthlyAccountService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.MonthlyAccountResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 库存管理-月库存转结-控制层   
 */
@RestController
@RequestMapping("/webapi/inventory/monthlyAccount")
@Api(tags = "库存管理月库存转结控制层")
public class APIMonthlyAccountController {

	// 月库存转结表头service
	@Autowired
	private MonthlyAccountService monthlyAccountService;
	
	// 月库存转结表体service
	@Autowired
	private MonthlyAccountItemService monthlyAccountItemService;

	/**
	 * 访问列表页面
	 * */
	@ApiOperation(value = "访问列表页面")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "settlementMonth", value = "结转月份")
	})
	@PostMapping(value="/toPage.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean<MonthlyAccountResponseDTO> toPage(String token, Long depotId, String settlementMonth)throws Exception  {
		User user= ShiroUtils.getUserByToken(token);
		try {
			MonthlyAccountResponseDTO responseDTO=new MonthlyAccountResponseDTO();
			responseDTO.setDepotId(depotId);
			responseDTO.setSettlementMonth(settlementMonth);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000, responseDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}

	/**
	 * 新增
	 * */

	@ApiOperation(value = "新增")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "depotIdAdd", value = "仓库id"),
			@ApiImplicitParam(name = "settlementMonthAdd", value = "结转月份")
	})
	@PostMapping(value="/saveMonthlyAccount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	public ResponseBean saveMonthlyAccount(String token,Long depotIdAdd,String settlementMonthAdd,HttpSession session) {
		try{
			User user= ShiroUtils.getUserByToken(token);		
			Map<String, Object> retMap = monthlyAccountService.saveMonthlyAccount(user,depotIdAdd,settlementMonthAdd);
			String code = (String) retMap.get("code");
			String message = (String) retMap.get("message");			
			if ("01".equals(code)) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode() ,message);
			}
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);
		} catch (Exception e) {
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}

	}

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取分页数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "begin", value = "开始位置", required = true),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true),
			@ApiImplicitParam(name = "merchantName", value = "公司名称"),
			@ApiImplicitParam(name = "depotId", value = "仓库id"),
			@ApiImplicitParam(name = "settlementMonth", value = "结转 "),
			@ApiImplicitParam(name = "state", value = "状态")	
	})
	@PostMapping(value="/listMonthlyAccount.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	private ResponseBean<MonthlyAccountDTO> listMonthlyAccount(String token,Integer begin, Integer pageSize,String  merchantName,Long depotId,String settlementMonth, String state ) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUserByToken(token);
			MonthlyAccountDTO model=new MonthlyAccountDTO();			
			model.setBegin(begin);
			model.setPageSize(pageSize);
			model.setDepotId(depotId);
			model.setSettlementMonth(settlementMonth);
			model.setState(state);			
			model.setMerchantId(user.getMerchantId());
			model = monthlyAccountService.listDTOMonthlyAccount(model);
			return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,model);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
		}
	}
	
	
	/**
	 * 月库存结转
	 * */
    @ApiOperation(value = "导出月库存结转")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "token", value = "令牌", required = true),
		@ApiImplicitParam(name = "id", value = "id", required = true)
	})
    @GetMapping(value = "/exportMonthlyAccountMap.asyn")
	private void exportMonthlyAccountMap(HttpServletResponse response, HttpServletRequest request,String token,Long id){

		try {
			
        	//根据勾选的获取信息
			User user= ShiroUtils.getUserByToken(token);  
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
	@ApiOperation(value = "刷新月结账单数据")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "ids", value = "月结id 多个应该逗号隔开", required = true)
	})
	@PostMapping(value="/refreshMonthlyBill.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	private	ResponseBean refreshMonthlyBill(HttpSession session, HttpServletResponse response, HttpServletRequest request, String  ids){
		try {
		  //校验id是否正确
	        boolean isRight = StrUtils.validateIds(ids);
	        if (!isRight) {
	        	return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10009);
			}	        
        	List<Long> list = StrUtils.parseIds(ids);
        	for(Long id:list){
        		MonthlyAccountModel monthlyAccountModel =monthlyAccountService.searchById(id);
        		if(monthlyAccountModel!=null){	        			
        			monthlyAccountItemService.refreshMonthlyBill(monthlyAccountModel,monthlyAccountModel.getEndDate(),monthlyAccountModel.getId());
        		}
        	}
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);  
     }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
     }

	}
	
	@ApiOperation(value = "修改为未结转")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "令牌", required = true),
			@ApiImplicitParam(name = "id", value = "月结id", required = true)
	})
	@PostMapping(value="/updateNotSettlement.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)	
	private	ResponseBean updateNotSettlement(Long  id){
		try {
			Map<String, Object> retrunMap = monthlyAccountService.updateNotSettlement(id);		
			String errorMessage = (String) retrunMap.get("errorMessage");	
			String status = (String) retrunMap.get("status");	
			if ("01".equals(status)) {
				return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999.getCode(), errorMessage);
			}		
        	return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000);  
     }catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, e);
     }

	}
}

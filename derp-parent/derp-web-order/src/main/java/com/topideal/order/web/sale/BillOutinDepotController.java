package com.topideal.order.web.sale;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtil;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.sale.BillOutinDepotDTO;
import com.topideal.entity.dto.sale.BillOutinDepotItemDTO;
import com.topideal.entity.vo.sale.BillOutinDepotBatchModel;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;
import com.topideal.order.service.sale.BillOutinDepotService;
import com.topideal.order.shiro.ShiroUtils;

/**
 * 账单出库列表
 * @author gy
 *
 */
@RequestMapping("/billOutinDepot")
@Controller
public class BillOutinDepotController {
	
	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(BillOutinDepotController.class);
	
	private static final String[] MAIN_COLUMNS = {"账单出库单号", "出库仓库","事业部", "结算账单号", "处理类型", "账单出库时间",
					"账单总量", "账单金额", "库存调整类型", "单据状态", "账单来源", "币种"} ;
	
	private static final String[] MAIN_KEYS = {"code", "depotName","buName","billCode", "processingTypeLabel", "deliverDate",
					"totalNum", "totalAmount", "operateTypeLabel", "stateLabel", "billSourceLabel", "currencyLabel"} ;
	
	private static final String[] ITEM_COLUMNS = {"账单出库单号", "商品货号", "平台SKU条码", "标准条码","条码", "商品名称",
					"PO号", "数量", "结算金额"} ;
	
	private static final String[] ITEM_KEYS = {"outinDepotCode", "goodsNo", "platformSku", "commbarcode","barcode",  "goodsName",
					"poNo", "num", "amount"} ;
	
	@Autowired
	BillOutinDepotService billOutinDepotService ;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception {
		return "/derp/sale/bill-outin-depot-list";
	}
	
	/**
	 * 访问详情页面
	 * */
	@RequestMapping("/toDetailsPage.html")
	public String toDetailsPage(Model model, Long id)throws Exception{
		BillOutinDepotDTO dto = billOutinDepotService.searchDetail(id);
		model.addAttribute("detail", dto);
		return "/derp/sale/bill-outin-depot-details";
	}
	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listBillOutinDepot.asyn")
	@ResponseBody
	public ViewResponseBean listBillOutinDepot(BillOutinDepotDTO dto,Model model) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			dto = billOutinDepotService.listBillOutinDepotByPage(dto,user);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	
	/**
	 * 访问导入页面
	 * */
	@RequestMapping("/toImportPage.html")
	public String toImportPage()throws Exception{
		return "/derp/sale/bill-outin-depot-import";
	}
	
	/**
	 * 手工导入
	 * @param file
	 * @return
	 */
	@RequestMapping("importBillOutinDepot.asyn")
	@ResponseBody
	public ViewResponseBean importBillOutinDepot(@RequestParam(value = "file", required = false) MultipartFile file) {
		Map<String, Object> resultMap = new HashMap<String, Object>();// 返回的结果集
		try {
			if (file == null) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			
			List<Map<String, String>> data = ExcelUtilXlsx.parseSheetOne(file.getInputStream());
			
			if (data == null) {// 数据为空
				return ResponseFactory.error(StateCodeEnum.ERROR_302);
			}
			
			User user= ShiroUtils.getUser(); 
			resultMap = billOutinDepotService.importBillOutinDepot(data, user);
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(resultMap);
	}
	
	/**
	 * 详情页分页查询明细
	 * @param model
	 * @return
	 */
	@RequestMapping("/listBillOutinDepotItem.asyn")
	@ResponseBody
	public ViewResponseBean listBillOutinDepotItem(BillOutinDepotItemModel model) {
		try{
			// 响应结果集
			model = billOutinDepotService.listBillOutinDepotItemByPage(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 详情页分页查询批次
	 * @param model
	 * @return
	 */
	@RequestMapping("/listBillOutinDepotBatch.asyn")
	@ResponseBody
	public ViewResponseBean listBillOutinDepotBatch(BillOutinDepotBatchModel model) {
		try{
			// 响应结果集
			model = billOutinDepotService.listBillOutinDepotBatchByPage(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	/**
	 * 逻辑删除
	 * @param ids
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("/delBillOutinDepot.asyn")
	@ResponseBody
	public ViewResponseBean delBillOutinDepot(String ids) {
		try {
			// 校验id是否正确
			boolean isRight = StrUtils.validateIds(ids);
			if (!isRight) {
				// 输入信息不完整
				return ResponseFactory.error(StateCodeEnum.ERROR_303);
			}
			List<Long> list = StrUtils.parseIds(ids);
			boolean b = billOutinDepotService.delBillOutinDepot(list);
			if (!b) {
				return ResponseFactory.error(StateCodeEnum.ERROR_301);
			}
		} catch (SQLException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
		} catch (NullPointerException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_304, e);
		} catch (RuntimeException e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_301, e);
		} catch (Exception e) {
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success();
	}
	
	/**
	 * 获取选中订单的所有商品和对应数量（相同商品数量叠加）
	 * */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getOrderGoodsInfo.asyn")
	@ResponseBody
	public ViewResponseBean getOrderGoodsInfo(String ids,String type) {
		Map<String,Integer> map =new HashMap<String,Integer>();
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<Long> list = StrUtils.parseIds(ids);
			// 响应结果集
            map = billOutinDepotService.getOrderGoodsInfo(list);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(RuntimeException e){
            return ResponseFactory.error(StateCodeEnum.ERROR_301,e);
        }catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		
		return ResponseFactory.success(map);
	}
	
	/**
	 * 审核
	 * */
	@SuppressWarnings("unchecked")
	@RequestMapping("/auditBillOutinDepot.asyn")
	@ResponseBody
	public ViewResponseBean auditBillOutinDepot(String ids) {
		Map<String,Object> result = new HashMap<>();
		int count = 0; //失败条数
		int total = 0; //总条数
		StringBuffer failureMsg = new StringBuffer();
		try{
			//校验id是否正确
            boolean isRight = StrUtils.validateIds(ids);
            if(!isRight){
                //输入信息不完整
                return ResponseFactory.error(StateCodeEnum.ERROR_303);
            }
            List<Long> list = StrUtils.parseIds(ids);
            total = list.size();
            User user= ShiroUtils.getUser(); 
			for (Long id : list) {
				try {
					Map<String,String> resultDetail = billOutinDepotService.auditSaleOutDepot(id,user);
					if (resultDetail.containsKey("code") && resultDetail.get("code").equals("01")) {
						count ++;
						failureMsg.append(resultDetail.get("msg"));
					}
				} catch (Exception e) {
					count ++;
					failureMsg.append(e.getMessage()+"\n");
				}
			}
			result.put("success", total-count);
			result.put("failure", count);
			result.put("failureMsg", failureMsg.toString());
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(result);
	}
	
	/**
	 * 获取导出出库清单的数量
	 */
	@RequestMapping("/getOrderCount.asyn")
	@ResponseBody
	private ViewResponseBean getOrderCount(BillOutinDepotDTO dto) throws Exception{
		Map<String,Object> data=new HashMap<String,Object>();
		try{
			User user= ShiroUtils.getUser(); 	
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			// 响应结果集
			Integer result = billOutinDepotService.getOrderCount(dto);
			data.put("total",result);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(data);
	}
	
	/**
	 * 导出销售出库单
	 * */
	@RequestMapping("/exportBillOutinDepot.asyn")
	@ResponseBody
	private void exportBillOutinDepot(HttpServletResponse response, HttpServletRequest request,BillOutinDepotDTO dto) throws Exception{
		User user= ShiroUtils.getUser(); 	
		// 设置商家id
		dto.setMerchantId(user.getMerchantId());
		
		List<ExportExcelSheet> sheetList = new ArrayList<ExportExcelSheet>() ;
		
		// 响应结果集
		List<BillOutinDepotDTO> mainList = billOutinDepotService.getExportMainList(dto,user);
		String mainSheetName = "账单出库单";
		
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(mainSheetName, MAIN_COLUMNS, MAIN_KEYS, mainList);
		sheetList.add(mainSheet) ;
		
		List<BillOutinDepotItemDTO> itemList = billOutinDepotService.getExportItemList(dto,user);
		String itemSheetName = "出库商品明细";
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(itemSheetName, ITEM_COLUMNS, ITEM_KEYS, itemList);
		sheetList.add(itemSheet) ;
		
        //生成表格
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheetList) ;
		//导出文件
		FileExportUtil.export2007ExcelFile(wb, response, request, mainSheetName);
	}
}

package com.topideal.inventory.web;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryBatchDTO;
import com.topideal.inventory.service.InventoryBatchService;
import com.topideal.inventory.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * 库存管理-批次库存明细-控制层 
 */
@RequestMapping("/inventoryBatch")
@Controller
public class InventoryBatchController {

	// 批次库存明细service
	@Autowired
	private InventoryBatchService inventoryBatchService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(HttpSession session,Model model)throws Exception  {
		User user= ShiroUtils.getUser();
		
		return "/inventory/inventoryBatch-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryBatch.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryBatch(HttpSession session,InventoryBatchDTO model) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model = inventoryBatchService.listInventoryBatch(model);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
		}catch(Exception e){
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(model);
	}
	
	
	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/inventoryBatchToPage.html")
	public String inventoryBatchToPage(HttpSession session,Model model,String goodsNo,Long depotId)throws Exception  {
		User user= ShiroUtils.getUser();
		model.addAttribute("goodsNo", goodsNo);
		model.addAttribute("depotId", depotId);
		return "/inventory/inventoryBatch-list";
	}
	
	
	

	/**
	 * 导出批次库存明细
	 * */
	@RequestMapping("/exportInventoryBatch.asyn")
	@ResponseBody
	private void exportInventoryBatch(HttpSession session, HttpServletResponse response, HttpServletRequest request, 
			Long depotId,String goodsNo,String batchNo,String validityType,Long brandId,String barcode){

		try {
			
			String sheetName = "批次库存明细";
        	//根据勾选的获取信息
			User user= ShiroUtils.getUser();   
    		InventoryBatchDTO model=new  InventoryBatchDTO();
    		model.setMerchantId(user.getMerchantId());
    		model.setDepotId(depotId);
    		model.setGoodsNo(goodsNo);
    		model.setBatchNo(batchNo);
    		model.setValidityType(validityType);
    		model.setBrandId(brandId);
    		model.setBarcode(barcode);
        	List<Map<String,Object>> result = inventoryBatchService.exportInventoryBatchMap(model);
        	String[] columns={"商家名称","仓库名称","商品货号","商品名称","批次号","生产日期","失效日期","库存类型","是否过期","库存数量","理货单位","托盘号","条码","品牌","标准条码"};
        	String[] keys={"merchant_name","depot_name","goods_no","goods_name","batch_no","production_date","overdue_date","type","is_expire","surplus_num","unit","lpn","barcode","brand_name","commbarcode"};
        	//生成表格
        	SXSSFWorkbook wb = ExcelUtilXlsx.createSXSSExcel(sheetName, columns, keys, result);
        	//导出文件
        	FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
	}
	
	/**
	 * 根据仓库ID  查询仓库所有批次是否存在批次效期为空
	 * @param session
	 * @param depotId
	 * @return
	 */
	@RequestMapping("/getBatchValidation.asyn")
	@ResponseBody
	public String getBatchValidation (HttpSession session,Long depotId){
		JSONObject jSONObject= new JSONObject();
		try{
			 jSONObject= inventoryBatchService.getBatchValidation(depotId);
			return jSONObject.toString();        
        }catch(Exception e){
        	jSONObject.put("status", "0");
        	jSONObject.put("msg", "库存查询异常");
            return jSONObject.toString();  
        }
	}
	
}

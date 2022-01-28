package com.topideal.inventory.web;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryBatchSnapshotDTO;
import com.topideal.entity.vo.InventoryBatchSnapshotModel;
import com.topideal.inventory.service.InventoryBatchSnapshotService;
import com.topideal.inventory.shiro.ShiroUtils;
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
 * 库存管理-库存批次快照表-控制层 
 */
@RequestMapping("/inventoryBatchSnapshot")
@Controller
public class InventoryBatchSnapshotController {

	// 库存批次快照service
	@Autowired
	private InventoryBatchSnapshotService inventoryBatchSnapshotService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session)throws Exception  {
		User user= ShiroUtils.getUser();
		return "/inventory/inventoryBatchSnapshot-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryBatchSnapshot.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryBatchSnapshot(InventoryBatchSnapshotDTO model,HttpSession session) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model = inventoryBatchSnapshotService.listInventoryBatchSnapshotModel(model);
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
	 * 导出库存批次快照
	 * */
	@RequestMapping("/exportInventoryBatchSnapshotModelMap.asyn")
	@ResponseBody
	private void exportInventoryBatchSnapshotModelMap(HttpSession session, HttpServletResponse response, HttpServletRequest request,Long depotId,String goodsNo,String batchNo, String snapshotDate){

		try {
			User user= ShiroUtils.getUser();
			InventoryBatchSnapshotModel model =new  InventoryBatchSnapshotModel();
		    model.setMerchantId(user.getMerchantId());
		    model.setDepotId(depotId);
		    model.setGoodsNo(goodsNo);
		    model.setBatchNo(batchNo);
		    model.setSnapshotDate(snapshotDate);
        	String sheetName = "库存批次快照";
        	//根据勾选的获取信息
        	List<Map<String,Object>> result = inventoryBatchSnapshotService.exportInventoryBatchSnapshotModelMap(model);
        	String[] columns={"商家名称","仓库名称","商品货号","商品名称","商品条码","品牌名称","批次号","生产日期","失效日期","库存余量","仓库现存量","库存类型","是否过期","理货单位","托盘号","创建时间"};
        	String[] keys={"merchant_name","depot_name","goods_no","goods_name","barcode","brand_name","batch_no","production_date","overdue_date","surplus_num","available_num","type","is_expire","unit","lpn","create_date"};
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

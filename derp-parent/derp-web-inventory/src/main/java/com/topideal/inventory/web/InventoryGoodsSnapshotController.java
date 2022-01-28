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
import com.topideal.entity.dto.InventoryGoodsSnapshotDTO;
import com.topideal.entity.vo.InventoryGoodsSnapshotModel;
import com.topideal.inventory.service.InventoryGoodsSnapshotService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.tools.DownloadExcelUtil;

/**
 * 库存管理-库存商品快照表-控制层 
 */
@RequestMapping("/inventoryGoodsSnapshot")
@Controller
public class InventoryGoodsSnapshotController {

	// 库存商品快照service
	@Autowired
	private InventoryGoodsSnapshotService inventoryGoodsSnapshotService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session)throws Exception  {
		User user= ShiroUtils.getUser();
		return "/inventory/inventoryGoodsSnapshot-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryGoodsSnapshot.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryGoodsSnapshot(InventoryGoodsSnapshotDTO model,HttpSession session) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model = inventoryGoodsSnapshotService.listInventoryGoodsSnapshotModel(model);
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
	 * 导出库存商品快照
	 * */
	@RequestMapping("/exportInventoryGoodsSnapshotModelMap.asyn")
	@ResponseBody
	private void exportInventoryGoodsSnapshotModelMap(HttpSession session, HttpServletResponse response, HttpServletRequest request,Long depotId,String goodsNo,String snapshotDate){

		try {
			User user= ShiroUtils.getUser();
			InventoryGoodsSnapshotModel model =new  InventoryGoodsSnapshotModel();
		    model.setMerchantId(user.getMerchantId());
		    model.setDepotId(depotId);
		    model.setGoodsNo(goodsNo);
            model.setSnapshotDate(snapshotDate);
		    
        	String sheetName = "库存商品快照";
        	//根据勾选的获取信息
        	List<Map<String,Object>> result = inventoryGoodsSnapshotService.exportInventoryGoodsSnapshotModelMap(model);
        	String[] columns={"商家名称","仓库名称","商品货号","商品名称","库存数量","冻结数量","坏品数量","过期数量","可用量","理货单位","快照时间"};
        	String[] keys={"merchant_name","depot_name","goods_no","goods_name","surplus_num","freeze_num","bad_num","expire_num","available_num","unit","create_date"};        	       	
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

package com.topideal.inventory.web;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryRealTimeSnapshotDTO;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.inventory.service.InventoryRealTimeSnapshotService;
import com.topideal.inventory.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
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
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-库存批次快照表-控制层 
 * 杨创
 * 2019-06-14
 */
@RequestMapping("/inventoryRealTimeSnapshot")
@Controller
public class InventoryRealTimeSnapshotController {

	// 库存批次快照service
	@Autowired
	private InventoryRealTimeSnapshotService inventoryRealTimeSnapshotService;
	



	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session)throws Exception  {
		User user= ShiroUtils.getUser();
		return "/inventory/inventoryRealTimeSnapshot-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listInventoryRealTimeSnapshot.asyn")
	@ResponseBody
	private ViewResponseBean listInventoryBatchSnapshot(InventoryRealTimeSnapshotDTO model,String createDateStr ,HttpSession session) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			Timestamp createDate=null;//创建时间
		    if (StringUtils.isNotBlank(createDateStr)) {
		    	createDate = Timestamp.valueOf(createDateStr +" 00:00:00");
			}
		    model.setCreateDate(createDate);
			model = inventoryRealTimeSnapshotService.listInventoryRealTimeSnapshotModel(model);
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
	private void exportInventoryBatchSnapshotModelMap(HttpSession session, HttpServletResponse response, HttpServletRequest request,Long depotId,String goodsNo,String batchNo, String createDateStr){

		try {
			User user= ShiroUtils.getUser();
			InventoryRealTimeSnapshotModel model =new  InventoryRealTimeSnapshotModel();
		    model.setMerchantId(user.getMerchantId());
		    model.setDepotId(depotId);
		    model.setGoodsNo(goodsNo);
		    model.setBatchNo(batchNo);
		    Timestamp createDate=null;//创建时间
		    if (StringUtils.isNotBlank(createDateStr)) {
		    	createDate = Timestamp.valueOf(createDateStr +" 00:00:00");
			}
		    model.setCreateDate(createDate);
        	String sheetName = "实时库存快照";
        	//根据勾选的获取信息
        	List<Map<String,Object>> result = inventoryRealTimeSnapshotService.exportInventoryRealTimeSnapshotModelMap(model);
        	String[] columns={"商家名称","仓库名称","商品货号","商品名称","OPG号","商品条码","批次号","生产日期","失效日期","库存数量","冻结数量","锁定数量","可用数量","库存类型","理货单位","托盘号","创建时间"};
        	String[] keys={"merchant_name","depot_name","goods_no","goods_name","opg_code","barcode","batch_no","production_date","overdue_date","qty","real_frozen_stock_num","lock_stock_num","real_stock_num","stock_type","unit","lpn","create_date"};
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

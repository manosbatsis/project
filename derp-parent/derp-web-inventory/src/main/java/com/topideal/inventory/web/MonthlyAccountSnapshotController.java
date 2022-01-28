package com.topideal.inventory.web;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.MonthlyAccountSnapshotDTO;
import com.topideal.entity.vo.MonthlyAccountSnapshotModel;
import com.topideal.inventory.service.MonthlyAccountSnapshotService;
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
 * 库存管理-月结快照表-控制层 
 */
@RequestMapping("/monthlyAccountSnapshot")
@Controller
public class MonthlyAccountSnapshotController {

	//月结库存快照表service
	@Autowired
	private MonthlyAccountSnapshotService monthlyAccountSnapshotService;

	/**
	 * 访问列表页面
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session)throws Exception  {
		User user= ShiroUtils.getUser();
		return "/inventory/monthlyAccountSnapshot-list";
	}


	/**
	 * 获取分页数据
	 * */
	@RequestMapping("/listMonthlyAccountSnapshot.asyn")
	@ResponseBody
	private ViewResponseBean listMonthlyAccountSnapshot(MonthlyAccountSnapshotDTO model,HttpSession session) {
		try{
			// 响应结果集
			User user= ShiroUtils.getUser();
			model.setMerchantId(user.getMerchantId());
			model = monthlyAccountSnapshotService.listMonthlyAccountSnapshotModel(model);
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
	 * 导出月结快照
	 * */
	@RequestMapping("/exportMonthlyAccountSnapshotModelMap.asyn")
	@ResponseBody
	private void exportMonthlyAccountSnapshotModelMap(HttpSession session, HttpServletResponse response, HttpServletRequest request,Long depotId,String goodsNo,String batchNo,String settlementMonth){

		try {
			User user= ShiroUtils.getUser();
			MonthlyAccountSnapshotModel model =new  MonthlyAccountSnapshotModel();
		    model.setMerchantId(user.getMerchantId());
		    model.setDepotId(depotId);
		    model.setGoodsNo(goodsNo);
		    model.setBatchNo(batchNo);
		    //model.setSnapshotDate(snapshotDate);
		    model.setSettlementMonth(settlementMonth);
        	String sheetName = "月结快照";
        	//根据勾选的获取信息
        	List<Map<String,Object>> result = monthlyAccountSnapshotService.exportMonthlyAccountSnapshotModelMap(model);
        	String[] columns={"商家名称","结转月份","仓库名称","商品货号","商品名称","批次号","生产日期","失效日期","库存余量","仓库现存量","库存类型","结转状态","标准条码"};
        	String[] keys={"merchant_name","settlement_month","depot_name","goods_no","goods_name","batch_no","production_date","overdue_date","surplus_num","available_num","type","state","commbarcode"};
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

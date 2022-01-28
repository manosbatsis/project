package com.topideal.inventory.webapi;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.entity.dto.InventoryRealTimeSnapshotDTO;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.inventory.service.InventoryRealTimeSnapshotService;
import com.topideal.inventory.shiro.ShiroUtils;
import com.topideal.inventory.webapi.form.InventoryRealTimeSnapshotForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 库存管理-库存批次快照表-控制层 
 * 杨创
 * 2019-06-14
 */
@RestController
@RequestMapping("/webapi/inventory/inventoryRealTimeSnapshot")
@Api(tags = "实时库存快照表")
public class APIInventoryRealTimeSnapshotController {

	// 库存批次快照service
	@Autowired
	private InventoryRealTimeSnapshotService inventoryRealTimeSnapshotService;

	/**
	 * 获取分页数据
	 * */
	@ApiOperation(value = "获取实时库存快照分页数据")
	@PostMapping(value = "/listInventoryRealTimeSnapshot.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	private ResponseBean<InventoryRealTimeSnapshotDTO> listInventoryBatchSnapshot(InventoryRealTimeSnapshotForm form) {
		InventoryRealTimeSnapshotDTO dto = new InventoryRealTimeSnapshotDTO();
		try{
			// 响应结果集
			User user= ShiroUtils.getUserByToken(form.getToken());
			BeanUtils.copyProperties(form, dto);
			dto.setMerchantId(user.getMerchantId());
			Timestamp createDate=null;//创建时间
		    if (StringUtils.isNotBlank(form.getCreateDateStr())) {
		    	createDate = Timestamp.valueOf(form.getCreateDateStr() +" 00:00:00");
			}
			dto.setCreateDate(createDate);
			dto = inventoryRealTimeSnapshotService.listInventoryRealTimeSnapshotModel(dto);
		}catch(Exception e){
			e.printStackTrace();
			return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);
		}
		return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,dto);
	}
	
	
	/**
	 * 导出库存批次快照
	 * */
	@ApiOperation(value = "导出实时库存快照")
	@GetMapping(value = "/exportInventoryBatchSnapshotModelMap.asyn")
	private void exportInventoryBatchSnapshotModelMap(HttpSession session, HttpServletResponse response, HttpServletRequest request,InventoryRealTimeSnapshotForm form){
		try {
			User user= ShiroUtils.getUserByToken(form.getToken());
			InventoryRealTimeSnapshotModel model =new  InventoryRealTimeSnapshotModel();
			BeanUtils.copyProperties(form, model);
		    model.setMerchantId(user.getMerchantId());
			Timestamp createDate=null;//创建时间
			if (StringUtils.isNotBlank(form.getCreateDateStr())) {
				createDate = Timestamp.valueOf(form.getCreateDateStr() +" 00:00:00");
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
			e.printStackTrace();
		}
	}
}

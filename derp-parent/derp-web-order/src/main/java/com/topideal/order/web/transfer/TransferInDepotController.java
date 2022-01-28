package com.topideal.order.web.transfer;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.ExcelUtilXlsx;
import com.topideal.common.tools.FileExportUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.common.tools.excel.ExportExcelSheet;
import com.topideal.entity.dto.transfer.TransferInDepotDTO;
import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.transfer.TransferInDepotItemService;
import com.topideal.order.service.transfer.TransferInDepotService;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调拨入库 控制层
 * @author yucaifu
 */
@RequestMapping("/transferIn")
@Controller
public class TransferInDepotController {

	
	@Autowired
	private TransferInDepotService transferInDepotService;
    //仓库信息service
    @Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	@Autowired
	private TransferInDepotItemService transferInDepotItemService;
	
	@Autowired
	private TransferOrderService transferOrderService;
	

	/**
	 * 访问列表页面
	 * @param model
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model,HttpSession session) throws Exception{
		User user= ShiroUtils.getUser();
		return "/derp/transfer/transferindepot-list";
	}
	/**
	 * 获取分页数据
	 * @param dto
	 * */
	@RequestMapping("/transferInDepot.asyn")
	@ResponseBody
	private ViewResponseBean transferInDepot(TransferInDepotDTO dto) {
		try{

			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());
			
			// 响应结果集
			dto = transferInDepotService.searchByPage(dto, user);
		
		}catch(Exception e){
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
		}
		return ResponseFactory.success(dto);
	}
	/**
	 * 详情
	 * @param id 调拨订单id
	 * */
	@RequestMapping("/toDetailPage.html")
	public String toDetailPage(Model model,String id) throws Exception{
		
		//查询调拨入库
		TransferInDepotDTO transferInDepot = transferInDepotService.searchById(Long.valueOf(id));
		
		//查询调拨订单
		TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(transferInDepot.getTransferOrderId());
		//查询调出仓库
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("depotId", transferOrder.getOutDepotId());
		DepotInfoMongo outDepotModel= depotInfoMongoDao.findOne(paraMap);
				
		//查询调拨出库详情列表
//		TransferInDepotItemModel itemModel = new TransferInDepotItemModel();
//		itemModel.setTransferDepotId(transferInDepot.getId());
		List<TransferInDepotItemDTO> itemList = transferInDepotItemService.searchItemList(transferInDepot.getId());
		
		model.addAttribute("transferInDepot", transferInDepot);
		model.addAttribute("transferOrder", transferOrder);
		model.addAttribute("outDepotModel", outDepotModel);
		model.addAttribute("itemList", itemList);
		
		return "/derp/transfer/transferindepot-detail";
	}
	/**
	 * 导出统计
	 * */
	@RequestMapping("/exportInDepotCount.asyn")
	@ResponseBody
	public ViewResponseBean exportInDepotCount(TransferInDepotDTO dto) throws Exception{

		//根据勾选的获取信息
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

		Integer count = transferInDepotService.listForCount(dto, user);
		Integer max = DERP.EXPORT_MAX;
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("code", "00");
		retMap.put("message", "检查通过");
	   	if(count.intValue()>max.intValue()){
			retMap.put("code", "01");
			retMap.put("message", "导出数量超过"+max+"请分多次导出");
		}
        return ResponseFactory.success(retMap);
	}
	/**
	 * 导出
	 * */
	@RequestMapping("/exportInDepot.asyn")
	@ResponseBody
	public String exportInDepot(HttpServletResponse response, HttpServletRequest request,TransferInDepotDTO dto) throws Exception{
		//根据勾选的获取信息
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

		List<Map<String,Object>> list1 = transferInDepotService.listForMap(dto, user);
		if(list1!=null&&list1.size()>0){
			for(Map<String, Object> map:list1){
				String status = (String) map.get("status");
				map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.transferInDepot_statusList,status));

			}
		}
		List<Map<String,Object>> listitem = transferInDepotService.listForMapItem(dto, user);
		if(listitem!=null&&listitem.size()>0){
			for(Map<String, Object> map:listitem){
				String tallyingUnit = (String) map.get("tallying_unit");//理货单位 00-托盘 01-箱 02-件
				if(!StringUtils.isEmpty(tallyingUnit)) {
					map.put("tallying_unit", DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit));
				}
				/*if(!StringUtils.isEmpty(tallyingUnit)&&tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_00)){
					map.put("tallying_unit", "托盘");
				}else if(!StringUtils.isEmpty(tallyingUnit)&&tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_01)){
					map.put("tallying_unit", "箱");
				} else if(!StringUtils.isEmpty(tallyingUnit)&&tallyingUnit.equals(DERP.ORDER_TALLYINGUNIT_02)){
					map.put("tallying_unit", "件");
				}*/
			}
		}
		//基础信息
		String sheetName1 = "表头";
		String[] columns1={"调拨入库单号","单据状态","企业调拨单号","调出仓库","调入仓库","事业部","调出公司名称","调入公司名称","合同号","调拨入库时间"};
		String[] keys1={"code","status","transfer_order_code","out_depot_name","in_depot_name","bu_name","merchant_name","in_customer_name","contract_no","create_date"};
		//商品信息
		String sheetName2 = "商品信息";
		String[] columns2={"调拨入库单号","调入商品编号","调入商品货号","调入商品名称","调入数量","正常数量","坏品数量","过期数量","调入批次","生产日期","失效日期","理货单位"};
		String[] keys2={"code","in_goods_code","in_goods_no","in_goods_name","tranfer_num_all","transfer_num","worn_num","expire_num","transfer_batch_no","production_date","overdue_date","tallying_unit"};
		//生成表格
		
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, list1);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, listitem);
		
		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		String fileName = "调拨入库单"+TimeUtils.formatDay();
		FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
        return null;
	}

	/**
	 * 上架入库
	 */
	@RequestMapping("/saveTransferInDepot.asyn")
	@ResponseBody
	public ViewResponseBean saveTransferInDepot(String json) {
		Map<String, String> retMap = new HashMap<>();
		try {
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("goodsList",TransferInDepotItemDTO.class);
			TransferInDepotDTO model = (TransferInDepotDTO) JSONObject.toBean(jsonData, TransferInDepotDTO.class, classMap);
			User user= ShiroUtils.getUser();
			retMap = transferInDepotService.saveTransferInDepot(model, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}

}

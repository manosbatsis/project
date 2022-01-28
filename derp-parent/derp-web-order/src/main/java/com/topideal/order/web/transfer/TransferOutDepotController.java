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
import com.topideal.entity.dto.transfer.TransferOrderDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotDTO;
import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.order.service.transfer.TransferOrderService;
import com.topideal.order.service.transfer.TransferOutDepotItemService;
import com.topideal.order.service.transfer.TransferOutDepotService;
import com.topideal.order.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 调拨出库 控制层
 * @author yucaifu
 */
@RequestMapping("/transferOut")
@Controller
public class TransferOutDepotController {

	
	@Autowired
	private TransferOutDepotService transferOutDepotService;
	// 仓库信息service
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	//@Autowired
	//private UserInfoService userInfoService;
	
	@Resource
	private TransferOutDepotItemService transferOutDepotItemService;
	
	@Autowired
	private TransferOrderService transferOrderService;
	

	/**
	 * 访问列表页面
	 * @param model
	 * */
	@RequestMapping("/toPage.html")
	public String toPage(Model model) throws Exception{
		/*UserInfoModel user= ShiroUtils.getUser();
		List<SelectBean> depotBean = depotService.getSelectBean(user.getMerchantId());
		model.addAttribute("depotBean", depotBean);*/
		return "/derp/transfer/transferoutdepot-list";
	}
	/**
	 * 获取分页数据
	 * @param dto
	 * */
	@RequestMapping("/transferOutDepot.asyn")
	@ResponseBody
	private ViewResponseBean transferOutDepot(TransferOutDepotDTO dto) {
		try{
			User user= ShiroUtils.getUser();
			// 设置商家id
			dto.setMerchantId(user.getMerchantId());

			// 响应结果集
			dto = transferOutDepotService.listTransferOutDepotPage(dto, user);
		}catch(SQLException e){
			return ResponseFactory.error(StateCodeEnum.ERROR_304,e);
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
	public String toDetailPage(Model model,Long id) throws Exception{
		
		//查询调拨出库
		TransferOutDepotDTO transferOutDepot = transferOutDepotService.searchDetail(id);
		
		//查询调拨订单
		TransferOrderDTO transferOrder = transferOrderService.searchTransferOrderById(transferOutDepot.getTransferOrderId());
		
		//查询调出仓库
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("depotId", transferOrder.getOutDepotId());
		DepotInfoMongo outDepotModel= depotInfoMongoDao.findOne(paraMap);
		
		//查询调拨出库详情列表
//		TransferOutDepotItemModel itemModel = new TransferOutDepotItemModel();
//		itemModel.setTransferDepotId(transferOutDepot.getId());
		List<TransferOutDepotItemDTO> itemList = transferOutDepotItemService.searchItemList(transferOutDepot.getId());
		
		model.addAttribute("transferOutDepot", transferOutDepot);
		model.addAttribute("transferOrder", transferOrder);
		model.addAttribute("outDepotModel", outDepotModel);
		model.addAttribute("itemList", itemList);
		
		return "/derp/transfer/transferoutdepot-detail";
	}
	
	/**
	 * 导出统计
	 * */
	@RequestMapping("/exportOutDepotCount.asyn")
	@ResponseBody
	public ViewResponseBean exportOutDepotCount(TransferOutDepotDTO dto) throws Exception{

		//根据勾选的获取信息
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

		Integer count = transferOutDepotService.listForCount(dto, user);
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
	@RequestMapping("/exportOutDepot.asyn")
	@ResponseBody
	public String exportOutDepot(HttpServletResponse response, HttpServletRequest request,TransferOutDepotDTO dto) throws Exception{
		//根据勾选的获取信息
		User user= ShiroUtils.getUser();
		dto.setMerchantId(user.getMerchantId());

		List<Map<String,Object>> list1 = transferOutDepotService.listForMap(dto, user);
		if(list1!=null&&list1.size()>0){
			for(Map<String, Object> map:list1){
				String status = (String) map.get("status");
				if (StringUtils.isNotBlank(status)) {
					map.put("status", DERP_ORDER.getLabelByKey(DERP_ORDER.transferOutDepot_statusList, status));
				}
			}
		}
		List<Map<String,Object>> listitem = transferOutDepotService.listForMapItem(dto, user);
		//基础信息
		String sheetName1 = "表头";
		String[] columns1={"调拨出库单号","单据状态","企业调拨单号","调出仓库","调入仓库","事业部","调出公司名称","调入公司名称","合同号","调拨出库时间"};
		String[] keys1={"code","status","transfer_order_code","out_depot_name","in_depot_name","bu_name","merchant_name","in_customer_name","contract_no","create_date"};
		//商品信息
		String sheetName2 = "商品信息";
		String[] columns2={"调拨出库单号","调出商品编号","调出商品货号","调出商品名称","调拨出库数量","正常数量","坏品数量","调出批次","生产日期","失效日期"};
		String[] keys2={"code","out_goods_code","out_goods_no","out_goods_name","transfer_num","goods_num","worn_num","transfer_batch_no","production_date","overdue_date"};
		//生成表格
		ExportExcelSheet mainSheet = ExcelUtilXlsx.createSheet(sheetName1, columns1, keys1, list1);
		ExportExcelSheet itemSheet = ExcelUtilXlsx.createSheet(sheetName2, columns2, keys2, listitem);
		
		List<ExportExcelSheet> sheets = new ArrayList<ExportExcelSheet>() ;
		sheets.add(mainSheet) ;
		sheets.add(itemSheet) ;
		
		SXSSFWorkbook wb = ExcelUtilXlsx.createMutiSheetExcel(sheets) ;
		//导出文件
		String fileName = "调拨出库单"+TimeUtils.formatDay();
		FileExportUtil.export2007ExcelFile(wb, response, request,fileName);
        return null;
	}

	/**
	 * 打托出库
	 */
	@RequestMapping("/saveTransferOutDepot.asyn")
	@ResponseBody
	public ViewResponseBean saveTransferOutDepot(String json) {
		Map<String, String> retMap = new HashMap<>();
		try {
			JSONObject jsonData=JSONObject.fromObject(json);
			Map classMap = new HashMap();
			classMap.put("goodsList",TransferOutDepotItemDTO.class);
			TransferOutDepotDTO model = (TransferOutDepotDTO) JSONObject.toBean(jsonData, TransferOutDepotDTO.class, classMap);
			User user= ShiroUtils.getUser();
			retMap = transferOutDepotService.saveTransferOutDepot(model, user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		}
		return ResponseFactory.success(retMap);
	}
}

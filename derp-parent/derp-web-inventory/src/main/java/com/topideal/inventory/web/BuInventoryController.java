/*
 * package com.topideal.inventory.web;
 * 
 * import java.sql.SQLException; import java.util.ArrayList; import
 * java.util.Date; import java.util.HashMap; import java.util.List; import
 * java.util.Map;
 *  
 * import javax.servlet.http.HttpServletRequest; import
 * javax.servlet.http.HttpServletResponse; import
 * javax.servlet.http.HttpSession;
 * 
 * import org.apache.poi.xssf.streaming.SXSSFWorkbook; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.ResponseBody;
 * 
 * import com.topideal.common.constant.DERP_INVENTORY; import
 * com.topideal.common.enums.MQInventoryEnum; import
 * com.topideal.common.system.auth.User; import
 * com.topideal.common.system.mq.RMQProducer; import
 * com.topideal.common.system.web.ResponseFactory; import
 * com.topideal.common.system.web.StateCodeEnum; import
 * com.topideal.common.system.web.ViewResponseBean; import
 * com.topideal.common.tools.ExcelUtilXlsx; import
 * com.topideal.common.tools.FileExportUtil; import
 * com.topideal.common.tools.TimeUtils; import
 * com.topideal.common.tools.excel.ExportExcelSheet; import
 * com.topideal.dao.MonthlyAccountDao; import
 * com.topideal.entity.dto.BuInventoryDTO; import
 * com.topideal.entity.dto.BuInventoryItemDTO; import
 * com.topideal.entity.vo.BuInventoryModel; import
 * com.topideal.entity.vo.MonthlyAccountModel; import
 * com.topideal.inventory.service.BuInventoryService; import
 * com.topideal.inventory.shiro.ShiroUtils; import
 * com.topideal.mongo.dao.DepotMerchantRelMongoDao; import
 * com.topideal.mongo.dao.FinanceCloseAccountsMongoDao; import
 * com.topideal.mongo.entity.DepotMerchantRelMongo;
 * 
 * import net.sf.json.JSONObject;
 * 
 * 
 *//**
	 * 事业部库存表
	 */
/*
 * @RequestMapping("/buInventory")
 * 
 * @Controller public class BuInventoryController {
 * 
 * // 事业部库存表service
 * 
 * @Autowired private BuInventoryService buInventoryService;
 * 
 * @Autowired private RMQProducer rocketMQProducer;//MQ
 * 
 * @Autowired private FinanceCloseAccountsMongoDao
 * financeCloseAccountsMongoDao;//财务经销存关账mongdb
 * 
 * @Autowired private MonthlyAccountDao monthlyAccountDao;
 * 
 * @Autowired private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
 * 
 *//**
	 * 访问列表页面
	 */
/*
 * @RequestMapping("/toPage.html") public String toPage(HttpSession
 * session,Model model, Long buId)throws Exception { User user=
 * ShiroUtils.getUser(); model.addAttribute("buId", buId); return
 * "/inventory/buInventory-list"; }
 * 
 * 
 *//**
	 * 展开
	 */
/*
 * @RequestMapping("/getBuInventoryItem.asyn")
 * 
 * @ResponseBody private ViewResponseBean getBuInventoryItem(HttpSession
 * session,Long id) { try{ // 响应结果集 BuInventoryItemDTO dto=new
 * BuInventoryItemDTO(); dto.setBuInventoryId(id); List<BuInventoryItemDTO>
 * buInventoryItem = buInventoryService.getBuInventoryItem(dto); return
 * ResponseFactory.success(buInventoryItem); }catch(SQLException e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_304,e); }catch(Exception e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); }
 * 
 * }
 * 
 *//**
	 * 获取分页数据
	 */
/*
 * @RequestMapping("/listBuInventory.asyn")
 * 
 * @ResponseBody private ViewResponseBean listBuInventory(HttpSession
 * session,BuInventoryDTO model) { try{ // 响应结果集 User user=
 * ShiroUtils.getUser(); model.setMerchantId(user.getMerchantId()); model =
 * buInventoryService.listBuInventory(model); }catch(SQLException e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_304,e); }catch(Exception e){ return
 * ResponseFactory.error(StateCodeEnum.ERROR_305,e); } return
 * ResponseFactory.success(model); }
 * 
 * 
 * 
 * 
 * 
 * 
 *//**
	 * 导出事业部库存明细
	 *//*
		 * @RequestMapping("/exportBuInventory.asyn")
		 * 
		 * @ResponseBody private void exportBuInventory(HttpSession session,
		 * HttpServletResponse response, HttpServletRequest request, Long depotId,String
		 * month,Long buId,Long brandId,String barcode,String goodsNo,String goodsName){
		 * try {
		 * 
		 * String sheetName = "事业部库存"; //根据勾选的获取信息 User user= ShiroUtils.getUser();
		 * BuInventoryModel model=new BuInventoryModel();
		 * model.setMerchantId(user.getMerchantId()); model.setDepotId(depotId);
		 * model.setMonth(month); model.setBuId(buId); model.setBrandId(brandId);
		 * model.setBarcode(barcode); model.setGoodsNo(goodsNo);
		 * model.setGoodsName(goodsName); List<ExportExcelSheet> sheetList=new
		 * ArrayList<ExportExcelSheet>(); List<Map<String, Object>>
		 * exportBuInventoryListMap = buInventoryService.exportBuInventory(model);
		 * String[]
		 * columns={"事业部","公司","仓库名称","品牌","商品货号","商品名称","商品条形码","好品数量","坏品数量","库存数量",
		 * "理货单位","月份","库存日期"}; String[]
		 * keys={"bu_name","merchant_name","depot_name","brand_name","goods_no",
		 * "goods_name","barcode","ok_num","worn_num","surplus_num","unit","month",
		 * "create_date"}; ExportExcelSheet sheet1=new ExportExcelSheet();
		 * sheet1.setKeys(keys); sheet1.setColums(columns);
		 * sheet1.setResultList(exportBuInventoryListMap);
		 * sheet1.setSheetNames(sheetName); sheetList.add(sheet1);
		 * 
		 * List<Map<String, Object>> exportBuInventoryItemListMap =
		 * buInventoryService.exportBuInventoryItem(model); for (Map<String, Object> map
		 * : exportBuInventoryItemListMap) { String effective_interval = (String)
		 * map.get("effective_interval"); String type = (String) map.get("type"); type =
		 * DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.initInventory_typeList, type);
		 * effective_interval = DERP_INVENTORY.getLabelByKey(DERP_INVENTORY.
		 * initInventory_effectiveIntervalList, effective_interval); map.put("type",
		 * type); map.put("effective_interval", effective_interval); }
		 * 
		 * String[]
		 * columns2={"事业部","公司","仓库名称","商品货号","商品名称","批次号","生产日期","失效日期","效期区间","好坏品类型",
		 * "库存数量","首次上架日期"}; String[]
		 * keys2={"bu_name","merchant_name","depot_name","goods_no","goods_name",
		 * "batch_no","production_date","overdue_date","effective_interval","type","num"
		 * ,"first_shelf_date"};
		 * 
		 * String sheetName2 = "事业部库存批次明细表"; ExportExcelSheet sheet2=new
		 * ExportExcelSheet(); sheet2.setKeys(keys2); sheet2.setColums(columns2);
		 * sheet2.setResultList(exportBuInventoryItemListMap);
		 * sheet2.setSheetNames(sheetName2); sheetList.add(sheet2); SXSSFWorkbook wb =
		 * ExcelUtilXlsx.createMutiSheetExcel(sheetList); //导出文件
		 * FileExportUtil.export2007ExcelFile(wb, response, request, sheetName);
		 * 
		 * 
		 * } catch (Exception e) { // TODO: handle exception e.printStackTrace(); }
		 * 
		 * }
		 * 
		 * @RequestMapping("/flashBuInventory.asyn")
		 * 
		 * @ResponseBody private ViewResponseBean flashBuInventory(HttpSession session,
		 * HttpServletResponse response, HttpServletRequest request, Long depotId,String
		 * month,Long buId){ Map<String, Object> map= new HashMap<String, Object>(); //
		 * 1成功 0失败 map.put("status", "1"); map.put("massage", "刷新成功"); String
		 * massage="仓库已经结转跳过刷新,不刷新的仓库id"; try { //根据勾选的获取信息 User user=
		 * ShiroUtils.getUser(); Map<String, Object> params= new HashMap<>();
		 * params.put("merchantId", user.getMerchantId()); if (depotId!=null) {
		 * params.put("depotId", depotId); } List<DepotMerchantRelMongo>
		 * depotMerchantRelList = depotMerchantRelMongoDao.findAll(params); if
		 * (depotMerchantRelList==null||depotMerchantRelList.size()==0) {
		 * map.put("status", "0"); map.put("massage", "商家ID:" + user.getMerchantId() +
		 * "商家仓库关系表无对应的仓库"); return ResponseFactory.success(map); } for
		 * (DepotMerchantRelMongo depotMerchantRelMongo : depotMerchantRelList) {
		 * JSONObject jsonMQ=new JSONObject();
		 * jsonMQ.put("merchantId",user.getMerchantId()); jsonMQ.put("depotId",
		 * depotMerchantRelMongo.getDepotId()); jsonMQ.put("month", month); if
		 * (buId!=null) { jsonMQ.put("buId", buId); } MonthlyAccountModel accountModel =
		 * new MonthlyAccountModel(); accountModel.setMerchantId(user.getMerchantId());
		 * accountModel.setDepotId(depotMerchantRelMongo.getDepotId());
		 * accountModel.setSettlementMonth(month); accountModel =
		 * monthlyAccountDao.searchByModel(accountModel); // 状态：1未转结 2 已转结 if
		 * (accountModel != null &&
		 * accountModel.getState().equals(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2)) { //
		 * 计算结转时间跟当前时间相差天数 int dayNum =
		 * TimeUtils.daysBetween(accountModel.getSettlementDate(), new Date()); if
		 * (dayNum > 1&&depotMerchantRelList.size()==1) { map.put("status", "0");
		 * map.put("massage", "商家ID:" + user.getMerchantId() + ",仓库Id：" + depotId +
		 * "月份：" + month + "已结转"); }else if(dayNum > 1&&depotMerchantRelList.size()>1) {
		 * massage=massage+depotMerchantRelMongo.getDepotId()+","; map.put("status",
		 * "1"); map.put("massage", massage); continue;
		 * 
		 * } }
		 * 
		 * rocketMQProducer.send(jsonMQ.toString(),
		 * MQInventoryEnum.INVENTORY_BU_INVENTORY.getTopic(),MQInventoryEnum.
		 * INVENTORY_BU_INVENTORY.getTags());
		 * 
		 * }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); return
		 * ResponseFactory.error(StateCodeEnum.ERROR_305, e);
		 * 
		 * } return ResponseFactory.success(map); }
		 * 
		 * 
		 * 
		 * }
		 */
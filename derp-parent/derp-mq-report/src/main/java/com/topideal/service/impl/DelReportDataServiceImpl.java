package com.topideal.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.dao.inventory.BuInventoryDao;
import com.topideal.dao.inventory.InventoryBatchDao;
import com.topideal.dao.inventory.InventoryDetailsDao;
import com.topideal.dao.order.BuMoveInventoryDao;
import com.topideal.dao.order.BuMoveInventoryItemDao;
import com.topideal.dao.order.InventoryLocationAdjustmentOrderDao;
import com.topideal.dao.order.InventoryLocationAdjustmentOrderItemDao;
import com.topideal.dao.order.MaterialOrderItemDao;
import com.topideal.dao.order.MerchandiseContrastItemDao;
import com.topideal.dao.order.PreSaleOrderItemDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.PurchaseReturnItemDao;
import com.topideal.dao.order.SaleOrderItemDao;
import com.topideal.dao.order.SaleReturnOrderDao;
import com.topideal.dao.order.SaleReturnOrderItemDao;
import com.topideal.dao.order.TransferOrderItemDao;
import com.topideal.dao.system.CustomerMerchantRelDao;
import com.topideal.dao.system.DepotMerchantRelDao;
import com.topideal.dao.system.GroupCommbarcodeDao;
import com.topideal.dao.system.GroupCommbarcodeItemDao;
import com.topideal.dao.system.MerchantDepotBuRelDao;
import com.topideal.entity.vo.order.SaleReturnOrderItemModel;
import com.topideal.service.DelReportDataService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 删除月结明细数据 
 */
@Service
public class DelReportDataServiceImpl implements DelReportDataService {

	private static final Logger logger = LoggerFactory.getLogger(DelReportDataServiceImpl.class);

	@Autowired
	private InventoryDetailsDao  inventoryDetailsDao;// 报表加减明细
	@Autowired
	private InventoryBatchDao inventoryBatchDao;//报表批次库存	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;//报表采购订单表体	
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;//报表销售订单表体
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;//报表调拨订单表体
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;//报表销售退货订单表体
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;//报表销售退货订单表头
	@Autowired
	private DepotMerchantRelDao depotMerchantRelDao ; //仓库公司关联关系
	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao ; //公司仓库事业部关联关系	
	@Autowired
	private BuInventoryDao buInventoryDao;// 事业部库存
	@Autowired
	private PurchaseReturnItemDao purchaseReturnItemDao ;
	@Autowired
	private GroupCommbarcodeDao groupCommbarcodeDao ;
	@Autowired
	private GroupCommbarcodeItemDao groupCommbarcodeItemDao ;

	@Autowired
	private PreSaleOrderItemDao preSaleOrderItemDao ;
	@Autowired
	private RMQProducer rMQProducer ;
	@Autowired
	private BuMoveInventoryDao buMoveInventoryDao ;
	@Autowired
	private BuMoveInventoryItemDao buMoveInventoryItemDao;
	// 库位调整单表头
	@Autowired
	private InventoryLocationAdjustmentOrderDao inventoryLocationAdjustmentOrderDao;
	// 库位调整单表体
	@Autowired
	private InventoryLocationAdjustmentOrderItemDao inventoryLocationAdjustmentOrderItemDao;
	@Autowired
	private MerchandiseContrastItemDao merchandiseContrastItemDao; //爬虫商品对照表 表体
	@Autowired
	private CustomerMerchantRelDao customerMerchantRelDao; //商家客户关系表
	@Autowired
	private MaterialOrderItemDao materialOrderItemDao; //领料单表体
	


	@Override
	@SystemServiceLog(point = "13201501070", model = "删除报表数据")
	public void delReportData(String json, String keys, String topics, String tags) throws Exception {
		logger.info("删除报表数据json:"+json);
		JSONObject jSONObject = JSONObject.fromObject(json);
		try {

			String source = jSONObject.getString("source");//1.来源库存加减明细 2.批次库存,3,采购订单表体 4,销售订单表体,5调拨订单表体6销售退货订单表体/表头,7仓库商家关系关联表	8.公司仓库事业部关系关联表 9.根据商家 仓库 事业部 月份 删除事业部库存				
			/*if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_1.equals(source)) {//删除库存加减明细
				delInventoryDetails(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_2.equals(source)) {// 删除批次库存
				delinventoryBatchs(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_3.equals(source)) {//3,采购订单表体 
				delPurchaseOrderItem(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_4.equals(source)) {// 4,销售订单表体
				delSaleOrderItem(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_5.equals(source)) {//5调拨订单表体
				delTransferOrderItem(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_6.equals(source)) {//6销售退货订单表体/表头
				delSaleReturnOrder(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_7.equals(source)) {//7公司仓库关系关联表
				delDepotMerchantRel(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_8.equals(source)) {//8公司仓库事业部关系关联表
				delMerchantDepotBuRel(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_9.equals(source)) {// 9.根据商家 仓库 事业部 月份 删除事业部库存
				delBuInventory(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_10.equals(source)) {// 10.采购退货表体
				delPurchaseReturnOrderItem(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_11.equals(source)) {// 11.组码
				delGroupCommBarcode(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_12.equals(source)) {// 12.采购勾稽
				delPurchaseAny(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_13.equals(source)) {// 13.预售单表体
				delPreSaleOrderItem(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_14.equals(source)) {// 14.事业部移库单表头表体
				delBuMoveInventory(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_15.equals(source)) {// 15.库位调整单表头表体
				delInventoryLocationAdjust(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_16.equals(source)){// 16.爬虫商品对照表体
				delMerchandiseContrastItem(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_17.equals(source)){// 17 删除商家客户关系表数据
				delCustomerMerchantRel(jSONObject);
			}else if (DERP_REPORT.DEL_REPORT_DATAS_ITEM_18.equals(source)){// 18 删除领料单表体数据
				delMaterialOrderItem(jSONObject);
			}*/
			
			/*if(DERP_REPORT.DEL_REPORT_DATAS_ITEM_3.equals(source) 
					|| DERP_REPORT.DEL_REPORT_DATAS_ITEM_4.equals(source)
					|| DERP_REPORT.DEL_REPORT_DATAS_ITEM_5.equals(source)
					||DERP_REPORT.DEL_REPORT_DATAS_ITEM_17.equals(source)) {
				//删除备份库数据
				rMQProducer.send(json, MQReportEnum.DEL_BXM_DATAS.getTopic(), MQReportEnum.DEL_BXM_DATAS.getTags()) ;
			}*/
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			String describe = (String)jSONObject.get("describe");
			throw new RuntimeException("删除"+describe+"失败");
		}
	}
	
	/**
	 *  删除商家客户关系表数据
	 * @throws Exception
	 */
	public void delCustomerMerchantRel (JSONObject jSONObject)throws Exception {
		logger.info(" 删除商家客户关系表数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!"17".equals(source)) {
			throw new RuntimeException(" 删除商家客户关系表数据");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids			
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}	
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = customerMerchantRelDao.delete(idsDel);
		}
		logger.info(" 删除商家客户关系表数据:"+delete);
	}
	

	/**
	 * 删除组码
	 * @param jSONObject
	 */
	private void delGroupCommBarcode(JSONObject jSONObject) throws Exception{
		logger.info("删除删除组码json:"+jSONObject);
		Object id = jSONObject.get("ids");
		
		if(id != null) {
			String idStr = String.valueOf(id) ;
			
			String[] ids = idStr.split(",") ;
			
			List<Long> idList = new ArrayList<Long>() ;
			for (String temp : ids) {
				idList.add(Long.valueOf(temp)) ;
			}
			
			groupCommbarcodeDao.delete(idList) ;
			int rows = groupCommbarcodeItemDao.deleteByCommbarcodeId(idList) ;
		}
	}

	/**
	 * 删除采购退货表体
	 * @param jSONObject
	 */
	private void delPurchaseReturnOrderItem(JSONObject jSONObject) {
		logger.info("删除报表采购退货表体json:"+jSONObject);
		Object orderId = jSONObject.get("orderId");
		
		if(orderId != null && orderId != null) {
			
			Map<String, Object> delMap = new HashMap<String, Object> ();
			delMap.put("orderId", jSONObject.getLong("orderId")) ;
			
			int rows = purchaseReturnItemDao.deleteByMap(delMap) ;
			
			logger.info("删除报表采购退货表体数量:" + rows);
		}
	}

	/**
	 * 删除报表公司仓库事业部关联关系
	 * @param jSONObject
	 */
	private void delBuInventory(JSONObject jSONObject) {
		logger.info("根据商家 仓库 事业部 月份 删除事业部库存:"+jSONObject);
		Map<String, String> delMap = (Map<String, String>) jSONObject.get("delMap");
		
		// 删除  事业部库存数据
		Set<String> delKeySet = delMap.keySet();
		for (String delkey : delKeySet) {
			String[]keyArr = delkey.split(",");
			Map<String, Object>buInventoryDelMap=new HashMap<>();
			buInventoryDelMap.put("merchantId", Long.parseLong(keyArr[0]));
			buInventoryDelMap.put("depotId", Long.parseLong(keyArr[1]));
			buInventoryDelMap.put("month", String.valueOf(keyArr[2]));//本月			
			int rows =buInventoryDao.delBuInventory(buInventoryDelMap);
			logger.info("删除报表公司仓库事业部库存:" + rows);
		}		
		

		
	}
	
	
	/**
	 * 删除报表公司仓库事业部关联关系
	 * @param jSONObject
	 */
	private void delMerchantDepotBuRel(JSONObject jSONObject) {
		logger.info("删除报表公司仓库事业部关联关系json:"+jSONObject);
		Object merchantId = jSONObject.get("merchantId");
		
		if(merchantId != null) {
			
			Map<String, Object> delMap = new HashMap<String, Object> ();
			delMap.put("merchantId", jSONObject.getLong("merchantId")) ;
			
			int rows = merchantDepotBuRelDao.deleteByMap(delMap) ;
			
			logger.info("删除报表公司仓库事业部关系数量:" + rows);
		}
		
	}
	
	/**
	 * 删除报表仓库商家关联关系
	 * @param jSONObject
	 */
	private void delDepotMerchantRel(JSONObject jSONObject) {
		logger.info("删除报表仓库商家关联关系json:"+jSONObject);
		Object merchantId = jSONObject.get("merchantId");
		
		if( merchantId != null) {
			
			Map<String, Object> delMap = new HashMap<String, Object> ();
			delMap.put("merchantId", jSONObject.getLong("merchantId")) ;
			
			int rows = depotMerchantRelDao.deleteByMap(delMap) ;
			
			logger.info("删除报表仓库商家关联关系数量:" + rows);
		}
		
	}

	/**
	 * 删除报表库存加减明细
	 * @throws Exception
	 */
	public void delInventoryDetails (JSONObject jSONObject)throws Exception {
		logger.info("删除报表加减明细数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!"1".equals(source)) {
			throw new RuntimeException("加减明细删除数据来源必须是1");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids			
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}	
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = inventoryDetailsDao.delete(idsDel);
		}
		logger.info("删除报表加减明细数量:"+delete);
	}
	
	/**
	 * 删除报表批次库存为0的数据
	 * @throws Exception
	 */
	public void delinventoryBatchs (JSONObject jSONObject)throws Exception {
		logger.info("删除报表批次库存数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!"2".equals(source)) {
			throw new RuntimeException("批次库存删除数据来源必须是2");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids			
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除批次库存
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = inventoryBatchDao.delete(idsDel);	
		}
		logger.info("删除报表批次库存数量:"+delete);
		
	}
	
	/**
	 * 采购订单表体
	 * @param jSONObject
	 * @throws Exception
	 */
	public void delPurchaseOrderItem (JSONObject jSONObject)throws Exception {
		logger.info("删除报表采购订单表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!"3".equals(source)) {
			throw new RuntimeException("采购订单表体删除数据来源必须是3");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids			
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除采购订单表体
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = purchaseOrderItemDao.delete(idsDel);
		}
		logger.info("删除报表采购订单表体:"+delete);
	}
	
	/**
	 * 销售订单表体
	 * @param jSONObject
	 * @throws Exception
	 */
	public void delSaleOrderItem (JSONObject jSONObject)throws Exception {
		logger.info("删除报表销售订单表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");;
		String source = jSONObject.getString("source");
		if (!"4".equals(source)) {
			throw new RuntimeException("销售订单表体删除数据来源必须是4");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids			
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除销售订单表体
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = saleOrderItemDao.delete(idsDel);
		}		
		logger.info("删除报表销售订单表体数量:"+delete);
	}
	
	/**
	 * 调拨订单表体
	 * @param jSONObject
	 * @throws Exception
	 */
	public void delTransferOrderItem (JSONObject jSONObject)throws Exception {
		logger.info("删除报表调拨订单表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!"5".equals(source)) {
			throw new RuntimeException("调拨订单表体删除数据来源必须是5");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids			
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除调拨订单表体
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = transferOrderItemDao.delete(idsDel);
		}		
		logger.info("删除报表调拨订单表体数量:"+delete);
	}
	/**
	 * 销售退货订单表体/表头
	 * @param jSONObject
	 * @throws Exception
	 */
	public void delSaleReturnOrder (JSONObject jSONObject)throws Exception {
		logger.info("删除报表销售退货订单表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		// 判断是删除销售退货订单表头还是表体 deleteType:0:表头 1：表体 2：表头+表体
		String deleteType = jSONObject.getString("deleteType");
		if (!"6".equals(source)) {
			throw new RuntimeException("销售退货订单表体/表头删除数据来源必须是6");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的表头ids
		List<Long> itemIdsDel=new ArrayList<>();// 要删除的表体ids
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
			if("2".equals(deleteType)){
				// 表体的ID
				SaleReturnOrderItemModel itemModel = new SaleReturnOrderItemModel();
				itemModel.setOrderId(Long.valueOf(id.toString()));
				List<SaleReturnOrderItemModel> itemList  = saleReturnOrderItemDao.list(itemModel);// 要删除的表体ids
					if(itemList!=null&&itemList.size()>0){
						for (SaleReturnOrderItemModel item : itemList) {
							itemIdsDel.add(item.getId());
						}
					}
			}
		}
		int delete=0;
		if(deleteType.equals("0")){
			// 批量删除销售订单表头
			if (idsDel!=null&&idsDel.size()>0) {
				delete =  saleReturnOrderDao.delete(idsDel);
			}		
		}else if(deleteType.equals("1")){
			// 批量删除销售订单表体
			if (idsDel!=null&&idsDel.size()>0) {
				delete =  saleReturnOrderItemDao.delete(idsDel);
			}		
		}else if(deleteType.equals("2")){
			// 批量删除销售订单表体+表头
			if (idsDel!=null&&idsDel.size()>0) {
				if(itemIdsDel!=null&&itemIdsDel.size()>0){
					delete =  saleReturnOrderItemDao.delete(itemIdsDel);	// 删除表体
				}
				int delete1 =  saleReturnOrderDao.delete(idsDel);	// 删除表头
				delete=delete+delete1;
			}		
		}
		logger.info("删除报表销售退货订单表体/表头数量:"+delete);
	}
	/**
	 * 删除预售单表体
	 * @param jSONObject
	 */
	private void delPreSaleOrderItem(JSONObject jSONObject) throws SQLException {
		logger.info("删除预售单表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!DERP_REPORT.DEL_REPORT_DATAS_ITEM_13.equals(source)) {
			throw new RuntimeException("预售单表体删除数据来源必须是13");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除预售单表体
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = preSaleOrderItemDao.delete(idsDel);
		}
		logger.info("删除报表销售订单表体数量:"+delete);
	}
	/**
	 * 删除事业部移库单表头表体
	 * @param jSONObject
	 */
	private void delBuMoveInventory(JSONObject jSONObject) throws SQLException {
		logger.info("删除事业部移库单表头表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!DERP_REPORT.DEL_REPORT_DATAS_ITEM_14.equals(source)) {
			throw new RuntimeException("预事业部移库单表头表体数据来源必须是14");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除事业部移库单表头表体
		int delete1=0;
		int delete2=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete1 = buMoveInventoryDao.delete(idsDel);
			delete2 = buMoveInventoryItemDao.delByIdBatch(idsDel);
		}
		logger.info("删除报表事业部移库单表头数量:"+delete1+";删除报表事业部移库单表体数量:"+delete2);
	}
	
	/**
	 * 删除库位调整单表头表体
	 * @param jSONObject
	 */
	private void delInventoryLocationAdjust(JSONObject jSONObject) throws SQLException {
		logger.info("删除库位调整单表头表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!DERP_REPORT.DEL_REPORT_DATAS_ITEM_15.equals(source)) {
			throw new RuntimeException("库位调整单表头表体数据来源必须是15");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除库位调整单表头表体
		int delete1=0;
		int delete2=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete1 = inventoryLocationAdjustmentOrderDao.delete(idsDel);
			delete2 = inventoryLocationAdjustmentOrderItemDao.delByIdBatch(idsDel);
		}
		logger.info("删除报表库位调整单表头数量:"+delete1+";删除库位调整单表体数量:"+delete2);
	}

	/**
	 * 删除爬虫商品对照表 表体
	 * @param jSONObject
	 */
	private void delMerchandiseContrastItem(JSONObject jSONObject) throws SQLException {
		logger.info("删除爬虫商品对照表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");
		String source = jSONObject.getString("source");
		if (!DERP_REPORT.DEL_REPORT_DATAS_ITEM_16.equals(source)) {
			throw new RuntimeException("爬虫商品对照表体数据来源必须是16");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 爬虫商品对照表体
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = merchandiseContrastItemDao.delete(idsDel);
		}
		logger.info("删除爬虫商品对照表体数量:"+delete);
	}
	/**
	 * 删除领料单 表体
	 * @param jSONObject
	 */
	private void delMaterialOrderItem(JSONObject jSONObject) throws SQLException {
		logger.info("删除报表领料单表体数据json:"+jSONObject);
		JSONArray jsonArray = jSONObject.getJSONArray("ids");;
		String source = jSONObject.getString("source");
		if (!"18".equals(source)) {
			throw new RuntimeException("领料单表体删除数据来源必须是18");
		}
		List<Long> idsDel=new ArrayList<>();// 要删除的ids			
		for (Object id : jsonArray) {
			idsDel.add(Long.valueOf(id.toString()));
		}
		// 批量删除领料单表体
		int delete=0;
		if (idsDel!=null&&idsDel.size()>0) {
			delete = materialOrderItemDao.delete(idsDel);
		}		
		logger.info("删除报表领料单表体数量:"+delete);
	}
}

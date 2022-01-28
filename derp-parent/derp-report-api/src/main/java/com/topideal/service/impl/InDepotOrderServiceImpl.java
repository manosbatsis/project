package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.RepotApiEnum;
import com.topideal.common.tools.DPMoney;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.storage.AdjustmentInventoryDao;
import com.topideal.dao.storage.AdjustmentInventoryItemDao;
import com.topideal.dao.storage.AdjustmentTypeDao;
import com.topideal.dao.storage.AdjustmentTypeItemDao;
import com.topideal.dao.system.ApiExternalConfigDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.PurchaseWarehouseBatchDao;
import com.topideal.dao.order.PurchaseWarehouseDao;
import com.topideal.dao.order.PurchaseWarehouseItemDao;
import com.topideal.dao.order.SaleReturnIdepotDao;
import com.topideal.dao.order.SaleReturnIdepotItemDao;
import com.topideal.dao.storage.TakesStockResultItemDao;
import com.topideal.dao.storage.TakesStockResultsDao;
import com.topideal.dao.order.TransferInDepotDao;
import com.topideal.dao.order.TransferInDepotItemDao;
import com.topideal.dao.order.WarehouseOrderRelDao;
import com.topideal.entity.dto.ResponseRoot;
import com.topideal.entity.dto.api10002.InDepotOrder;
import com.topideal.entity.dto.api10002.InDetail;
import com.topideal.entity.dto.api10002.InGoods;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;
import com.topideal.entity.vo.order.PurchaseWarehouseBatchModel;
import com.topideal.entity.vo.order.WarehouseOrderRelModel;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.service.InDepotOrderService;

import net.sf.json.JSONObject;
/**
 * 入库单实现类
 * @author 杨创
 *2019.04.11
 */
@Service
public class InDepotOrderServiceImpl implements InDepotOrderService{
    
	private static final String String = null;
	@Autowired
	private TransferInDepotDao transferInDepotDao;// 调拨入库单
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao;// 调拨入库单商品
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;// 采购订单
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;// 采购订单商品
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;// 采购入库单
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;// 采购入库单商品
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;//采购入库批次
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;//采购入库关联采购订单表
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;// 销售退货入库	
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;// 销售退货入库商品	
	@Autowired
	private TakesStockResultsDao takesStockResultsDao;//盘点结果单
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;//盘点结果实体
	@Autowired
	private AdjustmentInventoryDao adjustmentInventoryDao;// 库存调整单
	@Autowired
	private AdjustmentInventoryItemDao adjustmentInventoryItemDao;// 库存调整单表体
	@Autowired
	private AdjustmentTypeDao adjustmentTypeDao;// 类型调整单
	@Autowired
	private AdjustmentTypeItemDao AdjustmentTypeItemDao;// 类型调整单表体
	@Autowired
	private ApiExternalConfigDao apiExternalConfigDao;// 对外接口配置表
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;//商家仓库关系表
		
	/**
	 * 入库单
	 * */
	@Override
	public ResponseRoot queryInDepotOrder (JSONObject jsonData,Long merchantId)throws Exception{
		ResponseRoot responseRoot = new ResponseRoot();		
		// 根据商家id查询商家信息
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		if (merchantInfoModel==null) {
			responseRoot.setmCode("9999");
			responseRoot.setMessage("根据商家id没有查询到商家信息");
			return responseRoot;
		}		
		String orderType = jsonData.getString("orderType");
		//单据类型编码 001采购入库 003销售退货入库 006调拨入  009月结损益入 012其他入 013盘盈入 016好/坏品入 018货号变更入 020效期调整入	
		
		String startTime = jsonData.getString("startTime");
		String endTime = jsonData.getString("endTime");
		String pageNoStr=null;
		String pageSizeStr=null;
		if (jsonData.get("pageNo")!=null&&StringUtils.isNotBlank(jsonData.getString("pageNo"))) {
			pageNoStr=jsonData.getString("pageNo");
		}
		if (jsonData.get("pageSize")!=null&&StringUtils.isNotBlank(jsonData.getString("pageSize"))) {
			pageSizeStr=jsonData.getString("pageSize");
		}
		if (jsonData.get("pageNo") == null || StringUtils.isBlank(pageNoStr)) {
			pageNoStr="1";
		}
		if (jsonData.get("pageSize") == null|| StringUtils.isBlank(pageSizeStr)) {
			pageSizeStr="100";
		}
		Integer pageSize=Integer.valueOf(pageSizeStr);// 每页条数
		Integer startIndex=(Integer.valueOf(pageNoStr)-1)*pageSize;// 开始下标
		
		Map<String, Object> dMerRelParams =new HashMap<String, Object>();
		dMerRelParams.put("merchantId", merchantId);
		
		// 用于存放仓库对应商家和仓库关系表
		Map<Long, DepotMerchantRelMongo>depotMap=new HashedMap();
		List<DepotMerchantRelMongo> findAll = depotMerchantRelMongoDao.findAll(dMerRelParams);
		for (DepotMerchantRelMongo depotMerchantRelMongo : findAll) {
			depotMap.put(depotMerchantRelMongo.getDepotId(), depotMerchantRelMongo);
		}
		if (RepotApiEnum.CGRK.getKey().equals(orderType)) {	
			responseRoot = getPurchaseWarehouse(jsonData,merchantInfoModel,startTime,endTime,startIndex,pageSize,depotMap);
		}else if (RepotApiEnum.XSTR.getKey().equals(orderType)) {	//销售退货入库
			responseRoot = getSaleReturnIdepot(jsonData,merchantInfoModel,startTime,endTime,startIndex,pageSize,depotMap);
		}else if (RepotApiEnum.DBRK.getKey().equals(orderType)) {//调拨出库
			responseRoot = getTransferInDepot(jsonData,merchantInfoModel,startTime,endTime,startIndex,pageSize,depotMap);
			
		}else if (RepotApiEnum.YJSYR.getKey().equals(orderType)||RepotApiEnum.QTR.getKey().equals(orderType)||RepotApiEnum.WPHCR.getKey().equals(orderType)) {// 库存调整单月结入,库存调整单其他入
			String inventoryType=null;//库存调整单类型  1-销毁 2-月结损益 3-其他出入,4-唯品红冲
			if (RepotApiEnum.YJSYR.getKey().equals(orderType)) {//库存调整单月结入
				inventoryType=DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2;
			}
			if (RepotApiEnum.QTR.getKey().equals(orderType)) {//库存调整单其他入
				inventoryType=DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_3;
			}	
			if (RepotApiEnum.WPHCR.getKey().equals(orderType)) {//库存唯品红冲
				inventoryType=DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_4;
			}
			responseRoot=getAdjustmentInventory(jsonData,merchantInfoModel,inventoryType,startTime,endTime,startIndex,pageSize,depotMap);
		}else if (RepotApiEnum.PYR.getKey().equals(orderType)) {// 盘点结果
			responseRoot = getTakesStockResults(jsonData,merchantInfoModel,startTime,endTime,startIndex,pageSize,depotMap);
		}else if (RepotApiEnum.HHPR.getKey().equals(orderType)||RepotApiEnum.HHBGR.getKey().equals(orderType)
				||RepotApiEnum.XQTZR.getKey().equals(orderType)||RepotApiEnum.DHLHR.getKey().equals(orderType)) {// 好坏品入
			String type=null;//类型调整单业务类别 1.好坏品互转 2.货号变更 3效期调整,4大货理货	
			if (RepotApiEnum.HHPR.getKey().equals(orderType)) {//库存调整单其他入
				type=DERP_STORAGE.ADJUSTMENTTYPE_MODEL_1;
			}else if (RepotApiEnum.HHBGR.getKey().equals(orderType)) {
				type=DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2;
			}else if (RepotApiEnum.XQTZR.getKey().equals(orderType)) {
				type=DERP_STORAGE.ADJUSTMENTTYPE_MODEL_3;
			}else if (RepotApiEnum.DHLHR.getKey().equals(orderType)) {
				type=DERP_STORAGE.ADJUSTMENTTYPE_MODEL_4;
			} 			
			responseRoot = getAdjustmentTypeDao(jsonData, merchantInfoModel,type,startTime,endTime,startIndex,pageSize,depotMap);			
		}else {
			responseRoot.setmCode("9999");
			responseRoot.setMessage("orderType 传值不正确 ");
		}
				
		return responseRoot;
	}
	
	
	// 采购入库
	public ResponseRoot getPurchaseWarehouse(JSONObject jsonData,MerchantInfoModel merchantInfoModel,String startTime,String endTime,Integer startIndex,Integer pageSize,Map<Long, DepotMerchantRelMongo>depotMap)throws Exception{		
		
		// 获取总数
		Integer listCount = purchaseWarehouseDao.getListCount(merchantInfoModel.getId(),startTime,endTime);
		// 分页获取调拨入库信息
		List< Map<String, Object>> orderMapList = purchaseWarehouseDao.getListPage(merchantInfoModel.getId(),startTime,endTime,startIndex,pageSize);
		if (orderMapList==null||orderMapList.size()==0) {			
			ResponseRoot responseRoot =new ResponseRoot();
			responseRoot.setmCode("0000");
			responseRoot.setMessage("无订单");
			responseRoot.setTotalCount(listCount);
			responseRoot.setDataList(orderMapList);			
			return responseRoot;
		}			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<InDepotOrder> orderList=new ArrayList<>();
		for (Map<String, Object> orderMap : orderMapList) {								
			Long id = (Long)orderMap.get("id");
			String code =  (String)orderMap.get("code");
			String declareOrderCode = (String) orderMap.get("declare_order_code");
			String depotCode =  (String)orderMap.get("depot_code");//仓库编码
			String depotCode1 =  (String)orderMap.get("depot_code1");//仓库自编码
			String depotName =  (String)orderMap.get("depot_name");
			Timestamp inboundDate =  (Timestamp)orderMap.get("inbound_date");
			String contractNo =  (String)orderMap.get("contract_no");
			String depotType =  (String)orderMap.get("depot_type");
			String status = (String)orderMap.get("state");//011:待入仓,012:已入仓' 006 删除
			
			
			InDepotOrder inDepotOrder =new InDepotOrder();
			if(DERP.DEL_CODE_006.equals(status)){
				inDepotOrder.setStatus("02");//01-已入库、02-已删除
  			}else{
  				inDepotOrder.setStatus("01");
  			}
			inDepotOrder.setTopidealCode(merchantInfoModel.getTopidealCode());
			inDepotOrder.setCode(code);
			inDepotOrder.setBusinessNo(declareOrderCode);
			inDepotOrder.setOrderType(RepotApiEnum.CGRK.getKey());
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				inDepotOrder.setDepotCode(depotCode);
			}else{
				inDepotOrder.setDepotCode(depotCode1);
			}
			
						
			inDepotOrder.setDepotName(depotName);
			if (inboundDate!=null) {
				inDepotOrder.setDivergenceDate(sdf.format(inboundDate));
			}		
			inDepotOrder.setContractNo(contractNo);
			
			WarehouseOrderRelModel warehouseOrderRelModel =new WarehouseOrderRelModel();
			warehouseOrderRelModel.setWarehouseId(id);
			// 采购入库关联采购订单表    (根据采购入库单升序排列)
			List<WarehouseOrderRelModel> warehouseOrderRelModelList = warehouseOrderRelDao.getAscPurchaseOrderIdList(warehouseOrderRelModel);						
			if (warehouseOrderRelModelList!=null&&warehouseOrderRelModelList.size()>0) {
				// 取最小的采购订单id
				WarehouseOrderRelModel RelModel = warehouseOrderRelModelList.get(0);
				// 取最小采购订单的币种和po号、供应商信息
				Map<String, Object> purchaseOrderMap = purchaseWarehouseDao.getOrderInfo(RelModel.getPurchaseOrderId());
				if (purchaseOrderMap!=null) {
					inDepotOrder.setCurrency((String)purchaseOrderMap.get("currency"));// 采购订单币种
					inDepotOrder.setPoNo((String)purchaseOrderMap.get("po_no"));// 采购订单po号
					inDepotOrder.setSupplierCode("");//供应名称
					inDepotOrder.setSupplierName("");//供应商编码
				}
				
			}						
			 List<Map<String, Object>> itemMapList = purchaseWarehouseItemDao.getList(id);
			 List<InGoods>inGoodsList= new ArrayList<>();
			 for (Map<String, Object> itemMap : itemMapList) {			 
				 	Long itemId = (Long) itemMap.get("id");
					Long goodsId =(Long) itemMap.get("goods_id");
					String tallyingUnit = (String) itemMap.get("tallying_unit");
					String goodsNo = (String) itemMap.get("goods_no");
					String goodsName = (String) itemMap.get("goods_name");
					String brandName = (String) itemMap.get("brand_name");
					String commbarcode = (String) itemMap.get("commbarcode");			
					Long purchaseItemId =(Long) itemMap.get("purchase_item_id");
					
					if (!(DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit))) {
						tallyingUnit="";
					}													 
				InGoods inGoods = new InGoods();
				inGoods.setGoodsNo(goodsNo);
				inGoods.setGoodsName(goodsName);
				inGoods.setBrankName(brandName);
				inGoods.setCommonBarcode(commbarcode);
				// 按采购订单 从最小的一条采购订单开始找开始找 直到匹配到商品 价格
				for (WarehouseOrderRelModel warehouseModel : warehouseOrderRelModelList) {
					PurchaseOrderItemModel purchaseOrderItemModel=new PurchaseOrderItemModel();
					purchaseOrderItemModel.setPurchaseOrderId(warehouseModel.getPurchaseOrderId());
					purchaseOrderItemModel.setGoodsId(goodsId);
					purchaseOrderItemModel.setId(purchaseItemId);
					purchaseOrderItemModel = purchaseOrderItemDao.searchByModel(purchaseOrderItemModel);
					if (purchaseOrderItemModel!=null) {
						inGoods.setPrice(DPMoney.moneyFormat(purchaseOrderItemModel.getPrice()));	// 采购订单价格
						break;
					}
				}								
				inGoods.setUnit(tallyingUnit);
				//inGoods.setRemark(remark);	
				List<InDetail> detailList = new ArrayList<>();
				PurchaseWarehouseBatchModel warehouseBatchModel=new PurchaseWarehouseBatchModel();
				warehouseBatchModel.setItemId(itemId);
				List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.list(warehouseBatchModel);
				Integer transferNum=0;// 入库数量				
				// 在途仓没有批次 所以批次 是从商品中品出来的 
				/*if ("在途仓".equals(depotName)) {
					Integer normalNum = (Integer) itemMap.get("normal_num");//正常数量
					Integer multiNum = (Integer) itemMap.get("multi_num");//多货数量
					Integer lackNum = (Integer) itemMap.get("lack_num");//缺货数量
					if (normalNum==null) {
						normalNum=0;
					}
					if (multiNum==null) {
						multiNum=0;
					}
					if (lackNum==null) {
						lackNum=0;
					}
					InDetail inDetail=new InDetail();
					inDetail.setNum(normalNum+multiNum-lackNum);
					inDetail.setWornNum(0);
					inDetail.setExpireNum(0);
					transferNum=normalNum+multiNum-lackNum;
					detailList.add(inDetail);
				}else {*/
					for (PurchaseWarehouseBatchModel batchModel : batchList) {
						InDetail inDetail=new InDetail();
						inDetail.setNum(batchModel.getNormalNum());
						inDetail.setWornNum(batchModel.getWornNum());
						inDetail.setExpireNum(batchModel.getExpireNum());
						Date productionDate = batchModel.getProductionDate();
						Date overdueDate = batchModel.getOverdueDate();
						if (productionDate!=null) {
							inDetail.setProductionDate(format.format(productionDate));
						}
						if (overdueDate!=null) {
							inDetail.setOverdueDate(format.format(overdueDate));
						}									
						inDetail.setBatchNo(batchModel.getBatchNo());
						transferNum=transferNum+batchModel.getNormalNum()+batchModel.getWornNum()+batchModel.getExpireNum();
						detailList.add(inDetail);
					}
				/*}*/
				
				inGoods.setTotalNum(transferNum);	
				inGoods.setDetailList(detailList);
				inGoodsList.add(inGoods);
			}
			 
			 inDepotOrder.setGoodList(inGoodsList);
			 orderList.add(inDepotOrder);			 	
			
		}				
		ResponseRoot responseRoot =new ResponseRoot();
		responseRoot.setTotalCount(listCount);
		responseRoot.setDataList(orderList);		
		return responseRoot;
	}
		
	/**
	 * 调拨入库单信息
	 * @return
	 * @throws Exception
	 */
	public ResponseRoot getTransferInDepot(JSONObject jsonData,MerchantInfoModel merchantInfoModel,String startTime,String endTime,Integer startIndex,Integer pageSize,Map<Long, DepotMerchantRelMongo>depotMap)throws Exception{		
		// 获取总数
		Integer listCount = transferInDepotDao.getListCount(merchantInfoModel.getId(),startTime,endTime);
		// 分页获取调拨入库信息
		List< Map<String, Object>> orderMapList = transferInDepotDao.getListPage(merchantInfoModel.getId(),startTime,endTime,startIndex,pageSize);
		if (orderMapList==null||orderMapList.size()==0) {
			
			ResponseRoot responseRoot =new ResponseRoot();
			responseRoot.setmCode("0000");
			responseRoot.setMessage("无订单");
			responseRoot.setTotalCount(listCount);
			responseRoot.setDataList(orderMapList);			
			return responseRoot;
		}	
		Map<String, InDepotOrder>orderCheckMap=new HashedMap();
		List <Long>ids=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> orderMap : orderMapList) {
			Long id = (Long)orderMap.get("id");
			String code =  (String)orderMap.get("code");
			String transferOrderCode = (String) orderMap.get("transfer_order_code");
			String depotCode =  (String)orderMap.get("depot_code");//仓库编码
			String depotCode1 =  (String)orderMap.get("depot_code1");//仓库自编码
			String depotName =  (String)orderMap.get("depot_name");
			Timestamp transferDate =  (Timestamp)orderMap.get("transfer_date");
			String contractNo =  (String)orderMap.get("contract_no");
			String customerCode = (String) orderMap.get("in_customer_code");//供应编码
			String customerName = (String) orderMap.get("in_customer_name");//供应名称
			String depotType =  (String)orderMap.get("depot_type");
			String status = (String)orderMap.get("status");// 006 删
														
			InDepotOrder inDepotOrder =new InDepotOrder();
			if(DERP.DEL_CODE_006.equals(status)){
				inDepotOrder.setStatus("02");//01-已入库、02-已删除
  			}else{
  				inDepotOrder.setStatus("01");
  			}
			inDepotOrder.setTopidealCode(merchantInfoModel.getTopidealCode());
			inDepotOrder.setCode(code);
			inDepotOrder.setBusinessNo(transferOrderCode);
			inDepotOrder.setOrderType(RepotApiEnum.DBRK.getKey());
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				inDepotOrder.setDepotCode(depotCode);
			}else{
				inDepotOrder.setDepotCode(depotCode1);
			}
			
			inDepotOrder.setDepotName(depotName);
			if (transferDate!=null) {
				inDepotOrder.setDivergenceDate(sdf.format(transferDate));
			}			
			inDepotOrder.setContractNo(contractNo);
			//inDepotOrder.setPoNo(poNo);
			inDepotOrder.setSupplierCode("");
			inDepotOrder.setSupplierName("");
			inDepotOrder.setCurrency(DERP.CURRENCYCODE_CNY);//调拨默认人民币
			ids.add(id);	
			orderCheckMap.put(id.toString(), inDepotOrder);
		}
		// 根据ids查询调拨入库单商品
		Map<String, InGoods>itemcheckMap= new HashedMap();
		List<InDetail>inDetailList= new ArrayList<>();
		
		List<Map<String, Object>> itemMapList =new ArrayList<>();
		if (ids.size()>0) {
			itemMapList = transferInDepotItemDao.getList(ids);
		}
		
		for (Map<String, Object> itemMap : itemMapList) {
			Long transferDepotId = (Long) itemMap.get("transfer_depot_id");
			Long inGoodsId =(Long) itemMap.get("in_goods_id");
			String tallyingUnit = (String) itemMap.get("tallying_unit");
			String inGoodsNo = (String) itemMap.get("in_goods_no");
			String inGoodsName = (String) itemMap.get("in_goods_name");
			String brandName = (String) itemMap.get("brand_name");
			String commbarcode = (String) itemMap.get("commbarcode");
			BigDecimal price = (BigDecimal) itemMap.get("price");	
			if (price==null) {
				price=new BigDecimal(0);
			}
			Integer transferNum = (Integer) itemMap.get("transfer_num");
			if (transferNum==null) {
				transferNum=0;
			}
			Integer wornNum = (Integer) itemMap.get("worn_num");
			if (wornNum==null) {
				wornNum=0;
			}
			Integer expireNum = (Integer) itemMap.get("expire_num");
			if (expireNum==null) {
				expireNum=0;
			}
			Date productionDate = (Date) itemMap.get("production_date");
			Date overdueDate = (Date) itemMap.get("overdue_date");
			String transferBatchNo = (String) itemMap.get("transfer_batch_no");
			if (!(DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit))) {
				tallyingUnit="";
			}
			String orderKey=transferDepotId+"";
			String key=transferDepotId+","+inGoodsId+tallyingUnit+price;					
			InDetail inDetail=new InDetail();
			inDetail.setNum(transferNum);
			inDetail.setWornNum(wornNum);
			inDetail.setExpireNum(expireNum);
			if (productionDate!=null) {
				inDetail.setProductionDate(format.format(productionDate));
			}
			if (overdueDate!=null) {
				inDetail.setOverdueDate(format.format(overdueDate));
			}									
			inDetail.setBatchNo(transferBatchNo);			
			inDetailList.add(inDetail);					
			if (itemcheckMap.containsKey(key)) {
				InGoods inGoods = itemcheckMap.get(key);
				inGoods.setTotalNum(inGoods.getTotalNum()+transferNum+wornNum+expireNum);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}else {
				InGoods inGoods = new InGoods();
				inGoods.setGoodsNo(inGoodsNo);
				inGoods.setGoodsName(inGoodsName);
				inGoods.setBrankName(brandName);
				inGoods.setCommonBarcode(commbarcode);
				inGoods.setPrice(DPMoney.moneyFormat(price));
				inGoods.setTotalNum(transferNum+wornNum+expireNum);
				inGoods.setUnit(tallyingUnit);
				//inGoods.setRemark(remark);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}
											
		}
			
		Set<String> keySet = itemcheckMap.keySet();
		for (String key : keySet) {			
			InGoods inGoods = itemcheckMap.get(key);
			List<String> asList = Arrays.asList(key.split(","));
			String orderKey = asList.get(0);																		
			InDepotOrder inDepotOrder = orderCheckMap.get(orderKey);
			List<InGoods> goodList = inDepotOrder.getGoodList();
			if (goodList==null) {
				goodList=new ArrayList<>();
			}
			goodList.add(inGoods);
			inDepotOrder.setGoodList(goodList);
			orderCheckMap.put(orderKey, inDepotOrder);
			
		}
		Collection<InDepotOrder> values = orderCheckMap.values();
		
		List<InDepotOrder> inDepotOrderList = new ArrayList<InDepotOrder>(values);
		for (InDepotOrder inDepotOrder : inDepotOrderList) {
			List<InGoods> goodList2 = inDepotOrder.getGoodList();
			if (goodList2==null||goodList2.size()==0) {			
				inDepotOrder.setGoodList(new ArrayList<>());				
			}else {
				for (InGoods inGoods : goodList2) {
					List<InDetail> detailList = inGoods.getDetailList();
					if (detailList==null||detailList.size()==0) {
						inGoods.setDetailList(new ArrayList<>());
					}
				}
			}			
		}
		ResponseRoot responseRoot =new ResponseRoot();
		responseRoot.setTotalCount(listCount);
		responseRoot.setDataList(inDepotOrderList);
		
		return responseRoot;
	}
		
	// 销售退货入库
	public ResponseRoot getSaleReturnIdepot(JSONObject jsonData,MerchantInfoModel merchantInfoModel,String startTime,String endTime,Integer startIndex,Integer pageSize,Map<Long, DepotMerchantRelMongo>depotMap)throws Exception{
		
		// 获取总数
		Integer listCount = saleReturnIdepotDao.getListCount(merchantInfoModel.getId(),startTime,endTime);
		// 分页获取销售退货入库信息
		List< Map<String, Object>> orderMapList = saleReturnIdepotDao.getListPage(merchantInfoModel.getId(),startTime,endTime,startIndex,pageSize);
		if (orderMapList==null||orderMapList.size()==0) {		
			ResponseRoot responseRoot =new ResponseRoot();
			responseRoot.setmCode("0000");
			responseRoot.setMessage("无订单");
			responseRoot.setTotalCount(listCount);
			responseRoot.setDataList(orderMapList);			
			return responseRoot;
		}
		Map<String, InDepotOrder>orderCheckMap=new HashedMap();
		List <Long>ids=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> orderMap : orderMapList) {
			Long id = (Long)orderMap.get("id");									
			String code =  (String)orderMap.get("code");
			String orderCode = (String) orderMap.get("order_code");
			String depotCode =  (String)orderMap.get("depot_code");//仓库编码
			String depotCode1 =  (String)orderMap.get("depot_code1");//仓库自编码
			String depotName =  (String)orderMap.get("depot_name");
			Timestamp returnInDate =  (Timestamp)orderMap.get("return_in_date");
			String contractNo =  (String)orderMap.get("contract_no");
			String customerName = (String) orderMap.get("customer_name");
			String depotType =  (String)orderMap.get("depot_type");
			String supplierCode =  (String)orderMap.get("supplier_code");//供应商编码
			String status= (String)orderMap.get("status");//006 删除
			String currency= (String)orderMap.get("currency");//币种

			InDepotOrder inDepotOrder =new InDepotOrder();
			if(DERP.DEL_CODE_006.equals(status)){
				inDepotOrder.setStatus("02");//01-已入库、02-已删除
  			}else{
  				inDepotOrder.setStatus("01");
  			}
			inDepotOrder.setTopidealCode(merchantInfoModel.getTopidealCode());
			inDepotOrder.setCode(code);
			inDepotOrder.setBusinessNo(orderCode);
			inDepotOrder.setOrderType(RepotApiEnum.XSTR.getKey());
			
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				inDepotOrder.setDepotCode(depotCode);
			}else{
				inDepotOrder.setDepotCode(depotCode1);
			}
					
			inDepotOrder.setDepotName(depotName);

			if (returnInDate!=null) {
				inDepotOrder.setDivergenceDate(sdf.format(returnInDate));
			}			
			inDepotOrder.setContractNo(contractNo);
			//inDepotOrder.setPoNo(poNo);
			inDepotOrder.setSupplierName("");
			inDepotOrder.setSupplierCode("");
			inDepotOrder.setCurrency(currency);
			ids.add(id);	
			orderCheckMap.put(id.toString(), inDepotOrder);
		}
		// 根据ids查询销售退货入库单商品
		Map<String, InGoods>itemcheckMap= new HashedMap();
		List<InDetail>inDetailList= new ArrayList<>();
		
		List<Map<String, Object>> itemMapList =new ArrayList<>();
		if (ids.size()>0) {
			itemMapList = saleReturnIdepotItemDao.getList(ids);
		}
		for (Map<String, Object> itemMap : itemMapList) {
			
			Long sreturnIdepotId = (Long) itemMap.get("sreturn_idepot_id");
			Long inGoodsId =(Long) itemMap.get("in_goods_id");
			//String tallyingUnit = (String) itemMap.get("tallying_unit");
			String inGoodsNo = (String) itemMap.get("in_goods_no");
			String inGoodsName = (String) itemMap.get("in_goods_name");
			String brandName = (String) itemMap.get("brand_name");
			String commbarcode = (String) itemMap.get("commbarcode");
			Integer returnNum = (Integer) itemMap.get("return_num");
			if (returnNum==null) {
				returnNum=0;
			}
			Integer wornNum = (Integer) itemMap.get("worn_num");
			if (wornNum==null) {
				wornNum=0;
			}
			Integer expireNum = (Integer) itemMap.get("expire_num");
			if (expireNum==null) {
				expireNum=0;
			}
			Date productionDate = (Date) itemMap.get("production_date");
			Date overdueDate = (Date) itemMap.get("overdue_date");
			String returnBatchNo = (String) itemMap.get("return_batch_no");
			/*if (!("00".equals(tallyingUnit)||"01".equals(tallyingUnit)||"02".equals(tallyingUnit))) {
				tallyingUnit="";
			}*/
			String orderKey=sreturnIdepotId+"";
			String key=sreturnIdepotId+","+inGoodsId;
			
			
			InDetail inDetail=new InDetail();
			inDetail.setNum(returnNum);
			inDetail.setWornNum(wornNum);
			inDetail.setExpireNum(expireNum);
			if (productionDate!=null) {
				inDetail.setProductionDate(format.format(productionDate));
			}
			if (overdueDate!=null) {
				inDetail.setOverdueDate(format.format(overdueDate));
			}									
			inDetail.setBatchNo(returnBatchNo);
			
			inDetailList.add(inDetail);
			
			
			if (itemcheckMap.containsKey(key)) {
				InGoods inGoods = itemcheckMap.get(key);
				inGoods.setTotalNum(inGoods.getTotalNum()+returnNum+wornNum+expireNum);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}else {
				InGoods inGoods = new InGoods();
				inGoods.setGoodsNo(inGoodsNo);
				inGoods.setGoodsName(inGoodsName);
				inGoods.setBrankName(brandName);
				inGoods.setCommonBarcode(commbarcode);
				/*inGoods.setPrice(DPMoney.moneyFormat(price));*/
				inGoods.setTotalNum(returnNum+wornNum+expireNum);
				//inGoods.setUnit(tallyingUnit);
				//inGoods.setRemark(remark);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}
											
		}
						
		Set<String> keySet = itemcheckMap.keySet();
		for (String key : keySet) {			
			InGoods inGoods = itemcheckMap.get(key);
			List<String> asList = Arrays.asList(key.split(","));
			String orderKey = asList.get(0);																		
			InDepotOrder inDepotOrder = orderCheckMap.get(orderKey);
			List<InGoods> goodList = inDepotOrder.getGoodList();
			if (goodList==null) {
				goodList=new ArrayList<>();
			}
			goodList.add(inGoods);
			inDepotOrder.setGoodList(goodList);
			orderCheckMap.put(orderKey, inDepotOrder);
			
		}
		
		Collection<InDepotOrder> values = orderCheckMap.values();
		
		List<InDepotOrder> inDepotOrderList = new ArrayList<InDepotOrder>(values);
		// 取出null值

		for (InDepotOrder inDepotOrder : inDepotOrderList) {
			List<InGoods> goodList2 = inDepotOrder.getGoodList();
			if (goodList2==null||goodList2.size()==0) {			
				inDepotOrder.setGoodList(new ArrayList<>());				
			}else {
				for (InGoods inGoods : goodList2) {
					List<InDetail> detailList = inGoods.getDetailList();
					if (detailList==null||detailList.size()==0) {
						inGoods.setDetailList(new ArrayList<>());
					}
				}
			}
			
		}

		ResponseRoot responseRoot =new ResponseRoot();
		responseRoot.setTotalCount(listCount);
		responseRoot.setDataList(inDepotOrderList);		
		return responseRoot;
	
	}
	
	
	// 盘点结果
	public ResponseRoot getTakesStockResults(JSONObject jsonData,MerchantInfoModel merchantInfoModel,String startTime,String endTime,Integer startIndex,Integer pageSize,Map<Long, DepotMerchantRelMongo>depotMap)throws Exception{		
				
		// 获取总数
		Integer listCount = takesStockResultsDao.getListCount(merchantInfoModel.getId(),startTime,endTime);
		// 分页获取销售退货入库信息
		List< Map<String, Object>> orderMapList = takesStockResultsDao.getListPage(merchantInfoModel.getId(),startTime,endTime,startIndex,pageSize);
		if (orderMapList==null||orderMapList.size()==0) {		
			ResponseRoot responseRoot =new ResponseRoot();
			responseRoot.setmCode("0000");
			responseRoot.setMessage("无订单");
			responseRoot.setTotalCount(listCount);
			responseRoot.setDataList(orderMapList);			
			return responseRoot;
		}
		Map<String, InDepotOrder>orderCheckMap=new HashedMap();
		List <Long>ids=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> orderMap : orderMapList) {				
			Long id = (Long)orderMap.get("id");									
			String code =  (String)orderMap.get("code");
			String sourceCode = (String) orderMap.get("source_code");
			String depotCode =  (String)orderMap.get("depot_code");//仓库编码
			String depotCode1 =  (String)orderMap.get("depot_code1");//仓库自编码
			String depotName =  (String)orderMap.get("depot_name");
			Timestamp sourceTime =  (Timestamp)orderMap.get("source_time");
			//String contractNo =  (String)orderMap.get("contract_no");
			//String customerName = (String) orderMap.get("customer_name");
			String depotType =  (String)orderMap.get("depot_type");		
			String status = (String)orderMap.get("status");//006 删除
			
			InDepotOrder inDepotOrder =new InDepotOrder();
			if(DERP.DEL_CODE_006.equals(status)){
				inDepotOrder.setStatus("02");//01-已入库、02-已删除
  			}else{
  				inDepotOrder.setStatus("01");
  			}
			inDepotOrder.setTopidealCode(merchantInfoModel.getTopidealCode());
			inDepotOrder.setCode(code);
			inDepotOrder.setBusinessNo(sourceCode);
			inDepotOrder.setOrderType(RepotApiEnum.PYR.getKey());
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				inDepotOrder.setDepotCode(depotCode);
			}else{
				inDepotOrder.setDepotCode(depotCode1);
			}
			
			inDepotOrder.setDepotName(depotName);

			if (sourceTime!=null) {
				inDepotOrder.setDivergenceDate(sdf.format(sourceTime));
			}			
			//inDepotOrder.setContractNo(contractNo);
			//inDepotOrder.setPoNo(poNo);
			//inDepotOrder.setSupplierName(customerName);
			//inDepotOrder.setCurrency(currency);			
			ids.add(id);	
			orderCheckMap.put(id.toString(), inDepotOrder);
		}
		// 根据ids查询销售退货入库单商品
		Map<String, InGoods>itemcheckMap= new HashedMap();
		List<InDetail>inDetailList= new ArrayList<>();
		
		List<Map<String, Object>> itemMapList =new ArrayList<>();
		if (ids.size()>0) {
			itemMapList = takesStockResultItemDao.getList(ids);
		}
		for (Map<String, Object> itemMap : itemMapList) {
			
			Long takesStockResultId = (Long) itemMap.get("takes_stock_result_id");
			Long goodsId =(Long) itemMap.get("goods_id");
			String tallyingUnit = (String) itemMap.get("tally_unit");
			String goodsNo = (String) itemMap.get("goods_no");
			String goodsName = (String) itemMap.get("goods_name");
			String brandName = (String) itemMap.get("brand_name");
			String commbarcode = (String) itemMap.get("commbarcode");
			String isDamage = (String) itemMap.get("is_damage");// 0好品 1坏品
			/*BigDecimal price = (BigDecimal) itemMap.get("price");	
			if (price==null) {
				price=new BigDecimal(0);
			}*/
			Integer surplusNum = (Integer) itemMap.get("surplus_num");// 盘盈数量
			Integer num=0;	// 好品		
			Integer wornNum=0;// 坏品
			Integer expireNum=0;// 过期品
			
			
			// 生产日期和失效日期
			Date productionDate = (Date) itemMap.get("production_date");
			Date overdueDate = (Date) itemMap.get("overdue_date");
			// 归属月份
			String isExpire=DERP.ISEXPIRE_1;//0 过期 1 未过期 默认未过期
			Timestamp sourceTime = (Timestamp) itemMap.get("source_time");
			if (sourceTime!=null&&overdueDate!=null) {
				String falg = TimeUtils.timeComparisonSize(new Timestamp(overdueDate.getTime()), sourceTime);// 失效日期和归属日期比较 返回1  失效日期大于归属日期未过期  0标识已经过期
				if ("0".equals(falg)) {
					isExpire=DERP.ISEXPIRE_0;
				}
			}
			//如果已过期 直接设置为过期品 否则 盘点好品坏品
			if (DERP.ISEXPIRE_0.equals(isExpire)) {// 已过期
				expireNum=surplusNum;
			}else {//未过期
				if (DERP.ISDAMAGE_0.equals(isDamage)) {
					num=surplusNum;
				}
				if (DERP.ISDAMAGE_1.equals(isDamage)) {
					wornNum=surplusNum;
				}
			}
			
			
			
			String returnBatchNo = (String) itemMap.get("batch_no");
			if (!(DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit))) {
				tallyingUnit="";
			}
			String orderKey=takesStockResultId+"";
			String key=takesStockResultId+","+goodsId+tallyingUnit;
			
			
			InDetail inDetail=new InDetail();
			inDetail.setNum(num);
			inDetail.setWornNum(wornNum);
			inDetail.setExpireNum(expireNum);
			if (productionDate!=null) {
				inDetail.setProductionDate(format.format(productionDate));
			}
			if (overdueDate!=null) {
				inDetail.setOverdueDate(format.format(overdueDate));
			}									
			inDetail.setBatchNo(returnBatchNo);			
			inDetailList.add(inDetail);						
			if (itemcheckMap.containsKey(key)) {
				InGoods inGoods = itemcheckMap.get(key);
				inGoods.setTotalNum(inGoods.getTotalNum()+num+wornNum+expireNum);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}else {
				InGoods inGoods = new InGoods();
				inGoods.setGoodsNo(goodsNo);
				inGoods.setGoodsName(goodsName);
				inGoods.setBrankName(brandName);
				inGoods.setCommonBarcode(commbarcode);
				//inGoods.setPrice(DPMoney.moneyFormat(price));
				inGoods.setTotalNum(num+wornNum+expireNum);
				inGoods.setUnit(tallyingUnit);
				//inGoods.setRemark(remark);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}
											
		}
			
		Set<String> keySet = itemcheckMap.keySet();
		for (String key : keySet) {			
			InGoods inGoods = itemcheckMap.get(key);
			List<String> asList = Arrays.asList(key.split(","));
			String orderKey = asList.get(0);																		
			InDepotOrder inDepotOrder = orderCheckMap.get(orderKey);
			List<InGoods> goodList = inDepotOrder.getGoodList();
			if (goodList==null) {
				goodList=new ArrayList<>();
			}
			goodList.add(inGoods);
			inDepotOrder.setGoodList(goodList);
			orderCheckMap.put(orderKey, inDepotOrder);			
		}
		
		Collection<InDepotOrder> values = orderCheckMap.values();		
		List<InDepotOrder> inDepotOrderList = new ArrayList<InDepotOrder>(values);
		// 取出null值
		for (InDepotOrder inDepotOrder : inDepotOrderList) {
			List<InGoods> goodList2 = inDepotOrder.getGoodList();
			if (goodList2==null||goodList2.size()==0) {			
				inDepotOrder.setGoodList(new ArrayList<>());				
			}else {
				for (InGoods inGoods : goodList2) {
					List<InDetail> detailList = inGoods.getDetailList();
					if (detailList==null||detailList.size()==0) {
						inGoods.setDetailList(new ArrayList<>());
					}
				}
			}			
		}

		ResponseRoot responseRoot =new ResponseRoot();
		responseRoot.setTotalCount(listCount);
		responseRoot.setDataList(inDepotOrderList);		
		return responseRoot;		
	}
		
	// 库存调整
	public ResponseRoot getAdjustmentInventory(JSONObject jsonData,MerchantInfoModel merchantInfoModel,String inventoryType,String startTime,String endTime,Integer startIndex,Integer pageSize,Map<Long, DepotMerchantRelMongo>depotMap)throws Exception{	
		// 获取总数
		Integer listCount = adjustmentInventoryDao.getListCount(merchantInfoModel.getId(),startTime,endTime,inventoryType);
		// 分页获取销售退货入库信息
		List< Map<String, Object>> orderMapList = adjustmentInventoryDao.getListPage(merchantInfoModel.getId(),startTime,endTime,startIndex,pageSize,inventoryType);
		if (orderMapList==null||orderMapList.size()==0) {		
			ResponseRoot responseRoot =new ResponseRoot();
			responseRoot.setmCode("0000");
			responseRoot.setMessage("无订单");
			responseRoot.setTotalCount(listCount);
			responseRoot.setDataList(orderMapList);			
			return responseRoot;
		}
		Map<String, InDepotOrder>orderCheckMap=new HashedMap();
		List <Long>ids=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> orderMap : orderMapList) {			 
			Long id = (Long)orderMap.get("id");									
			String code =  (String)orderMap.get("code");
			String sourceCode = (String) orderMap.get("source_code");
			String depotCode =  (String)orderMap.get("depot_code");//仓库编码
			String depotCode1 =  (String)orderMap.get("depot_code1");//仓库自编码
			String depotName =  (String)orderMap.get("depot_name");
			Timestamp sourceTime =  (Timestamp)orderMap.get("source_time");
			//String contractNo =  (String)orderMap.get("contract_no");
			//String customerName = (String) orderMap.get("customer_name");
			String depotType =  (String)orderMap.get("depot_type");
			String status = (String)orderMap.get("status");//006 删除
			
			InDepotOrder inDepotOrder =new InDepotOrder();
			if(DERP.DEL_CODE_006.equals(status)){
				inDepotOrder.setStatus("02");//01-已入库、02-已删除
  			}else{
  				inDepotOrder.setStatus("01");
  			}
			inDepotOrder.setTopidealCode(merchantInfoModel.getTopidealCode());
			inDepotOrder.setCode(code);
			inDepotOrder.setBusinessNo(sourceCode);
			//库存调整单类型  1-销毁 2-月结损益 3-其他出入,4-唯品红冲
			if (DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_2.equals(inventoryType)) {
				inDepotOrder.setOrderType(RepotApiEnum.YJSYR.getKey());
			}else if (DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_3.equals(inventoryType)) {
				inDepotOrder.setOrderType(RepotApiEnum.QTR.getKey());
			}else if (DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_4.equals(inventoryType)) {
				inDepotOrder.setOrderType(RepotApiEnum.WPHCR.getKey());
			}
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				inDepotOrder.setDepotCode(depotCode);
			}else{
				inDepotOrder.setDepotCode(depotCode1);
			}
			
		
			inDepotOrder.setDepotName(depotName);

			if (sourceTime!=null) {
				inDepotOrder.setDivergenceDate(sdf.format(sourceTime));
			}
			
			//inDepotOrder.setContractNo(contractNo);
			//inDepotOrder.setPoNo(poNo);
			//inDepotOrder.setSupplierName(customerName);
			//inDepotOrder.setCurrency(currency);			
			ids.add(id);	
			orderCheckMap.put(id.toString(), inDepotOrder);
		}
		// 根据ids查询销售退货入库单商品
		Map<String, InGoods>itemcheckMap= new HashedMap();
		List<InDetail>inDetailList= new ArrayList<>();
		
		List<Map<String, Object>> itemMapList =new ArrayList<>();
		if (ids.size()>0) {
			itemMapList = adjustmentInventoryItemDao.getList(ids);
		}
		for (Map<String, Object> itemMap : itemMapList) {		
			Long adjustmentInventoryId = (Long) itemMap.get("t_adjustment_inventory_id");
			Long goodsId =(Long) itemMap.get("goods_id");
			String tallyingUnit = (String) itemMap.get("tallying_unit");
			String goodsNo = (String) itemMap.get("goods_no");
			String goodsName = (String) itemMap.get("goods_name");
			String brandName = (String) itemMap.get("brand_name");
			String commbarcode = (String) itemMap.get("commbarcode");
			String isDamage = (String) itemMap.get("is_damage");// 0好品 1坏品
			/*BigDecimal price = (BigDecimal) itemMap.get("price");	
			if (price==null) {
				price=new BigDecimal(0);
			}*/
			Integer adjustTotal = (Integer) itemMap.get("adjust_total");// 盘盈数量
			Integer num=0;	// 好品		
			Integer wornNum=0;// 坏品
			Integer expireNum=0;// 过期品
							
			// 生产日期和失效日期
			Date productionDate = (Date) itemMap.get("production_date");
			Date overdueDate = (Date) itemMap.get("overdue_date");
			// 归属月份
			String isExpire="1";//0 过期 1 未过期 默认未过期
			Timestamp sourceTime = (Timestamp) itemMap.get("source_time");
			if (sourceTime!=null&&overdueDate!=null) {
				String falg = TimeUtils.timeComparisonSize(TimeUtils.parseDay(format.format(overdueDate)), sourceTime);// 失效日期和归属日期比较 返回1  失效日期大于归属日期未过期  0标识已经过期
				if ("0".equals(falg)) {
					isExpire="0";
				}
			}
			//如果已过期 直接设置为过期品 否则 盘点好品坏品
			if (DERP.ISEXPIRE_0.equals(isExpire)) {// 已过期
				expireNum=adjustTotal;
			}else {//未过期
				if (DERP.ISDAMAGE_0.equals(isDamage)) {
					num=adjustTotal;
				}
				if (DERP.ISDAMAGE_1.equals(isDamage)) {
					wornNum=adjustTotal;
				}
			}	
						
			String oldBatchNo = (String) itemMap.get("old_batch_no");
			if (!(DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit))) {
				tallyingUnit="";
			}
			String orderKey=adjustmentInventoryId+"";
			String key=adjustmentInventoryId+","+goodsId+tallyingUnit;
						
			InDetail inDetail=new InDetail();
			inDetail.setNum(num);
			inDetail.setWornNum(wornNum);
			inDetail.setExpireNum(expireNum);
			if (productionDate!=null) {
				inDetail.setProductionDate(format.format(productionDate));
			}
			if (overdueDate!=null) {
				inDetail.setOverdueDate(format.format(overdueDate));
			}									
			inDetail.setBatchNo(oldBatchNo);			
			inDetailList.add(inDetail);
						
			if (itemcheckMap.containsKey(key)) {
				InGoods inGoods = itemcheckMap.get(key);
				inGoods.setTotalNum(inGoods.getTotalNum()+num+wornNum+expireNum);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}else {
				InGoods inGoods = new InGoods();
				inGoods.setGoodsNo(goodsNo);
				inGoods.setGoodsName(goodsName);
				inGoods.setBrankName(brandName);
				inGoods.setCommonBarcode(commbarcode);
				//inGoods.setPrice(DPMoney.moneyFormat(price));
				inGoods.setTotalNum(num+wornNum+expireNum);
				inGoods.setUnit(tallyingUnit);
				//inGoods.setRemark(remark);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}
											
		}
		
		Set<String> keySet = itemcheckMap.keySet();
		for (String key : keySet) {			
			InGoods inGoods = itemcheckMap.get(key);
			List<String> asList = Arrays.asList(key.split(","));
			String orderKey = asList.get(0);																		
			InDepotOrder inDepotOrder = orderCheckMap.get(orderKey);
			List<InGoods> goodList = inDepotOrder.getGoodList();
			if (goodList==null) {
				goodList=new ArrayList<>();
			}
			goodList.add(inGoods);
			inDepotOrder.setGoodList(goodList);
			orderCheckMap.put(orderKey, inDepotOrder);
			
		}
		
		Collection<InDepotOrder> values = orderCheckMap.values();		
		List<InDepotOrder> inDepotOrderList = new ArrayList<InDepotOrder>(values);
		// 取出null值

		for (InDepotOrder inDepotOrder : inDepotOrderList) {
			List<InGoods> goodList2 = inDepotOrder.getGoodList();
			if (goodList2==null||goodList2.size()==0) {			
				inDepotOrder.setGoodList(new ArrayList<>());				
			}else {
				for (InGoods inGoods : goodList2) {
					List<InDetail> detailList = inGoods.getDetailList();
					if (detailList==null||detailList.size()==0) {
						inGoods.setDetailList(new ArrayList<>());
					}
				}
			}			
		}
		ResponseRoot responseRoot =new ResponseRoot();
		responseRoot.setTotalCount(listCount);
		responseRoot.setDataList(inDepotOrderList);		
		return responseRoot;
	}
	
	// 类型调整单
	public ResponseRoot getAdjustmentTypeDao(JSONObject jsonData,MerchantInfoModel merchantInfoModel,String type,String startTime,String endTime,Integer startIndex,Integer pageSize,Map<Long, DepotMerchantRelMongo>depotMap)throws Exception{				
		// 获取总数
		Integer listCount = adjustmentTypeDao.getListCount(merchantInfoModel.getId(),startTime,endTime,type);
		// 分页获取销售退货入库信息
		List< Map<String, Object>> orderMapList = adjustmentTypeDao.getListPage(merchantInfoModel.getId(),startTime,endTime,startIndex,pageSize,type);
		if (orderMapList==null||orderMapList.size()==0) {		
			ResponseRoot responseRoot =new ResponseRoot();
			responseRoot.setmCode("0000");
			responseRoot.setMessage("无订单");
			responseRoot.setTotalCount(listCount);
			responseRoot.setDataList(orderMapList);			
			return responseRoot;
		}
		Map<String, InDepotOrder>orderCheckMap=new HashedMap();
		List <Long>ids=new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> orderMap : orderMapList) {					
			Long id = (Long)orderMap.get("id");									
			String code =  (String)orderMap.get("code");
			String sourceCode = (String) orderMap.get("source_code");
			String depotCode =  (String)orderMap.get("depot_code");//仓库编码
			String depotCode1 =  (String)orderMap.get("depot_code1");//仓库自编码
			String depotName =  (String)orderMap.get("depot_name");
			Timestamp pushTime =  (Timestamp)orderMap.get("push_time");
			//String contractNo =  (String)orderMap.get("contract_no");
			//String customerName = (String) orderMap.get("customer_name");
			String depotType =  (String)orderMap.get("depot_type");
			String status = (String)orderMap.get("status");//006 删除
											
			InDepotOrder inDepotOrder =new InDepotOrder();
			if(DERP.DEL_CODE_006.equals(status)){
				inDepotOrder.setStatus("02");//01-已入库、02-已删除
  			}else{
  				inDepotOrder.setStatus("01");
  			}
			inDepotOrder.setTopidealCode(merchantInfoModel.getTopidealCode());
			inDepotOrder.setCode(code);
			inDepotOrder.setBusinessNo(sourceCode);
			//1.好坏品互转 2.货号变更 3效期调整,4大货理货
			if (DERP_STORAGE.ADJUSTMENTTYPE_MODEL_1.equals(type)) {
				inDepotOrder.setOrderType(RepotApiEnum.HHPR.getKey());
			}else if (DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2.equals(type)) {
				inDepotOrder.setOrderType(RepotApiEnum.HHBGR.getKey());
			}else if (DERP_STORAGE.ADJUSTMENTTYPE_MODEL_3.equals(type)) {
				inDepotOrder.setOrderType(RepotApiEnum.XQTZR.getKey());
			}else if (DERP_STORAGE.ADJUSTMENTTYPE_MODEL_4.equals(type)) {
				inDepotOrder.setOrderType(RepotApiEnum.DHLHR.getKey());
			}
			
			// 获取商家仓库唯一表
			 DepotMerchantRelMongo depotMerchantRelMongo = depotMap.get((Long)orderMap.get("depot_id"));
			 if (depotMerchantRelMongo==null) {
				 depotMerchantRelMongo=new DepotMerchantRelMongo();
			 }
			if((DERP_SYS.DEPOTINFO_TYPE_A.equals(depotType)||DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType))&&
				DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1.equals(depotMerchantRelMongo.getIsInOutInstruction())	){//非卓志仓给仓库自编码；卓志仓给OP卓志编码			
				inDepotOrder.setDepotCode(depotCode);
			}else{
				inDepotOrder.setDepotCode(depotCode1);
			}
			
			inDepotOrder.setDepotName(depotName);

			if (pushTime!=null) {
				inDepotOrder.setDivergenceDate(sdf.format(pushTime));
			}
			
			//inDepotOrder.setContractNo(contractNo);
			//inDepotOrder.setPoNo(poNo);
			//inDepotOrder.setSupplierName(customerName);
			//inDepotOrder.setCurrency(currency);			
			ids.add(id);	
			orderCheckMap.put(id.toString(), inDepotOrder);
		}
		// 根据ids查询销售退货入库单商品
		Map<String, InGoods>itemcheckMap= new HashedMap();
		List<InDetail>inDetailList= new ArrayList<>();
		
		List<Map<String, Object>> itemMapList =new ArrayList<>();
		if (ids.size()>0) {
			itemMapList = AdjustmentTypeItemDao.getList(ids,type);
		}
		for (Map<String, Object> itemMap : itemMapList) {
				
			Long takesStockResultId = (Long) itemMap.get("t_adjustment_type_id");
			Long goodsId =(Long) itemMap.get("goods_id");
			String tallyingUnit = (String) itemMap.get("tallying_unit");
			String goodsNo = (String) itemMap.get("goods_no");
			String goodsName = (String) itemMap.get("goods_name");
			String brandName = (String) itemMap.get("brand_name");
			String commbarcode = (String) itemMap.get("commbarcode");
			String isDamage = (String) itemMap.get("is_damage");// 0好品 1坏品
			/*BigDecimal price = (BigDecimal) itemMap.get("price");	
			if (price==null) {
				price=new BigDecimal(0);
			}*/
			Integer adjustTotal = (Integer) itemMap.get("adjust_total");// 盘盈数量
			Integer num=0;	// 好品		
			Integer wornNum=0;// 坏品
			Integer expireNum=0;// 过期品
					
			// 生产日期和失效日期
			Date productionDate = (Date) itemMap.get("production_date");
			Date overdueDate = (Date) itemMap.get("overdue_date");
			// 归属月份
			String isExpire=DERP.ISEXPIRE_1;//0 过期 1 未过期 默认未过期
			Timestamp pushTime = (Timestamp) itemMap.get("push_time");// 归属月份
			if (pushTime!=null&&overdueDate!=null) {
				String falg = TimeUtils.timeComparisonSize(TimeUtils.parseDay(format.format(overdueDate)), pushTime);// 失效日期和归属日期比较 返回1  失效日期大于归属日期未过期  0标识已经过期
				if ("0".equals(falg)) {
					isExpire=DERP.ISEXPIRE_0;
				}
			}
			//如果已过期 直接设置为过期品 否则 盘点好品坏品
			if (DERP.ISEXPIRE_0.equals(isExpire)) {// 已过期
				expireNum=adjustTotal;
			}else {//未过期
				if (DERP.ISDAMAGE_0.equals(isDamage)) {
					num=adjustTotal;
				}
				if (DERP.ISDAMAGE_1.equals(isDamage)) {
					wornNum=adjustTotal;
				}
			}
			
			
			
			String oldBatchNo = (String) itemMap.get("old_batch_no");
			if (!(DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)||DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit))) {
				tallyingUnit="";
			}
			String orderKey=takesStockResultId+"";
			String key=takesStockResultId+","+goodsId+tallyingUnit;		
			InDetail inDetail=new InDetail();
			inDetail.setNum(num);
			inDetail.setWornNum(wornNum);
			inDetail.setExpireNum(expireNum);
			if (productionDate!=null) {
				inDetail.setProductionDate(format.format(productionDate));
			}
			if (overdueDate!=null) {
				inDetail.setOverdueDate(format.format(overdueDate));
			}									
			inDetail.setBatchNo(oldBatchNo);			
			inDetailList.add(inDetail);	
			
			if (itemcheckMap.containsKey(key)) {
				InGoods inGoods = itemcheckMap.get(key);
				inGoods.setTotalNum(inGoods.getTotalNum()+num+wornNum+expireNum);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}else {
				InGoods inGoods = new InGoods();
				inGoods.setGoodsNo(goodsNo);
				inGoods.setGoodsName(goodsName);
				inGoods.setBrankName(brandName);
				inGoods.setCommonBarcode(commbarcode);
				//inGoods.setPrice(DPMoney.moneyFormat(price));
				inGoods.setTotalNum(num+wornNum+expireNum);
				inGoods.setUnit(tallyingUnit);
				//inGoods.setRemark(remark);	
				List<InDetail> detailList = inGoods.getDetailList();
				if (detailList==null) {
					detailList=new ArrayList<>();
				}
				detailList.add(inDetail);
				inGoods.setDetailList(detailList);
				itemcheckMap.put(key, inGoods);
			}
											
		}			
		Set<String> keySet = itemcheckMap.keySet();
		for (String key : keySet) {			
			InGoods inGoods = itemcheckMap.get(key);
			List<String> asList = Arrays.asList(key.split(","));
			String orderKey = asList.get(0);																		
			InDepotOrder inDepotOrder = orderCheckMap.get(orderKey);
			List<InGoods> goodList = inDepotOrder.getGoodList();
			if (goodList==null) {
				goodList=new ArrayList<>();
			}
			goodList.add(inGoods);
			inDepotOrder.setGoodList(goodList);
			orderCheckMap.put(orderKey, inDepotOrder);			
		}
		
		Collection<InDepotOrder> values = orderCheckMap.values();		
		List<InDepotOrder> inDepotOrderList = new ArrayList<InDepotOrder>(values);
		// 取出null值
		for (InDepotOrder inDepotOrder : inDepotOrderList) {
			List<InGoods> goodList2 = inDepotOrder.getGoodList();
			if (goodList2==null||goodList2.size()==0) {			
				inDepotOrder.setGoodList(new ArrayList<>());				
			}else {
				for (InGoods inGoods : goodList2) {
					List<InDetail> detailList = inGoods.getDetailList();
					if (detailList==null||detailList.size()==0) {
						inGoods.setDetailList(new ArrayList<>());
					}
				}
			}			
		}
		ResponseRoot responseRoot =new ResponseRoot();
		responseRoot.setTotalCount(listCount);
		responseRoot.setDataList(inDepotOrderList);		
		return responseRoot;
	}
		
}

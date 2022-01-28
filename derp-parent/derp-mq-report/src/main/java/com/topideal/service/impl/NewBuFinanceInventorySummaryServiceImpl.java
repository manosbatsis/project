package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.BuInventoryDao;
import com.topideal.dao.inventory.MonthlyAccountDao;
import com.topideal.dao.order.BuMoveInventoryDao;
import com.topideal.dao.order.InventoryLocationAdjustmentOrderDao;
import com.topideal.dao.order.LocationAdjustmentOrderDao;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.PurchaseReturnItemDao;
import com.topideal.dao.order.PurchaseReturnOdepotDao;
import com.topideal.dao.order.PurchaseReturnOrderDao;
import com.topideal.dao.order.PurchaseSdOrderDao;
import com.topideal.dao.order.PurchaseSdOrderSditemDao;
import com.topideal.dao.order.PurchaseWarehouseDao;
import com.topideal.dao.order.PurchaseWarehouseItemDao;
import com.topideal.dao.order.SaleOrderDao;
import com.topideal.dao.order.SaleOrderItemDao;
import com.topideal.dao.order.SaleOutDepotDao;
import com.topideal.dao.order.SaleOutDepotItemDao;
import com.topideal.dao.order.SaleReturnOrderItemDao;
import com.topideal.dao.order.TransferInDepotDao;
import com.topideal.dao.order.TransferOrderDao;
import com.topideal.dao.order.WarehouseOrderRelDao;
import com.topideal.dao.reporting.BuFinanceAddPurchaseNotshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceAddSaleNoshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceAddTransferNoshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceDecreasePurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceDecreaseSaleNoshelfDao;
import com.topideal.dao.reporting.BuFinanceDestroyDao;
import com.topideal.dao.reporting.BuFinanceInventorySummaryDao;
import com.topideal.dao.reporting.BuFinanceLocationAdjustmentDetailsDao;
import com.topideal.dao.reporting.BuFinanceMoveDetailsDao;
import com.topideal.dao.reporting.BuFinancePurchaseDamagedDao;
import com.topideal.dao.reporting.BuFinancePurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceSaleDamagedDao;
import com.topideal.dao.reporting.BuFinanceSaleNotshelfDao;
import com.topideal.dao.reporting.BuFinanceSaleShelfDao;
import com.topideal.dao.reporting.BuFinanceSdAddPurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceSdWarehouseDetailsDao;
import com.topideal.dao.reporting.BuFinanceTakesStockResultsDao;
import com.topideal.dao.reporting.BuFinanceWarehouseDetailsDao;
import com.topideal.dao.reporting.SdInterestPriceDao;
import com.topideal.dao.reporting.SdWeightedPriceDao;
import com.topideal.dao.reporting.SettlementPriceDao;
import com.topideal.dao.reporting.SettlementPriceRecordDao;
import com.topideal.dao.reporting.WeightedPriceDao;
import com.topideal.dao.storage.AdjustmentInventoryDao;
import com.topideal.dao.storage.TakesStockResultItemDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.CostPriceDifferenceDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.dao.system.GroupCommbarcodeDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.dao.system.MerchantBuRelDao;
import com.topideal.dao.system.MerchantDepotBuRelDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.dto.SdInterestPriceDTO;
import com.topideal.entity.dto.SdWeightedPriceDTO;
import com.topideal.entity.dto.WeightedPriceDTO;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;
import com.topideal.entity.vo.order.PurchaseOrderModel;
import com.topideal.entity.vo.order.PurchaseReturnItemModel;
import com.topideal.entity.vo.order.PurchaseReturnOrderModel;
import com.topideal.entity.vo.order.PurchaseWarehouseModel;
import com.topideal.entity.vo.order.SaleOrderItemModel;
import com.topideal.entity.vo.order.SaleOrderModel;
import com.topideal.entity.vo.order.SaleOutDepotModel;
import com.topideal.entity.vo.order.SaleReturnOrderItemModel;
import com.topideal.entity.vo.order.TransferInDepotModel;
import com.topideal.entity.vo.order.WarehouseOrderRelModel;
import com.topideal.entity.vo.reporting.BuFinanceAddPurchaseNotshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceAddSaleNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceAddTransferNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceDecreaseSaleNoshelfModel;
import com.topideal.entity.vo.reporting.BuFinanceDestroyModel;
import com.topideal.entity.vo.reporting.BuFinanceInventorySummaryModel;
import com.topideal.entity.vo.reporting.BuFinanceLocationAdjustmentDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceMoveDetailsModel;
import com.topideal.entity.vo.reporting.BuFinancePurchaseDamagedModel;
import com.topideal.entity.vo.reporting.BuFinanceSaleDamagedModel;
import com.topideal.entity.vo.reporting.BuFinanceSaleShelfModel;
import com.topideal.entity.vo.reporting.BuFinanceSdAddPurchaseNotshelfModel;
import com.topideal.entity.vo.reporting.BuFinanceSdWarehouseDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceTakesStockResultsModel;
import com.topideal.entity.vo.reporting.BuFinanceWarehouseDetailsModel;
import com.topideal.entity.vo.reporting.SdInterestPriceModel;
import com.topideal.entity.vo.reporting.SdWeightedPriceModel;
import com.topideal.entity.vo.reporting.SettlementPriceModel;
import com.topideal.entity.vo.reporting.SettlementPriceRecordModel;
import com.topideal.entity.vo.reporting.WeightedPriceModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.MerchantBuRelModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.entity.vo.system.MerchantRelModel;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.FileTaskMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.service.BuFinanceInventorySummaryService;

import net.sf.json.JSONObject;

/**
 *事业部-财务进销存报表
 * 
 */
@Service
public class NewBuFinanceInventorySummaryServiceImpl implements BuFinanceInventorySummaryService {

	private static final Logger logger = LoggerFactory.getLogger(NewBuFinanceInventorySummaryServiceImpl.class);

	// 商家dao
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	// 商品dao
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;
	// 仓库dao
	@Autowired
	private DepotInfoDao depotInfoDao;

	// 事业部财务经销存
	@Autowired
	private BuFinanceInventorySummaryDao buFinanceInventorySummaryDao;
	// 事业部 库存
	@Autowired
	private BuInventoryDao  buInventoryDao;// 事业部库存
	@Autowired
	private SettlementPriceRecordDao settlementPriceRecordDao;
	@Autowired
	private SettlementPriceDao settlementPriceDao;
	
	// 采购入库表体dao
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	// 采购订单表体dao
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	// 销售出库表体dao
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 盘点结果表体dao
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;
	// 库存调整头dao
	@Autowired
	private AdjustmentInventoryDao adjustmentInventoryDao;
	// 采购订单dao
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	// 采购入库单dao
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	// 采购入库关联采购订单表
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;	
	// 销售订单商品表
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
    //销出库单
    @Autowired
    private SaleOutDepotDao saleOutDepotDao;
    @Autowired
    private FileTaskMongoDao fileTaskDao;
    @Autowired
    private MerchantBuRelDao merchantBuRelDao;
	// 组码dao
	@Autowired
	private GroupCommbarcodeDao groupCommbarcodeDao;
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;// 月结账单	
	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;//销售退货商品
    @Autowired
    private TransferOrderDao transferOrderDao;// 调拨订单
    @Autowired
    private TransferInDepotDao transferInDepotDao;// 调拨入库

    @Autowired
    private PurchaseReturnOdepotDao purchaseReturnOdepotDao;// 采购退货出库库单
    @Autowired
    private PurchaseReturnOrderDao purchaseReturnOrderDao;// 采购退货订单    
    @Autowired
    private BuFinanceWarehouseDetailsDao buFinanceWarehouseDetailsDao;
    @Autowired
    private BuFinancePurchaseNotshelfDao buFinancePurchaseNotshelfDao;
	//(事业部财务经销存)采购残损明细表 dao
	@Autowired
	private BuFinancePurchaseDamagedDao buFinancePurchaseDamagedDao;
	//(事业部财务经销存)销售残损明细表 dao
	@Autowired
	private BuFinanceSaleDamagedDao buFinanceSaleDamagedDao;
	//(事业部财务经销存)盘盈盘亏明细表 dao
	@Autowired
	private BuFinanceTakesStockResultsDao buFinanceTakesStockResultsDao;
	//(事业部财务经销存)销毁明细表 dao
	@Autowired
	private BuFinanceDestroyDao buFinanceDestroyDao;
	//(事业部财务经销存)销售上架
	@Autowired
	private BuFinanceSaleShelfDao buFinanceSaleShelfDao;
    @Autowired
    private PurchaseReturnItemDao purchaseReturnItemDao;// 采购退货订单表体
	@Autowired
	private BuFinanceDecreasePurchaseNotshelfDao buFinanceDecreasePurchaseNotshelfDao;//(事业部财务经销存)本期减少采购在途表
	// 财务经销存销售未上架
	@Autowired
	private BuFinanceSaleNotshelfDao buFinanceSaleNotshelfDao;	
	//(财务经销存)累计采购在途明细表
	@Autowired
	private BuFinanceAddPurchaseNotshelfDetailsDao buFinanceAddPurchaseNotshelfDetailsDao;
	//(财务经销存)累计销售在途明细表
	@Autowired
	private BuFinanceAddSaleNoshelfDetailsDao buFinanceAddSaleNoshelfDetailsDao;
	//(财务经分销)本期减少销售在途
	@Autowired
	private BuFinanceDecreaseSaleNoshelfDao buFinanceDecreaseSaleNoshelfDao;
    @Autowired
    private BuFinanceAddTransferNoshelfDetailsDao buFinanceAddTransferNoshelfDetailsDao;// 事业部财务经销存 累计调拨
    @Autowired
    private BuFinanceMoveDetailsDao buFinanceMoveDetailsDao;// 事业部财务经销存本期移库明细
    @Autowired
    private BuFinanceLocationAdjustmentDetailsDao buFinanceLocationAdjustmentDetailsDao;//(事业部财务务经销存)事业部库位类型调整单明细表
    @Autowired
    private LocationAdjustmentOrderDao locationAdjustmentOrderDao;//库位调整单
    @Autowired
    private BuMoveInventoryDao buMoveInventoryDao;// 移库单
    @Autowired
    private ExchangeRateDao exchangeRateDao;// 汇率
    @Autowired    
    private BusinessUnitDao businessUnitDao;
    @Autowired    
    private WeightedPriceDao weightedPriceDao;
    @Autowired    
    private SdWeightedPriceDao sdWeightedPriceDao;
    @Autowired    
    private SdInterestPriceDao sdInterestPriceDao;
    @Autowired 
    private InventoryLocationAdjustmentOrderDao inventoryLocationAdjustmentOrderDao;// 库位调整单
  	@Autowired
	private BrandParentMongoDao brandParentMongoDao;// 标准品牌
  	@Autowired
  	private PurchaseSdOrderDao purchaseSdOrderDao;
  	@Autowired
  	private PurchaseSdOrderSditemDao purchaseSdOrderSditemDao;
    @Autowired
    private BuFinanceSdWarehouseDetailsDao buFinanceSdWarehouseDetailsDao;
    @Autowired
    private BuFinanceSdAddPurchaseNotshelfDao buFinanceSdAddPurchaseNotshelfDao;    
    @Autowired
    private CostPriceDifferenceDao costPriceDifferenceDao;
    
    
	@Autowired
	private RMQLogProducer rocketLogMQProducer;
    

	/**
	 * 保存事业部财务进销存报表
	 * @param json
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	@Override
	public void saveSummaryReport(String json, String key, String topics, String tags) throws Exception {

		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		Integer merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
		String ownMonth = (String) jsonMap.get("month");//月份

	    //查询所有商家,若指定了商家则只查本商家
 		MerchantInfoModel model = new MerchantInfoModel();
 		if (merchantId != null && merchantId.longValue() > 0) {
 			model.setId(Long.valueOf(merchantId));
 		}
 		List<MerchantInfoModel> merchantList = merchantInfoDao.list(model);

 		// 多线程生成每个商家报表
 		ExecutorService pool = Executors.newCachedThreadPool();
 		for (int i = 0; i < merchantList.size(); i++) {
 			MerchantInfoModel merchant = merchantList.get(i);
 			
 			String threadName = merchant.getId()+"--------finance_thread"+(i+1);
 			Runnable run = new Runnable() {
 				public void run() {
 					try{			            
 						//生成商家财务报表
 					    saveGenerateReport(threadName,merchant,jsonMap);
 					    
 					} catch (Exception e) {
 						e.printStackTrace();
 						logger.error(threadName+"事业部财务经销存商家："+merchant.getName()+"---"+e.getMessage());
 						//发送异常日志                            
                        sendLog("0", e.getMessage(), merchant,ownMonth);
 					}finally {
 						//删除财务刷新任务
 						Map<String,Object> delTaskMap = new HashMap<String, Object>();
 						delTaskMap.put("merchantId", merchant.getId());
 						delTaskMap.put("ownMonth", ownMonth);						
 						delTaskMap.put("taskType","SXSYBCW");//	任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SXCW-刷新财务报表   SYBCWJXC- 事业部财务经销存  SYBYWJXC-进销存汇总报表  SXSYBCW-刷新事业部财务经销存
 						// 月份不为空说明来自页面删除任务
 						if (!StringUtils.isEmpty(ownMonth)) {
                            fileTaskDao.remove(delTaskMap);
                        }
 						
					}
 				}
 			};
 			pool.execute(run);
 		}
	}

	/**
	 * 生成报表
	 * 
	 * @param merchant
	 * @param depotIds
	 * @param month
	 * @param threadName
	 * @param barcode
	 * @param productName
	 */
	public void saveGenerateReport(String threadName,MerchantInfoModel merchant,Map<String, Object> jsonMap) throws Exception {
		
		String months = (String) jsonMap.get("month");//月份
		String buId = (String)jsonMap.get("buId");// 事业部id
	
		//计算要刷新的月份
		if(StringUtils.isEmpty(months)){
			//若没有指定月份则取当前时间前一天日期近二个月月份,定时器刷新未关账的，本月、上月
			months = TimeUtils.getLastTwoMonthsByNow();
		}
		// 现在不查代理商家
		List<MerchantRelModel> relList=new ArrayList<>();
		List<Long> idsList = getMerchantIdList(relList, merchant);//组装商家及代理商家id
		
		int goodsStartIndex = 0;
    	int goodsPageSize = 1000;//每页1000
		//查询商家及代理商家的所有商品货品条码、货号
		Map<String, Object> MerchandiseMap = new HashMap<String, Object>();
		MerchandiseMap.put("list", idsList);
		MerchandiseMap.put("pageSize", goodsPageSize);
		List<Map<String,Object>> allMerchandiseList=new ArrayList<>();
		while (true) {
			MerchandiseMap.put("startIndex", goodsStartIndex);
			List<Map<String,Object>> goodsList = merchandiseInfoDao.getAllMerchandiseByMerchantId(MerchandiseMap);
			goodsStartIndex=goodsStartIndex+goodsPageSize;
			if (goodsList.size()==0)break;
			allMerchandiseList.addAll(goodsList);
			goodsList=null;
			
		}
				
		if(allMerchandiseList==null||allMerchandiseList.size()<=0) {
			logger.info(threadName+"--商家名称："+merchant.getName()+",商品数量为0,结束执行");
            sendLog("0", threadName+"--商家名称："+merchant.getName()+",商品数量为0,结束执行", merchant,months);			
			return;
		}
		
		//获取所有的事业部
		List<BusinessUnitModel> buList = businessUnitDao.list(new BusinessUnitModel());
		Map<Long,BusinessUnitModel> buModelMap=new HashMap<>();
		for (BusinessUnitModel model : buList) {
			buModelMap.put(model.getId(), model);
		}
		// 获取所有仓
		List<DepotInfoModel> depotInfoList = depotInfoDao.list(new DepotInfoModel());
		Map<Long,DepotInfoModel> depotModelMap=new HashMap<>();
		for (DepotInfoModel model : depotInfoList) {
			depotModelMap.put(model.getId(), model);
		}
		
		// 查询标准条码 所对应的组码
		List<Map<String, Object>> commbarcodeAndGroupMapList = groupCommbarcodeDao.getCommbarcodeAndGroupCommbarcode();
		Map<String, Object> groupCommbarcodeMap=new HashMap<>();
		for (Map<String, Object> map : commbarcodeAndGroupMapList) {
			String groupCommbarcode = (String)map.get("group_commbarcode");
			String commbarcode = (String)map.get("commbarcode");			
			groupCommbarcodeMap.put(commbarcode, groupCommbarcode);
		}
		// 获取标准品牌
        Map<Long, BrandParentMongo> brandParentMap = getBrandParent();
		// 获取商品id 对应的商品信息
		Map<Long, Map<String, Object>> allMerchandiseMap = getAllMerchandiseMap(allMerchandiseList,groupCommbarcodeMap,brandParentMap);
		// 查询商家的所有仓库 若指定了仓库则只查指定仓库  统计汇总该商家保税仓、海外仓、中转仓的货品进销存数据
		/**Map<String, Object> depotMap = new HashMap<String, Object>();
		depotMap.put("merchantId", merchant.getId());
		if(!StringUtils.isEmpty(depotId)) {
			depotMap.put("depotId", Long.valueOf(depotId));
		}
		depotMap.put("types", "a,c,d");//保税仓、海外仓、中转仓
		List<DepotInfoModel> depotList = depotInfoDao.listMerchantDepot(depotMap);
		if (depotList==null||depotList.size()<=0) {
			logger.info(threadName+"--商家名称："+merchant.getName()+",仓库数量为0,结束执行");
            sendLog("0", threadName+"--商家名称："+merchant.getName()+",仓库数量为0,结束执行", merchant,months);
			return;
		}
		// 查询所有仓库
		depotMap.remove("depotId");
		List<DepotInfoModel> depotAllList = depotInfoDao.listMerchantDepot(depotMap);*/
		// 获取仓库id
    	//List<Long> depotIdList = getDepotIdList(depotAllList);//11这里要获取全仓
		String[] montharr = months.split(",");
		//月份循环
        for(String month:montharr){
        	if (Timestamp.valueOf("2020-07-01 00:00:00").getTime()>Timestamp.valueOf(month+"-01 00:00:00").getTime()) {
        		if (!("1000000544".equals(merchant.getTopidealCode())||"1000005377".equals(merchant.getTopidealCode())||"1000000204".equals(merchant.getTopidealCode()))) {// 卓普新是新商家不用过滤
        			logger.info(threadName+"事业部--商家名称:"+merchant.getName()+",月份："+month+"必须大于6月");
                    sendLog("0", threadName+"事业部--商家名称:"+merchant.getName()+",月份："+month+"必须大于6月", merchant,months);
        			continue;
				}       		
			}    	
        	//查询要刷新的事业部
    		MerchantBuRelModel merchantBuRelModel=new MerchantBuRelModel();
			merchantBuRelModel.setMerchantId(merchant.getId());
			if (!StringUtils.isEmpty(buId)) {
				merchantBuRelModel.setBuId(Long.valueOf(buId));
			}
			merchantBuRelModel.setStatus("1");
			List<MerchantBuRelModel> merchantBuRelList = merchantBuRelDao.list(merchantBuRelModel);
			if (merchantBuRelList.size()==0) {
				logger.info(threadName+"事业部--商家名称:"+merchant.getName()+"没有找到事业部");
                sendLog("0", threadName+"事业部--商家名称:"+merchant.getName()+"没有找到事业部", merchant,months);
				continue;
			}
    		//循环事业部开始
    		for (MerchantBuRelModel buModel : merchantBuRelList) {
				if (buModel.getBuId()==null) {
					logger.info(threadName+"事业部--商家名称:"+merchant.getName()+"事业部id不能为空");
	                sendLog("0", threadName+"事业部--商家名称:"+merchant.getName()+"事业部id不能为空", merchant,months);
					continue;				
				}
				Map<String, BigDecimal> priceDifferenceMap=getPriceDifference(merchant,buModel);
				
				//查询本月是否已关账
				Map<String, Object> statusMap = new HashMap<String, Object>();
	        	statusMap.put("merchantId", merchant.getId());
	        	statusMap.put("month", month);
	        	statusMap.put("buId", buModel.getBuId());
	        	statusMap.put("status",DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030);
	        	String status=buFinanceInventorySummaryDao.getSummaryStatus(statusMap);
				//状态 029-未关账 030-已关账
				if(!StringUtils.isEmpty(status)&&status.equals(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030)){
					logger.info(threadName+"--商家名称:"+merchant.getName()+",事业部:"+buModel.getBuName()+",月份："+month+"已关账,结束执行");
					if (!StringUtils.isEmpty(months)) {// 非定时器来的记录已关账数据
						sendLog("0", threadName+"--商家名称:"+merchant.getName()+",事业部:"+buModel.getBuName()+",月份："+month+"已关账,结束执行", merchant,months);
					}
					continue;
				}        	
	        	//加权单价 和标准单价处理
	        	String orderSource = merchant.getAccountPrice();//核算单价 1-标准成本单价 2-月末加权单价
	        	// 生成总表信息
	        	Map<String, Map<String, Object>> saveSummaryMap=new HashMap<>();       	
	        	// 标准单价 公共数据        	
				Map<String, Map<String, Object>> deBuLastSummaryMap=new HashMap<>();// 查询上月事业部财务经销存本期单价,金额币种 和数量  期初
				Map<String, Map<String, Object>> buLastSummaryMap=new HashMap<>();	// 去掉仓库的币种和单价
				Map<String, Map<String, Object> > settlementPriceMap=new HashMap<>();// 结算价格里的单价和币种
	        	//加权单价计算
				Map<String, Map<String, Object>>weightedPriceMap=new HashMap<>();
	    		//期初加权币种 getWeightedPrice方法里面赋值
	    		Map<String, String>currencyMap=new HashMap<>();  
	    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {//标准成本单价
	    			getCommbarcodePrice(buModel,saveSummaryMap,merchant,month,allMerchandiseList,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap);
	    			saveDetails(buModel,saveSummaryMap,merchant,buModelMap,month,depotModelMap,allMerchandiseMap,null,null, groupCommbarcodeMap,
	    					orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,null);
	    		}else if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {//月末加权单价   			
	            	
	        		//用于计算加权 getWeightedPrice方法里面赋值
	        		Map<String,  Map<String, Object>>weightedSummaryMap=new HashMap<>();      		
	            	getWeightedPrice(buModel,saveSummaryMap,merchant,month,deBuLastSummaryMap,weightedSummaryMap,currencyMap);
	    			//本月采购总数量=本期采购结转数量(汇总)=本期采购入库数量+本期采购在途量-本期采购减少在途量+事业部移库入数量
	        		//本月采购总金额=本期采购入库金额+本期采购在途入库金额-本期采购减少在途入库金额+事业部移库入库金额
	        		//本期采购结转数量(汇总)=本期采购入库数量+本期采购在途量-本期采购减少在途量+事业部移库入数量
	    			logger.info("获取并生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)加权单价明细数据");			
	    			saveDetails(buModel,saveSummaryMap,merchant,buModelMap,month,depotModelMap,allMerchandiseMap,weightedSummaryMap,currencyMap, groupCommbarcodeMap,
	    					orderSource,null,null,null,weightedPriceMap);		   			
	    		}else {
	    			logger.error(threadName+"--商家名称："+merchant.getName()+"month:"+month+",商家核算单据没有维护");
	    			sendLog("0", threadName+"--商家名称："+merchant.getName()+"month:"+month+",商家核算单据没有维护", merchant,month);
	    			continue;
				}
    					   		
			            
				//公共查询条件
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchantId", merchant.getId());				
				paramMap.put("month", month);
				paramMap.put("buId", buModel.getBuId());
				paramMap.put("tag", "1");// 不能删除 用于区分财务还是业务  财务只取部分仓 业务要取全仓
				
				// 获取 事业部库存 按 商家 仓库 事业部 库存余量
				List<Map<String, Object>> depotEndNumList = buInventoryDao.getBuDepotEndNumForMap(paramMap);					
				//期末结存数量转换为map存储便于获取
	        	for(Map<String, Object> map:depotEndNumList){        		
	        		String unit = (String)map.get("unit");
	        		Long goodsId = (Long)map.get("goods_id");
	        		Long depotId = (Long)map.get("depot_id");
	        		
					//String depotIdBuIdGoodsId = (String)map.get("depot_id_bu_id_goods_id");
					BigDecimal num = (BigDecimal)map.get("surplus_num");
					if (num==null) {
						num=new BigDecimal(0);
					}
					Integer changeNum=0;
					// 当单位为箱托的时候转化成件 说明 此处如果箱托转化  // 总表生成时 已经做了箱托转化校验此处不做校验了
					if (DERP.INVENTORY_UNIT_1.equals(unit)||DERP.INVENTORY_UNIT_0.equals(unit)) {
						MerchandiseInfoModel merchandiseInfoModel = merchandiseInfoDao.searchById(goodsId);
						Integer boxToUnit = merchandiseInfoModel.getBoxToUnit();
						Integer torrToUnit = merchandiseInfoModel.getTorrToUnit();
						// 箱转件
						if(unit.equals(DERP.INVENTORY_UNIT_1)&&boxToUnit!=null){						
							changeNum=num.intValue()*boxToUnit.intValue();
						}
						// 托转件
						if(unit.equals(DERP.INVENTORY_UNIT_0)&&torrToUnit!=null){
							changeNum=num.intValue()*torrToUnit.intValue();
						}
					}else {
						changeNum=num.intValue();
					}
											
					String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
		    		if (saveSummaryMap.containsKey(deBuKey)){
		    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
		    			Integer surplusNumMap = (Integer) summaryMap.get("surplusNum");
		    			if (surplusNumMap==null)surplusNumMap=0;
		    			summaryMap.put("surplusNum", changeNum+surplusNumMap);
		    			saveSummaryMap.put(deBuKey, summaryMap);        					
					}else {
						Map<String, Object> summaryMap = new HashMap<>();    				
						summaryMap.put("surplusNum", changeNum);
						saveSummaryMap.put(deBuKey, summaryMap);
					}
				}
											
				//清空事业部商家、仓库、月份报表数据
				logger.info("清除:"+merchant.getName()+"，"+"，"+buModel.getBuName()+month+"月的事业部财务汇总数据和差异分析数据");
            	//清空事业部商家、仓库、月份报表数据 
				buFinanceInventorySummaryDao.delBuDepotMonthReport(paramMap);
				// 四.删除(事业部财务经销存)采购残损明细表					
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存)采购残损明细表");
				buFinancePurchaseDamagedDao.delBuFinancePurchaseDamaged(paramMap);
				
				//(1)(事业部财务经销存)获取采购残损明细表 来货短缺数据
				List<Map<String, Object>> purchaseDamagedLackList = purchaseOrderItemDao.getBuFinancePurchaseDamagedLackList(paramMap);
				//(2)(财务经销存)获取采购残损明细表 来货残损数据
				List<Map<String, Object>> purchaseDamagedWornExpireList = purchaseWarehouseItemDao.getBuFinancePurchaseDamagedWornExpireList(paramMap);
				//生成(财务经销存)采购残损明细表
				logger.info("生成:"+merchant.getName()+"，"+month+"(财务经销存)采购残损明细表");
				saveFinancePurchaseDamaged(depotModelMap,saveSummaryMap,merchant,buModel,month,allMerchandiseMap,purchaseDamagedLackList,purchaseDamagedWornExpireList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap);
				//清缓存
				purchaseDamagedLackList=null;
				purchaseDamagedWornExpireList=null;
				
				
				// 五.删除(财务经销存)销售残损明细表
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存)销售残损明细表");					
				buFinanceSaleDamagedDao.delBuFinanceSaleDamaged(paramMap);
				//(2)(事业部财务经销存)获(财务经销存)销售残损明细表代销
				List<Map<String, Object>> saleDamagedDXList = saleOutDepotItemDao.getBuFinanceSaleDamagedDX(paramMap);
			    //生成(事业部财务经销存)销售残损明细
				logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)销售残损明细表");
				saveFinanceSaleDamaged(saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,saleDamagedDXList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,        		//期初加权币种 getWeightedPrice方法里面赋值
		        		currencyMap,priceDifferenceMap);
				//清缓存
				saleDamagedDXList=null;				
				// 六.删除(事业部财务经销存)盘盈盘亏明细表
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存)盘盈盘亏明细表)");
				buFinanceTakesStockResultsDao.delBuFinanceTakesStockResults(paramMap);
				// 获取(事业部财务经销存)盘盈盘亏明细表
				List<Map<String, Object>> takesStockResultsList = takesStockResultItemDao.getBuFinanceTakesStockResults(paramMap);
			    //生成(事业部财务经销存)盘盈盘亏明细表
				logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)盘盈盘亏明细表)");
				saveFinanceTakesStockResults(saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,takesStockResultsList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,        		//期初加权币种 getWeightedPrice方法里面赋值
		        		currencyMap,priceDifferenceMap);
				// 清缓存
				takesStockResultsList=null;
				
				// 八.删除(财务经销存)销毁明细表
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存)销毁明细表");
				buFinanceDestroyDao.delBuFinanceDestroy(paramMap);

				// 获取(事业部财务经销存)销毁明细表
				List<Map<String, Object>> financeDestroyList = adjustmentInventoryDao.getBuFinanceDestroy(paramMap);
			    //生成(事业部财务经销存)销毁明细表
				logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)销毁明细表");
				savefinanceDestroy(saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,financeDestroyList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,        		//期初加权币种 getWeightedPrice方法里面赋值
		        		currencyMap,priceDifferenceMap);
				// 清缓存
				financeDestroyList=null;										
				
				// 十二.删除(事业部财务经销存)累累计销售在途明细表
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存)累计销售在途明细表");					
				buFinanceAddSaleNoshelfDetailsDao.delBuFinanceAddSaleNoshelfDetails(paramMap);
				// 获取(事业部财务经销存)累计销售在途明细表代销
				List<Map<String, Object>> addSaleNoshelfDetailsDXList = saleOutDepotItemDao.getBuFinanceAddSaleNoshelfDetailsDX(paramMap);								
				// 生成(财务经销存)累计销售在途明细表
				logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)累计销售在途明细表");
				saveFinanceAddSaleNoshelfDetails(saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,addSaleNoshelfDetailsDXList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,        		//期初加权币种 getWeightedPrice方法里面赋值
		        		currencyMap);	
				addSaleNoshelfDetailsDXList=null;
							
				// 十四.删除(事业部财务经销存) 本期减少销售在途
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存) 本期减少销售在途");
				buFinanceDecreaseSaleNoshelfDao.delBuFinanceDecreaseSaleNoshelf(paramMap);
				
				// 获取(事业部财务经销存) 本期减少销售在途
				List<Map<String, Object>> decreaseSaleNoshelfDXList = saleOutDepotItemDao.getBuFinanceDecreaseSaleNoshelfDX(paramMap);
				// 生成(事业部财务经销存)本期减少销售在途
				saveFinanceDecreaseSaleNoshelf(saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,decreaseSaleNoshelfDXList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,
		        		currencyMap);
				decreaseSaleNoshelfDXList=null;
				// 十五.删除(事业部财务经销存) 累计调拨在途明细表
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存) 累计调拨在途明细表");					
				buFinanceAddTransferNoshelfDetailsDao.delBuFinanceAddTransferNoshelfDetail(paramMap);
				// 获取(事业部财务经销存)累计调拨在途明细表
				List<Map<String, Object>> addTransferNoshelfList = transferOrderDao.getBuBusinessAddTransferNoshelfDetails(paramMap);
				saveFinanceAddTransferNoshelf(saveSummaryMap,merchant, depotModelMap,buModel, month,allMerchandiseMap, addTransferNoshelfList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,
		        		currencyMap);
				addTransferNoshelfList=null;
				
				//Map<String, Map<String, Object>> goodsMap=new HashMap<>();
				// 十六.删除(事业部财务经销存) 本期事业部移库明细
				// buFinanceMoveDetailsDao.delBuFinanceMoveDetails(paramMap); 
				int moveDetailsOutCount = buMoveInventoryDao.getBuFinanceMoveOutDetailsCount(paramMap);
				Double moveOutCeil = Math.ceil(Integer.valueOf(moveDetailsOutCount) * 0.001);// 循环次数
				int startMoveOutIndex = 0;
				int pageMoveOutSize = 1000;//每页1000
				//移库明细出
				Map<String, Object> paramMoveOutMap = new HashMap<String, Object>();
				paramMoveOutMap.put("merchantId", merchant.getId());
				paramMoveOutMap.put("month", month);
				paramMoveOutMap.put("buId", buModel.getBuId());
				paramMoveOutMap.put("pageSize", pageMoveOutSize);
				paramMoveOutMap.put("tag", "2");
				for (int i = 0; i < moveOutCeil; i++) {
					paramMoveOutMap.put("startIndex", startMoveOutIndex);
					//获取(事业部财务经销存) 本期事业部移库明细
					List<Map<String, Object>> buFinanceMoveList = buMoveInventoryDao.getBuFinanceMoveDetails(paramMoveOutMap);
					saveBuFinanceMoveDetails(saveSummaryMap,merchant, depotModelMap,buModel, month, allMerchandiseMap,buFinanceMoveList,
							orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,currencyMap,"2");
					buFinanceMoveList=null;
					logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)本期事业部移库明细(出)"+"第"+(i+1)+"次");
					startMoveOutIndex=startMoveOutIndex+pageMoveOutSize;
				} 

				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务务经销存)库位类型调整明细表");
				//删除 (事业部业务经销存)库位类型调整明细表
				buFinanceLocationAdjustmentDetailsDao.delFinanceLocationAdjustmentDetails(paramMap);
				// 生成库位类型调整明细表
				List<Map<String, Object>>locationAdjustmentOrderMapList=locationAdjustmentOrderDao.getLocationAdjustmentOrder(paramMap);
				// 生成库位类型调整明细表
				logger.info("正在生成库位类型调整明细表"+"商家:"+merchant.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
				saveBuFinanceLocationAdjustmentDetail(merchant,buModel,month,locationAdjustmentOrderMapList,priceDifferenceMap,weightedPriceMap);										
				locationAdjustmentOrderMapList=null;
				

				
			    
				
				//七.删除(事业部财务经销存)销售已上架明细表
				logger.info("清除:"+merchant.getName()+"，"+month+"(事业部财务经销存)销售已上架明细表");
				buFinanceSaleShelfDao.delBuFinanceSaleShelf(paramMap);
				//3获取(事业部财务经销存)销售已上架明细表(1购销订单, 2代销订单, 3电商订单)
				// (1)(事业部财务经销存)获取销售订单代销已上架的 的量
				List<Map<String, Object>> saleOrderShelfMapList = saleOutDepotItemDao.getBuFinanceSaleOrderShelf(paramMap);
		
				// (5)(事业部财务经销存)销售已上架销售出库 -电商订单退货
				List<Map<String, Object>> orderReturnIdepotMapList = saleOutDepotItemDao.getBuOrderReturnIdepot(paramMap);
				// (5.1)(事业部财务经销存)销售已上架销售出库 -电商订单款
				List<Map<String, Object>> orderReturnIdepotAmountMapList = saleOutDepotItemDao.getBuOrderReturnIdepotAmount(paramMap);
				// (6) (事业部财务经销存) 销售退货单据中类型为购销退货的入库量
				List<Map<String, Object>> saleReturnIdepotGXMapList = saleOutDepotItemDao.getBuFinanceSaleReturnIdepotGX(paramMap);
				// (7) (事业部财务经销存) 账单出库数量（库存调整类型为调减的量）
				List<Map<String, Object>> billOutInDepotSubMapList = saleOutDepotItemDao.getBuFinanceBillOutInDepotSubMapList(paramMap);
				// (8) (事业部财务经销存) 账单出库数量（库存调整类型为调增的量）
				List<Map<String, Object>> billOutInDepotAddMapList = saleOutDepotItemDao.getBuFinanceBillOutInDepotAddMapList(paramMap);
				//生成(事业部财务经销存)销售已上架的量
				//(9) 查询库存调整单的量					
				List<Map<String, Object>>locationOrderMapList=inventoryLocationAdjustmentOrderDao.getLocationAdjustmentOrder(paramMap);
				
				logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)销售已上架明细表");
				saveFinanceSaleShelf(saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,saleOrderShelfMapList,orderReturnIdepotMapList,orderReturnIdepotAmountMapList,saleReturnIdepotGXMapList,
						billOutInDepotSubMapList,billOutInDepotAddMapList,locationOrderMapList,
						orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,currencyMap,priceDifferenceMap);				
				//清缓存
				saleOrderShelfMapList=null;
				orderReturnIdepotMapList=null;
				saleReturnIdepotGXMapList=null;
				billOutInDepotSubMapList=null;
				billOutInDepotAddMapList=null;
									
				// (3)(财务经销存)获取电商订单已出库 的量 比较大要分页查询 挪到下面去了
		    	int count =saleOutDepotItemDao.getBuFinanceDSOrderShelfCount(paramMap);
		    	Double ceil = Math.ceil(Integer.valueOf(count) * 0.001);// 循环次数
				int startIndex = 0;
		    	int pageSize = 1000;//每页1000
		    	Map<String, Object> paramDSMap = new HashMap<String, Object>();
		    	paramDSMap.put("merchantId", merchant.getId());
		    	paramDSMap.put("month", month);
		    	paramDSMap.put("buId", buModel.getBuId());			    	  	
		    	paramDSMap.put("pageSize", pageSize);	
				for (int i = 0; i < ceil; i++) {
					paramDSMap.put("startIndex", startIndex);
					List<Map<String, Object>> dSOrderShelfMapList = saleOutDepotItemDao.getBuFinanceDSOrderShelf(paramDSMap);
					saveDSFinanceSaleShelf(saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,dSOrderShelfMapList,
							orderSource,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,currencyMap,priceDifferenceMap);
					// 清缓存
					dSOrderShelfMapList=null;
					logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)销售已上架明细表电商"+"第"+(i+1)+"次");
					startIndex=startIndex+pageSize;
				}      						
				// 生产事业部财务经销存总表
				saveGoodsReport(threadName, saveSummaryMap,merchant,depotModelMap,buModel,month,allMerchandiseMap,
						deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,weightedPriceMap,currencyMap,orderSource);    		    			    			
				//明细数据 结束 -------------------------
				logger.info("生成 事业部财务经销存结束"+merchant.getName()+",仓库"+",事业部"+buModel.getBuName()+",月份"+month);
			
			
			}
    			
    			
    		
        	    		
    		
    		
        }
		

        //发送成功日志
        sendLog("1", "", merchant, months);
		logger.info("----------------------------事业部财务进销存报表生成结束");
	}

	/**
	 * 生成库位类型调整明细表
	 * @param merchant
	 * @param buModel
	 * @param month
	 * @param locationAdjustmentOrderMapList
	 * @param priceDifferenceMap
	 * @throws SQLException 
	 */
	private void saveBuFinanceLocationAdjustmentDetail(MerchantInfoModel merchant, MerchantBuRelModel buModel,
			String month, List<Map<String, Object>> locationAdjustmentOrderMapList,
			Map<String, BigDecimal> priceDifferenceMap,
			Map<String, Map<String, Object>>weightedPriceMap) throws SQLException {
		List<BuFinanceLocationAdjustmentDetailsModel> orderList= new ArrayList<>();
    	// 生成库位类型调整明细表
    	for (Map map : locationAdjustmentOrderMapList) {	
    		
    		Long brandId = (Long) map.get("comm_brand_parent_id");
    		String brandName = (String) map.get("comm_brand_parent_name");
    		String superiorParentBrand = (String) map.get("superior_parent_brand");
    		String commbarcode = (String) map.get("commbarcode");    		
    		Long id = (Long) map.get("id");
    		String code = (String) map.get("code");
    		String orderCode = (String) map.get("order_code");
    		Long depotId = (Long) map.get("depot_id");
    		String depotName = (String) map.get("depot_name");
    		String orderType = (String) map.get("order_type");
    		Long customerId = (Long) map.get("customer_id");
    		String customerName = (String) map.get("customer_name");   		
    		String platformCode = (String) map.get("platform_code");
    		String platformName = (String) map.get("platform_name");
    		String barcode = (String) map.get("barcode");
    		String goodsName = (String) map.get("goods_name");
    		Integer adjustNum = (Integer) map.get("adjust_num");
    		if (adjustNum==null)adjustNum=0;
    		String inventoryType = (String) map.get("inventory_type");//库存类型 0：好品 1：坏品',
    		Long inStockLocationTypeId = (Long) map.get("in_stock_location_type_id");
    		String inStockLocationTypeName = (String) map.get("in_stock_location_type_name");
    		Long outStockLocationTypeId = (Long) map.get("out_stock_location_type_id");
    		String outStockLocationTypeName = (String) map.get("out_stock_location_type_name");
    		if (StringUtils.isEmpty(barcode)) continue;
    		barcode=barcode.trim();
    		
    		String weightedKey=buModel.getBuId()+","+commbarcode;				
			Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
			if (priceMap==null)priceMap=new HashMap<>();
			BigDecimal weightedPrice = (BigDecimal) priceMap.get("weightedPrice");

			if (weightedPrice==null)weightedPrice=new BigDecimal(0);

    	
    		
    		// 调减
        	BuFinanceLocationAdjustmentDetailsModel adjustmentDetailsSub=new BuFinanceLocationAdjustmentDetailsModel();
        	adjustmentDetailsSub.setMerchantId(merchant.getId());
        	adjustmentDetailsSub.setMerchantName(merchant.getName());
        	adjustmentDetailsSub.setCostCurrency(merchant.getAccountCurrency());
        	adjustmentDetailsSub.setOutDepotPrice(weightedPrice);
        	adjustmentDetailsSub.setDepotId(depotId);
        	adjustmentDetailsSub.setDepotName(depotName);
        	adjustmentDetailsSub.setBuId(buModel.getBuId());
        	adjustmentDetailsSub.setBuName(buModel.getBuName());
        	adjustmentDetailsSub.setMonth(month);
        	//adjustmentDetails.setGoodsId(null);
        	//adjustmentDetails.setGoodsNo(month);
        	adjustmentDetailsSub.setGoodsName(goodsName);
        	adjustmentDetailsSub.setCommbarcode(commbarcode);
        	adjustmentDetailsSub.setBarcode(barcode);
        	adjustmentDetailsSub.setBrandId(brandId);
        	adjustmentDetailsSub.setBrandName(brandName);
        	adjustmentDetailsSub.setSuperiorParentBrand(superiorParentBrand);
        	adjustmentDetailsSub.setStockLocationTypeId(outStockLocationTypeId);
        	adjustmentDetailsSub.setStockLocationTypeName(outStockLocationTypeName);
        	adjustmentDetailsSub.setOperateType("0");
        	adjustmentDetailsSub.setInventoryType(inventoryType);
        	adjustmentDetailsSub.setOrderType(orderType);
        	adjustmentDetailsSub.setNum(adjustNum);
        	adjustmentDetailsSub.setOrderId(id);
        	adjustmentDetailsSub.setOrderCode(code);
        	adjustmentDetailsSub.setCustomerId(customerId);
        	adjustmentDetailsSub.setCustomerName(customerName);
        	adjustmentDetailsSub.setStorePlatformName(platformName);
        	adjustmentDetailsSub.setStorePlatformCode(platformCode);
        	adjustmentDetailsSub.setExternalCode(orderCode);

   		  	if (outStockLocationTypeId!=null) {
				String locationKey=outStockLocationTypeId+","+barcode;
				BigDecimal outDifferencePrice = priceDifferenceMap.get(locationKey);
				if (outDifferencePrice==null)outDifferencePrice=new BigDecimal(0);
				outDifferencePrice=outDifferencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				BigDecimal outDifferencePriceAmount=outDifferencePrice.multiply(new BigDecimal(adjustNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				adjustmentDetailsSub.setDifferencePrice(outDifferencePrice);
				adjustmentDetailsSub.setDifferenceAmount(outDifferencePriceAmount);
   		  	} 
        	
        	orderList.add(adjustmentDetailsSub); 
        	// 调增
        	if (inStockLocationTypeId!=null) {
        		String locationKey=inStockLocationTypeId+","+barcode;
				BigDecimal inDifferencePrice = priceDifferenceMap.get(locationKey);
				if (inDifferencePrice==null)inDifferencePrice=new BigDecimal(0);
				inDifferencePrice=inDifferencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				BigDecimal inDifferencePriceAmount=inDifferencePrice.multiply(new BigDecimal(adjustNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
								
        		BuFinanceLocationAdjustmentDetailsModel adjustmentDetailsAdd=new BuFinanceLocationAdjustmentDetailsModel();
        		BeanUtils.copyProperties(adjustmentDetailsSub, adjustmentDetailsAdd);
        		adjustmentDetailsAdd.setOperateType("1");
        		adjustmentDetailsAdd.setStockLocationTypeId(inStockLocationTypeId);
        		adjustmentDetailsAdd.setStockLocationTypeName(inStockLocationTypeName);
        		adjustmentDetailsAdd.setDifferencePrice(inDifferencePrice);
        		adjustmentDetailsAdd.setDifferenceAmount(inDifferencePriceAmount);
            	orderList.add(adjustmentDetailsAdd); 
			}    		  					
		}
       	
    	// 循环信息生成库位调整明细
    	for (BuFinanceLocationAdjustmentDetailsModel model : orderList) {
    		buFinanceLocationAdjustmentDetailsDao.save(model);
		}    	
    	orderList=null;
		
	}

	/**
	 * 业部财务经销存获取成本差异表数据
	 * @param merchant
	 * @param buModel
	 * @return
	 * @throws SQLException 
	 */
	private Map<String, BigDecimal> getPriceDifference(MerchantInfoModel merchant, MerchantBuRelModel buModel) throws SQLException {
		
		Map<String, BigDecimal> priceDifferenceMap=new HashMap<String, BigDecimal>();
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("merchantId", merchant.getId());				
		paramMap.put("buId", buModel.getBuId());
		//业部财务经销存获取成本差异表数据
		List<Map<String, Object>> costPriceDifferenceMapList = costPriceDifferenceDao.getCostPriceDifferenceMapList(paramMap);
		for (Map<String, Object> map : costPriceDifferenceMapList) {
			Long stockLocationTypeId = (Long) map.get("stock_location_type_id");
			String barcode = (String) map.get("barcode");
			if (StringUtils.isEmpty(barcode)) continue;
			barcode=barcode.trim();
			BigDecimal differencePrice = (BigDecimal) map.get("differencePrice");
			if (differencePrice==null)differencePrice=new BigDecimal(0);
			String locationKey=stockLocationTypeId+","+barcode;
			priceDifferenceMap.put(locationKey, differencePrice);
		}
		return priceDifferenceMap;
	}

	/**
	 * 生成事业部财务经销存总表
	 * @param threadName
	 * @param saveSummaryMap
	 * @param merchant
	 * @param depot
	 * @param buModel
	 * @param month
	 * @param depotEndNumMap
	 * @param deBuLastSummaryMap
	 * @param buLastSummaryMap
	 * @param settlementPriceMap
	 * @param weightedPriceMap
	 * @param currencyMap
	 * @throws SQLException 
	 */
	private void saveGoodsReport(String threadName, Map<String, Map<String, Object>> saveSummaryMap,
			MerchantInfoModel merchant, Map<Long,DepotInfoModel> depotModelMap, MerchantBuRelModel buModel, String month,
			Map<Long, Map<String, Object>> allMerchandiseMap,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap, Map<String, Map<String, Object>> settlementPriceMap,
			Map<String, Map<String, Object>> weightedPriceMap, Map<String, String> currencyMap,String orderSource) throws SQLException {
		// --------------------事业部财务经销存总表开始-----------------------------
		// 仓库Id+事业部Id+商品Id
		//String deBuIdkey=depot.getId()+","+buModel.getBuId();
		Set<String> summaryKeySet = saveSummaryMap.keySet();
		for (String summaryKey : summaryKeySet) {
			String[] summaryKeyStr = summaryKey.split(",");
			String depotIdStr = summaryKeyStr[0];
			String buIdStr = summaryKeyStr[1];
			String goodsIdStr = summaryKeyStr[2];
			String deBuSummaryKey=depotIdStr+","+buIdStr;
			
			
			if (StringUtils.isEmpty(goodsIdStr)||"null".equals(goodsIdStr)) {
				logger.error(merchant.getName()+",月份"+month+",仓库"+depotIdStr+",循环的事业id:"+buModel.getBuId()+"商品id为空");
				continue;
			}
			Map<String, Object> goodsMap = allMerchandiseMap.get(Long.valueOf(goodsIdStr));
			if (goodsMap==null) {
				logger.error(merchant.getName()+",月份"+month+"事业部财务经销存总表数据在商品表中没有商品信息,仓库id:"+depotIdStr+",循环的事业id:"+buModel.getBuId()+"商品id:"+goodsIdStr);
				continue;
			}
			/**1. 商品信息*/
			Long brandId = (Long) goodsMap.get("brand_id");//品牌id
			String brandName = (String) goodsMap.get("brand_name");//品牌名称
			Long typeId = (Long) goodsMap.get("type_id");//商品二级分类id
			String typeName = (String) goodsMap.get("type_name");//商品二级分类名称
			Long goodsId = (Long) goodsMap.get("goods_id");//商品id
			String goodsNo = (String) goodsMap.get("goods_no");//商品货号
			String goodsName = (String) goodsMap.get("goods_name");//商品名称
			String barcode = (String) goodsMap.get("barcode");//条码
			String commbarcode = (String) goodsMap.get("commbarcode");//标准条码	
			Integer boxToUnit = (Integer) goodsMap.get("box_to_unit");//一箱多少件
			Integer torrToUnit = (Integer) goodsMap.get("torr_to_unit");//一托多少件
			String groupCommbarcode = (String) goodsMap.get("group_commbarcode");//组码
			String superiorParentBrand = (String) goodsMap.get("superiorParentBrand");
			
	
			Map<String, Object> summaryMap = saveSummaryMap.get(summaryKey);
			/**2.期初数据*/
			BigDecimal price = (BigDecimal) summaryMap.get("tzPrice");// 期初单据
			Integer initNum=(Integer) summaryMap.get("endNum");// 期初数量
			BigDecimal initAmount= (BigDecimal) summaryMap.get("endAmount");//期初金额			
			String merchandiseCurrency=(String) summaryMap.get("currency");//期初币种							
			if (price==null)price=new BigDecimal(0);if (initNum==null)initNum=0;
			if (initAmount==null)initAmount=new BigDecimal(0);if (StringUtils.isEmpty(merchandiseCurrency))merchandiseCurrency=null; 		
			/**3.本期采购入库数量=采购入库单入库数量-采购退货出库量*/
			Integer warehouseInNum=(Integer) summaryMap.get("warehouseInNum");//本期采购入库数量
			Integer purReturnOutNum=(Integer) summaryMap.get("purReturnOutNum");//采购退货出的量
			if (warehouseInNum==null)warehouseInNum=0;if (purReturnOutNum==null)purReturnOutNum=0;
			Integer warehouseNum = warehouseInNum-purReturnOutNum;//本期采购入库数量
			/**4.本期采购在途量*/			
			Integer purchaseNotshelfNum=(Integer) summaryMap.get("purchaseNotshelfNum");
			if (purchaseNotshelfNum==null)purchaseNotshelfNum=0;			
			/**5.本期减少采购在途量*/				
			Integer decreasePurchaseNotshelfNum=(Integer) summaryMap.get("decreasePurchaseNotshelfNum");
			if (decreasePurchaseNotshelfNum==null)decreasePurchaseNotshelfNum=0;			
			/**6.来货残损量=来货残损残次+来货短缺*/
			Integer purchaseLackNum=(Integer) summaryMap.get("purchaseLackNum");// 来货短缺
			Integer purchaseDamagedNum=(Integer) summaryMap.get("purchaseDamagedNum");// 来货残次	
			if (purchaseLackNum==null)purchaseLackNum=0;if (purchaseDamagedNum==null)purchaseDamagedNum=0;
			Integer inDamagedNum=purchaseLackNum+purchaseDamagedNum;
			/**7.本期移库入量*/
			Integer moveInNum=(Integer) summaryMap.get("moveInNum");// 本期移库入
			if (moveInNum==null)moveInNum=0;
			/**8.本期采购结转量（汇总）：调整取值逻辑，取数=本期采购入库数量+本期移库入量*/
			Integer purchaseEndNum=warehouseNum+moveInNum;	
			/**9.本期单价*/			
			Map<String, Object> thisMonthPriceMap=new HashMap<String, Object>();
			BigDecimal tZprice = null;
			String currency = "";	
    		BigDecimal sdWeightedPrice=null;//sd本期加权单价   		
    		BigDecimal sdInterestPrice=null;// 本期利息单价
    		
			//String adjustPriceResult=null;//调价原因
		    if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {
				thisMonthPriceMap.put("merchantId", merchant.getId());
				thisMonthPriceMap.put("depotId", Long.valueOf(goodsIdStr));
				thisMonthPriceMap.put("month", month);//本月
				thisMonthPriceMap.put("barcode", barcode);
				thisMonthPriceMap.put("buId", buModel.getBuId());
		    	SettlementPriceRecordModel monthPrice = settlementPriceRecordDao.getBuBarcodePrice(thisMonthPriceMap);				
			    if(monthPrice!=null) {
			    	tZprice = monthPrice.getPrice();
			    	currency = monthPrice.getCurrency();
			    	//adjustPriceResult=monthPrice.getAdjustPriceResult();
			    }	
			}else if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {
				String  buIdCommbarcode= buModel.getBuId()+","+commbarcode;
				
				Map<String, Object>priceMap = weightedPriceMap.get(buIdCommbarcode);
				if (priceMap==null)priceMap=new HashMap<>();						
    			tZprice = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");    			
				String deBuGoodsIdkey=Long.valueOf(goodsIdStr)+","+buModel.getBuId()+","+goodsId;
				currency = currencyMap.get(deBuGoodsIdkey);
				if (StringUtils.isEmpty(currency))currency=merchant.getAccountCurrency();
			}		    
		    /**10.本期销售已上架量=销售订单已上架正常品量+电商出库量+账单出库数量（库存调整类型为调减的量）- 电商订单退货入库量-销售退货单据的退入库量-账单入库数量 +库位调整单调减-库位调整单调增*/
		    Integer saleShelfNumXS = (Integer) summaryMap.get("saleShelfNumXS");
		    Integer saleShelfNumDS = (Integer) summaryMap.get("saleShelfNumDS");
		    Integer saleShelfNumZDC = (Integer) summaryMap.get("saleShelfNumZDC");
		    Integer saleShelfNumDST = (Integer) summaryMap.get("saleShelfNumDST");
		    Integer saleShelfNumXST = (Integer) summaryMap.get("saleShelfNumXST");
		    Integer saleShelfNumZDR = (Integer) summaryMap.get("saleShelfNumZDR");
		    Integer saleShelfNumKWTZDZ = (Integer) summaryMap.get("saleShelfNumKWTZDZ");
		    Integer saleShelfNumKWTZDJ = (Integer) summaryMap.get("saleShelfNumKWTZDJ");

		    if (saleShelfNumXS==null)saleShelfNumXS=0;if (saleShelfNumDS==null)saleShelfNumDS=0;
		    if (saleShelfNumZDC==null)saleShelfNumZDC=0;if (saleShelfNumDST==null)saleShelfNumDST=0;
		    if (saleShelfNumXST==null)saleShelfNumXST=0;if (saleShelfNumZDR==null)saleShelfNumZDR=0;
		    if (saleShelfNumKWTZDZ==null)saleShelfNumKWTZDZ=0;if (saleShelfNumKWTZDJ==null)saleShelfNumKWTZDJ=0;
		    Integer saleShelfNum=saleShelfNumXS+saleShelfNumDS+saleShelfNumZDC-saleShelfNumDST-saleShelfNumXST-saleShelfNumZDR+saleShelfNumKWTZDJ-saleShelfNumKWTZDZ;
		    /**11.出库残损量*/		    
		    Integer outDamagedNum = (Integer) summaryMap.get("saleDamagedNum");
		    if (outDamagedNum==null)outDamagedNum=0;
		    /**12.本期移库出量*/	
		    Integer moveOutNum = (Integer) summaryMap.get("moveOutNum");
		    if (moveOutNum==null)moveOutNum=0;		    
		    /**13.本期销售结转量（汇总)==本期销售已上架数量+出库残损+本期移库出量*/
		    Integer saleEndNum=saleShelfNum+outDamagedNum+moveOutNum;		    
		    /**14.本期销毁量*/
		    Integer destroyNum = (Integer) summaryMap.get("destroyNum");
		    if (destroyNum==null)destroyNum=0;
		    /**15.盘盈数量*/
		    Integer profitNum = (Integer) summaryMap.get("profitNum");
		    if (profitNum==null)profitNum=0;
		    /**16.盘亏数量*/
		    Integer lossNum = (Integer) summaryMap.get("lossNum");
		    if (lossNum==null)lossNum=0;
		    /**17.本期损益结转量（汇总)=盘盈数量+本月销毁数量（负值）+盘亏数量（负值）*/
		    Integer lossOverflowNum=profitNum-destroyNum-lossNum;	
		    /**18.期末结转数量= 期初数量+本期采购结转数量(汇总)-本期销售结转数量(汇总)+本期损益结转数量(汇总)*/		   
		    Integer endNum=initNum+purchaseEndNum-saleEndNum+lossOverflowNum;
		    /**19.累计采购在途量*/
		    Integer addPurchaseNotshelfNum = (Integer) summaryMap.get("addPurchaseNotshelfNum");
		    if (addPurchaseNotshelfNum==null)addPurchaseNotshelfNum=0;
		    /**20.累计采购在途量金额*/
		    BigDecimal addPurchaseNotshelfAmount = (BigDecimal) summaryMap.get("addPurchaseNotshelfAmount");
		    if (addPurchaseNotshelfAmount==null)addPurchaseNotshelfAmount=new BigDecimal(0);
		    /**21.累计销售在途量*/
		    Integer addSaleNoshelfNum = (Integer) summaryMap.get("addSaleNoshelfNum");
		    if (addSaleNoshelfNum==null)addSaleNoshelfNum=0;
		    /**22.累计调拨在途量*/
		    Integer addTransferNoshelfNum = (Integer) summaryMap.get("addTransferNoshelfNum");
		    if (addTransferNoshelfNum==null)addTransferNoshelfNum=0;
		    /**23.各仓库存*/
		    
		    Integer surplusNum = (Integer) summaryMap.get("surplusNum");// 事业部库存余量
			if(surplusNum==null) surplusNum = 0;
			
			/**--------------------sd /lp 开始----------------------*/
			/**25本期采购SD金额 =采购入库-采购退货+本期SD调整金额  取值“采购订单+商品货号”的维度取采购订单中各项SD类型的 SD单价（本币）*入库数量 得出各项SD类型的金额*/
			BigDecimal sdTgtWarehouseInAmount = (BigDecimal) summaryMap.get("sdTgtWarehouseInAmount");
			BigDecimal sdTgtPurReturnOutAmount = (BigDecimal) summaryMap.get("sdTgtPurReturnOutAmount");
			BigDecimal purchaseTzSdOrdeAmount = (BigDecimal) summaryMap.get("purchaseTzSdOrdeAmount");
			if (sdTgtWarehouseInAmount==null)sdTgtWarehouseInAmount=new BigDecimal(0);
			if (sdTgtPurReturnOutAmount==null)sdTgtPurReturnOutAmount=new BigDecimal(0);
			if (purchaseTzSdOrdeAmount==null)purchaseTzSdOrdeAmount=new BigDecimal(0);
			BigDecimal sdWarehouseAmount = sdTgtWarehouseInAmount.subtract(sdTgtPurReturnOutAmount).add(purchaseTzSdOrdeAmount);//本期采购LP金额
			/**26 SD/LP期初单价和金额*/
			BigDecimal sdInitPrice= (BigDecimal) summaryMap.get("sdTzPrice");//期初SD单价
			BigDecimal sdInitAmount= (BigDecimal) summaryMap.get("sdEndAmount");//期初SD金额
			if (sdInitPrice==null)sdInitPrice=new BigDecimal(0);
			if (sdInitAmount==null)sdInitAmount=new BigDecimal(0);	
			/**27累计采购在途SD金额 取值以“采购订单+商品货号”的维度取采购订单中各项SD类型的 SD单价（本币）* 采购在途数量 得出各项SD类型的在途金额*/
			BigDecimal sdAddPurchaseNotshelfAmount = (BigDecimal) summaryMap.get("sdTgtaddPurchaseNotshelfAmount");

			if (sdAddPurchaseNotshelfAmount==null)sdAddPurchaseNotshelfAmount=new BigDecimal(0);
			/**28累计采购在途LP金额 取值 累计采购在途明细表中对应该字段“本期采购LP金额（本币）”取值于采购SD单据中对应”采购订单+商品货号“的采购单价（本币）* 采购在途数量。*/
			/**29 SD/LP金额信息*/			
			BigDecimal sdEndAmount=null;/**期末SD金额=本期SD单价*期末结转数量*/
			BigDecimal sdLossOverflowAmount=null;/**本期损益结转SD金额汇总*/
			BigDecimal sdSaleEndAmount=null;/**本期销售SD金额=期初SD金额+本期采购SD金额+本期损益SD金额-期末SD金额*/		
			if (sdWeightedPrice!=null) {
				sdEndAmount=sdWeightedPrice.multiply(new BigDecimal(endNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdLossOverflowAmount=sdWeightedPrice.multiply(new BigDecimal(lossOverflowNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdSaleEndAmount=sdInitAmount.add(sdWarehouseAmount).add(sdLossOverflowAmount).subtract(sdEndAmount);
			}
			/**--------------------sd 结束----------------------*/
			
			/**--------------------利息 开始----------------------*/
			
			
			/**本期采购结转利息SD金额=本期采购入库利息SD金额+本期SD调整金额（其中采购入库SD金额为正数、SD调整单金额有正有负跟着单据取）；*/
			BigDecimal sdInterestInitAmount = (BigDecimal) summaryMap.get("sdInterestEndAmount");// 利息期初金额 (上月期末)		
			BigDecimal sdInterestInitPrice = (BigDecimal) summaryMap.get("sdInterestTzPrice");// 利息期初单价 (上月调整单价)		
			
			
			
			BigDecimal sdInterestTgtWarehouseInAmount = (BigDecimal) summaryMap.get("sdInterestTgtWarehouseInAmount");		
			BigDecimal sdInterestTgtPurReturnOutAmount = (BigDecimal) summaryMap.get("sdInterestTgtPurReturnOutAmount");		
			BigDecimal purchaseTzSdOrdeInterestAmount = (BigDecimal) summaryMap.get("purchaseTzSdOrdeInterestAmount");		

			if (sdInterestInitAmount==null)sdInterestInitAmount=new BigDecimal(0);
			if (sdInterestInitPrice==null)sdInterestInitPrice=new BigDecimal(0);
			if (sdInterestTgtWarehouseInAmount==null)sdInterestTgtWarehouseInAmount=new BigDecimal(0);
			if (sdInterestTgtPurReturnOutAmount==null)sdInterestTgtPurReturnOutAmount=new BigDecimal(0);
			if (purchaseTzSdOrdeInterestAmount==null)purchaseTzSdOrdeInterestAmount=new BigDecimal(0);
			
			// 本期利息采购金额
			BigDecimal sdInterestPurchaseEndAmount = sdInterestTgtWarehouseInAmount.subtract(sdInterestTgtPurReturnOutAmount).add(purchaseTzSdOrdeInterestAmount);

			BigDecimal sdInterestEndAmount=null;/**期末利息金额=利息单价*期末结转数量*/
			BigDecimal sdInterestLossOverflowAmount=null;/**本期损益结转利息金额汇总*/
			BigDecimal sdInterestSaleEndAmount=null;/**本期销售利息金额=期初利息金额+本期采购利息金额+本期损益利息金额-期末利息金额*/		
			if (sdInterestPrice!=null) {
				sdInterestEndAmount=sdInterestPrice.multiply(new BigDecimal(endNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestLossOverflowAmount=sdInterestPrice.multiply(new BigDecimal(lossOverflowNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestSaleEndAmount=sdInterestInitAmount.add(sdInterestPurchaseEndAmount).add(sdInterestLossOverflowAmount).subtract(sdInterestEndAmount);
			}
			if (sdInterestEndAmount==null)sdInterestEndAmount=new BigDecimal(0);
			if (sdInterestLossOverflowAmount==null)sdInterestLossOverflowAmount=new BigDecimal(0);
			if (sdInterestSaleEndAmount==null)sdInterestSaleEndAmount=new BigDecimal(0);
			if (sdInterestPrice==null) sdInterestPrice=new BigDecimal(0);

			/**--------------------利息 结束----------------------*/
			
			
			
		    BigDecimal lossOverflowAmount=null;/** 本期损益结转金额(汇总)*/	    	
		    BigDecimal endAmount=null;/** 期末结转金额*/
		    BigDecimal purchaseEndAmount=null;/*采购结转金额*/
		    if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {
		    	String deBuKey=Long.valueOf(goodsIdStr)+","+buModel.getBuId()+","+goodsId;
		    	Map<String, Object> deBuMap = deBuLastSummaryMap.get(deBuSummaryKey);
		    	BigDecimal commbarcodePrice=null;
		    	if (deBuMap!=null) {
		    		commbarcodePrice = (BigDecimal) deBuMap.get("tzPrice");
				}
		    	if (commbarcodePrice==null) {
		    		String buKey=buModel.getBuId()+","+goodsId;
		    		Map<String, Object> buMap = buLastSummaryMap.get(buKey);
		    		if (buMap!=null) {
		    			commbarcodePrice = (BigDecimal) buMap.get("tzPrice");
					}
				}
		    	if (commbarcodePrice==null) {
		    		String barcodeKey=buModel.getBuId()+","+barcode;
		    		Map<String, Object> priceMap = settlementPriceMap.get(barcodeKey);
		    		if (priceMap!=null) {
		    			commbarcodePrice = (BigDecimal)priceMap.get("tzPrice");		    			
					}
				}    	
		    	if (commbarcodePrice!=null) {
		    		lossOverflowAmount=commbarcodePrice.multiply(new BigDecimal(lossOverflowNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
		    		endAmount=commbarcodePrice.multiply(new BigDecimal(endNum)).setScale(2,BigDecimal.ROUND_HALF_UP);			    		
		    	}
	    		purchaseEndAmount=commbarcodePrice.multiply(new BigDecimal(purchaseEndNum)).setScale(2,BigDecimal.ROUND_HALF_UP);	

	
			}else if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {
				String weightedKey=buModel.getBuId()+","+commbarcode;				
				Map<String, Object>priceMap = weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();						
				BigDecimal weightedPrice = (BigDecimal) priceMap.get("weightedPrice");  
				if (weightedPrice!=null) {
					logger.info("summaryMap:"+summaryMap);
					BigDecimal decreasePurchaseNotshelfAmount = (BigDecimal) summaryMap.get("decreasePurchaseNotshelfAmount");									
					if (decreasePurchaseNotshelfAmount==null)decreasePurchaseNotshelfAmount=new BigDecimal(0);										
		    		lossOverflowAmount=weightedPrice.multiply(new BigDecimal(lossOverflowNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
		    		endAmount=weightedPrice.multiply(new BigDecimal(endNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				}
				
				BigDecimal moveInAmount = (BigDecimal) summaryMap.get("moveInAmount");	
			    if (moveInAmount==null)moveInAmount=new BigDecimal(0);
			    BigDecimal warehouseInAmount = (BigDecimal) summaryMap.get("warehouseInAmount");			    
			    if (warehouseInAmount==null)warehouseInAmount=new BigDecimal(0);
			    BigDecimal purReturnOutAmount = (BigDecimal) summaryMap.get("purReturnOutAmount");
			    if (purReturnOutAmount==null) purReturnOutAmount= new BigDecimal(0);
			    //本期采购结转数量(汇总)= 本期入库金额
			     purchaseEndAmount=warehouseInAmount.add(moveInAmount).subtract(purReturnOutAmount);
			}
		    
		    /**
		     * 本期销售结转金额=期初金额+本期采购结转金额+本期损益结转金额-期末结转金额；
		     */
		    if (purchaseEndAmount==null) purchaseEndAmount=new BigDecimal(0);
		    if (lossOverflowAmount==null) lossOverflowAmount=new BigDecimal(0);
		    if (endAmount==null) endAmount=new BigDecimal(0);
		    BigDecimal saleEndAmount=initAmount.add(purchaseEndAmount).add(lossOverflowAmount).subtract(endAmount).setScale(2,BigDecimal.ROUND_HALF_UP);;

		    if (sdSaleEndAmount==null) sdSaleEndAmount=new BigDecimal(0);
		    if (sdLossOverflowAmount==null) sdLossOverflowAmount=new BigDecimal(0);
		    if (sdEndAmount==null) sdEndAmount=new BigDecimal(0);
		    if (sdAddPurchaseNotshelfAmount==null) sdAddPurchaseNotshelfAmount=new BigDecimal(0);
	    
		    if(initNum.intValue()==0&&purchaseNotshelfNum.intValue()==0&&warehouseNum.intValue()==0 
					&&saleShelfNum.intValue()==0
					&&inDamagedNum.intValue()==0&&outDamagedNum.intValue()==0
					&&profitNum.intValue()==0&&endNum.intValue()==0&&purchaseEndNum.intValue()==0
					&&saleEndNum.intValue()==0&&destroyNum.intValue()==0
					&&lossOverflowNum.intValue()==0&&addPurchaseNotshelfNum.intValue()==0
					&&addSaleNoshelfNum.intValue()==0&&lossNum.intValue()==0&&surplusNum.intValue()==0
					&&addTransferNoshelfNum.intValue()==0&&moveInNum.intValue()==0
					&&moveOutNum.intValue()==0
					&&purchaseTzSdOrdeAmount.compareTo(BigDecimal.ZERO)==0
					&&purchaseEndAmount.compareTo(BigDecimal.ZERO)==0
					&&saleEndAmount.compareTo(BigDecimal.ZERO)==0
					&&lossOverflowAmount.compareTo(BigDecimal.ZERO)==0
					&&addPurchaseNotshelfAmount.compareTo(BigDecimal.ZERO)==0
					&&sdInitAmount.compareTo(BigDecimal.ZERO)==0
					&&sdWarehouseAmount.compareTo(BigDecimal.ZERO)==0
					&&sdSaleEndAmount.compareTo(BigDecimal.ZERO)==0
					&&sdLossOverflowAmount.compareTo(BigDecimal.ZERO)==0
					&&sdEndAmount.compareTo(BigDecimal.ZERO)==0
					&&sdAddPurchaseNotshelfAmount.compareTo(BigDecimal.ZERO)==0					
					&&sdInterestInitAmount.compareTo(BigDecimal.ZERO)==0
					&&sdInterestPurchaseEndAmount.compareTo(BigDecimal.ZERO)==0
					&&sdInterestPrice.compareTo(BigDecimal.ZERO)==0
					&&sdInterestSaleEndAmount.compareTo(BigDecimal.ZERO)==0
					&&sdInterestLossOverflowAmount.compareTo(BigDecimal.ZERO)==0
					&&sdInterestEndAmount.compareTo(BigDecimal.ZERO)==0
		    		){		    	
				logger.info("商家:"+merchant.getName()+"仓库:"+depotIdStr+" 货号:"+goodsMap.get("goods_no")+"当前数据都为0,故不存");
			}else {
				// 保存报表数据
				BuFinanceInventorySummaryModel model = new BuFinanceInventorySummaryModel();
				model.setMerchantId(merchant.getId());
				model.setMerchantName(merchant.getName());
				DepotInfoModel depot = depotModelMap.get(Long.valueOf(depotIdStr));
				model.setDepotId(depot.getId());
				model.setDepotName(depot.getName());
				model.setMonth(month);
				model.setBrandId(brandId);
				model.setBrandName(brandName);
				model.setTypeId(typeId);
				model.setTypeName(typeName);
				model.setGoodsId(goodsId);
				model.setGoodsNo(goodsNo);
				model.setGoodsName(goodsName);
				model.setBarcode(barcode);
				model.setInitNum(initNum);//期初数量
				model.setInitAmount(initAmount);//期初金额
				model.setPrice(price);//标准单价
				model.setPurchaseNotshelfNum(purchaseNotshelfNum);//本期采购未上架
				//model.setPurchaseNotshelfAmount(purNotshelfAmount);//本期采购未上架金额
				model.setWarehouseNum(warehouseNum);//本期采购入库数量
				//model.setWarehouseAmount(warehouseAmont);//本期采购入库金额
				model.setSaleShelfNum(saleShelfNum);//本期销售已上架数量
				//model.setSaleShelfAmount(saleShelfAmount);//本期销售已上架金额
				//model.setSaleNoshelfNum(saleNoShelfNum);//本期销售未上架数量
				///model.setSaleNoshelfAmount(saleNoShelfAmount);//本期销售未上架金额
				model.setInDamagedNum(inDamagedNum);//本期来货残损
				//model.setInDamagedAmount(inDamagedAmount);//本期来货残损金额
				model.setOutDamagedNum(outDamagedNum);//本期出库残损数量
				//model.setOutDamagedAmount(outDamagedAmount);//本期出库残损金额
				model.setProfitNum(profitNum);//本期盘盈数量
				//model.setProfitAmount(profitAmount);//本期盘盈金额			
				model.setEndNum(endNum);//本期期末结存数量
				model.setEndAmount(endAmount);//本期期末结存金额
				model.setTzPrice(tZprice);//本期调整单价
				model.setCurrency(currency);//调整标准单价币种
				//model.setTzEndAmount(tZendAmount);//本期调整期末结存金额
				//model.setDifferenceAmount(differenceAmount);//差异金额
				model.setCreateDate(TimeUtils.getNow());
				model.setModifyDate(TimeUtils.getNow());			
				model.setCommbarcode(commbarcode);//标准条码
				model.setSuperiorParentBrand(superiorParentBrand);
				model.setPurchaseEndNum(purchaseEndNum);//本期采购结转数量汇总
				model.setPurchaseEndAmount(purchaseEndAmount);//本期采购结转金额汇总
				model.setSaleEndNum(saleEndNum);//本期销售结转数量汇总
				model.setSaleEndAmount(saleEndAmount);//本期销售结转金额汇总
				model.setDestroyNum(destroyNum);//本月销毁数量
				//model.setDestroyAmount(destroyAmount);//本月销毁金额
				model.setLossOverflowNum(lossOverflowNum);//本期损益结转数量汇总
				model.setLossOverflowAmount(lossOverflowAmount);//本期损益结转金额汇总
				model.setAddPurchaseNotshelfNum(addPurchaseNotshelfNum);//累计采购在途量
				model.setAddPurchaseNotshelfAmount(addPurchaseNotshelfAmount);//累计采购在途金额
				model.setAddSaleNoshelfNum(addSaleNoshelfNum);//累计销售在途量
				//model.setAddSaleNoshelfAmount(addSaleNoshelfAmount);//累计销售在途金额			
				model.setLossNum(lossNum);//本期盘亏数量
				//model.setLossAmount(lossAmount);//本期盘亏金额
				model.setDecreasePurchaseNotshelfNum(decreasePurchaseNotshelfNum);// 本期减少量
				//model.setDecreasePurchaseNotshelfAmount(decreasePurchaseNotshelfAmount);//  本期减少金额
				model.setGroupCommbarcode(groupCommbarcode);
				model.setSurplusNum(surplusNum);
				model.setAddTransferNoshelfNum(addTransferNoshelfNum);
				//model.setAddTransferNoshelfAmount(addTransferNoshelfAmount);
				model.setBuId(buModel.getBuId());
				model.setBuCode(buModel.getBuCode());
				model.setBuName(buModel.getBuName());
				model.setMoveInNum(moveInNum);
				model.setMoveOutNum(moveOutNum);
				model.setAccountPrice(orderSource);
				//model.setMoveNum(moveNum);	
				model.setSdInitAmount(sdInitAmount);
				model.setSdPrice(sdInitPrice);// 期初sd单价
				model.setSdWarehouseAmount(sdWarehouseAmount);
				model.setSdTzPrice(sdWeightedPrice);
				model.setSdSaleEndAmount(sdSaleEndAmount);
				model.setSdLossOverflowAmount(sdLossOverflowAmount);
				model.setSdEndAmount(sdEndAmount);
				model.setSdAddPurchaseNotshelfAmount(sdAddPurchaseNotshelfAmount);	
				model.setSdInterestInitAmount(sdInterestInitAmount);
				model.setSdInterestPrice(sdInterestInitPrice);
				model.setSdInterestPurchaseEndAmount(sdInterestPurchaseEndAmount);
				model.setSdInterestTzPrice(sdInterestPrice);
				model.setSdInterestSaleEndAmount(sdInterestSaleEndAmount);
				model.setSdInterestLossOverflowAmount(sdInterestLossOverflowAmount);
				model.setSdInterestEndAmount(sdInterestEndAmount);
				
				
				buFinanceInventorySummaryDao.save(model);

			}
		    
			
		}
		logger.info(merchant.getName()+",月份"+month+"saveSummaryMap:"+saveSummaryMap);

		// --------------------事业部财务经销存总表结束-----------------------------
		
	}

	/**
	 * 获取标准成本单价
	 * @return
	 */
	public void getCommbarcodePrice(MerchantBuRelModel buModel,Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,String month,List<Map<String,Object>> allMerchandiseList,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap)throws Exception {
		

		BuFinanceInventorySummaryModel buSummaryModel=new BuFinanceInventorySummaryModel();
		buSummaryModel.setMerchantId(merchant.getId());
		buSummaryModel.setMonth(TimeUtils.getUpMonth(month));//上一个月
		buSummaryModel.setBuId(buModel.getBuId());
		List<BuFinanceInventorySummaryModel> buLastSummaryList = buFinanceInventorySummaryDao.list(buSummaryModel);
		
		for (BuFinanceInventorySummaryModel model : buLastSummaryList) {
			String deBuKey=model.getDepotId()+","+model.getBuId()+","+model.getGoodsId();
			Map<String, Object> deBuMap=new HashMap<>();
			deBuMap.put("tzPrice", model.getTzPrice());//本期单价
			deBuMap.put("endAmount", model.getEndAmount());//期末金额
			deBuMap.put("endNum", model.getEndNum());//期末数量
			deBuMap.put("currency", model.getCurrency());//币种
			//存储仓库事业部单价,币种,数量 金额
			deBuLastSummaryMap.put(deBuKey, deBuMap);
			saveSummaryMap.put(deBuKey, deBuMap);// 存储总表
			// 存储所有 事业部 单价和币种
			String buKey=model.getBuId()+","+model.getGoodsId();
			if (!buLastSummaryMap.containsKey(buKey)) {
				Map<String, Object> buMap=new HashMap<>();
				buMap.put("tzPrice", model.getTzPrice());//本期单价
				buMap.put("currency", model.getCurrency());//币种
				buLastSummaryMap.put(buKey, buMap);
			}
		}	
		
		// 查询表中成本单价表头
		SettlementPriceModel  settlementPrice=new SettlementPriceModel();
		settlementPrice.setMerchantId(merchant.getId());
		settlementPrice.setBuId(buModel.getBuId());
		List<SettlementPriceModel> settlementPriceList = settlementPriceDao.list(settlementPrice);
		for (SettlementPriceModel model : settlementPriceList) {
			Map<String, Object> thisMonthPriceMap=new HashMap<String, Object>();
			thisMonthPriceMap.put("merchantId", merchant.getId());
			thisMonthPriceMap.put("month", month);//本月
			thisMonthPriceMap.put("barcode", model.getBarcode());
			thisMonthPriceMap.put("buId", model.getBuId());
			SettlementPriceRecordModel lastMonthPrice = settlementPriceRecordDao.getBuBarcodePrice(thisMonthPriceMap);
		    if(lastMonthPrice!=null) {
		    	BigDecimal price = lastMonthPrice.getPrice();
		    	String merchandiseCurrency=lastMonthPrice.getCurrency();
		    	Map<String, Object> priceMap=new HashMap<>();
		    	priceMap.put("tzPrice", price);
		    	priceMap.put("currency", merchandiseCurrency);
		    	if (!settlementPriceMap.containsKey(model.getBarcode())) {
		    		String barcodeKey=model.getBuId()+","+model.getBarcode();
		    		settlementPriceMap.put(barcodeKey, priceMap);
				}		    	
		    }
		}

	}
	
	/**
	 * 获取仓库idList
	 * @param merchant
	 * @param month
	 * @return
	 */
	/*
	 * public List<Long> getDepotIdList(List<DepotInfoModel> depotList) {
	 * List<Long>depotIdList=new ArrayList<>(); for (DepotInfoModel depot :
	 * depotList) { depotIdList.add(depot.getId()); } return depotIdList; }
	 */
	/**
	 * 获取加权单价
	 * @return
	 */
	public void getWeightedPrice(MerchantBuRelModel buModel,Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,String month,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String,  Map<String, Object>>weightedSummaryMap,Map<String, String>currencyMap)throws Exception {
		//报表期初金额和数量 : 公司+事业部+标准条码  为维度取事业部财务进销存上月期末金额和数量
		BuFinanceInventorySummaryModel buSummary =new BuFinanceInventorySummaryModel();
		buSummary.setMerchantId(merchant.getId());
		buSummary.setMonth(TimeUtils.getUpMonth(month));
		buSummary.setBuId(buModel.getBuId());
		List<BuFinanceInventorySummaryModel> buSummaryList=buFinanceInventorySummaryDao.getSummaryList(buSummary);
		for (BuFinanceInventorySummaryModel model : buSummaryList) {
			Long depotId = model.getDepotId();
			Long buId = model.getBuId();
			Long goodsId = model.getGoodsId();
			String commbarcode = model.getCommbarcode();
			Integer endNum = model.getEndNum(); 
			BigDecimal endAmount = model.getEndAmount();
			BigDecimal price = model.getPrice();
			String currency = model.getCurrency();
			if (endAmount==null) endAmount=new BigDecimal(0);
			if (endNum==null) endNum=0;
			BigDecimal sdEndAmount = model.getSdEndAmount();
			if (sdEndAmount==null)sdEndAmount=new BigDecimal(0);
			BigDecimal sdInterestEndAmount = model.getSdInterestEndAmount();
			if (sdInterestEndAmount==null) sdInterestEndAmount=new BigDecimal(0);
			
			String deBuKey=depotId+","+buId+","+goodsId;
			Map<String, Object> deBuMap=new HashMap<>();
			deBuMap.put("tzPrice", model.getTzPrice());//本期单价
			deBuMap.put("endAmount", model.getEndAmount());//期末金额
			deBuMap.put("endNum", model.getEndNum());//期末数量
			deBuMap.put("sdTzPrice", model.getSdTzPrice());//期末SD单价
			deBuMap.put("sdEndAmount", sdEndAmount);//期末SD金额			
			deBuMap.put("sdInterestTzPrice", model.getSdInterestTzPrice());//期末sd利息单价
			deBuMap.put("sdInterestEndAmount", sdInterestEndAmount);//期末sd利息金额
			
			//存储期初
			if (!deBuLastSummaryMap.containsKey(deBuKey)) {
				deBuLastSummaryMap.put(deBuKey, deBuMap);
			}
			saveSummaryMap.put(deBuKey, deBuMap);
			//存储计算加权期初金额和期初数量			
			String weightedKey=buId+","+commbarcode;
			if (weightedSummaryMap.containsKey(weightedKey)) {
				Map<String, Object>weightedMap =weightedSummaryMap.get(weightedKey);
				BigDecimal endAmountMap = (BigDecimal) weightedMap.get("endAmount");
				Integer endNumMap = (Integer) weightedMap.get("endNum");
				BigDecimal sdEndAmountMap = (BigDecimal) weightedMap.get("sdEndAmount");
				BigDecimal sdInterestEndAmountMap = (BigDecimal) weightedMap.get("sdInterestEndAmount");
				if (endAmountMap==null) endAmountMap=new BigDecimal(0);
				if (endNumMap==null) endNumMap=0;
				if (sdEndAmountMap==null)sdEndAmountMap=new BigDecimal(0);
				if (sdInterestEndAmountMap==null)sdInterestEndAmountMap=new BigDecimal(0);
				BigDecimal endAmountAdd = endAmount.add(endAmountMap);
				BigDecimal sdSaleEndAmountAdd = sdEndAmount.add(sdEndAmountMap);
				BigDecimal sdInterestEndAmountAdd = sdInterestEndAmount.add(sdInterestEndAmountMap);
				weightedMap.put("endAmount",endAmountAdd);//期末金额
				weightedMap.put("endNum", endNum+endNumMap);//期末数量
				weightedMap.put("currency", currency);//期末数量
				weightedMap.put("sdEndAmount", sdSaleEndAmountAdd);//sd期末金额
				weightedMap.put("sdInterestEndAmount", sdInterestEndAmountAdd);//期末sd利息金额	
				weightedSummaryMap.put(weightedKey, weightedMap);
			}else {
				Map<String, Object>weightedMap=new HashMap<>();
				weightedMap.put("endAmount", endAmount);//期末金额
				weightedMap.put("endNum", endNum);//期末数量
				weightedMap.put("currency", currency);//币种
				weightedMap.put("sdEndAmount", sdEndAmount);//期末SD期末金额	
				weightedMap.put("sdInterestEndAmount", sdInterestEndAmount);//期末sd利息金额	
				weightedSummaryMap.put(weightedKey, weightedMap);
			}	
			currencyMap.put(deBuKey, currency);
		}

	}
	  // 获取标准品牌
    public Map<Long, BrandParentMongo> getBrandParent(){
    	Map<Long, BrandParentMongo> brandParentMap=new HashMap<>();   	
    	Map<String, Object> params = new HashMap<>();
    	List<BrandParentMongo> findAll = brandParentMongoDao.findAll(params);
    	for (BrandParentMongo brandParentMongo : findAll) {
    		brandParentMap.put(brandParentMongo.getBrandParentId(), brandParentMongo);
		}
        return brandParentMap;
    }

	/**
	 * 获取所有的商品id对应的 商品信息
	 * @return
	 */
	public Map<Long, Map<String, Object>> getAllMerchandiseMap(List<Map<String,Object>> allMerchandiseList,Map<String, Object> groupCommbarcodeMap,
			Map<Long, BrandParentMongo> brandParentMap) {
		Map<Long, Map<String, Object>> allGoodsMap=new HashMap<>();
		for (Map<String, Object> goodsMap : allMerchandiseList) {
			String commbarcode = (String) goodsMap.get("commbarcode");//标准条码	
			String groupCommbarcode=commbarcode;
			Long goodsId = (Long) goodsMap.get("goods_id");//商品id
			Long brandId = (Long) goodsMap.get("brand_id");// 品牌id
			// 如果组码为空 组码为标准条码
			if (!StringUtils.isEmpty(commbarcode)&&!StringUtils.isEmpty((String) groupCommbarcodeMap.get(commbarcode))) {			
				groupCommbarcode=(String) groupCommbarcodeMap.get(commbarcode);
			}
			BrandParentMongo brandParentMongo=null;
			if (brandId!=null) {
				brandParentMongo = brandParentMap.get(brandId);
			}
			String superiorParentBrand="";
			String superiorParentBrandCode="";
			Long superiorParentBrandId =null;
			if (brandParentMongo!=null) {
				superiorParentBrand=brandParentMongo.getSuperiorParentBrand();
				superiorParentBrandCode = brandParentMongo.getSuperiorParentBrandCode();
				superiorParentBrandId = brandParentMongo.getSuperiorParentBrandId();
			}
			goodsMap.put("group_commbarcode", groupCommbarcode);
			goodsMap.put("superiorParentBrand", superiorParentBrand);
			goodsMap.put("superiorParentBrandCode", superiorParentBrandCode);
			goodsMap.put("superiorParentBrandId", superiorParentBrandId);
			allGoodsMap.put(goodsId, goodsMap);
		}
		
		return allGoodsMap;
	}
	/**
	 * 生成加权单价明细
	 * @param merchant
	 * @param depotIdList
	 * @param month
	 * @param accountPrice
	 * @param orderSource 1 来源标准成本单据 2.来源 加权单价
	 * @throws Exception 
	 */
	private void saveDetails(MerchantBuRelModel buModel,Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant, 
			Map<Long,BusinessUnitModel> buModelMap,String month,
			Map<Long,DepotInfoModel> depotModelMap,
			Map<Long, Map<String, Object>> allMerchandiseMap,
			Map<String,  Map<String, Object>>weightedSummaryMap,
			Map<String, String>currencyMap,
			Map<String, Object> groupCommbarcodeMap,
			String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap) throws Exception {
		//本月采购总数量=本期采购结转数量(汇总)=本期采购入库数量+本期采购在途量-本期采购减少在途量+事业部移库入数量
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantId", merchant.getId());
		paramMap.put("month", month);
		paramMap.put("buId", buModel.getBuId());
    	//-------------------------------- 采购入库明细开始---------------------------
		Map<String, Object> delParamMap = new HashMap<String, Object>();
		delParamMap.put("merchantId", merchant.getId());	
		delParamMap.put("month", month);
		delParamMap.put("buId", buModel.getBuId());
		
		// 删除采购入库明细
		buFinanceWarehouseDetailsDao.delBuDepotMonthFinanceWarehouseDetails(delParamMap);
		// 删除采购SD入库明细
		buFinanceSdWarehouseDetailsDao.delBuFinanceSdWarehouseDetails(delParamMap);
		//采购入库
		List<Map<String, Object>> warehouseAndAnalysisMapList = purchaseWarehouseItemDao.getBuPurWarehouseAndAnalysisWeighte(paramMap);
		//采购退货
		List<Map<String, Object>> purchaseReturnOdepotMapList = purchaseReturnOdepotDao.getBuPurchaseReturnOdepot(paramMap);					
		//获取采购调整sd单
		List<Map<String, Object>> tzPurchaseSdOrdeMapList = purchaseSdOrderDao.getTzPurchaseSdOrde(paramMap);
		
		saveFinanceWarehouseDetailsWeighted(buModel,saveSummaryMap,merchant, month, warehouseAndAnalysisMapList, 
				purchaseReturnOdepotMapList,
				tzPurchaseSdOrdeMapList,
				depotModelMap, 
				allMerchandiseMap,weightedSummaryMap,currencyMap,groupCommbarcodeMap,orderSource,
				deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap);
				
    	//-------------------------------- 采购入库明细结结束---------------------------

    	
    	//-------------------------------- 累计采购在途明细表开始---------------------------
		logger.info("清除:"+merchant.getName()+","+month+"(事业部财务经销存)累计采购在途明细表");					
		buFinanceAddPurchaseNotshelfDetailsDao.delBuFinanceAddPurchaseNotshelfDetails(delParamMap);
		logger.info("清除:"+merchant.getName()+","+month+"(事业部财务经销存)累计SD采购在途明细表");
		buFinanceSdAddPurchaseNotshelfDao.delBuFinanceSdAddPurchaseNotshelf(delParamMap);
		List<Map<String, Object>> addPurchaseNotshelfDetailsList = purchaseOrderItemDao.getBuFinanceAddPurchaseNotshelfDetailsWeighte(paramMap);
		
    	saveFinanceAddPurchaseNotshelfDetailsWeighte(buModel,saveSummaryMap,merchant, month, addPurchaseNotshelfDetailsList, depotModelMap,
				allMerchandiseMap,weightedSummaryMap,currencyMap,groupCommbarcodeMap,orderSource,
				deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap);
    	//-------------------------------- 累计采购在途明细表结束---------------------------

 
		
		
		//-------------------------------- 本期事业部移库明细开始---------------------------
		
		 buFinanceMoveDetailsDao.delBuFinanceMoveDetails(delParamMap); 
		// 获取移库明细入
		int moveDetailsInCount = buMoveInventoryDao.getBuFinanceMoveInDetailsCountWeighted(paramMap);
		Double moveInCeil = Math.ceil(Integer.valueOf(moveDetailsInCount) * 0.001);// 循环次数
		int startMoveInIndex = 0;
		int pageMoveInSize = 1000;//每页1000
    	Map<String, Object> paramMoveInMap = new HashMap<String, Object>();
    	paramMoveInMap.put("merchantId", merchant.getId());
    	paramMoveInMap.put("buId", buModel.getBuId());
    	paramMoveInMap.put("month", month);
    	paramMoveInMap.put("pageSize", pageMoveInSize);
    	paramMoveInMap.put("tag", "1");	
		for (int i = 0; i < moveInCeil; i++) {
			paramMoveInMap.put("startIndex", startMoveInIndex);
			//获取(事业部财务经销存) 本期事业部移库明细
			List<Map<String, Object>> buFinanceMoveList = buMoveInventoryDao.getBuFinanceMoveDetailsWeighted(paramMoveInMap);
			saveBuFinanceMoveDetailsWeighted(buModel,saveSummaryMap,merchant, month, buFinanceMoveList, depotModelMap,buModelMap,
					allMerchandiseMap,weightedSummaryMap,currencyMap,groupCommbarcodeMap,orderSource,
					deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap);
			logger.info("生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)本期事业部移库明细(入)"+"第"+(i+1)+"次");
			startMoveInIndex=startMoveInIndex+pageMoveInSize;
		}  

		//-------------------------------- 本期事业部移库明细结束---------------------------
		
		//-------------------------------- 本期加权表数据开始---------------------------
		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {
			saveWeighteDetails(merchant,month,buModel,weightedSummaryMap,groupCommbarcodeMap,weightedPriceMap);
		}		
		//-------------------------------- 本期加权表数据结束---------------------------

	}
	


	/**
	 * 生成本期加权
	 * @throws SQLException 
	 */
	public void saveWeighteDetails(MerchantInfoModel merchant,String month,MerchantBuRelModel buModel,
			Map<String,  Map<String, Object>>weightedSummaryMap,Map<String, Object> groupCommbarcodeMap,
			Map<String, Map<String, Object>>weightedPriceMap) throws SQLException{
		Map<String, Object>parmCommbarMap=new HashMap<String, Object>();				
		parmCommbarMap.put("merchantId", merchant.getId());
		parmCommbarMap.put("month", month);
		parmCommbarMap.put("buId", buModel.getBuId());
		//获取加权单价不等于0的数据 （为了获取本期期初为null和本期结转也未null的数据也要生成加权）
		List<Map<String, Object>> lastWeightedPriceAll = weightedPriceDao.getLastWeightedPriceAll(parmCommbarMap);
		for (Map<String, Object> lastWeightedPriceMap : lastWeightedPriceAll) {
			String commbarcode = (String) lastWeightedPriceMap.get("commbarcode");
			Long buId = (Long) lastWeightedPriceMap.get("bu_id");
			String weightedKey=buId+","+commbarcode;
			if (!weightedSummaryMap.containsKey(weightedKey)) {
				weightedSummaryMap.put(weightedKey, lastWeightedPriceMap);
			}
		}

		
		// 计算加权单价
		Set<String> weightedKeySet = weightedSummaryMap.keySet();
		//用于
		Map<String, Map<String, Object>>groupCommbarcodeweightedMap=new HashMap<>();
		Long buId =buModel.getBuId();
		String buName = buModel.getBuName();
		
		
		
		
		// 组码计算加权		
		for (String weightedKey : weightedKeySet) {
			Map<String, Object> weightedMap = weightedSummaryMap.get(weightedKey);
			String[]keyStr = weightedKey.split(",");
			//Long buId = Long.valueOf(keyStr[0]);
			String commbarcode = keyStr[1];
			BigDecimal initAmountMap = (BigDecimal) weightedMap.get("endAmount");
			Integer initNumMap = (Integer) weightedMap.get("endNum");
			BigDecimal warehouseAmountMap = (BigDecimal) weightedMap.get("warehouseInAmount");
			Integer warehouseNumMap = (Integer) weightedMap.get("warehouseInNum");
			BigDecimal purReturnOutAmountMap = (BigDecimal) weightedMap.get("purReturnOutAmount");
			Integer purReturnOutNumMap = (Integer) weightedMap.get("purReturnOutNum");
			Integer purchaseTzSdOrdeNumMap = (Integer) weightedMap.get("purchaseTzSdOrdeNum");
			BigDecimal moveInAmountMap = (BigDecimal) weightedMap.get("moveInAmount");
			Integer moveInNumMap = (Integer) weightedMap.get("moveInNum");			
			BigDecimal sdIntAmountMap = (BigDecimal) weightedMap.get("sdEndAmount");
			BigDecimal sdTgtWarehouseInAmountMap = (BigDecimal) weightedMap.get("sdTgtWarehouseInAmount");
			BigDecimal sdTgtPurReturnOutAmountMap = (BigDecimal) weightedMap.get("sdTgtPurReturnOutAmount");
			BigDecimal purchaseTzSdOrdeAmountMap = (BigDecimal) weightedMap.get("purchaseTzSdOrdeAmount");
			
			BigDecimal sdInterestEndAmountMap = (BigDecimal) weightedMap.get("sdInterestEndAmount");			
			BigDecimal purchaseTzSdOrdeInterestAmountMap = (BigDecimal) weightedMap.get("purchaseTzSdOrdeInterestAmount");
			Integer purchaseTzSdOrdeInterestNumMap = (Integer) weightedMap.get("purchaseTzSdOrdeInterestNum");
			BigDecimal sdInterestTgtWarehouseInAmountMap = (BigDecimal) weightedMap.get("sdInterestTgtWarehouseInAmount");
			BigDecimal sdInterestTgtPurReturnOutAmountMap = (BigDecimal) weightedMap.get("sdInterestTgtPurReturnOutAmount");
						
			if (initAmountMap==null)initAmountMap=new BigDecimal(0);
			if (warehouseAmountMap==null)warehouseAmountMap=new BigDecimal(0);
			if (purReturnOutAmountMap==null)purReturnOutAmountMap=new BigDecimal(0);
			if (moveInAmountMap==null)moveInAmountMap=new BigDecimal(0);	
			if (initNumMap==null)initNumMap=0;
			if (warehouseNumMap==null)warehouseNumMap=0;
			if (purReturnOutNumMap==null)purReturnOutNumMap=0;
			if (purchaseTzSdOrdeNumMap==null)purchaseTzSdOrdeNumMap=0;
			if (moveInNumMap==null)moveInNumMap=0;	
			if (purchaseTzSdOrdeInterestNumMap==null)purchaseTzSdOrdeInterestNumMap=0;
			if (sdIntAmountMap==null)sdIntAmountMap=new BigDecimal(0);	
			if (sdTgtWarehouseInAmountMap==null)sdTgtWarehouseInAmountMap=new BigDecimal(0);	
			if (sdTgtPurReturnOutAmountMap==null)sdTgtPurReturnOutAmountMap=new BigDecimal(0);
			if (purchaseTzSdOrdeAmountMap==null)purchaseTzSdOrdeAmountMap=new BigDecimal(0);
						
			if (sdInterestEndAmountMap==null)sdInterestEndAmountMap=new BigDecimal(0);
			if (purchaseTzSdOrdeInterestAmountMap==null)purchaseTzSdOrdeInterestAmountMap=new BigDecimal(0);
			if (sdInterestTgtWarehouseInAmountMap==null)sdInterestTgtWarehouseInAmountMap=new BigDecimal(0);
			if (sdInterestTgtPurReturnOutAmountMap==null)sdInterestTgtPurReturnOutAmountMap=new BigDecimal(0);
			
			
			if (groupCommbarcodeMap.containsKey(commbarcode)) {	// 组码合并换算
				String groupCommbarcode = (String) groupCommbarcodeMap.get(commbarcode);
				String groupCommbarcodekey=buId+","+groupCommbarcode;
				if (groupCommbarcodeweightedMap.containsKey(groupCommbarcodekey)) {
					Map<String, Object> groupMap = groupCommbarcodeweightedMap.get(groupCommbarcodekey);
					
					BigDecimal groupInitAmountMap = (BigDecimal) groupMap.get("initAmount");
					Integer groupInitNumMap = (Integer) groupMap.get("initNum");
					BigDecimal groupWarehouseAmountMap = (BigDecimal) groupMap.get("warehouseInAmount");
					Integer groupWarehouseNumMap = (Integer) groupMap.get("warehouseInNum");
					BigDecimal groupPurReturnOutAmountMap = (BigDecimal) groupMap.get("purReturnOutAmount");
					Integer groupPurReturnOutNumMap = (Integer) groupMap.get("purReturnOutNum");
					Integer groupPurchaseTzSdOrdeNumMap = (Integer) groupMap.get("purchaseTzSdOrdeNum");
					BigDecimal groupDecreasePurchaseNotshelfAmountMap = (BigDecimal) groupMap.get("decreasePurchaseNotshelfAmount");
					Integer groupDecreasePurchaseNotshelfNumMap = (Integer) groupMap.get("decreasePurchaseNotshelfNum");
					BigDecimal groupMoveAmountMap = (BigDecimal) groupMap.get("moveInAmount");
 					Integer groupMovefNumMap = (Integer) groupMap.get("moveInNum"); 					
					BigDecimal groupSdIntAmountMap = (BigDecimal) groupMap.get("sdIntAmount");
					BigDecimal groupSdTgtWarehouseInAmountMap = (BigDecimal) groupMap.get("sdTgtWarehouseInAmount");
					BigDecimal groupSdTgtPurReturnOutAmountMap = (BigDecimal) groupMap.get("sdTgtPurReturnOutAmount");
					BigDecimal groupPurchaseTzSdOrdeAmountMap = (BigDecimal) groupMap.get("purchaseTzSdOrdeAmount");
					
					BigDecimal groupSdInterestEndAmountMap = (BigDecimal) groupMap.get("sdInterestEndAmount");
					BigDecimal groupPurchaseTzSdOrdeInterestAmountMap = (BigDecimal) groupMap.get("purchaseTzSdOrdeInterestAmount");
					Integer groupPurchaseTzSdOrdeInterestNumMap = (Integer) groupMap.get("purchaseTzSdOrdeInterestNum");
					BigDecimal groupSdInterestTgtWarehouseInAmountMap = (BigDecimal) groupMap.get("sdInterestTgtWarehouseInAmount");
					BigDecimal groupSdInterestTgtPurReturnOutAmountMap = (BigDecimal) groupMap.get("sdInterestTgtPurReturnOutAmount");

					
					if (groupInitAmountMap==null)groupInitAmountMap=new BigDecimal(0);
					if (groupWarehouseAmountMap==null)groupWarehouseAmountMap=new BigDecimal(0);
					if (groupPurReturnOutAmountMap==null)groupPurReturnOutAmountMap=new BigDecimal(0);
					if (groupDecreasePurchaseNotshelfAmountMap==null)groupDecreasePurchaseNotshelfAmountMap=new BigDecimal(0);
					if (groupMoveAmountMap==null)groupMoveAmountMap=new BigDecimal(0);			
					if (groupInitNumMap==null)groupInitNumMap=0;
					if (groupWarehouseNumMap==null)groupWarehouseNumMap=0;
					if (groupPurReturnOutNumMap==null)groupPurReturnOutNumMap=0;
					if (groupPurchaseTzSdOrdeNumMap==null)groupPurchaseTzSdOrdeNumMap=0;
					if (groupPurchaseTzSdOrdeInterestNumMap==null)groupPurchaseTzSdOrdeInterestNumMap=0;
					
					if (groupDecreasePurchaseNotshelfNumMap==null)groupDecreasePurchaseNotshelfNumMap=0;
					if (groupMovefNumMap==null)groupMovefNumMap=0;					
					if (groupSdIntAmountMap==null)groupSdIntAmountMap=new BigDecimal(0);	
					if (groupSdTgtWarehouseInAmountMap==null)groupSdTgtWarehouseInAmountMap=new BigDecimal(0);	
					if (groupSdTgtPurReturnOutAmountMap==null)groupSdTgtPurReturnOutAmountMap=new BigDecimal(0);	
					if (groupPurchaseTzSdOrdeAmountMap==null)groupPurchaseTzSdOrdeAmountMap=new BigDecimal(0);
					
					
					if (groupSdInterestEndAmountMap==null)groupSdInterestEndAmountMap=new BigDecimal(0);
					if (groupPurchaseTzSdOrdeInterestAmountMap==null)groupPurchaseTzSdOrdeInterestAmountMap=new BigDecimal(0);
					if (groupSdInterestTgtWarehouseInAmountMap==null)groupSdInterestTgtWarehouseInAmountMap=new BigDecimal(0);
					if (groupSdInterestTgtPurReturnOutAmountMap==null)groupSdInterestTgtPurReturnOutAmountMap=new BigDecimal(0);
					
					
					groupMap.put("initAmount", initAmountMap.add(groupInitAmountMap));
					groupMap.put("initNum", initNumMap+groupInitNumMap);
					groupMap.put("warehouseInAmount", warehouseAmountMap.add(groupWarehouseAmountMap));
					groupMap.put("warehouseInNum", warehouseNumMap+groupWarehouseNumMap);
					groupMap.put("purReturnOutAmount", purReturnOutAmountMap.add(groupPurReturnOutAmountMap));					
					groupMap.put("purReturnOutNum", purReturnOutNumMap+groupPurReturnOutNumMap);
					groupMap.put("purchaseTzSdOrdeNum", purchaseTzSdOrdeNumMap+groupPurchaseTzSdOrdeNumMap);					
					groupMap.put("moveInAmount", moveInAmountMap.add(groupMoveAmountMap));
					groupMap.put("moveInNum", moveInNumMap+groupMovefNumMap);					
					groupMap.put("sdIntAmount", sdIntAmountMap.add(groupSdIntAmountMap));
					groupMap.put("sdTgtWarehouseInAmount", sdTgtWarehouseInAmountMap.add(groupSdTgtWarehouseInAmountMap));
					groupMap.put("sdTgtPurReturnOutAmount", sdTgtPurReturnOutAmountMap.add(groupSdTgtPurReturnOutAmountMap));
					groupMap.put("purchaseTzSdOrdeAmount", purchaseTzSdOrdeAmountMap.add(groupPurchaseTzSdOrdeAmountMap));

					groupMap.put("sdInterestEndAmount", sdInterestEndAmountMap.add(groupSdInterestEndAmountMap));
					groupMap.put("purchaseTzSdOrdeInterestAmount", purchaseTzSdOrdeInterestAmountMap.add(groupPurchaseTzSdOrdeInterestAmountMap));
					groupMap.put("purchaseTzSdOrdeInterestNum", purchaseTzSdOrdeInterestNumMap+groupPurchaseTzSdOrdeInterestNumMap);
					groupMap.put("sdInterestTgtWarehouseInAmount", sdInterestTgtWarehouseInAmountMap.add(groupSdInterestTgtWarehouseInAmountMap));
					groupMap.put("sdInterestTgtPurReturnOutAmount", sdInterestTgtPurReturnOutAmountMap.add(groupSdInterestTgtPurReturnOutAmountMap));
					

					groupCommbarcodeweightedMap.put(groupCommbarcodekey, groupMap);
				}else {
					Map<String, Object>groupMap=new HashMap<>();
					groupMap.put("initAmount", initAmountMap);
					groupMap.put("initNum", initNumMap);
					groupMap.put("warehouseInAmount", warehouseAmountMap);
					groupMap.put("warehouseInNum", warehouseNumMap);
					groupMap.put("purReturnOutAmount", purReturnOutAmountMap);					
					groupMap.put("purReturnOutNum", purReturnOutNumMap);
					groupMap.put("purchaseTzSdOrdeNum", purchaseTzSdOrdeNumMap);
					groupMap.put("moveInAmount", moveInAmountMap);
					groupMap.put("moveInNum", moveInNumMap);					
					groupMap.put("sdIntAmount", sdIntAmountMap);
					groupMap.put("sdTgtWarehouseInAmount", sdTgtWarehouseInAmountMap);
					groupMap.put("sdTgtPurReturnOutAmount", sdTgtPurReturnOutAmountMap);
					groupMap.put("purchaseTzSdOrdeAmount", purchaseTzSdOrdeAmountMap);
										
					groupMap.put("sdInterestEndAmount", sdInterestEndAmountMap);
					groupMap.put("purchaseTzSdOrdeInterestAmount", purchaseTzSdOrdeInterestAmountMap);
					groupMap.put("purchaseTzSdOrdeInterestNum", purchaseTzSdOrdeInterestNumMap);
					groupMap.put("sdInterestTgtWarehouseInAmount", sdInterestTgtWarehouseInAmountMap);
					groupMap.put("sdInterestTgtPurReturnOutAmount", sdInterestTgtPurReturnOutAmountMap);

					groupCommbarcodeweightedMap.put(groupCommbarcodekey, groupMap);
				}				

			}else {//非组码直接换算
				//本期加权单价=（期初金额+本月采购结转金额（汇总））/（期初数量+本期采购结转数量(汇总)）			
				BigDecimal weightedAmount=initAmountMap.add(warehouseAmountMap).add(moveInAmountMap).subtract(purReturnOutAmountMap);
				Integer weightedNum=initNumMap+warehouseNumMap+moveInNumMap-purReturnOutNumMap;
				BigDecimal sdWeightedAmount=sdIntAmountMap.add(sdTgtWarehouseInAmountMap).subtract(sdTgtPurReturnOutAmountMap).add(purchaseTzSdOrdeAmountMap);
				Integer sdWeightedNum=initNumMap+warehouseNumMap-purReturnOutNumMap+purchaseTzSdOrdeNumMap;
				// 利息金额 利息单价= sd利息期初+sd利息采购入库-sd利息采购退货+sd采购调整单利息调整金额
				BigDecimal sdInterestAmount=sdInterestEndAmountMap.add(sdInterestTgtWarehouseInAmountMap).subtract(sdInterestTgtPurReturnOutAmountMap).add(purchaseTzSdOrdeInterestAmountMap);
				// 利息数量= 期初数量+采购入库数量-采购退货数量+sd采购调整单利息调整数量
				Integer sdInterestNum=initNumMap+warehouseNumMap-purReturnOutNumMap+purchaseTzSdOrdeInterestNumMap;

				BigDecimal weightedPrice=new BigDecimal(0);
				BigDecimal sdWeightedPrice=new BigDecimal(0);
				BigDecimal sdInterestPrice=new BigDecimal(0);
				if (weightedNum!=0)weightedPrice=weightedAmount.divide(new BigDecimal(weightedNum),2, BigDecimal.ROUND_HALF_UP);// 加权单价				
				if (sdWeightedNum!=0)sdWeightedPrice=sdWeightedAmount.divide(new BigDecimal(sdWeightedNum),2, BigDecimal.ROUND_HALF_UP);//sd加权单价				
				if (BigDecimal.ZERO.compareTo(sdWeightedPrice)==0)sdWeightedPrice=new BigDecimal(0);
				if (sdInterestNum!=0)sdInterestPrice=sdInterestAmount.divide(new BigDecimal(sdInterestNum),2, BigDecimal.ROUND_HALF_UP);//sd加权单价				
				if (BigDecimal.ZERO.compareTo(sdInterestPrice)==0)sdInterestPrice=new BigDecimal(0);
			
				/**---------------------------------------------------计算非组码加权单价开始(特殊情况加权)-----------------------------------------------------------------------*/
				/**
				 * (非组码)
				 * 在现有的加权单价计算规则逻辑下增加判断，若该标准条码在上个月的期末金额且本期采购总金额均同时为0，需保存当月本期的加权单价，
				 * 则该标准条码的本期加权单价直接取最近一个月加权单价大于0的值，无需参与以上公式的计算
				 */
				Map<String, Object>parmMap=new HashMap<String, Object>();				
				parmMap.put("merchantId", merchant.getId());
				parmMap.put("month", month);
				parmMap.put("buId", buModel.getBuId());
				parmMap.put("commbarcode", commbarcode);
				
				//type 1加权单价  2 sd加权单价 3 sd利息加权
				BigDecimal purchaseEndAmount=warehouseAmountMap.add(moveInAmountMap).subtract(purReturnOutAmountMap);// 采购结转金额
				if (new BigDecimal("0").compareTo(initAmountMap)==0&&new BigDecimal("0").compareTo(purchaseEndAmount)==0) {
					weightedPrice=getLastWeightedPrice(parmMap,"1");
				}
				//type 1加权单价  2 sd加权单价 3 sd利息加权
				BigDecimal sdWarehouseAmount=sdTgtWarehouseInAmountMap.subtract(sdTgtPurReturnOutAmountMap).add(purchaseTzSdOrdeAmountMap);
				if (new BigDecimal("0").compareTo(sdIntAmountMap)==0&&new BigDecimal("0").compareTo(sdWarehouseAmount)==0) {
					sdWeightedPrice=getLastWeightedPrice(parmMap,"2");
				}
				//type 1加权单价  2 sd加权单价 3 sd利息加权
				BigDecimal sdInterestPurchaseEndAmount=sdInterestTgtWarehouseInAmountMap.subtract(sdInterestTgtPurReturnOutAmountMap).add(purchaseTzSdOrdeInterestAmountMap);
				if (new BigDecimal("0").compareTo(sdInterestEndAmountMap)==0&&new BigDecimal("0").compareTo(sdInterestPurchaseEndAmount)==0) {
					sdInterestPrice=getLastWeightedPrice(parmMap,"3");
				}
				/**---------------------------------------------------计算非组码加权单价结束(特殊情况加权)-----------------------------------------------------------------------*/
				Map<String, Object>priceMap=new HashMap<>();
				priceMap.put("weightedPrice", weightedPrice);
				priceMap.put("sdWeightedPrice", sdWeightedPrice);
				priceMap.put("sdInterestPrice", sdInterestPrice);	
				weightedPriceMap.put(weightedKey, priceMap);
			}			
		}
		
		// 组码计算加权 并且分配给每个标准条码
		Set<String> keySet = groupCommbarcodeweightedMap.keySet();
		for (String groupCommbarcodekey  : keySet) {
			String[] groupCommbarcodeStr = groupCommbarcodekey.split(",");
			//Long buId = Long.valueOf(groupCommbarcodeStr[0]);
			String groupCommbarcode = String.valueOf(groupCommbarcodeStr[1]);
			Map<String, Object> weightedMap = groupCommbarcodeweightedMap.get(groupCommbarcodekey);
			BigDecimal groupInitAmountMap = (BigDecimal) weightedMap.get("initAmount");
			Integer groupInitNumMap = (Integer) weightedMap.get("initNum");
			BigDecimal groupWarehouseAmountMap = (BigDecimal) weightedMap.get("warehouseInAmount");
			Integer groupWarehouseNumMap = (Integer) weightedMap.get("warehouseInNum");
			BigDecimal groupPurReturnOutAmountMap = (BigDecimal) weightedMap.get("purReturnOutAmount");
			Integer groupPurReturnOutNumMap = (Integer) weightedMap.get("purReturnOutNum");
			Integer groupPurchaseTzSdOrdeNumMap = (Integer) weightedMap.get("purchaseTzSdOrdeNum");
			BigDecimal groupMoveAmountMap = (BigDecimal) weightedMap.get("moveInAmount");
			Integer groupMovefNumMap = (Integer) weightedMap.get("moveInNum");
			BigDecimal groupSdIntAmountMap = (BigDecimal) weightedMap.get("sdIntAmount");
			BigDecimal groupSdTgtWarehouseInAmountMap = (BigDecimal) weightedMap.get("sdTgtWarehouseInAmount");
			BigDecimal groupSdTgtPurReturnOutAmountMap = (BigDecimal) weightedMap.get("sdTgtPurReturnOutAmount");
			BigDecimal groupPurchaseTzSdOrdeAmountMap = (BigDecimal) weightedMap.get("purchaseTzSdOrdeAmount");
			
			BigDecimal groupSdInterestEndAmountMap = (BigDecimal) weightedMap.get("sdInterestEndAmount");
			BigDecimal groupPurchaseTzSdOrdeInterestAmountMap = (BigDecimal) weightedMap.get("purchaseTzSdOrdeInterestAmount");
			Integer groupPurchaseTzSdOrdeInterestNumMap = (Integer) weightedMap.get("purchaseTzSdOrdeInterestNum");
			BigDecimal groupSdInterestTgtWarehouseInAmountMap = (BigDecimal) weightedMap.get("sdInterestTgtWarehouseInAmount");
			BigDecimal groupSdInterestTgtPurReturnOutAmountMap = (BigDecimal) weightedMap.get("sdInterestTgtPurReturnOutAmount");


			BigDecimal weightedAmount=groupInitAmountMap.add(groupWarehouseAmountMap).add(groupMoveAmountMap).subtract(groupPurReturnOutAmountMap);
			Integer weightedNum=groupInitNumMap+groupWarehouseNumMap+groupMovefNumMap-groupPurReturnOutNumMap;			
			Integer sdWeightedNum=groupInitNumMap+groupWarehouseNumMap-groupPurReturnOutNumMap+groupPurchaseTzSdOrdeNumMap;// lp/sd加权单价的数量
			BigDecimal sdWeightedAmount=groupSdIntAmountMap.add(groupSdTgtWarehouseInAmountMap).subtract(groupSdTgtPurReturnOutAmountMap).add(groupPurchaseTzSdOrdeAmountMap);

			// 利息金额 利息单价= sd利息期初+sd利息采购入库-sd利息采购退货+sd采购调整单利息调整金额
			BigDecimal sdInterestAmount=groupSdInterestEndAmountMap.add(groupSdInterestTgtWarehouseInAmountMap).subtract(groupSdInterestTgtPurReturnOutAmountMap).add(groupPurchaseTzSdOrdeInterestAmountMap);
			//  利息数量= 期初数量+采购入库数量-采购退货数量+sd采购调整单利息调整数量
			Integer sdInterestNum=groupInitNumMap+groupWarehouseNumMap-groupPurReturnOutNumMap+groupPurchaseTzSdOrdeInterestNumMap;//// 利息数量= 期初数量+采购入库数量-采购退货数量+sd采购调整单利息调整数量
			
			
			
			BigDecimal weightedPrice=new BigDecimal(0);
			BigDecimal sdWeightedPrice=new BigDecimal(0);
			BigDecimal sdInterestPrice=new BigDecimal(0);
			if (weightedNum!=0)weightedPrice=weightedAmount.divide(new BigDecimal(weightedNum),2,BigDecimal.ROUND_HALF_UP);			
			if (sdWeightedNum!=0)sdWeightedPrice=sdWeightedAmount.divide(new BigDecimal(sdWeightedNum),2, BigDecimal.ROUND_HALF_UP);//sd加权单价总金额				
			if (BigDecimal.ZERO.compareTo(sdWeightedPrice)==0)sdWeightedPrice=new BigDecimal(0);
			if (sdInterestNum!=0)sdInterestPrice=sdInterestAmount.divide(new BigDecimal(sdInterestNum),2, BigDecimal.ROUND_HALF_UP);//sd加权单价				
			if (BigDecimal.ZERO.compareTo(sdInterestPrice)==0)sdInterestPrice=new BigDecimal(0);

			/**---------------------------------------------------计算组码加权单价开始(特殊情况加权)-----------------------------------------------------------------------*/
			Map<String, Object>parmMap=new HashMap<String, Object>();				
			parmMap.put("merchantId", merchant.getId());
			parmMap.put("month", month);
			parmMap.put("buId", buModel.getBuId());
			parmMap.put("commbarcode", groupCommbarcode);
			
			//type 1加权单价  2 sd加权单价 3 sd利息加权
			BigDecimal purchaseEndAmount=groupWarehouseAmountMap.add(groupMoveAmountMap).subtract(groupPurReturnOutAmountMap);// 采购结转金额
			if (new BigDecimal("0").compareTo(groupInitAmountMap)==0&&new BigDecimal("0").compareTo(purchaseEndAmount)==0) {
				weightedPrice=getLastWeightedPrice(parmMap,"1");
			}
			//type 1加权单价  2 sd加权单价 3 sd利息加权
			BigDecimal sdWarehouseAmount=groupSdTgtWarehouseInAmountMap.subtract(groupSdTgtPurReturnOutAmountMap).add(groupPurchaseTzSdOrdeAmountMap);
			if (new BigDecimal("0").compareTo(groupSdIntAmountMap)==0&&new BigDecimal("0").compareTo(sdWarehouseAmount)==0) {
				sdWeightedPrice=getLastWeightedPrice(parmMap,"2");
			}
			//type 1加权单价  2 sd加权单价 3 sd利息加权
			BigDecimal sdInterestPurchaseEndAmount=groupSdInterestTgtWarehouseInAmountMap.subtract(groupSdInterestTgtPurReturnOutAmountMap).add(groupPurchaseTzSdOrdeInterestAmountMap);
			if (new BigDecimal("0").compareTo(groupSdInterestEndAmountMap)==0&&new BigDecimal("0").compareTo(sdInterestPurchaseEndAmount)==0) {
				sdInterestPrice=getLastWeightedPrice(parmMap,"3");
			}
			
			/**---------------------------------------------------计算组码加权单价结束(特殊情况加权)-----------------------------------------------------------------------*/

			// 根据组码获取组码对应的标准条码
			List<String> groupCommbarcodeList = groupCommbarcodeDao.getCommbarcodeBygroupCommbar(groupCommbarcode);
			for (String commbarcode : groupCommbarcodeList) {
				String weightedKey=buId+","+commbarcode;
				Map<String, Object>priceMap=new HashMap<>();
				priceMap.put("weightedPrice", weightedPrice);
				priceMap.put("sdWeightedPrice", sdWeightedPrice);
				priceMap.put("sdInterestPrice", sdInterestPrice);
				weightedPriceMap.put(weightedKey, priceMap);
			}			
		}
		logger.info("清除:"+merchant.getName()+","+month+"加权单价");
		Map<String, Object> delParamMap = new HashMap<String, Object>();
		delParamMap.put("merchantId", merchant.getId());
		delParamMap.put("month", month);
		delParamMap.put("buId", buId);
		weightedPriceDao.delWeightedPrice(delParamMap);	
		logger.info("清除:"+merchant.getName()+","+month+"加权单价");
		sdWeightedPriceDao.delSdWeightedPrice(delParamMap);				
		logger.info("生成:"+merchant.getName()+","+month+"加权单价开始");
		// 删除利息sd单价开始
		logger.info("清除:"+merchant.getName()+","+month+"利息加权单价");
		sdInterestPriceDao.delSdInterestPrice(delParamMap);		
		logger.info("生成:"+merchant.getName()+","+month+"利息加权单价开始");
		// 生成加权单价表
		Set<String> weightedPriceSet = weightedPriceMap.keySet();
		for (String weightedKey : weightedPriceSet) {
			Map<String, Object> weightedSummary = weightedSummaryMap.get(weightedKey);// 上月的币种
			String currency=null;
			if (weightedSummary!=null)currency = (String) weightedSummary.get("currency");//			
			if (StringUtils.isEmpty(currency)) currency=merchant.getAccountCurrency();
			String[] weightedKeyStr = weightedKey.split(",");
			//Long buId = Long.valueOf(weightedKeyStr[0]);
			//BusinessUnitModel buModel = buModelMap.get(buId);
			String commbarcode = weightedKeyStr[1];
			// 根据商家和标准条码查询商品
			Map<String, Object>paramMap=new HashMap<>();
			paramMap.put("merchantId", merchant.getId());
			paramMap.put("commbarcode", commbarcode);
			Map<String, Object> merchandiseInfoMap = merchandiseInfoDao.getGoodsByMerchantIdWeighted(paramMap);
			String goodsName=null;Long brandId=null;String brandName =null;
			if (merchandiseInfoMap!=null) {
				goodsName = (String) merchandiseInfoMap.get("goods_name");
				brandId = (Long) merchandiseInfoMap.get("brand_id");
				brandName = (String) merchandiseInfoMap.get("brand_name");
			}
			Map<String, Object>priceMap = weightedPriceMap.get(weightedKey);
			if (priceMap==null)priceMap=new HashMap<>();						
			BigDecimal weightedPrice = (BigDecimal) priceMap.get("weightedPrice");
			BigDecimal sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
			BigDecimal sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
			
			WeightedPriceModel weightedPriceModel=new WeightedPriceModel();
			weightedPriceModel.setMerchantId(merchant.getId());
			weightedPriceModel.setMerchantName(merchant.getName());
			weightedPriceModel.setGoodsName(goodsName);
			weightedPriceModel.setCommbarcode(commbarcode);
			weightedPriceModel.setBrandId(brandId);
			weightedPriceModel.setBrandName(brandName);
			weightedPriceModel.setCurrency(currency);
			weightedPriceModel.setPrice(weightedPrice);
			weightedPriceModel.setEffectiveMonth(month);
			weightedPriceModel.setBuId(buId);
			weightedPriceModel.setBuName(buName);
			weightedPriceDao.save(weightedPriceModel);
			// 生成sd单价
			SdWeightedPriceModel sdWeightedPriceModel=new SdWeightedPriceModel();
			sdWeightedPriceModel.setMerchantId(merchant.getId());
			sdWeightedPriceModel.setMerchantName(merchant.getName());
			sdWeightedPriceModel.setGoodsName(goodsName);
			sdWeightedPriceModel.setCommbarcode(commbarcode);
			sdWeightedPriceModel.setBrandId(brandId);
			sdWeightedPriceModel.setBrandName(brandName);
			sdWeightedPriceModel.setCurrency(currency);
			sdWeightedPriceModel.setPrice(sdWeightedPrice);
			sdWeightedPriceModel.setEffectiveMonth(month);
			sdWeightedPriceModel.setBuId(buId);
			sdWeightedPriceModel.setBuName(buName);
			sdWeightedPriceDao.save(sdWeightedPriceModel);
			
			// 生产利息sd单价
			SdInterestPriceModel sdInterestPriceModel=new SdInterestPriceModel();
			sdInterestPriceModel.setMerchantId(merchant.getId());
			sdInterestPriceModel.setMerchantName(merchant.getName());
			sdInterestPriceModel.setGoodsName(goodsName);
			sdInterestPriceModel.setCommbarcode(commbarcode);
			sdInterestPriceModel.setBrandId(brandId);
			sdInterestPriceModel.setBrandName(brandName);
			sdInterestPriceModel.setCurrency(currency);
			sdInterestPriceModel.setPrice(sdInterestPrice);
			sdInterestPriceModel.setEffectiveMonth(month);
			sdInterestPriceModel.setBuId(buId);
			sdInterestPriceModel.setBuName(buName);
			sdInterestPriceDao.save(sdInterestPriceModel);
			
	
		}
		logger.info("生成:"+merchant.getName()+","+month+"事业部:"+buName+"加权单价结束");
		
	}
	
	/**
	 * 获取加权单价/sd加权单价/sd利息单价
	 * type 1加权单价  2 sd加权单价 3 sd利息加权
	 * @param merchant
	 * @param month
	 * @param buModel
	 * @param commbarcode
	 * @return
	 * @throws SQLException 
	 */
	private BigDecimal getLastWeightedPrice(Map<String, Object>parmMap,String type) throws SQLException {
		BigDecimal price=new BigDecimal(0);
		// 加权单价
		if ("1".equals(type)) {
			WeightedPriceDTO lastWeightedPrice = weightedPriceDao.getLastWeightedPrice(parmMap);
			if (lastWeightedPrice!=null) price=lastWeightedPrice.getPrice();
		}
		// sd加权单价
		if ("2".equals(type)) {
			SdWeightedPriceDTO lastWeightedPrice = sdWeightedPriceDao.getLastWeightedPrice(parmMap);
			if (lastWeightedPrice!=null) price=lastWeightedPrice.getPrice();
		}
		// sd利息加权
		if ("3".equals(type)) {
			SdInterestPriceDTO lastWeightedPrice = sdInterestPriceDao.getLastWeightedPrice(parmMap);
			if (lastWeightedPrice!=null) price=lastWeightedPrice.getPrice();
		}
		if (price==null)price=new BigDecimal("0");
		return price;
	}

	/**
	 * 事业部财务经销存采购入库明细
	 * @param merchant
	 * @param month
	 * @param warehouseAndAnalysisMapList
	 * @param depotModelMap
	 * @param buModelMap
	 * @param depotIdTag
	 * @param allMerchandiseMap
	 * @param currencyMap
	 * @throws SQLException 
	 */
	private void saveFinanceWarehouseDetailsWeighted(MerchantBuRelModel buModel,Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant, String month,
			List<Map<String, Object>> warehouseAndAnalysisMapList, 
			List<Map<String, Object>> purchaseReturnOdepotMapList,
			List<Map<String, Object>> tzPurchaseSdOrdeMapList,
			Map<Long, DepotInfoModel> depotModelMap,
			/* String depotIdTag, */
			Map<Long, Map<String, Object>> allMerchandiseMap,
			Map<String,  Map<String, Object>>weightedSummaryMap,
			Map<String, String> currencyMap,
			Map<String, Object> groupCommbarcodeMap,String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap) throws SQLException {

		List<BuFinanceWarehouseDetailsModel> orderList=new ArrayList<>();
		List<BuFinanceSdWarehouseDetailsModel> orderSdList=new ArrayList<>();
		Long buId=buModel.getBuId();
		String buName = buModel.getBuName();
    	for (Map<String, Object> map : warehouseAndAnalysisMapList) {
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		//Long buId = (Long) map.get("bu_id");//事业部id
    		Long orderId = (Long) map.get("order_id");//采购订单id
    		String orderCode = (String) map.get("order_code");//采购订单编码
    		Long warehouseId = (Long) map.get("warehouse_id");//采购入库单id
    		String warehouseCode = (String) map.get("warehouse_code");//采购入库单编码
    		Long goodsId = (Long) map.get("goods_id");//商品id
    		Long purchaseItemId = (Long) map.get("purchase_item_id");//采购订单表体id
    		//String goodsNo = (String) map.get("goods_no");//商品货号
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位
    		BigDecimal num = (BigDecimal) map.get("num");//采购入库分析表数量
    		//Timestamp relDate = (Timestamp) map.get("rel_date");// 上架时间
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//理货单位
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//理货单位
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
    		
    		
    		String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			} 
    		    
    		

    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		Integer warehouseNum = changeUnit(tallyingUnit,num,boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(orderId);
    		PurchaseOrderItemModel purchaseOrderItemModel=purchaseOrderItemDao.searchById(purchaseItemId);
    		// 订单单价
    		BigDecimal orderPrice = purchaseOrderItemModel.getPrice();
    		BigDecimal orderChangeAmount =new BigDecimal(0);
    		BigDecimal orderChangePrice =null;
    		if (orderPrice!=null&&warehouseNum!=null&&warehouseNum!=0&&num!=null) {
    			orderChangeAmount=orderPrice.multiply(num);
    			orderChangeAmount=orderChangeAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    			orderChangePrice=orderPrice.multiply(num).divide(new BigDecimal(warehouseNum),8,BigDecimal.ROUND_HALF_UP);
			}
    		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		List<Map<String,Object>> sdOrderMapList=new ArrayList<>();	
			BigDecimal sdTgtAmount = new BigDecimal(0);// sd 本币金额	
			BigDecimal sdInterestTgtAmount = new BigDecimal(0);// sd 利息金额	
			BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
			
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buId,goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(warehouseNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算
				String tgtCurrency =purchaseOrderModel.getTgtCurrency();
	    		BigDecimal tgtPrice=purchaseOrderItemModel.getTgtPrice();
	    		if (tgtPrice==null)tgtPrice=new BigDecimal(0);
	    		if (num!=null&&warehouseNum!=null&&warehouseNum!=0) {
	    			warehouseAmount=tgtPrice.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);
	    			price = tgtPrice.multiply(num).divide(new BigDecimal(warehouseNum),8,BigDecimal.ROUND_HALF_UP);	
				}
	    		//获取差异金额
	    		differencePrice =getDifferencePrice(merchant, orderId, purchaseOrderModel.getCurrency(), tgtCurrency, month, orderChangePrice,price);
	    		if (differencePrice==null)differencePrice=new BigDecimal(0);
	    		differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
	    		differencePriceAmount=differencePrice.multiply(new BigDecimal(warehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				getWeightedMap(buId,commbarcode,weightedSummaryMap,warehouseNum,warehouseAmount,"warehouseInAmount","warehouseInNum");
    			currency = tgtCurrency;
    			if (orderId!=null&&goodsId!=null) {
    				Map<String, Object>sdMap= getLpAndSdAmount(orderId,warehouseNum,"1", purchaseItemId);// 采购					
    				sdOrderMapList = (List<Map<String, Object>>) sdMap.get("sdOrderMapList");					
		    		sdTgtAmount = (BigDecimal) sdMap.get("sdTgtAmount");
		    		sdInterestTgtAmount = (BigDecimal) sdMap.get("sdInterestTgtAmount");
					String weightedKey=buId+","+commbarcode;
					Map<String, Object> weightedMap = weightedSummaryMap.get(weightedKey);
					if (weightedMap==null)weightedMap=new HashMap<>();
					BigDecimal sdTgtWarehouseInAmountMap = (BigDecimal) weightedMap.get("sdTgtWarehouseInAmount");
					BigDecimal sdInterestTgtWarehouseInAmountMap = (BigDecimal) weightedMap.get("sdInterestTgtWarehouseInAmount");
					if (sdTgtWarehouseInAmountMap==null)sdTgtWarehouseInAmountMap=new BigDecimal(0);
					if (sdInterestTgtWarehouseInAmountMap==null)sdInterestTgtWarehouseInAmountMap=new BigDecimal(0);
					weightedMap.put("sdTgtWarehouseInAmount", sdTgtAmount.add(sdTgtWarehouseInAmountMap));
					weightedMap.put("sdInterestTgtWarehouseInAmount", sdInterestTgtAmount.add(sdInterestTgtWarehouseInAmountMap));
					weightedSummaryMap.put(weightedKey, weightedMap);					
				}
			}
    		if (warehouseAmount==null)warehouseAmount=new BigDecimal(0);    		
    		// 根据采购入库id查询 采购入库单
    		PurchaseWarehouseModel purchaseWarehouseModel = purchaseWarehouseDao.searchById(warehouseId);
    		
    		String deBuKey=depotId+","+buId+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)) {
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer warehouseNumMap = (Integer) summaryMap.get("warehouseInNum");
    			BigDecimal warehouseInAmountMap =(BigDecimal) summaryMap.get("warehouseInAmount");
    			BigDecimal sdTgtWarehouseInAmountMap =(BigDecimal) summaryMap.get("sdTgtWarehouseInAmount");
    			BigDecimal sdInterestTgtWarehouseInAmountMap =(BigDecimal) summaryMap.get("sdInterestTgtWarehouseInAmount");
    			if (warehouseNumMap==null)warehouseNumMap=0;
    			if (warehouseInAmountMap==null)warehouseInAmountMap=new BigDecimal(0);    			
    			if (sdTgtWarehouseInAmountMap==null)sdTgtWarehouseInAmountMap=new BigDecimal(0);
    			if (sdInterestTgtWarehouseInAmountMap==null)sdInterestTgtWarehouseInAmountMap=new BigDecimal(0);
    			summaryMap.put("warehouseInNum", warehouseNum+warehouseNumMap);
    			summaryMap.put("warehouseInAmount", warehouseAmount.add(warehouseInAmountMap));
    			summaryMap.put("sdTgtWarehouseInAmount", sdTgtAmount.add(sdTgtWarehouseInAmountMap));
    			summaryMap.put("sdInterestTgtWarehouseInAmount", sdInterestTgtAmount.add(sdInterestTgtWarehouseInAmountMap));
    			saveSummaryMap.put(deBuKey, summaryMap);
    					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();
				if (warehouseNum==null)warehouseNum=0;				
				summaryMap.put("warehouseInNum", warehouseNum);
				summaryMap.put("warehouseInAmount", warehouseAmount);
				summaryMap.put("sdTgtWarehouseInAmount", sdTgtAmount);
				summaryMap.put("sdInterestTgtWarehouseInAmount", sdInterestTgtAmount);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		// 生成 财务经销存采购入库明细
    		BuFinanceWarehouseDetailsModel financeWarehouseDetailsModel=new BuFinanceWarehouseDetailsModel();
    		financeWarehouseDetailsModel.setBrandId(brandId);
    		financeWarehouseDetailsModel.setBrandName(brandName);
    		financeWarehouseDetailsModel.setMerchantId(merchant.getId());//商家id
    		financeWarehouseDetailsModel.setMerchantName(merchant.getName());//商家名称
    		financeWarehouseDetailsModel.setInDepotId(purchaseWarehouseModel.getDepotId());//入库仓库id
    		financeWarehouseDetailsModel.setInDepotName(purchaseWarehouseModel.getDepotName());//入库仓库名称
    		financeWarehouseDetailsModel.setOrderId(orderId);//订单id
    		financeWarehouseDetailsModel.setOrderCode(orderCode);//订单编码
    		financeWarehouseDetailsModel.setOrderCreateDate(purchaseOrderModel.getCreateDate());//订单日期 (订单创建日期)
    		financeWarehouseDetailsModel.setWarehouseId(warehouseId);//入库单id
    		financeWarehouseDetailsModel.setWarehouseCode(warehouseCode);//入库单编码
    		financeWarehouseDetailsModel.setSupplierId(purchaseOrderModel.getSupplierId());//供应商id
    		financeWarehouseDetailsModel.setSupplierName(purchaseOrderModel.getSupplierName());//供应商名称
    		financeWarehouseDetailsModel.setWarehouseCreateDate(purchaseWarehouseModel.getInboundDate());//入库时间 (入库单创建时间)
    		financeWarehouseDetailsModel.setPoNo(purchaseOrderModel.getPoNo());//PO号
    		financeWarehouseDetailsModel.setDrawInvoiceDate(purchaseOrderModel.getDrawInvoiceDate());//开发票时间(发票上面的时间)
    		financeWarehouseDetailsModel.setInvoiceNo(purchaseOrderModel.getInvoiceNo());//发票号
    		financeWarehouseDetailsModel.setGoodsId(goodsId);//商品id
    		financeWarehouseDetailsModel.setGoodsName(goodsName);//商品名称
    		financeWarehouseDetailsModel.setGoodsNo(goodsNo);//商品货号
    		financeWarehouseDetailsModel.setBarcode(barcode);//商品条码
    		financeWarehouseDetailsModel.setCommbarcode(commbarcode);// 标准条码
    		financeWarehouseDetailsModel.setSuperiorParentBrand(superiorParentBrand);
    		//financeWarehouseDetailsModel.setTallyingUnit(tallyingUnit);//理货单位(00-托盘，01-箱，02-件)
    		financeWarehouseDetailsModel.setOrderCurrency(purchaseOrderModel.getCurrency());//采购币种 01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
    		financeWarehouseDetailsModel.setOrderPrice(orderChangePrice);//采购单价
    		financeWarehouseDetailsModel.setOrderAmount(orderChangeAmount);//采购金额
    		financeWarehouseDetailsModel.setWarehouseNum(warehouseNum);//数量
    		financeWarehouseDetailsModel.setWarehouseCurrency(currency);//入库币种 01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
    		financeWarehouseDetailsModel.setWarehousePrice(price);//入库单价(结算价格单价)
    		financeWarehouseDetailsModel.setWarehouseAmount(warehouseAmount);// 入库金额    		
    		financeWarehouseDetailsModel.setMonth(month);// 归属月份 YYYY-MM
    		//financeWarehouseDetailsModel.setRelDate(relDate);// 上架时间
    		financeWarehouseDetailsModel.setCreater(purchaseOrderModel.getCreater());
    		financeWarehouseDetailsModel.setCreateName(purchaseOrderModel.getCreateName());
    		financeWarehouseDetailsModel.setBuId(buId);
    		financeWarehouseDetailsModel.setBuName(buName);
    		financeWarehouseDetailsModel.setOrderType(DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_1);//采购入库   		
    		financeWarehouseDetailsModel.setSdTgtAmount(sdTgtAmount);  
    		
    		financeWarehouseDetailsModel.setStockLocationTypeId(stockLocationTypeId);
    		financeWarehouseDetailsModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeWarehouseDetailsModel.setDifferencePrice(differencePrice);
    		financeWarehouseDetailsModel.setDifferenceAmount(differencePriceAmount);
    		
    		orderList.add(financeWarehouseDetailsModel);
    		for (Map<String, Object> sdMap : sdOrderMapList) {
    			Long sdId=(Long) sdMap.get("id");
    			String sdCode=(String) sdMap.get("code");
    			String poNo=(String) sdMap.get("po_no");
    			BigDecimal tgtPrice=(BigDecimal) sdMap.get("sd_tgt_price");
    			String sdTypeName=(String) sdMap.get("sd_type_name");
    			String sdSimpleName=(String) sdMap.get("sd_simple_name");    
    			
    			BigDecimal sdItemSdPrice=(BigDecimal) sdMap.get("sd_price");// 原币单价
    			if (sdItemSdPrice==null)sdItemSdPrice=new BigDecimal(0);
    			BigDecimal sdOrderAmount= sdItemSdPrice.multiply(new BigDecimal(warehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);// sd原币金额
    			String sdtgtCurrency=(String) sdMap.get("tgt_currency"); // sd本币币种
    			String sdCurrency=(String) sdMap.get("currency"); // sd 原币币种
    			
    			BuFinanceSdWarehouseDetailsModel sdWarehouseDetails=new BuFinanceSdWarehouseDetailsModel();
        		sdWarehouseDetails.setMerchantId(merchant.getId());
        		sdWarehouseDetails.setMerchantName(merchant.getName());
        		sdWarehouseDetails.setInDepotId(purchaseWarehouseModel.getDepotId());
        		sdWarehouseDetails.setInDepotName(purchaseWarehouseModel.getDepotName());
        		sdWarehouseDetails.setBuId(buId);
        		sdWarehouseDetails.setBuName(buName);
        		sdWarehouseDetails.setOrderId(orderId);
        		sdWarehouseDetails.setOrderCode(orderCode);
        		sdWarehouseDetails.setWarehouseCreateDate(purchaseWarehouseModel.getInboundDate());
        		sdWarehouseDetails.setPoNo(poNo);
        		sdWarehouseDetails.setSupplierId(purchaseOrderModel.getSupplierId());
        		sdWarehouseDetails.setSupplierName(purchaseOrderModel.getSupplierName());
        		sdWarehouseDetails.setGoodsId(goodsId);
        		sdWarehouseDetails.setGoodsName(goodsName);
        		sdWarehouseDetails.setGoodsNo(goodsNo);
        		sdWarehouseDetails.setWarehouseNum(warehouseNum);
    			if (tgtPrice==null)tgtPrice=new BigDecimal(0);
    			BigDecimal sdAmount= tgtPrice.multiply(new BigDecimal(warehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
        		sdWarehouseDetails.setAmount(sdAmount);
        		sdWarehouseDetails.setSuperiorParentBrand(superiorParentBrand);
        		sdWarehouseDetails.setBrandId(brandId);
        		sdWarehouseDetails.setBrandName(brandName);
        		sdWarehouseDetails.setSdSimpleName(sdSimpleName);
        		sdWarehouseDetails.setSdTypeName(sdTypeName);
        		sdWarehouseDetails.setOrderType(DERP_REPORT.FINANCESDWAREHOUSEDETAILS_ORDERTYPE_1);
        		sdWarehouseDetails.setMonth(month);
        		sdWarehouseDetails.setSdOrderId(sdId);;
        		sdWarehouseDetails.setSdOrderCode(sdCode);
        		sdWarehouseDetails.setWarehouseCurrency(sdtgtCurrency);// 本位币
        		sdWarehouseDetails.setOrderCurrency(sdCurrency);// 原币
        		sdWarehouseDetails.setOrderAmount(sdOrderAmount);// 原币金额
        		sdWarehouseDetails.setOrderPrice(sdItemSdPrice);// 原币单价
        		
        		orderSdList.add(sdWarehouseDetails);
			}
    		

    		
    	}
    	
    	// 采购退货出库
    	for (Map<String, Object> map : purchaseReturnOdepotMapList) {
    		//Long buId = (Long) map.get("bu_id");//采购退货订单id
    		
    		
    		String depotName = (String) map.get("depot_name");//仓库名称  
    		Long depotId = (Long) map.get("depot_id");//采购退货订单id    		)
    		Long orderId = (Long) map.get("order_id");//采购退货订单id
    		String orderCode = (String) map.get("order_code");//采购退货订单编码
    		Long outOrderId = (Long) map.get("out_order_id");//采购退货出库单id
    		String outOrderCode = (String) map.get("out_order_code");//采购退货出库单编码
    		Long goodsId = (Long) map.get("goods_id");//商品id
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位
    		Integer num = (Integer) map.get("num");//数量
    		Timestamp returnOutDate = (Timestamp) map.get("return_out_date");// 出库时间
    		String supplierName = (String) map.get("supplier_name");//供应商名称
    		Long supplierId = (Long) map.get("supplier_id");//供应商id
    		String poNo = (String) map.get("po_no");//po
    		String orderCurrency = (String) map.get("currency");//币种
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//理货单位
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//理货单位
    		Map<String, Object> merchandiseMap =allMerchandiseMap.get(goodsId);
    		
    		String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");

			}
    		   		  		    		
    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		Integer warehouseNum = changeUnit(tallyingUnit,new BigDecimal(num),boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		// 根据采购订单id查询采购订单 // 如果采购入库单有商品,采购订单没有此商品 此时 查询不到对应的采购订单
    		PurchaseReturnOrderModel purReturnModel=new PurchaseReturnOrderModel();
    		if (orderId!=null) {
    			purReturnModel.setId(orderId);
    			List<PurchaseReturnOrderModel> purReturnList = purchaseReturnOrderDao.list(purReturnModel);
    			if (purReturnList.size()>0) {
    				purReturnModel=purReturnList.get(0);
				}
			}
    		    		
    		PurchaseReturnItemModel purReturnItemModel =new PurchaseReturnItemModel();
    		// 查询采购订单商品
    		if (purReturnModel!=null&&purReturnModel.getId()!=null) {    		
    			purReturnItemModel.setOrderId(purReturnModel.getId());
    			purReturnItemModel.setGoodsId(goodsId);
        		List<PurchaseReturnItemModel> purReturnItemList = purchaseReturnItemDao.list(purReturnItemModel);
        		if (purReturnItemList.size()>1) {
        			logger.error("采购退货出库明细:"+merchant.getName()+"订单id"+orderId+",商品id"+goodsId+"查询出两条采购退货订单商品");
					throw new RuntimeException("采购退货出库明细明细:"+merchant.getName()+"订单id"+orderId+",商品id"+goodsId+"查询出两条采购退货订单商品");
				}
        		if (purReturnItemList.size()>0) {
        			purReturnItemModel=purReturnItemList.get(0);
				}
			}
    		// 订单单价
    		BigDecimal orderPrice=purReturnItemModel.getReturnPrice();
    		BigDecimal orderAmount=null;//订单金额
    		if (orderPrice!=null&&num!=null) {
    			orderAmount = orderPrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
			}

    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		List<Map<String,Object>> sdOrderMapList= new ArrayList<>();
			BigDecimal sdTgtAmount = new BigDecimal(0);// sd 本币金额
			BigDecimal sdInterestTgtAmount = new BigDecimal(0);// sd 利息金额	
			BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buId,goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(warehouseNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String tgtCurrency =purReturnModel.getTgtCurrency();
	    		BigDecimal tgtPrice=purReturnItemModel.getTgtReturnPrice();
	    		if (tgtPrice==null)tgtPrice=new BigDecimal(0);
	    		if (num!=null&&warehouseNum!=null&&warehouseNum!=0) {
	    			warehouseAmount=tgtPrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);;
	    			price = tgtPrice.multiply(new BigDecimal(num)).divide(new BigDecimal(warehouseNum),2,BigDecimal.ROUND_HALF_UP);
				}
	    		//获取差异金额
	    		differencePrice =getDifferencePrice(merchant, orderId, orderCurrency, tgtCurrency, month, orderPrice,price);
	    		if (differencePrice==null)differencePrice=new BigDecimal(0);
	    		differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
	    		differencePriceAmount=differencePrice.multiply(new BigDecimal(warehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
	    		
				getWeightedMap(buId,commbarcode,weightedSummaryMap,warehouseNum,warehouseAmount,"purReturnOutAmount","purReturnOutNum");
    			currency = tgtCurrency;
    			// 获取 本期采购LP金额 和本期采购SD金额
    			//采购退货没有sd 单 此处注释
    			/**if (orderId!=null&&goodsId!=null) {
					Map<String, Object>sdMap= getLpAndSdAmount(orderId,warehouseNum,"2",purchaseReturnItemId);//采购退货
					sdOrderMapList = (List<Map<String, Object>>) sdMap.get("sdOrderMapList");
					sdTgtAmount = (BigDecimal) sdMap.get("sdTgtAmount");
					sdInterestTgtAmount = (BigDecimal) sdMap.get("sdInterestTgtAmount");

					String weightedKey=buId+","+commbarcode;
					Map<String, Object> weightedMap = weightedSummaryMap.get(weightedKey);
					if (weightedMap==null)weightedMap=new HashMap<>();
					BigDecimal sdTgtPurReturnOutAmountMap = (BigDecimal) weightedMap.get("sdTgtPurReturnOutAmount");
					BigDecimal sdInterestTgtPurReturnOutAmountMap = (BigDecimal) weightedMap.get("sdInterestTgtPurReturnOutAmount");
					if (sdTgtPurReturnOutAmountMap==null)sdTgtPurReturnOutAmountMap=new BigDecimal(0);
					if (sdInterestTgtPurReturnOutAmountMap==null)sdInterestTgtPurReturnOutAmountMap=new BigDecimal(0);
					weightedMap.put("sdTgtPurReturnOutAmount", sdTgtAmount.add(sdTgtPurReturnOutAmountMap));
					weightedMap.put("sdInterestTgtPurReturnOutAmount", sdInterestTgtAmount.add(sdInterestTgtPurReturnOutAmountMap));
					weightedSummaryMap.put(weightedKey, weightedMap);					
				}*/
			}
    		   		
    		
    		
    		if (warehouseAmount==null) warehouseAmount=new BigDecimal(0);
    		String deBuKey=depotId+","+buId+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer returnNumNumMap = (Integer) summaryMap.get("purReturnOutNum");
    			BigDecimal purReturnOutAmountMap = (BigDecimal) summaryMap.get("purReturnOutAmount");
    			BigDecimal sdTgtPurReturnOutAmountMap = (BigDecimal) summaryMap.get("sdTgtPurReturnOutAmount");
    			BigDecimal sdInterestTgtPurReturnOutAmountMap = (BigDecimal) summaryMap.get("sdInterestTgtPurReturnOutAmount");
    			if (returnNumNumMap==null)returnNumNumMap=0;
    			if (purReturnOutAmountMap==null) purReturnOutAmountMap=new BigDecimal(0);
    			if (sdTgtPurReturnOutAmountMap==null) sdTgtPurReturnOutAmountMap=new BigDecimal(0);
    			if (sdInterestTgtPurReturnOutAmountMap==null) sdInterestTgtPurReturnOutAmountMap=new BigDecimal(0);
    			summaryMap.put("purReturnOutNum", warehouseNum+returnNumNumMap);
    			summaryMap.put("purReturnOutAmount", warehouseAmount.add(purReturnOutAmountMap));
    			summaryMap.put("sdTgtPurReturnOutAmount", sdTgtAmount.add(sdTgtPurReturnOutAmountMap));
    			summaryMap.put("sdInterestTgtPurReturnOutAmount", sdInterestTgtAmount.add(sdInterestTgtPurReturnOutAmountMap));
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("purReturnOutNum", warehouseNum);
				summaryMap.put("purReturnOutAmount", warehouseAmount);
				summaryMap.put("sdTgtPurReturnOutAmount", sdTgtAmount);
				summaryMap.put("sdInterestTgtPurReturnOutAmount", sdInterestTgtAmount);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		    	    		 		
    		// 生成 财务经销存采购入库明细
    		BuFinanceWarehouseDetailsModel financeWarehouseDetailsModel=new BuFinanceWarehouseDetailsModel(); 
		    financeWarehouseDetailsModel.setBrandId(brandId);
    		financeWarehouseDetailsModel.setBrandName(brandName);
    		financeWarehouseDetailsModel.setMerchantId(merchant.getId());//商家id
    		financeWarehouseDetailsModel.setMerchantName(merchant.getName());//商家名称
    		financeWarehouseDetailsModel.setInDepotId(depotId);//出库仓库id
    		
    		financeWarehouseDetailsModel.setInDepotName(depotName);//退货出库仓库
    		financeWarehouseDetailsModel.setOrderId(orderId);//订单id
    		financeWarehouseDetailsModel.setOrderCode(orderCode);//订单编码
    		financeWarehouseDetailsModel.setOrderCreateDate(purReturnModel.getCreateDate());//订单日期 (订单创建日期)
    		financeWarehouseDetailsModel.setWarehouseId(outOrderId);//入库单id
    		financeWarehouseDetailsModel.setWarehouseCode(outOrderCode);//入库单编码
    		financeWarehouseDetailsModel.setSupplierId(supplierId);//供应商id
    		financeWarehouseDetailsModel.setSupplierName(supplierName);//供应商名称
    		financeWarehouseDetailsModel.setWarehouseCreateDate(returnOutDate);//入库时间 (入库单创建时间)
    		financeWarehouseDetailsModel.setPoNo(poNo);//PO号
    		//financeWarehouseDetailsModel.setDrawInvoiceDate(purchaseOrderModel.getDrawInvoiceDate());//开发票时间(发票上面的时间)
    		//financeWarehouseDetailsModel.setInvoiceNo(purchaseOrderModel.getInvoiceNo());//发票号
    		financeWarehouseDetailsModel.setGoodsId(goodsId);//商品id
    		financeWarehouseDetailsModel.setGoodsName(goodsName);//商品名称
    		financeWarehouseDetailsModel.setGoodsNo(goodsNo);//商品货号
    		financeWarehouseDetailsModel.setBarcode(barcode);//商品条码
    		financeWarehouseDetailsModel.setCommbarcode(commbarcode);// 标准条码
    		financeWarehouseDetailsModel.setSuperiorParentBrand(superiorParentBrand);
    		//financeWarehouseDetailsModel.setTallyingUnit(tallyingUnit);//理货单位(00-托盘，01-箱，02-件)
    		financeWarehouseDetailsModel.setOrderCurrency(orderCurrency);//采购币种 01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
    		financeWarehouseDetailsModel.setOrderPrice(orderPrice);//采购单价
    		financeWarehouseDetailsModel.setOrderAmount(orderAmount);//采购金额
    		financeWarehouseDetailsModel.setWarehouseNum(warehouseNum);//数量
    		financeWarehouseDetailsModel.setWarehouseCurrency(currency);//入库币种 01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑
    		financeWarehouseDetailsModel.setWarehousePrice(price);//入库单价(结算价格单价)
    		financeWarehouseDetailsModel.setWarehouseAmount(warehouseAmount);// 入库金额
    		
    		financeWarehouseDetailsModel.setMonth(month);// 归属月份 YYYY-MM
    		//financeWarehouseDetailsModel.setRelDate(relDate);// 上架时间
    		financeWarehouseDetailsModel.setCreater(purReturnModel.getCreater());
    		financeWarehouseDetailsModel.setCreateName(purReturnModel.getCreateName());
    		financeWarehouseDetailsModel.setOrderType(DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_2);//采购退货出库 		
    		financeWarehouseDetailsModel.setBuId(buId);
    		financeWarehouseDetailsModel.setBuName(buName);
    		financeWarehouseDetailsModel.setSdTgtAmount(sdTgtAmount);
    		financeWarehouseDetailsModel.setStockLocationTypeId(stockLocationTypeId);
    		financeWarehouseDetailsModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeWarehouseDetailsModel.setDifferencePrice(differencePrice);
    		financeWarehouseDetailsModel.setDifferenceAmount(differencePriceAmount);
    		orderList.add(financeWarehouseDetailsModel);
    		for (Map<String, Object> sdMap : sdOrderMapList) {
    			Long sdId=(Long) sdMap.get("id");
    			String sdCode=(String) sdMap.get("code");
    			String sdPoNo=(String) sdMap.get("po_no");
    			BigDecimal tgtPrice=(BigDecimal) sdMap.get("sd_tgt_price");
    			if (tgtPrice==null)tgtPrice=new BigDecimal(0);
    			String sdTypeName=(String) sdMap.get("sd_type_name");
    			String sdSimpleName=(String) sdMap.get("sd_simple_name");
    			
      			BigDecimal sdItemSdPrice=(BigDecimal) sdMap.get("sd_price");// 原币单价
    			if (sdItemSdPrice==null)sdItemSdPrice=new BigDecimal(0);
    			BigDecimal sdOrderAmount= sdItemSdPrice.multiply(new BigDecimal(warehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);// sd原币金额
    			String sdtgtCurrency=(String) sdMap.get("tgt_currency"); // sd本币币种
    			String sdCurrency=(String) sdMap.get("currency"); // sd 原币币种
    			// 封装采购入库SD明细实体
	    		BuFinanceSdWarehouseDetailsModel sdWarehouseDetails=new BuFinanceSdWarehouseDetailsModel();
	    		sdWarehouseDetails.setMerchantId(merchant.getId());
	    		sdWarehouseDetails.setMerchantName(merchant.getName());
	    		sdWarehouseDetails.setInDepotId(depotId);
	    		sdWarehouseDetails.setInDepotName(depotName);
	    		sdWarehouseDetails.setBuId(buId);
	    		sdWarehouseDetails.setBuName(buName);
	    		sdWarehouseDetails.setOrderId(orderId);
	    		sdWarehouseDetails.setOrderCode(orderCode);
	    		sdWarehouseDetails.setWarehouseCreateDate(returnOutDate);
	    		sdWarehouseDetails.setPoNo(sdPoNo);
	    		sdWarehouseDetails.setSupplierId(supplierId);
	    		sdWarehouseDetails.setSupplierName(supplierName);
	    		sdWarehouseDetails.setGoodsId(goodsId);
	    		sdWarehouseDetails.setGoodsName(goodsName);
	    		sdWarehouseDetails.setGoodsNo(goodsNo);
	    		sdWarehouseDetails.setWarehouseNum(warehouseNum);
				BigDecimal sdAmount= tgtPrice.multiply(new BigDecimal(warehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);;
	    		sdWarehouseDetails.setAmount(sdAmount);
	    		sdWarehouseDetails.setSuperiorParentBrand(superiorParentBrand);
	    		sdWarehouseDetails.setBrandId(brandId);
	    		sdWarehouseDetails.setBrandName(brandName);
	    		sdWarehouseDetails.setSdSimpleName(sdSimpleName);
	    		sdWarehouseDetails.setSdTypeName(sdTypeName);
	    		sdWarehouseDetails.setOrderType(DERP_REPORT.FINANCESDWAREHOUSEDETAILS_ORDERTYPE_2);
	    		sdWarehouseDetails.setMonth(month);
	    		sdWarehouseDetails.setSdOrderId(sdId);;
        		sdWarehouseDetails.setSdOrderCode(sdCode);
				sdWarehouseDetails.setWarehouseCurrency(sdtgtCurrency);// 本位币
        		sdWarehouseDetails.setOrderCurrency(sdCurrency);// 原币
        		sdWarehouseDetails.setOrderAmount(sdOrderAmount);// 原币金额
        		sdWarehouseDetails.setOrderPrice(sdItemSdPrice);// 原币单价
	    		orderSdList.add(sdWarehouseDetails);
    		}
    	}
    	
    	// 采购调整sd单
    	for (Map<String, Object> map : tzPurchaseSdOrdeMapList) {    		  		
			Long id = (Long) map.get("id");//sd单id
    		String code = (String) map.get("code");//sd code
    		String poNo = (String) map.get("po_no");//sd code
    		Timestamp inboundDate = (Timestamp) map.get("inbound_date");// 入库时间
    		Long supplierId = (Long) map.get("supplier_id");//供应商id
    		String supplierName = (String) map.get("supplier_name");//供应商名称
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		String depotName = (String) map.get("depot_name");//仓库id
    		//Long buId = (Long) map.get("bu_id");//事业部id    		
    		String orderCode = (String) map.get("purchase_code");//采购订单编码   		
    		Long goodsId = (Long) map.get("goods_id");//商品id    		
    		//String tallyingUnit = (String) map.get("tallying_unit");//理货单位
    		Integer num = (Integer) map.get("num");//数量
    		BigDecimal sdTgtAmount = (BigDecimal) map.get("sd_tgt_amount");//数量
    		String sdSimpleName = (String) map.get("sd_simple_name");
    		String sdTypeName = (String) map.get("sd_type_name");
    		Long creater = (Long) map.get("creater");
    		String createName = (String) map.get("create_name");
    		
			String sdtgtCurrency=(String) map.get("tgt_currency"); // sd本币币种
			String sdCurrency=(String) map.get("currency"); // sd 原币币种
    		if (num==null)num=0;
    		if (sdTgtAmount==null)sdTgtAmount=new BigDecimal(0);
    		
    		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
    		
    		
    		String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			} 
    		    		
    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		//Integer warehouseNum = changeUnit(tallyingUnit,num,boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		
    		// 根据采购订单id查询采购订单 // 如果采购入库单有商品,采购订单没有此商品 此时 查询不到对应的采购订单
    		PurchaseOrderModel purchaseOrderModel=new PurchaseOrderModel();
    		if (!StringUtils.isEmpty(orderCode)) {
    			purchaseOrderModel.setCode(orderCode);
    			List<PurchaseOrderModel> purchaseOrderList = purchaseOrderDao.list(purchaseOrderModel);
    			if (purchaseOrderList.size()>0) {
    				purchaseOrderModel=purchaseOrderList.get(0);
				}
			}
    		if (purchaseOrderModel==null)purchaseOrderModel=new PurchaseOrderModel();
    		    			
    			
    		if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算
    			sdTypeName=sdTypeName.trim();
    			if ("INT".equals(sdTypeName)) {//sd利息金额//"INT"("融资利息"）				
    				getWeightedMap(buId,commbarcode,weightedSummaryMap,num.intValue(),sdTgtAmount,"purchaseTzSdOrdeInterestAmount","purchaseTzSdOrdeInterestNum");
    			}else {//sd 金额
    				getWeightedMap(buId,commbarcode,weightedSummaryMap,num.intValue(),sdTgtAmount,"purchaseTzSdOrdeAmount","purchaseTzSdOrdeNum");
    			}	

			}
   		
    		
    		String deBuKey=depotId+","+buId+","+goodsId;
    		
    		sdTypeName=sdTypeName.trim();			    		
    		if (saveSummaryMap.containsKey(deBuKey)) {
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer purchaseTzSdOrdeNumMap = (Integer) summaryMap.get("purchaseTzSdOrdeNum");
    			BigDecimal purchaseTzSdOrdeAmountMap =(BigDecimal) summaryMap.get("purchaseTzSdOrdeAmount");
    			Integer purchaseTzSdOrdeInterestNumMap = (Integer) summaryMap.get("purchaseTzSdOrdeInterestNum");
    			BigDecimal purchaseTzSdOrdeInterestAmountMap =(BigDecimal) summaryMap.get("purchaseTzSdOrdeInterestAmount");
    			
    			if (purchaseTzSdOrdeNumMap==null)purchaseTzSdOrdeNumMap=0;
    			if (purchaseTzSdOrdeAmountMap==null)purchaseTzSdOrdeAmountMap=new BigDecimal(0);
    			if (purchaseTzSdOrdeInterestNumMap==null)purchaseTzSdOrdeInterestNumMap=0;
    			if (purchaseTzSdOrdeInterestAmountMap==null)purchaseTzSdOrdeInterestAmountMap=new BigDecimal(0); 

    			if ("INT".equals(sdTypeName)) {
        			summaryMap.put("purchaseTzSdOrdeInterestNum", num+purchaseTzSdOrdeInterestNumMap);
        			summaryMap.put("purchaseTzSdOrdeInterestAmount", sdTgtAmount.add(purchaseTzSdOrdeInterestAmountMap));
				}else {
					summaryMap.put("purchaseTzSdOrdeNum", num+purchaseTzSdOrdeNumMap);
	    			summaryMap.put("purchaseTzSdOrdeAmount", sdTgtAmount.add(purchaseTzSdOrdeAmountMap));
				}
    			   			
    			saveSummaryMap.put(deBuKey, summaryMap);
    					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();
				if ("INT".equals(sdTypeName)) {
					summaryMap.put("purchaseTzSdOrdeInterestNum", num);
					summaryMap.put("purchaseTzSdOrdeInterestAmount", sdTgtAmount);
				}else {
					summaryMap.put("purchaseTzSdOrdeNum", num);
					summaryMap.put("purchaseTzSdOrdeAmount", sdTgtAmount);
				}

				saveSummaryMap.put(deBuKey, summaryMap);
			}


    		
    		
    
    		BuFinanceSdWarehouseDetailsModel sdWarehouseDetails=new BuFinanceSdWarehouseDetailsModel();
    		sdWarehouseDetails.setMerchantId(merchant.getId());
    		sdWarehouseDetails.setMerchantName(merchant.getName());
    		sdWarehouseDetails.setInDepotId(depotId);
    		sdWarehouseDetails.setInDepotName(depotName);
    		sdWarehouseDetails.setBuId(buId);    
    		sdWarehouseDetails.setBuName(buName);
    		sdWarehouseDetails.setOrderId(purchaseOrderModel.getId());
    		sdWarehouseDetails.setOrderCode(orderCode);
    		sdWarehouseDetails.setWarehouseCreateDate(inboundDate);
    		sdWarehouseDetails.setPoNo(poNo);
    		sdWarehouseDetails.setSupplierId(supplierId);
    		sdWarehouseDetails.setSupplierName(supplierName);
    		sdWarehouseDetails.setGoodsId(goodsId);
    		sdWarehouseDetails.setGoodsName(goodsName);
    		sdWarehouseDetails.setGoodsNo(goodsNo);
    		sdWarehouseDetails.setWarehouseNum(num);
    		sdWarehouseDetails.setAmount(sdTgtAmount);
    		sdWarehouseDetails.setSuperiorParentBrand(superiorParentBrand);
    		sdWarehouseDetails.setBrandId(brandId);
    		sdWarehouseDetails.setBrandName(brandName);
    		sdWarehouseDetails.setSdSimpleName(sdSimpleName);
    		sdWarehouseDetails.setSdTypeName(sdTypeName);
    		sdWarehouseDetails.setOrderType(DERP_REPORT.FINANCESDWAREHOUSEDETAILS_ORDERTYPE_3);
    		sdWarehouseDetails.setMonth(month);
    		sdWarehouseDetails.setSdOrderId(id);;
    		sdWarehouseDetails.setSdOrderCode(code);
    		//sdWarehouseDetails.setOrderAmount(sdOrderAmount);// 原币金额
    		//sdWarehouseDetails.setOrderPrice(sdItemPrice);// 原币单价
    		orderSdList.add(sdWarehouseDetails);

    		
    	}
    	
    	// 循环生成
    	for (BuFinanceWarehouseDetailsModel financeWarehouseDetailsModel : orderList) {
    		// 生成采购入库明细
    		buFinanceWarehouseDetailsDao.save(financeWarehouseDetailsModel);
		}
    	orderList=null;
    	//循环生成 sd采购入库明细
    	for (BuFinanceSdWarehouseDetailsModel financeSdWarehouseDetailsModel : orderSdList) {
    		buFinanceSdWarehouseDetailsDao.save(financeSdWarehouseDetailsModel);
		}
    	orderSdList=null;
		
	}

	/**
	 * 获取差异单价
	 * @param merchant
	 * @param orderId
	 * @param currency
	 * @param tgtCurrency
	 * @param month
	 * @param orderChangePrice
	 * @param price
	 * @return
	 */
	private BigDecimal getDifferencePrice(MerchantInfoModel merchant,Long orderId,String purchaseCurrency, String currency, String month,
			BigDecimal orderChangePrice,
			BigDecimal price) throws SQLException {			
		BigDecimal differencePrice=new BigDecimal(0);
		if (!"0000134".equals(merchant.getTopidealCode())) {
			return differencePrice;
		}
		if (orderChangePrice==null) orderChangePrice=new BigDecimal(0);
		if (price==null) price=new BigDecimal(0);
		//采购币种和目标币种相同 汇率为1
		if (purchaseCurrency.equals(currency)) {
			differencePrice=orderChangePrice.subtract(price);
			return differencePrice;
		}		
		
		Timestamp now = TimeUtils.getNow();
		//获取当前月份最后一天数据
		String lastDayOfMonth = TimeUtils.getLastDayOfMonth(month);
		Timestamp attributionDate=null;
		// 当前时间大于  本月最后一天  查询 当前时间汇率  否则查询最后一天汇率
		if (now.getTime()>Timestamp.valueOf(lastDayOfMonth+" 23:59:59").getTime()) {
			attributionDate=Timestamp.valueOf(lastDayOfMonth+" 23:59:59");
		}else {
			attributionDate=now;
		}
		
		// 币种兑换
		ExchangeRateModel exchangeRate=new ExchangeRateModel();
		exchangeRate.setOrigCurrencyCode(purchaseCurrency);
		exchangeRate.setTgtCurrencyCode(currency);
		exchangeRate.setEffectiveDate(attributionDate);
		List<ExchangeRateModel> recentRateList = exchangeRateDao.getRecentRateList(exchangeRate);
		if (recentRateList==null||recentRateList.size()==0) {
			logger.error("采购入库明细:"+merchant.getName()+"订单id"+orderId+"采购币种"+purchaseCurrency+"入库币种"+currency+"没有查询到对应的汇率");
			throw new RuntimeException("采购入库明细:"+merchant.getName()+"订单id"+orderId+"采购币种"+purchaseCurrency+"入库币种"+currency+"没有查询到对应的汇率");
		}
		exchangeRate = recentRateList.get(0);
		Double rate = exchangeRate.getRate();
		if (rate==null) rate=0.0;
		BigDecimal tgtPrice=orderChangePrice.multiply(new BigDecimal(rate.toString()));
		differencePrice=tgtPrice.subtract(price);
		return differencePrice;
	}

	/**
	 * 1-采购SD，2-采购退SD，3-调整SD
	 * 获取sd单价和金额 获取lp单价和金额
	 * @param orderId
	 * @param itemId 采购退货表体id 或者采购订单表体id
	 * @param warehouseNum
	 * @return
	 * @throws SQLException 
	 */
	private Map<String, Object> getLpAndSdAmount(Long orderId, Integer warehouseNum,String type,Long itemId) throws SQLException {
		String code=null;
		if ("1".equals(type)) {
			PurchaseOrderModel order = purchaseOrderDao.searchById(orderId);	
			code=order.getCode();
		}else if("2".equals(type)){// 目前采购退货没有生产sd单 此代码注释
			//PurchaseReturnOrderModel order = purchaseReturnOrderDao.searchById(orderId);
			//code=order.getCode();
		}
		List<Map<String,Object>> sdOrderMapList=null;
		if (!StringUtils.isEmpty(code)&&itemId!=null) {
			Map<String, Object>sdMap=new HashMap<>();
			sdMap.put("type", type);
			sdMap.put("purchaseCode", code);
			sdMap.put("purchaseItemId", itemId);
			sdOrderMapList = purchaseSdOrderDao.getSdOrderItemList(sdMap);	
		}		
		if (sdOrderMapList==null)sdOrderMapList=new ArrayList<>();
		// sd 采购/采购退  金额 (过滤掉"INT"("融资利息"）)		
		BigDecimal sdTgtAmount=new BigDecimal(0);
		//// sd 采购/采购退  金额 ("INT"("融资利息"）)	
		BigDecimal sdInterestTgtAmount=new BigDecimal(0);
		for (Map<String, Object> map : sdOrderMapList) {
			BigDecimal sdTgtPrice = (BigDecimal) map.get("sd_tgt_price");			
			String sdTypeName  = (String) map.get("sd_type_name");//SD类型
			if (sdTgtPrice==null)sdTgtPrice=new BigDecimal(0);
			BigDecimal amount = sdTgtPrice.multiply(new BigDecimal(warehouseNum)).setScale(2,BigDecimal.ROUND_HALF_UP);		

			sdTypeName=sdTypeName.trim();
			if ("INT".equals(sdTypeName)) {//sd利息金额//"INT"("融资利息"）				
				sdInterestTgtAmount=sdInterestTgtAmount.add(amount);
			}else {//sd 金额
				sdTgtAmount=sdTgtAmount.add(amount);
			}			
		}	
		Map<String, Object>lpAndSdMap=new HashMap<>();		
		lpAndSdMap.put("sdInterestTgtAmount", sdInterestTgtAmount);// sd 利息金额
		lpAndSdMap.put("sdTgtAmount", sdTgtAmount);//sd金额
		lpAndSdMap.put("sdOrderMapList", sdOrderMapList);	
		return lpAndSdMap;
	}

	/**
	 * 获取加权单价 相关信息
	 * @param merchant
	 * @param buId
	 * @param commbarcode
	 * @param currencyMap
	 * @param orderCurrency
	 * @param weightedSummaryMap
	 * @param orderChangeAmount
	 * @throws SQLException 
	 */
	private void getWeightedMap( Long buId, String commbarcode,
			Map<String, Map<String, Object>> weightedSummaryMap,
			Integer num,
			BigDecimal amount,
			String amountName,String numName) throws SQLException {	 	
		if (num==null)num=0;
		if (amount==null)amount=new BigDecimal(0);
		//存储计算加权期初金额和期初数量			
		String weightedKey=buId+","+commbarcode;
		if (weightedSummaryMap.containsKey(weightedKey)) {
			Map<String, Object>weightedMap=weightedSummaryMap.get(weightedKey);
			BigDecimal warehouseAmountMap = (BigDecimal) weightedMap.get(amountName);
			Integer warehouseNumMap = (Integer) weightedMap.get(numName);
			if (warehouseAmountMap==null) warehouseAmountMap=new BigDecimal(0);
			if (warehouseNumMap==null) warehouseNumMap=0;
			BigDecimal warehouseAmountAdd = warehouseAmountMap.add(amount);
			weightedMap.put(amountName,warehouseAmountAdd);//期末金额
			weightedMap.put(numName, num+warehouseNumMap);//期末数量
			weightedSummaryMap.put(weightedKey, weightedMap);				
		}else {
			Map<String, Object>weightedMap=new HashMap<>();
			weightedMap.put(amountName,amount);//期末金额
			weightedMap.put(numName, num);//期末数量
			weightedSummaryMap.put(weightedKey, weightedMap);	
		}
 
	}
	
	/**
	 * 获取标准单价相关信息
	 * @param depotId
	 * @param buId
	 * @param goodsId
	 * @param barcode
	 * @param deBuLastSummaryMap
	 * @param buLastSummaryMap
	 * @param settlementPriceMap
	 * @param num
	 * @return
	 */
	private Map<String,Object>  getCommbarcodeMap(Long depotId, Long buId, Long goodsId, String barcode,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object>> settlementPriceMap,
			BigDecimal num) {
		// 商家 仓库 事业部
		String deBuKey=depotId+","+buId+","+goodsId;
		// 存储所有 事业部 单价和币种
		String buKey=buId+","+goodsId;
		String barcodeKey=buId+","+barcode;		
		BigDecimal price=null;
		String currency=null;
		Map<String, Object> deBuMap = deBuLastSummaryMap.get(deBuKey);
		if (deBuMap!=null) {
			price = (BigDecimal) deBuMap.get("tzPrice");
			currency = (String) deBuMap.get("currency");
		}
		if (price==null) {
			Map<String, Object> buMap = buLastSummaryMap.get(buKey);
			if (buMap!=null) {
				price = (BigDecimal) buMap.get("tzPrice");
				currency = (String) buMap.get("currency");
			}			
		}
		if (price==null) {
			Map<String, Object> priceMap = settlementPriceMap.get(barcodeKey);
			if (priceMap!=null) {
				price = (BigDecimal) priceMap.get("tzPrice");
				currency = (String) priceMap.get("currency");
			}
			
		}
		BigDecimal amount =null;
		if (price!=null) {
			amount = price.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);
		}
		Map<String,Object> commbarcodeMap=new HashMap<>();
		commbarcodeMap.put("price", price);
		commbarcodeMap.put("currency", currency);
		commbarcodeMap.put("amount", amount);
		
		return commbarcodeMap;
		
		
	}

	/**
	 * 计算汇率转化金额
	 * @param merchant
	 * @param orderId
	 * @param purchaseCurrency
	 * @param currency
	 * @param attributionDate
	 * @param orderChangeAmount
	 * @return
	 * @throws SQLException
	 */
	private BigDecimal getRateAmount(MerchantInfoModel merchant,Long orderId,String purchaseCurrency, String currency, String month,
			BigDecimal orderChangeAmount) throws SQLException {
		//采购币种和目标币种相同 汇率为1
		if (purchaseCurrency.equals(currency)) {
			return orderChangeAmount;
		}		
		
		Timestamp now = TimeUtils.getNow();
		//获取当前月份最后一天数据
		String lastDayOfMonth = TimeUtils.getLastDayOfMonth(month);
		Timestamp attributionDate=null;
		// 当前时间大于  本月最后一天  查询 当前时间汇率  否则查询最后一天汇率
		if (now.getTime()>Timestamp.valueOf(lastDayOfMonth+" 23:59:59").getTime()) {
			attributionDate=Timestamp.valueOf(lastDayOfMonth+" 23:59:59");
		}else {
			attributionDate=now;
		}
		
		// 币种兑换
		ExchangeRateModel exchangeRate=new ExchangeRateModel();
		exchangeRate.setOrigCurrencyCode(purchaseCurrency);
		exchangeRate.setTgtCurrencyCode(currency);
		exchangeRate.setEffectiveDate(attributionDate);
		List<ExchangeRateModel> recentRateList = exchangeRateDao.getRecentRateList(exchangeRate);
		if (recentRateList==null||recentRateList.size()==0) {
			logger.error("采购入库明细:"+merchant.getName()+"订单id"+orderId+"采购币种"+purchaseCurrency+"入库币种"+currency+"没有查询到对应的汇率");
			throw new RuntimeException("采购入库明细:"+merchant.getName()+"订单id"+orderId+"采购币种"+purchaseCurrency+"入库币种"+currency+"没有查询到对应的汇率");
		}
		exchangeRate = recentRateList.get(0);
		Double rate = exchangeRate.getRate();
		if (rate==null) rate=0.0;
		BigDecimal warehouseAmount=orderChangeAmount.multiply(new BigDecimal(rate.toString()));
		return warehouseAmount;
	}
    
	
    /**
     * 生成(财务经销存)累计采购在途明细表
     * @param merchant
     * @param depot
     * @param month
     * @param AddPurchaseNotshelfSummaryList
     * @throws Exception
     */
    public void saveFinanceAddPurchaseNotshelfDetailsWeighte(MerchantBuRelModel buModel,
    		Map<String, Map<String, Object>> saveSummaryMap,
    		MerchantInfoModel merchant,String month,
    		List<Map<String, Object>> addPurchaseNotshelfDetailsList,
    		Map<Long,DepotInfoModel> depotModelMap,
			Map<Long, Map<String, Object>> allMerchandiseMap,
			Map<String,  Map<String, Object>>weightedSummaryMap,
			Map<String, String>currencyMap,
			Map<String, Object> groupCommbarcodeMap,String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap)throws Exception{
    	List<BuFinanceAddPurchaseNotshelfDetailsModel> orderList=new ArrayList<>();
		List<BuFinanceSdAddPurchaseNotshelfModel> orderSdList=new ArrayList<>();
		Long buId =buModel.getBuId();
		String buName = buModel.getBuName();
		// 获取中转仓仓库编码 // 采购在途默认 写死中转仓
		DepotInfoModel depotInfo=new  DepotInfoModel();
		depotInfo.setCode("ZT001");
		depotInfo = depotInfoDao.searchByModel(depotInfo);
    	for (Map<String, Object> map : addPurchaseNotshelfDetailsList) {
    		//Long depotId = (Long) map.get("depot_id");//仓库id
    		//Long buId = (Long) map.get("bu_id");//事业部id
    		Long orderId = (Long) map.get("id");//订单id
    		BigDecimal num = (BigDecimal) map.get("num");//数量		
    		BigDecimal orderPrice = (BigDecimal) map.get("price");//单价	
    		Long goodsId = (Long) map.get("goods_id");//商品id    	  
    		Long purchaseItemId = (Long) map.get("purchase_item_id");//商品id  
    		String warehouseIds = (String) map.get("warehouseIds");//入库单id 
    		String warehouseCodes = (String) map.get("warehouseCodes");//入库单号
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    	
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}
    		
    		// 根据采购订单id查询采购订单
    		PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(orderId);
    		if (purchaseOrderModel==null)purchaseOrderModel=new PurchaseOrderModel();

    		//箱托转化单位 //数量    
    		Integer changeNum=null;
    		if (num!=null) {
    			changeNum = changeUnit(tallyingUnit,num,boxToUnit,torrToUnit,merchant.getName(),goodsNo);
			}
    		if (changeNum==null)changeNum=0;

	    	 BigDecimal orderChangeAmount =null;
	    	 BigDecimal orderChangePrice =null;
	    	 if (orderPrice!=null&&changeNum!=null&&changeNum!=0&&num!=null) {
	    		 orderChangeAmount=orderPrice.multiply(num);
	    		 orderChangePrice= orderPrice.multiply(num).divide(new BigDecimal(changeNum),8,BigDecimal.ROUND_HALF_UP);
			 }
	    	     	
	    	 Timestamp drawInvoiceDate = purchaseOrderModel.getDrawInvoiceDate();
			BigDecimal price=null; 	
	    	BigDecimal warehouseAmount=null; 
	    	String currency=null; 
	    	List<Map<String,Object>> sdOrderMapList= new ArrayList<>();	
			BigDecimal sdTgtAmount = new BigDecimal(0);// sd 本币金额
	   		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
	   			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotInfo.getId(),buId,goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(changeNum));
	   			price = (BigDecimal) commbarcodeMap.get("price");
	   			currency = (String) commbarcodeMap.get("currency");
	   			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算
		    			    			    	
		    	String purchaseCurrency = purchaseOrderModel.getCurrency();
				String tgtCurrency =purchaseOrderModel.getTgtCurrency();
		    	BigDecimal itemPrice=orderPrice;
		    	if (itemPrice==null)itemPrice=new BigDecimal(0);		    	
		    	if (num!=null&&changeNum!=null&&changeNum!=0) {
		    		if (orderChangeAmount==null)orderChangeAmount =new BigDecimal(0);
		    		warehouseAmount = getRateAmount(merchant, orderId, purchaseCurrency, tgtCurrency, month, orderChangeAmount);
		    		warehouseAmount=warehouseAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		    		price = warehouseAmount.divide(new BigDecimal(changeNum),8,BigDecimal.ROUND_HALF_UP);	
				}		    	
	    		 orderChangeAmount=orderChangeAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		    	/*if (drawInvoiceDate!=null&&TimeUtils.format(drawInvoiceDate, "yyyy-MM").equals(month)) {
					getWeightedMap(buId,commbarcode,weightedSummaryMap,changeNum,warehouseAmount,"purchaseNotshelfAmount","purchaseNotshelfNum");
				}*/
	    		currency = tgtCurrency;	
		    	// 获取 本期采购LP金额 和本期采购SD金额
    			if (orderId!=null&&goodsId!=null) {
					Map<String, Object>sdMap= getLpAndSdAmount(orderId,changeNum,"1",purchaseItemId);
					sdOrderMapList = (List<Map<String, Object>>) sdMap.get("sdOrderMapList");
		    		sdTgtAmount = (BigDecimal) sdMap.get("sdTgtAmount");	
				}
		    	
		    	
			}	
    		
    		if (warehouseAmount==null)warehouseAmount=new BigDecimal(0);
    		
    		String deBuKey=depotInfo.getId()+","+buId+","+goodsId;
    		if (drawInvoiceDate!=null&&TimeUtils.format(drawInvoiceDate, "yyyy-MM").equals(month)) {// 本期采购在途    			
        		if (saveSummaryMap.containsKey(deBuKey)) {
        			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
        			Integer purchaseNotshelfNumMap = (Integer) summaryMap.get("purchaseNotshelfNum");       			      			        			
        			BigDecimal sdTgtpurchaseNotshelfAmountMap =(BigDecimal) summaryMap.get("sdTgtpurchaseNotshelfAmount");
        			if (purchaseNotshelfNumMap==null)purchaseNotshelfNumMap=0;
        			if (sdTgtpurchaseNotshelfAmountMap==null)sdTgtpurchaseNotshelfAmountMap=new BigDecimal(0);        			
        			summaryMap.put("purchaseNotshelfNum", changeNum+purchaseNotshelfNumMap);  
        			summaryMap.put("sdTgtpurchaseNotshelfAmount", sdTgtAmount.add(sdTgtpurchaseNotshelfAmountMap));        			
        			saveSummaryMap.put(deBuKey, summaryMap);        					
    			}else {
    				Map<String, Object> summaryMap = new HashMap<>();    				
    				summaryMap.put("purchaseNotshelfNum", changeNum);
    				summaryMap.put("sdTgtpurchaseNotshelfAmount", sdTgtAmount);
    				saveSummaryMap.put(deBuKey, summaryMap);
    			}
    		}
    		// 累计采购在途
    		if (saveSummaryMap.containsKey(deBuKey)) {
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer purchaseNotshelfNumMap = (Integer) summaryMap.get("addPurchaseNotshelfNum");
    			BigDecimal addPurchaseNotshelfAmountMap = (BigDecimal) summaryMap.get("addPurchaseNotshelfAmount");
    			BigDecimal sdTgtaddPurchaseNotshelfAmountMap =(BigDecimal) summaryMap.get("sdTgtaddPurchaseNotshelfAmount");

    			   			
    			if (purchaseNotshelfNumMap==null)purchaseNotshelfNumMap=0;
    			if (addPurchaseNotshelfAmountMap==null)addPurchaseNotshelfAmountMap=new BigDecimal(0);  
    			if (sdTgtaddPurchaseNotshelfAmountMap==null)sdTgtaddPurchaseNotshelfAmountMap=new BigDecimal(0);      
    			summaryMap.put("addPurchaseNotshelfNum", changeNum+purchaseNotshelfNumMap);
    			summaryMap.put("addPurchaseNotshelfAmount", warehouseAmount.add(addPurchaseNotshelfAmountMap));
    			summaryMap.put("sdTgtaddPurchaseNotshelfAmount", sdTgtAmount.add(sdTgtaddPurchaseNotshelfAmountMap)); 
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("addPurchaseNotshelfNum", changeNum);
				summaryMap.put("addPurchaseNotshelfAmount", warehouseAmount);
				summaryMap.put("sdTgtaddPurchaseNotshelfAmount", sdTgtAmount);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		
    		
			BuFinanceAddPurchaseNotshelfDetailsModel addPurModel=new BuFinanceAddPurchaseNotshelfDetailsModel();
			addPurModel.setBrandId(brandId);
			addPurModel.setBrandName(brandName);
    		addPurModel.setMerchantId(merchant.getId());
    		addPurModel.setMerchantName(merchant.getName());
    		addPurModel.setDepotId(depotInfo.getId());
    		addPurModel.setDepotName(depotInfo.getName());
    		addPurModel.setOrderId(orderId);
    		addPurModel.setOrderCode(purchaseOrderModel.getCode());
    		addPurModel.setOrderCreateDate(purchaseOrderModel.getCreateDate());
    		addPurModel.setSupplierId(purchaseOrderModel.getSupplierId());
    		addPurModel.setSupplierName(purchaseOrderModel.getSupplierName());
    		addPurModel.setEndDate(purchaseOrderModel.getEndDate());
    		addPurModel.setInvoiceNo(purchaseOrderModel.getInvoiceNo());
    		addPurModel.setWarehouseIds(warehouseIds);
    		addPurModel.setWarehouseCodes(warehouseCodes);
    		addPurModel.setPoNo(purchaseOrderModel.getPoNo());
    		addPurModel.setDrawInvoiceDate(purchaseOrderModel.getCargoCuttingDate());// 现在这个字段取 在途开始日期了 (以前的财务经销存不能刷新了)
    		addPurModel.setGoodsId(goodsId);
    		addPurModel.setGoodsName(goodsName);
    		addPurModel.setGoodsNo(goodsNo);
    		addPurModel.setBarcode(barcode);
    		addPurModel.setCommbarcode(commbarcode);
    		addPurModel.setSuperiorParentBrand(superiorParentBrand);
    		//addPurModel.setTallyingUnit(tallyingUnit);
    		addPurModel.setNum(changeNum);
    		addPurModel.setOrderCurrency(purchaseOrderModel.getCurrency());
    		addPurModel.setOrderPrice(orderChangePrice);
			addPurModel.setOrderAmount(orderChangeAmount);	
    		addPurModel.setWarehouseCurrency(currency);
    		addPurModel.setWarehousePrice(price);
    		addPurModel.setWarehouseAmount(warehouseAmount);    		
    		addPurModel.setMonth(month);
    		addPurModel.setBuId(buId);
    		addPurModel.setBuName(buName);
    		addPurModel.setSdTgtAmount(sdTgtAmount);	
    		orderList.add(addPurModel);   
    		for (Map<String, Object> sdMap : sdOrderMapList) {   			
    			Long sdId=(Long) sdMap.get("id");
    			String sdCode=(String) sdMap.get("code");
    			String poNo=(String) sdMap.get("po_no");
    			BigDecimal tgtPrice=(BigDecimal) sdMap.get("sd_tgt_price");
				if (tgtPrice==null)tgtPrice=new BigDecimal(0);
    			String sdTypeName=(String) sdMap.get("sd_type_name");
    			String sdSimpleName=(String) sdMap.get("sd_simple_name"); 
    			
    			BuFinanceSdAddPurchaseNotshelfModel sdWarehouseDetails=new BuFinanceSdAddPurchaseNotshelfModel();
       			sdWarehouseDetails.setMerchantId(merchant.getId());
	    		sdWarehouseDetails.setMerchantName(merchant.getName());
	    		sdWarehouseDetails.setInDepotId(depotInfo.getId());
	    		sdWarehouseDetails.setInDepotName(depotInfo.getName());
	    		sdWarehouseDetails.setBuId(buId);
	    		sdWarehouseDetails.setBuName(buName);
	    		sdWarehouseDetails.setOrderId(orderId);
	    		sdWarehouseDetails.setOrderCode(purchaseOrderModel.getCode());
	    		sdWarehouseDetails.setWarehouseCreateDate(purchaseOrderModel.getCargoCuttingDate());
	    		sdWarehouseDetails.setPoNo(purchaseOrderModel.getPoNo());
	    		sdWarehouseDetails.setSupplierId(purchaseOrderModel.getSupplierId());
	    		sdWarehouseDetails.setSupplierName(purchaseOrderModel.getSupplierName());
	    		sdWarehouseDetails.setGoodsId(goodsId);
	    		sdWarehouseDetails.setGoodsName(goodsName);
	    		sdWarehouseDetails.setGoodsNo(goodsNo);
	    		sdWarehouseDetails.setWarehouseNum(changeNum);
				BigDecimal sdAmount= tgtPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);;
	    		sdWarehouseDetails.setAmount(sdAmount);
	    		sdWarehouseDetails.setSuperiorParentBrand(superiorParentBrand);
	    		sdWarehouseDetails.setBrandId(brandId);
	    		sdWarehouseDetails.setBrandName(brandName);
				sdWarehouseDetails.setSdSimpleName(sdSimpleName);
	    		sdWarehouseDetails.setSdTypeName(sdTypeName);
	    		sdWarehouseDetails.setMonth(month);
				sdWarehouseDetails.setSdOrderId(sdId);;
        		sdWarehouseDetails.setSdOrderCode(sdCode);
	    		orderSdList.add(sdWarehouseDetails);
    		}

    		
		}
    	
    	// 循环生成 累计采购在途明细表
    	for (BuFinanceAddPurchaseNotshelfDetailsModel model : orderList) {    		
    		buFinanceAddPurchaseNotshelfDetailsDao.save(model);
		}
    	orderList=null;
    	// 循环生成 累计采购在途sd明细表
		for (BuFinanceSdAddPurchaseNotshelfModel financeSdAddPurchaseNotshelfModel : orderSdList) {
			buFinanceSdAddPurchaseNotshelfDao.save(financeSdAddPurchaseNotshelfModel);
		}
		orderSdList=null;
    }
    
    
    /**
     *生成(事业部财务经销存)事业部移库单入加权
     * @param merchant
     * @param depot
     * @param month
     * @param addSaleNoshelfDetailsGXList
     * @param addSaleNoshelfDetailsDXList
     * @throws Exception
     */
    public void saveBuFinanceMoveDetailsWeighted(MerchantBuRelModel buModel,Map<String, Map<String, Object>> saveSummaryMap,
    		MerchantInfoModel merchant,
    		String month,
    		List<Map<String, Object>> buFinanceMoveList,
    		Map<Long,DepotInfoModel> depotModelMap,Map<Long,BusinessUnitModel> buModelMap,
			Map<Long, Map<String, Object>> allMerchandiseMap,
			Map<String,  Map<String, Object>>weightedSummaryMap,
			Map<String, String>currencyMap,
			Map<String, Object> groupCommbarcodeMap,String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap)throws Exception{
    	List<BuFinanceMoveDetailsModel> orderList= new ArrayList<>();
    	Long buId =buModel.getBuId();
		String buName = buModel.getBuName();
    	// 移库单
    	for (Map<String, Object> map : buFinanceMoveList) {
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		//Long buId = (Long) map.get("bu_id");//事业部id
    		Long orderId = (Long) map.get("id");//订单id
    		String orderCode = (String) map.get("code");//订单号
    		String businessNo = (String) map.get("business_no");//外部单号
    		Timestamp moveDate = (Timestamp) map.get("move_date");//移库时间
    		Timestamp createDate = (Timestamp) map.get("create_date");//创建时间
    		Integer num = (Integer) map.get("num");//数量
    		BigDecimal tgtPrice = (BigDecimal) map.get("price");//移库金额	
			BigDecimal agreementPrice = (BigDecimal) map.get("agreement_price");
    		String agreementCurrency = (String) map.get("agreement_currency");
    		if (tgtPrice==null) tgtPrice=new BigDecimal(0);
    		String tgtCurrency = (String) map.get("currency");//订单币种
    		String tallyingUnit = (String) map.get("tallying_unit");

    		
    		Long goodsId = (Long) map.get("goods_id");//商品id  
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);

			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}

    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		if (num==null)num=0;
    		Integer changeNum = changeUnit(tallyingUnit,new BigDecimal(num),boxToUnit,torrToUnit,merchant.getName(),goodsNo);   			   		   		   		
    		if (changeNum==null) changeNum=0;
    		BigDecimal agreementAmount=null;
    		if (agreementPrice!=null) agreementAmount=agreementPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);

    		
    		
    		BigDecimal price=null; 	
	    	BigDecimal warehouseAmount=null; 
	    	String currency=null; 
	   		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
	   			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buId,goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(changeNum));
	   			price = (BigDecimal) commbarcodeMap.get("price");
	   			currency = (String) commbarcodeMap.get("currency");
	   			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算
	    		if (changeNum!=null) {
	    			warehouseAmount=tgtPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);;
	    			price = tgtPrice.setScale(2,BigDecimal.ROUND_HALF_UP);	
				}
				getWeightedMap(buId,commbarcode,weightedSummaryMap,changeNum,warehouseAmount,"moveInAmount","moveInNum");
    			currency = tgtCurrency;	
			}
    		
    		if (warehouseAmount==null) warehouseAmount=new BigDecimal(0);
    		String deBuKey=depotId+","+buId+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer moveInNumMap = (Integer) summaryMap.get("moveInNum");
    			if (moveInNumMap==null)moveInNumMap=0;
    			BigDecimal moveInAmountMap = (BigDecimal) summaryMap.get("moveInAmount");
    			if (moveInAmountMap==null) moveInAmountMap=new BigDecimal(0);
    			summaryMap.put("moveInNum", changeNum+moveInNumMap);
    			summaryMap.put("moveInAmount", warehouseAmount.add(moveInAmountMap));
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("moveInNum", changeNum);
				summaryMap.put("moveInAmount", warehouseAmount);
				saveSummaryMap.put(deBuKey, summaryMap);
			}	
    		// 财务经分销 销售减少在途
    		BuFinanceMoveDetailsModel moveModel =new BuFinanceMoveDetailsModel();
    		moveModel.setBrandId(brandId);
    		moveModel.setBrandName(brandName);
    		moveModel.setMerchantId(merchant.getId());
    		moveModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		moveModel.setDepotId(depot.getId());
    		moveModel.setDepotName(depot.getName());
    		moveModel.setBuId(buId);
    		moveModel.setBuName(buName);
    		moveModel.setOrderId(orderId);
    		moveModel.setOrderCode(orderCode);
    		moveModel.setExternalCode(businessNo);
    		moveModel.setOrderType("1");//'1-移入 2-移除 ',
    		moveModel.setDeliverDate(moveDate);
    		moveModel.setMoveCreateDate(createDate);
    		moveModel.setGoodsId(goodsId);
    		moveModel.setGoodsNo(goodsNo);
    		moveModel.setGoodsName(goodsName);
    		moveModel.setBarcode(barcode);
    		moveModel.setCommbarcode(commbarcode);
    		moveModel.setSuperiorParentBrand(superiorParentBrand);
    		moveModel.setNum(changeNum.intValue());
    		moveModel.setMonth(month);
    		moveModel.setPrice(price);
    		moveModel.setAmount(warehouseAmount);
    		moveModel.setCurrency(currency);
    		moveModel.setAgreementCurrency(agreementCurrency);
    		moveModel.setAgreementPrice(agreementPrice);
    		moveModel.setAgreementAmount(agreementAmount);
    		orderList.add(moveModel);		
		}
    	
    	// 循环保存 (事业部财务经分销 本期移库
    	for (BuFinanceMoveDetailsModel model : orderList) {
    		buFinanceMoveDetailsDao.save(model);
		} 
    	orderList=null;
    }
    
    



	/**
	 * 合并商家id和关联商家id
	 * 
	 * @param relList
	 * @param merchant
	 * @return
	 */
	public List<Long> getMerchantIdList(List<MerchantRelModel> relList, MerchantInfoModel merchant) {
		List<Long> idList = new ArrayList<Long>();
		idList.add(merchant.getId());
		if (relList != null && relList.size() > 0) {
			for (MerchantRelModel relmodel : relList) {
				idList.add(relmodel.getProxyMerchantId());
			}
		}
		return idList;
	}
	/**理货单位转换
	 *箱/托盘转换为件
	 *boxToUnit 一箱多少件
	 *torrToUnit 一托多少件
	 *unit 理货单位 00-托盘 01-箱 02/空-件
	 * @return
	 */
    public Integer changeUnit(String unit,BigDecimal num,Integer boxToUnit,Integer torrToUnit,String merchantName,String goodsNo) throws RuntimeException{
    	Integer numInt=0;
    	if(num==null) return numInt;
    	
		//转换单位为件后返回
		if(StringUtils.isEmpty(unit)||unit.equals(DERP.ORDER_TALLYINGUNIT_02)){
			numInt=num.intValue();
		}else if(unit.equals(DERP.ORDER_TALLYINGUNIT_01)){
			if(boxToUnit==null||boxToUnit.intValue()<=0){
				throw new RuntimeException(merchantName+",货号："+goodsNo+"商品箱转件数据为空,结束执行");
			}
			numInt=num.intValue()*boxToUnit.intValue();
		}else if(unit.equals(DERP.ORDER_TALLYINGUNIT_00)){
			if(torrToUnit==null||torrToUnit.intValue()<=0){
				throw new RuntimeException(merchantName+",货号："+goodsNo+"商品托转件数据为空,结束执行");
			}
			numInt=num.intValue()*torrToUnit.intValue();
		}
    	return numInt;
    }

   
    
    /**
     * 生成销售上架明细表
     * @param merchant
     * @param depot
     * @param month
     * @param saleOrderShelfMapList
     * @param saleOutDepotShelfMapList
     * @param dSOrderShelfMapList
     * @throws Exception
     */
    public void saveFinanceSaleShelf(Map<String, Map<String, Object>> saveSummaryMap,
    		MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> saleOrderShelfMapList,
    		List<Map<String, Object>> orderReturnIdepotMapList,
    		List<Map<String, Object>> orderReturnIdepotAmountMapList,
    		List<Map<String, Object>> saleReturnIdepotGXMapList,
    		List<Map<String, Object>> billOutInDepotSubMapList,
    		List<Map<String, Object>> billOutInDepotAddMapList,
    		List<Map<String, Object>>locationOrderMapList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap,
			Map<String, BigDecimal> priceDifferenceMap)throws Exception{

    	
    	List<BuFinanceSaleShelfModel> orderList= new ArrayList<>();
    	
    	// (1)(财务经销存)获取销售订单代销已上架的 的量
    	for (Map<String, Object> map : saleOrderShelfMapList) {
    		Long sale_item_id = (Long) map.get("sale_item_id");//采购订单表体id
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Timestamp shelfDate=(Timestamp) map.get("shelf_date");//上架时间
    		Long goodsId = (Long) map.get("goods_id");//商品id
    		String poNo = (String) map.get("po_no");//po_no号   		   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    		    		
    		Integer num = (Integer) map.get("shelf_num");//采购入库分析表数量
    		String operator = (String) map.get("operator");//上架人名称
    		Long operatorId = (Long) map.get("operator_id");//上架人id
    		Long outOrderId = (Long) map.get("out_order_id");//出库单id
    		String outOrderCode = (String) map.get("out_order_code");//出库单 编码
    		Timestamp deliverDate=(Timestamp) map.get("deliver_date");//上架时间
    		BigDecimal salePrice=(BigDecimal) map.get("price");// 采购订单表体价格
    		if (salePrice==null)salePrice=new BigDecimal(0);
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
    		SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(outOrderId);
    		if (saleOutDepotModel==null)continue;
    		SaleOrderModel saleOrderModel=null;
    		if (saleOutDepotModel.getSaleOrderId()!=null) {
    			saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId()); 
			}   		   
    		if (saleOrderModel==null)continue;
	
    		
    		
    		// 销售订单单价
    		SaleOrderItemModel saleOrderItemModel =new SaleOrderItemModel();
    		saleOrderItemModel.setOrderId(saleOrderModel.getId());
    		saleOrderItemModel.setGoodsId(goodsId);
    	

    		 
    		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;   		
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");

			}
    		   		    		    		    		
    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		Integer saleNum = changeUnit(tallyingUnit,new BigDecimal(num),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// 利息sd单价
    		BigDecimal sdInterestAmount=null;// 利息sd金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(saleNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap =weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();						
				price = (BigDecimal) priceMap.get("weightedPrice");
				sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);
				if (saleNum==null)saleNum=0; 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}
    		BigDecimal orderChangeAmount =null;
    		BigDecimal orderChangePrice =null;
    		if (salePrice!=null&&saleNum!=null&&saleNum!=0&&num!=null) {
    			orderChangeAmount=salePrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
    			orderChangePrice=salePrice.multiply(new BigDecimal(num)).divide(new BigDecimal(saleNum),8,BigDecimal.ROUND_HALF_UP);
			} 
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumXSMap = (Integer) summaryMap.get("saleShelfNumXS");
    			if (saleShelfNumXSMap==null)saleShelfNumXSMap=0;
    			summaryMap.put("saleShelfNumXS", saleNum+saleShelfNumXSMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumXS", saleNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		
    		
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
		    financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		financeSaleShelfModel.setCustomerName(saleOrderModel.getCustomerName());
    		financeSaleShelfModel.setCustomerId(saleOrderModel.getCustomerId());
    		financeSaleShelfModel.setOrderId(saleOrderModel.getId());
    		financeSaleShelfModel.setOrderCode(saleOrderModel.getCode());
    		if (DERP_ORDER.SALEANALYSIS_SALETYPE_1.equals(saleOrderModel.getBusinessModel())) {
    			financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_9);//1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单  5-电商订单退货 6-购销退货 7-账单出入库单调减 8-账单出入库单调增 9-购销整批结算 10-购销实销实结
			}else if (DERP_ORDER.SALEANALYSIS_SALETYPE_4.equals(saleOrderModel.getBusinessModel())) {
				financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_10);//1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单  5-电商订单退货 6-购销退货 7-账单出入库单调减 8-账单出入库单调增 9-购销整批结算 10-购销实销实结
			}else if (DERP_ORDER.SALEANALYSIS_SALETYPE_3.equals(saleOrderModel.getBusinessModel())) {
				financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_13);//1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单  5-电商订单退货 6-购销退货 7-账单出入库单调减 8-账单出入库单调增 9-购销整批结算 10-购销实销实结

			}
    		financeSaleShelfModel.setOrderCreateDate(saleOrderModel.getCreateDate());
    		financeSaleShelfModel.setShelfDate(shelfDate);
    		financeSaleShelfModel.setOutOrderCode(outOrderCode);
    		financeSaleShelfModel.setOutOrderId(outOrderId);
    		financeSaleShelfModel.setDeliverDate(deliverDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		financeSaleShelfModel.setNum(saleNum);
    		financeSaleShelfModel.setSalePrice(orderChangePrice);
    		financeSaleShelfModel.setSaleAmount(orderChangeAmount);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		financeSaleShelfModel.setSaleCurrency(saleOrderModel.getCurrency());
    		financeSaleShelfModel.setPoNo(poNo);
    		financeSaleShelfModel.setShelfId(operatorId);
    		financeSaleShelfModel.setShelfName(operator);
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		financeSaleShelfModel.setChannelType("To B");
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		financeSaleShelfModel.setStockLocationTypeId(stockLocationTypeId);
    		financeSaleShelfModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeSaleShelfModel.setDifferencePrice(differencePrice);
    		financeSaleShelfModel.setDifferenceAmount(differencePriceAmount);
    		orderList.add(financeSaleShelfModel);       		
    			
		}

    	// (5)(财务经销存)获取电商订单退货 的量
    	for (Map<String, Object> map : orderReturnIdepotMapList) {
    		Long id=(Long) map.get("id");//电商订单退id    		
    		Long depotId = (Long) map.get("return_in_depot_id");//仓库id			
    		String code=(String) map.get("code");//电商订单退内部单号 
    		String externalCode=(String) map.get("external_code");//电商退货订单内部单号 
    		String shopCode=(String) map.get("shop_code");//店铺编码
    		String shopName=(String) map.get("shop_name");//店铺名称 
    		Timestamp returnInCreateDate=(Timestamp) map.get("return_in_create_date");//退货创建日期
    		Timestamp returnInDate=(Timestamp) map.get("return_in_date");//入库时间
    		BigDecimal amount=(BigDecimal) map.get("amount");//实付总金额
    		String saleCurrency=(String) map.get("currency");//币种
    		String storePlatformCode=(String) map.get("store_platform_code");//平台编码 
    		Long goodsId=(Long) map.get("in_goods_id");//商品id
    		BigDecimal decTotal=(BigDecimal) map.get("dec_total");//单价
    		Integer returnNum=(Integer) map.get("return_num");//退货正常品数量
    		Integer badBoodsNum=(Integer) map.get("bad_goods_num");//退货残次品数量
    		Long customerId=(Long) map.get("customer_id");//电商订单退客户id
    		String customerName=(String) map.get("customer_name");//平台编码 
    		String shopTypeCode=(String) map.get("shop_type_code");//店铺类型
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
    		if (returnNum==null) {
    			returnNum=0;
			}
    		if (badBoodsNum==null) {
    			badBoodsNum=0;
			}
    		Integer num =badBoodsNum+returnNum;// 数量
    		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");
    			
			}
    		
    		// 订单金额
    		BigDecimal salePrice=null;
    		if(decTotal!=null&&num!=null&&num!=0){
    			salePrice=decTotal.divide(new BigDecimal(num),2,BigDecimal.ROUND_HALF_UP);
    		}     		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// sd 利息单价
    		BigDecimal sdInterestAmount=null;// sd 利息金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(num));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				if (price==null)price=new BigDecimal(0);
				if (num==null)num=0; 
				warehouseAmount=price.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumDSTMap = (Integer) summaryMap.get("saleShelfNumDST");
    			if (saleShelfNumDSTMap==null)saleShelfNumDSTMap=0;
    			summaryMap.put("saleShelfNumDST", num+saleShelfNumDSTMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumDST", num);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		
    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(id);
    		financeSaleShelfModel.setOrderCode(code);
     		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_5);//1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单 5-电商订单退货 6-购销退货 7-账单出入库单(调减) 8-账单出入库单(调增) 
    		financeSaleShelfModel.setOrderCreateDate(returnInCreateDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		//financeSaleShelfModel.setOutOrderCode(outOrderCode);
    		//financeSaleShelfModel.setOutOrderId(outOrderId);
    		financeSaleShelfModel.setDeliverDate(returnInDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		
    		financeSaleShelfModel.setNum(num);
    		financeSaleShelfModel.setSalePrice(salePrice);   		
    		financeSaleShelfModel.setSaleAmount(decTotal);
    		financeSaleShelfModel.setSaleCurrency(saleCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		if (!StringUtils.isEmpty(storePlatformCode)) {
    	    	 // 获取电商平台 所有枚举值
    			String storePlatformName = DERP.getLabelByKey(DERP.storePlatformList,storePlatformCode);
            	financeSaleShelfModel.setStorePlatformName(storePlatformName);// 电商平台名称
			}
    		financeSaleShelfModel.setStorePlatformCode(storePlatformCode);
    		financeSaleShelfModel.setExternalCode(externalCode);  
    		//financeSaleShelfModel.setTaxes(taxes);
    		//financeSaleShelfModel.setWayFrtFee(wayFrtFee);
    		//financeSaleShelfModel.setDiscount(discount);
    		financeSaleShelfModel.setPayment(amount);
    		financeSaleShelfModel.setShopCode(shopCode);
    		financeSaleShelfModel.setShopName(shopName);
    		//financeSaleShelfModel.setSaleCom(saleCom); 
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额   	
    		financeSaleShelfModel.setShopTypeCode(shopTypeCode);
    		if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(shopTypeCode)) {
    			financeSaleShelfModel.setChannelType("To B");
			}
    		if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(shopTypeCode)) {
    			financeSaleShelfModel.setChannelType("To C");
			}
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		financeSaleShelfModel.setStockLocationTypeId(stockLocationTypeId);
    		financeSaleShelfModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeSaleShelfModel.setDifferencePrice(differencePrice);
    		financeSaleShelfModel.setDifferenceAmount(differencePriceAmount);
    		orderList.add(financeSaleShelfModel);       		
    			
		}
    	
    	// (5.1)(财务经销存)获取电商订单退款单
    	for (Map<String, Object> map : orderReturnIdepotAmountMapList) {
    		Long id=(Long) map.get("id");//电商订单退id   		
    		Long depotId = (Long) map.get("return_in_depot_id");//仓库id
    		String code=(String) map.get("code");//电商订单退内部单号 
    		String externalCode=(String) map.get("external_code");//电商退货订单内部单号 
    		String shopCode=(String) map.get("shop_code");//店铺编码
    		String shopName=(String) map.get("shop_name");//店铺名称 
    		Timestamp returnInCreateDate=(Timestamp) map.get("return_in_create_date");//退货创建日期
    		//Timestamp returnInDate=(Timestamp) map.get("return_in_date");//入库时间
    		//BigDecimal amount=(BigDecimal) map.get("amount");//实付总金额
    		String saleCurrency=(String) map.get("currency");//币种
    		String storePlatformCode=(String) map.get("store_platform_code");//平台编码 
    		Long goodsId=(Long) map.get("in_goods_id");//商品id
    		//BigDecimal decTotal=(BigDecimal) map.get("dec_total");//单价
    		//Integer returnNum=(Integer) map.get("return_num");//退货正常品数量
    		//Integer badBoodsNum=(Integer) map.get("bad_goods_num");//退货残次品数量
    		Long customerId=(Long) map.get("customer_id");//电商订单退客户id
    		String customerName=(String) map.get("customer_name");//平台编码 
    		String shopTypeCode=(String) map.get("shop_type_code");//店铺类型
    		BigDecimal refundAmount=(BigDecimal) map.get("refund_amount");// 
    		BigDecimal totalRefundAmount=(BigDecimal) map.get("total_refund_amount");//
    		Timestamp refundEndDate=(Timestamp) map.get("refund_end_date");//
    		

    		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");
    			
			}
    		Integer num=0;
  		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// sd 利息单价
    		BigDecimal sdInterestAmount=null;// sd 利息金额

    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(num));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				if (price==null)price=new BigDecimal(0);
				if (num==null)num=0; 
				warehouseAmount=price.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}
    		
    		/*String deBuKey=depot.getId()+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumDSTMap = (Integer) summaryMap.get("saleShelfNumDST");
    			if (saleShelfNumDSTMap==null)saleShelfNumDSTMap=0;
    			summaryMap.put("saleShelfNumDST", num+saleShelfNumDSTMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumDST", num);
				saveSummaryMap.put(deBuKey, summaryMap);
			}*/
    		
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(id);
    		financeSaleShelfModel.setOrderCode(code);
     		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_14);//1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单 5-电商订单退货 6-购销退货 7-账单出入库单(调减) 8-账单出入库单(调增) 
    		financeSaleShelfModel.setOrderCreateDate(returnInCreateDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		//financeSaleShelfModel.setOutOrderCode(outOrderCode);
    		//financeSaleShelfModel.setOutOrderId(outOrderId);
    		financeSaleShelfModel.setDeliverDate(refundEndDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		
    		financeSaleShelfModel.setNum(num);
    		financeSaleShelfModel.setSalePrice(new BigDecimal(0));   		
    		financeSaleShelfModel.setSaleAmount(refundAmount);
    		financeSaleShelfModel.setSaleCurrency(saleCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		if (!StringUtils.isEmpty(storePlatformCode)) {
    	    	 // 获取电商平台 所有枚举值
    			String storePlatformName = DERP.getLabelByKey(DERP.storePlatformList,storePlatformCode);
            	financeSaleShelfModel.setStorePlatformName(storePlatformName);// 电商平台名称
			}
    		financeSaleShelfModel.setStorePlatformCode(storePlatformCode);
    		financeSaleShelfModel.setExternalCode(externalCode);  
    		financeSaleShelfModel.setTaxes(new BigDecimal(0));//默认0
    		financeSaleShelfModel.setWayFrtFee(new BigDecimal(0));//默认0
    		financeSaleShelfModel.setDiscount(new BigDecimal(0));//默认0
    		financeSaleShelfModel.setPayment(totalRefundAmount);
    		financeSaleShelfModel.setShopCode(shopCode);
    		financeSaleShelfModel.setShopName(shopName);
    		financeSaleShelfModel.setSaleCom(new BigDecimal(0)); //默认0
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额   	 0
    		financeSaleShelfModel.setShopTypeCode(shopTypeCode);
    		if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(shopTypeCode)) {
    			financeSaleShelfModel.setChannelType("To B");
			}
    		if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(shopTypeCode)) {
    			financeSaleShelfModel.setChannelType("To C");
			}
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		orderList.add(financeSaleShelfModel);       		
    			
		}
    	
    	// 6(财务经销存)销售退货单据中类型为购销退货的入库量
    	for (Map<String, Object> map : saleReturnIdepotGXMapList) {
    		
    		Long depotId=(Long) map.get("in_depot_id");//入库单id
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    	
    		Long inOrderId=(Long) map.get("in_order_id");//入库单id
    		String inOderCode=(String) map.get("in_order_code");//电商订单退内部单号 
    		Long orderId=(Long) map.get("order_id");//退货订单id
    		String orderCode=(String) map.get("order_code");//退货订单号
    		Long customerId=(Long) map.get("customer_id");//客户id
    		String customerName=(String) map.get("customer_name");//客户名称
    		Timestamp orderCreateDate=(Timestamp) map.get("order_create_date");//退货创建日期
    		Timestamp returnInDate=(Timestamp) map.get("return_in_date");//入库时间
    		String oderCurrency=(String) map.get("currency");//订单币种    		
    		//BigDecimal salePrice=(BigDecimal) map.get("price");//单价
    		Long goodsId=(Long) map.get("in_goods_id");//商品id
    		Integer returnNum=(Integer) map.get("return_num");//退货正常品 
    		Integer wornNum=(Integer) map.get("worn_num");//残次品
    		Integer expireNum=(Integer) map.get("expire_num");//退货过期品
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
    		if (returnNum==null) {
    			returnNum=0;
			}
    		if (wornNum==null) {
    			wornNum=0;
			}
    		if (expireNum==null) {
    			expireNum=0;
			}
    		Integer ordernum=returnNum+wornNum+expireNum;

    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");
			}
    		
    		String poNo=null;
    		BigDecimal salePrice=null;
    		if (orderId!=null&&goodsId!=null) {
    			SaleReturnOrderItemModel itemModel=new SaleReturnOrderItemModel();
    			itemModel.setOrderId(orderId);
    			itemModel.setInGoodsId(goodsId);
    			List<SaleReturnOrderItemModel> itemList = saleReturnOrderItemDao.list(itemModel);
    			if (itemList.size()>=1) {
    				salePrice=itemList.get(0).getPrice();
    				poNo=itemList.get(0).getPoNo();
				}
			}

    		Integer saleNum = changeUnit(tallyingUnit,new BigDecimal(ordernum),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
     		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// sd 利息单价
    		BigDecimal sdInterestAmount=null;// sd 利息金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(saleNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);
				if (saleNum==null)saleNum=0; 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}
    		
    		
    		
    		// 订单金额

    		BigDecimal orderChangeAmount =null;
    		BigDecimal orderChangePrice =null;
    		if(salePrice!=null&&saleNum!=null&&saleNum!=0&&ordernum!=null){
    			orderChangeAmount=salePrice.multiply(new BigDecimal(ordernum)).setScale(2,BigDecimal.ROUND_HALF_UP);
    			orderChangePrice=salePrice.multiply(new BigDecimal(ordernum)).divide(new BigDecimal(saleNum),2,BigDecimal.ROUND_HALF_UP);
    		} 
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumXSTMap = (Integer) summaryMap.get("saleShelfNumXST");
    			if (saleShelfNumXSTMap==null)saleShelfNumXSTMap=0;
    			summaryMap.put("saleShelfNumXST", saleNum+saleShelfNumXSTMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumXST", saleNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}

    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(orderId);
    		financeSaleShelfModel.setOrderCode(orderCode);
     		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_6);//1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单 5-电商订单退货 6-购销退货 7-账单出入库单(调减) 8-账单出入库单(调增) 
    		financeSaleShelfModel.setOrderCreateDate(orderCreateDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		financeSaleShelfModel.setOutOrderCode(inOderCode);
    		financeSaleShelfModel.setOutOrderId(inOrderId);
    		financeSaleShelfModel.setDeliverDate(returnInDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		
    		financeSaleShelfModel.setNum(saleNum);
    		financeSaleShelfModel.setSalePrice(orderChangePrice);   		
    		financeSaleShelfModel.setSaleAmount(orderChangeAmount);
    		financeSaleShelfModel.setSaleCurrency(oderCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		financeSaleShelfModel.setPoNo(poNo);
    		//financeSaleShelfModel.setStorePlatformCode(storePlatformCode);
    		//financeSaleShelfModel.setExternalCode(externalCode);  
    		//financeSaleShelfModel.setTaxes(taxes);
    		//financeSaleShelfModel.setWayFrtFee(wayFrtFee);
    		//financeSaleShelfModel.setDiscount(discount);
    		//financeSaleShelfModel.setPayment(amount);
    		//financeSaleShelfModel.setShopCode(shopCode);
    		//financeSaleShelfModel.setShopName(shopName);
    		//financeSaleShelfModel.setSaleCom(saleCom);   	
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额   	
    		financeSaleShelfModel.setChannelType("To B");
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		financeSaleShelfModel.setStockLocationTypeId(stockLocationTypeId);
    		financeSaleShelfModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeSaleShelfModel.setDifferencePrice(differencePrice);
    		financeSaleShelfModel.setDifferenceAmount(differencePriceAmount);
    		
    		orderList.add(financeSaleShelfModel);       		
    			
		}
    	// (7) (财务经销存) 账单出库数量（库存调整类型为调减的量）
    	for (Map<String, Object> map : billOutInDepotSubMapList) {	
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Long orderId=(Long) map.get("order_id");//账单出入库单id
    		String orderCode=(String) map.get("order_code");//账单出入库单编码
    		Long customerId=(Long) map.get("customer_id");//客户id
    		String customerName=(String) map.get("customer_name");//客户名称
    		Timestamp createDate=(Timestamp) map.get("create_date");//账单出入库单创建日期
    		Timestamp deliverDate=(Timestamp) map.get("deliver_date");//发货时间
    		String oderCurrency=(String) map.get("currency");//订单币种    		
    		BigDecimal salePrice=(BigDecimal) map.get("price");//单价
    		BigDecimal saleAmount=(BigDecimal) map.get("amount");//金额
    		Long goodsId=(Long) map.get("goods_id");//商品id
    		BigDecimal orderNum=(BigDecimal) map.get("num");//数量
    		//String poNo=(String) map.get("po_no");//po号
			Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
    		if (orderNum==null) {
    			orderNum=new BigDecimal(0);
			}

    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");
			}

     		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// sd 利息单价
    		BigDecimal sdInterestAmount=null;// sd 利息金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,orderNum);
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
    			price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);
				if (orderNum==null)orderNum=new BigDecimal(0); 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}   
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumXSTMap = (Integer) summaryMap.get("saleShelfNumZDC");
    			if (saleShelfNumXSTMap==null)saleShelfNumXSTMap=0;
    			summaryMap.put("saleShelfNumZDC", orderNum.intValue()+saleShelfNumXSTMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumZDC", orderNum.intValue());
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		
    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(orderId);
    		financeSaleShelfModel.setOrderCode(orderCode);
     		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_7);//订单类型: 1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单 5-电商订单退货 6-购销退货 7-账单出入库单(调减) 8-账单出入库单(调增) 
    		financeSaleShelfModel.setOrderCreateDate(createDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		financeSaleShelfModel.setOutOrderCode(orderCode);
    		financeSaleShelfModel.setOutOrderId(orderId);
    		financeSaleShelfModel.setDeliverDate(deliverDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		
    		financeSaleShelfModel.setNum(orderNum.intValue());
    		financeSaleShelfModel.setSalePrice(salePrice);   		
    		financeSaleShelfModel.setSaleAmount(saleAmount);
    		financeSaleShelfModel.setSaleCurrency(oderCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		//financeSaleShelfModel.setPoNo(poNo);
    		//financeSaleShelfModel.setStorePlatformCode(storePlatformCode);
    		//financeSaleShelfModel.setExternalCode(externalCode);  
    		//financeSaleShelfModel.setTaxes(taxes);
    		//financeSaleShelfModel.setWayFrtFee(wayFrtFee);
    		//financeSaleShelfModel.setDiscount(discount);
    		//financeSaleShelfModel.setPayment(amount);
    		//financeSaleShelfModel.setShopCode(shopCode);
    		//financeSaleShelfModel.setShopName(shopName);
    		//financeSaleShelfModel.setSaleCom(saleCom);   	
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());   
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金
    		financeSaleShelfModel.setChannelType("To B");
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		financeSaleShelfModel.setStockLocationTypeId(stockLocationTypeId);
    		financeSaleShelfModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeSaleShelfModel.setDifferencePrice(differencePrice);
    		financeSaleShelfModel.setDifferenceAmount(differencePriceAmount);
    		orderList.add(financeSaleShelfModel);       		
    			
		}
    	
    	// (8) (财务经销存) 账单出库数量（库存调整类型为调减的量）
    	for (Map<String, Object> map : billOutInDepotAddMapList) {	
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Long orderId=(Long) map.get("order_id");//账单出入库单id
    		String orderCode=(String) map.get("order_code");//账单出入库单编码
    		Long customerId=(Long) map.get("customer_id");//客户id
    		String customerName=(String) map.get("customer_name");//客户名称
    		Timestamp createDate=(Timestamp) map.get("create_date");//账单出入库单创建日期
    		Timestamp deliverDate=(Timestamp) map.get("deliver_date");//发货时间
    		String oderCurrency=(String) map.get("currency");//订单币种    		
    		BigDecimal salePrice=(BigDecimal) map.get("price");//单价
    		BigDecimal saleAmount=(BigDecimal) map.get("amount");//金额
    		Long goodsId=(Long) map.get("goods_id");//商品id
    		BigDecimal orderNum=(BigDecimal) map.get("num");//数量
    		//String poNo=(String) map.get("po_no");//po号
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
    		if (orderNum==null) {
    			orderNum=new BigDecimal(0);
			}

    		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");

			}

    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// sd 利息单价
    		BigDecimal sdInterestAmount=null;// sd 利息金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,orderNum);
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
    			price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);
				if (orderNum==null)orderNum=new BigDecimal(0); 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			} 
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumZDRMap = (Integer) summaryMap.get("saleShelfNumZDR");
    			if (saleShelfNumZDRMap==null)saleShelfNumZDRMap=0;
    			summaryMap.put("saleShelfNumZDR", orderNum.intValue()+saleShelfNumZDRMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumZDR", orderNum.intValue());
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		
    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(orderNum).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		
    		
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(orderId);
    		financeSaleShelfModel.setOrderCode(orderCode);
     		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_8);//1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单 5-电商订单退货 6-购销退货 7-账单出入库单(调减) 8-账单出入库单(调增) 
    		financeSaleShelfModel.setOrderCreateDate(createDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		financeSaleShelfModel.setOutOrderCode(orderCode);
    		financeSaleShelfModel.setOutOrderId(orderId);
    		financeSaleShelfModel.setDeliverDate(deliverDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		
    		financeSaleShelfModel.setNum(orderNum.intValue());
    		financeSaleShelfModel.setSalePrice(salePrice);   		
    		financeSaleShelfModel.setSaleAmount(saleAmount);
    		financeSaleShelfModel.setSaleCurrency(oderCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		//financeSaleShelfModel.setPoNo(poNo);
    		//financeSaleShelfModel.setStorePlatformCode(storePlatformCode);
    		//financeSaleShelfModel.setExternalCode(externalCode);  
    		//financeSaleShelfModel.setTaxes(taxes);
    		//financeSaleShelfModel.setWayFrtFee(wayFrtFee);
    		//financeSaleShelfModel.setDiscount(discount);
    		//financeSaleShelfModel.setPayment(amount);
    		//financeSaleShelfModel.setShopCode(shopCode);
    		//financeSaleShelfModel.setShopName(shopName);
    		//financeSaleShelfModel.setSaleCom(saleCom);   	
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		financeSaleShelfModel.setChannelType("To B");
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		financeSaleShelfModel.setStockLocationTypeId(stockLocationTypeId);
    		financeSaleShelfModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeSaleShelfModel.setDifferencePrice(differencePrice);
    		financeSaleShelfModel.setDifferenceAmount(differencePriceAmount);
    		orderList.add(financeSaleShelfModel);       		
    			
		}
   
    	// 库位调整单 调增
    	for (Map<String, Object> map : locationOrderMapList) {	
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Long locationOrderId=(Long) map.get("location_order_id");//库位调整单id
    		String locationOrderCode=(String) map.get("location_order_code");//库位调整单
    		Timestamp ownDate=(Timestamp) map.get("own_date");//归属日期
    		Timestamp createDate=(Timestamp) map.get("create_date");//创建日期   		
    		String externalCode=(String) map.get("external_code");//外部单号
    		String orderCode=(String) map.get("order_code");//系统订单号	
    		Long goodsId=(Long) map.get("increase_goods_id");//调增商品id 		
    		String inventoryType=(String) map.get("inventory_type");//库存类型 0：好品 1：坏品' 
    		Integer adjustNum=(Integer) map.get("adjust_num");//数量    		
    		String platformCode=(String) map.get("platform_code");
    		String platformName=(String) map.get("platform_name");
    		String shopCode=(String) map.get("shop_code");
    		String shopName=(String) map.get("shop_name");
    		String type=(String) map.get("type");
    		String tallyingUnit=(String) map.get("tallying_unit");
    		Long customerId=(Long) map.get("customer_id");//客户id
    		String customerName=(String) map.get("customer_name");//客户名称
    		
    		
    		if (adjustNum==null)adjustNum=0;
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");
			}
    		Integer orderNum = changeUnit(tallyingUnit,new BigDecimal(adjustNum),boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// sd 利息单价
    		BigDecimal sdInterestAmount=null;// sd 利息金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(orderNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);
				if (orderNum==null)orderNum=0; 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(new BigDecimal(orderNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(orderNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(orderNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			} 
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumKWTZDZMap = (Integer) summaryMap.get("saleShelfNumKWTZDZ");
    			if (saleShelfNumKWTZDZMap==null)saleShelfNumKWTZDZMap=0;
    			summaryMap.put("saleShelfNumKWTZDZ", orderNum.intValue()+saleShelfNumKWTZDZMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumKWTZDZ", orderNum.intValue());
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		   
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(locationOrderId);
    		financeSaleShelfModel.setOrderCode(locationOrderCode);
     		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_11);
    		financeSaleShelfModel.setOrderCreateDate(createDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		financeSaleShelfModel.setExternalCode(externalCode);
    		financeSaleShelfModel.setOutOrderId(locationOrderId);
    		financeSaleShelfModel.setOutOrderCode(locationOrderCode);
    		financeSaleShelfModel.setDeliverDate(ownDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		
    		financeSaleShelfModel.setNum(orderNum.intValue());
    		//financeSaleShelfModel.setSalePrice(salePrice);   		
    		//financeSaleShelfModel.setSaleAmount(saleAmount);
    		//financeSaleShelfModel.setSaleCurrency(oderCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		//financeSaleShelfModel.setPoNo(poNo);    	
    		//financeSaleShelfModel.setTaxes(taxes);
    		//financeSaleShelfModel.setWayFrtFee(wayFrtFee);
    		//financeSaleShelfModel.setDiscount(discount);
    		//financeSaleShelfModel.setPayment(amount);
    		//financeSaleShelfModel.setSaleCom(saleCom);   	
    		financeSaleShelfModel.setShopCode(shopCode);
    		financeSaleShelfModel.setShopName(shopName);
    		financeSaleShelfModel.setStorePlatformCode(platformCode);
    		financeSaleShelfModel.setStorePlatformName(platformName);
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());   
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		if (DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DSDD.equals(type)) {
    			financeSaleShelfModel.setChannelType("To C");
			}else if (DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_XSCK.equals(type)) {
				financeSaleShelfModel.setChannelType("To B");
			}
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		
    		
    		orderList.add(financeSaleShelfModel);   
    		
    		// 调减商品
    		Long reduceGoodsId=(Long) map.get("reduce_goods_id");//调减商品id
    			
		}    	
    	// 库位调整单 调减
    	for (Map<String, Object> map : locationOrderMapList) {	
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Long locationOrderId=(Long) map.get("location_order_id");//库位调整单id
    		String locationOrderCode=(String) map.get("location_order_code");//库位调整单
    		Timestamp ownDate=(Timestamp) map.get("own_date");//归属日期
    		Timestamp createDate=(Timestamp) map.get("create_date");//创建日期   		
    		String externalCode=(String) map.get("external_code");//外部单号
    		String orderCode=(String) map.get("order_code");//系统订单号	
    		Long goodsId=(Long) map.get("reduce_goods_id");//调减商品id 		
    		String inventoryType=(String) map.get("inventory_type");//库存类型 0：好品 1：坏品' 
    		Integer adjustNum=(Integer) map.get("adjust_num");//数量    			
    		String platformCode=(String) map.get("platform_code");
    		String platformName=(String) map.get("platform_name");
    		String shopCode=(String) map.get("shop_code");
    		String shopName=(String) map.get("shop_name");
    		String type=(String) map.get("type");
    		String tallyingUnit=(String) map.get("tallying_unit");
    		Long customerId=(Long) map.get("customer_id");//客户id
    		String customerName=(String) map.get("customer_name");//客户名称
    		if (adjustNum==null)adjustNum=0;
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");
			}
    		Integer orderNum = changeUnit(tallyingUnit,new BigDecimal(adjustNum),boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null;
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;// sd 利息单价
    		BigDecimal sdInterestAmount=null;// sd 利息金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(orderNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
    			price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);
				if (orderNum==null)orderNum=0; 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(new BigDecimal(orderNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(orderNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(orderNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			} 
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumKWTZDJMap = (Integer) summaryMap.get("saleShelfNumKWTZDJ");
    			if (saleShelfNumKWTZDJMap==null)saleShelfNumKWTZDJMap=0;
    			summaryMap.put("saleShelfNumKWTZDJ", orderNum.intValue()+saleShelfNumKWTZDJMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumKWTZDJ", orderNum.intValue());
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		   
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleShelfModel.setOutDepotId(depot.getId());
    		financeSaleShelfModel.setOutDepotName(depot.getName());
    		//financeSaleShelfModel.setCustomerName(customerName);
    		//financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(locationOrderId);
    		financeSaleShelfModel.setOrderCode(locationOrderCode);
     		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_12);
    		financeSaleShelfModel.setOrderCreateDate(createDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		financeSaleShelfModel.setExternalCode(externalCode);
    		financeSaleShelfModel.setOutOrderId(locationOrderId);
    		financeSaleShelfModel.setOutOrderCode(locationOrderCode);
    		financeSaleShelfModel.setDeliverDate(ownDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		
    		financeSaleShelfModel.setNum(orderNum.intValue());
    		//financeSaleShelfModel.setSalePrice(salePrice);   		
    		//financeSaleShelfModel.setSaleAmount(saleAmount);
    		//financeSaleShelfModel.setSaleCurrency(oderCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		//financeSaleShelfModel.setPoNo(poNo);
    		//financeSaleShelfModel.setExternalCode(externalCode);  
    		//financeSaleShelfModel.setTaxes(taxes);
    		//financeSaleShelfModel.setWayFrtFee(wayFrtFee);
    		//financeSaleShelfModel.setDiscount(discount);
    		//financeSaleShelfModel.setPayment(amount);   		
    		//financeSaleShelfModel.setSaleCom(saleCom);   	
    		financeSaleShelfModel.setShopCode(shopCode);
    		financeSaleShelfModel.setShopName(shopName);
    		financeSaleShelfModel.setStorePlatformCode(platformCode);
    		financeSaleShelfModel.setStorePlatformName(platformName);
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());    	
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		if (DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DSDD.equals(type)) {
    			financeSaleShelfModel.setChannelType("To C");
			}else if (DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_XSCK.equals(type)) {
				financeSaleShelfModel.setChannelType("To B");
			}
    		orderList.add(financeSaleShelfModel);   
    		   			
		}  
    	
    	
    	// (财务经销存)销售已经上架
    	for (BuFinanceSaleShelfModel financeSaleShelfModel : orderList) {
    		buFinanceSaleShelfDao.save(financeSaleShelfModel);
		} 	
    	orderList=null;
    }
    
    
    /**
     * 生成销售上架明细表 (电商订单)
     * @param merchant
     * @param depot
     * @param month
     * @param saleOrderShelfMapList
     * @param saleOutDepotShelfMapList
     * @param dSOrderShelfMapList
     * @throws Exception
     */
    public void saveDSFinanceSaleShelf(Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> dSOrderShelfMapList,
			String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap,
			Map<String, BigDecimal> priceDifferenceMap)throws Exception{
    	
    	List<BuFinanceSaleShelfModel> orderList= new ArrayList<>();

    	// (3)(财务经销存)获取电商订单已出库 的量
    	for (Map<String, Object> map : dSOrderShelfMapList) {
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Long orderId = (Long) map.get("id");//电商订单id
    		String orderCode=(String) map.get("code");//电商订单库编码
    		Long customerId = (Long) map.get("customer_id");//客户id
    		String customerName = (String) map.get("customer_name");//客户名称  		
    		Timestamp createDate=(Timestamp) map.get("create_date");// 创建时间
    		Long outDepotId = (Long) map.get("depot_id");//仓仓库id   		
    		String outDepotName = (String) map.get("depot_name");//出库仓名称   		
    		Long goodsId = (Long) map.get("goods_id");//商品id    			    		
    		Integer num = (Integer) map.get("num");//数量
    		Timestamp deliverDate=(Timestamp) map.get("deliver_date");// 发货时间
    		BigDecimal salePrice = (BigDecimal) map.get("price");//销售价格
    		BigDecimal decTotal = (BigDecimal) map.get("dec_total");//总金额   
    		BigDecimal itemTax = (BigDecimal) map.get("item_tax");//商品分摊税费 
    		BigDecimal itemWayFrtFee = (BigDecimal) map.get("item_way_frt_fee");//商品分摊运费
    		String storePlatformCode = (String) map.get("store_platform_name");//电商平台名称	
    		String externalCode = (String) map.get("external_code");//外部单号 		 
    		String shopCode = (String) map.get("shop_code");//店铺编码   
    		String shopName = (String) map.get("shop_name");//店铺名称   
    		BigDecimal taxes = (BigDecimal) map.get("taxes");//税费
    		BigDecimal wayFrtFee = (BigDecimal) map.get("way_frt_fee");//运费
    		BigDecimal discount = (BigDecimal) map.get("discount");//优化减免金额
    		BigDecimal payment = (BigDecimal) map.get("payment");//订单实付金额
    		BigDecimal saleCom = (BigDecimal) map.get("sale_com");//总佣金
    		String saleCurrency = (String) map.get("currency");//电商订单币种  
    		String exOrderId = (String) map.get("ex_order_id");//电商订单币种  
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位
    		String shopTypeCode = (String) map.get("shop_type_code");//店铺类型值编码   
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
    		
    		if (decTotal==null)decTotal=new BigDecimal(0);
    		if (itemTax==null)itemTax=new BigDecimal(0);
    		if (itemWayFrtFee==null)itemWayFrtFee=new BigDecimal(0);
    		BigDecimal saleAmount=decTotal.add(itemTax).add(itemWayFrtFee);
    		
    		   		
    		/*BigDecimal saleAmount=null;// 销售金额
    		if(salePrice!=null){
    			saleAmount = salePrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);
    		} */   
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");	
			}
    		Integer saleNum = changeUnit(tallyingUnit,new BigDecimal(num),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		   		
    		
    		// 电商订单没有 单位 不用箱托转换 默认为件
     		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		BigDecimal sdInterestPrice=null;
    		BigDecimal sdInterestAmount=null;
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(num));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();						
    			price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
    			sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
				if (saleNum==null)saleNum=0; 
				warehouseAmount=price.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleShelfNumDSMap = (Integer) summaryMap.get("saleShelfNumDS");
    			if (saleShelfNumDSMap==null)saleShelfNumDSMap=0;
    			summaryMap.put("saleShelfNumDS", saleNum+saleShelfNumDSMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleShelfNumDS", saleNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		
    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		//(财务经销存)销售已经上架
    		BuFinanceSaleShelfModel financeSaleShelfModel =new  BuFinanceSaleShelfModel();
    		financeSaleShelfModel.setBrandId(brandId);
    		financeSaleShelfModel.setBrandName(brandName);
    		financeSaleShelfModel.setMerchantId(merchant.getId());
    		financeSaleShelfModel.setMerchantName(merchant.getName());
    		financeSaleShelfModel.setOutDepotId(outDepotId);
    		financeSaleShelfModel.setOutDepotName(outDepotName);
    		financeSaleShelfModel.setCustomerName(customerName);
    		financeSaleShelfModel.setCustomerId(customerId);
    		financeSaleShelfModel.setOrderId(orderId);
    		financeSaleShelfModel.setOrderCode(orderCode);
    		financeSaleShelfModel.setOrderType(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_3);
    		financeSaleShelfModel.setOrderCreateDate(createDate);
    		//financeSaleShelfModel.setShelfDate(shelfDate);
    		//financeSaleShelfModel.setOutOrderCode(outOrderCode);
    		//financeSaleShelfModel.setOutOrderId(outOrderId);
    		financeSaleShelfModel.setDeliverDate(deliverDate);
    		financeSaleShelfModel.setGoodsId(goodsId);
    		financeSaleShelfModel.setGoodsNo(goodsNo);
    		financeSaleShelfModel.setGoodsName(goodsName);
    		financeSaleShelfModel.setBarcode(barcode);
    		financeSaleShelfModel.setCommbarcode(commbarcode);
    		financeSaleShelfModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleShelfModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeSaleShelfModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeSaleShelfModel.setTallyingUnit(tallyingUnit);
    		financeSaleShelfModel.setNum(saleNum);
    		financeSaleShelfModel.setSalePrice(salePrice);
    		financeSaleShelfModel.setSaleAmount(saleAmount);   		
    		financeSaleShelfModel.setSaleCurrency(saleCurrency);
    		financeSaleShelfModel.setOutDepotCurrency(currency);
    		financeSaleShelfModel.setOutDepotPrice(price);
    		financeSaleShelfModel.setOutDepotAmount(warehouseAmount);
    		financeSaleShelfModel.setMonth(month);
    		if (!StringUtils.isEmpty(storePlatformCode)) {
    			String storePlatformName = DERP.getLabelByKey(DERP.storePlatformList,storePlatformCode);
            	financeSaleShelfModel.setStorePlatformName(storePlatformName);// 电商平台名称
			}
    		financeSaleShelfModel.setStorePlatformCode(storePlatformCode);
    		financeSaleShelfModel.setExternalCode(externalCode);  
    		financeSaleShelfModel.setTaxes(taxes);
    		financeSaleShelfModel.setWayFrtFee(wayFrtFee);
    		financeSaleShelfModel.setDiscount(discount);
    		financeSaleShelfModel.setPayment(payment);
    		financeSaleShelfModel.setShopCode(shopCode);
    		financeSaleShelfModel.setShopName(shopName);
    		financeSaleShelfModel.setSaleCom(saleCom);       		
    		financeSaleShelfModel.setExOrderId(exOrderId);
    		financeSaleShelfModel.setBuId(buModel.getBuId());
    		financeSaleShelfModel.setBuName(buModel.getBuName());
    		financeSaleShelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleShelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		financeSaleShelfModel.setShopTypeCode(shopTypeCode);//店铺类型值编码
    		if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002.equals(shopTypeCode)) {
    			financeSaleShelfModel.setChannelType("To B");
			}
    		if (DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(shopTypeCode)) {
    			financeSaleShelfModel.setChannelType("To C");
			}
    		financeSaleShelfModel.setSdInterestAmount(sdInterestAmount);
    		financeSaleShelfModel.setSdInterestPrice(sdInterestPrice);
    		financeSaleShelfModel.setStockLocationTypeId(stockLocationTypeId);
    		financeSaleShelfModel.setStockLocationTypeName(stockLocationTypeName);
    		financeSaleShelfModel.setDifferencePrice(differencePrice);
    		financeSaleShelfModel.setDifferenceAmount(differencePriceAmount);
    		orderList.add(financeSaleShelfModel);    					
		}
    	    	    	
    	// 批量新增(财务经销存)销售已经上架
    	if (orderList!=null&&orderList.size()>0) {
    		buFinanceSaleShelfDao.batchBuInsertFinanceSaleShelf(orderList);
		}
    	// 清缓存
    	orderList=null;	
    	
    	
    }
    
    
    /**
     * 
     * @param merchant
     * @param depot
     * @param month
     * @param saleNotshelfDXMapList
     * @param saleNotshelfGXMapList
     * @throws Exception
     */
    public void saveFinancePurchaseDamaged(Map<Long,DepotInfoModel> depotModelMap,Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> purchaseDamagedLackList,
    		List<Map<String, Object>> purchaseDamagedWornExpireList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap
			)throws Exception{
    	List<BuFinancePurchaseDamagedModel> orderList= new ArrayList<>();
    	//来货残损
    	for (Map<String, Object> map : purchaseDamagedLackList) {
    		// 获取中转仓仓库编码 // 采购在途默认 写死中转仓
    		DepotInfoModel depotInfo=new  DepotInfoModel();
    		depotInfo.setCode("ZT001");
    		depotInfo = depotInfoDao.searchByModel(depotInfo);
    		// 来货残损归属到中转仓   		
    		Long orderId = (Long) map.get("id");//采购订单id
    		String orderCode = (String) map.get("code");//采购订单号
    		Long purchaseItemId = (Long) map.get("purchase_item_id");//采购订单表体id    	   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    		    		
    		BigDecimal num = (BigDecimal) map.get("num");//残损数量
    		// 根据采购订单id查询采购订单
    		PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(orderId);  
    		if (purchaseOrderModel==null) {
    			purchaseOrderModel=new PurchaseOrderModel();
			}
    		PurchaseOrderItemModel purchaseOrderItem = purchaseOrderItemDao.searchById(purchaseItemId);
    		Long goodsId = purchaseOrderItem.getGoodsId();// 商品id   
			Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");

			}
    		    		
    		
    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		Integer changeNum = changeUnit(tallyingUnit,num,boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		// 根据采购入库单查询    采购入库关联采购订单表
    		WarehouseOrderRelModel warehouseOrderRelModel=new WarehouseOrderRelModel();
    		warehouseOrderRelModel.setWarehouseId(orderId);
    		List<WarehouseOrderRelModel> warehouseOrderRelList = warehouseOrderRelDao.list(warehouseOrderRelModel);
    		String warehouseIds="";//入库单id,多个用,号隔开订单id
    		String warehouseCodes="";//入库单编码, 多个用,号隔开
    		for (WarehouseOrderRelModel RelModel : warehouseOrderRelList) {
    			PurchaseWarehouseModel purchaseWarehouseModel = purchaseWarehouseDao.searchById(RelModel.getWarehouseId());
				if (purchaseWarehouseModel!=null) {
					if (StringUtils.isEmpty(warehouseIds)) {
						warehouseIds=""+purchaseWarehouseModel.getId();
						warehouseCodes=""+purchaseWarehouseModel.getCode();
					}else {
						warehouseIds=warehouseIds+","+purchaseWarehouseModel.getId();
						warehouseCodes=warehouseCodes+","+purchaseWarehouseModel.getCode();
					}
					
				}
			}

   		
    		//BigDecimal price=null;
    		BigDecimal sdWeightedPrice=null;
    		BigDecimal sdWeightedAmont=null;
    		// 加权
    		if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
    			String weightedKey=buModel.getBuId()+","+commbarcode;
    			Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
    			if (priceMap==null)priceMap=new HashMap<>();
    			//price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				//if (price==null)price=new BigDecimal(0);
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
				if (changeNum==null)changeNum=0; 
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			}
    		
    		String deBuKey=depotInfo.getId()+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer lackNumNumMap = (Integer) summaryMap.get("purchaseLackNum");
    			if (lackNumNumMap==null)lackNumNumMap=0;
    			summaryMap.put("purchaseLackNum", changeNum+lackNumNumMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("purchaseLackNum", changeNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		
    		BuFinancePurchaseDamagedModel financePurchaseDamagedModel =new BuFinancePurchaseDamagedModel();
    		financePurchaseDamagedModel.setBrandId(brandId);
    		financePurchaseDamagedModel.setBrandName(brandName);
    		financePurchaseDamagedModel.setMerchantId(merchant.getId());//商家id
    		financePurchaseDamagedModel.setMerchantName(merchant.getName());// 商家名称
    		financePurchaseDamagedModel.setDepotId(depotInfo.getId());//采购订单中的入仓仓库id
    		financePurchaseDamagedModel.setDepotName(depotInfo.getName());//采购订单中的入仓仓库名称
    		financePurchaseDamagedModel.setOrderId(String.valueOf(orderId));// 订单id
    		financePurchaseDamagedModel.setOrderCode(orderCode);//订单编码
    		financePurchaseDamagedModel.setWarehouseId(warehouseIds);//入库单id
    		financePurchaseDamagedModel.setWarehouseCode(warehouseCodes);//入库单编码
    		financePurchaseDamagedModel.setOrderType(DERP_REPORT.FINANCEPURCHASEDAMAGED_ORDERTYPE_2);// 残损类型：1来货残次、2来货短缺
    		financePurchaseDamagedModel.setGoodsId(goodsId);//商品id
    		financePurchaseDamagedModel.setGoodsName(goodsName);//商品名称
    		financePurchaseDamagedModel.setGoodsNo(goodsNo);//商品货号
    		financePurchaseDamagedModel.setBarcode(barcode);//商品条码
    		financePurchaseDamagedModel.setCommbarcode(commbarcode);// 标准条码
    		financePurchaseDamagedModel.setSuperiorParentBrand(superiorParentBrand);
    		financePurchaseDamagedModel.setNum(changeNum);//残损或者短缺数量
    		//financePurchaseDamagedModel.setPrice(price);//入库单价(结算价格单价)
    		//financePurchaseDamagedModel.setAmount(warehouseAmount);//入库金额
    		financePurchaseDamagedModel.setMonth(month);//归属月份 YYYY-MM
    		financePurchaseDamagedModel.setPoNo(purchaseOrderModel.getPoNo());//po号
    		financePurchaseDamagedModel.setCreater(purchaseOrderModel.getCreater());// 创建人id
    		financePurchaseDamagedModel.setCreateName(purchaseOrderModel.getCreateName());//创建人名称
    		financePurchaseDamagedModel.setBuId(buModel.getBuId());// 事业部id
    		financePurchaseDamagedModel.setBuName(buModel.getBuName());// 事业部名称
    		//financePurchaseDamagedModel.setWarehouseCreateDate(warehouseCreateDate);
    		financePurchaseDamagedModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financePurchaseDamagedModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		orderList.add(financePurchaseDamagedModel);
        	       	         		
    					
		}
    	//来货短缺
    	for (Map<String, Object> map : purchaseDamagedWornExpireList) {
    		Long warehouseId = (Long) map.get("warehouse_id");//采购入库订单id
    		Long purchaseItemId = (Long) map.get("purchase_item_id");//商品id    	   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    	
    		Long depotId = (Long) map.get("depot_id");//仓库
    		BigDecimal num = (BigDecimal) map.get("num");//残损数量
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//理货单位
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//理货单位
    		String tgtCurrency = (String) map.get("tgt_currency");//本位币币种
    		String orderCurrency = (String) map.get("currency");//原币币种
    		Long purchaseOrderId = (Long) map.get("purchase_order_id");//采购订单id
    		BigDecimal orderPrice = (BigDecimal) map.get("price");//单价
    		if (orderPrice==null)orderPrice=new BigDecimal(0);
    		BigDecimal tgtPrice = (BigDecimal) map.get("tgt_price");//单价
    		if (tgtPrice==null)tgtPrice=new BigDecimal(0);

    		// 根据采购入库单id查询采购入库单    		
    		PurchaseWarehouseModel purchaseWarehouseModel = purchaseWarehouseDao.searchById(warehouseId);
    		PurchaseOrderItemModel purchaseOrderItem = purchaseOrderItemDao.searchById(purchaseItemId);
    		Long goodsId = purchaseOrderItem.getGoodsId();// 商品id   
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
    		String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}   		
    		
    		// 根据采购入库单查询    采购入库关联采购订单表
    		WarehouseOrderRelModel warehouseOrderRelModel=new WarehouseOrderRelModel();
    		warehouseOrderRelModel.setWarehouseId(warehouseId);
    		List<WarehouseOrderRelModel> warehouseOrderRelList = warehouseOrderRelDao.list(warehouseOrderRelModel);
    		String orderIds="";//采购订单id,多个用,号隔开订单id
    		String orderCodes="";//采购订单编码, 多个用,号隔开
    		String poNos="";
    		for (WarehouseOrderRelModel RelModel : warehouseOrderRelList) {
    			PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(RelModel.getPurchaseOrderId());
				if (purchaseOrderModel!=null) {					
					if (StringUtils.isEmpty(orderIds)) {
						orderIds=""+purchaseOrderModel.getId();
						orderCodes=""+purchaseOrderModel.getCode();
						poNos=""+purchaseOrderModel.getPoNo();
					}else {
						orderIds=orderIds+","+purchaseOrderModel.getId();
						orderCodes=orderCodes+","+purchaseOrderModel.getCode();
						poNos=poNos+","+purchaseOrderModel.getPoNo();
					}
					
				}
			}
	
    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		Integer changeNum = changeUnit(tallyingUnit,num,boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		// 订单单价
    		BigDecimal orderChangePrice =null;
    		if (orderPrice!=null&&changeNum!=null&&changeNum!=0&&num!=null) {
    			orderChangePrice=orderPrice.multiply(num).divide(new BigDecimal(changeNum),8,BigDecimal.ROUND_HALF_UP);
			}
    		
    		BigDecimal sdWeightedPrice=null;
    		BigDecimal sdWeightedAmont=null;
    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
			BigDecimal warehouseAmount=null; 
			BigDecimal price=null;	
    		// 加权
    		if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
    			String weightedKey=buModel.getBuId()+","+commbarcode;
    			Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
    			if (priceMap==null)priceMap=new HashMap<>();
    			//price = (BigDecimal) priceMap.get("weightedPrice");
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				//if (price==null)price=new BigDecimal(0);
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
				if (changeNum==null)changeNum=0; 
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				
							
	    		if (num!=null&&changeNum!=0) {
	    			warehouseAmount=tgtPrice.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);
	    			price = tgtPrice.multiply(num).divide(new BigDecimal(changeNum),8,BigDecimal.ROUND_HALF_UP);	
				}
	    		
	    		
				//获取差异金额
	    		differencePrice =getDifferencePrice(merchant, purchaseOrderId, orderCurrency, tgtCurrency, month, orderChangePrice,price);
	    		if (differencePrice==null)differencePrice=new BigDecimal(0);
	    		differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
	    		differencePriceAmount=differencePrice.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);
			}
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer damagedNumNumMap = (Integer) summaryMap.get("purchaseDamagedNum");
    			if (damagedNumNumMap==null)damagedNumNumMap=0;
    			summaryMap.put("purchaseDamagedNum", changeNum+damagedNumNumMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("purchaseDamagedNum", changeNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		BuFinancePurchaseDamagedModel financePurchaseDamagedModel =new BuFinancePurchaseDamagedModel();
    		financePurchaseDamagedModel.setBrandId(brandId);
    		financePurchaseDamagedModel.setBrandName(brandName);
    		financePurchaseDamagedModel.setMerchantId(merchant.getId());//商家id
    		financePurchaseDamagedModel.setMerchantName(merchant.getName());// 商家名称
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financePurchaseDamagedModel.setDepotId(depot.getId());//采购订单中的入仓仓库id
    		financePurchaseDamagedModel.setDepotName(depot.getName());//采购订单中的入仓仓库名称
    		financePurchaseDamagedModel.setOrderId(orderIds);// 订单id
    		financePurchaseDamagedModel.setOrderCode(orderCodes);//订单编码
    		financePurchaseDamagedModel.setWarehouseId(String.valueOf(warehouseId));//入库单id
    		financePurchaseDamagedModel.setWarehouseCode(purchaseWarehouseModel.getCode());//入库单编码
    		financePurchaseDamagedModel.setOrderType(DERP_REPORT.FINANCEPURCHASEDAMAGED_ORDERTYPE_1);// 残损类型：1来货残次、2来货短缺
    		financePurchaseDamagedModel.setGoodsId(goodsId);//商品id
    		financePurchaseDamagedModel.setGoodsName(goodsName);//商品名称
    		financePurchaseDamagedModel.setGoodsNo(goodsNo);//商品货号
    		financePurchaseDamagedModel.setBarcode(barcode);//商品条码
    		financePurchaseDamagedModel.setCommbarcode(commbarcode);// 标准条码
    		financePurchaseDamagedModel.setSuperiorParentBrand(superiorParentBrand);
    		financePurchaseDamagedModel.setNum(changeNum);//残损或者短缺数量
    		financePurchaseDamagedModel.setPrice(price);//入库单价(结算价格单价)
    		financePurchaseDamagedModel.setAmount(warehouseAmount);//入库金额
    		financePurchaseDamagedModel.setMonth(month);//归属月份 YYYY-MM  
    		financePurchaseDamagedModel.setPoNo(poNos);//po号
    		financePurchaseDamagedModel.setWarehouseCreateDate(purchaseWarehouseModel.getInboundDate());
    		financePurchaseDamagedModel.setBuId(buModel.getBuId());// 事业部id
    		financePurchaseDamagedModel.setBuName(buModel.getBuName());// 事业部名称
    		financePurchaseDamagedModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financePurchaseDamagedModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		financePurchaseDamagedModel.setStockLocationTypeId(stockLocationTypeId);
    		financePurchaseDamagedModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financePurchaseDamagedModel.setDifferencePrice(differencePrice);
    		financePurchaseDamagedModel.setDifferenceAmount(differencePriceAmount);
    		orderList.add(financePurchaseDamagedModel);    		   		    		    		
		}
    	
    	// 新增采财务经销存购残损明细
    	for (BuFinancePurchaseDamagedModel financePurchaseDamagedModel : orderList) {
			buFinancePurchaseDamagedDao.save(financePurchaseDamagedModel);
		}
    	
    	
    }
    
    /**
     *  生成(事业部财务经销存)销售残损明细
     * @param merchant
     * @param depot
     * @param month
     * @param saleDamagedGXList
     * @param saleDamagedDXList
     * @throws Exception
     */
    public void saveFinanceSaleDamaged(Map<String, Map<String, Object>> saveSummaryMap,
    		MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> saleDamagedDXList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap,
			Map<String, BigDecimal> priceDifferenceMap)throws Exception{
    	// 销售残损保存List
    	List<BuFinanceSaleDamagedModel> orderList= new ArrayList<>();
    	    	   	
	    //销售订单
    	for (Map<String, Object> map : saleDamagedDXList) {
    		Long depotId = (Long) map.get("depot_id");//仓库id    		
    		Long outOrderId = (Long) map.get("out_order_id");//出库订单id
    		
    		String outOrderCode = (String) map.get("out_order_code");//出库订单号
    		Long goodsId = (Long) map.get("goods_id");//商品id    	   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    		    		
    		Integer damagedNum = (Integer) map.get("damaged_num");//残损数量
    		Integer lackNum = (Integer) map.get("lack_num");//残损数量   	
    		Timestamp shelfDate= (Timestamp) map.get("shelf_date");//上架时间
    		String poNo = (String) map.get("po_no");//po号
    		String operator = (String) map.get("operator");//上架人名称
    		Long operatorId = (Long) map.get("operator_id");//上架人id
    		Timestamp deliverDate= (Timestamp) map.get("deliver_date");//发货时间		
    		Long saleOrderId = (Long) map.get("sale_order_id");//出库订单id
    		BigDecimal salePrice=(BigDecimal) map.get("price");
    		if (salePrice==null)salePrice=new BigDecimal(0);
			Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
			Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			barcode=barcode.trim();
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}    		    		   		    		   		    		       		    		    		
    		//箱托转化单位          残货数量 		
    		Integer changeDamagedNum = changeUnit(tallyingUnit,new BigDecimal(damagedNum),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		// 箱托转化单位 	 少货数量数量	
    		Integer changeLackNum = changeUnit(tallyingUnit,new BigDecimal(lackNum),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
   		  	Integer changeNum=changeDamagedNum+changeLackNum;// 残损数量+少货数量
   		  	if (changeNum==null)changeNum=0;
   		  	// 查询销售订单
   		  	if (saleOrderId==null) throw new RuntimeException("生成(事业部财务经销存)销售残损明细销售订单id不能为空");
   		  	SaleOrderModel saleOrderModel = saleOrderDao.searchById(saleOrderId);
   		  	if (saleOrderModel==null)throw new RuntimeException("生成(事业部财务经销存)销售残损明细销 根据销售订单id没有找到销售订单,销售订单id:"+saleOrderId);
   		  	
   		  	BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
   		  	
   		  	BigDecimal saleAmount=null;
   		  	saleAmount=salePrice.multiply(new BigDecimal(changeNum));
	  		saleAmount=saleAmount.setScale(2,BigDecimal.ROUND_HALF_UP);   		  	
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		BigDecimal sdWeightedPrice=null;
    		BigDecimal sdWeightedAmont=null;    		
    		BigDecimal sdInterestPrice=null;
    		BigDecimal sdInterestAmount=null;
    		
    		String currency=null; 
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(changeNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
				sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");
				if (price==null)price=new BigDecimal(0);				 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
    			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);

				warehouseAmount=price.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdInterestAmount=sdInterestPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			}
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer saleDamagedNumMap = (Integer) summaryMap.get("saleDamagedNum");
    			if (saleDamagedNumMap==null)saleDamagedNumMap=0;
    			summaryMap.put("saleDamagedNum", changeNum+saleDamagedNumMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("saleDamagedNum", changeNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		
    		BuFinanceSaleDamagedModel financeSaleDamagedModel=new BuFinanceSaleDamagedModel();
    		financeSaleDamagedModel.setBrandId(brandId);
    		financeSaleDamagedModel.setBrandName(brandName);
    		financeSaleDamagedModel.setMerchantId(merchant.getId());// 商家id
    		financeSaleDamagedModel.setMerchantName(merchant.getName());//商家名称
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeSaleDamagedModel.setDepotId(depot.getId());//仓库id
    		financeSaleDamagedModel.setDepotName(depot.getName());//仓仓库名称
    		financeSaleDamagedModel.setOrderId(saleOrderModel.getId());//订单id
    		financeSaleDamagedModel.setOrderCode(saleOrderModel.getCode());//订单编码
    		financeSaleDamagedModel.setOrderCreateDate(saleOrderModel.getCreateDate());//订单日期 (订单创建日期)
    		financeSaleDamagedModel.setOutOrderCode(outOrderCode);//出库单编码
    		financeSaleDamagedModel.setOutOrderId(outOrderId);//销售出库订单id
    		financeSaleDamagedModel.setShelfDate(shelfDate);//上架时间
    		financeSaleDamagedModel.setDeliverDate(deliverDate);// 发货时间
    		if (DERP_ORDER.SALEANALYSIS_SALETYPE_1.equals(saleOrderModel.getBusinessModel())) {
    			financeSaleDamagedModel.setOrderType(DERP_REPORT.FINANCESALEDAMAGED_ORDERTYPE_3);//1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结
			}else if (DERP_ORDER.SALEANALYSIS_SALETYPE_4.equals(saleOrderModel.getBusinessModel())) {
				financeSaleDamagedModel.setOrderType(DERP_REPORT.FINANCESALEDAMAGED_ORDERTYPE_4);//1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结				
			}	
    		financeSaleDamagedModel.setGoodsId(goodsId);// 商品id
    		financeSaleDamagedModel.setGoodsName(goodsName);//商品名称
    		financeSaleDamagedModel.setGoodsNo(goodsNo);//商品货号
    		financeSaleDamagedModel.setBarcode(barcode);//商品条码
    		financeSaleDamagedModel.setCommbarcode(commbarcode);// 标准条码
    		financeSaleDamagedModel.setSuperiorParentBrand(superiorParentBrand);
    		financeSaleDamagedModel.setNum(changeDamagedNum);// 残损数量
    		financeSaleDamagedModel.setLackNum(changeLackNum);// 缺货数量
    		financeSaleDamagedModel.setPrice(price);//单价(结算价格单价)
    		financeSaleDamagedModel.setAmount(warehouseAmount);//残损金额
    		financeSaleDamagedModel.setMonth(month);//归属月份 YYYY-MM
    		financeSaleDamagedModel.setPoNo(poNo);
    		financeSaleDamagedModel.setShelfId(operatorId);
    		financeSaleDamagedModel.setShelfName(operator);
    		financeSaleDamagedModel.setBuId(buModel.getBuId());
    		financeSaleDamagedModel.setBuName(buModel.getBuName());
    		financeSaleDamagedModel.setCustomerId(saleOrderModel.getCustomerId());
    		financeSaleDamagedModel.setCustomerName(saleOrderModel.getCustomerName());
    		financeSaleDamagedModel.setSalePrice(salePrice);// 销售单价
    		financeSaleDamagedModel.setSaleAmount(saleAmount);// 销售金额
    		financeSaleDamagedModel.setSaleCurrency(saleOrderModel.getCurrency());//销售币种
    		financeSaleDamagedModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		financeSaleDamagedModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		financeSaleDamagedModel.setSdInterestAmount(sdInterestAmount);// sd利息单价
    		financeSaleDamagedModel.setSdInterestPrice(sdInterestPrice);// sd利息金额
    		financeSaleDamagedModel.setStockLocationTypeId(stockLocationTypeId);
    		financeSaleDamagedModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeSaleDamagedModel.setDifferencePrice(differencePrice);
    		financeSaleDamagedModel.setDifferenceAmount(differencePriceAmount);
    		financeSaleDamagedModel.setOutDepotCurrency(merchant.getAccountCurrency());
    	
    		orderList.add(financeSaleDamagedModel);
		}
	    // 循环新增	
    	for (BuFinanceSaleDamagedModel financeSaleDamagedModel : orderList) {
    		buFinanceSaleDamagedDao.save(financeSaleDamagedModel);
		}
    	
    }


    
    /**
     * 盘盈盘亏
     * @param merchant
     * @param depot
     * @param month
     * @param takesStockResultsList
     * @throws Exception
     */
    public void saveFinanceTakesStockResults(Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> takesStockResultsList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap,
			Map<String, BigDecimal> priceDifferenceMap)throws Exception{
    	
    	List<BuFinanceTakesStockResultsModel> orderList=new ArrayList<>();
    	
    	for (Map<String, Object> map : takesStockResultsList) {
    		
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Long orderId = (Long) map.get("id");//订单id
    		String orderCode = (String) map.get("code");//订单号
    		Timestamp createDate= (Timestamp) map.get("create_date");//订单创建时间
    		Long goodsId = (Long) map.get("goods_id");//商品id    	   		
    		String tallyingUnit = (String) map.get("tally_unit");//理货单位    		    		
    		Integer surplusNum = (Integer) map.get("surplus_num");//盘盈数量
    		Integer deficientNum = (Integer) map.get("deficient_num");//盘亏数量
			Date productionDate= (Date) map.get("production_date");//订单创建时间
			Date overdueDate= (Date) map.get("overdue_date");//订单创建时间
    		String type = (String) map.get("type");//调整类型: 1盘盈 2盘亏  
    		String isDamage = (String) map.get("is_damage");//好坏品
    		String batchNo = (String) map.get("batch_no");//批次时
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
			Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		String superiorParentBrandCode=null;
    		Long superiorParentBrandId=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
    			superiorParentBrandCode = (String) merchandiseMap.get("superiorParentBrandCode");
    			superiorParentBrandId = (Long) merchandiseMap.get("superiorParentBrandId");

			}    	   		       	
    		// 根据goodId查询商品表
    		/*MerchandiseInfoModel merchandiseInfoModel = merchandiseInfoDao.searchById(goodsId);
    		Integer boxToUnit = merchandiseInfoModel.getBoxToUnit();//一箱多少件
    		Integer torrToUnit = merchandiseInfoModel.getTorrToUnit();//一托多少件
*/    		  		
    		//箱托转化单位 //数量    
    		Integer changeSurplusNum=null;
    		Integer changeDeficientNum=null;
    		Integer changeNum=null;
    		if (surplusNum!=null) {
        		changeSurplusNum = changeUnit(tallyingUnit,new BigDecimal(surplusNum),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
        		changeNum=changeSurplusNum;
    		}
    		if (deficientNum!=null) {
        		changeDeficientNum = changeUnit(tallyingUnit,new BigDecimal(deficientNum),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
        		changeNum=deficientNum;
    		} 
    		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(changeDeficientNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;				
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
			}
    		
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if ("1".equals(type)){//盘盈   			
    			if (saveSummaryMap.containsKey(deBuKey)) {
    				Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    				Integer profitNumMap = (Integer) summaryMap.get("profitNum");
        			if (profitNumMap==null)profitNumMap=0;
        			summaryMap.put("profitNum", changeSurplusNum+profitNumMap);
        			saveSummaryMap.put(deBuKey, summaryMap);
				}else {
					Map<String, Object> summaryMap = new HashMap<>();    				
					summaryMap.put("profitNum", changeSurplusNum);
					saveSummaryMap.put(deBuKey, summaryMap);
				}
    			        					
			}else if ("2".equals(type)) {// 盘亏
				if (saveSummaryMap.containsKey(deBuKey)) {
    				Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    				Integer lossNumMap = (Integer) summaryMap.get("lossNum");
        			if (lossNumMap==null)lossNumMap=0;
        			summaryMap.put("lossNum", changeDeficientNum+lossNumMap);
        			saveSummaryMap.put(deBuKey, summaryMap);
				}else {
					Map<String, Object> summaryMap = new HashMap<>();    				
					summaryMap.put("lossNum", changeDeficientNum);
					saveSummaryMap.put(deBuKey, summaryMap);
				}
			}
    		
    		if (changeNum!=null&&price!=null) {
    			warehouseAmount=price.multiply(new BigDecimal(changeNum));
			}
    		
    		BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		BuFinanceTakesStockResultsModel financeTakesStockResultsModel=new BuFinanceTakesStockResultsModel();
    		financeTakesStockResultsModel.setBrandId(brandId);
    		financeTakesStockResultsModel.setBrandName(brandName);
    		financeTakesStockResultsModel.setMerchantId(merchant.getId());//商家id
    		financeTakesStockResultsModel.setMerchantName(merchant.getName());//商家名称
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeTakesStockResultsModel.setDepotId(depot.getId());//仓库id
    		financeTakesStockResultsModel.setDepotName(depot.getName());//仓库名称
    		financeTakesStockResultsModel.setOrderId(orderId);//订单id
    		financeTakesStockResultsModel.setOrderCode(orderCode);//订单编码
    		financeTakesStockResultsModel.setOrderCreateDate(createDate);//订单日期
    		financeTakesStockResultsModel.setGoodsId(goodsId);//商品id
    		financeTakesStockResultsModel.setGoodsNo(goodsNo);//商品货号
    		financeTakesStockResultsModel.setGoodsName(goodsName);//商品名称
    		financeTakesStockResultsModel.setBarcode(barcode);//商品条码
    		financeTakesStockResultsModel.setCommbarcode(commbarcode);// 标准条码
    		financeTakesStockResultsModel.setSuperiorParentBrand(superiorParentBrand);
    		financeTakesStockResultsModel.setSuperiorParentBrandCode(superiorParentBrandCode);
    		financeTakesStockResultsModel.setSuperiorParentBrandId(superiorParentBrandId);	
    		//financeTakesStockResultsModel.setTallyingUnit(tallyingUnit);//00-托盘，01-箱，02-件
    		financeTakesStockResultsModel.setSurplusNum(changeSurplusNum);//盘盈数量
    		financeTakesStockResultsModel.setDeficientNum(changeDeficientNum);//盘亏数量
    		financeTakesStockResultsModel.setProductionDate(productionDate);//生成日期
    		financeTakesStockResultsModel.setOverdueDate(overdueDate);//失效日期
    		financeTakesStockResultsModel.setBatchNo(batchNo);//批次号
    		financeTakesStockResultsModel.setType(type);//调整类型: 1盘盈 2盘亏
    		financeTakesStockResultsModel.setIsDamage(isDamage);//是否坏品
    		financeTakesStockResultsModel.setMonth(month);//归属月份
    		financeTakesStockResultsModel.setBuId(buModel.getBuId());
    		financeTakesStockResultsModel.setBuName(buModel.getBuName());
    		financeTakesStockResultsModel.setPrice(price);// 单价
    		financeTakesStockResultsModel.setAmount(warehouseAmount);// 金额
    		financeTakesStockResultsModel.setStockLocationTypeId(stockLocationTypeId);
    		financeTakesStockResultsModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeTakesStockResultsModel.setDifferencePrice(differencePrice);
    		financeTakesStockResultsModel.setDifferenceAmount(differencePriceAmount);
    		financeTakesStockResultsModel.setOutDepotCurrency(merchant.getAccountCurrency());
   	
    		orderList.add(financeTakesStockResultsModel);
		}
    	// 循环生成
    	for (BuFinanceTakesStockResultsModel financeTakesStockResultsModel : orderList) {
    		buFinanceTakesStockResultsDao.save(financeTakesStockResultsModel);
		}
    }
    
    /**
     * 生成销毁明细
     * @param merchant
     * @param depot
     * @param month
     * @param takesStockResultsList
     * @throws Exception
     */
    public void savefinanceDestroy(Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> financeDestroyList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap,
			Map<String, BigDecimal> priceDifferenceMap)throws Exception{
    	
    	List<BuFinanceDestroyModel> orderList=new ArrayList<>();
    	
    	for (Map<String, Object> map : financeDestroyList) {
    		
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		Long orderId = (Long) map.get("id");//订单id
    		String orderCode = (String) map.get("code");//订单号
    		Timestamp sourceTime= (Timestamp) map.get("source_time");//订单归属日期
    		Timestamp orderCreateDate= (Timestamp) map.get("create_date");//订单归属日期   		
    		Long goodsId = (Long) map.get("goods_id");//商品id    	   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    		    		
    		Integer adjustTotal = (Integer) map.get("adjust_total");//调整数量
    		Date productionDate= (Date) map.get("production_date");//订单创建时间
			Date overdueDate= (Date) map.get("overdue_date");//订单创建时间
    		String type = (String) map.get("type");//'调整类型 0 调减 1 调增 2.其他'
    		String isDamage = (String) map.get("is_damage");//好坏品
    		String batchNo = (String) map.get("old_batch_no");//批次时
    		Long createUserId = (Long) map.get("create_user_id");//创建人id
    		String createUsername = (String) map.get("create_username");//创建人名称
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称
    		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}		  	
    		//箱托转化单位 //数量    
    		Integer changeNum=null;
    		if (adjustTotal!=null) {
    			changeNum = changeUnit(tallyingUnit,new BigDecimal(adjustTotal),boxToUnit,torrToUnit,merchant.getName(),goodsNo);
			}
    		
			
			BigDecimal differencePrice=null;//差异单价
			BigDecimal differencePriceAmount=null;//差异金额
   		  	if (stockLocationTypeId!=null) {
				String locationKey=stockLocationTypeId+","+barcode;
				differencePrice = priceDifferenceMap.get(locationKey);
				if (differencePrice==null)differencePrice=new BigDecimal(0);
				differencePrice=differencePrice.setScale(8,BigDecimal.ROUND_HALF_UP);
				differencePriceAmount=differencePrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
			} 
    		
    		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		 if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
 				String weightedKey=buModel.getBuId()+","+commbarcode;				
 				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
 				if (priceMap==null)priceMap=new HashMap<>();
 				price = (BigDecimal) priceMap.get("weightedPrice");
 			}
    		 if (changeNum!=null&&price!=null) {
     			warehouseAmount=price.multiply(new BigDecimal(changeNum));
 			}
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer destroyNumMap = (Integer) summaryMap.get("destroyNum");
    			if (destroyNumMap==null)destroyNumMap=0;
    			summaryMap.put("destroyNum", changeNum+destroyNumMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("destroyNum", changeNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		BuFinanceDestroyModel financeDestroyModel=new BuFinanceDestroyModel();
    		financeDestroyModel.setBrandId(brandId);
    		financeDestroyModel.setBrandName(brandName);
    		financeDestroyModel.setMerchantId(merchant.getId());
    		financeDestroyModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		financeDestroyModel.setDepotId(depot.getId());
    		financeDestroyModel.setDepotName(depot.getName());
    		financeDestroyModel.setOrderId(orderId);
    		financeDestroyModel.setOrderCode(orderCode);
    		financeDestroyModel.setOrderCreateDate(orderCreateDate);
    		financeDestroyModel.setGoodsId(goodsId);  		  
    		financeDestroyModel.setGoodsNo(goodsNo);
    		financeDestroyModel.setGoodsName(goodsName);
    		financeDestroyModel.setCommbarcode(commbarcode);
    		financeDestroyModel.setSuperiorParentBrand(superiorParentBrand);
    		financeDestroyModel.setBarcode(barcode);
    		//financeDestroyModel.setTallyingUnit(tallyingUnit);
    		financeDestroyModel.setNum(changeNum);
    		financeDestroyModel.setProductionDate(productionDate);
    		financeDestroyModel.setOverdueDate(overdueDate);
    		financeDestroyModel.setBatchNo(batchNo);
    		financeDestroyModel.setType(type);
    		financeDestroyModel.setIsDamage(isDamage);
    		financeDestroyModel.setSourceTime(sourceTime);
    		financeDestroyModel.setMonth(month);
    		financeDestroyModel.setCreater(createUserId);//创建人id
    		financeDestroyModel.setCreateName(createUsername);//创建人名称
    		financeDestroyModel.setBuId(buModel.getBuId());
    		financeDestroyModel.setBuName(buModel.getBuName());
    		financeDestroyModel.setPrice(price);
    		financeDestroyModel.setAmount(warehouseAmount);
    		financeDestroyModel.setStockLocationTypeId(stockLocationTypeId);
    		financeDestroyModel.setStockLocationTypeName(stockLocationTypeName);   		
    		financeDestroyModel.setDifferencePrice(differencePrice);
    		financeDestroyModel.setDifferenceAmount(differencePriceAmount);
    		financeDestroyModel.setOutDepotCurrency(merchant.getAccountCurrency());
    		orderList.add(financeDestroyModel);   		   		
		}
    	// 循环生成
    	for (BuFinanceDestroyModel financeDestroyModel : orderList) {
    		//生成销毁明细
    		buFinanceDestroyDao.save(financeDestroyModel);
		}
    	
    }
    


    
    /**
     * 生成(财务经销存)累计销售在途明细表
     * @param merchant
     * @param depot
     * @param month
     * @param addSaleNoshelfDetailsGXList
     * @param addSaleNoshelfDetailsDXList
     * @throws Exception
     */
    public void saveFinanceAddSaleNoshelfDetails(Map<String, Map<String, Object>> saveSummaryMap,
    		MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> addSaleNoshelfDetailsDXList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap
    		)throws Exception{
    	
    	List<BuFinanceAddSaleNoshelfDetailsModel> orderList= new ArrayList<>();
    	// 代销
    	for (Map<String, Object> map : addSaleNoshelfDetailsDXList) {
    		Long outId = (Long) map.get("id");//销售出库订单id
    		Long goodsId = (Long) map.get("goods_id");//商品id 
    		Long saleItemId = (Long) map.get("sale_item_id");//销售订单表体id
    		Long depotId = (Long) map.get("depot_id");//仓库id   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    		    		
    		BigDecimal num = (BigDecimal) map.get("num");//在途表数量
    		BigDecimal salePrice = (BigDecimal) map.get("price");// 销售订单表体价格
    		if (salePrice==null) salePrice=new BigDecimal(0);
    		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;

    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");

			}
    		
			if (outId==null)continue;
			SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(outId);
    		if (saleOutDepotModel==null)continue;
    		SaleOrderModel saleOrderModel=null;
    		if (saleOutDepotModel.getSaleOrderId()!=null) {
    			saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId()); 
			}   		   
    		if (saleOrderModel==null)continue;
    		if (!DERP_ORDER.SALEORDER_ORDERSOURCE_1.equals(saleOrderModel.getOrderSource())) continue;//订单一单是人工录入
    		//业务模式 1-购销-整批结算 2-代销 3-采销执行 4.购销-实销实结 */ 只接收 1,4, 3 类型的单
    		if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderModel.getBusinessModel())||
    			DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderModel.getBusinessModel())||
    			DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderModel.getBusinessModel())) {				
			}else {
				continue;
			}
    		
    		
    		
    		// 销售订单商品单价
    		
    		   		    		
    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		Integer saleNum = changeUnit(tallyingUnit,num,boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		
    		
    		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		String currency=null; 
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(saleNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;				
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
				sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				if (price==null)price=new BigDecimal(0);
				if (saleNum==null)saleNum=0; 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(saleNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}
    		
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){// 累计销售在途
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer addSaleNoshelfNumMap = (Integer) summaryMap.get("addSaleNoshelfNum");
    			if (addSaleNoshelfNumMap==null)addSaleNoshelfNumMap=0;
    			summaryMap.put("addSaleNoshelfNum", saleNum+addSaleNoshelfNumMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("addSaleNoshelfNum", saleNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		Timestamp deliverDate = saleOutDepotModel.getDeliverDate();
    		if (deliverDate!=null&&TimeUtils.format(deliverDate, "yyyy-MM").equals(month)) {
    			if (saveSummaryMap.containsKey(deBuKey)){//本期销售在途
        			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
        			Integer saleNoShelfNumMap = (Integer) summaryMap.get("saleNoShelfNum");
        			if (saleNoShelfNumMap==null)saleNoShelfNumMap=0;
        			summaryMap.put("saleNoShelfNum", saleNum+saleNoShelfNumMap);
        			saveSummaryMap.put(deBuKey, summaryMap);        					
    			}else {
    				Map<String, Object> summaryMap = new HashMap<>();    				
    				summaryMap.put("saleNoShelfNum", saleNum);
    				saveSummaryMap.put(deBuKey, summaryMap);
    			}
			}
    		
    		BigDecimal orderChangeAmount =null;
    		BigDecimal orderChangePrice =null;
    		if (salePrice!=null&&saleNum!=null&&saleNum!=0&&num!=null) {
    			orderChangeAmount=salePrice.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);
    			orderChangePrice=salePrice.multiply(num).divide(new BigDecimal(saleNum)).setScale(8,BigDecimal.ROUND_HALF_UP);
			}
    		// 财务经分销 销售未上架
    		BuFinanceAddSaleNoshelfDetailsModel addPurchaseNotshelf =new BuFinanceAddSaleNoshelfDetailsModel();
    		addPurchaseNotshelf.setBrandId(brandId);
    		addPurchaseNotshelf.setBrandName(brandName);
    		addPurchaseNotshelf.setMerchantId(merchant.getId());
    		addPurchaseNotshelf.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		addPurchaseNotshelf.setDepotId(depot.getId());// 仓库id
    		addPurchaseNotshelf.setDepotName(depot.getName());//仓库名称
    		addPurchaseNotshelf.setOrderId(saleOrderModel.getId());
    		addPurchaseNotshelf.setOrderCode(saleOrderModel.getCode());
    		if (DERP_ORDER.SALEANALYSIS_SALETYPE_1.equals(saleOrderModel.getBusinessModel())) {
    			addPurchaseNotshelf.setOrderType(DERP_REPORT.FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_3);//1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结
			}else if (DERP_ORDER.SALEANALYSIS_SALETYPE_4.equals(saleOrderModel.getBusinessModel())) {
				addPurchaseNotshelf.setOrderType(DERP_REPORT.FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_4);//1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结				
			}
    		addPurchaseNotshelf.setOrderCreateDate(saleOrderModel.getCreateDate());
    		addPurchaseNotshelf.setOutOrderId(saleOutDepotModel.getId());
    		addPurchaseNotshelf.setOutOrderCode(saleOutDepotModel.getCode());
    		addPurchaseNotshelf.setDeliverDate(saleOutDepotModel.getDeliverDate());
    		addPurchaseNotshelf.setGoodsId(goodsId);
    		addPurchaseNotshelf.setGoodsNo(goodsNo);
    		addPurchaseNotshelf.setGoodsName(goodsName);
    		addPurchaseNotshelf.setBarcode(barcode);
    		addPurchaseNotshelf.setCommbarcode(commbarcode);
    		addPurchaseNotshelf.setSuperiorParentBrand(superiorParentBrand);
    		//addPurchaseNotshelf.setTallyingUnit(tallyingUnit);
    		addPurchaseNotshelf.setNum(saleNum);
    		addPurchaseNotshelf.setSaleCurrency(saleOrderModel.getCurrency());
    		addPurchaseNotshelf.setSalePrice(orderChangePrice);
    		addPurchaseNotshelf.setSaleAmount(orderChangeAmount);
    		addPurchaseNotshelf.setOutDepotCurrency(currency);
    		addPurchaseNotshelf.setOutDepotPrice(price);
    		addPurchaseNotshelf.setOutDepotAmount(warehouseAmount);
    		addPurchaseNotshelf.setMonth(month);
    		addPurchaseNotshelf.setPoNo(saleOrderModel.getPoNo());
    		addPurchaseNotshelf.setCustomerId(saleOrderModel.getCustomerId());
    		addPurchaseNotshelf.setCustomerName(saleOrderModel.getCustomerName());
    		addPurchaseNotshelf.setBuId(buModel.getBuId());
    		addPurchaseNotshelf.setBuName(buModel.getBuName());
    		addPurchaseNotshelf.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		addPurchaseNotshelf.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
      		
    		orderList.add(addPurchaseNotshelf);
    		
			
		}    	
    	// 循环信息 事业部累计销售在途明细表
    	for (BuFinanceAddSaleNoshelfDetailsModel model : orderList) {
    		buFinanceAddSaleNoshelfDetailsDao.save(model);
		}
    	
    }

    
    /**
     *生成(财务经销存)
     * @param merchant
     * @param depot
     * @param month
     * @param addSaleNoshelfDetailsGXList
     * @param addSaleNoshelfDetailsDXList
     * @throws Exception
     */
    public void saveBuFinanceMoveDetails(Map<String, Map<String, Object>> saveSummaryMap,
    		MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> buFinanceMoveList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap,
    		String tag)throws Exception{
    	List<BuFinanceMoveDetailsModel> orderList= new ArrayList<>();
    	// 移库单
    	for (Map<String, Object> map : buFinanceMoveList) {
    		Long orderId = (Long) map.get("id");//订单id
    		String orderCode = (String) map.get("code");//订单号
    		String businessNo = (String) map.get("business_no");//外部单号
    		Timestamp moveDate = (Timestamp) map.get("move_date");//移库时间
    		Timestamp createDate = (Timestamp) map.get("create_date");//创建时间
    		String orderCurrency = (String) map.get("currency");
    		BigDecimal orderPrice = (BigDecimal) map.get("price");
    		BigDecimal agreementPrice = (BigDecimal) map.get("agreement_price");
    		String agreementCurrency = (String) map.get("agreement_currency");
    		String tallyingUnit = (String) map.get("tallying_unit");
    		Long depotId = (Long) map.get("depot_id");//仓库id
    		
    		Integer num = (Integer) map.get("num");//数量
    		if (num==null)num=0;
    		Long goodsId = (Long) map.get("goods_id");//商品id  
    		   		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}
    		/**
    		 * 箱托转化单位
    		 */

    		Integer changeNum = changeUnit(tallyingUnit,new BigDecimal(num),boxToUnit,torrToUnit,merchant.getName(),goodsNo);   			   		   		   		
    		if (changeNum==null) changeNum=0;
    		BigDecimal agreementAmount=null;
    		if (agreementPrice!=null) agreementAmount=agreementPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
    		
    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		String currency=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(changeNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				currency = orderCurrency;			
    			String weightedKey=buModel.getBuId()+","+commbarcode;
    			Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
    			if (priceMap==null)priceMap=new HashMap<>();
    			price = (BigDecimal) priceMap.get("weightedPrice");
				if (price==null)price=new BigDecimal(0);
				if (changeNum==null)changeNum=0; 
				warehouseAmount=price.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
    			sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				//if (price==null)price=new BigDecimal(0);
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				
			}
    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer moveOutNumMap = (Integer) summaryMap.get("moveOutNum");
    			if (moveOutNumMap==null)moveOutNumMap=0;
    			summaryMap.put("moveOutNum", changeNum.intValue()+moveOutNumMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("moveOutNum", changeNum.intValue());
				saveSummaryMap.put(deBuKey, summaryMap);
			}
	
    		// 财务经分销 销售减少在途
    		BuFinanceMoveDetailsModel moveModel =new BuFinanceMoveDetailsModel();
    		moveModel.setBrandId(brandId);
    		moveModel.setBrandName(brandName);
    		moveModel.setMerchantId(merchant.getId());
    		moveModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		moveModel.setDepotId(depot.getId());
    		moveModel.setDepotName(depot.getName());
    		moveModel.setBuId(buModel.getBuId());
    		moveModel.setBuName(buModel.getBuName());
    		moveModel.setOrderId(orderId);
    		moveModel.setOrderCode(orderCode);
    		moveModel.setExternalCode(businessNo);
    		moveModel.setOrderType(tag);//'1-移入 2-移除 ',
    		moveModel.setDeliverDate(moveDate);
    		moveModel.setMoveCreateDate(createDate);
    		moveModel.setGoodsId(goodsId);
    		moveModel.setGoodsNo(goodsNo);
    		moveModel.setGoodsName(goodsName);
    		moveModel.setBarcode(barcode);
    		moveModel.setCommbarcode(commbarcode);
    		moveModel.setSuperiorParentBrand(superiorParentBrand);
    		moveModel.setNum(changeNum.intValue());
    		moveModel.setMonth(month);
    		moveModel.setPrice(price);
    		moveModel.setCurrency(currency);
    		moveModel.setAmount(warehouseAmount);
    		moveModel.setAgreementCurrency(agreementCurrency);
    		moveModel.setAgreementPrice(agreementPrice);
    		moveModel.setAgreementAmount(agreementAmount);
    		orderList.add(moveModel);		
		}
    	
    	// 循环保存 (事业部财务经分销 本期移库
    	for (BuFinanceMoveDetailsModel model : orderList) {
    		buFinanceMoveDetailsDao.save(model);
		} 
    	orderList=null;
    }
    /**
     *生成(财务经销存)本期减少销售在途
     * @param merchant
     * @param depot
     * @param month
     * @param addSaleNoshelfDetailsGXList
     * @param addSaleNoshelfDetailsDXList
     * @throws Exception
     */
    public void saveFinanceDecreaseSaleNoshelf(Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> decreaseSaleNoshelfDXList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap
    		)throws Exception{

    	List<BuFinanceDecreaseSaleNoshelfModel> orderList= new ArrayList<>();
    	// 代销
    	for (Map<String, Object> map : decreaseSaleNoshelfDXList) {
    		Long outId = (Long) map.get("id");//销售出库订单id
    		Long goodsId = (Long) map.get("goods_id");//商品id    	   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    		    		
    		BigDecimal num = (BigDecimal) map.get("num");//在途表数量
    		Timestamp shelfDate = (Timestamp) map.get("shelf_date");//上架时间
    		BigDecimal shelfNum = (BigDecimal) map.get("shelf_num");//上架好品数量
    		BigDecimal damagedNum = (BigDecimal) map.get("damaged_num");//上架坏品数量
    		BigDecimal lackNum = (BigDecimal) map.get("lack_num");//缺货
    		String poNo = (String) map.get("po_no");//理货单位    
    		String operator = (String) map.get("operator");//上架人名称
    		Long operatorId = (Long) map.get("operator_id");//上架人id
    		BigDecimal salePrice = (BigDecimal) map.get("price");//销售订单表体价格
    		if (salePrice==null)salePrice=new BigDecimal(0);
    		Long saleItemId = (Long) map.get("sale_item_id");//销售订单表体id
    		Long depotId = (Long) map.get("depot_id");//仓库id不能为空
    		
    		
    		if (outId==null)continue;
			SaleOutDepotModel saleOutDepotModel = saleOutDepotDao.searchById(outId);
    		if (saleOutDepotModel==null)continue;
    		SaleOrderModel saleOrderModel=null;
    		if (saleOutDepotModel.getSaleOrderId()!=null) {
    			saleOrderModel = saleOrderDao.searchById(saleOutDepotModel.getSaleOrderId()); 
			}   		   
    		if (saleOrderModel==null)continue;

    		if (!DERP_ORDER.SALEORDER_ORDERSOURCE_1.equals(saleOrderModel.getOrderSource())) continue;//订单一单是人工录入
    		//业务模式 1-购销-整批结算 2-代销 3-采销执行 4.购销-实销实结 */ 只接收 1,4, 3 类型的单
    		if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderModel.getBusinessModel())||
    			DERP_ORDER.SALEORDER_BUSINESSMODEL_3.equals(saleOrderModel.getBusinessModel())||
    			DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderModel.getBusinessModel())) {				
			}else {
				continue;
			}
    		
    		
    		

    		   		
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}
			   		
    		   		    		
    		/**
    		 * 箱托转化单位
    		 */
    		//数量
    		Integer changeNum = changeUnit(tallyingUnit,num,boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		String currency=null; 
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(changeNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
				sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				if (price==null)price=new BigDecimal(0);
				if (changeNum==null)changeNum=0; 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}

    		Integer changeshelfNum = changeUnit(tallyingUnit,shelfNum,boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		Integer changedamagedNum= changeUnit(tallyingUnit,damagedNum,boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		Integer changelackNum= changeUnit(tallyingUnit,lackNum,boxToUnit,torrToUnit,merchant.getName(),goodsNo);
    		BigDecimal orderChangeAmount =null;
    		BigDecimal orderChangePrice=null;
    		if (salePrice!=null&&changeNum!=null&&changeNum.intValue()!=0&&num!=null) {
    			orderChangeAmount=salePrice.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);
    			orderChangePrice=salePrice.multiply(num).divide(new BigDecimal(changeNum)).setScale(8,BigDecimal.ROUND_HALF_UP);
			}
    		
    		/*String deBuKey=depot.getId()+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer decreaseDecreaseSaleNoshelfNummMap = (Integer) summaryMap.get("decreaseSaleNoshelfNum");
    			if (decreaseDecreaseSaleNoshelfNummMap==null)decreaseDecreaseSaleNoshelfNummMap=0;
    			summaryMap.put("decreaseSaleNoshelfNum", changeNum+decreaseDecreaseSaleNoshelfNummMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("decreaseSaleNoshelfNum", changeNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}*/
    		
    		
    		
    		// 财务经分销 销售减少在途
    		BuFinanceDecreaseSaleNoshelfModel decreaseModel =new BuFinanceDecreaseSaleNoshelfModel();
    		decreaseModel.setBrandId(brandId);
    		decreaseModel.setBrandName(brandName);
    		decreaseModel.setMerchantId(merchant.getId());
    		decreaseModel.setMerchantName(merchant.getName());
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		decreaseModel.setDepotId(depot.getId());// 仓库id
    		decreaseModel.setDepotName(depot.getName());//仓库名称
    		decreaseModel.setOrderId(saleOrderModel.getId());
    		decreaseModel.setOrderCode(saleOrderModel.getCode());

    		if (DERP_ORDER.SALEANALYSIS_SALETYPE_1.equals(saleOrderModel.getBusinessModel())) {
    			decreaseModel.setOrderType(DERP_REPORT.FINANCEDECREASESALENOSHELF_ORDERTYPE_3);
			}else if (DERP_ORDER.SALEANALYSIS_SALETYPE_4.equals(saleOrderModel.getBusinessModel())) {
				decreaseModel.setOrderType(DERP_REPORT.FINANCEDECREASESALENOSHELF_ORDERTYPE_4);
			}
    		decreaseModel.setOrderCreateDate(saleOrderModel.getCreateDate());
    		decreaseModel.setOutOrderCode(saleOutDepotModel.getCode());
    		decreaseModel.setOutOrderId(saleOutDepotModel.getId());
    		decreaseModel.setDeliverDate(saleOutDepotModel.getDeliverDate());
    		decreaseModel.setGoodsId(goodsId);
    		decreaseModel.setGoodsNo(goodsNo);
    		decreaseModel.setGoodsName(goodsName);
    		decreaseModel.setBarcode(barcode);
    		decreaseModel.setCommbarcode(commbarcode);
    		decreaseModel.setSuperiorParentBrand(superiorParentBrand);
    		//decreaseModel.setTallyingUnit(tallyingUnit);
    		decreaseModel.setNum(changeNum);
    		decreaseModel.setSaleCurrency(saleOrderModel.getCurrency());
    		decreaseModel.setSalePrice(orderChangePrice);
    		decreaseModel.setSaleAmount(orderChangeAmount);
    		decreaseModel.setOutDepotCurrency(currency);
    		decreaseModel.setOutDepotPrice(price);
    		decreaseModel.setOutDepotAmount(warehouseAmount);
    		decreaseModel.setMonth(month);
    		decreaseModel.setShelfDate(shelfDate);
    		decreaseModel.setShelfNum(changeshelfNum);
    		decreaseModel.setDamagedNum(changedamagedNum);
    		decreaseModel.setLackNum(changelackNum);
    		decreaseModel.setPoNo(poNo);    
    		decreaseModel.setShelfId(operatorId);
    		decreaseModel.setShelfName(operator);
    		decreaseModel.setBuId(buModel.getBuId());
    		decreaseModel.setBuName(buModel.getBuName()); 
    		decreaseModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		decreaseModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		orderList.add(decreaseModel);    					
		}
    	
    	// 循环保存 (事业部财务经分销)本期减少销售在途
    	for (BuFinanceDecreaseSaleNoshelfModel model : orderList) {
    		buFinanceDecreaseSaleNoshelfDao.save(model);
		}   	
    }
    

    
    /**
     * 获取(财务经销存)累计调拨在途明细表
     * @param merchant
     * @param depot
     * @param month
     * @throws Exception
     */
    public void saveFinanceAddTransferNoshelf(Map<String, Map<String, Object>> saveSummaryMap,
    		MerchantInfoModel merchant,Map<Long,DepotInfoModel> depotModelMap,
    		MerchantBuRelModel buModel,String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		List<Map<String, Object>> addTransferNoshelfList,
    		String orderSource,
			Map<String, Map<String, Object>> deBuLastSummaryMap,
			Map<String, Map<String, Object>> buLastSummaryMap,
			Map<String, Map<String, Object> > settlementPriceMap,
			Map<String, Map<String, Object>>weightedPriceMap,
			Map<String, String>currencyMap)throws Exception{

    	List<BuFinanceAddTransferNoshelfDetailsModel> orderList= new ArrayList<>();
    	//调拨在途明细表
    	for (Map<String, Object> map : addTransferNoshelfList) {
    		Long orderId = (Long) map.get("order_id");//调拨订单id
    		Long outOrderId = (Long) map.get("out_order_id");//调拨出库单id
    		String orderCode = (String) map.get("order_code");//调拨订单编码
    		String outOrderCode = (String) map.get("out_order_code");//调拨出库订单编码
    		Long outDepotId = (Long) map.get("out_depot_id");//出库仓库  id
    		String outDepotName = (String) map.get("out_depot_name");//出库仓库 名称
    		Long depotId = (Long) map.get("depot_id");//出库仓库  id   		
    		String poNo = (String) map.get("po_no");//po号  	   				    		
    		Timestamp transferDate = (Timestamp) map.get("transfer_date");//发货时间
    		Long num = (Long) map.get("num");//数量
    		Long goodsId = (Long) map.get("goods_id");//商品id   
       		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    
       		/**
           	 * 根据调拨单id查询调拨入库单
           	 * 1. 有非删除状态的调拨入库单不算在途
           	 * 2. 有删除状态的调拨入库单 算在途
           	 * 3. 没有调拨入库单算在途	
           	 * 4. 有没删除状态的调拨入库 但调拨入库时间大于当前月份算在途 (小于等当前月份不算在途)
           	 */
       		TransferInDepotModel transferInDepotQuery=new TransferInDepotModel(); 
       		transferInDepotQuery.setTransferOrderId(orderId);       		
       		List<TransferInDepotModel> transferInDepotList = transferInDepotDao.list(transferInDepotQuery);
       		TransferInDepotModel transferInDepotModel=null;
 
       		for (TransferInDepotModel inDepot : transferInDepotList) {
       			if (!inDepot.getStatus().equals(DERP_ORDER.TRANSFERINDEPOT_STATUS_006)) {
       				transferInDepotModel=inDepot;// 获取非删除状态的调拨入库单
				}				
			}
       		if (transferInDepotModel!=null&&transferInDepotModel.getTransferDate()!=null) {// 如果单不为空 并且入库时间 小于等于当前月份不算在途
       			Timestamp intransferDate = transferInDepotModel.getTransferDate();
       			String intransferMoth = TimeUtils.format(intransferDate, "yyyy-MM"); 
       			// 入库时间小于等当前月份不算在途
       			if (Timestamp.valueOf(intransferMoth+"-01 00:00:00").getTime()<=Timestamp.valueOf(month+"-01 00:00:00").getTime()) {
					continue;
				}
			}
 
       		
       		

       		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
			String goodsNo=null;
    		String goodsName=null;
    		String barcode=null;
    		String commbarcode=null;
    		//String groupCommbarcode=null;
    		Integer boxToUnit=null;
    		Integer torrToUnit=null;
    		Long brandId=null;
    		String brandName=null;
    		String superiorParentBrand=null;
    		if (merchandiseMap!=null) {
    			brandId = (Long) merchandiseMap.get("brand_id");//品牌id
    			brandName = (String) merchandiseMap.get("brand_name");//品牌名称
    			goodsNo=(String) merchandiseMap.get("goods_no");
    			goodsName=(String) merchandiseMap.get("goods_name");
    			barcode=(String) merchandiseMap.get("barcode");
    			commbarcode=(String) merchandiseMap.get("commbarcode");
    			//groupCommbarcode=(String) merchandiseMap.get("group_commbarcode");
    			boxToUnit= (Integer) merchandiseMap.get("box_to_unit");
    			torrToUnit= (Integer) merchandiseMap.get("torr_to_unit");
    			superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
			}
			   
    		//数量
    		Integer changeNum = changeUnit(tallyingUnit,new BigDecimal(num),boxToUnit,torrToUnit,merchant.getName(),goodsNo);

    		BigDecimal price=null; 	
    		BigDecimal warehouseAmount=null; 
    		BigDecimal sdWeightedPrice=null;//sd加权单价
    		BigDecimal sdWeightedAmont=null;// sd加权金额
    		String currency=null; 
    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1.equals(orderSource)) {// 标准单价   			
    			Map<String, Object> commbarcodeMap = getCommbarcodeMap(depotId,buModel.getBuId(),goodsId,barcode,deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap,new BigDecimal(changeNum));
    			price = (BigDecimal) commbarcodeMap.get("price");
    			currency = (String) commbarcodeMap.get("currency");
    			warehouseAmount=(BigDecimal) commbarcodeMap.get("amount");	
			}else if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {// 加权的计算	
				String weightedKey=buModel.getBuId()+","+commbarcode;
				Map<String, Object>priceMap=weightedPriceMap.get(weightedKey);
				if (priceMap==null)priceMap=new HashMap<>();
				price = (BigDecimal) priceMap.get("weightedPrice");
				sdWeightedPrice = (BigDecimal) priceMap.get("sdWeightedPrice");
				if (price==null)price=new BigDecimal(0);
				if (changeNum==null)changeNum=0; 
    			if (sdWeightedPrice==null)sdWeightedPrice=new BigDecimal(0);
				warehouseAmount=price.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				sdWeightedAmont=sdWeightedPrice.multiply(new BigDecimal(changeNum)).setScale(2,BigDecimal.ROUND_HALF_UP);
				currency = currencyMap.get(goodsId);
				if (StringUtils.isEmpty(currency))currency = merchant.getAccountCurrency();
			}

    		String deBuKey=depotId+","+buModel.getBuId()+","+goodsId;
    		if (saveSummaryMap.containsKey(deBuKey)){
    			Map<String, Object> summaryMap = saveSummaryMap.get(deBuKey);
    			Integer addTransferNoshelfNumMap = (Integer) summaryMap.get("addTransferNoshelfNum");
    			if (addTransferNoshelfNumMap==null)addTransferNoshelfNumMap=0;
    			summaryMap.put("addTransferNoshelfNum", changeNum+addTransferNoshelfNumMap);
    			saveSummaryMap.put(deBuKey, summaryMap);        					
			}else {
				Map<String, Object> summaryMap = new HashMap<>();    				
				summaryMap.put("addTransferNoshelfNum", changeNum);
				saveSummaryMap.put(deBuKey, summaryMap);
			}
    		// (事业部业务经销存)调拨在途明细表
    		BuFinanceAddTransferNoshelfDetailsModel addTransferNoshelfModel =new BuFinanceAddTransferNoshelfDetailsModel();
    		addTransferNoshelfModel.setBrandId(brandId);
    		addTransferNoshelfModel.setBrandName(brandName);
    		addTransferNoshelfModel.setMerchantId(merchant.getId());//商家id
    		addTransferNoshelfModel.setMerchantName(merchant.getName());//商家名称
    		DepotInfoModel depot = depotModelMap.get(depotId);
    		addTransferNoshelfModel.setInDepotId(depot.getId());// 调入仓库id
    		addTransferNoshelfModel.setInDepotName(depot.getName());//调入仓库仓库名称
    		addTransferNoshelfModel.setOutDepotId(outDepotId);//出库仓库id
    		addTransferNoshelfModel.setOutDepotName(outDepotName);//出库仓库名称
    		addTransferNoshelfModel.setOrderId(orderId);//调拨订单id
    		addTransferNoshelfModel.setOrderCode(orderCode);//调拨订单编码
    		addTransferNoshelfModel.setOutOrderId(outOrderId);//出库单id
    		addTransferNoshelfModel.setOutOrderCode(outOrderCode);//出库单编码
    		addTransferNoshelfModel.setDeliverDate(transferDate);//发货时间
    		addTransferNoshelfModel.setNum(changeNum);// 数量
    		addTransferNoshelfModel.setGoodsId(goodsId);//商品id
    		addTransferNoshelfModel.setGoodsNo(goodsNo);// 商品货号
    		addTransferNoshelfModel.setGoodsName(goodsName);//商品名称
    		addTransferNoshelfModel.setBarcode(barcode);//条码
    		addTransferNoshelfModel.setCommbarcode(commbarcode);//标准条码
    		addTransferNoshelfModel.setSuperiorParentBrand(superiorParentBrand);
    		//addTransferNoshelfModel.setTallyingUnit(tallyingUnit);
    		addTransferNoshelfModel.setMonth(month);
    		addTransferNoshelfModel.setPoNo(poNo);
    		addTransferNoshelfModel.setOutDepotCurrency(currency);
    		addTransferNoshelfModel.setOutDepotPrice(price);
    		addTransferNoshelfModel.setOutDepotAmount(warehouseAmount);
    		addTransferNoshelfModel.setBuId(buModel.getBuId());
    		addTransferNoshelfModel.setBuName(buModel.getBuName());
    		addTransferNoshelfModel.setSdOrderPrice(sdWeightedPrice);// sd 加权单价
    		addTransferNoshelfModel.setSdOrderAmount(sdWeightedAmont);// sd 加权金额
    		orderList.add(addTransferNoshelfModel);    					
		}
    	   	
    
    	// 循环保存 (事业部业务经销存)累计调拨在途明细表
    	for (BuFinanceAddTransferNoshelfDetailsModel model : orderList) {
    		buFinanceAddTransferNoshelfDetailsDao.save(model);
		}   	
    }
    
    /**
     * 
     * @param state
     * @param expMsg
     * @throws Exception
     */
    private void sendLog(String state, String expMsg,MerchantInfoModel merchant,String ownMonth)  {
   	
    	JSONObject requestMessage=new JSONObject();
        requestMessage.put("merchantId", merchant.getId());
        requestMessage.put("merchantName", merchant.getName());
        if (StringUtils.isEmpty(ownMonth)) {
        	requestMessage.put("describe", "定时器刷新事业部财务经销存");
		}else {
			requestMessage.put("describe", "页面/其他刷新财务经销存");
		}                           
        requestMessage.put("ownMonth", ownMonth);
    	
        //数据集
       MQLog mqLog=new MQLog();
       mqLog.setKeyword("R"+ TimeUtils.getNow().getTime());
       mqLog.setStartDate(System.currentTimeMillis());// 开始时间
       mqLog.setDesc("生成事业部财务进销存报表");
       mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_13201501010));//
       mqLog.setModel(DERP_LOG_POINT.POINT_13201501010_Label);
       mqLog.setEndDate(System.currentTimeMillis());//结束时间

       //消费状态
       if ("0".equals(state)) {
       	mqLog.setState(0);
       	mqLog.setExpMsg(expMsg);
		}else {
			mqLog.setState(1);//成功
			mqLog.setExpMsg("");
			
		}
       
       JSONObject jsonObject=JSONObject.fromObject(mqLog);
       jsonObject.put("requestMessage",requestMessage);
       //设置响应报文
       
       jsonObject.put("id", UUID.randomUUID().toString());
       jsonObject.put("moduleCode", LogModuleType.MODULE_REPORT.getType());
       jsonObject.put("modulCode", "1002");
       logger.info("刷新事业部财务经销存日志报文："+jsonObject.toString()+"==");

		SendResult sendResult;
		try {
			sendResult = rocketLogMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("==报文："+jsonObject.toString()+"==");
		

		
	}
    
}

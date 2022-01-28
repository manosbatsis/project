package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.PurchaseOrderDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.PurchaseReturnItemDao;
import com.topideal.dao.order.PurchaseReturnOdepotDao;
import com.topideal.dao.order.PurchaseReturnOrderDao;
import com.topideal.dao.order.PurchaseSdOrderDao;
import com.topideal.dao.order.PurchaseWarehouseDao;
import com.topideal.dao.order.PurchaseWarehouseItemDao;
import com.topideal.dao.order.SaleOrderDao;
import com.topideal.dao.order.SaleOrderItemDao;
import com.topideal.dao.order.SaleOutDepotDao;
import com.topideal.dao.order.SaleOutDepotItemDao;
import com.topideal.dao.order.SaleReturnOrderItemDao;
import com.topideal.dao.order.TransferInDepotDao;
import com.topideal.dao.order.WarehouseOrderRelDao;
import com.topideal.dao.reporting.BuFinanceAddPurchaseNotshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceAddSaleNoshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceAddTransferNoshelfDetailsDao;
import com.topideal.dao.reporting.BuFinanceDecreasePurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceDecreaseSaleNoshelfDao;
import com.topideal.dao.reporting.BuFinanceDestroyDao;
import com.topideal.dao.reporting.BuFinanceInventorySummaryDao;
import com.topideal.dao.reporting.BuFinanceMoveDetailsDao;
import com.topideal.dao.reporting.BuFinancePurchaseDamagedDao;
import com.topideal.dao.reporting.BuFinancePurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceSaleDamagedDao;
import com.topideal.dao.reporting.BuFinanceSaleShelfDao;
import com.topideal.dao.reporting.BuFinanceSdAddPurchaseNotshelfDao;
import com.topideal.dao.reporting.BuFinanceSdWarehouseDetailsDao;
import com.topideal.dao.reporting.BuFinanceTakesStockResultsDao;
import com.topideal.dao.reporting.BuFinanceWarehouseDetailsDao;
import com.topideal.dao.reporting.SdInterestPriceDao;
import com.topideal.dao.reporting.SettlementPriceDao;
import com.topideal.dao.reporting.SettlementPriceRecordDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.ExchangeRateDao;
import com.topideal.dao.system.GroupCommbarcodeDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.dao.system.MerchantBuRelDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;
import com.topideal.entity.vo.order.PurchaseOrderModel;
import com.topideal.entity.vo.order.PurchaseReturnItemModel;
import com.topideal.entity.vo.order.PurchaseReturnOrderModel;
import com.topideal.entity.vo.order.PurchaseWarehouseModel;
import com.topideal.entity.vo.reporting.BuFinanceInventorySummaryModel;
import com.topideal.entity.vo.reporting.BuFinanceSdWarehouseDetailsModel;
import com.topideal.entity.vo.reporting.BuFinanceWarehouseDetailsModel;
import com.topideal.entity.vo.reporting.SdInterestPriceModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.ExchangeRateModel;
import com.topideal.entity.vo.system.MerchantBuRelModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.entity.vo.system.MerchantRelModel;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.service.DataManageService;

import net.sf.json.JSONObject;

/**
 *事业部-财务经销存利息数据处理
 * 
 */
@Service
public class DataManageServiceImpl implements DataManageService {

	private static final Logger logger = LoggerFactory.getLogger(DataManageServiceImpl.class);

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
    private MerchantBuRelDao merchantBuRelDao;
	// 组码dao
	@Autowired
	private GroupCommbarcodeDao groupCommbarcodeDao;
	@Autowired
	private SaleReturnOrderItemDao saleReturnOrderItemDao;//销售退货商品
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
    private ExchangeRateDao exchangeRateDao;// 汇率
    @Autowired    
    private BusinessUnitDao businessUnitDao;
    @Autowired    
    private SdInterestPriceDao sdInterestPriceDao;

  	@Autowired
	private BrandParentMongoDao brandParentMongoDao;// 标准品牌
  	@Autowired
  	private PurchaseSdOrderDao purchaseSdOrderDao;
    @Autowired
    private BuFinanceSdWarehouseDetailsDao buFinanceSdWarehouseDetailsDao;
    @Autowired
    private BuFinanceSdAddPurchaseNotshelfDao buFinanceSdAddPurchaseNotshelfDao;
	@Autowired
	private RMQLogProducer rocketLogMQProducer;
    

	/**
	 * 事业部-财务经销存利息数据处理
	 * @param json
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	@Override
	public void saveDataManageReport(String json, String key, String topics, String tags) throws Exception {

		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		Integer merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
		String ownMonth = (String) jsonMap.get("month");//月份
		if (!"1000030".equals(merchantId.toString())) {
			return;
		}

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
 						logger.error(threadName+"(刷新利息)事业部财务经销存商家："+merchant.getName()+"---"+e.getMessage());
 						//发送异常日志                            
                        sendLog("0", e.getMessage(), merchant,ownMonth);
 					}finally {
 						logger.info(threadName+"(刷新利息)事业部财务经销存商家："+merchant.getName()+"---");
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
		String depotId = (String)jsonMap.get("depotId");//仓库Id
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
		Map<String, Object> depotMap = new HashMap<String, Object>();
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
		List<DepotInfoModel> depotAllList = depotInfoDao.listMerchantDepot(depotMap);
		// 获取仓库id
    	List<Long> depotIdList = getDepotIdList(depotAllList);//11这里要获取全仓
		String[] montharr = months.split(",");
		//月份循环
        for(String month:montharr){
        	if (Timestamp.valueOf("2020-12-01 00:00:00").getTime()>Timestamp.valueOf(month+"-01 00:00:00").getTime()) {
                sendLog("0", threadName+"事业部--商家名称:"+merchant.getName()+",月份："+month+"必须大于2020年12月", merchant,months);
      		
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
     	
	        	//加权单价 和标准单价处理
	        	String orderSource = merchant.getAccountPrice();//核算单价 1-标准成本单价 2-月末加权单价
	        	// 生成总表信息
	        	Map<String, Map<String, Object>> saveSummaryMap=new HashMap<>();       	
	        	// 标准单价 公共数据        	
				Map<String, Map<String, Object>> deBuLastSummaryMap=new HashMap<>();// 查询上月事业部财务经销存本期单价,金额币种 和数量  期初
				Map<String, Map<String, Object>> buLastSummaryMap=new HashMap<>();	// 去掉仓库的币种和单价
	        	//加权单价计算
				Map<String, Map<String, Object>>weightedPriceMap=new HashMap<>();
	    		//期初加权币种 getWeightedPrice方法里面赋值
	    		Map<String, String>currencyMap=new HashMap<>();  
	    		if (DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals(orderSource)) {//月末加权单价   			
	            	
	        		//用于计算加权 getWeightedPrice方法里面赋值
	        		Map<String,  Map<String, Object>>weightedSummaryMap=new HashMap<>();      		
	            	getWeightedPrice(buModel,saveSummaryMap,merchant,depotIdList,month,deBuLastSummaryMap,weightedSummaryMap,currencyMap);
	    			//本月采购总数量=本期采购结转数量(汇总)=本期采购入库数量+本期采购在途量-本期采购减少在途量+事业部移库入数量
	        		//本月采购总金额=本期采购入库金额+本期采购在途入库金额-本期采购减少在途入库金额+事业部移库入库金额
	        		//本期采购结转数量(汇总)=本期采购入库数量+本期采购在途量-本期采购减少在途量+事业部移库入数量
	    			logger.info("获取并生成:"+merchant.getName()+"，"+month+"(事业部财务经销存)加权单价明细数据");			
	    			saveDetails(buModel,saveSummaryMap,merchant,depotIdList,buModelMap,month,depotModelMap,depotId,allMerchandiseMap,weightedSummaryMap,currencyMap, groupCommbarcodeMap,
	    					orderSource,null,null,null,weightedPriceMap);		   			
	    		}else {
	    			logger.error(threadName+"--商家名称："+merchant.getName()+"month:"+month+",商家核算单据不是月末加权单价");
	    			sendLog("0", threadName+"--商家名称："+merchant.getName()+"month:"+month+",商家核算单据不是月末加权单价", merchant,month);
	    			continue;
				}
	    		
	    		// 生产事业部财务经销存总表
				saveGoodsReport(threadName,saveSummaryMap,merchant,buModel,month,
						weightedPriceMap);  
    					   		
				 			
    			
    		}//循环仓库---end
        }
		

        //发送成功日志
        sendLog("1", "", merchant, months);
		logger.info("----------------------------事业部财务进销存报表生成结束");
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
	private void saveGoodsReport(String threadName,Map<String, Map<String, Object>> saveSummaryMap,
			MerchantInfoModel merchant, MerchantBuRelModel buModel, String month,			
			Map<String, Map<String, Object>> weightedPriceMap) throws SQLException {

		BuFinanceInventorySummaryModel summaryQuery =new  BuFinanceInventorySummaryModel();
		summaryQuery.setMerchantId(merchant.getId());
		summaryQuery.setBuId(buModel.getBuId());
		summaryQuery.setMonth(month);
		List<BuFinanceInventorySummaryModel> list = buFinanceInventorySummaryDao.list(summaryQuery);
		for (BuFinanceInventorySummaryModel inventorySummary : list) {
			//Long merchantId = inventorySummary.getMerchantId();
			Long depotId = inventorySummary.getDepotId();
			Long buId = inventorySummary.getBuId();
			Long goodsId = inventorySummary.getGoodsId();
			String commbarcode = inventorySummary.getCommbarcode();
			String summaryKey=depotId+","+buId+","+goodsId;
			// 获取总表
			Map<String, Object> summaryMap = saveSummaryMap.get(summaryKey);
			if (summaryMap==null) summaryMap=new HashMap<>();
			// 获取加权单价
			String  buIdCommbarcode= buModel.getBuId()+","+commbarcode;			
			Map<String, Object>priceMap = weightedPriceMap.get(buIdCommbarcode);
			if (priceMap==null) priceMap=new HashMap<>();
			BigDecimal sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice"); 
			if (sdInterestPrice==null)sdInterestPrice=new BigDecimal(0);
		
			
			
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

			Integer endNum = inventorySummary.getEndNum();
			if (endNum==null)endNum=0;
			Integer lossOverflowNum = inventorySummary.getLossOverflowNum();
			if (lossOverflowNum==null)lossOverflowNum=0;
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
			/**--------------------利息 结束----------------------*/
			
			BuFinanceInventorySummaryModel model=new BuFinanceInventorySummaryModel();
			model.setSdInterestInitAmount(sdInterestInitAmount);
			model.setSdInterestPrice(sdInterestInitPrice);
			model.setSdInterestPurchaseEndAmount(sdInterestPurchaseEndAmount);
			model.setSdInterestTzPrice(sdInterestPrice);
			model.setSdInterestSaleEndAmount(sdInterestSaleEndAmount);
			model.setSdInterestLossOverflowAmount(sdInterestLossOverflowAmount);
			model.setSdInterestEndAmount(sdInterestEndAmount);
			model.setId(inventorySummary.getId());
			buFinanceInventorySummaryDao.modify(model);
			
			
		}
		
		
	}


	
	/**
	 * 获取仓库idList
	 * @param merchant
	 * @param month
	 * @return
	 */
	public List<Long> getDepotIdList(List<DepotInfoModel> depotList) {
		List<Long>depotIdList=new ArrayList<>();
		for (DepotInfoModel depot : depotList) {
			depotIdList.add(depot.getId());
		}
		return depotIdList;
	}
	/**
	 * 获取加权单价
	 * @return
	 */
	public void getWeightedPrice(MerchantBuRelModel buModel,Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant,List<Long> depotIdList,String month,
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
	private void saveDetails(MerchantBuRelModel buModel,Map<String, Map<String, Object>> saveSummaryMap,MerchantInfoModel merchant, List<Long> depotIdList, 
			Map<Long,BusinessUnitModel> buModelMap,String month,
			Map<Long,DepotInfoModel> depotModelMap,String depotIdTag,
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
		paramMap.put("depotListIds", depotIdList);
		paramMap.put("month", month);
		paramMap.put("buId", buModel.getBuId());
    	//-------------------------------- 采购入库明细开始---------------------------
		//采购入库
		List<Map<String, Object>> warehouseAndAnalysisMapList = purchaseWarehouseItemDao.getBuPurWarehouseAndAnalysisWeighte(paramMap);
		//采购退货
		List<Map<String, Object>> purchaseReturnOdepotMapList = purchaseReturnOdepotDao.getBuPurchaseReturnOdepot(paramMap);					
		//获取采购调整sd单
		List<Map<String, Object>> tzPurchaseSdOrdeMapList = purchaseSdOrderDao.getTzPurchaseSdOrde(paramMap);
		
		saveFinanceWarehouseDetailsWeighted(buModel,saveSummaryMap,merchant, month, warehouseAndAnalysisMapList, 
				purchaseReturnOdepotMapList,
				tzPurchaseSdOrdeMapList,
				depotModelMap,depotIdTag,
				allMerchandiseMap,weightedSummaryMap,currencyMap,groupCommbarcodeMap,orderSource,
				deBuLastSummaryMap,buLastSummaryMap,settlementPriceMap);
				
    	//-------------------------------- 采购入库明细结结束---------------------------
		
		
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
			
			
			if (groupCommbarcodeMap.containsKey(commbarcode)) {	
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

			}else {
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
			BigDecimal sdInterestPrice = (BigDecimal) priceMap.get("sdInterestPrice");

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
			String depotIdTag,
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
    		//String goodsNo = (String) map.get("goods_no");//商品货号
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位
    		BigDecimal num = (BigDecimal) map.get("num");//采购入库分析表数量
    		Timestamp relDate = (Timestamp) map.get("rel_date");// 上架时间
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
    		
    		// 根据采购订单id查询采购订单 // 如果采购入库单有商品,采购订单没有此商品 此时 查询不到对应的采购订单
    		PurchaseOrderModel purchaseOrderModel=new PurchaseOrderModel();
    		if (orderId!=null) {
    			purchaseOrderModel.setId(orderId);
    			List<PurchaseOrderModel> purchaseOrderList = purchaseOrderDao.list(purchaseOrderModel);
    			if (purchaseOrderList.size()>0) {
    				purchaseOrderModel=purchaseOrderList.get(0);
				}
			}
    		    		
    		PurchaseOrderItemModel purchaseOrderItemModel =new PurchaseOrderItemModel();
    		// 查询采购订单商品
    		if (purchaseOrderModel!=null&&purchaseOrderModel.getId()!=null) {    		
        		purchaseOrderItemModel.setPurchaseOrderId(purchaseOrderModel.getId());
        		purchaseOrderItemModel.setGoodsId(goodsId);
        		List<PurchaseOrderItemModel> purchaseOrderItemList = purchaseOrderItemDao.list(purchaseOrderItemModel);
        		if (purchaseOrderItemList.size()>1) {
        			logger.error("采购入库明细:"+merchant.getName()+"订单id"+orderId+",商品id"+goodsId+"查询出两条采购订单商品");
					throw new RuntimeException("采购入库明细:"+merchant.getName()+"订单id"+orderId+",商品id"+goodsId+"查询出两条采购订单商品");
				}
        		if (purchaseOrderItemList.size()>0) {
        			purchaseOrderItemModel=purchaseOrderItemList.get(0);
				}
			}

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
			
    		if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals("2")) {// 加权的计算
				String tgtCurrency =purchaseOrderModel.getTgtCurrency();
	    		BigDecimal tgtPrice=purchaseOrderItemModel.getTgtPrice();
	    		if (tgtPrice==null)tgtPrice=new BigDecimal(0);
	    		if (num!=null&&warehouseNum!=null&&warehouseNum!=0) {
	    			warehouseAmount=tgtPrice.multiply(num).setScale(2,BigDecimal.ROUND_HALF_UP);;
	    			price = tgtPrice.multiply(num).divide(new BigDecimal(warehouseNum),8,BigDecimal.ROUND_HALF_UP);	
				}
				getWeightedMap(buId,commbarcode,weightedSummaryMap,warehouseNum,warehouseAmount,"warehouseInAmount","warehouseInNum");
    			currency = tgtCurrency;
    			if (orderId!=null&&goodsId!=null) {
    				Map<String, Object>sdMap= getLpAndSdAmount(orderId,goodsId,warehouseNum,"1");// 采购					
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
    		// depotIdTag不为空说明是页面传来的 如果depotId和depotIdTag 不相等不生成明细
    		if (!StringUtils.isEmpty(depotIdTag)) {
				if (!depotIdTag.equals(depotId.toString())) continue;
			}
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
    		financeWarehouseDetailsModel.setRelDate(relDate);// 上架时间
    		financeWarehouseDetailsModel.setCreater(purchaseOrderModel.getCreater());
    		financeWarehouseDetailsModel.setCreateName(purchaseOrderModel.getCreateName());
    		financeWarehouseDetailsModel.setBuId(buId);
    		financeWarehouseDetailsModel.setBuName(buName);
    		financeWarehouseDetailsModel.setOrderType(DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_1);//采购入库
    		
    		financeWarehouseDetailsModel.setSdTgtAmount(sdTgtAmount);    		
    		orderList.add(financeWarehouseDetailsModel);
    		for (Map<String, Object> sdMap : sdOrderMapList) {
    			Long sdId=(Long) sdMap.get("id");
    			String sdCode=(String) sdMap.get("code");
    			String poNo=(String) sdMap.get("po_no");
    			BigDecimal tgtPrice=(BigDecimal) sdMap.get("sd_tgt_price");
    			if (tgtPrice==null)tgtPrice=new BigDecimal(0);
    			String sdTypeName=(String) sdMap.get("sd_type_name");
    			String sdSimpleName=(String) sdMap.get("sd_simple_name");    	    			
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
        		
        		orderSdList.add(sdWarehouseDetails);
			}
    		

    		
    	}
    	
    	// 采购退货出库
    	for (Map<String, Object> map : purchaseReturnOdepotMapList) {


    		//Long buId = (Long) map.get("bu_id");//采购退货订单id
    		Long depotId = (Long) map.get("depot_id");//采购退货订单id
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
    		if(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2.equals("2")) {// 加权的计算	
				String tgtCurrency =purReturnModel.getTgtCurrency();
	    		BigDecimal tgtPrice=purReturnItemModel.getTgtReturnPrice();
	    		if (tgtPrice==null)tgtPrice=new BigDecimal(0);
	    		if (num!=null&&warehouseNum!=null&&warehouseNum!=0) {
	    			warehouseAmount=tgtPrice.multiply(new BigDecimal(num)).setScale(2,BigDecimal.ROUND_HALF_UP);;
	    			price = tgtPrice.multiply(new BigDecimal(num)).divide(new BigDecimal(warehouseNum),2,BigDecimal.ROUND_HALF_UP);
				}
				getWeightedMap(buId,commbarcode,weightedSummaryMap,warehouseNum,warehouseAmount,"purReturnOutAmount","purReturnOutNum");
    			currency = tgtCurrency;
    			// 获取 本期采购LP金额 和本期采购SD金额
    			if (orderId!=null&&goodsId!=null) {
					Map<String, Object>sdMap= getLpAndSdAmount(orderId,goodsId,warehouseNum,"2");//采购退货
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
				}
			}
    		   		
    		
    		if (!StringUtils.isEmpty(depotIdTag)) {
				if (!depotIdTag.equals(depotId.toString())) continue;
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
    		financeWarehouseDetailsModel.setInDepotName(purReturnModel.getOutDepotName());//退货出库仓库
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
    		orderList.add(financeWarehouseDetailsModel);
    		for (Map<String, Object> sdMap : sdOrderMapList) {
    			Long sdId=(Long) sdMap.get("id");
    			String sdCode=(String) sdMap.get("code");
    			String sdPoNo=(String) sdMap.get("po_no");
    			BigDecimal tgtPrice=(BigDecimal) sdMap.get("sd_tgt_price");
    			if (tgtPrice==null)tgtPrice=new BigDecimal(0);
    			String sdTypeName=(String) sdMap.get("sd_type_name");
    			String sdSimpleName=(String) sdMap.get("sd_simple_name");
    			// 封装采购入库SD明细实体
	    		BuFinanceSdWarehouseDetailsModel sdWarehouseDetails=new BuFinanceSdWarehouseDetailsModel();
	    		sdWarehouseDetails.setMerchantId(merchant.getId());
	    		sdWarehouseDetails.setMerchantName(merchant.getName());
	    		sdWarehouseDetails.setInDepotId(depotId);
	    		sdWarehouseDetails.setInDepotName(purReturnModel.getOutDepotName());
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
   		
    		// depotIdTag不为空说明是页面传来的 如果depotId和depotIdTag 不相等不生成明细
    		if (!StringUtils.isEmpty(depotIdTag)) {
				if (!depotIdTag.equals(depotId.toString())) continue;
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

    		
    		PurchaseOrderItemModel purchaseOrderItemModel =new PurchaseOrderItemModel();
    		// 查询采购订单商品
    		if (purchaseOrderModel!=null&&purchaseOrderModel.getId()!=null) {    		
        		purchaseOrderItemModel.setPurchaseOrderId(purchaseOrderModel.getId());
        		purchaseOrderItemModel.setGoodsId(goodsId);
        		List<PurchaseOrderItemModel> purchaseOrderItemList = purchaseOrderItemDao.list(purchaseOrderItemModel);

        		if (purchaseOrderItemList.size()>0) {
        			purchaseOrderItemModel=purchaseOrderItemList.get(0);
				}
			}

    		// 订单单价
    		BigDecimal orderPrice = purchaseOrderItemModel.getPrice();
    		BigDecimal orderChangeAmount =new BigDecimal(0);
    		if (orderPrice!=null&&num!=null&&num!=0) {
    			orderChangeAmount=orderPrice.multiply(new BigDecimal(num));
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
    		
    		orderSdList.add(sdWarehouseDetails);

    		
    	}
    	
    	// 循环生成
    	/*for (BuFinanceWarehouseDetailsModel financeWarehouseDetailsModel : orderList) {
    		// 生成采购入库明细
    		buFinanceWarehouseDetailsDao.save(financeWarehouseDetailsModel);
		}*/
    	orderList=null;
    	//循环生成 sd采购入库明细
    	for (BuFinanceSdWarehouseDetailsModel financeSdWarehouseDetailsModel : orderSdList) {
    		if ("INT".equals(financeSdWarehouseDetailsModel.getSdTypeName())) {
    			buFinanceSdWarehouseDetailsDao.save(financeSdWarehouseDetailsModel);
			}
    		
		}
    	orderSdList=null;
		
	}

	/**
	 * 1-采购SD，2-采购退SD，3-调整SD
	 * 获取sd单价和金额 获取lp单价和金额
	 * @param orderId
	 * @param goodsId
	 * @param warehouseNum
	 * @return
	 * @throws SQLException 
	 */
	private Map<String, Object> getLpAndSdAmount(Long orderId, Long goodsId, Integer warehouseNum,String type) throws SQLException {
		String code=null;
		if ("1".equals(type)) {
			PurchaseOrderModel order = purchaseOrderDao.searchById(orderId);	
			code=order.getCode();
		}else if("2".equals(type)){
			PurchaseReturnOrderModel order = purchaseReturnOrderDao.searchById(orderId);
			code=order.getCode();
		}
		List<Map<String,Object>> sdOrderMapList=null;
		if (!StringUtils.isEmpty(code)&&goodsId!=null) {
			Map<String, Object>sdMap=new HashMap<>();
			sdMap.put("type", type);
			sdMap.put("purchaseCode", code);
			sdMap.put("goodsId", goodsId);
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
       mqLog.setDesc("生成(利息经销存)事业部财务进销存报表");
       mqLog.setPoint(132015010101L);//
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

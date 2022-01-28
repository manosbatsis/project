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
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.InventorySummaryDetailStatusEnum;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.InitInventoryDao;
import com.topideal.dao.inventory.InventoryDetailsDao;
import com.topideal.dao.inventory.MonthlyAccountDao;
import com.topideal.dao.order.LocationAdjustmentOrderDao;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.dao.order.SaleOrderDao;
import com.topideal.dao.order.SaleOutDepotDao;
import com.topideal.dao.order.SaleOutDepotItemDao;
import com.topideal.dao.order.TransferInDepotDao;
import com.topideal.dao.order.TransferOrderDao;
import com.topideal.dao.reporting.BuBusinessAddSaleNoshelfDetailsDao;
import com.topideal.dao.reporting.BuBusinessAddTransferNoshelfDetailsDao;
import com.topideal.dao.reporting.BuBusinessDecreaseSaleNoshelfDao;
import com.topideal.dao.reporting.BuBusinessInBadDetailsDao;
import com.topideal.dao.reporting.BuBusinessLocationAdjustmentDetailsDao;
import com.topideal.dao.reporting.BuBusinessOutBadDetailsDao;
import com.topideal.dao.reporting.BuBusinessTransferNoshelfDetailsDao;
import com.topideal.dao.reporting.BuInventorySummaryDao;
import com.topideal.dao.reporting.BuInventorySummaryItemDao;
import com.topideal.dao.reporting.BuLocationSummaryDao;
import com.topideal.dao.reporting.BuPurchaseNotshelfInfoDao;
import com.topideal.dao.reporting.BuSaleNotshelfInfoDao;
import com.topideal.dao.system.CommbarcodeDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.dao.system.MerchantBuRelDao;
import com.topideal.dao.system.MerchantDepotBuRelDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.inventory.InitInventoryModel;
import com.topideal.entity.vo.inventory.InventoryDetailsModel;
import com.topideal.entity.vo.inventory.MonthlyAccountModel;
import com.topideal.entity.vo.order.SaleOrderModel;
import com.topideal.entity.vo.order.SaleOutDepotModel;
import com.topideal.entity.vo.order.TransferInDepotModel;
import com.topideal.entity.vo.reporting.BuBusinessAddSaleNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuBusinessAddTransferNoshelfDetailsModel;
import com.topideal.entity.vo.reporting.BuBusinessDecreaseSaleNoshelfModel;
import com.topideal.entity.vo.reporting.BuBusinessInBadDetailsModel;
import com.topideal.entity.vo.reporting.BuBusinessLocationAdjustmentDetailsModel;
import com.topideal.entity.vo.reporting.BuBusinessOutBadDetailsModel;
import com.topideal.entity.vo.reporting.BuInventorySummaryItemModel;
import com.topideal.entity.vo.reporting.BuInventorySummaryModel;
import com.topideal.entity.vo.reporting.BuLocationSummaryModel;
import com.topideal.entity.vo.reporting.BuPurchaseNotshelfInfoModel;
import com.topideal.entity.vo.reporting.BuSaleNotshelfInfoModel;
import com.topideal.entity.vo.system.CommbarcodeModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.MerchantBuRelModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.entity.vo.system.MerchantRelModel;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.FileTaskMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.service.BuInventorySummaryReportService;

import net.sf.json.JSONObject;

/**
 * 事业部进销存报表
 * @author 杨创
 */
@Service
public class NewBuInventorySummaryReportServiceImpl implements BuInventorySummaryReportService {
    /* 打印日志 */
    private static final Logger logger = LoggerFactory.getLogger(NewBuInventorySummaryReportServiceImpl.class);

	private static final String Integer = null;

    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;
    @Autowired
    private DepotInfoDao depotInfoDao;
    @Autowired
    private MonthlyAccountDao monthlyAccountDao;
    @Autowired
    private InventoryDetailsDao inventoryDetailsDao;
    @Autowired
    private PurchaseOrderItemDao purchaseOrderItemDao;
    @Autowired
    private SaleOutDepotItemDao saleOutDepotItemDao;
    @Autowired
    private SaleOrderDao saleOrderDao;
    @Autowired
    private TransferOrderDao transferOrderDao;
    @Autowired
    private TransferInDepotDao transferInDepotDao;// 调拨入库

  	@Autowired
  	private SaleOutDepotDao saleOutDepotDao;//销出库单
  	@Autowired
  	private CommbarcodeDao commbarcodeDao; //标准条码管理表    
	@Autowired
	private MerchantDepotBuRelDao merchantDepotBuRelDao;
	@Autowired
	private MerchantBuRelDao merchantBuRelDao;
	@Autowired
	private BuInventorySummaryDao buInventorySummaryDao;
	@Autowired
	private BuInventorySummaryItemDao buInventorySummaryItemDao;
    @Autowired
    private BuBusinessAddSaleNoshelfDetailsDao buBusinessAddSaleNoshelfDetailsDao;//(事业部业务经销存)累计销售在途明细表
    @Autowired
    private BuBusinessTransferNoshelfDetailsDao buBusinessTransferNoshelfDetailsDao;//(事业部业务经销存)调拨在途明细表    
    @Autowired
    private BuBusinessInBadDetailsDao buBusinessInBadDetailsDao;//(事业部业务经销存)来货残次明细
    @Autowired
    private BuBusinessOutBadDetailsDao buBusinessOutBadDetailsDao;//(事业部业务经销存)来货残次明细
    @Autowired
    private BuSaleNotshelfInfoDao buSaleNotshelfInfoDao;
    @Autowired
    private BuPurchaseNotshelfInfoDao buPurchaseNotshelfInfoDao;
    @Autowired
    private BuBusinessDecreaseSaleNoshelfDao buBusinessDecreaseSaleNoshelfDao; //(事业部业务经分销)本期减少销售在途明细表 
    @Autowired
    private BuBusinessAddTransferNoshelfDetailsDao buBusinessAddTransferNoshelfDetailsDao;//(事业部业务经销存)累计调拨在途明细表
    @Autowired
    private BuBusinessLocationAdjustmentDetailsDao buBusinessLocationAdjustmentDetailsDao;//(事业部业务经销存)库位类型调整明细表  
    @Autowired
    private LocationAdjustmentOrderDao locationAdjustmentOrderDao;//库位调整单
    @Autowired
    private BuLocationSummaryDao buLocationSummaryDao;//(事业部业务经销存)事业部库位经销存总报表
    @Autowired
    private InitInventoryDao initInventoryDao;//期初
    @Autowired
    private FileTaskMongoDao fileTaskDao;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;// 标准品牌
	@Autowired
	private RMQLogProducer rocketLogMQProducer;




    @SuppressWarnings("unchecked")
	public boolean saveBuSummaryReport(String json, String topics, String tags) throws Exception {
        try {
            JSONObject jsonData = JSONObject.fromObject(json);
            Map<String, Object> jsonMap = jsonData;
            Integer merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
            String ownMonth = (String) jsonMap.get("month");// 月份

            // 查询所有商家,若指定了商家则只查本商家
            MerchantInfoModel model = new MerchantInfoModel();
            if (merchantId != null && merchantId.longValue() > 0) {
                model.setId(Long.valueOf(merchantId));
            }
            model.setIsProxy(DERP_SYS.MERCHANTINFO_ISPROXY_0);// 是否代理 0商家 1 代理商
            List<MerchantInfoModel> merchantList = merchantInfoDao.list(model);

            //加载品牌信息 key=品牌id
            Map<String, CommbarcodeModel> brandMap = getBrand();
            // 获取标准品牌
            Map<Long, BrandParentMongo> brandParentMap = getBrandParent();

            /**
             * 多线程生成每个商家报表
             **/
            ExecutorService pool = Executors.newFixedThreadPool(5);
            for (int i = 0; i < merchantList.size(); i++) {
                MerchantInfoModel merchant = merchantList.get(i);
                String threadName = merchant.getId() + "--------thread" + (i + 1);
                Runnable run = new Runnable() {
                    public void run() {
                        try {
                            synsMerchant(threadName, jsonMap, merchant,brandMap,brandParentMap);
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error(threadName+"事业部业务经销存商家："+merchant.getName()+"---"+e.getMessage());
                            //发送异常日志                            
                            sendLog("0", e.getMessage(), merchant,ownMonth);
                        }finally {
     						//删除财务刷新任务
     						Map<String,Object> delTaskMap = new HashMap<String, Object>();
     						delTaskMap.put("merchantId", merchant.getId());
     						delTaskMap.put("ownMonth", ownMonth);	
     						delTaskMap.put("taskType","SXSYBYW");//任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SXCW-刷新财务
     						// 月份不为空说明来自页面删除任务
     						if (!StringUtils.isEmpty(ownMonth)) {
                                fileTaskDao.remove(delTaskMap);
                            }
    					}
                    }
                };
                pool.execute(run);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return true;
    }

    public void synsMerchant(String threadName, Map<String, Object> jsonMap,
                             MerchantInfoModel merchant, Map<String, CommbarcodeModel> brandMap,
                             Map<Long, BrandParentMongo> brandParentMap) throws Exception {
    	String depotIds = (String) jsonMap.get("depotIds");// 仓库Ids
        String months = (String) jsonMap.get("month");// 月份
        String flashForward = (String) jsonMap.get("flashForward");//true-刷新已结转
        String buId = (String) jsonMap.get("buId");// 月份
        //计算要刷新的月份
        if (StringUtils.isEmpty(months)) {
            //若没有指定月份则取当前时间前一天日期近二个月月份,定时器刷新未结转的，本月、上月
            months = TimeUtils.getLastTwoMonthsByNow();
        }
         
        //获取商家及代理商家的所有商品条码、货品、标准品牌、价格
        Map<Long, Map<String, Object>> allMerchandiseMap = getAllMerchandiseList(threadName, merchant, brandMap,brandParentMap);
        if (allMerchandiseMap == null || allMerchandiseMap.size() <= 0) {
            logger.info(threadName + "商家ID:" + merchant.getId() + ",商家名称：" + merchant.getName() + ",货品数量为0,结束执行");
            sendLog("0", "商家ID:" + merchant.getId() + ",商家名称：" + merchant.getName() + ",货品数量为0,结束执行", merchant, months);
            return;
        }
        logger.info(threadName + "商家ID:" + merchant.getId() + ",商家名称：" + merchant.getName() + ",货品数量:" + allMerchandiseMap.size());

        //查询商家的所有仓库 若指定了仓库则只查本仓库
        Map<String, Object> depotMap = new HashMap<String, Object>();
        depotMap.put("merchantId", merchant.getId());
        if (!StringUtils.isEmpty(depotIds)) {
        	String[] depotIdArrs = depotIds.split(",");
        	List<Long> depotListIds=new ArrayList<Long>();
        	for (String idStr : depotIdArrs) {
        		if(!StringUtils.isEmpty(idStr))depotListIds.add(Long.valueOf(idStr));
			}
            depotMap.put("depotIds",depotListIds );
        }
        List<DepotInfoModel> depotList = depotInfoDao.listMerchantDepot(depotMap);
        if (depotList == null || depotList.size() < 1) {
            logger.info(threadName + "商家ID:" + merchant.getId() + ",商家名称：" + merchant.getName() + ",仓库数量为0,结束执行");
            sendLog("0", threadName + "商家ID:" + merchant.getId() + ",商家名称：" + merchant.getName() + ",仓库数量为0,结束执行", merchant,months);
            return;
        }

        /** 循环生成报表数据-----------循环仓库*********************start */
        for (DepotInfoModel depot : depotList) {
        				
        	Map<String, Object> merchAndDepotBuMap =new HashMap<>();
			merchAndDepotBuMap.put("merchantId", merchant.getId());
			merchAndDepotBuMap.put("depotId", depot.getId());
			merchAndDepotBuMap.put("buId", buId);
			List<Map<String, Object>> merchAndDepotBuMapList = merchantDepotBuRelDao.getBuMerchAndDepotBuMap(merchAndDepotBuMap);
			for (Map<String, Object> buMap : merchAndDepotBuMapList) {
				Long buMapId = (Long) buMap.get("bu_id");
				if (buMapId==null) {
					logger.info(threadName+"事业部--商家名称:"+merchant.getName()+",仓库Id："+depot.getId()+"事业部id不能为空");
		            sendLog("0", threadName+"事业部--商家名称:"+merchant.getName()+",仓库Id："+depot.getId()+"事业部id不能为空", merchant,months);
					continue;				
				}
				MerchantBuRelModel merchantBuRelModel=new MerchantBuRelModel();
				merchantBuRelModel.setMerchantId(merchant.getId());
				merchantBuRelModel.setBuId(buMapId);
				merchantBuRelModel.setStatus("1");
				List<MerchantBuRelModel> merchantBuRelList = merchantBuRelDao.list(merchantBuRelModel);
				if (merchantBuRelList.size()!=1) {
					logger.info(threadName+"事业部--商家名称:"+merchant.getName()+",仓库Id："+depot.getId()+"商家事业部中间表 为空或者有多条 事业部id:"+buMapId);
		            sendLog("0", threadName+"事业部--商家名称:"+merchant.getName()+",仓库Id："+depot.getId()+"商家事业部中间表 为空或者有多条 事业部id:"+buMapId, merchant,months);
					continue;
				}
				// 事业部中间表
				MerchantBuRelModel buModel = merchantBuRelList.get(0);
				String[] montharr = months.split(",");
	        		        		            
	            /**-----------循环月份*********************start */
		        for (String month : montharr) {
	            	if (Timestamp.valueOf("2020-05-01 00:00:00").getTime()>Timestamp.valueOf(month+"-01 00:00:00").getTime()) {
	            		if (!("1000000544".equals(merchant.getTopidealCode())||"1000005377".equals(merchant.getTopidealCode())||"1000000204".equals(merchant.getTopidealCode()))) {// 卓普新是新商家不用过滤
	            			logger.info(threadName+"--商家名称:"+merchant.getName()+",仓库Id："+depot.getId()+"月份："+month+"必须大于5月");
	    		            sendLog("0", threadName+"--商家名称:"+merchant.getName()+",仓库Id："+depot.getId()+"月份："+month+"必须大于5月", merchant,month);
	            			continue;
	    				}
					}
	                //查询仓库本月结转单状态是否已结转
	                MonthlyAccountModel accountModel = new MonthlyAccountModel();
	                accountModel.setMerchantId(merchant.getId());
	                accountModel.setDepotId(depot.getId());
	                accountModel.setSettlementMonth(month);
	                accountModel = monthlyAccountDao.searchByModel(accountModel);
	
	                // 状态：1未转结 2 已转结
	                if (accountModel != null && accountModel.getState().equals(DERP_INVENTORY.MONTHLYACCOUNT_STATE_2) &&
	                        (StringUtils.isEmpty(flashForward) || flashForward.equals("false"))) {
	                    // 计算结转时间跟当前时间相差天数
	                    int dayNum = TimeUtils.daysBetween(accountModel.getSettlementDate(), new Date());
	                    if (dayNum > 1) {
	                        logger.info(threadName + "商家ID:" + merchant.getId() + ",仓库Id：" + depot.getId() + "月份：" + month + "已结转,结束执行");
	                        if (!StringUtils.isEmpty(months)&&!StringUtils.isEmpty((String) jsonMap.get("month"))) {// 非定时器来的记录已经月结日志
		                        sendLog("0", threadName + "商家ID:" + merchant.getId() + ",仓库Id：" + depot.getId() + "月份：" + month + "已结转,结束执行", merchant,month);	
	                        }
	                        continue;
	                    }
	                }
	                
	                
	                
	                //统计要生成总表的商品					
					Map<String, Map<String, Object>>itemSummaryMap=new HashMap<>();
					// 统计要生成的事业部库位经销存总报表 数据
					Map<String, Map<String, Object>> locationAdjustmentMap=new HashMap<String, Map<String,Object>>();
	                // 清空商家、仓库、货品在本月的报表数据
	                Map<String, Object> delMap = new HashMap<String, Object>();
	                delMap.put("merchantId", merchant.getId());
	                delMap.put("depotId", depot.getId());
	                delMap.put("month", month);
	                delMap.put("buId", buModel.getBuId());
	                //buSaleNotshelfInfoDao.delBuDepotMonthReport(delMap);	                
	                buPurchaseNotshelfInfoDao.delBuDepotMonthReport(delMap);
	                // 清空事业部业务经销存表体 出入库详情 损益详情
	                buInventorySummaryItemDao.delBuDepotMonthReport(delMap);	                
	                buInventorySummaryDao.delBuDepotMonthReport(delMap);

	                //公共查询条件
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("merchantId", merchant.getId());
					paramMap.put("depotId", depot.getId());
					paramMap.put("month", month);
					paramMap.put("buId", buModel.getBuId());
					
					Map<String, Object> upMonthParamMap = new HashMap<String, Object>();
					upMonthParamMap.put("merchantId", merchant.getId());
					upMonthParamMap.put("depotId", depot.getId());
					upMonthParamMap.put("month", TimeUtils.getUpMonth(month));
					upMonthParamMap.put("buId", buModel.getBuId());
					/**
					 * 1.查询上月事业部业务经销存是否存在 存在   ,存在取上月期末 ,不存在取查询之前是否有过事业部业务经销存记录
					 * 有,之后商品期初为0 没有 查询期初数据
					 * 2. 如果取期初数据 要把 期初的商品算进去
					 */	
					//获取总表这些数据monthBeginSum,monthBeginSumzc,monthBeginDamagedNum
					getInitNum(merchant,depot,buModel,month,itemSummaryMap);	
					// 获取来货短缺数据
					getBuLackNumSum(merchant,depot,buModel,month,allMerchandiseMap,paramMap,itemSummaryMap);
					/**-------------------------------经分销明细生成开始-------------------------------**/
	                // 生成事业部业务经销存出入库详情  获取总表这些数据 monthOutStorageSum,monthInStorageSum	
					logger.info("正在生成出入库明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
	                saveSummaryItem(merchant,depot, buModel,month,allMerchandiseMap,paramMap,itemSummaryMap, locationAdjustmentMap);	                
	                // 查询事业部业务经销存损益详情    获取总表这些数据 profitlossGoodInSum,profitlossGoodOutSum 好品损益出入
	                //profitlossDamagedInSum, profitlossDamagedOutSum;  坏品损益出入	
					logger.info("正在生成坏品损益出入明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
	                saveProfitLossSummaryItem(merchant,depot,buModel,month,allMerchandiseMap, paramMap,itemSummaryMap,locationAdjustmentMap);
	                // 事业部业务经销存累计采购在途 获取总表这些数据 purchaseNotshelfNum
	                savePurchaseNotshelfInfo(merchant,depot,buModel,month,allMerchandiseMap, paramMap,itemSummaryMap,locationAdjustmentMap);          
             
	                // 修改业务经销存详情的 po号合同
					logger.info("正在修改po号"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
	                updateBuItemNo(merchant,depot,buModel,month);
	                //1.删除 (事业部业务经销存)累计销售在途明细表
	                logger.info("清除:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经销存)累计销售在途明细表");
	                buBusinessAddSaleNoshelfDetailsDao.delBuBusinessAddSaleNoshelfDetails(paramMap);
					// 获取(事业部财务经销存) (事业部业务经销存)共用累计销售在途明细表购销
					List<Map<String, Object>> addSaleNoshelfDetailsDXList = saleOutDepotItemDao.getBuFinanceAddSaleNoshelfDetailsDX(paramMap);
	                //生成(财务经销存)累计销售在途明细
					logger.info("生成:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经销存)累计销售在途明细表");
					saveBusinessAddSaleNoshelfDetails(merchant,depot,buModel,month,addSaleNoshelfDetailsDXList,allMerchandiseMap,itemSummaryMap);
					//清除缓存
					addSaleNoshelfDetailsDXList=null;	                					
	                //2.删除 (事业部业务经分销)本期减少销售在途明细表
	                logger.info("清除:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经分销)本期减少销售在途明细表");  
	                buBusinessDecreaseSaleNoshelfDao.delBuBusinessDecreaseSaleNoshelf(paramMap);
					// 获取(事业部财务经销存)(业务经销存)共用 本期减少销售在途 代销
					List<Map<String, Object>> decreaseSaleNoshelfDXList = saleOutDepotItemDao.getBuFinanceDecreaseSaleNoshelfDX(paramMap);				
	                //生成(事业部业务经分销)本期减少销售在途明细表
					logger.info("生成:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经分销)本期减少销售在途明细表");
					saveBusinessDecreaseSaleNoshelf(merchant,depot,buModel,month,decreaseSaleNoshelfDXList,allMerchandiseMap);
					//清除缓存
					decreaseSaleNoshelfDXList=null;					
					//4.删除(事业部业务经销存)累计调拨在途明细表
					buBusinessAddTransferNoshelfDetailsDao.delBuBusinessAddTransferNoshelf(paramMap);					
					// 获取(业务经销存)累计调拨在途明细表
					List<Map<String, Object>> businessAddTransferNoshelfList = transferOrderDao.getBuBusinessAddTransferNoshelfDetails(paramMap);
					logger.info("正在生成累计调拨在途明细表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
					saveBusinessAddTransferNoshelf(merchant, depot,buModel, month, businessAddTransferNoshelfList,allMerchandiseMap,itemSummaryMap,locationAdjustmentMap);
					businessAddTransferNoshelfList=null;					
					//5.来货残次：取本月采购入库中残次品+调拨入库单中坏品数量+退货入库单中坏品数量+上架入库单中坏品数量，+电商订单退货残次量 正数，按入库时间归属月份
					//删除(事业部业务经销存)来货残次
					logger.info("清除:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经销存)来货残次明细");
					buBusinessInBadDetailsDao.delBuBusinessInBad(paramMap);
					List<InventoryDetailsModel> inbadDetails = inventoryDetailsDao.getInbadDetails(paramMap);
					logger.info("正在生成来货残次明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
					saveBuBsinessInBad(merchant, depot, buModel, month,inbadDetails,allMerchandiseMap,itemSummaryMap,locationAdjustmentMap);
					inbadDetails=null;

					//6.出库残次：取销售出库单中坏品出库量 + 调拨出库单中坏品出库量 + 销售退货出库单中坏品数量 + 采购退货出库单中坏品出库量。正数显示。
					logger.info("清除:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经销存)出库残次明细");
					//删除 (事业部业务经销存)出库残次
					buBusinessOutBadDetailsDao.delBuBusinessOutBad(paramMap);
					List<InventoryDetailsModel> outBadDetails = inventoryDetailsDao.getOutbadDetails(paramMap);
					// 生成
					logger.info("正在生成出库残次明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
					saveBuBsinessOutBad(merchant,depot,buModel,month,outBadDetails,allMerchandiseMap,itemSummaryMap,locationAdjustmentMap);										
					outBadDetails=null;	
					
					
					//7.库位类型调整明细表：取调整归属月份为报表当月且状态为已审核的库位调整单（新）
					logger.info("清除:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经销存)库位类型调整明细表");
					//删除 (事业部业务经销存)库位类型调整明细表
					buBusinessLocationAdjustmentDetailsDao.delBuBusinessLocationAdjustmentDetails(paramMap);
					
					List<Map<String, Object>>locationAdjustmentOrderMapList=locationAdjustmentOrderDao.getLocationAdjustmentOrder(paramMap);
					// 生成库位类型调整明细表
					logger.info("正在生成库位类型调整明细表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
					saveBuBusinessLocationAdjustment(merchant,depot,buModel,month,locationAdjustmentOrderMapList,locationAdjustmentMap);										
					locationAdjustmentOrderMapList=null;
					
					//------------------------
					//8.生成库位进销存汇总表
					logger.info("清除:"+merchant.getName()+"，"+depot.getName()+month+"(事业部业务经销存)生成库位进销存汇总表");
					buLocationSummaryDao.delBuLocationSummary(paramMap);
					logger.info("正在生成事业部库位进销存汇总表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
					saveBuLocationSummary(merchant, depot, buModel, month,upMonthParamMap,allMerchandiseMap,itemSummaryMap,locationAdjustmentMap);									
					locationAdjustmentMap=null;
					
					
					// 生成事业部业务经销存总表	
					logger.info("正在生成事业部业务经销存总表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month);
					saveSummary(merchant, depot, buModel, month,allMerchandiseMap,itemSummaryMap);									
					logger.info("生成 事业部业务经销存结束"+merchant.getName()+",仓库"+depot.getName()+",事业部"+buModel.getBuName()+",月份"+month);

		        }
            /**-----------循环月份*********************end */
			
			}
        }
        //发送成功日志
        sendLog("1", "", merchant, months);
        /**-----------循环仓库*********************end */
        logger.info(merchant.getId() + merchant.getName() + "========================end事业部业务经销存生成结束");
      
    }
    
    /**
     * 生成库位进销存汇总表
     * @param merchant
     * @param depot
     * @param buModel
     * @param month
     * @param allMerchandiseMap
     * @param itemSummaryMap
     * @throws SQLException 
     */
    private void saveBuLocationSummary(MerchantInfoModel merchant, DepotInfoModel depot, MerchantBuRelModel buModel,
			String month, 
			Map<String, Object>  upMonthParamMap,
			Map<Long, Map<String, Object>> allMerchandiseMap,
			Map<String, Map<String, Object>> itemSummaryMap,Map<String, Map<String, Object>> locationAdjustmentMap) throws SQLException {
    	// 获取期初
    	List<Map<String, Object>> locationSummaryInit = buLocationSummaryDao.getLocationSummaryInit(upMonthParamMap);
    	for (Map<String, Object> map : locationSummaryInit) {
    		String barcode = (String) map.get("barcode");
    		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");
    		String stockLocationTypeName = (String) map.get("stock_location_type_name");
    		BigDecimal monthBeginSum = (BigDecimal) map.get("month_end_num");    		
    		BigDecimal monthBeginSumzc = (BigDecimal) map.get("month_end_normal_num");
    		BigDecimal monthBeginDamagedNum = (BigDecimal) map.get("month_end_damaged_num");
    		String commbarcode = (String) map.get("commbarcode");
    		String goodsName = (String) map.get("goods_name");
    		Long brandId = (Long) map.get("brand_id");   		
    		String brandName = (String) map.get("brand_name");
    		String superiorParentBrand = (String) map.get("superior_parent_brand");
    		String unit = (String) map.get("unit");
    		if (monthBeginSum==null) monthBeginSum=new BigDecimal(0);
    		if (monthBeginSumzc==null) monthBeginSumzc=new BigDecimal(0);
    		if (monthBeginDamagedNum==null) monthBeginDamagedNum=new BigDecimal(0);
    		if (stockLocationTypeId==null) continue;
    		String locationKey=barcode+","+stockLocationTypeId+","+unit;     		
    		if (locationAdjustmentMap.containsKey(locationKey)) {
    			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(locationKey);       			

    			Integer monthBeginSumMap = (Integer) locationKeyMap.get("monthBeginSum");
    			Integer monthBeginSumzcMap = (Integer) locationKeyMap.get("monthBeginSumzc");
    			Integer monthBeginDamagedNumMap = (Integer) locationKeyMap.get("monthBeginDamagedNum");
				if (monthBeginSumMap==null) monthBeginSumMap=0;
				if (monthBeginSumzcMap==null) monthBeginSumzcMap=0;
				if (monthBeginDamagedNumMap==null) monthBeginDamagedNumMap=0;
				locationKeyMap.put("monthBeginSum", monthBeginSum.intValue()+monthBeginSumMap);
				locationKeyMap.put("monthBeginSumzc", monthBeginSumzc.intValue()+monthBeginSumzcMap.intValue());
				locationKeyMap.put("monthBeginDamagedNum", monthBeginDamagedNum.intValue()+monthBeginDamagedNumMap.intValue());
				locationAdjustmentMap.put(locationKey, locationKeyMap);    
			}else {
				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
				
				locationKeyMap.put("monthBeginSum", monthBeginSum.intValue());
				locationKeyMap.put("monthBeginSumzc", monthBeginSumzc.intValue());
				locationKeyMap.put("monthBeginDamagedNum",monthBeginDamagedNum.intValue());
				locationKeyMap.put("goodsName", goodsName);
				locationKeyMap.put("brandId", brandId);
				locationKeyMap.put("brandName", brandName);
				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
				locationKeyMap.put("commbarcode", commbarcode);    			
				locationKeyMap.put("stockLocationTypeId", stockLocationTypeId);
				locationKeyMap.put("stockLocationTypeName", stockLocationTypeName);
				locationKeyMap.put("unit", unit);
				locationAdjustmentMap.put(locationKey, locationKeyMap);
			}

		}
    	locationSummaryInit=null;
    	
      	Set<String> locationKeySet = locationAdjustmentMap.keySet();
    	for (String locationKey : locationKeySet) {
    		Map<String, Object> locationMap = (Map<String, Object>) locationAdjustmentMap.get(locationKey);
    		Integer monthBeginSum=(Integer) locationMap.get("monthBeginSum");//本月期初库存
         	Integer monthBeginSumzc=(Integer) locationMap.get("monthBeginSumzc");//本月正常品期初库存
         	Integer monthBeginDamagedNum=(Integer) locationMap.get("monthBeginDamagedNum");//本月残次品期初库存量
         	Integer monthInStorageSum=(Integer) locationMap.get("monthInStorageSum");// 本月入库数量
         	Integer monthOutStorageSum=(Integer) locationMap.get("monthOutStorageSum");// 本月出库数量
         	Integer inDamagedNum=(Integer) locationMap.get("inDamagedNum");//来货短缺
         	Integer outDamagedNum=(Integer) locationMap.get("outDamagedNum");//出库残损         	
         	Integer saleOutNum=(Integer) locationMap.get("saleOutNum");// 本月销售未确认数量
         	Integer purchaseNotshelfNum=(Integer) locationMap.get("purchaseNotshelfNum");// 本月采购未上架数量   
         	Integer profitlossGoodInSum=(Integer) locationMap.get("profitlossGoodInSum");// 好品损益入
         	Integer profitlossGoodOutSum=(Integer) locationMap.get("profitlossGoodOutSum");// 好品损益出 
         	Integer profitlossDamagedInSum=(Integer) locationMap.get("profitlossDamagedInSum");// 坏品损益入  
         	Integer profitlossDamagedOutSum=(Integer) locationMap.get("profitlossDamagedOutSum");// 坏品损益出          	
         	Integer monthTransferNotshelfNum=(Integer) locationMap.get("monthTransferNotshelfNum");// 本期调拨在途
         	Integer monthInBadNum=(Integer) locationMap.get("monthInBadNum");// 本期入库残次
         	Integer monthOutBadNum=(Integer) locationMap.get("monthOutBadNum");// 本期出库残次     
         	Integer adjustNumzcMap = (java.lang.Integer) locationMap.get("adjustNumzc");
         	Integer adjustNumDamagedMap = (java.lang.Integer) locationMap.get("adjustNumDamaged");
         	 
         	String[]keyArr = locationKey.split(",");
    		String barcode = keyArr[0];
         	

         	// 上面字段如果未空设置为0 如果都未0不存
         	if (monthBeginSum==null) monthBeginSum=0;if (monthBeginSumzc==null) monthBeginSumzc=0;if (monthBeginDamagedNum==null) monthBeginDamagedNum=0;
         	if (monthInStorageSum==null) monthInStorageSum=0;if (monthOutStorageSum==null) monthOutStorageSum=0;
         	if (inDamagedNum==null) inDamagedNum=0;if (outDamagedNum==null) outDamagedNum=0;        
         	if (saleOutNum==null) saleOutNum=0;if (purchaseNotshelfNum==null) purchaseNotshelfNum=0;
         	if (profitlossGoodInSum==null) profitlossGoodInSum=0;if (profitlossGoodOutSum==null) profitlossGoodOutSum=0;
         	if (profitlossDamagedInSum==null) profitlossDamagedInSum=0;if (profitlossDamagedOutSum==null) profitlossDamagedOutSum=0;
         	if (monthTransferNotshelfNum==null) monthTransferNotshelfNum=0;
         	if (monthInBadNum==null) monthInBadNum=0;if (monthOutBadNum==null) monthOutBadNum=0;
         	if (adjustNumzcMap==null) adjustNumzcMap=0;
         	if (adjustNumDamagedMap==null) adjustNumDamagedMap=0;
         	Long brandId = (Long) locationMap.get("brandId");
         	String brandName = (String) locationMap.get("brandName");
         	String superiorParentBrand = (String) locationMap.get("superiorParentBrand");
         	String commbarcode = (String) locationMap.get("commbarcode");
         	String stockLocationTypeName = (String) locationMap.get("stockLocationTypeName");
         	Long stockLocationTypeId = (Long) locationMap.get("stockLocationTypeId");
         	String goodsName = (String) locationMap.get("goodsName");
         	String unit = (String) locationMap.get("unit");
         	
         	Integer profitlossGoodNum=profitlossGoodInSum-profitlossGoodOutSum;// 好品损益
         	Integer profitlossDamagedNum=profitlossDamagedInSum-profitlossDamagedOutSum;//坏品损益        		      	
         	Integer profitLossSum=profitlossGoodNum+profitlossDamagedNum;// 损益
         	Integer adjustNum=adjustNumzcMap+adjustNumDamagedMap;
         	//本月期末量：等于本月期初量+本月入库量-本月出库量+损益+本月调整量；
         	Integer monthEndSum=monthBeginSum.intValue()+monthInStorageSum.intValue()-monthOutStorageSum.intValue()+profitLossSum.intValue()+adjustNum.intValue();//本月期末        	        	
         	//好品期末量：等于本月正常品期初库存+本月入库量-本月出库量-来货残次+出库残次+好品损益+本月调整量
         	Integer monthEndSumzc=monthBeginSumzc.intValue()+monthInStorageSum.intValue()-monthOutStorageSum.intValue()-monthInBadNum.intValue()+monthOutBadNum.intValue()+profitlossGoodNum.intValue()+adjustNumzcMap.intValue();//本月正常品期末库存
         	//坏品期末量：等于本月坏品期初量+来货残次-出库残次+坏品损益+本月调整量
         	Integer monthEndDamagedNum=monthBeginDamagedNum.intValue()+monthInBadNum.intValue()-monthOutBadNum.intValue()+profitlossDamagedNum.intValue()+adjustNumDamagedMap.intValue();// 本月残次品	
         	
         	BuLocationSummaryModel locationSummary=new BuLocationSummaryModel();
         	locationSummary.setMerchantId(merchant.getId());
         	locationSummary.setMerchantName(merchant.getName());
         	locationSummary.setDepotId(depot.getId());
         	locationSummary.setDepotName(depot.getName());
         	locationSummary.setBuId(buModel.getBuId());
         	locationSummary.setBuName(buModel.getBuName());
         	locationSummary.setMonth(month);
         	locationSummary.setBarcode(barcode);
         	locationSummary.setGoodsName(goodsName);
         	locationSummary.setBrandId(brandId);
         	locationSummary.setBrandName(brandName);
         	locationSummary.setSuperiorParentBrand(superiorParentBrand);
         	locationSummary.setCommbarcode(commbarcode);
         	locationSummary.setStockLocationTypeId(stockLocationTypeId);
         	locationSummary.setStockLocationTypeName(stockLocationTypeName);
         	locationSummary.setMonthInstorageNum(monthInStorageSum);
         	locationSummary.setMonthOutstorageNum(monthOutStorageSum);
         	locationSummary.setMonthProfitlossNum(profitLossSum);
         	locationSummary.setMonthAdjustmentNum(adjustNum);
         	locationSummary.setMonthBeginNum(monthBeginSum);
         	locationSummary.setMonthBeginNormalNum(monthBeginSumzc);
         	locationSummary.setMonthBeginDamagedNum(monthBeginDamagedNum);
         	locationSummary.setMonthEndNum(monthEndSum);
         	locationSummary.setMonthEndNormalNum(monthEndSumzc);
         	locationSummary.setMonthEndDamagedNum(monthEndDamagedNum);
         	locationSummary.setAddPurchaseNotshelfNum(purchaseNotshelfNum);
         	locationSummary.setAddTransferNotshelfNum(monthTransferNotshelfNum);
         	locationSummary.setUnit(unit);         	
         	buLocationSummaryDao.save(locationSummary);

		}
    	

    	logger.info("商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "库位进销存汇总表生成完毕");
	}

	/**
     * 生成库位类型调整明细表
     * @param merchant
     * @param depot
     * @param buModel
     * @param month
     * @param outBadDetails
     * @param allMerchandiseMap
     * @param locationAdjustmentOrderMapList
	 * @throws SQLException 
     */
    private void saveBuBusinessLocationAdjustment(MerchantInfoModel merchant, DepotInfoModel depot,
			MerchantBuRelModel buModel, String month, List<Map<String, Object>> locationAdjustmentOrderMapList,
			Map<String, Map<String, Object>> locationAdjustmentMap) throws SQLException {
    	List<BuBusinessLocationAdjustmentDetailsModel> orderList= new ArrayList<>();
    	// 生成库位类型调整明细表
    	for (Map map : locationAdjustmentOrderMapList) {	
    		
    		Long brandId = (Long) map.get("comm_brand_parent_id");
    		String brandName = (String) map.get("comm_brand_parent_name");
    		String superiorParentBrand = (String) map.get("superior_parent_brand");
    		String commbarcode = (String) map.get("commbarcode");    		
    		Long id = (Long) map.get("id");
    		String code = (String) map.get("code");
    		String orderType = (String) map.get("order_type");
    		Long customerId = (Long) map.get("customer_id");
    		String customerName = (String) map.get("customer_name");   		
    		String platformCode = (String) map.get("platform_code");
    		String platformName = (String) map.get("platform_name");
    		String orderCode = (String) map.get("order_code");
    		String barcode = (String) map.get("barcode");
    		String goodsName = (String) map.get("goods_name");
    		//String tallyingUnit = (String) map.get("tallying_unit");
    		Integer adjustNum = (Integer) map.get("adjust_num");
    		if (adjustNum==null)adjustNum=0;
    		String inventoryType = (String) map.get("inventory_type");//库存类型 0：好品 1：坏品',
    		Long inStockLocationTypeId = (Long) map.get("in_stock_location_type_id");
    		String inStockLocationTypeName = (String) map.get("in_stock_location_type_name");
    		Long outStockLocationTypeId = (Long) map.get("out_stock_location_type_id");
    		String outStockLocationTypeName = (String) map.get("out_stock_location_type_name");
    		if (StringUtils.isEmpty(barcode)) continue;
    		barcode=barcode.trim();
    		// 库位调整单默认为件
    		String unit=null;
    		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit="2";	
    		
    		// 增
    		String inLocationKey=barcode+","+inStockLocationTypeId+","+unit;
    		if (locationAdjustmentMap.containsKey(inLocationKey)) {
    			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(inLocationKey);
    			if ("0".equals(inventoryType)) {
    				Integer adjustNumzcMap = (java.lang.Integer) locationKeyMap.get("adjustNumzc");
    				if (adjustNumzcMap==null) adjustNumzcMap=0;
        			locationKeyMap.put("adjustNumzc", adjustNum.intValue()+adjustNumzcMap.intValue());
				}else {					
					Integer adjustNumDamagedMap = (java.lang.Integer) locationKeyMap.get("adjustNumDamaged");
					if (adjustNumDamagedMap==null) adjustNumDamagedMap=0;
        			locationKeyMap.put("adjustNumDamaged", adjustNum.intValue()+adjustNumDamagedMap);
				}
    			
    			locationAdjustmentMap.put(inLocationKey, locationKeyMap);
			}else {
				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
				if ("0".equals(inventoryType)) {
        			locationKeyMap.put("adjustNumzc", adjustNum.intValue());
				}else {					
					locationKeyMap.put("adjustNumDamaged", adjustNum.intValue());
				}
				
				locationKeyMap.put("brandId", brandId);
				locationKeyMap.put("brandName", brandName);
				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
				locationKeyMap.put("commbarcode", commbarcode);    							
				locationKeyMap.put("stockLocationTypeId", inStockLocationTypeId);
				locationKeyMap.put("stockLocationTypeName", inStockLocationTypeName);
				locationKeyMap.put("unit", unit);
				locationAdjustmentMap.put(inLocationKey, locationKeyMap);
			}
    		// 减
    		String outLocationKey=barcode+","+outStockLocationTypeId+","+unit;;
    		if (locationAdjustmentMap.containsKey(outLocationKey)) {
    			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(outLocationKey);
    			if ("0".equals(inventoryType)) {
    				Integer adjustNumzcMap = (java.lang.Integer) locationKeyMap.get("adjustNumzc");
    				if (adjustNumzcMap==null) adjustNumzcMap=0;
        			locationKeyMap.put("adjustNumzc", adjustNumzcMap.intValue()-adjustNum.intValue());
				}else {					
					Integer adjustNumDamagedMap = (java.lang.Integer) locationKeyMap.get("adjustNumDamaged");
					if (adjustNumDamagedMap==null) adjustNumDamagedMap=0;
        			locationKeyMap.put("adjustNumDamaged", adjustNumDamagedMap-adjustNum.intValue());
				}
    			locationAdjustmentMap.put(outLocationKey, locationKeyMap);
			}else {
				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
				if ("0".equals(inventoryType)) {
        			locationKeyMap.put("adjustNumzc", adjustNum.intValue()*-1);
				}else {					
					locationKeyMap.put("adjustNumDamaged", adjustNum.intValue()*-1);
				}
				locationKeyMap.put("brandId", brandId);
				locationKeyMap.put("brandName", brandName);
				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
				locationKeyMap.put("commbarcode", commbarcode);    			
				locationKeyMap.put("stockLocationTypeId", outStockLocationTypeId);
				locationKeyMap.put("stockLocationTypeName", outStockLocationTypeName);
				locationKeyMap.put("unit", unit);
    			locationAdjustmentMap.put(outLocationKey, locationKeyMap);
			}
    		
    		
    		// 调减
        	BuBusinessLocationAdjustmentDetailsModel adjustmentDetailsSub=new BuBusinessLocationAdjustmentDetailsModel();
        	adjustmentDetailsSub.setMerchantId(merchant.getId());
        	adjustmentDetailsSub.setMerchantName(merchant.getName());
        	adjustmentDetailsSub.setDepotId(depot.getId());
        	adjustmentDetailsSub.setDepotName(depot.getName());
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
        	adjustmentDetailsSub.setUnit(unit);
        	orderList.add(adjustmentDetailsSub); 
        	// 调增
        	if (inStockLocationTypeId!=null) {
        		BuBusinessLocationAdjustmentDetailsModel adjustmentDetailsAdd=new BuBusinessLocationAdjustmentDetailsModel();
        		BeanUtils.copyProperties(adjustmentDetailsSub, adjustmentDetailsAdd);
        		adjustmentDetailsAdd.setOperateType("1");
        		adjustmentDetailsAdd.setStockLocationTypeId(inStockLocationTypeId);
        		adjustmentDetailsAdd.setStockLocationTypeName(inStockLocationTypeName);
            	orderList.add(adjustmentDetailsAdd); 
			}    		  					
		}
       	
    	// 循环信息生成库位调整明细
    	for (BuBusinessLocationAdjustmentDetailsModel model : orderList) {
    		buBusinessLocationAdjustmentDetailsDao.save(model);
		}    	
    	orderList=null;

	}

	/**
     * 获取来货短缺数据
     * @param allMerchandiseMap
     * @param paramMap
     * @param itemSummaryMap 
     * @throws SQLException 
     */
    private void getBuLackNumSum(MerchantInfoModel merchant, DepotInfoModel depot, MerchantBuRelModel buModel, String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap, Map<String, Object> paramMap,
			Map<String, Map<String, Object>> itemSummaryMap) throws SQLException {
    	List<Map<String, Object>> buLackNumSumList = purchaseOrderItemDao.getBuLackNumSum(paramMap);
		// 获取中转仓仓库编码 // 采购在途默认 写死中转仓
		DepotInfoModel depotInfo=new  DepotInfoModel();
		depotInfo.setCode("ZT001");
		depotInfo = depotInfoDao.searchByModel(depotInfo);
		// 来货残损归属到中转仓
		if (depot.getId()!=depotInfo.getId())return;
    	
    	for (Map<String, Object> buLackMap : buLackNumSumList) {
    		Long goodsId = (Long) buLackMap.get("goods_id");
    		String tallyingUnit = (String) buLackMap.get("tallying_unit");
    		BigDecimal num = (BigDecimal) buLackMap.get("num");
    		
    		if (StringUtils.isEmpty(tallyingUnit) || DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
    			tallyingUnit="2";
            } else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
            	tallyingUnit="1";
            } else if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
            	tallyingUnit="0";
            }
    		
           	Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
    		if (merchandiseMap==null) {
    			logger.error("事业部来货短缺"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
			}
        	// 获取商品信息      
        	String goodsNo = (String) merchandiseMap.get("goodsNo");
        	String goodsName = (String) merchandiseMap.get("goodsName");
        	String commbarcode = (String) merchandiseMap.get("commbarcode");
        	String barcode = (String) merchandiseMap.get("barcode");
        	String factoryNo = (String) merchandiseMap.get("factoryNo");
        	String productName = (String) merchandiseMap.get("name");
        	Long brandId = (Long) merchandiseMap.get("brandId");
        	String brandName = (String) merchandiseMap.get("brandName");
        	
			if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))tallyingUnit=null;				
			String itemKey=goodsId+","+tallyingUnit;
			Integer numInt=0;
			if (num!=null)numInt=num.intValue();
        	if (itemSummaryMap.containsKey(itemKey)) {
        		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);
        		Integer inDamagedNum = (Integer) summaryMap.get("inDamagedNum");
				if (inDamagedNum==null) inDamagedNum=0;
				summaryMap.put("inDamagedNum", numInt+inDamagedNum);
				itemSummaryMap.put(itemKey, summaryMap);
        		
			}else {
				Map<String, Object> summaryMap=new HashMap<>();
				summaryMap.put("inDamagedNum", numInt);
				itemSummaryMap.put(itemKey, summaryMap);
			} 
    		
		}
		
	}

	/**
     * 获取期初数据
     * @param merchant
     * @param depot
     * @param buModel
     * @param month
     * @param itemSummaryMap
     */
    private void getInitNum(MerchantInfoModel merchant, DepotInfoModel depot, MerchantBuRelModel buModel, String month,
			Map<String, Map<String, Object>> itemSummaryMap) throws Exception{
	    	String isOrInitflag="1";//0 取期初数据  1.不取期初数据 
			BuInventorySummaryModel buISummaryQuery=new BuInventorySummaryModel();
			buISummaryQuery.setMerchantId(merchant.getId());
			buISummaryQuery.setDepotId(depot.getId());
			buISummaryQuery.setOwnMonth(TimeUtils.getUpMonth(month));//上月
			int lastCount= buInventorySummaryDao.getLastMonthSummaryCount(buISummaryQuery);
			if (lastCount<=0) {// 上月没有
				int beforLastCount=buInventorySummaryDao.getBeforLastMonthSummaryCount(buISummaryQuery);
				if (beforLastCount<=0) {//之前没有过取期初
					isOrInitflag="0";//取期初							
				}
			}else {// 查询上月
				buISummaryQuery.setBuId(buModel.getBuId());
				List<BuInventorySummaryModel> lastMonthSummaryList = buInventorySummaryDao.list(buISummaryQuery);
				for (BuInventorySummaryModel model : lastMonthSummaryList) {
					if (!itemSummaryMap.containsKey(model.getGoodsId())) {
						Map<String, Object> summaryMap=new HashMap<>();
						Integer monthBeginSum = model.getMonthEndNum();//期初 默认为0
						Integer monthBeginSumzc = model.getMonthEndNormalNum();//正常品默认为0 期初正常品  
						Integer monthBeginDamagedNum = model.getMonthEndDamagedNum();//期初残次品默认为0     件2   箱1   托盘0
						if (monthBeginSum==null)monthBeginSum=0;
						if (monthBeginSumzc==null)monthBeginSumzc=0;
						if (monthBeginDamagedNum==null)monthBeginDamagedNum=0;
						String unit = model.getUnit();									
						if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;
						String itemKey=model.getGoodsId()+","+unit;
						summaryMap.put("monthBeginSum", monthBeginSum);
						summaryMap.put("monthBeginSumzc", monthBeginSumzc);
						summaryMap.put("monthBeginDamagedNum", monthBeginDamagedNum);
						itemSummaryMap.put(itemKey, summaryMap);
					}
				}
			}
								
			/**
			 * 存放
			 * 期初,期初正常品 ,期初残次品
			 */
			if ("0".equals(isOrInitflag)) {
				// 查询期初
				InitInventoryModel initModel=new InitInventoryModel();
				initModel.setMerchantId(merchant.getId());
				initModel.setDepotId(depot.getId());
				initModel.setBuId(buModel.getBuId());
				List<InitInventoryModel> initList = initInventoryDao.list(initModel);
				for (InitInventoryModel model : initList) {							
					Integer initNum = model.getInitNum();//期初
					if (initNum==null)initNum=0;
					String type = model.getType();
					Integer monthBeginSum=0;Integer monthBeginSumzc=0;Integer monthBeginDamagedNum=0;
					if (DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)) {//好品
						monthBeginSumzc=initNum;// 期初正常品
					}else if (DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)) {//坏品
						monthBeginDamagedNum=initNum;//期初残次品
					}
					monthBeginSum=initNum;// 期初总量(好品和坏品的和)
					String unit = model.getUnit();
					if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;
					String itemKey=model.getGoodsId()+","+unit;
					// 如果相同商品 相同单位 就叠加
					if (itemSummaryMap.containsKey(itemKey)) {
						Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);
						Integer monthBeginSumMap = (Integer) summaryMap.get("monthBeginSum");
						Integer monthBeginSumzcMap = (Integer) summaryMap.get("monthBeginSumzc");
						Integer monthBeginDamagedNumMap = (Integer) summaryMap.get("monthBeginDamagedNum");
						if (monthBeginSumMap==null)monthBeginSumMap=0;
						if (monthBeginSumzcMap==null)monthBeginSumzcMap=0;
						if (monthBeginDamagedNumMap==null)monthBeginDamagedNumMap=0;
						monthBeginSum=monthBeginSum+monthBeginSumMap;
						monthBeginSumzc=monthBeginSumzc+monthBeginSumzcMap;
						monthBeginDamagedNum=monthBeginDamagedNum+monthBeginDamagedNumMap;
						summaryMap.put("monthBeginSum", monthBeginSum);
						summaryMap.put("monthBeginSumzc", monthBeginSumzc);
						summaryMap.put("monthBeginDamagedNum", monthBeginDamagedNum);
						itemSummaryMap.put(itemKey, summaryMap);
					}else {
						Map<String, Object> summaryMap=new HashMap<>();
						summaryMap.put("monthBeginSum", monthBeginSum);
						summaryMap.put("monthBeginSumzc", monthBeginSumzc);
						summaryMap.put("monthBeginDamagedNum", monthBeginDamagedNum);
						itemSummaryMap.put(itemKey, summaryMap);
					}														
				}
			}
		
	}

	/**
     * 修改合同号 ,po号等
     * @throws Exception
     */
    public void updateBuItemNo(MerchantInfoModel merchant,DepotInfoModel depot,MerchantBuRelModel buModel,String month)throws Exception{
        // 更新合同号、po号、客户
       	BuInventorySummaryItemModel itemModel = new BuInventorySummaryItemModel();
        itemModel.setMerchantId(merchant.getId());
        itemModel.setDepotId(depot.getId());
        itemModel.setOwnMonth(month);
        itemModel.setBuId(buModel.getBuId());   
    	buInventorySummaryItemDao.updateBuItemNoBySale(itemModel);
        buInventorySummaryItemDao.updateBuItemNoBySaleReturn(itemModel);
        buInventorySummaryItemDao.updateBuItemNoByTransferOutDepot(itemModel);// 调拨出库不插入和更新客户id和客户名称
        buInventorySummaryItemDao.updateBuItemNoByTransferInDepot(itemModel);
        buInventorySummaryItemDao.updateBuItemNoByOrder(itemModel);// 修改 业务经销存 类型是电商订单的 平台单号放入 业务经销存
        //修改 上架入库单  客户 id 客户名称 po号
        buInventorySummaryItemDao.updateBuItemNoBySaleShelfIdepot(itemModel);// 上架入库单
        //查询采购入库id
        List<Map<String, Object>> warehouseIds = buInventorySummaryItemDao.getBuWarehouseIds(itemModel);
        for (Map<String, Object> map : warehouseIds) {
            Long warehouseId = (Long) map.get("warehouse_id");
            Long id = (Long) map.get("id");
            //根据入库单id获取采购信息
            List<Map<String, Object>> purchaseInfos = buInventorySummaryItemDao.getBuPurchaseInfos(warehouseId);
            String poNos = "";//PO号
            String contractNos = "";//合同号
            String supplierNames = "";//供应商名称
            for (Map<String, Object> infoMap : purchaseInfos) {
                String poNo = (String) infoMap.get("po_no");
                String contractNo = (String) infoMap.get("contract_no");
                String supplierName = (String) infoMap.get("supplier_name");
                if (!StringUtils.isEmpty(poNo)) {
                    if ("".equals(poNos)) {
                        poNos += poNo;
                    } else {
                        poNos += "," + poNo;
                    }
                }
                if (!StringUtils.isEmpty(contractNo)) {
                    if ("".equals(contractNos)) {
                        contractNos += contractNo;
                    } else {
                        contractNos += "," + contractNo;
                    }
                }
                if (!StringUtils.isEmpty(supplierName)) {
                    if ("".equals(supplierNames)) {
                        supplierNames += supplierName;
                    } else {
                        supplierNames += "," + supplierName;
                    }
                }
            }
            //根据id修改
            Map<String, Object> updateMap = new HashMap<String, Object>();
            updateMap.put("po_no", poNos);
            updateMap.put("contract_no", contractNos);
            updateMap.put("customer_name", supplierNames);
            updateMap.put("id", id);
            buInventorySummaryItemDao.updateBuItemNoById(updateMap);
        }
    }
    
    /**
     * 生成总表信息
     * @param merchant
     * @param depot
     * @param buModel
     * @param month
     */
    public void saveSummary(MerchantInfoModel merchant, DepotInfoModel depot,MerchantBuRelModel buModel, String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,Map<String, Map<String, Object>>itemSummaryMap) throws Exception {
    	Set<String> keySet = itemSummaryMap.keySet();
    	for (String itemKey : keySet) {
    		logger.info("总表的itemKey"+itemKey);
    		Map<String, Object>  summaryMap= itemSummaryMap.get(itemKey);
    		logger.info("总表的map"+summaryMap.toString());
    		String[]keyArr = itemKey.split(",");
    		Long goodsId = Long.parseLong(keyArr[0]);
    		String unit=null;
    		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit = keyArr[1];
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
    		if (merchandiseMap==null) {
    			logger.error("事业部业务经销存总表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
			}
    		logger.info("生成总表的商品信息"+merchandiseMap.toString());
         	String goodsNo = (String) merchandiseMap.get("goodsNo");
         	String goodsName = (String) merchandiseMap.get("goodsName");
         	String commbarcode = (String) merchandiseMap.get("commbarcode");
         	String barcode = (String) merchandiseMap.get("barcode");
         	String factoryNo = (String) merchandiseMap.get("factoryNo");
         	String productName = (String) merchandiseMap.get("name");
         	Long brandId = (Long) merchandiseMap.get("brandId");
         	String brandName = (String) merchandiseMap.get("brandName");
         	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
         	
         	Integer monthBeginSum=(Integer) summaryMap.get("monthBeginSum");//本月期初库存
         	Integer monthBeginSumzc=(Integer) summaryMap.get("monthBeginSumzc");//本月正常品期初库存
         	Integer monthBeginDamagedNum=(Integer) summaryMap.get("monthBeginDamagedNum");//本月残次品期初库存量
         	Integer monthInStorageSum=(Integer) summaryMap.get("monthInStorageSum");// 本月入库数量
         	Integer monthOutStorageSum=(Integer) summaryMap.get("monthOutStorageSum");// 本月出库数量
         	Integer inDamagedNum=(Integer) summaryMap.get("inDamagedNum");//来货短缺
         	Integer outDamagedNum=(Integer) summaryMap.get("outDamagedNum");//出库残损         	
         	Integer saleOutNum=(Integer) summaryMap.get("saleOutNum");// 本月销售未确认数量
         	Integer purchaseNotshelfNum=(Integer) summaryMap.get("purchaseNotshelfNum");// 本月采购未上架数量   
         	Integer profitlossGoodInSum=(Integer) summaryMap.get("profitlossGoodInSum");// 好品损益入
         	Integer profitlossGoodOutSum=(Integer) summaryMap.get("profitlossGoodOutSum");// 好品损益出 
         	Integer profitlossDamagedInSum=(Integer) summaryMap.get("profitlossDamagedInSum");// 坏品损益入  
         	Integer profitlossDamagedOutSum=(Integer) summaryMap.get("profitlossDamagedOutSum");// 坏品损益出          	
         	Integer monthTransferNotshelfNum=(Integer) summaryMap.get("monthTransferNotshelfNum");// 本期调拨在途
         	Integer monthInBadNum=(Integer) summaryMap.get("monthInBadNum");// 本期入库残次
         	Integer monthOutBadNum=(Integer) summaryMap.get("monthOutBadNum");// 本期出库残次     
         	        	
         	// 上面字段如果未空设置为0 如果都未0不存
         	if (monthBeginSum==null) monthBeginSum=0;if (monthBeginSumzc==null) monthBeginSumzc=0;if (monthBeginDamagedNum==null) monthBeginDamagedNum=0;
         	if (monthInStorageSum==null) monthInStorageSum=0;if (monthOutStorageSum==null) monthOutStorageSum=0;
         	if (inDamagedNum==null) inDamagedNum=0;if (outDamagedNum==null) outDamagedNum=0;        
         	if (saleOutNum==null) saleOutNum=0;if (purchaseNotshelfNum==null) purchaseNotshelfNum=0;
         	if (profitlossGoodInSum==null) profitlossGoodInSum=0;if (profitlossGoodOutSum==null) profitlossGoodOutSum=0;
         	if (profitlossDamagedInSum==null) profitlossDamagedInSum=0;if (profitlossDamagedOutSum==null) profitlossDamagedOutSum=0;
         	if (monthTransferNotshelfNum==null) monthTransferNotshelfNum=0;
         	if (monthInBadNum==null) monthInBadNum=0;if (monthOutBadNum==null) monthOutBadNum=0;
         	
         	//Integer availableNum=(Integer) summaryMap.get("availableNum");// 仓库现存量
         	//Integer monthNormalExpireNum=(Integer) summaryMap.get("monthNormalExpireNum");//正常品过期量        	
         	Integer profitlossGoodNum=profitlossGoodInSum-profitlossGoodOutSum;// 好品损益
         	Integer profitlossDamagedNum=profitlossDamagedInSum-profitlossDamagedOutSum;//坏品损益        		      	
         	Integer profitLossSum=profitlossGoodNum+profitlossDamagedNum;// 损益
         	Integer monthEndSum=monthBeginSum+monthInStorageSum-monthOutStorageSum+profitLossSum;//本月期末        	        	
         	//本月正常品期末库存=本月正常品期初库存+本月入库-本月出库-来货残次+出库残次+好品损益
         	Integer monthEndSumzc=monthBeginSumzc+monthInStorageSum-monthOutStorageSum-monthInBadNum+monthOutBadNum+profitlossGoodNum;//本月正常品期末库存
         	//本月残次品期末库存=本月残次品期初库存+来货残次-出库残次+坏品损益
         	Integer monthEndDamagedNum=monthBeginDamagedNum+monthInBadNum-monthOutBadNum+profitlossDamagedNum;// 本月残次品		
         	// 不生成总表判断
         	if (monthBeginSum==0&&monthBeginSumzc==0&&monthBeginDamagedNum==0&&
         			monthInStorageSum==0&&monthOutStorageSum==0&&
         			inDamagedNum==0&&outDamagedNum==0&&
         			saleOutNum==0&&purchaseNotshelfNum==0&&
         			monthTransferNotshelfNum==0&&
         			profitlossGoodNum==0&&profitlossDamagedNum==0&&profitLossSum==0&&
         			monthEndSum==0&&monthEndSumzc==0&&monthEndDamagedNum==0
             		) {
         		logger.info(merchant.getName()+depot.getName()+buModel.getBuName()+ "当前数据都为0，故不存");
         		continue;
			}
         	         	
         	// 插入汇总
            BuInventorySummaryModel summaryModel = new BuInventorySummaryModel();
            summaryModel.setMerchantId(merchant.getId());
            summaryModel.setTopidealCode(merchant.getTopidealCode());
            summaryModel.setMerchantName(merchant.getName());
            summaryModel.setDepotId(depot.getId());
            summaryModel.setDepotCode(depot.getCode());
            summaryModel.setDepotName(depot.getName());
            summaryModel.setOwnMonth(month);
            summaryModel.setBrandId(brandId);
            summaryModel.setBrandName(brandName);
            summaryModel.setProductName(productName);
            summaryModel.setGoodsId(goodsId);
            summaryModel.setGoodsNo(goodsNo);
            summaryModel.setGoodsName(goodsName);
            summaryModel.setBarcode(barcode);
            summaryModel.setCommbarcode(commbarcode);
            summaryModel.setSuperiorParentBrand(superiorParentBrand);
            summaryModel.setUnit(unit);
            summaryModel.setFactoryNo(factoryNo);
           // summaryModel.setSupplyMinPrice(new BigDecimal(0));// 暂时去掉字段 供应商价格
            summaryModel.setMonthBeginNum(monthBeginSum);//本月期初库存
            summaryModel.setMonthBeginNormalNum(monthBeginSumzc);//本月正常品期初库存
            summaryModel.setMonthBeginDamagedNum(monthBeginDamagedNum);//本月残次品期初库存量
            summaryModel.setMonthInstorageNum(monthInStorageSum);// 本月入库数量
            summaryModel.setMonthOutstorageNum(monthOutStorageSum);// 本月出库数量
            summaryModel.setInDamagedNum(inDamagedNum);//来货短缺
            summaryModel.setOutDamagedNum(outDamagedNum);//出库残损
            summaryModel.setMonthProfitlossNum(profitLossSum);// 本月损益数量
            summaryModel.setProfitlossGoodNum(profitlossGoodNum);//好品损益
            summaryModel.setProfitlossDamagedNum(profitlossDamagedNum);//坏品损益
            summaryModel.setMonthEndNum(monthEndSum);// 本月期末库存
           // summaryModel.setMonthEndAmount(new BigDecimal(0));// 本月期末库存金额 暂时去掉字段 供应商价格 *本期结转金额
            summaryModel.setMonthEndNormalNum(monthEndSumzc);//本月正常品期末库存
            summaryModel.setMonthEndDamagedNum(monthEndDamagedNum);//本月残次品期末库存量                      
            summaryModel.setMonthSaleUnconfirmedNum(saleOutNum);// 本月销售未确认数量
            summaryModel.setMonthPurchaseNotshelfNum(purchaseNotshelfNum);// 本月采购未上架数量                                   
            summaryModel.setMonthTransferNotshelfNum(monthTransferNotshelfNum);// 本期调拨在途
            summaryModel.setMonthInBadNum(monthInBadNum);// 本期入库残次
            summaryModel.setMonthOutBadNum(monthOutBadNum);// 本期出库残次      
            summaryModel.setAvailableNum(0);// 仓库现存量
            summaryModel.setMonthNormalExpireNum(0);//正常品过期量
            summaryModel.setBuId(buModel.getBuId());
            summaryModel.setBuName(buModel.getBuName());
            summaryModel.setBuCode(buModel.getBuCode());
            Long summaryId = buInventorySummaryDao.save(summaryModel);
         	
    		
		} 
    	logger.info("商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "总表生成完毕");
    }
    
    /**
     * 获取商家及代理商家的所有商品条码
     * 字段：barcode、name、brandId、brandName、factory_no、goods_id
     *
     * @throws Exception
     */
    public Map<Long,  Map<String, Object>>  getAllMerchandiseList(String threadName, MerchantInfoModel merchant,
    		Map<String, CommbarcodeModel> brandMap,Map<Long, BrandParentMongo> brandParentMap) throws Exception {    	
    	
    	//查询商品
    	List<MerchandiseInfoModel> merchandiseList =new ArrayList<>();
    	MerchandiseInfoModel merchandise=new MerchandiseInfoModel();
    	merchandise.setMerchantId(merchant.getId());
    	merchandise.setPageSize(5000);
    	merchandise.setBegin(0);
    	while(true){
    		MerchandiseInfoModel merchandisePage = merchandiseInfoDao.searchByPage(merchandise);	
    		List<MerchandiseInfoModel> list = merchandisePage.getList();
    		merchandise.setBegin(merchandise.getBegin()+merchandise.getPageSize());
			if (list.size()==0) break;
			merchandiseList.addAll(list);
			merchandisePage=null;
			list=null;
    	}
    	
    	 if (merchandiseList == null || merchandiseList.size() <= 0) {
             return null;
         }

        // 获取商品相关信息
        Map<Long,  Map<String, Object>> merchandiseMap=new HashMap<>();
        //循环根据条码获取商品的id、工厂编码、货品名称、品牌id、品牌名称
        for (MerchandiseInfoModel item : merchandiseList) {
        	Map<String, Object> itemMap =new HashMap<>();
        	itemMap.put("goodsId", item.getId());
        	itemMap.put("goodsNo", item.getGoodsNo());
        	itemMap.put("goodsName", item.getName());
            itemMap.put("commbarcode", item.getCommbarcode());
        	itemMap.put("barcode", item.getBarcode());
        	itemMap.put("factoryNo", item.getFactoryNo());
        	           
            Long brandId = null;//品牌id
            String brandName = "";//品牌名称
            String superiorParentBrand="";// 母品牌
            if (!StringUtils.isEmpty(item.getCommbarcode())) {
            	CommbarcodeModel commbarcodeModel = brandMap.get(item.getCommbarcode());
            	if (commbarcodeModel!=null) {
            		brandId=commbarcodeModel.getCommBrandParentId();
                	brandName=commbarcodeModel.getCommBrandParentName();
				}           	
			}
            BrandParentMongo brandParentMongo=null;
            //获取母品牌
            if (brandId!=null) {
            	brandParentMongo = brandParentMap.get(brandId);
			}
            if (brandParentMongo!=null) {
            	superiorParentBrand=brandParentMongo.getSuperiorParentBrand();
			}
            
            itemMap.put("name", item.getName());
            itemMap.put("brandId", brandId);
            itemMap.put("brandName", brandName);
            itemMap.put("superiorParentBrand", superiorParentBrand);
            merchandiseMap.put(item.getId(), itemMap);
        }

        return merchandiseMap;
    }

    /**
     * 合并商家id和关联商家id
     **/
    public List<Long> getMerchantIdList(List<MerchantRelModel> relList, MerchantInfoModel merchant) {
        List<Long> idlist = new ArrayList<Long>();
        idlist.add(merchant.getId());
        if (relList != null && relList.size() > 0) {
            for (MerchantRelModel relmodel : relList) {
                idlist.add(relmodel.getProxyMerchantId());
            }
        }
        return idlist;
    }

    /**
     * 分页保存业务报表出入库表体
     * @throws SQLException 
     */
/*    public void saveSummaryItem(MerchantInfoModel merchant,DepotInfoModel depot,MerchantBuRelModel buModel,
    		String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		Map<String, Object> detailParamMap,Map<String, Map<String, Object>>itemSummaryMap) {
        	
    	//分页查询出入库明细
        int startIndex = 0;
        while (true) {
            detailParamMap.put("startIndex", startIndex);
            List<InventoryDetailsModel> listDetail = inventoryDetailsDao.getBuOutInStorageDetail(detailParamMap);
            if (listDetail == null || listDetail.size() < 1)
                break;

            //获取采购单、销售单、调拨单表体备注
            Map<String, Object> remarkMap = getOrderRemak(listDetail);
            List<BuInventorySummaryItemModel> itemList = new ArrayList<BuInventorySummaryItemModel>();
            for (InventoryDetailsModel detail : listDetail) {
            	Map<String, Object> merchandiseMap = allMerchandiseMap.get(detail.getGoodsId());
        		if (merchandiseMap==null) {
        			logger.error("事业部出入库明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+detail.getGoodsId());
    			}
            	// 获取商品信息
            	Long goodsId = (Long) merchandiseMap.get("goodsId");
            	String goodsNo = (String) merchandiseMap.get("goodsNo");
            	String goodsName = (String) merchandiseMap.get("goodsName");
            	String commbarcode = (String) merchandiseMap.get("commbarcode");
            	String barcode = (String) merchandiseMap.get("barcode");
            	String factoryNo = (String) merchandiseMap.get("factoryNo");
            	String productName = (String) merchandiseMap.get("name");
            	Long brandId = (Long) merchandiseMap.get("brandId");
            	String brandName = (String) merchandiseMap.get("brandName");
            	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
            	
            	String unit = detail.getUnit();//单位
				if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;				
				String itemKey=goodsId+","+unit;
				
				Integer num = detail.getNum();
				if (num==null) num=0;
            	if (itemSummaryMap.containsKey(itemKey)) {
            		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);           							
					if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//调减
						Integer monthOutStorageSum = (Integer) summaryMap.get("monthOutStorageSum");
						if (monthOutStorageSum==null) monthOutStorageSum=0;
						summaryMap.put("monthOutStorageSum", num+monthOutStorageSum);
						itemSummaryMap.put(itemKey, summaryMap);
					}else if(DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//调增
						Integer monthInStorageSum = (Integer) summaryMap.get("monthInStorageSum");
						if (monthInStorageSum==null) monthInStorageSum=0;
						summaryMap.put("monthInStorageSum", num+monthInStorageSum);
						itemSummaryMap.put(itemKey, summaryMap);
					}					           		
				}else {
					Map<String, Object> summaryMap=new HashMap<>();
					if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//调减						
						summaryMap.put("monthOutStorageSum", num);
						itemSummaryMap.put(itemKey, summaryMap);
					}else if(DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//调增
						summaryMap.put("monthInStorageSum", num);
						itemSummaryMap.put(itemKey, summaryMap);
					}					
				}            	
                BuInventorySummaryItemModel itemModel = new BuInventorySummaryItemModel();                                
                itemModel.setShopCode(detail.getShopCode());// 店铺编码                       
                itemModel.setShopName(detail.getShopName());// 店铺名称
                String storePlatformCode = detail.getStorePlatformName();
                itemModel.setStorePlatformCode(storePlatformCode);// 电商平台编码
                if (!StringUtils.isEmpty(storePlatformCode)) {
                	String storePlatformName = DERP.getLabelByKey(DERP.storePlatformList,storePlatformCode);
                	itemModel.setStorePlatformName(storePlatformName);// 电商平台名称
				}
                itemModel.setCode(detail.getOrderNo());// 单号
                itemModel.setBusinessNo(detail.getBusinessNo());// 业务单号
                itemModel.setSourceType(detail.getSourceType());// 单据类型
                itemModel.setThingName(detail.getThingName());//事务类型名称
                itemModel.setOperateType(detail.getOperateType());// 操作类型0调减 1调增
                itemModel.setMerchantId(merchant.getId());// 商家ID
                itemModel.setMerchantName(merchant.getName());// 商家名称
                itemModel.setDepotId(depot.getId());
                itemModel.setDepotName(depot.getName());
                itemModel.setDivergenceDate(detail.getDivergenceDate());// 出入时间
                itemModel.setOwnMonth(month);// 归属月份
                itemModel.setProductName(productName);// 货品名称
                itemModel.setBarcode(barcode);// 条码
                itemModel.setCommbarcode(commbarcode);// 标准条码
                if (depot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                    itemModel.setUnit(detail.getUnit());
                }
                itemModel.setFactoryNo(factoryNo);// 工厂编码
                itemModel.setGoodsId(goodsId);
                itemModel.setGoodsNo(goodsNo);
                itemModel.setSuperiorParentBrand(superiorParentBrand);               
                itemModel.setGoodsName(goodsName);
                itemModel.setNum(detail.getNum());// 数量
                itemModel.setModifyDate(TimeUtils.getNow());
                itemModel.setCreateDate(TimeUtils.getNow());
                String remark = "";
                if (DERP_INVENTORY.INVENTORY_SOURCETYPE_001.equals(detail.getSourceType())) {
                    //获取采购单表体备注 key = 入库单号+商品id
                    remark = (String) remarkMap.get(itemModel.getCode() + itemModel.getGoodsId());
                } else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_003.equals(detail.getSourceType())) {
                    //获取销售订单表体备注 key = 销售单号+商品id
                    remark = (String) remarkMap.get(itemModel.getBusinessNo() + itemModel.getGoodsId());
                } else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0011.equals(detail.getSourceType())) {
                    //获取调拨单表体备注 key = 调拨单号+商品id
                    remark = (String) remarkMap.get(itemModel.getBusinessNo() + itemModel.getGoodsId());
                }
                itemModel.setRemark(remark);
                itemModel.setDetailType(InventorySummaryDetailStatusEnum.CRKMX.getKey()); //明细类型
                itemModel.setBuId(buModel.getBuId());
                itemModel.setBuName(buModel.getBuName());
                itemList.add(itemModel);
            }
             buInventorySummaryItemDao.insertBuBatch(itemList);

            startIndex = startIndex + 1000;
        }
    }*/

    public boolean saveSummaryItem(MerchantInfoModel merchant,DepotInfoModel depot,MerchantBuRelModel buModel,
    		String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		Map<String, Object> detailParamMap,Map<String, Map<String, Object>>itemSummaryMap,
    		Map<String, Map<String, Object>> locationAdjustmentMap) throws SQLException {
        // 分组统计出入库明细的数据
    	List<Map<String, Object>> listDetailList = inventoryDetailsDao.getBuOutInStorageDetail(detailParamMap);
    	if (listDetailList == null || listDetailList.size() < 1) return true;
    	//1. 获取出入库明细的量
    	for (Map<String, Object> listDetailMap : listDetailList) {
    		Long goodsId = (Long) listDetailMap.get("goods_id");
    		String operateType = (String) listDetailMap.get("operate_type");
    		String unit = (String) listDetailMap.get("unit");
    		BigDecimal numMap = (BigDecimal) listDetailMap.get("num");   
    		int num=0;
			if (numMap!=null) num=numMap.intValue();
    		Long stockLocationTypeId = (Long) listDetailMap.get("stock_location_type_id");
    		String stockLocationTypeName = (String) listDetailMap.get("stock_location_type_name");
    		// 获取商品信息
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
    		if (merchandiseMap==null) {
     			logger.error("事业部损益明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
 			}
    		// 获取商品信息
         	//Long goodsId = (Long) merchandiseMap.get("goodsId");
         	String goodsNo = (String) merchandiseMap.get("goodsNo");
         	String goodsName = (String) merchandiseMap.get("goodsName");
         	String commbarcode = (String) merchandiseMap.get("commbarcode");
         	String barcode = (String) merchandiseMap.get("barcode");
         	barcode=barcode.trim();
         	String factoryNo = (String) merchandiseMap.get("factoryNo");
         	String productName = (String) merchandiseMap.get("name");
         	Long brandId = (Long) merchandiseMap.get("brandId");
         	String brandName = (String) merchandiseMap.get("brandName");
         	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
         	if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;	
    		if (stockLocationTypeId!=null) {   			
    			String inLocationKey=barcode+","+stockLocationTypeId+","+unit;
        		if (locationAdjustmentMap.containsKey(inLocationKey)) {
        			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(inLocationKey);       			
        			if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(operateType)) {//调减
    					Integer monthOutStorageSum = (Integer) locationKeyMap.get("monthOutStorageSum");
    					if (monthOutStorageSum==null) monthOutStorageSum=0;
    					locationKeyMap.put("monthOutStorageSum", num+monthOutStorageSum);
    				}else if(DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(operateType)) {//调增
    					Integer monthInStorageSum = (Integer) locationKeyMap.get("monthInStorageSum");
    					if (monthInStorageSum==null) monthInStorageSum=0;
    					locationKeyMap.put("monthInStorageSum", num+monthInStorageSum);
    				}
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);        			
    			}else {
    				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
    				if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(operateType)) {//调减						
    					locationKeyMap.put("monthOutStorageSum", num);
    				}else if(DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(operateType)) {//调增
    					locationKeyMap.put("monthInStorageSum", num);
    				}
    				locationKeyMap.put("goodsName", goodsName);
    				locationKeyMap.put("brandId", brandId);
    				locationKeyMap.put("brandName", brandName);
    				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
    				locationKeyMap.put("commbarcode", commbarcode);    			
    				locationKeyMap.put("stockLocationTypeId", stockLocationTypeId);
    				locationKeyMap.put("stockLocationTypeName", stockLocationTypeName);
    				locationKeyMap.put("unit", unit);
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);
    			}
			}
    		
    		if (merchandiseMap==null) {
    			logger.error("事业部出入库明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
			}
    					
			String itemKey=goodsId+","+unit;
			
        	if (itemSummaryMap.containsKey(itemKey)) {
        		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);           							
				if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(operateType)) {//调减
					Integer monthOutStorageSum = (Integer) summaryMap.get("monthOutStorageSum");
					if (monthOutStorageSum==null) monthOutStorageSum=0;
					summaryMap.put("monthOutStorageSum", num+monthOutStorageSum);
					itemSummaryMap.put(itemKey, summaryMap);
				}else if(DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(operateType)) {//调增
					Integer monthInStorageSum = (Integer) summaryMap.get("monthInStorageSum");
					if (monthInStorageSum==null) monthInStorageSum=0;
					summaryMap.put("monthInStorageSum", num+monthInStorageSum);
					itemSummaryMap.put(itemKey, summaryMap);
				}					           		
			}else {
				Map<String, Object> summaryMap=new HashMap<>();
				if (DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(operateType)) {//调减						
					summaryMap.put("monthOutStorageSum", num);
					itemSummaryMap.put(itemKey, summaryMap);
				}else if(DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(operateType)) {//调增
					summaryMap.put("monthInStorageSum", num);
					itemSummaryMap.put(itemKey, summaryMap);
				}					
			}
		}
    	Map<String, Object>outInMap=new HashMap<>();
    	outInMap.put("merchantId", merchant.getId());
    	outInMap.put("merchantName", merchant.getName());
    	outInMap.put("depotId", depot.getId());
    	outInMap.put("depotName", depot.getName());
    	outInMap.put("buId", buModel.getBuId());
    	outInMap.put("buName", buModel.getBuName());
    	outInMap.put("month", month);
    	outInMap.put("depotType", depot.getType());
    	outInMap.put("detailType", InventorySummaryDetailStatusEnum.CRKMX.getKey());
    	
    	//2.生成业务经销存表体
		logger.info("批量插入事业部出入库明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+"--开始");
    	buInventorySummaryItemDao.insertBuOutInStorageDetail(outInMap);
		logger.info("正在批量插入事业部出入库明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+"--结束");
		// 修改母品牌
		BuInventorySummaryItemModel summaryItem= new  BuInventorySummaryItemModel();
		summaryItem.setMerchantId(merchant.getId());
		summaryItem.setDepotId(depot.getId());
		summaryItem.setBuId(buModel.getBuId());
		summaryItem.setOwnMonth(month);
		logger.info("修改母品牌"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+"--开始");
		buInventorySummaryItemDao.updateSuperiorParentBrand(summaryItem);
		logger.info("修改母品牌"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+"--结束");
    	return true;

    }
    /**
     * 保存销售未上架明细
     */
    public void saveSaleNotshelfInfo(DepotInfoModel depot,MerchantBuRelModel buModel, Long summaryId, Map<String, Object> detailParamMap) {
        //临时保存购销、代销明细
        List<Map<String, Object>> itemMapList = new ArrayList<Map<String, Object>>();

        //查询销售未上架订单id、货号、未上架量-销售订单
        List<Map<String, Object>> notshelfDXList = saleOutDepotItemDao.getBuSaleNotshelfDXList(detailParamMap);
        if (notshelfDXList != null && notshelfDXList.size() > 0) {
            for (Map<String, Object> infoMap : notshelfDXList) {
                if (infoMap == null) continue;
                BigDecimal notshelfNum = (BigDecimal) infoMap.get("num");//未上架量
                if (notshelfNum == null || notshelfNum.intValue() <= 0) continue;

                //获取未上架明细字段
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("orderId", (Long) infoMap.get("id"));
                paramMap.put("goodsNo", (String) infoMap.get("goods_no"));
                paramMap.put("unit", (String) detailParamMap.get("unit"));//00-托盘 01-箱 02-件

                Map<String, Object> itemValue = saleOutDepotItemDao.getBuSaleNotshelfDXItem(paramMap);
                itemValue.put("num", notshelfNum.intValue());
                //itemValue.put("businessModel", "2");
                itemMapList.add(itemValue);
            }
        }

        //拼装实体保存明细
        List<BuSaleNotshelfInfoModel> addList = new ArrayList<BuSaleNotshelfInfoModel>();
        for (Map<String, Object> itemMap : itemMapList) {
            String unit = (String) itemMap.get("tallying_unit");
            if (StringUtils.isEmpty(unit)) {
                unit = null;
            } else if (unit.equals(DERP.ORDER_TALLYINGUNIT_00)) {
                unit = "0";
            } else if (unit.equals(DERP.ORDER_TALLYINGUNIT_01)) {
                unit = "1";
            } else if (unit.equals(DERP.ORDER_TALLYINGUNIT_02)) {
                unit = "2";
            }
            BuSaleNotshelfInfoModel model = new BuSaleNotshelfInfoModel();
            model.setCode((String) itemMap.get("code"));
            model.setSaleOrderCode((String) itemMap.get("sale_order_code"));
            model.setMerchantId((Long) itemMap.get("merchant_id"));
            model.setMerchantName((String) itemMap.get("merchant_name"));
            model.setDepotId((Long) itemMap.get("out_depot_id"));
            model.setDepotName((String) itemMap.get("out_depot_name"));
            model.setPoNo((String) itemMap.get("po_no"));
            model.setShelfDate((Timestamp) itemMap.get("shelf_date"));
            model.setDeliverDate((Timestamp) itemMap.get("deliver_date"));
            model.setGoodsId((Long) itemMap.get("goods_id"));
            model.setGoodsName((String) itemMap.get("goods_name"));
            model.setBarcode((String) itemMap.get("barcode"));
            model.setCommbarcode((String) itemMap.get("commbarcode"));
            model.setNotshelfNum((Integer) itemMap.get("num"));
            String businessModel = (String) itemMap.get("business_model");
            if (DERP_ORDER.SALEANALYSIS_SALETYPE_1.equals(businessModel)) {
            	model.setBusinessModel(DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_3);
			}else if (DERP_ORDER.SALEANALYSIS_SALETYPE_4.equals(businessModel)) {
				model.setBusinessModel(DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_4);
			}           
            model.setOwnMonth((String) detailParamMap.get("month"));
            model.setUnit(unit);
            model.setCreateDate(TimeUtils.getNow());
            model.setModifyDate(TimeUtils.getNow());
            model.setInventorySummaryId(summaryId);
            model.setBuId(buModel.getBuId());
            model.setBuName(buModel.getBuName());
            addList.add(model);
        }
        if (addList.size() > 0) {
            buSaleNotshelfInfoDao.insertBuBatch(addList);
        }

    }

    /**
     * 保存采购未上架明细
     *
     * @throws Exception
     */
    public void savePurchaseNotshelfInfo(MerchantInfoModel merchant,DepotInfoModel depot,MerchantBuRelModel buModel,
    		String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		Map<String, Object> detailParamMap,Map<String, Map<String, Object>>itemSummaryMap,
    		Map<String, Map<String, Object>> locationAdjustmentMap) throws Exception {
		// 获取中转仓仓库编码 // 采购在途默认 写死中转仓
		DepotInfoModel depotInfo=new  DepotInfoModel();
		depotInfo.setCode("ZT001");
		depotInfo = depotInfoDao.searchByModel(depotInfo);
		// 来货残损归属到中转仓
		if (depot.getId()!=depotInfo.getId())return;
    	
        List<Map<String, Object>> notshelfInfoList = purchaseOrderItemDao.getBuMonthPurchaseNotshelfInfo(detailParamMap);
        if (notshelfInfoList != null && notshelfInfoList.size() > 0) {
            List<BuPurchaseNotshelfInfoModel> infoList = new ArrayList<BuPurchaseNotshelfInfoModel>();
            for (Map<String, Object> infoMap : notshelfInfoList) {
            	Long goodsId  = (Long) infoMap.get("goods_id");
            	Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
            	if (merchandiseMap==null) {
            		logger.info("采购在途获取商品为空goodsId："+goodsId);
				}
  				// 获取商品信息
              	String goodsNo = (String) merchandiseMap.get("goodsNo");
              	String goodsName = (String) merchandiseMap.get("goodsName");
              	String commbarcode = (String) merchandiseMap.get("commbarcode");
              	String barcode = (String) merchandiseMap.get("barcode");
              	String factoryNo = (String) merchandiseMap.get("factoryNo");
              	String productName = (String) merchandiseMap.get("name");
              	Long brandId = (Long) merchandiseMap.get("brandId");
              	String brandName = (String) merchandiseMap.get("brandName");
              	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
              	String unit = (String) infoMap.get("tallying_unit");
              	Long stockLocationTypeId = (Long) infoMap.get("stock_location_type_id");
              	String stockLocationTypeName = (String) infoMap.get("stock_location_type_name");
                if (StringUtils.isEmpty(unit)) {
                    unit = null;
                } else if (unit.equals(DERP.ORDER_TALLYINGUNIT_00)) {
                    unit = "0";
                } else if (unit.equals(DERP.ORDER_TALLYINGUNIT_01)) {
                    unit = "1";
                } else if (unit.equals(DERP.ORDER_TALLYINGUNIT_02)) {
                    unit = "2";
                }
                
                BigDecimal notshelfNum = (BigDecimal) infoMap.get("notshelf_num");
                Integer notshelfNumInt=0;// 销售未上架量
                if (notshelfNum!=null) notshelfNumInt=notshelfNum.intValue();
                if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;	
                if (stockLocationTypeId!=null) {
        			String inLocationKey=barcode+","+stockLocationTypeId+","+unit;
            		if (locationAdjustmentMap.containsKey(inLocationKey)) {
            			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(inLocationKey);       			
            			Integer purchaseNotshelfNum = (Integer) locationKeyMap.get("purchaseNotshelfNum");
                		if (purchaseNotshelfNum==null) purchaseNotshelfNum=0;
                		locationKeyMap.put("purchaseNotshelfNum", notshelfNumInt+purchaseNotshelfNum);
        				locationAdjustmentMap.put(inLocationKey, locationKeyMap);        			
        			}else {
        				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
        				locationKeyMap.put("purchaseNotshelfNum", notshelfNumInt);
        				locationKeyMap.put("goodsName", goodsName);
        				locationKeyMap.put("brandId", brandId);
        				locationKeyMap.put("brandName", brandName);
        				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
        				locationKeyMap.put("commbarcode", commbarcode);    			
        				locationKeyMap.put("stockLocationTypeId", stockLocationTypeId);
        				locationKeyMap.put("stockLocationTypeName", stockLocationTypeName);
        				locationKeyMap.put("unit", unit);
        				locationAdjustmentMap.put(inLocationKey, locationKeyMap);
        			}
    			}
                
                
                
                String itemKey=goodsId+","+unit;
                
                if (itemSummaryMap.containsKey(itemKey)) {
            		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);
            		Integer purchaseNotshelfNum = (Integer) summaryMap.get("purchaseNotshelfNum");
            		if (purchaseNotshelfNum==null) purchaseNotshelfNum=0;
            		summaryMap.put("purchaseNotshelfNum", notshelfNumInt+purchaseNotshelfNum);
					itemSummaryMap.put(itemKey, summaryMap);            		
				}else {
					Map<String, Object> summaryMap=new HashMap<>();
					summaryMap.put("purchaseNotshelfNum", notshelfNumInt);
					itemSummaryMap.put(itemKey, summaryMap);
				}      
                if (notshelfNum == null || notshelfNum.intValue() <= 0) continue;
                
                BuPurchaseNotshelfInfoModel model = new BuPurchaseNotshelfInfoModel();
                model.setCode((String) infoMap.get("code"));
                model.setMerchantId(merchant.getId());
                model.setMerchantName(merchant.getName());
                model.setDepotId(depot.getId());
                model.setDepotName(depot.getName());
                model.setPoNo((String) infoMap.get("po_no"));
                model.setOrderCreateDate((Timestamp) infoMap.get("create_date"));
                model.setDrawInvoiceDate((Timestamp) infoMap.get("draw_invoice_date"));
                model.setStatus((String) infoMap.get("status"));
                model.setGoodsId(goodsId);
                model.setGoodsName(goodsName);
                model.setBarcode(barcode);
                model.setCommbarcode(commbarcode);
                model.setSuperiorParentBrand(superiorParentBrand);
                model.setNotshelfNum(notshelfNum.intValue());
                if (depot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                    model.setUnit(unit);
                }
                //model.setInventorySummaryId(summaryId);
                model.setOwnMonth(month);
                model.setModifyDate(TimeUtils.getNow());
                model.setCreateDate(TimeUtils.getNow());
                model.setBuId(buModel.getBuId());
                model.setBuName(buModel.getBuName());
                model.setAttributionDate((Timestamp) infoMap.get("attribution_date"));
                model.setEndDate((Timestamp) infoMap.get("end_date"));
                model.setStockLocationTypeId(stockLocationTypeId);
                model.setStockLocationTypeName(stockLocationTypeName);
                infoList.add(model);
            }
            if (infoList.size() > 0) {
                buPurchaseNotshelfInfoDao.insertBuBatch(infoList);
            }

        }

    }

 /*   @SuppressWarnings("rawtypes")
	public Map<String, Object> getOrderRemak1(List<InventoryDetailsModel> listDetail) {
        Map<String, Object> remarkMapAll = new HashMap<String, Object>();
        //循环获取单号分别查询采购、销售、调拨表体备注
        List<String> purchaseOrderNoList = new ArrayList<String>();
        List<String> saleOrderNoList = new ArrayList<String>();
        List<String> transferOrderNoList = new ArrayList<String>();
        for (InventoryDetailsModel detail : listDetail) {
            if (InventoryStatusEnum.CGRK.getKey().equals(detail.getSourceType())) {
                purchaseOrderNoList.add(detail.getOrderNo());
            } else if (InventoryStatusEnum.XSCK.getKey().equals(detail.getSourceType())) {
                saleOrderNoList.add(detail.getBusinessNo());
            } else if (InventoryStatusEnum.DBRK.getKey().equals(detail.getSourceType())
                    || InventoryStatusEnum.DBCK.getKey().equals(detail.getSourceType())) {
                transferOrderNoList.add(detail.getBusinessNo());
            }
        }

        //获取采购订单表体备注 key = 采购出库单号+商品id
        List<Map<String, Object>> purchaseMapList = null;
        if (purchaseOrderNoList != null && purchaseOrderNoList.size() > 0) {
            //purchaseMapList = purchaseOrderItemDao.getOrderRemark(purchaseOrderNoList);
        }
        if (purchaseMapList != null && purchaseMapList.size() > 0) {
            for (Map map : purchaseMapList) {
                remarkMapAll.put((String) map.get("orderkey"), map.get("remark"));
            }
        }

        //获取销售订单表体备注 key = 销售单号+商品id
        List<Map<String, Object>> saleMapList = null;
        if (saleOrderNoList != null && saleOrderNoList.size() > 0) {
            //saleMapList = saleOrderDao.getOrderRemark(saleOrderNoList);
        }
        if (saleMapList != null && saleMapList.size() > 0) {
            for (Map map : saleMapList) {
                remarkMapAll.put((String) map.get("orderkey"), map.get("remark"));

            }
        }
        //获取调拨单表体备注 key = 调拨单号+商品id
        List<Map<String, Object>> transferMapList = null;
        if (transferOrderNoList != null && transferOrderNoList.size() > 0) {
          //  transferMapList = transferOrderDao.getOrderRemark(transferOrderNoList);
        }
        if (transferMapList != null && transferMapList.size() > 0) {
            for (Map map : transferMapList) {
                remarkMapAll.put((String) map.get("orderkey"), map.get("remark"));
            }
        }
        return remarkMapAll;
    }*/

    
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
     * 获取所有标准品牌信息
     */
    public Map<String, CommbarcodeModel> getBrand() {
        Map<String, CommbarcodeModel> brandMap = new HashMap<String, CommbarcodeModel>();
        try {
        	// 获取标准条码和品牌
            //List<BrandModel> list = brandDao.getListBrandInfo();
        	CommbarcodeModel commbarcodeModel=new CommbarcodeModel();
        	List<CommbarcodeModel> list = commbarcodeDao.list(commbarcodeModel);
            if (list != null && list.size() > 0) {
                for (CommbarcodeModel model : list) {
                    brandMap.put(model.getCommbarcode(), model);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brandMap;
    }

    /**
     * 分页保存业务报表损益表体
     */
    public void saveProfitLossSummaryItem(MerchantInfoModel merchant,DepotInfoModel depot,MerchantBuRelModel buModel, 
    		String month,
    		Map<Long, Map<String, Object>> allMerchandiseMap,
    		Map<String, Object> detailParamMap,Map<String, Map<String, Object>>itemSummaryMap,
    		Map<String, Map<String, Object>> locationAdjustmentMap) {
        //分页查询损益明细
        int startIndex = 0;
        while (true) {
            detailParamMap.put("startIndex", startIndex);
            List<InventoryDetailsModel> listDetail = inventoryDetailsDao.getBuProfitLossOutInStorageDetail(detailParamMap);
            if (listDetail == null || listDetail.size() < 1)
                break;

            List<BuInventorySummaryItemModel> itemList = new ArrayList<BuInventorySummaryItemModel>();
            for (InventoryDetailsModel detail : listDetail) {
            	Map<String, Object> merchandiseMap = allMerchandiseMap.get(detail.getGoodsId());
            	if (merchandiseMap==null) {
         			logger.error("事业部损益明细"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+detail.getGoodsId());
     			}
 				// 获取商品信息
             	Long goodsId = (Long) merchandiseMap.get("goodsId");
             	String goodsNo = (String) merchandiseMap.get("goodsNo");
             	String goodsName = (String) merchandiseMap.get("goodsName");
             	String commbarcode = (String) merchandiseMap.get("commbarcode");
             	String barcode = (String) merchandiseMap.get("barcode");
             	barcode=barcode.trim();
             	String factoryNo = (String) merchandiseMap.get("factoryNo");
             	String productName = (String) merchandiseMap.get("name");
             	Long brandId = (Long) merchandiseMap.get("brandId");
             	String brandName = (String) merchandiseMap.get("brandName");
             	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
             	// 数量
             	Integer num = detail.getNum();
             	if (num==null) num=0;
             	String type = detail.getType();//0 正常品  1 残次品
             	Long stockLocationTypeId = detail.getStockLocationTypeId();
             	String stockLocationTypeName = detail.getStockLocationTypeName();    
             	String unit = detail.getUnit();//单位
				if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;		
             	if (stockLocationTypeId!=null) {            		
        			String inLocationKey=barcode+","+stockLocationTypeId+","+unit;
            		if (locationAdjustmentMap.containsKey(inLocationKey)) {
            			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(inLocationKey);       			
            			if (DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//好品调减
    						Integer profitlossGoodOutSum = (Integer) locationKeyMap.get("profitlossGoodOutSum");
    						if (profitlossGoodOutSum==null) profitlossGoodOutSum=0;
    						locationKeyMap.put("profitlossGoodOutSum", num+profitlossGoodOutSum);
    					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//好品调增
    						Integer profitlossGoodInSum = (Integer) locationKeyMap.get("profitlossGoodInSum");
    						if (profitlossGoodInSum==null) profitlossGoodInSum=0;
    						locationKeyMap.put("profitlossGoodInSum", num+profitlossGoodInSum);						
    					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//坏品品调减
    						Integer profitlossDamagedOutSum = (Integer) locationKeyMap.get("profitlossDamagedOutSum");
    						if (profitlossDamagedOutSum==null) profitlossDamagedOutSum=0;
    						locationKeyMap.put("profitlossDamagedOutSum", num+profitlossDamagedOutSum);						
    					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//坏品品调增
    						Integer profitlossGoodInSum = (Integer) locationKeyMap.get("profitlossDamagedInSum");
    						if (profitlossGoodInSum==null) profitlossGoodInSum=0;
    						locationKeyMap.put("profitlossDamagedInSum", num+profitlossGoodInSum);						
    					}
        				locationAdjustmentMap.put(inLocationKey, locationKeyMap);        			
        			}else {
        				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
        				if (DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//好品调减						
        					locationKeyMap.put("profitlossGoodOutSum", num);
    					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//好品调增
    						locationKeyMap.put("profitlossGoodInSum", num);
    					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//坏品调减
    						locationKeyMap.put("profitlossDamagedOutSum", num);
    					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
    							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//坏品调增
    						locationKeyMap.put("profitlossDamagedInSum", num);
    					}
        				locationKeyMap.put("goodsName", goodsName);
        				locationKeyMap.put("brandId", brandId);
        				locationKeyMap.put("brandName", brandName);
        				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
        				locationKeyMap.put("commbarcode", commbarcode);    			
        				locationKeyMap.put("stockLocationTypeId", stockLocationTypeId);
        				locationKeyMap.put("stockLocationTypeName", stockLocationTypeName);
        				locationKeyMap.put("unit", unit);
        				locationAdjustmentMap.put(inLocationKey, locationKeyMap);
        			}
    			}
             	
	
				String itemKey=goodsId+","+unit;            	
             	if (itemSummaryMap.containsKey(itemKey)) {
             		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);               		
					if (DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//好品调减
						Integer profitlossGoodOutSum = (Integer) summaryMap.get("profitlossGoodOutSum");
						if (profitlossGoodOutSum==null) profitlossGoodOutSum=0;
						summaryMap.put("profitlossGoodOutSum", num+profitlossGoodOutSum);
					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//好品调增
						Integer profitlossGoodInSum = (Integer) summaryMap.get("profitlossGoodInSum");
						if (profitlossGoodInSum==null) profitlossGoodInSum=0;
						summaryMap.put("profitlossGoodInSum", num+profitlossGoodInSum);						
					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//坏品品调减
						Integer profitlossDamagedOutSum = (Integer) summaryMap.get("profitlossDamagedOutSum");
						if (profitlossDamagedOutSum==null) profitlossDamagedOutSum=0;
						summaryMap.put("profitlossDamagedOutSum", num+profitlossDamagedOutSum);						
					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//坏品品调增
						Integer profitlossGoodInSum = (Integer) summaryMap.get("profitlossDamagedInSum");
						if (profitlossGoodInSum==null) profitlossGoodInSum=0;
						summaryMap.put("profitlossDamagedInSum", num+profitlossGoodInSum);						
					}
					itemSummaryMap.put(itemKey, summaryMap);
             	}else {
					Map<String, Object> summaryMap=new HashMap<>();
					if (DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//好品调减						
						summaryMap.put("profitlossGoodOutSum", num);
					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//好品调增
						summaryMap.put("profitlossGoodInSum", num);
					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_0.equals(detail.getOperateType())) {//坏品调减
						summaryMap.put("profitlossDamagedOutSum", num);
					}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(type)&&
							DERP_INVENTORY.INVENTORY_OPERATETYPE_1.equals(detail.getOperateType())) {//坏品调增
						summaryMap.put("profitlossDamagedInSum", num);
					}
					itemSummaryMap.put(itemKey, summaryMap);
				}      
            	BuInventorySummaryItemModel itemModel = new BuInventorySummaryItemModel();
                itemModel.setCode(detail.getOrderNo());// 调整单号
                itemModel.setSourceType(detail.getSourceType());// 业务类别
                itemModel.setBusinessNo(detail.getBusinessNo());// 来源单据号
                itemModel.setGoodsId(goodsId); //商品ID
                itemModel.setGoodsNo(goodsNo); //商品货号
                itemModel.setGoodsName(goodsName); //商品名称
                itemModel.setBarcode(barcode); //商品条码
                itemModel.setCommbarcode(commbarcode);// 标准条码
                itemModel.setSuperiorParentBrand(superiorParentBrand);
                itemModel.setOperateType(detail.getOperateType());// 调整类型 0调减 1调增
                itemModel.setNum(detail.getNum());// 调整数量
                itemModel.setDivergenceDate(detail.getDivergenceDate());// 调整时间
                itemModel.setMerchantId(merchant.getId());// 商家ID
                itemModel.setMerchantName(merchant.getName());// 商家名称
                itemModel.setDepotId(depot.getId()); //仓库id
                itemModel.setDepotName(depot.getName()); //仓库名称
                itemModel.setBatchNo(detail.getBatchNo()); //原始批次号
                itemModel.setIsWorn(detail.getType()); //是否坏品
                itemModel.setProductionDate(detail.getProductionDate()); //生产日期
                itemModel.setOverdueDate(detail.getOverdueDate()); //失效日期
                itemModel.setThingName(detail.getThingName());//事务类型名称
                itemModel.setOwnMonth(month);// 归属月份
                itemModel.setProductName(productName);// 货品名称
                if (depot.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
                    itemModel.setUnit(detail.getUnit());
                }
                itemModel.setFactoryNo(factoryNo);// 工厂编码
                itemModel.setModifyDate(TimeUtils.getNow());
                itemModel.setCreateDate(TimeUtils.getNow());
                itemModel.setDetailType(InventorySummaryDetailStatusEnum.SYMX.getKey()); //明细类型
                itemModel.setBuId(buModel.getBuId());
                itemModel.setBuName(buModel.getBuName());
                itemModel.setStockLocationTypeId(detail.getStockLocationTypeId());
                itemModel.setStockLocationTypeName(detail.getStockLocationTypeName());
                itemList.add(itemModel);
                
            }
            buInventorySummaryItemDao.insertBuBatch(itemList);
            startIndex = startIndex + 1000;
        }
    }




    /**
     * 获取区间
     */
    private String getSurplusInterval(double rate) {
        double three = 3.0;
        double one = 1;
        double two = 2;
        if (rate > 0 && rate <= 0.1) {
            return "0＜X≤1/10";
        } else if (rate > 0.1 && rate <= 0.2) {
            return "1/10＜X≤1/5";
        } else if (rate > 0.2 && rate <= one/three) {
            return "1/5＜X≤1/3";
        } else if (rate > one/three && rate <= 0.5) {
            return "1/3＜X≤1/2";
        } else if (rate > 0.5 && rate <= two/three) {
            return "1/2＜X≤2/3";
        } else if (rate > two/three) {
            return "2/3以上";
        } else {
            return "过期";
        }
    }

    private String getSurplusRate(double rate) {
        if (rate > 0 && rate < 0.01) {
            return String.format("%.2f", 0.01);
        }
        return String.format("%.2f", rate);
    }
    
    
    /**
     * 生产(业务经销存)累计销售在途明细表
     * @param merchant
     * @param depot
     * @param month
     * @param addSaleNoshelfDetailsGXList
     * @param addSaleNoshelfDetailsDXList
     * @throws Exception
     */
    public void saveBusinessAddSaleNoshelfDetails(MerchantInfoModel merchant,DepotInfoModel depot,
    		MerchantBuRelModel buModel,String month,
    		List<Map<String, Object>> addSaleNoshelfDetailsDXList,
    		Map<Long, Map<String, Object>> allMerchandiseMap,Map<String, Map<String, Object>>itemSummaryMap)throws Exception{
    	List<BuBusinessAddSaleNoshelfDetailsModel> orderList= new ArrayList<>();
    	
    	for (Map<String, Object> map : addSaleNoshelfDetailsDXList) {
    		Long outId = (Long) map.get("id");//销售出库订单id
    		Long goodsId = (Long) map.get("goods_id");//商品id    	   		
    		String unit = (String) map.get("tallying_unit");//理货单位 
    		if (StringUtils.isEmpty(unit) || DERP.ORDER_TALLYINGUNIT_02.equals(unit)) {
    			unit="2";
            } else if (DERP.ORDER_TALLYINGUNIT_01.equals(unit)) {
            	unit="1";
            } else if (DERP.ORDER_TALLYINGUNIT_00.equals(unit)) {
            	unit="0";
            }
    		BigDecimal num = (BigDecimal) map.get("num");//在途表数量   	
    		
    		
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
    		if (merchandiseMap==null) {
     			logger.error("事业部累计销售在途明细表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
 			}
			// 获取商品信息    	
        	String goodsNo = (String) merchandiseMap.get("goodsNo");
        	String goodsName = (String) merchandiseMap.get("goodsName");
        	String commbarcode = (String) merchandiseMap.get("commbarcode");
        	String barcode = (String) merchandiseMap.get("barcode");
        	String factoryNo = (String) merchandiseMap.get("factoryNo");
        	String productName = (String) merchandiseMap.get("name");
        	Long brandId = (Long) merchandiseMap.get("brandId");
        	String brandName = (String) merchandiseMap.get("brandName");
        	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
        	
    		// 根据goodId查询商品表
    		//MerchandiseInfoModel merchandiseInfoModel = merchandiseInfoDao.searchById(goodsId);   		
    		// 财务经分销 销售未上架
    		BuBusinessAddSaleNoshelfDetailsModel addSaleNoshelfDetails =new BuBusinessAddSaleNoshelfDetailsModel();
    		addSaleNoshelfDetails.setMerchantId(merchant.getId());
    		addSaleNoshelfDetails.setMerchantName(merchant.getName());
    		addSaleNoshelfDetails.setDepotId(depot.getId());
    		addSaleNoshelfDetails.setDepotName(depot.getName());
    		addSaleNoshelfDetails.setOrderId(saleOrderModel.getId());
    		addSaleNoshelfDetails.setOrderCode(saleOrderModel.getCode());
    		addSaleNoshelfDetails.setPoNo(saleOrderModel.getPoNo());
    		if (DERP_ORDER.SALEORDER_BUSINESSMODEL_1.equals(saleOrderModel.getBusinessModel())) {
        		addSaleNoshelfDetails.setOrderType(DERP_REPORT.BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_3);//'1代销订单、2购销订单', 3 购销整批结算 4.购销实销实结
			}else if (DERP_ORDER.SALEORDER_BUSINESSMODEL_4.equals(saleOrderModel.getBusinessModel())) {
        		addSaleNoshelfDetails.setOrderType(DERP_REPORT.BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_4);//'1代销订单、2购销订单', 3 购销整批结算 4.购销实销实结

			}
    		/*SaleOutDepotModel saleOutDepotModel=new SaleOutDepotModel();
    		// 代销不查  销售出库订单
			if (!DERP_ORDER.SALEORDER_BUSINESSMODEL_2.equals(saleOrderModel.getBusinessModel())) {				
				saleOutDepotModel.setSaleOrderId(orderId);
				saleOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_018);
				saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
				if (saleOutDepotModel==null) {
					saleOutDepotModel=new SaleOutDepotModel();
				}
			}*/
			// 获取销售出库数量
			String outMonth = null;
			if (saleOutDepotModel.getDeliverDate()!=null) outMonth=TimeUtils.format(saleOutDepotModel.getDeliverDate(), "yyyy-MM");
			Integer saleOutNumInt = 0;
        	if (num!=null) saleOutNumInt=num.intValue();
        	 if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;	
             String itemKey=goodsId+","+unit; 
             if (month.equals(outMonth)) {
            	 if (itemSummaryMap.containsKey(itemKey)) {
 	         		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);  
 	         		Integer saleOutNum = (Integer) summaryMap.get("saleOutNum");
 					if (saleOutNum==null) saleOutNum=0;
 					summaryMap.put("saleOutNum", saleOutNumInt+saleOutNum);
 					itemSummaryMap.put(itemKey, summaryMap);         		         		
 				}else {
 					Map<String, Object> summaryMap=new HashMap<>();
 					summaryMap.put("saleOutNum", saleOutNumInt);
 					itemSummaryMap.put(itemKey, summaryMap);					
 				}
			}	
    		addSaleNoshelfDetails.setOutOrderCode(saleOutDepotModel.getCode());
    		addSaleNoshelfDetails.setOutOrderId(saleOutDepotModel.getId());
    		addSaleNoshelfDetails.setDeliverDate(saleOutDepotModel.getDeliverDate());
    		addSaleNoshelfDetails.setGoodsId(goodsId);
    		addSaleNoshelfDetails.setGoodsNo(goodsNo);
    		addSaleNoshelfDetails.setGoodsName(goodsName);
    		addSaleNoshelfDetails.setBarcode(barcode);
    		addSaleNoshelfDetails.setCommbarcode(commbarcode);
    		addSaleNoshelfDetails.setSuperiorParentBrand(superiorParentBrand);
    		addSaleNoshelfDetails.setTallyingUnit(unit);
    		addSaleNoshelfDetails.setNum(num.intValue());
    		addSaleNoshelfDetails.setMonth(month);   
    		addSaleNoshelfDetails.setOrderOwnMonth(saleOrderModel.getOwnMonth());
    		addSaleNoshelfDetails.setBuId(buModel.getBuId());
    		addSaleNoshelfDetails.setBuName(buModel.getBuName());
    		addSaleNoshelfDetails.setCustomerId(saleOrderModel.getCustomerId());
    		addSaleNoshelfDetails.setCustomerName(saleOrderModel.getCustomerName());
    		orderList.add(addSaleNoshelfDetails);   					
		}
    	   	

    	// 循环信息 事业部累计销售在途明细表
    	for (BuBusinessAddSaleNoshelfDetailsModel model : orderList) {
    		buBusinessAddSaleNoshelfDetailsDao.save(model);
		}    	
    }
    
     
    /**
     * 生成(业务经分销)本期减少销售在途明细表
     * @param merchant
     * @param depot
     * @param month
     * @param decreaseSaleNoshelfGXList
     * @param decreaseSaleNoshelfDXList
     * @throws Exception
     */
    public void saveBusinessDecreaseSaleNoshelf(MerchantInfoModel merchant,DepotInfoModel depot,
    		MerchantBuRelModel buModel,String month,
    		List<Map<String, Object>> decreaseSaleNoshelfDXList,
    		Map<Long, Map<String, Object>> allMerchandiseMap)throws Exception{

    	List<BuBusinessDecreaseSaleNoshelfModel> orderList= new ArrayList<>();
    	// 代销
    	for (Map<String, Object> map : decreaseSaleNoshelfDXList) {
    		Long outId = (Long) map.get("id");//销售出库订单id
    		Long goodsId = (Long) map.get("goods_id");//商品id    	   		
    		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    		    		
    		BigDecimal shelfNum = (BigDecimal) map.get("shelf_num");//上架数量
    		BigDecimal damagedNum = (BigDecimal) map.get("damaged_num");//坏品数量
    		BigDecimal lackNum = (BigDecimal) map.get("lack_num");//少货数量    
    		Timestamp shelfDate = (Timestamp) map.get("shelf_date");//上架时间
    		String poNo = (String) map.get("po_no");//po号    
    		if (StringUtils.isEmpty(tallyingUnit) || DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
    			tallyingUnit="2";
            } else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
            	tallyingUnit="1";
            } else if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
            	tallyingUnit="0";
            }
	        Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
	        if (merchandiseMap==null) {
     			logger.error("事业部本期减少销售在途明细表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
 			}
			// 获取商品信息
        	String goodsNo = (String) merchandiseMap.get("goodsNo");
        	String goodsName = (String) merchandiseMap.get("goodsName");
        	String commbarcode = (String) merchandiseMap.get("commbarcode");
        	String barcode = (String) merchandiseMap.get("barcode");
        	String factoryNo = (String) merchandiseMap.get("factoryNo");
        	String productName = (String) merchandiseMap.get("name");
        	Long brandId = (Long) merchandiseMap.get("brandId");
        	String brandName = (String) merchandiseMap.get("brandName");
        	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
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
	

    		// 代销不查  销售出库订单
    		/*SaleOutDepotModel saleOutDepotModel=new SaleOutDepotModel();
    		if (!DERP_ORDER.SALEORDER_BUSINESSMODEL_2.equals(saleOrderModel.getBusinessModel())) {				
				saleOutDepotModel.setSaleOrderId(orderId);
				saleOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_018);
				saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
				if (saleOutDepotModel==null) {
					saleOutDepotModel=new SaleOutDepotModel();
				}
			} */			  		    		   		       		
    		// 业务经销存 销售减少在途
    		BuBusinessDecreaseSaleNoshelfModel decreaseModel =new BuBusinessDecreaseSaleNoshelfModel();
    		decreaseModel.setMerchantId(merchant.getId());
    		decreaseModel.setMerchantName(merchant.getName());
    		decreaseModel.setDepotId(depot.getId());// 代销取销售订单原仓库id
    		decreaseModel.setDepotName(depot.getName());//代销取销售订单原仓库名称
    		decreaseModel.setOrderId(saleOrderModel.getId());
    		decreaseModel.setOrderCode(saleOrderModel.getCode());
    		if (DERP_ORDER.SALEANALYSIS_SALETYPE_1.equals(saleOrderModel.getBusinessModel())) {
    			decreaseModel.setOrderType(DERP_REPORT.BUSINESSDECREASESALENOSHELF_ORDERTYPE_3);//'1代销订单、2购销订单', 3 购销整批结算 4.购销实销实结
			}else if (DERP_ORDER.SALEANALYSIS_SALETYPE_4.equals(saleOrderModel.getBusinessModel())) {
				decreaseModel.setOrderType(DERP_REPORT.BUSINESSDECREASESALENOSHELF_ORDERTYPE_4);//'1代销订单、2购销订单', 3 购销整批结算 4.购销实销实结
			}

    		decreaseModel.setOrderCode(saleOrderModel.getCode());
    		decreaseModel.setPoNo(poNo);
    		decreaseModel.setOutOrderCode(saleOutDepotModel.getCode());
    		decreaseModel.setOutOrderId(saleOutDepotModel.getId());
    		decreaseModel.setDeliverDate(saleOutDepotModel.getDeliverDate());
    		decreaseModel.setGoodsId(goodsId);
    		decreaseModel.setGoodsNo(goodsNo);
    		decreaseModel.setGoodsName(goodsName);
    		decreaseModel.setBarcode(barcode);
    		decreaseModel.setCommbarcode(commbarcode);
    		decreaseModel.setSuperiorParentBrand(superiorParentBrand);
    		decreaseModel.setTallyingUnit(tallyingUnit);   		
    		decreaseModel.setShelfNum(shelfNum.intValue());
    		decreaseModel.setDamagedNum(damagedNum.intValue());
    		decreaseModel.setLackNum(lackNum.intValue());
    		decreaseModel.setMonth(month);   
    		decreaseModel.setShelfDate(shelfDate);
    		decreaseModel.setOrderOwnMonth(saleOrderModel.getOwnMonth());
    		decreaseModel.setBuId(buModel.getBuId());
    		decreaseModel.setBuName(buModel.getBuName());
    		orderList.add(decreaseModel);    					
		}

    	// 循环保存 (事业部业务经销存)本期减少销售在途
    	for (BuBusinessDecreaseSaleNoshelfModel model : orderList) {
    		buBusinessDecreaseSaleNoshelfDao.save(model);
		}   	
    }
            
    /**
     * 获取(业务经销存)累计调拨在途明细表
     * @param merchant
     * @param depot
     * @param month
     * @throws Exception
     */
    public void saveBusinessAddTransferNoshelf(MerchantInfoModel merchant,DepotInfoModel depot,
    		MerchantBuRelModel buModel,String month,
    		List<Map<String, Object>> businessAddTransferNoshelfList,
    		Map<Long, Map<String, Object>> allMerchandiseMap,Map<String, Map<String, Object>>itemSummaryMap,
    		Map<String, Map<String, Object>> locationAdjustmentMap)throws Exception{

    	List<BuBusinessAddTransferNoshelfDetailsModel> orderList= new ArrayList<>();
    	//调拨在途明细表
    	for (Map<String, Object> map : businessAddTransferNoshelfList) {
    		Long orderId = (Long) map.get("order_id");//调拨订单id
    		Long outOrderId = (Long) map.get("out_order_id");//调拨出库单id
    		String orderCode = (String) map.get("order_code");//调拨订单编码
    		String outOrderCode = (String) map.get("out_order_code");//调拨出库订单编码
    		Long outDepotId = (Long) map.get("out_depot_id");//出库仓库  id
    		String outDepotName = (String) map.get("out_depot_name");//出库仓库 名称
    		String poNo = (String) map.get("po_no");//po号  	   				    		
    		Timestamp transferDate = (Timestamp) map.get("transfer_date");//发货时间
    		Long num = (Long) map.get("num");//数量
    		Long goodsId = (Long) map.get("goods_id");//商品id   
    		//String goodsNo = (String) map.get("goods_no");//商品货号
    		//String goodsName = (String) map.get("goods_name");//商品名称
    		//String barcode = (String) map.get("barcode");//条形码
    		//String commbarcode = (String) map.get("commbarcode");//标准条码
       		String tallyingUnit = (String) map.get("tallying_unit");//理货单位    
       		Long stockLocationTypeId = (Long) map.get("stock_location_type_id");//库位类型id
       		String stockLocationTypeName = (String) map.get("stock_location_type_name");//库位类型名称   
       		
       		
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
       		
       		
    		if (StringUtils.isEmpty(tallyingUnit) || DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
    			tallyingUnit="2";
            } else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
            	tallyingUnit="1";
            } else if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
            	tallyingUnit="0";
            }
    		if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))tallyingUnit=null;	
	        Map<String, Object> merchandiseMap = allMerchandiseMap.get(goodsId);
	        if (merchandiseMap==null) {
     			logger.error("事业部累计调拨在途明细表"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
 			}
			// 获取商品信息
        	String goodsNo = (String) merchandiseMap.get("goodsNo");
        	String goodsName = (String) merchandiseMap.get("goodsName");
        	String commbarcode = (String) merchandiseMap.get("commbarcode");
        	String barcode = (String) merchandiseMap.get("barcode");
        	String factoryNo = (String) merchandiseMap.get("factoryNo");
        	String productName = (String) merchandiseMap.get("name");
        	Long brandId = (Long) merchandiseMap.get("brandId");
        	String brandName = (String) merchandiseMap.get("brandName");
        	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
        	
        	Integer monthTransferNotshelfIntNum = 0;
        	if (num!=null) monthTransferNotshelfIntNum=num.intValue();
        	
        	if (stockLocationTypeId!=null) {
    			String inLocationKey=barcode+","+stockLocationTypeId+","+tallyingUnit;
        		if (locationAdjustmentMap.containsKey(inLocationKey)) {
        			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(inLocationKey);       			
        			Integer monthTransferNotshelfNum = (Integer) locationKeyMap.get("monthTransferNotshelfNum");
 					if (monthTransferNotshelfNum==null) monthTransferNotshelfNum=0;
 					locationKeyMap.put("monthTransferNotshelfNum", monthTransferNotshelfIntNum+monthTransferNotshelfNum);
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);        			
    			}else {
    				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
    				locationKeyMap.put("monthTransferNotshelfNum", monthTransferNotshelfIntNum);
    				locationKeyMap.put("goodsName", goodsName);
    				locationKeyMap.put("brandId", brandId);
    				locationKeyMap.put("brandName", brandName);
    				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
    				locationKeyMap.put("commbarcode", commbarcode);    			
    				locationKeyMap.put("stockLocationTypeId", stockLocationTypeId);
    				locationKeyMap.put("stockLocationTypeName", stockLocationTypeName);
    				locationKeyMap.put("unit", tallyingUnit);
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);
    			}
			}
        	
        	String outMonth =null;
        	if (transferDate!=null)outMonth=TimeUtils.format(transferDate, "yyyy-MM");
        	
        	 
             String itemKey=goodsId+","+tallyingUnit; 
             if (month.equals(outMonth)) {
            	 if (itemSummaryMap.containsKey(itemKey)) {
 	         		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);  
 	         		Integer monthTransferNotshelfNum = (Integer) summaryMap.get("monthTransferNotshelfNum");
 					if (monthTransferNotshelfNum==null) monthTransferNotshelfNum=0;
 					summaryMap.put("monthTransferNotshelfNum", monthTransferNotshelfIntNum+monthTransferNotshelfNum);
 					itemSummaryMap.put(itemKey, summaryMap);          		         		
 				}else {
 					Map<String, Object> summaryMap=new HashMap<>();
 					summaryMap.put("monthTransferNotshelfNum", monthTransferNotshelfIntNum);
 					itemSummaryMap.put(itemKey, summaryMap); 					
 				}
			}
    		// (业务经销存)调拨在途明细表
    		BuBusinessAddTransferNoshelfDetailsModel AddTransferNoshelfModel =new BuBusinessAddTransferNoshelfDetailsModel();
    		AddTransferNoshelfModel.setMerchantId(merchant.getId());//商家id
    		AddTransferNoshelfModel.setMerchantName(merchant.getName());//商家名称
    		AddTransferNoshelfModel.setInDepotId(depot.getId());// 调入仓库id
    		AddTransferNoshelfModel.setInDepotName(depot.getName());//调入仓库仓库名称
    		AddTransferNoshelfModel.setOutDepotId(outDepotId);//出库仓库id
    		AddTransferNoshelfModel.setOutDepotName(outDepotName);//出库仓库名称
    		AddTransferNoshelfModel.setOrderId(orderId);//调拨订单id
    		AddTransferNoshelfModel.setOrderCode(orderCode);//调拨订单编码
    		AddTransferNoshelfModel.setOutOrderId(outOrderId);//出库单id
    		AddTransferNoshelfModel.setOutOrderCode(outOrderCode);//出库单编码
    		AddTransferNoshelfModel.setDeliverDate(transferDate);//发货时间
    		AddTransferNoshelfModel.setNum(num.intValue());// 数量
    		AddTransferNoshelfModel.setGoodsId(goodsId);//商品id
    		AddTransferNoshelfModel.setGoodsNo(goodsNo);// 商品货号
    		AddTransferNoshelfModel.setGoodsName(goodsName);//商品名称
    		AddTransferNoshelfModel.setBarcode(barcode);//条码
    		AddTransferNoshelfModel.setCommbarcode(commbarcode);//标准条码
    		AddTransferNoshelfModel.setSuperiorParentBrand(superiorParentBrand);
    		AddTransferNoshelfModel.setTallyingUnit(tallyingUnit);
    		AddTransferNoshelfModel.setMonth(month);
    		AddTransferNoshelfModel.setPoNo(poNo);    
    		AddTransferNoshelfModel.setBuId(buModel.getBuId());
    		AddTransferNoshelfModel.setBuName(buModel.getBuName());
    		AddTransferNoshelfModel.setStockLocationTypeId(stockLocationTypeId);
    		AddTransferNoshelfModel.setStockLocationTypeName(stockLocationTypeName);
    		orderList.add(AddTransferNoshelfModel);    					
		}
    	   	
    
    	// 循环保存 (事业部业务经销存)调拨在途明细表
    	for (BuBusinessAddTransferNoshelfDetailsModel model : orderList) {
    		buBusinessAddTransferNoshelfDetailsDao.save(model);
		}   	
    }
    
    /**
     * 生成事业部来货残次
     * @param merchant
     * @param depot
     * @param buModel
     * @param month
     * @throws Exception
     */
    public void saveBuBsinessInBad(MerchantInfoModel merchant,DepotInfoModel depot,
    		MerchantBuRelModel buModel,String month,
    		List<InventoryDetailsModel> inbadDetails,
    		Map<Long, Map<String, Object>> allMerchandiseMap,Map<String, Map<String, Object>>itemSummaryMap,
    		Map<String, Map<String, Object>> locationAdjustmentMap)throws Exception{
    	List<BuBusinessInBadDetailsModel> orderList= new ArrayList<>();   
    	   	
    	// 入库残次
    	for (InventoryDetailsModel detail : inbadDetails) {
    		Long goodsId = detail.getGoodsId();
    		Map<String, Object> merchandiseMap = allMerchandiseMap.get(detail.getGoodsId());
	        if (merchandiseMap==null) {
     			logger.error("事业部来货残次"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+goodsId);
 			}
        	// 获取商品信息
        	String goodsNo = (String) merchandiseMap.get("goodsNo");
        	String goodsName = (String) merchandiseMap.get("goodsName");
        	String commbarcode = (String) merchandiseMap.get("commbarcode");
        	String barcode = (String) merchandiseMap.get("barcode");
        	String factoryNo = (String) merchandiseMap.get("factoryNo");
        	String productName = (String) merchandiseMap.get("name");
        	Long brandId = (Long) merchandiseMap.get("brandId");
        	String brandName = (String) merchandiseMap.get("brandName");
        	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
        	
        	Integer num = detail.getNum();
			if (num==null) num=0;
        	Long stockLocationTypeId = detail.getStockLocationTypeId();
        	String stockLocationTypeName = detail.getStockLocationTypeName();
        	
        	String unit = detail.getUnit();//单位
			if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;	
			
        	if (stockLocationTypeId!=null) {
    			String inLocationKey=barcode+","+stockLocationTypeId+","+unit;
        		if (locationAdjustmentMap.containsKey(inLocationKey)) {
        			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(inLocationKey);       			
    				Integer monthInBadNum = (Integer) locationKeyMap.get("monthInBadNum");
    				if (monthInBadNum==null) monthInBadNum=0;
    				locationKeyMap.put("monthInBadNum", num+monthInBadNum);
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);        			
    			}else {
    				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
    				locationKeyMap.put("monthInBadNum", num);
    				locationKeyMap.put("goodsName", goodsName);
    				locationKeyMap.put("brandId", brandId);
    				locationKeyMap.put("brandName", brandName);
    				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
    				locationKeyMap.put("commbarcode", commbarcode);    			
    				locationKeyMap.put("stockLocationTypeId", stockLocationTypeId);
    				locationKeyMap.put("stockLocationTypeName", stockLocationTypeName);
    				locationKeyMap.put("unit", unit);
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);
    			}
			}
        	
        	
        	
        				
			String itemKey=goodsId+","+unit;			
        	if (itemSummaryMap.containsKey(itemKey)) {
        		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);           							
				Integer monthInBadNum = (Integer) summaryMap.get("monthInBadNum");
				if (monthInBadNum==null) monthInBadNum=0;
				summaryMap.put("monthInBadNum", num+monthInBadNum);
				itemSummaryMap.put(itemKey, summaryMap);       		
			}else {
				Map<String, Object> summaryMap=new HashMap<>();
				summaryMap.put("monthInBadNum", num);
				itemSummaryMap.put(itemKey, summaryMap);
			}        		
    		// 财务经分销 销售未上架
    		BuBusinessInBadDetailsModel InBadDetails =new BuBusinessInBadDetailsModel();
    		InBadDetails.setMerchantId(merchant.getId());
    		InBadDetails.setMerchantName(merchant.getName());
    		InBadDetails.setInDepotId(depot.getId());
    		InBadDetails.setInDepotName(depot.getName());
    		InBadDetails.setBuId(buModel.getBuId());
    		InBadDetails.setBuName(buModel.getBuName());
    		String orderType=null;
    		if (DERP_INVENTORY.INVENTORY_SOURCETYPE_001.equals(detail.getSourceType())) {//采购入库
    			orderType=DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_1;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0010.equals(detail.getSourceType())) {//调拨入库
				orderType=DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_2;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_005.equals(detail.getSourceType())) {//销售退货入库
				orderType=DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_3;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0029.equals(detail.getSourceType())) {//上架入库
				orderType=DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_4;
			}
    		InBadDetails.setOrderType(orderType);
    		//InBadDetails.setOrderId(inOrderId);
    		InBadDetails.setOrderCode(detail.getBusinessNo());
    		//InBadDetails.setInOrderId(inOrderId);
    		InBadDetails.setInOrderCode(detail.getOrderNo());
    		InBadDetails.setDeliverDate(detail.getDivergenceDate());
    		InBadDetails.setNum(detail.getNum());
    		InBadDetails.setGoodsId(goodsId);
    		InBadDetails.setGoodsNo(goodsNo);
    		InBadDetails.setGoodsName(goodsName);
    		InBadDetails.setBarcode(barcode);
    		InBadDetails.setCommbarcode(commbarcode);
    		InBadDetails.setSuperiorParentBrand(superiorParentBrand);
    		InBadDetails.setTallyingUnit(detail.getUnit());
    		InBadDetails.setMonth(month);   
    		InBadDetails.setStockLocationTypeId(detail.getStockLocationTypeId());
    		InBadDetails.setStockLocationTypeName(detail.getStockLocationTypeName());
    		orderList.add(InBadDetails);   					
		}
    	//生成
    	for (BuBusinessInBadDetailsModel model : orderList) {
    		buBusinessInBadDetailsDao.save(model);
		}
	
    }

    /**
     * 
     * @param merchant
     * @param depot
     * @param buModel
     * @param month
     * @param buPurchaseInBadList
     * @param buTransferInBadList
     * @param buSaleReturnInBadList
     * @param buSaleShelfInBadList
     * @throws Exception
     */
    public void saveBuBsinessOutBad(MerchantInfoModel merchant,DepotInfoModel depot,
    		MerchantBuRelModel buModel,String month,
    		List<InventoryDetailsModel> outBadDetailsList,
    		Map<Long, Map<String, Object>> allMerchandiseMap,Map<String, Map<String, Object>>itemSummaryMap,
    		Map<String, Map<String, Object>> locationAdjustmentMap)throws Exception{
    	List<BuBusinessOutBadDetailsModel> orderList= new ArrayList<>();
			
    	// 出库残次
    	for (InventoryDetailsModel detail : outBadDetailsList) {

	        Map<String, Object> merchandiseMap = allMerchandiseMap.get(detail.getGoodsId());
	        if (merchandiseMap==null) {
     			logger.error("事业部出库残次"+"商家:"+merchant.getName()+",仓库:"+depot.getName()+",事业部:"+buModel.getBuName()+",月份:"+month+ "商品表没有此商品数据,商品id:"+detail.getGoodsId());
 			}
			// 获取商品信息
        	Long goodsId = (Long) merchandiseMap.get("goodsId");
        	String goodsNo = (String) merchandiseMap.get("goodsNo");
        	String goodsName = (String) merchandiseMap.get("goodsName");
        	String commbarcode = (String) merchandiseMap.get("commbarcode");
        	String barcode = (String) merchandiseMap.get("barcode");
        	String factoryNo = (String) merchandiseMap.get("factoryNo");
        	String productName = (String) merchandiseMap.get("name");
        	Long brandId = (Long) merchandiseMap.get("brandId");
        	String brandName = (String) merchandiseMap.get("brandName");
        	String superiorParentBrand = (String) merchandiseMap.get("superiorParentBrand");
        	
			Integer num = detail.getNum();
			if (num==null)num=0;
			Long stockLocationTypeId = detail.getStockLocationTypeId();
			String stockLocationTypeName = detail.getStockLocationTypeName();
			
			String unit = detail.getUnit();//单位
			if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType()))unit=null;	
			if (stockLocationTypeId!=null) {
    			String inLocationKey=barcode+","+stockLocationTypeId+","+unit;
        		if (locationAdjustmentMap.containsKey(inLocationKey)) {
        			Map<String, Object> locationKeyMap = locationAdjustmentMap.get(inLocationKey);       			
    				Integer monthOutBadNum = (Integer) locationKeyMap.get("monthOutBadNum");
    				if (monthOutBadNum==null) monthOutBadNum=0;
    				locationKeyMap.put("monthOutBadNum", num+monthOutBadNum);
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);        			
    			}else {
    				Map<String, Object> locationKeyMap = new HashMap<String, Object>();
    				locationKeyMap.put("monthOutBadNum", num);
    				locationKeyMap.put("goodsName", goodsName);
    				locationKeyMap.put("brandId", brandId);
    				locationKeyMap.put("brandName", brandName);
    				locationKeyMap.put("superiorParentBrand", superiorParentBrand);
    				locationKeyMap.put("commbarcode", commbarcode);    			
    				locationKeyMap.put("stockLocationTypeId", stockLocationTypeId);
    				locationKeyMap.put("stockLocationTypeName", stockLocationTypeName);
    				locationKeyMap.put("unit", unit);
    				locationAdjustmentMap.put(inLocationKey, locationKeyMap);
    			}
			}
        	
        				
			String itemKey=goodsId+","+unit;		
        	if (itemSummaryMap.containsKey(itemKey)) {
        		Map<String, Object> summaryMap = itemSummaryMap.get(itemKey);           							
				Integer monthOutBadNum = (Integer) summaryMap.get("monthOutBadNum");
				if (monthOutBadNum==null) monthOutBadNum=0;
				summaryMap.put("monthOutBadNum", num+monthOutBadNum);
				itemSummaryMap.put(itemKey, summaryMap);       		
			}else {
				Map<String, Object> summaryMap=new HashMap<>();
				summaryMap.put("monthOutBadNum", num);
				itemSummaryMap.put(itemKey, summaryMap);
			} 
        	
        	String orderType=null;
        	if (DERP_INVENTORY.INVENTORY_SOURCETYPE_003.equals(detail.getSourceType())) {//销售出库
        		orderType=DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_1;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0011.equals(detail.getSourceType())) {//调拨出库
				orderType=DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_2;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_006.equals(detail.getSourceType())) {//销售退货出库
				orderType=DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_3;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0030.equals(detail.getSourceType())) {//采购退货出库
				orderType=DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_4;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0021.equals(detail.getSourceType())) {//采购执行
				orderType=DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_5;
			}else if (DERP_INVENTORY.INVENTORY_SOURCETYPE_0033.equals(detail.getSourceType())) {//领料单
				orderType=DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_6;
			}
	
        	
    		// 出库残次
    		BuBusinessOutBadDetailsModel outBadDetails =new BuBusinessOutBadDetailsModel();
    		outBadDetails.setMerchantId(merchant.getId());
    		outBadDetails.setMerchantName(merchant.getName());
    		outBadDetails.setOutDepotId(depot.getId());
    		outBadDetails.setOutDepotName(depot.getName());
    		outBadDetails.setBuId(buModel.getBuId());
    		outBadDetails.setBuName(buModel.getBuName());
    		outBadDetails.setOrderType(orderType);
    		//outBadDetails.setOrderId(orderId);
    		outBadDetails.setOrderCode(detail.getBusinessNo());
    		//outBadDetails.setOutOrderId(outOrderId);
    		outBadDetails.setOutOrderCode(detail.getOrderNo());
    		outBadDetails.setDeliverDate(detail.getDivergenceDate());
    		outBadDetails.setNum(detail.getNum());
    		outBadDetails.setGoodsId(goodsId);
    		outBadDetails.setGoodsNo(goodsNo);
    		outBadDetails.setGoodsName(goodsName);
    		outBadDetails.setBarcode(barcode);
    		outBadDetails.setCommbarcode(commbarcode);
    		outBadDetails.setSuperiorParentBrand(superiorParentBrand);
    		outBadDetails.setTallyingUnit(detail.getUnit());
    		outBadDetails.setMonth(month);   
    		outBadDetails.setStockLocationTypeId(detail.getStockLocationTypeId());
    		outBadDetails.setStockLocationTypeName(detail.getStockLocationTypeName());
    		orderList.add(outBadDetails);   					
		}
       	
    	// 循环信息生成出库残损
    	for (BuBusinessOutBadDetailsModel model : orderList) {
    		buBusinessOutBadDetailsDao.save(model);
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
        	requestMessage.put("describe", "定时器刷新事业部业务经销存");
		}else {
			requestMessage.put("describe", "页面/其他刷新业务经销存");
		}                           
        requestMessage.put("ownMonth", ownMonth);
    	
        //数据集
       MQLog mqLog=new MQLog();
       mqLog.setKeyword("R"+ TimeUtils.getNow().getTime());
       mqLog.setStartDate(System.currentTimeMillis());// 开始时间
       mqLog.setDesc("生成事业部业务进销存报表");
       mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_13201501040));//
       mqLog.setModel(DERP_LOG_POINT.POINT_13201501040_Label);//
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
       logger.info("刷新事业部业务经销存日志报文："+jsonObject.toString()+"==");

		SendResult sendResult;
		try {
			sendResult = rocketLogMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("==报文："+jsonObject.toString()+"==");
		

		
	}
}

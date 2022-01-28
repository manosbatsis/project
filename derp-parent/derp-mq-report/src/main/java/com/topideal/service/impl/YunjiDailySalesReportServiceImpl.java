package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.log.MQLog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.CrawlerBillDao;
import com.topideal.dao.order.CrawlerInventoryDao;
import com.topideal.dao.order.MerchandiseContrastDao;
import com.topideal.dao.order.MerchandiseContrastItemDao;
import com.topideal.dao.reporting.YunjiDailySalesReportDao;
import com.topideal.dao.system.BrandDao;
import com.topideal.dao.system.MerchandiseCatDao;
import com.topideal.dao.system.MerchandiseInfoDao;
import com.topideal.entity.vo.order.CrawlerBillModel;
import com.topideal.entity.vo.order.CrawlerInventoryModel;
import com.topideal.entity.vo.order.MerchandiseContrastItemModel;
import com.topideal.entity.vo.order.MerchandiseContrastModel;
import com.topideal.entity.vo.reporting.YunjiDailySalesReportModel;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.service.YunjiDailySalesReportService;

import net.sf.json.JSONObject;

/**
 *
 * 新云集生成采销日报天数据
 * 
 * @author longcheng.mao
 *
 */
@Service
public class YunjiDailySalesReportServiceImpl implements YunjiDailySalesReportService {

    // 爬虫账单
    @Autowired
    CrawlerBillDao crawlerBillDao;
    // 商品
    @Autowired
    MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    // 商品对照表
    @Autowired
    private MerchandiseContrastDao merchandiseContrastDao;
    // 商品对照表表体
    @Autowired
    private MerchandiseContrastItemDao merchandiseContrastItemDao;
    @Autowired
    private MerchandiseInfoDao merchandiseInfoDao;
    // 云集爬虫数据
    @Autowired
    CrawlerInventoryDao crawlerInventoryDao;
    @Autowired
    MerchandiseCatDao merchandiseCatDao;
    @Autowired
    BrandDao brandDao;
	@Autowired
	ReptileConfigMongoDao reptileConfigMongoDao;
	@Autowired
	private YunjiDailySalesReportDao yunjiDailySalesReportDao;// 云集采销日报
	@Autowired
	private RMQLogProducer rocketLogMQProducer;


    /* 打印日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(YunjiDailySalesReportServiceImpl.class);

    /*
     * 生成采销日报天数据(前一天)
     * 页面刷新(根据传来的时间刷新)
     * 每日生成
     */
    @Override
    //@SystemServiceLog(point="13201502000",model="云集采销日报")
    public boolean getYunjiDailySalesReport(String json, String keys, String topics, String tags,MerchantInfoModel merchantInfo)
            throws Exception {
    	
        LOGGER.info("=============生成云集采销日报天数据开始==========="+json);       
        JSONObject jsonData = JSONObject.fromObject(json);
        String sDate = (String) jsonData.get("snapshotDate");// 单个刷新时间

        Timestamp time=null;
        if (sDate != null) {      	
            time = Timestamp.valueOf(sDate + " 00:00:00");

        }else {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");                      	  
    	    Calendar c = Calendar.getInstance();               	  
    	    c.add(Calendar.DATE, - 2);               	  
    	    Date timeDate = c.getTime();     	    
    	    String format = sdf.format(timeDate);
    	    time = Timestamp.valueOf(format + " 00:00:00");// 两天前是日期
		}
        
		
		
        /**
         * 说明: 只显示 当日账单和当日库存的数据
         */
		// 云集爬虫账单
		CrawlerBillModel cModel = new CrawlerBillModel();
		cModel.setBillDate(time);//爬虫账单时间
		cModel.setMerchantId(merchantInfo.getId());
		//云集爬虫库存
        CrawlerInventoryModel iModel=new CrawlerInventoryModel(); 
        iModel.setMerchantId(merchantInfo.getId());
        iModel.setInventoryDate(time);// 云集库存
        //云集采销日报
        YunjiDailySalesReportModel yModel = new YunjiDailySalesReportModel();
        yModel.setMerchantId(merchantInfo.getId());       
        yModel.setSnapshotDate(time);
        // 获取当年云集的 账单数据
        List<Map<String, Object>> yearCrawlerBillList=crawlerBillDao.getYearYunjiCrawlerBill(cModel);
        // 获取当月云集的账单数据
        List<Map<String, Object>> monthCrawlerBillList=crawlerBillDao.getMonthYunjiCrawlerBill(cModel);
        // 获取当日云集的账单数据
        List<Map<String, Object>> dayCrawlerBillList=crawlerBillDao.getDayYunjiCrawlerBill(cModel);
        // 获取当日云集的库存数据 (保税仓)
        List<Map<String, Object>> dayCrawlerInventoryList=crawlerInventoryDao.getDayCrawlerInventory(iModel);
        // 获取当日云集的库存数据 (退货仓)
        List<Map<String, Object>> dayReturnbinInventoryList = crawlerInventoryDao.getDayReturnbinInventory(iModel);
        
        //当日的账单和当日库存相同sku合并 (避免sku 账单有库存没有 或者库存有 账单没有数据漏统计)
        Map<String, String> billAndInventoryMap=new HashMap<>();        
        // 云集当日库存Map(保税仓)
        Map<String, Integer> dayInventoryMap=new HashMap<>();
        // 云集当日库存Map(退货仓)
        Map<String, Integer> dayReturnbinInventoryMap=new HashMap<>();
        // 云集当日销量Map
        Map<String, Integer> dayBillMap=new HashMap<>();
        // 云集当月销量Map
        Map<String, Integer> monthBillMap=new HashMap<>();
        // 云集当年销量map
        Map<String, Integer> yearBillMap=new HashMap<>();
        
       
        // 当日库存(保税仓)
        for (Map<String, Object> map : dayCrawlerInventoryList) {
        	Long merchantId = (Long) map.get("merchant_id");
        	String sku = (String) map.get("sku");
        	BigDecimal inventoryNum = (BigDecimal) map.get("inventory_num");
        	if (inventoryNum==null) {
        		inventoryNum=new BigDecimal(0);
			}
        	String key=merchantId+""+sku;
        	billAndInventoryMap.put(key, sku);
        	dayInventoryMap.put(key, inventoryNum.intValue());
		}
        // 当日库存(退货仓)
        for (Map<String, Object> map : dayReturnbinInventoryList) {
        	Long merchantId = (Long) map.get("merchant_id");
        	String sku = (String) map.get("sku");
        	BigDecimal inventoryNum = (BigDecimal) map.get("inventory_num");
        	if (inventoryNum==null) {
        		inventoryNum=new BigDecimal(0);
			}
        	String key=merchantId+""+sku;
        	billAndInventoryMap.put(key, sku);
        	dayReturnbinInventoryMap.put(key, inventoryNum.intValue());
		}
        
        //当日账单
        for (Map<String, Object> map : dayCrawlerBillList) {
        	Long merchantId = (Long) map.get("merchant_id");
        	String sku = (String) map.get("goods_no");
        	BigDecimal totalSalesQty = (BigDecimal) map.get("total_sales_qty");
        	if (totalSalesQty==null) {
        		totalSalesQty=new BigDecimal(0);
			}
        	String key=merchantId+""+sku;
        	billAndInventoryMap.put(key, sku);
        	dayBillMap.put(key, totalSalesQty.intValue());
        	
		}
        // 当月账单
        for (Map<String, Object> map : monthCrawlerBillList) {
        	Long merchantId = (Long) map.get("merchant_id");
        	String sku = (String) map.get("goods_no");
        	BigDecimal totalSalesQty = (BigDecimal) map.get("total_sales_qty");
        	if (totalSalesQty==null) {
        		totalSalesQty=new BigDecimal(0);
			}
        	String key=merchantId+""+sku;
        	monthBillMap.put(key, totalSalesQty.intValue());
		}
        // 当年账单
        for (Map<String, Object> map : yearCrawlerBillList) {
        	Long merchantId = (Long) map.get("merchant_id");
        	String sku = (String) map.get("goods_no");
        	BigDecimal totalSalesQty = (BigDecimal) map.get("total_sales_qty");
        	if (totalSalesQty==null) {
        		totalSalesQty=new BigDecimal(0);
			}
        	String key=merchantId+""+sku;
        	yearBillMap.put(key, totalSalesQty.intValue());
		}
        
        // 封装云集采销日报实体
        List<YunjiDailySalesReportModel>yunjiDailyList=new ArrayList<>();
        String state1="1";// 0失败 1成功
        String returnMessage1=merchantInfo.getName()+":";
        // 循环当合并后的sku 
        Set<String> keySet = billAndInventoryMap.keySet();
        for (String key : keySet) {
	
        	String sku = billAndInventoryMap.get(key);//sku    
        	Integer dayInventoryNum = dayInventoryMap.get(key);// 当日库存
            Integer dayReturnBinNum = dayReturnbinInventoryMap.get(key);// 退货仓当日库存
            Integer daySaleNum = dayBillMap.get(key);// 保税仓当日销量            
            Integer monthSaleNum = monthBillMap.get(key);// 当月销量
            Integer yearSaleNum = yearBillMap.get(key); // 当年销量       
        	if (dayInventoryNum==null) {
        		dayInventoryNum=0;
			}
        	if (dayReturnBinNum==null) {
        		dayReturnBinNum=0;
			}
        	if (daySaleNum==null) {
        		daySaleNum=0;
			}
        	if (monthSaleNum==null) {
        		monthSaleNum=0;
			}
        	if (yearSaleNum==null) {
        		yearSaleNum=0;
			}
              	
        	// 根据sku获取商品
        	List<MerchandiseContrastItemModel> contrastItemList = getMerchandiseContrastItem(merchantInfo, sku);
        	if (contrastItemList==null||contrastItemList.size()==0) {      		
        		sendLog("0", "根据商家id"+merchantInfo.getId()+",sku"+sku+"没有找到爬虫商品对照表表体信息");
        		continue;
        	}
        	int fail=0;
        	for (MerchandiseContrastItemModel contrastItem : contrastItemList) {
        		
        		Integer itemNum = contrastItem.getNum();
        		// 根据商品货号 和商家查询商品
            	MerchandiseInfoModel merchandiseInfoModel=new MerchandiseInfoModel();
            	merchandiseInfoModel.setId(contrastItem.getGoodsId());
            	merchandiseInfoModel.setStatus("1");
            	merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);
            	if (merchandiseInfoModel==null) {      		
            		sendLog("0", "根据商家id"+merchantInfo.getId()+",货号"+contrastItem.getGoodsNo()+"没有找到启用状态的商品");
            		fail=fail+1;
            		break;
            	}
            	
            	BrandModel brand=brandDao.searchById(merchandiseInfoModel.getBrandId());;
            	
            	if (brand == null) brand=new BrandModel();            	
            	YunjiDailySalesReportModel yunjiDaily=new YunjiDailySalesReportModel();
            	yunjiDaily.setMerchantId(merchantInfo.getId());//商家ID
            	yunjiDaily.setMerchantName(merchantInfo.getName());//商家名称
            	yunjiDaily.setBrandId(brand.getId());//品牌id
            	yunjiDaily.setBrandName(brand.getName());//品牌名称
            	yunjiDaily.setProductTypeName0(merchandiseInfoModel.getProductTypeName0());//产品一级分类名称
            	yunjiDaily.setProductTypeId0(merchandiseInfoModel.getProductTypeId0());//产品一级分类id
            	yunjiDaily.setProductTypeName(merchandiseInfoModel.getProductTypeName());//产品二级分类名称
            	yunjiDaily.setProductTypeId(merchandiseInfoModel.getProductTypeId());//产品二级分类id
            	yunjiDaily.setGoodsName(merchandiseInfoModel.getName());//商品名称
            	yunjiDaily.setGoodsId(merchandiseInfoModel.getId());//商品ID
            	yunjiDaily.setGoodsNo(merchandiseInfoModel.getGoodsNo());//商品货号
            	yunjiDaily.setBarcode(merchandiseInfoModel.getBarcode());//条形码
            	yunjiDaily.setCommbarcode(merchandiseInfoModel.getCommbarcode());//标准条码
            	// 数量相乘

            	yunjiDaily.setDaySaleNum(daySaleNum*itemNum);//当日销售量
            	yunjiDaily.setMonthSaleNum(monthSaleNum*itemNum);//当月销售量
            	yunjiDaily.setYearSaleNum(yearSaleNum*itemNum);//当年销量
            	yunjiDaily.setDayInventoryNum(dayInventoryNum*itemNum);//当日库存
            	yunjiDaily.setDayReturnBinNum(dayReturnBinNum*itemNum);
            	yunjiDaily.setSnapshotDate(time);// 快照时间
            	
            	yunjiDailyList.add(yunjiDaily);
            	
    		}	
			
		}
        
        // 删除 当日的云集采销日报
        yunjiDailySalesReportDao.deleteYunjiDailySalesReport(yModel);
        // 生成 云集采销日报
        for (YunjiDailySalesReportModel model : yunjiDailyList) {
        	yunjiDailySalesReportDao.save(model);
		}
        sendLog("1", "成功");
        return true;
    }

    /**
     * 
     * @param state
     * @param expMsg
     * @throws Exception 
     */
    private void sendLog(String state, String expMsg) throws Exception {
    	 //数据集
		MQLog mqLog=new MQLog();
		mqLog.setKeyword("R"+ TimeUtils.getNow().getTime());
        mqLog.setStartDate(System.currentTimeMillis());// 开始时间
        mqLog.setDesc("云集采销日报");
        mqLog.setPoint(Long.valueOf(DERP_LOG_POINT.POINT_13201502000));
        mqLog.setEndDate(System.currentTimeMillis());//结束时间
       String returnMessage="";
        //消费状态
        if ("0".equals(state)) {
        	mqLog.setState(0);
        	mqLog.setExpMsg(expMsg);
        	returnMessage=expMsg;
		}else {
			mqLog.setState(1);//成功
			returnMessage="成功";
			mqLog.setExpMsg("");
			
		}
        JSONObject jsonObject=JSONObject.fromObject(mqLog);
        jsonObject.put("returnMessage",returnMessage);
        //设置响应报文
        
        jsonObject.put("id", UUID.randomUUID().toString());
        jsonObject.put("moduleCode", LogModuleType.MODULE_REPORT.getType());
        jsonObject.put("modulCode", "1002");
        LOGGER.info("云集采销日报推送蓝精灵日志报文："+jsonObject.toString()+"==");

		SendResult sendResult = rocketLogMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_CONSUME_MONITOR.getTopic(),MQLogEnum.LOG_CONSUME_MONITOR.getTags());
		LOGGER.info("==报文："+jsonObject.toString()+"==");
		LOGGER.info("==响应报文："+sendResult+"==");
        /**String resultMsg=SmurfsUtils.sendLog(jsonObject,SmurfsAPIEnum.SMURFS_DERPLOG_REPORT);
        LOGGER.info("云集采销日报推送蓝精灵响应报文："+resultMsg);
        */
		
	}


	/**
     * 更加sku 获取商品
     * @param merchantInfo
     * @param reptileConfig
     * @param goodsNo
     * @return
     * @throws Exception
     */
    public List<MerchandiseContrastItemModel>  getMerchandiseContrastItem (MerchantInfoModel merchantInfo,String sku)throws Exception{
    	
    	// 查询商品对照表获取对应经分销货号
    	MerchandiseContrastModel contrastModel = new MerchandiseContrastModel();
		contrastModel.setCrawlerGoodsNo(sku);
		contrastModel.setMerchantId(merchantInfo.getId());
		contrastModel.setStatus("0");// 0-启用 1-禁用
		contrastModel.setType("1");// 1云集 2唯品
		List<MerchandiseContrastModel> contrastList = merchandiseContrastDao.list(contrastModel);		
    	if (contrastList == null || contrastList.size() <= 0) {
    		LOGGER.error("根据"+sku+"在商品对照表表头中没有查到商品");
    		sendLog("0", "根据"+sku+"在商品对照表表头中没有查到商品");
    		return null;
		} 
    	if (contrastList.size()>1) {
    		LOGGER.error("根据"+sku+"查询商品对照表表头查询到多条");
    		sendLog("0", "根据"+sku+"查询商品对照表表头查询到多条");
    		return null;
		}
    	MerchandiseContrastModel contrast = contrastList.get(0);
    	MerchandiseContrastItemModel contrastItemQuery=new MerchandiseContrastItemModel();
    	contrastItemQuery.setContrastId(contrast.getId());
    	List<MerchandiseContrastItemModel> contrastItemList = merchandiseContrastItemDao.list(contrastItemQuery);
    	if (contrastItemList.size()<=0) {
    		LOGGER.error("根据"+sku+"在商品对照表表体中没有查到商品");
    		sendLog("0", "根据"+sku+"在商品对照表表体中没有查到商品");
    		return null;
		}
    	return contrastItemList;		
    	
    } 
    


}

package com.topideal.mq.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.dao.sale.YunjiAccountDataDao;
import com.topideal.dao.sale.YunjiDeliveryDetailDao;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.YunJiDeliveryAndReturnDetailService;

import net.sf.json.JSONObject;
/**
 * 根据云集爬虫账单生成退货入库单和 销售出库清单MQ
 */
@Component
public class YunJiDeliveryAndReturnDetailMQ extends ConsumerMQAbstract{
	
	@Autowired
	private YunJiDeliveryAndReturnDetailService yunJiDeliveryAndReturnDetailService;
	//云集爬虫发货明细
	@Autowired
	private YunjiDeliveryDetailDao yunjiDeliveryDetailDao;
	@Autowired
	private YunjiAccountDataDao yunjiAccountDataDao;
	// 商家
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;		
	//MQ
	@Autowired
	private RMQProducer mqProducer;
	/**
	 * 打印日志
	 */
	public static final Logger logger= LoggerFactory.getLogger(YunJiDeliveryAndReturnDetailMQ.class);
	
	public YunJiDeliveryAndReturnDetailMQ(){
		super.setTags(MQOrderEnum.TIMER_YUNJI_DELIVERY_RETURN_DETAIL.getTags());
		super.setTopics(MQOrderEnum.TIMER_YUNJI_DELIVERY_RETURN_DETAIL.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		logger.info("===================根据云集退货爬虫明细单生成销售退货入库单开始=================="+json);
		//锁住代码块避免重复出库
		synchronized(this){
			JSONObject jsonObj = JSONObject.fromObject(json);
			String sourceType = (String) jsonObj.get("sourceType");//3云集发货,4云集退货
			String settleIds = (String) jsonObj.get("settleIds");//指定账单id
			String userCode = (String) jsonObj.get("userCode");//用户名称
			/**如果有指定账单id则按指定账单id生成出库单/销售退货入库单*/
			List<String> settleIdList = new ArrayList<String>();
			if(StringUtils.isNotBlank(settleIds)){
				String[] settleIdarr = settleIds.split(",");
				if(settleIdarr!=null&&settleIdarr.length>0){
					for(String settleId:settleIdarr){
						settleIdList.add(settleId);
					}
				}
			}
			//  获取云集结算账单表头有哪些商家
			Map<String, Object> detailMap = new HashMap<String, Object>();
			detailMap.put("idList", settleIdList);
			detailMap.put("userCode", userCode);			
			List<Map<String, Object>> yunjiAccountMerchantList = yunjiAccountDataDao.getYunjiAccountMerchant(detailMap);

			//云集退货
			if(StringUtils.isBlank(sourceType)||"4".equals(sourceType)){
				logger.info("===================生成云集销售退货入库单开始==================");
				try{
					// 一个商家一个循环商家出库
					for (Map<String, Object> yunjiAccountMap : yunjiAccountMerchantList) {
						Long merchantId = (Long) yunjiAccountMap.get("merchant_id");// 商家id		
						String accountUserCode = (String) yunjiAccountMap.get("user_code");// 用户	
						if (merchantId==null) {
							logger.info("云集发货爬虫账单-商家id为空,结束运行");
							continue;
						}			
						// 根据商家id查询 查询云集商家配置表				
						Map<String, Object> merchantMap = new HashMap<String, Object>();
						merchantMap.put("merchantId", merchantId);
						MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
						if (merchant==null) {
							logger.info("云集发货爬虫明细-未找到商家信息,结束运行");
							continue;
						}
						try {
							//云集退货数据生成销售退货入库单
							List<InvetAddOrSubtractRootJson> rootJsonList = yunJiDeliveryAndReturnDetailService.saveYunJiReturnDetail(json,keys,topics,tags,settleIdList,merchant,accountUserCode);
							if (rootJsonList!=null&&rootJsonList.size()>0) {
								/**	4.批量推送库存**/
								for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : rootJsonList) {
									Thread.sleep(100);
									// 推送库存
									mqProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
								}
							}
							
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("执行云集爬虫退货明细消费异常",e.getMessage());
						}
						
					
					}
					
				}catch(Exception e) {
					e.printStackTrace();
					logger.error("云集退货爬虫明细单生成销售退货入库单消费异常",e.getMessage());
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				// 睡眠40秒后进行云集发货
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			    logger.info("===================生成云集销售退货入库单结束==================");
			}
			
			
			//3云集发货
			if(StringUtils.isBlank(sourceType)||"3".equals(sourceType)){
				logger.info("===================执行云集爬虫发货明细开始==================");
				try{
					/**0、清空云集临时表*/
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("sourceType", "1");//数据来源 1-云集 2-唯品 
					yunJiDeliveryAndReturnDetailService.clearTable(paramMap);
					// 循环商家
					for (Map<String, Object> yunjiAccountMap : yunjiAccountMerchantList) {
						Long merchantId = (Long) yunjiAccountMap.get("merchant_id");// 商家id		
						String accountUserCode = (String) yunjiAccountMap.get("user_code");// 用户
						if (merchantId==null) {
							logger.info("云集发货爬虫账单-商家id为空,结束运行");
							continue;
						}			
						// 根据商家id查询 查询云集商家配置表				
						Map<String, Object> merchantMap = new HashMap<String, Object>();
						merchantMap.put("merchantId", merchantId);
						MerchantInfoMongo merchant = merchantMongoDao.findOne(merchantMap);
						if (merchant==null) {
							logger.info("云集发货爬虫明细-未找到商家信息,结束运行");
							continue;
						}
						try {
							
							/** 获取所有出库单账单号 */
							Map<String, Object> detailMerchantMap = new HashMap<String, Object>();
							detailMerchantMap.put("idList", settleIdList);
							detailMerchantMap.put("merchantId", merchant.getMerchantId());
							detailMerchantMap.put("userCode", accountUserCode);
							List<Map<String, Object>> deliveryDetailList =yunjiDeliveryDetailDao.getYunjiDeliveryDetailBySettleId(detailMerchantMap);
							if (deliveryDetailList == null || deliveryDetailList.size() <= 0) {
								logger.info("云集发货爬虫明细-未使用的账单数量为0,继续");
								continue;
							}
							// 出单个账单
							for (Map<String, Object> map : deliveryDetailList) {
								String settleId = (String) map.get("settle_id");
								if (StringUtils.isBlank(settleId)) continue;
								//云集的发货数据生成销售出库清单
								List<InvetAddOrSubtractRootJson> rootJsonList = yunJiDeliveryAndReturnDetailService.saveYunJiDeliveryDetail(json,keys,topics,tags,settleIdList,merchant,accountUserCode,settleId);
								// 批量推送库存
								if (rootJsonList!=null&&rootJsonList.size()>0) {
									yunJiDeliveryAndReturnDetailService.pushMQ(rootJsonList);
								}
							}
	
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("执行云集爬虫发货明细消费异常",e.getMessage());
						}
						
						
					}
					Thread.sleep(3000);
				}catch(Exception e) {
					e.printStackTrace();
					logger.error("执行云集爬虫发货明细消费异常",e.getMessage());
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
			    logger.info("===================执行云集爬虫发货明细结束==================");
			}
			
			
			
			
		}
		logger.info("===================爬虫生成销售出库单和销售退货入库单结束==================");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
	


}

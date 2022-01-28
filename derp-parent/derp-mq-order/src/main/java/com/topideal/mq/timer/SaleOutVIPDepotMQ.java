package com.topideal.mq.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.VipCreateSaleOutDepotService;

import net.sf.json.JSONObject;
/**
 * 根据爬虫账单生成唯品销售出库清单MQ
 */
@Component
public class SaleOutVIPDepotMQ extends ConsumerMQAbstract{
	
	@Autowired
	private VipCreateSaleOutDepotService outDepotService;
	
	//MQ
	@Autowired
	private RMQProducer mqProducer;
	/**
	 * 打印日志
	 */
	public static final Logger logger= LoggerFactory.getLogger(SaleOutVIPDepotMQ.class);
	
	public SaleOutVIPDepotMQ(){
		super.setTags(MQOrderEnum.TIMER_SALE_OUT_VIP_DEPOT.getTags());
		super.setTopics(MQOrderEnum.TIMER_SALE_OUT_VIP_DEPOT.getTopic());
	}
	/**==============================虫账单生成销售出库单4.0======================================*/
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		logger.info("===================爬虫账单生成出/入库单开始=================="+json);
		//锁住代码块避免重复出库
		synchronized(this){
			JSONObject jsonObj = JSONObject.fromObject(json);
			String sourceType = (String) jsonObj.get("sourceType");//out-出库 in-入库
			String billCodes = (String) jsonObj.get("billCodes");//指定账单号
			String ids = (String) jsonObj.get("ids");//指定账单id
			/**有指定账单号或账单id则出指定账单号账单id的账单*/
			List<String> billCodeList = StrUtils.parseIdsToStr(billCodes);
			List<Long> idList = StrUtils.parseIds(ids);
			
	        /**生成入库单*/	
			if(StringUtils.isEmpty(sourceType)||sourceType.equals("in")) {
				try{
					/**0、清空临时表*/
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("sourceType", "2");//数据来源 1-云集 2-唯品
					outDepotService.clearTable(paramMap);
					vipExecuteIn(billCodeList, idList);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("唯品的账单数据生成入库单消费异常",e.getMessage());
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
				logger.info("===================唯品生成入库单结束==================");
			}
			
			/**生成出库单*/	
			if(StringUtils.isEmpty(sourceType)||sourceType.equals("out")) {
				try{
					/**0、清空临时表*/
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("sourceType", "2");//数据来源 1-云集 2-唯品
					outDepotService.clearTable(paramMap);
					vipExecuteOut(billCodeList, idList);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("唯品的账单数据生成销售出库清单/库存调整单消费异常",e.getMessage());
					return ConsumeConcurrentlyStatus.RECONSUME_LATER;
				}
			}
			logger.info("===================唯品生成出库单结束==================");
			try {
				Thread.sleep(5000);//睡眠2秒等待数据库事务完全提交
			} catch (Exception e) {}

		}
		logger.info("===================唯品爬虫账单生成出库单结束==================");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	/**生成入库单*/
	public void vipExecuteIn(List<String> billCodeList,List<Long> idList) throws Exception{
		/**1.查询未使用过的账单按商家+账单号分组去重*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("billCodeList", billCodeList);
	    paramMap.put("idList", idList);
	    paramMap.put("processingType", "rk");//入库
		List<Map<String, Object>> merchantIdBillCodeList = outDepotService.searchMerchantIdBillCodeList(paramMap);
		if(merchantIdBillCodeList==null||merchantIdBillCodeList.size()<=0) {
			logger.info("未使用过的商家账单号数量为0,结束运行");
			return ;
		}
		/**2.循环商家+账单号*/
		for(int i=0;i<merchantIdBillCodeList.size();i++){
			Map<String, Object> billCodeMap = merchantIdBillCodeList.get(i);
			Long merchantId = (Long)billCodeMap.get("merchant_id");
			String billCode = (String)billCodeMap.get("bill_code");
			logger.info("第"+(i+1)+"个,唯品入库--账单号："+billCode+"商家id:"+merchantId+"开始");
			List<InvetAddOrSubtractRootJson> rootJsonList = outDepotService.saveVIPSaleInDepot(merchantId, billCode, idList);
		    /**推送报文扣减库存*/
			outDepotService.pushMQ(rootJsonList);
		}
	}
	/**生成出库单*/
	public void vipExecuteOut(List<String> billCodeList,List<Long> idList) throws Exception{
		/**1.查询未使用过的账单按商家+账单号分组去重*/
		Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("billCodeList", billCodeList);
	    paramMap.put("idList", idList);
	    paramMap.put("processingType", "ck");//出库
		List<Map<String, Object>> merchantIdBillCodeList = outDepotService.searchMerchantIdBillCodeList(paramMap);
		/**2.循环商家+账单号*/
		if(merchantIdBillCodeList==null||merchantIdBillCodeList.size()<=0) {
			logger.info("未使用过的商家账单号数量为0,结束运行");
			return ;
		}
		
		for(int i=0;i<merchantIdBillCodeList.size();i++){
			Map<String, Object> billCodeMap = merchantIdBillCodeList.get(i);
			Long merchantId = (Long)billCodeMap.get("merchant_id");
			String billCode = (String)billCodeMap.get("bill_code");
			logger.info("第"+(i+1)+"个,唯品出库--账单号："+billCode+"商家id:"+merchantId+"开始");
			List<InvetAddOrSubtractRootJson> rootJsonList = outDepotService.saveVIPSaleOutDepot(merchantId, billCode, idList);
		    /**推送报文扣减库存*/
			outDepotService.pushMQ(rootJsonList);
		}
	}
	
	
	
	/**==============================虫账单生成销售出库单4.0=end==================================*/

}

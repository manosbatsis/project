package com.topideal.mq.report;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.*;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 月结自动校验
 * @author gy
 *
 */
@Component
public class MonthlyAccountAutomaticVerificationMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER = LoggerFactory.getLogger(MonthlyAccountAutomaticVerificationMQ.class);

    @Autowired
    private BuInventorySummaryReportService buInventorySummaryReportService ;
    @Autowired
    private MonthlyAccountAutomaticVeriService monthlyAccountAutomaticVerificationService ;
    @Autowired
	private RMQProducer rocketMQProducer;//mq

    public MonthlyAccountAutomaticVerificationMQ() {
        super.setTags(MQReportEnum.MONTHLY_ACCOUNT_AUTO_VERI.getTags());
        super.setTopics(MQReportEnum.MONTHLY_ACCOUNT_AUTO_VERI.getTopic());
    }

    @SuppressWarnings("unchecked")
	@Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("=============生成月结自核表开始===========");
        
        Integer merchantId = null ;
		String month = null ;
		Integer depotId = null ;
		String syn = null ;
        
        try {
            JSONObject jsonData = JSONObject.fromObject(json);
            Map<String, Object> jsonMap = jsonData;
            
            if(jsonMap.get("syn") != null) {
            	syn = (String) jsonMap.get("syn");//是否页面触发
            }
            
            if(jsonMap.get("merchantId") != null) {
            	merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
            }
            
            if(jsonMap.get("month") != null) {
            	month = (String) jsonMap.get("month"); // 报表月份
            }

			if (jsonMap.get("depotId") != null) {
				depotId = (Integer) jsonMap.get("depotId");
			}
			
            //若是页面触发，同步数据、刷新报表：前后顺序步骤为：月结报表、事业部库存、事业部业务进销存、总业务进销存
            if("true".equals(syn)){
            	
            	LOGGER.info("=============页面触发调用库存MQ以及同步数据开始===========");
            	
            	//发送消息刷新月结
            	rocketMQProducer.send(json, MQInventoryEnum.INVENTORY_REFRESH_MONTHLY_BILL.getTopic(), MQInventoryEnum.INVENTORY_REFRESH_MONTHLY_BILL.getTags()) ;
            	//发送消息刷新事业部库存
            	rocketMQProducer.send(json, MQInventoryEnum.INVENTORY_BU_INVENTORY.getTopic(), MQInventoryEnum.INVENTORY_BU_INVENTORY.getTags()) ;
            	
            	//休眠40秒等待月结、事业部库存统计
            	Thread.sleep(30000);
            	
                //同步数据
            	//delOrSyncService.getDelOrSync(json, keys, topics, MQReportEnum.SYS_DATA.getTags());
                //刷新报表
                buInventorySummaryReportService.saveBuSummaryReport(json, topics, tags);
                
                //正式环境，休眠5分钟待经销存统计
            	Thread.sleep(300000);
                //测试环境，休眠90秒待经销存统计
            	//Thread.sleep(90000);
                
                LOGGER.info("=============页面触发调用库存MQ以及同步数据结束===========");
            }
            
            monthlyAccountAutomaticVerificationService.saveAutoVeriReport(json, keys, topics, tags);
            
            LOGGER.info("=============生成月结自核表结束===========");
            
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("生成月结自核表开始异常:{}", e.getMessage(), e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        } finally {
            //删除刷新任务
			Map<String, Object> delTaskMap = new HashMap<String, Object>();
			delTaskMap.put("taskType", DERP_REPORT.FILETASK_TASKTYPE_SXZHD);// 任务类型 SXZHD-刷新自核对
			delTaskMap.put("depotId",depotId) ;
			delTaskMap.put("merchantId",merchantId) ;
			delTaskMap.put("ownMonth", month) ;
            monthlyAccountAutomaticVerificationService.delByTaskType(delTaskMap);
        }
        
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}

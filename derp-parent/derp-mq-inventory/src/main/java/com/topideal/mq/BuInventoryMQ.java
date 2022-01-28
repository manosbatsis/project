package com.topideal.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.BuInventoryService;

import net.sf.json.JSONObject;

/**
 *  事业部库存接口
 *  杨创  2020/04/13
 */
@Component
public class BuInventoryMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(BuInventoryMQ.class);

    @Autowired
    private BuInventoryService buInventoryService;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ

    public BuInventoryMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_BU_INVENTORY.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_BU_INVENTORY.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============生成事业部库存接口===========");
        System.out.println("------------------json："+json);
        synchronized(this){
        	try{
        		JSONObject jsonObj = JSONObject.fromObject(json);
        		String months = (String) jsonObj.get("month");//月份
        		//计算要刷新的月份
                if (StringUtils.isEmpty(months)) {
                    //若没有指定月份则取当前时间前一天日期近二个月月份,定时器刷新未结转的，本月、上月
                    months = TimeUtils.getLastTwoMonthsByNow();
                }
                String[] montharr = months.split(",");
                for (String month : montharr) {
                	jsonObj.put("month", month);
                	try {
                		buInventoryService.synsBuInventory(jsonObj.toString(),keys,topics,tags);
                		Thread.sleep(3000);// 睡眠三秒
					} catch (Exception e) {
						e.printStackTrace();
					}               	
                }

                String selectTime = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd");
                JSONObject jSONObject=new JSONObject();
                jSONObject.put("selectTime", selectTime);
                jSONObject.put("database", "derp-inventory");
                jSONObject.put("table", "i_bu_inventory");
                //rocketMQProducer.send(jSONObject.toString(), MQReportEnum.SYS_DATA.getTopic(), MQReportEnum.SYS_DATA.getTags());

            }catch(Exception e){
            	e.printStackTrace();
               return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}

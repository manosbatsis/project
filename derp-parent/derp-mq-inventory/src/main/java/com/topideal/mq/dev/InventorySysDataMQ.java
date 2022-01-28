package com.topideal.mq.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.dev.InventorySysDataService;

/**
 *  事业部库存接口
 *  杨创  2020/04/13
 */
@Component
public class InventorySysDataMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(InventorySysDataMQ.class);

    @Autowired
    private InventorySysDataService InventorySysDataService;


    public InventorySysDataMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_SYS_DATA.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_SYS_DATA.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============生成事业部库存接口===========");
        System.out.println("------------------json："+json);
        synchronized(this){
        	try{
        		InventorySysDataService.synsDataToMongo(json,keys,topics,tags);

            }catch(Exception e){
            	e.printStackTrace();
               return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}

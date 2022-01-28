package com.topideal.mq.api.purchase;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.purchase.YwmsPurchaseWarehouseService;
import com.topideal.service.common.CommonBusinessService;

/**
 * 众邦云仓采购入库回推
 * @author gy
 */
@Component
public class YWmsPurchaseWarehouseMQ extends ConsumerMQAbstract {

    @Autowired 
    private YwmsPurchaseWarehouseService service;
    @Autowired
	private CommonBusinessService commonBusinessService ;

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(YWmsPurchaseWarehouseMQ.class);

    public YWmsPurchaseWarehouseMQ(){
        super.setTopics(MQOrderEnum.YWMS_PURCHASE_WAREHOUSE.getTopic());
        super.setTags(MQOrderEnum.YWMS_PURCHASE_WAREHOUSE.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============众邦云仓采购入库接口回推===========");
        try{
        	//生成入库单
        	InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = service.saveStatus(json, keys, topics, tags);
        	//调用公共方法生成勾稽(已经不生成勾稽了 删除了差异分析表)
        	//commonBusinessService.saveAutoPurchaseAnalysis(invetAddOrSubtractRootJson.getOrderNo());
        	//推库存
        	service.pushInventory(invetAddOrSubtractRootJson);
        	
        }catch (DerpException e) {
			
			try {
				commonBusinessService.modifyCorrelationstatus((PurchaseWarehouseModel)e.getObj());
			} catch (SQLException e1) {
				LOGGER.error("异常,{}", e1.getMessage(), e1);
				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			}
			
			LOGGER.error("异常,{}", e.getMessage(), e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
			
		}catch(Exception e){
            LOGGER.error("异常:{}",e.getMessage(), e);
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}

package com.topideal.mq;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQErpEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.CopyMerchandiseService;

import net.sf.json.JSONObject;

/**
 * 生成/修改卓普信和嘉宝商品MQ
 **/
@Component
public class CopyMerchandiseMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CopyMerchandiseMQ.class);

    @Autowired
    private CopyMerchandiseService copyMerchandiseService;
	@Autowired
	private MerchantInfoDao merchantInfoDao;	
    public CopyMerchandiseMQ() {
        super.setTags(MQErpEnum.COPY_MERCHANDISE.getTags());
        super.setTopics(MQErpEnum.COPY_MERCHANDISE.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("----生成/修改卓普信和嘉宝商品开始----");
      //锁住代码块
      	synchronized (this) {
      		try {
      			
      			String topidealCodes="0000138,1000000204,0000134,1000000626,1000005377";
      			JSONObject jSONObject = JSONObject.fromObject(json);
      			Object tagObj = jSONObject.get("tag");//"2" 页面/其他触发单个货号 "1" 定时器 
      			if (tagObj!=null&&"2".equals(tagObj.toString())) {//页面/其他来源
      				Thread.sleep(1000);//睡眠1秒
      				//merchantId/goodsId为空不进行复制
      				if (jSONObject.get("merchantId")==null||jSONObject.get("goodsId")==null) {
      					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}
      				String merchantIdStr=jSONObject.getString("merchantId");
      				String goodsIdStr=jSONObject.getString("goodsId");
      				     				
      				MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(Long.valueOf(merchantIdStr));
      				if (merchantInfoModel==null){
      					 return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
      				}      				
      				//非上面这些商家不进行复制
      				if (!topidealCodes.contains(merchantInfoModel.getTopidealCode())) {
      					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
					}
      				JSONObject jsonData = JSONObject.fromObject(json);
	            	jsonData.put("derpTime", TimeUtils.getNow().getTime());
	            	jsonData.put("merchantId", merchantInfoModel.getId());
	            	jsonData.put("goodsIdStr", Long.valueOf(goodsIdStr));
	            	jsonData.put("tag", "2");//来源页面
	            	jsonData.put("describe", "来源非定时器");//来源页面
	            	copyMerchandiseService.saveCopyMerchandise(jsonData.toString(),merchantInfoModel);	            	
	            	return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}else {//定时器
					
					List<String> topidealCodeList = Arrays.asList(topidealCodes.split(","));	      			
	      			for (String topidealCode : topidealCodeList) {
	      				MerchantInfoModel merchantInfoModel=new MerchantInfoModel();
	      				merchantInfoModel.setTopidealCode(topidealCode);
	      				merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
	      				if (merchantInfoModel==null)continue;
	      				JSONObject jsonData = JSONObject.fromObject(json);
		            	jsonData.put("derpTime", TimeUtils.getNow().getTime());
		            	jsonData.put("merchantId", merchantInfoModel.getId());
		            	jsonData.put("tag", "1");//来源页面
		            	jsonData.put("describe", "来源定时器");//来源页面
		            	copyMerchandiseService.saveCopyMerchandise(jsonData.toString(),merchantInfoModel);
		            	Thread.sleep(8000);//睡眠8秒
					}
				}

     			     			            	            	            	
            } catch(Exception e) {
            	e.printStackTrace();
                LOGGER.error("生成/修改卓普信和嘉宝商品异常:{}", e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
      	}        
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}

package com.topideal.mq;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.MonthlyAccountDao;
import com.topideal.entity.vo.MonthlyAccountModel;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.RefreshInventoryMonthlyBillService;

import net.sf.json.JSONObject;

/**
 *  刷新月结账单   MQ
 * Created by baols on 2018/6/11.
 */
@Component
public class RefreshInventoryMonthlyBillMQ extends ConsumerMQAbstract {

    /*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshInventoryMonthlyBillMQ.class);

    @Autowired
    private RefreshInventoryMonthlyBillService refreshInventoryMonthlyBillService;
    @Autowired
    private  MonthlyAccountDao monthlyAccountDao;
	@Autowired
	private RMQProducer rocketMQProducer;//MQ

    

    public RefreshInventoryMonthlyBillMQ(){
        super.setTopics(MQInventoryEnum.INVENTORY_REFRESH_MONTHLY_BILL.getTopic());
        super.setTags(MQInventoryEnum.INVENTORY_REFRESH_MONTHLY_BILL.getTags());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
        LOGGER.info("===============刷新月结账单MQ被调用===========");
        try{
			JSONObject jsonData = JSONObject.fromObject(json);
			
			String tag = (String) jsonData.get("tag");// "1"来源页面 
			// 来源页面刷新
			if ("1".equals(tag)) {
				String monthlyAccountId = (String) jsonData.get("monthlyAccountId");
				MonthlyAccountModel monthlyAccountModel = monthlyAccountDao.searchById(Long.valueOf(monthlyAccountId));
				refreshInventoryMonthlyBillService.synsRefreshMonthlyBill(json,keys,topics,tags,monthlyAccountModel);
				//触发刷新事业部库存
				JSONObject jsonMQ=new JSONObject();
	    		jsonMQ.put("merchantId",monthlyAccountModel.getMerchantId());
	    		jsonMQ.put("depotId", monthlyAccountModel.getDepotId());   		
	    		jsonMQ.put("month", monthlyAccountModel.getSettlementMonth());
	    		
	    		rocketMQProducer.send(jsonMQ.toString(), MQInventoryEnum.INVENTORY_BU_INVENTORY.getTopic(),MQInventoryEnum.INVENTORY_BU_INVENTORY.getTags());
			}else {// 非页面刷新
				// 商家id不为空来自月结字核对
				if (jsonData.get("merchantId")!=null&&!StringUtils.isEmpty(jsonData.getString("merchantId"))) {
					String merchantIdStr = jsonData.getString("merchantId");
					if (jsonData.get("month")==null||StringUtils.isEmpty(jsonData.getString("month"))) {
						throw new RuntimeException("月份不能为空");
					}
					String month = jsonData.getString("month");

					MonthlyAccountModel monthlyAccount=new  MonthlyAccountModel();
					monthlyAccount.setMerchantId(Long.valueOf(merchantIdStr));
					monthlyAccount.setSettlementMonth(month);
					if (jsonData.get("depotId")!=null) {
						monthlyAccount.setDepotId(Long.valueOf(jsonData.getString("depotId")));
					}
					monthlyAccount.setState("1");
					// 查询该商家仓库月份未结转数据
					List<MonthlyAccountModel> monthlyAccountList = monthlyAccountDao.list(monthlyAccount);
					
					for (MonthlyAccountModel monthlyAccountModel : monthlyAccountList) {
						refreshInventoryMonthlyBillService.synsRefreshMonthlyBill(json,keys,topics,tags,monthlyAccountModel);
					}
				}else {// 商家id为空来自 定时器
					// 获取当前月份和上月数据
					String months = TimeUtils.getLastTwoMonthsByNow();
					String[] montharr = months.split(",");
					
					for (String month : montharr) {
						// 查询没有月结的的月结账单进行刷新
						MonthlyAccountModel monthlyAccount=new  MonthlyAccountModel();
						monthlyAccount.setState("1");
						monthlyAccount.setSettlementMonth(month);
						List<MonthlyAccountModel> monthlyAccountList = monthlyAccountDao.list(monthlyAccount);
						for (MonthlyAccountModel monthlyAccountModel : monthlyAccountList) {
							JSONObject jSONObject= new JSONObject();
							jSONObject.put("merchantName", monthlyAccountModel.getMerchantName());
							jSONObject.put("depotName", monthlyAccountModel.getDepotName());
							jSONObject.put("settlementMonth", monthlyAccountModel.getSettlementMonth());
							jSONObject.put("source", "定时器刷新没有月结的数据");
							LOGGER.info("刷新月结jSONObject:"+jSONObject.toString());
							refreshInventoryMonthlyBillService.synsRefreshMonthlyBill(jSONObject.toString(),keys,topics,tags,monthlyAccountModel);
						}
						//休眠10秒
						Thread.sleep(10000);						
					}
					LOGGER.info("刷新月结jSONObject:结束");
				}
			}
			

        }catch(Exception e){
           return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;


    }
}

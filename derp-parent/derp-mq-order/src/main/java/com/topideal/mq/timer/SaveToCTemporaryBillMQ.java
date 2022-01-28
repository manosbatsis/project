package com.topideal.mq.timer;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.SaveToCTemporaryBillService;
import com.topideal.service.timer.ToCTempBillVerifyService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 生成To C暂估应收数据
 */
@Component
public class SaveToCTemporaryBillMQ extends ConsumerMQAbstract{

	@Autowired
	private SaveToCTemporaryBillService saveToCTemporaryBillService;
	@Autowired
	private ToCTempBillVerifyService toCTempBillVerifyService;

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(SaveToCTemporaryBillMQ.class);

	public SaveToCTemporaryBillMQ(){
		super.setTags(MQOrderEnum.TIMER_ToC_Temp_RECEVICE_BILL.getTags());
		super.setTopics(MQOrderEnum.TIMER_ToC_Temp_RECEVICE_BILL.getTopic());
	}
	
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================生成To C暂估应收数据MQ 开始=================="+json);
		synchronized (this) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				int monthDay = 0;
				String month = null;
				JSONObject jsonData = JSONObject.fromObject(json);

				Integer merchantId = (Integer) jsonData.get("merchantId");// 商家Id

				String sourceType = (String) jsonData.get("sourceType");

				String orderType = (String) jsonData.get("orderType");

				List<MerchantInfoMongo> merchantInfoMongos = saveToCTemporaryBillService.getMerchantList(merchantId);

				if (jsonData.containsKey("month")) {
					month = (String) jsonData.get("month");
				}

				List<String> months = new ArrayList<>();

				//（1）每月1号生成当月已发货的电商订单TO C暂估应收数据；
				//（2）每天刷新当月的To C暂估应收，每月前3天均自动刷新上个月的To C暂估应收数据；
				if (!StringUtils.isNotBlank(month)) {
					Calendar calendar = Calendar.getInstance();
					Date today = new Date();
					calendar.setTime(today);
					monthDay = calendar.get(Calendar.DAY_OF_MONTH);
					if (monthDay < 4) {
						month = TimeUtils.getLastMonth(today);
						months.add(month);
					}
					month = sdf.format(today);
					months.add(month);
				} else {
					months.add(month);
				}

				//以公司维度生成toc暂估
				for (MerchantInfoMongo merchantInfo : merchantInfoMongos) {
					jsonData.put("merchantId", merchantInfo.getMerchantId());

					for (String mon : months) {
						jsonData.put("month", mon);
						if (StringUtils.isBlank(sourceType) || "0".equals(sourceType)) {
							if (StringUtils.isBlank(orderType) || "0".equals(orderType)) {
								//生成toc暂估收入
								LOGGER.info("===================生成toc暂估收入 开始=================="+json);
								saveToCTemporaryBillService.saveToCTemporaryBill(jsonData.toString(), keys, topics, tags);
								LOGGER.info("===================生成toc暂估收入 结束=================="+json);
							}

							if (StringUtils.isBlank(orderType) || "1".equals(orderType)) {
								//生成toc暂估费用
								LOGGER.info("===================生成toc暂估费用 开始=================="+json);
								saveToCTemporaryBillService.saveToCCostTemporaryBill(jsonData.toString(), keys, topics, tags);
								LOGGER.info("===================生成toc暂估费用 结束=================="+json);
							}
						}

						if (StringUtils.isBlank(sourceType) || "1".equals(sourceType)) {
							if (StringUtils.isBlank(orderType) || "0".equals(orderType)) {
								//核销toc结算单应收明细
								LOGGER.info("===================核销toc结算单应收收入 开始=================="+json);
								toCTempBillVerifyService.updateVerifyToCTempBill(jsonData.toString(), keys, topics, tags);
								LOGGER.info("===================核销toc结算单应收收入 结束=================="+json);
							}

							if (StringUtils.isBlank(orderType) || "1".equals(orderType)) {
								//核销toc结算单应收费用
								LOGGER.info("===================核销toc结算单应收费用 开始=================="+json);
								toCTempBillVerifyService.updateVerifyToCTempCostBill(jsonData.toString(), keys, topics, tags);
								LOGGER.info("===================核销toc结算单应收费用 结束=================="+json);
							}
						}
					}

				}

				LOGGER.info("===================生成To C暂估应收数据MQ 结束==================");
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.error("生成To C暂估应收数据异常",e);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

	}

}

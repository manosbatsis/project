package com.topideal.mq.report;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 业财自核MQ
 * @Author: Chen Yiluan
 * @Date: 2020/05/18 15:43
 **/
@Component
public class InventoryFinanceAutomaticVerificationMQ extends ConsumerMQAbstract {

    public static final Logger LOGGER = LoggerFactory.getLogger(InventoryFinanceAutomaticVerificationMQ.class);

    @Autowired
    private InventoryFinanceAutomaticVerificationService inventoryFinanceAutomaticVerificationService;
    @Autowired
    private BuInventorySummaryReportService buInventorySummaryReportService;
    @Autowired
    private BuFinanceInventorySummaryService buFinanceInventorySummaryService;

    public InventoryFinanceAutomaticVerificationMQ() {
        super.setTags(MQReportEnum.INVENTORY_FINANCE_AUTO_VERI.getTags());
        super.setTopics(MQReportEnum.INVENTORY_FINANCE_AUTO_VERI.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("=============生成业财自核表开始===========");
        Integer merchantId = null ;
        String month = null ;
        String refresh = null ;
        try {
            JSONObject jsonData = JSONObject.fromObject(json);
            LOGGER.info("========================业财自核表请求参数："+jsonData.toString());
            Map<String, Object> jsonMap = jsonData;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if(jsonMap.get("refresh") != null) {
                refresh = (String) jsonMap.get("refresh");//是否页面触发
            }

            if(jsonMap.get("merchantId") != null) {
                merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
            }

            if(jsonMap.get("month") != null) {
                month = (String) jsonMap.get("month"); // 报表月份
            }

            //若是页面触发，同步数据、刷新报表：事业部业务进销存、业务进销存汇总表、事业部财务进销存、财务进销存汇总表
            if(!StringUtils.isEmpty(refresh)){
                if (refresh.equals("true")) {
                    //同步数据
                    //delOrSyncService.getDelOrSync(json, keys, topics, MQReportEnum.SYS_DATA.getTags());
                    //刷新报表
                    buInventorySummaryReportService.saveBuSummaryReport(json, topics, tags);
                    buFinanceInventorySummaryService.saveSummaryReport(json,keys, topics, tags);
                    Thread.sleep(300000);
                }
                inventoryFinanceAutomaticVerificationService.saveAutoVeriReport(json, keys, topics, tags);
            } else {
                //业财自校验1~7号统计当月和上一月份的业务进销存和财务进销存报表核对结果；
                // 8~31号统计本月的业务进销存和财务进销存报表核对结果；
                if (StringUtils.isEmpty(month)) {
                    Calendar calendar = Calendar.getInstance();
                    Date today = new Date();
                    calendar.setTime(today);
                    int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
                    if (monthDay < 8) {
                        month = TimeUtils.getLastMonth(today);
                        jsonData.put("month", month);
                        inventoryFinanceAutomaticVerificationService.saveAutoVeriReport(jsonData.toString(), keys, topics, tags);
                    }
                    month = sdf.format(today);
                }
                jsonData.put("month", month);
                inventoryFinanceAutomaticVerificationService.saveAutoVeriReport(jsonData.toString(), keys, topics, tags);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("生成生成业财自核表开始异常:{}", e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }  finally {
            //删除刷新任务
            Map<String, Object> delTaskMap = new HashMap<String, Object>();
            delTaskMap.put("taskType", DERP_REPORT.FILETASK_TASKTYPE_SXZHD);// 任务类型 SXZHD-刷新自核对
            delTaskMap.put("merchantId",merchantId) ;
            delTaskMap.put("ownMonth", month) ;
            inventoryFinanceAutomaticVerificationService.delByTaskType(delTaskMap);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}

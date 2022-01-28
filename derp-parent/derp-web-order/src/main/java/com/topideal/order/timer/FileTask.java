package com.topideal.order.timer;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.order.service.bill.*;
import com.topideal.order.service.sale.OrderService;
import com.topideal.order.service.sale.PlatformTemporaryCostOrderService;
import com.topideal.order.service.timer.FileTaskService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * order文件生成定时任务
 */
@Component
public class FileTask {

    private static final Logger logger = Logger.getLogger(FileTask.class);

    @Autowired
    private FileTaskService fileTaskService;

    @Autowired
    private ToCTempReceiveBillService toCTempReceiveBillService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PlatformTemporaryCostOrderService platformTemporaryCostOrderService;
    @Autowired
    private ToCReceiveBillService toCReceiveBillService;
    @Autowired
    private TocTempReceiveBillMonthlyService tocTempReceiveBillMonthlyService;
    @Autowired
    private ReceiveAgingReportService receiveAgingReportService;
    @Autowired
    private PaymentBillService paymentBillService;
    @Autowired
    private PlatformStatementOrderService platformStatementOrderService;
    /**
     * 5秒钟执行一次
     */
    @Scheduled(cron = "5 * * * * ? ")
    public void runTask() {
        logger.info("----【order定时任务开始...】----");
        try {
            String basePath = ApolloUtilDB.orderBasepath;//excel文件存放目录

            FileTaskMongo paramModel = new FileTaskMongo();
            //查询待执行的任务列表
            paramModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
            paramModel.setModule(DERP_REPORT.FILETASK_MODULE_2);

            List<FileTaskMongo> taksList = fileTaskService.getListByParamTask(paramModel);
            if (taksList == null || taksList.size() <= 0) {
                logger.info("待执行任务数量为0,结束执行");
                return;
            }
            //循环执行任务
            for (int i = 0; i < taksList.size(); i++) {
                String path = "";//保存目录
                FileTaskMongo task = taksList.get(i);
                logger.info("第" + (i + 1) + "个任务开始:" + task.getTaskName() + ",任务编码:" + task.getCode() + ",参数:" + task.getParam());
                task.setState(DERP_REPORT.FILETASK_STATE_2);//进行中

                //toc暂估收入明细导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEM)) {
                    fileTaskService.modify(task);
                    path = toCTempReceiveBillService.createItemExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //toc暂估费用明细导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOST)) {
                    fileTaskService.modify(task);
                    path = toCTempReceiveBillService.createCostExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }
                //电商订单导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_DSDD)) {
                    fileTaskService.modify(task);
                    path = orderService.createExportExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //toc暂估收入累计暂估导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEMTOTAL)) {
                    fileTaskService.modify(task);
                    path = toCTempReceiveBillService.createTempItemExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //toc暂估费用累计暂估导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOSTTOTAL)) {
                    fileTaskService.modify(task);
                    path = toCTempReceiveBillService.createTempCostExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //toc暂估收入汇总导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEMSUMMARY)) {
                    fileTaskService.modify(task);
                    path = toCTempReceiveBillService.createTempReceiveSummaryExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //toc暂估费用汇总导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOSTSUMMARY)) {
                    fileTaskService.modify(task);
                    path = toCTempReceiveBillService.createTempCostSummaryExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //toc平台暂估费用单导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCPLATFORMTEMPCOSTTOTAL)) {
                    fileTaskService.modify(task);
                    path = platformTemporaryCostOrderService.createTempCostExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //ToC平台结算单导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCSETTLEMENT)) {
                    fileTaskService.modify(task);
                    path = toCReceiveBillService.createReceiveBillExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //ToC平台结算单汇总导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOCSETTLEMENT_SUMMARY)) {
                    fileTaskService.modify(task);
                    path = toCReceiveBillService.createReceiveBillSummaryExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //ToC暂估应收月结快照导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOC_MONTHLY_RECEIVE)) {
                    fileTaskService.modify(task);
                    path = tocTempReceiveBillMonthlyService.createReceiveBillExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //ToC暂估费用月结快照导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_TOC_MONTHLY_COST)) {
                    fileTaskService.modify(task);
                    path = tocTempReceiveBillMonthlyService.createCostBillExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //应收账龄月结导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_AGINGREPORTMONTHLY)) {
                    fileTaskService.modify(task);
                    path = receiveAgingReportService.createMonthlyReportExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                //应付账单导出
//                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_PAYMENT_BILL)) {
//                    fileTaskService.modify(task);
//                    path = paymentBillService.createPaymentBillExcel(task, basePath);
//                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
//                }

                //平台结算单导出
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_PLATFORMSTATEMENT)) {
                    fileTaskService.modify(task);
                    path = platformStatementOrderService.createPlatformSettlementOrderExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }

                task.setPath(path);
                fileTaskService.modify(task);//已完成
                logger.info("第" + (i + 1) + "个任务结束");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        logger.info("----【order定时任务结束...】----");
    }
}

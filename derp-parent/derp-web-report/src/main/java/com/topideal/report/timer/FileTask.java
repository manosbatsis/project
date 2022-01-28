package com.topideal.report.timer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.mongo.entity.FileTaskMongo;
import com.topideal.report.service.reporting.BuFinanceInventorySummaryService;
import com.topideal.report.service.reporting.BuInventorySummaryService;
import com.topideal.report.service.reporting.FileTaskService;
import com.topideal.report.service.reporting.InWareHouseDaysService;
import com.topideal.report.service.reporting.VipPoBillVerificationService;

/**
 * 报表文件生成定时任务
 * */
@Component
public class FileTask {
	
	private static final Logger logger = Logger.getLogger(FileTask.class);
	
	 @Autowired
     private FileTaskService fileTaskService;
	 @Autowired
	 private BuFinanceInventorySummaryService buFinanceInventorySummaryService;
	 @Autowired
	 private VipPoBillVerificationService vipPoBillVerificationService ;
	 @Autowired
	 private BuInventorySummaryService buInventorySummaryService;
	 @Autowired
	 private InWareHouseDaysService inWareHouseDaysService ;
	 
	 /**
     * 5秒钟执行一次
     */
	@Scheduled(cron = "5 * * * * ? ")
    public void runTask(){
		logger.info("----【报表定时任务开始...】----");
    	try{
            String basePath = ApolloUtilDB.reportBasepath;//excel文件存放目录

            //查询执行中的任务 存在则结束本次运行
    		/*FileTaskModel paramModel = new FileTaskModel();
    		paramModel.setState(DERP_REPORT.FILETASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
    		List<FileTaskModel> taksList = fileTaskService.getListByParamTaks(paramModel);
    		if(taksList!=null&&taksList.size()>0){
    			logger.info("任务列表存在执行中的任务结束执行");
    			return;
    		}*/
			FileTaskMongo paramModel = new FileTaskMongo();
			//查询待执行的任务列表
			paramModel.setState(DERP_REPORT.FILETASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
			paramModel.setModule(DERP_REPORT.FILETASK_MODULE_1);
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
                //1-进销存汇总报表
                if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_YWJXC)) {
                    fileTaskService.modify(task);
                    //path = inventorySummaryService.createExcel(task, basePath);
                    task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
                }
                //2-财务进销存报表
                else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_CWJXC)) {
                    fileTaskService.modify(task);
    				//path = financeInventorySummaryService.createExcel(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			}
    			//3-唯品核销报表
    			else if(task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_VIPHX)){
    				fileTaskService.modify(task);
    				path = vipPoBillVerificationService.createExcel(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			// 事业部财务经销存 
    			}else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_SYBCWJXC)) {
    				fileTaskService.modify(task);
    				path = buFinanceInventorySummaryService.createExcel(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			//SD事业部财务经销存 
				}else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_SDSYBCWJXC)) {
    				fileTaskService.modify(task);
    				path = buFinanceInventorySummaryService.createSdExcel(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			//事业部财务利息经销存
				}else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_SYBCWLX)) {
    				fileTaskService.modify(task);
    				path = buFinanceInventorySummaryService.createInterestTask(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			//事业部财务 美赞成本差异导出
				}else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_SYBCW_MZCYCBDC)) {
    				fileTaskService.modify(task);
    				path = buFinanceInventorySummaryService.createCostDifferenceTask(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			//事业部财务总账
				}else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZZ)) {
    				fileTaskService.modify(task);
    				path = buFinanceInventorySummaryService.createAllAccountExcel(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			//事业部财务经销存暂估应收导出
				}else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZGYS)) {
    				fileTaskService.modify(task);
    				path = buFinanceInventorySummaryService.createTempEstimatePdf(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
    			//事业部业务经销存	
				}else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_SYBYWJXC)) {
    				fileTaskService.modify(task);
    				path = buInventorySummaryService.createExcel(task, basePath);
    				task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
				}
    			//在库天数
	    		else if (task.getTaskType().equals(DERP_REPORT.FILETASK_TASKTYPE_ZKTS)) {
					fileTaskService.modify(task);
					path = inWareHouseDaysService.createExcel(task, basePath);
					task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
				}
    			task.setPath(path);
    			fileTaskService.modify(task);//已完成
    			logger.info("第"+(i+1)+"个任务结束");
    		}
    		
		}catch(Exception e){
			e.printStackTrace();
		}
    	logger.info("----【报表定时任务结束...】----");
    }
}

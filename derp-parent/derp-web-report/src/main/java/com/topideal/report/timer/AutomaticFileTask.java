package com.topideal.report.timer;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.tools.ApolloUtilDB;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.report.service.automatic.AutomaticCheckTaskService;
import com.topideal.report.service.automatic.DepotAutomaticFileTaskService;
import com.topideal.report.service.automatic.POPAmountAutomaticCheckTaskService;
import com.topideal.report.service.automatic.POPAutomaticCheckTaskService;

/**
 * 自动校验文件生成定时任务
 * */
@Component
public class AutomaticFileTask {
	
	private static final Logger logger = Logger.getLogger(AutomaticFileTask.class);
	
	 @Autowired
     private AutomaticCheckTaskService automaticCheckTaskService;
	 @Autowired
	 private DepotAutomaticFileTaskService depotAutomaticFileTaskService ;
	 @Autowired
	 private POPAutomaticCheckTaskService popAutomaticCheckTaskService ;
	 @Autowired
	 private POPAmountAutomaticCheckTaskService popAmountAutomaticCheckTaskService ;
	 
	 /**
     * 5秒钟执行一次
     */
	@Scheduled(cron = "5 * * * * ? ")
    public void runTask(){
		logger.info("----【自动校验任务开始...】----");
    	try{
    		String basePath = ApolloUtilDB.reportBasepath;//excel文件存放目录
    		
    		
    		
			//查询执行中的任务 存在则结束本次运行
    		AutomaticCheckTaskModel paramModel = new AutomaticCheckTaskModel();
    		paramModel.setState(DERP_REPORT.AUTOMATICCHECKTASK_STATE_2);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
    		List<AutomaticCheckTaskModel> taksList = automaticCheckTaskService.getListByModel(paramModel);
    		if(taksList!=null&&taksList.size()>0){
    			logger.info("自动校验列表存在执行中的任务，结束执行");
    			return;
    		}
    		//查询待执行的任务列表
    		paramModel.setState(DERP_REPORT.AUTOMATICCHECKTASK_STATE_1);//任务状态 1-待执行 2-执行中 3-已完成 4-失败
    		taksList = automaticCheckTaskService.getListByModel(paramModel);
    		if(taksList==null||taksList.size()<=0){
    			logger.info("自动校验列表任务数量为0,结束执行");
    			return;
    		}
    		//循环执行任务
    		for(int i=0;i<taksList.size();i++){
					String path = "";//保存目录
					AutomaticCheckTaskModel task = taksList.get(i);
					logger.info("第"+(i+1)+"个任务开始:"+",任务Id:"+task.getId());
					
					try {
						task.setState(DERP_REPORT.FILETASK_STATE_2);//进行中
						task.setModifyDate(TimeUtils.getNow());
						automaticCheckTaskService.modify(task);
						
						//1-POP流水核对 2-仓库流水核对
						if(task.getTaskType().equals(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_2)){
							path = depotAutomaticFileTaskService.createVeriExcel(task, basePath);
						}else if(task.getTaskType().equals(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_1)){
							path = popAutomaticCheckTaskService.createExcel(task, basePath);
						}else if(task.getTaskType().equals(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_3)){
							path = popAmountAutomaticCheckTaskService.createExcel(task, basePath);
						}
						
						task.setStorePath(path);
						task.setState(DERP_REPORT.FILETASK_STATE_3);//已完成
						task.setModifyDate(TimeUtils.getNow());
						automaticCheckTaskService.modify(task);
						
						logger.info("第"+(i+1)+"个任务结束");
						
					} catch (Exception e) {
						
						task.setStorePath(null);
						task.setState(DERP_REPORT.FILETASK_STATE_4);//处理失败
						task.setModifyDate(TimeUtils.getNow());
						automaticCheckTaskService.modify(task);
						
						logger.error("第"+(i+1)+"个任务异常，id：" + task.getId() + ", 原因：" + e);
					}
    		}
    		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("自动校验任务异常：" + e);
		}
    	logger.info("----【自动校验任务结束...】----");
    }
}

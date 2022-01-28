package com.topideal.report.service.automatic;

import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;

/**
 * POP核对任务   service
 */
public interface POPAutomaticCheckTaskService {
	

	String createExcel(AutomaticCheckTaskModel model, String basePath) throws Exception ;
			
}

package com.topideal.report.service.automatic;

import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;

public interface POPAmountAutomaticCheckTaskService {

	String createExcel(AutomaticCheckTaskModel model, String basePath) throws Exception ;
}

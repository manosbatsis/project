package com.topideal.report.service.automatic;

import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;

public interface DepotAutomaticFileTaskService {

	String createVeriExcel(AutomaticCheckTaskModel model, String basePath) throws Exception ;
}

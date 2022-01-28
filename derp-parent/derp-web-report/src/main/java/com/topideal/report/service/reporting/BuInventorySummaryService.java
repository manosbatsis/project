package com.topideal.report.service.reporting;

import java.util.List;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.BuInventorySummaryDTO;
import com.topideal.entity.dto.BuInventorySummaryItemDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mongo.entity.FileTaskMongo;

public interface BuInventorySummaryService {

    public BuInventorySummaryDTO listByPage(User user,BuInventorySummaryDTO model) throws Exception;

    public List<BuInventorySummaryItemDTO> listBuItem(BuInventorySummaryItemDTO model) throws Exception;

    public BusinessUnitModel getBusinessUnit(Long buId)throws Exception;
    //生成excel文件
    public String createExcel(FileTaskMongo task, String basePath) throws Exception;
}

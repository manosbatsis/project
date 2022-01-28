package com.topideal.report.service.reporting;


import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.InWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.BuInventorySummaryItemModel;
import com.topideal.entity.vo.reporting.InWarehouseDetailsModel;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mongo.entity.FileTaskMongo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface InWareHouseDaysService {

    InWarehouseDetailsDTO listInWarehouseDays(User user,InWarehouseDetailsDTO dto) throws Exception;

    //String getRangeDate(InWarehouseDetailsModel inWarehouseDetailsModel);

    InWarehouseDetailsModel listInWarehouseAllDetails(InWarehouseDetailsModel model) throws Exception;

    List<InWarehouseDetailsDTO> listInWarehouseOrderByInWarehousedays(InWarehouseDetailsDTO model);

	List<Map<String, Object>> getInwarehouseDetails(InWarehouseDetailsModel inWarehouseDetailsModel,List<Long> buList);

	/**
	 * 在库天数获取按仓库统计库存
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> countInWarehouseOrderInvertoryByDepot(InWarehouseDetailsModel model,List<Long> buList) throws SQLException;

	/**
	 * 本月入库详情
	 * @param tempItem
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> getInOutDepotInfo(BuInventorySummaryItemModel tempItem) throws SQLException;

	/**
	 * 获取所有事业部
	 * @return
	 * @throws SQLException 
	 */
	List<BusinessUnitModel> getAllBuUnit() throws SQLException;

    /**
     * 创建导出
     *
     * @param task
     * @param basePath
     * @return
     * @throws SQLException
     * @throws Exception
     */
    String createExcel(FileTaskMongo task, String basePath) throws Exception;

}

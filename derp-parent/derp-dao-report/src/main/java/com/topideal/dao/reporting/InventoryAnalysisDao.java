package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.ManageDepartmentInventoryCleanDTO;
import com.topideal.entity.vo.reporting.InventoryAnalysisModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface InventoryAnalysisDao extends BaseDao<InventoryAnalysisModel> {


    Integer batchSave(List<InventoryAnalysisModel> list) throws SQLException;

    List<Map<String, Object>> getInventoryAnalysisData(Map<String, Object> queryMap) throws SQLException;

    //根据归属月份删除数据
    void batchDelByDate(String month) throws SQLException;

    /**
     * 库存分析导出（销售洞察）
     * @param queryMap
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getInventoryAnalysisExportList(Map<String, Object> queryMap);

    /**
     * 部门库存统计 (经营报表)
     * @param paramMap
     * @return
     */
    List<ManageDepartmentInventoryCleanDTO> getDepartmentInventoryCleanStatistic(Map<String, Object> paramMap);
}

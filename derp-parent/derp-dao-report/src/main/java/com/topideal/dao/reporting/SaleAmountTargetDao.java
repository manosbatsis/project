package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.ManageDepartmentSaleAchieveDTO;
import com.topideal.entity.dto.SaleAmountTargetDTO;
import com.topideal.entity.vo.reporting.SaleAmountTargetModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface SaleAmountTargetDao extends BaseDao<SaleAmountTargetModel> {

    SaleAmountTargetDTO getAmountListByPage(SaleAmountTargetDTO dto) throws SQLException ;

    Integer getOrderCount(SaleAmountTargetDTO dto);

    List<SaleAmountTargetDTO> getList(SaleAmountTargetDTO dto);
    /**
     * 根据月份事业部获取月度目标和年度目标
     * @param map
     * @return
     */
    List<Map<String, Object>> getTargetExportList(Map<String, Object> map);
    //获取月度目标和年度销售额目标
    List<Map<String, Object>> getMonthAndYearTargetAmount(Map<String, Object> map);

    /**
     * 获取部门销售达成统计数据(目标) --经营报表
     * @param queryMap
     * @return
     */
    List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesTargetStatistic(Map<String, Object> queryMap);
}

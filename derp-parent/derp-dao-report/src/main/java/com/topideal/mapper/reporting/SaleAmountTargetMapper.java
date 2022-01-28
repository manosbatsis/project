package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.ManageDepartmentSaleAchieveDTO;
import com.topideal.entity.dto.SaleAmountTargetDTO;
import com.topideal.entity.vo.reporting.SaleAmountTargetModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface SaleAmountTargetMapper extends BaseMapper<SaleAmountTargetModel> {
    /**
     * 列表展示 分页
     * @return
     */
    PageDataModel<SaleAmountTargetDTO> getAmountListByPage(SaleAmountTargetDTO dto);

    /**
     * 获取月度销售额列表总数
     * @param dto
     * @return
     */
    Integer getOrderCount(SaleAmountTargetDTO dto);

    /**
     * 获取全部信息
     * @param dto
     * @return
     */
    List<SaleAmountTargetDTO> getList(SaleAmountTargetDTO dto);
    /**
     * 根据月份事业部获取月度目标和年度目标
     * @param map
     * @return
     */
    List<Map<String, Object>> getTargetExportList(Map<String, Object> map);
    //获取月度目标和年度销售额目标
    List<Map<String, Object>> getMonthAndYearTargetAmount(Map<String, Object> map);

    List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesTargetStatistic(Map<String, Object> queryMap);
}

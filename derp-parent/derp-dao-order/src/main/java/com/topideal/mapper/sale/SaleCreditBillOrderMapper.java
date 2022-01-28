package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.OccupationCapitalStatisticsDTO;
import com.topideal.entity.vo.sale.SaleCreditBillOrderModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@MyBatisRepository
public interface SaleCreditBillOrderMapper extends BaseMapper<SaleCreditBillOrderModel> {

    /**
     * 批量更新赊销收款单
     * @param saleCreditBillOrderModels
     * @return
     */
    void batchUpdate(@Param("saleCreditBillOrderModels") List<SaleCreditBillOrderModel> saleCreditBillOrderModels);

    /**
     * 获取资金占用统计表分页列表
     * @param dto
     * @return
     */
    PageDataModel<OccupationCapitalStatisticsDTO> listOccupationCapitalDTOByPage(OccupationCapitalStatisticsDTO dto);

    /**
     * 获取资金占用统计表导出数量
     * @param dto
     * @return
     */
    Integer getOccupationCapitalCount(OccupationCapitalStatisticsDTO dto);
    /**
     * 获取资金占用统计表列表
     * @param dto
     * @return
     */
    List<OccupationCapitalStatisticsDTO> listOccupationCapitalDTO(OccupationCapitalStatisticsDTO dto);

}

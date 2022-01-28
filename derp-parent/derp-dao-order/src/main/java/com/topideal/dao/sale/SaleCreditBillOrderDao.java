package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.OccupationCapitalStatisticsDTO;
import com.topideal.entity.vo.sale.SaleCreditBillOrderModel;

import java.util.List;


public interface SaleCreditBillOrderDao extends BaseDao<SaleCreditBillOrderModel>{

    /**
     * 批量更新赊销收款单
     * @param saleCreditBillOrderModels
     * @return
     */
    void batchUpdate(List<SaleCreditBillOrderModel> saleCreditBillOrderModels);

    /**
     * 获取资金占用统计表分页列表
     * @param dto
     * @return
     */
    OccupationCapitalStatisticsDTO listOccupationCapitalDTOByPage(OccupationCapitalStatisticsDTO dto);

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

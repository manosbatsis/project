package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.FixedCostPriceDTO;
import com.topideal.entity.vo.main.FixedCostPriceModel;

import java.util.List;


public interface FixedCostPriceDao extends BaseDao<FixedCostPriceModel> {

    /**
     * 根据DTO获取列表
     * @param dto
     * @return
     */
    List<FixedCostPriceDTO> listByDTO(FixedCostPriceDTO dto);

    /**
     * update
     * @param dto
     * @return
     */
    int updateByDTO(FixedCostPriceDTO dto);

    int countByDTO(FixedCostPriceDTO dto);

    /**
     * 导出List
     * @param dto
     * @return
     */
    List<FixedCostPriceDTO> listForExport(FixedCostPriceDTO dto);

    /**
     * 批量插入
     * @param insertList
     * @return
     */
    int batchSave(List<FixedCostPriceModel> insertList);

    /**
     * 相同“公司+事业部+条形码+价格类型” 存在多个已审核的采购单价时，取审核日期最近的一个；
     * @return
     */
    List<FixedCostPriceModel> getLatestModel(FixedCostPriceDTO dto);

    FixedCostPriceDTO listDTOByPage(FixedCostPriceDTO dto);
}

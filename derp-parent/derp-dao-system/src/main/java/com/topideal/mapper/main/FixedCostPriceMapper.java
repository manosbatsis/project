package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.FixedCostPriceDTO;
import com.topideal.entity.vo.main.FixedCostPriceModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface FixedCostPriceMapper extends BaseMapper<FixedCostPriceModel> {

    List<FixedCostPriceDTO> listByDTO(FixedCostPriceDTO dto);

    int updateByDTO(FixedCostPriceDTO dto);

    int countByDTO(FixedCostPriceDTO dto);

    List<FixedCostPriceDTO> listForExport(FixedCostPriceDTO dto);

    int batchSave(List<FixedCostPriceModel> list);

    /**
     * 相同“公司+事业部+条形码+价格类型” 存在多个已审核的采购单价时，取审核日期最近的一个；
     * @param dto
     * @return
     */
    List<FixedCostPriceModel> getLatestModel(FixedCostPriceDTO dto);

    PageDataModel<FixedCostPriceDTO> listDTOByPage(FixedCostPriceDTO dto);
}

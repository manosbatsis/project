package com.topideal.mapper.sale;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleFinancingOrderItemDTO;
import com.topideal.entity.vo.sale.SaleFinancingOrderItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleFinancingOrderItemMapper extends BaseMapper<SaleFinancingOrderItemModel> {

	/**
     * 查询所有信息
     * @param dto
     * @return
     */
    List<SaleFinancingOrderItemDTO> listByDTO(SaleFinancingOrderItemDTO dto);

}

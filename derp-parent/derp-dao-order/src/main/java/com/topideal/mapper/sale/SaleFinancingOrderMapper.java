package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.SaleFinancingOrderDTO;
import com.topideal.entity.vo.sale.SaleFinancingOrderModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface SaleFinancingOrderMapper extends BaseMapper<SaleFinancingOrderModel> {
	 /**
     * 根据条件查询
     * @param dto
     * @return
     */
    SaleFinancingOrderDTO searchByDTO(SaleFinancingOrderDTO dto);


}

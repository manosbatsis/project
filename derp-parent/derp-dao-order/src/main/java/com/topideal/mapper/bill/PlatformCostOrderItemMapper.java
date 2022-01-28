package com.topideal.mapper.bill;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.bill.PlatformCostOrderDTO;
import com.topideal.entity.dto.bill.PlatformCostOrderItemDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;


@MyBatisRepository
public interface PlatformCostOrderItemMapper extends BaseMapper<PlatformCostOrderItemModel> {


    /**
     * 分页查询
     * @param dto
     * @return
     */
    PageDataModel<PlatformCostOrderItemDTO> listPlatformCostOrderDTOByPage(PlatformCostOrderItemDTO dto)throws SQLException;

}

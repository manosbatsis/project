package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderItemDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;


@MyBatisRepository
public interface PlatformPurchaseOrderItemMapper extends BaseMapper<PlatformPurchaseOrderItemModel> {

    List<PlatformPurchaseOrderItemDTO> listDTO(PlatformPurchaseOrderItemDTO dto);
}

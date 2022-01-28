package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderItemDTO;
import com.topideal.entity.vo.sale.PlatformPurchaseOrderItemModel;

import java.util.List;


public interface PlatformPurchaseOrderItemDao extends BaseDao<PlatformPurchaseOrderItemModel> {

    List<PlatformPurchaseOrderItemDTO> listDTO(PlatformPurchaseOrderItemDTO dto);


}

package com.topideal.dao.bill;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.bill.PlatformCostOrderItemDTO;
import com.topideal.entity.vo.bill.PlatformCostOrderItemModel;

import java.sql.SQLException;


public interface PlatformCostOrderItemDao extends BaseDao<PlatformCostOrderItemModel> {


    /**
     * 分页查询
     * @param dto
     * @return
     */
    PlatformCostOrderItemDTO listPlatformCostOrderDTOByPage(PlatformCostOrderItemDTO dto)throws SQLException;








}

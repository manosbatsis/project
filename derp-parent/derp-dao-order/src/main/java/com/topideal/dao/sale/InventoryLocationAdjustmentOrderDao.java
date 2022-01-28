package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.*;
import com.topideal.entity.vo.sale.InventoryLocationAdjustmentOrderModel;

import java.sql.SQLException;
import java.util.List;


public interface InventoryLocationAdjustmentOrderDao extends BaseDao<InventoryLocationAdjustmentOrderModel> {


    /**
     * 根据条件查询（分页）
     * @return
     */
    InventoryLocationAdjustmentOrderDTO queryDTOListByPage(InventoryLocationAdjustmentOrderDTO dto)throws SQLException;

    int getTotal(InventoryLocationAdjustmentOrderDTO dto) throws SQLException;

    Integer getOrderCount(InventoryLocationAdjustmentOrderDTO dto);

    List<InventoryLocationAdjustExportDTO> listDto(InventoryLocationAdjustmentOrderDTO dto);

    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
    InventoryLocationAdjustmentOrderDTO searchDTOById(Long id)throws SQLException;
  }

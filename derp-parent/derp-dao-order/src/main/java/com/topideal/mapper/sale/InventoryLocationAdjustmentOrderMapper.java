package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.vo.sale.InventoryLocationAdjustmentOrderModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface InventoryLocationAdjustmentOrderMapper extends BaseMapper<InventoryLocationAdjustmentOrderModel> {

    /**
     * 根据条件查询（分页）
     * @return
     */
    PageDataModel<InventoryLocationAdjustmentOrderDTO> queryDTOListByPage(InventoryLocationAdjustmentOrderDTO dto)throws SQLException;

    int getTotal(InventoryLocationAdjustmentOrderDTO dto) throws SQLException;

    Integer getOrderCount(InventoryLocationAdjustmentOrderDTO dto);

    List<InventoryLocationAdjustExportDTO> listDto(InventoryLocationAdjustmentOrderDTO dto);

    /**
     * 条件过滤查询
     * @return
     */
    InventoryLocationAdjustmentOrderDTO getInventoryLocationDTOById(InventoryLocationAdjustmentOrderDTO dto)throws SQLException;
}

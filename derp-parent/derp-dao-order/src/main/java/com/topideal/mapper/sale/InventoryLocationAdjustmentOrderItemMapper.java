package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderItemDTO;
import com.topideal.entity.vo.sale.InventoryLocationAdjustmentOrderItemModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface InventoryLocationAdjustmentOrderItemMapper extends BaseMapper<InventoryLocationAdjustmentOrderItemModel> {


    /**
     * 查询所有数据
     * @return
     */
    List<InventoryLocationAdjustmentOrderItemDTO> listInventoryLocationAdjustItemDTO(InventoryLocationAdjustmentOrderItemDTO dto)throws SQLException;
    /**
     * 批量根据表头id删除表体
     * @param list
     * @return
     * @throws SQLException
     */
    int delByIdBatch(List list) throws SQLException;
}

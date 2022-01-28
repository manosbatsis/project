package com.topideal.order.service.sale;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderItemDTO;

/**
 * 库位调整单 service
 * */
public interface InventoryLocationAdjustmentOrderService {

    /**
     * 列表（分页）
     * @param dto
     * @return
     * @throws SQLException
     */
    InventoryLocationAdjustmentOrderDTO listInventoryLocationAdjust(InventoryLocationAdjustmentOrderDTO dto,User user)throws SQLException;

    /**
     * 导入库位调整单
     * @param data
     * @param user
     * @return
     * @throws SQLException
     */
    Map saveInventoryLocationAdjust(Map<Integer, List<List<Object>>> data, User user) throws SQLException;
    /**
     * 根据查询条件获取条数
     * @param dto
     * @return
     * @throws SQLException
     */
    Integer getOrderCount(InventoryLocationAdjustmentOrderDTO dto) throws SQLException;

    /**
     * 获取导出列表
     * @param dto
     * @return
     */
    List<InventoryLocationAdjustExportDTO> getExportMainList(InventoryLocationAdjustmentOrderDTO dto,User user);

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    boolean delInventoryLocationAdjust(List ids)throws Exception;
    /**
     * 根据ID获取详情
     * @param id
     * @return
     * @throws SQLException
     */
    InventoryLocationAdjustmentOrderDTO searchDetail(Long id) throws SQLException;
    /**
     * 根据表头ID获取表体
     * @param id
     * @return
     */
    List<InventoryLocationAdjustmentOrderItemDTO> listItemByOrderId(Long id)throws SQLException;
    /**
     * 审核
     * @param list
     * @param user
     * @throws Exception
     */
    void auditInventoryLocationAdjust(List<Long> list ,User user) throws Exception;
}

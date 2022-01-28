package com.topideal.order.service.sale;


import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.InventoryLocationAdjustExportDTO;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.LocationAdjustmentOrderDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface LocationAdjustmentService {

    /**
     * 列表（分页）
     * @param dto
     * @return
     * @throws SQLException
     */
    LocationAdjustmentOrderDTO listInventoryLocationAdjustDTO(LocationAdjustmentOrderDTO dto, User user)throws SQLException;

    /**
     * 导入库位调整单
     * @param data
     * @param user
     * @return
     * @throws SQLException
     */
    Map saveImportLocationAdjustment(List<List<Map<String, String>>> data, User user) throws Exception;

    /**
     * 根据查询条件获取条数
     * @param dto
     * @return
     * @throws SQLException
     */
    Integer getOrderCount(LocationAdjustmentOrderDTO dto, User user) throws SQLException;

    /**
     * 获取导出列表
     * @param dto
     * @return
     */
    List<LocationAdjustmentOrderDTO> getExportMainList(LocationAdjustmentOrderDTO dto, User user) throws SQLException;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    boolean delLocationAdjust(String ids)throws Exception;

    /**
     * 审核
     * @param ids
     * @return
     * @throws Exception
     */
    void auditLocationAdjust(String ids, User user)throws Exception;

    /**
     * 刷新
     * @param ids
     * @return
     * @throws Exception
     */
    void refreshLocationAdjust(String ids, User user)throws Exception;
}

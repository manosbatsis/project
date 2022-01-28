package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.LocationAdjustmentOrderDTO;
import com.topideal.entity.vo.sale.LocationAdjustmentOrderModel;

import java.sql.SQLException;
import java.util.List;


public interface LocationAdjustmentOrderDao extends BaseDao<LocationAdjustmentOrderModel> {

    /**
     * 列表查询
     * @param dto
     * @return
     * @throws SQLException
     */
    LocationAdjustmentOrderDTO queryDTOListByPage(LocationAdjustmentOrderDTO dto)throws SQLException;

    /**
     * 批量插入
     * */
    Integer batchSave(List<LocationAdjustmentOrderModel> list) throws SQLException;


    /**
     * 根据查询条件统计导出数量
     * @param dto
     * @return
     * @throws SQLException
     */
    Integer countByDTO(LocationAdjustmentOrderDTO dto) throws SQLException;

    /**
     * 根据查询条件查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    List<LocationAdjustmentOrderDTO> listByDTO(LocationAdjustmentOrderDTO dto) throws SQLException;

    /**
     * 批量更新库位调整单状态
     * @param ids
     * @param status
     * @return
     * @throws SQLException
     */
    Integer batchUpdateStatus(List<Long> ids, String status) throws SQLException;
}

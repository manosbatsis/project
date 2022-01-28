package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.sale.InventoryLocationAdjustmentOrderDTO;
import com.topideal.entity.dto.sale.LocationAdjustmentOrderDTO;
import com.topideal.entity.vo.sale.LocationAdjustmentOrderModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface LocationAdjustmentOrderMapper extends BaseMapper<LocationAdjustmentOrderModel> {


    PageDataModel<LocationAdjustmentOrderDTO> queryDTOListByPage(LocationAdjustmentOrderDTO dto)throws SQLException;

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
    Integer batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status) throws SQLException;
}

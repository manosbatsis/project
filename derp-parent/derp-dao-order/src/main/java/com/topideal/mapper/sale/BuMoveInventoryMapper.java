package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryExportDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.vo.sale.BuMoveInventoryModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface BuMoveInventoryMapper extends BaseMapper<BuMoveInventoryModel> {

    /**
     * 根据条件查询（分页）
     * @return
     */
    PageDataModel<BuMoveInventoryDTO> queryDTOListByPage(BuMoveInventoryDTO dto)throws SQLException;
    /**
     * 条件过滤查询
     * @return
     */
    BuMoveInventoryDTO getBuMoveInventoryDTOById(BuMoveInventoryDTO dto)throws SQLException;
    /**
     * 根据条件查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<BuMoveInventoryDTO> queryDTOList(BuMoveInventoryDTO dto)throws SQLException;
    /**
     * 导出查询表头表体
     * @param dto
     * @return
     * @throws SQLException
     */
    List<BuMoveInventoryExportDTO> getDetailsByExport(BuMoveInventoryDTO dto) throws SQLException;
}

package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.purchase.PurchaseOrderExportDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryDTO;
import com.topideal.entity.dto.sale.BuMoveInventoryExportDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.vo.sale.BuMoveInventoryModel;

import java.sql.SQLException;
import java.util.List;


public interface BuMoveInventoryDao extends BaseDao<BuMoveInventoryModel> {


    /**
     * 根据条件查询（分页）
     * @return
     */
    BuMoveInventoryDTO queryDTOListByPage(BuMoveInventoryDTO dto)throws SQLException;

    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
    BuMoveInventoryDTO searchDTOById(Long id)throws SQLException;

    /**
     * 根据条件查询
     * @return
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

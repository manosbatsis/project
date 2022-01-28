package com.topideal.dao.sale;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.sale.BuMoveInventoryItemDTO;
import com.topideal.entity.dto.sale.SaleOrderItemDTO;
import com.topideal.entity.vo.sale.BuMoveInventoryItemModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


public interface BuMoveInventoryItemDao extends BaseDao<BuMoveInventoryItemModel> {

    /**
     * 列表查询
     * @param dto
     * @return
     * @throws SQLException
     */
    List<BuMoveInventoryItemDTO> lisBuMoveInventoryItemDTO(BuMoveInventoryItemDTO dto)throws SQLException;

    /**
     * 批量根据表头id删除表体
     * @param list
     * @return
     * @throws SQLException
     */
    int delByIdBatch(List list) throws SQLException;





}

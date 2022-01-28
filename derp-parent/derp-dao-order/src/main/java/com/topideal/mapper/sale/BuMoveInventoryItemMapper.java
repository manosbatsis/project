package com.topideal.mapper.sale;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.sale.BuMoveInventoryItemDTO;
import com.topideal.entity.vo.sale.BuMoveInventoryItemModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface BuMoveInventoryItemMapper extends BaseMapper<BuMoveInventoryItemModel> {


    /**
     * 查询所有数据
     * @return
     */
    List<BuMoveInventoryItemDTO> listBuMoveInventoryItemDTO(BuMoveInventoryItemDTO dto)throws SQLException;
    /**
     * 批量根据表头id删除表体
     * @param list
     * @return
     * @throws SQLException
     */
    int delByIdBatch(@Param("list")List list) throws SQLException;

}

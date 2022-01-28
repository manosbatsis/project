package com.topideal.dao.main;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.CostPriceDifferenceDTO;
import com.topideal.entity.vo.main.CostPriceDifferenceModel;

import java.sql.SQLException;
import java.util.List;


public interface CostPriceDifferenceDao extends BaseDao<CostPriceDifferenceModel> {


    /**
     * 批量插入
     * @param insertList
     * @return
     */
    int batchSave(List<CostPriceDifferenceModel> insertList);

    /**
     * 列表查询
     * @param dto
     * @return
     * @throws SQLException
     */
    CostPriceDifferenceDTO queryDTOListByPage(CostPriceDifferenceDTO dto) throws SQLException;

    /**
     * 查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    List<CostPriceDifferenceDTO> listByDTO(CostPriceDifferenceDTO dto)throws SQLException;



}

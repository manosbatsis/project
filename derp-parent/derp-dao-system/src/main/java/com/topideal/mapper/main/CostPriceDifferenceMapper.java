package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.main.CostPriceDifferenceDTO;
import com.topideal.entity.vo.main.CostPriceDifferenceModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


@MyBatisRepository
public interface CostPriceDifferenceMapper extends BaseMapper<CostPriceDifferenceModel> {

    /**
     * 批量插入
     * @param insertList
     * @return
     */
    int batchSave(List<CostPriceDifferenceModel> insertList);


    PageDataModel<CostPriceDifferenceDTO> queryDTOListByPage(CostPriceDifferenceDTO dto)throws SQLException;

    /**
     * 查询数据
     * @param dto
     * @return
     * @throws SQLException
     */
    List<CostPriceDifferenceDTO> listByDTO(CostPriceDifferenceDTO dto)throws SQLException;
}

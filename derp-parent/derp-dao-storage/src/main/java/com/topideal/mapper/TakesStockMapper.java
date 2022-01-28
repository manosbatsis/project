package com.topideal.mapper;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.TakesStockDTO;
import com.topideal.entity.vo.TakesStockModel;

import java.sql.SQLException;
import java.util.List;

/**
 * 盘点指令表
 * @author lian_
 *
 */
@MyBatisRepository
public interface TakesStockMapper extends BaseMapper<TakesStockModel> {

    PageDataModel<TakesStockDTO> listDTOByPage(TakesStockDTO dto) throws SQLException;

    TakesStockDTO getDetail(Long id) throws SQLException;

}


package com.topideal.dao;

import com.topideal.entity.dto.TakesStockDTO;
import com.topideal.entity.vo.TakesStockModel;

import java.sql.SQLException;

/**
 * 盘点指令表
 * @author lian_
 *
 */
public interface TakesStockDao extends BaseDao<TakesStockModel>{

    /**
     * 分页查询
     */
    TakesStockDTO searchDTOByPage(TakesStockDTO dto) throws SQLException;

    /**
     * 获取详情
     */
    TakesStockDTO getDtoDetail(Long id) throws SQLException;







}


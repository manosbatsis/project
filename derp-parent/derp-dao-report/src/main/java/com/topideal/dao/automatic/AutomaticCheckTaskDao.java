package com.topideal.dao.automatic;

import java.sql.SQLException;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;


public interface AutomaticCheckTaskDao extends BaseDao<AutomaticCheckTaskModel> {
	/**
     * 根据条件查询（分页）
     * @return
     */
	AutomaticCheckTaskDTO queryDTOListByPage(AutomaticCheckTaskDTO dto)throws SQLException;

    /**
     * 通过id查询实体类信息
     * @param id
     * @return
     */
	AutomaticCheckTaskDTO searchDTOById(Long id)throws SQLException;








}

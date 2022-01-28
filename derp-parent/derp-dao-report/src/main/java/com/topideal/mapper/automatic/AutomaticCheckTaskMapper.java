package com.topideal.mapper.automatic;

import java.sql.SQLException;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.AutomaticCheckTaskDTO;
import com.topideal.entity.vo.automatic.AutomaticCheckTaskModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface AutomaticCheckTaskMapper extends BaseMapper<AutomaticCheckTaskModel> {

	/**
     * 根据条件查询（分页）
     * @return
     */
	PageDataModel<AutomaticCheckTaskDTO> queryDTOListByPage(AutomaticCheckTaskDTO dto)throws SQLException;
	 /**
     * 条件过滤查询
     * @return
     */
	AutomaticCheckTaskDTO getAutomaticCheckTaskDTOById(AutomaticCheckTaskDTO dto)throws SQLException;
}

package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.main.DepotScheduleModel;
import com.topideal.mapper.BaseMapper;

/**
 * 仓库附表  mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface DepotScheduleMapper extends BaseMapper<DepotScheduleModel> {


	/**
	 * 根据仓库id查询仓库附表下拉框
	 * */
	List<SelectBean> loadSelectByDepotId(DepotScheduleModel model) throws SQLException;
	
}

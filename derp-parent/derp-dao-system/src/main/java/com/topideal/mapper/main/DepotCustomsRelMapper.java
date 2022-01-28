package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.main.DepotCustomsRelModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface DepotCustomsRelMapper extends BaseMapper<DepotCustomsRelModel> {

	List<SelectBean> getSelectBean(DepotCustomsRelModel model);
	


}

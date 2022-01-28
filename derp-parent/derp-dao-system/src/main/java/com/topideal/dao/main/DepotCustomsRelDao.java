package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.DepotCustomsRelModel;


public interface DepotCustomsRelDao extends BaseDao<DepotCustomsRelModel> {
		
	public List<SelectBean> getSelectBean(DepotCustomsRelModel model) throws SQLException;
	


}

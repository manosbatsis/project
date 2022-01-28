package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.YunjiDepotContrastModel;

/**
 * 云集仓库对照表 dao
 * @author lian_
 */
public interface YunjiDepotContrastDao extends BaseDao<YunjiDepotContrastModel>{
		

	public List<YunjiDepotContrastModel> listDesc(YunjiDepotContrastModel model) throws SQLException;
	public YunjiDepotContrastModel  searchGetByPage(YunjiDepotContrastModel  model) throws SQLException;

}

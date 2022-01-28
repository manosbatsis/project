package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.BatchValidationInfoModel;

/**
 * 批次效期强校验明细 dao
 * @author lian_
 */
public interface BatchValidationInfoDao extends BaseDao<BatchValidationInfoModel>{
		


	/**
	 * 9011 弹框列表查询
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<BatchValidationInfoModel> getListById(BatchValidationInfoModel model) throws SQLException;



}

package com.topideal.mapper.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.main.BatchValidationInfoModel;
import com.topideal.mapper.BaseMapper;

/**
 * 批次效期强校验明细 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface BatchValidationInfoMapper extends BaseMapper<BatchValidationInfoModel> {

	/**
	 * 9011 弹框列表查询
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<BatchValidationInfoModel> getListById(BatchValidationInfoModel model) throws SQLException;

}

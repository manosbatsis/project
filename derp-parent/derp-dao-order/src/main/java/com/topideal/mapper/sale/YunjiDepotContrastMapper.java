package com.topideal.mapper.sale;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.vo.sale.YunjiDepotContrastModel;
import com.topideal.mapper.BaseMapper;

/**
 * 云集仓库对照表 mapper
 * @author lian_
 */
@MyBatisRepository
public interface YunjiDepotContrastMapper extends BaseMapper<YunjiDepotContrastModel> {

	public List<YunjiDepotContrastModel> listDesc(YunjiDepotContrastModel model) throws SQLException;

	public PageDataModel<YunjiDepotContrastModel>  getListByPage(YunjiDepotContrastModel  model) throws SQLException;

	public YunjiDepotContrastModel  getDesc(YunjiDepotContrastModel model)throws SQLException;

}

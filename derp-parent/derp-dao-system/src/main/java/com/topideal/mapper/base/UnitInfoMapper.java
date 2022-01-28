package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;

/**
 * 标准单位mapper
 * @author zhanghx
 */
@MyBatisRepository
public interface UnitInfoMapper extends BaseMapper<UnitInfoModel> {

	/**
	 * 查询品牌表下拉列表
	 * */
	List<SelectBean> getSelectBean() throws SQLException;

    List<UnitInfoModel> listByLike(UnitInfoModel unitInfoModel);
}


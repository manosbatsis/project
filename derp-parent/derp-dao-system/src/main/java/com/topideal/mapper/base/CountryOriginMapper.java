package com.topideal.mapper.base;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;


/**
 * 原产国
 * @author lchenxing
 *
 */
@MyBatisRepository
public interface CountryOriginMapper extends BaseMapper<CountryOriginModel> {


	/**
	 * 查询原产国下拉列表
	 * */
	List<SelectBean> getSelectBean() throws SQLException;

    List<CountryOriginModel> listByLike(CountryOriginModel countryOriginModel);
}


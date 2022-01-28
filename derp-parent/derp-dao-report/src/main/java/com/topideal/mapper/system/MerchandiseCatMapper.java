package com.topideal.mapper.system;

import java.sql.SQLException;
import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.system.MerchandiseCatModel;
import com.topideal.mapper.BaseMapper;

/**
 * 商品分类表
 * @author lchenxing
 *
 */
@MyBatisRepository
public interface MerchandiseCatMapper extends BaseMapper<MerchandiseCatModel> {

	/**
	 * 查询品牌表下拉列表
	 * */
	List<SelectBean> getSelectBean() throws SQLException;

}


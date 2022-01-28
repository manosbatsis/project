package com.topideal.mapper.main;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.mapper.BaseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 商品分类表
 * @author lchenxing
 *
 */
@MyBatisRepository
public interface MerchandiseCatMapper extends BaseMapper<MerchandiseCatModel> {

	/**
	 * 查询分类下拉列表
	 * */
	List<SelectBean> getSelectBean(Map<String, Object> map) throws SQLException;
	
	/**
	 * 根据传参获取商品分类表数据
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SelectBean> getSelectBeanByModel(MerchandiseCatModel model) throws SQLException;

    List<MerchandiseCatModel> listByLike(MerchandiseCatModel catModel);
}


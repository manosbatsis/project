package com.topideal.dao.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.MerchandiseCatModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 商品分类表
 * @author lchenxing
 *
 */
public interface MerchandiseCatDao extends BaseDao<MerchandiseCatModel> {

	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBean(Map<String, Object> map) throws SQLException;
	/**
	 * 根据传参获取商品分类表数据
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public List<SelectBean> getSelectBeanByModel(MerchandiseCatModel model) throws SQLException;

	/**
	 * 模糊查询
	 * @param catModel
	 * @return
	 */
    List<MerchandiseCatModel> listByLike(MerchandiseCatModel catModel);
}


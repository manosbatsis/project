package com.topideal.service.main;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.vo.main.MerchandiseCatModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 商品分类表   service
 * @author zhanghx
 */
public interface MerchandiseCatService {

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
}

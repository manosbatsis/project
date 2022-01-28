package com.topideal.service.main.impl;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.dao.main.MerchandiseCatDao;
import com.topideal.entity.vo.main.MerchandiseCatModel;
import com.topideal.service.main.MerchandiseCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 商品分类表   serviceImpl
 * @author zhanghx
 */
@Service
public class MerchandiseCatServiceImpl implements MerchandiseCatService {

	//商品分类dao
	@Autowired
	private MerchandiseCatDao merchandiseCatDao;
	
	/**
	 * 查询下拉列表
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBean(Map<String, Object> map) throws SQLException {
		return merchandiseCatDao.getSelectBean(map);
	}
	/**
	 * 根据传参获取商品分类表数据
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<SelectBean> getSelectBeanByModel(MerchandiseCatModel model) throws SQLException {
		return merchandiseCatDao.getSelectBeanByModel(model);
	}

}

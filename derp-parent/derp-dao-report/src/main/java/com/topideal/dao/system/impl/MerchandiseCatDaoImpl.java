package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.MerchandiseCatDao;
import com.topideal.entity.vo.system.MerchandiseCatModel;
import com.topideal.mapper.system.MerchandiseCatMapper;

/**
 * 商品分类表 impl
 * 
 * @author lchenxing
 */
@Repository
public class MerchandiseCatDaoImpl implements MerchandiseCatDao {

	@Autowired
	private MerchandiseCatMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<MerchandiseCatModel> list(MerchandiseCatModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(MerchandiseCatModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param model
	 */
	@Override
	public int modify(MerchandiseCatModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public MerchandiseCatModel searchByPage(MerchandiseCatModel model) throws SQLException {
		PageDataModel<MerchandiseCatModel> pageModel = mapper.listByPage(model);
		return (MerchandiseCatModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public MerchandiseCatModel searchById(Long id) throws SQLException {
		MerchandiseCatModel model = new MerchandiseCatModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据实体类查询
	 * 
	 * @param model
	 */
	@Override
	public MerchandiseCatModel searchByModel(MerchandiseCatModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return mapper.getSelectBean();
	}
}

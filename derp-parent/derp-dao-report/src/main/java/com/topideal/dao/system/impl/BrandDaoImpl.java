package com.topideal.dao.system.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.system.BrandDao;
import com.topideal.entity.vo.system.BrandModel;
import com.topideal.mapper.system.BrandMapper;

@Repository
public class BrandDaoImpl implements BrandDao {

	@Autowired
	private BrandMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<BrandModel> list(BrandModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(BrandModel model) throws SQLException {
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
	@SuppressWarnings("rawtypes")
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
	public int modify(BrandModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public BrandModel searchByPage(BrandModel model) throws SQLException {
		PageDataModel<BrandModel> pageModel = mapper.listByPage(model);
		return (BrandModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public BrandModel searchById(Long id) throws SQLException {
		BrandModel model = new BrandModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public BrandModel searchByModel(BrandModel model) throws SQLException {
		return mapper.get(model);
	}

	@Override
	public List<SelectBean> getSelectBean() throws SQLException {
		return mapper.getSelectBean();
	}

	@Override
	public List<BrandModel> getListBrandInfo() throws SQLException {
		return mapper.getListBrandInfo();
	}

}

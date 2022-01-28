package com.topideal.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentTypeItemDao;
import com.topideal.entity.vo.AdjustmentTypeItemModel;
import com.topideal.mapper.AdjustmentTypeItemMapper;

/**
 * 类型调整详情表
 * 
 * @author lchenxing
 */
@Repository
public class AdjustmentTypeItemDaoImpl implements AdjustmentTypeItemDao {

	@Autowired
	private AdjustmentTypeItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<AdjustmentTypeItemModel> list(AdjustmentTypeItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(AdjustmentTypeItemModel model) throws SQLException {
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
	public int modify(AdjustmentTypeItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentTypeItemModel searchByPage(AdjustmentTypeItemModel model) throws SQLException {
		PageDataModel<AdjustmentTypeItemModel> pageModel = mapper.listByPage(model);
		return (AdjustmentTypeItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public AdjustmentTypeItemModel searchById(Long id) throws SQLException {
		AdjustmentTypeItemModel model = new AdjustmentTypeItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param model
	 */
	@Override
	public AdjustmentTypeItemModel searchByModel(AdjustmentTypeItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 * @author zhanghx
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}

	@Override
	public Long countNoExistBu(Long id) throws SQLException {
		return mapper.countNoExistBu(id);
	}

}

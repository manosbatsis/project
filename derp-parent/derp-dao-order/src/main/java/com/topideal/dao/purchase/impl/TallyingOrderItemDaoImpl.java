package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.TallyingOrderItemDao;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;
import com.topideal.mapper.purchase.TallyingOrderItemMapper;

/**
 * 理货单商品详情
 * 
 * @author lchenxing
 */
@Repository
public class TallyingOrderItemDaoImpl implements TallyingOrderItemDao {

	@Autowired
	private TallyingOrderItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TallyingOrderItemModel> list(TallyingOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TallyingOrderItemModel
	 */
	@Override
	public Long save(TallyingOrderItemModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * 
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * 
	 * @param List
	 */
	@Override
	public int modify(TallyingOrderItemModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TallyingOrderItemModel
	 */
	@Override
	public TallyingOrderItemModel searchByPage(TallyingOrderItemModel model) throws SQLException {
		PageDataModel<TallyingOrderItemModel> pageModel = mapper.listByPage(model);
		return (TallyingOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TallyingOrderItemModel searchById(Long id) throws SQLException {
		TallyingOrderItemModel model = new TallyingOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TallyingOrderItemModel searchByModel(TallyingOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 检查商品是否使用
	 * @param id
	 * @return
	 */
	@Override
	public Integer checkGoodsIsUse(Long id) {
		return mapper.checkGoodsIsUse(id);
	}

}

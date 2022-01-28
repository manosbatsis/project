package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.order.SaleOrderItemDao;
import com.topideal.entity.vo.order.SaleOrderItemModel;
import com.topideal.mapper.order.SaleOrderItemMapper;

/**
 * 销售订单商品 impl
 * 
 * @author longcheng.mao
 */
@Repository
public class SaleOrderItemDaoImpl implements SaleOrderItemDao {

	@Autowired
	private SaleOrderItemMapper mapper; // 销售订单商品

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleOrderItemModel> list(SaleOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param model
	 */
	@Override
	public Long save(SaleOrderItemModel model) throws SQLException {
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
	public int modify(SaleOrderItemModel model) throws SQLException {
		
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param model
	 */
	@Override
	public SaleOrderItemModel searchByPage(SaleOrderItemModel model) throws SQLException {
		PageDataModel<SaleOrderItemModel> pageModel = mapper.listByPage(model);
		return (SaleOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param id
	 */
	@Override
	public SaleOrderItemModel searchById(Long id) throws SQLException {
		SaleOrderItemModel model = new SaleOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据实体类查询
	 * 
	 * @param model
	 */
	@Override
	public SaleOrderItemModel searchByModel(SaleOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}
}

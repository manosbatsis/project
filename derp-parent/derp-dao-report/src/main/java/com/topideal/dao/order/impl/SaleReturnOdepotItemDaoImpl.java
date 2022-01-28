package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleReturnOdepotItemDao;
import com.topideal.entity.vo.order.SaleReturnOdepotItemModel;
import com.topideal.mapper.order.SaleReturnOdepotItemMapper;

/**
 * 销售退货出库表体 impl
 * 
 * @author lchenxing
 */
@Repository
public class SaleReturnOdepotItemDaoImpl implements SaleReturnOdepotItemDao {

	@Autowired
	private SaleReturnOdepotItemMapper mapper; // 销售退货出库表体

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleReturnOdepotItemModel> list(SaleReturnOdepotItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param SaleReturnOdepotItemModel
	 */
	@Override
	public Long save(SaleReturnOdepotItemModel model) throws SQLException {
		model.setCreateDate(TimeUtils.getNow());
		model.setModifyDate(TimeUtils.getNow());
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
	@SuppressWarnings("rawtypes")
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
	public int modify(SaleReturnOdepotItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param SaleReturnOdepotItemModel
	 */
	@Override
	public SaleReturnOdepotItemModel searchByPage(SaleReturnOdepotItemModel model) throws SQLException {
		PageDataModel<SaleReturnOdepotItemModel> pageModel = mapper.listByPage(model);
		return (SaleReturnOdepotItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public SaleReturnOdepotItemModel searchById(Long id) throws SQLException {
		SaleReturnOdepotItemModel model = new SaleReturnOdepotItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public SaleReturnOdepotItemModel searchByModel(SaleReturnOdepotItemModel model) throws SQLException {
		return mapper.get(model);
	}

}

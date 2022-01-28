package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.DeclarePurchaseRelDao;
import com.topideal.entity.vo.purchase.DeclareOrderModel;
import com.topideal.entity.vo.purchase.DeclarePurchaseRelModel;
import com.topideal.mapper.purchase.DeclarePurchaseRelMapper;

/**
 * 预申报单关联采购订单表 impl
 * @author lchenxing
 */
@Repository
public class DeclarePurchaseRelDaoImpl implements DeclarePurchaseRelDao {

	@Autowired
	private DeclarePurchaseRelMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<DeclarePurchaseRelModel> list(DeclarePurchaseRelModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param DeclarePurchaseRelModel
	 */
	@Override
	public Long save(DeclarePurchaseRelModel model) throws SQLException {
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
	public int modify(DeclarePurchaseRelModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param DeclarePurchaseRelModel
	 */
	@Override
	public DeclarePurchaseRelModel searchByPage(DeclarePurchaseRelModel model) throws SQLException {
		PageDataModel<DeclarePurchaseRelModel> pageModel = mapper.listByPage(model);
		return (DeclarePurchaseRelModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public DeclarePurchaseRelModel searchById(Long id) throws SQLException {
		DeclarePurchaseRelModel model = new DeclarePurchaseRelModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * @param MerchandiseInfoModel
	 */
	@Override
	public DeclarePurchaseRelModel searchByModel(DeclarePurchaseRelModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 根据采购订单id获取所有预申报的预计到港时间
	 * @param id
	 * @return
	 */
	@Override
	public List<DeclareOrderModel> getTimeById(Long id) {
		return mapper.getTimeById(id);
	}

}

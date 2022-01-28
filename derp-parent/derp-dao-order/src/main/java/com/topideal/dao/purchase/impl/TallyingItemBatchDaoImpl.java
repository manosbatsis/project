package com.topideal.dao.purchase.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.purchase.TallyingItemBatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.TallyingItemBatchDao;
import com.topideal.entity.vo.purchase.TallyingItemBatchModel;
import com.topideal.mapper.purchase.TallyingItemBatchMapper;

/**
 * 理货单商品批次详情 impl
 * @author lchenxing
 */
@Repository
public class TallyingItemBatchDaoImpl implements TallyingItemBatchDao {

	@Autowired
	private TallyingItemBatchMapper mapper;

	/**
	 * 列表查询
	 * @param model
	 */
	@Override
	public List<TallyingItemBatchModel> list(TallyingItemBatchModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * @param ItemBatchModel
	 */
	@Override
	public Long save(TallyingItemBatchModel model) throws SQLException {
		int num = mapper.insert(model);
		if (num == 1) {
			return model.getId();
		}
		return null;
	}

	/**
	 * 删除
	 * @param List
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 * @param List
	 */
	@Override
	public int modify(TallyingItemBatchModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * @param ItemBatchModel
	 */
	@Override
	public TallyingItemBatchModel searchByPage(TallyingItemBatchModel model) throws SQLException {
		PageDataModel<TallyingItemBatchModel> pageModel = mapper.listByPage(model);
		return (TallyingItemBatchModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * @param Long
	 */
	@Override
	public TallyingItemBatchModel searchById(Long id) throws SQLException {
		TallyingItemBatchModel model = new TallyingItemBatchModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TallyingItemBatchModel searchByModel(TallyingItemBatchModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 根据理货单id获取商品信息和批次信息
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<TallyingItemBatchDTO> getGoodsAndBatch(Long id) throws SQLException {
		return mapper.getGoodsAndBatch(id);
	}

}

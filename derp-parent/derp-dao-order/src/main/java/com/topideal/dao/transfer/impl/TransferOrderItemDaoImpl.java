package com.topideal.dao.transfer.impl;

import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.transfer.TransferOrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.TransferOrderItemDao;
import com.topideal.entity.vo.transfer.TransferOrderItemModel;
import com.topideal.mapper.transfer.TransferOrderItemMapper;

/**
 * 调拨订单表体 impl
 * 
 * @author lchenxing
 */
@Repository
public class TransferOrderItemDaoImpl implements TransferOrderItemDao {

	@Autowired
	private TransferOrderItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TransferOrderItemModel> list(TransferOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TransferOrderItemModel
	 */
	@Override
	public Long save(TransferOrderItemModel model) throws SQLException {
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
	public int modify(TransferOrderItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TransferOrderItemModel
	 */
	@Override
	public TransferOrderItemModel searchByPage(TransferOrderItemModel model) throws SQLException {
		PageDataModel<TransferOrderItemModel> pageModel = mapper.listByPage(model);
		return (TransferOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TransferOrderItemModel searchById(Long id) throws SQLException {
		TransferOrderItemModel model = new TransferOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TransferOrderItemModel searchByModel(TransferOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 按商品货号统计调拨单各商品数量
	 */
	public List<TransferOrderItemModel> countByGoodsNo(TransferOrderItemModel model) {
		return mapper.countByGoodsNo(model);
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

	@Override
	public List<TransferOrderItemDTO> searchTransferOrderItem(TransferOrderItemDTO dto) throws SQLException {
		return mapper.searchTransferOrderItem(dto);
	}
}

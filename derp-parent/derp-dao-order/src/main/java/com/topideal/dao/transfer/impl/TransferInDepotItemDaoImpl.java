package com.topideal.dao.transfer.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.transfer.TransferInDepotItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.TransferInDepotItemDao;
import com.topideal.entity.vo.transfer.TransferInDepotItemModel;
import com.topideal.mapper.transfer.TransferInDepotItemMapper;

/**
 * 调拨入库表体 impl
 * 
 * @author lchenxing
 */
@Repository
public class TransferInDepotItemDaoImpl implements TransferInDepotItemDao {

	@Autowired
	private TransferInDepotItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TransferInDepotItemModel> list(TransferInDepotItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TransferInDepotItemModel
	 */
	@Override
	public Long save(TransferInDepotItemModel model) throws SQLException {
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
	public int modify(TransferInDepotItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TransferInDepotItemModel
	 */
	@Override
	public TransferInDepotItemModel searchByPage(TransferInDepotItemModel model) throws SQLException {
		PageDataModel<TransferInDepotItemModel> pageModel = mapper.listByPage(model);
		return (TransferInDepotItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TransferInDepotItemModel searchById(Long id) throws SQLException {
		TransferInDepotItemModel model = new TransferInDepotItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TransferInDepotItemModel searchByModel(TransferInDepotItemModel model) throws SQLException {
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

	@Override
	public List<Map<String, Object>> getInItemListByTransferId(Long transferId){
		return mapper.getInItemListByTransferId(transferId);
	}

	@Override
	public List<TransferInDepotItemDTO> getItemByTransferInId(Long transferId) {
		return mapper.getItemByTransferInId(transferId);
	}
}

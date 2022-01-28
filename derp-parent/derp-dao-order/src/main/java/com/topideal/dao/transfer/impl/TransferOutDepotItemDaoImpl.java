package com.topideal.dao.transfer.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.transfer.TransferOutDepotItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.TransferOutDepotItemDao;
import com.topideal.entity.vo.transfer.TransferOutDepotItemModel;
import com.topideal.mapper.transfer.TransferOutDepotItemMapper;

/**
 * 调拨出库表体 impl
 * 
 * @author lchenxing
 */
@Repository
public class TransferOutDepotItemDaoImpl implements TransferOutDepotItemDao {

	@Autowired
	private TransferOutDepotItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<TransferOutDepotItemModel> list(TransferOutDepotItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param TransferOutDepotItemModel
	 */
	@Override
	public Long save(TransferOutDepotItemModel model) throws SQLException {
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
	public int modify(TransferOutDepotItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param TransferOutDepotItemModel
	 */
	@Override
	public TransferOutDepotItemModel searchByPage(TransferOutDepotItemModel model) throws SQLException {
		PageDataModel<TransferOutDepotItemModel> pageModel = mapper.listByPage(model);
		return (TransferOutDepotItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public TransferOutDepotItemModel searchById(Long id) throws SQLException {
		TransferOutDepotItemModel model = new TransferOutDepotItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public TransferOutDepotItemModel searchByModel(TransferOutDepotItemModel model) throws SQLException {
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

	public List<Map<String, Object>> getItemListByTransferId(Long transferId){
		return mapper.getItemListByTransferId(transferId);
	}

	@Override
	public List<TransferOutDepotItemDTO> getDTOItemListByTransferId(Long transferId) {
		return mapper.getDTOItemListByTransferId(transferId);
	}

	@Override
	public List<TransferOutDepotItemModel> getItemListByTransferIdAndGoodsId(Long transferId, Long outGoodsId) {
		return mapper.getItemListByTransferIdAndGoodsId(transferId, outGoodsId);
	}

	@Override
	public List<Map<String, Object>> getItemSumByIds(List<Long> ids) {
		return mapper.getItemSumByIds(ids);
	}
}

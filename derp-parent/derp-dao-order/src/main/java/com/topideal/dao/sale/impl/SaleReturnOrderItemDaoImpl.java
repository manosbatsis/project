package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleReturnOrderItemDao;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import com.topideal.entity.vo.sale.SaleReturnOrderItemModel;
import com.topideal.mapper.sale.SaleReturnOrderItemMapper;

/**
 * 销售退货订单表体 dao
 * 
 * @author lchenxing
 */
@Repository
public class SaleReturnOrderItemDaoImpl implements SaleReturnOrderItemDao {

	@Autowired
	private SaleReturnOrderItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleReturnOrderItemModel> list(SaleReturnOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param SaleReturnOrderItemModel
	 */
	@Override
	public Long save(SaleReturnOrderItemModel model) throws SQLException {
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
	public int modify(SaleReturnOrderItemModel model) throws SQLException {
    	model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param SaleReturnOrderItemModel
	 */
	@Override
	public SaleReturnOrderItemModel searchByPage(SaleReturnOrderItemModel model) throws SQLException {
		PageDataModel<SaleReturnOrderItemModel> pageModel = mapper.listByPage(model);
		return (SaleReturnOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public SaleReturnOrderItemModel searchById(Long id) throws SQLException {
		SaleReturnOrderItemModel model = new SaleReturnOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public SaleReturnOrderItemModel searchByModel(SaleReturnOrderItemModel model) throws SQLException {
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
	public List<SaleReturnOrderItemModel> searchByOrderId(Long orderId) {
		return mapper.searchByOrderId(orderId);
	}

	@Override
	public List<SaleReturnOrderItemModel> searchByInfo(SaleReturnOrderItemModel saleReturnOrderItemModel) {
		return mapper.searchByInfo(saleReturnOrderItemModel);
	}

	@Override
	public List<SaleReturnOrderItemDTO> listSaleReturnOrderItemDTO(SaleReturnOrderItemDTO dto) throws SQLException {
		return mapper.listSaleReturnOrderItemDTO(dto);
	}

	@Override
	public List<SaleReturnOrderItemModel> getListByBesidesIds(List<Long> itemIds, Long id) {
		return mapper.getListByBesidesIds(itemIds, id);
	}

	@Override
	public Long delBesidesIds(List<Long> itemIds, Long id) {
		return mapper.delBesidesIds(itemIds, id);
	}

	@Override
	public List<SaleReturnOrderItemDTO> getSaleReturnOrderItem(SaleReturnOrderItemDTO itemDto) throws Exception {
		return mapper.getSaleReturnOrderItem(itemDto);
	}

	@Override
	public List<SaleReturnOrderItemModel> statisticsBySaleCode(List<String> saleCodeList) throws SQLException {
		return mapper.statisticsBySaleCode(saleCodeList);
	}

	@Override
	public List<SaleReturnOrderItemModel> statisticsByPoNos(List<String> poNoList) throws SQLException {
		return mapper.statisticsByPoNos(poNoList);
	}
	
	@Override
	public SaleReturnOrderItemDTO getSalePopupListByPage(SaleReturnOrderItemDTO itemDto)throws SQLException{
		PageDataModel<SaleReturnOrderItemModel> pageModel = mapper.getSalePopupListByPage(itemDto);
		return (SaleReturnOrderItemDTO) pageModel.getModel();
	}

	@Override
	public List<SaleReturnOrderItemModel> listByOrderIds(List<Long> orderIds) throws SQLException {
		return mapper.listByOrderIds(orderIds);
	}

}

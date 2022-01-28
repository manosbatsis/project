package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleReturnOdepotItemDao;
import com.topideal.entity.dto.sale.SaleReturnOdepotItemDTO;
import com.topideal.entity.vo.sale.SaleReturnOdepotItemModel;
import com.topideal.entity.vo.transfer.TransferOutDepotItemModel;
import com.topideal.mapper.sale.SaleReturnOdepotItemMapper;

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
	public List<SaleReturnOdepotItemDTO> listSaleReturnOdepotItemDTO(SaleReturnOdepotItemDTO dto) throws SQLException {
		return mapper.listSaleReturnOdepotItemDTO(dto);
	}

	@Override
	public List<SaleReturnOdepotItemModel> getSaleReturnOdepotItemList(Long sreturnOdepotId, Long outGoodsId) {
		return mapper.getSaleReturnOdepotItemList(sreturnOdepotId, outGoodsId);
	}


	

}

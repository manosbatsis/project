package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleReturnIdepotItemDao;
import com.topideal.entity.dto.sale.SaleReturnIdepotItemDTO;
import com.topideal.entity.vo.sale.SaleReturnIdepotItemModel;
import com.topideal.mapper.sale.SaleReturnIdepotItemMapper;

/**
 * 销售退货入库表体 impl
 * 
 * @author lchenxing
 */
@Repository
public class SaleReturnIdepotItemDaoImpl implements SaleReturnIdepotItemDao {

	@Autowired
	private SaleReturnIdepotItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleReturnIdepotItemModel> list(SaleReturnIdepotItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param SaleReturnIdepotItemModel
	 */
	@Override
	public Long save(SaleReturnIdepotItemModel model) throws SQLException {
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
	public int modify(SaleReturnIdepotItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param SaleReturnIdepotItemModel
	 */
	@Override
	public SaleReturnIdepotItemModel searchByPage(SaleReturnIdepotItemModel model) throws SQLException {
		PageDataModel<SaleReturnIdepotItemModel> pageModel = mapper.listByPage(model);
		return (SaleReturnIdepotItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public SaleReturnIdepotItemModel searchById(Long id) throws SQLException {
		SaleReturnIdepotItemModel model = new SaleReturnIdepotItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public SaleReturnIdepotItemModel searchByModel(SaleReturnIdepotItemModel model) throws SQLException {
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
	public List<SaleReturnIdepotItemDTO> listDTO(SaleReturnIdepotItemDTO dto) throws SQLException {
		return mapper.listDTO(dto);
	}

	@Override
	public List<SaleReturnIdepotItemModel> listBySreturnIdepotIds(List<Long> sreturnIdepotIds) throws SQLException {
		return mapper.listBySreturnIdepotIds(sreturnIdepotIds);
	}

	@Override
	public List<SaleReturnIdepotItemModel> getDetailsByReceive(Map<String, Object> map) {
		return mapper.getDetailsByReceive(map);
	}

}

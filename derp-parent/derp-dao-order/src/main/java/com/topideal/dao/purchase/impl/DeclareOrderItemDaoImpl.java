package com.topideal.dao.purchase.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.dao.purchase.DeclareOrderItemDao;
import com.topideal.entity.dto.purchase.DeclareOrderItemDTO;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import com.topideal.mapper.purchase.DeclareOrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 预申报单表体 impl
 *
 * @author lchenxing
 */
@Repository
public class DeclareOrderItemDaoImpl implements DeclareOrderItemDao {

	@Autowired
	private DeclareOrderItemMapper mapper; // 预申报单表体

	/**
	 * 列表查询
	 *
	 * @param model
	 */
	@Override
	public List<DeclareOrderItemModel> list(DeclareOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 *
	 * @param DeclareOrderItemModel
	 */
	@Override
	public Long save(DeclareOrderItemModel model) throws SQLException {
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
	public int modify(DeclareOrderItemModel model) throws SQLException {
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 *
	 * @param DeclareOrderItemModel
	 */
	@Override
	public DeclareOrderItemModel searchByPage(DeclareOrderItemModel model) throws SQLException {
		PageDataModel<DeclareOrderItemModel> pageModel = mapper.listByPage(model);
		return (DeclareOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 *
	 * @param Long
	 */
	@Override
	public DeclareOrderItemModel searchById(Long id) throws SQLException {
		DeclareOrderItemModel model = new DeclareOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 *
	 * @param MerchandiseInfoModel
	 */
	@Override
	public DeclareOrderItemModel searchByModel(DeclareOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 获取已经生成过的预申报单商品
	 *
	 * @param goodsId
	 * @param purchaseOrderId
	 * @return
	 */
	@Override
	public DeclareOrderItemModel getDeclareOrderItems(Map<String,Object> paramMap) {
		return mapper.getDeclareOrderItems(paramMap);
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
	public List<DeclareOrderItemDTO> getDetailsByExport(DeclareOrderItemDTO dto) throws SQLException {
		return mapper.getDetailsByExport(dto);
	}

	@Override
	public DeclareOrderItemDTO getItemPopupListByPage(DeclareOrderItemDTO dto) {
		PageDataModel<DeclareOrderItemDTO> pageModel = mapper.getItemPopupListByPage(dto);
		return (DeclareOrderItemDTO) pageModel.getModel();
	}

	/**按商品、采购单价排序
	 */
	@Override
	public List<DeclareOrderItemModel> listOrderByPrice(DeclareOrderItemModel model) throws SQLException {
		return mapper.listOrderByPrice(model);
	}

	@Override
	public List<DeclareOrderItemDTO> listDTO(DeclareOrderItemDTO dto){
		return mapper.listDTO(dto);
	}
}

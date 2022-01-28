package com.topideal.dao.sale.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.entity.dto.sale.SaleOrderItemDTO;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.mapper.sale.SaleOrderItemMapper;

/**
 * 销售订单商品 impl
 * 
 * @author lchenxing
 */
@Repository
public class SaleOrderItemDaoImpl implements SaleOrderItemDao {

	@Autowired
	private SaleOrderItemMapper mapper; // 销售订单商品

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleOrderItemModel> list(SaleOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param SaleOrderItemModel
	 */
	@Override
	public Long save(SaleOrderItemModel model) throws SQLException {
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
	public int modify(SaleOrderItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param SaleOrderItemModel
	 */
	@Override
	public SaleOrderItemModel searchByPage(SaleOrderItemModel model) throws SQLException {
		PageDataModel<SaleOrderItemModel> pageModel = mapper.listByPage(model);
		return (SaleOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public SaleOrderItemModel searchById(Long id) throws SQLException {
		SaleOrderItemModel model = new SaleOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public SaleOrderItemModel searchByModel(SaleOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
	 * 根据表头Id删除不要的数据（除itemIds之外的数据）
	 * 
	 * @param itemIds
	 * @param id
	 *            表头id
	 */
	@Override
	public void delBesidesIds(List<Long> itemIds, Long id) {
		mapper.delBesidesIds(itemIds, id);
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
	/**查询销售单商品总数量
	 * @param code 销售单号
	 * @param goodsId 商品id
	 * */
	public Integer getGoodsNumBySum(Map<String, Object> map){
		return mapper.getGoodsNumBySum(map);
	}
	
	/**
	 * 根据表头Id查询的数据（除itemIds之外的数据）
	 */
	@Override
	public List<SaleOrderItemModel> getListByBesidesIds(List<Long> itemIds, Long id) {
		return mapper.getListByBesidesIds(itemIds, id);
	}

	@Override
	public List<SaleOrderItemDTO> listSaleOrderItemDTO(SaleOrderItemDTO dto) throws SQLException {
		return mapper.listSaleOrderItemDTO(dto);
	}

	@Override
	public List<SaleOrderItemModel> getDetailsByReceive(Map<String, Object> map) {
		return mapper.getDetailsByReceive(map);
	}

	@Override
	public Map<String, Object> getShelfNumAndAmountByOrderId(Long orderId) {
		return mapper.getShelfNumAndAmountByOrderId(orderId);
	}

	@Override
	public int modifyWithNull(SaleOrderItemModel model) throws SQLException {		
		return mapper.modifyWithNull(model);
	}
}

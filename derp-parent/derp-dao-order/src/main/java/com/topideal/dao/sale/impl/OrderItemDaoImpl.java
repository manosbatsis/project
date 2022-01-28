package com.topideal.dao.sale.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.dto.sale.OrderDTO;
import com.topideal.entity.dto.sale.OrderItemDTO;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.mapper.sale.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 电商订单表体 impl
 * 
 * @author lchenxing
 */
@Repository
public class OrderItemDaoImpl implements OrderItemDao {

	@Autowired
	private OrderItemMapper mapper; // 电商订单表体

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<OrderItemModel> list(OrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param OrderItemModel
	 */
	@Override
	public Long save(OrderItemModel model) throws SQLException {
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
	public int modify(OrderItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param OrderItemModel
	 */
	@Override
	public OrderItemModel searchByPage(OrderItemModel model) throws SQLException {
		PageDataModel<OrderItemModel> pageModel = mapper.listByPage(model);
		return (OrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public OrderItemModel searchById(Long id) throws SQLException {
		OrderItemModel model = new OrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public OrderItemModel searchByModel(OrderItemModel model) throws SQLException {
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
	public List<OrderItemDTO> listItemDTO(OrderItemDTO dto) {
		return mapper.listItemDTO(dto);
	}
	/**
	 * 迁移数据到历史表
	 * */
	@Override
	public int synsMoveToHistory(Map<String,Object> map){
		return mapper.synsMoveToHistory(map);
	}
	/**
	 * 删除已迁移到历史表的数据
	 * */
	@Override
	public int delMoveToHistory(Map<String,Object> map){
		return mapper.delMoveToHistory(map);
	}

	@Override
	public List<OrderItemDTO> listItemByOrderDTO(List<Long> orderIds) {
		return mapper.listItemByOrderDTO(orderIds);
	}

	@Override
	public List<OrderItemDTO> listItemByOrderIdsAndBuId(List<Long> orderIds, Long buId) {
		return mapper.listItemByOrderIdsAndBuId(orderIds, buId);
	}

	@Override
	public List<OrderItemModel> getMaxPriceByOrderId(List<Long> orderIds, Boolean buIdFlag) {
		return mapper.getMaxPriceByOrderId(orderIds, buIdFlag);
	}

	@Override
	public List<Long> listBuByOrder(OrderDTO dto) {
		return mapper.listBuByOrder(dto);
	}

	@Override
	public Integer batchSave(List<OrderItemModel> list) throws SQLException {
		return mapper.batchSave(list);
	}

	@Override
	public void batchUpdate(List<OrderItemModel> list) throws SQLException {
		mapper.batchUpdate(list);
	}
}

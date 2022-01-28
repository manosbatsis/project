package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.vo.order.OrderModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.OrderItemDao;
import com.topideal.entity.dto.OrderItemDTO;
import com.topideal.entity.vo.order.OrderItemModel;
import com.topideal.mapper.order.OrderItemMapper;

/**
 * 电商订单表体 impl
 * @author zhanghx
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

	@Override
	public List<OrderItemDTO> listItemDTO(OrderItemDTO dto) {
		return mapper.listItemDTO(dto);
	}

	@Override
	public List<OrderItemModel> listItem(String externalCode, Long merchantId, String goodsNo) {
		return mapper.listItem(externalCode, merchantId, goodsNo);
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
	public Map<String, Object> sumDiscountAndDecTotalByOrderID(Long id) {
		return mapper.sumDiscountAndDecTotalByOrderID(id);
	}

	@Override
	public OrderItemModel getPopAutoVeriSumGoodsItemByOrder(Map<String, Object> sumItemMap) {
		return mapper.getPopAutoVeriSumGoodsItemByOrder(sumItemMap) ;
	}
}

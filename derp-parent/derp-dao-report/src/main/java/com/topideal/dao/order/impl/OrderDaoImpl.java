package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.OrderDao;
import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.dto.OrderDTO;
import com.topideal.entity.vo.order.OrderModel;
import com.topideal.mapper.order.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 电商订单表 impl
 * @author zhanghx
 */
@Repository
public class OrderDaoImpl implements OrderDao {

	@Autowired
	private OrderMapper mapper; // 电商订单表

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<OrderModel> list(OrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 */
	@Override
	public Long save(OrderModel model) throws SQLException {
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
	 */
	@Override
	public int delete(List ids) throws SQLException {
		return mapper.batchDel(ids);
	}

	/**
	 * 修改
	 */
	@Override
	public int modify(OrderModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 */
	@Override
	public OrderModel searchByPage(OrderModel model) throws SQLException {
		PageDataModel<OrderModel> pageModel = mapper.listByPage(model);
		return (OrderModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 */
	@Override
	public OrderModel searchById(Long id) throws SQLException {
		OrderModel model = new OrderModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 */
	@Override
	public OrderModel searchByModel(OrderModel model) throws SQLException {
		return mapper.get(model);
	}
	/**
	 * 根据创建修改时间查询出库单
	 * */
	public List<Map<String, Object>> getOutDepotOrderByTime(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTime(paramMap);
	}
	/**
	 * 根据创建修改时间查询出库单统计数量
	 * */
	public Integer getOutDepotOrderByTimeCount(Map<String, Object> paramMap){
		return mapper.getOutDepotOrderByTimeCount(paramMap);
	}
	/**
	 * 根据出库单号查询出库单商品
	 * */
	public List<Map<String, Object>> getOutDepotItemByCodes(List<String> codes){
		return mapper.getOutDepotItemByCodes(codes);
	}
	/**
	 * 根据出库单查询商品批次
	 * */
	public List<Map<String, Object>> getItemBatchByCode(Map<String, Object> paramMap){
		return mapper.getItemBatchByCode(paramMap);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间分页查询电商订单
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getListPage(Long merchantId, String startTime,String endTime,Integer startIndex,Integer pageSize) throws SQLException {
		
		return mapper.getListPage(merchantId, startTime,endTime,startIndex,pageSize);
	}
	
	/**
	 * 报表api 根据 商家开始时间结束时间查询电商订单总数
	 * @param merchantId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Integer getListCount(Long merchantId, String startTime, String endTime) {
		// TODO Auto-generated method stub
		return mapper.getListCount(merchantId,startTime,endTime);
	}

	@Override
	public List<BrandSaleDTO> countOrderByMerchantIdAndShopType(Long merchantId, String shopType, String deliverDate) {
		return mapper.countOrderByMerchantIdAndShopType(merchantId, shopType, deliverDate);
	}

	@Override
	public List<BrandSaleDTO> getOrderTop10ByBrand(Long merchantId, String shopType, String deliverDate) {
		return mapper.getOrderTop10ByBrand(merchantId, shopType,deliverDate);
	}

	@Override
	public List<OrderDTO> getRepotApiList(Map<String, Object> queryMap) {
		return mapper.getRepotApiList(queryMap);
	}

	@Override
	public Integer getRepotApiListCount(Map<String, Object> queryMap) {
		return mapper.getRepotApiListCount(queryMap);
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
	public List<OrderModel> getCurrentMonthOdepotList() {
		return mapper.getCurrentMonthOdepotList();
	}
	/**查询时间范围内有修改的电商订单号
	 * */
	@Override
	public List<String> getOrderCodeList(Map<String,Object> map) {
		return mapper.getOrderCodeList(map);
	}
	/**根据单号查询订单
	 * */
	@Override
	public List<Map<String,Object>> getOrderByCodeList(Map<String,Object> map) {
		return mapper.getOrderByCodeList(map);
	}
	/**根据单号查询订单表体
	 * */
	@Override
	public List<Map<String,Object>> getOrderItemByCodeList(Map<String,Object> map) {
		return mapper.getOrderItemByCodeList(map);
	}
}

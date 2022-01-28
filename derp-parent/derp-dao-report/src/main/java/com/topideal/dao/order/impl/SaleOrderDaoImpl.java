package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.SaleOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleOrderDao;
import com.topideal.entity.vo.order.SaleOrderModel;
import com.topideal.mapper.order.SaleOrderMapper;

/**
 * 销售订单 impl
 * @author zhanghx
 */
@Repository
public class SaleOrderDaoImpl implements SaleOrderDao {

	@Autowired
	private SaleOrderMapper mapper;// 销售订单


	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleOrderModel> list(SaleOrderModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 */
	@Override
	public Long save(SaleOrderModel model) throws SQLException {
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
	public int modify(SaleOrderModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 */
	@Override
	public SaleOrderModel searchByPage(SaleOrderModel model) throws SQLException {
		PageDataModel<SaleOrderModel> pageModel = mapper.listByPage(model);
		return (SaleOrderModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 */
	@Override
	public SaleOrderModel searchById(Long id) throws SQLException {
		SaleOrderModel model = new SaleOrderModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 */
	@Override
	public SaleOrderModel searchByModel(SaleOrderModel model) throws SQLException {
		return mapper.get(model);
	}


	/**
	 * 根据条件查询
	 * 
	 * @return
	 */
	@Override
	public List<SaleOrderModel> queryList(SaleOrderModel model) throws SQLException {
		return mapper.queryList(model);
	}
	/**
	 * 根据销售单号获取表体备注
	 * */
	/*public List<Map<String, Object>> getOrderRemark(List<String> list) {
		return mapper.getOrderRemark(list);
	}*/

	@Override
	public List<SaleOrderDTO> getRepotApiList(Map<String, Object> queryMap) {
		return mapper.getRepotApiList(queryMap);
	}

	@Override
	public Integer getRepotApiListCount(Map<String, Object> queryMap) {
		return mapper.getRepotApiListCount(queryMap);
	}
	/**查询时间范围内有修改的销售单号
	 * */
	@Override
	public List<String> getSaleCodeList(Map<String,Object> map){
		return mapper.getSaleCodeList(map);
	}
	/**根据单号查询订单
	 * */
	@Override
	public List<Map<String,Object>> getSaleByCodeList(Map<String,Object> map){
		return mapper.getSaleByCodeList(map);
	}
	/**根据单号查询订单表体
	 * */
	@Override
	public List<Map<String,Object>> getSaleItemByCodeList(Map<String,Object> map){
		return mapper.getSaleItemByCodeList(map);
	}
}

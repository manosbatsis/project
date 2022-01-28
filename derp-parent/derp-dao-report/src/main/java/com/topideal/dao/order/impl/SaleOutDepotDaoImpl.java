package com.topideal.dao.order.impl;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleOutDepotDao;
import com.topideal.entity.dto.BrandSaleDTO;
import com.topideal.entity.vo.order.SaleOutDepotModel;
import com.topideal.mapper.order.SaleOutDepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 销售出库 impl
 * @author zhanghx
 */
@Repository
public class SaleOutDepotDaoImpl implements SaleOutDepotDao {

	@Autowired
	private SaleOutDepotMapper mapper; // 销售出库表体

	/**
	 * 列表查询
	 *
	 * @param model
	 */
	@Override
	public List<SaleOutDepotModel> list(SaleOutDepotModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 *
	 * @param SaleOutDepotModel
	 */
	@Override
	public Long save(SaleOutDepotModel model) throws SQLException {
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
	public int modify(SaleOutDepotModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 *
	 * @param SaleOutDepotModel
	 */
	@Override
	public SaleOutDepotModel searchByPage(SaleOutDepotModel model) throws SQLException {
		PageDataModel<SaleOutDepotModel> pageModel = mapper.listByPage(model);
		return (SaleOutDepotModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 *
	 * @param Long
	 */
	@Override
	public SaleOutDepotModel searchById(Long id) throws SQLException {
		SaleOutDepotModel model = new SaleOutDepotModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 *
	 * @param MerchandiseInfoModel
	 */
	@Override
	public SaleOutDepotModel searchByModel(SaleOutDepotModel model) throws SQLException {
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

	@Override
	public List<BrandSaleDTO> countSaleOutOrderByMerchantIdAndSaleType(Map<String, Object> paramMap) {
		return mapper.countSaleOutOrderByMerchantIdAndSaleType(paramMap);
	}

	@Override
	public List<BrandSaleDTO> getSaleOutOrderTop10ByBrand(Map<String, Object> paramMap) {
		return mapper.getSaleOutOrderTop10ByBrand(paramMap);
	}
}

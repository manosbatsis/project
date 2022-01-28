package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.PurchaseOrderItemDao;
import com.topideal.entity.vo.order.PurchaseOrderItemModel;
import com.topideal.mapper.order.PurchaseOrderItemMapper;

/**
 * 采购订单表体 impl
 * @author zhanghx
 */
@Repository
public class PurchaseOrderItemDaoImpl implements PurchaseOrderItemDao {

	@Autowired
	private PurchaseOrderItemMapper mapper;

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<PurchaseOrderItemModel> list(PurchaseOrderItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param PurchaseOrderItemModel
	 */
	@Override
	public Long save(PurchaseOrderItemModel model) throws SQLException {
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
	public int modify(PurchaseOrderItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param PurchaseOrderItemModel
	 */
	@Override
	public PurchaseOrderItemModel searchByPage(PurchaseOrderItemModel model) throws SQLException {
		PageDataModel<PurchaseOrderItemModel> pageModel = mapper.listByPage(model);
		return (PurchaseOrderItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public PurchaseOrderItemModel searchById(Long id) throws SQLException {
		PurchaseOrderItemModel model = new PurchaseOrderItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public PurchaseOrderItemModel searchByModel(PurchaseOrderItemModel model) throws SQLException {
		return mapper.get(model);
	}


	/**
	 * 统计事业部商家、仓库、本月采购未上架明细
	 */
	@Override
	public List<Map<String, Object>> getBuMonthPurchaseNotshelfInfo(Map<String, Object> map) throws SQLException{
		return mapper.getBuMonthPurchaseNotshelfInfo(map);
	}


	/**
	 * 获取采购未上架金额
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	/*@Override
	public List<PurchaseOrderItemModel> getNoShelvesAmountByZY(Map<String, Object> map) throws SQLException {
		return mapper.getNoShelvesAmountByZY(map);
	}*/



	/**
	 * (事业部财务经销存)获取采购残损明细表 来货短缺数据
	 */
	@Override
	public List<Map<String, Object>> getBuFinancePurchaseDamagedLackList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return mapper.getBuFinancePurchaseDamagedLackList(map);
	}


	/**
	 * 获取(事业部财务经销存)累计采购在途明细表加权
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceAddPurchaseNotshelfDetailsWeighte(Map<String, Object> map) {
		return mapper.getBuFinanceAddPurchaseNotshelfDetailsWeighte(map);
	}

	
	/**事业部业务经销存-本月来货残次
	 * 按理货单位分组统计
	 * */
	@Override
	public List<Map<String, Object>> getBuMonthInBadNum(Map<String, Object> map) {
		return mapper.getBuMonthInBadNum(map);
	}
	
	/**
	 * 来货残次：取本月采购入库中残次品
	 */
	@Override
	public List<Map<String, Object>> getBuPurchaseInBad(Map<String, Object> map) {
		return mapper.getBuPurchaseInBad(map);
	}

	/**
	 * 来货残次：调拨入库单中坏品
	 */
	@Override
	public List<Map<String, Object>> getBuTransferInBad(Map<String, Object> map) {
		return mapper.getBuTransferInBad(map);
	}
	/**
	 * 来货残次：退货入库单中坏品
	 */
	@Override
	public List<Map<String, Object>> getBuSaleReturnInBad(Map<String, Object> map) {
		return mapper.getBuSaleReturnInBad(map);
	}
	/**
	 * 来货残次：上架入库单中坏品
	 */
	@Override
	public List<Map<String, Object>> getBuSaleShelfInBad(Map<String, Object> map) {
		return mapper.getBuSaleShelfInBad(map);
	}

	@Override
	public List<PurchaseOrderItemModel> getInWarehouserItem(Map<String, Object> queryPurchaseMap) {
		return mapper.getInWarehouserItem(queryPurchaseMap);
	}

	/**
	 * 获取来货短缺汇总
	 */
	@Override
	public List<Map<String, Object>> getBuLackNumSum(Map<String, Object> map) {
		return mapper.getBuLackNumSum(map);
	}
	/**
	 * 查询表体list
	 *按效期区间排序
	 */
	@Override
	public List<PurchaseOrderItemModel> listOrderByEffective(PurchaseOrderItemModel itemModel) throws SQLException{
		return mapper.listOrderByEffective(itemModel);
	}
}

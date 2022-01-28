package com.topideal.dao.order.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.order.SaleOutDepotItemDao;
import com.topideal.entity.vo.order.SaleOutDepotItemModel;
import com.topideal.mapper.order.SaleOutDepotItemMapper;

/**
 * 销售出库表体 impl
 * @author zhanghx
 */
@Repository
public class SaleOutDepotItemDaoImpl implements SaleOutDepotItemDao {

	@Autowired
	private SaleOutDepotItemMapper mapper; // 销售出库表体

	/**
	 * 列表查询
	 * 
	 * @param model
	 */
	@Override
	public List<SaleOutDepotItemModel> list(SaleOutDepotItemModel model) throws SQLException {
		return mapper.list(model);
	}

	/**
	 * 新增
	 * 
	 * @param SaleOutDepotItemModel
	 */
	@Override
	public Long save(SaleOutDepotItemModel model) throws SQLException {
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
	@SuppressWarnings("rawtypes")
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
	public int modify(SaleOutDepotItemModel model) throws SQLException {
		model.setModifyDate(TimeUtils.getNow());
		return mapper.update(model);
	}

	/**
	 * 分页查询
	 * 
	 * @param SaleOutDepotItemModel
	 */
	@Override
	public SaleOutDepotItemModel searchByPage(SaleOutDepotItemModel model) throws SQLException {
		PageDataModel<SaleOutDepotItemModel> pageModel = mapper.listByPage(model);
		return (SaleOutDepotItemModel) pageModel.getModel();
	}

	/**
	 * 通过id查询实体类信息
	 * 
	 * @param Long
	 */
	@Override
	public SaleOutDepotItemModel searchById(Long id) throws SQLException {
		SaleOutDepotItemModel model = new SaleOutDepotItemModel();
		model.setId(id);
		return mapper.get(model);
	}

	/**
	 * 根据商家实体类查询商品
	 * 
	 * @param MerchandiseInfoModel
	 */
	@Override
	public SaleOutDepotItemModel searchByModel(SaleOutDepotItemModel model) throws SQLException {
		return mapper.get(model);
	}

	/**
     *业务报表-查询商家、仓库、条码在本月的销售未上架的出库单id、货号、未上架量-购销
     * **/
	/*public List<Map<String, Object>> getSaleNotshelfGXList(Map<String, Object> map){
		return mapper.getSaleNotshelfGXList(map);
	}*/

	/**
     *业务报表-查询商家、仓库、条码在本月的销售未上架的销售单id、货号、未上架量-代销
     * **/
	public List<Map<String, Object>> getBuSaleNotshelfDXList(Map<String, Object> map){
		return mapper.getBuSaleNotshelfDXList(map);
	}
	/**
	 * 业务报表-查询销售未上架明细-购销
	 **/
	public Map<String, Object> getSaleNotshelfGXItem(Map<String, Object> map){
		return mapper.getSaleNotshelfGXItem(map);
	}
	/**
	 * 业务报表-查询销售未上架明细-代销
	 **/
	public Map<String, Object> getSaleNotshelfDXItem(Map<String, Object> map){
		return mapper.getSaleNotshelfDXItem(map);
	}
	/**
	 * 业务报表-查询事业部销售未上架明细-代销
	 **/
	public Map<String, Object> getBuSaleNotshelfDXItem(Map<String, Object> map){
		return mapper.getBuSaleNotshelfDXItem(map);
	}
	/**
	 * 本期销售出库数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getOutDepotNum(Map<String, Object> map) throws SQLException {
		return mapper.getOutDepotNum(map);
	}

	/**
	 * 本期销售未确认数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getUnconfirmedNum(Map<String, Object> map) throws SQLException {
		return mapper.getUnconfirmedNum(map);
	}


	/**
	 * 事业部财务报表-本期销售已上架
	 */
	public List<Map<String,Object>> getBuSaleShelf(Map<String, Object> map){
		return mapper.getBuSaleShelf(map);
	}


	/**
	 * 事业部财务报表-本期销售未上架-代销
	 */
	public List<Map<String, Object>> getBuSaleNoShelfDX(Map<String, Object> map){
		return mapper.getBuSaleNoShelfDX(map);
	}

	/**
	 * 事业部财务报表-本期出库残损
	 */
	public List<Map<String, Object>> getBuOutDamaged(Map<String, Object> map){
		return mapper.getBuOutDamaged(map);
	}

	/**
	 * 1(事业部财务经销存)获取销售订单代销已上架的 的量 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceSaleOrderShelf(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceSaleOrderShelf(map);
	}

	
	/**
	 * 3(事业部财务经销存)销售已上架 获取电商出库的总数数量 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int getBuFinanceDSOrderShelfCount(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceDSOrderShelfCount(map);
	}

	/**
	 * 3(事业部财务经销存)获取电商出库的量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceDSOrderShelf(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceDSOrderShelf(map);
	}
	


	/**
	 * 5 (事业部财务经销存)销售已上架销售出库 电商订单退货
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getBuOrderReturnIdepot(Map<String, Object> map) throws SQLException {
		return mapper.getBuOrderReturnIdepot(map);
	}
	/**
	 * 5.1 (事业部财务经销存)销售已上架销售出库 电商订单退款
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getBuOrderReturnIdepotAmount(Map<String, Object> map) throws SQLException {
		return mapper.getBuOrderReturnIdepotAmount(map);
	}
	

	/**
	 * 6(事业部财务经销存)销售退货单据中类型为购销退货的入库量
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceSaleReturnIdepotGX(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceSaleReturnIdepotGX(map);
	}

	/**
	 * (7) (事业部财务经销存) 账单出库数量（库存调整类型为调减的量）
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceBillOutInDepotSubMapList(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceBillOutInDepotSubMapList(map);
	}
	
	
	/**
	 * (8) (事业部财务经销存) 账单出库数量（库存调整类型为调增的量）
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceBillOutInDepotAddMapList(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceBillOutInDepotAddMapList(map);
	}

	/**
	 * 1(事业部财务经分销) 销售未上架代销 
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceSaleNotshelfDX(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceSaleNotshelfDX(map);
	}

	
	/**
	 * 2(事业部财务经分销) 销售残损代销
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceSaleDamagedDX(Map<String, Object> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapper.getBuFinanceSaleDamagedDX(map);
	}



	/**
	 * 事业部财务报表-本期 累计销售未上架-代销
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> getBuAddSaleNoShelfDX(Map<String, Object> map) throws SQLException {		
		return mapper.getBuAddSaleNoShelfDX(map);
	}


	/**
	 * (事业部财务经分销)(事业部业务经销存)共用累计销售在途明细表代销
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceAddSaleNoshelfDetailsDX(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceAddSaleNoshelfDetailsDX(map);
	}


	/**
	 * 商家、仓库、po号、标准条码、销售类型是代销的出库数量汇总
	 */
	@Override
	public Integer getVipSalesOutNum(Map<String, Object> queryMap) {
		return mapper.getVipSalesOutNum(queryMap);
	}



	/**
	 * 新财务报表 (事业部业务经销存)共用本期减少销售在途量代销(本期减少销售在途表)
	 */
	@Override
	public List<Map<String, Object>> getBuFinanceDecreaseSaleNoshelfDX(Map<String, Object> map) throws SQLException {
		return mapper.getBuFinanceDecreaseSaleNoshelfDX(map);
	}


	/**
	 * 查询唯品PO销售出库明细
	 */
	@Override
	public List<Map<String, Object>> getVipSalesOutDetails(Map<String, Object> queryMap) {
		return mapper.getVipSalesOutDetails(queryMap);
	}



	@Override
	public Double getPrice(String orderCode, String goodsNo) throws SQLException {
		return mapper.getPrice(orderCode, goodsNo);
	}

	@Override
	public Integer getVipAutoVeriSystemNum(Map<String, Object> queryMap) {
		return mapper.getVipAutoVeriSystemNum(queryMap);
	}

	@Override
	public Integer getYJVeriSaleOutDepotAccount(Map<String, Object> queryMap) {
		return mapper.getYJVeriSaleOutDepotAccount(queryMap);
	}

	/**
	 * 事业部业务报表-本期出库残次
	 */
	@Override
	public List<Map<String, Object>> getBuMonthOutBadNum(Map<String, Object> map) {
		return mapper.getBuMonthOutBadNum(map);
	}

	/**
	 * 事业部业务报表-本期出库残次 销售出库 
	 */
	@Override
	public List<Map<String, Object>> getBuSaleOutBad(Map<String, Object> map) {
		return mapper.getBuSaleOutBad(map);
	}
	
	/**
	 *  事业部业务报表-本期出库残次 调拨出库
	 */
	@Override
	public List<Map<String, Object>> getBuTransferOutBad(Map<String, Object> map) {
		return mapper.getBuTransferOutBad(map);
	}
	
	/**
	 * 事业部业务报表-本期出库残次 销售退货出库
	 */
	@Override
	public List<Map<String, Object>> getBuSaleReturnOutBad(Map<String, Object> map) {
		return mapper.getBuSaleReturnOutBad(map);
	}
	
	/**
	 * 事业部业务报表-本期出库残次 采购退货出库
	 */
	@Override
	public List<Map<String, Object>> getBuPurchaseReturnOutBad(Map<String, Object> map) {
		return mapper.getBuPurchaseReturnOutBad(map);
	}

	

	


}

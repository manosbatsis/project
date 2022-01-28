package com.topideal.dao.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.order.SaleOutDepotItemModel;

/**
 * 销售出库表体 dao
 * @author zhanghx
 */
public interface SaleOutDepotItemDao extends BaseDao<SaleOutDepotItemModel> {

	/**
     * 业务报表-查询商家、仓库、条码在本月的销售未上架的出库单ID、货号、未上架量-购销
     * **/
	//public List<Map<String, Object>> getSaleNotshelfGXList(Map<String, Object> map);

	/**
     * 业务报表-查询商家、仓库、条码在本月的销售未上架的销售单ID、货号、未上架量-代销
     * **/
	public List<Map<String, Object>> getBuSaleNotshelfDXList(Map<String, Object> map);
	/**
	 * 业务报表-查询销售未上架明细-购销
	 **/
	public Map<String, Object> getSaleNotshelfGXItem(Map<String, Object> map);
	/**
	 * 业务报表-查询销售未上架明细-代销
	 **/
	public Map<String, Object> getSaleNotshelfDXItem(Map<String, Object> map);
	/**
	 * 业务报表-查询销售未上架明细-代销
	 **/
	public Map<String, Object> getBuSaleNotshelfDXItem(Map<String, Object> map);
	/**
	 * 本期销售出库数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getOutDepotNum(Map<String, Object> map) throws SQLException;
	
	/**
	 * 本期销售未确认数量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getUnconfirmedNum(Map<String, Object> map) throws SQLException;


	/**
	 * 事业部财务报表-本期销售已上架
	 */
	public List<Map<String, Object>> getBuSaleShelf(Map<String, Object> map);


	/**
	 * 事业部财务报表-本期销售未上架-代销
	 */
	public List<Map<String, Object>> getBuSaleNoShelfDX(Map<String, Object> map);

	/**
	 * 事业部业务报表-本期出库残次
	 */
	public List<Map<String, Object>> getBuMonthOutBadNum(Map<String, Object> map);
	/**
	 * 事业部业务报表-本期出库残次 销售出库 
	 */
	public List<Map<String, Object>> getBuSaleOutBad(Map<String, Object> map);
	/**
	 * 事业部业务报表-本期出库残次 调拨出库
	 */
	public List<Map<String, Object>> getBuTransferOutBad(Map<String, Object> map);
	/**
	 * 事业部业务报表-本期出库残次 销售退货出库
	 */
	public List<Map<String, Object>> getBuSaleReturnOutBad(Map<String, Object> map);
	/**
	 * 事业部业务报表-本期出库残次 采购退货出库
	 */
	public List<Map<String, Object>> getBuPurchaseReturnOutBad(Map<String, Object> map);
	
	/**
	 * 新财务报表-本期出库残损
	 */
	public List<Map<String, Object>> getBuOutDamaged(Map<String, Object> map);
	

	/**
	 * 1(事业部财务经销存)获取销售订单代销已上架的 的量 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuFinanceSaleOrderShelf(Map<String, Object> map) throws SQLException;

	
	/**
	 * 3(事业部财务经销存)销售已上架 获取电商出库的总数数量 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public int getBuFinanceDSOrderShelfCount(Map<String, Object> map) throws SQLException;

	/**
	 * 3(事业部财务经销存)获取电商出库的量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuFinanceDSOrderShelf(Map<String, Object> map) throws SQLException;

	/**
	 * 5 (财务经销存)销售已上架销售出库 电商订单退货
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuOrderReturnIdepot(Map<String, Object> map) throws SQLException;
	
	/**
	 * 5.1 (财务经销存)销售已上架销售出库 电商订单退款
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuOrderReturnIdepotAmount(Map<String, Object> map) throws SQLException;
	
	/**
	 * 6(事业部财务经销存)销售退货单据中类型为购销退货的入库量
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuFinanceSaleReturnIdepotGX(Map<String, Object> map) throws SQLException;
	
	/**
	 * 7(事业部财务经销存) 账单出库数量（库存调整类型为调减的量）
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuFinanceBillOutInDepotSubMapList(Map<String, Object> map) throws SQLException;
		
	
	/**
	 * 8(事业部财务经销存)(财务经销存) 账单出库数量（库存调整类型为调增的量）
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuFinanceBillOutInDepotAddMapList(Map<String, Object> map) throws SQLException;
		

	/**
	 * 1(事业部财务经分销) 销售未上架代销 
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuFinanceSaleNotshelfDX(Map<String, Object> map) throws SQLException;
	

	/**
	 * 2(事业部财务经分销) 销售残损代销
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuFinanceSaleDamagedDX(Map<String, Object> map) throws SQLException;
	


	/**
	 * 事业部财务报表-本期 累计销售未上架-代销
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getBuAddSaleNoShelfDX(Map<String, Object> map) throws SQLException;
	

	
	
	/**
	 * (事业部财务经分销)(事业部业务经销存)共用累计销售在途明细表代销
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuFinanceAddSaleNoshelfDetailsDX(Map<String, Object> map) throws SQLException;
	
	/**
	 * 商家、仓库、po号、标准条码、销售类型是代销的出库数量汇总
	 * @param queryMap
	 * @return
	 */
	Integer getVipSalesOutNum(Map<String, Object> queryMap);
	
	/**
	 * 查询唯品PO销售出库明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getVipSalesOutDetails(Map<String, Object> queryMap);



	/**
	 * 新财务报表(事业部业务经销存)共用 本期 本期减少销售在途量代销(本期减少销售在途表)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuFinanceDecreaseSaleNoshelfDX(Map<String, Object> map) throws SQLException;
	

	Double getPrice(String orderCode, String goodsNo) throws SQLException;
	
	/**
	 * 唯品自动校验获取系统出库数
	 * @param queryMap
	 * @return
	 */
	Integer getVipAutoVeriSystemNum(Map<String, Object> queryMap);
	
	/**
	 * 云集自动校验获取系统出库数
	 * @param queryMap
	 * @return
	 */
	Integer getYJVeriSaleOutDepotAccount(Map<String, Object> queryMap);

}


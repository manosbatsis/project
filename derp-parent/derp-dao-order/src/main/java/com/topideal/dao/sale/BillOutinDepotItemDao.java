package com.topideal.dao.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.sale.BillOutinDepotItemModel;
import com.topideal.entity.vo.sale.PreSaleOrderItemModel;


public interface BillOutinDepotItemDao extends BaseDao<BillOutinDepotItemModel> {
		

	/**唯品4.0-获取商家+仓库+po+货号账单出入库量 */
	public Integer getBillOutInDepotNum(Map<String, Object> map);
	
	/**唯品4.0-根据单号汇总表体相同商品的数量*/
	public List<Map<String, Object>> getCodeGoodsNum(String code);

	/**根据账单单号统计数量和总金额*/
	public List<Map<String, Object>> getTotalByBillCode(String billCode, String currency, Long merchantId, Long buId);

	/**统计出库占用数量*/
	public Integer getOutOccupyNum(Map<String, Object> queryMap);
	/**统计入库占用数量*/
	public Integer getInOccupyNum(Map<String, Object> queryMap);

	/**根据单号汇总表体相同商品、批次效期的数量*/
	public List<Map<String, Object>> getCodeGoodsBacthNum(String code);

	/**
	 *  根据账单出库单号、po号、商品货号查询账单出库单明细
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<BillOutinDepotItemModel> getDetailsByReceive (Map<String, Object> map) throws SQLException;
}

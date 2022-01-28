package com.topideal.dao;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventorySummaryDTO;
import com.topideal.entity.vo.InventorySummaryModel;

/**
 * 商品收发汇总 dao
 * 
 * @author lian_
 *
 */
public interface InventorySummaryDao extends BaseDao<InventorySummaryModel> {

	/**
	 * 商品收发汇总 
	 * @param inventorySummaryModel
	 * @return
	 * @throws Exception
	 */
	InventorySummaryDTO listInventorySummaryByPage(InventorySummaryDTO inventorySummaryModel) throws Exception;
	
	
	/**
	 *  导出商品收发汇总统计
	 * @param inventorySummaryModel
	 * @return
	 * @throws Exception
	 */
	 List<InventorySummaryDTO> exportInventorySummary  (InventorySummaryModel inventorySummaryModel) throws Exception;

	/**
	 * 根据商家、商品货号、仓库、当前月获取本月期初库存(月结)
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getOpeningInventoryNum(Long merchantId, Long depotId, String goodsNo,
			String currentMonth,String unit);

	/**
	 * 根据商家、商品货号、仓库、当前月获取本月期初库存(期初)
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getInitInventoryNum(Long merchantId, Long depotId, String goodsNo,String unit);

	/**
	 * 获取本月累计入库数量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getInDepotTotal(Long merchantId, Long depotId, String goodsNo, String currentMonth,String unit);

	/**
	 * 获取本月累计出库数量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getOutDepotTotal(Long merchantId, Long depotId, String goodsNo, String currentMonth,String unit);

	/**
	 * 获取电商在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getEOnwayNum(Long merchantId, Long depotId, String goodsNo, String currentMonth,String unit);

	/**
	 * 获取调拨出库在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getTransferOutNum(Long merchantId, Long depotId, String goodsNo, String currentMonth,String unit);

	/**
	 * 获取销售在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getSaleOnwayNum(Long merchantId, Long depotId, String goodsNo, String currentMonth,String unit);

	/**
	 * 120天的均销量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @return
	 */
	List<Map<String, Object>> getAverageNum(Long merchantId, Long depotId, String goodsNo,String unit);

}

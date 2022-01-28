package com.topideal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.InventorySummaryDTO;
import com.topideal.entity.vo.InventorySummaryModel;

/**
 * 商品收发汇总 mapper
 * @author lian_
 *
 */
@MyBatisRepository
public interface InventorySummaryMapper extends BaseMapper<InventorySummaryModel> {


	/**
	 *   商品收发汇总分页统计
	 * @param inventorySummaryModel
	 * @return
	 * @throws Exception
	 */
	List<InventorySummaryDTO> listInventorySummaryPage(InventorySummaryDTO inventorySummaryModel) throws Exception;
	/**
	 * 分页统计数量
	 * @param inventorySummaryModel
	 * @return
	 * @throws Exception
	 */
	int listInventorySummaryCount (InventorySummaryDTO inventorySummaryModel) throws Exception;
	
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
	List<InventorySummaryModel> getOpeningInventoryNum(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,
			@Param("currentMonth")String currentMonth,@Param("unit")String unit);
	/**
	 * 根据商家、商品货号、仓库、当前月获取本月期初库存(期初)
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getInitInventoryNum(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,@Param("unit")String unit);
	/**
	 * 获取本月累计入库数量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getInDepotTotal(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,
			@Param("currentMonth")String currentMonth,@Param("unit")String unit);
	/**
	 * 获取本月累计出库数量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getOutDepotTotal(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,
			@Param("currentMonth")String currentMonth,@Param("unit")String unit);
	/**
	 * 获取电商在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getEOnwayNum(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,
			@Param("currentMonth")String currentMonth,@Param("unit")String unit);
	/**
	 * 获取调拨出库在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getTransferOutNum(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,
			@Param("currentMonth")String currentMonth,@Param("unit")String unit);
	/**
	 * 获取销售在途量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @param currentMonth
	 * @return
	 */
	List<InventorySummaryModel> getSaleOnwayNum(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,
			@Param("currentMonth")String currentMonth,@Param("unit")String unit);
	/**
	 * 120天的均销量
	 * @param merchantId
	 * @param depotId
	 * @param goodsNo
	 * @return
	 */
	List<Map<String, Object>> getAverageNum(@Param("merchantId")Long merchantId, @Param("depotId")Long depotId, @Param("goodsNo")String goodsNo,@Param("unit")String unit);
}


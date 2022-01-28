package com.topideal.mapper.reporting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.BuInventorySummaryItemDTO;
import com.topideal.entity.vo.reporting.BuInventorySummaryItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface BuInventorySummaryItemMapper extends BaseMapper<BuInventorySummaryItemModel> {


	/**
	 * 清空事业部商家、仓库、本月的报表数据
	 * 
	 * @return
	 */
	 int delBuDepotMonthReport(Map<String, Object> map);
	 /**
	  * 批量新增 事业部 业务经销存表体
	  * @param list
	  * @return
	  */
	 int insertBuBatch(List<BuInventorySummaryItemModel> list);
		// 更新合同号、po号、客户
	 void updateBuItemNoBySale(BuInventorySummaryItemModel model);
	// 调拨出库不插入和更新客户id和客户名称
	 void updateBuItemNoBySaleReturn(BuInventorySummaryItemModel model);
	//  调拨出库不插入和更新客户id和客户名称
	 void updateBuItemNoByTransferOutDepot(BuInventorySummaryItemModel model);
	//
	 void updateBuItemNoByTransferInDepot(BuInventorySummaryItemModel model);
	// 修改 业务经销存 类型是电商订单的 平台单号放入 业务经销存
	 void updateBuItemNoByOrder(BuInventorySummaryItemModel model);
	// 上架入库单
	 void updateBuItemNoBySaleShelfIdepot(BuInventorySummaryItemModel model);
	 //根据id修改po号，合同号，供应商名称
	 void updateBuItemNoById(Map<String, Object> map) throws SQLException;
	 // 修改母品牌
	void updateSuperiorParentBrand(BuInventorySummaryItemModel model) throws SQLException;
	

	//查询入库单ids 
	List<Map<String, Object>> getBuWarehouseIds(BuInventorySummaryItemModel model) throws SQLException;
	
	/**
	 * 获取事业部采购的po号，合同号，供应商名称
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> getBuPurchaseInfos(@Param("id") Long id) throws SQLException;
	// 出库明细
	List<Map<String, Object>> listBuOutListForMap(Map<String, Object> map);
	// 入库明细
	List<Map<String, Object>> listBuInListForMap(Map<String, Object> map);
	// 损益明细
	List<Map<String, Object>> listBuProfitLossListForMap(Map<String, Object> map);
	//统计入库明细数量
	Integer listBuInListCount(Map<String, Object> map);
	//统计出库明细数量
	Integer listBuOutListCount(Map<String, Object> map);
	
	List<BuInventorySummaryItemDTO> getBuList(BuInventorySummaryItemDTO model);
	// 批量新增出入库明细
	int insertBuOutInStorageDetail(Map<String, Object> outInMap);
	
	/**
	 * 获取在库天数导出
	 * @param tempItem
	 * @return
	 */
	List<BuInventorySummaryItemModel> getWarehouseExportlist(BuInventorySummaryItemModel tempItem);

}

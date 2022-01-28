package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.InventoryFallingPriceReservesDTO;
import com.topideal.entity.dto.InventoryWarningCountDto;
import com.topideal.entity.vo.reporting.InventoryFallingPriceReservesModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface InventoryFallingPriceReservesDao extends BaseDao<InventoryFallingPriceReservesModel> {

	/**
	 * 删除数据
	 * @param delMap
	 * @return
	 */
	int delReportByMap(Map<String, Object> delMap);

	/**
	 * 分页获取
	 * @param model
	 * @return
	 */
	List<InventoryFallingPriceReservesDTO> listInventoryFallingPriceReservesPage(
            InventoryFallingPriceReservesDTO model);

	/**
	 * 获取分页总数
	 * @param model
	 * @return
	 */
	int countInventoryFallingPriceReserves(InventoryFallingPriceReservesDTO model);

	/**
	 * 根据IDS查询
	 * @param ids
	 * @return
	 */
	List<InventoryFallingPriceReservesModel> searchByIds(String ids);

	/**
	 * 获取导出列表
	 */
	List<InventoryFallingPriceReservesDTO> getExportList(InventoryFallingPriceReservesDTO dto);


	/**
	 * 根据商家、仓库、日期统计效期预警
	 * @Param
	 * @return
	 */
	List<InventoryWarningCountDto> countInventoryWarning(Map<String, Object> params) throws SQLException;

	/**
	 * 获取美赞库位货号
	 * @param queryMap
	 * @return
	 */
	List<String> getInventoryGoodsNo(Map<String, Object> queryMap);
	/**
	 * 仓库滞销预警(销售洞察)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getDepotUnsellableWarning(Map<String, Object> queryMap);

	/**
	 * 仓库效期预警(销售洞察)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getDepotExpireWarning(Map<String, Object> queryMap);
	/**
	 * 仓库滞销预警获取仓库(销售洞察)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getDepotInUnsellableWarning(Map<String, Object> queryMap);
	/**
	 * 仓库效期预警获取品牌(销售洞察)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getBrandInDepotExpireWarning(Map<String, Object> queryMap);
	/**
	 * 仓库效期预警导出(销售洞察)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getDepotExpireWarningExportList(Map<String, Object> queryMap);
	/**
	 * 仓库滞销预警导出(销售洞察)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getDepotUnsellableWarningExportList(Map<String, Object> queryMap);
	/**
	 * 获取仓库滞销预警各品牌金额明细
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getDepotUnsellableDetail(Map<String, Object> queryMap);
}

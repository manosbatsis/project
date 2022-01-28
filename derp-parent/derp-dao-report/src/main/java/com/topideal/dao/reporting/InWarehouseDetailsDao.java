package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.InWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.InWarehouseDetailsModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface InWarehouseDetailsDao extends BaseDao<InWarehouseDetailsModel> {

	/**
	 * 删除该月报表数据
	 * @param delMap
	 * @return
	 */
	int delDepotMonthReport(Map<String, Object> delMap);

	/**
	 * 获取在库天数范围内数据
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> listInWarehouseDaysRange(Map<String, Object> queryMap);

	/**
	 * 查出规定范围时间内列表数据
	 * @param queryMap
	 * @return
	 * @throws SQLException 
	 */
	InWarehouseDetailsModel listInWarehouseDetails(InWarehouseDetailsModel model) throws SQLException;

	List<InWarehouseDetailsModel> getDetailsByMonthAndCommbarcode(Map<String, Object> queryMap);

	Map<String, Object> getRangeDate(Map<String, Object> queryMap);

	/**
	 * 删除在库数量为空的明细
	 */
	
	boolean deleteNullInWarehouseNumDetails();

	/**
	 * 根据入库天数倒序查询
	 * @param model
	 * @return
	 */
	List<InWarehouseDetailsDTO> listInWarehouseOrderByInWarehousedays(InWarehouseDetailsDTO model);

	/**
	 * 列表查询在库天数
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> listInWarehouseDays(Map<String, Object> queryMap);

	/**
	 * 销售洞察页面：商品在库天数分析数据
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> inWarehouseDaysRangeData(Map<String, Object> queryMap);
	/**
	 * 销售洞察页面：品牌在库天数TOP8
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getBrandInWarehouse(Map<String, Object> queryMap);
	/**
	 * 销售洞察页面：品牌在库天数 其他
	 * @param queryMap
	 * @return
	 */
	Map<String, Object> getBrandInWarehouseOther(Map<String, Object> queryMap);
	/**
	 * 销售洞察页面：商品在库天数导出
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getInWarehouseDaysExportList(Map<String, Object> queryMap);
}

package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.InWarehouseDetailsDTO;
import com.topideal.entity.vo.reporting.InWarehouseDetailsModel;
import com.topideal.mapper.BaseMapper;

import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface InWarehouseDetailsMapper extends BaseMapper<InWarehouseDetailsModel> {

	int delDepotMonthReport(Map<String, Object> delMap);

	List<Map<String, Object>> listInWarehouseDaysRange(Map<String, Object> queryMap);

	List<InWarehouseDetailsModel> getDetailsByMonthAndCommbarcode(Map<String, Object> queryMap);

	Map<String, Object> getRangeDate(Map<String, Object> queryMap);

	boolean deleteNullInWarehouseNumDetails();

	List<InWarehouseDetailsDTO> listInWarehouseOrderByInWarehousedays(InWarehouseDetailsDTO model);

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

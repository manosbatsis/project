package com.topideal.dao.reporting;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.ManageDepartmentSaleAchieveDTO;
import com.topideal.entity.dto.ManageDepartmentSaleDataDTO;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.entity.vo.reporting.SaleDataModel;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface SaleDataDao extends BaseDao<SaleDataModel> {

	/**
	 * 根据集合删除
	 * @param delMap
	 * @return
	 */
	Integer deleteByMap(Map<String, Object> delMap);

	/**
	 * 批量插入
	 * @param subList
	 * @return
	 */
	Integer batchSave(List<SaleDataModel> subList);

	/**
	 * 按类型统计
	 * @param countMap
	 * @return
	 */
	List<Map<String, Object>> countByMap(Map<String, Object> countMap);
	/**
	 * 获取 品牌销售前八的销售额
	 * @param dto
	 * @return
	 */
	List<SaleDataDTO> getBrandSaleTop(SaleDataDTO dto, String startDate, String endDate);

	/**
	 * 获取 客户销售前八的销售额
	 * @param dto
	 * @return
	 */
	List<SaleDataDTO> getCusSaleTop(SaleDataDTO dto, String startDate, String endDate);
	
	/**
	 * 获取 品牌销售其他的销售额
	 * @param dto
	 * @return
	 */
	SaleDataDTO getBrandSaleOther(SaleDataDTO dto, String startDate, String endDate, List<String> names);

	/**
	 * 获取 客户销售其他的销售额
	 * @param dto
	 * @return
	 */
	SaleDataDTO getCusSaleOther(SaleDataDTO dto, String startDate, String endDate, List<String> ids);

	/**
	 * 根据公司、仓库、事业部、标准条码维度统计选择月份的销售数量
	 * @Param
	 * @return
	 */
	List<SaleDataModel> getSaleDataByCommbarcode(List<String> months) throws SQLException;
	
	/**
	 * 累计汇销售汇总分页
	 */
	SaleDataDTO getListAddSale(SaleDataDTO model) throws SQLException;

	/**
	 * 累计销售汇总导出
	 * @param model
	 * @return
	 * @throws SQLException
	 */
    List<SaleDataDTO> getListAddExport(SaleDataDTO model) throws SQLException;
	/**
	 * 获取年度销售销量
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> getSaleAmountYear(SaleDataDTO model, String isParentBrand, List<Long> brandIds);
	/**
	 * 获取销售达成
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> getAchieveExportList(Map<String, Object> map);
	/**
	 * 客户销量导出
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> getCustomerExportList(@Param("dto") SaleDataDTO dto, @Param("startDate") String startDate, @Param("endDate") String endDate);
	/**
	 * 品牌销量导出
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> getBrandExportList(@Param("dto") SaleDataDTO dto, @Param("startDate") String startDate, @Param("endDate") String endDate);
	/**
	 * 年度进销存 根据事业部、月份、标准条码 统计销售量(销售洞察)
	 * @param queryMap
	 * @return
	 */
	List<Map<String, Object>> getSaleCnyAmountList(Map<String, Object> queryMap);

	/**
	 * 部门销售金额统计(经营管理)
	 * @param dto
	 * @return
	 */
    List<ManageDepartmentSaleDataDTO> getDepartmentSalesAmountStatistic(Map<String, Object> queryMap);

    List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesAchieveStatistic(Map<String, Object> paramMap);
	//获取月度目标和年度销售额达成
	List<Map<String, Object>> getMonthYearAchieveAmount(Map<String, Object> map);
}

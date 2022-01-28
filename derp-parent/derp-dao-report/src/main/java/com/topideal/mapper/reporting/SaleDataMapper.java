package com.topideal.mapper.reporting;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.ManageDepartmentSaleAchieveDTO;
import com.topideal.entity.dto.ManageDepartmentSaleDataDTO;
import com.topideal.entity.dto.SaleDataDTO;
import com.topideal.entity.vo.reporting.SaleDataModel;
import com.topideal.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@MyBatisRepository
public interface SaleDataMapper extends BaseMapper<SaleDataModel> {

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
	List<SaleDataDTO> getBrandSaleTop(@Param("dto") SaleDataDTO dto, @Param("startDate") String startDate, @Param("endDate") String endDate);

	/**
	 * 获取 客户销售前八的销售额
	 * @param dto
	 * @return
	 */
	List<SaleDataDTO> getCusSaleTop(@Param("dto") SaleDataDTO dto, @Param("startDate") String startDate, @Param("endDate") String endDate);
	/**
	 * 获取 品牌销售其他的销售额
	 * @param dto
	 * @return
	 */
	SaleDataDTO getBrandSaleOther(@Param("dto") SaleDataDTO dto, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("names") List<String> names);

	/**
	 * 获取 客户销售其他的销售额
	 * @param dto
	 * @return
	 */
	SaleDataDTO getCusSaleOther(@Param("dto") SaleDataDTO dto, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("ids") List<String> ids);

	/**
	 * 根据公司、仓库、事业部、标准条码维度统计选择月份的销售数量
	 * @Param
	 * @return
	 */
	List<SaleDataModel> getSaleDataByCommbarcode(@Param("months") List<String> months) throws SQLException;
	
	/**
	 * 获取累计销售汇总表数据分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	List<SaleDataDTO> getListAddSale(SaleDataDTO model) throws SQLException;

	/**
	 * 获取最大创建时间
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	SaleDataDTO getMaxCreatDate(SaleDataDTO model) throws SQLException;
	/**
	 * 分页导出总数量
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	int getListAddSaleCount(SaleDataDTO model) throws SQLException;
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
	List<Map<String, Object>> getSaleAmountYear(@Param("dto") SaleDataDTO model, @Param("isParentBrand") String isParentBrand, @Param("brandIds") List<Long> brandIds);
	/**
	 * 获取销售达成导出
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
	 * 年度进销存 根据事业部、月份、标准条码 汇总销售量(销售洞察)
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
	//获取月度目标和年度销售额达成
	List<Map<String, Object>> getMonthYearAchieveAmount(Map<String, Object> map);

    List<ManageDepartmentSaleAchieveDTO> getDepartmentSalesAchieveStatistic(Map<String, Object> queryMap);
}

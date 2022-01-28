package com.topideal.report.service.reporting;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.SaleTargetDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SaleTargetService {

	List<BusinessUnitModel> getAllBuUnit() throws SQLException;

	/**
	 * 导入销售目标
	 * @param shopData 
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> importSaleTarget(List<Map<String, String>> saleTypeData, List<Map<String, String>> platformData,
                                         List<Map<String, String>> shopData, User user) throws SQLException;

	/**
	 * 分页信息
	 * @param dto
	 * @return
	 */
	SaleTargetDTO listSaleTarget(SaleTargetDTO dto, User user);

	/**
	 * 导出
	 * @param arr
	 * @return
	 */
	List<SaleTargetDTO> exportSaleTarget(String[] arr, User user);

	/**
	 * 删除
	 * @param arr
	 */
	Integer delSaleTarget(String[] arr);

	/**
	 * 详情获取
	 * @param dto
	 * @return
	 */
	Map<String, Object> getSaleTargetDetails(SaleTargetDTO dto);


}

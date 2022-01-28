package com.topideal.order.service.sale;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigDTO;
import com.topideal.entity.dto.sale.AgreementCurrencyConfigExportDTO;


/**
 * 协议单价配置 service
 * */
public interface AgreementCurrencyConfigService {

	/**
	 * 列表（分页）
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	AgreementCurrencyConfigDTO listAgreementCurrencyByPage(AgreementCurrencyConfigDTO dto,User user)throws SQLException;
	/**
	 * 导出
	 * @param dto
	 * @return
	 */
	List<AgreementCurrencyConfigExportDTO> getDetailsByExport(AgreementCurrencyConfigDTO dto,User user)throws SQLException;
	/**
	 * 删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	boolean delAgreementCurrencyConfig(List ids)throws Exception;
	/**
	 * 根据条件获取信息
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	List<AgreementCurrencyConfigDTO> listAgreementCurrencyConfig(AgreementCurrencyConfigDTO dto,User user) throws SQLException;
	/**
	 * 新增
	 * @param json
	 * @param userId
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	String saveAgreementCurrencyConfig(String json, Long userId, String userName, String topidealCode)throws Exception;
	/**
	 * 导入
	 * @param data
	 * @param userId
	 * @param name
	 * @param merchantId
	 * @param merchantName
	 * @param topidealCode
	 * @param relMerchantIds
	 * @return
	 * @throws SQLException
	 */
	Map saveImportAgreementCurrencyConfig(Map<Integer, List<List<Object>>> data, Long userId, String name, Long merchantId,
                               String merchantName, String topidealCode, String relMerchantIds) throws SQLException;


}

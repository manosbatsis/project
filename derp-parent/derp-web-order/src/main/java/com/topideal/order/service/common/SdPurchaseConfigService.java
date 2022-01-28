package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.common.SdPurchaseConfigDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SdPurchaseConfigService {

	/**
	 * 分页获取
	 * @param dto
	 * @return
	 */
	SdPurchaseConfigDTO getSdPurchaseConfigListByPage(SdPurchaseConfigDTO dto);

	/**
	 * 保存修改
	 * @param model
	 * @param itemList
	 * @throws SQLException
	 */
	void saveOrModify(SdPurchaseConfigModel model, String itemList, User user) throws SQLException;

	/**
	 * 获取详情
	 * @param id
	 * @return
	 */
	SdPurchaseConfigDTO getDTOById(Long id);

	/**
	 * 获取明细
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<SdPurchaseConfigItemModel> getItemById(Long id) throws SQLException;

	/**
	 * 删除
	 * @param list
	 * @throws SQLException
	 */
	void delOrders(List<Long> list) throws SQLException;

	/**
	 * 获取标准品牌
	 * @return
	 */
	List<SelectBean> getBrandParent();

	/**
	 *	导入
	 * @param data
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> importSdPurchaseConfig(List<Map<String, String>> data) throws SQLException;

}

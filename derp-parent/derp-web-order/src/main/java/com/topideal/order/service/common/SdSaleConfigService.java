package com.topideal.order.service.common;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.common.SdSaleConfigDTO;
import com.topideal.entity.vo.common.SdSaleConfigItemModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SdSaleConfigService {

	/**
	 * 获取列表信息 分页
	 * @param dto
	 * @return
	 */
	SdSaleConfigDTO listDTOByPage(SdSaleConfigDTO dto,User user) throws Exception;

	/**
	 * 保存 或 审核
	 * @param dto
	 * @throws SQLException
	 */
	void saveOrAudit(SdSaleConfigDTO dto,User user, String operate) throws Exception;

	/**
	 * 获取详情
	 * @param id
	 * @return
	 */
	SdSaleConfigDTO searchDetail(Long id) throws Exception ;

	/**
	 * 获取明细
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	List<SdSaleConfigItemModel> searchItemDetail(SdSaleConfigItemModel model) throws Exception;

	/**
	 * 删除
	 * @param list
	 * @throws SQLException
	 */
	void delSdSaleConfig(String ids) throws Exception;

	/**
	 * 多比例商品导入
	 * @param data
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> importGoods(List<List<Map<String, String>>> data) throws Exception ;

	/**
	 * 反审
	 * @param id
	 * @throws SQLException
	 */
	void reverseAudit(Long id, User user) throws Exception;

}

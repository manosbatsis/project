package com.topideal.order.service.sale;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.sale.MerchandiseContrastDTO;
import com.topideal.entity.vo.sale.MerchandiseContrastModel;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MerchandiseContrastService {
	/**
	 * 分页
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	public MerchandiseContrastDTO getListByPage(MerchandiseContrastDTO dto) throws SQLException;

	MerchandiseContrastDTO searchDetail(Long id) throws SQLException;

	Map<String, Object> saveContrast(String json,User user) throws Exception;

	boolean delContrast(List<Long> list) throws SQLException;

	boolean modifyContrast(MerchandiseContrastModel model) throws SQLException;
	
	public List<MerchandiseInfoMogo> getMerchandiseList(Long merchantId,String goodsNo) throws SQLException;

	/**
	 * 根据条件查询导出
	 */
	public List<Map<String, Object>> listForExport(MerchandiseContrastDTO dto) throws SQLException;

}

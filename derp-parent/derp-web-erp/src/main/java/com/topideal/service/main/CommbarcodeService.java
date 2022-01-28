package com.topideal.service.main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.common.system.auth.User;
import com.topideal.entity.dto.main.CommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeItemModel;
import com.topideal.entity.vo.main.CommbarcodeModel;

public interface CommbarcodeService {

	/**
	 * 查询分页信息
	 * @param model
	 * @return
	 */
	CommbarcodeDTO listCommbarcodes(CommbarcodeDTO model);

	/**
	 * 查询详情
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	CommbarcodeModel detail(CommbarcodeModel model) throws SQLException;

	/**
	 * 修改
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	int modify(CommbarcodeModel model) throws SQLException;

	/**
	 * 导入
	 * @param data
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("rawtypes")
	Map importCommbarcodes(Map<Integer, List<List<Object>>> data, User user) throws SQLException;

	/**
	 * 获取未维护标准条码
	 * @return
	 * @throws SQLException 
	 */
	List<CommbarcodeModel> getUnMaintainList() throws SQLException;

	/**
	 * 查询实体
	 * @param commbarcodeModel
	 * @return
	 * @throws SQLException 
	 */
	CommbarcodeModel searchByModel(CommbarcodeModel commbarcodeModel) throws SQLException;

	/**
	 * 查询列表
	 * @param commbarcodeModel
	 * @return
	 * @throws SQLException 
	 */
	List<CommbarcodeModel> list(CommbarcodeModel commbarcodeModel) throws SQLException;

	/**
	 * 获取导出列表
	 * @param ids
	 * @return
	 */
	List<CommbarcodeDTO> getExportList(String ids);

	CommbarcodeDTO searchByDTO(CommbarcodeDTO dto);
	
	List<CommbarcodeDTO> getExportCommbarcodes(String ids);

}

package com.topideal.dao.main;

import java.sql.SQLException;
import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.CommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;


public interface CommbarcodeDao extends BaseDao<CommbarcodeModel> {

	/**
	 * 查询分页信息
	 * @param model
	 * @return
	 */
	List<CommbarcodeDTO> listCommbarcodes(CommbarcodeDTO model);

	/**
	 * 查询分页信息总数
	 * @param model
	 * @return
	 */
	Integer listCommbarcodesCount(CommbarcodeDTO model);

	/**
	 * 获取导出列表
	 * @param idList
	 * @return
	 */
	List<CommbarcodeDTO> getExportList(List<String> idList);

	/**
	 * 根据标准条码模糊查询
	 * @param commbarcode
	 * @return
	 */
	List<CommbarcodeModel> getVagueList(String commbarcode);

	CommbarcodeDTO searchByDTO(CommbarcodeDTO dto);

	/**
	 * 批量更新标准条码信息
	 * @param commbarcodeModels
	 * @return
	 */
	void batchUpdateCommbarcode(List<CommbarcodeModel> commbarcodeModels) throws SQLException;
	/**
	 * 导出列表
	 * @param ids
	 * @return
	 */
	List<CommbarcodeDTO> getExportCommbarcodes(List<String> ids);







}

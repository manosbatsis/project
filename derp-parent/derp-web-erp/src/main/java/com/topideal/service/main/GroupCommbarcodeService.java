package com.topideal.service.main;


import java.sql.SQLException;
import java.util.List;

import com.topideal.entity.dto.main.GroupCommbarcodeDTO;
import com.topideal.entity.vo.main.CommbarcodeModel;

public interface GroupCommbarcodeService {

	/**
	 * 获取组码列表分页信息
	 * @param model
	 * @return
	 */
	GroupCommbarcodeDTO listgroupCommbarcodesPage(GroupCommbarcodeDTO model);

	/**
	 * 获取标准条码
	 * @param word
	 * @return
	 */
	List<CommbarcodeModel> getCommbarcode(String word);

	/**
	 * 保存
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	boolean save(GroupCommbarcodeDTO model) throws SQLException;

	/**
	 * 编辑
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	boolean modify(GroupCommbarcodeDTO model) throws SQLException;

	/**
	 * 删除
	 * @param id
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	boolean delete(String id) throws Exception;

	/**
	 * 获取详情
	 * @param model
	 * @return
	 * @throws SQLException 
	 */
	List<GroupCommbarcodeDTO> getDetailList(GroupCommbarcodeDTO model) throws SQLException;

}

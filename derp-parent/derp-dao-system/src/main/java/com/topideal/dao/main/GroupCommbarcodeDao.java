package com.topideal.dao.main;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.main.GroupCommbarcodeDTO;
import com.topideal.entity.vo.main.GroupCommbarcodeModel;


public interface GroupCommbarcodeDao extends BaseDao<GroupCommbarcodeModel> {

	/**
	 * 列表
	 * @param model
	 * @return
	 */
	List<GroupCommbarcodeDTO> listgroupCommbarcodes(GroupCommbarcodeDTO model);

	/**
	 * 分页列表
	 * @param model
	 * @return
	 */
	List<GroupCommbarcodeDTO> listgroupCommbarcodesPage(GroupCommbarcodeDTO model);
		










}

package com.topideal.mapper.main;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.dto.main.GroupCommbarcodeDTO;
import com.topideal.entity.vo.main.GroupCommbarcodeModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface GroupCommbarcodeMapper extends BaseMapper<GroupCommbarcodeModel> {

	/**
	 * 获取列表信息
	 */
	List<GroupCommbarcodeDTO> listgroupCommbarcodes(GroupCommbarcodeDTO model);

	/**
	 * 获取列表分页信息
	 */
	List<GroupCommbarcodeDTO> listgroupCommbarcodesPage(GroupCommbarcodeDTO model);



}

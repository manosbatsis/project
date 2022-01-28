package com.topideal.mapper.main;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.main.GroupCommbarcodeItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface GroupCommbarcodeItemMapper extends BaseMapper<GroupCommbarcodeItemModel> {

	/**
	 * 根据标准条码ID删除
	 * @param idList
	 * @return
	 */
	int deleteByCommbarcodeId(List<Long> idList);



}

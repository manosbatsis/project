package com.topideal.dao.main;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.main.GroupCommbarcodeItemModel;


public interface GroupCommbarcodeItemDao extends BaseDao<GroupCommbarcodeItemModel> {

	/**
	 * 根据标准条码ID删除
	 * @param idList
	 * @return
	 */
	int deleteByCommbarcodeId(List<Long> idList);
		










}

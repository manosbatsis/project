package com.topideal.dao.system;

import java.util.List;

import com.topideal.dao.BaseDao;
import com.topideal.entity.vo.system.GroupCommbarcodeItemModel;


public interface GroupCommbarcodeItemDao extends BaseDao<GroupCommbarcodeItemModel> {

	int deleteByCommbarcodeId(List<Long> idList);
		










}

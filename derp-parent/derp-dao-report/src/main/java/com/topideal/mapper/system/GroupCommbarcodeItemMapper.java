package com.topideal.mapper.system;

import java.util.List;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.entity.vo.system.GroupCommbarcodeItemModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface GroupCommbarcodeItemMapper extends BaseMapper<GroupCommbarcodeItemModel> {

	int deleteByCommbarcodeId(List<Long> idList);



}

package com.topideal.mapper.platformdata;

import com.topideal.common.system.annotation.MyBatisRepository;
import com.topideal.common.system.ibatis.PageDataModel;
import com.topideal.entity.dto.platformdata.PlatfromGoodsCategoryDTO;
import com.topideal.entity.vo.platformdata.PlatfromGoodsCategoryModel;
import com.topideal.mapper.BaseMapper;


@MyBatisRepository
public interface PlatfromGoodsCategoryMapper extends BaseMapper<PlatfromGoodsCategoryModel> {


    PageDataModel<PlatfromGoodsCategoryDTO> getListByPage(PlatfromGoodsCategoryDTO dto);
}

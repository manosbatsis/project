package com.topideal.dao.platformdata;

import com.topideal.dao.BaseDao;
import com.topideal.entity.dto.platformdata.PlatfromGoodsCategoryDTO;
import com.topideal.entity.vo.platformdata.PlatfromGoodsCategoryModel;


public interface PlatfromGoodsCategoryDao extends BaseDao<PlatfromGoodsCategoryModel> {


    PlatfromGoodsCategoryDTO getListByPage(PlatfromGoodsCategoryDTO dto);
}
